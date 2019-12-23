package com.codevsolution.base.settings;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.EVENTO;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.NOTA;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTO;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PROYECTO;

public class PreferenciasBase extends FragmentBase {

    private int contDes;
    private int contAnt;

    private Switch letraProp;
    private TextView ajustes;
    private EditMaterialLayout tiempoAuto;
    private Switch autoGuardadoSw;
    public static final String LETRAPROP = "letraprop";
    public static final String CIFRADO = "cifrado";
    public static final String CIFRADOPASS = "cifradoPass";
    public static final String CIFRADOPASSPLUS = "cifradoPassPlus";
    public static final String CIFRADOGEN = "cifradoGen";
    public static final String AUTOGUARDADO = "autoguardado";
    public static final String TIEMPOAUTOGUARDADO = "tiempoautoguardado";
    private TextView tituloEncrypt;
    private RadioGroup gEncrypt;
    private RadioButton noEncrypt;
    private RadioButton userEncrypt;
    private RadioButton passEncrypt;
    private RadioButton passPlusEncrypt;
    private final int idNoEncrypt = View.generateViewId();
    private int idUserEncrypt;
    private int idPassEncrypt;
    private int idPassPlusEncrypt;
    private Button btnPass;
    private static boolean iniciado;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        iniciado = false;

        visible(frdetalle);
        vistaMain = new ViewGroupLayout(contexto, frdetalle);
        vistaMain.addTextView(R.string.ajustes_generales);
        letraProp = vistaMain.addSwitch(R.string.letra_proporcional, false);
        letraProp.setChecked(getPref(LETRAPROP, false));
        setOnCheck(letraProp, LETRAPROP);
        tituloEncrypt = vistaMain.addTextView(R.string.titulo_encriptado);
        gEncrypt = (RadioGroup) vistaMain.addVista(new RadioGroup(contexto));
        noEncrypt = new RadioButton(contexto);
        noEncrypt.setId(idNoEncrypt);
        noEncrypt.setText(R.string.no_encrypt);
        userEncrypt = new RadioButton(contexto);
        idUserEncrypt = View.generateViewId();
        userEncrypt.setId(idUserEncrypt);
        userEncrypt.setText(R.string.nivel_encrypt_1);
        passEncrypt = new RadioButton(contexto);
        idPassEncrypt = View.generateViewId();
        passEncrypt.setId(idPassEncrypt);
        passEncrypt.setText(R.string.nivel_encrypt_2);
        passPlusEncrypt = new RadioButton(contexto);
        idPassPlusEncrypt = View.generateViewId();
        passPlusEncrypt.setId(idPassPlusEncrypt);
        passPlusEncrypt.setText(R.string.nivel_encrypt_3);
        onCheckEncrypt();
        setEncrypt();

        gEncrypt.addView(noEncrypt);
        gEncrypt.addView(userEncrypt);
        gEncrypt.addView(passEncrypt);
        gEncrypt.addView(passPlusEncrypt);

