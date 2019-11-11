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
import android.os.Bundle;
import android.speech.RecognizerIntent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.LockableScrollView;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.animation.OneFrameLayout;
import com.codevsolution.base.interfaces.ICFragmentos;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.models.Contactos;
import com.codevsolution.base.models.DestinosVoz;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.SENSOR_SERVICE;

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

    protected LinearLayout frdetalle;
    protected LinearLayout frdetalleExtraspost;
    protected LinearLayout frdetalleExtrasante;
    protected LinearLayout frPie;
    protected LinearLayout frPubli;
    protected LinearLayout frWeb;
    protected LinearLayout frCabecera;
    protected LinearLayout frLista;
    protected LinearLayout frCuerpo;

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
    protected OneFrameLayout frameAnimationCuerpo;
    protected LockableScrollView scrollDetalle;
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
    protected boolean autoGuardado = true;
    protected int tiempoGuardado = 1;
    protected int sizeTextD;
    protected boolean swipeOn;
    protected boolean modulo;
    protected FragmentBase fragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, getMetodo());

        contexto = activityBase;
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

        land = getResources().getBoolean(R.bool.esLand);
        tablet = getResources().getBoolean(R.bool.esTablet);
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

        super.onCreate(savedInstanceState);
    }

    public boolean esMultiPanel(DisplayMetrics metrics) {
        Log.d(TAG, getMetodo());
        // Determinar que siempre sera multipanel
        return ((float) metrics.densityDpi / (float) metrics.widthPixels) < 0.30;
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

        if (modulo) {

            view = inflater.inflate(R.layout.modulo, container, false);

            frdetalle = view.findViewById(R.id.layout_detalle_mod);
            frdetalleExtraspost = view.findViewById(R.id.layout_extras_post_detalle_mod);
            frdetalleExtrasante = view.findViewById(R.id.layout_extras_antes_detalle_mod);
            frCabecera = view.findViewById(R.id.layout_cabecera_mod);
            frPie = view.findViewById(R.id.layout_pie_mod);
            frWeb = view.findViewById(R.id.layout_web_detalle_mod);
            frPubli = view.findViewById(R.id.layout_publi_mod);
            frLista = view.findViewById(R.id.layout_rv_mod);
            frCuerpo = view.findViewById(R.id.layout_cuerpo_mod);
            scrollDetalle = view.findViewById(R.id.scrolldetalle_mod);
            frameAnimationCuerpo = view.findViewById(R.id.frameanimationcuerpo_mod);
            timerg = view.findViewById(R.id.chronocrud_mod);

        } else {

            view = inflater.inflate(R.layout.contenido, container, false);

            frdetalle = view.findViewById(R.id.layout_detalle);
            frdetalleExtraspost = view.findViewById(R.id.layout_extras_post_detalle);
            frdetalleExtrasante = view.findViewById(R.id.layout_extras_antes_detalle);
            frCabecera = view.findViewById(R.id.layout_cabecera);
            frPie = view.findViewById(R.id.layout_pie);
            frWeb = view.findViewById(R.id.layout_web_detalle);
            frPubli = view.findViewById(R.id.layout_publi);
            frLista = view.findViewById(R.id.layout_rv);
            frCuerpo = view.findViewById(R.id.layout_cuerpo);
            scrollDetalle = view.findViewById(R.id.scrolldetalle);
            frameAnimationCuerpo = view.findViewById(R.id.frameanimationcuerpo);
            timerg = view.findViewById(R.id.chronocrud);
        }

        if (layoutCuerpo == 0) {
            layoutCuerpo = layout;
        }

        land = getResources().getBoolean(R.bool.esLand);
        tablet = getResources().getBoolean(R.bool.esTablet);
        System.out.println("land = " + land);
        System.out.println("tablet = " + tablet);


        if (layoutCuerpo > 0) {
            viewCuerpo = inflater.inflate(layoutCuerpo, container, false);
            if (viewCuerpo.getParent() != null) {
                ((ViewGroup) viewCuerpo.getParent()).removeView(viewCuerpo); // <- fix
            }
            if (viewCuerpo != null) {
                frdetalle.addView(viewCuerpo);
                visible(frdetalle);
            }

        } else {
            gone(frdetalle);
        }

        if (layoutCabecera > 0) {
            viewCabecera = inflater.inflate(layoutCabecera, container, false);
            if (viewCabecera.getParent() != null) {
                ((ViewGroup) viewCabecera.getParent()).removeView(viewCabecera); // <- fix
            }
            if (viewCabecera != null) {
                frCabecera.addView(viewCabecera);
                visible(frCabecera);
            }
        } else {
            gone(frCabecera);
        }

        if (layoutPie > 0) {

            viewBotones = inflater.inflate(layoutPie, container, false);
            if (viewBotones.getParent() != null) {
                ((ViewGroup) viewBotones.getParent()).removeView(viewBotones); // <- fix
            }
            if (viewBotones != null) {
                frPie.addView(viewBotones);
                visible(frPie);
            } else {
                gone(frPie);
            }

        }

        contexto = activityBase;

        System.out.println("frameAnimationCuerpo = " + frameAnimationCuerpo);

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

        if (modulo) {
            setModulo();
        }

        System.out.println("Es modulo = " + modulo);

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

        System.out.println("multipanel = " + esMultiPanel(metrics));
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

    public LinearLayout getFrdetalle() {
        return frdetalle;
    }

    public LinearLayout getFrdetalleExtraspost() {
        return frdetalleExtraspost;
    }

    public LinearLayout getFrdetalleExtrasante() {
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

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, getMetodo());

        if (context instanceof MainActivityBase) {
            this.activityBase = (MainActivityBase) context;
            icFragmentos = this.activityBase;

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

    protected void setModulo() {

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

        activityBase.fabInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icFragmentos.enviarBundleAFragment(bundle, Interactor.fragmentMenuInicio);
            }
        });


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
        SharedPreferences persistencia = getActivity().getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = persistencia.edit();

        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                // Con la orientación en horizontal actualizamos el adaptador

            case Configuration.ORIENTATION_PORTRAIT:
                // Con la orientación en vertical actualizamos el adaptador
                editor.putString(ORIGEN, origen);
                editor.putString(ACTUAL, actual);
                editor.putString(ACTUALTEMP, actualtemp);
                editor.putString(SUBTITULO, subTitulo);
                editor.apply();
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

        System.out.println("onTick base");
    }

    protected void setTimerEdit(Chronometer timerEdit) {

        timerg = timerEdit;
        System.out.println("Set Timer base");
    }

    protected void startTimer() {

        timerg.start();
        System.out.println("Start timerEdit");
        onTimer = true;
        isOnTimer();
    }

    protected void stopTimer() {

        if (onTimer) {
            timerg.stop();
            System.out.println("Stop timerEdit");
            onTimer = false;
            isOnTimer();
        }
    }

    protected boolean isOnTimer() {
        System.out.println("onTimer = " + onTimer);

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

        System.out.println("Vaciando controles");

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
                sensorManagerProx.getDefaultSensor(Sensor.TYPE_PROXIMITY);

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
        mALS = sensorManagerLuz.getDefaultSensor(Sensor.TYPE_LIGHT);

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

    protected void imagenMediaPantalla(ImageView imagen) {
        Log.d(TAG, getMetodo());

        if (!land) {
            imagen.setMinimumHeight((int) ((double) alto * densidad / 2));
            imagen.setMinimumWidth((int) (ancho * densidad));
            imagen.setMaxHeight((int) ((double) alto * densidad / 2));
            imagen.setMaxWidth((int) (ancho * densidad));
        } else {
            imagen.setMinimumWidth((int) ((double) ancho * densidad / 2));
            imagen.setMinimumHeight((int) ((double) alto * densidad / 2));
            imagen.setMaxWidth((int) ((double) ancho * densidad / 2));
            imagen.setMaxHeight((int) ((double) alto * densidad / 2));

        }

    }


    protected void imagenPantalla(ImageView imagen, int falto, int fancho) {
        Log.d(TAG, getMetodo());
        int altotemp = (int) (alto * densidad);
        int anchotemp = (int) (ancho * densidad);

        System.out.println("ancho = " + anchotemp);
        System.out.println("alto = " + altotemp);
        System.out.println("densidad = " + densidad);

        if (!land) {
            imagen.setMinimumHeight((int) ((double) altotemp / (falto + 2)));
            imagen.setMinimumWidth((int) ((double) anchotemp / (fancho + 2)));
            imagen.setMaxHeight((int) ((double) altotemp / (falto + 2)));
            imagen.setMaxWidth((int) ((double) anchotemp / (fancho + 2)));
            //imagen.measure((int) ((double) (ancho*densidad) / (fancho)),(int) ((double) (alto) / (falto)));


        } else {
            imagen.setMinimumWidth((int) ((double) anchotemp / ((fancho * 2) + 2)));
            imagen.setMinimumHeight((int) ((double) (altotemp * 2) / (falto + 2)));
            imagen.setMaxWidth((int) ((double) anchotemp / ((fancho * 2) + 2)));
            imagen.setMaxHeight((int) ((double) (altotemp * 2) / (falto + 2)));
            //imagen.measure((int) ((double) (ancho*densidad) / ((fancho*2))),(int) ((double) (alto*2) / (falto)));

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, getMetodo());

        System.out.println("requestCode = " + requestCode);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case RECOGNIZE_SPEECH_ACTIVITY:

                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    grabarVoz = speech.get(0).toLowerCase();

                    if (grabarVoz.substring(0, 16).equals("llamar contacto ")) {

                        llamarContacto(grabarVoz.substring(16));

                    } else if (grabarVoz.substring(0, 5).equals("ir a ")) {

                        seleccionarDestino(grabarVoz.substring(5));

                    } else if (grabarVoz.substring(0, 6).equals("crear ") ||
                            grabarVoz.substring(0, 6).equals("nuevo ")) {

                        seleccionarNuevoDestino(grabarVoz.substring(6));

                    } else if (grabarVoz.equals(getString(R.string.salir).toLowerCase())) {

                        activityBase.finish();

                    }
            }

            code = 10000;
            for (EditMaterial materialEdit : materialEdits) {

                if (requestCode == code) {
                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);
                    grabarVoz = strSpeech2Text;

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
                    String strSpeech2Text = speech.get(0);
                    grabarVoz = strSpeech2Text;

                    materialEdit.setText(grabarVoz);
                    alCambiarCampos(materialEdit);
                    break;

                }
                code++;
            }
        }
    }

    public void seleccionarDestino(String destino) {

        ArrayList<DestinosVoz> listaDestinos = Interactor.getListaDestinosVoz();

        for (DestinosVoz destinosVoz : listaDestinos) {

            if (destino.equals(destinosVoz.getDestino())) {
                icFragmentos.enviarBundleAFragment(null, destinosVoz.getFragment());
            }
        }


    }

    public void seleccionarNuevoDestino(String destino) {

        ArrayList<DestinosVoz> listaDestinos = Interactor.getListaNuevosDestinosVoz();

        for (DestinosVoz destinosVoz : listaDestinos) {

            if (destino.equals(destinosVoz.getDestino())) {
                icFragmentos.enviarBundleAFragment(null, destinosVoz.getFragment());
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

            System.out.println("contacto = " + contacto);
            System.out.println("contactos = " + contactos.getDatos());
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

        activityBase.addFragment(bundle, fragment, layout);
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
        return Estilos.getIdStrig(contexto, string);
    }

    protected int getIdDrawable(String drawable) {
        return Estilos.getIdDrawable(contexto, drawable);
    }

}
