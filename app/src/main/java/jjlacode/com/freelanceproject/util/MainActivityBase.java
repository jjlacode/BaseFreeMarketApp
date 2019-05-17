package jjlacode.com.freelanceproject.util;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ACTUAL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ID;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.LISTA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.MODELO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ORIGENTEMP;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.NUEVOREGISTRO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.SECUENCIA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ORIGEN;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.VERLISTA;

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


    @Override
    protected void onResume() {
        super.onResume();
        if (!inicio){
            persitencia();
        }else {

            inicio = false;
        }
    }


    protected void persitencia(){

        bundle = new Bundle();

        SharedPreferences persistencia=getSharedPreferences(PERSISTENCIA, MODE_PRIVATE);
        bundle.putString(ORIGEN, persistencia.getString(ORIGEN,""));
        bundle.putString(ACTUAL, persistencia.getString(ACTUAL,""));
        bundle.putString(ID, persistencia.getString(ID,""));
        bundle.putInt(SECUENCIA, persistencia.getInt(SECUENCIA,0));
        recargarFragment();
        System.out.println("persistencia");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        validarPermisos();

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
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


    private void validarPermisos() {


        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M ){

            CommonPry.permiso = true;
        }
        else if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CALL_PHONE)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(INTERNET)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                ){

            CommonPry.permiso = true;
        }
        else if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))||
                (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE))||
                (shouldShowRequestPermissionRationale(INTERNET))||
                (shouldShowRequestPermissionRationale(CAMERA))||
                (shouldShowRequestPermissionRationale(CALL_PHONE))){

            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle("Permisos desactivados");
            dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la app");

            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    requestPermissions(new String[]
                            {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE,CAMERA,RECORD_AUDIO,INTERNET},100);
                }
            });
            dialogo.show();

        }
        else {

            requestPermissions(new String[]
                    {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE,CAMERA,RECORD_AUDIO,INTERNET},100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100 && grantResults.length==5 && (grantResults[0]==PackageManager.PERMISSION_GRANTED &&
                grantResults[1]==PackageManager.PERMISSION_GRANTED &&
                grantResults[2]==PackageManager.PERMISSION_GRANTED &&
                grantResults[3]==PackageManager.PERMISSION_GRANTED &&
                grantResults[4]==PackageManager.PERMISSION_GRANTED)){

            CommonPry.permiso = true;

        }else{

            Toast.makeText(this,"Debe aceptar todos los permisos para " +
                    "que la app se ejecute correctamente",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState = bundle;
        super.onSaveInstanceState(outState);
        System.out.println("guardado outstate");
    }

    protected void recargarFragment(){



    }


}
