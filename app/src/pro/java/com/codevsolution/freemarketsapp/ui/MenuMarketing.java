package com.codevsolution.freemarketsapp.ui;

import android.view.View;

import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.base.chat.FragmentChatBase;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.HTTPAYUDA;

public class MenuMarketing extends FragmentGridImagen {

    private String campanias;
    private String clientes;
    private String chat;
    private String notas;
    private String calendarioNotas;
    private String sorteos;
    private String sorteosPro;

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
        sorteos = getString(R.string.sorteos);
        sorteosPro = getString(R.string.sorteos_pro);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_evento_indigo, campanias));
        lista.add(new GridModel(R.drawable.ic_clientes_indigo, clientes));
        lista.add(new GridModel(R.drawable.ic_chat_indigo, chat));
        lista.add(new GridModel(R.drawable.ic_lista_notas_indigo, notas));
        lista.add(new GridModel(R.drawable.ic_lista_eventos_indigo, calendarioNotas));
        lista.add(new GridModel(R.drawable.ic_sorteo, sorteos));
        lista.add(new GridModel(R.drawable.ic_sorteo, sorteosPro));


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

        } else if (nombre.equals(sorteos)) {

            icFragmentos.enviarBundleAFragment(bundle, new AltaSorteosCli());

        } else if (nombre.equals(sorteosPro)) {

            icFragmentos.enviarBundleAFragment(bundle, new AltaSorteosPro());

        } else if (nombre.equals(chat)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentChatBase()).addToBackStack(null).commit();

        }


    }


}
