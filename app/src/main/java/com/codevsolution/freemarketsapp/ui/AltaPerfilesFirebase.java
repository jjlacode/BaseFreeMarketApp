package com.codevsolution.freemarketsapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb;
import com.codevsolution.freemarketsapp.sqlite.ContratoPry;

public abstract class AltaPerfilesFirebase extends FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb implements ContratoPry.Tablas {

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        location = true;
        super.setOnCreateView(view, inflater, container);
    }

    @Override
    protected void selector() {
        super.selector();
        activityBase.fabInicio.hide();

    }


}
