package jjlacode.com.freelanceproject;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import jjlacode.com.freelanceproject.ui.FragmentAgenda;
import jjlacode.com.freelanceproject.ui.FragmentCRUDEvento;
import jjlacode.com.freelanceproject.ui.FragmentCUDPartidaProyecto;
import jjlacode.com.freelanceproject.ui.FragmentPartidasProyecto;
import jjlacode.com.freelanceproject.util.ICFragmentos;
import jjlacode.com.freelanceproject.util.Modelo;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.ui.FragmentCPartidaProyecto;
import jjlacode.com.freelanceproject.ui.FragmentCRUDAmortizacion;
import jjlacode.com.freelanceproject.ui.FragmentCRUDCliente;
import jjlacode.com.freelanceproject.ui.FragmentCUDGastoFijo;
import jjlacode.com.freelanceproject.ui.FragmentCUDPerfil;
import jjlacode.com.freelanceproject.ui.FragmentGastoFijo;
import jjlacode.com.freelanceproject.ui.FragmentPartidaBase;
import jjlacode.com.freelanceproject.ui.FragmentPerfil;
import jjlacode.com.freelanceproject.ui.FragmentPreferencias;
import jjlacode.com.freelanceproject.ui.FragmentCUDProyecto;
import jjlacode.com.freelanceproject.util.CommonPry;
import jjlacode.com.freelanceproject.util.VisorPDF;
import jjlacode.com.freelanceproject.util.VisorPDFEmail;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static jjlacode.com.freelanceproject.util.CommonPry.namesubdef;
import static jjlacode.com.freelanceproject.util.CommonPry.setNamefdef;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ID;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.MODELO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.NAMEF;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.NAMEFTEMP;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.NAMESUB;
import static jjlacode.com.freelanceproject.util.CommonPry.TiposEvento.EVENTO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.NAMESUBTEMP;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.NUEVOREGISTRO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.SECUENCIA;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ICFragmentos,
        CommonPry.Constantes, ContratoPry.Tablas {

    TextView tituloCabecera ;
    String namef ;
    String nameftemp ;
    Bundle bundle;
    Toolbar toolbar;
    FloatingActionButton fab;

    private String namesub;
    private Modelo modelo;
    private String id;
    private int secuencia;
    private boolean inicio;
    private String namesubtemp;


    @Override
    protected void onResume() {
        super.onResume();
        if (!inicio){
            persitencia();
        }else {
            inicio = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

        }
        return super.onKeyDown(keyCode, event);
    }

    protected void persitencia(){

        SharedPreferences persistencia=getSharedPreferences(PERSISTENCIA, MODE_PRIVATE);
        namef = persistencia.getString(NAMEF,"");
        namesub = persistencia.getString(NAMESUB,"");
        nameftemp = persistencia.getString(NAMEFTEMP,"");
        namesubtemp = persistencia.getString(NAMESUBTEMP,"");
        id = persistencia.getString(ID,"");
        secuencia = persistencia.getInt(SECUENCIA,0);
        recargarFragment();
        System.out.println("persistencia");

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            namef = savedInstanceState.getString(NAMEF);
            namesub = savedInstanceState.getString(NAMESUB);
            nameftemp = savedInstanceState.getString(NAMEFTEMP);
            namesubtemp = savedInstanceState.getString(NAMESUBTEMP);
            modelo = (Modelo) savedInstanceState.getSerializable(MODELO);
            id = savedInstanceState.getString(ID);
            secuencia = savedInstanceState.getInt(SECUENCIA);
            System.out.println("saveinstance");

        }else {

            namef = AGENDA;
            namesub = this.getString(R.string.proximos_eventos);

        }
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inicio = true;

        tituloCabecera = findViewById(R.id.tvmaintitulo);
        tituloCabecera.setVisibility(View.GONE);

        validarPermisos();


        if (namef!=null){
            toolbar.setTitle(namef.toUpperCase());
        }
        if (namesub!=null){
            toolbar.setSubtitle(namesub);
        }


        fab =  findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String accion = "Action";
                fab.hide();


                    if (namef.equals(CLIENTE) ) {

                        namesub = NUEVOCLIENTE;
                        recargarFragment(new FragmentCRUDCliente(),false);

                    }else if (namef.equals(PROSPECTO)) {

                        namesub = NUEVOPROSPECTO;
                        recargarFragment(new FragmentCRUDCliente(),false);

                    } else if (namef.equals(PARTIDA)) {

                        secuencia=0;
                        namesub = NUEVAPARTIDA;
                        recargarFragment(new FragmentCUDPartidaProyecto(),true);

                    }else if (namef.equals(PROYECTO)){

                        namesub = NUEVOPROYECTO;
                        id=null;
                        recargarFragment(new FragmentCUDProyecto(),false);

                    }else if (namef.equals(PRESUPUESTO)){

                        namesub = NUEVOPRESUPUESTO;
                        id=null;
                        recargarFragment(new FragmentCUDProyecto(),false);

                    }else if (namef.equals(AGENDA)) {

                        namesub = NUEVOEVENTO;
                        recargarFragment(new FragmentCRUDEvento(),false);

                    }else if (namef.equals(EVENTO)) {

                        namesub = NUEVOEVENTO;
                        recargarFragment(new FragmentCRUDEvento(),false);

                    }else if (namef.equals(PERFIL)) {

                        namesub = NUEVOPERFIL;
                        recargarFragment(new FragmentCUDPerfil(),false);

                    }else if (namef.equals(AMORTIZACION)) {

                        namesub = NUEVAAMORTIZACION;
                        recargarFragment(new FragmentCRUDAmortizacion(),false);

                    }else if (namef.equals(GASTOSFIJOS)) {

                        namesub = NUEVOGASTOFIJO;
                        recargarFragment(new FragmentCUDGastoFijo(),false);

                    }else if (namef.equals(PARTIDABASE)) {

                        namesub = NUEVAPARTIDABASE;
                        recargarFragment(new FragmentCPartidaProyecto(),false);

                    }

                Snackbar.make(view, trans(namesub,true), Snackbar.LENGTH_SHORT)
                        .setAction(accion, null).show();

                    //namesub = CommonPry.setNamefdef();

               bundle=null;
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recargarFragment();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        //namesub = namesubtemp;
        //namef = nameftemp;
        //if ((!namef.equals(PREFERENCIAS))&&(!namef.equals("visor pdf"))){
            recargarFragment();
        //}
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
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            namef= PREFERENCIAS;
            recargarFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int itemId = item.getItemId();
        id=null;
        modelo=null;
        secuencia=0;

        if (itemId == R.id.nav_clientes) {

            namef= CLIENTE;
            recargarFragment();

        }
        else if (itemId == R.id.nav_proyectos) {

            namef= PROYECTO;
            recargarFragment();

        } else if (itemId == R.id.nav_agenda) {

            namef= AGENDA;
            recargarFragment();

        }  else if (itemId == R.id.nav_perfiles) {

            namef = PERFIL;
            recargarFragment();

        }else if (itemId == R.id.nav_eventos) {

            namef = EVENTO;
            recargarFragment();

        }else if (itemId == R.id.nav_amortizacion) {

            namef = AMORTIZACION;
            recargarFragment();

        }else if (itemId == R.id.nav_gastosfijos) {

            namef = GASTOSFIJOS;
            recargarFragment();

        }else if (itemId == R.id.nav_partidabase) {

            namef = PARTIDABASE;
            recargarFragment();

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void enviarBundleAFragment(Bundle bundle, Fragment myFragment) {

        myFragment.setArguments(bundle);

        this.bundle = bundle;
        nameftemp =namef;
        namesubtemp = namesub;
        namef=null;
        if (bundle.containsKey(NAMEF)) {
            namef = bundle.getString(NAMEF);
            if (namef != null) {
                //tituloCabecera.setText(namef.toUpperCase());
                toolbar.setTitle(trans(namef,true).toUpperCase());
            }
        }
        if (bundle.containsKey(NAMESUB)) {
            namesub = bundle.getString(NAMESUB);
            if (namesub != null) {
                //tituloCabecera.setText(namef.toUpperCase());
                toolbar.setSubtitle(trans(namesub,true));
            }
        }
        modelo = (Modelo) bundle.getSerializable(MODELO);
        id = bundle.getString(ID);
        secuencia = bundle.getInt(SECUENCIA);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_main,myFragment).addToBackStack(null).commit();

    }

    @Override
    public void enviarBundleAActivity(Bundle bundle) {

       this.bundle = bundle;
       nameftemp =namef;
       namesubtemp = namesub;
       namef=null;
       namesub = CommonPry.setNamefdef();
       if (bundle.containsKey(NAMEF)) {
           namef = bundle.getString(NAMEF);
           if (namef != null) {
               toolbar.setTitle(trans(namef,true).toUpperCase());
           }
       }
        if (bundle.containsKey(NAMESUB)) {
            namesub = bundle.getString(NAMESUB);
            if (namesub != null) {
                toolbar.setSubtitle(trans(namesub,true));
            }
        }
       modelo = (Modelo) bundle.getSerializable(MODELO);
       id = bundle.getString(ID);
       secuencia = bundle.getInt(SECUENCIA);
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
                            {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE,CAMERA,CALL_PHONE,INTERNET},100);
                }
            });
            dialogo.show();

        }
        else {

            requestPermissions(new String[]
                    {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE,CAMERA,CALL_PHONE,INTERNET},100);
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
        super.onSaveInstanceState(outState);
        outState.putString(NAMEF,namef);
        outState.putString(NAMESUB,namesub);
        outState.putString(NAMEFTEMP,nameftemp);
        outState.putString(NAMESUBTEMP, namesubtemp);
        outState.putSerializable(MODELO,modelo);
        outState.putString(ID,id);
        outState.putInt(SECUENCIA,secuencia);
        System.out.println("guardado outstate");
    }

    private void recargarFragment(Fragment fragment) {

        bundle = new Bundle();
        bundle.putString(NAMEF, namef);
        bundle.putString(NAMESUB, namesub);
        bundle.putString(NAMEFTEMP,nameftemp);
        bundle.putString(NAMESUBTEMP, namesubtemp);
        bundle.putSerializable(MODELO, modelo);
        bundle.putString(ID, id);
        bundle.putInt(SECUENCIA, secuencia);
        bundle.putBoolean(NUEVOREGISTRO,false);

        enviarBundleAFragment(bundle, fragment);
    }

    private void recargarFragment(Fragment fragment, boolean esDetalle) {

        bundle = new Bundle();
        bundle.putString(NAMEF, namef);
        bundle.putString(NAMESUB, namesub);
        bundle.putString(NAMEFTEMP,nameftemp);
        bundle.putString(NAMESUBTEMP, namesubtemp);
        if (esDetalle) {
            bundle.putString(ID, id);
            bundle.putInt(SECUENCIA, secuencia);
        }
            bundle.putBoolean(NUEVOREGISTRO, true);

        enviarBundleAFragment(bundle, fragment);
    }

    private void recargarFragment(){

        bundle = new Bundle();
        bundle.putString(NAMEF,namef);
        namesub = namesubdef = setNamefdef();
        bundle.putString(NAMESUB,namesub);
        bundle.putString(NAMEFTEMP,nameftemp);
        bundle.putString(NAMESUBTEMP, namesubtemp);
        bundle.putSerializable(MODELO,modelo);
        bundle.putString(ID,id);
        bundle.putInt(SECUENCIA,secuencia);
        bundle.putBoolean(NUEVOREGISTRO, false);

        switch (namef){

            case PROYECTO:

            case COBROS:

            case HISTORICO:

            case PRESUPUESTO:

                enviarBundleAFragment(bundle, new FragmentCUDProyecto());
                break;

            case PARTIDA:

                //namesub = namesubtemp;
                //bundle.putString(NAMESUB,namesub);
                enviarBundleAFragment(bundle, new FragmentCUDPartidaProyecto());
                break;

            case PROSPECTO:

            case CLIENTE:

                enviarBundleAFragment(bundle, new FragmentCRUDCliente());
                break;

            case AGENDA:

                namesub = getString(R.string.proximos_eventos);
                bundle.putString(NAMESUB,namesub);
                enviarBundleAFragment(bundle, new FragmentAgenda());
                break;
            case AMORTIZACION:

                enviarBundleAFragment(bundle, new FragmentCRUDAmortizacion());
                break;

            case PERFIL:

                enviarBundleAFragment(bundle, new FragmentPerfil());
                break;

            case GASTOSFIJOS:

                enviarBundleAFragment(bundle, new FragmentGastoFijo());
                break;

            case EVENTO:
                enviarBundleAFragment(bundle, new FragmentCRUDEvento());
                break;

            case PARTIDABASE:
                enviarBundleAFragment(bundle, new FragmentPartidaBase());
                break;

            case PREFERENCIAS:
                enviarBundleAFragment(bundle, new FragmentPreferencias());
                break;

            case VISORPDFMAIL:
                enviarBundleAFragment(bundle, new VisorPDFEmail());
                break;

            case VISORPDF:
                enviarBundleAFragment(bundle, new VisorPDF());
                break;


        }
        bundle = null;
    }

    private String trans(String name, boolean plural){

        switch (name){

            case PRESUPUESTO:

                if (plural){
                    return PRESUPUESTOS;
                }else{
                    return TPRESUPUESTO;
                }

            case PROYECTO:

                if (plural){
                    return PROYECTOS;
                }else{
                    return TPROYECTO;
                }

            case CLIENTE:

                if (plural){
                    return CLIENTES;
                }else{
                    return TCLIENTE;
                }

            case PROSPECTO:

                if (plural){
                    return PROSPECTOS;
                }else{
                    return TPROSPECTO;
                }

            default:

                return name;
        }
    }

}
