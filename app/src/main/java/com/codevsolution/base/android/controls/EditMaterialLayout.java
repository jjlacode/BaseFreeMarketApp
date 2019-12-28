package com.codevsolution.base.android.controls;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentActivity;

import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.interfaces.ICFragmentos;
import com.codevsolution.base.style.Estilos;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class EditMaterialLayout implements Estilos.Constantes {

    private LinearLayoutCompat linearLayout;
    private ViewGroup viewGroup;
    private EditText editText;
    private TextInputLayout textInputLayout;
    private ImageButton btnInicio;
    private ImageButton btnAccion;
    private ImageButton btnAccion2;
    private ImageButton btnAccion3;
    private Button btnText;
    private AlCambiarListener listener;
    private AudioATexto grabarListener;
    private TextToSpeech tts;
    private CambioFocoEdit listenerFoco;
    private ClickAccion listenerAccion;
    private ClickAccion2 listenerAccion2;
    private ClickAccion3 listenerAccion3;
    private ClickAccionTxt listenerAccionTxt;
    private int posicion;
    DisplayMetrics metrics = new DisplayMetrics();
    private boolean textoCambiado;
    private String textoEdit;
    private AppCompatActivity activity;
    private ICFragmentos icFragmentos;
    private int weight = 5;
    private boolean valido;
    private boolean obligatorio;
    private String asunto;
    private String mensaje;
    private String path;
    private int id;
    private boolean playOn;

    public static final int NUMERO = 2;
    public static final int TEXTO = 1;
    public static final int TELEFONO = 3;
    public static final int FECHA = 4;
    public static final int DECIMAL = 8192;
    public static final int EMAIL = 32;
    public static final int ASUNTO = 48;
    public static final int MENSAJE = 80;
    public static final int MULTI = 131072;
    public static final int IMEMULTI = 262144;
    public static final int DIRECCION = 112;
    public static final int PASS = 128;
    public static final int PERSONA = 96;
    public static final int URI = 16;
    public static final int CAPINI = 16384;
    public static final int CAPPALABRAS = 8192;
    public static final int CAP = 4096;
    public static final int AUTO = 65536;
    public static final int SIGNO = 4096;
    public static final int NUMEROPASS = 16;
    public static final int NULO = 0;
    private CharSequence msgError;
    private int tipoDato;
    private int gravedad;
    private int colorFondo;
    private boolean activo;
    private boolean excedido;
    private Context context;
    private String nombreLlamada;
    private boolean isPlay;


    public EditMaterialLayout(ViewGroup viewGroup, Context context) {
        this.context = context;
        this.viewGroup = viewGroup;
        inicializar();
    }

    public EditMaterialLayout(ViewGroup viewGroup, Context context, int hint) {
        this.context = context;
        this.viewGroup = viewGroup;

        inicializar();
        setHint(context.getString(hint));

    }

    public EditMaterialLayout(ViewGroup viewGroup, Context context, String hint) {
        this.context = context;
        this.viewGroup = viewGroup;

        inicializar();
        setHint(hint);

    }

    public EditMaterialLayout(ViewGroup viewGroup, Context context, int hint, boolean activo) {
        this.context = context;
        this.viewGroup = viewGroup;

        inicializar();
        setHint(context.getString(hint));
        setActivo(activo);

    }

    public EditMaterialLayout(ViewGroup viewGroup, Context context, String hint, boolean activo) {
        this.context = context;
        this.viewGroup = viewGroup;

        inicializar();
        setHint(hint);
        setActivo(activo);

    }

    public EditMaterialLayout(ViewGroup viewGroup, Context context, ICFragmentos icFragmentos) {
        this.icFragmentos = icFragmentos;
        this.context = context;
        this.viewGroup = viewGroup;
        inicializar();
    }

    public EditMaterialLayout(ViewGroup viewGroup, Context context, int hint, ICFragmentos icFragmentos) {
        this.icFragmentos = icFragmentos;
        this.context = context;
        this.viewGroup = viewGroup;

        inicializar();
        setHint(context.getString(hint));

    }

    public EditMaterialLayout(ViewGroup viewGroup, Context context, String hint, ICFragmentos icFragmentos) {
        this.icFragmentos = icFragmentos;
        this.context = context;
        this.viewGroup = viewGroup;

        inicializar();
        setHint(hint);

    }

    public EditMaterialLayout(ViewGroup viewGroup, Context context, int hint, boolean activo, ICFragmentos icFragmentos) {
        this.icFragmentos = icFragmentos;
        this.context = context;
        this.viewGroup = viewGroup;

        inicializar();
        setHint(context.getString(hint));
        setActivo(activo);

    }

    public EditMaterialLayout(ViewGroup viewGroup, Context context, String hint, boolean activo, ICFragmentos icFragmentos) {
        this.icFragmentos = icFragmentos;
        this.context = context;
        this.viewGroup = viewGroup;

        inicializar();
        setHint(hint);
        setActivo(activo);

    }

    private void inicializar() {

        setViewGroup();
        asignarEventos();
        setWeigthLayout();
        comprobarEdit();
        id = View.generateViewId();
        btnInicio.setFocusable(false);
        btnAccion.setFocusable(false);
        btnAccion2.setFocusable(false);
        btnText.setFocusable(false);
        setPlayOn(true);
    }

    public void setViewGroup() {

        linearLayout = new LinearLayoutCompat(context);
        linearLayout.setOrientation(LinearLayoutCompat.HORIZONTAL);
        viewGroup.addView(linearLayout);
        //setLayoutParams(linearLayout);
        btnInicio = new ImageButton(context);
        btnInicio.setImageResource(context.getResources().
                getIdentifier("ic_rec_indigo", DRAWABLE, context.getPackageName()));
        btnInicio.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        btnInicio.setBackground(Estilos.getBotonSecondary());
        linearLayout.addView(btnInicio);
        textInputLayout = new TextInputLayout(context, null, context.getResources().
                getIdentifier("TextInputLayout", "style", context.getPackageName()));
        linearLayout.addView(textInputLayout);
        editText = new TextInputEditText(context);
        textInputLayout.addView(editText);
        btnAccion = new ImageButton(context);
        btnAccion.setVisibility(View.GONE);
        btnAccion.setBackground(Estilos.getBotonSecondary());
        btnAccion.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        linearLayout.addView(btnAccion);
        btnAccion2 = new ImageButton(context);
        btnAccion2.setVisibility(View.GONE);
        btnAccion2.setBackground(Estilos.getBotonPrimaryStroke());
        btnAccion2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        linearLayout.addView(btnAccion2);
        btnAccion3 = new ImageButton(context);
        btnAccion3.setVisibility(View.GONE);
        btnAccion3.setBackground(Estilos.getBotonPrimary());
        btnAccion3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        linearLayout.addView(btnAccion3);
        btnText = new Button(context);
        btnText.setVisibility(View.GONE);
        btnText.setBackground(Estilos.getBotonSecondary());
        linearLayout.addView(btnText);
    }

    public int getId() {
        return id;
    }

    public LinearLayoutCompat getLinearLayout() {
        return linearLayout;
    }

    public ViewGroup getViewGroup() {
        return linearLayout;
    }

    protected void setLayoutParams(View view) {

        ViewGroup.LayoutParams params;
        if (linearLayout.getOrientation() == LinearLayoutCompat.VERTICAL) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

        }
        view.setLayoutParams(params);

    }

    public void setTTS(boolean enable) {

        if (enable) {

            btnAccion2Enable(true);
            setImgBtnAccion2(Estilos.getIdDrawable(context, "ic_play_indigo"));

            btnAccion2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    reproducir();
                }
            });
        } else {

            btnAccion2Enable(false);
            if (tts != null) {
                tts.stop();
                tts.shutdown();
            }
        }
    }

    public void setPlayOn(boolean enable) {
        playOn = enable;
    }

    public boolean isPlayOn() {
        return playOn;
    }

    public void reproducir() {
        reproducir(getTexto());
    }

    public void reproducir(String text) {

        System.out.println("text to speech= " + text);
        if (icFragmentos != null) {
            TextToSpeech ttsIcf = icFragmentos.getTTs();
            isPlay = true;
            speakOut(text, ttsIcf);

        } else {

            tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = tts.setLanguage(Locale.getDefault());

                        if (result == TextToSpeech.LANG_MISSING_DATA
                                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "This Language is not supported");
                        } else {
                            isPlay = true;
                            speakOut(text);
                        }
                    } else {
                        Log.e("TTS", "Initilization Failed!");
                    }
                }
            });
        }
    }

    private void onEndPlayTTs() {

    }

    public void speakOut(String text) {

        if (text == null || text.isEmpty())
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String utteranceId = this.hashCode() + "";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                isPlay = true;
            }

            @Override
            public void onDone(String utteranceId) {

                isPlay = false;

                onEndPlayTTs();
            }

            @Override
            public void onError(String utteranceId) {

                isPlay = false;

            }
        });
    }

    public void speakOut(String text, TextToSpeech tts) {

        if (text == null || text.isEmpty())
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String utteranceId = this.hashCode() + "";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                isPlay = true;
            }

            @Override
            public void onDone(String utteranceId) {

                isPlay = false;

                onEndPlayTTs();
            }

            @Override
            public void onError(String utteranceId) {

                isPlay = false;

            }
        });
    }

    public void setAlCambiarListener(AlCambiarListener l) {
        this.listener = l;
    }

    public void setGrabarListener(AudioATexto grabarListener) {
        this.grabarListener = grabarListener;
    }

    public void setCambioFocoListener(CambioFocoEdit listener) {
        listenerFoco = listener;
    }

    public void setClickAccion(ClickAccion listener) {
        listenerAccion = listener;
    }

    public void setClickAccion2(ClickAccion2 listener) {
        listenerAccion2 = listener;
    }

    public void setClickAccion3(ClickAccion3 listener) {
        listenerAccion3 = listener;
    }

    public void setClickAccionTxt(ClickAccionTxt listener) {
        listenerAccionTxt = listener;
    }

    private void asignarEventos() {

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (listener != null) {
                    listener.antesCambio(s, start, count, after);
                }
                textoEdit = getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (listener != null) {
                    listener.cambiando(s, start, before, count);
                }
                if (!textoEdit.equals(getText().toString())) {
                    textoCambiado = true;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (listener != null) {
                    listener.despuesCambio(s);
                }
                comprobarEdit();
            }
        });

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (grabarListener != null) {
                    grabarListener.onGrabar(view, posicion);
                }
            }
        });

        btnAccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listenerAccion != null) {
                    listenerAccion.onClickAccion(view);
                }

            }
        });

        btnAccion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listenerAccion2 != null) {
                    listenerAccion2.onClickAccion2(view);
                }

            }
        });

        btnAccion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listenerAccion3 != null) {
                    listenerAccion3.onClickAccion3(view);
                }

            }
        });

        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listenerAccionTxt != null) {
                    listenerAccionTxt.onClickAccionTxt(view);
                }

            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (!b && textoCambiado) {
                    if (listenerFoco != null) {
                        listenerFoco.alPerderFoco(view);
                    }
                    textoCambiado = false;

                }
                if (b) {
                    if (listenerFoco != null) {
                        listenerFoco.alRecibirFoco(view);
                        editText.setBackgroundColor(Estilos.colorSecondary);
                        editText.setTextColor(Estilos.colorSecondaryDark);
                    }
                } else {
                    editText.setBackgroundColor(Estilos.colorPrimary);
                    editText.setTextColor(Estilos.colorSecondaryDark);
                    comprobarEdit();
                }
            }
        });


    }

    private void setWeigthLayout() {

        int vis = btnInicio.getVisibility() + btnAccion.getVisibility() + btnAccion2.getVisibility()
                + btnAccion3.getVisibility() + btnText.getVisibility();

        switch (vis) {

            case 32:
                weight = 9;
                break;
            case 24:
                weight = 8;
                break;
            case 20:
                weight = 5;
                break;
            case 16:
                weight = 5;
                break;
            case 8:
                weight = 4;
                break;
        }

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, weight);
        params.gravity = Gravity.CENTER_VERTICAL;
        btnInicio.setLayoutParams(params);
        btnInicio.setAdjustViewBounds(true);
        btnAccion.setLayoutParams(params);
        btnAccion.setAdjustViewBounds(true);
        btnAccion2.setLayoutParams(params);
        btnAccion2.setAdjustViewBounds(true);
        btnAccion3.setLayoutParams(params);
        btnAccion3.setAdjustViewBounds(true);
        params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, weight - 2);
        params.gravity = Gravity.CENTER_VERTICAL;
        btnText.setLayoutParams(params);
        btnText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        if (btnText.getVisibility() == View.VISIBLE) {
            params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, weight - 2);
            textInputLayout.setLayoutParams(params);
        } else {
            params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            textInputLayout.setLayoutParams(params);
        }

        FrameLayout.LayoutParams paramst = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(paramst);

    }

    public void setFondo(int colorFondo) {
        textInputLayout.setBoxBackgroundColor(colorFondo);

    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setHint(String hint) {

        textInputLayout.setHint(hint);
    }

    public void grabarEnable(boolean enable) {

        if (enable) {
            btnInicio.setVisibility(View.VISIBLE);
        } else {
            btnInicio.setVisibility(View.GONE);
        }
        setWeigthLayout();
    }

    public void btnInicioEnable(boolean enable) {

        if (enable) {
            btnInicio.setVisibility(View.VISIBLE);
        } else {
            btnInicio.setVisibility(View.GONE);
        }
        setWeigthLayout();

    }

    public void btnInicioVisible(boolean enable) {

        if (enable) {
            btnInicio.setVisibility(View.VISIBLE);
        } else {
            btnInicio.setVisibility(View.INVISIBLE);
        }
        setWeigthLayout();

    }

    public void btnAccionEnable(boolean enable) {

        if (enable) {
            btnAccion.setVisibility(View.VISIBLE);
        } else {
            btnAccion.setVisibility(View.GONE);
        }
        setWeigthLayout();

    }

    public void btnAccionVisible(boolean enable) {

        if (enable) {
            btnAccion.setVisibility(View.VISIBLE);
        } else {
            btnAccion.setVisibility(View.INVISIBLE);
        }
        setWeigthLayout();

    }

    public void btnAccion2Visible(boolean enable) {

        if (enable) {
            btnAccion2.setVisibility(View.VISIBLE);
        } else {
            btnAccion2.setVisibility(View.INVISIBLE);
        }
    }

    public void btnAccion3Visible(boolean enable) {

        if (enable) {
            btnAccion3.setVisibility(View.VISIBLE);
        } else {
            btnAccion3.setVisibility(View.INVISIBLE);
        }
    }

    public void btnAccion2Enable(boolean enable) {

        if (enable) {
            btnAccion2.setVisibility(View.VISIBLE);
        } else {
            btnAccion2.setVisibility(View.GONE);
        }
        setWeigthLayout();

    }

    public void btnAccion3Enable(boolean enable) {

        if (enable) {
            btnAccion3.setVisibility(View.VISIBLE);
        } else {
            btnAccion3.setVisibility(View.GONE);
        }
        setWeigthLayout();

    }

    public void btnAccionTxtEnable(boolean enable) {

        if (enable) {
            btnText.setVisibility(View.VISIBLE);
        } else {
            btnText.setVisibility(View.GONE);
        }
        setWeigthLayout();

    }

    public void setImgBtnAccion(int recurso) {

        btnAccionEnable(true);
        btnAccion.setImageResource(recurso);
    }

    public void setImgBtnAccion2(int recurso) {

        btnAccion2Enable(true);
        btnAccion2.setImageResource(recurso);
    }

    public void setImgBtnAccion3(int recurso) {

        btnAccion3Enable(true);
        btnAccion3.setImageResource(recurso);
    }

    public void setTextBtnTxt(String textoBtn) {

        btnAccionTxtEnable(true);
        btnText.setText(textoBtn);
    }

    public void setTextBtnTxt(int recursoString) {

        btnAccionTxtEnable(true);
        btnText.setText(recursoString);
    }

    public void setTipo(int tipo) {

        tipoDato = tipo;
        editText.setInputType(tipo);
    }

    public void setAccionLlamada(AppCompatActivity activityCompat, ClickAccion listenerAccion) {

        btnAccionEnable(true);
        setImgBtnAccion(context.getResources().
                getIdentifier("ic_llamada_indigo", DRAWABLE,
                        context.getPackageName()));
        activity = activityCompat;
        this.listenerAccion = listenerAccion;
    }

    public void setAccionFecha(ClickAccion listenerAccion) {

        btnAccionEnable(true);
        setActivo(false);
        setImgBtnAccion(context.getResources().
                getIdentifier("ic_evento_indigo", DRAWABLE,
                        context.getPackageName()));
        this.listenerAccion = listenerAccion;
    }

    public void setAccionVerMapa(ClickAccion listenerAccion) {

        btnAccionEnable(true);
        setImgBtnAccion(context.getResources().
                getIdentifier("ic_lugar_indigo", DRAWABLE,
                        context.getPackageName()));
        this.listenerAccion = listenerAccion;
    }

    public void setAccionEnviarMail(ClickAccion listenerAccion) {

        btnAccionEnable(true);
        setImgBtnAccion(context.getResources().
                getIdentifier("ic_email_indigo", DRAWABLE,
                        context.getPackageName()));

        this.listenerAccion = listenerAccion;
    }

    public void setAccionVerWeb(ClickAccion listenerAccion) {

        btnAccionEnable(true);
        setImgBtnAccion(context.getResources().
                getIdentifier("ic_web_indigo", DRAWABLE,
                        context.getPackageName()));

        this.listenerAccion = listenerAccion;
    }

    public void enviarEmail() {

        if (valido) {
            if (nombreLlamada != null && !nombreLlamada.isEmpty()) {
                reproducir(Estilos.getString(context, "enviando_correo") + " " + nombreLlamada);
            } else {
                reproducir(Estilos.getString(context, "enviando_correo") + " " + getTexto());
            }
            AppActivity.enviarEmail(context, getTexto());
        }

    }

    public void enviarEmail(String asunto, String texto) {

        if (valido) {

            if (nombreLlamada != null && !nombreLlamada.isEmpty()) {
                reproducir(Estilos.getString(context, "enviando_correo") + " " + nombreLlamada);
            } else {
                reproducir(Estilos.getString(context, "enviando_correo") + " " + getTexto());
            }
            AppActivity.enviarEmail(context, getTexto(), asunto, texto);
        }

    }

    public void enviarEmail(String asunto, String texto, String path) {

        if (valido) {

            if (nombreLlamada != null && !nombreLlamada.isEmpty()) {
                reproducir(Estilos.getString(context, "enviando_correo") + " " + nombreLlamada);
            } else {
                reproducir(Estilos.getString(context, "enviando_correo") + " " + getTexto());
            }
            AppActivity.enviarEmail(context, getTexto(), asunto, texto, path);
        }

    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setPathEmail(String path) {
        this.path = path;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getPath() {
        return path;
    }

    public void verEnMapa() {

        if (!getTexto().equals("")) {

            if (nombreLlamada != null && !nombreLlamada.isEmpty()) {
                reproducir(Estilos.getString(context, "buscar_direccion_de") + " " + nombreLlamada + " " + getTexto());
            } else {
                reproducir(Estilos.getString(context, "buscar_direccion") + " " + getTexto());
            }
            AppActivity.viewOnMapA(context, getTexto());

        }
    }

    public void llamar() {

        if (valido) {

            if (nombreLlamada != null && !nombreLlamada.isEmpty()) {
                reproducir(Estilos.getString(context, "llamando") + " " + nombreLlamada);
            } else {
                reproducir(Estilos.getString(context, "llamando") + " " + getTexto());
            }

            if (icFragmentos != null) {
                int seg = 0;
                while (isPlay || seg == 10) {

                    try {
                        Thread.sleep(500);
                        System.out.println("seg = " + seg);
                        seg++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            AppActivity.hacerLlamada(context, getTexto(), activity);
        }
    }

    public void verWeb() {

        if (valido) {
            AppActivity.verWeb(context, getTexto(), activity);
        }

    }

    public void setText(String text) {
        editText.setText(text);
        comprobarEdit();
    }

    public void setText(int text) {
        editText.setText(String.valueOf(text));
        comprobarEdit();
    }

    public void setText(int text, boolean recurso) {
        if (recurso) {
            editText.setText(text);
        } else {
            editText.setText(String.valueOf(text));
        }
        comprobarEdit();
    }

    public void setText(double text) {
        editText.setText(String.valueOf(text));
        comprobarEdit();
    }

    public void setText(long text) {
        editText.setText(String.valueOf(text));
        comprobarEdit();
    }

    public void setText(float text) {
        editText.setText(String.valueOf(text));
        comprobarEdit();
    }

    public void setText(boolean text) {
        editText.setText(String.valueOf(text));
        comprobarEdit();
    }

    public void setGravedad(int gravedad) {
        editText.setGravity(gravedad);
    }

    public void setTextSize(AppCompatActivity activityCompat) {

        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int ancho = metrics.widthPixels;
        int alto = metrics.heightPixels;

        float size = ((float) (alto * ancho) / (metrics.densityDpi * 300));
        editText.setTextSize(size);
        activity = activityCompat;
    }

    public void setTextSize(FragmentActivity activityCompat) {

        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float ancho = metrics.widthPixels;
        float alto = metrics.heightPixels;


        float size = ((alto * ancho) / (metrics.densityDpi * 300));
        editText.setTextSize(size);
    }

    public Editable getText() {
        return editText.getText();
    }

    public String getTexto() {
        if (editText.getText() != null) {
            return editText.getText().toString();
        }
        return null;
    }

    public void comprobarEdit() {

        valido = true;
        excedido = false;
        if (obligatorio && getActivo() && ((getText() == null) || (getText() != null && getTexto().equals("")) ||
                (getText() != null && getTexto().equals("0.0")))) {
            //editText.setBackgroundColor(context.getResources().getColor(context.getResources().
            //        getIdentifier(COLOREDITVACIO, COLOR,
            //                context.getPackageName())));
            editText.setBackground(Estilos.getDrawable(context, "edit_error"));
            editText.setTextColor(context.getResources().getColor(context.getResources().
                    getIdentifier(COLORSECONDARYDARK, COLOR,
                            context.getPackageName())));
        } else {
            editText.setBackgroundColor(context.getResources().getColor(context.getResources().
                    getIdentifier(COLORPRIMARY, COLOR,
                            context.getPackageName())));
            editText.setTextColor(context.getResources().getColor(context.getResources().
                    getIdentifier(COLORTEXTO, COLOR,
                            context.getPackageName())));

        }

        if (textInputLayout.isCounterEnabled() && textInputLayout.getCounterMaxLength() < editText.length()) {

            valido = false;
            excedido = true;
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Excedido el numero de caracteres");
            editText.setBackground(Estilos.getDrawable(context, "edit_error"));


        } else {
            if (textInputLayout.isErrorEnabled()) {
                textInputLayout.setError(null);
            }

        }
        if (tipoDato == TEXTO + EMAIL) {
            if (!Patterns.EMAIL_ADDRESS.matcher(getTexto()).matches()) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError("Correo electrónico inválido");
                editText.setBackground(Estilos.getDrawable(context, "edit_error"));

                if (obligatorio) {
                    valido = false;
                }
            } else {
                if (textInputLayout.isErrorEnabled()) {
                    textInputLayout.setError(null);
                }
            }
        } else if (tipoDato == TELEFONO) {
            if (!Patterns.PHONE.matcher(getTexto()).matches()) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError("telefono invalido");
                editText.setBackground(Estilos.getDrawable(context, "edit_error"));

                if (obligatorio) {
                    valido = false;
                }
            } else {
                if (textInputLayout.isErrorEnabled()) {
                    textInputLayout.setError(null);
                }
            }
        } else if (tipoDato == TEXTO + URI) {
            if (!Patterns.WEB_URL.matcher(getTexto()).matches()) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError("web invalida");
                editText.setBackground(Estilos.getDrawable(context, "edit_error"));

                if (obligatorio) {
                    valido = false;
                }
            } else {
                if (textInputLayout.isErrorEnabled()) {
                    textInputLayout.setError(null);
                }
            }
        }
        if (obligatorio && getTexto().equals("")) {
            valido = false;
        }

    }

    public ImageButton getBtnInicio() {
        return btnInicio;
    }

    public ImageButton getBtnAccion() {
        return btnAccion;
    }

    public ImageButton getBtnAccion2() {
        return btnAccion2;
    }

    public ImageButton getBtnAccion3() {
        return btnAccion3;
    }

    public Button getBtnText() {
        return btnText;
    }

    public EditText getEditText() {
        return editText;
    }

    public TextInputLayout getTextInputLayout() {
        return textInputLayout;
    }

    public void setMsgError(CharSequence msgError) {
        this.msgError = msgError;
    }

    public void setObligatorio(boolean obligatorio) {

        this.obligatorio = obligatorio;
    }

    public boolean getValido() {

        if (!obligatorio && !excedido) {
            return true;
        }
        return valido;
    }

    public boolean getObligatorio() {
        return obligatorio;
    }

    public void setCounterEnable(boolean enable) {
        textInputLayout.setCounterEnabled(enable);
    }

    public boolean isCounterEnable() {
        return textInputLayout.isCounterEnabled();
    }

    public void setCounterMax(int counterMax) {

        textInputLayout.setCounterMaxLength(counterMax);
    }

    public int getCounterMax() {
        return textInputLayout.getCounterMaxLength();
    }

    public void setErrorEnable(boolean enable, CharSequence msgError) {

        textInputLayout.setErrorEnabled(enable);
        textInputLayout.setError(msgError);
    }

    public void setMsgAyuda(CharSequence msgAyuda) {

        textInputLayout.setHelperTextEnabled(true);
        textInputLayout.setHelperText(msgAyuda);

    }

    public void setAyudaEnable(boolean enable) {
        textInputLayout.setHelperTextEnabled(true);

    }

    public void seleccionarTexto() {
        editText.selectAll();
    }

    public void finalTexto() {
        editText.setSelection(editText.length());
    }

    public void setActivo(boolean activo) {
        editText.setEnabled(activo);

        if (!activo){
            btnInicioVisible(false);
        }
    }

    public boolean getActivo() {
        return editText.isEnabled();
    }

    public String getnombreLlamada() {
        return nombreLlamada;
    }

    public void setNombreLlamada(String nombre) {
        nombreLlamada = nombre;
    }

    public interface AudioATexto {

        void onGrabar(View view, int posicion);
    }

    public interface CambioFocoEdit {

        void alPerderFoco(View view);

        void alRecibirFoco(View view);
    }

    public interface AlCambiarListener {

        void antesCambio(CharSequence s, int start, int count, int after);

        void cambiando(CharSequence s, int start, int before, int count);

        void despuesCambio(Editable s);

    }

    public interface ClickAccion {

        void onClickAccion(View view);
    }

    public interface ClickAccion2 {

        void onClickAccion2(View view);
    }

    public interface ClickAccion3 {

        void onClickAccion3(View view);
    }

    public interface ClickAccionTxt {

        void onClickAccionTxt(View view);
    }

}
