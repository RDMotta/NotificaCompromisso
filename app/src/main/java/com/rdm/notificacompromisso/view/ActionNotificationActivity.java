package com.rdm.notificacompromisso.view;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.model.Compromisso;
import com.rdm.notificacompromisso.presenter.db.CompromissosProvider;
import com.rdm.notificacompromisso.presenter.services.CheckCompromissoService;

public class ActionNotificationActivity extends AppCompatActivity implements ServiceConnection {

    private boolean bBindService = false;
    private CheckCompromissoService mBoundService;
    private int idNotification;
    private String action_confirm = "";
    private String action = "";
    private boolean seenEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seenEvent = false;
        idNotification = getIntent().getIntExtra(getString(R.string.id_notification_value), 0);
        action_confirm = getString(R.string.action_notification_confirm);
        action = getIntent().getStringExtra(action_confirm);
    }

    protected void confirmarNotificacao() {
        if (bBindService) {
            mBoundService.confirmCompromissos(idNotification);
        }
    }

    protected boolean showEventoIfExist() {
        String action_show = getString(R.string.action_notification_show);

        boolean existAction = ((action_show != null) && getIntent().getExtras().containsKey(action_show));
        seenEvent = false;
        if (existAction) {
            seenEvent = true;
            Intent intentEvento = new Intent(getApplicationContext(), EventosActivity.class);
            Compromisso compromisso = getIntent().getExtras().getParcelable(action_show);
            intentEvento.putExtra(action_show, compromisso);
            startActivity(intentEvento);

        }
        return seenEvent;
    }

    protected void cancelarNotificacao() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(idNotification);
        finish();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mBoundService =
                ((CheckCompromissoService.CheckCompromissoBinder) iBinder)
                        .getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mBoundService = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        doBindService();

        if (seenEvent) {
            finish();
        } else {
            if ((action != null) && action.equals(action_confirm)) {
                confirmarNotificacao();
            } else if (!showEventoIfExist()) {
                cancelarNotificacao();
                cancelarEvento();
            }
        }
    }

    protected void cancelarEvento() {
        String action_evento_notification = getString(R.string.action_evento_notification);
        if (getIntent().getExtras().containsKey(action_evento_notification)) {
            Compromisso compromisso = getIntent().getExtras().getParcelable(action_evento_notification);
            compromisso.setCancelado(1);
            CompromissosProvider compromissosProvider = new CompromissosProvider();
            compromissosProvider.atualizarCompromisso(getApplicationContext(), compromisso);
        }
    }

    protected void doBindService() {
        Intent intentBindService = new Intent(ActionNotificationActivity.this, CheckCompromissoService.class);
        bindService(intentBindService, this, Context.BIND_AUTO_CREATE);
        bBindService = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
        bBindService = false;
    }
}
