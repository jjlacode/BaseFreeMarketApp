package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import jjlacode.com.androidutils.ICFragmentos;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;


public class FragmentUDCliente extends Fragment implements Common.Constantes, Contract.Tablas {

    private String idCliente;
    private String namef;
    private String idProyecto;

    View vista;
    EditText nombreCliente,direccionCliente,telefonoCliente,emailCliente,contactoCliente;
    TextView tipoCliente;
    Button btnsave;
    Button btndelete;
    Button btnback;
    Button btnevento;
    ImageView imagen = null;
    ArrayList<String> listaTipos;
    ArrayList <Modelo> objTiposCli;
    String idTipoCliente = null;
    int peso;
    private AppCompatActivity activity;
    private ICFragmentos icFragmentos;
    private Modelo proyecto;
    private Bundle bundle;
    private Modelo cliente;

    public FragmentUDCliente() {
        // Required empty public constructor
    }


    public static FragmentUDCliente newInstance() {
        FragmentUDCliente fragment = new FragmentUDCliente();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista=inflater.inflate(R.layout.fragment_ud_cliente, container, false);

        nombreCliente = vista.findViewById(R.id.etnombreudcliente);
        direccionCliente = vista.findViewById(R.id.etdirudcliente);
        telefonoCliente = vista.findViewById(R.id.etteludcliente);
        emailCliente = vista.findViewById(R.id.etemailudcliente);
        contactoCliente = vista.findViewById(R.id.etcontudcliente);
        btnsave = vista.findViewById(R.id.btnsaveudcliente);
        btnback = vista.findViewById(R.id.btnbackudcliente);
        btnevento = vista.findViewById(R.id.btneventoudcliente);
        btndelete = vista.findViewById(R.id.btndleudcliente);
        tipoCliente = vista.findViewById(R.id.tvtipoudcliente);
        imagen = vista.findViewById(R.id.imgudcliente);

        bundle = getArguments();
        cliente = (Modelo) bundle.getSerializable(TABLA_CLIENTE);
        proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
        namef = bundle.getString("namef");
        bundle = null;

            nombreCliente.setText(cliente.getString(CLIENTE_NOMBRE));
            direccionCliente.setText(cliente.getString(CLIENTE_DIRECCION));
            telefonoCliente.setText(cliente.getString(CLIENTE_TELEFONO));
            emailCliente.setText(cliente.getString(CLIENTE_EMAIL));
            contactoCliente.setText(cliente.getString(CLIENTE_CONTACTO));
            idTipoCliente=cliente.getString(CLIENTE_ID_TIPOCLIENTE);
            idCliente=cliente.getString(CLIENTE_ID_CLIENTE);
            tipoCliente.setText(cliente.getString(CLIENTE_DESCRIPCIONTIPOCLI));
            peso = cliente.getInt(CLIENTE_PESOTIPOCLI);

        if (namef.equals(PROYECTO) || namef.equals(PRESUPUESTO)
                || namef.equals(AGENDA)){

            btndelete.setVisibility(View.GONE);

        }else {

            btndelete.setVisibility(View.VISIBLE);

        }

                    if (peso>6){imagen.setImageResource(R.drawable.clientev);}
                    else if (peso>3){imagen.setImageResource(R.drawable.clientea);}
                    else if (peso>0){imagen.setImageResource(R.drawable.clienter);}
                    else {imagen.setImageResource(R.drawable.cliente);}

        btnsave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                updateCliente();

                if (namef.equals(PROYECTO) || namef.equals(PRESUPUESTO)
                        || namef.equals(AGENDA)){

                    bundle = new Bundle();
                    bundle.putSerializable(TABLA_PROYECTO,proyecto);
                    bundle.putString("namef",namef);
                    icFragmentos.enviarBundleAFragment(bundle,new FragmentUDProyecto());

                }else{

                    bundle = new Bundle();
                    bundle.putString("namef",namef);
                    icFragmentos.enviarBundleAFragment(bundle,new FragmentCliente());
                }

            }
        });

        btnback.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (namef.equals(PROYECTO) || namef.equals(PRESUPUESTO)
                        || namef.equals(AGENDA)){

                    bundle = new Bundle();
                    bundle.putSerializable(TABLA_PROYECTO,proyecto);
                    bundle.putString("namef",namef);
                    icFragmentos.enviarBundleAFragment(bundle,new FragmentUDProyecto());

                }else{

                    bundle = new Bundle();
                    bundle.putString("namef",namef);
                    icFragmentos.enviarBundleAFragment(bundle,new FragmentCliente());
                }

            }
        });

        btnevento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(TABLA_CLIENTE,cliente);
                bundle.putString("namef",namef);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCEvento());
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    borraCliente();

            }
            });

        return vista;
    }


    public void updateCliente(){

        ContentValues valores=new ContentValues();
        QueryDB.putDato(valores,CAMPOS_CLIENTE,CLIENTE_NOMBRE,nombreCliente.getText().toString());
        QueryDB.putDato(valores,CAMPOS_CLIENTE,CLIENTE_DIRECCION,direccionCliente.getText().toString());
        QueryDB.putDato(valores,CAMPOS_CLIENTE,CLIENTE_TELEFONO,telefonoCliente.getText().toString());
        QueryDB.putDato(valores,CAMPOS_CLIENTE,CLIENTE_EMAIL,emailCliente.getText().toString());
        QueryDB.putDato(valores,CAMPOS_CLIENTE,CLIENTE_CONTACTO,contactoCliente.getText().toString());
        QueryDB.putDato(valores,CAMPOS_CLIENTE,CLIENTE_ID_TIPOCLIENTE,idTipoCliente);
        QueryDB.putDato(valores,CAMPOS_CLIENTE,CLIENTE_PESOTIPOCLI,peso);

        getActivity().getContentResolver().update(
                Contract.crearUriTabla(idCliente, TABLA_CLIENTE),
                valores,null,null
        );

    }

    public void borraCliente(){



        getActivity().getContentResolver().delete(
                Contract.crearUriTabla(idCliente, TABLA_CLIENTE),
                null,null
        );
        bundle = new Bundle();
        bundle.putString("namef",namef);
        icFragmentos.enviarBundleAFragment(bundle,new FragmentCliente());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            this.activity = (AppCompatActivity) context;
            icFragmentos = (ICFragmentos) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
