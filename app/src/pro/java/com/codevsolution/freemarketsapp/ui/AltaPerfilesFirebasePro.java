package com.codevsolution.freemarketsapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb;

public class AltaPerfilesFirebasePro extends FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb {

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
        return PRO;
    }

    @Override
    protected String setTipoForm() {
        return NUEVO;
    }
}
