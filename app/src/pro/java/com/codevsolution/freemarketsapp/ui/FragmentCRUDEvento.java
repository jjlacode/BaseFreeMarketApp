package com.codevsolution.freemarketsapp.ui;
// Created by jjlacode on 29/04/19. 

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.style.Dialogos;
import com.codevsolution.base.time.DatePickerFragment;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.base.time.TimePickerFragment;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.codevsolution.base.android.AppActivity.viewOnMapA;
import static com.codevsolution.base.javautil.JavaUtil.getDate;
import static com.codevsolution.base.javautil.JavaUtil.getTime;
import static com.codevsolution.base.javautil.JavaUtil.hoy;
import static com.codevsolution.base.time.calendar.DiaCalBase.HORACAL;

public class FragmentCRUDEvento extends FragmentCRUD implements Interactor.ConstantesPry,
        ContratoPry.Tablas, Interactor.TiposEvento {

    private EditMaterialLayout proyRel;
    private EditMaterialLayout cliRel;
    private EditMaterialLayout descipcion;
    private EditMaterialLayout lugar;
    private EditMaterialLayout telefono;
    private EditMaterialLayout email;
    private EditMaterialLayout asunto;
    private EditMaterialLayout mensaje;
    private EditMaterialLayout fechaIni;
    private EditMaterialLayout fechaFin;
    private EditMaterialLayout horaIni;
    private EditMaterialLayout horaFin;
    private EditMaterialLayout repAnios;
    private EditMaterialLayout repMeses;
    private EditMaterialLayout repDias;
    private EditMaterialLayout drepAnios;
    private EditMaterialLayout drepMeses;
    private EditMaterialLayout drepDias;
    private EditMaterialLayout avisoMinutos;
    private EditMaterialLayout avisoHoras;
    private EditMaterialLayout avisoDias;
    private CheckBox aviso;
    private CheckBox repeticiones;
    private CheckBox mismoDiaMes;
    private CheckBox mismoDiaAnio;
    private CheckBox chNotificado;
    private TextView lrep;
    private TextView ldrep;
    private ImageButton btnNota;
    private ImageButton btnVerNotas;
    private Button btnVerRepeticiones;
    private Button generarRepeticiones;
    private Button generarAviso;

    private ArrayList<ModeloSQL> listaClientes;
    private ArrayList<ModeloSQL> listaProyectos;
    private String idCliente;
    private String idProyecto;
    private String nombreProyecto;
    private String nombreCliente;
    private String tevento;
    private long finiEvento;
    private long ffinEvento;
    private long hiniEvento;
    private long hfinEvento;


    private String idMulti;
    private EditMaterialLayout completa;

    private CheckBox completada;
    private ModeloSQL proyecto;
    private ModeloSQL cliente;
    private int notificado;
    private TextView textAviso;
    private TextView fechaAvisoTxt;
    private ViewGroupLayout vistaAviso;
    private Button btnColor;
    //private Button btnRelCli;
    //private Button btnRelProy;


    public FragmentCRUDEvento() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    @Override
    protected void setInicio() {

        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalle);

        imagen = vistaForm.addViewImagenLayout();
        descipcion = vistaForm.addEditMaterialLayout(R.string.descripcion, EVENTO_DESCRIPCION);
        ViewGroupLayout vistaFecha = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaFecha.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        fechaIni = vistaFecha.addEditMaterialLayoutFecha(getString(R.string.fecha_ini_evento),
                1, view -> showDatePickerDialogInicio());

        horaIni = vistaFecha.addEditMaterialLayoutFecha(getString(R.string.hora_ini_evento),
                2, view -> showTimePickerDialogini());

        actualizarArrays(vistaFecha);
        ViewGroupLayout vistaFechaFin = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaFechaFin.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        fechaFin = vistaFechaFin.addEditMaterialLayoutFecha(getString(R.string.fecha_fin_evento),
                1, view -> showDatePickerDialogFin());
        horaFin = vistaFechaFin.addEditMaterialLayoutFecha(getString(R.string.hora_fin_evento),
                2, view -> showTimePickerDialogfin());
        actualizarArrays(vistaFechaFin);
        lugar = vistaForm.addEditMaterialLayout(getString(R.string.lugar_cita), EVENTO_DIRECCION, ViewGroupLayout.MAPA, activityBase);
        email = vistaForm.addEditMaterialLayoutMailFull(getString(R.string.email), EVENTO_EMAIL);
        telefono = vistaForm.addEditMaterialLayout(getString(R.string.telefono), EVENTO_TELEFONO, ViewGroupLayout.LLAMADA, activityBase);
        asunto = vistaForm.addEditMaterialLayout(R.string.asunto, EVENTO_ASUNTO);
        mensaje = vistaForm.addEditMaterialLayout(R.string.texto_mensaje, EVENTO_MENSAJE);
        btnColor = vistaForm.addButtonTrans(R.string.color_trabajo);
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogos.PaletaColoresDialog(new Dialogos.PaletaColoresDialog.OnClick() {
                    @Override
                    public void onClick(String color) {
                        btnColor.setBackgroundColor(Color.parseColor(color));
                        CRUDutil.actualizarCampo(modeloSQL, EVENTO_COLOR, color);
                        modeloSQL = CRUDutil.updateModelo(modeloSQL);
                    }
                }, contexto).show(getFragmentManager(), "paletacolores");
            }
        });
        aviso = vistaForm.addCheckBox(R.string.aviso, false);
        aviso.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {

                if (!eventoPasado()) {

                    chNotificado.setVisibility(View.VISIBLE);
                    visible(generarAviso);
                    visible(vistaAviso.getViewGroup());

                } else {
                    Toast.makeText(contexto, R.string.evento_pasado, Toast.LENGTH_SHORT).show();
                    aviso.setChecked(false);
                }

            } else {

                gone(vistaAviso.getViewGroup());
                gone(chNotificado);
                gone(generarAviso);

            }
        });
        vistaAviso = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaAviso.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        avisoDias = vistaAviso.addEditMaterialLayout(R.string.dias, 1);
        avisoHoras = vistaAviso.addEditMaterialLayout(R.string.horas, 1);
        avisoMinutos = vistaAviso.addEditMaterialLayout(R.string.minutos, 1);
        actualizarArrays(vistaAviso);
        generarAviso = vistaForm.addButtonPrimary(R.string.generar_aviso);
        generarAviso.setOnClickListener(v -> {

            long fechaaviso = (JavaUtil.comprobarLong(avisoMinutos.getText().toString()) * MINUTOSLONG) +
                    (JavaUtil.comprobarLong(avisoHoras.getText().toString()) * HORASLONG) +
                    (JavaUtil.comprobarLong(avisoDias.getText().toString()) * DIASLONG);


            long fechaHoraEvento;

            if (tevento.equals(TIPOEVENTOTAREA)) {
                fechaHoraEvento = modeloSQL.getLong(EVENTO_FECHAFINEVENTO) +
                        modeloSQL.getLong(EVENTO_HORAFINEVENTO);
            } else {
                fechaHoraEvento = modeloSQL.getLong(EVENTO_FECHAINIEVENTO) +
                        modeloSQL.getLong(EVENTO_HORAINIEVENTO);
            }

            System.out.println("fechaHoraEvento = " + TimeDateUtil.getDateTimeString(fechaHoraEvento));
            System.out.println("fechaHoraAviso = " + TimeDateUtil.getDateTimeString(fechaHoraEvento - fechaaviso));

            if (fechaaviso > 0 && TimeDateUtil.ahora() < fechaHoraEvento - fechaaviso) {

                if (avisoDias.getLinearLayout().getVisibility() == View.VISIBLE) {


                    String saviso = getString(R.string.avisar) +
                            " " +
                            avisoDias.getTexto() +
                            " " +
                            getString(R.string.dias) +
                            " " +
                            getString(R.string.y) +
                            " " +
                            avisoHoras.getTexto() +
                            " " +
                            getString(R.string.horas) +
                            " " +
                            getString(R.string.y) +
                            " " +
                            avisoMinutos.getTexto() +
                            " " +
                            getString(R.string.minutos) +
                            " " +
                            getString(R.string.antes);

                    textAviso.setText(saviso);
                    textAviso.setTextColor(getResources().getColor(R.color.colorAccent));
                    textAviso.setGravity(Gravity.CENTER);
                    fechaAvisoTxt.setText(TimeDateUtil.getDateTimeString(fechaHoraEvento - fechaaviso));
                    fechaAvisoTxt.setTextColor(getResources().getColor(R.color.colorAccent));
                    fechaAvisoTxt.setGravity(Gravity.CENTER);
                    CRUDutil.actualizarCampo(modeloSQL, EVENTO_AVISO, fechaaviso);
                    visible(textAviso);
                    visible(fechaAvisoTxt);

                } else {
                    visible(avisoDias.getLinearLayout());
                    visible(avisoHoras.getLinearLayout());
                    visible(avisoMinutos.getLinearLayout());
                    CRUDutil.actualizarCampo(modeloSQL, EVENTO_AVISO, 0);
                }
            } else if (TimeDateUtil.ahora() > fechaHoraEvento - fechaaviso) {

                Toast.makeText(contexto, R.string.fecha_aviso_pasada, Toast.LENGTH_SHORT).show();

            } else {

                aviso.setChecked(false);
                visible(aviso);
                gone(generarAviso);
                gone(textAviso);
                gone(chNotificado);
                gone(avisoDias.getLinearLayout());
                gone(avisoHoras.getLinearLayout());
                gone(avisoMinutos.getLinearLayout());

            }
        });
        textAviso = vistaForm.addTextView(R.string.aviso);
        fechaAvisoTxt = vistaForm.addTextView(R.string.aviso);
        chNotificado = vistaForm.addCheckBox(R.string.notificado, false);
        chNotificado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    notificado = 0;
                    CRUDutil.actualizarCampo(modeloSQL, EVENTO_NOTIFICADO, 0);
                    gone(chNotificado);
                    visible(aviso);
                    aviso.setChecked(false);
                    gone(textAviso);
                } else {
                    notificado = 1;
                    CRUDutil.actualizarCampo(modeloSQL, EVENTO_NOTIFICADO, 1);
                    aviso.setChecked(false);
                    CRUDutil.actualizarCampo(modeloSQL, EVENTO_AVISO, 0);
                    gone(generarAviso);
                    gone(vistaAviso.getViewGroup());
                    textAviso.setText(R.string.aviso_notificado);
                    gone(fechaAvisoTxt);
                    visible(textAviso);


                }
                CRUDutil.actualizarCampo(modeloSQL, EVENTO_NOTIFICADO, notificado);
                modeloSQL = CRUDutil.updateModelo(modeloSQL);
            }
        });
        btnVerRepeticiones = vistaForm.addButtonSecondary(R.string.ver_repeticiones);
        btnVerRepeticiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verRepeticiones();
            }
        });
        repeticiones = vistaForm.addCheckBox(R.string.modificar_repeticiones, false);
        repeticiones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    mismoDiaMes.setVisibility(View.VISIBLE);
                    mismoDiaAnio.setVisibility(View.VISIBLE);
                    repAnios.getLinearLayout().setVisibility(View.VISIBLE);
                    repMeses.getLinearLayout().setVisibility(View.VISIBLE);
                    repDias.getLinearLayout().setVisibility(View.VISIBLE);
                    drepAnios.getLinearLayout().setVisibility(View.VISIBLE);
                    drepMeses.getLinearLayout().setVisibility(View.VISIBLE);
                    drepDias.getLinearLayout().setVisibility(View.VISIBLE);
                    lrep.setVisibility(View.VISIBLE);
                    ldrep.setVisibility(View.VISIBLE);
                    visible(generarRepeticiones);

                } else if (idMulti == null) {

                    mismoDiaMes.setVisibility(View.GONE);
                    mismoDiaAnio.setVisibility(View.GONE);
                    repAnios.getLinearLayout().setVisibility(View.GONE);
                    repMeses.getLinearLayout().setVisibility(View.GONE);
                    repDias.getLinearLayout().setVisibility(View.GONE);
                    repAnios.setText("0");
                    repMeses.setText("0");
                    repDias.setText("0");
                    drepAnios.getLinearLayout().setVisibility(View.GONE);
                    drepMeses.getLinearLayout().setVisibility(View.GONE);
                    drepDias.getLinearLayout().setVisibility(View.GONE);
                    drepAnios.setText("0");
                    drepMeses.setText("0");
                    drepDias.setText("0");
                    lrep.setVisibility(View.GONE);
                    ldrep.setVisibility(View.GONE);
                    gone(generarRepeticiones);

                }
            }
        });
        mismoDiaMes = vistaForm.addCheckBox(R.string.mismo_dia_mes, false);
        mismoDiaMes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    mismoDiaAnio.setVisibility(View.GONE);
                    repAnios.getLinearLayout().setVisibility(View.GONE);
                    repMeses.getLinearLayout().setVisibility(View.GONE);
                    repDias.getLinearLayout().setVisibility(View.GONE);
                    repAnios.setText("0");
                    repMeses.setText("0");
                    repDias.setText("0");
                } else {

                    mismoDiaAnio.setVisibility(View.VISIBLE);
                    repAnios.getLinearLayout().setVisibility(View.VISIBLE);
                    repMeses.getLinearLayout().setVisibility(View.VISIBLE);
                    repDias.getLinearLayout().setVisibility(View.VISIBLE);
                    repAnios.setText("0");
                    repMeses.setText("0");
                    repDias.setText("0");

                }
            }
        });

        mismoDiaAnio = vistaForm.addCheckBox(R.string.mismo_dia_anio, false);
        mismoDiaAnio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    mismoDiaMes.setVisibility(View.GONE);
                    repAnios.getLinearLayout().setVisibility(View.GONE);
                    repMeses.getLinearLayout().setVisibility(View.GONE);
                    repDias.getLinearLayout().setVisibility(View.GONE);
                    repAnios.setText("0");
                    repMeses.setText("0");
                    repDias.setText("0");
                } else {

                    mismoDiaMes.setVisibility(View.VISIBLE);
                    repAnios.getLinearLayout().setVisibility(View.VISIBLE);
                    repMeses.getLinearLayout().setVisibility(View.VISIBLE);
                    repDias.getLinearLayout().setVisibility(View.VISIBLE);
                    repAnios.setText("0");
                    repMeses.setText("0");
                    repDias.setText("0");

                }
            }
        });
        ViewGroupLayout vistaRep = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaRep.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        lrep = vistaRep.addTextView(R.string.repetir_evento, 1);
        repAnios = vistaRep.addEditMaterialLayout(R.string.anios, 1);
        repMeses = vistaRep.addEditMaterialLayout(R.string.meses, 1);
        repDias = vistaRep.addEditMaterialLayout(R.string.dias, 1);
        actualizarArrays(vistaRep);

        ViewGroupLayout vistaDur = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaDur.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        ldrep = vistaDur.addTextView(R.string.duracion, 1);
        drepAnios = vistaDur.addEditMaterialLayout(R.string.anios, 1);
        drepMeses = vistaDur.addEditMaterialLayout(R.string.meses, 1);
        drepDias = vistaDur.addEditMaterialLayout(R.string.dias, 1);
        actualizarArrays(vistaDur);

        generarRepeticiones = vistaForm.addButtonPrimary(R.string.generar_repeticiones);
        generarRepeticiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean nuevo = false;

                if (idMulti != null) {
                    String seleccion = ContratoPry.Tablas.EVENTO_IDMULTI + " = '" + idMulti +
                            "' AND " + ContratoPry.Tablas.EVENTO_ID_EVENTO +
                            " <> '" + id + "'";
                    ConsultaBD.deleteRegistros(TABLA_EVENTO, seleccion);

                } else {
                    idMulti = id;
                    nuevo = true;
                }

                valores = new ContentValues();

                ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_IDMULTI, idMulti);

                ConsultaBD.updateRegistro(TABLA_EVENTO, idMulti, valores);

                long hoy = hoy();
                if (finiEvento == 0) {
                    finiEvento = hoy;
                }

                int dia = JavaUtil.diaMes(finiEvento);

                long diffecha = ffinEvento - finiEvento;

                long offRep = 0;
                long fecharep = 0;

                if (mismoDiaMes.isChecked()) {

                    fecharep = JavaUtil.mismoDiaMes(finiEvento, dia);

                } else if (mismoDiaAnio.isChecked()) {

                    fecharep = JavaUtil.mismoDiaAnio(finiEvento, dia);

                } else {

                    offRep = (JavaUtil.comprobarLong(repAnios.getText().toString()) * ANIOSLONG) +
                            (JavaUtil.comprobarLong(repMeses.getText().toString()) * MESESLONG) +
                            (JavaUtil.comprobarLong(repDias.getText().toString()) * DIASLONG);

                    fecharep = finiEvento + offRep;

                }

                long duracionRep = (JavaUtil.comprobarLong(drepAnios.getText().toString()) * ANIOSLONG) +
                        (JavaUtil.comprobarLong(drepMeses.getText().toString()) * MESESLONG) +
                        (JavaUtil.comprobarLong(drepDias.getText().toString()) * DIASLONG);


                int reg = 0;

                while (duracionRep + hoy > fecharep) {

                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTO, fecharep);
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTOF, getDate(fecharep));


                    if (tevento.equals(Interactor.TiposEvento.TIPOEVENTOEVENTO)) {
                        ConsultaBD.putDato(valores, campos, EVENTO_FECHAFINEVENTO, String.valueOf(fecharep + diffecha));
                        ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAFINEVENTOF, getDate(fecharep + diffecha));

                    }
                    Uri uri = ConsultaBD.insertRegistro(TABLA_EVENTO, valores);

                    if (uri != null) {
                        reg++;
                    }

                    if (mismoDiaMes.isChecked()) {

                        fecharep = JavaUtil.mismoDiaMes(fecharep, dia);

                    } else if (mismoDiaAnio.isChecked()) {

                        fecharep = JavaUtil.mismoDiaAnio(fecharep, dia);

                    } else {

                        fecharep += offRep;
                    }
                }

                if (nuevo && reg > 0) {
                    Toast.makeText(contexto, R.string.repeticiones_creadas, Toast.LENGTH_SHORT).show();
                } else if (reg > 0) {
                    Toast.makeText(contexto, R.string.repeticiones_actualizadas, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(contexto, R.string.no_actualizado, Toast.LENGTH_SHORT).show();
                }

            }
        });

        proyRel = vistaForm.addEditMaterialLayout(R.string.proyecto_relacionado);
        proyRel.getEditText().setEnabled(false);
        proyRel.getBtnInicio().setImageResource(R.drawable.ic_search_black_24dp);
        proyRel.getBtnInicio().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                putBundle(ORIGEN, EVENTO);
                putBundle(EVENTO, modeloSQL);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
            }
        });
        proyRel.setImgBtnAccion(R.drawable.ic_eliminar_acent);
        proyRel.setClickAccion(new EditMaterialLayout.ClickAccion() {
            @Override
            public void onClickAccion(View view) {

                idProyecto = null;
                nombreProyecto = null;
                proyRel.setText(null);
                CRUDutil.actualizarCampoNull(modeloSQL, EVENTO_PROYECTOREL);
                CRUDutil.actualizarCampoNull(modeloSQL, EVENTO_NOMPROYECTOREL);
                CRUDutil.actualizarCampoNull(modeloSQL, EVENTO_CLIENTEREL);
                CRUDutil.actualizarCampoNull(modeloSQL, EVENTO_NOMCLIENTEREL);
                CRUDutil.updateModelo(modeloSQL);

            }
        });

        cliRel = vistaForm.addEditMaterialLayout(R.string.cliente_relacionado);
        cliRel.getEditText().setEnabled(false);
        cliRel.getBtnInicio().setImageResource(R.drawable.ic_search_black_24dp);
        cliRel.getBtnInicio().setOnClickListener(v -> {

            bundle = new Bundle();
            putBundle(ORIGEN, EVENTO);
            putBundle(EVENTO, modeloSQL);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDCliente());
        });
        cliRel.setImgBtnAccion(R.drawable.ic_eliminar_acent);
        cliRel.setClickAccion(view -> {

            idCliente = null;
            nombreCliente = null;
            cliRel.setText(null);
            CRUDutil.actualizarCampoNull(modeloSQL, EVENTO_CLIENTEREL);
            CRUDutil.actualizarCampoNull(modeloSQL, EVENTO_NOMCLIENTEREL);
            CRUDutil.updateModelo(modeloSQL);

        });

        ViewGroupLayout vistaComplet = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaComplet.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        completa = vistaComplet.addEditMaterialLayout(R.string.completa, EVENTO_COMPLETADA, 1);
        completada = vistaComplet.addCheckBox(R.string.completada, false);
        completada.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                completa.setText("100.0");
                CRUDutil.actualizarCampo(modeloSQL, EVENTO_COMPLETADA, 100);
            } else {
                completa.setText("0.0");
            }
        });
        actualizarArrays(vistaComplet);

        ViewGroupLayout vistaNotas = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaNotas.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        btnNota = vistaNotas.addImageButtonSecundary(R.drawable.ic_nueva_nota_indigo, 1);
        btnNota.setOnClickListener(v -> {

            enviarBundle();
            bundle.putString(IDREL, modeloSQL.getString(EVENTO_ID_EVENTO));
            bundle.putString(SUBTITULO, modeloSQL.getString(EVENTO_DESCRIPCION));
            bundle.putString(ORIGEN, EVENTO);
            bundle.putString(ACTUAL, NOTA);
            bundle.putSerializable(MODELO, null);
            bundle.putSerializable(LISTA, null);
            bundle.putString(CAMPO_ID, null);
            bundle.putBoolean(NUEVOREGISTRO, true);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevaNota());
        });
        btnVerNotas = vistaNotas.addImageButtonSecundary(R.drawable.ic_lista_notas_indigo, 1);
        btnVerNotas.setOnClickListener(v -> {

            enviarBundle();
            bundle.putString(IDREL, modeloSQL.getString(EVENTO_ID_EVENTO));
            bundle.putString(SUBTITULO, modeloSQL.getString(EVENTO_DESCRIPCION));
            bundle.putString(ORIGEN, EVENTO);
            bundle.putString(ACTUAL, NOTA);
            bundle.putSerializable(LISTA, null);
            bundle.putSerializable(MODELO, null);
            bundle.putString(CAMPO_ID, null);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
        });
        actualizarArrays(vistaNotas);
        actualizarArrays(vistaForm);

    }

    private boolean eventoPasado() {

        long fechaHoraEvento = 0;

        if (!tevento.equals(TIPOEVENTOTAREA)) {

            fechaHoraEvento = finiEvento + hiniEvento;

        } else {

            fechaHoraEvento = ffinEvento + hfinEvento;
        }

        return TimeDateUtil.ahora() >= fechaHoraEvento;
    }

    @Override
    protected void setLayout() {

        //layoutCuerpo = R.layout.fragment_crud_evento;
        layoutItem = R.layout.item_list_evento;

    }

    @Override
    protected void setLista() {

        if (origen != null && origen.equals(CALENDARIO)) {
            visibleSoloBtnBack();
        }

    }

    @Override
    protected void alGuardarCampo(EditMaterialLayout editMaterialLayout) {
        super.alGuardarCampo(editMaterialLayout);

        if (editMaterialLayout.getId() == asunto.getId()) {
            email.setAsunto(editMaterialLayout.getTexto());
        } else if (editMaterialLayout.getId() == mensaje.getId()) {
            email.setMensaje(editMaterialLayout.getTexto());
        }
    }

    @Override
    protected void onClickNuevo() {
        icFragmentos.enviarBundleAFragment(null, new FragmentNuevoEvento());
    }

    @Override
    protected void setNuevo() {


        comprobarCliProy();


    }

    @Override
    protected void mostrarDialogDelete() {

        if (idMulti == null) {
            delete();
        } else {
            super.mostrarDialogDelete();
        }
    }

    @Override
    protected boolean delete() {

        return mostrarDialogoBorrarRep(id);

    }



    @Override
    protected void setTabla() {

        tabla = TABLA_EVENTO;

    }

    @Override
    protected void setTablaCab() {

        tablaCab = ContratoPry.getTabCab(tabla);
    }

    @Override
    protected void setCampos() {

        campos = ContratoPry.obtenerCampos(tabla);

    }

    @Override
    protected void setBundle() {

        proyecto = (ModeloSQL) bundle.getSerializable(PROYECTO);
        cliente = (ModeloSQL) bundle.getSerializable(CLIENTE);
        tevento = bundle.getString(TIPO);
        long fecha = bundle.getLong(FECHA);
        long hora = bundle.getLong(HORACAL);
        if (tevento != null && tevento.equals(TIPOEVENTOTAREA)) {
            if (fecha > 0) {
                ffinEvento = fecha;
            }
            if (hora > 0) {
                hfinEvento = hora;
            }
        } else {
            if (fecha > 0) {
                finiEvento = fecha;
            }
            if (hora > 0) {
                hiniEvento = hora;
            }
        }
        origen = bundle.getString(ORIGEN);

        if (modeloSQL != null) {
            idMulti = modeloSQL.getString(EVENTO_IDMULTI);
            tevento = modeloSQL.getString(EVENTO_TIPO);
        }
        System.out.println("origen = " + origen);
        if (origen == null) {
            origen = EVENTO;
        }

    }

    @Override
    protected void setDatos() {

        gone(btnsave);

        if (modeloSQL.getString(EVENTO_RUTAFOTO) == null) {

            if (proyecto != null && proyecto.getString(PROYECTO_RUTAFOTO) != null) {
                path = proyecto.getString(PROYECTO_RUTAFOTO);
                imagen.setImageUri(path);
            } else if (cliente != null && cliente.getString(CLIENTE_RUTAFOTO) != null) {
                path = cliente.getString(CLIENTE_RUTAFOTO);
                imagen.setImageUri(path);
            }

            CRUDutil.actualizarCampo(modeloSQL, EVENTO_RUTAFOTO, path);
        }

        if (modeloSQL.getString(EVENTO_COLOR) != null) {
            btnColor.setBackgroundColor(Color.parseColor(modeloSQL.getString(EVENTO_COLOR)));
        }

        email.setPathEmail(modeloSQL.getString(EVENTO_RUTAADJUNTO));
        allGone();
        btnVerNotas.setVisibility(View.VISIBLE);
        btnNota.setVisibility(View.VISIBLE);
        btndelete.setVisibility(View.VISIBLE);
        mismoDiaMes.setVisibility(View.GONE);
        mismoDiaAnio.setVisibility(View.GONE);
        mismoDiaMes.setChecked(false);
        mismoDiaAnio.setChecked(false);
        chNotificado.setVisibility(View.GONE);
        visible(imagen.getLinearLayoutCompat());
        visible(descipcion.getLinearLayout());
        visible(cliRel.getLinearLayout());
        visible(proyRel.getLinearLayout());
        visible(generarRepeticiones);
        visible(avisoDias.getLinearLayout());
        visible(avisoHoras.getLinearLayout());
        visible(avisoMinutos.getLinearLayout());
        visible(btnColor);

        completa.setText(modeloSQL.getString(EVENTO_COMPLETADA));

        notificado = modeloSQL.getInt(EVENTO_NOTIFICADO);

        finiEvento = modeloSQL.getLong(EVENTO_FECHAINIEVENTO);
        hiniEvento = modeloSQL.getLong(EVENTO_HORAINIEVENTO);
        ffinEvento = modeloSQL.getLong(EVENTO_FECHAFINEVENTO);
        hfinEvento = modeloSQL.getLong(EVENTO_HORAFINEVENTO);

        if (ConsultaBD.checkQueryList(CAMPOS_NOTA,NOTA_ID_RELACIONADO,id,null,IGUAL,null)){
            btnVerNotas.setVisibility(View.VISIBLE);
        }else{
            btnVerNotas.setVisibility(View.GONE);
        }


        if (modeloSQL.getString(EVENTO_PROYECTOREL) != null) {

            idProyecto = modeloSQL.getString(EVENTO_PROYECTOREL);
            nombreProyecto = modeloSQL.getString(EVENTO_NOMPROYECTOREL);
            proyRel.setText(nombreProyecto);
            idCliente = modeloSQL.getString(EVENTO_CLIENTEREL);
            nombreCliente = modeloSQL.getString(EVENTO_NOMCLIENTEREL);
            cliRel.setText(nombreCliente);
            proyRel.getLinearLayout().setVisibility(View.VISIBLE);
            cliRel.getLinearLayout().setVisibility(View.VISIBLE);
            activityBase.toolbar.setSubtitle(nombreProyecto);


        } else if (modeloSQL.getString(EVENTO_CLIENTEREL) != null) {

            idCliente = modeloSQL.getString(EVENTO_CLIENTEREL);
            nombreCliente = modeloSQL.getString(EVENTO_NOMCLIENTEREL);
            if (nombreProyecto==null) {
                activityBase.toolbar.setSubtitle(nombreCliente);
            }

            cliRel.setText(nombreCliente);
            cliRel.getLinearLayout().setVisibility(View.VISIBLE);
        } else {
            comprobarCliProy();
        }

        repeticiones.setChecked(false);
        completa.getLinearLayout().setVisibility(View.VISIBLE);

        idMulti = modeloSQL.getString(EVENTO_IDMULTI);

        if (idMulti==null){
            repeticiones.setText(getString(R.string.crear_repeticiones));
            gone(generarRepeticiones);
            btnVerRepeticiones.setVisibility(View.GONE);
        }else{

            generarRepeticiones.setText(getString(R.string.modificar_repeticiones));
            gone(repeticiones);
            btnVerRepeticiones.setVisibility(View.VISIBLE);

        }

        if (modeloSQL != null) {
            tevento = modeloSQL.getString(EVENTO_TIPO);
            descipcion.setText(modeloSQL.getString(EVENTO_DESCRIPCION));
            asunto.setText(modeloSQL.getString(EVENTO_ASUNTO));
            mensaje.setText(modeloSQL.getString(EVENTO_MENSAJE));


            if (modeloSQL.getLong(EVENTO_AVISO) > 0) {

                long[] res = JavaUtil.longA_ddhhmm(modeloSQL.getLong(EVENTO_AVISO));

                aviso.setChecked(true);
                generarAviso.setText(R.string.modificar_aviso);
                chNotificado.setVisibility(View.VISIBLE);
                visible(textAviso);
                visible(fechaAvisoTxt);

                if (notificado==1){
                    chNotificado.setChecked(true);
                    visible(chNotificado);
                    aviso.setChecked(false);
                    CRUDutil.actualizarCampo(modeloSQL, EVENTO_AVISO, 0);
                    gone(generarAviso);
                    gone(vistaAviso.getViewGroup());
                    textAviso.setText(R.string.aviso_notificado);
                    gone(fechaAvisoTxt);

                }else{
                    chNotificado.setChecked(false);
                    visible(generarAviso);
                    visible(textAviso);
                    visible(fechaAvisoTxt);
                    visible(vistaAviso.getViewGroup());
                    avisoDias.setText(String.valueOf(res[0]));
                    avisoHoras.setText(String.valueOf(res[1]));
                    avisoMinutos.setText(String.valueOf(res[2]));

                    String saviso = getString(R.string.avisar) +
                            " " +
                            avisoDias.getTexto() +
                            " " +
                            getString(R.string.dias) +
                            " " +
                            getString(R.string.y) +
                            " " +
                            avisoHoras.getTexto() +
                            " " +
                            getString(R.string.horas) +
                            " " +
                            getString(R.string.y) +
                            " " +
                            avisoMinutos.getTexto() +
                            " " +
                            getString(R.string.minutos) +
                            " " +
                            getString(R.string.antes);

                    textAviso.setText(saviso);
                    textAviso.setTextColor(getResources().getColor(R.color.colorAccent));
                    textAviso.setGravity(Gravity.CENTER);

                    long fechaaviso = (JavaUtil.comprobarLong(avisoMinutos.getText().toString()) * MINUTOSLONG) +
                            (JavaUtil.comprobarLong(avisoHoras.getText().toString()) * HORASLONG) +
                            (JavaUtil.comprobarLong(avisoDias.getText().toString()) * DIASLONG);


                    long fechaHoraEvento;

                    if (tevento.equals(TIPOEVENTOTAREA)) {
                        fechaHoraEvento = modeloSQL.getLong(EVENTO_FECHAFINEVENTO) +
                                modeloSQL.getLong(EVENTO_HORAFINEVENTO);
                    } else {
                        fechaHoraEvento = modeloSQL.getLong(EVENTO_FECHAINIEVENTO) +
                                modeloSQL.getLong(EVENTO_HORAINIEVENTO);
                    }

                    fechaAvisoTxt.setText(TimeDateUtil.getDateTimeString(fechaHoraEvento - fechaaviso));
                    fechaAvisoTxt.setTextColor(getResources().getColor(R.color.colorAccent));
                    fechaAvisoTxt.setGravity(Gravity.CENTER);


                }

            } else {
                gone(vistaAviso.getViewGroup());
                avisoDias.setText("0");
                avisoHoras.setText("0");
                avisoMinutos.setText("0");
                aviso.setChecked(false);
                visible(aviso);
                gone(generarAviso);

                if (notificado == 1) {
                    chNotificado.setChecked(true);
                    textAviso.setText(R.string.aviso_notificado);
                    gone(fechaAvisoTxt);
                    visible(textAviso);

                } else {

                    chNotificado.setChecked(false);
                    chNotificado.setVisibility(View.GONE);
                    gone(textAviso);
                    gone(fechaAvisoTxt);

                }
            }
        }

        if (tevento!=null) {

            imagen.setTextTitulo(tevento.toUpperCase());
            imagen.setSizeTextTitulo(sizeText * 2);
            imagen.getTitulo().setGravity(Gravity.CENTER);
            fechaIni.getLinearLayout().setVisibility(View.VISIBLE);
            horaIni.getLinearLayout().setVisibility(View.VISIBLE);
            fechaFin.getLinearLayout().setVisibility(View.VISIBLE);
            horaFin.getLinearLayout().setVisibility(View.VISIBLE);

            if (finiEvento == 0) {
                fechaIni.setText(getString(R.string.sinfecha));
            } else {
                fechaIni.setText(TimeDateUtil.getDateString(finiEvento));
            }
            if (hiniEvento == 0) {
                horaIni.setText(getString(R.string.sinhora));
            } else {
                horaIni.setText(TimeDateUtil.getTimeString(hiniEvento));
            }
            if (ffinEvento == 0) {
                fechaFin.setText(getString(R.string.sinfecha));
            } else {
                fechaFin.setText(TimeDateUtil.getDateString(ffinEvento));
            }
            if (hfinEvento == 0) {
                horaFin.setText(getString(R.string.sinhora));
            } else {
                horaFin.setText(TimeDateUtil.getTimeString(hfinEvento));
            }

            switch (tevento) {

                case TIPOEVENTOTAREA:

                    break;

                case TIPOEVENTOCITA:
                    lugar.getLinearLayout().setVisibility(View.VISIBLE);
                    asunto.getLinearLayout().setVisibility(View.VISIBLE);
                    lugar.setText(modeloSQL.getString(EVENTO_DIRECCION));
                    break;

                case TIPOEVENTOEMAIL:
                    email.getLinearLayout().setVisibility(View.VISIBLE);
                    asunto.getLinearLayout().setVisibility(View.VISIBLE);
                    mensaje.getLinearLayout().setVisibility(View.VISIBLE);
                    email.setText(modeloSQL.getString(EVENTO_EMAIL));
                    break;

                case TIPOEVENTOLLAMADA:
                    telefono.getLinearLayout().setVisibility(View.VISIBLE);
                    asunto.getLinearLayout().setVisibility(View.VISIBLE);
                    mensaje.getLinearLayout().setVisibility(View.VISIBLE);
                    telefono.setText(modeloSQL.getString(EVENTO_TELEFONO));
                    break;

                case Interactor.TiposEvento.TIPOEVENTOEVENTO:

                    break;

            }
        }

    }

    private void comprobarCliProy(){


        if (proyecto != null) {

            idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);
            nombreProyecto = proyecto.getString(PROYECTO_NOMBRE);
            proyRel.setText(nombreProyecto);
            visible(proyRel.getLinearLayout());
            CRUDutil.actualizarCampo(modeloSQL, EVENTO_PROYECTOREL, idProyecto);
            CRUDutil.actualizarCampo(modeloSQL, EVENTO_NOMPROYECTOREL, nombreProyecto);
            cliente = CRUDutil.updateModelo(CAMPOS_CLIENTE, proyecto.getString(PROYECTO_ID_CLIENTE));
            idCliente = cliente.getString(CLIENTE_ID_CLIENTE);
            nombreCliente = cliente.getString(CLIENTE_NOMBRE);
            cliRel.setText(nombreCliente);
            visible(cliRel.getLinearLayout());
            telefono.setText(cliente.getString(CLIENTE_TELEFONO));
            lugar.setText(cliente.getString(CLIENTE_DIRECCION));
            email.setText(cliente.getString(CLIENTE_EMAIL));
            CRUDutil.actualizarCampo(modeloSQL, EVENTO_CLIENTEREL, idCliente);
            CRUDutil.actualizarCampo(modeloSQL, EVENTO_NOMCLIENTEREL, nombreCliente);
            CRUDutil.actualizarCampo(modeloSQL, EVENTO_TELEFONO, cliente.getString(CLIENTE_TELEFONO));
            CRUDutil.actualizarCampo(modeloSQL, EVENTO_DIRECCION, cliente.getString(CLIENTE_DIRECCION));
            CRUDutil.actualizarCampo(modeloSQL, EVENTO_EMAIL, cliente.getString(CLIENTE_EMAIL));

            activityBase.toolbar.setSubtitle(subTitulo + " - " + proyecto.getString(PROYECTO_NOMBRE) + " - " + cliente.getString(CLIENTE_NOMBRE));

        } else if (cliente != null) {

            idCliente = cliente.getString(CLIENTE_ID_CLIENTE);
            nombreCliente = cliente.getString(CLIENTE_NOMBRE);
            cliRel.setText(nombreCliente);
            telefono.setText(cliente.getString(CLIENTE_TELEFONO));
            lugar.setText(cliente.getString(CLIENTE_DIRECCION));
            email.setText(cliente.getString(CLIENTE_EMAIL));
            CRUDutil.actualizarCampo(modeloSQL, EVENTO_CLIENTEREL, idCliente);
            CRUDutil.actualizarCampo(modeloSQL, EVENTO_NOMCLIENTEREL, nombreCliente);
            CRUDutil.actualizarCampo(modeloSQL, EVENTO_TELEFONO, cliente.getString(CLIENTE_TELEFONO));
            CRUDutil.actualizarCampo(modeloSQL, EVENTO_DIRECCION, cliente.getString(CLIENTE_DIRECCION));
            CRUDutil.actualizarCampo(modeloSQL, EVENTO_EMAIL, cliente.getString(CLIENTE_EMAIL));

            activityBase.toolbar.setSubtitle(subTitulo + " - " + cliente.getString(CLIENTE_NOMBRE));

        } else {

            activityBase.toolbar.setSubtitle(subTitulo);

        }

        imagen.getLinearLayoutCompat().setVisibility(View.VISIBLE);
        repeticiones.setText(getString(R.string.crear_repeticiones));

        if (lista != null && lista.sizeLista() > 0) {
            visible(frLista);
        }

        if (nuevo) {
            onUpdate();
        }

    }

    private void verRepeticiones(){

        idMulti = modeloSQL.getString(EVENTO_IDMULTI);
        listab = new ListaModeloSQL(campos, EVENTO_IDMULTI, idMulti, null, IGUAL, null);
        onBack();
    }

    @Override
    protected void setTitulo() {

        tituloPlural = R.string.eventos;
        tituloSingular = R.string.evento;
        tituloNuevo = R.string.nuevo_evento;
    }

    @Override
    protected void setContenedor() {

        setDato(EVENTO_TIPO, tevento);
        if (finiEvento > 0) {
            setDato(EVENTO_FECHAINIEVENTO, finiEvento);
            setDato(EVENTO_FECHAINIEVENTOF, TimeDateUtil.getDateString(finiEvento));
            if (ffinEvento == 0) {
                setDato(EVENTO_FECHAFINEVENTO, finiEvento);
                setDato(EVENTO_FECHAFINEVENTOF, TimeDateUtil.getDateString(finiEvento));
            }
        }
        if (ffinEvento > 0) {
            setDato(EVENTO_FECHAFINEVENTO, ffinEvento);
            setDato(EVENTO_FECHAFINEVENTOF, TimeDateUtil.getDateString(ffinEvento));
            if (finiEvento == 0) {
                setDato(EVENTO_FECHAINIEVENTO, ffinEvento);
                setDato(EVENTO_FECHAINIEVENTOF, TimeDateUtil.getDateString(ffinEvento));
            }
        }
        if (hiniEvento > 0) {
            setDato(EVENTO_HORAINIEVENTO, hiniEvento);
            setDato(EVENTO_HORAINIEVENTOF, TimeDateUtil.getTimeString(hiniEvento));
            if (hfinEvento == 0) {
                setDato(EVENTO_HORAFINEVENTO, hiniEvento);
                setDato(EVENTO_HORAFINEVENTOF, TimeDateUtil.getTimeString(hiniEvento));
            }
        }
        if (hfinEvento > 0) {
            setDato(EVENTO_HORAFINEVENTO, hfinEvento);
            setDato(EVENTO_HORAFINEVENTOF, TimeDateUtil.getTimeString(hfinEvento));
            if (hiniEvento > 0) {
                setDato(EVENTO_HORAINIEVENTO, hfinEvento);
                setDato(EVENTO_HORAINIEVENTOF, TimeDateUtil.getTimeString(hfinEvento));
            }
        }

    }

    @Override
    protected void setcambioFragment() {


        switch (origen) {
            case PRESUPUESTO:
            case PROYECTO:

                bundle.putSerializable(MODELO, proyecto);
                bundle.putString(CAMPO_ID, proyecto.getString(PROYECTO_ID_PROYECTO));
                bundle.putString(ACTUAL, origen);

                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());

                break;
            case CLIENTE:
            case PROSPECTO:

                bundle.putSerializable(MODELO, cliente);
                bundle.putString(ACTUAL, origen);

                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDCliente());

                break;
            case CALENDARIO:

                bundle.putString(ACTUAL, origen);

                icFragmentos.enviarBundleAFragment(bundle, new CalendarioEventos());

                break;
            default:

                if (subTitulo != null) {
                    activityBase.toolbar.setSubtitle(subTitulo);
                }
                idCliente = null;
                idProyecto = null;
                idMulti = null;
                nuevo = false;

                break;
        }

    }

    private boolean mostrarDialogoBorrarRep(final String idEvento) {

        modeloSQL = ConsultaBD.queryObject(campos, idEvento);
        final CharSequence[] opciones = {"Borrar sólo este modeloSQL", "Borrar este y repeticiones", "Borrar sólo repeticiones", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Borrar sólo este modeloSQL")) {

                    if (ConsultaBD.deleteRegistro(TABLA_EVENTO, idEvento)>0) {

                        Toast.makeText(contexto, "Registro borrado ", Toast.LENGTH_SHORT).show();
                        if (listab!=null){
                            listab = new ListaModeloSQL(campos, EVENTO_IDMULTI, idMulti, null, IGUAL, null);
                        }
                        id = null;
                        modeloSQL = null;
                        selector();

                    }else{

                        Toast.makeText(contexto, "Error al borrar registro "+idEvento, Toast.LENGTH_SHORT).show();
                    }

                } else if (opciones[which].equals("Borrar este y repeticiones")) {

                    idMulti = modeloSQL.getString(EVENTO_IDMULTI);
                    String seleccion = EVENTO_IDMULTI + " = '" + idMulti + "'";
                    if (ConsultaBD.deleteRegistros(TABLA_EVENTO,EVENTO_IDMULTI,idMulti,null,IGUAL)>0) {

                        id = null;
                        modeloSQL = null;
                        listab = null;
                        Toast.makeText(contexto, "Regitros borrados", Toast.LENGTH_SHORT).show();
                        selector();

                    }else{
                        Toast.makeText(contexto, "Error al borrar los registros "+idEvento, Toast.LENGTH_SHORT).show();
                    }


                } else if (opciones[which].equals("Borrar sólo repeticiones")) {

                    idMulti = modeloSQL.getString(EVENTO_IDMULTI);
                    String seleccion = EVENTO_IDMULTI + " = '" + idMulti +
                            "' AND " + EVENTO_ID_EVENTO + " <> '" + idEvento + "'";
                    if (ConsultaBD.deleteRegistros(TABLA_EVENTO,seleccion)>0) {

                        valores = new ContentValues();
                        valores.putNull(EVENTO_IDMULTI);
                        ConsultaBD.updateRegistro(tabla,idEvento,valores);
                        modeloSQL = CRUDutil.updateModelo(campos, idEvento);
                        idMulti = null;
                        Toast.makeText(contexto, "Regitros borrados", Toast.LENGTH_SHORT).show();
                        listab = null;
                        selector();

                    }else{
                        Toast.makeText(contexto, "Error al borrar los registros "+idEvento, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


        return true;
    }


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView descripcion,fechaini,telefono,lugar,nomPryRel,nomCliRel,
                fechafin, horaini,horafin,porccompleta,email,tipo;
        ImageView btnllamada, btnmapa, btnemail;
        ProgressBar pbar;
        ImageView foto;
        CheckBox completa;
        Button btneditar;
        CardView card;

        public ViewHolderRV(View itemView) {
            super(itemView);

            descripcion = itemView.findViewById(R.id.tvdesclevento);
            fechaini = itemView.findViewById(R.id.tvfinilevento);
            fechafin = itemView.findViewById(R.id.tvffinlevento);
            horaini = itemView.findViewById(R.id.tvhinilevento);
            horafin = itemView.findViewById(R.id.tvhfinlevento);
            telefono = itemView.findViewById(R.id.tvtelefonolevento);
            lugar = itemView.findViewById(R.id.tvlugarlevento);
            email = itemView.findViewById(R.id.tvemaillevento);
            nomPryRel = itemView.findViewById(R.id.tvnompryrellevento);
            nomCliRel = itemView.findViewById(R.id.tvnomclirelevento);
            porccompleta = itemView.findViewById(R.id.tvcompporclevento);
            btnllamada = itemView.findViewById(R.id.imgbtnllamadalevento);
            btnmapa = itemView.findViewById(R.id.imgbtnmapalevento);
            btnemail = itemView.findViewById(R.id.imgbtnemaillevento);
            pbar = itemView.findViewById(R.id.pbarlevento);
            foto = itemView.findViewById(R.id.imglevento);
            completa = itemView.findViewById(R.id.cBoxcompletlevento);
            btneditar = itemView.findViewById(R.id.btneditlevento);
            tipo = itemView.findViewById(R.id.tvtipolevento);
            card = itemView.findViewById(R.id.cardlevento);
        }

        @Override
        public void bind(@NotNull final ModeloSQL modeloSQL) {

            String tEvento = modeloSQL.getString(EVENTO_TIPO);

            if (tEvento == null) {
                tEvento = TIPOEVENTOCITA;
            }

            tipo.setText(tEvento.toUpperCase());
            descripcion.setText(modeloSQL.getString(EVENTO_DESCRIPCION));
            telefono.setText(modeloSQL.getString(EVENTO_TELEFONO));
            lugar.setText(modeloSQL.getString(EVENTO_DIRECCION));
            email.setText(modeloSQL.getString(EVENTO_EMAIL));
            nomPryRel.setText(modeloSQL.getString(EVENTO_NOMPROYECTOREL));
            nomCliRel.setText(modeloSQL.getString(EVENTO_NOMCLIENTEREL));
            fechaini.setText(getDate(modeloSQL.getLong(EVENTO_FECHAINIEVENTO)));
            fechafin.setText(getDate(modeloSQL.getLong(EVENTO_FECHAFINEVENTO)));
            horaini.setText(getTime(modeloSQL.getLong(EVENTO_HORAINIEVENTO)));
            horafin.setText(getTime(modeloSQL.getLong(EVENTO_HORAFINEVENTO)));
            double completada = modeloSQL.getDouble(EVENTO_COMPLETADA);
            System.out.println("completada = " + completada);
            if (completada == 0) {
                pbar.setVisibility(View.GONE);
                completa.setChecked(false);
                porccompleta.setVisibility(View.GONE);
            } else if (completada > 99) {
                completa.setChecked(true);
                pbar.setVisibility(View.GONE);
                porccompleta.setVisibility(View.GONE);

            } else {
                pbar.setVisibility(View.VISIBLE);
                porccompleta.setVisibility(View.VISIBLE);
                completa.setChecked(false);
                pbar.setProgress((int) completada);
                porccompleta.setText(String.format("%s %s", modeloSQL.getDouble(EVENTO_COMPLETADA), "%"));
            }


            fechaini.setVisibility(View.GONE);
            fechafin.setVisibility(View.GONE);
            horaini.setVisibility(View.GONE);
            horafin.setVisibility(View.GONE);
            btnllamada.setVisibility(View.GONE);
            btnmapa.setVisibility(View.GONE);
            btnemail.setVisibility(View.GONE);
            telefono.setVisibility(View.GONE);
            lugar.setVisibility(View.GONE);
            email.setVisibility(View.GONE);

            switch (tEvento) {

                case TAREA:
                    fechafin.setVisibility(View.VISIBLE);
                    horafin.setVisibility(View.VISIBLE);
                    break;

                case TIPOEVENTOCITA:
                    lugar.setVisibility(View.VISIBLE);
                    fechaini.setVisibility(View.VISIBLE);
                    horaini.setVisibility(View.VISIBLE);
                    btnmapa.setVisibility(View.VISIBLE);
                    break;

                case TIPOEVENTOEMAIL:
                    btnemail.setVisibility(View.VISIBLE);
                    fechaini.setVisibility(View.VISIBLE);
                    horaini.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    break;

                case TIPOEVENTOLLAMADA:
                    telefono.setVisibility(View.VISIBLE);
                    fechaini.setVisibility(View.VISIBLE);
                    horaini.setVisibility(View.VISIBLE);
                    btnllamada.setVisibility(View.VISIBLE);
                    break;

                case Interactor.TiposEvento.TIPOEVENTOEVENTO:
                    fechaini.setVisibility(View.VISIBLE);
                    horaini.setVisibility(View.VISIBLE);
                    fechafin.setVisibility(View.VISIBLE);
                    horafin.setVisibility(View.VISIBLE);
                    break;

            }
            mediaUtil = new MediaUtil(contexto);
            if (modeloSQL.getString(EVENTO_RUTAFOTO) != null) {
                mediaUtil.setImageUriCircle(modeloSQL.getString(EVENTO_RUTAFOTO), foto);
            }

            if (completada < 100) {

                long retraso = hoy() - modeloSQL.getLong(EVENTO_FECHAINIEVENTO);

                if (!tEvento.equals(TIPOEVENTOTAREA)) {
                    if (retraso > 3 * Interactor.DIASLONG) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
                    } else if (retraso > Interactor.DIASLONG) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));
                    } else {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
                    }//imgret.setImageResource(R.drawable.alert_box_v);}
                } else {
                    retraso = hoy() - modeloSQL.getLong(EVENTO_FECHAFINEVENTO);
                    if (retraso > 3 * Interactor.DIASLONG) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
                    } else if (retraso > Interactor.DIASLONG) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));
                    } else {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
                    }
                }

            } else {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
            }


            btneditar.setVisibility(View.GONE);
            btneditar.setText("EDITAR " + tEvento.toUpperCase());
            btnllamada.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppActivity.hacerLlamada(AppActivity.getAppContext()
                            , modeloSQL.getString(EVENTO_TELEFONO));
                }
            });

            btnemail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppActivity.enviarEmail(contexto, modeloSQL.getString(EVENTO_EMAIL),
                            modeloSQL.getString(EVENTO_ASUNTO), modeloSQL.getString(EVENTO_MENSAJE),
                            modeloSQL.getString(EVENTO_RUTAADJUNTO));

                }
            });

            btnmapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!modeloSQL.getString(EVENTO_DIRECCION).equals("")) {

                        viewOnMapA(contexto, modeloSQL.getString(EVENTO_DIRECCION));
                    }

                }
            });


            completa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {

                        ContentValues valores = new ContentValues();

                        ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_COMPLETADA, "100");
                        ConsultaBD.updateRegistro(TABLA_EVENTO, modeloSQL.getString
                                (EVENTO_ID_EVENTO), valores);
                        porccompleta.setVisibility(View.GONE);
                        pbar.setVisibility(View.GONE);

                    }
                }
            });

            btneditar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    id = modeloSQL.getString(EVENTO_ID_EVENTO);
                    maestroDetalle();
                    setDatos();


                }
            });

            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {

            return new ViewHolderRV(view);
        }
    }


    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<ModeloSQL> entrada) {

            TextView descripcion,fechaini,telefono,lugar,nomPryRel,nomCliRel,
                    fechafin, horaini,horafin,porccompleta,email,tipo;
            ImageView btnllamada, btnmapa, btnemail;
            ProgressBar pbar;
            ImageView foto;
            CheckBox completa;
            Button btneditar;
            CardView card;

            descripcion = itemView.findViewById(R.id.tvdesclevento);
            fechaini = itemView.findViewById(R.id.tvfinilevento);
            fechafin = itemView.findViewById(R.id.tvffinlevento);
            horaini = itemView.findViewById(R.id.tvhinilevento);
            horafin = itemView.findViewById(R.id.tvhfinlevento);
            telefono = itemView.findViewById(R.id.tvtelefonolevento);
            lugar = itemView.findViewById(R.id.tvlugarlevento);
            email = itemView.findViewById(R.id.tvemaillevento);
            nomPryRel = itemView.findViewById(R.id.tvnompryrellevento);
            nomCliRel = itemView.findViewById(R.id.tvnomclirelevento);
            porccompleta = itemView.findViewById(R.id.tvcompporclevento);
            btnllamada = itemView.findViewById(R.id.imgbtnllamadalevento);
            btnmapa = itemView.findViewById(R.id.imgbtnmapalevento);
            btnemail = itemView.findViewById(R.id.imgbtnemaillevento);
            pbar = itemView.findViewById(R.id.pbarlevento);
            foto = itemView.findViewById(R.id.imglevento);
            completa = itemView.findViewById(R.id.cBoxcompletlevento);
            btneditar = itemView.findViewById(R.id.btneditlevento);
            tipo = itemView.findViewById(R.id.tvtipolevento);
            card = itemView.findViewById(R.id.cardlevento);

            tipo.setText(entrada.get(posicion).getString(EVENTO_TIPO).toUpperCase());
            descripcion.setText(entrada.get(posicion).getCampos
                    (ContratoPry.Tablas.EVENTO_DESCRIPCION));
            telefono.setText(entrada.get(posicion).getCampos
                    (ContratoPry.Tablas.EVENTO_TELEFONO));
            lugar.setText(entrada.get(posicion).getCampos
                    (ContratoPry.Tablas.EVENTO_DIRECCION));
            email.setText(entrada.get(posicion).getCampos
                    (ContratoPry.Tablas.EVENTO_EMAIL));
            nomPryRel.setText(entrada.get(posicion).getCampos
                    (ContratoPry.Tablas.EVENTO_NOMPROYECTOREL));
            nomCliRel.setText(entrada.get(posicion).getCampos
                    (ContratoPry.Tablas.EVENTO_NOMCLIENTEREL));
            fechaini.setText(getDate(Long.parseLong(entrada.get(posicion).getCampos
                    (ContratoPry.Tablas.EVENTO_FECHAINIEVENTO))));
            fechafin.setText(getDate(Long.parseLong(entrada.get(posicion).getCampos
                    (ContratoPry.Tablas.EVENTO_FECHAFINEVENTO))));
            horaini.setText(getTime(entrada.get(posicion).getLong
                    (ContratoPry.Tablas.EVENTO_HORAINIEVENTO)));
            horafin.setText(getTime(Long.parseLong(entrada.get(posicion).getCampos
                    (ContratoPry.Tablas.EVENTO_HORAFINEVENTO))));
            double completada = entrada.get(posicion).getDouble(
                    (ContratoPry.Tablas.EVENTO_COMPLETADA));
            System.out.println("completada = " + completada);
            if (completada==0){
                completa.setChecked(false);
                pbar.setVisibility(View.GONE);
                porccompleta.setVisibility(View.GONE);
            }else if (completada>99) {
                completa.setChecked(true);
                porccompleta.setVisibility(View.GONE);
                pbar.setVisibility(View.GONE);
            }else{
                pbar.setVisibility(View.VISIBLE);
                porccompleta.setVisibility(View.VISIBLE);
                completa.setChecked(false);
                pbar.setProgress((int)completada);
                porccompleta.setText(entrada.get(posicion).getString(EVENTO_COMPLETADA));
            }

            String tipoEvento = entrada.get(posicion).getCampos
                    (ContratoPry.Tablas.EVENTO_TIPO);

            fechaini.setVisibility(View.GONE);
            fechafin.setVisibility(View.GONE);
            horaini.setVisibility(View.GONE);
            horafin.setVisibility(View.GONE);
            btnllamada.setVisibility(View.GONE);
            btnmapa.setVisibility(View.GONE);
            btnemail.setVisibility(View.GONE);
            telefono.setVisibility(View.GONE);
            lugar.setVisibility(View.GONE);
            email.setVisibility(View.GONE);

            switch (tipoEvento){

                case TAREA:
                    fechafin.setVisibility(View.VISIBLE);
                    horafin.setVisibility(View.VISIBLE);
                    break;

                case TIPOEVENTOCITA:
                    lugar.setVisibility(View.VISIBLE);
                    fechaini.setVisibility(View.VISIBLE);
                    horaini.setVisibility(View.VISIBLE);
                    btnmapa.setVisibility(View.VISIBLE);
                    break;

                case TIPOEVENTOEMAIL:
                    btnemail.setVisibility(View.VISIBLE);
                    fechaini.setVisibility(View.VISIBLE);
                    horaini.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    break;

                case TIPOEVENTOLLAMADA:
                    telefono.setVisibility(View.VISIBLE);
                    fechaini.setVisibility(View.VISIBLE);
                    horaini.setVisibility(View.VISIBLE);
                    btnllamada.setVisibility(View.VISIBLE);
                    break;

                case Interactor.TiposEvento.TIPOEVENTOEVENTO:
                    fechaini.setVisibility(View.VISIBLE);
                    horaini.setVisibility(View.VISIBLE);
                    fechafin.setVisibility(View.VISIBLE);
                    horafin.setVisibility(View.VISIBLE);
                    break;

            }
            mediaUtil = new MediaUtil(contexto);
            if (entrada.get(posicion).getCampos
                    (ContratoPry.Tablas.EVENTO_RUTAFOTO)!=null) {
                mediaUtil.setImageUriCircle(entrada.get(posicion).getCampos
                        (ContratoPry.Tablas.EVENTO_RUTAFOTO),foto);
            }


            if (completada < 100) {

                long retraso = hoy() - entrada.get(posicion).getLong(EVENTO_FECHAINIEVENTO);

                if (!tipoEvento.equals(TIPOEVENTOTAREA)) {
                    if (retraso > 3 * Interactor.DIASLONG) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
                    } else if (retraso > Interactor.DIASLONG) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));
                    } else {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
                    }//imgret.setImageResource(R.drawable.alert_box_v);}
                }else {
                    retraso = hoy() - entrada.get(posicion).getLong(EVENTO_FECHAFINEVENTO);
                    if (retraso > 3 * Interactor.DIASLONG) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
                    } else if (retraso > Interactor.DIASLONG) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));
                    } else {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
                    }
                }

            }else{
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
            }

            btneditar.setVisibility(View.GONE);

            super.setEntradas(posicion, view, entrada);
        }
    }

    private void showDatePickerDialogInicio() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance
                (hoy(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        finiEvento = JavaUtil.fechaALong(year, month, day);
                        String selectedDate = getDate(finiEvento);
                        CRUDutil.actualizarCampo(modeloSQL, EVENTO_FECHAINIEVENTO, finiEvento);
                        CRUDutil.actualizarCampo(modeloSQL, EVENTO_FECHAINIEVENTOF, selectedDate);
                        if (ffinEvento == 0) {
                            CRUDutil.actualizarCampo(modeloSQL, EVENTO_FECHAFINEVENTO, finiEvento);
                            CRUDutil.actualizarCampo(modeloSQL, EVENTO_FECHAFINEVENTOF, selectedDate);
                        }
                        modeloSQL = CRUDutil.updateModelo(modeloSQL);
                        fechaIni.setText(selectedDate);
                        if (!tevento.equals(Interactor.TiposEvento.TIPOEVENTOEVENTO)) {
                            fechaFin.setText(selectedDate);
                        }
                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void showDatePickerDialogFin() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance
                (hoy(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        ffinEvento = JavaUtil.fechaALong(year, month, day);
                        String selectedDate = getDate(ffinEvento);
                        CRUDutil.actualizarCampo(modeloSQL, EVENTO_FECHAFINEVENTO, ffinEvento);
                        CRUDutil.actualizarCampo(modeloSQL, EVENTO_FECHAFINEVENTOF, selectedDate);
                        if (finiEvento == 0) {
                            CRUDutil.actualizarCampo(modeloSQL, EVENTO_FECHAINIEVENTO, ffinEvento);
                            CRUDutil.actualizarCampo(modeloSQL, EVENTO_FECHAINIEVENTOF, selectedDate);
                        }
                        modeloSQL = CRUDutil.updateModelo(modeloSQL);
                        fechaFin.setText(selectedDate);
                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialogini(){

        TimePickerFragment newFragment = TimePickerFragment.newInstance
                (hoy(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        hiniEvento = JavaUtil.horaALong(hourOfDay, minute);
                        String selectedHour = TimeDateUtil.getTimeString(hiniEvento);
                        CRUDutil.actualizarCampo(modeloSQL, EVENTO_HORAINIEVENTO, hiniEvento);
                        CRUDutil.actualizarCampo(modeloSQL, EVENTO_HORAINIEVENTOF, selectedHour);
                        if (hfinEvento == 0) {
                            CRUDutil.actualizarCampo(modeloSQL, EVENTO_HORAFINEVENTO, hiniEvento);
                            CRUDutil.actualizarCampo(modeloSQL, EVENTO_HORAFINEVENTOF, selectedHour);
                        }
                        modeloSQL = CRUDutil.updateModelo(modeloSQL);
                        System.out.println("hiniEvento = " + hiniEvento);
                        horaIni.setText(selectedHour);

                    }
                });
        newFragment.show(getActivity().getSupportFragmentManager(),"timePicker");

    }

    public void showTimePickerDialogfin(){

        TimePickerFragment newFragment = TimePickerFragment.newInstance
                (hoy(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        hfinEvento = JavaUtil.horaALong(hourOfDay, minute);
                        String selectedHour = TimeDateUtil.getTimeString(hfinEvento);
                        CRUDutil.actualizarCampo(modeloSQL, EVENTO_HORAFINEVENTO, hfinEvento);
                        CRUDutil.actualizarCampo(modeloSQL, EVENTO_HORAFINEVENTOF, selectedHour);
                        if (hiniEvento == 0) {
                            CRUDutil.actualizarCampo(modeloSQL, EVENTO_HORAINIEVENTO, hfinEvento);
                            CRUDutil.actualizarCampo(modeloSQL, EVENTO_HORAINIEVENTOF, selectedHour);
                        }
                        modeloSQL = CRUDutil.updateModelo(modeloSQL);
                        horaFin.setText(selectedHour);

                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(),"timePicker");

    }


}
