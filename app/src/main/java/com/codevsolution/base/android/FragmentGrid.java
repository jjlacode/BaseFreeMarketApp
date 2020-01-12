package com.codevsolution.base.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.codevsolution.base.R;
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.models.Modelo;

import java.util.ArrayList;

public class FragmentGrid extends FragmentGridBase {

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected void setLayout() {

        layoutItem = R.layout.item_grid_list;
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList lista, String[] campos) {
        return null;
    }

    @Override
    public void setOnClickRV(Object object) {

    }

    protected ViewGroupLayout setVistaMain(Context contexto, ViewGroup viewGroup, Modelo modelo) {
        return null;
    }

    private class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        LinearLayoutCompat main;

        public ViewHolderRV(View view) {
            super(view);

            main = view.findViewById(R.id.main_item_grid);
        }

        @Override
        public void bind(ArrayList<?> lista, int position) {
            super.bind(lista, position);

            Modelo modelo = (Modelo) lista.get(position);
            vistaMain = setVistaMain(contexto, main, modelo);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

}
