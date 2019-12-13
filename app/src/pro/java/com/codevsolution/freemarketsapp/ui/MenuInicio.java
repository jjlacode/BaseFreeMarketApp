package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;

import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.MENUCRM;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.MENUMARKETING;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.MENUUM;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTO;

public class MenuInicio extends FragmentGridImagen {

    private String um;
    private String crm;
    private String marketing;
    private String proyectos;
    private String ajustes;
    private String productos;

    @Override
    protected void setContext() {
        contexto = getContext();
    }

    @Override
    protected String setAyudaWeb() {
        return "inicio";
    }

    @Override
    protected void setInicio() {
        super.setInicio();

        icFragmentos.showSubTitle(R.string.inicio);
        reproducir(getString(R.string.inicio));
    }

    @Override
    protected void setLista() {

        um = getString(R.string.union_market);
        crm = getString(R.string.crm);
        marketing = getString(R.string.marketing);
        proyectos = getString(R.string.proyectos);
        ajustes = getString(R.string.informesyajustes);
        productos = getString(R.string.productos);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.logo, um));
        lista.add(new GridModel(R.drawable.ic_producto_indigo, productos));
        lista.add(new GridModel(R.drawable.ic_marketing_indigo, marketing));
        lista.add(new GridModel(R.drawable.ic_clientes_indigo, crm));
        lista.add(new GridModel(R.drawable.ic_proy_curso_indigo, proyectos));
        lista.add(new GridModel(R.drawable.ic_ajustes_black_24dp, ajustes));

    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();


        if (nombre.equals(crm)) {

            //activityBase.getSupportFragmentManager().beginTransaction()
            //        .replace(R.id.content_main, new MenuCRM()).addToBackStack(null).commit();
            bundle = new Bundle();
            bundle.putString(ACTUAL, MENUCRM);
            icFragmentos.enviarBundleAFragment(bundle, new MenuCRM());

        } else if (nombre.equals(proyectos)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuGestionProyectos()).addToBackStack(null).commit();

        } else if (nombre.equals(ajustes)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuInformesAjustes()).addToBackStack(null).commit();

        } else if (nombre.equals(marketing)) {

            //activityBase.getSupportFragmentManager().beginTransaction()
            //        .replace(R.id.content_main, new MenuMarketing()).addToBackStack(null).commit();
            bundle = new Bundle();
            bundle.putString(ACTUAL, MENUMARKETING);
            icFragmentos.enviarBundleAFragment(bundle, new MenuMarketing());

        } else if (nombre.equals(um)) {

            //activityBase.getSupportFragmentManager().beginTransaction()
            //        .replace(R.id.content_main, new UnionMarket()).addToBackStack(null).commit();
            bundle = new Bundle();
            bundle.putString(ACTUAL, MENUUM);
            icFragmentos.enviarBundleAFragment(bundle, new UnionMarket());

        } else if (nombre.equals(productos)) {

            bundle = new Bundle();
            bundle.putString(ACTUAL, PRODUCTO);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProducto());


        }


    }


}
