package com.jjlacode.freelanceproject.services;

import android.content.Intent;

import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.services.JobServiceBase;
import com.jjlacode.freelanceproject.logica.Interactor;

import java.util.ArrayList;

import static com.jjlacode.freelanceproject.logica.Interactor.Constantes.ACCION_AVISOEVENTO;
import static com.jjlacode.freelanceproject.logica.Interactor.Constantes.EVENTO;
import static com.jjlacode.freelanceproject.sqlite.ContratoPry.Tablas.EVENTO_AVISO;
import static com.jjlacode.freelanceproject.sqlite.ContratoPry.Tablas.EVENTO_COMPLETADA;

public class AvisoEventos extends JobServiceBase {

    private ArrayList<Modelo> listaEventos;

    public AvisoEventos() {
    }

    @Override
    protected void setJob() {
        super.setJob();

        listaEventos = Interactor.Calculos.comprobarEventos();

        for (Modelo evento : listaEventos) {


            if (evento != null && evento.getLong(EVENTO_AVISO) > 0 &&
                    evento.getDouble(EVENTO_COMPLETADA) < 100) {

                Intent intent = new Intent(ACCION_AVISOEVENTO).putExtra(EVENTO, evento);

                sendBroadcast(intent);
            }
        }

    }

}
