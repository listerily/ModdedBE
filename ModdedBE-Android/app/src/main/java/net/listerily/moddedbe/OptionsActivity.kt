package net.listerily.moddedbe

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

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

    fun onManageNModsClicked() {}
    fun onInstallNModsClicked() {}
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
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}