package com.jjlacode.freelanceproject.ui;

import com.jjlacode.base.util.nosql.FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb;

public class AltaPerfilesFirebase extends FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb {

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
        return NUEVO;
    }
}
