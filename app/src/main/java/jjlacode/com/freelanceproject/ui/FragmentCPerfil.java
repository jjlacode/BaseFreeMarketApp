package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import jjlacode.com.freelanceproject.interfaces.ICFragmentos;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.Contract;

public class FragmentCPerfil extends Fragment implements Contract.Tablas {


    //EditText nombreCliente,direccionCliente,telefonoCliente,emailCliente,contactoCliente;
    //TextView titulo;
    private Button btnsave;
    private String namef;
    private Activity activity;
    private ICFragmentos icFragmentos;
    private String namefsub;
    private Bundle bundle;

    public FragmentCPerfil() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_c_perfil, container, false);

        /*
        nombreCliente= (EditText) vista.findViewById(R.id.etnombrenuevocliente);
        direccionCliente= (EditText) vista.findViewById(R.id.etdirnuevocliente);
        telefonoCliente= (EditText) vista.findViewById(R.id.ettelnuevocliente);
        emailCliente= (EditText) vista.findViewById(R.id.etemailnuevocliente);
        contactoCliente= (EditText) vista.findViewById(R.id.etcontnuevocliente);
        btnsave= (Button) vista.findViewById(R.id.btnsavenuevocliente);
        titulo = vista.findViewById(R.id.tvtitnuevocliente);
       
        */

        try {
            bundle = getArguments();
            if (bundle != null) {
                namef = bundle.getString("namef");
                namefsub = bundle.getString("namefsub");
                bundle = null;
            }
        } catch (Exception e) {
        }


        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                registrarPerfil();

            }
        });
        // Inflate the layout for this fragment
        return vista;
    }

    public void registrarPerfil() {

        ContentValues valores = new ContentValues();
        //valores.put(ColumnasPerfil.NOMBRE,nombrePerfil.getText().toString());


        Uri uri = getActivity().getContentResolver().insert(
                Contract.obtenerUriContenido(TABLA_PERFIL),
                valores);
        bundle = new Bundle();

        String idPerfil = Contract.obtenerIdTabla(uri);

        bundle.putString("namefsub", namefsub);
        bundle.putString("namef", namef);
        icFragmentos.enviarBundleAFragment(bundle, new Fragment());//TODO cambiar fragmento de salida
        bundle = null;

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
}