package com.jjlacode.freelanceproject.ui;
// Created by jjlacode on 29/04/19. 

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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

import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.adapter.BaseViewHolder;
import com.jjlacode.base.util.adapter.ListaAdaptadorFiltro;
import com.jjlacode.base.util.adapter.ListaAdaptadorFiltroModelo;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.base.util.android.AppActivity;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.base.util.android.controls.ImagenLayout;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.crud.FragmentCRUD;
import com.jjlacode.base.util.crud.ListaModelo;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.media.MediaUtil;
import com.jjlacode.base.util.sqlite.ConsultaBD;
import com.jjlacode.base.util.sqlite.ContratoPry;
import com.jjlacode.base.util.time.DatePickerFragment;
import com.jjlacode.base.util.time.TimePickerFragment;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.logica.Interactor;

import java.util.ArrayList;
import java.util.List;

import static com.jjlacode.base.util.JavaUtil.getDate;
import static com.jjlacode.base.util.JavaUtil.getTime;
import static com.jjlacode.base.util.JavaUtil.hoy;
import static com.jjlacode.base.util.android.AppActivity.viewOnMapA;
import static com.jjlacode.base.util.time.calendar.clases.DiaCalBase.HORACAL;
import static com.jjlacode.freelanceproject.logica.Interactor.setNamefdef;

