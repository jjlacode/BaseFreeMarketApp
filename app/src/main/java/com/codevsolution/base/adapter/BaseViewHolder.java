package com.codevsolution.base.adapter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.ModeloSQL;

import java.util.ArrayList;


public class BaseViewHolder extends RecyclerView.ViewHolder {


    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(ModeloSQL modeloSQL) {

    }

    public void bind(ListaModelo lista, int position) {

    }

    public void bind(ArrayList<?> lista, int position) {

    }

    public Context getContext() {
        return itemView.getContext();

    }


}
