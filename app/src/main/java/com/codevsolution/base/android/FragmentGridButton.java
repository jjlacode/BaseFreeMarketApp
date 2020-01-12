package com.codevsolution.base.android;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.codevsolution.base.R;
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.controls.ViewGroupLayout;

import java.util.ArrayList;

public class FragmentGridButton extends FragmentGridBase {

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


    protected ViewGroupLayout setVistaMain(Context contexto, ViewGroup viewGroup, GridModel gridModel) {
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

            GridModel gridModel = (GridModel) lista.get(position);
            vistaMain = setVistaMain(contexto, main, gridModel);

            if (vistaMain == null) {
                ViewGroupLayout vistaGrid = new ViewGroupLayout(contexto, main);
                Button button = vistaGrid.addButtonTrans(gridModel.nombre);

                LinearLayoutCompat.LayoutParams param = new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        (int) (((rv.getHeight()) - (padalto * densidad)) / filas));
                main.setLayoutParams(param);

                button.setBackgroundColor(Color.parseColor(gridModel.getColorHex()));
                button.setTextColor(gridModel.getColorTexto());
                button.setPadding(padancho, padalto, padancho, 0);

                if (!gridModel.isSinTexto()) {
                    button.setText(gridModel.getNombre());
                    button.setTextSize(sizeT);
                    button.setPadding(padtxt, 0, padtxt, 0);
                } else {
                    button.setText("");
                    button.setPadding(padancho, padalto, padancho, padalto);
                }
            }

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    public class GridModel {

        private int color;
        private String colorHex;
        private int colorTexto;
        private String nombre;
        private boolean sinTexto;

        public GridModel(String colorHex, int color, int colorTexto, String nombre) {
            this.color = color;
            this.colorHex = colorHex;
            this.colorTexto = colorTexto;
            this.nombre = nombre;
            sinTexto = false;
        }

        public GridModel(String colorHex, int color, int colorTexto, String nombre, boolean sinTexto) {
            this.color = color;
            this.colorHex = colorHex;
            this.colorTexto = colorTexto;
            this.nombre = nombre;
            this.sinTexto = sinTexto;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public String getColorHex() {
            return colorHex;
        }

        public void setColorHex(String colorHex) {
            this.colorHex = colorHex;
        }

        public int getColorTexto() {
            return colorTexto;
        }

        public void setColorTexto(int colorTexto) {
            this.colorTexto = colorTexto;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public boolean isSinTexto() {
            return sinTexto;
        }

        public void setSinTexto(boolean sinTexto) {
            this.sinTexto = sinTexto;
        }
    }

}
