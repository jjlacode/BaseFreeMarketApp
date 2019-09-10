package com.codevsolution.freemarketsapp.ui;

import com.codevsolution.base.util.nosql.FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb;

public class ListadosPerfilesFirebase extends FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb {

    @Override
    protected void accionesImagen() {

        imagen.setVisibleBtn();
        imagen.setImageFirestore(firebaseFormBase.getIdchatBase() + tipo);

        System.out.println("Acciones listados firebase");

    }

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
