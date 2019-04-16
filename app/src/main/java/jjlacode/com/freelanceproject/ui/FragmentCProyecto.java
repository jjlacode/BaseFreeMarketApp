package jjlacode.com.freelanceproject.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

import jjlacode.com.androidutils.AppActivity;
import jjlacode.com.androidutils.FragmentC;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.ListaAdaptadorFiltro;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.utilities.CommonPry;


public class FragmentCProyecto extends FragmentC implements CommonPry.Constantes, ContratoPry.Tablas, CommonPry.Estados {

    private TextView titulo;
    private EditText nombreProyecto;
    private EditText descripcionProyecto;
    private AutoCompleteTextView spClienteProyecto;
    private Spinner spEstadoProyecto;
    private ImageButton imagenTipoClienteProyecto;
    private ArrayList<Modelo> listaClientes;
    private ArrayList<String> listaEstados;
    private ArrayList<Modelo> objEstados;
    private String idCliente;
    private int peso;
    private String idEstado;
    private int tipoEstado;
    private int posEstado;
    private long fecha;
    Modelo proyecto;
    private String nombreCliente;

    private static ConsultaBD consulta = new ConsultaBD();

    public FragmentCProyecto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_c_proyecto, container, false);
        // Inflate the layout for this fragment
        imagen = view.findViewById(R.id.imgnpry);
        nombreProyecto = view.findViewById(R.id.etnombrenpry);
        descripcionProyecto = view.findViewById(R.id.etdescnpry);
        spClienteProyecto = view.findViewById(R.id.spclinpry);
        spClienteProyecto.setThreshold(1);
        spEstadoProyecto = view.findViewById(R.id.spestnpry);
        btnsave = view.findViewById(R.id.buttonsavenpry);
        imagenTipoClienteProyecto = view.findViewById(R.id.imgtipoclinpry);
        titulo = view.findViewById(R.id.tvtitnpry);
        fecha = JavaUtil.hoy();

        bundle = getArguments();

        if (bundle!= null){

            if (bundle.getString("namefsub")!= null){

                proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
                idCliente = proyecto.getString(PROYECTO_ID_CLIENTE);
                namef = bundle.getString("namef");
                bundle = null;

            }else {

                namef = bundle.getString("namef");
                bundle = null;

            }
        }

        if (namef.equals(PRESUPUESTO)){

            titulo.setText(NUEVOPRESUP);

        }else {

            titulo.setText(NUEVOPROYECTO);
        }


        if (CommonPry.permiso) {
            imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mostrarDialogoOpcionesImagen();
                }
            });
        }

        setAdaptadorClientes(spClienteProyecto);


        listaObjetosEstados();

        ArrayAdapter<String> adaptadorEstado = new ArrayAdapter<>
                (getContext(),android.R.layout.simple_spinner_item,listaEstados);

        spEstadoProyecto.setAdapter(adaptadorEstado);

        if (namef.equals(PRESUPUESTO)){

            spEstadoProyecto.setSelection(1);
        }

        spEstadoProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position>0){

                    idEstado = objEstados.get(position-1).getString(ESTADO_ID_ESTADO);
                    tipoEstado = objEstados.get(position-1).getInt(ESTADO_TIPOESTADO);
                    posEstado = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registrar();
            }
        });
        imagenTipoClienteProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            mostrarDialogoTipoCliente();
            }
        });

        if (idCliente!=null){
        Modelo cliente = consulta.queryObject(CAMPOS_CLIENTE,idCliente);
        nombreCliente = cliente.getString(CLIENTE_NOMBRE);
        spClienteProyecto.setText(cliente.getString(CLIENTE_NOMBRE));
        }

        spClienteProyecto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Modelo cliente = (Modelo) spClienteProyecto.getAdapter().getItem(position);
                idCliente = cliente.getString(CLIENTE_ID_CLIENTE);
                nombreCliente = cliente.getString(CLIENTE_NOMBRE);
                spClienteProyecto.setText(nombreCliente);
                peso = cliente.getInt(CLIENTE_PESOTIPOCLI);
                if (peso>6){imagenTipoClienteProyecto.setImageResource(R.drawable.clientev);}
                else if (peso>3){imagenTipoClienteProyecto.setImageResource(R.drawable.clientea);}
                else if (peso>0){imagenTipoClienteProyecto.setImageResource(R.drawable.clienter);}
                else {imagenTipoClienteProyecto.setImageResource(R.drawable.cliente);}

            }

        });


        return view;
    }

    @Override
    protected boolean registrar() {


        ContentValues valores = new ContentValues();
        consulta.putDato(valores, CAMPOS_PROYECTO, PROYECTO_NOMBRE, nombreProyecto.getText().toString());
        consulta.putDato(valores, CAMPOS_PROYECTO, PROYECTO_DESCRIPCION, descripcionProyecto.getText().toString());
        consulta.putDato(valores, CAMPOS_PROYECTO, PROYECTO_ID_CLIENTE, idCliente);
        consulta.putDato(valores, CAMPOS_PROYECTO, PROYECTO_ID_ESTADO, idEstado);
        consulta.putDato(valores, CAMPOS_PROYECTO, PROYECTO_FECHAENTRADA, fecha);
        consulta.putDato(valores, CAMPOS_PROYECTO, PROYECTO_RUTAFOTO, path);

        if (idCliente != null) {

            if (idEstado != null) {

                System.out.println("valores = " + valores);
                String idProyecto = consulta.idInsertRegistro(TABLA_PROYECTO, valores);

                Modelo proyecto = consulta.queryObject(CAMPOS_PROYECTO, idProyecto);

                new CommonPry.Calculos.Tareafechas().execute();
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO, proyecto);
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentUDProyecto());
                bundle = null;

            } else {
                Toast.makeText(getContext(), "Debe elegir un estado", Toast.LENGTH_LONG).show();
            }
        } else {
            //Toast.makeText(getContext(), "Debe elegir un cliente", Toast.LENGTH_LONG).show();
            mostrarDialogoTipoCliente();
        }
        return true;
    }

    private void listaObjetosEstados() {

        objEstados = new ArrayList<>();
        String seleccion = null;
        if (namef.equals(PROYECTO)){
            seleccion = ESTADO_TIPOESTADO + " >3";
        }else if (namef.equals(PRESUPUESTO)){
            seleccion = ESTADO_TIPOESTADO + " <4";
        }else if (namef.equals(COBROS)){
            seleccion = ESTADO_TIPOESTADO + " >6";
        }else if (namef.equals(HISTORICO)){
            seleccion = ESTADO_TIPOESTADO + " == 8";
        }

        objEstados = consulta.queryList(CAMPOS_ESTADO,seleccion,null);

        obtenerListaEstados();
    }

    private void obtenerListaEstados() {

        listaEstados = new ArrayList<String>();
        listaEstados.add("Seleccione Estado");

        for (int i=0;i<objEstados.size();i++){

            listaEstados.add(objEstados.get(i).getString(ESTADO_DESCRIPCION));
        }
    }


    private void mostrarDialogoTipoCliente() {

        final CharSequence[] opciones = {"Nuevo cliente","Nuevo prospecto","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Nuevo cliente")){

                    enviarProyectoTemporal(namef, CLIENTE,new FragmentCCliente());

                }else if (opciones[which].equals("Nuevo prospecto")){

                    enviarProyectoTemporal(namef, PROSPECTO,new FragmentCCliente());

                }else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void enviarProyectoTemporal(String namef, String namefsub, Fragment myfragment) {

        Modelo proyecto = new Modelo(CAMPOS_PROYECTO);
        proyecto.setCampos(PROYECTO_ID_ESTADO,idEstado);
        proyecto.setCampos(PROYECTO_NOMBRE,nombreProyecto.getText().toString());
        proyecto.setCampos(PROYECTO_DESCRIPCION,descripcionProyecto.getText().toString());
        proyecto.setCampos(PROYECTO_ID_CLIENTE,idCliente);
        proyecto.setCampos(PROYECTO_FECHAENTRADA,fecha);
        proyecto.setCampos(PROYECTO_RUTAFOTO,path);

        bundle = new Bundle();
        bundle.putSerializable(TABLA_PROYECTO, proyecto);
        bundle.putString("namef", namef);
        bundle.putString("namefsub", namefsub);

        icFragmentos.enviarBundleAFragment(bundle, myfragment);
        bundle = null;
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

}
