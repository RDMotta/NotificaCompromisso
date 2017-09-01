package com.rdm.notificacompromisso.presenter.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rdm.notificacompromisso.model.Compromisso;

import java.util.HashMap;

/**
 * Created by Robson Da Motta on 08/08/2017.
 */

public class CompromissosProvider extends ContentProvider {

    public static final String IDENTIFICADOR = "identificador";
    public static final String TITULO = "titulo";
    public static final String DESCRICAO = "descricao";
    public static final String AUTOR = "autor";
    public static final String HORA = "hora";
    public static final String MINUTO = "minuto";
    public static final String DIA = "dia";
    public static final String MES = "mes";
    public static final String ANO = "ano";
    public static final String CANCELADO = "cancelado";
    public static final String PROVIDER_NAME = "com.rdm.notificacompromisso.presenter.provider.CompromissosProvider";
    public static final String TABLE_NAME = "compromissos";
    public static final String URL = "content://" + PROVIDER_NAME + "/" + TABLE_NAME;
    public static final Uri CONTENT_URI = Uri.parse(URL);
    public static final int COMPROMISSOS = 1;
    public static final int COMPROMISSOS_ID = 2;
    public static final UriMatcher uriMatcher;
    private static HashMap<String, String> COMPROMISSOS_PROJECTION;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, TABLE_NAME, COMPROMISSOS);
        uriMatcher.addURI(PROVIDER_NAME, TABLE_NAME + "/#", COMPROMISSOS_ID);
    }

    protected DbHelper mHelper;

    public static Compromisso compromissoFrom(Cursor cursor) {
        Compromisso compromisso = new Compromisso();

        compromisso.setIdentificador(cursor.getInt(cursor.getColumnIndex(CompromissosProvider.IDENTIFICADOR)));
        compromisso.setTitulo(cursor.getString(cursor.getColumnIndex(CompromissosProvider.TITULO)));
        compromisso.setAutor(cursor.getString(cursor.getColumnIndex(CompromissosProvider.AUTOR)));
        compromisso.setDescricao(cursor.getString(cursor.getColumnIndex(CompromissosProvider.DESCRICAO)));
        compromisso.setDia(cursor.getInt(cursor.getColumnIndex(CompromissosProvider.DIA)));
        compromisso.setMes(cursor.getInt(cursor.getColumnIndex(CompromissosProvider.MES)));
        compromisso.setAno(cursor.getInt(cursor.getColumnIndex(CompromissosProvider.ANO)));
        compromisso.setHora(cursor.getInt(cursor.getColumnIndex(CompromissosProvider.HORA)));
        compromisso.setMinuto(cursor.getInt(cursor.getColumnIndex(CompromissosProvider.MINUTO)));
        compromisso.setCancelado(cursor.getInt(cursor.getColumnIndex(CompromissosProvider.CANCELADO)));

        return compromisso;
    }

    protected ContentValues pegarContentValueFrom(Compromisso compromisso) {
        ContentValues values = new ContentValues();
        values.put(CompromissosProvider.IDENTIFICADOR, compromisso.getIdentificador());
        values.put(CompromissosProvider.TITULO, compromisso.getTitulo());
        values.put(CompromissosProvider.DESCRICAO, compromisso.getDescricao());
        values.put(CompromissosProvider.AUTOR, compromisso.getAutor());
        values.put(CompromissosProvider.HORA, compromisso.getHora());
        values.put(CompromissosProvider.MINUTO, compromisso.getMinuto());
        values.put(CompromissosProvider.DIA, compromisso.getDia());
        values.put(CompromissosProvider.MES, compromisso.getMes());
        values.put(CompromissosProvider.ANO, compromisso.getAno());
        values.put(CompromissosProvider.CANCELADO, compromisso.getCancelado());
        return values;
    }

    public Uri adicionarCompromisso(Context context, Compromisso compromisso) {
        ContentValues values = pegarContentValueFrom(compromisso);
        Uri uri = context.getContentResolver().insert(
                CompromissosProvider.CONTENT_URI, values);
        return uri;
    }

    public boolean atualizarCompromisso(Context context, Compromisso compromisso) {
        ContentValues values = pegarContentValueFrom(compromisso);
        String whereClause = CompromissosProvider.IDENTIFICADOR + " = ? ";
        String[] whereArgs = {String.valueOf(compromisso.getIdentificador())};
        int retorno = context.getContentResolver().update(
                CompromissosProvider.CONTENT_URI, values, whereClause, whereArgs);
        return retorno > 0;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mHelper = new DbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case COMPROMISSOS:
                qb.setProjectionMap(COMPROMISSOS_PROJECTION);
                break;

            case COMPROMISSOS_ID:
                qb.appendWhere(IDENTIFICADOR + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        if (sortOrder == null || sortOrder == "") {
            sortOrder = DIA;
        }
        SQLiteDatabase database = mHelper.getReadableDatabase();
        Cursor curson = qb.query(database, projection, selection,
                selectionArgs, null, null, sortOrder);
        curson.setNotificationUri(getContext().getContentResolver(), uri);
        return curson;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case COMPROMISSOS:
                return "vnd.android.cursor.dir/vnd.rdm.notificacompromisso.compromissos";
            case COMPROMISSOS_ID:
                return "vnd.android.cursor.item/vnd.rdm.notificacompromisso.compromissos";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        long rowID = database.insert(TABLE_NAME, "", contentValues);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Erro ao incluir um novo compromisso " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase database = null;
        switch (uriMatcher.match(uri)) {
            case COMPROMISSOS:
                database = mHelper.getWritableDatabase();
                count = database.delete(TABLE_NAME, selection, selectionArgs);
                break;

            case COMPROMISSOS_ID:
                String id = uri.getPathSegments().get(1);
                database = mHelper.getWritableDatabase();
                count = database.delete(TABLE_NAME, IDENTIFICADOR + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = null;
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case COMPROMISSOS:
                database = mHelper.getWritableDatabase();
                count = database.update(TABLE_NAME, contentValues, selection, selectionArgs);
                break;

            case COMPROMISSOS_ID:
                database = mHelper.getWritableDatabase();
                count = database.update(TABLE_NAME, contentValues,
                        IDENTIFICADOR + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? "  AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
