package com.rdm.notificacompromisso.presenter.services;

import android.content.Context;
import android.os.AsyncTask;

import com.rdm.notificacompromisso.model.ParamServiceDTO;
import com.rdm.notificacompromisso.presenter.http.ConnectHttp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Robson Da Motta on 06/08/2017.
 */

public class AsyncTaskServiceSendConfirm  extends AsyncTask<ParamServiceDTO, Void, Boolean> {

    private Context mContext;
    private String mUrl;
    private int mIdentificador;

    @Override
    protected Boolean doInBackground(ParamServiceDTO... paramServiceDTOs) {

        mContext = paramServiceDTOs[0].getContext();
        mUrl = paramServiceDTOs[0].getUrl();
        mIdentificador = paramServiceDTOs[0].getIdentificador();

        try {
            mUrl += "?confirmar="+mIdentificador;
           return confirmar(new URL(mUrl));
        }catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean confirmar(URL url) throws IOException  {
        ConnectHttp connectHttp = new ConnectHttp();
        return connectHttp.confirmCompromisso(url, mIdentificador);
    }
}
