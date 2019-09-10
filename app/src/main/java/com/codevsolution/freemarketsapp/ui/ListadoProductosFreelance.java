package com.codevsolution.freemarketsapp.ui;


import com.codevsolution.base.util.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.codevsolution.freemarketsapp.R;

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
