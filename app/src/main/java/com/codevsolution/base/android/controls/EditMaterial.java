package com.codevsolution.base.android.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentActivity;

import com.codevsolution.base.android.AppActivity;
import com.codevsolution.freemarketsapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditMaterial extends LinearLayoutCompat {

    private TextInputEditText editText;
    private TextInputLayout textInputLayout;
    private ImageButton btnInicio;
    private ImageButton btnAccion;
    private ImageButton btnAccion2;
    private Button btnText;
    private AlCambiarListener listener;
    private AudioATexto grabarListener;
    private CambioFocoEdit listenerFoco;
    private ClickAccion listenerAccion;
    private ClickAccion2 listenerAccion2;
    private ClickAccionTxt listenerAccionTxt;
    private int posicion;
    DisplayMetrics metrics = new DisplayMetrics();
    private boolean textoCambiado;
    private String textoEdit;
    private AppCompatActivity activity;
    private int weight = 5;

    public EditMaterial(Context context) {
        super(context);
        inicializar();

    }

    public EditMaterial(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inicializar();

        setAtributos(attrs);
    }

    public EditMaterial(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inicializar();

        setAtributos(attrs);
    }

    private void inicializar() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li =
                (LayoutInflater) getContext().getSystemService(infService);
        li.inflate(R.layout.edit_material, this, true);

        editText = findViewById(R.id.et_material);
        textInputLayout = findViewById(R.id.ti_material);
        btnInicio = findViewById(R.id.imgmaterial);
        btnAccion = findViewById(R.id.imgmaterialder);
        btnAccion2 = findViewById(R.id.imgmaterialder2);
        btnText = findViewById(R.id.imgmaterialder3);

        asignarEventos();
        setWeigthLayout();

    }


    private void setAtributos(AttributeSet attrs) {

        TypedArray a =
                getContext().obtainStyledAttributes(attrs,
                        R.styleable.EditMaterial);

        String textoHint = a.getString(
                R.styleable.EditMaterial_hint_text);
        int gravedad = a.getInt(
                R.styleable.EditMaterial_gravedad, 8388611);
        int tipoDato = a.getInt(
                R.styleable.EditMaterial_tipo_dato, 1);
        int colorFondo = a.getInt(
                R.styleable.EditMaterial_fondo, getContext().getResources().getColor(R.color.colorPrimary));
        boolean activo = a.getBoolean(
                R.styleable.EditMaterial_activo, true);

        textInputLayout.setHint(textoHint);
        textInputLayout.setBoxBackgroundColor(colorFondo);
        editText.setInputType(tipoDato);
        editText.setGravity(gravedad);
        editText.setEnabled(activo);
        btnInicio.setFocusable(false);
        btnAccion.setFocusable(false);

        a.recycle();
        setWeigthLayout();

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

            }
        });

        btnInicio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                grabarListener.onGrabar(view, posicion);
            }
        });

        btnAccion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listenerAccion != null) {
                    listenerAccion.onClickAccion(view);
                }

            }
        });

        btnAccion2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listenerAccion2 != null) {
                    listenerAccion2.onClickAccion2(view);
                }

            }
        });

        btnText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listenerAccionTxt != null) {
                    listenerAccionTxt.onClickAccionTxt(view);
                }

            }
        });

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
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
                        editText.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                        editText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                } else {
                    editText.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    editText.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                    comprobarEdit();
                }
            }
        });


    }

    private void setWeigthLayout() {

        int vis = btnInicio.getVisibility() + btnAccion.getVisibility() + btnAccion2.getVisibility() + btnText.getVisibility();

        switch (vis) {

            case 24:
                weight = 10;
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
        System.out.println("weight = " + weight);

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, weight);
        params.gravity = Gravity.CENTER_VERTICAL;
        btnInicio.setLayoutParams(params);
        btnAccion.setLayoutParams(params);
        btnAccion2.setLayoutParams(params);
        params = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, weight - 2);
        params.gravity = Gravity.CENTER_VERTICAL;
        btnText.setLayoutParams(params);
        btnText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        if (btnText.getVisibility() == View.VISIBLE) {
            params = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT, weight - 2);
            textInputLayout.setLayoutParams(params);
        } else {
            params = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT, 1);
            textInputLayout.setLayoutParams(params);
        }
    }

    public String getHint() {
        return editText.getHint().toString();
    }
    public void setFondo(int colorFondo) {
        textInputLayout.setBoxBackgroundColor(colorFondo);

    }

    public void setMaxLeng(int leng) {

        textInputLayout.setCounterEnabled(true);
        textInputLayout.setCounterMaxLength(leng);
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

    public void btnInicioInvisible(boolean enable) {

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

    public void btnAccionInvisible(boolean enable) {

        if (enable) {
            btnAccion.setVisibility(View.VISIBLE);
        } else {
            btnAccion.setVisibility(View.INVISIBLE);
        }
        setWeigthLayout();

    }

    public void btnAccion2Invisible(boolean enable) {

        if (enable) {
            btnAccion2.setVisibility(View.VISIBLE);
        } else {
            btnAccion2.setVisibility(View.INVISIBLE);
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

    public void btnAccionTxtEnable(boolean enable) {

        if (enable) {
            btnText.setVisibility(View.VISIBLE);
        } else {
            btnText.setVisibility(View.GONE);
        }
        setWeigthLayout();

    }

    public void setImgBtnAccion(int recurso) {

        btnAccion.setImageResource(recurso);
    }

    public void setImgBtnAccion2(int recurso) {

        btnAccion2.setImageResource(recurso);
    }

    public void setTextBtnTxt(String textoBtn) {

        btnText.setText(textoBtn);
    }

    public void setTextBtnTxt(int recursoString) {

        btnText.setText(recursoString);
    }

    public void setAccionLlamada(AppCompatActivity activityCompat, ClickAccion listenerAccion) {

        btnAccionEnable(true);
        setImgBtnAccion(R.drawable.ic_llamada_indigo);
        activity = activityCompat;
        this.listenerAccion = listenerAccion;
    }

    public void setAccionVerMapa(ClickAccion listenerAccion) {

        btnAccionEnable(true);
        setImgBtnAccion(R.drawable.ic_lugar_indigo);
        this.listenerAccion = listenerAccion;
    }

    public void setAccionEnviarMail(ClickAccion listenerAccion) {

        btnAccionEnable(true);
        setImgBtnAccion(R.drawable.ic_email_indigo);
        this.listenerAccion = listenerAccion;
    }

    public void enviarEmail() {

        AppActivity.enviarEmail(getContext(), getTexto());

    }

    public void verEnMapa() {

        if (!getTexto().equals("")) {

            AppActivity.viewOnMapA(getContext(), getTexto());

        }
    }

    public void llamar() {

        AppActivity.hacerLlamada(getContext(), getTexto(), activity);

    }

    public void setText(String text) {
        editText.setText(text);
        comprobarEdit();
    }

    public void setGravedad(int gravedad) {
        editText.setGravity(gravedad);
    }

    public void setTextSize(AppCompatActivity activityCompat) {

        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        boolean land = getResources().getBoolean(R.bool.esLand);
        boolean tablet = getResources().getBoolean(R.bool.esTablet);
        int ancho = metrics.widthPixels;
        int alto = metrics.heightPixels;

        float size = ((float) (alto * ancho) / (metrics.densityDpi * 300));
        editText.setTextSize(size);
        activity = activityCompat;
    }

    public void setTextSize(FragmentActivity activityCompat) {

        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        boolean land = getResources().getBoolean(R.bool.esLand);
        boolean tablet = getResources().getBoolean(R.bool.esTablet);
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
        if (getActivo() && ((getText() == null) || (getText() != null && getTexto().equals("")) ||
                (getText() != null && getTexto().equals("0.0")))) {
            editText.setBackgroundColor(getResources().getColor(R.color.Color_edit_vacio));
            editText.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
        } else {
            editText.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            editText.setTextColor(getResources().getColor(R.color.Color_texto));

        }

    }

    public void seleccionarTexto() {
        editText.selectAll();
    }

    public void finalTexto() {
        editText.setSelection(editText.length());
    }

    public void setActivo(boolean activo) {
        editText.setEnabled(activo);
    }

    public boolean getActivo() {
        return editText.isEnabled();
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

    public interface ClickAccionTxt {

        void onClickAccionTxt(View view);
    }

}
