package com.codevsolution.freemarketsapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.sqlite.ContratoPry;

import java.util.ArrayList;

import static com.codevsolution.base.time.calendar.DiaCalBase.HORACAL;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposEvento.TIPOEVENTOEVENTO;

public class EventosSem extends SemCalHorario implements ContratoPry.Tablas {


    protected void setViewGroupDatos() {

        Button[][] botonesDato = new Button[filas][columnasCard];
        ViewGroupLayout vistaCell = new ViewGroupLayout(contexto, vistaBtn.getViewGroup());

        ListaModeloSQL listaSegmentos = crudUtil.setListaModelo(CAMPOS_EVENTO, null, EVENTO_FECHAINIEVENTO + ORDENASCENDENTE);
        ModeloSQL[] evento = new ModeloSQL[columnasCard];
        vistaCell.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        for (int i = 0; i < columnasCard; i++) {
            botonesDato[fila][i] = vistaCell.addButtonTrans(null, 1);
            Estilos.setLayoutParams(vistaCell.getViewGroup(), botonesDato[fila][i], Estilos.Constantes.MATCH_PARENT, sizeTextD * 2, 1, 5);
            for (ModeloSQL segmento : listaSegmentos.getLista()) {

                setFechaIni(segmento.getLong(EVENTO_FECHAINIEVENTO) + segmento.getLong(EVENTO_HORAINIEVENTO));
                setFechaFin(segmento.getLong(EVENTO_FECHAFINEVENTO) + segmento.getLong(EVENTO_HORAFINEVENTO));
                setFechaIniBoton(TimeDateUtil.soloFecha(fechaBtn) + (im + (i * HORASLONG)));
                setCondicionHorario((fm > 0 && (im + (i * HORASLONG)) < fm) || (ft > 0 && (im + (i * HORASLONG)) < ft && (im + (i * HORASLONG)) >= it));

                if (getCondicionCelda()) {

                    evento[i] = updateModelo(CAMPOS_EVENTO, segmento.getString(EVENTO_ID_EVENTO));
                    botonesDato[fila][i].setBackgroundColor(Color.parseColor(evento[i].getString(EVENTO_COLOR)));


                }
            }

            int finalI = i;
            long finalF = fechaBtn;
            botonesDato[fila][i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mostrarDialogNuevo(finalF, (im + (finalI * HORASLONG)));

                }
            });

        }
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL();
        ArrayList<String> listaId = new ArrayList<>();
        ViewGroupLayout vistaDatos = new ViewGroupLayout(contexto, vistaBtn.getViewGroup());
        for (int i = 0; i < columnasCard; i++) {
            boolean nuevo = true;
            if (evento[i] != null) {
                for (String s : listaId) {
                    if (evento[i].getString(EVENTO_ID_EVENTO).equals(s)) {
                        nuevo = false;
                    }
                }
                if (nuevo) {

                    long finalF = fechaBtn;
                    int finalI = i;
                    listaId.add(evento[i].getString(EVENTO_ID_EVENTO));
                    Button btnEvent = vistaDatos.addButtonTrans(evento[i].getString(EVENTO_DESCRIPCION));
                    btnEvent.setBackgroundColor(Color.parseColor(evento[i].getString(EVENTO_COLOR)));
                    btnEvent.setTextColor(Estilos.colorPrimary);
                    Estilos.setLayoutParams(vistaDatos.getViewGroup(), btnEvent, Estilos.Constantes.MATCH_PARENT, sizeTextD * 2, 1, 5);
                    listaModeloSQL.addModelo(evento[i]);
                    btnEvent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            bundle = new Bundle();
                            bundle.putSerializable(MODELO, evento[finalI]);
                            bundle.putString(CAMPO_ID, evento[finalI].getString(EVENTO_ID_EVENTO));
                            bundle.putString(ORIGEN, CALENDARIO);
                            bundle.putString(SUBTITULO, JavaUtil.getDate(finalF));
                            bundle.putString(ACTUAL, TIPOEVENTOEVENTO);
                            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
                        }
                    });
                }
            }
        }
        btnDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle = new Bundle();
                bundle.putLong(FECHA, fechaBtn);

                icFragmentos.enviarBundleAFragment(bundle, new DiaCalCalendario());
            }
        });

    }

    @Override
    protected void onNew(long fecha, long hora) {
        super.onNew(fecha, hora);

        bundle = new Bundle();
        bundle.putLong(FECHA, fecha);
        bundle.putLong(HORACAL, hora);
        bundle.putString(ORIGEN, CALENDARIO);
        System.out.println("fecha = " + fecha);
        System.out.println("hora = " + hora);

        icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevoEvento());
    }
}
