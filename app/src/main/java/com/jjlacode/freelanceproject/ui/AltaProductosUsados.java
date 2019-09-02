package com.jjlacode.freelanceproject.ui;

import com.jjlacode.base.util.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;

public class AltaProductosUsados extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb {

    @Override
    protected String setTipo() {
        return USADO;
    }

    @Override
    protected String setPerfil() {
        return CLIENTEWEB;
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
