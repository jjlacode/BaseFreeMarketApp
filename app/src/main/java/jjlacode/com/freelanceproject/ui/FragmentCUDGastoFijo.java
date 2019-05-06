package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import jjlacode.com.freelanceproject.util.FragmentCUD;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;

public class FragmentCUDGastoFijo extends FragmentCUD implements ContratoPry.Tablas {

    private EditText nombre;
    private EditText descripcion;
    private EditText cantidad;
    private EditText importe;
    private EditText anios;
    private EditText meses;
    private EditText dias;


    public FragmentCUDGastoFijo() {
        // Required empty public constructor
    }

    @Override
    protected void setAcciones() {

    }


    @Override
    protected void setNuevo() {

        btndelete.setVisibility(View.GONE);

    }


    @Override
    protected void setTabla() {

        tabla = TABLA_GASTOFIJO;
    }

    @Override
    protected void setTablaCab() {

        tablaCab = null;
    }

    @Override
    protected void setContext() {

        contexto = getContext();
    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_GASTOFIJO;
    }

    @Override
    protected void setCampoID() {
        campoID = GASTOFIJO_ID_GASTOFIJO;
    }

    @Override
    protected void setBundle() {

        if (bundle != null) {

            if (modelo !=null) {
                id = modelo.getString(GASTOFIJO_ID_GASTOFIJO);
            }

        }

    }

    @Override
    protected void setDatos() {

        btndelete.setVisibility(View.VISIBLE);
        nombre.setText(modelo.getString(GASTOFIJO_NOMBRE));
        descripcion.setText(modelo.getString(GASTOFIJO_DESCRIPCION));
        cantidad.setText(modelo.getString(GASTOFIJO_CANTIDAD));
        importe.setText(modelo.getString(GASTOFIJO_IMPORTE));
        anios.setText(modelo.getString(GASTOFIJO_ANYOS));
        meses.setText(modelo.getString(GASTOFIJO_MESES));
        dias.setText(modelo.getString(GASTOFIJO_DIAS));

    }


    @Override
    protected void setLayout() {

        layout = R.layout.fragment_cud_gastofijo;

    }

    @Override
    protected void setInicio() {

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

    }

    @Override
    protected void setContenedor() {

        consulta.putDato(valores,campos,GASTOFIJO_NOMBRE,nombre.getText().toString());
        consulta.putDato(valores,campos,GASTOFIJO_DESCRIPCION,descripcion.getText().toString());
        consulta.putDato(valores,campos,GASTOFIJO_CANTIDAD,JavaUtil.comprobarDouble(cantidad.getText().toString()));
        consulta.putDato(valores,campos,GASTOFIJO_IMPORTE,JavaUtil.comprobarDouble(importe.getText().toString()));
        consulta.putDato(valores,campos,GASTOFIJO_ANYOS, JavaUtil.comprobarInteger(anios.getText().toString()));
        consulta.putDato(valores,campos,GASTOFIJO_MESES,JavaUtil.comprobarInteger(meses.getText().toString()));
        consulta.putDato(valores,campos,GASTOFIJO_DIAS,JavaUtil.comprobarInteger(dias.getText().toString()));


    }

    @Override
    protected void setcambioFragment() {

        icFragmentos.enviarBundleAFragment(bundle, new FragmentGastoFijo());

    }

}