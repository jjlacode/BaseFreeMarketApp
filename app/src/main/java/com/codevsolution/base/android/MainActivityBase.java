package com.codevsolution.base.android;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.chat.FragmentChatBase;
import com.codevsolution.base.encrypt.EncryptUtil;
import com.codevsolution.base.interfaces.ICFragmentos;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.services.AutoArranque;
import com.codevsolution.base.style.Dialogos;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.web.FragmentWebView;
import com.codevsolution.freemarketsapp.settings.Preferencias;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;

public class MainActivityBase extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ICFragmentos,
        InteractorBase.Constantes, JavaUtil.Constantes {

    private static final int LOCATION_REQUEST_CODE = 333;
    protected Bundle bundle;
    public Toolbar toolbar;
    public FloatingActionButton fabNuevo;
    public FloatingActionButton fabVoz;
    public FloatingActionButton fabInicio;

    public Context context;
    protected boolean land;
    protected boolean tablet;
    protected float sizeF;
    protected String ayudaWeb;
    protected String TAG = getClass().getSimpleName();

    public boolean permisoInternet;
    protected Intent intent;
    protected String accion;
    public ImageView imagenPerfil;
    public DrawerLayout drawer;
    protected String pathAyuda;
    protected String idUser = AndroidUtil.getSharePreference(AppActivity.getAppContext(), USERID, USERID, NULL);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        Log.d(TAG, "on Create");
        context = this;
        bundle = new Bundle();
        if (AndroidUtil.getSharePreference(context, PERSISTENCIA, CAMBIO, false)) {
            recuperarPersistencia();
        }

        checkPermisos();

        land = Estilos.getBool(this, "esLand");
        tablet = Estilos.getBool(this, "esTablet");

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int ancho = metrics.widthPixels;
        int alto = metrics.heightPixels;
        if (!land) {
            sizeF = ((float) ancho * (float) alto) / (metrics.densityDpi * 300);
        } else {
            sizeF = ((float) ancho * (float) alto) / (metrics.densityDpi * 300);
        }

        PreferenceManager.setDefaultValues(this, Estilos.getIdXml(this, "settings"), false);

        setContentView(Estilos.getIdLayout(this, "activity_main"));

        toolbar = findViewById(Estilos.getIdResource(this, "toolbar"));
        setSupportActionBar(toolbar);

        fabNuevo = findViewById(Estilos.getIdResource(this, "fab"));
        fabVoz = findViewById(Estilos.getIdResource(this, "fab2"));
        fabInicio = findViewById(Estilos.getIdResource(this, "fab3"));

        acciones();

        drawer = findViewById(Estilos.getIdResource(this, "drawer_layout"));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, Estilos.getIdString(this, "navigation_drawer_open"), Estilos.getIdString(this, "navigation_drawer_close"));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recargarFragment();

        NavigationView navigationView = findViewById(Estilos.getIdResource(this, "nav_view"));
        imagenPerfil = navigationView.getHeaderView(0).findViewById(Estilos.getIdResource(this, "imgnav"));

        if (idUser != null && !idUser.equals(NULL) && !idUser.isEmpty()) {
            ImagenUtil.setImageFireStoreCircle(SLASH + idUser + SLASH + idUser + CAMPO_ACTIVO, imagenPerfil);
            setPathImagenPerfil();
        }

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemTextAppearance(Estilos.getIdStyle(this, "TextAppearance_AppCompat_Menu"));

    }

    protected void inicio(int inicio) {
    }

    protected void recuperarPersistencia() {
    }

    protected void setPathImagenPerfil() {
    }

    protected void setPathAyuda() {
    }

    public void seleccionarDestino(String destino) {

    }

    public void seleccionarNuevoDestino(String destino) {

    }

    protected void acciones() {

        try {
            InteractorBase.key = EncryptUtil.desencriptarStrAES(AndroidUtil.getSharePreference(context, PREFERENCIAS, PASSOK, NULL), idUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("InteractorBase.key = " + InteractorBase.key);

        if (InteractorBase.key == null || InteractorBase.key.equals(NULL)) {

            if (AndroidUtil.getSharePreference(context, PREFERENCIAS, Preferencias.CIFRADOPASS, false)) {
                String pass = AndroidUtil.getSharePreference(context, PREFERENCIAS, ENCODEPASS, NULL);
                if (pass == null || pass.equals(NULL)) {
                    String titulo = "CIFRADO";
                    String mensaje = "Introduzca nueva contraseña de cifrado";
                    new Dialogos.DialogoCambioPass(titulo, mensaje, pass, context,
                            new Dialogos.DialogoCambioPass.OnClick() {
                                @Override
                                public void onConfirm(String text) {

                                    InteractorBase.key = text;
                                    EncryptUtil.codificaPass();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            onOkPass();
                                        }
                                    }).start();
                                }

                                @Override
                                public void onCancel() {

                                    Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show();
                                }
                            }).show(getSupportFragmentManager(), "dialogEdit");

                } else {

                    String titulo = "CIFRADO";
                    String mensaje = "Introduzca contraseña de cifrado";
                    new Dialogos.DialogoEdit(titulo, mensaje, EditMaterialLayout.TEXTO | EditMaterialLayout.PASS, "Contraseña",
                            context, new Dialogos.DialogoEdit.OnClick() {
                        @Override
                        public void onConfirm(String text) {

                            InteractorBase.key = text;


                            if (text != null && text != "" && EncryptUtil.verificarPass(text)) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        String pass = NULL;
                                        try {
                                            pass = EncryptUtil.encriptarStrAES(text, idUser);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        AndroidUtil.setSharePreference(context, PREFERENCIAS, PASSOK, pass);
                                        onOkPass();
                                    }
                                }).start();
                            } else {
                                Toast.makeText(context, "La contraseña de cifrado no es valida", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancel() {

                            Toast.makeText(context, "Tendrá que introducir la contraseña en las preferencias para poder cifrar y descifrar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }).show(getSupportFragmentManager(), "dialogEdit");
                }
            }
        }

        intent = getIntent();

        System.out.println("inicio = " + intent.getIntExtra(INICIO, 0));

        if (intent.getIntExtra(INICIO, 0) == 0) {
        } else if (intent.getIntExtra(INICIO, 0) == 1) {

            inicio(0);


            System.out.println("Permiso boot " + CheckPermisos.validarPermisos(this, RECEIVE_BOOT_COMPLETED, 100));
            AutoArranque.scheduleJob(AppActivity.getAppContext());

            inicio(1);

        } else if (intent.getIntExtra(INICIO, 0) == 2) {

            inicio(2);
            setPathAyuda();
            ayudaWeb = pathAyuda + "bienvenida";
            bundle.putString(WEB, ayudaWeb);
            enviarBundleAFragment(bundle, new FragmentWebView());
        }


        accion = intent.getAction();

        if (accion != null && accion.equals(ACCION_VERCHAT)) {

            System.out.println("Accion ver chat");

            String idChat = intent.getStringExtra(EXTRA_IDCHAT);
            String idUserChat = intent.getStringExtra(EXTRA_IDSPCHAT);
            int secChat = intent.getIntExtra(EXTRA_SECCHAT, 0);
            bundle.putString(ACTUAL, CHAT + intent.getStringExtra(EXTRA_ACTUAL));
            bundle.putString(CAMPO_ID, idChat);
            bundle.putString(USERID, idUserChat);
            bundle.putInt(CAMPO_SECUENCIA, secChat);
            NotificationManager notifyMgr = (NotificationManager)
                    AppActivity.getAppContext().getSystemService(NOTIFICATION_SERVICE);
            notifyMgr.cancel(intent.getIntExtra(EXTRA_ID, 0));

        }

    }

    protected void onOkPass() {

    }

    protected void checkPermisos() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (AndroidUtil.getSharePreference(context, PREFERENCIAS, Preferencias.CIFRADOPASSPLUS, false)) {
            AndroidUtil.setSharePreference(context, PREFERENCIAS, PASSOK, NULL);
        }
    }

    @Override
    public void onBackPressed() {

        recargarFragment();
        DrawerLayout drawer = findViewById(Estilos.getIdResource(this, "drawer_layout"));
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(Estilos.getIdMenu(this, "main"), menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        bundle = new Bundle();

        setOnNavigation(item);

        DrawerLayout drawer = findViewById(Estilos.getIdResource(this, "drawer_layout"));
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void setOnNavigation(MenuItem item) {

    }

    @Override
    public void enviarBundleAFragment(Bundle bundle, Fragment myFragment) {

        myFragment.setArguments(bundle);

        this.bundle = bundle;
        System.out.println("bundle.getString(ACTUAL,NULL) = " + bundle.getString(ACTUAL, NULL));

        getSupportFragmentManager().beginTransaction().replace(Estilos.getIdResource(this, "content_main"), myFragment).addToBackStack(null).commit();

    }


    @Override
    public void enviarBundleAActivity(Bundle bundle) {

        this.bundle = bundle;

    }

    @Override
    public void enviarBundleFragmentFragment(Bundle bundle) {


    }

    @Override
    public void addFragment(Fragment myFragment, int layout) {

        getSupportFragmentManager().beginTransaction().add(layout, myFragment).addToBackStack(null).commit();
    }

    @Override
    public void addFragment(Bundle bundle, Fragment myFragment, int layout) {

        myFragment.setArguments(bundle);

        this.bundle = bundle;

        if (layout <= 0) {
            getSupportFragmentManager().beginTransaction().add(Estilos.getIdResource(this, "content_main"), myFragment).addToBackStack(null).commit();
        } else {

            getSupportFragmentManager().beginTransaction().add(layout, myFragment).addToBackStack(null).commit();
        }

    }

    @Override
    public void reemplazaFragment(Bundle bundle, Fragment myFragment, int layout) {

        myFragment.setArguments(bundle);

        this.bundle = bundle;

        if (layout <= 0) {
            getSupportFragmentManager().beginTransaction().replace(Estilos.getIdResource(this, "content_main"), myFragment).addToBackStack(null).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(layout, myFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void eliminarFragment(Fragment myFragment) {

        getSupportFragmentManager().beginTransaction().remove(myFragment).commit();
    }

    @Override
    public void fabVisible() {

        fabNuevo.show();

    }

    @Override
    public void snackBarShow(View view, String mensaje) {

        Snackbar.make(view, mensaje, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

    }

    @Override
    public void setIcoFab(int recurso) {

        fabNuevo.setImageResource(recurso);
    }

    @Override
    public void setIcoFab(Drawable drawable) {

        fabNuevo.setImageDrawable(drawable);
    }

    @Override
    public void fabOculto() {
        fabNuevo.hide();
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

        switch (bundle.getString(ACTUAL, INICIO)) {

            case CHAT + CHAT:

                enviarBundleAFragment(bundle, new FragmentChatBase());
                break;


        }

    }

}
