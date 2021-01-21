package net.listerily.moddedbe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.endercore.android.EnderCore
import org.endercore.android.exception.LauncherException
import org.endercore.android.interf.implemented.InitializationListener
import java.io.PrintWriter
import java.io.StringWriter

class InitializingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initializing)
        object : Thread() {
            override fun run() {
                super.run()
                EnderCore.instance.launcher.setGameInitializationListener(object : InitializationListener() {
                    override fun onFinish() {
                        val finishMessage = Message()
                        finishMessage.what = LAUNCH_FINISH
                        handler.sendMessage(finishMessage)
                    }
                })
                try {
                    EnderCore.instance.launcher.initializeGame(this@InitializingActivity)
                    val finishMessage = Message()
                    finishMessage.what = LAUNCH_FINISH
                    handler.sendMessage(finishMessage)
                } catch (e: LauncherException) {
                    val errorMessage = Message()
                    errorMessage.what = LAUNCH_SUSPEND
                    errorMessage.obj = e
                    handler.sendMessage(errorMessage)
                }
            }
        }.start()
    }

    override fun onBackPressed() {
        Toast.makeText(this, R.string.app_loading_summary, Toast.LENGTH_LONG).show()
    }

    fun startGameActivity() {
        try {
            EnderCore.instance.launcher.startGame(this)
            finish()
        } catch (e: LauncherException) {
            startFatalActivity(e)
        }
    }

    fun startFatalActivity(exception: LauncherException) {
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        exception.printStackTrace(printWriter)
        val activityIntent = Intent(this, FatalActivity::class.java)
        activityIntent.putExtra(FatalActivity.TAG_FATAL_MESSAGES, writer.toString())
        startActivity(activityIntent)
        exception.printStackTrace()
        finish()
    }

    private val handler = MHandler(this)

    private class MHandler(private val context: InitializingActivity) : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                LAUNCH_FINISH -> context.startGameActivity()
                LAUNCH_SUSPEND -> context.startFatalActivity(msg.obj as LauncherException)
            }
        }
    }

    companion object {
        private const val LAUNCH_FINISH = 0
        private const val LAUNCH_SUSPEND = 1
    }
}