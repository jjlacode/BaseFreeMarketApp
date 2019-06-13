package jjlacode.com.freelanceproject.ui;
// Created by jjlacode on 10/06/19. 

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.crud.FragmentCRUD;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;


public class FragmentCRUDProducto extends FragmentCRUD implements CommonPry.Constantes, ContratoPry.Tablas {


    public FragmentCRUDProducto() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroRV setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroRV(context, layoutItem, lista, campos);
    }

    @Override
    protected void setTabla() {

        tabla = TABLA_PRODUCTO;

    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.producto;
        tituloPlural = R.string.productos;
        tituloNuevo = R.string.nuevo_producto;
    }

    @Override
    protected void setDatos() {

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_producto;
        layoutItem = R.layout.item_list_producto;
        //layoutCabecera = ;

    }

    @Override
    protected void setInicio() {

    }

    @Override
    protected void setContenedor() {


    }

    public class AdaptadorFiltroRV extends ListaAdaptadorFiltroRV {

        public AdaptadorFiltroRV(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<Modelo> entrada) {

            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {


        public ViewHolderRV(View itemView) {
            super(itemView);


        }

        @Override
        public void bind(Modelo modelo) {


            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }


}
