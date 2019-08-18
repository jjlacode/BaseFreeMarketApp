package com.jjlacode.freelanceproject.ui;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.jjlacode.base.util.android.FragmentGrid;
import com.jjlacode.base.util.web.FragmentWebView;
import com.jjlacode.freelanceproject.R;

import java.util.ArrayList;

public class MenuCRM extends FragmentGrid {

    private String proximosEventos;
    private String clientes;
    private String eventos;
    private String notas;
    private String listas;
    private String calendarioNotas;
    private String salir;
    private String home;
    private String ayuda;

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

        proximosEventos = getString(R.string.proximos_eventos);
        clientes = getString(R.string.clientes);
        notas = getString(R.string.notas);
        listas = getString(R.string.listas);
        eventos = getString(R.string.eventos);
        calendarioNotas = getString(R.string.calendario_notas);
        salir = getString(R.string.salir);
        home = getString(R.string.inicio);
        ayuda = getString(R.string.ayuda);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_clientes_indigo, clientes));
        lista.add(new GridModel(R.drawable.ic_lista_notas_indigo, listas));
        lista.add(new GridModel(R.drawable.ic_evento_indigo, proximosEventos));
        lista.add(new GridModel(R.drawable.ic_nueva_nota_indigo, notas));
        lista.add(new GridModel(R.drawable.ic_lista_eventos_indigo, eventos));
        lista.add(new GridModel(R.drawable.ic_lista_eventos_indigo, calendarioNotas));
        lista.add(new GridModel(R.drawable.ic_ayuda_indigo, ayuda));
        lista.add(new GridModel(R.drawable.ic_inicio_black_24dp, home));
        lista.add(new GridModel(R.drawable.ic_salir_rojo, salir));

    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();

        if (nombre.equals(proximosEventos)) {

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

        } else if (nombre.equals(eventos)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentCRUDEvento()).addToBackStack(null).commit();

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

        }


    }


}