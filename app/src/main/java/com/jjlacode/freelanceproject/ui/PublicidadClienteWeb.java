package com.jjlacode.freelanceproject.ui;

import com.jjlacode.um.base.ui.FragmentPublicidad;

public class PublicidadClienteWeb extends FragmentPublicidad {
    @Override
    protected boolean setPagado() {
        return true;
    }

    @Override
    protected String setTipoIdUser() {
        return IDCLIENTEWEB;
    }

    @Override
    protected String setTipo() {
        return CLIENTEWEB;
    }
}
