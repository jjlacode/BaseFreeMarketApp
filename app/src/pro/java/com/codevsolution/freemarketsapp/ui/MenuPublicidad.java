package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;

import com.codevsolution.base.chat.FragmentChatBase;
import com.google.firebase.auth.FirebaseAuth;
import com.codevsolution.base.android.FragmentGrid;
import com.codevsolution.base.web.FragmentWebView;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.FREELANCE;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.HTTPAYUDA;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODFREELANCE;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODPROVCAT;

public class MenuPublicidad extends FragmentGrid {

    private String altaProdProvWeb;
    private String clientes;
    private String chat;
    private String notas;
    private String calendarioNotas;
    private String salir;
    private String home;
    private String ayuda;
    private String publicidadFreelance;

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

        altaProdProvWeb = getString(R.string.productos_proveedor);
        clientes = getString(R.string.clientes);
        chat = getString(R.string.chat);
        notas = getString(R.string.notas);
        calendarioNotas = getString(R.string.calendario_notas);
        salir = getString(R.string.salir);
        home = getString(R.string.inicio);
        ayuda = getString(R.string.ayuda);
        publicidadFreelance = getString(R.string.publicidad_freelance);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_evento_indigo, altaProdProvWeb));
        lista.add(new GridModel(R.drawable.ic_clientes_indigo, clientes));
        lista.add(new GridModel(R.drawable.ic_chat_indigo, chat));
        lista.add(new GridModel(R.drawable.ic_lista_notas_indigo, notas));
        lista.add(new GridModel(R.drawable.ic_lista_eventos_indigo, calendarioNotas));
        lista.add(new GridModel(R.drawable.ic_catalogo_indigo, publicidadFreelance));
        lista.add(new GridModel(R.drawable.ic_ayuda_indigo, ayuda));
        lista.add(new GridModel(R.drawable.ic_inicio_black_24dp, home));
        lista.add(new GridModel(R.drawable.ic_salir_rojo, salir));

    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();

        if (nombre.equals(altaProdProvWeb)) {

            bundle = new Bundle();
            bundle.putString(TIPO, PRODPROVCAT);
            bundle.putString(PERFIL, PRODPROVCAT);
            bundle.putString(TITULO, getString(R.string.productos_proveedor));

            icFragmentos.enviarBundleAFragment(bundle, new AltaProductosCli());

        } else if (nombre.equals(clientes)) {

            bundle = new Bundle();
            bundle.putString(TIPO, PRODFREELANCE);
            bundle.putString(PERFIL, FREELANCE);
            bundle.putString(TITULO, getString(R.string.servicios_freelance));

            icFragmentos.enviarBundleAFragment(bundle, new AltaProductosCli());

        } else if (nombre.equals(notas)) {



        } else if (nombre.equals(calendarioNotas)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new Notas()).addToBackStack(null).commit();

        } else if (nombre.equals(publicidadFreelance)) {


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
                    .replace(R.id.content_main, new FragmentChatBase()).addToBackStack(null).commit();

        }


    }


}
