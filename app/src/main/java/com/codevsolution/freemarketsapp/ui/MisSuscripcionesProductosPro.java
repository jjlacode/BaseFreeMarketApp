package com.codevsolution.freemarketsapp.ui;


import com.codevsolution.base.android.FragmentBase;

public class MisSuscripcionesProductosPro extends ListadoProductosMisSuscripciones {

    @Override
    protected String setTipo() {
        return PRODUCTOPRO;
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

}
