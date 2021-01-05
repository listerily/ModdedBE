package net.listerily.moddedbe;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.viewOptions, new OptionsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onManageNModsClicked()
    {

    }

    public void onInstallNModsClicked()
    {

    }

    public static class OptionsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            findPreference("manage").setOnPreferenceClickListener(preference -> {
                ((OptionsActivity)getActivity()).onManageNModsClicked();
                return false;
            });

            findPreference("install").setOnPreferenceClickListener(preference -> {
                ((OptionsActivity)getActivity()).onInstallNModsClicked();
                return false;
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}