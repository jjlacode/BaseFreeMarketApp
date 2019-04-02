package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import jjlacode.com.androidutils.DatePickerFragment;
import jjlacode.com.androidutils.ICFragmentos;
import jjlacode.com.androidutils.ImagenUtil;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.ListaAdaptadorFiltro;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.androidutils.TimePickerFragment;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;

import static jjlacode.com.freelanceproject.utilities.Common.permiso;

public class FragmentCEvento extends Fragment
        implements Common.TiposEvento, Contract.Tablas, JavaUtil.Constantes, Common.Constantes {

    private AppCompatActivity activity;
    private ICFragmentos icFragmentos;
    private Bundle bundle;
    private String namef;
    ImageView imagen;
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
    private long finiEvento;
    private long ffinEvento;
    private long hiniEvento;
    private long hfinEvento;
    private String nombreProyecto;
    private String nombreCliente;


    private String idMulti;
    private ImagenUtil imagenUtil;
    private String path;

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
        btnvolver = vista.findViewById(R.id.btnvolvernevento);
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

        ArrayAdapter<CharSequence> adapterTiposEvento = new ArrayAdapter
                (getContext(),android.R.layout.simple_spinner_item,listaTiposEvento);

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
                    cliRel.setVisibility(View.VISIBLE);
                    proyRel.setVisibility(View.VISIBLE);

                    tipoEvento = listaTiposEvento.get(position);
                    imagen.setVisibility(View.VISIBLE);
                    descipcion.setVisibility(View.VISIBLE);

                    if (idProyecto!=null){

                        //relProy.setVisibility(View.GONE);
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

                }else if (namef.equals(CLIENTE)||namef.equals(PROSPECTO)){
                if (tipoEvento != null) {
                    guardarEvento();
                    bundle = new Bundle();
                    bundle.putSerializable(TABLA_CLIENTE,cliente);
                    bundle.putString("namef", namef);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentUDCliente());
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

        listaProyectos =  QueryDB.queryList(CAMPOS_PROYECTO,null,null);

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

        /*
        if (idProyecto!=null){autoCompleteTextView.setText(proyecto.getString(PROYECTO_NOMBRE));}

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Modelo proyecto = (Modelo) autoCompleteTextView.getAdapter().getItem(position);
                nombreProyecto = proyecto.getString(PROYECTO_NOMBRE);
                idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);
                proyRel.setText(nombreProyecto);
            }

        });

         */

    }

    private void setAdaptadorClientes(final AutoCompleteTextView autoCompleteTextView) {

        listaClientes = QueryDB.queryList(CAMPOS_CLIENTE, null, null);

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

        /*
        if (idCliente!=null){autoCompleteTextView.setText(cliente.getString(CLIENTE_NOMBRE));}

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Modelo cliente = (Modelo) autoCompleteTextView.getAdapter().getItem(position);
                idCliente = cliente.getString(CLIENTE_ID_CLIENTE);
                nombreCliente = cliente.getString(CLIENTE_NOMBRE);
                cliRel.setText(nombreCliente);

            }

        });

         */
    }

    private boolean guardarEvento() {

        ContentValues valores = new ContentValues();

        if (idProyecto!=null){

            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_NOMPROYECTOREL,nombreProyecto);
            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_PROYECTOREL,idProyecto);

        }
        if (idCliente!=null){

            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_NOMCLIENTEREL,nombreCliente);
            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_CLIENTEREL,idCliente);
        }

        if (path!=null){

            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_RUTAFOTO,path);
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
        long hoy = JavaUtil.hoy();
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
            this.activity = (AppCompatActivity) context;
            icFragmentos = (ICFragmentos) this.activity;
        }
   }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void showDatePickerDialogInicio() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance
                (JavaUtil.hoy(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        //String selectedDate = Common.twoDigits(day) + " / " +
                        //        Common.twoDigits(month+1) + " / " + year;
                        finiEvento = JavaUtil.fechaALong(year, month, day);
                        //String selectedDate = Common.formatDateForUi(year,month,day);
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
                        //String selectedDate = Common.twoDigits(day) + " / " +
                        //        Common.twoDigits(month+1) + " / " + year;
                        ffinEvento = JavaUtil.fechaALong(year, month, day);
                        //String selectedDate = Common.formatDateForUi(year,month,day);
                        String selectedDate = JavaUtil.getDate(ffinEvento);
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

    public void mostrarDialogoOpcionesImagen() {

        final CharSequence[] opciones = {"Imagen del proyecto relacionado","Hacer foto desde cámara",
                "Elegir de la galería", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                imagenUtil = new ImagenUtil(getContext());

                if (opciones[which].equals("Hacer foto desde cámara")) {

                    try {
                        startActivityForResult(imagenUtil.takePhotoIntent(), COD_FOTO);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imagenUtil.addToGallery();

                } else if (opciones[which].equals("Elegir de la galería")) {

                    startActivityForResult(imagenUtil.openGalleryIntent(), COD_SELECCIONA);

                }else if (opciones[which].equals("Imagen del proyecto relacionado")) {

                    if (proyecto!=null && proyecto.getString(PROYECTO_RUTAFOTO)!=null) {
                        path = proyecto.getString(PROYECTO_RUTAFOTO);
                        imagen.setImageURI(Uri.parse(path));
                    }

                }else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String photoPath;

        switch (requestCode) {

            case COD_SELECCIONA:
                imagenUtil.setPhotoUri(data.getData());
                photoPath = imagenUtil.getPath();
                try {
                    Bitmap bitmap = ImagenUtil.ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    imagen.setImageBitmap(bitmap);
                    path = photoPath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case COD_FOTO:
                photoPath = imagenUtil.getPhotoPath();
                try {
                    Bitmap bitmap = ImagenUtil.ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    imagen.setImageBitmap(bitmap); //imageView is your ImageView
                    path = photoPath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

        }
        System.out.println("photoPath = " + path);
    }

}
