package com.codevsolution.freemarketsapp.ui;

import android.content.Intent;
import android.os.Bundle;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.base.login.LoginActivity;
import com.codevsolution.base.style.Dialogos;
import com.codevsolution.base.web.FragmentWebView;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.CLIENTE;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.HTTPAYUDA;
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
    private String clientes;
    private String salir;
    private String ayuda;

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
        proyectos = getString(R.string.gestion_proyectos);
        ajustes = getString(R.string.informesyajustes);
        productos = getString(R.string.productos);
        clientes = getString(R.string.clientes);
        salir = getString(R.string.cerrar_sesion);
        ayuda = getString(R.string.ayuda);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.logo, um));
        lista.add(new GridModel(R.drawable.ic_marketing_indigo, marketing));
        lista.add(new GridModel(R.drawable.ic_lista_eventos_indigo, crm));
        lista.add(new GridModel(R.drawable.ic_clientes_indigo, clientes));
        lista.add(new GridModel(R.drawable.ic_producto_indigo, productos));
        lista.add(new GridModel(R.drawable.ic_proy_curso_indigo, proyectos));
        lista.add(new GridModel(R.drawable.config32, ajustes));
        lista.add(new GridModel(R.drawable.ic_salir_rojo, salir));
        lista.add(new GridModel(R.drawable.help, ayuda));

    }

    @Override
    public void setOnClickRV(Object object) {

        GridModel gridModel = (GridModel) object;

        String nombre = gridModel.getNombre();


        if (nombre.equals(crm)) {

            //activityBase.getSupportFragmentManager().beginTransaction()
            //        .replace(R.id.content_main, new MenuCRM()).addToBackStack(null).commit();
            bundle = new Bundle();
            bundle.putString(ACTUAL, MENUCRM);
            icFragmentos.enviarBundleAFragment(bundle, new MenuCRM());

        } else if (nombre.equals(proyectos)) {

            getFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new MenuGestionProyectos()).addToBackStack(null).commit();

        } else if (nombre.equals(ajustes)) {

            getFragmentManager().beginTransaction()
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

        } else if (nombre.equals(clientes)) {

            bundle = new Bundle();
            bundle.putString(ACTUAL, CLIENTE);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDCliente());

        }else if (nombre.equals(ayuda)) {

            bundle = new Bundle();
            bundle.putString(WEB, HTTPAYUDA);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentWebView());

        } else if (nombre.equals(salir)) {

            String titulo = getString(R.string.cerrar_sesion);
            String mensaje = getString(R.string.msg_cerrar_sesion);
            new Dialogos.DialogoTexto(titulo, mensaje, contexto, new Dialogos.DialogoTexto.OnClick() {
                @Override
                public void onConfirm() {

                    AndroidUtil.setSharePreference(contexto, PREFERENCIAS, PASSOK, NULL);
                    AndroidUtil.setSharePreference(contexto, USERID, USERID, NULL);
                    AndroidUtil.setSharePreference(contexto, USERID, USERIDCODE, NULL);
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(contexto, LoginActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onCancel() {

                    String contenido = getString(R.string.operacion_cancelada);
                    message(contenido,false);
                }

            }).show(getFragmentManager(),"cerrar sesion");


        }

    }


}
