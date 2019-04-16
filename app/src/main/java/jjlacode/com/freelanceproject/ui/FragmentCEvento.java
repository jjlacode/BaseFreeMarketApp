package jjlacode.com.freelanceproject.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.ArrayList;

import jjlacode.com.androidutils.DatePickerFragment;
import jjlacode.com.androidutils.FragmentC;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.ListaAdaptadorFiltro;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.androidutils.TimePickerFragment;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.utilities.CommonPry;

import static jjlacode.com.freelanceproject.utilities.CommonPry.permiso;

public class FragmentCEvento extends FragmentC
        implements CommonPry.TiposEvento, ContratoPry.Tablas, CommonPry.Constantes {

    Spinner tiposEvento;
    AutoCompleteTextView proyRel;
    AutoCompleteTextView cliRel;
    EditText descipcion;
    EditText lugar;
    EditText telefono;
    EditText email;
    TextView fechaIni;
    TextView fechaFin;
    TextView horaIni;
    TextView horaFin;
    EditText repAnios;
    EditText repMeses;
    EditText repDias;
    EditText drepAnios;
    EditText drepMeses;
    EditText drepDias;
    EditText avisoMinutos;
    EditText avisoHoras;
    EditText avisoDias;
    CheckBox aviso;
    CheckBox repeticiones;
    CheckBox relProy;
    CheckBox relCli;
    TextView laviso;
    TextView lrep;
    TextView ldrep;
    ImageButton btnfini;
    ImageButton btnffin;
    ImageButton btnhini;
    ImageButton btnhfin;

    String tipoEvento;
    ArrayList<Modelo> listaClientes;
    ArrayList<Modelo> listaProyectos;
    Modelo proyecto;
    Modelo cliente;
    String idCliente;
    String idProyecto;
    private long finiEvento;
    private long ffinEvento;
    private long hiniEvento;
    private long hfinEvento;
    private String nombreProyecto;
    private String nombreCliente;


    private String idMulti;

    private static ConsultaBD consulta = new ConsultaBD();

    public FragmentCEvento() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_c_evento, container, false);

        bundle = getArguments();
        if (bundle != null) {

            proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
            cliente = (Modelo) bundle.getSerializable(TABLA_CLIENTE);
            namef = bundle.getString("namef");
            bundle = null;


        }

        imagen = vista.findViewById(R.id.imgnevento);
        if (proyecto!=null && proyecto.getString(PROYECTO_RUTAFOTO)!=null){
            path = proyecto.getString(PROYECTO_RUTAFOTO);
            imagen.setImageURI(Uri.parse(path));
        }
        tiposEvento = vista.findViewById(R.id.sptiponevento);
        proyRel = vista.findViewById(R.id.spprynevento);
        cliRel = vista.findViewById(R.id.spclinevento);
        descipcion = vista.findViewById(R.id.etdescnevento);
        lugar = vista.findViewById(R.id.etlugarnevento);
        email = vista.findViewById(R.id.etemailnevento);
        telefono = vista.findViewById(R.id.ettelefononevento);
        fechaIni = vista.findViewById(R.id.etfechaininevento);
        horaIni = vista.findViewById(R.id.ethoraininevento);
        fechaFin = vista.findViewById(R.id.etfechafinnevento);
        horaFin = vista.findViewById(R.id.ethorafinnevento);
        repAnios = vista.findViewById(R.id.etrepaniosnevento);
        repMeses = vista.findViewById(R.id.etrepmesesnevento);
        repDias = vista.findViewById(R.id.etrepdiasnevento);
        drepAnios = vista.findViewById(R.id.etrepdaniosnevento);
        drepMeses = vista.findViewById(R.id.etrepdmesesnevento);
        drepDias = vista.findViewById(R.id.etrepddiasnevento);
        avisoDias = vista.findViewById(R.id.etdiasnevento);
        avisoHoras = vista.findViewById(R.id.ethorasnevento);
        avisoMinutos = vista.findViewById(R.id.etminutosnevento);
        aviso = vista.findViewById(R.id.chavisonevento);
        repeticiones = vista.findViewById(R.id.chrptnevento);
        relProy = vista.findViewById(R.id.chprynevento);
        relCli = vista.findViewById(R.id.chclinevento);
        btnsave = vista.findViewById(R.id.btnsavenevento);
        btnback = vista.findViewById(R.id.btnvolvernevento);
        laviso = vista.findViewById(R.id.ltvavisonevento);
        lrep = vista.findViewById(R.id.ltvreptnevento);
        ldrep = vista.findViewById(R.id.ltvdreptnevento);
        btnfini = vista.findViewById(R.id.imgbtnfininevento);
        btnffin = vista.findViewById(R.id.imgbtnffinnevento);
        btnhini = vista.findViewById(R.id.imgbtnhininevento);
        btnhfin = vista.findViewById(R.id.imgbtnhfinnevento);

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

        final ArrayList<String> listaTiposEvento = new ArrayList<>();
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

                tipoEvento = null;

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

                    tipoEvento = listaTiposEvento.get(position);
                    imagen.setVisibility(View.VISIBLE);
                    descipcion.setVisibility(View.VISIBLE);

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


                    switch (tipoEvento){

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

        setAdaptadorClientes(cliRel);

        setAdaptadorProyectos(proyRel);

        aviso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    avisoDias.setVisibility(View.VISIBLE);
                    avisoHoras.setVisibility(View.VISIBLE);
                    avisoMinutos.setVisibility(View.VISIBLE);
                    laviso.setVisibility(View.VISIBLE);

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

                }
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (namef.equals(AGENDA)) {
                    if (tipoEvento != null) {
                        registrar();
                        bundle = new Bundle();
                        bundle.putString("namef", namef);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentAgenda());
                        bundle = null;
                    }
                }else if (namef.equals(EVENTO)){
                    if (tipoEvento != null) {
                        registrar();
                        bundle = new Bundle();
                        bundle.putString("namef", namef);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentEvento());
                        bundle = null;
                    }

                }else if (namef.equals(PRESUPUESTO)||namef.equals(PROYECTO)){
                    if (tipoEvento != null) {
                        registrar();
                        bundle = new Bundle();
                        bundle.putSerializable(TABLA_PROYECTO,proyecto);
                        bundle.putString("namef", namef);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentUDProyecto());
                        bundle = null;
                    }

                }else if (namef.equals(CLIENTE)||namef.equals(PROSPECTO)){
                if (tipoEvento != null) {
                    registrar();
                    bundle = new Bundle();
                    bundle.putSerializable(TABLA_CLIENTE,cliente);
                    bundle.putString("namef", namef);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentUDCliente());
                    bundle = null;
                }

            }
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });

        if (idProyecto!=null){proyRel.setText(proyecto.getString(PROYECTO_NOMBRE));}

        proyRel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Modelo proyecto = (Modelo) proyRel.getAdapter().getItem(position);
                nombreProyecto = proyecto.getString(PROYECTO_NOMBRE);
                idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);
                proyRel.setText(nombreProyecto);
                cliRel.setText(proyecto.getString(PROYECTO_CLIENTE_NOMBRE));

            }

        });

        if (idCliente!=null){cliRel.setText(cliente.getString(CLIENTE_NOMBRE));}

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

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (permiso) {
                    mostrarDialogoOpcionesImagen();
                }

            }
        });

        return vista;
    }

    private void setAdaptadorProyectos(AutoCompleteTextView autoCompleteTextView){

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

                descripcion.setText(entrada.getString(PROYECTO_DESCRIPCION));


                    nombre.setText(entrada.getString(PROYECTO_NOMBRE));
                    nomcli.setText(entrada.getString(CLIENTE_NOMBRE));
                    estado.setText(entrada.getString(ESTADO_DESCRIPCION));

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

                    if (entrada.getString(PROYECTO_RUTAFOTO) != null) {
                        imagen.setImageURI(Uri.parse(entrada.getString(PROYECTO_RUTAFOTO)));
                    }
                    int peso = Integer.parseInt(entrada.getString(CLIENTE_PESOTIPOCLI));

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

    private void setAdaptadorClientes(AutoCompleteTextView autoCompleteTextView) {

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

                dirCli.setText(entrada.getString(CLIENTE_DIRECCION));


                    int peso = entrada.getInt
                            (CLIENTE_PESOTIPOCLI);

                    if (peso > 6) {
                        imgcli.setImageResource(R.drawable.clientev);
                    } else if (peso > 3) {
                        imgcli.setImageResource(R.drawable.clientea);
                    } else if (peso > 0) {
                        imgcli.setImageResource(R.drawable.clienter);
                    } else {
                        imgcli.setImageResource(R.drawable.cliente);
                    }

                    nombreCli.setText(entrada.getString(CLIENTE_NOMBRE));
                    contactoCli.setText(entrada.getString(CLIENTE_CONTACTO));
                    telefonoCli.setText(entrada.getString(CLIENTE_TELEFONO));
                    emailCli.setText(entrada.getString(CLIENTE_EMAIL));

            }

        });

    }

    protected boolean registrar() {

        ContentValues valores = new ContentValues();

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

        switch (tipoEvento){

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
        consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_TIPOEVENTO,tipoEvento);

        if (aviso.isChecked() && !tipoEvento.equals(TAREA)){

            long fechaaviso = (JavaUtil.comprobarLong(avisoMinutos.getText().toString()) * MINUTOSLONG) +
                    (JavaUtil.comprobarLong(avisoHoras.getText().toString()) * HORASLONG) +
                    (JavaUtil.comprobarLong(avisoDias.getText().toString()) * DIASLONG);
            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_AVISO,fechaaviso);


        }

        System.out.println("valores = " + valores);

        idMulti = consulta.idInsertRegistro(TABLA_EVENTO,valores);

        if (repeticiones.isChecked() && !tipoEvento.equals(TAREA)) {

            generarRepeticiones(valores);

        }
        return true;
    }

    private void generarRepeticiones(ContentValues valores){

        consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_IDMULTI,idMulti);

        consulta.updateRegistro(TABLA_EVENTO,idMulti, valores);

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
        System.out.println("duracion mayor k = "+ (duracionRep+hoy > fecharep));
        while (duracionRep+hoy > fecharep){

            consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,fecharep);

            if (tipoEvento.equals(EVENTO)) {
                valores.put(ContratoPry.Tablas.EVENTO_FECHAFINEVENTO, String.valueOf(fecharep + diffecha));
            }
            consulta.insertRegistro(TABLA_EVENTO,valores);
            fecharep += offRep;
        }
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
                        String selectedDate = JavaUtil.getDate(finiEvento);
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
                        String selectedDate = JavaUtil.getDate(ffinEvento);
                        fechaFin.setText(selectedDate);
                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void showTimePickerDialogini(){

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

    private void showTimePickerDialogfin(){

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
