package com.jjlacode.freelanceproject;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.jjlacode.base.util.android.AppActivity;
import com.jjlacode.base.util.android.CheckPermisos;
import com.jjlacode.base.util.android.MainActivityBase;
import com.jjlacode.base.util.media.VisorPDFEmail;
import com.jjlacode.base.util.sqlite.SQLiteUtil;
import com.jjlacode.base.util.web.FragmentWebView;
import com.jjlacode.freelanceproject.logica.Interactor;
import com.jjlacode.freelanceproject.settings.SettingsActivity;
import com.jjlacode.freelanceproject.ui.AltaProductosSorteos;
import com.jjlacode.freelanceproject.ui.FragmentCRUDAmortizacion;
import com.jjlacode.freelanceproject.ui.FragmentCRUDCliente;
import com.jjlacode.freelanceproject.ui.FragmentCRUDEvento;
import com.jjlacode.freelanceproject.ui.FragmentCRUDGastoFijo;
import com.jjlacode.freelanceproject.ui.FragmentCRUDPartidaBase;
import com.jjlacode.freelanceproject.ui.FragmentCRUDPartidaProyecto;
import com.jjlacode.freelanceproject.ui.FragmentCRUDPerfil;
import com.jjlacode.freelanceproject.ui.FragmentCRUDProducto;
import com.jjlacode.freelanceproject.ui.FragmentCRUDProyecto;
import com.jjlacode.freelanceproject.ui.FragmentCRUDTrabajo;
import com.jjlacode.freelanceproject.ui.FragmentChat;
import com.jjlacode.freelanceproject.ui.ListadoProductos;
import com.jjlacode.freelanceproject.ui.ListadoProductosFreelance;
import com.jjlacode.freelanceproject.ui.ListadoProductosSorteos;
import com.jjlacode.freelanceproject.ui.ListadosProductosUsados;
import com.jjlacode.freelanceproject.ui.MenuInicio;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_CONTACTS;

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
            Interactor.hora = Interactor.Calculos.calculoPrecioHora();
            Interactor.setNamefdef();

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

            bundle.putString(ACTUAL, CLIENTE);
            recargarFragment();

        }
        else if (itemId == R.id.nav_proyectos) {

            bundle.putString(ACTUAL, PROYECTO);
            recargarFragment();

        } else if (itemId == R.id.nav_agenda) {

            bundle.putString(ACTUAL, INICIO);
            recargarFragment();

        }  else if (itemId == R.id.nav_perfiles) {

            bundle.putString(ACTUAL, PERFIL);
            recargarFragment();

        }else if (itemId == R.id.nav_eventos) {

            bundle.putString(ACTUAL, EVENTO);
            recargarFragment();

        }else if (itemId == R.id.nav_amortizacion) {

            bundle.putString(ACTUAL, AMORTIZACION);
            recargarFragment();

        }else if (itemId == R.id.nav_gastosfijos) {

            bundle.putString(ACTUAL, GASTOSFIJOS);
            recargarFragment();

        }else if (itemId == R.id.nav_partidabase) {

            bundle.putString(ACTUAL, PARTIDABASE);
            recargarFragment();

        }else if (itemId == R.id.nav_tarea) {

            bundle.putString(ACTUAL, TAREA);
            recargarFragment();

        }else if (itemId == R.id.nav_producto) {

            bundle.putString(ACTUAL, PRODUCTO);
            recargarFragment();

        }


    }

    protected void recargarFragment(){

        super.recargarFragment();


        switch (bundle.getString(ACTUAL, INICIO)) {

            case PROYECTO:

            case COBROS:

            case HISTORICO:

            case PRESUPUESTO:

                enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
                break;

            case PARTIDA:

                enviarBundleAFragment(bundle, new FragmentCRUDPartidaProyecto());
                break;

            case PROSPECTO:

            case CLIENTE:

                enviarBundleAFragment(bundle, new FragmentCRUDCliente());
                break;

            case INICIO:

                fabVoz.show();
                fabNuevo.hide();
                toolbar.setSubtitle(Interactor.setNamefdef());
                enviarBundleAFragment(bundle, new MenuInicio());
                break;
            case AMORTIZACION:

                enviarBundleAFragment(bundle, new FragmentCRUDAmortizacion());
                break;

            case PERFILTR:

                enviarBundleAFragment(bundle, new FragmentCRUDPerfil());
                break;

            case GASTOSFIJOS:

                enviarBundleAFragment(bundle, new FragmentCRUDGastoFijo());
                break;

            case EVENTO:
                enviarBundleAFragment(bundle, new FragmentCRUDEvento());
                break;

            case PARTIDABASE:
                enviarBundleAFragment(bundle, new FragmentCRUDPartidaBase());
                break;

            case VISORPDFMAIL:
                enviarBundleAFragment(bundle, new VisorPDFEmail());
                break;

            case TAREA:
                enviarBundleAFragment(bundle, new FragmentCRUDTrabajo());
                break;

            case PRODUCTO:
                enviarBundleAFragment(bundle, new FragmentCRUDProducto());
                break;

            case CHAT:

                enviarBundleAFragment(bundle, new FragmentChat());
                break;

            case PRODFREELANCE:

                bundle.putString(TIPO, PRODFREELANCE);
                bundle.putString(PERFIL, FREELANCE);
                bundle.putBoolean(AVISO, true);
                bundle.putString(TITULO, getString(R.string.servicios_freelance));

                enviarBundleAFragment(bundle, new ListadoProductosFreelance());
                break;

            case PRODPROVCAT:

                bundle.putString(TIPO, PRODPROVCAT);
                bundle.putString(PERFIL, PROVEEDORWEB);
                bundle.putBoolean(AVISO, true);
                bundle.putString(TITULO, getString(R.string.productos_proveedor));

                enviarBundleAFragment(bundle, new ListadoProductos());
                break;

            case PRODCOMERCIAL:

                bundle.putString(TIPO, PRODCOMERCIAL);
                bundle.putString(PERFIL, COMERCIAL);
                bundle.putBoolean(AVISO, true);
                bundle.putString(TITULO, getString(R.string.productos_comercial));

                enviarBundleAFragment(bundle, new ListadoProductos());
                break;

            case PRODECOMMERCE:

                bundle.putString(TIPO, PRODECOMMERCE);
                bundle.putString(PERFIL, ECOMMERCE);
                bundle.putBoolean(AVISO, true);
                bundle.putString(TITULO, getString(R.string.productos_ecommerce));

                enviarBundleAFragment(bundle, new ListadoProductos());
                break;

            case PRODLUGAR:

                bundle.putString(TIPO, PRODLUGAR);
                bundle.putString(PERFIL, LUGAR);
                bundle.putBoolean(AVISO, true);
                bundle.putString(TITULO, getString(R.string.productos_lugar));

                enviarBundleAFragment(bundle, new ListadoProductos());
                break;

            case PRODSORTEOS:

                bundle.putString(TIPO, PRODSORTEOS);
                bundle.putString(PERFIL, SORTEOS);
                bundle.putBoolean(AVISO, true);
                bundle.putString(TITULO, getString(R.string.sorteos));

                enviarBundleAFragment(bundle, new ListadoProductosSorteos());
                break;

            case USADO:

                bundle.putString(TIPO, USADO);
                bundle.putString(PERFIL, CLIENTEWEB);
                bundle.putBoolean(AVISO, true);
                bundle.putString(TITULO, getString(R.string.productos_usados));

                enviarBundleAFragment(bundle, new ListadosProductosUsados());
                break;

            case SORTEO:

                bundle.putString(TIPO, SORTEO);
                bundle.putString(PERFIL, SORTEO);
                bundle.putString(TITULO, getString(R.string.sorteos));

                enviarBundleAFragment(bundle, new AltaProductosSorteos());
                break;


        }

        System.out.println("Recargado fragment " + bundle.getString(ACTUAL, INICIO));
        System.out.println("bundle = " + bundle);
    }


    protected void checkPermisos() {

        System.out.println("chekeando permisos");

        permisoBoot = CheckPermisos.validarPermisos(this, RECEIVE_BOOT_COMPLETED, 100);
        permisoRecordAudio = CheckPermisos.validarPermisos(this, RECORD_AUDIO, 100);
        permisoInternet = CheckPermisos.validarPermisos(this, INTERNET, 100);
        permisoReadCont = CheckPermisos.validarPermisos(this, READ_CONTACTS, 100);
        permisoWriteCont = CheckPermisos.validarPermisos(this, WRITE_CONTACTS, 100);
        permisoCall = CheckPermisos.validarPermisos(this, CALL_PHONE, 100);

    }

}
