package com.codevsolution.freemarketsapp.ui;


import com.codevsolution.base.android.FragmentBase;

public class MisSorteosPro extends ListadoProductosMisSorteos {

    @Override
    protected String setTipo() {
        return SORTEOPRO;
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

}
