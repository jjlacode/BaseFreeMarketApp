package com.jjlacode.freelanceproject.ui;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.adapter.BaseViewHolder;
import com.jjlacode.base.util.adapter.ListaAdaptadorFiltroModelo;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.base.util.crud.FragmentCRUD;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.logica.Interactor;
import com.jjlacode.freelanceproject.sqlite.ContratoPry;

import java.util.ArrayList;

public class FragmentCRUDGastoFijo extends FragmentCRUD implements ContratoPry.Tablas {

    private EditText nombre;
    private EditText descripcion;
    private EditText cantidad;
    private EditText importe;
    private EditText anios;
    private EditText meses;
    private EditText dias;


    public FragmentCRUDGastoFijo() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto
            (Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    @Override
    protected void setLista() {

    }

    @Override
    protected void setMaestroDetallePort() { maestroDetalleSeparados = true;

    }

    @Override
    protected void setMaestroDetalleLand() { maestroDetalleSeparados = false;

    }

    @Override
    protected void setMaestroDetalleTabletLand() { maestroDetalleSeparados = false;

    }

    @Override
    protected void setMaestroDetalleTabletPort() { maestroDetalleSeparados = false;

    }

    @Override
    protected void setAcciones() {

    }

    @Override
    protected void setTitulo() {

        tituloSingular = R.string.gasto_fijo;
        tituloPlural = R.string.gastos_fijos;
    }


    @Override
    protected void setNuevo() {


    }


    @Override
    protected void setTabla() {

        tabla = TABLA_GASTOFIJO;
    }

    @Override
    protected void setTablaCab() {

        tablaCab = ContratoPry.getTabCab(tabla);
    }

    @Override
    protected void setCampos() {

        campos = ContratoPry.obtenerCampos(tabla);

    }

    @Override
    protected void setDatos() {

        btndelete.setVisibility(View.VISIBLE);
        nombre.setText(modelo.getString(GASTOFIJO_NOMBRE));
        descripcion.setText(modelo.getString(GASTOFIJO_DESCRIPCION));
        cantidad.setText(modelo.getString(GASTOFIJO_CANTIDAD));
        importe.setText(modelo.getString(GASTOFIJO_PRECIO));
        anios.setText(modelo.getString(GASTOFIJO_ANYOS));
        meses.setText(modelo.getString(GASTOFIJO_MESES));
        dias.setText(modelo.getString(GASTOFIJO_DIAS));

    }


    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_gastofijo;
        layoutItem = R.layout.item_list_gastofijo;

    }

    @Override
    protected void setInicio() {

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

        setDato(GASTOFIJO_NOMBRE, nombre.getText().toString());
        setDato(GASTOFIJO_DESCRIPCION, descripcion.getText().toString());
        setDato(GASTOFIJO_CANTIDAD, JavaUtil.comprobarDouble(cantidad.getText().toString()));
        setDato(GASTOFIJO_PRECIO, JavaUtil.comprobarDouble(importe.getText().toString()));
        setDato(GASTOFIJO_ANYOS, JavaUtil.comprobarInteger(anios.getText().toString()));
        setDato(GASTOFIJO_MESES, JavaUtil.comprobarInteger(meses.getText().toString()));
        setDato(GASTOFIJO_DIAS, JavaUtil.comprobarInteger(dias.getText().toString()));

    }

    @Override
    protected void setcambioFragment() {

        subTitulo = Interactor.setNamefdef();

    }



    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView nombre, descripcion, cantidad, importe, anios, meses, dias;

        public ViewHolderRV(View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.tvnomlgasto);
            descripcion = itemView.findViewById(R.id.tvdesclgasto);
            cantidad = itemView.findViewById(R.id.tvcantlgasto);
            importe = itemView.findViewById(R.id.tvimplgasto);
            anios = itemView.findViewById(R.id.tvanioslgasto);
            meses = itemView.findViewById(R.id.tvmeseslgasto);
            dias = itemView.findViewById(R.id.tvdiaslgasto);

        }

        @Override
        public void bind(Modelo modelo) {

            nombre.setText(modelo.getString(GASTOFIJO_NOMBRE));
            descripcion.setText(modelo.getString(GASTOFIJO_DESCRIPCION));
            cantidad.setText(modelo.getString(GASTOFIJO_CANTIDAD));
            importe.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(GASTOFIJO_PRECIO)));
            anios.setText(modelo.getString(GASTOFIJO_ANYOS));
            meses.setText(modelo.getString(GASTOFIJO_MESES));
            dias.setText(modelo.getString(GASTOFIJO_DIAS));
            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {


        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<Modelo> entrada) {

            TextView nombre, descripcion, cantidad, importe, anios, meses, dias;

            nombre = itemView.findViewById(R.id.tvnomlgasto);
            descripcion = itemView.findViewById(R.id.tvdesclgasto);
            cantidad = itemView.findViewById(R.id.tvcantlgasto);
            importe = itemView.findViewById(R.id.tvimplgasto);
            anios = itemView.findViewById(R.id.tvanioslgasto);
            meses = itemView.findViewById(R.id.tvmeseslgasto);
            dias = itemView.findViewById(R.id.tvdiaslgasto);

            nombre.setText(entrada.get(posicion).getString(GASTOFIJO_NOMBRE));
            descripcion.setText(entrada.get(posicion).getString(GASTOFIJO_DESCRIPCION));
            cantidad.setText(entrada.get(posicion).getString(GASTOFIJO_CANTIDAD));
            importe.setText(JavaUtil.formatoMonedaLocal(entrada.get(posicion).getDouble(GASTOFIJO_PRECIO)));
            anios.setText(entrada.get(posicion).getString(GASTOFIJO_ANYOS));
            meses.setText(entrada.get(posicion).getString(GASTOFIJO_MESES));
            dias.setText(entrada.get(posicion).getString(GASTOFIJO_DIAS));

            super.setEntradas(posicion, view, entrada);
        }
    }
}