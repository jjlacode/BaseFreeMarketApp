package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import jjlacode.com.freelanceproject.interfaces.ICFragmentos;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.model.Modelo;
import jjlacode.com.freelanceproject.sqlite.Contract;

public class FragmentUDAmortizacion extends Fragment implements Contract.Tablas {

    private String idAmortizacion;
    private String namef;

    /*
    EditText nombreCliente,direccionCliente,telefonoCliente,emailCliente,contactoCliente;
    TextView tipoCliente;
    */
    private Button btnsave;
    private Button btndelete;
    private Button btnback;

    private Activity activity;
    private ICFragmentos icFragmentos;
    private Bundle bundle;
    private Modelo amortizacion;

    public FragmentUDAmortizacion() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ud_amortizacion, container, false);

        btnsave = (Button) vista.findViewById(R.id.amortizacion_ud_btn_save);
        btndelete = (Button) vista.findViewById(R.id.amortizacion_ud_btn_del);
        btnback = (Button) vista.findViewById(R.id.amortizacion_ud_btn_back);

        bundle = getArguments();
        amortizacion = (Modelo) bundle.getSerializable(TABLA_AMORTIZACION);

        namef = bundle.getString("namef");
        bundle = null;

        //TODO insertar asignaciones de campos     

        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                updateAmortizacion();

                bundle = new Bundle();
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentAmortizacion());
                bundle = null;

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                borraAmortizacion();

                bundle = new Bundle();
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentAmortizacion());
                bundle = null;

            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                borraAmortizacion();

                bundle = new Bundle();
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentAmortizacion());
                bundle = null;

            }
        });

        return vista;
    }


    public void updateAmortizacion() {

        ContentValues valores = new ContentValues();
        /*
        valores.put(Contract.ColumnasCliente.NOMBRE,nombreCliente.getText().toString());
        //TODO insertar valores al ContentValues para upddate
        */

        getActivity().getContentResolver().update(
                Contract.crearUriTabla(idAmortizacion, TABLA_AMORTIZACION),
                valores, null, null
        );

    }

    public void borraAmortizacion() {

        getActivity().getContentResolver().delete(
                Contract.crearUriTabla(idAmortizacion, TABLA_AMORTIZACION), null, null
        );

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