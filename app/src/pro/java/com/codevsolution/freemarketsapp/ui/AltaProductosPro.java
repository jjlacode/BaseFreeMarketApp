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
    protected void setInicio() {
        super.setInicio();

        parent.setOnSetDatosProListener(new FragmentCRUDProducto.OnSetDatosPro() {
            @Override
            public void onSetDatos(Bundle bundle) {

                System.out.println(TAG + " Al recibir datos de activity");
                if (nn(bundle)) {

                    System.out.println("bundle = " + bundle);
                    prodCrud = (ModeloSQL) bundle.getSerializable(CRUD);

                    System.out.println("prodCrud = " + prodCrud);
                    if (prodCrud != null) {
                        prodProv = convertirProdCrud(prodCrud);
                        if (prodProv != null) {
                            prodProv.setTipo(tipo);
                            if (prodProv.getId() == null) {
                                guardar();

                            }
                            System.out.println("prodProv = " + prodProv.toString());
                        }
                    }
                    esDetalle = true;

                    selector();
                }
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
