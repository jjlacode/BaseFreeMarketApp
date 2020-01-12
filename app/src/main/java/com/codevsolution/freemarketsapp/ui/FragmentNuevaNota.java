package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;

import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.NOTA;

public class FragmentNuevaNota extends FragmentGridImagen implements Interactor.TiposNota {

    private String notaTexto;
    private String notaAudio;
    private String notaVideo;
    private String notaImagen;
    private long fecha = -1;
    private long hora = -1;

    @Override
    protected void setContext() {
        contexto = getContext();
    }

    @Override
    protected String setTAG() {
        return getClass().getSimpleName();
    }

    @Override
    protected void setInicio() {
        super.setInicio();
        icFragmentos.showSubTitle(R.string.nueva_nota);
        reproducir(getString(R.string.nueva_nota));
    }

    @Override
    protected void setLista() {

        notaTexto = getString(R.string.nota_texto);
        notaAudio = getString(R.string.nota_audio);
        notaVideo = getString(R.string.nota_video);
        notaImagen = getString(R.string.nota_imagen);

        lista = new ArrayList<>();

        lista.add(new GridModel(R.drawable.ic_nota_texto_indigo, notaTexto));
        lista.add(new GridModel(R.drawable.ic_nota_audio_indigo, notaAudio));
        lista.add(new GridModel(R.drawable.ic_nota_video_indigo, notaVideo));
        lista.add(new GridModel(R.drawable.ic_nota_imagen_indigo, notaImagen));

    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();
        String tipoNota = null;

        if (nombre.equals(notaTexto)) {

            tipoNota = NOTATEXTO;

        } else if (nombre.equals(notaAudio)) {

            tipoNota = NOTAAUDIO;
        } else if (nombre.equals(notaVideo)) {

            tipoNota = NOTAVIDEO;

        } else if (nombre.equals(notaImagen)) {
            tipoNota = NOTAIMAGEN;

        }
        if (tipoNota != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString(TIPO, tipoNota);
            bundle.putString(ACTUAL, NOTA);

            if (origen == null) {
                bundle.putString(ORIGEN, NUEVOREGISTRO);
            }
            bundle.putBoolean(NUEVOREGISTRO, true);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
        }

    }

    @Override
    public void setOnClickRV(Object object) {

    }

}
