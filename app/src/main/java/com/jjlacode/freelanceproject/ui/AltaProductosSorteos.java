package com.jjlacode.freelanceproject.ui;

import com.jjlacode.base.util.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;

public class AltaProductosSorteos extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb {

    @Override
    protected String setTipo() {
        return PRODSORTEOS;
    }

    @Override
    protected String setPerfil() {
        return SORTEOS;
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
