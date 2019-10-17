package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;

import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.base.web.FragmentWebView;
import com.codevsolution.freemarketsapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.HTTPAYUDA;

public class MenuInformesAjustes extends FragmentGridImagen {

    private String perfiles;
    private String informes;
    private String ayuda;
    private String diario;
    private String salir;
    private String perfilesUser;
    private String home;
    private String estadisticas;

    @Override
    protected void setContext() {
        contexto = getContext();
    }


    @Override
    protected void setLista() {

        perfiles = getString(R.string.proximos_eventos);
        informes = getString(R.string.informes);
        ayuda = getString(R.string.ayuda);
        estadisticas = getString(R.string.estadisticas);
        diario = getString(R.string.diario);
        salir = getString(R.string.salir);
        perfilesUser = getString(R.string.perfil_freelance);
        home = getString(R.string.inicio);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_evento_indigo, perfiles));
        lista.add(new GridModel(R.drawable.ic_informes_indigo, informes));
        lista.add(new GridModel(R.drawable.ic_estadisticas_indigo, estadisticas));
        lista.add(new GridModel(R.drawable.ic_registro_indigo, diario));
        lista.add(new GridModel(R.drawable.ic_configuracion_indigo, perfilesUser));
        lista.add(new GridModel(R.drawable.ic_ayuda_indigo, ayuda));
        lista.add(new GridModel(R.drawable.ic_inicio_black_24dp, home));
        lista.add(new GridModel(R.drawable.ic_salir_rojo, salir));

    }

    @Override
    protected String setAyudaWeb() {
        return HTTPAYUDA + "informes-ajustes";
    }

    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();

        if (nombre.equals(perfiles)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentCRUDPerfil()).addToBackStack(null).commit();
        } else if (nombre.equals(informes)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new Informes()).addToBackStack(null).commit();
        } else if (nombre.equals(estadisticas)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new Notas()).addToBackStack(null).commit();

        } else if (nombre.equals(ayuda)) {

            bundle = new Bundle();
            putBundle(WEB, ayudaWeb);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentWebView());

        } else if (nombre.equals(diario)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new Diario()).addToBackStack(null).commit();

        } else if (nombre.equals(perfilesUser)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentCRUDPerfil()).addToBackStack(null).commit();

        } else if (nombre.equals(home)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuInicio()).addToBackStack(null).commit();

        } else if (nombre.equals(salir)) {

            FirebaseAuth.getInstance().signOut();
            activityBase.finish();

        }


    }


}
