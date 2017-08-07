package com.rdm.notificacompromisso.presenter.app;

import android.app.Application;
import android.content.Intent;

import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.presenter.receivers.AppReceiver;
import com.rdm.notificacompromisso.presenter.services.CheckCompromissoService;
import com.rdm.notificacompromisso.presenter.utils.PreferencesUtils;

/**
 * Created by Robson Da Motta on 26/07/2017.
 */

public class CompromissoApp extends Application {

    AppReceiver afterLoadingSystemReceiver = new AppReceiver();

    @Override
    public void onCreate(){
        super.onCreate();
        initializeApp();
    }

    protected void initializeApp(){
        String urlConection = PreferencesUtils.getPreferencesUrlConection(this);
        Intent intent = new Intent(this, CheckCompromissoService.class);
        intent.putExtra(getString(R.string.action_bind_service), urlConection);
        startService(intent);
    }
}
