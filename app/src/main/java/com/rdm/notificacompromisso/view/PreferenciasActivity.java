package com.rdm.notificacompromisso.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.presenter.utils.PreferencesUtils;

/**
 * Created by Robson Da Motta on 06/08/2017.
 */

public class PreferenciasActivity extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Preference preference;
    private PreferencesUtils preferencesUtils;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
        preferencesUtils = new PreferencesUtils(getContext());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        preference = findPreference(key);
        preference.setOnPreferenceChangeListener(preferencesUtils);
    }
}
