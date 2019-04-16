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
import jjlacode.com.androidutils.FragmentC;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.utilities.CommonPry;

public class FragmentCCliente extends FragmentC implements CommonPry.Constantes, ContratoPry.Tablas {

    View vista;
    EditText nombreCliente,direccionCliente,telefonoCliente,emailCliente,contactoCliente;
    TextView titulo;
    ArrayList <Modelo> objTiposCli;
    String idTipoCliente = null;
    int peso;

    private String namefsub;
    private Modelo proyecto;

    private ConsultaBD consulta = new ConsultaBD();

    public FragmentCCliente() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista=inflater.inflate(R.layout.fragment_c_cliente, container, false);

        nombreCliente= (EditText) vista.findViewById(R.id.etnombreudcliente);
        direccionCliente= (EditText) vista.findViewById(R.id.etdirudcliente);
        telefonoCliente= (EditText) vista.findViewById(R.id.etteludcliente);
        emailCliente= (EditText) vista.findViewById(R.id.etemailudcliente);
        contactoCliente= (EditText) vista.findViewById(R.id.etcontudcliente);
        btnsave= (Button) vista.findViewById(R.id.btnsavenuevocliente);
        titulo = vista.findViewById(R.id.tvtitnuevocliente);


        listaObjetosTipo();

        try {
            bundle = getArguments();
            if (bundle!=null) {
                namef = bundle.getString("namef");
                namefsub = bundle.getString("namefsub");
                proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
                bundle = null;
            }

            if (namef.equals(PROSPECTO) ){

                titulo.setText(getString(R.string.nuevoprospecto));

                for (int i=0;i < objTiposCli.size();i++){

                    if (objTiposCli.get(i).getString(TIPOCLIENTE_DESCRIPCION).equals(PROSPECTO)){

                        idTipoCliente = objTiposCli.get(i).getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                        peso = objTiposCli.get(i).getInt(TIPOCLIENTE_PESO);
                    }
                }
            }else if (namef.equals(CLIENTE) ){

                titulo.setText(getString(R.string.nuevocliente));

                for (int i=0;i < objTiposCli.size();i++){

                    if (objTiposCli.get(i).getString(TIPOCLIENTE_DESCRIPCION).equals(NUEVO)){

                        idTipoCliente = objTiposCli.get(i).getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                        peso = objTiposCli.get(i).getInt(TIPOCLIENTE_PESO);
                    }
                }
            }else if (namefsub.equals(PROSPECTO)){
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                icFragmentos.enviarBundleAActivity(bundle);
                bundle = null;

                titulo.setText(getString(R.string.nuevoprospecto));

                for (int i=0;i < objTiposCli.size();i++){

                    if (objTiposCli.get(i).getString(TIPOCLIENTE_DESCRIPCION).equals(PROSPECTO)){

                        idTipoCliente = objTiposCli.get(i).getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                        peso = objTiposCli.get(i).getInt(TIPOCLIENTE_PESO);
                    }
                }
            }else if (namefsub.equals(CLIENTE) ){
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                icFragmentos.enviarBundleAActivity(bundle);
                bundle = null;

                titulo.setText(getString(R.string.nuevocliente));

                for (int i=0;i < objTiposCli.size();i++){

                    if (objTiposCli.get(i).getString(TIPOCLIENTE_DESCRIPCION).equals(NUEVO)){

                        idTipoCliente = objTiposCli.get(i).getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                        peso = objTiposCli.get(i).getInt(TIPOCLIENTE_PESO);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        btnsave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                registrar();

            }
        });
        // Inflate the layout for this fragment
        return vista;
    }

    private void listaObjetosTipo(){

            objTiposCli = consulta.queryList(CAMPOS_TIPOCLIENTE,null,null);

    }

    @Override
    public boolean registrar(){

        ContentValues valores=new ContentValues();
        consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_NOMBRE,nombreCliente.getText().toString());
        consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_DIRECCION,direccionCliente.getText().toString());
        consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_TELEFONO,telefonoCliente.getText().toString());
        consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_EMAIL,emailCliente.getText().toString());
        consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_CONTACTO,contactoCliente.getText().toString());
        consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_ID_TIPOCLIENTE,idTipoCliente);
        consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_PESOTIPOCLI,peso);

        String idCliente = consulta.idInsertRegistro(TABLA_CLIENTE,valores);

            if ((namef.equals(CLIENTE))||(namef.equals(PROSPECTO))) {

                bundle = new Bundle();
                bundle.putString("namef",namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCliente());
                bundle = null;

            }else if ((namef.equals(PROYECTO))||(namef.equals(PRESUPUESTO))){


                bundle = new Bundle();
                proyecto.setCampos(PROYECTO_ID_CLIENTE,idCliente);
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString("namefsub",namefsub);
                bundle.putString("namef",namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentUDProyecto());
                bundle = null;

            }
            return true;
    }


}
