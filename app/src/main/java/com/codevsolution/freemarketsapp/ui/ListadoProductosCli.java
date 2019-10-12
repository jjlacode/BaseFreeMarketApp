package com.codevsolution.freemarketsapp.ui;


import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;

import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PRODUCTO_ALCANCE;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PRODUCTO_CATEGORIA;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PRODUCTO_DESCPROV;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PRODUCTO_DESCRIPCION;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PRODUCTO_ID_CLON;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PRODUCTO_ID_PRODFIRE;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PRODUCTO_ID_PROVFIRE;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PRODUCTO_NOMBRE;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PRODUCTO_NOMBREPROV;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PRODUCTO_PRECIO;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PRODUCTO_REFERENCIA;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PRODUCTO_RUTAFOTO;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PRODUCTO_TIPO;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PRODUCTO_WEB;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.TABLA_PRODUCTO;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTOCLI;

public class ListadoProductosCli extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb {

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        location = true;
        super.setOnCreateView(view, inflater, container);
    }

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

    }

    @Override
    protected String setTipo() {
        return PRODUCTOCLI;
    }

    @Override
    protected String setTipoForm() {
        return LISTA;
    }

    @Override
    protected void setDatos() {

        if (prodProv != null && !nuevo) {

            if (nn(prodProv.getIdClon()) && !prodProv.getIdClon().isEmpty()) {
                visible(sincronizaClon);
            } else {
                gone(sincronizaClon);
            }
        }
        super.setDatos();
    }

    @Override
    protected String crearProdCrud(Productos prodProv) {
        super.crearProdCrud(prodProv);

        ContentValues values = new ContentValues();

        values.put(PRODUCTO_ID_PRODFIRE, prodProv.getId());
        values.put(PRODUCTO_ID_CLON, prodProv.getIdClon());
        values.put(PRODUCTO_ID_PROVFIRE, prodProv.getIdprov());
        values.put(PRODUCTO_NOMBRE, prodProv.getNombre());
        values.put(PRODUCTO_DESCRIPCION, prodProv.getDescripcion());
        values.put(PRODUCTO_DESCPROV, prodProv.getDescProv());
        values.put(PRODUCTO_CATEGORIA, prodProv.getCategoria());
        values.put(PRODUCTO_PRECIO, prodProv.getPrecio());
        values.put(PRODUCTO_ALCANCE, prodProv.getAlcance());
        values.put(PRODUCTO_NOMBREPROV, prodProv.getProveedor());
        values.put(PRODUCTO_REFERENCIA, prodProv.getRefprov());
        values.put(PRODUCTO_RUTAFOTO, prodProv.getRutafoto());
        values.put(PRODUCTO_TIPO, prodProv.getTipo());
        values.put(PRODUCTO_WEB, prodProv.getWeb());

        return CRUDutil.crearRegistroId(TABLA_PRODUCTO,values);

    }

    @Override
    protected void actualizarProdCrud(Productos prodProv) {
        super.actualizarProdCrud(prodProv);

        ContentValues values = new ContentValues();

        values.put(PRODUCTO_ID_PRODFIRE, prodProv.getId());
        values.put(PRODUCTO_ID_CLON, prodProv.getIdClon());
        values.put(PRODUCTO_ID_PROVFIRE, prodProv.getIdprov());
        values.put(PRODUCTO_NOMBRE, prodProv.getNombre());
        values.put(PRODUCTO_DESCRIPCION, prodProv.getDescripcion());
        values.put(PRODUCTO_DESCPROV, prodProv.getDescProv());
        values.put(PRODUCTO_CATEGORIA, prodProv.getCategoria());
        values.put(PRODUCTO_PRECIO, prodProv.getPrecio());
        values.put(PRODUCTO_ALCANCE, prodProv.getAlcance());
        values.put(PRODUCTO_NOMBREPROV, prodProv.getProveedor());
        values.put(PRODUCTO_REFERENCIA, prodProv.getRefprov());
        values.put(PRODUCTO_RUTAFOTO, prodProv.getRutafoto());
        values.put(PRODUCTO_TIPO, prodProv.getTipo());
        values.put(PRODUCTO_WEB, prodProv.getWeb());

        if (nnn(prodProv.getIdCrud())) {
            CRUDutil.actualizarRegistro(TABLA_PRODUCTO, prodProv.getIdCrud(), values);
        }
    }
}
