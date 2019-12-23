package com.codevsolution.freemarketsapp;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.CheckPermisos;
import com.codevsolution.base.android.MainActivityBase;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.encrypt.EncryptUtil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.login.LoginActivity;
import com.codevsolution.base.models.DestinosVoz;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.sqlite.ContratoSystem;
import com.codevsolution.base.sqlite.SQLiteUtil;
import com.codevsolution.base.web.FragmentWebView;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.codevsolution.freemarketsapp.services.AutoArranquePro;
import com.codevsolution.freemarketsapp.settings.Preferencias;
import com.codevsolution.freemarketsapp.ui.ListadoProductosCli;
import com.codevsolution.freemarketsapp.ui.ListadoProductosPro;
import com.codevsolution.freemarketsapp.ui.ListadoSorteosCli;
import com.codevsolution.freemarketsapp.ui.ListadoSorteosPro;
import com.codevsolution.freemarketsapp.ui.ListadosPerfilesFirebaseCli;
import com.codevsolution.freemarketsapp.ui.ListadosPerfilesFirebasePro;
import com.codevsolution.freemarketsapp.ui.MenuInicio;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.UUID;

import static android.Manifest.permission.INTERNET;
import static com.codevsolution.base.sqlite.ContratoSystem.Tablas.CAMPOS_USERS;
import static com.codevsolution.base.sqlite.ContratoSystem.Tablas.TABLA_USERS;
import static com.codevsolution.base.sqlite.ContratoSystem.Tablas.USERS_USERID;
import static com.codevsolution.base.sqlite.ContratoSystem.Tablas.USERS_USERIDCODE;

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

    protected void setIdUserCode() {
        String pathDb = Environment.getDataDirectory().getPath() + "/data/"
                + AppActivity.getPackage(context) + "/databases/" + idUser + "/";

        String BASEDATOS = SYSTEM + idUser + ".db";

        if (!SQLiteUtil.checkDataBase(pathDb + BASEDATOS)) {

            String id = UUID.randomUUID().toString();
            AndroidUtil.setSharePreference(context, USERID, USERIDCODE, id);
            idUserCode = id;

            ContentValues values = new ContentValues();
            ConsultaBD.putDato(values, USERS_USERID, idUser);
            ConsultaBD.putDato(values, USERS_USERIDCODE, id);
            ConsultaBD.insertRegistro(TABLA_USERS, values);

        } else {

            ModeloSQL user = ConsultaBD.queryObject(CAMPOS_USERS, USERS_USERID, idUser);
            idUserCode = EncryptUtil.decodificaStr(user.getString(USERS_USERIDCODE));
            AndroidUtil.setSharePreference(context, USERID, USERIDCODE, idUserCode);
        }
    }

    @Override
    public void inicio(int inicio) {

        setIdUserCode();

        if (inicio == 1) {

            AutoArranquePro.scheduleJob(AppActivity.getAppContext());

        }

        SharedPreferences preferences = getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);

        if (comprobarInicio()) {

            Interactor.perfila = preferences.getString(PERFILACTIVO, "Defecto");
            Interactor.prioridad = preferences.getBoolean(PRIORIDAD, true);
            Interactor.diaspasados = preferences.getInt(DIASPASADOS, 20);
            Interactor.diasfuturos = preferences.getInt(DIASFUTUROS, 90);
            //Interactor.hora = Interactor.Calculos.calculoPrecioHora();
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

    @Override
    protected void onOkPass() {
        super.onOkPass();

        EncryptUtil.cifrarBase(ContratoPry.obtenerListaCampos());
        EncryptUtil.cifrarBase(ContratoSystem.obtenerListaCampos());
    }

    private Boolean comprobarInicio() {

        String pathDb = Environment.getDataDirectory().getPath() + "/data/"
                + AppActivity.getPackage(context) + "/databases/" + idUserCode + "/";

        String BASEDATOS = context.getString(R.string.app_name) + idUserCode + ".db";

        if (!SQLiteUtil.checkDataBase(pathDb + BASEDATOS)) {

            return false;

        } else {

            SQLiteDatabase db = SQLiteDatabase.openDatabase(pathDb + BASEDATOS,
                    null, SQLiteDatabase.OPEN_READONLY);
            if (SQLiteUtil.isTableExists(TABLA_PERFIL, db)) {

                db.close();
                return true;
            }
        }

        return false;
    }

    private boolean iniciarDB() {

        try {

            if (!comprobarInicio()) {

                SharedPreferences preferences = AndroidUtil.openSharePreference(context, PREFERENCIAS);

                SharedPreferences.Editor editor = preferences.edit();

                ContentValues valoresPer = new ContentValues();

                ConsultaBD.putDato(valoresPer, PERFIL_NOMBRE, "Defecto");
                ConsultaBD.putDato(valoresPer, PERFIL_DESCRIPCION,
                        "Perfil por defecto, jornada normal de 8 horas diarias" +
                                " de lunes a viernes y 30 dias de vacaciones al año, " +
                                " y un sueldo anual de " + JavaUtil.formatoMonedaLocal(20000));
                ConsultaBD.putDato(valoresPer,PERFIL_HORAIMLUNES,9*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAFMLUNES,14*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAITLUNES,16*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAFTLUNES,20*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAIMMARTES,9*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAFMMARTES,14*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAITMARTES,16*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAFTMARTES,20*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAIMMIERCOLES,9*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAFMMIERCOLES,14*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAITMIERCOLES,16*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAFTMIERCOLES,20*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAIMJUEVES,9*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAFMJUEVES,14*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAITJUEVES,16*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAFTJUEVES,20*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAIMVIERNES,9*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAFMVIERNES,14*HORASLONG);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAITVIERNES,-1);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAFTVIERNES,-1);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAIMSABADO,-1);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAFMSABADO,-1);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAITSABADO,-1);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAFTSABADO,-1);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAIMDOMINGO,-1);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAFMDOMINGO,-1);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAITDOMINGO,-1);
                ConsultaBD.putDato(valoresPer,PERFIL_HORAFTDOMINGO,-1);
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
                //Interactor.hora = Interactor.Calculos.calculoPrecioHora();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //startActivity(new Intent(this, SettingsActivityPro.class));
            enviarBundleAFragment(bundle, new Preferencias());
            return true;
        } else if (id == R.id.action_info) {
            bundle = new Bundle();
            bundle.putString(ACTUAL, INICIO);
            recargarFragment();
            return true;
        } else if (id == R.id.action_help) {
            setPathAyuda();
            if (ayudaWeb!=null){
                ayudaWeb = pathAyuda+ayudaWeb+"/";
            }else{
                ayudaWeb = pathAyuda;
            }
            if (JavaUtil.isValidURL(ayudaWeb)) {
                bundle = new Bundle();
                bundle.putString(WEB, ayudaWeb);
                enviarBundleAFragment(bundle, new FragmentWebView());
            }
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

        SharedPreferences persistencia = AndroidUtil.openSharePreference(context, PERSISTENCIA);
        bundle.putString(CAMPO_ID, persistencia.getString(CAMPO_ID, null));
        bundle.putString(ACTUAL, persistencia.getString(ACTUAL, INICIO));
        bundle.putInt(CAMPO_SECUENCIA, persistencia.getInt(CAMPO_SECUENCIA, 0));
        bundle.putString(IDREL, persistencia.getString(IDREL, null));
        bundle.putBoolean(NUEVOREGISTRO, persistencia.getBoolean(NUEVOREGISTRO, false));
        bundle.putString(CAMPO_RUTAFOTO, persistencia.getString(CAMPO_RUTAFOTO, null));
        bundle.putString(PATH, persistencia.getString(PATH, null));
        bundle.putBoolean(CAMBIO, persistencia.getBoolean(CAMBIO, false));
        bundle.putString(TAGPERS, persistencia.getString(TAGPERS, MenuInicio.class.getName()));
        AndroidUtil.setSharePreference(this, PERSISTENCIA, CAMBIO, false);
        System.out.println("bundle pers= " + bundle);


    }

    protected void recargarFragment() {

        super.recargarFragment();

        System.out.println("bundle.getString(TAGPERS, INICIO) = " + bundle.getString(TAGPERS, MenuInicio.class.getName()));

        String string = bundle.getString(TAGPERS, MenuInicio.class.getName());
        String actual = bundle.getString(ACTUAL, NULL);


        if (SALIR.equals(actual)) {
            AndroidUtil.setSharePreference(context, PREFERENCIAS, PASSOK, NULL);
            AndroidUtil.setSharePreference(context, USERID, USERID, NULL);
            AndroidUtil.setSharePreference(context, USERID, USERIDCODE, NULL);
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if ((CHAT + CLI).equals(actual)) {
            bundle.putBoolean(AVISO, true);
            enviarBundleAFragment(bundle, new ListadosPerfilesFirebaseCli());
        } else if ((CHAT + PRO).equals(actual)) {
            bundle.putBoolean(AVISO, true);
            enviarBundleAFragment(bundle, new ListadosPerfilesFirebasePro());
        } else if ((CHAT + PRODUCTOCLI).equals(actual)) {
            bundle.putBoolean(AVISO, true);
            enviarBundleAFragment(bundle, new ListadoProductosCli());
        } else if ((CHAT + PRODUCTOPRO).equals(actual)) {
            bundle.putBoolean(AVISO, true);
            enviarBundleAFragment(bundle, new ListadoProductosPro());
        } else if ((CHAT + SORTEOCLI).equals(actual)) {
            bundle.putBoolean(AVISO, true);
            enviarBundleAFragment(bundle, new ListadoSorteosCli());
        } else if ((CHAT + SORTEOPRO).equals(actual)) {
            bundle.putBoolean(AVISO, true);
            enviarBundleAFragment(bundle, new ListadoSorteosPro());
        } else {
            try {
                Class FragmentClass = Class.forName(string);
                if (FragmentClass.newInstance() instanceof Fragment) {
                    enviarBundleAFragment(bundle, (Fragment) FragmentClass.newInstance());
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                fabVoz.show();
                fabNuevo.hide();
                enviarBundleAFragment(bundle, new MenuInicio());
            }
        }


    }


    protected void checkPermisos() {

        permisoInternet = CheckPermisos.validarPermisos(this, INTERNET, 100);

    }

}
