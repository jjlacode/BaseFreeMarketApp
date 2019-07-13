package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.android.FragmentGrid;

import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ACTUAL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.CALENDARIO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.FECHA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.NUEVOREGISTRO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ORIGEN;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.TIPO;
import static jjlacode.com.freelanceproject.util.time.calendar.clases.DiaCalBase.HORACAL;

public class FragmentNuevoEvento extends FragmentGrid {

    private String nuevaTarea;
    private String nuevaCita;
    private String nuevoEmail;
    private String nuevaLlamada;
    private String nuevoEvento;
    private String volver;
    private String origen;
    private long fecha = -1;
    private long hora = -1;

    @Override
    protected void setContext() {
        contexto = getContext();
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        fecha = bundle.getLong(FECHA);
        hora = bundle.getLong(HORACAL);
        origen = bundle.getString(ORIGEN);
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
            bundle.putString(ORIGEN, EVENTO);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());

        }

        if (tipoEvento!=null) {
            bundle = new Bundle();
            bundle.putString(TIPO, tipoEvento);
            bundle.putString(ORIGEN, origen);
            if (origen!=null && origen.equals(CALENDARIO)){
                bundle.putString(ORIGEN,CALENDARIO);
                bundle.putLong(FECHA,fecha);
                bundle.putString(ACTUAL, EVENTO);
                if (hora>=0){
                    bundle.putLong(HORACAL,hora);
                }

            }
            bundle.putBoolean(NUEVOREGISTRO, true);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
        }

    }

}
