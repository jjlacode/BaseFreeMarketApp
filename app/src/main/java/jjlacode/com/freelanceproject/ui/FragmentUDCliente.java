package jjlacode.com.freelanceproject.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;

import jjlacode.com.androidutils.AppActivity;
import jjlacode.com.androidutils.FragmentUD;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.utilities.CommonPry;


public class FragmentUDCliente extends FragmentUD implements CommonPry.Constantes, ContratoPry.Tablas {

    private String idCliente;

    EditText nombreCliente,direccionCliente,telefonoCliente,emailCliente,contactoCliente;
    TextView tipoCliente;
    Button btnevento;
    String idTipoCliente = null;
    int peso;
    private Modelo proyecto;
    private Modelo cliente;

    private ConsultaBD consulta = new ConsultaBD();

    public FragmentUDCliente() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ud_cliente, container, false);

        nombreCliente = view.findViewById(R.id.etnombreudcliente);
        direccionCliente = view.findViewById(R.id.etdirudcliente);
        telefonoCliente = view.findViewById(R.id.etteludcliente);
        emailCliente = view.findViewById(R.id.etemailudcliente);
        contactoCliente = view.findViewById(R.id.etcontudcliente);
        btnsave = view.findViewById(R.id.btnsaveudcliente);
        btnback = view.findViewById(R.id.btnbackudcliente);
        btnevento = view.findViewById(R.id.btneventoudcliente);
        btndelete = view.findViewById(R.id.btndleudcliente);
        tipoCliente = view.findViewById(R.id.tvtipoudcliente);
        imagen = view.findViewById(R.id.imgudcliente);

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

                update();

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

                    delete();

            }
            });

        return view;
    }

    @Override
    protected void update(){

        ContentValues valores=new ContentValues();
        consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_NOMBRE,nombreCliente.getText().toString());
        consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_DIRECCION,direccionCliente.getText().toString());
        consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_TELEFONO,telefonoCliente.getText().toString());
        consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_EMAIL,emailCliente.getText().toString());
        consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_CONTACTO,contactoCliente.getText().toString());
        consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_ID_TIPOCLIENTE,idTipoCliente);
        consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_PESOTIPOCLI,peso);

        consulta.updateRegistro(TABLA_CLIENTE,idCliente,valores);

    }

    protected void delete(){

        consulta.deleteRegistro(TABLA_CLIENTE,idCliente);

        bundle = new Bundle();
        bundle.putString("namef",namef);
        icFragmentos.enviarBundleAFragment(bundle,new FragmentCliente());
    }


}
