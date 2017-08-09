package com.rdm.notificacompromisso.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.presenter.services.CheckCompromissoService;
import com.rdm.notificacompromisso.presenter.utils.DialogUtils;
import com.rdm.notificacompromisso.presenter.utils.PreferencesUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ServiceConnection {

    private boolean mIsBound = false;
    private CheckCompromissoService mBoundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    /**
     * Método local para inicializar os componentes da activity
     */
    protected void initComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (onActionItemMenu(item.getItemId())) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        onActionItemMenu(item.getItemId());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Método para centralizar as ações dos menus
     * @param menuItemId identificador do item de menu selecionado
     * @return retorno verdadeiro quando identificado um item de menu
     */
    protected boolean onActionItemMenu(int menuItemId) {
        boolean actionItemExecuted = false;
        if ((menuItemId == R.id.nav_today) || (menuItemId == R.id.action_today)) {
            getFragmentManager().beginTransaction().replace(R.id.content, CalendarFragment.newInstance()).commit();
            bindCheckCompromisso();
            actionItemExecuted = true;
        } else if ((menuItemId == R.id.nav_refresh) || (menuItemId == R.id.action_refresh)) {
            bindCheckCompromisso();
            actionItemExecuted = true;
        } else if ((menuItemId == R.id.nav_preferences) || (menuItemId == R.id.action_preferences)) {

            getFragmentManager().beginTransaction().
                    replace(R.id.content, new PreferenciasActivity()).commit();
            actionItemExecuted = true;
        }
        return actionItemExecuted;
    }

    /**
     * Método para acessar o Service e realizar a conexão
     * com o WS para obter novas atualizações;
     */
    protected void bindCheckCompromisso() {
        if (mIsBound) {
            String urlConection = PreferencesUtils.getPreferencesUrlConection(getApplicationContext());
            mBoundService.checkCompromissos(urlConection);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        doBindService();
        getFragmentManager().beginTransaction().replace(R.id.content, CalendarFragment.newInstance()).commit();

        //showCompromissos();

        AlertDialog.Builder alerta = DialogUtils.showCompromisso(this, getIntent());
        if (alerta != null){
            alerta.show();
        }

    }

//    private void showCompromissos() {
//
//        String URL = "content://" + CompromissosProvider.PROVIDER_NAME;
//        Uri compromissos = Uri.parse(URL);
//        Cursor c = managedQuery(compromissos, null, null, null, CompromissosProvider.DIA);
//        if (c.moveToFirst()) {
//            do{
//
//
//                Toast.makeText(this,"Compromisso em: " + c.getInt(c.getColumnIndex(CompromissosProvider.DIA)) + "" +
//                       " As : " + c.getInt(c.getColumnIndex(CompromissosProvider.HORA)), Toast.LENGTH_SHORT).show();
//
//
//            } while (c.moveToNext());
//        }
//        c.close();
//
//    }

    /**
     * Método local para iniciar a coneção com o Service
     */
    protected void doBindService() {
        Intent intentBindService = new Intent(MainActivity.this, CheckCompromissoService.class);
        bindService(intentBindService, this, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    /**
     * Método local para remover a conexão com o Servie
     */
    protected void doUnbindService() {
        if (mIsBound) {
            unbindService(this);
            mIsBound = false;
        }
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
}


