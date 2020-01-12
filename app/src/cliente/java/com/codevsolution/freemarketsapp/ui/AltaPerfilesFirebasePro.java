package com.codevsolution.freemarketsapp.ui;

import com.codevsolution.base.android.FragmentBase;

public class AltaPerfilesFirebasePro extends AltaPerfilesFirebase {

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
        return PRO;
    }

    @Override
    protected String setTipoForm() {
        return NUEVO;
    }
}
