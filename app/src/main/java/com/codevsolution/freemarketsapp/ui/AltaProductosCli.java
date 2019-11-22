package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;

import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.logica.Interactor;

public class AltaProductosCli extends AltaProductosFirebase
        implements Interactor.ConstantesPry, ContratoPry.Tablas {

    private FragmentCRUDProducto parent;

    public AltaProductosCli(FragmentCRUDProducto parent) {

        this.parent = parent;
    }

    @Override
    protected boolean getDatos() {
        return iniciado;
    }

    @Override
    protected void cargarDatos() {

        parent.setOnSetDatosCliListener(new FragmentCRUDProducto.OnSetDatosCli() {
            @Override
            public void onSetDatos(Bundle bundle) {

                if (nn(bundle)) {

                    prodCrud = (ModeloSQL) bundle.getSerializable(CRUD);
                    if (prodCrud != null) {
                        prodProv = convertirProdCrud(prodCrud);
                        if (prodProv != null) {
                            prodProv.setTipo(PRODUCTOCLI);
                            esDetalle = true;
                            iniciado = true;
                            System.out.println("iniciadoCli = " + iniciado);

                        }
                    }

                }

                selector();
            }
        });
    }

    @Override
    protected String setTipo() {
        return PRODUCTOCLI;
    }

    @Override
    protected String setTipoSorteo() {
        return SORTEOCLI;
    }

}
