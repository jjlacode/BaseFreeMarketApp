package com.jjlacode.freelanceproject.ui;


import com.jjlacode.base.util.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.jjlacode.freelanceproject.logica.Interactor;

public class ListadosProductosUsados extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb
        implements Interactor.ConstantesPry {


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


}
