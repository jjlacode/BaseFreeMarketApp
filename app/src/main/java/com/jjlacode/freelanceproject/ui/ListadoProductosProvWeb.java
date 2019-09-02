package com.jjlacode.freelanceproject.ui;

import com.jjlacode.base.util.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;

public class ListadoProductosProvWeb extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb {


    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {


    }

    @Override
    protected String setTipo() {
        return PRODPROVCAT;
    }

    @Override
    protected String setPerfil() {
        return PROVEEDORWEB;
    }

    @Override
    protected String setTipoForm() {
        return LISTA;
    }


}
