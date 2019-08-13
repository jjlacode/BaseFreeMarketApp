package com.jjlacode.freelanceproject.ui;

import com.jjlacode.um.base.ui.FragmentPublicidad;

public class PublicidadProveedorWeb extends FragmentPublicidad {
    @Override
    protected boolean setPagado() {
        return true;
    }

    @Override
    protected String setTipoIdUser() {
        return IDPROVEEDORWEB;
    }

    @Override
    protected String setTipo() {
        return PROVEEDORWEB;
    }
}
