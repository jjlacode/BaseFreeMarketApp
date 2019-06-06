package jjlacode.com.freelanceproject.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;

public class MainActivityBase extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ICFragmentos,
        CommonPry.Constantes, ContratoPry.Tablas, JavaUtil.Constantes {

    protected TextView tituloCabecera ;
    protected Bundle bundle;
    public Toolbar toolbar;
    public FloatingActionButton fab;
    public FloatingActionButton fab2;

    protected Modelo modelo;
    protected String id;
    protected int secuencia;
    public boolean inicio;
    protected ListaModelo lista;
    public Context context;
    protected boolean land;
    protected boolean tablet;


    @Override
    protected void onResume() {
        super.onResume();
        if (!inicio){
            persitencia();
        }
    }

    protected void persitencia(){

        bundle = new Bundle();

        SharedPreferences persistencia=getSharedPreferences(PERSISTENCIA, MODE_PRIVATE);
        bundle.putString(ORIGEN, persistencia.getString(ORIGEN,""));
        bundle.putString(ACTUAL, persistencia.getString(ACTUAL,""));
        bundle.putString(ACTUALTEMP, persistencia.getString(ACTUALTEMP,""));
        bundle.putString(ID, persistencia.getString(ID,""));
        bundle.putInt(SECUENCIA, persistencia.getInt(SECUENCIA,0));
        recargarFragment();
        System.out.println("persistencia");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        land = getResources().getBoolean(R.bool.esLand);
        tablet = getResources().getBoolean(R.bool.esTablet);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int ancho = metrics.widthPixels;
        int alto = 100;
        int pad = 10;
        float sizef = 0f;
        if (!land){
            alto = (int) ((double)ancho/3);
            pad = (int) ((double)ancho/10);
            sizef = (float) ((double)ancho/100);
        }else {
            alto = (int) ((double)ancho/6);
            pad = (int) ((double)ancho/20);
            sizef = (float) ((double)ancho/100);
        }
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        if (savedInstanceState!=null){
            bundle = savedInstanceState;
            System.out.println("saveinstance");

        }else {

            bundle = new Bundle();
            bundle.putString(ACTUAL, INICIO);

        }
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inicio = true;

        tituloCabecera = findViewById(R.id.tvmaintitulo);
        tituloCabecera.setVisibility(View.GONE);

        fab =  findViewById(R.id.fab);
        fab2 =  findViewById(R.id.fab2);

        acciones();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recargarFragment();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    protected void acciones(){

    }

    @Override
    public void onBackPressed() {
        recargarFragment();
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
        id=null;
        modelo=null;
        secuencia=0;
        bundle = new Bundle();

        setOnNavigation(item);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void setOnNavigation(MenuItem item){

    }

    @Override
    public void enviarBundleAFragment(Bundle bundle, Fragment myFragment) {

        myFragment.setArguments(bundle);
        System.out.println("envia bundle a fragment = " + bundle);

        this.bundle = bundle;

        getSupportFragmentManager().beginTransaction().replace(R.id.content_main,myFragment).addToBackStack(null).commit();

    }


    @Override
    public void enviarBundleAActivity(Bundle bundle) {

        this.bundle = bundle;

    }

    @Override
    public void fabVisible() {

        fab.show();

    }

    @Override
    public void snackBarShow(View view, String mensaje) {

        Snackbar.make(view, mensaje, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

    }

    @Override
    public void setIcoFab(int recurso) {

        fab.setImageResource(recurso);
    }

    @Override
    public void setIcoFab(Drawable drawable) {

        fab.setImageDrawable(drawable);
    }

    @Override
    public void fabOculto() {
        fab.hide();
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState = bundle;
        super.onSaveInstanceState(outState);
        System.out.println("guardado outstate");
        System.out.println("outState = " + outState);
    }

    protected void recargarFragment(){



    }


}
