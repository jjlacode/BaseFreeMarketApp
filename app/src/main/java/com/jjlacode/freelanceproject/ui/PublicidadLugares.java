package com.jjlacode.freelanceproject.ui;

import com.jjlacode.um.base.ui.FragmentPublicidad;

public class PublicidadLugares extends FragmentPublicidad {

    @Override
    protected boolean setPagado() {
        return true;
    }

    @Override
    protected String setTipoIdUser() {
        return IDLUGAR;
    }

    @Override
    protected String setTipo() {
        return LUGAR;
    }
}
