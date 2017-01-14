package org.onpanic.uninstallapps.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import org.onpanic.uninstallapps.R;


public class UninstallSettings extends PreferenceFragment {

    public UninstallSettings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.uninstall_settings);

        Preference triggerApp = (Preference) findPreference(getString(R.string.pref_trigger_app));
        triggerApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, new TriggerApps())
                        .commit();

                return false;
            }
        });
    }
}
