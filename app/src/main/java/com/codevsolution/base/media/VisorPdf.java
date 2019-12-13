package com.codevsolution.base.media;

import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.module.PdfViewerModule;

public class VisorPdf extends FragmentBase {

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        String rutaPdf = bundle.getString(PATH);
        String titulo = bundle.getString(TITULO);
        if (nnn(rutaPdf)) {
            visible(frdetalle);
            new PdfViewerModule(frdetalle, contexto, this, altoReal, rutaPdf);
            icFragmentos.showSubTitle(titulo);
        }
    }
}
