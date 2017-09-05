package com.rdm.notificacompromisso.app.presenter.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.rdm.notificacompromisso.app.R;
import com.rdm.notificacompromisso.app.presenter.services.CheckCompromissoService;
import com.rdm.notificacompromisso.app.presenter.utils.PreferencesUtils;

/**
 * Created by Robson Da Motta on 27/07/2017.
 */

public class AppReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (isActionToStartServer(intent)) {
            startServIfConected(context);
        }
    }

    protected boolean isActionToStartServer(Intent intent) {
        boolean bootCompleted = intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED);
        boolean changeConection = intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION);

        return bootCompleted || changeConection;
    }

    protected void startServIfConected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean conected = (connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected());

        if (!conected) {
            return;
        }

        String urlConection = PreferencesUtils.getPreferencesUrlConection(context);
        Intent intentServer = new Intent(context, CheckCompromissoService.class);
        intentServer.putExtra(context.getString(R.string.action_bind_service), urlConection);
        context.startService(intentServer);
    }
}
