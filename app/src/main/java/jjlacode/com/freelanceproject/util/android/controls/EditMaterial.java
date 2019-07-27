package jjlacode.com.freelanceproject.util.android.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import jjlacode.com.freelanceproject.MainActivity;
import jjlacode.com.freelanceproject.R;

public class EditMaterial extends LinearLayoutCompat {

    private TextInputEditText editText;
    private TextInputLayout textInputLayout;
    private ImageButton grabar;
    private AlCambiarListener listener;
    private AudioATexto grabarListener;
    private CambioFocoEdit listenerFoco;
    private int posicion;
    DisplayMetrics metrics = new DisplayMetrics();
    private boolean textoCambiado;
    private String textoEdit;

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
                (LayoutInflater)getContext().getSystemService(infService);
        li.inflate(R.layout.edit_material, this, true);

        editText = findViewById(R.id.et_material);
        textInputLayout = findViewById(R.id.ti_material);
        grabar = findViewById(R.id.imgmaterial);

        asignarEventos();

    }


    private void setAtributos(AttributeSet attrs){

        TypedArray a =
                getContext().obtainStyledAttributes(attrs,
                        R.styleable.EditMaterial);

        String textoHint = a.getString(
                R.styleable.EditMaterial_hint_text);
        int gravedad = a.getInt(
                R.styleable.EditMaterial_gravedad,8388611);
        int tipoDato = a.getInt(
                R.styleable.EditMaterial_tipo_dato,1);
        int colorFondo = a.getInt(
                R.styleable.EditMaterial_fondo,getContext().getResources().getColor(R.color.colorPrimary));
        boolean activo = a.getBoolean(
                R.styleable.EditMaterial_activo,true);

        textInputLayout.setHint(textoHint);
        textInputLayout.setBoxBackgroundColor(colorFondo);
        editText.setInputType(tipoDato);
        editText.setGravity(gravedad);
        editText.setEnabled(activo);
        grabar.setFocusable(false);

        a.recycle();

    }

    public void setAlCambiarListener(AlCambiarListener l){
        this.listener = l;
    }

    public void setGrabarListener(AudioATexto grabarListener){
        this.grabarListener = grabarListener;
    }

    public void setCambioFocoListener(CambioFocoEdit listener) {
        listenerFoco = listener;
    }
    private void asignarEventos() {

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (listener!=null) {
                    listener.antesCambio(s,start,count,after);
                }
                textoEdit = getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (listener!=null) {
                    listener.cambiando(s,start,before,count);
                }
                if (!textoEdit.equals(getText().toString())) {
                    textoCambiado = true;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (listener!=null) {
                    listener.despuesCambio(s);
                }

            }
        });

        grabar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                grabarListener.onGrabar(view, posicion);
            }
        });

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (!b && textoCambiado) {
                    listenerFoco.alPerderFoco(view);
                    textoCambiado = false;

                }
                if (b) {
                    listenerFoco.alRecibirFoco(view);
                    editText.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    editText.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    editText.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    editText.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                    comprobarEdit();
                }
            }
        });


    }

    public void setFondo(int colorFondo){
        textInputLayout.setBoxBackgroundColor(colorFondo);

    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setHint(String hint){

        textInputLayout.setHint(hint);
    }

    public void grabarEnable(boolean enable){

        if (enable) {
            grabar.setVisibility(View.VISIBLE);
        }else{
            grabar.setVisibility(View.GONE);
        }
    }

    public void setText(String text){
        editText.setText(text);
        if (getText() == null || (getText() != null && getTexto().equals(""))) {
            editText.setBackgroundColor(getResources().getColor(R.color.Color_card_notok));
            editText.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
        }
    }

    public void setGravedad(int gravedad){
        editText.setGravity(gravedad);
    }

    public void setTextSize(MainActivity activityCompat){

        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(metrics);


        boolean land = getResources().getBoolean(R.bool.esLand);
        boolean tablet = getResources().getBoolean(R.bool.esTablet);
        int ancho = metrics.widthPixels;
        int alto = metrics.heightPixels;

        float size = ((float) (alto*ancho)/(metrics.densityDpi*300));
        editText.setTextSize(size);
    }

    public void setTextSize(FragmentActivity activityCompat){

        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        boolean land = getResources().getBoolean(R.bool.esLand);
        boolean tablet = getResources().getBoolean(R.bool.esTablet);
        float ancho = metrics.widthPixels;
        float alto = metrics.heightPixels;


        float size = ((float) (alto*ancho)/(metrics.densityDpi*300));
        editText.setTextSize(size);
    }

    public Editable getText(){
        return editText.getText();
    }

    public String getTexto() {
        if (editText.getText() != null) {
            return editText.getText().toString();
        }
        return null;
    }

    public void comprobarEdit() {
        if (getText() == null || (getText() != null && getTexto().equals(""))) {
            editText.setBackgroundColor(getResources().getColor(R.color.Color_card_notok));
            editText.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
        }

    }

    public void seleccionarTexto() {
        editText.selectAll();
    }

    public void finalTexto() {
        editText.setSelection(editText.length());
    }

    public void setActivo(boolean activo){
        editText.setEnabled(activo);
    }

    public boolean getActivo(){
        return editText.isEnabled();
    }

    public interface AudioATexto {

        void onGrabar(View view, int posicion);
    }

    public interface CambioFocoEdit {

        void alPerderFoco(View view);

        void alRecibirFoco(View view);
    }

    public interface AlCambiarListener{

        void antesCambio(CharSequence s, int start, int count, int after);
        void cambiando(CharSequence s, int start, int before, int count);
        void despuesCambio(Editable s);

    }

}
