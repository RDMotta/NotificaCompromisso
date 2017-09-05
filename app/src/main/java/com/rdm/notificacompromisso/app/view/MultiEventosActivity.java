package com.rdm.notificacompromisso.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rdm.notificacompromisso.app.R;
import com.rdm.notificacompromisso.app.model.AdapterEventos;
import com.rdm.notificacompromisso.app.model.Compromisso;
import com.rdm.notificacompromisso.app.presenter.utils.EventsUtils;

/**
 * Created by Robson Da Motta on 28/08/2017.
 */

public class MultiEventosActivity extends AppCompatActivity {

    public static String TAG = "MultiEventosActivity";
    public static int REQUEST_CODE_MULTEVENTS = 10001;
    private RecyclerView recyclerView;
    private EventsUtils eventsUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_eventos);
        eventsUtils = new EventsUtils(getApplicationContext(), this);
        init();
    }

    protected void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_multEvents);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        Compromisso compromisso = pegarCompromissoExtra();
        eventsUtils.setEventoInicial(compromisso);
        eventsUtils.loadMultEventos();
    }

    public void setAdapterRecycler(AdapterEventos adapter) {
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEvento = new Intent(getApplicationContext(), EventosActivity.class);
                Compromisso compromisso = (Compromisso) view.getTag();
                intentEvento.putExtra(getString(R.string.action_notification_show), compromisso);
                startActivityForResult(intentEvento, REQUEST_CODE_MULTEVENTS);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public  void definirCompromissoExtra(Compromisso compromisso){
        String action_notification_show = getString(R.string.action_notification_show);
        getIntent().putExtra(action_notification_show, compromisso);
    }

    public Compromisso pegarCompromissoExtra(){
        String action_notification_show = getString(R.string.action_notification_show);
        return getIntent().getExtras().getParcelable(action_notification_show);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == EventosActivity.RESULT_CODE_EVENTO) {
            eventsUtils.loadMultEventos();
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
