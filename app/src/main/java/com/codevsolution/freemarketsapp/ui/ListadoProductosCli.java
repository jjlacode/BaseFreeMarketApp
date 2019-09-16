package com.codevsolution.freemarketsapp.ui;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTOCLI;

public class ListadoProductosCli extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb {

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
        return PRODUCTOCLI;
    }

    @Override
    protected String setTipoForm() {
        return LISTA;
    }

    @Override
    protected void setDatos() {

        if (prodProv != null && !nuevo) {

            if (nn(prodProv.getIdClon()) && !prodProv.getIdClon().isEmpty()) {
                visible(sincronizaClon);
            } else {
                gone(sincronizaClon);
            }
        }
        super.setDatos();
    }
}
