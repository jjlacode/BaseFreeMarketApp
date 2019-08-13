package com.jjlacode.freelanceproject.ui;

import com.jjlacode.um.base.ui.FragmentPublicidad;

public class PublicidadEcommerce extends FragmentPublicidad {
    @Override
    protected boolean setPagado() {
        return true;
    }

    @Override
    protected String setTipoIdUser() {
        return IDECOMMERCE;
    }

    @Override
    protected String setTipo() {
        return ECOMMERCE;
    }
}
