package com.rdm.notificacompromisso.app.presenter.http;

import com.rdm.notificacompromisso.app.model.Compromisso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Robson Da Motta on 01/08/2017.
 */

public interface HttpPostMethods {
    public ArrayList<Compromisso> requestCompromissoPost(String url) throws IOException;
}
