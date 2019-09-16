package com.codevsolution.freemarketsapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb;
import com.codevsolution.freemarketsapp.R;

public class ListadosPerfilesFirebasePro extends FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb {

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        location = true;
        super.setOnCreateView(view, inflater, container);
        titulo = getString(R.string.proveedores);

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
    protected void setDatos() {
        suscripcion.setText(R.string.suscripcion_pro);
        super.setDatos();
    }

    @Override
    protected String setTipo() {
        return PRO;
    }

    @Override
    protected String setTipoForm() {
        return LISTA;
    }
}
