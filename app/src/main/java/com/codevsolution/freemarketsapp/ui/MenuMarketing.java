package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;

import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.base.chat.FragmentChatBase;
import com.codevsolution.base.media.VisorPdf;
import com.codevsolution.base.pay.chargebee.SuscripcionesChargebee;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.templates.CatalogoPDF;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTO;

public class MenuMarketing extends FragmentGridImagen {

    private String campanias;
    private String chat;
    private String sorteos;
    private String sorteosPro;
    private String suscripcion;
    private String catalogoProductos;

    @Override
    protected void setContext() {
        contexto = getContext();
    }

    @Override
    protected String setAyudaWeb() {
        return "marketing";
    }

    @Override
    protected void setInicio() {
        super.setInicio();

        icFragmentos.showSubTitle(R.string.marketing_y_publicidad);
        reproducir(getString(R.string.marketing_y_publicidad));

    }

    @Override
    protected void setLista() {

        campanias = getString(R.string.proximos_eventos);
        chat = getString(R.string.chat);
        sorteos = getString(R.string.sorteos);
        sorteosPro = getString(R.string.sorteos_pro);
        suscripcion = getString(R.string.suscripcion);
        catalogoProductos = getString(R.string.catalogo);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_lista_notas_indigo, suscripcion));
        //lista.add(new GridModel(R.drawable.ic_evento_indigo, campanias));
        lista.add(new GridModel(R.drawable.ic_chat_indigo, chat));
        lista.add(new GridModel(R.drawable.ic_sorteo, sorteos));
        lista.add(new GridModel(R.drawable.ic_sorteo, sorteosPro));
        lista.add(new GridModel(R.drawable.ic_pdf_indigo, catalogoProductos));


    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();

        if (nombre.equals(campanias)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new CalendarioEventos()).addToBackStack(null).commit();
        } else if (nombre.equals(suscripcion)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new SuscripcionesChargebee()).addToBackStack(null).commit();

        } else if (nombre.equals(sorteos)) {

            icFragmentos.enviarBundleAFragment(bundle, new AltaSorteosCli());

        } else if (nombre.equals(sorteosPro)) {

            icFragmentos.enviarBundleAFragment(bundle, new AltaSorteosPro());

        } else if (nombre.equals(chat)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentChatBase()).addToBackStack(null).commit();

        } else if (nombre.equals(catalogoProductos)) {

            CatalogoPDF catalogoPDF = new CatalogoPDF();
            catalogoPDF.crearPdf(PRODUCTO);
            bundle = new Bundle();
            bundle.putString(PATH, catalogoPDF.getRutaArchivo());
            bundle.putString(TITULO, getString(R.string.catalogo_productos));
            icFragmentos.enviarBundleAFragment(bundle, new VisorPdf());
            //AppActivity.mostrarPDF(catalogoPDF.getRutaArchivo());
        }


    }

    @Override
    public void setOnClickRV(Object object) {

    }


}
