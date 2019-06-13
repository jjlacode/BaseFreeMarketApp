package jjlacode.com.freelanceproject.util.android.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import jjlacode.com.freelanceproject.R;

public class EditMaterial extends LinearLayout {

    private TextInputEditText editText;
    private TextInputLayout textInputLayout;
    private AlCambiarListener listener;
    private OnClick onClickListener;

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

    public EditMaterial(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        boolean activo = a.getBoolean(
                R.styleable.EditMaterial_activo,true);

        textInputLayout.setHint(textoHint);
        editText.setInputType(tipoDato);
        editText.setGravity(gravedad);
        editText.setEnabled(activo);

        a.recycle();

    }

    public void setAlCambiarListener(AlCambiarListener l){
        this.listener = l;
    }

    public void setOnClick(OnClick l) {this.onClickListener = l;}

    private void asignarEventos() {

        textInputLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onClickListener!=null){
                    onClickListener.onClick(v);
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (listener!=null) {
                    listener.antesCambio(getText().toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (listener!=null) {
                    listener.cambiando(getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (listener!=null) {
                    listener.despuesCambio(getText().toString());
                }
            }
        });
    }


    public void setHint(String hint){

        textInputLayout.setHint(hint);
    }

    public void setText(String text){
        editText.setText(text);
    }

    public Editable getText(){
        return editText.getText();
    }

    public void activo(boolean activo){
        editText.setEnabled(activo);
    }

    public interface AlCambiarListener{

        void antesCambio(String texto);
        void despuesCambio(String texto);
        void cambiando(String texto);
    }

    public interface OnClick{
        void onClick(View v);
    }
}
