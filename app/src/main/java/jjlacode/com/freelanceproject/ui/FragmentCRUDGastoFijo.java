package jjlacode.com.freelanceproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.BaseViewHolder;
import jjlacode.com.freelanceproject.util.CommonPry;
import jjlacode.com.freelanceproject.util.FragmentCRUD;
import jjlacode.com.freelanceproject.util.FragmentCUD;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.Modelo;
import jjlacode.com.freelanceproject.util.RVAdapter;
import jjlacode.com.freelanceproject.util.TipoViewHolder;

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
    protected ListaAdaptadorFiltroRV setAdaptadorAuto
            (Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroRV(context,layoutItem,lista,campos);
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

        layoutCuerpo = R.layout.fragment_cud_gastofijo;
        layoutitem = R.layout.item_list_gastofijo;

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

        consulta.putDato(valores, campos, GASTOFIJO_NOMBRE, nombre.getText().toString());
        consulta.putDato(valores, campos, GASTOFIJO_DESCRIPCION, descripcion.getText().toString());
        consulta.putDato(valores, campos, GASTOFIJO_CANTIDAD, JavaUtil.comprobarDouble(cantidad.getText().toString()));
        consulta.putDato(valores, campos, GASTOFIJO_IMPORTE, JavaUtil.comprobarDouble(importe.getText().toString()));
        consulta.putDato(valores, campos, GASTOFIJO_ANYOS, JavaUtil.comprobarInteger(anios.getText().toString()));
        consulta.putDato(valores, campos, GASTOFIJO_MESES, JavaUtil.comprobarInteger(meses.getText().toString()));
        consulta.putDato(valores, campos, GASTOFIJO_DIAS, JavaUtil.comprobarInteger(dias.getText().toString()));


    }

    @Override
    protected void setcambioFragment() {

        namesubclass = CommonPry.setNamefdef();

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
            importe.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(GASTOFIJO_IMPORTE)));
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

    public class AdaptadorFiltroRV extends ListaAdaptadorFiltroRV{


        public AdaptadorFiltroRV(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
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
            importe.setText(JavaUtil.formatoMonedaLocal(entrada.get(posicion).getDouble(GASTOFIJO_IMPORTE)));
            anios.setText(entrada.get(posicion).getString(GASTOFIJO_ANYOS));
            meses.setText(entrada.get(posicion).getString(GASTOFIJO_MESES));
            dias.setText(entrada.get(posicion).getString(GASTOFIJO_DIAS));

            super.setEntradas(posicion, view, entrada);
        }
    }
}