package net.listerily.moddedbe

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import net.listerily.endercore.android.EnderCore


class OptionsActivity : AppCompatActivity() {
    companion object {
        private const val CODE_PICK_NMOD = 1
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

    fun onNModPicked(){

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CODE_PICK_NMOD && resultCode == RESULT_OK){
            if (data != null) {

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