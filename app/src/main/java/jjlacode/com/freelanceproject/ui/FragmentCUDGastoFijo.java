package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import jjlacode.com.androidutils.FragmentCUD;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;

public class FragmentCUDGastoFijo extends FragmentCUD implements ContratoPry.Tablas {

    private String idGastoFijo;

    private Modelo gastoFijo;
    private EditText nombre;
    private EditText descripcion;
    private EditText cantidad;
    private EditText importe;
    private EditText anios;
    private EditText meses;
    private EditText dias;

    private static ConsultaBD consulta = new ConsultaBD();

    public FragmentCUDGastoFijo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cud_gastofijo, container, false);

        bundle = getArguments();
        if (bundle != null) {

            gastoFijo = (Modelo) bundle.getSerializable(TABLA_GASTOFIJO);
            if (gastoFijo!=null) {
                idGastoFijo = gastoFijo.getString(GASTOFIJO_ID_GASTOFIJO);
            }
            namef = bundle.getString("namef");
            bundle = null;
        }

        btnsave = view.findViewById(R.id.gastoFijo_ud_btn_save);
        btndelete = view.findViewById(R.id.gastoFijo_ud_btn_del);
        btnback = view.findViewById(R.id.gastoFijo_ud_btn_back);
        nombre = view.findViewById(R.id.etnomcudgasto);
        descripcion = view.findViewById(R.id.etdesccudgasto);
        cantidad = view.findViewById(R.id.etcantcudgasto);
        importe = view.findViewById(R.id.etimpcudgasto);
        anios = view.findViewById(R.id.etanioscudgasto);
        meses = view.findViewById(R.id.etmesescudgasto);
        dias = view.findViewById(R.id.etdiascudgasto);


        btndelete.setVisibility(View.GONE);

        if (idGastoFijo != null) {


            btndelete.setVisibility(View.VISIBLE);
            nombre.setText(gastoFijo.getString(GASTOFIJO_NOMBRE));
            descripcion.setText(gastoFijo.getString(GASTOFIJO_DESCRIPCION));
            cantidad.setText(gastoFijo.getString(GASTOFIJO_CANTIDAD));
            importe.setText(gastoFijo.getString(GASTOFIJO_IMPORTE));
            anios.setText(gastoFijo.getString(GASTOFIJO_ANYOS));
            meses.setText(gastoFijo.getString(GASTOFIJO_MESES));
            dias.setText(gastoFijo.getString(GASTOFIJO_DIAS));
        }

        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                update();

                cambiarFragment();

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                delete();

                cambiarFragment();

            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cambiarFragment();

            }
        });

        return view;
    }


    @Override
    protected void update() {

        contenedor();

        if (idGastoFijo != null) {
            consulta.updateRegistro(TABLA_GASTOFIJO, idGastoFijo, valores);
            icFragmentos.ejecutarEnActivity();
        } else {

            registrar();
        }

    }

    @Override
    protected void delete() {

        consulta.deleteRegistro(TABLA_GASTOFIJO, idGastoFijo);

    }

    @Override
    protected boolean registrar() {

        consulta.insertRegistro(TABLA_GASTOFIJO, valores);
        icFragmentos.ejecutarEnActivity();
        return true;
    }

    protected boolean contenedor() {

        valores = new ContentValues();

        consulta.putDato(valores,CAMPOS_GASTOFIJO,GASTOFIJO_NOMBRE,nombre.getText().toString());
        consulta.putDato(valores,CAMPOS_GASTOFIJO,GASTOFIJO_DESCRIPCION,descripcion.getText().toString());
        consulta.putDato(valores,CAMPOS_GASTOFIJO,GASTOFIJO_CANTIDAD,JavaUtil.comprobarDouble(cantidad.getText().toString()));
        consulta.putDato(valores,CAMPOS_GASTOFIJO,GASTOFIJO_IMPORTE,JavaUtil.comprobarDouble(importe.getText().toString()));
        consulta.putDato(valores,CAMPOS_GASTOFIJO,GASTOFIJO_ANYOS, JavaUtil.comprobarInteger(anios.getText().toString()));
        consulta.putDato(valores,CAMPOS_GASTOFIJO,GASTOFIJO_MESES,JavaUtil.comprobarInteger(meses.getText().toString()));
        consulta.putDato(valores,CAMPOS_GASTOFIJO,GASTOFIJO_DIAS,JavaUtil.comprobarInteger(dias.getText().toString()));

        return true;
    }

    protected void cambiarFragment() {

        bundle = new Bundle();
        bundle.putString("namef", namef);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentGastoFijo());

    }

}