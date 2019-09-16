package com.codevsolution.freemarketsapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codevsolution.freemarketsapp.R;

public class ListadoSorteosPro extends ListadoProductosSorteos {

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);
        titulo = getString(R.string.sorteos_pro);
    }

    @Override
    protected String setTipo() {
        return SORTEOPRO;
    }
}
