package com.codevsolution.freemarketsapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb;
import com.codevsolution.freemarketsapp.R;

public class ListadosPerfilesFirebaseCli extends FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb {

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        location = true;
        super.setOnCreateView(view, inflater, container);
        titulo = getString(R.string.clientes);

    }

    @Override
    protected void accionesImagen() {

        imagen.setVisibleBtn();
        imagen.setImageFirestore(firebaseFormBase.getIdchatBase());

        System.out.println("Acciones listados firebase");

    }

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

    }

    @Override
    protected String setTipo() {
        return CLI;
    }

    @Override
    protected String setTipoForm() {
        return LISTA;
    }
}
