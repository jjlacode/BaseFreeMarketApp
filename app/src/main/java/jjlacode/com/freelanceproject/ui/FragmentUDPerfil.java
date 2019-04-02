package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import jjlacode.com.androidutils.ICFragmentos;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.Contract;

public class FragmentUDPerfil extends Fragment implements Contract.Tablas {

    private String idPerfil;
    private String namef;

    /*
    EditText nombreCliente,direccionCliente,telefonoCliente,emailCliente,contactoCliente;
    TextView tipoCliente;
    */
    private Button btnsave;
    private Button btndelete;
    private Button btnback;

    private AppCompatActivity activity;
    private ICFragmentos icFragmentos;
    private Bundle bundle;
    private Modelo perfil;

    public FragmentUDPerfil() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ud_perfil, container, false);

        btnsave = (Button) vista.findViewById(R.id.perfil_ud_btn_save);
        btndelete = (Button) vista.findViewById(R.id.perfil_ud_btn_del);
        btnback = (Button) vista.findViewById(R.id.perfil_ud_btn_back);

        bundle = getArguments();
        perfil = (Modelo) bundle.getSerializable(TABLA_PERFIL);

        namef = bundle.getString("namef");
        bundle = null;

        //TODO insertar asignaciones de campos     

        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                updatePerfil();

                bundle = new Bundle();
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentPerfil());
                bundle = null;

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                borraPerfil();

                bundle = new Bundle();
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentPerfil());
                bundle = null;

            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                borraPerfil();

                bundle = new Bundle();
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentPerfil());
                bundle = null;

            }
        });

        return vista;
    }


    public void updatePerfil() {

        ContentValues valores = new ContentValues();
        /*
        valores.put(Contract.ColumnasCliente.NOMBRE,nombreCliente.getText().toString());
        //TODO insertar valores al ContentValues para upddate
        */

        getActivity().getContentResolver().update(
                Contract.crearUriTabla(idPerfil, TABLA_PERFIL),
                valores, null, null
        );

    }

    public void borraPerfil() {

        getActivity().getContentResolver().delete(
                Contract.crearUriTabla(idPerfil, TABLA_PERFIL), null, null
        );

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

}