package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.widget.Toast;

import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.base.sqlite.DataBaseBackup;
import com.codevsolution.base.sqlite.DataBaseManager;
import com.codevsolution.base.sqlite.SQLiteUtil;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.MENUDB;

public class MenuDB extends FragmentGridImagen {

    private String restaura;
    private String backup;
    private String manager;


    @Override
    protected void setContext() {
        contexto = getContext();
    }

    @Override
    protected String setAyudaWeb() {
        return "db";
    }

    @Override
    protected void setInicio() {
        super.setInicio();

        icFragmentos.showSubTitle(R.string.inicio);
        reproducir(getString(R.string.inicio));
    }

    @Override
    protected void setLista() {

        restaura = getString(R.string.restauradb);
        backup = getString(R.string.backup);
        manager = getString(R.string.managerbd);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_guardar_indigo, backup));
        lista.add(new GridModel(R.drawable.ic_restdatabase_indigo, restaura));
        lista.add(new GridModel(R.drawable.ic_restdatabase_indigo, manager));


    }

    @Override
    public void setOnClickRV(Object object) {

        GridModel gridModel = (GridModel) object;

        String nombre = gridModel.getNombre();


        if (nombre.equals(backup)) {

            if (SQLiteUtil.BD_backup(null, false)) {
                Toast.makeText(contexto, getString(R.string.copiadb), Toast.LENGTH_SHORT).show();
                bundle = new Bundle();
                icFragmentos.enviarBundleAFragment(bundle, new DataBaseManager());
            }

        } else if (nombre.equals(restaura)) {

            bundle = new Bundle();
            bundle.putString(ACTUAL, MENUDB);
            icFragmentos.enviarBundleAFragment(bundle, new DataBaseBackup());

        } else if (nombre.equals(manager)) {

            bundle = new Bundle();
            icFragmentos.enviarBundleAFragment(bundle, new DataBaseManager());

        }

    }


}
