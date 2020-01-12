package com.codevsolution.freemarketsapp.templates;


import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.PdfUtils;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.sqlite.ContratoPry;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTO;


public class CatalogoPDF extends PdfUtils implements ContratoPry.Tablas {


    public CatalogoPDF() {

    }

    public void crearPdf(String tipoCatalogo) {

        ListaModeloSQL listaModeloSQL = null;
        if (tipoCatalogo.equals(PRODUCTO)) {
            listaModeloSQL = new ListaModeloSQL(CAMPOS_PRODUCTO);
            abrirPdf(context.getString(R.string.catalogo) + " " + tipoCatalogo);
        }
        addResource(R.drawable.logo_cds_512_a, context, ALINEACION_IZQUIERDA, 100, 100);

        setFuente(HELVETICA, 38, BOLD, AZUL);
        addParrafo(ALINEACION_CENTRO, context.getString(R.string.catalogo) + " " + tipoCatalogo);

        PdfPTable tabla = crearTabla(18);
        String[] cabecera = {null, context.getString(R.string.nombre),
                context.getString(R.string.descripcion),
                context.getString(R.string.importe)};
        int[] colspan = {2, 4, 9, 3};
        int[] rowspan = {1, 1, 1, 1};
        int[] alinh = {ALINEACION_CENTRO, ALINEACION_IZQUIERDA, ALINEACION_IZQUIERDA, ALINEACION_CENTRO};
        int[] alinv = {ALINEACION_MEDIO, ALINEACION_ARRIBA, ALINEACION_ARRIBA, ALINEACION_ABAJO};
        addTablaCab(tabla, cabecera, colspan, rowspan, alinh, alinv, 30, getFuente(HELVETICA, 18, BOLD, NEGRO), GRIS, BORDE_TODOS);

        ArrayList<String[]> lista = new ArrayList<>();

        if (listaModeloSQL != null && tipoCatalogo.equals(PRODUCTO)) {
            for (ModeloSQL modelo : listaModeloSQL.getLista()) {

                String[] partidatemp = {modelo.getString(PRODUCTO_RUTAFOTO),
                        modelo.getString(PRODUCTO_NOMBRE),
                        modelo.getString(PRODUCTO_DESCRIPCION),
                        JavaUtil.formatoMonedaLocal(modelo.getDouble(PRODUCTO_PRECIO))};

                lista.add(partidatemp);
            }
        }

        int[] colspand = {2, 4, 9, 3};

        addListaImagenTabla(tabla, 4, lista, 7, colspand, alinh, alinv, getFuente(HELVETICA, 14, NORMAL, NEGRO), BORDE_TODOS);

        dibujarTabla(new Paragraph(), tabla);
        cerrarPdf();

    }

}
