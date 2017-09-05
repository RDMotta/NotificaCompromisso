package com.rdm.notificacompromisso.app.presenter.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;

import com.rdm.notificacompromisso.app.model.AdapterEventos;
import com.rdm.notificacompromisso.app.model.Compromisso;
import com.rdm.notificacompromisso.app.presenter.db.CompromissosProvider;
import com.rdm.notificacompromisso.app.view.EventosActivity;
import com.rdm.notificacompromisso.app.view.MultiEventosActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Robson Da Motta on 31/08/2017.
 */

public class EventsUtils {

    private WeakReference<MultiEventosActivity> multiEventosActivityWeakReference;
    private WeakReference<EventosActivity> eventosActivityWeakReference;
    private Context mContext;
    private Compromisso mCompromisso;

    public EventsUtils(Context context, AppCompatActivity compatActivity) {
        this.mContext = context;
        if (compatActivity instanceof EventosActivity) {
            this.eventosActivityWeakReference = new WeakReference<EventosActivity>((EventosActivity) compatActivity);
        } else if (compatActivity instanceof MultiEventosActivity) {
            this.multiEventosActivityWeakReference = new WeakReference<MultiEventosActivity>((MultiEventosActivity) compatActivity);
        }
    }

    public void setEventoInicial(Compromisso compromisso) {
        this.mCompromisso = compromisso;
    }

    public void loadEvento() {
        if (eventosActivityWeakReference != null) {
            loadEventSelectedDate(mCompromisso);
        }
    }

    public void loadMultEventos() {
        if (multiEventosActivityWeakReference != null) {
            loadEventsSelectedDate(mCompromisso);
        }
    }

    public void loadEventSelectedDate(Compromisso compromisso) {
        String mSelectionClause = CompromissosProvider.IDENTIFICADOR + " = ? and " +
                CompromissosProvider.DIA + " = ? and " +
                CompromissosProvider.MES + " = ? and " +
                CompromissosProvider.ANO + " = ? ";
        String[] mSelectionArgs = {String.valueOf(compromisso.getDia()),
                String.valueOf(compromisso.getMes()),
                String.valueOf(compromisso.getAno())};

        Cursor cursor = eventosActivityWeakReference.get()
                .managedQuery(CompromissosProvider.CONTENT_URI, null, mSelectionClause, mSelectionArgs, CompromissosProvider.HORA);
        eventosActivityWeakReference.get().startManagingCursor(cursor);
        if (cursor.moveToNext()) {
            mCompromisso = CompromissosProvider.compromissoFrom(cursor);
        }
        eventosActivityWeakReference.get().definirCompromissoExtra(mCompromisso);
        eventosActivityWeakReference.get().stopManagingCursor(cursor);
    }


    public void loadEventsSelectedDate(Compromisso compromisso) {

        ArrayList<Compromisso> compromissoArrayList = new ArrayList<>();
        String mSelectionClause = CompromissosProvider.DIA + " = ? and " +
                CompromissosProvider.MES + " = ? and " +
                CompromissosProvider.ANO + " = ? ";
        String[] mSelectionArgs = {String.valueOf(compromisso.getDia()),
                String.valueOf(compromisso.getMes()),
                String.valueOf(compromisso.getAno())};
        Cursor cursor = multiEventosActivityWeakReference.get()
                .managedQuery(CompromissosProvider.CONTENT_URI, null, mSelectionClause, mSelectionArgs, CompromissosProvider.HORA);
        multiEventosActivityWeakReference.get().startManagingCursor(cursor);
        if (cursor.moveToFirst()) {
            do {
                Compromisso compromissoDia = CompromissosProvider.compromissoFrom(cursor);
                compromissoArrayList.add(compromissoDia);
            } while (cursor.moveToNext());
        }
        multiEventosActivityWeakReference.get().stopManagingCursor(cursor);
        AdapterEventos adapterEventos = new AdapterEventos(mContext, compromissoArrayList);
        multiEventosActivityWeakReference.get().setAdapterRecycler(adapterEventos);
    }

}
