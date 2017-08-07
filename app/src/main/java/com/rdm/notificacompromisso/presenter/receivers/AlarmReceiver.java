package com.rdm.notificacompromisso.presenter.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.presenter.services.CheckCompromissoService;
import com.rdm.notificacompromisso.presenter.utils.PreferencesUtils;

/**
 * Created by Robson Da Motta on 01/08/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        initializeCheckCompromissoServece(context);

        if ((context != null) && (intent != null)) {
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            alarmMgr.cancel(alarmIntent);
        }

    }

    protected void initializeCheckCompromissoServece(Context context){
        String urlConection = PreferencesUtils.getPreferencesUrlConection(context);
        Intent intentServer = new Intent(context, CheckCompromissoService.class);
        intentServer.putExtra(context.getString(R.string.action_bind_service), urlConection);
        context.startService(intentServer);
    }
}
