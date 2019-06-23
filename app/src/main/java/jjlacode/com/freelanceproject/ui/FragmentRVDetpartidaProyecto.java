package jjlacode.com.freelanceproject.ui;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.crud.FragmentRV;
import jjlacode.com.freelanceproject.util.crud.Modelo;

public class FragmentRVDetpartidaProyecto extends FragmentRV {

    @Override
    protected void setDatos() {

    }

    @Override
    protected void setTitulo() {

        tituloSingular = R.string.detpartida;
        tituloPlural = tituloSingular;

    }

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroRV setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroRV(context,layoutItem,lista,campos);
    }

    @Override
    public void setOnClickRV(String id, int secuencia, Modelo modelo) {

    }

    public class AdaptadorFiltroRV extends ListaAdaptadorFiltroRV{

        public AdaptadorFiltroRV(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        public ViewHolderRV(View itemView) {
            super(itemView);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return null;
        }
    }
}
