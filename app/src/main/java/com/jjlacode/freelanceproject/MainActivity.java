package com.jjlacode.freelanceproject;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.jjlacode.base.util.android.AppActivity;
import com.jjlacode.base.util.android.CheckPermisos;
import com.jjlacode.base.util.android.MainActivityBase;
import com.jjlacode.base.util.media.VisorPDFEmail;
import com.jjlacode.base.util.services.AutoArranque;
import com.jjlacode.base.util.sqlite.SQLiteUtil;
import com.jjlacode.base.util.web.FragmentWebView;
import com.jjlacode.freelanceproject.logica.Interactor;
import com.jjlacode.freelanceproject.settings.SettingsActivity;
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
import com.jjlacode.freelanceproject.ui.MenuInicio;
import com.jjlacode.um.base.ui.FragmentChat;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends MainActivityBase {

    private static final int PERIOD_MS = 5000;
    public boolean permisoBoot;
    public boolean permisoImagen;
    public boolean permisoReadExt;
    public boolean permisoWriteExt;
    public boolean permisoCamara;
    public boolean permisoInternet;
    public boolean permisoWriteCont;
    public boolean permisoReadCont;
    public boolean permisoRecordAudio;
    public boolean permisoCall;


    @Override
    protected void acciones() {
        super.acciones();

        validarPermisos();

        Intent intent = getIntent();

        System.out.println("inicio = " + intent.getIntExtra(INICIO, 0));

        if (intent.getIntExtra(INICIO,0)==0){

            inicio();

        }else if (intent.getIntExtra(INICIO,0)==1){

            AutoArranque.scheduleJob(AppActivity.getAppContext());

            inicio();

        } else if (intent.getIntExtra(INICIO, 0) == 2) {

            inicio();
            ayudaWeb = HTTPAYUDA + "Bienvenida";
            bundle.putString(WEB, ayudaWeb);
            enviarBundleAFragment(bundle, new FragmentWebView());
        }


            String accion = intent.getAction();


        if (accion!=null && accion.equals(ACCION_VER)) {

            System.out.println("Accion ver");

            String idEvento = intent.getStringExtra(EXTRA_IDEVENTO);
            bundle.putString(ACTUAL, intent.getStringExtra(EXTRA_ACTUAL));
            bundle.putString(CAMPO_ID, idEvento);
            NotificationManager notifyMgr = (NotificationManager)
                    AppActivity.getAppContext().getSystemService(NOTIFICATION_SERVICE);
            notifyMgr.cancel(intent.getIntExtra(EXTRA_ID,0));

        } else if (accion != null && accion.equals(ACCION_VERCHAT)) {

            System.out.println("Accion ver chat");

            String idChat = intent.getStringExtra(EXTRA_IDCHAT);
            int secChat = intent.getIntExtra(EXTRA_SECCHAT, 0);
            String tipoChat = intent.getStringExtra(EXTRA_TIPOCHAT);
            String tipoChatRetorno = intent.getStringExtra(EXTRA_TIPOCHATRETORNO);
            bundle.putString(ACTUAL, intent.getStringExtra(EXTRA_ACTUAL));
            bundle.putString(CAMPO_ID, idChat);
            bundle.putInt(CAMPO_SECUENCIA, secChat);
            bundle.putString(CAMPO_TIPO, tipoChat);
            bundle.putString(CAMPO_TIPORETORNO, tipoChatRetorno);
            NotificationManager notifyMgr = (NotificationManager)
                    AppActivity.getAppContext().getSystemService(NOTIFICATION_SERVICE);
            notifyMgr.cancel(intent.getIntExtra(EXTRA_ID, 0));

        }

    }

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

        String actual = bundle.getString(ACTUAL, INICIO);

        switch (actual){

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

            case PERFIL:

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


        }

        System.out.println("Recargado fragment " + actual);
        System.out.println("bundle = " + bundle);
    }

    private void validarPermisos() {


        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M ){

            Interactor.permiso = true;
        }
        else if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CALL_PHONE)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(INTERNET)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(RECEIVE_BOOT_COMPLETED)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        ){

            Interactor.permiso = true;
        }
        else if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))||
                (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE))||
                (shouldShowRequestPermissionRationale(RECEIVE_BOOT_COMPLETED))||
                (shouldShowRequestPermissionRationale(RECORD_AUDIO))||
                (shouldShowRequestPermissionRationale(INTERNET))||
                (shouldShowRequestPermissionRationale(CAMERA))||
                (shouldShowRequestPermissionRationale(WRITE_CONTACTS))||
                (shouldShowRequestPermissionRationale(READ_CONTACTS))||
                (shouldShowRequestPermissionRationale(CALL_PHONE))){

            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle("Permisos desactivados");
            dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la app");

            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    requestPermissions(new String[]
                            {RECEIVE_BOOT_COMPLETED,READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE,
                                    CAMERA,RECORD_AUDIO,INTERNET,READ_CONTACTS,
                                    WRITE_CONTACTS,CALL_PHONE},100);
                }
            });
            dialogo.show();

        }
        else {

            requestPermissions(new String[]
                    {RECEIVE_BOOT_COMPLETED,READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE,
                            CAMERA,RECORD_AUDIO,INTERNET,READ_CONTACTS,
                            WRITE_CONTACTS,CALL_PHONE},100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100 && grantResults.length==8 && (grantResults[0]==PackageManager.PERMISSION_GRANTED &&
                grantResults[1]==PackageManager.PERMISSION_GRANTED &&
                grantResults[2]==PackageManager.PERMISSION_GRANTED &&
                grantResults[3]==PackageManager.PERMISSION_GRANTED &&
                grantResults[4]==PackageManager.PERMISSION_GRANTED &&
                grantResults[5]==PackageManager.PERMISSION_GRANTED &&
                grantResults[6]==PackageManager.PERMISSION_GRANTED &&
                grantResults[7] == PackageManager.PERMISSION_GRANTED &&
                grantResults[8] == PackageManager.PERMISSION_GRANTED)) {

            Interactor.permiso = true;

        }else{

            Toast.makeText(this,"Debe aceptar todos los permisos para " +
                    "que la app se ejecute correctamente",Toast.LENGTH_LONG).show();
        }
    }

    private void checkPermisos() {

        permisoBoot = CheckPermisos.validarPermisos(this, RECEIVE_BOOT_COMPLETED, 101);
        permisoBoot = CheckPermisos.validarPermisos(this, READ_EXTERNAL_STORAGE, 102);
        permisoBoot = CheckPermisos.validarPermisos(this, WRITE_EXTERNAL_STORAGE, 103);
        permisoBoot = CheckPermisos.validarPermisos(this, CAMERA, 104);
        permisoBoot = CheckPermisos.validarPermisos(this, RECORD_AUDIO, 105);
        permisoBoot = CheckPermisos.validarPermisos(this, INTERNET, 106);
        permisoBoot = CheckPermisos.validarPermisos(this, READ_CONTACTS, 107);
        permisoBoot = CheckPermisos.validarPermisos(this, WRITE_CONTACTS, 108);
        permisoBoot = CheckPermisos.validarPermisos(this, CALL_PHONE, 109);
    }

}
