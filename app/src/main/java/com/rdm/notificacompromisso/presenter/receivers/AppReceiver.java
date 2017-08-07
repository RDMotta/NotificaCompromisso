package com.rdm.notificacompromisso.presenter.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.presenter.services.CheckCompromissoService;
import com.rdm.notificacompromisso.presenter.utils.PreferencesUtils;

/**
 * Created by Robson Da Motta on 27/07/2017.
 */

public class AppReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            initializeCheckCompromissoServece(context);
        }

        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            initializeCheckCompromissoServece(context);
        }
    }

    protected void initializeCheckCompromissoServece(Context context){
        String urlConection = PreferencesUtils.getPreferencesUrlConection(context);
        Intent intentServer = new Intent(context, CheckCompromissoService.class);
        intentServer.putExtra(context.getString(R.string.action_bind_service), urlConection);
        context.startService(intentServer);
    }
}

/****

 boolean usuarioConectado = isActiveNetwork(context);
 if ((usuarioConectado) && (isSetConnection(context))) {
 Intent intentServer = new Intent(context, CheckCompromissoService.class);
 context.startService(intentServer);
 }
 }

 protected boolean isSetConnection(Context context) {
 SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_app), 0);
 String preferences_app_url_conection = sharedPreferences.getString(context.getString(R.string.preferences_app_url_conection), "");
 return !preferences_app_url_conection.equals("");
 }

 protected boolean isActiveNetwork(Context context) {
 ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

 if (connectivityManager.getActiveNetworkInfo() != null
 && connectivityManager.getActiveNetworkInfo().isAvailable()
 && connectivityManager.getActiveNetworkInfo().isConnected()) {
 return true;
 } else {
 return false;
 }
 }
 }

 */

