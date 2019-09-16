package com.codevsolution.freemarketsapp.ui;

import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.codevsolution.freemarketsapp.logica.Interactor;

public class AltaProductosPro extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb
        implements Interactor.ConstantesPry {


    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

    }

    @Override
    protected String setTipo() {
        return PRODUCTOPRO;
    }

    @Override
    protected String setTipoForm() {
        return NUEVO;
    }

    @Override
    protected String setTipoSorteo() {
        return SORTEOPRO;
    }
}
