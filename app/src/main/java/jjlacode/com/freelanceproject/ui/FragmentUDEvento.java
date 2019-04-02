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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import jjlacode.com.androidutils.DatePickerFragment;
import jjlacode.com.androidutils.ICFragmentos;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.ListaAdaptadorFiltro;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.androidutils.TimePickerFragment;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;

import static jjlacode.com.androidutils.JavaUtil.getDate;
import static jjlacode.com.androidutils.JavaUtil.getTime;

public class FragmentUDEvento extends Fragment
        implements Contract.Tablas, JavaUtil.Constantes, Common.Constantes, Common.TiposEvento {

    private String idEvento;
    private String namef;

    private Button btnsave;
    private Button btndelete;
    private Button btnback;

    private AppCompatActivity activity;
    private ICFragmentos icFragmentos;
    private Bundle bundle;
    private Modelo evento;

    ImageView imagen;
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
    TextView tipoEvento;

    ArrayList<Modelo> listaClientes;
    ArrayList<Modelo> listaProyectos;
    String idCliente;
    String idProyecto;
    String nombreProyecto;
    String nombreCliente;
    String tevento;
    private long finiEvento;
    private long ffinEvento;
    private long hiniEvento;
    private long hfinEvento;


    private String idMulti;
    private EditText completa;


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
        if (bundle != null) {

            evento = (Modelo) bundle.getSerializable(TABLA_EVENTO);
            idEvento = evento.getString(EVENTO_ID_EVENTO);
            idMulti = evento.getString(EVENTO_IDMULTI);
            namef = bundle.getString("namef");
            bundle = null;


        }

        imagen = vista.findViewById(R.id.imgudevento);
        if (evento.getCampos(EVENTO_RUTAFOTO)!=null){imagen.setImageURI(Uri.parse(evento.getCampos(EVENTO_RUTAFOTO)));}
        tipoEvento = vista.findViewById(R.id.sptipoudevento);
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
        completa = vista.findViewById(R.id.etcompletadaudevento);

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


        if (namef.equals(AGENDA)){

            tipoEvento.setText(evento.getString(EVENTO_TIPOEVENTO).toUpperCase());
            tevento = evento.getString(EVENTO_TIPOEVENTO);
            if (evento.getString(EVENTO_RUTAFOTO)!=null){
                imagen.setImageURI(Uri.parse(evento.getString(EVENTO_RUTAFOTO)));
            }
            descipcion.setText(evento.getString(EVENTO_DESCRIPCION));

        }

        if (evento.getLong(EVENTO_AVISO)>0) {

            long[] res = JavaUtil.longAddhhmm(evento.getLong(EVENTO_AVISO));

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

        if (idProyecto!=null){

            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_NOMPROYECTOREL,nombreProyecto);
            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_PROYECTOREL,idProyecto);

        }
        if (idCliente!=null){

            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_NOMCLIENTEREL,nombreCliente);
            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_CLIENTEREL,idCliente);
        }

        switch (tevento){

            case TAREA:
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,"0");
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAFINEVENTO,"0");
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,"0");
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAFINEVENTO,"0");
                break;

            case CITA:
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAFINEVENTO,"0");
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAFINEVENTO,"0");
                valores.put(Contract.Tablas.EVENTO_LUGAR,lugar.getText().toString());
                break;

            case EMAIL:
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAFINEVENTO,"0");
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAFINEVENTO,"0");
                valores.put(Contract.Tablas.EVENTO_EMAIL,email.getText().toString());
                break;

            case LLAMADA:
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAFINEVENTO,"0");
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAFINEVENTO,"0");
                valores.put(Contract.Tablas.EVENTO_TELEFONO,telefono.getText().toString());
                break;

            case EVENTO:
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,finiEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAFINEVENTO,ffinEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAINIEVENTO,hiniEvento);
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_HORAFINEVENTO,hfinEvento);
                break;

        }

        QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_DESCRIPCION,descipcion.getText().toString());
        QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_TIPOEVENTO,tevento);
        QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_COMPLETADA,completa.getText().toString());

        if (aviso.isChecked()){

            if (avisoDias.getText().toString().equals("")){avisoDias.setText("0");}
            if (avisoHoras.getText().toString().equals("")){avisoHoras.setText("0");}
            if (avisoMinutos.getText().toString().equals("")){avisoMinutos.setText("0");}

            long fechaaviso = (Long.parseLong(avisoMinutos.getText().toString()) * MINUTOSLONG) +
                    (Long.parseLong(avisoHoras.getText().toString()) * HORASLONG) +
                    (Long.parseLong(avisoDias.getText().toString()) * DIASLONG);
            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_AVISO,String.valueOf(fechaaviso));

        }else{

            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_AVISO,evento.getLong(EVENTO_AVISO));
        }

        QueryDB.updateRegistro(TABLA_EVENTO,idEvento,valores);

        if (repeticiones.isChecked()) {

            generarRepeticiones(valores);
            System.out.println("repeticiones is checked");

        }
    }
    private void generarRepeticiones(ContentValues valores){


        idMulti = idEvento;
        String seleccion = Contract.Tablas.EVENTO_IDMULTI+" = '"+idMulti+ "' AND "+ Contract.Tablas.EVENTO_ID_EVENTO+
                " <> '"+idEvento+"'";
        int res = QueryDB.deteteRegistros(TABLA_EVENTO,seleccion);
        System.out.println("registros rep borrados = " + res);

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
        System.out.println("duracion mayor k = "+ (duracionRep+hoy > fecharep));

        while (duracionRep+hoy > fecharep){

            QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAINIEVENTO,fecharep);
            if (tipoEvento.equals(EVENTO)) {
                QueryDB.putDato(valores,CAMPOS_EVENTO,EVENTO_FECHAFINEVENTO,(fecharep+diffecha));
            }
            QueryDB.insertRegistro(TABLA_EVENTO,valores);
            System.out.println("valores = " + valores);
            fecharep += offRep;
        }
    }

    private void setAdaptadorProyectos(final AutoCompleteTextView autoCompleteTextView){

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

                descripcion.setText(entrada.getCampos(Contract.Tablas.PROYECTO_DESCRIPCION));

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
        });

        /*
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Modelo proyecto = (Modelo) autoCompleteTextView.getAdapter().getItem(position);
                idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);
                nombreProyecto = proyecto.getString(PROYECTO_NOMBRE);
                autoCompleteTextView.setText(nombreProyecto);

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

                   nombreCli.setText(entrada.getCampos(Contract.Tablas.CLIENTE_NOMBRE));
                   contactoCli.setText(entrada.getCampos(Contract.Tablas.CLIENTE_CONTACTO));
                   telefonoCli.setText(entrada.getCampos(Contract.Tablas.CLIENTE_TELEFONO));
                   emailCli.setText(entrada.getCampos(Contract.Tablas.CLIENTE_EMAIL));
                   dirCli.setText(entrada.getCampos(Contract.Tablas.CLIENTE_DIRECCION));


               }

           });

        /*
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Modelo cliente = (Modelo) autoCompleteTextView.getAdapter().getItem(position);
                    idCliente = cliente.getString(CLIENTE_ID_CLIENTE);
                    nombreCliente = cliente.getString(CLIENTE_NOMBRE);
                    autoCompleteTextView.setText(nombreCliente);

                }

            });

         */
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
                        //String selectedDate = Common.twoDigits(day) + " / " +
                        //        Common.twoDigits(month+1) + " / " + year;
                        ffinEvento = JavaUtil.fechaALong(year, month, day);
                        //String selectedDate = Common.formatDateForUi(year,month,day);
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