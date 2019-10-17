package com.codevsolution.freemarketsapp.ui;

import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.time.calendar.clases.DiaCalBase;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class HorarioPerfil extends DiaCalBase implements ContratoPry.Tablas {

    @Override
    protected int setColorCard(long fecha, long horaCal){

        ModeloSQL perfilActivo = null;
        ListaModelo listaModelo = CRUDutil.setListaModelo(CAMPOS_PERFIL);
        for (ModeloSQL perfil : listaModelo.getLista()) {
            if (perfil.getString(PERFIL_NOMBRE).equals(Interactor.perfila)) {
                perfilActivo = CRUDutil.updateModelo(perfil);
            }

        }
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fecha);

        if (nn(perfilActivo)) {
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                if ((horaCal >= perfilActivo.getLong(PERFIL_HORAIMLUNES) && horaCal <
                        perfilActivo.getLong(PERFIL_HORAFMLUNES)) ||
                        (horaCal >= perfilActivo.getLong(PERFIL_HORAITLUNES) && horaCal <
                                perfilActivo.getLong(PERFIL_HORAFTLUNES))) {

                    return R.color.colorPrimary;
                }
            } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                if ((horaCal >= perfilActivo.getLong(PERFIL_HORAIMMARTES) && horaCal <
                        perfilActivo.getLong(PERFIL_HORAFMMARTES)) ||
                        (horaCal >= perfilActivo.getLong(PERFIL_HORAITMARTES) && horaCal <
                                perfilActivo.getLong(PERFIL_HORAFTMARTES))) {

                    return R.color.colorPrimary;
                }
            } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                if ((horaCal >= perfilActivo.getLong(PERFIL_HORAIMMIERCOLES) && horaCal <
                        perfilActivo.getLong(PERFIL_HORAFMMIERCOLES)) ||
                        (horaCal >= perfilActivo.getLong(PERFIL_HORAITMIERCOLES) && horaCal <
                                perfilActivo.getLong(PERFIL_HORAFTMIERCOLES))) {

                    return R.color.colorPrimary;
                }
            } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                if ((horaCal >= perfilActivo.getLong(PERFIL_HORAIMJUEVES) && horaCal <
                        perfilActivo.getLong(PERFIL_HORAFMJUEVES)) ||
                        (horaCal >= perfilActivo.getLong(PERFIL_HORAITJUEVES) && horaCal <
                                perfilActivo.getLong(PERFIL_HORAFTJUEVES))) {

                    return R.color.colorPrimary;
                }
            } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                if ((horaCal >= perfilActivo.getLong(PERFIL_HORAIMVIERNES) && horaCal <
                        perfilActivo.getLong(PERFIL_HORAFMVIERNES)) ||
                        (horaCal >= perfilActivo.getLong(PERFIL_HORAITVIERNES) && horaCal <
                                perfilActivo.getLong(PERFIL_HORAFTVIERNES))) {

                    return R.color.colorPrimary;
                }
            } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                if ((horaCal >= perfilActivo.getLong(PERFIL_HORAIMSABADO) && horaCal <
                        perfilActivo.getLong(PERFIL_HORAFMSABADO)) ||
                        (horaCal >= perfilActivo.getLong(PERFIL_HORAITSABADO) && horaCal <
                                perfilActivo.getLong(PERFIL_HORAFTSABADO))) {

                    return R.color.colorPrimary;
                }
            } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                if ((horaCal >= perfilActivo.getLong(PERFIL_HORAIMDOMINGO) && horaCal <
                        perfilActivo.getLong(PERFIL_HORAFMDOMINGO)) ||
                        (horaCal >= perfilActivo.getLong(PERFIL_HORAITDOMINGO) && horaCal <
                                perfilActivo.getLong(PERFIL_HORAFTDOMINGO))) {

                    return R.color.colorPrimary;
                }
            }
        }

        return R.color.Color_card_defecto;
    }

}
