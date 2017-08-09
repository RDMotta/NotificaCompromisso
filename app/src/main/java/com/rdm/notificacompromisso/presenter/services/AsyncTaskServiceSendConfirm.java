package com.rdm.notificacompromisso.presenter.services;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;

import com.rdm.notificacompromisso.model.ParamServiceDTO;
import com.rdm.notificacompromisso.presenter.db.CompromissosProvider;
import com.rdm.notificacompromisso.presenter.http.ConnectHttp;

import java.io.IOException;

/**
 * Created by Robson Da Motta on 06/08/2017.
 */

public class AsyncTaskServiceSendConfirm extends AsyncTask<ParamServiceDTO, Void, Boolean> {

    private Context mContext;
    private String mUrl;
    private int mIdentificador;

    @Override
    protected Boolean doInBackground(ParamServiceDTO... paramServiceDTOs) {
        mContext = paramServiceDTOs[0].getContext();
        mUrl = paramServiceDTOs[0].getUrl();
        mIdentificador = paramServiceDTOs[0].getIdentificador();
        boolean confirmCompromisso;
        try {
            confirmCompromisso = confirmar(mUrl);
            if (confirmCompromisso) {

                ContentResolver contentResolver = mContext.getContentResolver();
                contentResolver.delete(
                        CompromissosProvider.CONTENT_URI.buildUpon().appendPath(String.valueOf(mIdentificador)).build(),
                        null, null);
            }
            return confirmCompromisso;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean confirmar(String url) throws IOException {
        ConnectHttp connectHttp = new ConnectHttp(mContext);
        return connectHttp.confirmCompromisso(url, mIdentificador);
    }
}
