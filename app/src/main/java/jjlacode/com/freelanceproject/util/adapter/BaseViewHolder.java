package jjlacode.com.freelanceproject.util.adapter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.crud.ListaModelo;
import jjlacode.com.freelanceproject.util.crud.Modelo;


public class BaseViewHolder extends RecyclerView.ViewHolder {



        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(Modelo modelo){

        }

        public void bind(ListaModelo lista, int position){

        }

        public void bind(ArrayList<?> lista, int position){

        }

        public Context getContext(){
            return itemView.getContext();

        }


}
