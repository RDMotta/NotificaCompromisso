package com.rdm.notificacompromisso.presenter.http;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.rdm.notificacompromisso.model.Compromisso;
import com.rdm.notificacompromisso.presenter.utils.PreferencesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Robson Da Motta on 01/08/2017.
 */

public class ConnectHttp implements HttpGetMethods, HttpPostMethods {

    private Context mContext;

    public ConnectHttp (Context context){
        this.mContext = context;
    }

    public static URLConnection request(String metodoConectar, String urlConexao, String accessToken, String mimeType, String requestBody) throws IOException {
        URL url = new URL(urlConexao);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(3000);
        urlConnection.setConnectTimeout(3000);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(requestBody != null);
        urlConnection.setRequestMethod(metodoConectar);


        if (accessToken != null) {
            urlConnection.setRequestProperty("Authorization", "imei_app " + accessToken);
        }
        urlConnection.setRequestProperty("Content-Type", mimeType);
        if (requestBody != null) {
            byte[] postDataBytes = requestBody.toString().getBytes("UTF-8");
            urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty( "charset", "utf-8");
            urlConnection.setRequestProperty( "Content-Length", String.valueOf(postDataBytes.length));

            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            writer.write(requestBody);
            writer.flush();
            writer.close();
            outputStream.close();
        }

        urlConnection.connect();

        return urlConnection;
    }

    public static String addParameters(Object content) {
        String output = null;
        if ((content instanceof String) ||
                (content instanceof JSONObject) ||
                (content instanceof JSONArray)) {
            output = content.toString();
        } else if (content instanceof Map) {
            Uri.Builder builder = new Uri.Builder();
            HashMap hashMap = (HashMap) content;
            if (hashMap != null) {
                Iterator entries = hashMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    builder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                    entries.remove();
                }
                output = builder.build().getEncodedQuery();
            }
        }

        return output;
    }

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
    public Compromisso requestCompromissoGet(String url) throws IOException {
        Compromisso compromisso = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        String imei = PreferencesUtils.getPreferencesIMEI(mContext);

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("imei", imei);
        String query = builder.build().getEncodedQuery();
        ///urlApi += builder.toString();
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) request("GET", url, null, "application/json", query);

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
            } else {
                throw new IOException("HTTP error code: " + responseCode);
            }
            String retorno = streamToString(inputStream);
            compromisso = compromissoFromStrJSON(retorno);

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
            return compromisso;
        }
    }

    @Override
    public Compromisso requestCompromissoPost(String url) throws IOException {
        return null;
    }

    public boolean confirmCompromisso(String url, int id) throws IOException {
        InputStream stream = null;
        HttpURLConnection connection = null;
        String imei = PreferencesUtils.getPreferencesIMEI(mContext);

        try {
            Uri.Builder builder = new Uri.Builder()

                    .appendQueryParameter("imei", imei)
                    .appendQueryParameter("id", String.valueOf(id));
            String query = builder.build().getEncodedQuery();

            HttpURLConnection urlConnection = (HttpURLConnection) request("POS", url, null, "application/json", null);
            int responseCode = urlConnection.getResponseCode();
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
