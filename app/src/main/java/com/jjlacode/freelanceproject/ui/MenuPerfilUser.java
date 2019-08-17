package com.jjlacode.freelanceproject.ui;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.jjlacode.base.util.android.FragmentGrid;
import com.jjlacode.base.util.web.FragmentWebView;
import com.jjlacode.freelanceproject.R;

import java.util.ArrayList;

public class MenuPerfilUser extends FragmentGrid {

    private String um;
    private String crm;
    private String marketing;
    private String proyectos;
    private String facturacion;
    private String contabilidad;
    private String ajustes;
    private String ayuda;
    private String salir;

    @Override
    protected void setContext() {
        contexto = getContext();
    }

    @Override
    protected String setAyudaWeb() {
        return HTTPAYUDA + "inicio";
    }

    @Override
    protected void setLista() {

        um = getString(R.string.union_market);
        crm = getString(R.string.crm);
        marketing = getString(R.string.marketing);
        proyectos = getString(R.string.proyectos);
        facturacion = getString(R.string.facturacion);
        contabilidad = getString(R.string.contabilidad);
        ajustes = getString(R.string.informesyajustes);
        ayuda = getString(R.string.ayuda);
        salir = getString(R.string.salir);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.logo, um));
        lista.add(new GridModel(R.drawable.ic_clientes_indigo, crm));
        lista.add(new GridModel(R.drawable.ic_marketing_indigo, marketing));
        lista.add(new GridModel(R.drawable.ic_proy_curso_indigo, proyectos));
        lista.add(new GridModel(R.drawable.ic_lista_notas_indigo, facturacion));
        lista.add(new GridModel(R.drawable.ic_cobros_indigo, contabilidad));
        lista.add(new GridModel(R.drawable.ic_tareas_indigo, ajustes));
        lista.add(new GridModel(R.drawable.ic_ayuda_indigo, ayuda));
        lista.add(new GridModel(R.drawable.ic_salir_rojo, salir));

    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();


        if (nombre.equals(crm)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuCRM()).addToBackStack(null).commit();
        } else if (nombre.equals(proyectos)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuGestionProyectos()).addToBackStack(null).commit();
        } else if (nombre.equals(facturacion)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuFacturacion()).addToBackStack(null).commit();

        } else if (nombre.equals(contabilidad)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuContabilidad()).addToBackStack(null).commit();


        } else if (nombre.equals(ajustes)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuInformesAjustes()).addToBackStack(null).commit();


        } else if (nombre.equals(marketing)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuMarketing()).addToBackStack(null).commit();


        } else if (nombre.equals(um)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new UnionMarket()).addToBackStack(null).commit();


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
