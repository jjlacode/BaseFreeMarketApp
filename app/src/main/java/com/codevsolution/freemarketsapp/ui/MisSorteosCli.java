package com.codevsolution.freemarketsapp.ui;

import com.codevsolution.base.android.FragmentBase;

public class MisSorteosCli extends ListadoProductosMisSorteos {
    @Override
    protected String setTipo() {
        return SORTEOCLI;
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }
}
