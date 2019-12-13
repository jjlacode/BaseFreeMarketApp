package com.codevsolution.freemarketsapp.ui;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTOCLI;

public class ListadoProductosCli extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb {

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        location = true;
        super.setOnCreateView(view, inflater, container);
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

        gone(btnClonar);
    }

    @Override
    protected String setTipo() {
        return PRODUCTOCLI;
    }

    @Override
    protected String setTipoForm() {
        return LISTA;
    }



}
