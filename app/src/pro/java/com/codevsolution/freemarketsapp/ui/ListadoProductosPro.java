package com.codevsolution.freemarketsapp.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.codevsolution.freemarketsapp.R;

import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.CAMPOS_DETPARTIDABASE;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.DETPARTIDABASE_ID_DETPARTIDABASE;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.DETPARTIDABASE_ID_PARTIDABASE;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.DETPARTIDABASE_TIPO;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PARTIDABASE_ID_PARTIDABASE;
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
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.TABLA_PARTIDABASE;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.TABLA_PRODUCTO;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTOCLI;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTOPRO;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposDetPartida.TIPOPRODUCTOPROV;

public class ListadoProductosPro extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb {

    private Button btnAddAPartidaBase;

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

        btnAddAPartidaBase = view.findViewById(R.id.btn_add_prod);
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
        putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_ID_DETPARTIDABASE, prodProv.getId());
        putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_ID_PARTIDABASE, idPartidabase);
        putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_TIPO, TIPOPRODUCTOPROV);

        return CRUDutil.crearRegistroSec(CAMPOS_DETPARTIDABASE, idPartidabase, TABLA_PARTIDABASE, valores);

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

        visible(descuento);
        gone(sincronizaClon);
        visible(btnClonar);
        visible(btnClonarPro);

        super.setDatos();
    }

    @Override
    protected void onFirebaseFormBase() {
        super.onFirebaseFormBase();

        btnClonar.setEnabled(true);
        btnClonarPro.setEnabled(true);
    }

    @Override
    protected void acciones() {
        super.acciones();

        if (tipoForm.equals(LISTA)) {

            btnClonar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clonarProd(PRODUCTOCLI);

                }
            });

            btnClonarPro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clonarProd(PRODUCTOPRO);

                }
            });
        }
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
