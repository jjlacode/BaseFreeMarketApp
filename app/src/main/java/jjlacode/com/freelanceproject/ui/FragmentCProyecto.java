package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import jjlacode.com.freelanceproject.interfaces.ICFragmentos;
import jjlacode.com.freelanceproject.model.Modelo;
import jjlacode.com.freelanceproject.sqlite.Contract;

import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.utilities.ImagenUtil;
import jjlacode.com.utilidades.Utilidades;


public class FragmentCProyecto extends Fragment implements Common.Constantes, Contract.Tablas, Common.Estados {

    private String namef;

    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    private static final String CARPETA_PRINCIPAL = "misImagenesAPP/";
    private static final String CARPETA_IMAGEN = "freelanceProyect";
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL+CARPETA_IMAGEN;
    private String path;

    File fileImagen;
    Bitmap bitmap;
    View vista;
    ImageView imagenProyecto;
    TextView titulo;
    EditText nombreProyecto;
    EditText descripcionProyecto;
    Spinner spClienteProyecto;
    Spinner spEstadoProyecto;
    Button btnguardarProyecto;
    ImageButton imagenTipoClienteProyecto;
    ArrayList<String> listaClientes;
    ArrayList<String> listaEstados;
    ArrayList<Modelo> objClientes;
    ArrayList<Modelo> objEstados;
    String idCliente;
    int peso;
    int posCliente;
    String idEstado;
    int tipoEstado;
    int posEstado;
    ICFragmentos icFragments;
    Activity activity;
    Bundle bundle;
    long fecha;
    Modelo proyecto;
    private ImagenUtil imagen;


    public FragmentCProyecto() {
        // Required empty public constructor
    }

    public static FragmentCProyecto newInstance() {
        FragmentCProyecto fragment = new FragmentCProyecto();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_c_proyecto, container, false);
        // Inflate the layout for this fragment
        imagenProyecto = vista.findViewById(R.id.imgnpry);
        nombreProyecto = vista.findViewById(R.id.etnombrenpry);
        descripcionProyecto = vista.findViewById(R.id.etdescnpry);
        spClienteProyecto = vista.findViewById(R.id.spclinpry);
        spEstadoProyecto = vista.findViewById(R.id.spestnpry);
        btnguardarProyecto = vista.findViewById(R.id.buttonsavenpry);
        imagenTipoClienteProyecto = vista.findViewById(R.id.imgtipoclinpry);
        titulo = vista.findViewById(R.id.tvtitnpry);
        fecha = Utilidades.hoy();

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


