package com.rdm.notificacompromisso.app.model;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by Robson Da Motta on 26/07/2017.
 */

public class ParamServiceDTO implements Serializable {

    protected Context context;
    protected String url;
    protected int identificador;

    public ParamServiceDTO(){}

    public ParamServiceDTO(Context context, String url){
        this.context = context;
        this.url = url;
    }
    public ParamServiceDTO(Context context, String url, int identificador){
        this.context = context;
        this.url = url;
        this.identificador = identificador;
    }

    public int getIdentificador(){
        return identificador;
    }

    public Context getContext(){
        return context;
    }

    public String getUrl(){
        return url;
    }

}
