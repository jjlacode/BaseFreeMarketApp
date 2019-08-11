package com.jjlacode.base.util.android;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.Models.Contactos;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.base.util.animation.OneFrameLayout;
import com.jjlacode.base.util.interfaces.ICFragmentos;
import com.jjlacode.freelanceproject.CommonPry;
import com.jjlacode.freelanceproject.MainActivity;
import com.jjlacode.freelanceproject.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.SENSOR_SERVICE;

public abstract class FragmentBase extends Fragment implements JavaUtil.Constantes {

    protected String TAG;
    protected View view;
    protected int layout;
    protected MainActivityBase activityBase;
    protected MainActivity mainActivity;
    protected Context contexto;
    protected ICFragmentos icFragmentos;
    protected Bundle bundle;
    protected boolean land;
    protected boolean tablet;
    protected DisplayMetrics metrics;

    protected int ancho;
    protected int alto;
    protected int densidadDpi;
    protected float sizeText;
    protected boolean multiPanel;
    protected ArrayList<View> vistas;
    protected ArrayList<EditMaterial> materialEdits;
    protected ArrayList<Integer> recursos;

    protected RelativeLayout frPrincipal;
    protected LinearLayout frdetalle;
    protected LinearLayout frdetalleExtras;
    protected LinearLayout frPie;
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
    private Chronometer timerg;
    private boolean onTimer;
    protected LayoutInflater inflaterMain;
    protected ViewGroup containerMain;
    protected ImageButton btnsave;
    protected ImageButton btnback;
    protected ImageButton btndelete;
    protected OneFrameLayout frameAnimationCuerpo;
    protected ScrollView scrollDetalle;
    protected float densidad;
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
    protected ArrayList camposEdit;
    protected Timer timer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, getMetodo());

        setLayout();
        setLayoutExtra();
        setContext();

        metrics = new DisplayMetrics();
        activityBase.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        multiPanel = esMultiPanel(metrics);

        land = getResources().getBoolean(R.bool.esLand);
        tablet = getResources().getBoolean(R.bool.esTablet);
        densidad = metrics.density;
        ancho = (int) (metrics.widthPixels/densidad);
        alto = (int) (metrics.heightPixels/densidad);
        densidadDpi = (int) (metrics.densityDpi);

        sizeText = ((float) (ancho+alto+densidadDpi)/(100));
        System.out.println("sizeText = " + sizeText);

        materialEdits = new ArrayList<>();
        vistas = new ArrayList<>();
        recursos = new ArrayList<>();
        camposEdit = new ArrayList();
        TAG = getClass().getSimpleName();

        super.onCreate(savedInstanceState);
    }

    public boolean esMultiPanel(DisplayMetrics metrics) {
        Log.d(TAG, getMetodo());
        // Determinar que siempre sera multipanel
        return ((float)metrics.densityDpi / (float)metrics.widthPixels) < 0.30;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, getMetodo());
        inflaterMain = inflater;
        containerMain = container;

        view = inflater.inflate(R.layout.contenido, container, false);
        land = getResources().getBoolean(R.bool.esLand);
        tablet = getResources().getBoolean(R.bool.esTablet);
        System.out.println("land = " + land);
        System.out.println("tablet = " + tablet);

        if (layoutCuerpo==0) {
            layoutCuerpo = layout;
        }

        frPrincipal = view.findViewById(R.id.contenedor);
        frdetalle = view.findViewById(R.id.layout_detalle);
        frdetalleExtras = view.findViewById(R.id.layout_extras_detalle);
        frCabecera = view.findViewById(R.id.layout_cabecera);
        frPie = view.findViewById(R.id.layout_pie);
        frLista = view.findViewById(R.id.layout_rv);
        frCuerpo = view.findViewById(R.id.layout_cuerpo);
        scrollDetalle = view.findViewById(R.id.scrolldetalle);

        if (layoutCuerpo>0) {
            viewCuerpo = inflater.inflate(layoutCuerpo, container, false);
            if (viewCuerpo.getParent()!=null){
                ((ViewGroup)viewCuerpo.getParent()).removeView(viewCuerpo); // <- fix
            }
            if (viewCuerpo!=null) {
                frdetalle.addView(viewCuerpo);
                visible(frdetalle);
            }

        }else{
            gone(frdetalle);
        }

        if (layoutCabecera>0) {
            viewCabecera = inflater.inflate(layoutCabecera, container,false);
            if(viewCabecera.getParent() != null) {
                ((ViewGroup)viewCabecera.getParent()).removeView(viewCabecera); // <- fix
            }
            if (viewCabecera!=null) {
                frCabecera.addView(viewCabecera);
                visible(frCabecera);
            }
        }else{
            gone(frCabecera);
        }

        if (layoutPie>0){

            viewBotones = inflater.inflate(layoutPie,container,false);
            if(viewBotones.getParent() != null) {
                ((ViewGroup)viewBotones.getParent()).removeView(viewBotones); // <- fix
            }
            if (viewBotones!=null) {
                frPie.addView(viewBotones);
                visible(frPie);
            }else{
                gone(frPie);
            }

        }

        contexto = activityBase;

        frameAnimationCuerpo = view.findViewById(R.id.frameanimationcuerpo);

        frameAnimationCuerpo.setAncho((int) (ancho * densidad));

        gone(frLista);

        setOnCreateView(view,inflaterMain,containerMain);

        timerg = (Chronometer) view.findViewById(R.id.chronocrud);

        setInicio();

        setSizeTextControles(sizeText);

        AndroidUtil.ocultarTeclado(activityBase, view);

        frameAnimationCuerpo.setOnSwipeListener(new OneFrameLayout.OnSwipeListener() {
            @Override
            public void rightSwipe() {
                setOnRigthSwipeCuerpo();
            }

            @Override
            public void leftSwipe() {
                setOnLeftSwipeCuerpo();
            }
        });


        return view;
    }

    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container){

        Log.d(TAG, getMetodo());

        activityBase.fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reconocimientoVoz(RECOGNIZE_SPEECH_ACTIVITY);
            }
        });

        System.out.println("multipanel = "+esMultiPanel(metrics));
    }

    protected void setContext(){
        Log.d(TAG, getMetodo());

        contexto = activityBase;

    }

    protected void setLayoutExtra() {
        Log.d(TAG, getMetodo());

    }

    protected void setTAG(){
        TAG = getClass().getSimpleName();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, getMetodo());

        ayudaWeb = setAyudaWeb();
        icFragmentos.enviarAyudaWeb(ayudaWeb);

        if (!listenerSensorProx && sensorProximidad() ) {
            sensorManagerProx.registerListener(proximitySensorListener,
                    proximitySensor, 1000 * 1000);
            listenerSensorProx = true;
        }

        if (!listenerSensorLuz && listenerSensorProx && sensorLuz()){
            sensorManagerLuz.registerListener(sensorLuzListener,
                    mALS,1000 * 1000);
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

    protected void cargarBundle(){
        Log.d(TAG, getMetodo());

        bundle = getArguments();

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

    protected void acciones(){

        code = 10000;
        contCode = 0;
        codigo = new int[materialEdits.size()+1];
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


    }

    protected void enviarAct(){
        Log.d(TAG, getMetodo());

        bundle = new Bundle();
        bundle.putString(ORIGEN, origen);
        bundle.putString(ACTUAL, actual);
        bundle.putString(ACTUALTEMP, actualtemp);
        bundle.putString(SUBTITULO, subTitulo);
        System.out.println("Enviando bundle a MainActivity");
        icFragmentos.enviarBundleAActivity(bundle);
    }

    protected void enviarBundle(){
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
        SharedPreferences persistencia=getActivity().getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=persistencia.edit();

        switch(orientation ) {
            case Configuration.ORIENTATION_LANDSCAPE:
                // Con la orientación en horizontal actualizamos el adaptador
                editor.putString(ORIGEN, origen);
                editor.putString(ACTUAL, actual);
                editor.putString(ACTUALTEMP, actualtemp);
                editor.putString(SUBTITULO, subTitulo);
                editor.apply();
                break;
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

    protected String setAyudaWeb() {
        return null;
    }

    protected void setOnRigthSwipeCuerpo(){
        Log.d(TAG, getMetodo());
    }

    protected void setOnLeftSwipeCuerpo(){
        Log.d(TAG, getMetodo());
    }


    protected abstract void setLayout();

    protected abstract void setInicio();

    /**
     * Asigna el recurso al control en la vista actual
     * @param recurso Recurso del la vista actual que se asigna al Control
     */
    protected View ctrl(int recurso){

        View vista = view.findViewById(recurso);
        vista.setFocusable(false);
        vistas.add(vista);
        if (vista instanceof EditMaterial){
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
                        ArrayList<EditMaterial> controles, ArrayList<Integer> recursos){

        View vista = v.findViewById(recurso);
        vistas.add(vista);
        if (controles!=null) {
            if (vista instanceof EditMaterial) {
                controles.add((EditMaterial) vista);
            }
        }
        if (recursos!=null) {
            recursos.add(recurso);
        }

        return vista;

    }


    protected EditMaterial setEditMaterial(int recurso){

        EditMaterial vista = view.findViewById(recurso);
        materialEdits.add(vista);
        return vista;

    }

    protected EditMaterial setEditMaterial(View v, ArrayList<EditMaterial> editMaterialList, int recurso){

        EditMaterial vista = v.findViewById(recurso);
        editMaterialList.add(vista);
        return vista;

    }

    protected void setOnCronometro(Chronometer arg0){

        System.out.println("onTick base");
    }

    protected void setTimerEdit(Chronometer timerEdit) {

        timerg = timerEdit;
        System.out.println("Set Timer base");
    }

    protected void startTimer(){

        timerg.start();
        System.out.println("Start timerEdit");
        onTimer = true;
        isOnTimer();
    }

    protected void stopTimer(){

        if (onTimer) {
            timerg.stop();
            System.out.println("Stop timerEdit");
            onTimer = false;
            isOnTimer();
        }
    }

    protected boolean isOnTimer(){
        System.out.println("onTimer = "+onTimer);

        return onTimer;
    }

    protected long setCounterUp(Chronometer arg0){

        if (JavaUtil.hoy() > arg0.getBase()) {
            return (JavaUtil.hoy() - arg0.getBase()) ;
        } else {
            return (arg0.getBase() - JavaUtil.hoy()) ;
        }
    }

    protected void vaciarControles(){

        for (View vista : vistas) {
            if (vista instanceof EditText){
                ((EditText) vista).setText("");
            }else if (vista instanceof EditMaterial){
                ((EditMaterial) vista).setText("");
            }else if (vista instanceof CheckBox){
                ((CheckBox) vista).setChecked(false);
            }else if (vista instanceof ProgressBar){
                ((ProgressBar) vista).setProgress(0);
            }
        }

    }

    protected void vaciarEditMaterial(){

        for (EditMaterial vista : materialEdits) {
            vista.setText("");
        }
    }

    protected void setSizeTextControles(float sizeText){

        for (View vista : vistas) {
            if (vista instanceof AutoCompleteTextView) {
                ((AutoCompleteTextView) vista).setTextSize(sizeText);
            } else if (vista instanceof EditText) {
                ((EditText) vista).setTextSize(sizeText);
            }else if (vista instanceof EditMaterial){
                ((EditMaterial) vista).setTextSize(activityBase);
            }else if (vista instanceof CheckBox){
                ((CheckBox) vista).setTextSize(sizeText);
            }else if (vista instanceof TextView){
                ((TextView) vista).setTextSize(sizeText);
            }
        }

    }

    protected void putBundle(String key, Serializable serializable){
        if (serializable!=null) {
            bundle.putSerializable(key, serializable);
        }
    }

    protected void putBundleModelo(Serializable serializable){

        if (bundle!=null) {
            if (serializable != null) {
                bundle.putSerializable(MODELO, serializable);
            }else {
                Log.e(TAG,"Serializable nulo en bundle.putSerializable");
            }
        }else {
            Log.d(TAG,"Bundle nulo");
        }
    }

    protected void putBundle(String key, String string){
        if (bundle!=null) {
            if (string != null) {
                bundle.putString(key, string);
            }else {
                Log.e(TAG,key +" nulo en bundle.putString");
            }
        }else {
            Log.d(TAG,"Bundle nulo");
        }
    }

    protected void putBundle(String key, int integer){
            bundle.putInt(key, integer);
    }

    protected void putBundle(String key, long largo){
        bundle.putLong(key, largo);
    }

    protected void putBundle(String key, double doble){
        bundle.putDouble(key, doble);
    }

    protected void putBundle(String key, boolean bool){
        bundle.putBoolean(key, bool);
    }

    protected int getIntBundle(String key, int defValue){
        return bundle.getInt(key, defValue);
    }

    protected long getLongBundle(String key, long defValue){
        return bundle.getLong(key, defValue);
    }

    protected double getDoubleBundle(String key, double defValue){
        return bundle.getDouble(key, defValue);
    }

    protected boolean getBooleanBundle(String key, boolean defValue){
        return bundle.getBoolean(key, defValue);
    }

    protected String getStringBundle(String key, String defValue){
        return bundle.getString(key, defValue);
    }

    protected Serializable getBundleSerial(String key){
        return bundle.getSerializable(key);
    }

    protected void gone(View view){

        view.setVisibility(View.GONE);

    }

    protected void visible(View view){

        view.setVisibility(View.VISIBLE);

    }

    protected void allGone(){

        for (View vista : vistas) {

            vista.setVisibility(View.GONE);
        }
    }

    protected void allVisible(){

        for (View vista : vistas) {

            vista.setVisibility(View.VISIBLE);
        }
    }

    protected String getMetodo(){
        return Thread.currentThread().getStackTrace()[3].getMethodName();
    }

    public void reconocimientoVoz(int code){

        Intent intentActionRecognizeSpeech = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentActionRecognizeSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        try {
            update();
            startActivityForResult(intentActionRecognizeSpeech,
                    code,null);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(contexto,
                    "Tú dispositivo no soporta el reconocimiento por voz",
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected boolean update() {
        return false;
    }

    protected boolean sensorProximidad(){
        sensorManagerProx =
                (SensorManager) activityBase.getSystemService(SENSOR_SERVICE);

        proximitySensor =
                sensorManagerProx.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if(proximitySensor == null) {
            Log.e(TAG, "Proximity sensor not available.");
            return false;
        }

        proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if((sensorEvent.values[0] < proximitySensor.getMaximumRange() &&
                        sensorEvent.values[0] > 0.0f) ||
                        (sensorEvent.values[0] < proximitySensor.getMaximumRange() && valorLuz>0.0f)) {
                    reconocimientoVoz(RECOGNIZE_SPEECH_ACTIVITY);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        return true;
    }

    protected boolean sensorLuz(){

        sensorManagerLuz = (SensorManager)activityBase.getSystemService(Context.SENSOR_SERVICE);
        mALS = sensorManagerLuz.getDefaultSensor(Sensor.TYPE_LIGHT);

        if(mALS == null) {
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

    protected void imagenMediaPantalla(ImageView imagen){
        Log.d(TAG, getMetodo());

        if (!land) {
            imagen.setMinimumHeight((int) ((double) alto*densidad / 2));
            imagen.setMinimumWidth((int)(ancho*densidad));
            imagen.setMaxHeight((int) ((double) alto*densidad / 2));
            imagen.setMaxWidth((int)(ancho*densidad));
        }else{
            imagen.setMinimumWidth((int) ((double) ancho*densidad / 2));
            imagen.setMinimumHeight((int) ((double) alto*densidad / 2));
            imagen.setMaxWidth((int) ((double) ancho*densidad / 2));
            imagen.setMaxHeight((int) ((double) alto*densidad / 2));

        }

    }


    protected void imagenPantalla(ImageView imagen, int falto, int fancho){
        Log.d(TAG, getMetodo());
        int altotemp = (int)(alto*densidad);
        int anchotemp = (int)(ancho*densidad);

        System.out.println("ancho = " + anchotemp);
        System.out.println("alto = " + altotemp);
        System.out.println("densidad = " + densidad);

        if (!land) {
            imagen.setMinimumHeight((int) ((double) altotemp / (falto+2)));
            imagen.setMinimumWidth((int) ((double)anchotemp/(fancho+2)));
            imagen.setMaxHeight((int) ((double) altotemp / (falto+2)));
            imagen.setMaxWidth((int) ((double)anchotemp/(fancho+2)));
            //imagen.measure((int) ((double) (ancho*densidad) / (fancho)),(int) ((double) (alto) / (falto)));


        }else{
            imagen.setMinimumWidth((int) ((double) anchotemp / ((fancho*2)+2)));
            imagen.setMinimumHeight((int) ((double) (altotemp*2) / (falto+2)));
            imagen.setMaxWidth((int) ((double) anchotemp / ((fancho*2)+2)));
            imagen.setMaxHeight((int) ((double) (altotemp*2) / (falto+2)));
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

                    if (grabarVoz.length() >= 16 && grabarVoz.substring(0, 16).equals("llamar contacto ")) {

                        llamarContacto(grabarVoz.substring(16));

                    } else if (grabarVoz.length() >= 5 && grabarVoz.substring(0, 5).equals("ir a ")) {

                        CommonPry.seleccionarDestino(icFragmentos, bundle, grabarVoz.substring(5));

                    } else if (grabarVoz.length() >= 6 && (grabarVoz.substring(0, 6).equals("crear ") ||
                            grabarVoz.substring(0, 6).equals("nuevo "))) {

                        CommonPry.seleccionarNuevoDestino(icFragmentos, bundle, grabarVoz.substring(6));

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
        }
    }

    protected void alCambiarCampos(EditMaterial editMaterial) {


}

    protected void llamarContacto(String contacto) {

        ContentResolver cr = contexto.getContentResolver();
        ArrayList<Contactos> listaContactos = AppActivity.registroContactos(cr);

        for (Contactos contactos : listaContactos) {

            System.out.println("contacto = " + contacto);
            System.out.println("contactos = " + contactos.getDatos());
            if (contactos.getDatos().toLowerCase().equals(contacto)) {
                AppActivity.hacerLlamada(contexto, contactos.getNumero(), CommonPry.permiso);
            }
        }
    }

    protected boolean nn(Object object) {
        if (object!=null){
            return true;
        }
        return false;
    }
}
