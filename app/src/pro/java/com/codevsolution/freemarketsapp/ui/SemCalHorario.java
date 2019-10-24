package com.codevsolution.freemarketsapp.ui;

import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.time.calendar.SemCalBase;
import com.codevsolution.freemarketsapp.logica.Interactor;

public class SemCalHorario extends SemCalBase implements ContratoPry.Tablas {

    @Override
    protected void setHorarios() {
        super.setHorarios();

        ModeloSQL perfil = CRUDutil.updateModelo(CAMPOS_PERFIL, PERFIL_NOMBRE, Interactor.perfila, null, IGUAL, null);

        imLunes = perfil.getLong(PERFIL_HORAIMLUNES);
        imMartes = perfil.getLong(PERFIL_HORAIMMARTES);
        imMiercoles = perfil.getLong(PERFIL_HORAIMMIERCOLES);
        imJueves = perfil.getLong(PERFIL_HORAIMJUEVES);
        imViernes = perfil.getLong(PERFIL_HORAIMVIERNES);
        imSabado = perfil.getLong(PERFIL_HORAIMSABADO);
        imDomingo = perfil.getLong(PERFIL_HORAIMDOMINGO);

        itLunes = perfil.getLong(PERFIL_HORAITLUNES);
        itMartes = perfil.getLong(PERFIL_HORAITMARTES);
        itMiercoles = perfil.getLong(PERFIL_HORAITMIERCOLES);
        itJueves = perfil.getLong(PERFIL_HORAITJUEVES);
        itViernes = perfil.getLong(PERFIL_HORAITVIERNES);
        itSabado = perfil.getLong(PERFIL_HORAITSABADO);
        itDomingo = perfil.getLong(PERFIL_HORAITDOMINGO);

        fmLunes = perfil.getLong(PERFIL_HORAFMLUNES);
        fmMartes = perfil.getLong(PERFIL_HORAFMMARTES);
        fmMiercoles = perfil.getLong(PERFIL_HORAFMMIERCOLES);
        fmJueves = perfil.getLong(PERFIL_HORAFMJUEVES);
        fmViernes = perfil.getLong(PERFIL_HORAFMVIERNES);
        fmSabado = perfil.getLong(PERFIL_HORAFMSABADO);
        fmDomingo = perfil.getLong(PERFIL_HORAFMDOMINGO);

        ftLunes = perfil.getLong(PERFIL_HORAFTLUNES);
        ftMartes = perfil.getLong(PERFIL_HORAFTMARTES);
        ftMiercoles = perfil.getLong(PERFIL_HORAFTMIERCOLES);
        ftJueves = perfil.getLong(PERFIL_HORAFTJUEVES);
        ftViernes = perfil.getLong(PERFIL_HORAFTVIERNES);
        ftSabado = perfil.getLong(PERFIL_HORAFTSABADO);
        ftDomingo = perfil.getLong(PERFIL_HORAFTDOMINGO);
    }
}
