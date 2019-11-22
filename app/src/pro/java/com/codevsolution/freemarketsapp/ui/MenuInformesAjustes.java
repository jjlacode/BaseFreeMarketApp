package com.codevsolution.freemarketsapp.ui;

import android.view.View;

import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.HTTPAYUDA;

public class MenuInformesAjustes extends FragmentGridImagen {

    private String informes;
    private String diario;
    private String perfilesUser;
    private String estadisticas;

    @Override
    protected void setContext() {
        contexto = getContext();
    }


    @Override
    protected void setLista() {

        informes = getString(R.string.informes);
        estadisticas = getString(R.string.estadisticas);
        diario = getString(R.string.diario);
        perfilesUser = getString(R.string.perfil_freelance);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_informes_indigo, informes));
        lista.add(new GridModel(R.drawable.ic_estadisticas_indigo, estadisticas));
        lista.add(new GridModel(R.drawable.ic_registro_indigo, diario));
        lista.add(new GridModel(R.drawable.ic_configuracion_indigo, perfilesUser));

    }

    @Override
    protected String setAyudaWeb() {
        return HTTPAYUDA + "informes-ajustes";
    }

    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();

        if (nombre.equals(informes)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new Informes()).addToBackStack(null).commit();
        } else if (nombre.equals(estadisticas)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new Notas()).addToBackStack(null).commit();

        } else if (nombre.equals(diario)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new Diario()).addToBackStack(null).commit();

        } else if (nombre.equals(perfilesUser)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentCRUDPerfil()).addToBackStack(null).commit();

        }


    }


}
