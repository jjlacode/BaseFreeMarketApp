package com.codevsolution.freemarketsapp.ui;

import com.codevsolution.base.android.FragmentBase;

public class AltaSorteosCli extends AltaProductosSorteos {
    @Override
    protected String setTipo() {
        return SORTEOCLI;
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }
}
