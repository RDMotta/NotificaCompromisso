package com.rdm.notificacompromisso.app.presenter.utils;

import android.content.Context;
import android.content.Intent;

import com.rdm.notificacompromisso.app.R;
import com.rdm.notificacompromisso.app.model.Compromisso;
import com.rdm.notificacompromisso.app.view.EventosActivity;
import com.rdm.notificacompromisso.app.view.MainActivity;

/**
 * Created by Robson Da Motta on 07/08/2017.
 */

public class DialogUtils {


    public static void showEventoIfExist(MainActivity mainActivity){
        if ((mainActivity.getIntent() != null) && (mainActivity.getIntent().getExtras() != null)){
            Context context = mainActivity.getApplicationContext();
            String action_notification_confirm = context.getString(R.string.action_notification_confirm);
            Intent intentEvento = new Intent(context, EventosActivity.class);
            Compromisso compromisso = intentEvento.getExtras().getParcelable(action_notification_confirm);
            intentEvento.putExtra(action_notification_confirm, compromisso);
            mainActivity.startActivity(intentEvento);
        }
    }
}
