package com.codevsolution.freemarketsapp.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.codevsolution.freemarketsapp.R;

import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.CAMPOS_DETPARTIDABASE;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.DETPARTIDABASE_ID_DETPARTIDABASE;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.DETPARTIDABASE_TIPO;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PARTIDABASE_ID_PARTIDABASE;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.TABLA_PARTIDABASE;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTOCLI;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTOPRO;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposDetPartida.TIPOPRODUCTOPROV;

public class ListadoProductosPro extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb {

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

        Button btnAddAPartidaBase = view.findViewById(R.id.btn_add_prod);
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

    }

    private int crearProdProvBase(String idPartidabase) {

        ContentValues valores = new ContentValues();
        putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_ID_DETPARTIDABASE, prodProv.getId());
        putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_TIPO, TIPOPRODUCTOPROV);

        return CRUDutil.crearRegistroSec(CAMPOS_DETPARTIDABASE, TABLA_PARTIDABASE, idPartidabase, valores);

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

}
