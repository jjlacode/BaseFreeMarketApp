package com.codevsolution.freemarketsapp.ui;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

import static com.codevsolution.base.sqlite.ConsultaBD.putDato;

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
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto
            (Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos) {
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
    protected void setDatos() {

        btndelete.setVisibility(View.VISIBLE);
        nombre.setText(modeloSQL.getString(GASTOFIJO_NOMBRE));
        descripcion.setText(modeloSQL.getString(GASTOFIJO_DESCRIPCION));
        cantidad.setText(modeloSQL.getString(GASTOFIJO_CANTIDAD));
        importe.setText(modeloSQL.getString(GASTOFIJO_PRECIO));
        anios.setText(modeloSQL.getString(GASTOFIJO_ANYOS));
        meses.setText(modeloSQL.getString(GASTOFIJO_MESES));
        dias.setText(modeloSQL.getString(GASTOFIJO_DIAS));

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

        putDato(valores,GASTOFIJO_NOMBRE, nombre.getText().toString());
        putDato(valores,GASTOFIJO_DESCRIPCION, descripcion.getText().toString());
        putDato(valores,GASTOFIJO_CANTIDAD, JavaUtil.comprobarDouble(cantidad.getText().toString()));
        putDato(valores,GASTOFIJO_PRECIO, JavaUtil.comprobarDouble(importe.getText().toString()));
        putDato(valores,GASTOFIJO_ANYOS, JavaUtil.comprobarInteger(anios.getText().toString()));
        putDato(valores,GASTOFIJO_MESES, JavaUtil.comprobarInteger(meses.getText().toString()));
        putDato(valores,GASTOFIJO_DIAS, JavaUtil.comprobarInteger(dias.getText().toString()));

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
        public void bind(ModeloSQL modeloSQL) {

            nombre.setText(modeloSQL.getString(GASTOFIJO_NOMBRE));
            descripcion.setText(modeloSQL.getString(GASTOFIJO_DESCRIPCION));
            cantidad.setText(modeloSQL.getString(GASTOFIJO_CANTIDAD));
            importe.setText(JavaUtil.formatoMonedaLocal(modeloSQL.getDouble(GASTOFIJO_PRECIO)));
            anios.setText(modeloSQL.getString(GASTOFIJO_ANYOS));
            meses.setText(modeloSQL.getString(GASTOFIJO_MESES));
            dias.setText(modeloSQL.getString(GASTOFIJO_DIAS));
            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {


        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<ModeloSQL> entrada) {

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