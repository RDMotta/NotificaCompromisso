package com.rdm.notificacompromisso.presenter.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.rdm.notificacompromisso.model.Compromisso;
import com.rdm.notificacompromisso.model.NotificationCompromisso;
import com.rdm.notificacompromisso.model.ParamServiceDTO;
import com.rdm.notificacompromisso.presenter.http.ConnectHttp;
import com.rdm.notificacompromisso.presenter.receivers.AlarmReceiver;
import com.rdm.notificacompromisso.presenter.utils.PreferencesUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;


/**
 * Created by Robson Da Motta on 26/07/2017.
 */

public class AsyncTaskService extends AsyncTask<ParamServiceDTO, Void, Compromisso> {

    private Context mContext;
    private String mUrl;

    @Override
    protected Compromisso doInBackground(ParamServiceDTO... params) {
        mContext = params[0].getContext();
        mUrl = params[0].getUrl();
        URL url = null;
        Compromisso compromisso = null;
        try {
            url = new URL(mUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            compromisso = obterCompromissoFromUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return compromisso;
    }

    protected void onPostExecute(Compromisso compromisso) {
        if (compromisso != null) {
            notificarCompromisso(compromisso);
        }
        CallOnNextFifteenMinutes();
    }



    protected void notificarCompromisso(Compromisso compromisso) {

        NotificationCompromisso notificationCompromisso = new NotificationCompromisso(mContext);
        NotificationCompat.Builder mBuilder = notificationCompromisso.buildCompromisso(compromisso);

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);


        mNotificationManager.notify((int) compromisso.getIdentificador(), mBuilder.build());
    }


    protected Compromisso obterCompromissoFromUrl(URL url) throws IOException {
        ConnectHttp connectHttp = new ConnectHttp();
        return connectHttp.requestCompromissoGet(url);
    }

    protected void CallOnNextFifteenMinutes() {
        AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

        int intervalo = PreferencesUtils.getPreferencesIntervaloConection(mContext);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, intervalo);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }
}