        btnPass = vistaMain.addButtonSecondary(R.string.btnpass);
        gone(btnPass);
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogoPass();
            }
        });
        autoGuardadoSw = vistaMain.addSwitch(R.string.autoguardado, false);
        autoGuardadoSw.setChecked(getPref(AUTOGUARDADO, true));
        setOnCheck(autoGuardadoSw, AUTOGUARDADO);
        tiempoAuto = vistaMain.addEditMaterialLayout(R.string.tiempo_autoguardado);
        tiempoAuto.setText(String.valueOf(getPref(TIEMPOAUTOGUARDADO, 1)));
        tiempoAuto.btnInicioVisible(false);
        setAlCambiarEditPref(tiempoAuto, TIEMPOAUTOGUARDADO, INT);

        ajustes = vistaMain.addTextView(R.string.ajustes);
        gone(ajustes);

        actualizarArrays(vistaMain);
        iniciado = true;
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        origen = getStringBundle(ORIGEN, NULL);
        actual = getStringBundle(ACTUAL, NULL);

        if (nnn(origen)) {

            visible(ajustes);
            ajustes.setText(String.format(Locale.getDefault(), "%s %s", getString(R.string.ajustes), origen));

            switch (origen) {

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

    private void setAlCambiarEditPref(EditMaterialLayout editPref, String key, String tipo) {

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

                            switch (tipo) {

                                case INT:
                                    setPref(key, JavaUtil.comprobarInteger(valor));
                                    break;
                                case LONG:
                                    setPref(key, JavaUtil.comprobarLong(valor));
                                    break;
                                case STRING:
                                    setPref(key, valor);
                                    break;
                                case FLOAT:
                                    setPref(key, Float.parseFloat(valor));
                                    break;
                                default:
                                    setPref(key, valor);

                            }
                        }
                    }, tiempoGuardado * 1000);
                }
            }
        });
    }

    private void setOnCheck(Switch aSwitch, String key) {

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                setPref(key, isChecked);
            }
        });
    }

    private void setOnCheck(CheckBox checkBox, String key) {

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                setPref(key, isChecked);
            }
        });
    }

    private void onCheckEncrypt() {

        gEncrypt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (iniciado && checkedId == idNoEncrypt) {

                    setPref(CIFRADO, false);
                    setPref(CIFRADOPASS, false);
                    setPref(CIFRADOPASSPLUS, false);
                    setPref(PASSOK, NULL);
                    gone(btnPass);
                    if (InteractorBase.key != null) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                EncryptUtil.desCifrarBase(ContratoPry.obtenerListaCampos());
                                EncryptUtil.desCifrarBase(ContratoSystem.obtenerListaCampos());
                                InteractorBase.key = null;
                            }
                        }).start();

                    }

                } else if (iniciado && checkedId == idUserEncrypt) {

                    setPref(CIFRADO, true);
                    setPref(CIFRADOPASS, false);
                    setPref(CIFRADOPASSPLUS, false);
                    setPref(PASSOK, NULL);
                    gone(btnPass);
                    if (InteractorBase.key != null) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                EncryptUtil.desCifrarBase(ContratoPry.obtenerListaCampos());
                                EncryptUtil.desCifrarBase(ContratoSystem.obtenerListaCampos());
                                InteractorBase.key = idUser;
                                EncryptUtil.cifrarBase(ContratoPry.obtenerListaCampos());
                                EncryptUtil.cifrarBase(ContratoSystem.obtenerListaCampos());
                            }
                        }).start();

                    }

                } else if (iniciado && checkedId == idPassEncrypt) {

                    setPref(CIFRADO, false);
                    setPref(CIFRADOPASS, true);
                    setPref(CIFRADOPASSPLUS, false);
                    visible(btnPass);
                    InteractorBase.key = getPref(PASSOK, NULL);
                    if (InteractorBase.key != null && !InteractorBase.key.equals(NULL)) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                EncryptUtil.cifrarBase(ContratoPry.obtenerListaCampos());
                                EncryptUtil.cifrarBase(ContratoSystem.obtenerListaCampos());
                            }
                        }).start();

                    } else if (InteractorBase.key != null && InteractorBase.key.equals(idUser)) {

                        EncryptUtil.desCifrarBase(ContratoPry.obtenerListaCampos());
                        EncryptUtil.desCifrarBase(ContratoSystem.obtenerListaCampos());
                        dialogoPass();

                    } else {
                        dialogoPass();
                    }

                } else if (iniciado && checkedId == idPassPlusEncrypt) {

                    setPref(CIFRADO, false);
                    setPref(CIFRADOPASS, true);
                    setPref(CIFRADOPASSPLUS, true);
                    visible(btnPass);
                    InteractorBase.key = getPref(PASSOK, NULL);
                    if (InteractorBase.key != null && !InteractorBase.key.equals(NULL)) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                EncryptUtil.cifrarBase(ContratoPry.obtenerListaCampos());
                                EncryptUtil.cifrarBase(ContratoSystem.obtenerListaCampos());
                            }
                        }).start();

                    } else if (InteractorBase.key != null && InteractorBase.key.equals(idUser)) {

                        EncryptUtil.desCifrarBase(ContratoPry.obtenerListaCampos());
                        EncryptUtil.desCifrarBase(ContratoSystem.obtenerListaCampos());
                        dialogoPass();

                    } else {
                        dialogoPass();
                    }

                }

            }
        });
    }

    private void setEncrypt() {

        if (getPref(CIFRADOPASSPLUS, false)) {
            passPlusEncrypt.setChecked(true);

        } else if (getPref(CIFRADOPASS, false)) {
            passEncrypt.setChecked(true);

        } else if (getPref(CIFRADO, false)) {
            userEncrypt.setChecked(true);
        } else {
            noEncrypt.setChecked(true);
        }
    }

    private void dialogoPass() {

        String titulo = "CIFRADO";
        String mensaje = "Introduzca nueva contraseña de cifrado";
        String pass = null;
        try {
            pass = EncryptUtil.desencriptarPassAES(getPref(PASSOK, NULL), idUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Dialogos.DialogoCambioPass(titulo, mensaje, pass, contexto,
                new Dialogos.DialogoCambioPass.OnClick() {
                    @Override
                    public void onConfirm(String text) {

                        InteractorBase.key = text;
                        EncryptUtil.codificaPass();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                EncryptUtil.cifrarBase(ContratoPry.obtenerListaCampos());
                                EncryptUtil.cifrarBase(ContratoSystem.obtenerListaCampos());
                            }
                        }).start();
                    }

                    @Override
                    public void onCancel() {

                        Toast.makeText(contexto, "Operación cancelada", Toast.LENGTH_SHORT).show();
                    }
                }).show(getFragmentManager(), "dialogEdit");
    }


}
