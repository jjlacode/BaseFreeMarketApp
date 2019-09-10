package com.jjlacode.freelanceproject.ui;


import com.jjlacode.base.util.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.jjlacode.freelanceproject.R;

public class ListadoProductosFreelance extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb {

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

        referencia.setHint(getString(R.string.refsevicio));
    }

    @Override
    protected String setTipoForm() {
        return LISTA;
    }


}
