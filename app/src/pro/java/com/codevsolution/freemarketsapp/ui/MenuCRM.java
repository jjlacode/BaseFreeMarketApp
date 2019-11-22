package com.codevsolution.freemarketsapp.ui;

import android.view.View;

import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.HTTPAYUDA;

public class MenuCRM extends FragmentGridImagen {

    private String proximosEventos;
    private String clientes;
    private String eventos;
    private String notas;
    private String listas;
    private String calendarioNotas;

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

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_clientes_indigo, clientes));
        lista.add(new GridModel(R.drawable.ic_lista_notas_indigo, listas));
        lista.add(new GridModel(R.drawable.ic_lista_eventos_indigo, eventos));
        lista.add(new GridModel(R.drawable.ic_evento_indigo, proximosEventos));
        lista.add(new GridModel(R.drawable.ic_nueva_nota_indigo, notas));
        lista.add(new GridModel(R.drawable.ic_lista_eventos_indigo, calendarioNotas));

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

        }


    }


}
