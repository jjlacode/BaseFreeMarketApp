package com.codevsolution.freemarketsapp.ui;

import com.codevsolution.base.android.FragmentBase;

public class MisSuscripcionesPro extends ListadoProductosMisSuscripciones {
    @Override
    protected String setTipo() {
        return PRO;
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }
}
