package com.codevsolution.base.android;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

public class FragmentGridImagen extends FragmentGridBase {


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

    private class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        LinearLayoutCompat main;
        TextView nombre;
        ImageView imagen;

        public ViewHolderRV(View view) {
            super(view);

            main = view.findViewById(R.id.main_item_grid);
            nombre = view.findViewById(R.id.tv_item_grid);
            imagen = view.findViewById(R.id.img_item_grid);
        }

        @Override
        public void bind(ArrayList<?> lista, int position) {
            super.bind(lista, position);

            GridModel gridModel = (GridModel) lista.get(position);
            LinearLayoutCompat.LayoutParams param = new LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT, (int) (((rv.getHeight()) - (padalto * densidad)) / filas));
            main.setLayoutParams(param);

            imagen.setImageResource(gridModel.getDrawable());
            imagen.setPadding(padancho, padalto, padancho, 0);

            if (!gridModel.isSinTexto()) {
                nombre.setText(gridModel.getNombre());
                nombre.setTextSize(sizeT);
                nombre.setPadding(padtxt, 0, padtxt, 0);
            } else {
                gone(nombre);
                imagen.setPadding(padancho, padalto, padancho, padalto);
            }

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    public class GridModel {

        private int drawable;
        private String nombre;
        private boolean sinTexto;

        public GridModel(int drawable, String nombre) {
            this.drawable = drawable;
            this.nombre = nombre;
            sinTexto = false;
        }

        public GridModel(int drawable, String nombre, boolean sinTexto) {
            this.drawable = drawable;
            this.nombre = nombre;
            this.sinTexto = sinTexto;
        }

        public int getDrawable() {
            return drawable;
        }

        public void setDrawable(int drawable) {
            this.drawable = drawable;
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
