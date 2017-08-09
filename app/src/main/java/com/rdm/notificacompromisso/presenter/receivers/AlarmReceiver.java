package com.rdm.notificacompromisso.presenter.receivers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationCompat;

import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.model.Compromisso;
import com.rdm.notificacompromisso.model.NotificationCompromisso;
import com.rdm.notificacompromisso.presenter.services.CheckCompromissoService;
import com.rdm.notificacompromisso.presenter.utils.PreferencesUtils;

/**
 * Created by Robson Da Motta on 01/08/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        startServIfConected(context);
        notificarSeExistirCompromisso(context, intent);

        if ((context != null) && (intent != null)) {
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            alarmMgr.cancel(alarmIntent);
        }
    }

    protected void notificarSeExistirCompromisso(Context context, Intent intent) {

        if (intent == null) {
            return;
        }

        if (intent.getExtras() == null) {
            return;
        }
        String key_compromisso = context.getString(R.string.action_alarm_confirm);

        if (intent.getExtras().containsKey(key_compromisso)) {
            Compromisso compromisso = (Compromisso) intent.getExtras().getParcelable(key_compromisso);

            NotificationCompromisso notificationCompromisso = new NotificationCompromisso(context);
            NotificationCompat.Builder mBuilder = notificationCompromisso.buildCompromisso(compromisso);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify((int) compromisso.getIdentificador(), mBuilder.build());
        }
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
