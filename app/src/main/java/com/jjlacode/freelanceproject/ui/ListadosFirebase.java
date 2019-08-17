package com.jjlacode.freelanceproject.ui;

import com.jjlacode.base.util.nosql.FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb;

public class ListadosFirebase extends FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb {

    @Override
    protected void accionesImagen() {

        imagen.setVisibleBtn();
        imagen.setImageFirestore(firebaseFormBase.getIdchatBase() + tipo);

        System.out.println("Acciones listados firebase");
    }

}
