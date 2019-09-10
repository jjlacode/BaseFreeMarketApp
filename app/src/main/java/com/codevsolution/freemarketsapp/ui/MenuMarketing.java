package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.codevsolution.base.util.android.FragmentGrid;
import com.codevsolution.base.util.web.FragmentWebView;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

public class MenuMarketing extends FragmentGrid {

    private String campanias;
    private String clientes;
    private String chat;
    private String notas;
    private String calendarioNotas;
    private String salir;
    private String home;
    private String ayuda;
    private String publicidad;

    @Override
    protected void setContext() {
        contexto = getContext();
    }

    @Override
    protected String setAyudaWeb() {
        return HTTPAYUDA + "crm";
    }

    @Override
    protected void setLista() {

        campanias = getString(R.string.proximos_eventos);
        clientes = getString(R.string.clientes);
        chat = getString(R.string.chat);
        notas = getString(R.string.notas);
        calendarioNotas = getString(R.string.calendario_notas);
        salir = getString(R.string.salir);
        home = getString(R.string.inicio);
        ayuda = getString(R.string.ayuda);
        publicidad = getString(R.string.publicidad);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_evento_indigo, campanias));
        lista.add(new GridModel(R.drawable.ic_clientes_indigo, clientes));
        lista.add(new GridModel(R.drawable.ic_chat_indigo, chat));
        lista.add(new GridModel(R.drawable.ic_lista_notas_indigo, notas));
        lista.add(new GridModel(R.drawable.ic_lista_eventos_indigo, calendarioNotas));
        lista.add(new GridModel(R.drawable.ic_catalogo_indigo, publicidad));
        lista.add(new GridModel(R.drawable.ic_ayuda_indigo, ayuda));
        lista.add(new GridModel(R.drawable.ic_inicio_black_24dp, home));
        lista.add(new GridModel(R.drawable.ic_salir_rojo, salir));

    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();

        if (nombre.equals(campanias)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new CalendarioEventos()).addToBackStack(null).commit();
        } else if (nombre.equals(clientes)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentCRUDCliente()).addToBackStack(null).commit();
        } else if (nombre.equals(notas)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentCRUDNota()).addToBackStack(null).commit();

        } else if (nombre.equals(calendarioNotas)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new Notas()).addToBackStack(null).commit();

        } else if (nombre.equals(publicidad)) {

            icFragmentos.enviarBundleAFragment(bundle, new MenuPublicidad());

        } else if (nombre.equals(home)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuInicio()).addToBackStack(null).commit();

        } else if (nombre.equals(salir)) {

            FirebaseAuth.getInstance().signOut();
            activityBase.finish();

        } else if (nombre.equals(ayuda)) {

            bundle = new Bundle();
            putBundle(WEB, ayudaWeb);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentWebView());

        } else if (nombre.equals(chat)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentChat()).addToBackStack(null).commit();

        }


    }


}