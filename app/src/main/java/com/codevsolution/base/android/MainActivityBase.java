package com.codevsolution.base.android;

import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.chat.FragmentChatBase;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.encrypt.EncryptUtil;
import com.codevsolution.base.interfaces.ICFragmentos;
import com.codevsolution.base.interfaces.TipoConsultaBD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.services.AutoArranqueChat;
import com.codevsolution.base.services.AutoArranqueJedi;
import com.codevsolution.base.sqlite.ConsultaBDBase;
import com.codevsolution.base.style.Dialogos;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.web.FragmentWebView;
import com.codevsolution.freemarketsapp.settings.Preferencias;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.WRITE_CONTACTS;
import static com.codevsolution.base.settings.PreferenciasBase.SERVVOZ;

public class MainActivityBase extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ICFragmentos,
        InteractorBase.Constantes, JavaUtil.Constantes {

    private static final int LOCATION_REQUEST_CODE = 333;
    protected static final int RECOGNIZE_SPEECH_ACTIVITY = 30;
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
    protected String idUserCode = AndroidUtil.getSharePreference(AppActivity.getAppContext(), USERID, USERIDCODE, NULL);
    protected boolean cambio;
    private TextToSpeech tts;
    private boolean isInitTTS;
    private String ttsTmp;
    protected String comandVoz;
    protected FragmentBase fragment;
    protected CRUDutil crudUtil;
    protected ConsultaBDBase consultaBD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        Log.d(TAG, "on Create");
        context = this;
        AndroidUtil.setSharePreference(context, PREFERENCIAS, ENEJECUCION, true);
        AutoArranqueJedi.cancelJob(context);
        ttsInit();
        bundle = new Bundle();
        crudUtil = new CRUDutil();
        consultaBD = new ConsultaBDBase(getConsultaBd());

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

        drawer = findViewById(Estilos.getIdResource(this, "drawer_layout"));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, Estilos.getIdString(this, "navigation_drawer_open"), Estilos.getIdString(this, "navigation_drawer_close"));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(Estilos.getIdResource(this, "nav_view"));
        imagenPerfil = navigationView.getHeaderView(0).findViewById(Estilos.getIdResource(this, "imgnav"));
        TextView emailUser = navigationView.getHeaderView(0).findViewById(Estilos.getIdResource(this, "emailnav"));
        String email = AndroidUtil.getSharePreference(context, USERID, EMAILUSER, NULL);
        if (email != null && !email.equals(NULL)) {
            emailUser.setText(email);
        }

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

    protected boolean acciones() {

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
            inicio(0);
        } else if (intent.getIntExtra(INICIO, 0) == 1) {

            System.out.println("Permiso boot " + CheckPermisos.validarPermisos(this, RECEIVE_BOOT_COMPLETED, 100));
            AutoArranqueChat.scheduleJob(AppActivity.getAppContext());
            //AutoArranqueJedi.scheduleJob(context);
            //fabVoz.setBackgroundTintList(ColorStateList.valueOf(Estilos.colorBrightGreen));
            //Toast.makeText(context, "Activada fuerza Jedi, que la fuerza te acompañe", Toast.LENGTH_SHORT).show();
            //AndroidUtil.setSharePreference(context, PREFERENCIAS, SERVVOZ, true);

            inicio(1);

        } else if (intent.getIntExtra(INICIO, 0) == 2) {

            setPathAyuda();
            ayudaWeb = pathAyuda + "bienvenida";
            bundle.putString(WEB, ayudaWeb);
            enviarBundleAFragment(bundle, new FragmentWebView());
            return false;
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
            if (notifyMgr != null) {
                notifyMgr.cancel(intent.getIntExtra(EXTRA_ID, 0));
            }

        } else if (accion != null && accion.equals(ACCION_JEDI)) {
            comandVoz = intent.getStringExtra(EXTRA_VOZ);
            onComandVoz(comandVoz);

        }
        fabVoz.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                boolean vozOn = AndroidUtil.getSharePreference(context, PREFERENCIAS, SERVVOZ, false);
                if (vozOn) {
                    AutoArranqueJedi.cancelJob(context);
                    fabVoz.setBackgroundTintList(ColorStateList.valueOf(Estilos.colorSecondaryDark));
                    AndroidUtil.setSharePreference(context, PREFERENCIAS, SERVVOZ, false);

                } else {
                    AndroidUtil.setSharePreference(context, PREFERENCIAS, SERVVOZ, true);
                    AutoArranqueJedi.scheduleJob(context);
                    fabVoz.setBackgroundTintList(ColorStateList.valueOf(Estilos.colorBrightGreen));
                    Toast.makeText(context, "Activada fuerza Jedi, que la fuerza te acompañe", Toast.LENGTH_SHORT).show();

                }
                return true;
            }
        });
        return true;
    }

    protected void onComandVoz(String comandVoz) {

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

        if (tts != null && isInitTTS && !tts.isSpeaking()) {
            tts.stop();
            tts.shutdown();
            isInitTTS = false;
        }

    }

    @Override
    protected void onPause() {
        AndroidUtil.setSharePreference(context, PREFERENCIAS, ENEJECUCION, false);
        AutoArranqueJedi.scheduleJob(context);
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        enviarBundleAFragment(bundle, getCurrentFragment());
        //recargarFragment();
        DrawerLayout drawer = findViewById(Estilos.getIdResource(this, "drawer_layout"));
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (callbackBackPress != null) {
            if (!callbackBackPress.onPressBack()) {
                return;
            }
        }
        super.onBackPressed();

    }

    protected Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            System.out.println("fragmentTag = " + fragmentTag);
            return fragmentManager.findFragmentByTag(fragmentTag);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(Estilos.getIdMenu(this, "main"), menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == Estilos.getIdResource(context, "action_help")) {
            abrirAyuda(NULL);
            return true;
        }
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

    protected void abrirAyuda(String ayuda) {
        setPathAyuda();
        if (ayuda != NULL) {
            ayudaWeb = pathAyuda + ayuda + "/";
        } else if (ayudaWeb != null) {
            ayudaWeb = pathAyuda + ayudaWeb + "/";
        } else {
            ayudaWeb = pathAyuda;
        }
        if (JavaUtil.isValidURL(ayudaWeb)) {
            bundle = new Bundle();
            bundle.putString(WEB, ayudaWeb);
            enviarBundleAFragment(bundle, new FragmentWebView());
        }
    }
    @Override
    public void enviarBundleAFragment(Bundle bundle, Fragment myFragment) {

        if (myFragment != null) {
            myFragment.setArguments(bundle);

            this.bundle = bundle;

            getSupportFragmentManager().beginTransaction().replace(Estilos.getIdResource(this,
                    "content_main"), myFragment).addToBackStack(null).commit();
        }

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

    @Override
    public void abrirAyudaWeb() {
        abrirAyuda(NULL);
    }

    @Override
    public TextToSpeech playTTs(String texto) {

        String utteranceId = this.hashCode() + "";

        return playTTs(texto, utteranceId);
    }

    @Override
    public TextToSpeech playTTs(String texto, String utteranceId) {
        if (isInitTTS && tts != null) {

            if (texto != null && !texto.isEmpty()) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(texto, TextToSpeech.QUEUE_ADD, null, utteranceId);
                } else {
                    tts.speak(texto, TextToSpeech.QUEUE_ADD, null);
                }
            }
        } else {
            ttsTmp = texto;
        }

        return tts;
    }

    @Override
    public TextToSpeech getTTs() {
        return tts;
    }

    @Override
    public void onVoz(Bundle bundle) {
        this.bundle = bundle;
        recargarFragment();
    }

    @Override
    public void setFragment(FragmentBase fragment) {
        this.fragment = fragment;

    }

    @Override
    public void pressBack(Bundle bundle) {
        if (bundle != null) {
            this.bundle = bundle;
        }
        onBackPressed();
    }

    @Override
    public TipoConsultaBD setConsultaBd() {
        return getConsultaBd();
    }

    protected TipoConsultaBD getConsultaBd() {
        return null;
    }

    protected void recargarFragment() {

        boolean vozOn = AndroidUtil.getSharePreference(context, PREFERENCIAS, SERVVOZ, false);
        if (vozOn) {
            fabVoz.setBackgroundTintList(ColorStateList.valueOf(Estilos.colorBrightGreen));
        } else {
            fabVoz.setBackgroundTintList(ColorStateList.valueOf(Estilos.colorSecondaryDark));
        }

        if (bundle == null) {
            bundle = new Bundle();
        }
        String web = bundle.getString(WEB, NULL).toLowerCase();
        System.out.println("web = " + web);
        System.out.println("txtvoz = " + Estilos.getString(context, "voz").toLowerCase());
        if (!web.equals(NULL)) {
            String msg = NULL;
            if (web.equalsIgnoreCase(Estilos.getString(context, "comandos_voz").toLowerCase())) {
                msg = "comandos-de-voz";
            } else if (web.equalsIgnoreCase(Estilos.getString(context, "voz").toLowerCase())) {
                msg = "comandos-de-voz";
            }
            System.out.println("msg = " + msg);
            if (msg != NULL) {
                abrirAyuda(msg);
            }
            return;
        }
        switch (bundle.getString(ACTUAL, INICIO)) {

            case CHAT + CHAT:

                enviarBundleAFragment(bundle, new FragmentChatBase());
                break;


        }

    }

    public void reconocimientoVoz(int code) {

        if (CheckPermisos.validarPermisos(this, READ_CONTACTS, 100) &&
                CheckPermisos.validarPermisos(this, WRITE_CONTACTS, 100)) {

            Intent intentActionRecognizeSpeech = new Intent(
                    RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intentActionRecognizeSpeech.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
            intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            try {
                startActivityForResult(intentActionRecognizeSpeech,
                        code, null);
            } catch (ActivityNotFoundException a) {
                Log.e(TAG, "Tú dispositivo no soporta el reconocimiento por voz");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ttsInit();
        if (acciones()) {

            cambio = AndroidUtil.getSharePreference(context, PERSISTENCIA, CAMBIO, false);
            if (cambio) {
                recuperarPersistencia();
            }
            recargarFragment();
        }
    }

    protected void ttsInit() {

        if (tts == null) {
            tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = tts.setLanguage(Locale.getDefault());

                        if (result == TextToSpeech.LANG_MISSING_DATA
                                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "This Language is not supported");
                        } else {

                            isInitTTS = true;
                            if (ttsTmp != null && !ttsTmp.isEmpty()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    String utteranceId = this.hashCode() + "";
                                    tts.speak(ttsTmp, TextToSpeech.QUEUE_ADD, null, utteranceId);
                                } else {
                                    tts.speak(ttsTmp, TextToSpeech.QUEUE_ADD, null);
                                }
                                ttsTmp = null;
                            }
                            Log.d("TTS", "Initilization Success!");
                        }

                    } else {
                        Log.e("TTS", "Initilization Failed!");
                    }

                }
            });
        } else {
            if (!isInitTTS) {
                isInitTTS = true;
            }
        }
    }

    protected CallbackBackPress callbackBackPress;

    public void setCallbackBackPress(CallbackBackPress callbackBackPress) {
        this.callbackBackPress = callbackBackPress;
    }

    public interface CallbackBackPress {

        boolean onPressBack();

    }
}
