package com.codevsolution.freemarketsapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.freemarketsapp.sqlite.ContratoPry;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

public class TrabajosSem extends SemCalHorario implements ContratoPry.Tablas {


    protected void setViewGroupDatos() {

        Button[][] botonesDato = new Button[filas][columnasCard];
        ViewGroupLayout vistaCell = new ViewGroupLayout(contexto, vistaBtn.getViewGroup());

        ListaModeloSQL listaSegmentos = crudUtil.setListaModelo(CAMPOS_AGENDA, null, AGENDA_VALORENTRADA + ORDENASCENDENTE);
        ModeloSQL[] detPartida = new ModeloSQL[columnasCard];
        vistaCell.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        for (int i = 0; i < columnasCard; i++) {
            botonesDato[fila][i] = vistaCell.addButtonTrans(null, 1);
            for (ModeloSQL segmento : listaSegmentos.getLista()) {

                if (TimeDateUtil.soloFecha(fechaBtn) + (im + (i * HORASLONG)) > segmento.getLong(AGENDA_VALORENTRADA) &&
                        TimeDateUtil.soloFecha(fechaBtn) + (im + (i * HORASLONG)) < segmento.getLong(AGENDA_VALORSALIDA) &&
                        ((fm > 0 && (im + (i * HORASLONG)) < fm) || (ft > 0 && (im + (i * HORASLONG)) < ft && (im + (i * HORASLONG)) >= it))) {
                    detPartida[i] = updateModelo(CAMPOS_DETPARTIDA, segmento.getString(AGENDA_ID_DETPARTIDA), segmento.getInt(AGENDA_SECUENCIA_DETPARTIDA));
                    botonesDato[fila][i].setBackgroundColor(Color.parseColor(detPartida[i].getString(DETPARTIDA_COLOR)));
                    Estilos.setLayoutParams(vistaCell.getViewGroup(), botonesDato[fila][i], Estilos.Constantes.MATCH_PARENT, sizeTextD * 2, 1, 5);

                }
            }

        }
        final ListaModeloSQL listaModeloSQL = new ListaModeloSQL();
        ArrayList<String> listaId = new ArrayList<>();
        ViewGroupLayout vistaDatos = new ViewGroupLayout(contexto, vistaBtn.getViewGroup());
        for (int i = 0; i < columnasCard; i++) {
            boolean nuevo = true;
            if (detPartida[i] != null) {
                for (String s : listaId) {
                    if (detPartida[i].getString(DETPARTIDA_ID_PARTIDA).equals(s)) {
                        nuevo = false;
                    }
                }
                if (nuevo) {
                    listaId.add(detPartida[i].getString(DETPARTIDA_ID_PARTIDA));
                    Button btnEvent = vistaDatos.addButtonTrans(detPartida[i].getString(DETPARTIDA_NOMBRE));
                    btnEvent.setBackgroundColor(Color.parseColor(detPartida[i].getString(DETPARTIDA_COLOR)));
                    btnEvent.setTextColor(getResources().getColor(R.color.colorPrimary));
                    Estilos.setLayoutParams(vistaDatos.getViewGroup(), btnEvent, Estilos.Constantes.MATCH_PARENT, sizeTextD * 2, 1, 5);
                    listaModeloSQL.addModelo(detPartida[i]);
                }
            }
        }
        btnDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle = new Bundle();
                bundle.putLong(FECHA, fechaBtn);

                icFragmentos.enviarBundleAFragment(bundle, new DiaCalTrabajos());
            }
        });

    }
}
