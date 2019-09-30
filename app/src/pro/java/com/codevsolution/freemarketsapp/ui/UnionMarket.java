package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;

import com.codevsolution.base.android.FragmentGrid;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

public class UnionMarket extends FragmentGrid {

    private String productos;
    private String productosPro;
    private String proveedoresWeb;
    private String sorteos;
    private String sorteosPro;

    @Override
    protected void setLista() {

        productos = getString(R.string.productosyservicios);
        productosPro = getString(R.string.productosyserviciosPro);
        proveedoresWeb = getString(R.string.proveedoresweb);
        sorteos = getString(R.string.sorteos);
        sorteosPro = getString(R.string.sorteos_pro);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_proveedor_indigo, proveedoresWeb));
        lista.add(new GridModel(R.drawable.ic_producto_indigo, productos));
        lista.add(new GridModel(R.drawable.ic_txt_pro, productosPro));
        lista.add(new GridModel(R.drawable.ic_sorteo, sorteos));
        lista.add(new GridModel(R.drawable.ic_sorteo, sorteosPro));

    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();

        if (nombre.equals(proveedoresWeb)) {

            icFragmentos.enviarBundleAFragment(bundle, new ListadosPerfilesFirebasePro());

        } else if (nombre.equals(sorteos)) {

            icFragmentos.enviarBundleAFragment(bundle, new ListadoSorteosCli());

        } else if (nombre.equals(sorteosPro)) {

            icFragmentos.enviarBundleAFragment(bundle, new ListadoSorteosPro());

        } else if (nombre.equals(productos)) {


            icFragmentos.enviarBundleAFragment(bundle, new ListadoProductosCli());

        } else if (nombre.equals(productosPro)) {


            icFragmentos.enviarBundleAFragment(bundle, new ListadoProductosPro());

        }


    }


}
