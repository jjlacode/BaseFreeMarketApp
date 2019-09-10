package com.jjlacode.freelanceproject.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.jjlacode.base.util.android.AndroidUtil;
import com.jjlacode.base.util.android.FragmentGrid;
import com.jjlacode.base.util.web.FragmentWebView;
import com.jjlacode.freelanceproject.R;

import java.util.ArrayList;

public class MenuInformesAjustes extends FragmentGrid {

    private String perfiles;
    private String informes;
    private String ayuda;
    private String diario;
    private String salir;
    private String perfilesUser;
    private String home;
    private String estadisticas;
    private String cambioPerfil;

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
        perfilesUser = getString(R.string.perfiles_usuario);
        home = getString(R.string.inicio);
        cambioPerfil = getString(R.string.cambioperfil);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_evento_indigo, perfiles));
        lista.add(new GridModel(R.drawable.ic_informes_indigo, informes));
        lista.add(new GridModel(R.drawable.ic_estadisticas_indigo, estadisticas));
        lista.add(new GridModel(R.drawable.ic_registro_indigo, diario));
        lista.add(new GridModel(R.drawable.ic_configuracion_indigo, perfilesUser));
        lista.add(new GridModel(R.drawable.ic_clientes_indigo, cambioPerfil));
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
        } else if (nombre.equals(cambioPerfil)) {

            final CharSequence[] opciones = {getString(R.string.freelance),
                    getString(R.string.clienteweb), getString(R.string.proveedorweb),
                    getString(R.string.comercial), getString(R.string.ecommerce),
                    getString(R.string.empresa), "Cancelar"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
            builder.setTitle("Elige una opción");
            builder.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    if (opciones[which].equals(getString(R.string.freelance))) {
                        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, PERFILUSER, getString(R.string.freelance));
                    } else if (opciones[which].equals(getString(R.string.clienteweb))) {
                        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, PERFILUSER, getString(R.string.clienteweb));
                    } else if (opciones[which].equals(getString(R.string.proveedorweb))) {
                        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, PERFILUSER, getString(R.string.proveedorweb));
                    } else if (opciones[which].equals(getString(R.string.comercial))) {
                        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, PERFILUSER, getString(R.string.comercial));
                    } else if (opciones[which].equals(getString(R.string.ecommerce))) {
                        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, PERFILUSER, getString(R.string.ecommerce));
                    } else if (opciones[which].equals(getString(R.string.empresa))) {
                        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, PERFILUSER, getString(R.string.empresa));
                    } else {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();

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
                    .replace(R.id.content_main, new MenuPerfilUser()).addToBackStack(null).commit();

        } else if (nombre.equals(home)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuInicio()).addToBackStack(null).commit();

        } else if (nombre.equals(salir)) {

            FirebaseAuth.getInstance().signOut();
            activityBase.finish();

        }


    }


}
