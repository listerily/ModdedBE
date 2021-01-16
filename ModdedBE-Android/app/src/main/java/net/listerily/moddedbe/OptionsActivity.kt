package net.listerily.moddedbe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class OptionsActivity : AppCompatActivity() {
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
        startActivity(Intent(this,ManageNModsActivity::class.java))
    }
    fun onInstallNModsClicked() {}
    fun onInfoClicked() {}
    fun onExtractClicked() {
        val zippedInternalFiles = File(externalCacheDir,"moddedbe_internal_files.zip");
        zippedInternalFiles.parentFile?.mkdirs()
        zippedInternalFiles.createNewFile()
        val zipOutputStream = ZipOutputStream(FileOutputStream(zippedInternalFiles))
        val names = ArrayList<String>()
        val sourceDir = filesDir.parent
        names.add(filesDir.name)
        if(sourceDir != null)
        {
            for(name in names)
            {
                val file = File(sourceDir + File.separator + name)
                if(file.isDirectory)
                {
                    val fileArray = file.listFiles()
                    zipOutputStream.putNextEntry(ZipEntry(name))
                    if(fileArray != null)
                        for(element in fileArray)
                            names.add(name + File.separator + element.name)
                }
                else
                {
                    val entry = ZipEntry(name)
                    zipOutputStream.putNextEntry(entry)
                    var length: Int
                    val buffer = ByteArray(1024)
                    val input = FileInputStream(file)
                    do{
                        length = input.read(buffer)
                        zipOutputStream.write(buffer,0, length)
                    }
                    while(length != 0)
                }
            }
        }

        val intent = Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setAction(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(zippedInternalFiles),"application/zip")
        startActivity(intent)
    }
    class OptionsFragment : PreferenceFragmentCompat() {
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
            findPreference<Preference>("extract")!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                (activity as OptionsActivity?)!!.onExtractClicked()
                false
            }
            findPreference<Preference>("info")!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                (activity as OptionsActivity?)!!.onInfoClicked()
                false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}