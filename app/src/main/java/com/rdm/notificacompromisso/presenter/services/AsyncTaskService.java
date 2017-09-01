package com.rdm.notificacompromisso.presenter.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.model.Compromisso;
import com.rdm.notificacompromisso.model.ParamServiceDTO;
import com.rdm.notificacompromisso.presenter.db.CompromissosProvider;
import com.rdm.notificacompromisso.presenter.http.ConnectHttp;
import com.rdm.notificacompromisso.presenter.receivers.AlarmReceiver;
import com.rdm.notificacompromisso.presenter.utils.PreferencesUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Robson Da Motta on 26/07/2017.
 */

public class AsyncTaskService extends AsyncTask<ParamServiceDTO, Void, ArrayList<Compromisso> > {

    private Context mContext;
    private String mUrl;

    @Override
    protected ArrayList<Compromisso>  doInBackground(ParamServiceDTO... params) {
        mContext = params[0].getContext();
        mUrl = params[0].getUrl();
        ArrayList<Compromisso>  compromisso = null;
        try {
            compromisso = obterCompromissosFromUrl(mUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return compromisso;
    }
    @Override
    protected void onPostExecute(ArrayList<Compromisso>  compromissos) {
        notificarCompromisso(compromissos);
        CallOnNextMinutes();
    }

    protected void notificarCompromisso(ArrayList<Compromisso>  compromissos) {
        if (compromissos == null) {
            return;
        }
        if (compromissos.size() == 0){
            return;
        }

        CompromissosProvider compromissosProvider = new CompromissosProvider();
        for (Compromisso compromisso : compromissos ) {
            AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

            Intent intentAlertCompromisso = new Intent(mContext, AlarmReceiver.class);
            intentAlertCompromisso.putExtra(mContext.getString(R.string.action_alarm_confirm), compromisso);
            PendingIntent alarmIntentCompromisso = PendingIntent.getBroadcast(mContext, 0, intentAlertCompromisso, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.set(compromisso.getAno(), compromisso.getMes(), compromisso.getDia());
            calendar.set(Calendar.HOUR_OF_DAY, compromisso.getHora());
            calendar.set(Calendar.MINUTE, compromisso.getMinuto());
            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntentCompromisso);


            compromissosProvider.adicionarCompromisso(mContext, compromisso);
        }
    }


    protected ArrayList<Compromisso> obterCompromissosFromUrl(String url) throws IOException {
        ConnectHttp connectHttp = new ConnectHttp(mContext);
        return connectHttp.requestCompromissoGet(url);
    }

    protected void CallOnNextMinutes() {
        AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        Intent intentAlarm = new Intent(mContext, AlarmReceiver.class);
        PendingIntent alarmIntentAlarm = PendingIntent.getBroadcast(mContext, 0, intentAlarm, 0);

        int intervalo = PreferencesUtils.getPreferencesIntervaloConection(mContext);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, intervalo);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntentAlarm);
    }
}
