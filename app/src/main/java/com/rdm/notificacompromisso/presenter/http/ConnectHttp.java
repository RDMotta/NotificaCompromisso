package com.rdm.notificacompromisso.presenter.http;

import com.google.gson.Gson;
import com.rdm.notificacompromisso.model.Compromisso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Robson Da Motta on 01/08/2017.
 */

public class ConnectHttp implements HttpGetMethods, HttpPostMethods{

    protected String streamToString(InputStream stream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String output;
        try {
            while ((output = br.readLine()) != null) {
               sb.append(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    protected Compromisso compromissoFromStrJSON(String compromisso) {
        Compromisso compRecebido = new Gson().fromJson(compromisso, Compromisso.class);
        if (compRecebido.getIdentificador() > 0) {
            return compRecebido;
        } else {
            return null;
        }
    }

    @Override
    public Compromisso requestCompromissoGet(URL url) throws IOException{
        InputStream stream = null;
        HttpURLConnection connection = null;
        Compromisso compromisso = null;
        try {
            connection = (HttpURLConnection ) url.openConnection();
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            String retorno = streamToString(connection.getInputStream());
            compromisso = compromissoFromStrJSON(retorno);
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return compromisso;

    }

    @Override
    public Compromisso requestCompromissoPost(URL url) throws IOException {
        return null;
    }

    public boolean confirmCompromisso(URL url, int id) throws IOException {
        InputStream stream = null;
        HttpURLConnection connection = null;

        try {

            connection = (HttpURLConnection ) url.openConnection();
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            return true;
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
