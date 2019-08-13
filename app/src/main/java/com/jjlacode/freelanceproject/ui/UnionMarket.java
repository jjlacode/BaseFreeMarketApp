package com.jjlacode.freelanceproject.ui;

import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.jjlacode.base.util.android.FragmentGrid;
import com.jjlacode.freelanceproject.R;

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
        lista.add(new GridModel(R.drawable.ic_freelance_indigo, freelance));
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
                    .replace(R.id.content_main, new FragmentCRM()).addToBackStack(null).commit();
        } else if (nombre.equals(freelance)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new Freelancers()).addToBackStack(null).commit();
        } else if (nombre.equals(freelanceDemanda)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentInformesAjustes()).addToBackStack(null).commit();


        } else if (nombre.equals(proveedoresWeb)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentFacturacion()).addToBackStack(null).commit();

        } else if (nombre.equals(comercios)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentContabilidad()).addToBackStack(null).commit();


        } else if (nombre.equals(trabajoOferta)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentInformesAjustes()).addToBackStack(null).commit();


        } else if (nombre.equals(trabajoDemanda)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentInformesAjustes()).addToBackStack(null).commit();


        } else if (nombre.equals(usado)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentInformesAjustes()).addToBackStack(null).commit();


        } else if (nombre.equals(salir)) {

            FirebaseAuth.getInstance().signOut();
            activityBase.finish();

        }


    }


}
