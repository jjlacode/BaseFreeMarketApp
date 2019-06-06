package jjlacode.com.freelanceproject.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import jjlacode.com.freelanceproject.util.CommonPry;
import jjlacode.com.freelanceproject.util.Modelo;

import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.EVENTO_AVISO;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.EVENTO_COMPLETADA;
import static jjlacode.com.freelanceproject.util.CommonPry.Constantes.ACCION_AVISOEVENTO;
import static jjlacode.com.freelanceproject.util.CommonPry.Constantes.EVENTO;

public class AvisoEventos extends JobService {

    private ArrayList<Modelo> listaEventos;

    public AvisoEventos() {
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.d(this.getClass().getSimpleName(),"onStartJob");
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                        listaEventos = CommonPry.Calculos.comprobarEventos();

                        for (Modelo evento : listaEventos) {


                            if (evento != null && evento.getLong(EVENTO_AVISO)>0 &&
                                    evento.getDouble(EVENTO_COMPLETADA)<100) {

                                Intent intent = new Intent(ACCION_AVISOEVENTO).putExtra(EVENTO, evento);

                                sendBroadcast(intent);
                            }
                        }

                        jobFinished(params, false);

                    }

            });

        AutoArranque.scheduleJob(getApplicationContext());
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}
