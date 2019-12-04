package com.codevsolution.freemarketsapp;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.CheckPermisos;
import com.codevsolution.base.android.MainActivityBase;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.login.LoginActivity;
import com.codevsolution.base.media.VisorPDFEmail;
import com.codevsolution.base.models.DestinosVoz;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.sqlite.SQLiteUtil;
import com.codevsolution.base.web.FragmentWebView;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.codevsolution.freemarketsapp.services.AutoArranquePro;
import com.codevsolution.freemarketsapp.settings.SettingsActivityPro;
import com.codevsolution.freemarketsapp.ui.AltaPerfilesFirebasePro;
import com.codevsolution.freemarketsapp.ui.AltaSorteosCli;
import com.codevsolution.freemarketsapp.ui.AltaSorteosPro;
import com.codevsolution.freemarketsapp.ui.FragmentCRUDAmortizacion;
import com.codevsolution.freemarketsapp.ui.FragmentCRUDCliente;
import com.codevsolution.freemarketsapp.ui.FragmentCRUDEvento;
import com.codevsolution.freemarketsapp.ui.FragmentCRUDGastoFijo;
import com.codevsolution.freemarketsapp.ui.FragmentCRUDPartidaBase;
import com.codevsolution.freemarketsapp.ui.FragmentCRUDPartidaProyecto;
import com.codevsolution.freemarketsapp.ui.FragmentCRUDPerfil;
import com.codevsolution.freemarketsapp.ui.FragmentCRUDProducto;
import com.codevsolution.freemarketsapp.ui.FragmentCRUDProyecto;
import com.codevsolution.freemarketsapp.ui.FragmentCRUDTrabajo;
import com.codevsolution.freemarketsapp.ui.ListadoProductosCli;
import com.codevsolution.freemarketsapp.ui.ListadoProductosPro;
import com.codevsolution.freemarketsapp.ui.ListadoSorteosCli;
import com.codevsolution.freemarketsapp.ui.ListadoSorteosPro;
import com.codevsolution.freemarketsapp.ui.ListadosPerfilesFirebasePro;
import com.codevsolution.freemarketsapp.ui.MenuInicio;
import com.codevsolution.freemarketsapp.ui.MisSorteosCli;
import com.codevsolution.freemarketsapp.ui.MisSorteosPro;
import com.codevsolution.freemarketsapp.ui.MisSuscripcionesPro;
import com.codevsolution.freemarketsapp.ui.MisSuscripcionesProductosCli;
import com.codevsolution.freemarketsapp.ui.MisSuscripcionesProductosPro;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static android.Manifest.permission.INTERNET;

public class MainActivity extends MainActivityBase implements Interactor.ConstantesPry, ContratoPry.Tablas {


