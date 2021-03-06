package com.rdm.notificacompromisso.app.presenter.app;

import android.app.Application;
import android.content.Intent;

import com.rdm.notificacompromisso.app.R;
import com.rdm.notificacompromisso.app.presenter.receivers.AppReceiver;
import com.rdm.notificacompromisso.app.presenter.services.CheckCompromissoService;
import com.rdm.notificacompromisso.app.presenter.utils.PreferencesUtils;

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

        String urlConection = PreferencesUtils.getPreferencesUrlConection(getApplicationContext());
        Intent intent = new Intent(this, CheckCompromissoService.class);
        intent.putExtra(getString(R.string.action_bind_service), urlConection);
        startService(intent);
    }
}
