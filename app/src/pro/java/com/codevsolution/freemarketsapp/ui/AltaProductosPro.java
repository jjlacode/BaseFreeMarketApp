package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;

import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.freemarketsapp.logica.Interactor;

public class AltaProductosPro extends AltaProductosFirebase implements Interactor.ConstantesPry {

    private FragmentCRUDProducto parent;

    public AltaProductosPro() {
    }

    public AltaProductosPro(FragmentCRUDProducto parent) {

        this.parent = parent;
    }

    @Override
    protected boolean getDatos() {
        return iniciado;
    }

    @Override
    protected void cargarDatos() {

        parent.setOnSetDatosProListener(new FragmentCRUDProducto.OnSetDatosPro() {
            @Override
            public void onSetDatos(Bundle bundle) {

                if (nn(bundle)) {

                    prodCrud = (ModeloSQL) bundle.getSerializable(CRUD);
                    if (prodCrud != null) {
                        prodProv = convertirProdCrud(prodCrud);
                        if (prodProv != null) {
                            prodProv.setTipo(PRODUCTOPRO);
                            esDetalle = true;
                            iniciado = true;
                            System.out.println("iniciadoPro = " + iniciado);
                        }
                    }

                }

                selector();
            }
        });

    }

    @Override
    protected String setTipo() {
        return PRODUCTOPRO;
    }

    @Override
    protected String setTipoSorteo() {
        return SORTEOPRO;
    }

}
