package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;

import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

import static com.codevsolution.base.time.calendar.DiaCalBase.HORACAL;

public class FragmentNuevoEvento extends FragmentGridImagen implements Interactor.ConstantesPry, Interactor.TiposEvento {

    private String nuevaTarea;
    private String nuevaCita;
    private String nuevoEmail;
    private String nuevaLlamada;
    private String nuevoEvento;
    private String volver;
    private String origen;
    private long fecha;
    private long hora;

    @Override
    protected void setContext() {
        contexto = getContext();
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        if (nn(bundle)) {
            fecha = bundle.getLong(FECHA);
            hora = bundle.getLong(HORACAL);
            origen = bundle.getString(ORIGEN);
        }
    }

    @Override
    protected void setLista() {

        nuevaTarea = getString(R.string.nueva_tarea);
        nuevaCita = getString(R.string.nueva_cita);
        nuevoEmail = getString(R.string.nuevo_email);
        nuevaLlamada = getString(R.string.nueva_llamada);
        nuevoEvento = getString(R.string.nuevo_evento);
        volver = getString(R.string.volver);

        lista = new ArrayList<>();

        lista.add(new GridModel(R.drawable.ic_lista_notas_indigo, nuevaTarea));
        lista.add(new GridModel(R.drawable.ic_lugar_indigo, nuevaCita));
        lista.add(new GridModel(R.drawable.ic_email_indigo, nuevoEmail));
        lista.add(new GridModel(R.drawable.ic_llamada_indigo, nuevaLlamada));
        lista.add(new GridModel(R.drawable.ic_evento_indigo, nuevoEvento));
        lista.add(new GridModel(R.drawable.ic_volver_verde, volver));

    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();
        String tipoEvento=null;

        if (nombre.equals(nuevaTarea)){

            tipoEvento = TIPOEVENTOTAREA;

        }else if (nombre.equals(nuevaCita)){

            tipoEvento = TIPOEVENTOCITA;
        }else if (nombre.equals(nuevoEmail)){

            tipoEvento = TIPOEVENTOEMAIL;

        }else if (nombre.equals(nuevaLlamada)){
            tipoEvento = TIPOEVENTOLLAMADA;

        }else if (nombre.equals(nuevoEvento)){
            tipoEvento = TIPOEVENTOEVENTO;

        }else if (nombre.equals(volver)){

            bundle = new Bundle();
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());

        }

        if (tipoEvento!=null) {

            if (bundle == null) {
                bundle = new Bundle();
            }
            if (origen == null) {
                origen = EVENTO;
            }
            bundle.putString(TIPO, tipoEvento);
            bundle.putString(ACTUAL, EVENTO);
            bundle.putString(ORIGEN, origen);
            bundle.putLong(HORACAL, hora);
            bundle.putLong(FECHA, fecha);
            bundle.putBoolean(NUEVOREGISTRO, true);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
        }

    }

}
