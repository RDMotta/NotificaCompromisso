package com.rdm.notificacompromisso.presenter.http;

import com.rdm.notificacompromisso.model.Compromisso;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Robson Da Motta on 01/08/2017.
 */

public interface HttpPostMethods {
    public Compromisso requestCompromissoPost(URL url) throws IOException;
}
