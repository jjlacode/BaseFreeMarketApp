package com.jjlacode.freelanceproject.ui;


import com.jjlacode.base.util.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;

public class ListadoProductosComercial extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb {

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

    }

    @Override
    protected String setTipoForm() {
        return LISTA;
    }

    @Override
    protected String setTipo() {
        return PRODCOMERCIAL;
    }

    @Override
    protected String setPerfil() {
        return COMERCIAL;
    }
}