        if (Common.permiso) {
            imagenProyecto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mostrarDialogoOpciones();
                }
            });
        }

        listaObjetosClientes();

        ArrayAdapter<CharSequence> adaptadorCliente = new ArrayAdapter
                (getContext(),android.R.layout.simple_spinner_item,listaClientes);

        spClienteProyecto.setAdapter(adaptadorCliente);

        if (idCliente!=null){spClienteProyecto.setSelection(posCliente);}

        spClienteProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position>0) {

                    idCliente = objClientes.get(position-1).getString(CLIENTE_ID_CLIENTE);
                    peso = objClientes.get(position - 1).getInt(CLIENTE_PESOTIPOCLI);
                    posCliente = position;
                    if (peso>6){imagenTipoClienteProyecto.setImageResource(R.drawable.clientev);}
                    else if (peso>3){imagenTipoClienteProyecto.setImageResource(R.drawable.clientea);}
                    else if (peso>0){imagenTipoClienteProyecto.setImageResource(R.drawable.clienter);}
                    else {imagenTipoClienteProyecto.setImageResource(R.drawable.cliente);}


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listaObjetosEstados();

        ArrayAdapter<CharSequence> adaptadorEstado = new ArrayAdapter
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

        btnguardarProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registrarProyecto();
            }
        });
        imagenTipoClienteProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            mostrarDialogoTipoCliente();
            }
        });

        return vista;
    }

    private void registrarProyecto() {

        ContentValues valores = new ContentValues();
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_NOMBRE,nombreProyecto.getText().toString());
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_DESCRIPCION,descripcionProyecto.getText().toString());
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_ID_CLIENTE,idCliente);
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_ID_ESTADO,idEstado);
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_FECHAENTRADA,fecha);
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_RUTAFOTO,path);

        //valores.put(Contract.Columnas.PROYECTO_FECHAENTREGAACORDADA, "0");
        //valores.put(Contract.Columnas.PROYECTO_FECHAENTREGACALCULADA, "0");
        //valores.put(Contract.Columnas.PROYECTO_FECHAENTREGAPRESUP, "0");
        //valores.put(Contract.Columnas.PROYECTO_FECHAFINAL, "0");
        //valores.put(Contract.Columnas.PROYECTO_IMPORTEPRESUPUESTO, "0");
        //valores.put(Contract.Columnas.PROYECTO_IMPORTEFINAL, "0");
        //valores.put(Contract.Columnas.PROYECTO_TOTCOMPLETADO, "0");


        if (posCliente > 0) {

            if (posEstado > 0) {

                System.out.println("valores = " + valores);
                Uri uri = QueryDB.insertRegistro(TABLA_PROYECTO,valores);

                Modelo proyecto = QueryDB.queryObject(CAMPOS_PROYECTO,uri);

                new Common.Calculos.Tareafechas().execute();
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString("namef",namef);
                icFragments.enviarBundleAFragment(bundle,new FragmentUDProyecto());
                bundle = null;

            } else {
                Toast.makeText(getContext(), "Debe elegir un estado", Toast.LENGTH_LONG).show();
            }
        }else {
                Toast.makeText(getContext(),"Debe elegir un cliente",Toast.LENGTH_LONG).show();
            }

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

        objEstados = QueryDB.queryList(CAMPOS_ESTADO,seleccion,null);

        obtenerListaEstados();
    }

    private void obtenerListaEstados() {

        listaEstados = new ArrayList<String>();
        listaEstados.add("Seleccione Estado");

        for (int i=0;i<objEstados.size();i++){

            listaEstados.add(objEstados.get(i).getString(ESTADO_DESCRIPCION));
        }
    }

    private void listaObjetosClientes() {

        objClientes = new ArrayList<>();

        if (namef.equals(PRESUPUESTO)) {

                objClientes = QueryDB.queryList(CAMPOS_CLIENTE,null, null);

        }else{


            ArrayList<Modelo> lista = QueryDB.queryList(CAMPOS_CLIENTE,null,  null);

            objClientes = new ArrayList<>();

            for (int i=0;i< lista.size();i++) {

                Modelo cliente = new Modelo(CAMPOS_CLIENTE);

                if (cliente.getInt(CLIENTE_PESOTIPOCLI) >0){

                    objClientes.add(cliente);
                }
            }

        }

        obterListaClientes();

    }

    private void obterListaClientes() {

        listaClientes = new ArrayList<String>();
        listaClientes.add("Seleccione Cliente");

        for (int i=0;i<objClientes.size();i++){

            listaClientes.add(objClientes.get(i).getString(CLIENTE_NOMBRE));
            if (idCliente!=null && objClientes.get(i).getString(CLIENTE_ID_CLIENTE).equals(idCliente)){posCliente=i+1;}
        }
    }

    public void mostrarDialogoOpciones() {

        final CharSequence[] opciones = {"Hacer foto desde cámara", "Elegir de la galería", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                imagen = new ImagenUtil(getContext());

                if (opciones[which].equals("Hacer foto desde cámara")) {

                    try {
                        startActivityForResult(imagen.takePhotoIntent(), COD_FOTO);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imagen.addToGallery();

                } else if (opciones[which].equals("Elegir de la galería")) {

                    startActivityForResult(imagen.openGalleryIntent(), COD_SELECCIONA);

                } else {
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
                imagen.setPhotoUri(data.getData());
                photoPath = imagen.getPath();
                try {
                    Bitmap bitmap = ImagenUtil.ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    imagenProyecto.setImageBitmap(bitmap);
                    path = photoPath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case COD_FOTO:
                photoPath = imagen.getPhotoPath();
                try {
                    Bitmap bitmap = ImagenUtil.ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    imagenProyecto.setImageBitmap(bitmap); //imageView is your ImageView
                    path = photoPath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
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
        proyecto.setCampos(PROYECTO_FECHAENTRADA,String.valueOf(fecha));

        bundle = new Bundle();
        bundle.putSerializable(TABLA_PROYECTO, proyecto);
        bundle.putString("namef", namef);
        bundle.putString("namefsub", namefsub);

        icFragments.enviarBundleAFragment(bundle, myfragment);
        bundle = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            this.activity = (Activity) context;
            icFragments = (ICFragmentos) this.activity;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
