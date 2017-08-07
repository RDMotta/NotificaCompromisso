package com.rdm.notificacompromisso.presenter.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import com.rdm.notificacompromisso.R;

/**
 * Created by Robson Da Motta on 01/08/2017.
 */

public class PreferencesUtils implements Preference.OnPreferenceChangeListener{

    protected Context mContext;

    public PreferencesUtils(Context context){
        mContext = context;
    }

    public static String getPreferencesUrlConection(Context context){
        String preferences_app = context.getString(R.string.preferences_app);
        String preferences_url = context.getString(R.string.preferences_app_url_conection);
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferences_app, Context.MODE_PRIVATE);

        return sharedPreferences.getString(preferences_url, "");
    }
    public static int getPreferencesIntervaloConection(Context context){
        String preferences_app = context.getString(R.string.preferences_app);
        String preferences_app_intervalo_conection = context.getString(R.string.preferences_app_intervalo_conection);
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferences_app, Context.MODE_PRIVATE);

        return sharedPreferences.getInt(preferences_app_intervalo_conection, 5);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String preferences_app = mContext.getString(R.string.preferences_app);
        String preferences_url = mContext.getString(R.string.preferences_app_url_conection);
        String preferences_app_intervalo_conection = mContext.getString(R.string.preferences_app_intervalo_conection);
        String key_intervalo = mContext.getString(R.string.pref_key_intervalo);
        String key_url = mContext.getString(R.string.pref_key_url);

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(preferences_app, Context.MODE_PRIVATE);
        if (preference.getKey().equals(key_url)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(preferences_url, (String) newValue);
            editor.commit();
            return true;
        } else if (preference.getKey().equals(key_intervalo)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(preferences_app_intervalo_conection, (int) newValue);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }
}
