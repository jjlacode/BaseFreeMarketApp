package jjlacode.com.freelanceproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import jjlacode.com.androidutils.ICFragmentos;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.ui.FragmentAgenda;
import jjlacode.com.freelanceproject.ui.FragmentAmortizacion;
import jjlacode.com.freelanceproject.ui.FragmentCCliente;
import jjlacode.com.freelanceproject.ui.FragmentCEvento;
import jjlacode.com.freelanceproject.ui.FragmentCPartidaProyecto;
import jjlacode.com.freelanceproject.ui.FragmentCProyecto;
import jjlacode.com.freelanceproject.ui.FragmentCUDAmortizacion;
import jjlacode.com.freelanceproject.ui.FragmentCUDGastoFijo;
import jjlacode.com.freelanceproject.ui.FragmentCUDPerfil;
import jjlacode.com.freelanceproject.ui.FragmentCliente;
import jjlacode.com.freelanceproject.ui.FragmentEvento;
import jjlacode.com.freelanceproject.ui.FragmentGastoFijo;
import jjlacode.com.freelanceproject.ui.FragmentPartidaBase;
import jjlacode.com.freelanceproject.ui.FragmentPerfil;
import jjlacode.com.freelanceproject.ui.FragmentProyecto;
import jjlacode.com.freelanceproject.ui.FragmentUDPreferencias;
import jjlacode.com.freelanceproject.ui.FragmentUDProyecto;
import jjlacode.com.freelanceproject.utilities.CommonPry;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static jjlacode.com.freelanceproject.utilities.CommonPry.TiposEvento.EVENTO;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ICFragmentos,
        CommonPry.Constantes, ContratoPry.Tablas {

    TextView tituloCabecera ;
    String namef ;
    String nameftemp ;
    Bundle bundle;
    Toolbar toolbar;

    private String namefsub;
    private String precioHora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        namef = AGENDA;

        nameftemp = AGENDA;
        tituloCabecera = findViewById(R.id.tvmaintitulo);
        tituloCabecera.setText(namef.toUpperCase());

        validarPermisos();
        //String [] args = {CAMERA,READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE};
        //CommonPry.Permisos permisos = new CommonPry.Permisos(context,this,args);
        //permisos.validarPermisos();

        precioHora= JavaUtil.formatoMonedaLocal(CommonPry.hora);

        if (CommonPry.prioridad) {
            toolbar.setSubtitle("Perfil: " + CommonPry.perfila + " - hora= " + precioHora + " - P");
        }else{

            toolbar.setSubtitle("Perfil: " + CommonPry.perfila + " - hora= " + precioHora );
        }


        FloatingActionButton fab =  findViewById(R.id.fab);
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
                            }
                        }else {
                            bundle = new Bundle();
                            bundle.putString("namef", namef);
                            enviarBundleAFragment(bundle, new FragmentUDProyecto());
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
                    }else if (namef.equals(PERFIL)) {

                        bundle = new Bundle();
                        bundle.putString("namef", namef);
                        enviarBundleAFragment(bundle, new FragmentCUDPerfil());
                        bundle = null;
                        Snackbar.make(view, "Nuevo Perfil", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else if (namef.equals(AMORTIZACION)) {

                        bundle = new Bundle();
                        bundle.putString("namef", namef);
                        enviarBundleAFragment(bundle, new FragmentCUDAmortizacion());
                        bundle = null;
                        Snackbar.make(view, "Nueva Amortización", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else if (namef.equals(GASTOSFIJOS)) {

                        bundle = new Bundle();
                        bundle.putString("namef", namef);
                        enviarBundleAFragment(bundle, new FragmentCUDGastoFijo());
                        bundle = null;
                        Snackbar.make(view, "Nuevo Gasto fijo", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else if (namef.equals(PARTIDABASE)) {

                        bundle = new Bundle();
                        bundle.putString("namef", namef);
                        enviarBundleAFragment(bundle, new FragmentCPartidaProyecto());
                        bundle = null;
                        Snackbar.make(view, "Nueva Partida Base", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                //}
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        bundle = new Bundle();
        bundle.putString("namef",namef);
        enviarBundleAFragment(bundle, new FragmentAgenda());
        bundle = null;
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
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
            tituloCabecera.setText(namef.toUpperCase());
            bundle = new Bundle();
            bundle.putString("namef",namef);
            enviarBundleAFragment(bundle, new FragmentUDPreferencias());
            bundle = null;
        }

        return super.onOptionsItemSelected(item);
    }

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

        }else if (id == R.id.nav_amortizacion) {

            namef = AMORTIZACION;
            tituloCabecera.setText(namef.toUpperCase());
            bundle.putString("namef",namef);
            enviarBundleAFragment(bundle, new FragmentAmortizacion());
            bundle = null;

        }else if (id == R.id.nav_gastosfijos) {

            namef = GASTOSFIJOS;
            tituloCabecera.setText(namef.toUpperCase());
            bundle.putString("namef",namef);
            enviarBundleAFragment(bundle, new FragmentGastoFijo());
            bundle = null;

        }else if (id == R.id.nav_partidabase) {

            namef = PARTIDABASE;
            tituloCabecera.setText(namef.toUpperCase());
            bundle.putString("namef",namef);
            enviarBundleAFragment(bundle, new FragmentPartidaBase());
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

    @Override
    public void ejecutarEnActivity() {

        precioHora= JavaUtil.formatoMonedaLocal(CommonPry.hora);

        if (CommonPry.prioridad) {
            toolbar.setSubtitle("Perfil: " + CommonPry.perfila + " - " +
                    "hora= " + precioHora + " - P");
        }else{

            toolbar.setSubtitle("Perfil: " + CommonPry.perfila + " - " +
                    "hora= " + precioHora);
        }

        System.out.println("precioHora = " + precioHora);

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

}
