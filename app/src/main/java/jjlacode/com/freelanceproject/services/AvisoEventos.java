package jjlacode.com.freelanceproject.services;

import android.content.Intent;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.services.JobServiceBase;

import static jjlacode.com.freelanceproject.CommonPry.Constantes.ACCION_AVISOEVENTO;
import static jjlacode.com.freelanceproject.CommonPry.Constantes.EVENTO;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.EVENTO_AVISO;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.EVENTO_COMPLETADA;

public class AvisoEventos extends JobServiceBase {

    private ArrayList<Modelo> listaEventos;

    public AvisoEventos() {
    }

    @Override
    protected void setJob() {
        super.setJob();

        listaEventos = CommonPry.Calculos.comprobarEventos();

        for (Modelo evento : listaEventos) {


            if (evento != null && evento.getLong(EVENTO_AVISO) > 0 &&
                    evento.getDouble(EVENTO_COMPLETADA) < 100) {

                Intent intent = new Intent(ACCION_AVISOEVENTO).putExtra(EVENTO, evento);

                sendBroadcast(intent);
            }
        }

    }

}
