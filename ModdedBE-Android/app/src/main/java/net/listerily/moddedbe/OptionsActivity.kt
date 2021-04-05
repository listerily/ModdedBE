package net.listerily.moddedbe

import android.content.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.endercore.android.EnderCore
import org.endercore.android.exception.NModException
import org.endercore.android.nmod.NMod
import org.endercore.android.nmod.NModPackage
import org.endercore.android.utils.FileUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.io.StringWriter


class OptionsActivity : AppCompatActivity() {
    companion object {
        private const val CODE_PICK_NMOD = 1

        private const val MSG_READ_FAILED = 1
        private const val MSG_READ_SUCCEED = 2
        private const val MSG_INSTALL_FAILED = 3
        private const val MSG_INSTALL_SUCCEED = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.viewOptions, OptionsFragment())
                .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun onManageNModsClicked() {
        startActivity(Intent(this, ManageNModsActivity::class.java))
    }

    fun onInstallNModsClicked() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"

        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intent, ""), CODE_PICK_NMOD)
    }

    fun onInfoClicked() {
        startActivity(Intent(this, AboutUsActivity::class.java))
    }

    class OptionsFragment : PreferenceFragmentCompat() {
        private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            findPreference<Preference>("manage")!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                (activity as OptionsActivity?)!!.onManageNModsClicked()
                false
            }
            findPreference<Preference>("install")!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                (activity as OptionsActivity?)!!.onInstallNModsClicked()
                false
            }
            findPreference<Preference>("info")!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                (activity as OptionsActivity?)!!.onInfoClicked()
                false
            }

            listener =
                    SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences, key: String ->
                        run {
                            when (key) {
                                "auto_license" -> {
                                    EnderCore.getInstance().optionsManager.autoLicense = sharedPreferences.getBoolean(key, EnderCore.getInstance().optionsManager.autoLicense)
                                }
                                "redirect_directory" -> {
                                    EnderCore.getInstance().optionsManager.redirectGameDir = sharedPreferences.getBoolean(key, EnderCore.getInstance().optionsManager.redirectGameDir)
                                }
                                "unlock_mjscript" -> {
                                    EnderCore.getInstance().optionsManager.unlockMjscript = sharedPreferences.getBoolean(key, EnderCore.getInstance().optionsManager.unlockMjscript)
                                }
                                "use_nmods" -> {
                                    EnderCore.getInstance().optionsManager.useNMods = sharedPreferences.getBoolean(key, EnderCore.getInstance().optionsManager.useNMods)
                                }
                            }
                        }
                        EnderCore.getInstance().optionsManager.saveDataToFile()
                    }
            preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        }

        override fun onResume() {
            super.onResume()
            preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        }

        override fun onPause() {
            super.onPause()
            preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    private var nmodPackage: NModPackage? = null
    private fun onNModPackagePicked(uri: Uri) {
        object : Thread() {
            override fun run() {
                super.run()
                val inputStream = contentResolver.openInputStream(uri)
                val copiedFile = File(EnderCore.getInstance().fileEnvironment.codeCacheDirPathForNMods, "package.nmod")
                FileUtils.copy(inputStream, copiedFile)
                try {
                    nmodPackage = NModPackage(copiedFile)
                } catch (nmodException: NModException) {
                    val msg = Message()
                    msg.what = MSG_READ_FAILED
                    msg.obj = nmodException
                    handler.sendMessage(msg)
                    return
                }
                val msg = Message()
                msg.obj = nmodPackage
                msg.what = MSG_READ_SUCCEED
                handler.sendMessage(msg)
            }
        }.start()
    }

    private fun onDialogInstallClicked() {
        object : Thread() {
            override fun run() {
                super.run()
                val nmod: NMod?
                try {
                    nmod = EnderCore.instance.nModManager.installNMod(nmodPackage)
                } catch (nmodException: NModException) {
                    val msg = Message()
                    msg.what = MSG_INSTALL_FAILED
                    msg.obj = nmodException
                    handler.sendMessage(msg)
                    return
                }
                val msg = Message()
                msg.obj = nmod
                msg.what = MSG_INSTALL_SUCCEED
                handler.sendMessage(msg)
            }
        }.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_PICK_NMOD && resultCode == RESULT_OK) {
            if (data != null) {
                data.data?.let { onNModPackagePicked(it) }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private val handler = MyHandler(this)

    private class MyHandler(private val context: OptionsActivity) : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_READ_FAILED -> {
                    val exception = msg.obj as Exception
                    val writer = StringWriter()
                    val printWriter = PrintWriter(writer)
                    exception.printStackTrace(printWriter)
                    exception.printStackTrace()
                    AlertDialog.Builder(context).setTitle(R.string.app_nmod_package_open_failed_title).setMessage(context.getString(R.string.app_nmod_package_open_failed_summary) + writer.toString()).setPositiveButton(android.R.string.ok) { dialogInterface: DialogInterface, _: Int ->
                        run {
                            dialogInterface.dismiss()
                        }
                    }.setNegativeButton(android.R.string.copy) { _: DialogInterface, _: Int ->
                        run {
                            val cm = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            val mClipData = ClipData.newPlainText("NMod Package Read Failed Message", writer.toString())
                            cm.setPrimaryClip(mClipData)
                            Toast.makeText(context, R.string.app_copied, Toast.LENGTH_LONG).show()
                        }
                    }.setCancelable(false).show()
                }
                MSG_READ_SUCCEED -> {
                    val nmodPackage = msg.obj as NModPackage
                    var icon: Drawable? = null
                    if (nmodPackage.packageManifest.icon != null)
                        try {
                            icon = Drawable.createFromStream(nmodPackage.openInPackage(nmodPackage.packageManifest.icon), nmodPackage.packageManifest.icon)
                        } catch (ignored: FileNotFoundException) {
                        }
                    val dialogBuilder = AlertDialog.Builder(context).setCancelable(false).setTitle(nmodPackage.name).setMessage(context.getString(R.string.app_nmod_install_message, nmodPackage.name))
                    if (icon != null)
                        dialogBuilder.setIcon(icon)
                    dialogBuilder.setPositiveButton(R.string.app_nmod_install) { dialogInterface: DialogInterface, _: Int ->
                        run {
                            context.onDialogInstallClicked()
                            dialogInterface.dismiss()
                        }
                    }
                    dialogBuilder.setNegativeButton(R.string.app_nmod_cancel) { dialogInterface: DialogInterface, _: Int ->
                        run {
                            dialogInterface.dismiss()
                        }
                    }
                    dialogBuilder.show()
                }
                MSG_INSTALL_SUCCEED -> {
                    val nmod = msg.obj as NMod
                    var icon: Drawable? = null
                    if (nmod.packageManifest.icon != null)
                        try {
                            icon = Drawable.createFromStream(nmod.openInFiles(nmod.packageManifest.icon), nmod.packageManifest.icon)
                        } catch (ignored: FileNotFoundException) {
                        }
                    val dialogBuilder = AlertDialog.Builder(context).setCancelable(false).setTitle(R.string.app_nmod_install_succeed_title).setMessage(R.string.app_nmod_install_succeed_message)
                    if (icon != null)
                        dialogBuilder.setIcon(icon)
                    dialogBuilder.setPositiveButton(android.R.string.ok) { dialogInterface: DialogInterface, _: Int ->
                        run {
                            dialogInterface.dismiss()
                        }
                    }
                    dialogBuilder.show()
                }
                MSG_INSTALL_FAILED -> {
                    val exception = msg.obj as Exception
                    val writer = StringWriter()
                    val printWriter = PrintWriter(writer)
                    exception.printStackTrace(printWriter)
                    exception.printStackTrace()
                    AlertDialog.Builder(context).setTitle(R.string.app_nmod_install_failed_title).setMessage(context.getString(R.string.app_nmod_install_failed_message) + writer.toString()).setPositiveButton(android.R.string.ok) { dialogInterface: DialogInterface, _: Int ->
                        run {
                            dialogInterface.dismiss()
                        }
                    }.setNegativeButton(android.R.string.copy) { _: DialogInterface, _: Int ->
                        run {
                            val cm = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            val mClipData = ClipData.newPlainText("NMod Install Failed Message", writer.toString())
                            cm.setPrimaryClip(mClipData)
                            Toast.makeText(context, R.string.app_copied, Toast.LENGTH_LONG).show()
                        }
                    }.setCancelable(false).show()
                }
            }
        }
    }
}