public class FragmentCRUDEvento extends FragmentCRUD implements Interactor.Constantes,
        ContratoPry.Tablas, Interactor.TiposEvento {

    private AutoCompleteTextView proyRel;
    private AutoCompleteTextView cliRel;
    private EditMaterial descipcion;
    private EditMaterial lugar;
    private EditMaterial telefono;
    private EditMaterial email;
    private EditMaterial asunto;
    private EditMaterial mensaje;
    private EditMaterial fechaIni;
    private EditMaterial fechaFin;
    private EditMaterial horaIni;
    private EditMaterial horaFin;
    private EditMaterial repAnios;
    private EditMaterial repMeses;
    private EditMaterial repDias;
    private EditMaterial drepAnios;
    private EditMaterial drepMeses;
    private EditMaterial drepDias;
    private EditMaterial avisoMinutos;
    private EditMaterial avisoHoras;
    private EditMaterial avisoDias;
    private CheckBox aviso;
    private CheckBox repeticiones;
    private CheckBox mismoDiaMes;
    private CheckBox mismoDiaAnio;
    private CheckBox chNotificado;
    private TextView laviso;
    private TextView lrep;
    private TextView ldrep;
    private TextView tipoEvento;
    private ImageButton btnNota;
    private ImageButton btnVerNotas;
    private Button btnVerRepeticiones;

    private ArrayList<Modelo> listaClientes;
    private ArrayList<Modelo> listaProyectos;
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
    private EditMaterial completa;

    private ImageButton btnhfin;
    private ImageButton btnhini;
    private ImageButton btnffin;
    private ImageButton btnfini;
    private ImageButton imgmapa;
    private ImageButton imgllamada;
    private ImageButton imgemail;
    private CheckBox relCli;
    private CheckBox relProy;
    private CheckBox completada;
    private Modelo proyecto;
    private Modelo cliente;
    private int notificado;


    public FragmentCRUDEvento() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    @Override
    protected void setLista() {

        if (origen.equals(CALENDARIO)) {
            visibleSoloBtnBack();
        }

    }

    @Override
    protected void onClickNuevo() {
        icFragmentos.enviarBundleAFragment(null,new FragmentNuevoEvento());
    }

    @Override
    protected void setNuevo() {

        allGone();
        visibleSoloBtnBack();
        gone(frLista);
        visible(frdetalle);

        switch (tevento) {


            case TIPOEVENTOTAREA:

                subTitulo = getString(R.string.nueva_tarea);
                visible(descipcion);
                asunto.setVisibility(View.VISIBLE);
                mensaje.setVisibility(View.VISIBLE);
                fechaFin.setVisibility(View.VISIBLE);
                horaFin.setVisibility(View.VISIBLE);
                btnffin.setVisibility(View.VISIBLE);
                btnhfin.setVisibility(View.VISIBLE);
                comprobarCliProy();
                break;

            case TIPOEVENTOCITA:

                subTitulo = getString(R.string.nueva_cita);
                visible(descipcion);
                lugar.setVisibility(View.VISIBLE);
                asunto.setVisibility(View.VISIBLE);
                mensaje.setVisibility(View.VISIBLE);
                fechaIni.setVisibility(View.VISIBLE);
                horaIni.setVisibility(View.VISIBLE);
                btnfini.setVisibility(View.VISIBLE);
                btnhini.setVisibility(View.VISIBLE);
                repeticiones.setVisibility(View.VISIBLE);
                aviso.setVisibility(View.VISIBLE);
                asunto.setVisibility(View.VISIBLE);
                mensaje.setVisibility(View.VISIBLE);
                comprobarCliProy();
                break;


            case TIPOEVENTOLLAMADA:

                subTitulo = getString(R.string.nueva_llamada);
                visible(descipcion);
                telefono.setVisibility(View.VISIBLE);
                asunto.setVisibility(View.VISIBLE);
                mensaje.setVisibility(View.VISIBLE);
                fechaIni.setVisibility(View.VISIBLE);
                horaIni.setVisibility(View.VISIBLE);
                btnfini.setVisibility(View.VISIBLE);
                btnhini.setVisibility(View.VISIBLE);
                repeticiones.setVisibility(View.VISIBLE);
                aviso.setVisibility(View.VISIBLE);
                asunto.setVisibility(View.VISIBLE);
                mensaje.setVisibility(View.VISIBLE);
                comprobarCliProy();
                break;

            case TIPOEVENTOEMAIL:

                subTitulo = getString(R.string.nuevo_email);
                visible(descipcion);
                email.setVisibility(View.VISIBLE);
                asunto.setVisibility(View.VISIBLE);
                mensaje.setVisibility(View.VISIBLE);
                fechaIni.setVisibility(View.VISIBLE);
                horaIni.setVisibility(View.VISIBLE);
                btnfini.setVisibility(View.VISIBLE);
                btnhini.setVisibility(View.VISIBLE);
                repeticiones.setVisibility(View.VISIBLE);
                aviso.setVisibility(View.VISIBLE);
                asunto.setVisibility(View.VISIBLE);
                mensaje.setVisibility(View.VISIBLE);
                comprobarCliProy();

            case TIPOEVENTOEVENTO:

                subTitulo = getString(R.string.nuevo_evento);
                visible(descipcion);
                fechaIni.setVisibility(View.VISIBLE);
                horaIni.setVisibility(View.VISIBLE);
                fechaFin.setVisibility(View.VISIBLE);
                horaFin.setVisibility(View.VISIBLE);
                btnfini.setVisibility(View.VISIBLE);
                btnhini.setVisibility(View.VISIBLE);
                btnffin.setVisibility(View.VISIBLE);
                btnhfin.setVisibility(View.VISIBLE);
                repeticiones.setVisibility(View.VISIBLE);
                aviso.setVisibility(View.VISIBLE);
                asunto.setVisibility(View.VISIBLE);
                mensaje.setVisibility(View.VISIBLE);
                comprobarCliProy();

        }

    }

    @Override
    protected void mostrarDialogDelete() {

        delete();
    }

    @Override
    protected boolean delete() {

        return mostrarDialogoBorrarRep(id);

    }

    @Override
    protected boolean update() {

        if (id==null){
            if(registrar()){return true;}
        }else {

            valores = new ContentValues();

            if (idProyecto != null) {

                ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_NOMPROYECTOREL, nombreProyecto);
                ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_PROYECTOREL, idProyecto);

            }
            if (idCliente != null) {

                ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_NOMCLIENTEREL, nombreCliente);
                ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_CLIENTEREL, idCliente);
            }

            switch (tevento) {

                case TIPOEVENTOTAREA:

                    ConsultaBD.putDato(valores,campos,EVENTO_ASUNTO,asunto.getText().toString());
                    ConsultaBD.putDato(valores,campos,EVENTO_MENSAJE,mensaje.getText().toString());
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAFINEVENTO, ffinEvento);
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_HORAFINEVENTO, hfinEvento);
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAFINEVENTOF, getDate(ffinEvento));
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_HORAFINEVENTOF, getTime(hfinEvento));


                    break;

                case TIPOEVENTOCITA:
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTO, finiEvento);
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_HORAINIEVENTO, hiniEvento);
                    ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTOF,fechaIni.getText().toString());
                    ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTOF,horaIni.getText().toString());
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_DIRECCION, lugar.getText().toString());
                    ConsultaBD.putDato(valores,campos,EVENTO_ASUNTO,asunto.getText().toString());
                    ConsultaBD.putDato(valores,campos,EVENTO_MENSAJE,mensaje.getText().toString());
                    break;

                case TIPOEVENTOEMAIL:
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTO, finiEvento);
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_HORAINIEVENTO, hiniEvento);
                    ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTOF,fechaIni.getText().toString());
                    ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTOF,horaIni.getText().toString());
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_EMAIL, email.getText().toString());
                    ConsultaBD.putDato(valores,campos,EVENTO_ASUNTO,asunto.getText().toString());
                    ConsultaBD.putDato(valores,campos,EVENTO_MENSAJE,mensaje.getText().toString());
                    break;

                case TIPOEVENTOLLAMADA:
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTO, finiEvento);
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_HORAINIEVENTO, hiniEvento);
                    ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTOF,fechaIni.getText().toString());
                    ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTOF,horaIni.getText().toString());
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_TELEFONO, telefono.getText().toString());
                    ConsultaBD.putDato(valores,campos,EVENTO_ASUNTO,asunto.getText().toString());
                    ConsultaBD.putDato(valores,campos,EVENTO_MENSAJE,mensaje.getText().toString());
                    break;

                case Interactor.TiposEvento.TIPOEVENTOEVENTO:
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTO, finiEvento);
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAFINEVENTO, ffinEvento);
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_HORAINIEVENTO, hiniEvento);
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_HORAFINEVENTO, hfinEvento);
                    ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTOF,fechaIni.getText().toString());
                    ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTOF,horaIni.getText().toString());
                    ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAFINEVENTOF,fechaFin.getText().toString());
                    ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAFINEVENTOF,horaFin.getText().toString());
                    ConsultaBD.putDato(valores,campos,EVENTO_ASUNTO,asunto.getText().toString());
                    ConsultaBD.putDato(valores,campos,EVENTO_MENSAJE,mensaje.getText().toString());

                    break;

            }

            ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_DESCRIPCION, descipcion.getText().toString());
            ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_TIPO, tevento);
            ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_COMPLETADA, JavaUtil.comprobarDouble(completa.getText().toString()));
            ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_RUTAFOTO, path);
            ConsultaBD.putDato(valores,campos,EVENTO_NOTIFICADO,notificado);

            if (aviso.isChecked()) {


                long fechaaviso = (JavaUtil.comprobarLong(avisoMinutos.getText().toString()) * MINUTOSLONG) +
                        (JavaUtil.comprobarLong(avisoHoras.getText().toString()) * HORASLONG) +
                        (JavaUtil.comprobarLong(avisoDias.getText().toString()) * DIASLONG);
                ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_AVISO, fechaaviso);

            } else {

                if (modelo != null) {
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_AVISO, modelo.getLong(EVENTO_AVISO));
                }
            }

            if (idMulti==null && repeticiones.isChecked()){
                ConsultaBD.putDato(valores,campos,EVENTO_IDMULTI,id);
            }

            ConsultaBD.updateRegistro(TABLA_EVENTO, id, valores);
            modelo = CRUDutil.setModelo(campos,id);

            if (repeticiones.isChecked()) {

                idMulti = modelo.getString(EVENTO_IDMULTI);

                String seleccion = ContratoPry.Tablas.EVENTO_IDMULTI + " = '" + idMulti +
                        "' AND " + ContratoPry.Tablas.EVENTO_ID_EVENTO +
                        " <> '" + id + "'";
                if (ConsultaBD.deleteRegistros(TABLA_EVENTO, seleccion)>=0) {

                    long hoy = hoy();
                    if (finiEvento == 0) {
                        finiEvento = hoy;
                    }

                    int dia = JavaUtil.diaMes(finiEvento);

                    long diffecha = ffinEvento - finiEvento;

                    long offRep = 0;
                    long fecharep = 0;

                    if (mismoDiaMes.isChecked()){

                        fecharep = JavaUtil.mismoDiaMes(finiEvento,dia);

                    }else if (mismoDiaAnio.isChecked()){

                        fecharep = JavaUtil.mismoDiaAnio(finiEvento,dia);

                    }else {
                        offRep = (JavaUtil.comprobarLong(repAnios.getText().toString()) * ANIOSLONG) +
                                (JavaUtil.comprobarLong(repMeses.getText().toString()) * MESESLONG) +
                                (JavaUtil.comprobarLong(repDias.getText().toString()) * DIASLONG);

                        fecharep = finiEvento + offRep;
                    }

                    long duracionRep = (JavaUtil.comprobarLong(drepAnios.getText().toString()) * ANIOSLONG) +
                            (JavaUtil.comprobarLong(drepMeses.getText().toString()) * MESESLONG) +
                            (JavaUtil.comprobarLong(drepDias.getText().toString()) * DIASLONG);



                    while (duracionRep + hoy > fecharep) {

                        ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTO, fecharep);
                        ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTOF, getDate(fecharep));

                        if (tevento.equals(Interactor.TiposEvento.TIPOEVENTOEVENTO)) {
                            ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAFINEVENTO, (fecharep + diffecha));
                            ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAFINEVENTOF, getDate(fecharep + diffecha));

                        }
                        ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_COMPLETADA, 0);

                        ConsultaBD.insertRegistro(TABLA_EVENTO, valores);

                        if (mismoDiaMes.isChecked()){

                            fecharep = JavaUtil.mismoDiaMes(fecharep,dia);

                        }else if (mismoDiaAnio.isChecked()){

                            fecharep = JavaUtil.mismoDiaAnio(fecharep,dia);

                        }else {

                            fecharep += offRep;

                        }

                    }
                }

                Toast.makeText(contexto, "Repeticiones actualizadas", Toast.LENGTH_SHORT).show();

            }

            Toast.makeText(contexto, "Registro actualizado", Toast.LENGTH_SHORT).show();

            modelo = CRUDutil.setModelo(campos,id);
            setDatos();
            return true;
        }

        return false;
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

        proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
        cliente = (Modelo) bundle.getSerializable(TABLA_CLIENTE);
        tevento = bundle.getString(TIPO);
        if (modelo!=null) {
            idMulti = modelo.getString(EVENTO_IDMULTI);
        }

    }

    @Override
    protected void setDatos() {

        allGone();
        btnVerNotas.setVisibility(View.VISIBLE);
        btnNota.setVisibility(View.VISIBLE);
        btndelete.setVisibility(View.VISIBLE);
        mismoDiaMes.setVisibility(View.GONE);
        mismoDiaAnio.setVisibility(View.GONE);
        mismoDiaMes.setChecked(false);
        mismoDiaAnio.setChecked(false);
        chNotificado.setVisibility(View.GONE);
        visible(imagen);
        visible(descipcion);
        visible(relCli);
        visible(relProy);
        visiblePie();

        completa.setText(modelo.getString(EVENTO_COMPLETADA));

        if (ConsultaBD.checkQueryList(CAMPOS_NOTA,NOTA_ID_RELACIONADO,id,null,IGUAL,null)){
            btnVerNotas.setVisibility(View.VISIBLE);
        }else{
            btnVerNotas.setVisibility(View.GONE);
        }


        notificado = modelo.getInt(EVENTO_NOTIFICADO);

        if (modelo.getString(EVENTO_PROYECTOREL)!=null){

            idProyecto = modelo.getString(EVENTO_PROYECTOREL);
            nombreProyecto = modelo.getString(EVENTO_NOMPROYECTOREL);
            proyRel.setText(nombreProyecto);
            relProy.setChecked(true);
            idCliente = modelo.getString(EVENTO_CLIENTEREL);
            nombreCliente = modelo.getString(EVENTO_NOMCLIENTEREL);
            cliRel.setText(nombreCliente);
            relCli.setChecked(true);
            proyRel.setVisibility(View.VISIBLE);
            cliRel.setVisibility(View.VISIBLE);
            proyRel.setEnabled(false);
            cliRel.setEnabled(false);
            activityBase.toolbar.setSubtitle(nombreProyecto);


        }else if (modelo.getString(EVENTO_CLIENTEREL)!=null){

            idCliente = modelo.getString(EVENTO_CLIENTEREL);
            nombreCliente = modelo.getString(EVENTO_NOMCLIENTEREL);
            if (nombreProyecto==null) {
                activityBase.toolbar.setSubtitle(nombreCliente);
            }

            cliRel.setText(nombreCliente);
            relCli.setChecked(true);
            cliRel.setVisibility(View.VISIBLE);
            cliRel.setEnabled(false);
        }

        repeticiones.setChecked(false);
        completa.setVisibility(View.VISIBLE);

        idMulti =  modelo.getString(EVENTO_IDMULTI);

        if (idMulti==null){
            repeticiones.setText(getString(R.string.crear_repeticiones));
            btnVerRepeticiones.setVisibility(View.GONE);
        }else{

            repeticiones.setText(getString(R.string.modificar_repeticiones));
            btnVerRepeticiones.setVisibility(View.VISIBLE);

        }

        if (modelo!=null) {
            tevento = modelo.getString(EVENTO_TIPO);
            tipoEvento.setVisibility(View.VISIBLE);
            descipcion.setText(modelo.getString(EVENTO_DESCRIPCION));
            asunto.setText(modelo.getString(EVENTO_ASUNTO));
            mensaje.setText(modelo.getString(EVENTO_MENSAJE));


            if (modelo.getLong(EVENTO_AVISO) > 0) {

                long[] res = JavaUtil.longA_ddhhmm(modelo.getLong(EVENTO_AVISO));

                avisoDias.setText(String.valueOf(res[0]));
                avisoHoras.setText(String.valueOf(res[1]));
                avisoMinutos.setText(String.valueOf(res[2]));
                aviso.setChecked(true);
                avisoDias.setVisibility(View.VISIBLE);
                avisoHoras.setVisibility(View.VISIBLE);
                avisoMinutos.setVisibility(View.VISIBLE);
                laviso.setVisibility(View.VISIBLE);
                chNotificado.setVisibility(View.VISIBLE);
                if (notificado==1){
                    chNotificado.setChecked(true);
                }else{
                    chNotificado.setChecked(false);
                }

            } else {
                avisoDias.setVisibility(View.GONE);
                avisoHoras.setVisibility(View.GONE);
                avisoMinutos.setVisibility(View.GONE);
                laviso.setVisibility(View.GONE);
                aviso.setChecked(false);
                chNotificado.setVisibility(View.GONE);
            }
        }

        if (tevento!=null) {

            tipoEvento.setText(tevento.toUpperCase());

            switch (tevento) {

                case TIPOEVENTOTAREA:

                    fechaFin.setVisibility(View.VISIBLE);
                    horaFin.setVisibility(View.VISIBLE);
                    btnffin.setVisibility(View.VISIBLE);
                    btnhfin.setVisibility(View.VISIBLE);
                    ffinEvento = modelo.getLong(EVENTO_FECHAFINEVENTO);
                    fechaFin.setText(getDate(ffinEvento));
                    hfinEvento = modelo.getLong(EVENTO_HORAFINEVENTO);
                    horaFin.setText(getTime(hfinEvento));

                    break;

                case TIPOEVENTOCITA:
                    lugar.setVisibility(View.VISIBLE);
                    imgmapa.setVisibility(View.VISIBLE);
                    asunto.setVisibility(View.VISIBLE);
                    fechaIni.setVisibility(View.VISIBLE);
                    horaIni.setVisibility(View.VISIBLE);
                    btnfini.setVisibility(View.VISIBLE);
                    btnhini.setVisibility(View.VISIBLE);
                    repeticiones.setVisibility(View.VISIBLE);
                    aviso.setVisibility(View.VISIBLE);
                    lugar.setText(modelo.getString(EVENTO_DIRECCION));
                    finiEvento = modelo.getLong(EVENTO_FECHAINIEVENTO);
                    fechaIni.setText(getDate(finiEvento));
                    hiniEvento = modelo.getLong(EVENTO_HORAINIEVENTO);
                    horaIni.setText(getTime(hiniEvento));
                    break;

                case TIPOEVENTOEMAIL:
                    email.setVisibility(View.VISIBLE);
                    imgemail.setVisibility(View.VISIBLE);
                    asunto.setVisibility(View.VISIBLE);
                    mensaje.setVisibility(View.VISIBLE);
                    fechaIni.setVisibility(View.VISIBLE);
                    horaIni.setVisibility(View.VISIBLE);
                    btnfini.setVisibility(View.VISIBLE);
                    btnhini.setVisibility(View.VISIBLE);
                    repeticiones.setVisibility(View.VISIBLE);
                    aviso.setVisibility(View.VISIBLE);
                    email.setText(modelo.getString(EVENTO_EMAIL));
                    finiEvento = modelo.getLong(EVENTO_FECHAINIEVENTO);
                    fechaIni.setText(getDate(finiEvento));
                    hiniEvento = modelo.getLong(EVENTO_HORAINIEVENTO);
                    horaIni.setText(getTime(hiniEvento));
                    break;

                case TIPOEVENTOLLAMADA:
                    telefono.setVisibility(View.VISIBLE);
                    imgllamada.setVisibility(View.VISIBLE);
                    asunto.setVisibility(View.VISIBLE);
                    mensaje.setVisibility(View.VISIBLE);
                    fechaIni.setVisibility(View.VISIBLE);
                    horaIni.setVisibility(View.VISIBLE);
                    btnfini.setVisibility(View.VISIBLE);
                    btnhini.setVisibility(View.VISIBLE);
                    repeticiones.setVisibility(View.VISIBLE);
                    aviso.setVisibility(View.VISIBLE);
                    telefono.setText(modelo.getString(EVENTO_TELEFONO));
                    finiEvento = modelo.getLong(EVENTO_FECHAINIEVENTO);
                    fechaIni.setText(getDate(finiEvento));
                    hiniEvento = modelo.getLong(EVENTO_HORAINIEVENTO);
                    horaIni.setText(getTime(hiniEvento));
                    break;

                case Interactor.TiposEvento.TIPOEVENTOEVENTO:
                    fechaIni.setVisibility(View.VISIBLE);
                    horaIni.setVisibility(View.VISIBLE);
                    fechaFin.setVisibility(View.VISIBLE);
                    horaFin.setVisibility(View.VISIBLE);
                    btnfini.setVisibility(View.VISIBLE);
                    btnhini.setVisibility(View.VISIBLE);
                    btnffin.setVisibility(View.VISIBLE);
                    btnhfin.setVisibility(View.VISIBLE);
                    repeticiones.setVisibility(View.VISIBLE);
                    aviso.setVisibility(View.VISIBLE);
                    finiEvento = modelo.getLong(EVENTO_FECHAINIEVENTO);
                    fechaIni.setText(getDate(finiEvento));
                    hiniEvento = modelo.getLong(EVENTO_HORAINIEVENTO);
                    horaIni.setText(getTime(hiniEvento));
                    ffinEvento = modelo.getLong(EVENTO_FECHAFINEVENTO);
                    fechaFin.setText(getDate(ffinEvento));
                    hfinEvento = modelo.getLong(EVENTO_HORAFINEVENTO);
                    horaFin.setText(getTime(hfinEvento));
                    break;

            }
        }

    }

    private void comprobarCliProy(){

        if (cliente != null) {

            idCliente = cliente.getString(CLIENTE_ID_CLIENTE);
            relCli.setChecked(true);
            activityBase.toolbar.setSubtitle(subTitulo +" - "+cliente.getString(CLIENTE_NOMBRE));

        }
        if (proyecto!=null) {

            idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);
            relProy.setChecked(true);
            activityBase.toolbar.setSubtitle(subTitulo+" - "+proyecto.getString(PROYECTO_NOMBRE));

        }else {

            activityBase.toolbar.setSubtitle(subTitulo);

        }

        if (idProyecto!=null){

            proyRel.setVisibility(View.VISIBLE);
            nombreProyecto = proyecto.getString(PROYECTO_NOMBRE);
            proyRel.setText(nombreProyecto);
            cliRel.setVisibility(View.VISIBLE);
            lugar.setText(cliente.getString(CLIENTE_DIRECCION));
            telefono.setText(cliente.getString(CLIENTE_TELEFONO));
            email.setText(cliente.getString(CLIENTE_EMAIL));
            nombreCliente = cliente.getString(CLIENTE_NOMBRE);
            cliRel.setText(nombreCliente);

        }else if (idCliente!=null){

            //relCli.setVisibility(View.GONE);
            cliRel.setVisibility(View.VISIBLE);
            lugar.setText(cliente.getString(CLIENTE_DIRECCION));
            telefono.setText(cliente.getString(CLIENTE_TELEFONO));
            email.setText(cliente.getString(CLIENTE_EMAIL));
            nombreCliente = cliente.getString(CLIENTE_NOMBRE);
            cliRel.setText(nombreCliente);
        }

        relProy.setVisibility(View.VISIBLE);
        relCli.setVisibility(View.VISIBLE);
        imagen.setVisibility(View.VISIBLE);
        repeticiones.setText(getString(R.string.crear_repeticiones));
        if (origen.equals(CALENDARIO)){
            finiEvento = bundle.getLong(FECHA);
            fechaIni.setText(getDate(finiEvento));
        }
        if (origen.equals(CALENDARIO)){
            hiniEvento = bundle.getLong(HORACAL);
            horaIni.setText(getTime(hiniEvento));
        }

        visibleBtnBackSave();
        if (lista!=null && lista.sizeLista()>0){
            visible(frLista);
        }
    }

    @Override
    protected void setAcciones() {

        setAdaptadorClientes(cliRel);

        setAdaptadorProyectos(proyRel);


        btnfini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogInicio();
            }
        });

        fechaIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDatePickerDialogInicio();

            }
        }) ;

        btnffin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogFin();
            }
        });
        fechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDatePickerDialogFin();

            }
        }) ;
        btnhini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialogini();
            }
        });
        horaIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showTimePickerDialogini();

            }
        }) ;
        btnhfin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialogfin();
            }
        });
        horaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showTimePickerDialogfin();

            }
        }) ;

        imgllamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppActivity.hacerLlamada(AppActivity.getAppContext()
                        ,modelo.getString(EVENTO_TELEFONO));
            }
        });

        imgemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppActivity.enviarEmail(contexto, modelo.getString(EVENTO_EMAIL),
                        modelo.getString(EVENTO_ASUNTO),modelo.getString(EVENTO_MENSAJE),
                        modelo.getString(EVENTO_RUTAADJUNTO));

            }
        });

        imgmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!lugar.getText().toString().equals("")){

                    viewOnMapA(contexto,modelo.getString(EVENTO_DIRECCION));
                }

            }
        });


        aviso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    avisoDias.setVisibility(View.VISIBLE);
                    avisoHoras.setVisibility(View.VISIBLE);
                    avisoMinutos.setVisibility(View.VISIBLE);
                    laviso.setVisibility(View.VISIBLE);
                    if (finiEvento>hoy()) {
                        notificado = 0;
                    }
                    chNotificado.setVisibility(View.VISIBLE);

                }else{

                    avisoDias.setVisibility(View.GONE);
                    avisoHoras.setVisibility(View.GONE);
                    avisoMinutos.setVisibility(View.GONE);
                    laviso.setVisibility(View.GONE);
                    avisoDias.setText("0");
                    avisoHoras.setText("0");
                    avisoMinutos.setText("0");
                    chNotificado.setVisibility(View.GONE);

                }
            }
        });

        chNotificado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked){
                    notificado = 0;
                }else{
                    notificado = 1;
                }
            }
        });

        repeticiones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    mismoDiaMes.setVisibility(View.VISIBLE);
                    mismoDiaAnio.setVisibility(View.VISIBLE);
                    repAnios.setVisibility(View.VISIBLE);
                    repMeses.setVisibility(View.VISIBLE);
                    repDias.setVisibility(View.VISIBLE);
                    drepAnios.setVisibility(View.VISIBLE);
                    drepMeses.setVisibility(View.VISIBLE);
                    drepDias.setVisibility(View.VISIBLE);
                    lrep.setVisibility(View.VISIBLE);
                    ldrep.setVisibility(View.VISIBLE);

                }else{

                    mismoDiaMes.setVisibility(View.GONE);
                    mismoDiaAnio.setVisibility(View.GONE);
                    repAnios.setVisibility(View.GONE);
                    repMeses.setVisibility(View.GONE);
                    repDias.setVisibility(View.GONE);
                    repAnios.setText("0");
                    repMeses.setText("0");
                    repDias.setText("0");
                    drepAnios.setVisibility(View.GONE);
                    drepMeses.setVisibility(View.GONE);
                    drepDias.setVisibility(View.GONE);
                    drepAnios.setText("0");
                    drepMeses.setText("0");
                    drepDias.setText("0");
                    lrep.setVisibility(View.GONE);
                    ldrep.setVisibility(View.GONE);

                }
            }
        });

        mismoDiaMes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    mismoDiaAnio.setVisibility(View.GONE);
                    repAnios.setVisibility(View.GONE);
                    repMeses.setVisibility(View.GONE);
                    repDias.setVisibility(View.GONE);
                    repAnios.setText("0");
                    repMeses.setText("0");
                    repDias.setText("0");
                }else{

                    mismoDiaAnio.setVisibility(View.VISIBLE);
                    repAnios.setVisibility(View.VISIBLE);
                    repMeses.setVisibility(View.VISIBLE);
                    repDias.setVisibility(View.VISIBLE);
                    repAnios.setText("0");
                    repMeses.setText("0");
                    repDias.setText("0");

                }
            }
        });

        mismoDiaAnio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    mismoDiaMes.setVisibility(View.GONE);
                    repAnios.setVisibility(View.GONE);
                    repMeses.setVisibility(View.GONE);
                    repDias.setVisibility(View.GONE);
                    repAnios.setText("0");
                    repMeses.setText("0");
                    repDias.setText("0");
                }else{

                    mismoDiaMes.setVisibility(View.VISIBLE);
                    repAnios.setVisibility(View.VISIBLE);
                    repMeses.setVisibility(View.VISIBLE);
                    repDias.setVisibility(View.VISIBLE);
                    repAnios.setText("0");
                    repMeses.setText("0");
                    repDias.setText("0");

                }
            }
        });

        relProy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    proyRel.setVisibility(View.VISIBLE);

                }else{

                    proyRel.setVisibility(View.GONE);
                    idProyecto = null;
                    nombreProyecto = null;
                    proyRel.setText(null);
                    relProy.setEnabled(true);

                }
            }
        });

        relCli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    cliRel.setVisibility(View.VISIBLE);

                }else{

                    cliRel.setVisibility(View.GONE);
                    idCliente = null;
                    nombreCliente = null;
                    cliRel.setText(null);
                    relCli.setEnabled(true);

                }
            }
        });

        proyRel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Modelo proyecto = (Modelo) proyRel.getAdapter().getItem(position);
                idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);
                nombreProyecto = proyecto.getString(PROYECTO_NOMBRE);
                proyRel.setText(nombreProyecto);
                cliRel.setText(proyecto.getString(PROYECTO_CLIENTE_NOMBRE));

            }

        });

        cliRel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Modelo cliente = (Modelo) cliRel.getAdapter().getItem(position);
                idCliente = cliente.getString(CLIENTE_ID_CLIENTE);
                nombreCliente = cliente.getString(CLIENTE_NOMBRE);
                cliRel.setText(nombreCliente);
                telefono.setText(cliente.getString(CLIENTE_TELEFONO));
                lugar.setText(cliente.getString(CLIENTE_DIRECCION));
                email.setText(cliente.getString(CLIENTE_EMAIL));

            }

        });

        btnNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarBundle();
                bundle.putString(IDREL,modelo.getString(EVENTO_ID_EVENTO));
                bundle.putString(SUBTITULO, modelo.getString(EVENTO_DESCRIPCION));
                bundle.putString(ORIGEN, EVENTO);
                bundle.putString(ACTUAL,NOTA);
                bundle.putSerializable(MODELO,null);
                bundle.putSerializable(LISTA,null);
                bundle.putString(CAMPO_ID,null);
                bundle.putBoolean(NUEVOREGISTRO,true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevaNota());
            }
        });

        btnVerNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarBundle();
                bundle.putString(IDREL,modelo.getString(EVENTO_ID_EVENTO));
                bundle.putString(SUBTITULO, modelo.getString(EVENTO_DESCRIPCION));
                bundle.putString(ORIGEN, EVENTO);
                bundle.putString(ACTUAL,NOTA);
                bundle.putSerializable(LISTA,null);
                bundle.putSerializable(MODELO,null);
                bundle.putString(CAMPO_ID,null);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
            }
        });

        btnVerRepeticiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verRepeticiones();
            }
        });

        completada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    completa.setText("100.0");
                }else{
                    completa.setText("0.0");
                }
            }
        });


    }

    private void verRepeticiones(){

        idMulti = modelo.getString(EVENTO_IDMULTI);
        listab = new ListaModelo(campos,EVENTO_IDMULTI,idMulti,null,IGUAL,null);
        id = null;
        modelo = null;
        selector();
    }

    @Override
    protected void setTitulo() {

        tituloPlural = R.string.eventos;
        tituloSingular = R.string.evento;
        tituloNuevo = R.string.nuevo_evento;
    }


    @Override
    protected void setInicio() {

        imagen = (ImagenLayout) ctrl(R.id.imgudevento);
        if (proyecto!=null && proyecto.getString(PROYECTO_RUTAFOTO)!=null){
            path = proyecto.getString(PROYECTO_RUTAFOTO);
            imagen.setImageUri(path);
        }
        tipoEvento = (TextView) ctrl(R.id.tvtipoudevento);
        proyRel = (AutoCompleteTextView) ctrl(R.id.sppryudevento);
        cliRel = (AutoCompleteTextView) ctrl(R.id.spcliudevento);
        descipcion = (EditMaterial) ctrl(R.id.etdescudevento, EVENTO_DESCRIPCION);
        lugar = (EditMaterial) ctrl(R.id.etlugarudevento, EVENTO_DIRECCION);
        email = (EditMaterial) ctrl(R.id.etemailudevento, EVENTO_EMAIL);
        telefono = (EditMaterial) ctrl(R.id.ettelefonoudevento, EVENTO_TELEFONO);
        fechaIni = (EditMaterial) ctrl(R.id.etfechainiudevento);
        horaIni = (EditMaterial) ctrl(R.id.ethorainiudevento);
        fechaFin = (EditMaterial) ctrl(R.id.etfechafinudevento);
        horaFin = (EditMaterial) ctrl(R.id.ethorafinudevento);
        repAnios = (EditMaterial) ctrl(R.id.etrepaniosudevento);
        repMeses = (EditMaterial) ctrl(R.id.etrepmesesudevento);
        repDias = (EditMaterial) ctrl(R.id.etrepdiasudevento);
        drepAnios = (EditMaterial) ctrl(R.id.etrepdaniosudevento);
        drepMeses = (EditMaterial) ctrl(R.id.etrepdmesesudevento);
        drepDias = (EditMaterial) ctrl(R.id.etrepddiasudevento);
        avisoDias = (EditMaterial) ctrl(R.id.etdiasudevento);
        avisoHoras = (EditMaterial) ctrl(R.id.ethorasudevento);
        avisoMinutos = (EditMaterial) ctrl(R.id.etminutosudevento);
        aviso = (CheckBox) ctrl(R.id.chavisoudevento);
        repeticiones = (CheckBox) ctrl(R.id.chrptudevento);
        mismoDiaMes = (CheckBox) ctrl(R.id.chmmesevento);
        mismoDiaAnio = (CheckBox) ctrl(R.id.chmanioevento);
        relProy = (CheckBox) ctrl(R.id.chpryudevento);
        relCli = (CheckBox) ctrl(R.id.chcliudevento);
        laviso = (TextView) ctrl(R.id.ltvavisoudevento);
        lrep = (TextView) ctrl(R.id.ltvreptudevento);
        ldrep = (TextView) ctrl(R.id.ltvdreptudevento);
        btnfini = (ImageButton) ctrl(R.id.imgbtnfiniudevento);
        btnffin = (ImageButton) ctrl(R.id.imgbtnffinudevento);
        btnhini = (ImageButton) ctrl(R.id.imgbtnhiniudevento);
        btnhfin = (ImageButton) ctrl(R.id.imgbtnhfinudevento);
        completa = (EditMaterial) ctrl(R.id.etcompletadaudevento, EVENTO_COMPLETADA);
        btnNota = (ImageButton) ctrl(R.id.btn_crearnota_evento);
        btnVerNotas = (ImageButton) ctrl(R.id.btn_vernotas_evento);
        btnVerRepeticiones = (Button) ctrl(R.id.btnverrepevento);
        imgmapa = (ImageButton) ctrl(R.id.imgmapaevento);
        imgllamada = (ImageButton) ctrl(R.id.imgllamadaevento);
        imgemail = (ImageButton) ctrl(R.id.imgemailevento);
        asunto = (EditMaterial) ctrl(R.id.etasuntoevento, EVENTO_ASUNTO);
        mensaje = (EditMaterial) ctrl(R.id.etmensajeevento, EVENTO_MENSAJE);
        chNotificado = (CheckBox) ctrl(R.id.chnotificadoevento);
        completada = (CheckBox) ctrl(R.id.chcompletadaevento);

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_evento;
        layoutItem = R.layout.item_list_evento;

    }

    @Override
    protected boolean registrar() {

        valores = new ContentValues();

        if (idProyecto!=null){

            ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_NOMPROYECTOREL,nombreProyecto);
            ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_PROYECTOREL,idProyecto);

        }
        if (idCliente!=null){

            ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_NOMCLIENTEREL,nombreCliente);
            ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_CLIENTEREL,idCliente);
        }

        if (path!=null){

            ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_RUTAFOTO,path);
        }

        switch (tevento){

            case TAREA:

                System.out.println("ffinEvento = " + ffinEvento);
                System.out.println("hfinEvento = " + hfinEvento);

                if (ffinEvento==0){ffinEvento = JavaUtil.hoyFecha();}
                if (hfinEvento==0){hfinEvento = JavaUtil.hoyHora();}
                ConsultaBD.putDato(valores,campos,EVENTO_ASUNTO,asunto.getText().toString());
                ConsultaBD.putDato(valores,campos,EVENTO_MENSAJE,mensaje.getText().toString());
                ConsultaBD.putDato(valores, CAMPOS_EVENTO,EVENTO_FECHAFINEVENTO, ffinEvento);
                ConsultaBD.putDato(valores, CAMPOS_EVENTO,EVENTO_HORAFINEVENTO, hfinEvento);
                ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAFINEVENTOF, getDate(ffinEvento));
                ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_HORAFINEVENTOF, getTime(hfinEvento));


                break;

            case TIPOEVENTOCITA:
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTOF,fechaIni.getText().toString());
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTOF,horaIni.getText().toString());
                ConsultaBD.putDato(valores,campos,EVENTO_ASUNTO,asunto.getText().toString());
                ConsultaBD.putDato(valores,campos,EVENTO_MENSAJE,mensaje.getText().toString());

                ConsultaBD.putDato(valores,CAMPOS_EVENTO, EVENTO_DIRECCION,lugar.getText().toString());

                if (lugar==null || lugar.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Debe introducir una direccion para la cita",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;

            case TIPOEVENTOEMAIL:
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTOF,fechaIni.getText().toString());
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTOF,horaIni.getText().toString());
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_EMAIL,email.getText().toString());
                if (email==null || email.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Debe introducir una direccion de email",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                ConsultaBD.putDato(valores,campos,EVENTO_ASUNTO,asunto.getText().toString());
                ConsultaBD.putDato(valores,campos,EVENTO_MENSAJE,mensaje.getText().toString());
                break;

            case TIPOEVENTOLLAMADA:
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTOF,fechaIni.getText().toString());
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTOF,horaIni.getText().toString());
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_TELEFONO,telefono.getText().toString());

                if (telefono==null || telefono.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Debe introducir un numero de telefono",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                ConsultaBD.putDato(valores,campos,EVENTO_ASUNTO,asunto.getText().toString());
                ConsultaBD.putDato(valores,campos,EVENTO_MENSAJE,mensaje.getText().toString());
                break;

            case Interactor.TiposEvento.TIPOEVENTOEVENTO:
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAFINEVENTO,ffinEvento);
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAFINEVENTO,hfinEvento);
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTOF,fechaIni.getText().toString());
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTOF,horaIni.getText().toString());
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAFINEVENTOF,fechaFin.getText().toString());
                ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAFINEVENTOF,horaFin.getText().toString());
                ConsultaBD.putDato(valores,campos,EVENTO_ASUNTO,asunto.getText().toString());
                ConsultaBD.putDato(valores,campos,EVENTO_MENSAJE,mensaje.getText().toString());

                break;

        }

        ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_DESCRIPCION,descipcion.getText().toString());
        ConsultaBD.putDato(valores,CAMPOS_EVENTO, EVENTO_TIPO,tevento);

        if (aviso.isChecked() && !tevento.equals(TIPOEVENTOTAREA)){

            long fechaaviso = (JavaUtil.comprobarLong(avisoMinutos.getText().toString()) * MINUTOSLONG) +
                    (JavaUtil.comprobarLong(avisoHoras.getText().toString()) * HORASLONG) +
                    (JavaUtil.comprobarLong(avisoDias.getText().toString()) * DIASLONG);
            ConsultaBD.putDato(valores,CAMPOS_EVENTO,EVENTO_AVISO,fechaaviso);


        }

            id = ConsultaBD.idInsertRegistro(tabla, valores);

            modelo = new Modelo(campos,id);//consulta.queryObject(campos,id);

            if (repeticiones.isChecked() && !tevento.equals(TIPOEVENTOTAREA)) {

                idMulti = id;

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

                if (mismoDiaMes.isChecked()){

                    fecharep = JavaUtil.mismoDiaMes(finiEvento,dia);

                }else if (mismoDiaAnio.isChecked()){

                    fecharep = JavaUtil.mismoDiaAnio(finiEvento,dia);

                }else {

                    offRep = (JavaUtil.comprobarLong(repAnios.getText().toString()) * ANIOSLONG) +
                            (JavaUtil.comprobarLong(repMeses.getText().toString()) * MESESLONG) +
                            (JavaUtil.comprobarLong(repDias.getText().toString()) * DIASLONG);

                    fecharep = finiEvento + offRep;

                }

                long duracionRep = (JavaUtil.comprobarLong(drepAnios.getText().toString()) * ANIOSLONG) +
                        (JavaUtil.comprobarLong(drepMeses.getText().toString()) * MESESLONG) +
                        (JavaUtil.comprobarLong(drepDias.getText().toString()) * DIASLONG);


                while (duracionRep + hoy > fecharep) {

                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTO, fecharep);
                    ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTOF, getDate(fecharep));


                    if (tevento.equals(Interactor.TiposEvento.TIPOEVENTOEVENTO)) {
                        ConsultaBD.putDato(valores,campos,EVENTO_FECHAFINEVENTO, String.valueOf(fecharep + diffecha));
                        ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAFINEVENTOF, getDate(fecharep + diffecha));

                    }
                    ConsultaBD.insertRegistro(TABLA_EVENTO, valores);

                    if (mismoDiaMes.isChecked()){

                        fecharep = JavaUtil.mismoDiaMes(fecharep,dia);

                    }else if (mismoDiaAnio.isChecked()){

                        fecharep = JavaUtil.mismoDiaAnio(fecharep,dia);

                    }else {

                        fecharep += offRep;
                    }
                }

            }


            if (modelo!=null) {
                Toast.makeText(getContext(), "Registro creado",
                        Toast.LENGTH_SHORT).show();
                modelo = CRUDutil.setModelo(campos,id);
                nuevo = false;
                return true;
            }

        Toast.makeText(getContext(), "Error al crear registro",
                Toast.LENGTH_SHORT).show();
            return true;

    }

    @Override
    protected void setContenedor() {

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

                activityBase.toolbar.setSubtitle(setNamefdef());
                idCliente = null;
                idProyecto = null;
                idMulti = null;

                break;
        }

    }

    private boolean mostrarDialogoBorrarRep(final String idEvento) {

        modelo = ConsultaBD.queryObject(campos,idEvento);
        final CharSequence[] opciones = {"Borrar sólo este modelo", "Borrar este y repeticiones", "Borrar sólo repeticiones", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Borrar sólo este modelo")) {

                    if (ConsultaBD.deleteRegistro(TABLA_EVENTO, idEvento)>0) {

                        Toast.makeText(contexto, "Registro borrado ", Toast.LENGTH_SHORT).show();
                        if (listab!=null){
                            listab = new ListaModelo(campos,EVENTO_IDMULTI,idMulti,null,IGUAL,null);
                        }
                        id = null;
                        modelo = null;
                        selector();

                    }else{

                        Toast.makeText(contexto, "Error al borrar registro "+idEvento, Toast.LENGTH_SHORT).show();
                    }

                } else if (opciones[which].equals("Borrar este y repeticiones")) {

                    idMulti = modelo.getString(EVENTO_IDMULTI);
                    String seleccion = EVENTO_IDMULTI + " = '" + idMulti + "'";
                    if (ConsultaBD.deleteRegistros(TABLA_EVENTO,EVENTO_IDMULTI,idMulti,null,IGUAL)>0) {

                        id = null;
                        modelo = null;
                        listab = null;
                        Toast.makeText(contexto, "Regitros borrados", Toast.LENGTH_SHORT).show();
                        selector();

                    }else{
                        Toast.makeText(contexto, "Error al borrar los registros "+idEvento, Toast.LENGTH_SHORT).show();
                    }


                } else if (opciones[which].equals("Borrar sólo repeticiones")) {

                    idMulti = modelo.getString(EVENTO_IDMULTI);
                    String seleccion = EVENTO_IDMULTI + " = '" + idMulti +
                            "' AND " + EVENTO_ID_EVENTO + " <> '" + idEvento + "'";
                    if (ConsultaBD.deleteRegistros(TABLA_EVENTO,seleccion)>0) {

                        valores = new ContentValues();
                        valores.putNull(EVENTO_IDMULTI);
                        ConsultaBD.updateRegistro(tabla,idEvento,valores);
                        modelo = CRUDutil.setModelo(campos,idEvento);
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
        public void bind(final Modelo modelo) {

            tipo.setText(modelo.getString(EVENTO_TIPO).toUpperCase());
            descripcion.setText(modelo.getString(EVENTO_DESCRIPCION));
            telefono.setText(modelo.getString(EVENTO_TELEFONO));
            lugar.setText(modelo.getString(EVENTO_DIRECCION));
            email.setText(modelo.getString(EVENTO_EMAIL));
            nomPryRel.setText(modelo.getString(EVENTO_NOMPROYECTOREL));
            nomCliRel.setText(modelo.getString(EVENTO_NOMCLIENTEREL));
            fechaini.setText(getDate(modelo.getLong(EVENTO_FECHAINIEVENTO)));
            fechafin.setText(getDate(modelo.getLong(EVENTO_FECHAFINEVENTO)));
            horaini.setText(getTime(modelo.getLong(EVENTO_HORAINIEVENTO)));
            horafin.setText(getTime(modelo.getLong(EVENTO_HORAFINEVENTO)));
            double completada = modelo.getDouble(EVENTO_COMPLETADA);
            System.out.println("completada = " + completada);
            if (completada==0){
                pbar.setVisibility(View.GONE);
                completa.setChecked(false);
                porccompleta.setVisibility(View.GONE);
            }else if (completada>99) {
                completa.setChecked(true);
                pbar.setVisibility(View.GONE);
                porccompleta.setVisibility(View.GONE);

            }else{
                pbar.setVisibility(View.VISIBLE);
                porccompleta.setVisibility(View.VISIBLE);
                completa.setChecked(false);
                pbar.setProgress((int)completada);
                porccompleta.setText(String.format("%s %s",modelo.getDouble(EVENTO_COMPLETADA),"%"));
            }

            String tipoEvento = modelo.getString(EVENTO_TIPO);

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
            if (modelo.getString(EVENTO_RUTAFOTO)!=null) {
                mediaUtil.setImageUriCircle(modelo.getString(EVENTO_RUTAFOTO),foto);
            }

            if (completada < 100) {

                long retraso = hoy() - modelo.getLong(EVENTO_FECHAINIEVENTO);

                if (!tipoEvento.equals(TIPOEVENTOTAREA)) {
                    if (retraso > 3 * Interactor.DIASLONG) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
                    } else if (retraso > Interactor.DIASLONG) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));
                    } else {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
                    }//imgret.setImageResource(R.drawable.alert_box_v);}
                }else {
                    retraso = hoy() - modelo.getLong(EVENTO_FECHAFINEVENTO);
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
            btneditar.setText("EDITAR "+ tipoEvento.toUpperCase());
            btnllamada.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppActivity.hacerLlamada(AppActivity.getAppContext()
                            ,modelo.getString(EVENTO_TELEFONO));
                }
            });

            btnemail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppActivity.enviarEmail(contexto,modelo.getString(EVENTO_EMAIL),
                            modelo.getString(EVENTO_ASUNTO), modelo.getString(EVENTO_MENSAJE),
                            modelo.getString(EVENTO_RUTAADJUNTO));

                }
            });

            btnmapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!modelo.getString(EVENTO_DIRECCION).equals("")){

                        viewOnMapA(contexto,modelo.getString(EVENTO_DIRECCION));
                    }

                }
            });


            completa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {

                        ContentValues valores = new ContentValues();

                        ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_COMPLETADA, "100");
                        ConsultaBD.updateRegistro(TABLA_EVENTO, modelo.getString
                                (EVENTO_ID_EVENTO), valores);
                        porccompleta.setVisibility(View.GONE);
                        pbar.setVisibility(View.GONE);

                    }
                }
            });

            btneditar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    id = modelo.getString(EVENTO_ID_EVENTO);
                    maestroDetalle();
                    setDatos();


                }
            });
            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {

            return new ViewHolderRV(view);
        }
    }


    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<Modelo> entrada) {

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


    private void setAdaptadorProyectos(final AutoCompleteTextView autoCompleteTextView){


        listaProyectos =  ConsultaBD.queryList(CAMPOS_PROYECTO,null,null);

        autoCompleteTextView.setAdapter(new ListaAdaptadorFiltroModelo(getContext(),
                R.layout.item_list_proyecto,listaProyectos,CAMPOS_PROYECTO) {

            @Override
            protected void setEntradas(int posicion, View view, ArrayList<Modelo> entrada) {

                ImageView imagen = view.findViewById(R.id.imglistaproyectos);
                TextView nombre = view.findViewById(R.id.tvnombrelistaproyectos);
                TextView descripcion = view.findViewById(R.id.tvdesclistaproyectos);
                ImageView imgcli = view.findViewById(R.id.imgclientelistaproyectos);
                TextView nomcli = view.findViewById(R.id.tvnombreclientelistaproyectos);
                ImageView imgest = view.findViewById(R.id.imgestadolistaproyectos);
                TextView estado = view.findViewById(R.id.tvestadolistaproyectos);
                ProgressBar bar = view.findViewById(R.id.progressBarlistaproyectos);

                descripcion.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.PROYECTO_DESCRIPCION));

                nombre.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.PROYECTO_NOMBRE));
                nomcli.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.CLIENTE_NOMBRE));
                estado.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.ESTADO_DESCRIPCION));

                bar.setProgress(Integer.parseInt(entrada.get(posicion).getCampos
                        (ContratoPry.Tablas.PROYECTO_TOTCOMPLETADO)));

                long retraso = Long.parseLong(entrada.get(posicion).getCampos
                        (ContratoPry.Tablas.PROYECTO_RETRASO));
                if (retraso > 3 * Interactor.DIASLONG) {
                    imgest.setImageResource(R.drawable.alert_box_r);
                } else if (retraso > Interactor.DIASLONG) {
                    imgest.setImageResource(R.drawable.alert_box_a);
                } else {
                    imgest.setImageResource(R.drawable.alert_box_v);
                }

                if (entrada.get(posicion).getCampos(ContratoPry.Tablas.PROYECTO_RUTAFOTO) != null) {
                    imagen.setImageURI(Uri.parse(entrada.get(posicion).getCampos
                            (ContratoPry.Tablas.PROYECTO_RUTAFOTO)));
                }
                int peso = Integer.parseInt(entrada.get(posicion).getCampos
                        (ContratoPry.Tablas.CLIENTE_PESOTIPOCLI));

                if (peso > 6) {
                    imgcli.setImageResource(R.drawable.clientev);
                } else if (peso > 3) {
                    imgcli.setImageResource(R.drawable.clientea);
                } else if (peso > 0) {
                    imgcli.setImageResource(R.drawable.clienter);
                } else {
                    imgcli.setImageResource(R.drawable.cliente);
                }


                super.setEntradas(posicion, view, entrada);
            }


        });

    }

    private void setAdaptadorClientes(final AutoCompleteTextView autoCompleteTextView) {


        listaClientes = ConsultaBD.queryList(CAMPOS_CLIENTE, null, null);

        autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(getContext(),
                R.layout.item_list_cliente, listaClientes) {

            @Override
            public void onEntrada(Object entrada, View view) {

                ImagenLayout imgcli = view.findViewById(R.id.imglcliente);
                ImagenLayout imgcliPeso = view.findViewById(R.id.imglclientepeso);
                TextView nombreCli = view.findViewById(R.id.tvnomclilcliente);
                TextView contactoCli = view.findViewById(R.id.tvcontacclilcliente);
                TextView telefonoCli = view.findViewById(R.id.tvtelclilcliente);
                TextView emailCli = view.findViewById(R.id.tvemailclilcliente);
                TextView dirCli = view.findViewById(R.id.tvdirclilcliente);

                dirCli.setText(((Modelo) entrada).getString(CLIENTE_DIRECCION));

                int peso = ((Modelo) entrada).getInt
                        (CLIENTE_PESOTIPOCLI);

                if (peso > 6) {
                    imgcliPeso.setImageResource(R.drawable.clientev);
                } else if (peso > 3) {
                    imgcliPeso.setImageResource(R.drawable.clientea);
                } else if (peso > 0) {
                    imgcliPeso.setImageResource(R.drawable.clienter);
                } else {
                    imgcliPeso.setImageResource(R.drawable.cliente);
                }

                nombreCli.setText(((Modelo) entrada).getString(CLIENTE_NOMBRE));
                contactoCli.setText(((Modelo) entrada).getString(CLIENTE_CONTACTO));
                telefonoCli.setText(((Modelo) entrada).getString(CLIENTE_TELEFONO));
                emailCli.setText(((Modelo) entrada).getString(CLIENTE_EMAIL));
                imgcli.setImageUriPerfil(activityBase, ((Modelo) entrada).getString(CLIENTE_RUTAFOTO));


            }

            @Override
            public List onFilter(ArrayList entradas, CharSequence constraint) {

                List suggestion = new ArrayList();

                for (Object item : entradas) {

                    for (int i = 2; i < CAMPOS_CLIENTE.length; i += 3) {


                        if (((Modelo) item).getString(CAMPOS_CLIENTE[i]) != null && !((Modelo) item).getString(CAMPOS_CLIENTE[i]).equals("")) {
                            if (((Modelo) item).getString(CAMPOS_CLIENTE[i]).toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                                break;
                            }
                        }
                    }

                }

                return null;
            }

        });

    }

    private void showDatePickerDialogInicio() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance
                (hoy(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        finiEvento = JavaUtil.fechaALong(year, month, day);
                        String selectedDate = getDate(finiEvento);
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

                        hiniEvento = JavaUtil.sumaHoraMin(JavaUtil.horaALong(hourOfDay,minute));
                        System.out.println("hiniEvento = " + hiniEvento);
                        String selectedHour = getTime(hiniEvento);
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

                        hfinEvento = JavaUtil.sumaHoraMin(JavaUtil.horaALong(hourOfDay,minute));
                        String selectedHour = getTime(hfinEvento);
                        horaFin.setText(selectedHour);

                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(),"timePicker");

    }


}
