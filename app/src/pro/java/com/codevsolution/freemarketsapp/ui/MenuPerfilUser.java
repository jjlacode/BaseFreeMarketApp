package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;

import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.base.web.FragmentWebView;
import com.codevsolution.freemarketsapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.HTTPAYUDA;

public class MenuPerfilUser extends FragmentGridImagen {

    private String clienteWeb;
    private String freelance;
    private String comercial;
    private String ecommerce;
    private String lugar;
    private String empresa;
    private String proveedorWeb;
    private String ayuda;
    private String salir;

    @Override
    protected void setContext() {
        contexto = getContext();
    }

    @Override
    protected String setAyudaWeb() {
        return HTTPAYUDA + "perfil-usuario";
    }

    @Override
    protected void setLista() {

        clienteWeb = getString(R.string.clienteweb);
        freelance = getString(R.string.freelance);
        comercial = getString(R.string.comercial);
        ecommerce = getString(R.string.ecommerce);
        lugar = getString(R.string.lugar);
        empresa = getString(R.string.empresa);
        proveedorWeb = getString(R.string.proveedorweb);
        ayuda = getString(R.string.ayuda);
        salir = getString(R.string.salir);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_clientes_indigo, clienteWeb));
        lista.add(new GridModel(R.drawable.ic_tareas_indigo, freelance));
        lista.add(new GridModel(R.drawable.ic_comercial_indigo, comercial));
        lista.add(new GridModel(R.drawable.ic_producto_indigo, ecommerce));
        lista.add(new GridModel(R.drawable.ic_proveedor_indigo, lugar));
        lista.add(new GridModel(R.drawable.ic_empresa_indigo, empresa));
        lista.add(new GridModel(R.drawable.ic_catalogo_indigo, proveedorWeb));
        lista.add(new GridModel(R.drawable.ic_ayuda_indigo, ayuda));
        lista.add(new GridModel(R.drawable.ic_salir_rojo, salir));

    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();


        if (nombre.equals(clienteWeb)) {

            bundle = new Bundle();
            putBundle(TIPO, clienteWeb);
            icFragmentos.enviarBundleAFragment(bundle, new AltaPerfilesFirebaseCli());
        } else if (nombre.equals(freelance)) {

            bundle = new Bundle();
            putBundle(TIPO, freelance);
            icFragmentos.enviarBundleAFragment(bundle, new AltaPerfilesFirebaseCli());
        } else if (nombre.equals(comercial)) {

            bundle = new Bundle();
            putBundle(TIPO, comercial);
            icFragmentos.enviarBundleAFragment(bundle, new AltaPerfilesFirebaseCli());
        } else if (nombre.equals(ecommerce)) {

            bundle = new Bundle();
            putBundle(TIPO, ecommerce);
            icFragmentos.enviarBundleAFragment(bundle, new AltaPerfilesFirebaseCli());


        } else if (nombre.equals(lugar)) {

            bundle = new Bundle();
            putBundle(TIPO, lugar);
            icFragmentos.enviarBundleAFragment(bundle, new AltaPerfilesFirebaseCli());


        } else if (nombre.equals(empresa)) {

            bundle = new Bundle();
            putBundle(TIPO, empresa);
            icFragmentos.enviarBundleAFragment(bundle, new AltaPerfilesFirebaseCli());


        } else if (nombre.equals(proveedorWeb)) {

            bundle = new Bundle();
            putBundle(TIPO, proveedorWeb);
            icFragmentos.enviarBundleAFragment(bundle, new AltaPerfilesFirebaseCli());


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
