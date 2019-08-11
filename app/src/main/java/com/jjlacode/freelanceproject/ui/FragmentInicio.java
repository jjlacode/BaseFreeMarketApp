package com.jjlacode.freelanceproject.ui;

import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.jjlacode.base.util.android.FragmentGrid;
import com.jjlacode.freelanceproject.R;

import java.util.ArrayList;

public class FragmentInicio extends FragmentGrid {

    private String um;
    private String crm;
    private String proyectos;
    private String facturacion;
    private String contabilidad;
    private String ajustes;
    private String salir;

    @Override
    protected void setContext() {
        contexto = getContext();
    }


    @Override
    protected void setLista() {

        um = getString(R.string.union_market);
        crm = getString(R.string.crm);
        proyectos = getString(R.string.proyectos);
        facturacion = getString(R.string.facturacion);
        contabilidad = getString(R.string.contabilidad);
        ajustes = getString(R.string.informesyajustes);
        salir = getString(R.string.salir);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.logoum_sintxt_512, um));
        lista.add(new GridModel(R.drawable.ic_clientes_indigo, crm));
        lista.add(new GridModel(R.drawable.ic_proy_curso_indigo, proyectos));
        lista.add(new GridModel(R.drawable.ic_lista_notas_indigo, facturacion));
        lista.add(new GridModel(R.drawable.ic_cobros_indigo, contabilidad));
        lista.add(new GridModel(R.drawable.ic_tareas_indigo, ajustes));
        lista.add(new GridModel(R.drawable.ic_salir_rojo, salir));

    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();


        if (nombre.equals(crm)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentCRM()).addToBackStack(null).commit();
        } else if (nombre.equals(proyectos)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentGestionProyectos()).addToBackStack(null).commit();
        } else if (nombre.equals(facturacion)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentFacturacion()).addToBackStack(null).commit();

        } else if (nombre.equals(contabilidad)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentContabilidad()).addToBackStack(null).commit();


        } else if (nombre.equals(ajustes)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentInformesAjustes()).addToBackStack(null).commit();


        } else if (nombre.equals(um)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new UnionMarket()).addToBackStack(null).commit();


        }else if (nombre.equals(salir)){

            FirebaseAuth.getInstance().signOut();
            activityBase.finish();

        }


    }


}
