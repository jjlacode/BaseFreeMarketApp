package com.jjlacode.freelanceproject.templates;


import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.media.PdfUtils;
import com.jjlacode.base.util.models.Modelo;
import com.jjlacode.base.util.sqlite.ConsultaBD;
import com.jjlacode.base.util.sqlite.ContratoPry;

import java.util.ArrayList;


public class PresupuestoPDF extends PdfUtils implements ContratoPry.Tablas {


    public PresupuestoPDF() {

    }

    public void crearPdf(String idProyecto, String rutalogo){

        Modelo presupuesto = ConsultaBD.queryObject(CAMPOS_PROYECTO,idProyecto);
        ArrayList<Modelo> listaPartidas = ConsultaBD.queryListDetalle(CAMPOS_PARTIDA,idProyecto,TABLA_PROYECTO);
        abrirPdf(idProyecto);
        if (rutalogo!=null) {
            addImagen(rutalogo, ALINEACION_IZQUIERDA, 100, 100);
        }
        setFuente(HELVETICA,38,BOLD,ROJO);
        addParrafo(ALINEACION_CENTRO,"PRESUPUESTO");
        setFuente(HELVETICA,22,BOLD,NEGRO);
        addParrafo(ALINEACION_IZQUIERDA,idProyecto);
        addParrafo(ALINEACION_CENTRO,"Cliente : " + presupuesto.getString(PROYECTO_CLIENTE_NOMBRE));
        setFuente(HELVETICA,14,BOLD,NEGRO);
        addParrafo(ALINEACION_CENTRO,presupuesto.getString(PROYECTO_DESCRIPCION));
        setFuente(HELVETICA,14,NORMAL,NEGRO);

        abrirTabla(5);
        String[] cabecera = {"Descripción","Cantidad","importe"};
        int[] colspan = {3,1,1};
        int[] rowspan = {1,1,1};
        int[] alinh = {ALINEACION_IZQUIERDA,ALINEACION_CENTRO,ALINEACION_CENTRO};
        int[] alinv = {ALINEACION_CENTRO,ALINEACION_CENTRO,ALINEACION_CENTRO};
        addTablaCab(cabecera,colspan,rowspan,alinh,alinv,30,getFuente(HELVETICA,18,BOLD,NEGRO),VERDE);

        ArrayList<String[]> lista = new ArrayList<>();

        for (Modelo partida : listaPartidas) {

            String[] partidatemp = {partida.getString(PARTIDA_NOMBRE),
                                    partida.getString(PARTIDA_CANTIDAD),
                                    JavaUtil.formatoMonedaLocal(partida.getDouble(PARTIDA_PRECIO))};

            lista.add(partidatemp);
        }

        addListaTabla(3,lista,colspan,rowspan,alinh, alinv,20,getFuente(HELVETICA,14,NORMAL,NEGRO));

        cerrarTabla();
        cerrarPdf();

    }

}
