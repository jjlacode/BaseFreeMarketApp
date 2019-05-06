package jjlacode.com.freelanceproject.ui;
// Created by jjlacode on 29/04/19. 

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.AppActivity;
import jjlacode.com.freelanceproject.util.DatePickerFragment;
import jjlacode.com.freelanceproject.util.FragmentCRUD;
import jjlacode.com.freelanceproject.util.ImagenUtil;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.ListaAdaptadorFiltro;
import jjlacode.com.freelanceproject.util.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.CommonPry;
import jjlacode.com.freelanceproject.util.TimePickerFragment;

import static jjlacode.com.freelanceproject.util.AppActivity.viewOnMapA;
import static jjlacode.com.freelanceproject.util.CommonPry.namesubdef;
import static jjlacode.com.freelanceproject.util.CommonPry.setNamefdef;
import static jjlacode.com.freelanceproject.util.JavaUtil.getDate;
import static jjlacode.com.freelanceproject.util.JavaUtil.getTime;


public class FragmentCRUDEvento extends FragmentCRUD implements CommonPry.Constantes,
        ContratoPry.Tablas, CommonPry.TiposEvento {

    Spinner tiposEvento;
    private AutoCompleteTextView proyRel;
    private AutoCompleteTextView cliRel;
    private EditText descipcion;
    private EditText lugar;
    private EditText telefono;
    private EditText email;
    private TextView fechaIni;
    private TextView fechaFin;
    private TextView horaIni;
    private TextView horaFin;
    private EditText repAnios;
    private EditText repMeses;
    private EditText repDias;
    private EditText drepAnios;
    private EditText drepMeses;
    private EditText drepDias;
    private EditText avisoMinutos;
    private EditText avisoHoras;
    private EditText avisoDias;
    private CheckBox aviso;
    private CheckBox repeticiones;
    private TextView laviso;
    private TextView lrep;
    private TextView ldrep;
    private TextView tipoEvento;

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
    private EditText completa;

    private ImageButton btnhfin;
    private ImageButton btnhini;
    private ImageButton btnffin;
    private ImageButton btnfini;
    private CheckBox relCli;
    private CheckBox relProy;
    private Modelo proyecto;
    private Modelo cliente;
    private ArrayList<String> listaTiposEvento;
    private TextView lcompleta;


    public FragmentCRUDEvento() {
        // Required empty public constructor
    }

    @Override
    protected void setLista() {


        AdaptadorRV adapter = new AdaptadorRV(lista);

        rv.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modelo = lista.get(rv.getChildAdapterPosition(v));
                id = modelo.getString(EVENTO_ID_EVENTO);
                maestroDetalle();
                setDatos();
            }
        });

        setAdaptadorAuto(auto);

        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {

                auto.setText("");
                modelo = (Modelo) auto.getAdapter().getItem(position);
                id = modelo.getString(EVENTO_ID_EVENTO);
                maestroDetalle();
                setDatos();

            }
        });

    }

    @Override
    protected void setNuevo() {

        if (cliente != null) {

            idCliente = cliente.getString(CLIENTE_ID_CLIENTE);
            relCli.setChecked(true);

        }
        if (proyecto!=null) {

            idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);
            relProy.setChecked(true);

        }

        lugar.setVisibility(View.GONE);
        telefono.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        repAnios.setVisibility(View.GONE);
        repMeses.setVisibility(View.GONE);
        repDias.setVisibility(View.GONE);
        drepAnios.setVisibility(View.GONE);
        drepMeses.setVisibility(View.GONE);
        drepDias.setVisibility(View.GONE);
        avisoDias.setVisibility(View.GONE);
        avisoHoras.setVisibility(View.GONE);
        avisoMinutos.setVisibility(View.GONE);
        fechaIni.setVisibility(View.GONE);
        horaIni.setVisibility(View.GONE);
        fechaFin.setVisibility(View.GONE);
        horaFin.setVisibility(View.GONE);
        btnfini.setVisibility(View.GONE);
        btnffin.setVisibility(View.GONE);
        btnhini.setVisibility(View.GONE);
        btnhfin.setVisibility(View.GONE);
        laviso.setVisibility(View.GONE);
        lrep.setVisibility(View.GONE);
        ldrep.setVisibility(View.GONE);
        repeticiones.setVisibility(View.GONE);
        aviso.setVisibility(View.GONE);
        proyRel.setVisibility(View.GONE);
        cliRel.setVisibility(View.GONE);
        imagen.setVisibility(View.GONE);
        descipcion.setVisibility(View.GONE);
        relProy.setVisibility(View.GONE);
        relCli.setVisibility(View.GONE);
        tiposEvento.setVisibility(View.VISIBLE);
        completa.setVisibility(View.GONE);
        lcompleta.setVisibility(View.GONE);

        listaTiposEvento = new ArrayList<>();
        listaTiposEvento.add("Elija el tipo de evento");
        listaTiposEvento.add(TAREA);
        listaTiposEvento.add(CITA);
        listaTiposEvento.add(EMAIL);
        listaTiposEvento.add(LLAMADA);
        listaTiposEvento.add(EVENTO);

        ArrayAdapter<String> adapterTiposEvento = new ArrayAdapter<>
                (getContext(),R.layout.spinner_item_tipo_evento,listaTiposEvento);

        tiposEvento.setAdapter(adapterTiposEvento);

        tiposEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position>0) {

                    lugar.setVisibility(View.GONE);
                    telefono.setVisibility(View.GONE);
                    email.setVisibility(View.GONE);
                    repAnios.setVisibility(View.GONE);
                    repMeses.setVisibility(View.GONE);
                    repDias.setVisibility(View.GONE);
                    drepAnios.setVisibility(View.GONE);
                    drepMeses.setVisibility(View.GONE);
                    drepDias.setVisibility(View.GONE);
                    avisoDias.setVisibility(View.GONE);
                    avisoHoras.setVisibility(View.GONE);
                    avisoMinutos.setVisibility(View.GONE);
                    fechaIni.setVisibility(View.GONE);
                    horaIni.setVisibility(View.GONE);
                    fechaFin.setVisibility(View.GONE);
                    horaFin.setVisibility(View.GONE);
                    btnfini.setVisibility(View.GONE);
                    btnffin.setVisibility(View.GONE);
                    btnhini.setVisibility(View.GONE);
                    btnhfin.setVisibility(View.GONE);
                    laviso.setVisibility(View.GONE);
                    lrep.setVisibility(View.GONE);
                    ldrep.setVisibility(View.GONE);
                    repeticiones.setVisibility(View.GONE);
                    aviso.setVisibility(View.GONE);
                    imagen.setVisibility(View.GONE);
                    descipcion.setVisibility(View.GONE);
                    relProy.setVisibility(View.VISIBLE);
                    relCli.setVisibility(View.VISIBLE);
                    cliRel.setVisibility(View.GONE);
                    proyRel.setVisibility(View.GONE);

                    tevento = listaTiposEvento.get(position);
                    imagen.setVisibility(View.VISIBLE);
                    descipcion.setVisibility(View.VISIBLE);
                    repeticiones.setText("Crear repeticiones");

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


                    switch (tevento){

                        case TAREA:

                            break;

                        case CITA:
                            lugar.setVisibility(View.VISIBLE);
                            fechaIni.setVisibility(View.VISIBLE);
                            horaIni.setVisibility(View.VISIBLE);
                            btnfini.setVisibility(View.VISIBLE);
                            btnhini.setVisibility(View.VISIBLE);
                            repeticiones.setVisibility(View.VISIBLE);
                            aviso.setVisibility(View.VISIBLE);
                            break;

                        case EMAIL:
                            email.setVisibility(View.VISIBLE);
                            fechaIni.setVisibility(View.VISIBLE);
                            horaIni.setVisibility(View.VISIBLE);
                            btnfini.setVisibility(View.VISIBLE);
                            btnhini.setVisibility(View.VISIBLE);
                            repeticiones.setVisibility(View.VISIBLE);
                            aviso.setVisibility(View.VISIBLE);
                            break;

                        case LLAMADA:
                            telefono.setVisibility(View.VISIBLE);
                            fechaIni.setVisibility(View.VISIBLE);
                            horaIni.setVisibility(View.VISIBLE);
                            btnfini.setVisibility(View.VISIBLE);
                            btnhini.setVisibility(View.VISIBLE);
                            repeticiones.setVisibility(View.VISIBLE);
                            aviso.setVisibility(View.VISIBLE);
                            break;

                        case EVENTO:
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
                            break;

                    }

                }else{

                    lugar.setVisibility(View.GONE);
                    telefono.setVisibility(View.GONE);
                    email.setVisibility(View.GONE);
                    repAnios.setVisibility(View.GONE);
                    repMeses.setVisibility(View.GONE);
                    repDias.setVisibility(View.GONE);
                    drepAnios.setVisibility(View.GONE);
                    drepMeses.setVisibility(View.GONE);
                    drepDias.setVisibility(View.GONE);
                    avisoDias.setVisibility(View.GONE);
                    avisoHoras.setVisibility(View.GONE);
                    avisoMinutos.setVisibility(View.GONE);
                    fechaIni.setVisibility(View.GONE);
                    horaIni.setVisibility(View.GONE);
                    fechaFin.setVisibility(View.GONE);
                    horaFin.setVisibility(View.GONE);
                    btnfini.setVisibility(View.GONE);
                    btnffin.setVisibility(View.GONE);
                    btnhini.setVisibility(View.GONE);
                    btnhfin.setVisibility(View.GONE);
                    laviso.setVisibility(View.GONE);
                    lrep.setVisibility(View.GONE);
                    ldrep.setVisibility(View.GONE);
                    repeticiones.setVisibility(View.GONE);
                    aviso.setVisibility(View.GONE);
                    proyRel.setVisibility(View.GONE);
                    cliRel.setVisibility(View.GONE);
                    imagen.setVisibility(View.GONE);
                    descipcion.setVisibility(View.GONE);
                    relProy.setVisibility(View.GONE);
                    relCli.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    protected void setMaestroDetallePort() {
        maestroDetalleSeparados = true;
    }

    @Override
    protected void setMaestroDetalleLand() { maestroDetalleSeparados = false; }

    @Override
    protected void setMaestroDetalleTabletLand() { maestroDetalleSeparados = false; }

    @Override
    protected void setMaestroDetalleTabletPort() { maestroDetalleSeparados = false; }

    @Override
    protected boolean delete() {

        if (mostrarDialogoBorrarRep(id)){id=null;return true;}
        return false;

    }

    @Override
    protected boolean update() {

        if (id==null){
            if(registrar()){return true;}
        }else {

            valores = new ContentValues();

            if (idProyecto != null) {

                consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_NOMPROYECTOREL, nombreProyecto);
                consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_PROYECTOREL, idProyecto);

            }
            if (idCliente != null) {

                consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_NOMCLIENTEREL, nombreCliente);
                consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_CLIENTEREL, idCliente);
            }

            switch (tevento) {

                case TAREA:
                    break;

                case CITA:
                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTO, finiEvento);
                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_HORAINIEVENTO, hiniEvento);
                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_LUGAR, lugar.getText().toString());
                    break;

                case EMAIL:
                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTO, finiEvento);
                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_HORAINIEVENTO, hiniEvento);
                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_EMAIL, email.getText().toString());
                    break;

                case LLAMADA:
                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTO, finiEvento);
                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_HORAINIEVENTO, hiniEvento);
                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_TELEFONO, telefono.getText().toString());
                    break;

                case EVENTO:
                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTO, finiEvento);
                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAFINEVENTO, ffinEvento);
                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_HORAINIEVENTO, hiniEvento);
                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_HORAFINEVENTO, hfinEvento);
                    break;

            }

            consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_DESCRIPCION, descipcion.getText().toString());
            consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_TIPOEVENTO, tevento);
            consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_COMPLETADA, JavaUtil.comprobarInteger(completa.getText().toString()));
            consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_RUTAFOTO, path);

            if (aviso.isChecked()) {


                long fechaaviso = (JavaUtil.comprobarLong(avisoMinutos.getText().toString()) * MINUTOSLONG) +
                        (JavaUtil.comprobarLong(avisoHoras.getText().toString()) * HORASLONG) +
                        (JavaUtil.comprobarLong(avisoDias.getText().toString()) * DIASLONG);
                consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_AVISO, String.valueOf(fechaaviso));

            } else {

                if (modelo != null) {
                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_AVISO, modelo.getLong(EVENTO_AVISO));
                }
            }

            consulta.updateRegistro(TABLA_EVENTO, id, valores);

            if (repeticiones.isChecked()) {

                idMulti = modelo.getString(EVENTO_IDMULTI);
                System.out.println("idMulti = " + idMulti);
                String seleccion = ContratoPry.Tablas.EVENTO_IDMULTI + " = '" + idMulti +
                        "' AND " + ContratoPry.Tablas.EVENTO_ID_EVENTO +
                        " <> '" + id + "'";
                if (consulta.deleteRegistros(TABLA_EVENTO, seleccion)>0) {

                    long diffecha = ffinEvento - finiEvento;

                    long offRep = (JavaUtil.comprobarLong(repAnios.getText().toString()) * ANIOSLONG) +
                            (JavaUtil.comprobarLong(repMeses.getText().toString()) * MESESLONG) +
                            (JavaUtil.comprobarLong(repDias.getText().toString()) * DIASLONG);

                    long duracionRep = (JavaUtil.comprobarLong(drepAnios.getText().toString()) * ANIOSLONG) +
                            (JavaUtil.comprobarLong(drepMeses.getText().toString()) * MESESLONG) +
                            (JavaUtil.comprobarLong(drepDias.getText().toString()) * DIASLONG);
                    long hoy = JavaUtil.hoy();
                    if (finiEvento == 0) {
                        finiEvento = hoy;
                    }
                    long fecharep = finiEvento + offRep;

                    while (duracionRep + hoy > fecharep) {

                        consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTO, fecharep);
                        if (tipoEvento.equals(EVENTO)) {
                            consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAFINEVENTO, (fecharep + diffecha));
                        }
                        consulta.insertRegistro(TABLA_EVENTO, valores);
                        fecharep += offRep;
                    }
                }

                Toast.makeText(contexto, "Repeticiones actualizadas", Toast.LENGTH_SHORT).show();

            }

            Toast.makeText(contexto, "Registro actualizado", Toast.LENGTH_SHORT).show();

            modelo = consulta.queryObject(campos, id);
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

        tablaCab = null;
    }

    @Override
    protected void setContext() {

        contexto = getContext();
    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_EVENTO;
    }

    @Override
    protected void setCampoID() {
        campoID = EVENTO_ID_EVENTO;
    }

    @Override
    protected void setBundle() {

        proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
        cliente = (Modelo) bundle.getSerializable(TABLA_CLIENTE);

        if (modelo!=null) {
            idMulti = modelo.getString(EVENTO_IDMULTI);
        }

    }

    @Override
    protected void setDatos() {

        tiposEvento.setVisibility(View.GONE);

        setAdaptadorClientes(cliRel);

        setAdaptadorProyectos(proyRel);

        completa.setText(modelo.getString(EVENTO_COMPLETADA));
        if (modelo !=null && modelo.getCampos(EVENTO_RUTAFOTO)!=null){
            imagenUtil.setImageUriCircle(modelo.getCampos(EVENTO_RUTAFOTO),imagen);}
            //imagen.setImageURI(Uri.parse(modelo.getCampos(EVENTO_RUTAFOTO)));}


        if (modelo.getString(EVENTO_PROYECTOREL)!=null){

            idProyecto = modelo.getString(EVENTO_PROYECTOREL);
            nombreProyecto = modelo.getString(EVENTO_NOMPROYECTOREL);
            proyRel.setText(nombreProyecto);
            relProy.setChecked(true);
            idCliente = modelo.getString(EVENTO_CLIENTEREL);
            nombreCliente = modelo.getString(EVENTO_NOMCLIENTEREL);
            cliRel.setText(nombreCliente);
            relCli.setChecked(true);

        }else if (modelo.getString(EVENTO_CLIENTEREL)!=null){

            idCliente = modelo.getString(EVENTO_CLIENTEREL);
            nombreCliente = modelo.getString(EVENTO_NOMCLIENTEREL);
            cliRel.setText(nombreCliente);
            relCli.setChecked(true);
        }

        lugar.setVisibility(View.GONE);
        telefono.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        repAnios.setVisibility(View.GONE);
        repMeses.setVisibility(View.GONE);
        repDias.setVisibility(View.GONE);
        drepAnios.setVisibility(View.GONE);
        drepMeses.setVisibility(View.GONE);
        drepDias.setVisibility(View.GONE);
        fechaIni.setVisibility(View.GONE);
        horaIni.setVisibility(View.GONE);
        fechaFin.setVisibility(View.GONE);
        horaFin.setVisibility(View.GONE);
        btnfini.setVisibility(View.GONE);
        btnffin.setVisibility(View.GONE);
        btnhini.setVisibility(View.GONE);
        btnhfin.setVisibility(View.GONE);
        laviso.setVisibility(View.GONE);
        lrep.setVisibility(View.GONE);
        ldrep.setVisibility(View.GONE);
        repeticiones.setVisibility(View.GONE);
        proyRel.setVisibility(View.GONE);
        cliRel.setVisibility(View.GONE);
        lcompleta.setVisibility(View.VISIBLE);
        completa.setVisibility(View.VISIBLE);

        tipoEvento.setText(modelo.getString(EVENTO_TIPOEVENTO).toUpperCase());
        tevento = modelo.getString(EVENTO_TIPOEVENTO);
        imagenUtil = new ImagenUtil(contexto);

        if (modelo.getString(EVENTO_RUTAFOTO)!=null){
            imagen.setImageURI(Uri.parse(modelo.getString(EVENTO_RUTAFOTO)));
            path = modelo.getString(EVENTO_RUTAFOTO);
        }
        descipcion.setText(modelo.getString(EVENTO_DESCRIPCION));

        if (modelo.getLong(EVENTO_AVISO)>0) {

            long[] res = JavaUtil.longA_ddhhmm(modelo.getLong(EVENTO_AVISO));

            avisoDias.setText(String.valueOf(res[0]));
            avisoHoras.setText(String.valueOf(res[1]));
            avisoMinutos.setText(String.valueOf(res[2]));
            aviso.setChecked(true);
            avisoDias.setVisibility(View.VISIBLE);
            avisoHoras.setVisibility(View.VISIBLE);
            avisoMinutos.setVisibility(View.VISIBLE);
            laviso.setVisibility(View.VISIBLE);
        }else{
            avisoDias.setVisibility(View.GONE);
            avisoHoras.setVisibility(View.GONE);
            avisoMinutos.setVisibility(View.GONE);
            laviso.setVisibility(View.GONE);
        }




        switch (tevento){

            case TAREA:

                break;

            case CITA:
                lugar.setVisibility(View.VISIBLE);
                fechaIni.setVisibility(View.VISIBLE);
                horaIni.setVisibility(View.VISIBLE);
                btnfini.setVisibility(View.VISIBLE);
                btnhini.setVisibility(View.VISIBLE);
                repeticiones.setVisibility(View.VISIBLE);
                aviso.setVisibility(View.VISIBLE);
                lugar.setText(modelo.getString(EVENTO_LUGAR));
                finiEvento = modelo.getLong(EVENTO_FECHAINIEVENTO);
                fechaIni.setText(getDate(finiEvento));
                hiniEvento = modelo.getLong(EVENTO_HORAINIEVENTO);
                horaIni.setText(getTime(hiniEvento));
                break;

            case EMAIL:
                email.setVisibility(View.VISIBLE);
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

            case LLAMADA:
                telefono.setVisibility(View.VISIBLE);
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

            case EVENTO:
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

    @Override
    protected void setAcciones() {

        btnfini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialogInicio();
            }
        });
        btnffin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialogFin();
            }
        });
        btnhini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTimePickerDialogini();
            }
        });
        btnhfin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTimePickerDialogfin();
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
                    //avisoDias.setText("0");
                    //avisoHoras.setText("0");
                    //avisoMinutos.setText("0");

                }else{

                    avisoDias.setVisibility(View.GONE);
                    avisoHoras.setVisibility(View.GONE);
                    avisoMinutos.setVisibility(View.GONE);
                    laviso.setVisibility(View.GONE);
                    avisoDias.setText("0");
                    avisoHoras.setText("0");
                    avisoMinutos.setText("0");

                }
            }
        });

        repeticiones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    repAnios.setVisibility(View.VISIBLE);
                    repMeses.setVisibility(View.VISIBLE);
                    repDias.setVisibility(View.VISIBLE);
                    drepAnios.setVisibility(View.VISIBLE);
                    drepMeses.setVisibility(View.VISIBLE);
                    drepDias.setVisibility(View.VISIBLE);
                    lrep.setVisibility(View.VISIBLE);
                    ldrep.setVisibility(View.VISIBLE);

                }else{

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



    }


    @Override
    protected void setInicio() {

        lista = new ArrayList<>();
        btnsave = view.findViewById(R.id.btn_save);
        btndelete = view.findViewById(R.id.btn_del);
        btnback = view.findViewById(R.id.btn_back);
        imagen = view.findViewById(R.id.imgudevento);
        if (proyecto!=null && proyecto.getString(PROYECTO_RUTAFOTO)!=null){
            path = proyecto.getString(PROYECTO_RUTAFOTO);
            imagen.setImageURI(Uri.parse(path));
        }
        tiposEvento = view.findViewById(R.id.sptiponevento);
        tipoEvento = view.findViewById(R.id.sptipoudevento);
        proyRel = view.findViewById(R.id.sppryudevento);
        cliRel = view.findViewById(R.id.spcliudevento);
        descipcion = view.findViewById(R.id.etdescudevento);
        lugar = view.findViewById(R.id.etlugarudevento);
        email = view.findViewById(R.id.etemailudevento);
        telefono = view.findViewById(R.id.ettelefonoudevento);
        fechaIni = view.findViewById(R.id.etfechainiudevento);
        horaIni = view.findViewById(R.id.ethorainiudevento);
        fechaFin = view.findViewById(R.id.etfechafinudevento);
        horaFin = view.findViewById(R.id.ethorafinudevento);
        repAnios = view.findViewById(R.id.etrepaniosudevento);
        repMeses = view.findViewById(R.id.etrepmesesudevento);
        repDias = view.findViewById(R.id.etrepdiasudevento);
        drepAnios = view.findViewById(R.id.etrepdaniosudevento);
        drepMeses = view.findViewById(R.id.etrepdmesesudevento);
        drepDias = view.findViewById(R.id.etrepddiasudevento);
        avisoDias = view.findViewById(R.id.etdiasudevento);
        avisoHoras = view.findViewById(R.id.ethorasudevento);
        avisoMinutos = view.findViewById(R.id.etminutosudevento);
        aviso = view.findViewById(R.id.chavisoudevento);
        repeticiones = view.findViewById(R.id.chrptudevento);
        relProy = view.findViewById(R.id.chpryudevento);
        relCli = view.findViewById(R.id.chcliudevento);
        laviso = view.findViewById(R.id.ltvavisoudevento);
        lrep = view.findViewById(R.id.ltvreptudevento);
        ldrep = view.findViewById(R.id.ltvdreptudevento);
        btnfini = view.findViewById(R.id.imgbtnfiniudevento);
        btnffin = view.findViewById(R.id.imgbtnffinudevento);
        btnhini = view.findViewById(R.id.imgbtnhiniudevento);
        btnhfin = view.findViewById(R.id.imgbtnhfinudevento);
        completa = view.findViewById(R.id.etcompletadaudevento);
        lcompleta = view.findViewById(R.id.ltvcompletadaudevento);

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_ud_evento;

    }

    @Override
    protected boolean registrar() {

        valores = new ContentValues();

        if (idProyecto!=null){

            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_NOMPROYECTOREL,nombreProyecto);
            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_PROYECTOREL,idProyecto);

        }
        if (idCliente!=null){

            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_NOMCLIENTEREL,nombreCliente);
            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_CLIENTEREL,idCliente);
        }

        if (path!=null){

            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_RUTAFOTO,path);
        }

        switch (tevento){

            case TAREA:
                break;

            case CITA:
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_LUGAR,lugar.getText().toString());

                if (lugar==null || lugar.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Debe introducir una direccion para la cita",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;

            case EMAIL:
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_EMAIL,email.getText().toString());
                if (email==null || email.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Debe introducir una direccion de email",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;

            case LLAMADA:
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_TELEFONO,telefono.getText().toString());

                if (telefono==null || telefono.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Debe introducir un numero de telefono",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;

            case EVENTO:
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAFINEVENTO,ffinEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAFINEVENTO,hfinEvento);
                break;

        }
        consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_DESCRIPCION,descipcion.getText().toString());
        consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_TIPOEVENTO,tevento);

        if (aviso.isChecked() && !tipoEvento.equals(TAREA)){

            long fechaaviso = (JavaUtil.comprobarLong(avisoMinutos.getText().toString()) * MINUTOSLONG) +
                    (JavaUtil.comprobarLong(avisoHoras.getText().toString()) * HORASLONG) +
                    (JavaUtil.comprobarLong(avisoDias.getText().toString()) * DIASLONG);
            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_AVISO,fechaaviso);


        }

        System.out.println("valores = " + valores);



            idMulti = id = consulta.idInsertRegistro(tabla, valores);

            modelo = consulta.queryObject(campos,id);

            if (repeticiones.isChecked() && !tipoEvento.equals(TAREA)) {

                consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_IDMULTI, idMulti);

                consulta.updateRegistro(TABLA_EVENTO, idMulti, valores);

                long diffecha = ffinEvento - finiEvento;

                long offRep = (JavaUtil.comprobarLong(repAnios.getText().toString()) * ANIOSLONG) +
                        (JavaUtil.comprobarLong(repMeses.getText().toString()) * MESESLONG) +
                        (JavaUtil.comprobarLong(repDias.getText().toString()) * DIASLONG);

                long duracionRep = (JavaUtil.comprobarLong(drepAnios.getText().toString()) * ANIOSLONG) +
                        (JavaUtil.comprobarLong(drepMeses.getText().toString()) * MESESLONG) +
                        (JavaUtil.comprobarLong(drepDias.getText().toString()) * DIASLONG);
                long hoy = JavaUtil.hoy();
                if (finiEvento == 0) {
                    finiEvento = hoy;
                }
                long fecharep = finiEvento + offRep;
                System.out.println("duracion mayor k = " + (duracionRep + hoy > fecharep));
                while (duracionRep + hoy > fecharep) {

                    consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTO, fecharep);

                    if (tipoEvento.equals(EVENTO)) {
                        valores.put(ContratoPry.Tablas.EVENTO_FECHAFINEVENTO, String.valueOf(fecharep + diffecha));
                    }
                    consulta.insertRegistro(TABLA_EVENTO, valores);
                    fecharep += offRep;
                }

            }

            Toast.makeText(getContext(), "Registro creado",
                    Toast.LENGTH_SHORT).show();
            nuevo = false;
            setDatos();
            return true;

    }

    @Override
    protected void setContenedor() {


    }

    @Override
    protected void setcambioFragment() {

        if (tipoEvento!=null) {

            if (namef.equals(AGENDA)) {

                icFragmentos.enviarBundleAFragment(bundle, new FragmentAgenda());

            } else if (namef.equals(PRESUPUESTO) || namef.equals(PROYECTO)) {

                bundle.putSerializable(TABLA_PROYECTO, proyecto);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDProyecto());

            } else if (namef.equals(CLIENTE) || namef.equals(PROSPECTO)) {

                bundle.putSerializable(TABLA_CLIENTE, cliente);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDCliente());

            }else if (namef.equals(EVENTO) && namesubclass.equals(NUEVOEVENTO)){
                namesubclass = namesubdef = setNamefdef();

            }

        }
    }

    private boolean mostrarDialogoBorrarRep(final String id) {

        modelo = consulta.queryObject(campos,id);
        final CharSequence[] opciones = {"Borrar sólo este modelo", "Borrar este y repeticiones", "Borrar sólo repeticiones", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Borrar sólo este modelo")) {

                    if (consulta.deleteRegistro(TABLA_EVENTO, id)>0) {

                        Toast.makeText(contexto, "Registro borrado ", Toast.LENGTH_SHORT).show();
                        actualizarConsultasRV();
                        listaRV();
                        namesubclass = namesubdef = setNamefdef();
                        enviarAct();
                        if (maestroDetalleSeparados) {
                            defectoMaestroDetalleSeparados();
                            cambiarFragment();
                        }

                    }else{

                        Toast.makeText(contexto, "Error al borrar registro "+id, Toast.LENGTH_SHORT).show();
                    }

                } else if (opciones[which].equals("Borrar este y repeticiones")) {

                    idMulti = modelo.getString(EVENTO_IDMULTI);
                    String seleccion = EVENTO_IDMULTI + " = '" + idMulti + "'";
                    if (consulta.deleteRegistros(TABLA_EVENTO,EVENTO_IDMULTI,idMulti,null,IGUAL)>0) {

                        Toast.makeText(contexto, "Regitros borrados", Toast.LENGTH_SHORT).show();
                        actualizarConsultasRV();
                        listaRV();
                        namesubclass = namesubdef = setNamefdef();
                        enviarAct();
                        if (maestroDetalleSeparados) {
                            defectoMaestroDetalleSeparados();
                            cambiarFragment();
                        }

                    }else{
                        Toast.makeText(contexto, "Error al borrar los registros "+id, Toast.LENGTH_SHORT).show();
                    }


                } else if (opciones[which].equals("Borrar sólo repeticiones")) {

                    idMulti = modelo.getString(EVENTO_IDMULTI);
                    String seleccion = EVENTO_IDMULTI + " = '" + idMulti +
                            "' AND " + EVENTO_ID_EVENTO + " <> '" + id + "'";
                    if (consulta.deleteRegistros(TABLA_EVENTO,seleccion)>0) {

                        Toast.makeText(contexto, "Regitros borrados", Toast.LENGTH_SHORT).show();
                        actualizarConsultasRV();
                        listaRV();
                        namesubclass = namesubdef = setNamefdef();
                        enviarAct();
                        if (maestroDetalleSeparados) {
                            defectoMaestroDetalleSeparados();
                            cambiarFragment();
                        }

                    }else{
                        Toast.makeText(contexto, "Error al borrar los registros "+id, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


        return true;
    }

    public class AdaptadorRV extends RecyclerView.Adapter<AdaptadorRV.ViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas {

        ArrayList<Modelo> list;
        private View.OnClickListener listener;

        public AdaptadorRV(ArrayList<Modelo> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_evento, null, false);

            view.setOnClickListener(this);


            return new ViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder eventoViewHolder, final int position) {

            eventoViewHolder.tipo.setText(list.get(position).getString(EVENTO_TIPOEVENTO).toUpperCase());
            eventoViewHolder.descripcion.setText(list.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_DESCRIPCION));
            eventoViewHolder.telefono.setText(list.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_TELEFONO));
            eventoViewHolder.lugar.setText(list.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_LUGAR));
            eventoViewHolder.email.setText(list.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_EMAIL));
            eventoViewHolder.nomPryRel.setText(list.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_NOMPROYECTOREL));
            eventoViewHolder.nomCliRel.setText(list.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_NOMCLIENTEREL));
            eventoViewHolder.fechaini.setText(getDate(Long.parseLong(list.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_FECHAINIEVENTO))));
            eventoViewHolder.fechafin.setText(getDate(Long.parseLong(list.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_FECHAFINEVENTO))));
            eventoViewHolder.horaini.setText(getTime(Long.parseLong(list.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_HORAINIEVENTO))));
            eventoViewHolder.horafin.setText(getTime(Long.parseLong(list.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_HORAFINEVENTO))));
            if (Integer.parseInt(list.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_COMPLETADA))==0){
                eventoViewHolder.pbar.setVisibility(View.GONE);
                eventoViewHolder.porccompleta.setVisibility(View.GONE);
            }else if (Integer.parseInt(list.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_COMPLETADA))>99) {
                eventoViewHolder.completa.setChecked(true);
            }else{
                eventoViewHolder.pbar.setProgress(Integer.parseInt(list.get(position).getCampos
                        (ContratoPry.Tablas.EVENTO_COMPLETADA)));
                eventoViewHolder.porccompleta.setText(String.format("%s %s",list.get(position).getCampos
                        (EVENTO_COMPLETADA),"%"));
            }

            String tipoEvento = list.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_TIPOEVENTO);

            eventoViewHolder.fechaini.setVisibility(View.GONE);
            eventoViewHolder.fechafin.setVisibility(View.GONE);
            eventoViewHolder.horaini.setVisibility(View.GONE);
            eventoViewHolder.horafin.setVisibility(View.GONE);
            eventoViewHolder.btnllamada.setVisibility(View.GONE);
            eventoViewHolder.btnmapa.setVisibility(View.GONE);
            eventoViewHolder.btnemail.setVisibility(View.GONE);
            eventoViewHolder.telefono.setVisibility(View.GONE);
            eventoViewHolder.lugar.setVisibility(View.GONE);
            eventoViewHolder.email.setVisibility(View.GONE);

            switch (tipoEvento){

                case TAREA:
                    break;

                case CITA:
                    eventoViewHolder.lugar.setVisibility(View.VISIBLE);
                    eventoViewHolder.fechaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.horaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.btnmapa.setVisibility(View.VISIBLE);
                    break;

                case EMAIL:
                    eventoViewHolder.btnemail.setVisibility(View.VISIBLE);
                    eventoViewHolder.fechaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.horaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.email.setVisibility(View.VISIBLE);
                    break;

                case LLAMADA:
                    eventoViewHolder.telefono.setVisibility(View.VISIBLE);
                    eventoViewHolder.fechaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.horaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.btnllamada.setVisibility(View.VISIBLE);
                    break;

                case EVENTO:
                    eventoViewHolder.fechaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.horaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.fechafin.setVisibility(View.VISIBLE);
                    eventoViewHolder.horafin.setVisibility(View.VISIBLE);
                    break;

            }
            imagenUtil = new ImagenUtil(contexto);
            if (list.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_RUTAFOTO)!=null) {
                imagenUtil.setImageUriCircle(list.get(position).getCampos
                        (ContratoPry.Tablas.EVENTO_RUTAFOTO),eventoViewHolder.foto);
                //eventoViewHolder.foto.setImageURI(Uri.parse(list.get(position).getCampos
                //        (ContratoPry.Tablas.EVENTO_RUTAFOTO)));
            }


            long retraso = JavaUtil.hoy()-list.get(position).getLong(EVENTO_FECHAINIEVENTO);
            if (retraso > 3 * CommonPry.DIASLONG){eventoViewHolder.card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));}
            else if (retraso > CommonPry.DIASLONG){eventoViewHolder.card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));}
            else {eventoViewHolder.card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));}//imgret.setImageResource(R.drawable.alert_box_v);}
            if(tipoEvento.equals(TAREA))
            {eventoViewHolder.card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_tarea));}

            eventoViewHolder.btneditar.setText("EDITAR "+ tipoEvento.toUpperCase());
            eventoViewHolder.btnllamada.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppActivity.hacerLlamada(AppActivity.getAppContext()
                            ,eventoViewHolder.telefono.getText().toString());
                }
            });

            eventoViewHolder.btnemail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppActivity.enviarEmail(AppActivity.getAppContext(),eventoViewHolder.email.getText().toString());

                }
            });

            eventoViewHolder.btnmapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!eventoViewHolder.lugar.getText().toString().equals("")){

                        viewOnMapA(AppActivity.getAppContext(),eventoViewHolder.lugar.getText().toString());
                    }




                }
            });


            eventoViewHolder.completa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    ContentValues valores = new ContentValues();

                    consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_COMPLETADA,"100");
                    consulta.updateRegistro(TABLA_EVENTO,list.get(position).getString
                            (EVENTO_ID_EVENTO),valores);

                }
            });

            eventoViewHolder.btneditar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    modelo = list.get(position);
                    id = modelo.getString(EVENTO_ID_EVENTO);
                    maestroDetalle();
                    setDatos();


                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onClick(View v) {

            if (listener != null) {

                listener.onClick(v);


            }

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView descripcion,fechaini,telefono,lugar,nomPryRel,nomCliRel,
                    fechafin, horaini,horafin,porccompleta,email,tipo;
            ImageButton btnllamada, btnmapa, btnemail;
            ProgressBar pbar;
            ImageView foto,imgret;
            CheckBox completa;
            Button btneditar;
            CardView card;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                descripcion = itemView.findViewById(R.id.tvdesclevento);
                fechaini = itemView.findViewById(R.id.tvfinilevento);
                fechafin = itemView.findViewById(R.id.tvffinlevento);
                horaini = itemView.findViewById(R.id.tvhinilevento);
                horafin = itemView.findViewById(R.id.tvhfinlevento);
                telefono = itemView.findViewById(R.id.tvtelefonoevento);
                lugar = itemView.findViewById(R.id.tvlugarevento);
                email = itemView.findViewById(R.id.tvemaillevento);
                nomPryRel = itemView.findViewById(R.id.tvnompryrellevento);
                nomCliRel = itemView.findViewById(R.id.tvnomclirelevento);
                porccompleta = itemView.findViewById(R.id.tvcompporcevento);
                btnllamada = itemView.findViewById(R.id.imgbtnllamadaevento);
                btnmapa = itemView.findViewById(R.id.imgbtnmapaevento);
                btnemail = itemView.findViewById(R.id.imgbtnemaillevento);
                pbar = itemView.findViewById(R.id.pbarevento);
                foto = itemView.findViewById(R.id.imglevento);
                completa = itemView.findViewById(R.id.cBoxcompletlevento);
                btneditar = itemView.findViewById(R.id.btneditevento);
                tipo = itemView.findViewById(R.id.tvtipolevento);
                card = itemView.findViewById(R.id.cardlevento);

            }
        }
    }

    private void setAdaptadorAuto(final AutoCompleteTextView autoCompleteTextView) {

        autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(
                getContext(), R.layout.item_list_evento, lista, EVENTO_DESCRIPCION) {
            @Override
            public void onEntrada(Modelo entrada, View itemView) {

                TextView descripcion,fechaini,telefono,lugar,nomPryRel,nomCliRel,
                        fechafin, horaini,horafin,porccompleta,email,tipo;
                ImageButton btnllamada, btnmapa, btnemail;
                ProgressBar pbar;
                ImageView foto,imgret;
                CheckBox completa;
                Button btneditar;
                CardView card;

                descripcion = itemView.findViewById(R.id.tvdesclevento);
                fechaini = itemView.findViewById(R.id.tvfinilevento);
                fechafin = itemView.findViewById(R.id.tvffinlevento);
                horaini = itemView.findViewById(R.id.tvhinilevento);
                horafin = itemView.findViewById(R.id.tvhfinlevento);
                telefono = itemView.findViewById(R.id.tvtelefonoevento);
                lugar = itemView.findViewById(R.id.tvlugarevento);
                email = itemView.findViewById(R.id.tvemaillevento);
                nomPryRel = itemView.findViewById(R.id.tvnompryrellevento);
                nomCliRel = itemView.findViewById(R.id.tvnomclirelevento);
                porccompleta = itemView.findViewById(R.id.tvcompporcevento);
                btnllamada = itemView.findViewById(R.id.imgbtnllamadaevento);
                btnmapa = itemView.findViewById(R.id.imgbtnmapaevento);
                btnemail = itemView.findViewById(R.id.imgbtnemaillevento);
                pbar = itemView.findViewById(R.id.pbarevento);
                foto = itemView.findViewById(R.id.imglevento);
                completa = itemView.findViewById(R.id.cBoxcompletlevento);
                btneditar = itemView.findViewById(R.id.btneditevento);
                tipo = itemView.findViewById(R.id.tvtipolevento);
                card = itemView.findViewById(R.id.cardlevento);

                tipo.setText(entrada.getString(EVENTO_TIPOEVENTO).toUpperCase());
                descripcion.setText(entrada.getCampos
                        (ContratoPry.Tablas.EVENTO_DESCRIPCION));
                telefono.setText(entrada.getCampos
                       (ContratoPry.Tablas.EVENTO_TELEFONO));
                lugar.setText(entrada.getCampos
                        (ContratoPry.Tablas.EVENTO_LUGAR));
                email.setText(entrada.getCampos
                        (ContratoPry.Tablas.EVENTO_EMAIL));
                nomPryRel.setText(entrada.getCampos
                        (ContratoPry.Tablas.EVENTO_NOMPROYECTOREL));
                nomCliRel.setText(entrada.getCampos
                        (ContratoPry.Tablas.EVENTO_NOMCLIENTEREL));
                fechaini.setText(getDate(Long.parseLong(entrada.getCampos
                        (ContratoPry.Tablas.EVENTO_FECHAINIEVENTO))));
                fechafin.setText(getDate(Long.parseLong(entrada.getCampos
                        (ContratoPry.Tablas.EVENTO_FECHAFINEVENTO))));
                horaini.setText(getTime(Long.parseLong(entrada.getCampos
                        (ContratoPry.Tablas.EVENTO_HORAINIEVENTO))));
                horafin.setText(getTime(Long.parseLong(entrada.getCampos
                        (ContratoPry.Tablas.EVENTO_HORAFINEVENTO))));
                if (Integer.parseInt(entrada.getCampos
                        (ContratoPry.Tablas.EVENTO_COMPLETADA))==0){
                    pbar.setVisibility(View.GONE);
                    porccompleta.setVisibility(View.GONE);
                }else if (Integer.parseInt(entrada.getCampos
                        (ContratoPry.Tablas.EVENTO_COMPLETADA))>99) {
                    completa.setChecked(true);
                }else{
                    pbar.setProgress(Integer.parseInt(entrada.getCampos
                            (ContratoPry.Tablas.EVENTO_COMPLETADA)));
                    porccompleta.setText(entrada.getCampos
                            (ContratoPry.Tablas.EVENTO_COMPLETADA));
                }

                String tipoEvento = entrada.getCampos
                        (ContratoPry.Tablas.EVENTO_TIPOEVENTO);

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
                        break;

                    case CITA:
                        lugar.setVisibility(View.VISIBLE);
                        fechaini.setVisibility(View.VISIBLE);
                        horaini.setVisibility(View.VISIBLE);
                        btnmapa.setVisibility(View.VISIBLE);
                        break;

                    case EMAIL:
                        btnemail.setVisibility(View.VISIBLE);
                        fechaini.setVisibility(View.VISIBLE);
                        horaini.setVisibility(View.VISIBLE);
                        email.setVisibility(View.VISIBLE);
                        break;

                    case LLAMADA:
                        telefono.setVisibility(View.VISIBLE);
                        fechaini.setVisibility(View.VISIBLE);
                        horaini.setVisibility(View.VISIBLE);
                        btnllamada.setVisibility(View.VISIBLE);
                        break;

                    case EVENTO:
                        fechaini.setVisibility(View.VISIBLE);
                        horaini.setVisibility(View.VISIBLE);
                        fechafin.setVisibility(View.VISIBLE);
                        horafin.setVisibility(View.VISIBLE);
                        break;

                }
                imagenUtil = new ImagenUtil(contexto);
                if (entrada.getCampos
                        (ContratoPry.Tablas.EVENTO_RUTAFOTO)!=null) {
                    imagenUtil.setImageUriCircle(entrada.getCampos
                            (ContratoPry.Tablas.EVENTO_RUTAFOTO),foto);
                    //eventoViewHolder.foto.setImageURI(Uri.parse(list.get(position).getCampos
                    //        (ContratoPry.Tablas.EVENTO_RUTAFOTO)));
                }


                long retraso = JavaUtil.hoy()-entrada.getLong(EVENTO_FECHAINIEVENTO);
                if (retraso > 3 * CommonPry.DIASLONG){card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));}
                else if (retraso > CommonPry.DIASLONG){card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));}
                else {card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));}//imgret.setImageResource(R.drawable.alert_box_v);}
                if(tipoEvento.equals(TAREA))
                {card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_tarea));}

                btneditar.setVisibility(View.GONE);

            }

        });

    }

    private void setAdaptadorProyectos(final AutoCompleteTextView autoCompleteTextView){

        listaProyectos =  consulta.queryList(CAMPOS_PROYECTO,null,null);

        autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(getContext(),R.layout.item_list_proyecto,listaProyectos,PROYECTO_NOMBRE) {
            @Override
            public void onEntrada(Modelo entrada, View view) {

                ImageView imagen = view.findViewById(R.id.imglistaproyectos);
                TextView nombre = view.findViewById(R.id.tvnombrelistaproyectos);
                TextView descripcion = view.findViewById(R.id.tvdesclistaproyectos);
                ImageView imgcli = view.findViewById(R.id.imgclientelistaproyectos);
                TextView nomcli = view.findViewById(R.id.tvnombreclientelistaproyectos);
                ImageView imgest = view.findViewById(R.id.imgestadolistaproyectos);
                TextView estado = view.findViewById(R.id.tvestadolistaproyectos);
                ProgressBar bar = view.findViewById(R.id.progressBarlistaproyectos);

                descripcion.setText(entrada.getCampos(ContratoPry.Tablas.PROYECTO_DESCRIPCION));

                nombre.setText(entrada.getCampos(ContratoPry.Tablas.PROYECTO_NOMBRE));
                nomcli.setText(entrada.getCampos(ContratoPry.Tablas.CLIENTE_NOMBRE));
                estado.setText(entrada.getCampos(ContratoPry.Tablas.ESTADO_DESCRIPCION));

                bar.setProgress(Integer.parseInt(entrada.getCampos
                        (ContratoPry.Tablas.PROYECTO_TOTCOMPLETADO)));

                long retraso = Long.parseLong(entrada.getCampos
                        (ContratoPry.Tablas.PROYECTO_RETRASO));
                if (retraso > 3 * CommonPry.DIASLONG) {
                    imgest.setImageResource(R.drawable.alert_box_r);
                } else if (retraso > CommonPry.DIASLONG) {
                    imgest.setImageResource(R.drawable.alert_box_a);
                } else {
                    imgest.setImageResource(R.drawable.alert_box_v);
                }

                if (entrada.getCampos(ContratoPry.Tablas.PROYECTO_RUTAFOTO) != null) {
                    imagen.setImageURI(Uri.parse(entrada.getCampos
                            (ContratoPry.Tablas.PROYECTO_RUTAFOTO)));
                }
                int peso = Integer.parseInt(entrada.getCampos
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

            }
        });

    }

    private void setAdaptadorClientes(final AutoCompleteTextView autoCompleteTextView) {

        listaClientes = consulta.queryList(CAMPOS_CLIENTE, null, null);

        autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(getContext(),R.layout.item_list_cliente,listaClientes,CLIENTE_NOMBRE) {
            @Override
            public void onEntrada(Modelo entrada, View view) {

                ImageView imgcli = view.findViewById(R.id.imgclilcliente);
                TextView nombreCli = view.findViewById(R.id.tvnomclilcliente);
                TextView contactoCli = view.findViewById(R.id.tvcontacclilcliente);
                TextView telefonoCli = view.findViewById(R.id.tvtelclilcliente);
                TextView emailCli = view.findViewById(R.id.tvemailclilcliente);
                TextView dirCli = view.findViewById(R.id.tvdirclilcliente);

                int peso = entrada.getInt((CLIENTE_PESOTIPOCLI));

                if (peso > 6) {
                    imgcli.setImageResource(R.drawable.clientev);
                } else if (peso > 3) {
                    imgcli.setImageResource(R.drawable.clientea);
                } else if (peso > 0) {
                    imgcli.setImageResource(R.drawable.clienter);
                } else {
                    imgcli.setImageResource(R.drawable.cliente);
                }

                nombreCli.setText(entrada.getCampos(ContratoPry.Tablas.CLIENTE_NOMBRE));
                contactoCli.setText(entrada.getCampos(ContratoPry.Tablas.CLIENTE_CONTACTO));
                telefonoCli.setText(entrada.getCampos(ContratoPry.Tablas.CLIENTE_TELEFONO));
                emailCli.setText(entrada.getCampos(ContratoPry.Tablas.CLIENTE_EMAIL));
                dirCli.setText(entrada.getCampos(ContratoPry.Tablas.CLIENTE_DIRECCION));


            }

        });

    }

    private void showDatePickerDialogInicio() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance
                (JavaUtil.hoy(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        //String selectedDate = CommonPry.twoDigits(day) + " / " +
                        //        CommonPry.twoDigits(month+1) + " / " + year;
                        finiEvento = JavaUtil.fechaALong(year, month, day);
                        //String selectedDate = CommonPry.formatDateForUi(year,month,day);
                        String selectedDate = getDate(finiEvento);
                        fechaIni.setText(selectedDate);
                        if (!tipoEvento.equals(EVENTO)){
                            fechaFin.setText(selectedDate);
                        }

                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void showDatePickerDialogFin() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance
                (JavaUtil.hoy(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        //String selectedDate = CommonPry.twoDigits(day) + " / " +
                        //        CommonPry.twoDigits(month+1) + " / " + year;
                        ffinEvento = JavaUtil.fechaALong(year, month, day);
                        //String selectedDate = CommonPry.formatDateForUi(year,month,day);
                        String selectedDate = getDate(ffinEvento);
                        fechaFin.setText(selectedDate);
                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialogini(){

        TimePickerFragment newFragment = TimePickerFragment.newInstance
                (JavaUtil.hoy(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        hiniEvento = JavaUtil.horaALong(hourOfDay,minute);
                        String selectedHour = JavaUtil.getTime(hiniEvento);
                        horaIni.setText(selectedHour);

                    }
                });
        newFragment.show(getActivity().getSupportFragmentManager(),"timePicker");

    }

    public void showTimePickerDialogfin(){

        TimePickerFragment newFragment = TimePickerFragment.newInstance
                (JavaUtil.hoy(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        hfinEvento = JavaUtil.horaALong(hourOfDay,minute);
                        String selectedHour = JavaUtil.getTime(hfinEvento);
                        horaFin.setText(selectedHour);

                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(),"timePicker");

    }
}
