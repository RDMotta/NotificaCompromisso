package com.rdm.notificacompromisso.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.presenter.services.CheckCompromissoService;
import com.rdm.notificacompromisso.presenter.utils.PermissionUtils;
import com.rdm.notificacompromisso.presenter.utils.PreferencesUtils;

/**
 * Created by Robson Da Motta on 27/07/2017.
 */

public class ConditionalActivity extends AppCompatActivity {

    public static final int READ_PHONE_STATE = 101;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String urlConection = PreferencesUtils.getPreferencesUrlConection(this);
        if (!urlConection.equals("")){
            initializeCheckCompromissoServece(urlConection);
        }
        initializeMain();
    }

    protected void initializeCheckCompromissoServece(String url){
        Intent intentServer = new Intent(this, CheckCompromissoService.class);
        intentServer.putExtra(getString(R.string.action_bind_service),url);
        startService(intentServer);
    }

    protected void initializeMain(){
        if (PermissionUtils.requestPermission(this, READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE)){
            callMain();
        }
    }

    protected void callMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_PHONE_STATE && resultCode == RESULT_OK && data != null) {
            callMain();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_PHONE_STATE:
                if (PermissionUtils.permissionGranted(requestCode, READ_PHONE_STATE, grantResults)) {
                    PreferencesUtils.guardarImei(this);
                    callMain();
                } else {
                    finish();
                }
                break;
        }
    }

}
