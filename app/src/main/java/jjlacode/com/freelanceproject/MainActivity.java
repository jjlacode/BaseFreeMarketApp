package jjlacode.com.freelanceproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import jjlacode.com.freelanceproject.interfaces.ICFragmentos;
import jjlacode.com.freelanceproject.model.Modelo;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.ui.FragmentAgenda;
import jjlacode.com.freelanceproject.ui.FragmentCCliente;
import jjlacode.com.freelanceproject.ui.FragmentCEvento;
import jjlacode.com.freelanceproject.ui.FragmentCGastoProyecto;
import jjlacode.com.freelanceproject.ui.FragmentCPartidaProyecto;
import jjlacode.com.freelanceproject.ui.FragmentCProyecto;
import jjlacode.com.freelanceproject.ui.FragmentCliente;
import jjlacode.com.freelanceproject.ui.FragmentEvento;
import jjlacode.com.freelanceproject.ui.FragmentPerfil;
import jjlacode.com.freelanceproject.ui.FragmentProyecto;
import jjlacode.com.freelanceproject.ui.FragmentUDPreferencias;
import jjlacode.com.freelanceproject.utilities.Common;
import jjlacode.com.utilidades.Utilidades;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static jjlacode.com.freelanceproject.utilities.Common.TiposEvento.EVENTO;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ICFragmentos,
        Common.Constantes, Contract.Tablas {

    TextView tituloCabecera ;
    String namef ;
    String nameftemp ;
    Bundle bundle;

    public static Context context;
    private String namefsub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        namef = AGENDA;

        nameftemp = AGENDA;
        tituloCabecera = findViewById(R.id.tvmaintitulo);
        tituloCabecera.setText(namef.toUpperCase());

        validarPermisos();
        //String [] args = {CAMERA,READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE};
        //Common.Permisos permisos = new Common.Permisos(context,this,args);
        //permisos.validarPermisos();

        String precioHora= Utilidades.getDecimales(Common.hora);

        if (Common.prioridad>0) {
            toolbar.setSubtitle("Perfil: " + Common.perfila + " - hora= " + precioHora + " € - P");
        }else{

            toolbar.setSubtitle("Perfil: " + Common.perfila + " - hora= " + precioHora + " €");
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if (bundle!=null) {


                    if (namef.equals(CLIENTE) || namef.equals(PROSPECTO)) {

                        bundle = new Bundle();
                        bundle.putString("namef", namef);
                        enviarBundleAFragment(bundle, new FragmentCCliente());
                        bundle = null;
                        Snackbar.make(view, "Nuevo Cliente", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else if (namef.equals(PROYECTO) || namef.equals(PRESUPUESTO)) {


                        if (bundle.getString("namefsub") != null) {

                            namefsub = bundle.getString("namefsub");

                            Modelo proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);

                            if (namefsub.equals(PARTIDA)) {

                                bundle = new Bundle();
                                bundle.putString("namef", namef);
                                bundle.putString("namefsub", PARTIDA);
                                bundle.putSerializable(TABLA_PROYECTO, proyecto);
                                enviarBundleAFragment(bundle, new FragmentCPartidaProyecto());
                                bundle = null;
                                Snackbar.make(view, "Nueva Partida", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            } else if (namefsub.equals(GASTO)) {

                                bundle = new Bundle();
                                bundle.putString("namef", namef);
                                bundle.putString("namefsub", GASTO);
                                bundle.putSerializable(TABLA_PROYECTO, proyecto);
                                enviarBundleAFragment(bundle, new FragmentCGastoProyecto());
                                bundle = null;
                                Snackbar.make(view, "Nuevo Gasto", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }else {
                            bundle = new Bundle();
                            bundle.putString("namef", namef);
                            enviarBundleAFragment(bundle, new FragmentCProyecto());
                            bundle = null;
                            Snackbar.make(view, "Nuevo Proyecto", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                        }
                    }else if (namef.equals(AGENDA) || namef.equals(EVENTO)) {

                        bundle = new Bundle();
                        bundle.putString("namef", namef);
                        enviarBundleAFragment(bundle, new FragmentCEvento());
                        bundle = null;
                        Snackbar.make(view, "Nuevo Evento", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                //}
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        bundle = new Bundle();
        bundle.putString("namef",namef);
        enviarBundleAFragment(bundle, new FragmentAgenda());
        bundle = null;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            tituloCabecera.setText(namef.toUpperCase());
            bundle = new Bundle();
            bundle.putString("namef",namef);
            enviarBundleAFragment(bundle, new FragmentUDPreferencias());
            bundle = null;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        bundle = new Bundle();

        if (id == R.id.nav_prospecto) {

            namef= PROSPECTO;
            tituloCabecera.setText(namef.toUpperCase());
            bundle.putString("namef",namef);
            enviarBundleAFragment(bundle, new FragmentCliente());
            bundle = null;

        }
        else if (id == R.id.nav_presup) {

            namef= PRESUPUESTO;
            tituloCabecera.setText(namef.toUpperCase());
            bundle.putString("namef",namef);
            enviarBundleAFragment(bundle, new FragmentProyecto());
            bundle = null;

        }else if (id == R.id.nav_proyectos) {

            namef= PROYECTO;
            tituloCabecera.setText(namef.toUpperCase());
            bundle.putString("namef",namef);
            enviarBundleAFragment(bundle, new FragmentProyecto());
            bundle = null;

        } else if (id == R.id.nav_cobros) {

            namef= COBROS;
            tituloCabecera.setText(namef.toUpperCase());
            bundle.putString("namef",namef);
            enviarBundleAFragment(bundle, new FragmentProyecto());
            bundle = null;

        } else if (id == R.id.nav_agenda) {

            namef= AGENDA;
            tituloCabecera.setText(namef.toUpperCase());
            bundle.putString("namef",namef);
            enviarBundleAFragment(bundle, new FragmentAgenda());
            bundle = null;

        } else if (id == R.id.nav_historico) {

            namef= HISTORICO;
            tituloCabecera.setText(namef.toUpperCase());
            bundle.putString("namef",namef);
            enviarBundleAFragment(bundle, new FragmentProyecto());
            bundle = null;

        } else if (id == R.id.nav_clientes) {

            namef = CLIENTE;
            tituloCabecera.setText(namef.toUpperCase());
            bundle.putString("namef",namef);
            enviarBundleAFragment(bundle, new FragmentCliente());
            bundle = null;

        } else if (id == R.id.nav_perfiles) {

            namef = PERFIL;
            tituloCabecera.setText(namef.toUpperCase());
            bundle.putString("namef",namef);
            enviarBundleAFragment(bundle, new FragmentPerfil());
            bundle = null;

        }else if (id == R.id.nav_eventos) {

            namef = EVENTO;
            tituloCabecera.setText(namef.toUpperCase());
            bundle.putString("namef",namef);
            enviarBundleAFragment(bundle, new FragmentEvento());
            bundle = null;

        }

        tituloCabecera.setText(namef.toUpperCase());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void enviarBundleAFragment(Bundle bundle, Fragment myFragment) {

        myFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_main,myFragment).addToBackStack(null).commit();

    }

    @Override
    public void enviarBundleAActivity(Bundle bundle) {

       this.bundle = bundle;

    }

    public static Context getMainContext() {
        return context;
    }

    private void validarPermisos() {


        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M ){

            Common.permiso = true;
        }
        else if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                ){

            Common.permiso = true;
        }
        else if (((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))||
                (shouldShowRequestPermissionRationale(CAMERA)))){

            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle("Permisos desactivados");
            dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la app");

            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
                }
            });
            dialogo.show();

        }
        else {

            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100 && grantResults.length==2 && (grantResults[0]==PackageManager.PERMISSION_DENIED ||
                grantResults[1]==PackageManager.PERMISSION_DENIED )){
            Toast.makeText(this,"Debe aceptar todos los permisos para " +
                    "que la app se ejecute correctamente",Toast.LENGTH_LONG).show();

        }else if(requestCode==100 && grantResults.length==2 && (grantResults[0]==PackageManager.PERMISSION_GRANTED ||
                grantResults[1]==PackageManager.PERMISSION_GRANTED)){

            Common.permiso = true;

        }
    }

}
