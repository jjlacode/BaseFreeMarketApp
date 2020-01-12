package com.codevsolution.freemarketsapp.ui;

import android.view.View;

import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.base.file.filemanager.FileManagerMain;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

public class MenuInformesAjustes extends FragmentGridImagen {

    private String informes;
    private String diario;
    private String perfilesUser;
    private String estadisticas;
    private String dataBase;
    private String fileManager;

    @Override
    protected void setContext() {
        contexto = getContext();
    }

    @Override
    protected String setAyudaWeb() {
        return "informes-y-ajustes";
    }

    @Override
    protected void setInicio() {
        super.setInicio();

        icFragmentos.showSubTitle(R.string.informesyajustes);
        reproducir(getString(R.string.informesyajustes));
    }


    @Override
    protected void setLista() {

        informes = getString(R.string.informes);
        estadisticas = getString(R.string.estadisticas);
        diario = getString(R.string.diario);
        perfilesUser = getString(R.string.perfil_freelance);
        dataBase = getString(R.string.basedatosutil);
        fileManager = getString(R.string.filemanager);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_informes_indigo, informes));
        lista.add(new GridModel(R.drawable.ic_estadisticas_indigo, estadisticas));
        lista.add(new GridModel(R.drawable.ic_registro_indigo, diario));
        lista.add(new GridModel(R.drawable.ic_configuracion_indigo, perfilesUser));
        lista.add(new GridModel(R.drawable.ic_database_indigo, dataBase));
        lista.add(new GridModel(R.drawable.ic_lista_notas_indigo, fileManager));

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

        } else if (nombre.equals(dataBase)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuDB()).addToBackStack(null).commit();

        } else if (nombre.equals(fileManager)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FileManagerMain()).addToBackStack(null).commit();

        }


    }

    @Override
    public void setOnClickRV(Object object) {

    }


}
