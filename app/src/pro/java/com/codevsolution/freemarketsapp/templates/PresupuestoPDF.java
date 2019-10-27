package com.codevsolution.freemarketsapp.templates;


import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.PdfUtils;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.ArrayList;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.CAMPO_DIRECCION;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.CAMPO_EMAIL;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.CAMPO_NOMBRE;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.CAMPO_TELEFONO;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.CAMPO_WEB;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.PREFERENCIAS;


public class PresupuestoPDF extends PdfUtils implements ContratoPry.Tablas {


    public PresupuestoPDF() {

    }

    public void crearPdf(String idProyecto, String rutalogo) {

        ModeloSQL presupuesto = ConsultaBD.queryObject(CAMPOS_PROYECTO, idProyecto);
        ArrayList<ModeloSQL> listaPartidas = ConsultaBD.queryListDetalle(CAMPOS_PARTIDA, idProyecto, TABLA_PROYECTO);
        abrirPdf(idProyecto);
        addResource(R.drawable.logo_cds_512_a, context, ALINEACION_IZQUIERDA, 100, 100);

        String nombre = AndroidUtil.getSharePreference(context, PREFERENCIAS, CAMPO_NOMBRE, "");
        String direccion = AndroidUtil.getSharePreference(context, PREFERENCIAS, CAMPO_DIRECCION, "");
        String telefono = AndroidUtil.getSharePreference(context, PREFERENCIAS, CAMPO_TELEFONO, "");
        String email = AndroidUtil.getSharePreference(context, PREFERENCIAS, CAMPO_EMAIL, "");
        String web = AndroidUtil.getSharePreference(context, PREFERENCIAS, CAMPO_WEB, "");

        PdfPTable tablaDatosEmpresa = crearTabla(1, 0, 40);
        String[] datosEmpresa = {nombre, direccion, telefono, email, web};
        for (String s : datosEmpresa) {
            addCellDato(tablaDatosEmpresa, s, 1, 1, 20, ALINEACION_IZQUIERDA, ALINEACION_ARRIBA, BLANCO, SIN_BORDES);
        }

        PdfPTable tablaCab = crearTabla(2, 20, 100);
        addCellTabla(tablaCab, tablaDatosEmpresa, 1, 1, 100, ALINEACION_IZQUIERDA, ALINEACION_ARRIBA, BLANCO, SIN_BORDES);
        addCellTabla(tablaCab, tablaDatosEmpresa, 1, 1, 100, ALINEACION_IZQUIERDA, ALINEACION_ARRIBA, BLANCO, SIN_BORDES);

        dibujarTabla(new Paragraph(), tablaCab);

        String nombreCliente;

        setFuente(HELVETICA, 38, BOLD, AZUL);
        addParrafo(ALINEACION_CENTRO, context.getString(R.string.presupuesto).toUpperCase());
        if (rutalogo != null) {
            addImagen(rutalogo, ALINEACION_IZQUIERDA, 100, 100);
        }
        setFuente(HELVETICA, 22, BOLD, NEGRO);
        //addParrafo(ALINEACION_IZQUIERDA, idProyecto);
        addParrafo(ALINEACION_IZQUIERDA, "Cliente : " + presupuesto.getString(PROYECTO_CLIENTE_NOMBRE));
        setFuente(HELVETICA, 14, BOLD, NEGRO);
        addParrafo(ALINEACION_IZQUIERDA, presupuesto.getString(PROYECTO_DESCRIPCION));
        setFuente(HELVETICA, 14, NORMAL, NEGRO);

        PdfPTable tabla = crearTabla(5);
        String[] cabecera = {context.getString(R.string.descripcion),
                context.getString(R.string.cantidad),
                context.getString(R.string.importe)};
        int[] colspan = {3, 1, 1};
        int[] rowspan = {1, 1, 1};
        int[] alinh = {ALINEACION_IZQUIERDA, ALINEACION_CENTRO, ALINEACION_CENTRO};
        int[] alinv = {ALINEACION_CENTRO, ALINEACION_CENTRO, ALINEACION_CENTRO};
        addTablaCab(tabla, cabecera, colspan, rowspan, alinh, alinv, 30, getFuente(HELVETICA, 18, BOLD, NEGRO), GRIS, BORDE_TODOS);

        ArrayList<String[]> lista = new ArrayList<>();

        for (ModeloSQL partida : listaPartidas) {

            String[] partidatemp = {partida.getString(PARTIDA_NOMBRE),
                    partida.getString(PARTIDA_CANTIDAD),
                    JavaUtil.formatoMonedaLocal(partida.getDouble(PARTIDA_PRECIO))};

            lista.add(partidatemp);
        }

        System.out.println("lista = " + lista.size());
        addListaTabla(tabla, 3, lista, colspan, alinh, alinv, getFuente(HELVETICA, 14, NORMAL, NEGRO), BORDE_TODOS);

        dibujarTabla(new Paragraph(), tabla);
        cerrarPdf();

    }

}
