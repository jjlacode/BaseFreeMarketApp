package com.codevsolution.freemarketsapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.CheckPermisos;
import com.codevsolution.base.android.MainActivityBase;
import com.codevsolution.base.sqlite.SQLiteUtil;
import com.codevsolution.base.web.FragmentWebView;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.codevsolution.freemarketsapp.settings.SettingsActivity;
import com.codevsolution.freemarketsapp.ui.AltaProductosSorteos;
import com.codevsolution.freemarketsapp.ui.ListadoProductos;
import com.codevsolution.freemarketsapp.ui.ListadoProductosSorteos;

import static android.Manifest.permission.INTERNET;

public class MainActivity extends MainActivityBase implements Interactor.ConstantesPry {


    @Override
    protected void acciones() {
        super.acciones();

        //validarPermisos();



        if (accion!=null && accion.equals(ACCION_VER)) {

            System.out.println("Accion ver");

            String idEvento = intent.getStringExtra(EXTRA_IDEVENTO);
            bundle.putString(ACTUAL, intent.getStringExtra(EXTRA_ACTUAL));
            bundle.putString(CAMPO_ID, idEvento);
            NotificationManager notifyMgr = (NotificationManager)
                    AppActivity.getAppContext().getSystemService(NOTIFICATION_SERVICE);
            notifyMgr.cancel(intent.getIntExtra(EXTRA_ID,0));

        }

        if (accion != null && accion.equals(ACCION_VERSORTEO)) {

            System.out.println("Accion ver sorteo");

            String id = intent.getStringExtra(EXTRA_SORTEO);
            String idGanador = intent.getStringExtra(EXTRA_GANADOR);
            bundle.putString(ACTUAL, intent.getStringExtra(EXTRA_ACTUAL));
            bundle.putString(GANADORSORTEO, idGanador);
            bundle.putString(CAMPO_ID, id);
            NotificationManager notifyMgr = (NotificationManager)
                    AppActivity.getAppContext().getSystemService(NOTIFICATION_SERVICE);
            notifyMgr.cancel(intent.getIntExtra(EXTRA_ID, 0));

        }

    }

    @Override
    public void inicio() {

        SharedPreferences preferences = getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);

        if (SQLiteUtil.checkDataBase(getDatabasePath(this.getString(R.string.app_name) + ".db").getAbsolutePath())
                && preferences.contains(PERFILACTIVO)) {

            Interactor.perfila = preferences.getString(PERFILACTIVO, "Defecto");
            Interactor.prioridad = preferences.getBoolean(PRIORIDAD, true);
            Interactor.diaspasados = preferences.getInt(DIASPASADOS, 20);
            Interactor.diasfuturos = preferences.getInt(DIASFUTUROS, 90);

            Log.d("inicio", "Inicio correcto");

            SharedPreferences persistencia = getSharedPreferences(PERSISTENCIA, MODE_PRIVATE);
            SharedPreferences.Editor editor = persistencia.edit();
            editor.clear();
            editor.apply();

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action barOk item clicks here. The action barOk will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activityBase in AndroidManifest.xml.
        int id = item.getItemId();
        bundle = new Bundle();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }else if (id == R.id.action_info) {
            bundle.putString(ACTUAL, INICIO);
            recargarFragment();
            return true;
        }else if (id == R.id.action_help) {
            bundle.putString(WEB, ayudaWeb);
            enviarBundleAFragment(bundle, new FragmentWebView());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setOnNavigation(MenuItem item) {
        // Handle navigation view item clicks here.
        int itemId = item.getItemId();

        bundle.putString(CAMPO_ID, null);
        bundle.putString(MODELO, null);
        bundle.putInt(CAMPO_SECUENCIA, 0);
        bundle.putString(ORIGEN, INICIO);
        bundle.putString(ACTUAL, null);
        bundle.putString(ACTUALTEMP, null);
        bundle.putBoolean(PERSISTENCIA,true);

        if (itemId == R.id.nav_clientes) {

            bundle.putString(ACTUAL, PROVEEDOR);
            recargarFragment();

        }else if (itemId == R.id.nav_agenda) {

            bundle.putString(ACTUAL, INICIO);
            recargarFragment();

        }else if (itemId == R.id.nav_perfiles) {

            bundle.putString(ACTUAL, PERFIL);
            recargarFragment();

        }else if (itemId == R.id.nav_tarea) {

            bundle.putString(ACTUAL, CONFIGURACION);
            recargarFragment();

        }else if (itemId == R.id.nav_producto) {

            bundle.putString(ACTUAL, PRODUCTO);
            recargarFragment();

        }


    }

    protected void recargarFragment(){

        super.recargarFragment();


        switch (bundle.getString(ACTUAL, PRODUCTOS)) {



            case CHAT:

                enviarBundleAFragment(bundle, new FragmentChat());
                break;



            case PRODUCTOS:

                bundle.putBoolean(AVISO, true);
                bundle.putString(TITULO, getString(R.string.productos));

                enviarBundleAFragment(bundle, new ListadoProductos());
                break;


            case PRODSORTEOS:

                bundle.putString(TIPO, PRODSORTEOS);
                bundle.putString(PERFIL, SORTEOS);
                bundle.putBoolean(AVISO, true);
                bundle.putString(TITULO, getString(R.string.sorteos));

                enviarBundleAFragment(bundle, new ListadoProductosSorteos());
                break;

            case SORTEO:

                bundle.putString(TIPO, SORTEO);
                bundle.putString(PERFIL, SORTEO);
                bundle.putString(TITULO, getString(R.string.sorteos));

                enviarBundleAFragment(bundle, new AltaProductosSorteos());
                break;

            case INICIO:

                enviarBundleAFragment(bundle, Interactor.fragmentMenuInicio);
                break;

            case CONFIGURACION:

                startActivity(new Intent(this, SettingsActivity.class));
                break;


        }

        System.out.println("Recargado fragment " + bundle.getString(ACTUAL, INICIO));
        System.out.println("bundle = " + bundle);
    }


    protected void checkPermisos() {

        System.out.println("chekeando permisos");

        permisoInternet = CheckPermisos.validarPermisos(this, INTERNET, 100);

    }

}
