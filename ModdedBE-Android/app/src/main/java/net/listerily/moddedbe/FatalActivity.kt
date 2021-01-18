package net.listerily.moddedbe

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import net.listerily.endercore.android.EnderCore
import net.listerily.endercore.android.utils.CPUArch
import net.listerily.endercore.android.utils.FileUtils
import java.io.File

class FatalActivity : AppCompatActivity() {
    private var message: String? = null
    private var appVersionName: String? = null
    private var gameVersionName: String? = null
    private var useNMods = 0
    private var abisFull: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fatal)
        message = intent.getStringExtra(TAG_FATAL_MESSAGES)
        if (message == null) return
        val gamePackageManager = EnderCore.instance.gamePackageManager
        val options = EnderCore.instance.optionsManager
        appVersionName = getString(R.string.app_version_name)
        gameVersionName = gamePackageManager.versionName
        useNMods = if (options.useNMods) 1 else 0
        val abis: String
        val builder = StringBuilder()
        val builderFull = StringBuilder()
        for (abi in CPUArch.getSupportedAbis()) {
            val text: String = if (abi == "armeabi") "arm" else if (abi.startsWith("arm") && abi.endsWith("v7a")) "arm32" else if (abi.startsWith("arm") && abi.endsWith("v8a")) "arm64" else if (abi == "arm64") "arm64" else if (abi == "x86") "x86" else if (abi.startsWith("x86") && abi.endsWith("64")) "x64" else abi
            builder.append(text).append(" ")
            builderFull.append(abi).append(" ")
        }
        abis = builder.toString()
        abisFull = builderFull.toString()
        (findViewById<View>(R.id.textViewFatalMessage) as TextView).text = message
        (findViewById<View>(R.id.textViewAppVersion) as TextView).text = getString(R.string.app_fatal_version_name, getString(R.string.app_version_name))
        (findViewById<View>(R.id.textViewGameVersion) as TextView).text = getString(R.string.app_fatal_game_version, gamePackageManager.versionName)
        (findViewById<View>(R.id.textViewEnderCoreSdk) as TextView).text = getString(R.string.app_fatal_endercore_sdk, EnderCore.SDK_INT)
        (findViewById<View>(R.id.textViewOSSdk) as TextView).text = getString(R.string.app_fatal_os_sdk, Build.VERSION.SDK_INT)
        (findViewById<View>(R.id.textViewBrand) as TextView).text = getString(R.string.app_fatal_brand, Build.BRAND)
        (findViewById<View>(R.id.textViewModel) as TextView).text = getString(R.string.app_fatal_model, Build.MODEL)
        (findViewById<View>(R.id.textViewSafeMode) as TextView).text = getString(R.string.app_fatal_use_nmods, if (options.useNMods) 1 else 0)
        (findViewById<View>(R.id.textViewSupportedABIS) as TextView).text = getString(R.string.app_fatal_abi, abis)
    }

    fun onExitClicked(view: View) {
        finish()
    }

    fun onCopyClicked(view: View) {
        val messageHead = """
               -----------------------
               A fatal error occurred in ModdedBE game initializing.
               Version Name: $appVersionName
               Game Version: $gameVersionName
               EnderCore SDK: ${EnderCore.SDK_INT}
               OS SDK: ${Build.VERSION.SDK_INT}
               Brand: ${Build.BRAND}
               Model: ${Build.MODEL}
               Safe Mode: $useNMods
               ABI:$abisFull
               -----------------------
               
               """.trimIndent()
        val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val mClipData = ClipData.newPlainText("ModdedBE Error Message", messageHead + message)
        cm.setPrimaryClip(mClipData)
        Toast.makeText(this, R.string.app_copied, Toast.LENGTH_LONG).show()
    }

    fun onClearClicked(view: View) {
        FileUtils.removeFiles(File(EnderCore.getInstance().fileEnvironment.codeCacheDirPath))
        FileUtils.removeFiles(File(EnderCore.getInstance().fileEnvironment.enderCoreDirPath))
        Toast.makeText(this, R.string.app_app_data_cleared, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val TAG_FATAL_MESSAGES = "FATAL_MESSAGES"
    }
}