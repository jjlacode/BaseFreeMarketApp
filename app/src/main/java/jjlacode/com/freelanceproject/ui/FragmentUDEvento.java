package jjlacode.com.freelanceproject.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;

import jjlacode.com.androidutils.AppActivity;
import jjlacode.com.androidutils.DatePickerFragment;
import jjlacode.com.androidutils.FragmentUD;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.ListaAdaptadorFiltro;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.androidutils.TimePickerFragment;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.utilities.CommonPry;

import static jjlacode.com.androidutils.JavaUtil.getDate;
import static jjlacode.com.androidutils.JavaUtil.getTime;
import static jjlacode.com.freelanceproject.utilities.CommonPry.permiso;

public class FragmentUDEvento extends FragmentUD
        implements ContratoPry.Tablas, JavaUtil.Constantes, CommonPry.Constantes, CommonPry.TiposEvento {

    private String idEvento;
    private Modelo evento;
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

    private static ConsultaBD consulta = new ConsultaBD();
    public FragmentUDEvento() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ud_evento, container, false);

        btnsave = view.findViewById(R.id.evento_ud_btn_save);
        btndelete = view.findViewById(R.id.evento_ud_btn_del);
        btnback = view.findViewById(R.id.evento_ud_btn_back);

        bundle = getArguments();
        if (bundle != null) {

            evento = (Modelo) bundle.getSerializable(TABLA_EVENTO);
            idEvento = evento.getString(EVENTO_ID_EVENTO);
            idMulti = evento.getString(EVENTO_IDMULTI);
            namef = bundle.getString("namef");
            bundle = null;


        }

        imagen = view.findViewById(R.id.imgudevento);
        if (evento.getCampos(EVENTO_RUTAFOTO)!=null){imagen.setImageURI(Uri.parse(evento.getCampos(EVENTO_RUTAFOTO)));}
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
        CheckBox relProy = view.findViewById(R.id.chpryudevento);
        CheckBox relCli = view.findViewById(R.id.chcliudevento);
        laviso = view.findViewById(R.id.ltvavisoudevento);
        lrep = view.findViewById(R.id.ltvreptudevento);
        ldrep = view.findViewById(R.id.ltvdreptudevento);
        ImageButton btnfini = view.findViewById(R.id.imgbtnfiniudevento);
        ImageButton btnffin = view.findViewById(R.id.imgbtnffinudevento);
        ImageButton btnhini = view.findViewById(R.id.imgbtnhiniudevento);
        ImageButton btnhfin = view.findViewById(R.id.imgbtnhfinudevento);
        completa = view.findViewById(R.id.etcompletadaudevento);

        setAdaptadorClientes(cliRel);

        setAdaptadorProyectos(proyRel);

        completa.setText(evento.getString(EVENTO_COMPLETADA));


        if (evento.getString(EVENTO_PROYECTOREL)!=null){

            idProyecto = evento.getString(EVENTO_PROYECTOREL);
            nombreProyecto = evento.getString(EVENTO_NOMPROYECTOREL);
            proyRel.setText(nombreProyecto);
            relProy.setChecked(true);
            idCliente = evento.getString(EVENTO_CLIENTEREL);
            nombreCliente = evento.getString(EVENTO_NOMCLIENTEREL);
            cliRel.setText(nombreCliente);
            relCli.setChecked(true);

        }else if (evento.getString(EVENTO_CLIENTEREL)!=null){

            idCliente = evento.getString(EVENTO_CLIENTEREL);
            nombreCliente = evento.getString(EVENTO_NOMCLIENTEREL);
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

            tipoEvento.setText(evento.getString(EVENTO_TIPOEVENTO).toUpperCase());
            tevento = evento.getString(EVENTO_TIPOEVENTO);
            if (evento.getString(EVENTO_RUTAFOTO)!=null){
                imagen.setImageURI(Uri.parse(evento.getString(EVENTO_RUTAFOTO)));
            }
            descipcion.setText(evento.getString(EVENTO_DESCRIPCION));

        if (evento.getLong(EVENTO_AVISO)>0) {

            long[] res = JavaUtil.longA_ddhhmm(evento.getLong(EVENTO_AVISO));

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
                            lugar.setText(evento.getString(EVENTO_LUGAR));
                            finiEvento = evento.getLong(EVENTO_FECHAINIEVENTO);
                            fechaIni.setText(getDate(finiEvento));
                            hiniEvento = evento.getLong(EVENTO_HORAINIEVENTO);
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
                            email.setText(evento.getString(EVENTO_EMAIL));
                            finiEvento = evento.getLong(EVENTO_FECHAINIEVENTO);
                            fechaIni.setText(getDate(finiEvento));
                            hiniEvento = evento.getLong(EVENTO_HORAINIEVENTO);
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
                            telefono.setText(evento.getString(EVENTO_TELEFONO));
                            finiEvento = evento.getLong(EVENTO_FECHAINIEVENTO);
                            fechaIni.setText(getDate(finiEvento));
                            hiniEvento = evento.getLong(EVENTO_HORAINIEVENTO);
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
                            finiEvento = evento.getLong(EVENTO_FECHAINIEVENTO);
                            fechaIni.setText(getDate(finiEvento));
                            hiniEvento = evento.getLong(EVENTO_HORAINIEVENTO);
                            horaIni.setText(getTime(hiniEvento));
                            ffinEvento = evento.getLong(EVENTO_FECHAFINEVENTO);
                            fechaFin.setText(getDate(ffinEvento));
                            hfinEvento = evento.getLong(EVENTO_HORAFINEVENTO);
                            horaFin.setText(getTime(hfinEvento));
                            break;

                    }




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



                if (permiso) {
                    imagen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mostrarDialogoOpcionesImagen();
                        }
                    });
                }



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

        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                update();

                cambiarFragment();

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                delete();


            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cambiarFragment();
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

        return view;
    }


    @Override
    protected void delete() {

        mostrarDialogoBorrarRep();

    }

    private void mostrarDialogoBorrarRep() {

        final CharSequence[] opciones = {"Borrar sólo este evento", "Borrar este y repeticiones", "Borrar sólo repeticiones", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Borrar sólo este evento")) {

                    consulta.deleteRegistro(TABLA_EVENTO,idEvento);
                    cambiarFragment();

                } else if (opciones[which].equals("Borrar este y repeticiones")) {

                    idMulti = evento.getString(EVENTO_IDMULTI);
                    String seleccion = EVENTO_IDMULTI + " = '" + idMulti + "'";
                    consulta.deteteRegistros(TABLA_EVENTO,seleccion);
                    cambiarFragment();

                } else if (opciones[which].equals("Borrar sólo repeticiones")) {

                    idMulti = evento.getString(EVENTO_IDMULTI);
                    String seleccion = EVENTO_IDMULTI + " = '" + idMulti +
                            "' AND " + EVENTO_ID_EVENTO + " <> '" + idEvento + "'";
                    consulta.deteteRegistros(TABLA_EVENTO,seleccion);
                    cambiarFragment();

                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void update() {

        ContentValues valores = new ContentValues();

        if (idProyecto!=null){

            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_NOMPROYECTOREL,nombreProyecto);
            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_PROYECTOREL,idProyecto);

        }
        if (idCliente!=null){

            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_NOMCLIENTEREL,nombreCliente);
            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_CLIENTEREL,idCliente);
        }

        switch (tevento){

            case TAREA:
                break;

            case CITA:
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_LUGAR,lugar.getText().toString());
                break;

            case EMAIL:
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_EMAIL,email.getText().toString());
                break;

            case LLAMADA:
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_TELEFONO,telefono.getText().toString());
                break;

            case EVENTO:
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAFINEVENTO,ffinEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAFINEVENTO,hfinEvento);
                break;

        }

        consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_DESCRIPCION,descipcion.getText().toString());
        consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_TIPOEVENTO,tevento);
        consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_COMPLETADA,JavaUtil.comprobarInteger(completa.getText().toString()));
        consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_RUTAFOTO,path);

        if (aviso.isChecked()){


            long fechaaviso = (JavaUtil.comprobarLong(avisoMinutos.getText().toString()) * MINUTOSLONG) +
                    (JavaUtil.comprobarLong(avisoHoras.getText().toString()) * HORASLONG) +
                    (JavaUtil.comprobarLong(avisoDias.getText().toString()) * DIASLONG);
            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_AVISO,String.valueOf(fechaaviso));

        }else{

            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_AVISO,evento.getLong(EVENTO_AVISO));
        }

        consulta.updateRegistro(TABLA_EVENTO,idEvento,valores);

        if (repeticiones.isChecked()) {

            generarRepeticiones(valores);
            System.out.println("repeticiones is checked");

        }
    }
    private void generarRepeticiones(ContentValues valores){

        idMulti = evento.getString(EVENTO_IDMULTI);
        System.out.println("idMulti = " + idMulti);
        String seleccion = ContratoPry.Tablas.EVENTO_IDMULTI+" = '"+idMulti+
                "' AND "+ ContratoPry.Tablas.EVENTO_ID_EVENTO+
                " <> '"+idEvento+"'";
        int res = consulta.deteteRegistros(TABLA_EVENTO,seleccion);

        long diffecha = ffinEvento-finiEvento;

        long offRep = (JavaUtil.comprobarLong(repAnios.getText().toString()) * ANIOSLONG)+
                (JavaUtil.comprobarLong(repMeses.getText().toString()) * MESESLONG)+
                        (JavaUtil.comprobarLong(repDias.getText().toString()) * DIASLONG);

        long duracionRep = (JavaUtil.comprobarLong(drepAnios.getText().toString()) * ANIOSLONG)+
                (JavaUtil.comprobarLong(drepMeses.getText().toString()) * MESESLONG)+
                        (JavaUtil.comprobarLong(drepDias.getText().toString()) * DIASLONG);
        long hoy = JavaUtil.hoy();
        if (finiEvento==0){finiEvento = hoy;}
        long fecharep = finiEvento + offRep;

        while (duracionRep+hoy > fecharep){

            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,fecharep);
            if (tipoEvento.equals(EVENTO)) {
                consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAFINEVENTO,(fecharep+diffecha));
            }
            consulta.insertRegistro(TABLA_EVENTO,valores);
            fecharep += offRep;
        }
    }

    private void cambiarFragment(){

        if (namef.equals(AGENDA)) {
            if (tipoEvento != null) {
                bundle = new Bundle();
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentAgenda());
                bundle = null;
            }
        }else if (namef.equals(EVENTO)) {
            if (tipoEvento != null) {
                bundle = new Bundle();
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentEvento());
                bundle = null;
            }
        }
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