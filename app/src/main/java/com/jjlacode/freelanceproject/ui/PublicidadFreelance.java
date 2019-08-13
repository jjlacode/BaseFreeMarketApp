package com.jjlacode.freelanceproject.ui;

import com.jjlacode.um.base.ui.FragmentPublicidad;

public class PublicidadFreelance extends FragmentPublicidad {
    @Override
    protected boolean setPagado() {
        return true;
    }

    @Override
    protected String setTipoIdUser() {
        return IDFREELANCE;
    }

    @Override
    protected String setTipo() {
        return FREELANCE;
    }
}
