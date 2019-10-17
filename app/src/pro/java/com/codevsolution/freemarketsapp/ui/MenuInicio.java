package com.codevsolution.freemarketsapp.ui;

import android.view.View;

import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.base.pay.chargebee.SuscripcionesChargebee;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

public class MenuInicio extends FragmentGridImagen {

    private String um;
    private String crm;
    private String marketing;
    private String proyectos;
    private String ajustes;
    private String suscripcion;
    private String altaProd;
    private String altaProdPro;
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
    protected void setLista() {

        um = getString(R.string.union_market);
        crm = getString(R.string.crm);
        marketing = getString(R.string.marketing);
        proyectos = getString(R.string.proyectos);
        ajustes = getString(R.string.informesyajustes);
        suscripcion = getString(R.string.suscripcion);
        altaProd = getString(R.string.alta_productos);
        altaProdPro = getString(R.string.alta_productos_pro);
        productos = getString(R.string.productos);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.logo, um));
        lista.add(new GridModel(R.drawable.ic_lista_notas_indigo, suscripcion));
        lista.add(new GridModel(R.drawable.ic_producto_indigo, productos));
        lista.add(new GridModel(R.drawable.ic_txt_cli, altaProd));
        lista.add(new GridModel(R.drawable.ic_txt_pro, altaProdPro));
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

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuCRM()).addToBackStack(null).commit();

        } else if (nombre.equals(proyectos)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuGestionProyectos()).addToBackStack(null).commit();

        } else if (nombre.equals(ajustes)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuInformesAjustes()).addToBackStack(null).commit();

        } else if (nombre.equals(marketing)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuMarketing()).addToBackStack(null).commit();

        } else if (nombre.equals(um)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new UnionMarket()).addToBackStack(null).commit();

        } else if (nombre.equals(suscripcion)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new SuscripcionesChargebee()).addToBackStack(null).commit();

        } else if (nombre.equals(altaProd)) {


            icFragmentos.enviarBundleAFragment(bundle, new AltaProductosCli());


        } else if (nombre.equals(altaProdPro)) {


            icFragmentos.enviarBundleAFragment(bundle, new AltaProductosPro());


        } else if (nombre.equals(productos)) {


            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProducto());


        }


    }


}
