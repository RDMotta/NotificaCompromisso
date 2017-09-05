package com.rdm.notificacompromisso.app.presenter.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.webkit.URLUtil;

import com.rdm.notificacompromisso.app.R;
import com.rdm.notificacompromisso.app.model.ParamServiceDTO;
import com.rdm.notificacompromisso.app.presenter.utils.PreferencesUtils;

public class CheckCompromissoService extends Service {
    private final IBinder mBinder = new CheckCompromissoBinder();
    private String mUrlConexao = "";

    public class CheckCompromissoBinder extends Binder {
        public CheckCompromissoService getService() {
            return CheckCompromissoService.this;
        }
    }
    public CheckCompromissoService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null){
            mUrlConexao = intent.getStringExtra(getString(R.string.action_bind_service));
        }
        checkCompromissos(mUrlConexao);
        return START_NOT_STICKY;
    }

    public void checkCompromissos(String url){
        if (URLUtil.isValidUrl(url)){
            ParamServiceDTO paramServiceDTO = new ParamServiceDTO(getApplicationContext(),url);
            new AsyncTaskService().execute(paramServiceDTO);
        }
    }

    public void confirmCompromissos(int idCompromisso){
        String url = PreferencesUtils.getPreferencesUrlConection(getApplicationContext());
        if (URLUtil.isValidUrl(url)){
            ParamServiceDTO paramServiceDTO = new ParamServiceDTO(getApplicationContext(), url, idCompromisso);
            new AsyncTaskServiceSendConfirm().execute(paramServiceDTO);
        }
    }

    @Override
    public void onDestroy(){}

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
