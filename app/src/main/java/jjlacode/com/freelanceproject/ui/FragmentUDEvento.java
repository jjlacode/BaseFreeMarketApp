package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;

import jjlacode.com.androidutils.DatePickerFragment;
import jjlacode.com.androidutils.ICFragmentos;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.ListaAdaptador;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.androidutils.TimePickerFragment;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;

public class FragmentUDEvento extends Fragment
        implements Contract.Tablas, JavaUtil.Constantes, Common.Constantes, Common.TiposEvento {

    private String idEvento;
    private String namef;

    private Button btnsave;
    private Button btndelete;
    private Button btnback;

    private Activity activity;
    private ICFragmentos icFragmentos;
    private Bundle bundle;
    private Modelo evento;

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


    private String idMulti;


    public FragmentUDEvento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ud_evento, container, false);

        btnsave = (Button) vista.findViewById(R.id.evento_ud_btn_save);
        btndelete = (Button) vista.findViewById(R.id.evento_ud_btn_del);
        btnback = (Button) vista.findViewById(R.id.evento_ud_btn_back);

        bundle = getArguments();
        evento = (Modelo) bundle.getSerializable(TABLA_EVENTO);

        namef = bundle.getString("namef");
        bundle = null;

        bundle = getArguments();
        if (bundle != null) {

            proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
            cliente = (Modelo) bundle.getSerializable(TABLA_CLIENTE);
            evento = (Modelo) bundle.getSerializable(TABLA_EVENTO);
            namef = bundle.getString("namef");
            bundle = null;
            if (cliente != null) {

                idCliente = cliente.getCampos(Contract.Tablas.CLIENTE_ID_CLIENTE);

            }
            if (proyecto!=null) {

                idProyecto = proyecto.getCampos(Contract.Tablas.PROYECTO_ID_PROYECTO);

            }
            if (evento!=null){

                idProyecto = evento.getCampos(Contract.Tablas.EVENTO_PROYECTOREL);
            }

        }

        imagen = vista.findViewById(R.id.imgudevento);
        if (proyecto!=null){imagen.setImageURI(Uri.parse(proyecto.getCampos(Contract.Tablas.PROYECTO_RUTAFOTO)));}
        tiposEvento = vista.findViewById(R.id.sptipoudevento);
        proyRel = vista.findViewById(R.id.sppryudevento);
        cliRel = vista.findViewById(R.id.spcliudevento);
        descipcion = vista.findViewById(R.id.etdescudevento);
        lugar = vista.findViewById(R.id.etlugarudevento);
        email = vista.findViewById(R.id.etemailudevento);
        telefono = vista.findViewById(R.id.ettelefonoudevento);
        fechaIni = vista.findViewById(R.id.etfechainiudevento);
        horaIni = vista.findViewById(R.id.ethorainiudevento);
        fechaFin = vista.findViewById(R.id.etfechafinudevento);
        horaFin = vista.findViewById(R.id.ethorafinudevento);
        repAnios = vista.findViewById(R.id.etrepaniosudevento);
        repMeses = vista.findViewById(R.id.etrepmesesudevento);
        repDias = vista.findViewById(R.id.etrepdiasudevento);
        drepAnios = vista.findViewById(R.id.etrepdaniosudevento);
        drepMeses = vista.findViewById(R.id.etrepdmesesudevento);
        drepDias = vista.findViewById(R.id.etrepddiasudevento);
        avisoDias = vista.findViewById(R.id.etdiasudevento);
        avisoHoras = vista.findViewById(R.id.ethorasudevento);
        avisoMinutos = vista.findViewById(R.id.etminutosudevento);
        aviso = vista.findViewById(R.id.chavisoudevento);
        repeticiones = vista.findViewById(R.id.chrptudevento);
        relProy = vista.findViewById(R.id.chpryudevento);
        relCli = vista.findViewById(R.id.chcliudevento);
        laviso = vista.findViewById(R.id.ltvavisoudevento);
        lrep = vista.findViewById(R.id.ltvreptudevento);
        ldrep = vista.findViewById(R.id.ltvdreptudevento);
        btnfini = vista.findViewById(R.id.imgbtnfiniudevento);
        btnffin = vista.findViewById(R.id.imgbtnffinudevento);
        btnhini = vista.findViewById(R.id.imgbtnhiniudevento);
        btnhfin = vista.findViewById(R.id.imgbtnhfinudevento);

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
        proyRel.setVisibility(View.GONE);
        cliRel.setVisibility(View.GONE);

        if (namef.equals(AGENDA)){

            //evento = QueryDB.queryObject(Contract.Columnas.EVENTO, idEvento);
            tipoEvento = evento.getCampos(Contract.Tablas.EVENTO_TIPOEVENTO);
            System.out.println("tipoEvento = " + tipoEvento);
        }

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

        if (tipoEvento!=null) {

            switch (tipoEvento) {

                case TAREA:
                    tiposEvento.setSelection(1);
                    break;
                case CITA:
                    tiposEvento.setSelection(2);
                    break;
                case EMAIL:
                    tiposEvento.setSelection(3);
                    break;
                case LLAMADA:
                    tiposEvento.setSelection(4);
                    break;
                case EVENTO:
                    tiposEvento.setSelection(5);
                    break;
            }
        }

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

                if (position>0) {

                    tipoEvento = listaTiposEvento.get(position);
                    System.out.println("tipoEvento = " + tipoEvento);
                    descipcion.setText(evento.getCampos(Contract.Tablas.EVENTO_DESCRIPCION));

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
        if (idCliente!=null){cliRel.setSelection(poscli);}

        setAdaptadorProyectos(proyRel);
        if (idProyecto!=null){proyRel.setSelection(posproy);}

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

                guardarEvento();

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

        btndelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                borraEvento();

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

        return vista;
    }


    public void borraEvento() {

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

                    QueryDB.deleteRegistro(TABLA_EVENTO,idEvento);

                } else if (opciones[which].equals("Borrar este y repeticiones")) {

                    idMulti = idEvento;
                    String seleccion = Contract.Tablas.EVENTO_IDMULTI + " = '" + idMulti + "'";
                    getActivity().getContentResolver().delete
                            (Contract.obtenerUriContenido(TABLA_EVENTO), seleccion, null);

                } else if (opciones[which].equals("Borrar sólo repeticiones")) {

                    idMulti = idEvento;
                    String seleccion = Contract.Tablas.EVENTO_IDMULTI + " = '" + idMulti + "' AND " + Contract.Tablas.EVENTO_ID_EVENTO +
                            " <> '" + idEvento + "'";
                    getActivity().getContentResolver().delete
                            (Contract.obtenerUriContenido(TABLA_EVENTO), seleccion, null);

                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void guardarEvento() {

        ContentResolver resolver = getActivity().getContentResolver();

        ContentValues valores = new ContentValues();

        switch (tipoEvento){

            case TAREA:
                valores.put(Contract.Tablas.EVENTO_FECHAINIEVENTO, "0");
                valores.put(Contract.Tablas.EVENTO_HORAINIEVENTO, "0");
                valores.put(Contract.Tablas.EVENTO_FECHAFINEVENTO, "0");
                valores.put(Contract.Tablas.EVENTO_HORAFINEVENTO, "0");
                break;

            case CITA:
                valores.put(Contract.Tablas.EVENTO_FECHAINIEVENTO, String.valueOf(finiEvento));
                valores.put(Contract.Tablas.EVENTO_HORAINIEVENTO, String.valueOf(hiniEvento));
                valores.put(Contract.Tablas.EVENTO_FECHAFINEVENTO, "0");
                valores.put(Contract.Tablas.EVENTO_HORAFINEVENTO, "0");
                valores.put(Contract.Tablas.EVENTO_LUGAR,lugar.getText().toString());
                break;

            case EMAIL:
                valores.put(Contract.Tablas.EVENTO_FECHAINIEVENTO, String.valueOf(finiEvento));
                valores.put(Contract.Tablas.EVENTO_HORAINIEVENTO, String.valueOf(hiniEvento));
                valores.put(Contract.Tablas.EVENTO_FECHAFINEVENTO, "0");
                valores.put(Contract.Tablas.EVENTO_HORAFINEVENTO, "0");
                valores.put(Contract.Tablas.EVENTO_EMAIL,email.getText().toString());
                break;

            case LLAMADA:
                valores.put(Contract.Tablas.EVENTO_FECHAINIEVENTO, String.valueOf(finiEvento));
                valores.put(Contract.Tablas.EVENTO_HORAINIEVENTO, String.valueOf(hiniEvento));
                valores.put(Contract.Tablas.EVENTO_FECHAFINEVENTO, "0");
                valores.put(Contract.Tablas.EVENTO_HORAFINEVENTO, "0");
                valores.put(Contract.Tablas.EVENTO_TELEFONO,telefono.getText().toString());
                break;

            case EVENTO:
                valores.put(Contract.Tablas.EVENTO_FECHAINIEVENTO, String.valueOf(finiEvento));
                valores.put(Contract.Tablas.EVENTO_HORAINIEVENTO, String.valueOf(hiniEvento));
                valores.put(Contract.Tablas.EVENTO_FECHAFINEVENTO, String.valueOf(ffinEvento));
                valores.put(Contract.Tablas.EVENTO_HORAFINEVENTO, String.valueOf(hfinEvento));
                break;

        }

        valores.put(Contract.Tablas.EVENTO_DESCRIPCION,descipcion.getText().toString());
        valores.put(Contract.Tablas.EVENTO_TIPOEVENTO,tipoEvento);
        valores.put(Contract.Tablas.EVENTO_COMPLETADA,"0");
        valores.put(Contract.Tablas.EVENTO_PROYECTOREL,idProyecto);

        if (aviso.isChecked()){

            if (avisoDias.getText().toString().equals("")){avisoDias.setText("0");}
            if (avisoHoras.getText().toString().equals("")){avisoHoras.setText("0");}
            if (avisoMinutos.getText().toString().equals("")){avisoMinutos.setText("0");}

            long fechaaviso = (Long.parseLong(avisoMinutos.getText().toString()) * MINUTOSLONG) +
                    (Long.parseLong(avisoHoras.getText().toString()) * HORASLONG) +
                    (Long.parseLong(avisoDias.getText().toString()) * DIASLONG);
            valores.put(Contract.Tablas.EVENTO_AVISO,String.valueOf(fechaaviso));

        }

        String seleccion = Contract.Tablas.EVENTO_ID_EVENTO+" = '"+idEvento+"'";

        resolver.update(Contract.obtenerUriContenido(TABLA_EVENTO),valores,seleccion,null);

        if (repeticiones.isChecked()) {

            generarRepeticiones(valores);

        }
    }
    private void generarRepeticiones(ContentValues valores){

        ContentResolver resolver = getActivity().getContentResolver();

        idMulti = idEvento;
        String seleccion = Contract.Tablas.EVENTO_IDMULTI+" = '"+idMulti+ "' AND "+ Contract.Tablas.EVENTO_ID_EVENTO+
                " <> '"+idEvento+"'";
        resolver.delete(Contract.obtenerUriContenido(TABLA_EVENTO),seleccion,null);

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

            valores.put(Contract.Tablas.EVENTO_FECHAINIEVENTO,String.valueOf(fecharep));
            if (tipoEvento.equals(EVENTO)) {
                valores.put(Contract.Tablas.EVENTO_FECHAFINEVENTO, String.valueOf(fecharep + diffecha));
            }            resolver.insert(Contract.obtenerUriContenido(TABLA_EVENTO),valores);
            fecharep += offRep;
        }
    }

    private void setAdaptadorProyectos(Spinner spinner){

        listaObjProyectos =  QueryDB.queryList(CAMPOS_PROYECTO,null,null);
        listaProyectos = new ArrayList<>();
        final String elija = "Elija un Presupuesto o Proyecto relacionado con el evento";
        Modelo proyecto = new Modelo(CAMPOS_PROYECTO);
        proyecto.setCampos(Contract.Tablas.PROYECTO_DESCRIPCION,elija);
        listaProyectos = new ArrayList<>();
        listaProyectos.add(proyecto);
        listaProyectos.addAll(listaObjProyectos);
        System.out.println("idProyecto = " + idProyecto);

        if (idProyecto!=null) {
            for (int i = 0; i < listaProyectos.size(); i++) {

                if (listaProyectos.get(i).getCampos(Contract.Tablas.PROYECTO_ID_PROYECTO).equals(idProyecto)) {

                    posproy = i;
                    break;
                }
            }
        }

        spinner.setAdapter(new ListaAdaptador(getContext(),R.layout.item_list_proyecto,listaProyectos) {
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                idProyecto = listaProyectos.get(position).getCampos(Contract.Tablas.PROYECTO_ID_PROYECTO);

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
        cliente.setCampos(Contract.Tablas.CLIENTE_DIRECCION, elija);
        listaClientes = new ArrayList<>();
        listaClientes.add(cliente);
        listaClientes.addAll(listaobjClientes);
        System.out.println("idCliente = " + idCliente);

        if (idCliente != null) {
            for (int i = 0; i < listaClientes.size(); i++) {

                if (listaClientes.get(i).getCampos(Contract.Tablas.CLIENTE_ID_CLIENTE).equals(idCliente)) {

                    poscli = i;
                    break;
                }
            }
        }
        spinner.setAdapter(new ListaAdaptador(getContext(),R.layout.item_list_cliente,listaClientes) {
               @Override
               public void onEntrada(Modelo entrada, View view) {

                   ImageView imgcli = view.findViewById(R.id.imgclilcliente);
                   TextView nombreCli = view.findViewById(R.id.tvnomclilcliente);
                   TextView contactoCli = view.findViewById(R.id.tvcontacclilcliente);
                   TextView telefonoCli = view.findViewById(R.id.tvtelclilcliente);
                   TextView emailCli = view.findViewById(R.id.tvemailclilcliente);
                   TextView dirCli = view.findViewById(R.id.tvdirclilcliente);

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

                   nombreCli.setText(entrada.getCampos(Contract.Tablas.CLIENTE_NOMBRE));
                   contactoCli.setText(entrada.getCampos(Contract.Tablas.CLIENTE_CONTACTO));
                   telefonoCli.setText(entrada.getCampos(Contract.Tablas.CLIENTE_TELEFONO));
                   emailCli.setText(entrada.getCampos(Contract.Tablas.CLIENTE_EMAIL));
                   dirCli.setText(entrada.getCampos(Contract.Tablas.CLIENTE_DIRECCION));


               }

           });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    idCliente = listaClientes.get(position).getCampos(Contract.Tablas.CLIENTE_ID_CLIENTE);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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

}