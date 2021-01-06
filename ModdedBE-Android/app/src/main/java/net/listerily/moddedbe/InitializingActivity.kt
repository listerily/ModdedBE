package net.listerily.moddedbe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import net.listerily.endercore.android.EnderCore
import net.listerily.endercore.android.exception.LauncherException
import net.listerily.endercore.android.nmod.NMod
import net.listerily.endercore.android.operator.Launcher.GameInitializationListener
import java.io.PrintWriter
import java.io.StringWriter

class InitializingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initializing)
        object : Thread() {
            override fun run() {
                super.run()
                EnderCore.instance.launcher.setGameInitializationListener(object : GameInitializationListener {
                    override fun onStart() {}
                    override fun onLoadGameFilesStart() {}
                    override fun onLoadNativeLibrariesStart() {}
                    override fun onLoadNativeLibrary(name: String) {}
                    override fun onLoadNativeLibrariesFinish() {}
                    override fun onLoadJavaLibrariesStart() {}
                    override fun onLoadJavaLibrary(name: String) {}
                    override fun onLoadJavaLibrariesFinish() {}
                    override fun onLoadResourcesStart() {}
                    override fun onLoadAppAsset(name: String) {}
                    override fun onLoadAppResource(name: String) {}
                    override fun onLoadResourcesFinish() {}
                    override fun onLoadGameFilesFinish() {}
                    override fun onLoadNModsStart() {}
                    override fun onLoadNMod(nmod: NMod) {}
                    override fun onLoadNModNativeLibrary(nmod: NMod, name: String) {}
                    override fun onLoadNModJavaLibrary(nmod: NMod, name: String) {}
                    override fun onLoadNModAsset(name: String) {}
                    override fun onLoadNModsFinish() {}
                    override fun onArrange() {}
                    override fun onFinish() {
                        val finishMessage = Message()
                        finishMessage.what = LAUNCH_FINISH
                        handler.sendMessage(finishMessage)
                    }

                    override fun onSuspend() {}
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