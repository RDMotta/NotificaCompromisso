package com.rdm.notificacompromisso.app.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.rdm.notificacompromisso.app.R;
import com.rdm.notificacompromisso.app.presenter.services.CheckCompromissoService;

/**
 * Created by Robson Da Motta on 06/08/2017.
 */

public class PreferenciasActivity extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, ServiceConnection {

    private boolean boundedService;
    private Resources resources;
    private CheckCompromissoService mBoundService;
    private SharedPreferences preferences;
    private ListPreference listPreference;
    private EditTextPreference editTextPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
        boundedService = false;
        resources = getActivity().getResources();
        editTextPreference = (EditTextPreference) findPreference(resources.getString(R.string.pref_key_url));
        listPreference = (ListPreference) findPreference(resources.getString(R.string.pref_key_intervalo));

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.registerOnSharedPreferenceChangeListener(this);
        doBindService();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SharedPreferences pref = getActivity().getApplicationContext()
                .getSharedPreferences(resources.getString(R.string.preferences_app), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(editTextPreference.getKey(), editTextPreference.getText());
        editor.putInt(listPreference.getKey(), Integer.valueOf(listPreference.getValue()));
        editor.commit();

        if (key.equals(editTextPreference.getKey())) {
            if (boundedService) {
                String url = preferences.getString(editTextPreference.getKey(), "");
                mBoundService.checkCompromissos(url);
            }
        }
    }

    protected void doBindService() {
        if (!boundedService) {
            Intent intentBindService = new Intent(getActivity(), CheckCompromissoService.class);
            getActivity().bindService(intentBindService, this, Context.BIND_AUTO_CREATE);
            boundedService = true;
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mBoundService =
                ((CheckCompromissoService.CheckCompromissoBinder) iBinder)
                        .getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mBoundService = null;
        boundedService = false;
    }
}
