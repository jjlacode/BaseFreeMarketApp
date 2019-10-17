package com.codevsolution.freemarketsapp.ui;

import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.logica.Interactor;

public class AltaProductosCli extends AltaProductosFirebase
        implements Interactor.ConstantesPry, ContratoPry.Tablas {

    @Override
    protected String setTipo() {
        return PRODUCTOCLI;
    }

    @Override
    protected String setTipoSorteo() {
        return SORTEOCLI;
    }



}
