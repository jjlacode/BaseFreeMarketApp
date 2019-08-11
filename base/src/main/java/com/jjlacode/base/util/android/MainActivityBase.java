package com.jjlacode.base.util.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.interfaces.ICFragmentos;
import com.jjlacode.freelanceproject.CommonPry;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.sqlite.ContratoPry;

public class MainActivityBase extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ICFragmentos,
        CommonPry.Constantes, ContratoPry.Tablas, JavaUtil.Constantes {

    protected Bundle bundle;
    public Toolbar toolbar;
    public FloatingActionButton fab;
    public FloatingActionButton fab2;

    public Context context;
    protected boolean land;
    protected boolean tablet;
    protected float sizeF;
    protected String ayudaWeb;
    private String TAG = getClass().getSimpleName();

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

        land = getResources().getBoolean(R.bool.esLand);
        tablet = getResources().getBoolean(R.bool.esTablet);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int ancho = metrics.widthPixels;
        int alto = metrics.heightPixels;
        if (!land) {
            sizeF = (float) (((float) ancho * (float) alto) / (metrics.densityDpi * 300));
        } else {
            sizeF = (float) (((float) ancho * (float) alto) / (metrics.densityDpi * 300));
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

        fab = findViewById(R.id.fab);
        fab2 = findViewById(R.id.fab2);

        acciones();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recargarFragment();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemTextAppearance(R.style.TextAppearance_AppCompat_Menu);

    }

    protected void acciones() {

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    public void enviarAyudaWeb(String ayudaWeb) {

        this.ayudaWeb = ayudaWeb;
    }

    protected void recargarFragment() {


    }


}
