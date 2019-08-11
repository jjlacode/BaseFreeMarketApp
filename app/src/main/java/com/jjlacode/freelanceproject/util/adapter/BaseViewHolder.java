package com.jjlacode.freelanceproject.util.adapter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.jjlacode.freelanceproject.util.crud.ListaModelo;
import com.jjlacode.freelanceproject.util.crud.Modelo;

import java.util.ArrayList;


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
