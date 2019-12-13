package com.codevsolution.freemarketsapp.ui;

import com.codevsolution.base.android.FragmentBase;

public class AltaPerfilesFirebaseCli extends AltaPerfilesFirebase {

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

    }

    @Override
    protected String setTipo() {
        return CLI;
    }

    @Override
    protected String setTipoForm() {
        return NUEVO;
    }
}
