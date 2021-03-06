package com.rdm.notificacompromisso.app.presenter.http;

import android.content.Context;
import android.net.Uri;

import com.rdm.notificacompromisso.app.model.Compromisso;
import com.rdm.notificacompromisso.app.presenter.utils.PreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.ArrayList;

/**
 * Created by Robson Da Motta on 01/08/2017.
 */

public class ConnectHttp implements HttpGetMethods, HttpPostMethods {

    private Context mContext;

    public ConnectHttp(Context context) {
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
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

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

    protected ArrayList<Compromisso> compromissosFromStrJSON(String strCompromisso) {
        ArrayList<Compromisso> compRecebidos = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(strCompromisso);
            for (int indice =0; indice < jsonArray.length(); indice ++){
                String strJson = jsonArray.get(indice).toString();

                JSONObject json = new JSONObject(strJson);
                Compromisso compRecebido = new Compromisso();

                compRecebido.setIdentificador(json.getLong("identificador"));
                compRecebido.setTitulo(json.getString("titulo"));
                compRecebido.setDescricao(json.getString("descricao"));
                compRecebido.setAutor(json.getString("autor"));
                compRecebido.setHora(json.getInt("hora"));
                compRecebido.setMinuto(json.getInt("minuto"));
                compRecebido.setDia(json.getInt("dia"));
                compRecebido.setMes(json.getInt("mes")-1);
                compRecebido.setAno(json.getInt("ano"));
                compRecebido.setCancelado(json.getInt("cancelado"));
                compRecebidos.add(compRecebido);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return compRecebidos;
    }

    @Override
    public ArrayList<Compromisso>  requestCompromissoGet(String url) throws IOException {
        ArrayList<Compromisso>  compromisso = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String imei = PreferencesUtils.getPreferencesIMEI(mContext);

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("imei", imei);
        String query = builder.build().getEncodedQuery();

        try {
            urlConnection = (HttpURLConnection) request("GET", url, null, "application/json", query);

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
            } else {
                throw new IOException("HTTP error code: " + responseCode);
            }
            String retorno = streamToString(inputStream);
            compromisso = compromissosFromStrJSON(retorno);

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            return compromisso;
        }
    }

    @Override
    public ArrayList<Compromisso>  requestCompromissoPost(String url) throws IOException {
        return null;
    }

    public boolean confirmCompromisso(String url, int id) throws IOException {
        InputStream stream = null;
        HttpURLConnection urlConnection = null;
        String imei = PreferencesUtils.getPreferencesIMEI(mContext);
        try {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("imei", imei)
                    .appendQueryParameter("id", String.valueOf(id));
            String query = builder.build().getEncodedQuery();

            urlConnection = (HttpURLConnection) request("POS", url, null, "application/json", query);
            int responseCode = urlConnection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            return true;
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

}
