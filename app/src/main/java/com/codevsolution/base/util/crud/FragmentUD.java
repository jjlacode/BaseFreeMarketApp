package com.codevsolution.base.util.crud;

import android.content.ContentValues;
import android.widget.Button;
import android.widget.ImageView;

public abstract class FragmentUD extends FragmentBaseCRUD {

    protected Button btndelete;
    protected ImageView imagen;
    protected String path;
    protected ContentValues valores;
    protected String id;

    public FragmentUD() {
    }

    protected boolean update(){
        return false;
    }

    protected abstract void delete();

    @Override
    public void onResume() {

        bundle = getArguments();

        if (bundle != null) {
            origen = bundle.getString(ORIGEN);
            subTitulo = bundle.getString(SUBTITULO);
        }

        cargarBundle();

            if (id != null) {

                setDatos();

            }

        super.onResume();
    }



}
