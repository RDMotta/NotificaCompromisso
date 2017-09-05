package com.rdm.notificacompromisso.app.view;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.rdm.notificacompromisso.app.R;
import com.rdm.notificacompromisso.app.model.Compromisso;
import com.rdm.notificacompromisso.app.presenter.db.CompromissosProvider;
import com.rdm.notificacompromisso.app.presenter.utils.EventsUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EventosActivity extends AppCompatActivity {

    public static String TAG = "EventosActivity";
    public static int REQUEST_CODE_EVENTO = 10003;
    public static int RESULT_CODE_EVENTO = 10005;

    private Compromisso mCompromisso;
    private int idNotification;
    private TextView txt_tituloEvento;
    private TextView txt_horaEvento;
    private TextView txt_descricaoEvento;
    private FloatingActionButton fab;
    private EventsUtils eventsUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        if (!contemEvento()) {
            finish();
        }
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        idNotification = 0;
        fab = (FloatingActionButton) findViewById(R.id.fabCancel);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invalidarTexto();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fab.setImageResource(R.drawable.ic_bell_off);
                        mCompromisso.setCancelado(1);
                        definirCompromissoExtra(mCompromisso);
                        CompromissosProvider compromissosProvider = new CompromissosProvider();
                        compromissosProvider.atualizarCompromisso(getApplicationContext(), mCompromisso);
                        setResult(RESULT_CODE_EVENTO);
                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.cancel((int) mCompromisso.getIdentificador());
                    }
                });
            }
        });

        String action_notification_confirm = getString(R.string.action_notification_confirm);
        idNotification = getIntent().getIntExtra(action_notification_confirm, 0);
        if (idNotification > 0) {
            mCompromisso = getIntent().getExtras().getParcelable(action_notification_confirm);
        } else {
            mCompromisso = pegarCompromissoExtra();
        }

        txt_tituloEvento = (TextView) findViewById(R.id.txt_tituloEvento);
        txt_horaEvento = (TextView) findViewById(R.id.txt_horaEvento);
        txt_descricaoEvento = (TextView) findViewById(R.id.txt_descricaoEvento);

        eventsUtils = new EventsUtils(getApplicationContext(), this);
        eventsUtils.setEventoInicial(mCompromisso);
        eventsUtils.loadEvento();

        formatCamposEvento();
    }

    protected void formatCamposEvento() {
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        SimpleDateFormat hf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, mCompromisso.getDia());
        calendar.set(Calendar.MONTH, mCompromisso.getMes());
        calendar.set(Calendar.YEAR, mCompromisso.getAno());
        calendar.set(Calendar.HOUR_OF_DAY, mCompromisso.getHora());
        calendar.set(Calendar.MINUTE, mCompromisso.getMinuto());

        txt_tituloEvento.setText(mCompromisso.getTitulo());
        txt_descricaoEvento.setText(mCompromisso.getDescricao());
        txt_horaEvento.setText(hf.format(calendar.getTime()));
        setTitle(df.format(calendar.getTime()));
        if (mCompromisso.getCancelado() == 1) {
            invalidarTexto();
            fab.setImageResource(R.drawable.ic_bell_off);
        } else{
            fab.setImageResource(R.drawable.ic_bell);
        }
    }

    protected boolean contemEvento() {
        String action_notification_confirm = getString(R.string.action_notification_confirm);
        String action_notification_show = getString(R.string.action_notification_show);
        boolean exists = getIntent().getExtras().containsKey(action_notification_confirm) || getIntent().getExtras().containsKey(action_notification_show);

        return exists;
    }

    protected void invalidarTexto() {
        txt_horaEvento.setPaintFlags(txt_tituloEvento.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txt_tituloEvento.setPaintFlags(txt_tituloEvento.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txt_descricaoEvento.setPaintFlags(txt_tituloEvento.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public void definirCompromissoExtra(Compromisso compromisso) {
        String action_notification_show = getString(R.string.action_notification_show);
        getIntent().putExtra(action_notification_show, compromisso);
    }

    public Compromisso pegarCompromissoExtra() {
        String action_notification_show = getString(R.string.action_notification_show);
        return getIntent().getExtras().getParcelable(action_notification_show);
    }
}
