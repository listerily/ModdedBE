package net.listerily.moddedbe.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ProgressBar

class LoadingView : ProgressBar {
    private val mPaint: Paint

    constructor(context: Context?) : super(context) {
        mPaint = Paint()
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.parseColor("#66BA44")
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mPaint = Paint()
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.parseColor("#66BA44")
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mPaint = Paint()
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.parseColor("#66BA44")
    }

    private var mWidth = 0
    private var mHeight = 0
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    private var mBlockDrawingProgress = 0f
    private var mShowedBlocks = 1
    private var mIsScaling = true
    override fun onDraw(canvas: Canvas) {
        mBlockDrawingProgress += if (mIsScaling) mDefaultSpeed / 2 else mDefaultSpeed
        if (mBlockDrawingProgress >= 1 && !mIsScaling) {
            mBlockDrawingProgress = 0f
            ++mShowedBlocks
            if (mShowedBlocks > 4) {
                mShowedBlocks = 1
                mIsScaling = true
            }
        } else if (mBlockDrawingProgress >= 0.5 && mIsScaling) {
            mIsScaling = false
            mBlockDrawingProgress = 0f
            mShowedBlocks = 2
        }
        when (mShowedBlocks) {
            1 -> {
                val drawWidth = (mWidth.toFloat() * mBlockDrawingProgress).toInt()
                val drawHeight = (mHeight.toFloat() * mBlockDrawingProgress).toInt()
                canvas.drawRect(0f, drawHeight.toFloat(), (mWidth - drawWidth).toFloat(), mHeight.toFloat(), mPaint)
            }
            2 -> {
                canvas.drawRect(0f, mHeight / 2f, mWidth / 2f, mHeight.toFloat(), mPaint)
                val blockDrawHeight = (mHeight.toFloat() / 2 * mBlockDrawingProgress).toInt()
                canvas.drawRect(mWidth / 2f, blockDrawHeight.toFloat(), mWidth.toFloat(), blockDrawHeight + mHeight / 2f, mPaint)
            }
            3 -> {
                canvas.drawRect(0f, mHeight / 2f, mWidth.toFloat(), mHeight.toFloat(), mPaint)
                val blockDrawHeight = (mHeight.toFloat() / 2 * mBlockDrawingProgress).toInt()
                canvas.drawRect(0f, 0f, mWidth / 2f, (blockDrawHeight + 1).toFloat(), mPaint)
            }
            4 -> {
                canvas.drawRect(0f, mHeight / 2f, mWidth.toFloat(), mHeight.toFloat(), mPaint)
                canvas.drawRect(0f, 0f, mWidth / 2f, mHeight / 2f, mPaint)
                val blockDrawHeight = (mHeight.toFloat() / 2 * mBlockDrawingProgress).toInt()
                canvas.drawRect(mWidth / 2f, 0f, mWidth.toFloat(), (blockDrawHeight + 1).toFloat(), mPaint)
            }
        }
        invalidate()
    }

    companion object {
        private const val mDefaultSpeed = 0.075f
    }
}