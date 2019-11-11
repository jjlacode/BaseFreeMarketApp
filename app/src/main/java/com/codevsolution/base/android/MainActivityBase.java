package com.codevsolution.base.android;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.codevsolution.base.chat.FragmentChatBase;
import com.codevsolution.base.interfaces.ICFragmentos;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.services.AutoArranque;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.web.FragmentWebView;
import com.codevsolution.freemarketsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.HTTPAYUDA;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.MIPERFIL;

public class MainActivityBase extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ICFragmentos,
        InteractorBase.Constantes, ContratoPry.Tablas, JavaUtil.Constantes {

    private static final int LOCATION_REQUEST_CODE = 333;
    protected Bundle bundle;
    public Toolbar toolbar;
    public FloatingActionButton fabNuevo;
    public FloatingActionButton fabVoz;
    public FloatingActionButton fabInicio;

    public Context context;
    protected boolean land;
    protected boolean tablet;
    protected float sizeF;
    protected String ayudaWeb;
    private String TAG = getClass().getSimpleName();

    public boolean permisoInternet;
    protected Intent intent;
    protected String accion;
    public ImageView imagenPerfil;
    public DrawerLayout drawer;

    protected void persitencia() {

        bundle = new Bundle();

        SharedPreferences persistencia = getSharedPreferences(PERSISTENCIA, MODE_PRIVATE);
        bundle.putString(ORIGEN, persistencia.getString(ORIGEN, ""));
        bundle.putString(ACTUAL, persistencia.getString(ACTUAL, ""));
        bundle.putString(ACTUALTEMP, persistencia.getString(ACTUALTEMP, ""));
        bundle.putString(CAMPO_ID, persistencia.getString(CAMPO_ID, ""));
        bundle.putInt(CAMPO_SECUENCIA, persistencia.getInt(CAMPO_SECUENCIA, 0));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "on Create");

        checkPermisos();

        land = getResources().getBoolean(R.bool.esLand);
        tablet = getResources().getBoolean(R.bool.esTablet);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int ancho = metrics.widthPixels;
        int alto = metrics.heightPixels;
        if (!land) {
            sizeF = ((float) ancho * (float) alto) / (metrics.densityDpi * 300);
        } else {
            sizeF = ((float) ancho * (float) alto) / (metrics.densityDpi * 300);
        }
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        if (savedInstanceState != null) {

            persitencia();

        } else {

            bundle = new Bundle();
            bundle.putString(ACTUAL, INICIO);

        }
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabNuevo = findViewById(R.id.fab);
        fabVoz = findViewById(R.id.fab2);
        fabInicio = findViewById(R.id.fab3);

        acciones();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recargarFragment();

        NavigationView navigationView = findViewById(R.id.nav_view);
        imagenPerfil = navigationView.getHeaderView(0).findViewById(R.id.imgnav);

        String idUser = AndroidUtil.getSharePreference(this, USERID, USERID, NULL);
        if (idUser != null && !idUser.equals(NULL) && !idUser.isEmpty()) {
            ImagenUtil.setImageFireStoreCircle(idUser, imagenPerfil);
            imagenPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    drawer.closeDrawers();
                    bundle = new Bundle();
                    bundle.putString(ACTUAL, MIPERFIL);
                    recargarFragment();
                }
            });
        }

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemTextAppearance(R.style.TextAppearance_AppCompat_Menu);

    }

    protected void inicio(int inicio) {


    }

    protected void acciones() {

        intent = getIntent();

        System.out.println("inicio = " + intent.getIntExtra(INICIO, 0));

        if (intent.getIntExtra(INICIO, 0) == 0) {
        } else if (intent.getIntExtra(INICIO, 0) == 1) {

            inicio(0);


            System.out.println("Permiso boot " + CheckPermisos.validarPermisos(this, RECEIVE_BOOT_COMPLETED, 100));
            AutoArranque.scheduleJob(AppActivity.getAppContext());

            inicio(1);

        } else if (intent.getIntExtra(INICIO, 0) == 2) {

            inicio(2);
            ayudaWeb = HTTPAYUDA + "Bienvenida";
            bundle.putString(WEB, ayudaWeb);
            enviarBundleAFragment(bundle, new FragmentWebView());
        }

        accion = intent.getAction();

        if (accion != null && accion.equals(ACCION_VERCHAT)) {

            System.out.println("Accion ver chat");

            String idChat = intent.getStringExtra(EXTRA_IDCHAT);
            String idUser = intent.getStringExtra(EXTRA_IDSPCHAT);
            int secChat = intent.getIntExtra(EXTRA_SECCHAT, 0);
            bundle.putString(ACTUAL, CHAT + intent.getStringExtra(EXTRA_ACTUAL));
            bundle.putString(CAMPO_ID, idChat);
            bundle.putString(USERID, idUser);
            bundle.putInt(CAMPO_SECUENCIA, secChat);
            NotificationManager notifyMgr = (NotificationManager)
                    AppActivity.getAppContext().getSystemService(NOTIFICATION_SERVICE);
            notifyMgr.cancel(intent.getIntExtra(EXTRA_ID, 0));

        }

    }

    protected void checkPermisos() {
    }

    @Override
    public void onBackPressed() {
        //recargarFragment();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action barOk if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action barOk item clicks here. The action barOk will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activityBase in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        bundle = new Bundle();

        setOnNavigation(item);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void setOnNavigation(MenuItem item) {

    }

    @Override
    public void enviarBundleAFragment(Bundle bundle, Fragment myFragment) {

        myFragment.setArguments(bundle);

        this.bundle = bundle;

        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment).addToBackStack(null).commit();

    }


    @Override
    public void enviarBundleAActivity(Bundle bundle) {

        this.bundle = bundle;

    }

    @Override
    public void enviarBundleFragmentFragment(Bundle bundle) {


    }

    @Override
    public void addFragment(Fragment myFragment, int layout) {

        getSupportFragmentManager().beginTransaction().add(layout, myFragment).addToBackStack(null).commit();
    }

    @Override
    public void addFragment(Bundle bundle, Fragment myFragment, int layout) {

        myFragment.setArguments(bundle);

        this.bundle = bundle;

        if (layout <= 0) {
            getSupportFragmentManager().beginTransaction().add(R.id.content_main, myFragment).addToBackStack(null).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(layout, myFragment).addToBackStack(null).commit();
        }

    }

    @Override
    public void reemplazaFragment(Bundle bundle, Fragment myFragment, int layout) {

        myFragment.setArguments(bundle);

        this.bundle = bundle;

        if (layout <= 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment).addToBackStack(null).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(layout, myFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void eliminarFragment(Fragment myFragment) {

        getSupportFragmentManager().beginTransaction().remove(myFragment).commit();
    }

    @Override
    public void fabVisible() {

        fabNuevo.show();

    }

    @Override
    public void snackBarShow(View view, String mensaje) {

        Snackbar.make(view, mensaje, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

    }

    @Override
    public void setIcoFab(int recurso) {

        fabNuevo.setImageResource(recurso);
    }

    @Override
    public void setIcoFab(Drawable drawable) {

        fabNuevo.setImageDrawable(drawable);
    }

    @Override
    public void fabOculto() {
        fabNuevo.hide();
    }

    @Override
    public void showTitle(int title) {
        toolbar.setTitle(title);
    }

    @Override
    public void showSubTitle(int subTitle) {
        toolbar.setSubtitle(subTitle);
    }

    @Override
    public void showTitle(String title) {
        toolbar.setTitle(title);

    }

    @Override
    public void showSubTitle(String subTitle) {

        toolbar.setSubtitle(subTitle);
    }


    @Override
    public void enviarAyudaWeb(String ayudaWeb) {

        this.ayudaWeb = HTTPAYUDA + ayudaWeb;
    }

    protected void recargarFragment() {

        switch (bundle.getString(ACTUAL, INICIO)) {

            case CHAT + CHAT:

                enviarBundleAFragment(bundle, new FragmentChatBase());
                break;


        }

    }

}
