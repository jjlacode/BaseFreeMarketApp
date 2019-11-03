package com.codevsolution.freemarketsapp.ui;

import com.codevsolution.freemarketsapp.logica.Interactor;

public class AltaProductosPro extends AltaProductosFirebase
        implements Interactor.ConstantesPry {


    public AltaProductosPro() {
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
