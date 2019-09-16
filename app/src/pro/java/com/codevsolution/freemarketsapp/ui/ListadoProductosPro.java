package com.codevsolution.freemarketsapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTOCLI;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTOPRO;

public class ListadoProductosPro extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb {

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        location = true;
        super.setOnCreateView(view, inflater, container);
    }

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
        return LISTA;
    }

    @Override
    protected void setDatos() {

        visible(descuento);
        gone(sincronizaClon);
        visible(btnClonar);

        super.setDatos();
    }

    @Override
    protected void onFirebaseFormBase() {
        super.onFirebaseFormBase();

        btnClonar.setEnabled(true);
    }

    @Override
    protected String setTipoProdClon() {
        return PRODUCTOCLI;
    }
}
