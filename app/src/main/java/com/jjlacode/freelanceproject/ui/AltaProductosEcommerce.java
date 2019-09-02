package com.jjlacode.freelanceproject.ui;

import com.jjlacode.base.util.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;

public class AltaProductosEcommerce extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb {

    @Override
    protected String setTipo() {
        return PRODECOMMERCE;
    }

    @Override
    protected String setPerfil() {
        return ECOMMERCE;
    }


    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

    }

    @Override
    protected String setTipoForm() {
        return NUEVO;
    }

}
