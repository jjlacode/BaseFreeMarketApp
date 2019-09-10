package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.codevsolution.base.util.android.FragmentGrid;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

public class UnionMarket extends FragmentGrid {

    private String clientesWeb;
    private String freelance;
    private String freelanceDemanda;
    private String proveedoresWeb;
    private String comercios;
    private String trabajoOferta;
    private String trabajoDemanda;
    private String usado;
    private String salir;

    @Override
    protected void setContext() {
        contexto = getContext();
    }


    @Override
    protected void setLista() {

        clientesWeb = getString(R.string.clientesweb);
        freelance = getString(R.string.freelance);
        freelanceDemanda = getString(R.string.demanda_freelance);
        proveedoresWeb = getString(R.string.proveedoresweb);
        comercios = getString(R.string.comercios);
        trabajoOferta = getString(R.string.trabajo_oferta);
        trabajoDemanda = getString(R.string.trabajo_demanda);
        usado = getString(R.string.usado);
        salir = getString(R.string.salir);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_clientes_indigo, clientesWeb));
        lista.add(new GridModel(R.drawable.ic_comercial_indigo, freelance));
        lista.add(new GridModel(R.drawable.logofp, freelanceDemanda));
        lista.add(new GridModel(R.drawable.ic_proveedor_indigo, proveedoresWeb));
        lista.add(new GridModel(R.drawable.ic_producto_indigo, comercios));
        lista.add(new GridModel(R.drawable.ic_usado_indigo, usado));
        lista.add(new GridModel(R.drawable.ic_oferta_indigo, trabajoOferta));
        lista.add(new GridModel(R.drawable.ic_tareas_indigo, trabajoDemanda));
        lista.add(new GridModel(R.drawable.ic_salir_rojo, salir));

    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();

        if (nombre.equals(clientesWeb)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuCRM()).addToBackStack(null).commit();
        } else if (nombre.equals(freelance)) {
            bundle = new Bundle();
            putBundle(TIPO, FREELANCE);
            putBundle(PERFIL, FREELANCE);
            putBundle(TITULO, getString(R.string.perfil_freelance));
            icFragmentos.enviarBundleAFragment(bundle, new ListadosPerfilesFirebase());

        } else if (nombre.equals(freelanceDemanda)) {

            bundle = new Bundle();
            putBundle(TIPO, PRODFREELANCE);
            putBundle(PERFIL, FREELANCE);
            putBundle(TITULO, getString(R.string.servicios_freelance));
            icFragmentos.enviarBundleAFragment(bundle, new ListadoProductosFreelance());



        } else if (nombre.equals(proveedoresWeb)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new ListadoProductos()).addToBackStack(null).commit();

        } else if (nombre.equals(comercios)) {

            bundle = new Bundle();
            putBundle(TIPO, SORTEO);
            putBundle(PERFIL, SORTEO);
            putBundle(TITULO, getString(R.string.sorteos));
            icFragmentos.enviarBundleAFragment(bundle, new AltaProductosSorteos());


        } else if (nombre.equals(trabajoOferta)) {

            bundle = new Bundle();
            putBundle(TIPO, SORTEO);
            putBundle(PERFIL, SORTEO);
            putBundle(TITULO, getString(R.string.sorteos));
            icFragmentos.enviarBundleAFragment(bundle, new ListadoProductosSorteos());



        } else if (nombre.equals(trabajoDemanda)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuInformesAjustes()).addToBackStack(null).commit();


        } else if (nombre.equals(usado)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuInformesAjustes()).addToBackStack(null).commit();


        } else if (nombre.equals(salir)) {

            FirebaseAuth.getInstance().signOut();
            activityBase.finish();

        }


    }


}
