package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.ViewGroup;

import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.logica.Interactor;

public class AltaProductosCli extends AltaProductosFirebase
        implements Interactor.ConstantesPry, ContratoPry.Tablas {

    private ViewGroup viewGroup;
    private FragmentCRUDProducto parent;

    public AltaProductosCli() {


    }

    public AltaProductosCli(FragmentCRUDProducto parent) {

        this.parent = parent;
    }

    @Override
    protected void setInicio() {
        super.setInicio();

        parent.setOnSetDatosCliListener(new FragmentCRUDProducto.OnSetDatosCli() {
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
        return PRODUCTOCLI;
    }

    @Override
    protected String setTipoSorteo() {
        return SORTEOCLI;
    }

}
