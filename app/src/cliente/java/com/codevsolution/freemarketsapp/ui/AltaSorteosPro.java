package com.codevsolution.freemarketsapp.ui;

import com.codevsolution.base.android.FragmentBase;

public class AltaSorteosPro extends AltaProductosSorteos {
    @Override
    protected String setTipo() {
        return SORTEOPRO;
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }
}
