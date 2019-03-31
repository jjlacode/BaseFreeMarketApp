package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.Arrays;

import jjlacode.com.freelanceproject.adapter.Lista_adaptador;
import jjlacode.com.freelanceproject.interfaces.ICFragmentos;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.model.Modelo;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;
import jjlacode.com.utilidades.Utilidades;

public class FragmentCEvento extends Fragment
        implements Common.TiposEvento, Contract.Tablas, Utilidades.Constantes, Common.Constantes {

    private Activity activity;
    private ICFragmentos icFragmentos;
    private Bundle bundle;
    private String namef;
    ImageView imagen;
    Spinner tiposEvento;
    Spinner proyRel;
    Spinner cliRel;
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
    Button btnsave;
    Button btnvolver;
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
    private ArrayList<Modelo> listaObjProyectos;
    private ArrayList<Modelo> listaobjClientes;
    private int posproy;
    private int poscli;
    private long finiEvento;
    private long ffinEvento;
    private long hiniEvento;
    private long hfinEvento;
    private String nombreProyecto;
    private String nombreCliente;


    private String idMulti;

    public FragmentCEvento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            if (cliente != null) {

                idCliente = cliente.getString(CLIENTE_ID_CLIENTE);

            }
            if (proyecto!=null) {

                idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);

            }

        }

        imagen = vista.findViewById(R.id.imgnevento);
        //if (proyecto!=null && proyecto.getString(PROYECTO_RUTAFOTO)!=null){
        //    imagen.setImageURI(Uri.parse(proyecto.getString(PROYECTO_RUTAFOTO)));
       // }
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
        btnvolver = vista.findViewById(R.id.btnvolvernevento);
        laviso = vista.findViewById(R.id.ltvavisonevento);
        lrep = vista.findViewById(R.id.ltvreptnevento);
        ldrep = vista.findViewById(R.id.ltvdreptnevento);
        btnfini = vista.findViewById(R.id.imgbtnfininevento);
        btnffin = vista.findViewById(R.id.imgbtnffinnevento);
        btnhini = vista.findViewById(R.id.imgbtnhininevento);
        btnhfin = vista.findViewById(R.id.imgbtnhfinnevento);

        /*
        avisoDias.setText("0");
        avisoHoras.setText("0");
        avisoMinutos.setText("0");
        repAnios.setText("0");
        repMeses.setText("0");
        repDias.setText("0");
        */

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
        laviso.setVisibility(View.GONE);
        lrep.setVisibility(View.GONE);
        ldrep.setVisibility(View.GONE);

        final ArrayList<String> listaTiposEvento = new ArrayList<>();
        listaTiposEvento.add("Elija el tipo de evento");
        listaTiposEvento.add(TAREA);
        listaTiposEvento.add(CITA);
        listaTiposEvento.add(EMAIL);
        listaTiposEvento.add(LLAMADA);
        listaTiposEvento.add(EVENTO);

        ArrayAdapter<CharSequence> adapterTiposEvento = new ArrayAdapter
                (getContext(),android.R.layout.simple_spinner_item,listaTiposEvento);

        tiposEvento.setAdapter(adapterTiposEvento);

        tiposEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tipoEvento = null;

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

                if (idCliente!=null){

                    relCli.setVisibility(View.GONE);
                    cliRel.setVisibility(View.VISIBLE);
                }
                if (idProyecto!=null){

                    relProy.setVisibility(View.GONE);
                    proyRel.setVisibility(View.VISIBLE);
                }

                if (position>0) {

                    tipoEvento = listaTiposEvento.get(position);
                    System.out.println("tipoEvento = " + tipoEvento);

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

                }
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (namef.equals(AGENDA)) {
                    if (tipoEvento != null) {
                        guardarEvento();
                        bundle = new Bundle();
                        bundle.putString("namef", namef);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentAgenda());
                        bundle = null;
                    }
                }else if (namef.equals(EVENTO)){
                    if (tipoEvento != null) {
                        guardarEvento();
                        bundle = new Bundle();
                        bundle.putString("namef", namef);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentEvento());
                        bundle = null;
                    }

                }else if (namef.equals(PRESUPUESTO)||namef.equals(PROYECTO)){
                    if (tipoEvento != null) {
                        guardarEvento();
                        bundle = new Bundle();
                        bundle.putSerializable(TABLA_PROYECTO,proyecto);
                        bundle.putString("namef", namef);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentUDProyecto());
                        bundle = null;
                    }

                }
            }
        });

        btnvolver.setOnClickListener(new View.OnClickListener() {
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
        return vista;
    }

    private void setAdaptadorProyectos(Spinner spinner){

        listaObjProyectos =  QueryDB.queryList(CAMPOS_PROYECTO,null,null);
        listaProyectos = new ArrayList<>();
        final String elija = "Elija un Presupuesto o Proyecto relacionado con el evento";
        Modelo proyecto = new Modelo(CAMPOS_PROYECTO);
        proyecto.setCampos(PROYECTO_DESCRIPCION,elija);
        listaProyectos = new ArrayList<>();
        listaProyectos.add(proyecto);
        listaProyectos.addAll(listaObjProyectos);
        System.out.println("proyecto.getCampos() = " + Arrays.toString(proyecto.getCampos()));
        if (idProyecto!=null) {
            for (int i = 1; i < listaProyectos.size(); i++) {

                if (listaProyectos.get(i).getCampos(Contract.Tablas.PROYECTO_ID_PROYECTO).equals(idProyecto)) {

                    posproy = i;
                    break;
                }
            }
        }

        spinner.setAdapter(new Lista_adaptador(getContext(),R.layout.item_list_proyecto,listaProyectos) {
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

                descripcion.setText(entrada.getCampos(Contract.Tablas.PROYECTO_DESCRIPCION));

                if (descripcion.getText().toString().equals(elija)){

                    imagen.setVisibility(View.GONE);
                    nombre.setVisibility(View.GONE);
                    imgcli.setVisibility(View.GONE);
                    imgest.setVisibility(View.GONE);
                    nomcli.setVisibility(View.GONE);
                    estado.setVisibility(View.GONE);

                }else if (entrada.getCampos(Contract.Tablas.PROYECTO_ID_PROYECTO)!=null){

                    imagen.setVisibility(View.VISIBLE);
                    nombre.setVisibility(View.VISIBLE);
                    imgcli.setVisibility(View.VISIBLE);
                    imgest.setVisibility(View.VISIBLE);
                    nomcli.setVisibility(View.VISIBLE);
                    estado.setVisibility(View.VISIBLE);

                    nombre.setText(entrada.getCampos(Contract.Tablas.PROYECTO_NOMBRE));
                    nomcli.setText(entrada.getCampos(Contract.Tablas.CLIENTE_NOMBRE));
                    estado.setText(entrada.getCampos(Contract.Tablas.ESTADO_DESCRIPCION));

                        bar.setProgress(Integer.parseInt(entrada.getCampos
                                (Contract.Tablas.PROYECTO_TOTCOMPLETADO)));

                        long retraso = Long.parseLong(entrada.getCampos
                                (Contract.Tablas.PROYECTO_RETRASO));
                        if (retraso > 3 * Common.DIASLONG) {
                            imgest.setImageResource(R.drawable.alert_box_r);
                        } else if (retraso > Common.DIASLONG) {
                            imgest.setImageResource(R.drawable.alert_box_a);
                        } else {
                            imgest.setImageResource(R.drawable.alert_box_v);
                        }

                    if (entrada.getCampos(Contract.Tablas.PROYECTO_RUTAFOTO) != null) {
                        imagen.setImageURI(Uri.parse(entrada.getCampos
                                (Contract.Tablas.PROYECTO_RUTAFOTO)));
                    }
                    int peso = Integer.parseInt(entrada.getCampos
                            (Contract.Tablas.CLIENTE_PESOTIPOCLI));

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

            }
        });

        if (idProyecto!=null){spinner.setSelection(posproy);}

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                nombreProyecto = listaProyectos.get(position).getString(PROYECTO_NOMBRE);
                idProyecto = listaProyectos.get(position).getString(PROYECTO_ID_PROYECTO);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setAdaptadorClientes(Spinner spinner) {

        listaobjClientes = QueryDB.queryList(CAMPOS_CLIENTE, null, null);
        listaProyectos = new ArrayList<>();
        final String elija = "Elija un Cliente o Prospecto relacionado con el evento";
        Modelo cliente = new Modelo(CAMPOS_CLIENTE);
        cliente.setCampos(CLIENTE_DIRECCION, elija);
        listaClientes = new ArrayList<>();
        listaClientes.add(cliente);
        listaClientes.addAll(listaobjClientes);
        System.out.println("idCliente = " + idCliente);

        if (idCliente != null) {
            for (int i = 1; i < listaClientes.size(); i++) {

                if (listaClientes.get(i).getCampos(CLIENTE_ID_CLIENTE).equals(idCliente)) {

                    poscli = i;
                    break;
                }
            }
        }
        spinner.setAdapter(new Lista_adaptador(getContext(),R.layout.item_list_cliente,listaClientes) {
            @Override
            public void onEntrada(Modelo entrada, View view) {

                ImageView imgcli = view.findViewById(R.id.imgclilcliente);
                TextView nombreCli = view.findViewById(R.id.tvnomclilcliente);
                TextView contactoCli = view.findViewById(R.id.tvcontacclilcliente);
                TextView telefonoCli = view.findViewById(R.id.tvtelclilcliente);
                TextView emailCli = view.findViewById(R.id.tvemailclilcliente);
                TextView dirCli = view.findViewById(R.id.tvdirclilcliente);

                dirCli.setText(entrada.getString(CLIENTE_DIRECCION));

                if (dirCli.getText().toString().equals(elija)) {

                    imgcli.setVisibility(View.GONE);
                    nombreCli.setVisibility(View.GONE);
                    contactoCli.setVisibility(View.GONE);
                    telefonoCli.setVisibility(View.GONE);
                    emailCli.setVisibility(View.GONE);

                }else{

                    imgcli.setVisibility(View.VISIBLE);
                    nombreCli.setVisibility(View.VISIBLE);
                    contactoCli.setVisibility(View.VISIBLE);
                    telefonoCli.setVisibility(View.VISIBLE);
                    emailCli.setVisibility(View.VISIBLE);

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



            }

        });

        if (idCliente!=null){spinner.setSelection(poscli);}

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                idCliente = listaClientes.get(position).getString(CLIENTE_ID_CLIENTE);
                nombreCliente = listaClientes.get(position).getString(CLIENTE_NOMBRE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean guardarEvento() {

        ContentValues valores = new ContentValues();

        if (idProyecto!=null){

            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_NOMPROYECTOREL,nombreProyecto);

        }
        if (idCliente!=null){

            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_NOMCLIENTEREL,nombreCliente);
        }

        switch (tipoEvento){

            case TAREA:
                //QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,0);
                //valores.put(Contract.Tablas.EVENTO_HORAINIEVENTO, "0");
                //valores.put(Contract.Tablas.EVENTO_FECHAFINEVENTO, "0");
                //valores.put(Contract.Tablas.EVENTO_HORAFINEVENTO, "0");
                break;

            case CITA:
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                //valores.put(Contract.Tablas.EVENTO_FECHAFINEVENTO, "0");
                //valores.put(Contract.Tablas.EVENTO_HORAFINEVENTO, "0");
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_LUGAR,lugar.getText().toString());

                if (lugar==null || lugar.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Debe introducir una direccion para la cita",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;

            case EMAIL:
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                //valores.put(Contract.Tablas.EVENTO_FECHAFINEVENTO, "0");
                //valores.put(Contract.Tablas.EVENTO_HORAFINEVENTO, "0");
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_EMAIL,email.getText().toString());
                if (email==null || email.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Debe introducir una direccion de email",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;

            case LLAMADA:
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                //valores.put(Contract.Tablas.EVENTO_FECHAFINEVENTO, "0");
                //valores.put(Contract.Tablas.EVENTO_HORAFINEVENTO, "0");
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_TELEFONO,telefono.getText().toString());

                if (telefono==null || telefono.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Debe introducir un numero de telefono",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;

            case EVENTO:
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAFINEVENTO,ffinEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAFINEVENTO,hfinEvento);
                break;

        }
        QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_DESCRIPCION,descipcion.getText().toString());
        QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_TIPOEVENTO,tipoEvento);
        //valores.put(Contract.Tablas.EVENTO_COMPLETADA,"0");

        if (aviso.isChecked() && !tipoEvento.equals(TAREA)){

            if (avisoDias.getText().toString().equals("")){avisoDias.setText("0");}
            if (avisoHoras.getText().toString().equals("")){avisoHoras.setText("0");}
            if (avisoMinutos.getText().toString().equals("")){avisoMinutos.setText("0");}

            long fechaaviso = (Long.parseLong(avisoMinutos.getText().toString()) * MINUTOSLONG) +
                    (Long.parseLong(avisoHoras.getText().toString()) * HORASLONG) +
                    (Long.parseLong(avisoDias.getText().toString()) * DIASLONG);
            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_AVISO,fechaaviso);


        }

        System.out.println("valores = " + valores);

        Uri uri = QueryDB.insertRegistro(TABLA_EVENTO,valores);

        if (repeticiones.isChecked() && !tipoEvento.equals(TAREA)) {

            generarRepeticiones(uri,valores);

        }
        return true;
    }

    private void generarRepeticiones(Uri uri,ContentValues valores){

        idMulti = Contract.obtenerIdTabla(uri);
        QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_IDMULTI,idMulti);

        QueryDB.updateRegistro(uri, valores);

        long diffecha = ffinEvento-finiEvento;

        if (repAnios.getText().toString().equals("")){repAnios.setText("0");}
        if (repMeses.getText().toString().equals("")){repMeses.setText("0");}
        if (repDias.getText().toString().equals("")){repDias.setText("0");}
        if (drepAnios.getText().toString().equals("")){drepAnios.setText("0");}
        if (drepMeses.getText().toString().equals("")){drepMeses.setText("0");}
        if (drepDias.getText().toString().equals("")){drepDias.setText("0");}

        long offRep = (Long.parseLong(repAnios.getText().toString()) * ANIOSLONG)+
                (Long.parseLong(repMeses.getText().toString()) * MESESLONG)+
                        (Long.parseLong(repDias.getText().toString()) * DIASLONG);

        long duracionRep = (Long.parseLong(drepAnios.getText().toString()) * ANIOSLONG)+
                (Long.parseLong(drepMeses.getText().toString()) * MESESLONG)+
                        (Long.parseLong(drepDias.getText().toString()) * DIASLONG);
        long hoy = Utilidades.hoy();
        if (finiEvento==0){finiEvento = hoy;}
        long fecharep = finiEvento + offRep;

        while (duracionRep+hoy > fecharep){

            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,fecharep);

            if (tipoEvento.equals(EVENTO)) {
                valores.put(Contract.Tablas.EVENTO_FECHAFINEVENTO, String.valueOf(fecharep + diffecha));
            }
            QueryDB.insertRegistro(TABLA_EVENTO,valores);
            fecharep += offRep;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.activity = (Activity) context;
            icFragmentos = (ICFragmentos) this.activity;
        }
   }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void showDatePickerDialogInicio() {
        Common.DatePickerFragment newFragment = Common.DatePickerFragment.newInstance
                (Utilidades.hoy(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        //String selectedDate = Common.twoDigits(day) + " / " +
                        //        Common.twoDigits(month+1) + " / " + year;
                        finiEvento = Utilidades.fechaALong(year, month, day);
                        //String selectedDate = Common.formatDateForUi(year,month,day);
                        String selectedDate = Utilidades.getDate(finiEvento);
                        fechaIni.setText(selectedDate);
                        if (!tipoEvento.equals(EVENTO)){
                            fechaFin.setText(selectedDate);
                        }

                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void showDatePickerDialogFin() {
        Common.DatePickerFragment newFragment = Common.DatePickerFragment.newInstance
                (Utilidades.hoy(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        //String selectedDate = Common.twoDigits(day) + " / " +
                        //        Common.twoDigits(month+1) + " / " + year;
                        ffinEvento = Utilidades.fechaALong(year, month, day);
                        //String selectedDate = Common.formatDateForUi(year,month,day);
                        String selectedDate = Utilidades.getDate(ffinEvento);
                        fechaFin.setText(selectedDate);
                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialogini(){

        Common.TimePickerFragment newFragment = Common.TimePickerFragment.newInstance
                (Utilidades.hoy(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        hiniEvento = Utilidades.horaALong(hourOfDay,minute);
                        String selectedHour = Utilidades.getTime(hiniEvento);
                        horaIni.setText(selectedHour);

                    }
                });
        newFragment.show(getActivity().getSupportFragmentManager(),"timePicker");

    }

    public void showTimePickerDialogfin(){

        Common.TimePickerFragment newFragment = Common.TimePickerFragment.newInstance
                (Utilidades.hoy(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        hfinEvento = Utilidades.horaALong(hourOfDay,minute);
                        String selectedHour = Utilidades.getTime(hfinEvento);
                        horaFin.setText(selectedHour);

                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(),"timePicker");

    }



}
