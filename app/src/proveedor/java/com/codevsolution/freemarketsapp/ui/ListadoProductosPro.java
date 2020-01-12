package com.codevsolution.freemarketsapp.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.codevsolution.freemarketsapp.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTOPRO;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposDetPartida.TIPOPRODUCTOPROV;

public class ListadoProductosPro extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb implements ContratoPry.Tablas {

    private Button btnAddAPartidaBase;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        location = true;
        super.setOnCreateView(view, inflater, container);

    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

        ViewGroupLayout vistaBtnPartida = new ViewGroupLayout(contexto, frdetalleExtrasante);
        btnAddAPartidaBase = vistaBtnPartida.addButtonSecondary(R.string.clonar_prod_a_partidabase);
        btnAddAPartidaBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = new Bundle();
                String idPartidabase = AndroidUtil.getSharePreference(contexto, PERSISTENCIA, PARTIDABASE_ID_PARTIDABASE, NULL);
                putBundle(CAMPO_ID, idPartidabase);
                putBundle(CAMPO_SECUENCIA, crearProdProvBase(idPartidabase));
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaBaseProdProvCat());
            }
        });

        String idPartidabase = AndroidUtil.getSharePreference(contexto, PERSISTENCIA, PARTIDABASE_ID_PARTIDABASE, NULL);
        if (nnn(idPartidabase)){
            visible(btnAddAPartidaBase);
        }

    }

    private int crearProdProvBase(String idPartidabase) {

        ContentValues valores = new ContentValues();
        putDato(valores, DETPARTIDABASE_ID_DETPARTIDABASE, prodProv.getId());
        putDato(valores, DETPARTIDABASE_ID_PARTIDABASE, idPartidabase);
        putDato(valores, DETPARTIDABASE_TIPO, TIPOPRODUCTOPROV);

        return crearRegistroSec(CAMPOS_DETPARTIDABASE, idPartidabase, valores);

    }

    @Override
    protected String setTipo() {
        return PRODUCTOPRO;
    }

    @Override
    protected String setTipoForm() {
        return LISTA;
    }

    @Override
    protected void setDatos() {

        visible(descuento.getLinearLayout());
        visible(btnClonar);

        super.setDatos();
    }

    @Override
    protected void onFirebaseFormBase() {
        super.onFirebaseFormBase();

        btnClonar.setEnabled(true);
    }

    @Override
    protected void acciones() {
        super.acciones();

        if (tipoForm.equals(LISTA)) {

            btnClonar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    crearProdCrud(prodProv);

                }
            });

        }
    }

    @Override
    protected void crearProdCrud(Productos prodProv) {
        super.crearProdCrud(prodProv);

        ContentValues values = new ContentValues();

        values.put(PRODUCTO_ID_CLON, prodProv.getId());
        values.put(PRODUCTO_ID_PROVFIRE, prodProv.getIdprov());
        values.put(PRODUCTO_NOMBRE, prodProv.getNombre());
        values.put(PRODUCTO_DESCRIPCION, prodProv.getDescripcion());
        values.put(PRODUCTO_CATEGORIA, prodProv.getCategoria());
        values.put(PRODUCTO_SUBCATEGORIA, prodProv.getSubCategoria());
        values.put(PRODUCTO_ALCANCE, prodProv.getAlcance());
        values.put(PRODUCTO_REFERENCIA, prodProv.getRefprov());
        values.put(PRODUCTO_WEB, prodProv.getWeb());
        values.put(PRODUCTO_NOMBREPRO, prodProv.getNombre());
        values.put(PRODUCTO_DESCRIPCIONPRO, prodProv.getDescripcion());
        values.put(PRODUCTO_CATEGORIAPRO, prodProv.getCategoria());
        values.put(PRODUCTO_SUBCATEGORIAPRO, prodProv.getSubCategoria());
        values.put(PRODUCTO_ALCANCEPRO, prodProv.getAlcance());
        values.put(PRODUCTO_REFERENCIAPRO, prodProv.getRefprov());
        values.put(PRODUCTO_WEBPRO, prodProv.getWeb());
        values.put(PRODUCTO_PRECIO, prodProv.getPrecio());
        values.put(PRODUCTO_DESCPROV, prodProv.getDescProv());

        String id = crearRegistroId(TABLA_PRODUCTO, values);
        ModeloSQL producto = updateModelo(CAMPOS_PRODUCTO, id);

        ImagenUtil.copyImageFirestoreToCrud(prodProv.getId() + prodProv.getTipo(), producto, CAMPO_RUTAFOTO);
        ImagenUtil.copyImageFirestoreToCrud(prodProv.getId() + prodProv.getTipo(), producto, CAMPO_RUTAFOTO + PRO);

        bundle = new Bundle();
        putBundle(CAMPO_ID, id);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProducto());
    }

}
