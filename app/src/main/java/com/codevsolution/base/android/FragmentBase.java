package com.codevsolution.base.android;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.LockableScrollView;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.animation.OneFrameLayout;
import com.codevsolution.base.interfaces.ICFragmentos;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.models.Contactos;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.freemarketsapp.settings.Preferencias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.SENSOR_SERVICE;

//import com.codevsolution.freemarketsapp.logica.Interactor;

public abstract class FragmentBase extends Fragment implements JavaUtil.Constantes,
        InteractorBase.Constantes {

    protected String TAG;
    protected View view;
    protected int layout;
    protected MainActivityBase activityBase;
    protected Context contexto;
    protected ICFragmentos icFragmentos;
    protected Bundle bundle;
    protected boolean land;
    protected boolean tablet;
    protected DisplayMetrics metrics;

    protected static int ancho;
    protected int anchoReal;
    protected int alto;
    protected int altoReal;
    protected int densidadDpi;
    protected float sizeText;
    protected boolean multiPanel;
    protected ArrayList<View> vistas;
    protected ArrayList<EditMaterial> materialEdits;
    protected ArrayList<EditMaterialLayout> materialEditLayouts;
    protected ArrayList<Integer> recursos;

    protected LinearLayoutCompat lyDetalle;
    protected LinearLayoutCompat frdetalle;
    protected LinearLayoutCompat frdetalleExtraspost;
    protected LinearLayoutCompat frdetalleExtrasante;
    protected LinearLayoutCompat frPie;
    protected LinearLayoutCompat frPubli;
    protected LinearLayoutCompat frWeb;
    protected LinearLayoutCompat frCabecera;
    protected LinearLayoutCompat frLista;
    protected LinearLayoutCompat frCuerpo;

    protected View viewRV;
    protected View viewCabecera;
    protected View viewCuerpo;
    protected View viewBotones;
    protected int layoutCuerpo;
    protected int layoutCabecera;
    protected int layoutPie;
    protected boolean cabecera;
    private Chronometer timerg;
    private boolean onTimer;
    protected LayoutInflater inflaterMain;
    protected ViewGroup containerMain;
    protected ImageButton btnsave;
    protected ImageButton btnback;
    protected ImageButton btndelete;
    public OneFrameLayout frameAnimationCuerpo;
    public LockableScrollView scrollDetalle;
    protected static float densidad;
    protected static final int RECOGNIZE_SPEECH_ACTIVITY = 30;
    protected String grabarVoz;
    protected String origen;
    protected String actual;
    protected String actualtemp;
    protected String subTitulo;
    protected String destino;
    protected String ayudaWeb;
    protected int code;
    protected int[] codigo;
    protected int contCode;
    private SensorEventListener proximitySensorListener;
    private SensorManager sensorManagerProx;
    private Sensor proximitySensor;
    private boolean listenerSensorProx;
    private SensorManager sensorManagerLuz;
    private Sensor mALS;
    private SensorEventListener sensorLuzListener;
    private boolean listenerSensorLuz;
    private float valorLuz;
    protected ArrayList<Map> camposEdit;
    protected Timer timer;
    protected String perfilUser;
    protected String idUser;
    protected boolean autoGuardado;
    protected boolean letraProp;
    protected boolean cifrado;
    protected int tiempoGuardado = 1;
    protected int sizeTextD;
    protected boolean swipeOn;
    protected boolean modulo;
    protected FragmentBase fragment;
    protected FragmentBase parent;
    protected RelativeLayout frContenedor;
    protected LinearLayoutCompat frContenedorMod;
    protected ViewGroupLayout vistaMain;
    protected TextToSpeech tts;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, getMetodo());

        fragment = setFragment();
        parent = getParent();
        if (contexto == null) {
            setContext();
        }
        setLayout();
        setLayoutExtra();

        bundle = getArguments();

        System.out.println("getArguments() = " + getArguments());
        if (nn(bundle)) {
            modulo = esModulo();
        }

        metrics = new DisplayMetrics();
        activityBase.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        multiPanel = esMultiPanel(metrics);

        densidad = metrics.density;
        anchoReal = metrics.widthPixels;
        altoReal = metrics.heightPixels;
        ancho = (int) (metrics.widthPixels / densidad);
        alto = (int) (metrics.heightPixels / densidad);
        densidadDpi = metrics.densityDpi;

        sizeText = ((float) (ancho + alto + densidadDpi) / (100));
        System.out.println("sizeText = " + sizeText);
        sizeTextD = (int) (sizeText * densidad);
        materialEdits = new ArrayList<>();
        materialEditLayouts = new ArrayList<>();
        vistas = new ArrayList<>();
        recursos = new ArrayList<>();
        camposEdit = new ArrayList();
        TAG = setTAG();

        perfilUser = AndroidUtil.getSharePreference(AppActivity.getAppContext(), PREFERENCIAS, PERFILUSER, NULL);
        idUser = AndroidUtil.getSharePreference(AppActivity.getAppContext(), USERID, USERID, NULL);

        setPreferences();
        super.onCreate(savedInstanceState);
    }

    protected abstract FragmentBase setFragment();

    protected void setPreferences() {

        autoGuardado = getPref(Preferencias.AUTOGUARDADO, true);
        letraProp = getPref(Preferencias.LETRAPROP, true);
        cifrado = getPref(Preferencias.CIFRADO, false);
        tiempoGuardado = getPref(Preferencias.TIEMPOAUTOGUARDADO, 1);

    }

    public boolean esMultiPanel(DisplayMetrics metrics) {
        Log.d(TAG, getMetodo());
        // Determinar que siempre sera multipanel
        return ((float) metrics.densityDpi / (float) metrics.widthPixels) < 0.30;
    }

    protected FragmentBase getParent() {

        return this;
    }

    public String getTAG() {
        return TAG;
    }

    protected boolean esModulo() {
        return bundle.getBoolean(MODULO, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, getMetodo());
        inflaterMain = inflater;
        containerMain = container;

        view = inflater.inflate(Estilos.getIdLayout(contexto, "module"), container, false);
        //frContenedorMod = view.findViewById(Estilos.getIdResource(contexto, "fr_content_mod"));
        //Estilos.setLayoutParams(frContenedorMod);
        //frContenedorMod.setOrientation(LinearLayoutCompat.VERTICAL);
        frContenedor = view.findViewById(Estilos.getIdResource(contexto, "fr_content_mod"));
        Estilos.setLayoutParams(frContenedor);
        timerg = (Chronometer) addVista(new Chronometer(contexto), frContenedor);
        timerg.setVisibility(View.GONE);
        frCabecera = new LinearLayoutCompat(contexto);
        LinearLayoutCompat sepCab = new LinearLayoutCompat(contexto);
        int idFrCab = frCabecera.getId();
        if (idFrCab < 0) {
            idFrCab = View.generateViewId();
            frCabecera.setId(idFrCab);
        }
        int idSepCab = sepCab.getId();
        if (idSepCab < 0) {
            idSepCab = View.generateViewId();
            sepCab.setId(idSepCab);
        }
        frCuerpo = new LinearLayoutCompat(contexto);
        int idFrCuerpo = frCuerpo.getId();
        if (idFrCuerpo < 0) {
            idFrCuerpo = View.generateViewId();
            frCuerpo.setId(idFrCuerpo);
        }
        frPubli = new LinearLayoutCompat(contexto);
        int idFrPubli = frPubli.getId();
        if (idFrPubli < 0) {
            idFrPubli = View.generateViewId();
            frPubli.setId(idFrPubli);
        }
        frPie = new LinearLayoutCompat(contexto);
        int idFrPie = frPie.getId();
        if (idFrPie < 0) {
            idFrPie = View.generateViewId();
            frPie.setId(idFrPie);
        }

        Estilos.setLayoutParamsRelative(frContenedor, frCabecera, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.ALIGN_PARENT_START});
        frCabecera.setOrientation(LinearLayoutCompat.VERTICAL);
        Estilos.setLayoutParamsRelative(frContenedor, sepCab, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, new int[]{RelativeLayout.BELOW, RelativeLayout.ALIGN_PARENT_START}, new int[]{idFrCab, 0});
        Estilos.setLayoutParamsRelative(frContenedor, frCuerpo, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, new int[]{RelativeLayout.BELOW, RelativeLayout.ABOVE,
                        RelativeLayout.ALIGN_PARENT_START}, new int[]{idSepCab, idFrPie, 0});
        frCuerpo.setOrientation(LinearLayoutCompat.VERTICAL);
        Estilos.setLayoutParamsRelative(frContenedor, frPie, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, new int[]{RelativeLayout.ABOVE, RelativeLayout.ALIGN_PARENT_START}, new int[]{idFrPubli, 0});
        frPie.setOrientation(LinearLayoutCompat.VERTICAL);
        Estilos.setLayoutParamsRelative(frContenedor, frPubli, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, new int[]{RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.ALIGN_PARENT_START});
        frPubli.setOrientation(LinearLayoutCompat.VERTICAL);
        frPubli.setBackgroundColor(Estilos.colorSecondary);
        frPubli.setMinimumHeight(activityBase.fabVoz.getHeight());
        frLista = (LinearLayoutCompat) addVista(new LinearLayoutCompat(contexto), frCuerpo);
        Estilos.setLayoutParams(frLista);
        frLista.setOrientation(LinearLayoutCompat.VERTICAL);
        frameAnimationCuerpo = (OneFrameLayout) addVista(new OneFrameLayout(contexto), frCuerpo);
        Estilos.setLayoutParams(frameAnimationCuerpo);
        scrollDetalle = (LockableScrollView) addVista(new LockableScrollView(contexto), frameAnimationCuerpo);
        Estilos.setLayoutParams(scrollDetalle);
        lyDetalle = (LinearLayoutCompat) addVista(new LinearLayoutCompat(contexto), scrollDetalle);
        Estilos.setLayoutParams(lyDetalle);
        lyDetalle.setOrientation(LinearLayoutCompat.VERTICAL);
        frdetalleExtrasante = (LinearLayoutCompat) addVista(new LinearLayoutCompat(contexto), lyDetalle);
        Estilos.setLayoutParams(frdetalleExtrasante);
        frdetalleExtrasante.setOrientation(LinearLayoutCompat.VERTICAL);
        frdetalle = (LinearLayoutCompat) addVista(new LinearLayoutCompat(contexto), lyDetalle);
        Estilos.setLayoutParams(frdetalle);
        frdetalle.setOrientation(LinearLayoutCompat.VERTICAL);
        frdetalleExtraspost = (LinearLayoutCompat) addVista(new LinearLayoutCompat(contexto), lyDetalle);
        Estilos.setLayoutParams(frdetalleExtraspost);
        frdetalleExtraspost.setOrientation(LinearLayoutCompat.VERTICAL);
        frWeb = (LinearLayoutCompat) addVista(new LinearLayoutCompat(contexto), lyDetalle);

        if (layoutCuerpo == 0) {
            layoutCuerpo = layout;
        }

        land = Estilos.getBool(contexto, "esLand");//getResources().getBoolean(R.bool.esLand);
        tablet = Estilos.getBool(contexto, "esTablet");//getResources().getBoolean(R.bool.esTablet);

        viewCabecera = addVista(layoutCabecera, frCabecera);
        viewCuerpo = addVista(layoutCuerpo, frdetalle);
        viewBotones = addVista(layoutPie, frPie);


        frameAnimationCuerpo.setAncho((int) (ancho * densidad));

        gone(frLista);

        setOnCreateView(view, inflaterMain, containerMain);

        setInicio();

        setSizeTextControles(sizeText);

        AndroidUtil.ocultarTeclado(activityBase, view);

        swipeOn = true;

        frameAnimationCuerpo.setOnSwipeListener(new OneFrameLayout.OnSwipeListener() {
            @Override
            public void rightSwipe() {
                if (swipeOn) setOnRigthSwipeCuerpo();
            }

            @Override
            public void leftSwipe() {
                if (swipeOn) setOnLeftSwipeCuerpo();
            }
        });

        System.out.println("modulo = " + modulo);
        if (modulo) {
            setModuloInicio();
        }
        activityBase.fabNuevo.hide();
        activityBase.fabInicio.hide();
        activityBase.fabVoz.hide();


        return view;
    }

    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {

        Log.d(TAG, getMetodo());

        activityBase.fabVoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reconocimientoVoz(RECOGNIZE_SPEECH_ACTIVITY);
            }
        });

    }

    public void setActivoFrameAnimationCuerpo(boolean activo) {
        frameAnimationCuerpo.setActivo(activo);
    }

    public void setScrollingDetalleEnable(boolean enable) {
        scrollDetalle.setScrollingEnabled(enable);
    }

    public ICFragmentos getIcFragmentos() {
        return icFragmentos;
    }

    public LinearLayoutCompat getFrdetalle() {
        return frdetalle;
    }

    public LinearLayoutCompat getFrdetalleExtraspost() {
        return frdetalleExtraspost;
    }

    public LinearLayoutCompat getFrdetalleExtrasante() {
        return frdetalleExtrasante;
    }

    public int getAltoReal() {
        return altoReal;
    }

    protected void setContext() {
        Log.d(TAG, getMetodo());

        contexto = activityBase;

    }

    protected void setLayoutExtra() {
        Log.d(TAG, getMetodo());

    }

    protected String setTAG() {
        return getClass().getSimpleName();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, getMetodo());

        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, NULL);

        ayudaWeb = setAyudaWeb();
        icFragmentos.enviarAyudaWeb(ayudaWeb);

        if (!listenerSensorProx && sensorProximidad()) {
            sensorManagerProx.registerListener(proximitySensorListener,
                    proximitySensor, 1000 * 1000);
            listenerSensorProx = true;
        }

        if (!listenerSensorLuz && listenerSensorProx && sensorLuz()) {
            sensorManagerLuz.registerListener(sensorLuzListener,
                    mALS, 1000 * 1000);
            listenerSensorLuz = true;
        }

        timerg.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {

                setOnCronometro(arg0);

            }
        });

        bundle = getArguments();
        cargarBundle();

        SharedPreferences persistencia = AndroidUtil.openSharePreference(contexto, PERSISTENCIA);
        TAG = setTAG();
        System.out.println("TAG = " + TAG);
        System.out.println("persistencia.getString(TAGPERS) = " + persistencia.getString(TAGPERS, null));

        if (!modulo && (persistencia.getBoolean(CAMBIO, false) || (persistencia.getString(TAGPERS, null) != null &&
                TAG != null && persistencia.getString(TAGPERS, null).equals(TAG)))) {
            System.out.println("Recuperando datos persistencia");
            origen = persistencia.getString(ORIGEN, null);
            actual = persistencia.getString(ACTUAL, null);
            actualtemp = persistencia.getString(ACTUALTEMP, null);
            subTitulo = persistencia.getString(SUBTITULO, null);
            setRecuperarPersistencia(persistencia);

        }

        SharedPreferences.Editor editor = persistencia.edit();

        editor.putString(TAGPERS, null);
        editor.apply();


    }

    protected void setRecuperarPersistencia(SharedPreferences persistencia) {

    }

    protected void reproducir(String text) {

        tts = new TextToSpeech(contexto, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.getDefault());

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    } else {

                        if (text == null || text.isEmpty())
                            return;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            String utteranceId = this.hashCode() + "";
                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                        } else {
                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        }

                    }
                } else {
                    Log.e("TTS", "Initilization Failed!");
                }

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, getMetodo());

        if (context instanceof MainActivityBase) {
            activityBase = (MainActivityBase) context;
            icFragmentos = activityBase;
            contexto = activityBase;

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, getMetodo());

        icFragmentos = null;
    }

    protected void cargarBundle() {
        Log.d(TAG, getMetodo());


    }

    protected void setModuloInicio() {

    }

    @Override
    public void onPause() {
        super.onPause();

        if (listenerSensorProx) {
            sensorManagerProx.unregisterListener(proximitySensorListener);
        }

        if (listenerSensorLuz) {
            sensorManagerLuz.unregisterListener(sensorLuzListener);
        }

        setActual();

        if (!modulo) {
            SharedPreferences persistencia = getActivity().getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = persistencia.edit();

            editor.putString(TAGPERS, setTAG());
            editor.putString(ORIGEN, origen);
            editor.putString(ACTUAL, actual);
            editor.putString(ACTUALTEMP, actualtemp);
            editor.putString(SUBTITULO, subTitulo);
            setPersistencia(editor);

            editor.apply();
        }


    }

    protected void setActual() {

    }

    protected void setPersistencia(SharedPreferences.Editor editor) {

    }


    protected void acciones() {
        Log.d(TAG, getMetodo());

        code = 10000;
        contCode = 0;
        codigo = new int[materialEdits.size() + materialEditLayouts.size() + 1];
        codigo[contCode] = code;
        for (EditMaterial materialEdit : materialEdits) {

            if (materialEdit.getActivo()) {
                materialEdit.grabarEnable(true);
                materialEdit.setPosicion(contCode);
                materialEdit.setGrabarListener(new EditMaterial.AudioATexto() {
                    @Override
                    public void onGrabar(View view, int posicion) {

                        reconocimientoVoz(codigo[posicion]);

                    }

                });
                code++;
                contCode++;
                codigo[contCode] = code;
            }
        }

        for (EditMaterialLayout materialEdit : materialEditLayouts) {

            if (materialEdit.getActivo()) {
                materialEdit.grabarEnable(true);
                materialEdit.setTTS(true);
                materialEdit.setPosicion(contCode);
                materialEdit.setGrabarListener(new EditMaterialLayout.AudioATexto() {
                    @Override
                    public void onGrabar(View view, int posicion) {

                        reconocimientoVoz(codigo[posicion]);

                    }

                });
                code++;
                contCode++;
                codigo[contCode] = code;
            }
        }

    }

    protected void enviarAct() {
        Log.d(TAG, getMetodo());

        bundle = new Bundle();
        bundle.putString(ORIGEN, origen);
        bundle.putString(ACTUAL, actual);
        bundle.putString(ACTUALTEMP, actualtemp);
        bundle.putString(SUBTITULO, subTitulo);
        System.out.println("Enviando bundle a MainActivity");
        icFragmentos.enviarBundleAActivity(bundle);
    }

    protected void enviarBundle() {
        Log.d(TAG, getMetodo());

        bundle = new Bundle();
        bundle.putString(ORIGEN, actual);
        bundle.putString(ACTUAL, destino);
        bundle.putString(ACTUALTEMP, actualtemp);
        bundle.putString(SUBTITULO, subTitulo);

    }

    @Override
    public void onConfigurationChanged(Configuration myConfig) {
        super.onConfigurationChanged(myConfig);
        Log.d(TAG, getMetodo());

        int orientation = getResources().getConfiguration().orientation;
        SharedPreferences persistencia = AndroidUtil.openSharePreference(contexto, PERSISTENCIA);
        SharedPreferences.Editor editor = persistencia.edit();

        if (!modulo) {
            editor.putBoolean(CAMBIO, true);
            editor.putString(TAGPERS, setTAG());
            editor.putString(ORIGEN, origen);
            editor.putString(ACTUAL, actual);
            editor.putString(ACTUALTEMP, actualtemp);
            editor.putString(SUBTITULO, subTitulo);
            setPersistencia(editor);
            editor.apply();
        }

        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                // Con la orientación en horizontal actualizamos el adaptador
                break;

            case Configuration.ORIENTATION_PORTRAIT:
                // Con la orientación en vertical actualizamos el adaptador
                break;
        }
    }


    protected void setSwipeOn(boolean enable) {
        swipeOn = enable;
    }
    protected String setAyudaWeb() {
        return null;
    }

    /**
     *
     */
    protected void setOnRigthSwipeCuerpo() {

        Log.d(TAG, getMetodo());
    }

    protected void setOnLeftSwipeCuerpo() {
        Log.d(TAG, getMetodo());
    }


    protected void setLayout() {

    }

    protected void setInicio() {

    }

    /**
     * Asigna el recurso al control en la vista actual
     *
     * @param recurso Recurso del la vista actual que se asigna al Control
     */
    protected View ctrl(int recurso) {

        View vista = view.findViewById(recurso);
        vista.setFocusable(false);
        vistas.add(vista);
        if (vista instanceof EditMaterial) {
            materialEdits.add((EditMaterial) vista);
        }
        recursos.add(recurso);
        return vista;

    }

    protected View ctrl(View view, int recurso) {

        View vista = view.findViewById(recurso);
        vista.setFocusable(false);
        vistas.add(vista);
        if (vista instanceof EditMaterial) {
            materialEdits.add((EditMaterial) vista);
        }
        recursos.add(recurso);
        return vista;

    }

    protected View ctrl(int recurso, String campoEdit) {

        View vista = view.findViewById(recurso);
        vistas.add(vista);
        if (vista instanceof EditMaterial) {
            vista.setFocusable(true);
            materialEdits.add((EditMaterial) vista);
            Map mapaCtrl = new HashMap();
            mapaCtrl.put("materialEdit", vista);
            mapaCtrl.put("campoEdit", campoEdit);
            camposEdit.add(mapaCtrl);

        }
        recursos.add(recurso);
        return vista;

    }


    protected View ctrl(View v, int recurso, ArrayList<View> vistas,
                        ArrayList<EditMaterial> controles, ArrayList<Integer> recursos) {

        View vista = v.findViewById(recurso);
        vistas.add(vista);
        if (controles != null) {
            if (vista instanceof EditMaterial) {
                controles.add((EditMaterial) vista);
            }
        }
        if (recursos != null) {
            recursos.add(recurso);
        }

        return vista;

    }


    protected EditMaterial setEditMaterial(int recurso) {

        EditMaterial vista = view.findViewById(recurso);
        materialEdits.add(vista);
        return vista;

    }

    protected EditMaterial setEditMaterial(View v, ArrayList<EditMaterial> editMaterialList, int recurso) {

        EditMaterial vista = v.findViewById(recurso);
        editMaterialList.add(vista);
        return vista;

    }

    protected void setOnCronometro(Chronometer arg0) {

    }

    protected void setTimerEdit(Chronometer timerEdit) {

        timerg = timerEdit;
    }

    protected void startTimer() {

        timerg.start();
        onTimer = true;
        isOnTimer();
    }

    protected void stopTimer() {

        if (onTimer) {
            timerg.stop();
            onTimer = false;
            isOnTimer();
        }
    }

    protected boolean isOnTimer() {

        return onTimer;
    }

    protected long setCounterUp(Chronometer arg0) {

        if (JavaUtil.hoy() > arg0.getBase()) {
            return (JavaUtil.hoy() - arg0.getBase());
        } else {
            return (arg0.getBase() - JavaUtil.hoy());
        }
    }

    protected void vaciarControles() {


        for (View vista : vistas) {
            if (vista instanceof EditText) {
                ((EditText) vista).setText("");
            } else if (vista instanceof EditMaterial) {
                ((EditMaterial) vista).setText("");
            } else if (vista instanceof CheckBox) {
                ((CheckBox) vista).setChecked(false);
            } else if (vista instanceof ProgressBar) {
                ((ProgressBar) vista).setProgress(0);
            }
        }

        for (EditMaterialLayout materialEditLayout : materialEditLayouts) {
            materialEditLayout.setText("");
        }

    }

    protected void vaciarEditMaterial() {

        for (EditMaterial vista : materialEdits) {
            vista.setText("");
        }

        for (EditMaterialLayout materialEditLayout : materialEditLayouts) {
            materialEditLayout.setText("");
        }
    }

    protected void setSizeTextControles(float sizeText) {

        for (View vista : vistas) {
            if (vista instanceof AutoCompleteTextView) {
                ((AutoCompleteTextView) vista).setTextSize(sizeText);
            } else if (vista instanceof EditText) {
                ((EditText) vista).setTextSize(sizeText);
            } else if (vista instanceof EditMaterial) {
                ((EditMaterial) vista).setTextSize(activityBase);
            } else if (vista instanceof CheckBox) {
                ((CheckBox) vista).setTextSize(sizeText);
            } else if (vista instanceof TextView) {
                ((TextView) vista).setTextSize(sizeText);
            }
        }

        for (EditMaterialLayout materialEditLayout : materialEditLayouts) {
            materialEditLayout.setTextSize(activityBase);
        }

    }

    protected void putBundle(String key, Serializable serializable) {
        if (serializable != null) {
            bundle.putSerializable(key, serializable);
        }
    }

    protected void putBundleModelo(Serializable serializable) {

        if (bundle != null) {
            if (serializable != null) {
                bundle.putSerializable(MODELO, serializable);
            } else {
                Log.e(TAG, "Serializable nulo en bundle.putSerializable");
            }
        } else {
            Log.d(TAG, "Bundle nulo");
        }
    }

    protected void putBundle(String key, String string) {
        if (bundle != null) {
            if (string != null) {
                bundle.putString(key, string);
            } else {
                Log.e(TAG, key + " nulo en bundle.putString");
            }
        } else {
            Log.d(TAG, "Bundle nulo");
        }
    }

    protected void putBundle(String key, int integer) {
        bundle.putInt(key, integer);
    }

    protected void putBundle(String key, long largo) {
        bundle.putLong(key, largo);
    }

    protected void putBundle(String key, double doble) {
        bundle.putDouble(key, doble);
    }

    protected void putBundle(String key, boolean bool) {
        bundle.putBoolean(key, bool);
    }

    protected int getIntBundle(String key, int defValue) {
        return bundle.getInt(key, defValue);
    }

    protected long getLongBundle(String key, long defValue) {
        return bundle.getLong(key, defValue);
    }

    protected double getDoubleBundle(String key, double defValue) {
        return bundle.getDouble(key, defValue);
    }

    protected boolean getBooleanBundle(String key, boolean defValue) {
        return bundle.getBoolean(key, defValue);
    }

    protected String getStringBundle(String key, String defValue) {
        if (nn(bundle)) {
            return bundle.getString(key, defValue);
        }
        return NULL;
    }

    protected Serializable getBundleSerial(String key) {

        if (nn(bundle) && nn(bundle.getSerializable(key))) {
            return bundle.getSerializable(key);
        }
        return null;
    }

    protected void gone(View view) {

        view.setVisibility(View.GONE);

    }

    protected void visible(View view) {

        view.setVisibility(View.VISIBLE);

    }

    protected void allGone() {

        for (View vista : vistas) {

            vista.setVisibility(View.GONE);
        }
    }

    protected void allVisible() {

        for (View vista : vistas) {

            vista.setVisibility(View.VISIBLE);
        }
    }

    protected String getMetodo() {
        return Thread.currentThread().getStackTrace()[3].getMethodName();
    }

    public void reconocimientoVoz(int code) {

        if (CheckPermisos.validarPermisos(activityBase, READ_CONTACTS, 100) &&
                CheckPermisos.validarPermisos(activityBase, WRITE_CONTACTS, 100)) {

            Intent intentActionRecognizeSpeech = new Intent(
                    RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intentActionRecognizeSpeech.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
            intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            try {
                update();
                startActivityForResult(intentActionRecognizeSpeech,
                        code, null);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(contexto,
                        "Tú dispositivo no soporta el reconocimiento por voz",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected boolean update() {
        return false;
    }

    protected boolean sensorProximidad() {
        sensorManagerProx =
                (SensorManager) activityBase.getSystemService(SENSOR_SERVICE);

        proximitySensor =
                sensorManagerProx != null ? sensorManagerProx.getDefaultSensor(Sensor.TYPE_PROXIMITY) : null;

        if (proximitySensor == null) {
            Log.e(TAG, "Proximity sensor not available.");
            return false;
        }

        proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if ((sensorEvent.values[0] < proximitySensor.getMaximumRange() &&
                        sensorEvent.values[0] > 0.0f) ||
                        (sensorEvent.values[0] < proximitySensor.getMaximumRange() && valorLuz > 0.0f)) {
                    reconocimientoVoz(RECOGNIZE_SPEECH_ACTIVITY);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        return true;
    }

    protected boolean sensorLuz() {

        sensorManagerLuz = (SensorManager) activityBase.getSystemService(Context.SENSOR_SERVICE);
        mALS = sensorManagerLuz != null ? sensorManagerLuz.getDefaultSensor(Sensor.TYPE_LIGHT) : null;

        if (mALS == null) {
            Log.e(TAG, "sensor de luz not available.");
            return false;
        }

        sensorLuzListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                valorLuz = sensorEvent.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, getMetodo());

        if (resultCode == RESULT_OK) {

            if (requestCode == RECOGNIZE_SPEECH_ACTIVITY) {
                ArrayList<String> speech = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if (speech != null) {
                    grabarVoz = speech.get(0).toLowerCase();
                }

                if (grabarVoz.substring(0, 16).equals("llamar contacto ")) {

                    llamarContacto(grabarVoz.substring(16));

                } else if (grabarVoz.substring(0, 5).equals("ir a ")) {

                    activityBase.seleccionarDestino(grabarVoz.substring(5));

                } else if (grabarVoz.substring(0, 6).equals("crear ") ||
                        grabarVoz.substring(0, 6).equals("nuevo ")) {

                    activityBase.seleccionarNuevoDestino(grabarVoz.substring(6));

                } else if (grabarVoz.equals(Estilos.getString(contexto, "salir").toLowerCase())) {

                    activityBase.finish();

                }
            }

            code = 10000;
            for (EditMaterial materialEdit : materialEdits) {

                if (requestCode == code) {
                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    grabarVoz = speech.get(0);

                    materialEdit.setText(grabarVoz);
                    alCambiarCampos(materialEdit);
                    break;

                }
                code++;
            }

            for (EditMaterialLayout materialEdit : materialEditLayouts) {

                if (requestCode == code) {
                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    grabarVoz = speech.get(0);

                    materialEdit.setText(grabarVoz);
                    alCambiarCampos(materialEdit);
                    break;

                }
                code++;
            }
        }
    }



    protected void alCambiarCampos(EditMaterial editMaterial) {


    }

    protected void alCambiarCampos(EditMaterialLayout editMaterial) {


    }

    protected void llamarContacto(String contacto) {

        ContentResolver cr = contexto.getContentResolver();
        ArrayList<Contactos> listaContactos = AppActivity.registroContactos(cr);

        for (Contactos contactos : listaContactos) {

            if (contactos.getDatos().toLowerCase().equals(contacto)) {
                AppActivity.hacerLlamada(contexto, contactos.getNumero(), activityBase);
            }
        }
    }

    protected boolean nn(Object object) {
        return object != null;
    }

    protected boolean nnn(String string) {
        return string != null && !string.equals(NULL);
    }

    protected void changeFragment(Bundle bundle, Fragment fragment, int layout) {

        activityBase.reemplazaFragment(bundle, fragment, layout);
    }

    protected void addFragment(Bundle bundle, Fragment fragment, int layout) {

        fragment.setArguments(bundle);
        FragmentManager fr = getChildFragmentManager();//activityBase.getSupportFragmentManager();
        FragmentTransaction ft = fr.beginTransaction();
        ft.add(layout, fragment);
        ft.commit();
    }

    protected void removeFragment(Fragment fragment) {

        activityBase.eliminarFragment(fragment);

    }

    protected View addVista(int layout, ViewGroup viewGroup) {

        View view = null;
        if (layout > 0) {
            view = inflaterMain.inflate(layout, containerMain, false);
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view); // <- fix
            }
            viewGroup.addView(view);
            visible(viewGroup);

        } else {
            gone(viewGroup);
        }
        return view;
    }

    protected View addVista(View view, ViewGroup viewGroup) {

        viewGroup.addView(view);
        Estilos.setLayoutParams(view);

        return view;
    }

    public void actualizarArrays(ViewGroupLayout vista) {
        vistas.addAll(vista.getVistas());
        materialEdits.addAll(vista.getEditMaterials());
        camposEdit.addAll(vista.getCamposEdit());
        materialEditLayouts.addAll(vista.getEditMaterialLayouts());
    }

    protected int getId(String recurso) {
        return Estilos.getIdResource(contexto, recurso);
    }

    protected XmlResourceParser getLayout(String layout) {
        return Estilos.getLayout(contexto, layout);
    }

    protected int getIdLayout(String layout) {
        return Estilos.getIdLayout(contexto, layout);
    }

    protected int color(String color) {
        return Estilos.getIdColor(contexto, color);
    }

    protected String getString(String string) {
        return Estilos.getString(contexto, string);
    }

    protected int getIdString(String string) {
        return Estilos.getIdString(contexto, string);
    }

    protected int getIdDrawable(String drawable) {
        return Estilos.getIdDrawable(contexto, drawable);
    }

    protected void setPref(String key, String valor) {

        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, key, valor);
    }

    protected void setPref(String key, boolean valor) {

        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, key, valor);
    }

    protected void setPref(String key, int valor) {

        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, key, valor);
    }

    protected void setPref(String key, long valor) {

        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, key, valor);
    }

    protected void setPref(String key, float valor) {

        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, key, valor);
    }

    protected String getPref(String key, String defecto) {

        return AndroidUtil.getSharePreference(contexto, PREFERENCIAS, key, defecto);
    }

    protected boolean getPref(String key, boolean defecto) {

        return AndroidUtil.getSharePreference(contexto, PREFERENCIAS, key, defecto);
    }

    protected int getPref(String key, int defecto) {

        return AndroidUtil.getSharePreference(contexto, PREFERENCIAS, key, defecto);
    }

    protected long getPref(String key, long defecto) {

        return AndroidUtil.getSharePreference(contexto, PREFERENCIAS, key, defecto);
    }

    protected float getPref(String key, float defecto) {

        return AndroidUtil.getSharePreference(contexto, PREFERENCIAS, key, defecto);
    }

    public MainActivityBase getActivityBase() {
        return activityBase;
    }

    protected Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void onAfterSetDatos();

        void onBeforeSetdatos();
    }
}
