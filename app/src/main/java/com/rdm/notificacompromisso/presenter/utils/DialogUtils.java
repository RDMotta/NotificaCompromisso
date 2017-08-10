package com.rdm.notificacompromisso.presenter.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.model.Compromisso;

import java.util.List;

/**
 * Created by Robson Da Motta on 07/08/2017.
 */

public class DialogUtils {


    public static AlertDialog.Builder showCompromisso(Context context, Intent intent) {

        AlertDialog.Builder alerta = null;
        String action_notification_confirm = context.getString(R.string.action_notification_confirm);
        if (intent == null){
            return null;
        }
        if (intent.getExtras() == null){
            return null;
        }
        Compromisso compromisso = intent.getExtras().getParcelable(action_notification_confirm);
        if (compromisso != null) {
            alerta = new AlertDialog.Builder(context);
            alerta.setTitle(compromisso.getAutor());
            alerta.setMessage(compromisso.getDescricao());
            alerta.setNeutralButton(R.string.ok_txt, null);
        }
        return alerta;
    }

    public static void showEventos(Context context, List<Event> eventList) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(context);
        StringBuilder dados = new StringBuilder();
        for (int i = 0; i <  eventList.size(); i++) {
            dados.append(eventList.get(i).getData().toString()).append("\n").append("");
        }
        if (eventList.size() > 0 ){
            alerta.setMessage(dados.toString());
            alerta.setTitle(context.getString(R.string.app_name));
            alerta.setNeutralButton(context.getString(R.string.ok_txt), null);
            alerta.show();
        }
    }
}
