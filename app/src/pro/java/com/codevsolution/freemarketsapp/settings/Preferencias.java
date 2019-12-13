package com.codevsolution.freemarketsapp.settings;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.encrypt.EncryptUtil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.sqlite.ContratoSystem;
import com.codevsolution.base.style.Dialogos;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.EVENTO;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.NOTA;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTO;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PROYECTO;

public class Preferencias extends FragmentBase {

    private int contDes;
    private int contAnt;

    private Switch letraProp;
    private Switch cifrado;
    private Switch cifradoPass;
    private TextView txtCifrado;
    private TextView ajustes;
    private EditMaterialLayout tiempoAuto;
    private Switch autoGuardadoSw;
    public static final String LETRAPROP = "letraprop";
    public static final String CIFRADO = "cifrado";
    public static final String CIFRADOPASS = "cifradoPass";
    public static final String AUTOGUARDADO = "autoguardado";
    public static final String TIEMPOAUTOGUARDADO = "tiempoautoguardado";

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        visible(frdetalle);
        vistaMain = new ViewGroupLayout(contexto, frdetalle);
        vistaMain.addTextView(R.string.ajustes_generales);
        letraProp = vistaMain.addSwitch(R.string.letra_proporcional,false);
        letraProp.setChecked(getPref(LETRAPROP,false));
        setOnCheck(letraProp,LETRAPROP);
        cifrado = vistaMain.addSwitch(R.string.cifrar_datos,false);
        cifrado.setChecked(getPref(CIFRADO,false));
        setOnCheck(cifrado,CIFRADO);
        cifradoPass = vistaMain.addSwitch(R.string.cifrar_datos_pass,false);
        cifradoPass.setChecked(getPref(CIFRADOPASS,false));
        cifradoPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                setPref(CIFRADOPASS, isChecked);

                if (isChecked){
                    String titulo = "CIFRADO";
                    String mensaje = "Introduzca contraseña de cifrado";
                    new Dialogos.DialogoEdit(titulo, mensaje, EditMaterialLayout.PASS,"Contraseña",
                            contexto, new Dialogos.DialogoEdit.OnClick() {
                        @Override
                        public void onConfirm(String text) {

                            InteractorBase.key = text;
                            String pass = AndroidUtil.getSharePreference(contexto,PREFERENCIAS+idUser,ENCODEPASS,NULL);
                            if (text!=null && text!="" && pass==NULL) {
                                EncryptUtil.codificaPass();
                            }
                            if (text!=null && text!="" && EncryptUtil.verificarPass(text)){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onOkPass();
                                    }
                                }).start();
                            }else{
                                Toast.makeText(contexto, "La contraseña de cifrado no es valida", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancel() {

                            Toast.makeText(contexto, "Tendrá que introducir la contraseña en las preferencias para poder cifrar y descifrar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }).show(getFragmentManager(),"dialogEdit");
                }else{
                    InteractorBase.key = null;
                }
            }
        });
        txtCifrado = vistaMain.addTextView(R.string.txt_cifrado);
        autoGuardadoSw = vistaMain.addSwitch(R.string.autoguardado,false);
        autoGuardadoSw.setChecked(getPref(AUTOGUARDADO, true));
        setOnCheck(autoGuardadoSw,AUTOGUARDADO);
        tiempoAuto = vistaMain.addEditMaterialLayout(R.string.tiempo_autoguardado);
        tiempoAuto.setText(String.valueOf(getPref(TIEMPOAUTOGUARDADO,1)));
        setAlCambiarEditPref(tiempoAuto, TIEMPOAUTOGUARDADO,INT);

        ajustes = vistaMain.addTextView(R.string.ajustes);
        gone(ajustes);

        actualizarArrays(vistaMain);
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    protected void onOkPass() {

        EncryptUtil.cifrarBase(ContratoPry.obtenerListaCampos());
        EncryptUtil.cifrarBase(ContratoSystem.obtenerListaCampos());
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        origen = getStringBundle(ORIGEN, NULL);
        actual = getStringBundle(ACTUAL, NULL);

        if (nnn(origen)){

            visible(ajustes);
            ajustes.setText(String.format(Locale.getDefault(),"%s %s", getString(R.string.ajustes), origen));

            switch (origen){

                case PRODUCTO:

                    break;

                case PROYECTO:

                    break;

                case EVENTO:

                    break;

                case NOTA:

                    break;

            }
        }

    }

    private void setAlCambiarEditPref(EditMaterialLayout editPref, String key, String tipo){

        editPref.setAlCambiarListener(new EditMaterialLayout.AlCambiarListener() {
            @Override
            public void antesCambio(CharSequence s, int start, int count, int after) {

                contAnt = count;
            }

            @Override
            public void cambiando(CharSequence s, int start, int before, int count) {

                if (timer != null) {
                    timer.cancel();
                }

                contDes = count;
            }

            @Override
            public void despuesCambio(Editable s) {

                System.out.println("s.toString() = " + s.toString());
                if (!s.toString().equals("") && contAnt != contDes) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            String valor = s.toString();

                            switch (tipo){

                                case INT:
                                    setPref(key, JavaUtil.comprobarInteger(valor));
                                    break;
                                case LONG:
                                    setPref(key,JavaUtil.comprobarLong(valor));
                                    break;
                                case STRING:
                                    setPref(key,valor);
                                    break;
                                case FLOAT:
                                    setPref(key,Float.parseFloat(valor));
                                    break;
                                default:
                                    setPref(key,valor);

                            }
                        }
                    }, tiempoGuardado * 1000);
                }
            }
        });
    }

    private void setOnCheck(Switch aSwitch, String key){

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                setPref(key, isChecked);
            }
        });
    }

    private void setOnCheck(CheckBox checkBox, String key){

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                setPref(key, isChecked);
            }
        });
    }


}
