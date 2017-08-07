package com.rdm.notificacompromisso.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.presenter.services.CheckCompromissoService;
import com.rdm.notificacompromisso.presenter.utils.PreferencesUtils;

/**
 * Created by Robson Da Motta on 27/07/2017.
 */

public class ConditionalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String urlConection = PreferencesUtils.getPreferencesUrlConection(this);
        if (!urlConection.equals("")){
            initializeCheckCompromissoServece(urlConection);
        } else {
            initializeMain();
        }
        finish();
    }

    protected void initializeCheckCompromissoServece(String url){
        Intent intentServer = new Intent(this, CheckCompromissoService.class);
        intentServer.putExtra(getString(R.string.action_bind_service),url);
        startService(intentServer);
    }

    protected void initializeMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
