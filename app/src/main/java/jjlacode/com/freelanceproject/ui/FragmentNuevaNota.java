package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.android.FragmentGrid;

import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ACTUAL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.CALENDARIO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.FECHA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.NUEVOREGISTRO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ORIGEN;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.TIPO;
import static jjlacode.com.freelanceproject.util.time.calendar.clases.DiaCalBase.HORACAL;

public class FragmentNuevaNota extends FragmentGrid implements CommonPry.TiposNota {

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
    protected void cargarBundle() {
        super.cargarBundle();

        if (bundle!=null) {
            fecha = bundle.getLong(FECHA);
            hora = bundle.getLong(HORACAL);
            origen = bundle.getString(ORIGEN);
        }
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
        String tipoNota=null;

        if (nombre.equals(notaTexto)){

            tipoNota = NOTATEXTO;

        }else if (nombre.equals(notaAudio)){

            tipoNota = NOTAAUDIO;
        }else if (nombre.equals(notaVideo)){

            tipoNota = NOTAVIDEO;

        }else if (nombre.equals(notaImagen)){
            tipoNota = NOTAIMAGEN;

        }
        if (tipoNota!=null) {
            bundle.putString(TIPO, tipoNota);
            bundle.putString(ACTUAL, NOTA);

            if (origen==null){
                bundle.putString(ORIGEN, NUEVOREGISTRO);
            }
            bundle.putBoolean(NUEVOREGISTRO, true);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
        }

    }

}
