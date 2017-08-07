package com.rdm.notificacompromisso.presenter.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.webkit.URLUtil;

import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.model.ParamServiceDTO;
import com.rdm.notificacompromisso.presenter.utils.PreferencesUtils;

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
            ParamServiceDTO paramServiceDTO = new ParamServiceDTO(this,url);
            new AsyncTaskService().execute(paramServiceDTO);
        } else {
            ParamServiceDTO paramServiceDTO = new ParamServiceDTO(this,"http://geo-pt.appspot.com/srtmPT?x=117006.46&y=-11722.69&interpol=bilinear");
            new AsyncTaskService().execute(paramServiceDTO);
        }
    }

    public void confirmCompromissos(int idCompromisso){
        String url = PreferencesUtils.getPreferencesUrlConection(this);
        if (URLUtil.isValidUrl(url)){
            ParamServiceDTO paramServiceDTO = new ParamServiceDTO(this, url, idCompromisso);
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