    @Override
    protected void acciones() {
        super.acciones();

        AndroidUtil.setSharePreference(this, PREFERENCIAS, PERFILUSER, PRO);

        if (accion != null && accion.equals(ACCION_VER)) {

            System.out.println("Accion ver");

            String idEvento = intent.getStringExtra(EXTRA_IDEVENTO);
            ModeloSQL evento = CRUDutil.updateModelo(CAMPOS_EVENTO, idEvento);
            CRUDutil.actualizarCampo(evento, EVENTO_NOTIFICADO, 1);
            bundle.putString(ACTUAL, intent.getStringExtra(EXTRA_ACTUAL));
            bundle.putString(CAMPO_ID, idEvento);
            NotificationManager notifyMgr = (NotificationManager)
                    AppActivity.getAppContext().getSystemService(NOTIFICATION_SERVICE);
            notifyMgr.cancel(intent.getIntExtra(EXTRA_ID, 0));

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

        fabInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarBundleAFragment(bundle, Interactor.fragmentMenuInicio);
            }
        });

    }

    @Override
    public void inicio(int inicio) {

        if (inicio == 1) {

            AutoArranquePro.scheduleJob(AppActivity.getAppContext());

        }

        SharedPreferences preferences = getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);

        if (comprobarInicio()) {

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

        } else {
            iniciarDB();
        }

    }

    private Boolean comprobarInicio() {

        String BASEDATOS = AppActivity.getAppContext().getString(R.string.app_name) + ".db";

        System.out.println("PathBD = " + getDatabasePath(BASEDATOS));
        if (!SQLiteUtil.checkDataBase(getDatabasePath(BASEDATOS).getAbsolutePath())) {

            return false;

        } else {

            SQLiteDatabase db = SQLiteDatabase.openDatabase(getDatabasePath(BASEDATOS).getAbsolutePath(),
                    null, SQLiteDatabase.OPEN_READONLY);
            if (SQLiteUtil.isTableExists(TABLA_PERFIL, db)) {

                db.close();
                ListaModeloSQL listaModeloSQL = CRUDutil.setListaModelo(CAMPOS_PERFIL);
                return listaModeloSQL.getLista().size() > 0 && listaModeloSQL.getLista().get(0).getString(PERFIL_NOMBRE).equals("Defecto");
            }
        }

        return false;
    }

    private boolean iniciarDB() {

        try {

            if (!comprobarInicio()) {

                SharedPreferences preferences = getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();

                ContentValues valoresPer = new ContentValues();
                String [] campos = CAMPOS_PERFIL;

                ConsultaBD.putDato(valoresPer, campos, PERFIL_NOMBRE, "Defecto");
                ConsultaBD.putDato(valoresPer, campos, PERFIL_DESCRIPCION,
                        "Perfil por defecto, jornada normal de 8 horas diarias" +
                                " de lunes a viernes y 30 dias de vacaciones al año, " +
                                " y un sueldo anual de " + JavaUtil.formatoMonedaLocal(20000));
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAIMLUNES,9*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAFMLUNES,14*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAITLUNES,16*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAFTLUNES,20*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAIMMARTES,9*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAFMMARTES,14*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAITMARTES,16*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAFTMARTES,20*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAIMMIERCOLES,9*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAFMMIERCOLES,14*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAITMIERCOLES,16*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAFTMIERCOLES,20*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAIMJUEVES,9*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAFMJUEVES,14*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAITJUEVES,16*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAFTJUEVES,20*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAIMVIERNES,9*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAFMVIERNES,14*HORASLONG);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAITVIERNES,-1);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAFTVIERNES,-1);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAIMSABADO,-1);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAFMSABADO,-1);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAITSABADO,-1);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAFTSABADO,-1);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAIMDOMINGO,-1);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAFMDOMINGO,-1);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAITDOMINGO,-1);
                ConsultaBD.putDato(valoresPer, campos,PERFIL_HORAFTDOMINGO,-1);


                Uri reg = ConsultaBD.insertRegistro(TABLA_PERFIL, valoresPer);
                System.out.println(reg);

                editor.putString(PERFILACTIVO, "Defecto");
                editor.putBoolean(PRIORIDAD, true);
                editor.putInt(DIASPASADOS, 20);
                editor.putInt(DIASFUTUROS, 90);
                editor.apply();

                Interactor.prioridad = true;
                Interactor.perfila = "Defecto";
                Interactor.diasfuturos = 90;
                Interactor.diaspasados = 20;
                Interactor.hora = Interactor.Calculos.calculoPrecioHora();
                Interactor.setNamefdef();

                return true;
            }

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("error al crear base");

        }

        return false;

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
            startActivity(new Intent(this, SettingsActivityPro.class));
            return true;
        } else if (id == R.id.action_info) {
            bundle.putString(ACTUAL, INICIO);
            recargarFragment();
            return true;
        } else if (id == R.id.action_help) {
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
        bundle.putBoolean(PERSISTENCIA, true);

        if (itemId == R.id.nav_mi_perfil) {

            bundle.putString(ACTUAL, MIPERFIL);
            recargarFragment();

        } else if (itemId == R.id.nav_mis_sorteos_cli) {

            bundle.putString(ACTUAL, MISSORTEOSCLI);
            recargarFragment();

        } else if (itemId == R.id.nav_mis_sorteos_pro) {

            bundle.putString(ACTUAL, MISSORTEOSPRO);
            recargarFragment();

        } else if (itemId == R.id.nav_inicio) {

            bundle.putString(ACTUAL, INICIO);
            recargarFragment();

        } else if (itemId == R.id.nav_siguiendo_prodcli) {

            bundle.putString(ACTUAL, SIGUIENDOPRODCLI);
            recargarFragment();

        } else if (itemId == R.id.nav_siguiendo_prodpro) {

            bundle.putString(ACTUAL, SIGUIENDOPRODPRO);
            recargarFragment();

        } else if (itemId == R.id.nav_siguiendo_pro) {

            bundle.putString(ACTUAL, SIGUIENDOPRO);
            recargarFragment();

        } else if (itemId == R.id.nav_salir) {

            bundle.putString(ACTUAL, SALIR);
            recargarFragment();

        }


    }

    @Override
    protected void setPathImagenPerfil() {
        super.setPathImagenPerfil();
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

    @Override
    protected void setPathAyuda() {
        super.setPathAyuda();

        pathAyuda = HTTPAYUDA;
    }

    public void seleccionarDestino(String destino) {

        ArrayList<DestinosVoz> listaDestinos = Interactor.getListaDestinosVoz();

        for (DestinosVoz destinosVoz : listaDestinos) {

            if (destino.equals(destinosVoz.getDestino())) {
                enviarBundleAFragment(null, destinosVoz.getFragment());
            }
        }


    }

    public void seleccionarNuevoDestino(String destino) {

        ArrayList<DestinosVoz> listaDestinos = Interactor.getListaNuevosDestinosVoz();

        for (DestinosVoz destinosVoz : listaDestinos) {

            if (destino.equals(destinosVoz.getDestino())) {
                enviarBundleAFragment(null, destinosVoz.getFragment());
            }
        }


    }

    @Override
    protected void recuperarPersistencia() {

        SharedPreferences persistencia = getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
        bundle.putString(CAMPO_ID, persistencia.getString(CAMPO_ID, null));
        bundle.putString(ACTUAL, persistencia.getString(ACTUAL, INICIO));
        bundle.putInt(CAMPO_SECUENCIA, persistencia.getInt(CAMPO_SECUENCIA, 0));
        bundle.putString(IDREL, persistencia.getString(IDREL, null));
        bundle.putBoolean(NUEVOREGISTRO, persistencia.getBoolean(NUEVOREGISTRO, false));
        bundle.putString(CAMPO_RUTAFOTO, persistencia.getString(CAMPO_RUTAFOTO, null));
        bundle.putString(PATH, persistencia.getString(PATH, null));
        bundle.putBoolean(CAMBIO, persistencia.getBoolean(CAMBIO, false));
        AndroidUtil.setSharePreference(this, PERSISTENCIA, CAMBIO, false);

        System.out.println("bundle = " + bundle);
    }

    protected void recargarFragment() {

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



            case SORTEOPRO:

                enviarBundleAFragment(bundle, new AltaSorteosPro());
                break;

            case SORTEOCLI:

                enviarBundleAFragment(bundle, new AltaSorteosCli());
                break;

            case MISSORTEOSPRO:

                enviarBundleAFragment(bundle, new MisSorteosPro());
                break;

            case MISSORTEOSCLI:

                enviarBundleAFragment(bundle, new MisSorteosCli());
                break;

            case SIGUIENDOPRODCLI:

                enviarBundleAFragment(bundle, new MisSuscripcionesProductosCli());
                break;

            case SIGUIENDOPRODPRO:

                enviarBundleAFragment(bundle, new MisSuscripcionesProductosPro());
                break;

            case SIGUIENDOPRO:

                enviarBundleAFragment(bundle, new MisSuscripcionesPro());
                break;


            case MIPERFIL:

                enviarBundleAFragment(bundle, new AltaPerfilesFirebasePro());
                break;

            case SALIR:

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);

            case CHAT + PRO:

                bundle.putBoolean(AVISO, true);
                enviarBundleAFragment(bundle, new ListadosPerfilesFirebasePro());
                break;

            case CHAT + PRODUCTOCLI:

                bundle.putBoolean(AVISO, true);
                enviarBundleAFragment(bundle, new ListadoProductosCli());
                break;

            case CHAT + PRODUCTOPRO:

                bundle.putBoolean(AVISO, true);
                enviarBundleAFragment(bundle, new ListadoProductosPro());
                break;

            case CHAT + SORTEOCLI:

                bundle.putBoolean(AVISO, true);
                enviarBundleAFragment(bundle, new ListadoSorteosCli());
                break;

            case CHAT + SORTEOPRO:

                bundle.putBoolean(AVISO, true);
                enviarBundleAFragment(bundle, new ListadoSorteosPro());
                break;


        }

        System.out.println("Recargado fragment " + bundle.getString(ACTUAL, INICIO));
        System.out.println("bundle = " + bundle);

    }


    protected void checkPermisos() {

        System.out.println("chekeando permisos iniciales");

        permisoInternet = CheckPermisos.validarPermisos(this, INTERNET, 100);
        System.out.println("permisoInternet = " + permisoInternet);

    }

}
