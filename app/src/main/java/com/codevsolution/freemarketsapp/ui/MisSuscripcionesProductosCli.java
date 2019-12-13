package com.codevsolution.freemarketsapp.ui;

import com.codevsolution.base.android.FragmentBase;

public class MisSuscripcionesProductosCli extends ListadoProductosMisSuscripciones {
    @Override
    protected String setTipo() {
        return PRODUCTOCLI;
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }
}
