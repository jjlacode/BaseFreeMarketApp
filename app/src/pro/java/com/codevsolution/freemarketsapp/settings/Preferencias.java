package com.codevsolution.freemarketsapp.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.settings.PreferenciasBase;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.freemarketsapp.R;

import java.util.Locale;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.EVENTO;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.NOTA;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTO;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PROYECTO;

public class Preferencias extends PreferenciasBase {


    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);


    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        origen = getStringBundle(ORIGEN, NULL);
        actual = getStringBundle(ACTUAL, NULL);

        if (nnn(origen)){

            visible(ajustes);
            ajustes.setText(String.format(Locale.getDefault(),"%s %s", getString(R.string.ajustes), origen));

            switch (origen){

                case PRODUCTO:

                    TextView txtPrueba = vistaSetPage.addTextView("Texto de prueba");

                    break;

                case PROYECTO:

                    break;

                case EVENTO:

                    break;

                case NOTA:

                    break;


            }
        } else {
            TextView txtSinAjustes = vistaSetPage.addTextView(getString(R.string.sin_ajustes).toUpperCase());
            txtSinAjustes.setTextColor(Estilos.colorBrightRed);
        }

    }

}
