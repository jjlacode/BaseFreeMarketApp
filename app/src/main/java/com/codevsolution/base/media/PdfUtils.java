package com.codevsolution.base.media;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.codevsolution.base.R;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.interfaces.ICFragmentos;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.shockwave.pdfium.PdfDocument;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.ACTUAL;
import static com.codevsolution.base.logica.InteractorBase.Constantes.VISORPDFMAIL;

public class PdfUtils {

    private final static String NOMBRE_DIRECTORIO = AppActivity.getAppContext().getString(R.string.app_name) + "/Pdf";
    private final static String ETIQUETA_ERROR = "ERROR PDF";
    public File archivoPDF;
    private Font fuente;
    private int fPDF = 0;
    private String nombrePdf;
    private Document documento;
    private PdfWriter writer;
    //private Paragraph paragraph;
    //protected PdfPTable tabla;
    //private PdfPCell pdfCell;
    protected int ALINEACION_IZQUIERDA = Element.ALIGN_LEFT;
    protected int ALINEACION_CENTRO = Element.ALIGN_CENTER;
    protected int ALINEACION_DERECHA = Element.ALIGN_RIGHT;
    protected int ALINEACION_ABAJO = Element.ALIGN_BOTTOM;
    protected int ALINEACION_ARRIBA = Element.ALIGN_TOP;
    protected int ALINEACION_MEDIO = Element.ALIGN_MIDDLE;
    protected int ALINEACION_JUSTIFICADA = Element.ALIGN_JUSTIFIED;
    protected int ALINEACION_JUSTIFICADA_TODO = Element.ALIGN_JUSTIFIED_ALL;
    protected int ALINEACION_BASELINE = Element.ALIGN_BASELINE;
    protected int ALINEACION_INDEFINIDA = Element.ALIGN_UNDEFINED;
    protected String HELVETICA = FontFactory.HELVETICA;
    protected String HELVETICA_BOLD = FontFactory.HELVETICA_BOLD;
    protected String HELVETICA_BOLD_OBLIQUE = FontFactory.HELVETICA_BOLDOBLIQUE;
    protected String HELVETICA_OBLIQUE = FontFactory.HELVETICA_OBLIQUE;
    protected String TIMES = FontFactory.TIMES;
    protected String TIMES_BOLD = FontFactory.TIMES_BOLD;
    protected String TIMES_BOLD_ITALIC = FontFactory.TIMES_BOLDITALIC;
    protected String TIMES_ITALIC = FontFactory.TIMES_ITALIC;
    protected String TIMES_ROMAN = FontFactory.TIMES_ROMAN;
    protected String COURIER = FontFactory.COURIER;
    protected String COURIER_BOLD = FontFactory.COURIER_BOLD;
    protected String COURIER_BOLD_OBLIQUE = FontFactory.COURIER_BOLDOBLIQUE;
    protected String COURIER_OBLIQUE = FontFactory.COURIER_OBLIQUE;
    protected int ITALIC = Font.ITALIC;
    protected int NORMAL = Font.NORMAL;
    protected int BOLD = Font.BOLD;
    protected int BOLDITALIC = Font.BOLDITALIC;
    protected int DEFAULTSIZE = Font.DEFAULTSIZE;
    protected final int NEGRO = 0;
    protected final int ROJO = 1;
    protected final int AZUL = 2;
    protected final int CYAN = 3;
    protected final int GRIS = 4;
    protected final int VERDE = 5;
    protected final int MAGENTA = 6;
    protected final int BLANCO = 7;
    protected final int SIN_BORDES = Rectangle.NO_BORDER;
    protected final int BORDE_DERECHO = Rectangle.RIGHT;
    protected final int BORDE_IZQUIERDO = Rectangle.LEFT;
    protected final int BORDE_ARRIBA = Rectangle.TOP;
    protected final int BORDE_ABAJO = Rectangle.BOTTOM;
    protected final int BORDE_TODOS = Rectangle.BOX;

    protected Uri fileUri;
    protected String path;
    protected Context context = AppActivity.getAppContext();


    protected PdfUtils() {

    }

    public Uri getFileUri(Context context) {

        buscarPDF();
        return fileUri;
    }

    public String getPath() {
        return path;
    }

    protected void setFuente(String fontname, float size, int style, int color) {

        BaseColor baseColor = getColor(color);

        fuente = FontFactory.getFont(fontname, size, style, baseColor);
    }

    protected Font getFuente(String fontname, float size, int style, int color) {

        BaseColor baseColor = getColor(color);

        return FontFactory.getFont(fontname, size, style, baseColor);
    }

    private BaseColor getColor(int color) {

        BaseColor baseColor = null;
        switch (color) {

            case NEGRO:
                baseColor = BaseColor.BLACK;
                break;
            case ROJO:
                baseColor = BaseColor.RED;
                break;
            case AZUL:
                baseColor = BaseColor.BLUE;
                break;
            case CYAN:
                baseColor = BaseColor.CYAN;
                break;
            case GRIS:
                baseColor = BaseColor.GRAY;
                break;
            case VERDE:
                baseColor = BaseColor.GREEN;
                break;
            case MAGENTA:
                baseColor = BaseColor.MAGENTA;
                break;
        }

        return baseColor;
    }

    protected void verPDFAPP(Context context) {
        //Primero busca el archivo
        buscarPDF();
        if (fPDF == 1) {
            Uri uri = fileUri;//Uri.fromFile(archivoPDF);
            //Creo un Intent para inciar una nueva actividad, pero de otra APP
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "applicacion/PDF");
            //Try catch ´por si no existe una APP en el dispositivo
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=cn.wps.moffice_eng&hl=es")));
                //Mensaje en un Toast para que se visualize el posible error
                Toast.makeText(AppActivity.getAppContext(),
                        "No hay una APP para ver PDFs", Toast.LENGTH_LONG).show();
                Log.e("verPDFAPP", e.toString());
            }

        } else if (fPDF == 0) {
            Toast.makeText(context, "No existe archivo PDF para LEER", Toast.LENGTH_LONG).show();
        }


    }

    public String getNombreArchivo() {

        return archivoPDF.getName();
    }

    public void setNombreArchivo(String nombreArchivo) {

        this.nombrePdf = nombreArchivo;
    }

    public Uri getUriArchivo() {

        return AppActivity.getUriFromFile(archivoPDF);
    }

    public String getRutaArchivo() {

        buscarPDF();

        return path;
    }


    public void enviarPDFEmail(ICFragmentos icFragmentos, Context context,
                               String email, String asunto, String texto) {

        buscarPDF();
        if (fPDF == 1) {

            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            bundle.putString("asunto", asunto);
            bundle.putString("texto", texto);
            bundle.putString("path", archivoPDF.getAbsolutePath());
            bundle.putString(ACTUAL, VISORPDFMAIL);
            icFragmentos.enviarBundleAFragment(bundle, new VisorPDFEmail());
            Toast.makeText(context, "Si existe archivo PDF para Enviar", Toast.LENGTH_LONG).show();
        } else if (fPDF == 0) {
            Toast.makeText(context, "No existe archivo PDF para Enviar", Toast.LENGTH_LONG).show();
        }

    }

    public void enviarPDFEmail(Context context, String path,
                               String email, String asunto, String texto) {

        buscarPDF(path);
        if (fPDF == 1) {

            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            bundle.putString("asunto", asunto);
            bundle.putString("texto", texto);
            bundle.putString("path", archivoPDF.getAbsolutePath());
            bundle.putString(ACTUAL, VISORPDFMAIL);
            AppActivity.enviarEmail(context, email, asunto, texto, path);
        } else if (fPDF == 0) {
            Toast.makeText(context, "No existe archivo PDF para Enviar", Toast.LENGTH_LONG).show();
        }

    }

    public boolean buscarPDF() {
        try {
            File carpeta = null;
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                carpeta = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        NOMBRE_DIRECTORIO);
                archivoPDF = new File(carpeta, nombrePdf + ".pdf");


                //Verifica si la carpeta existe
                if (archivoPDF.exists()) {
                    fPDF = 1;     //La bandera esta activada para leer PDFs
                    fileUri = AppActivity.getUriFromFile(archivoPDF);
                    path = archivoPDF.getPath();
                    System.out.println("path = " + path);
                    return true;
                } else {
                    fPDF = 0;     //La bandera está desactivada para leer los PDFs
                }

            }
        } catch (Exception e) {
            //Mensaje que me dirá si hay errores
            Log.e(ETIQUETA_ERROR, e.toString());
        }

        return false;
    }

    public boolean buscarPDF(String path) {
        try {

            archivoPDF = new File(path);
            //Verifica si la carpeta existe
            if (archivoPDF.exists()) {
                fPDF = 1;     //La bandera esta activada para leer PDFs
                fileUri = AppActivity.getUriFromFile(archivoPDF);
                this.path = archivoPDF.getAbsolutePath();
                return true;
            } else {
                fPDF = 0;     //La bandera está desactivada para leer los PDFs
            }

        } catch (Exception e) {
            //Mensaje que me dirá si hay errores
            Log.e(ETIQUETA_ERROR, e.toString());
        }

        return false;
    }

    public boolean abrirPdf(String nombrePdf) {

        if (crearPdf(nombrePdf)) {
            documento.open();
            return true;
        }

        return false;

    }


    public void addParrafo(int alineacion, String... args) {

        Paragraph paragraph = new Paragraph();
        for (String arg : args) {

            if (null != arg) {
                Paragraph childParagraph = new Paragraph(arg, fuente);
                childParagraph.setAlignment(alineacion);
                paragraph.add(childParagraph);
            } else {
                break;
            }
        }
        paragraph.setSpacingBefore(5);
        paragraph.setSpacingAfter(5);
        try {
            documento.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    public void addParrafo(String... args) {

        Paragraph paragraph = new Paragraph();
        for (String arg : args) {

            if (null != arg) {
                Paragraph childParagraph = new Paragraph(arg, fuente);
                childParagraph.setAlignment(Element.ALIGN_CENTER);
                paragraph.add(childParagraph);
            } else {
                break;
            }
        }
        paragraph.setSpacingBefore(5);
        paragraph.setSpacingAfter(5);
        try {
            documento.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }


    public void addMetadata(String titulo, String tema, String autor) {

        documento.addTitle(titulo);
        documento.addSubject(tema);
        documento.addAuthor(autor);

    }


    public void addResource(int resource, Context context, int alineacion, int ancho, int alto) {

        if (resource > 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                    resource);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image imagen = null;
            try {
                imagen = Image.getInstance(stream.toByteArray());
                imagen.scaleToFit(ancho, alto);
                imagen.setAlignment(alineacion);

            } catch (BadElementException | IOException e) {
                e.printStackTrace();
            }
            try {
                documento.add(imagen);
            } catch (DocumentException e) {
                e.printStackTrace();
                Log.e("Añadir recurso", e.toString());
            }
        }
    }

    public void addImagen(String rutafoto, int alineacion, int ancho, int alto) {

        if (rutafoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(rutafoto);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            }
            Image imagen = null;
            try {
                imagen = Image.getInstance(stream.toByteArray());
                imagen.scaleToFit(ancho, alto);
                imagen.setAlignment(alineacion);
            } catch (BadElementException | IOException e) {
                e.printStackTrace();
            }
            try {
                documento.add(imagen);
            } catch (DocumentException e) {
                e.printStackTrace();
                Log.e("Añadir imagen", e.toString());
            }
        }
    }

    public void addTabla(String[] cabeceras, ArrayList<String[]> datos, int columnas,
                         int[] colspan, int altoceldas, int porcentaje, Font fuentecab, Font fuentefilas, int colorcab) {

        try {
            Paragraph paragraph = new Paragraph();
            paragraph.setFont(fuentecab);
            PdfPTable tabla = new PdfPTable(columnas);
            tabla.setWidthPercentage(porcentaje);
            tabla.setSpacingBefore(20);
            PdfPCell pdfCell;
            int indexC = 0;
            while (indexC < cabeceras.length) {

                pdfCell = new PdfPCell(new Phrase(cabeceras[indexC], fuentecab));
                pdfCell.setColspan(colspan[indexC]);
                pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfCell.setBackgroundColor(getColor(colorcab));
                pdfCell.setFixedHeight(altoceldas);
                pdfCell.setVerticalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(pdfCell);
                indexC++;
            }
            for (int i = 0; i < datos.size(); i++) {
                String[] row = datos.get(i);
                for (indexC = 0; indexC < cabeceras.length; indexC++) {

                    pdfCell = new PdfPCell(new Phrase(row[indexC], fuentefilas));
                    pdfCell.setColspan(colspan[indexC]);
                    pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfCell.setVerticalAlignment(Element.ALIGN_CENTER);
                    pdfCell.setFixedHeight(altoceldas);
                    tabla.addCell(pdfCell);
                }
            }

            paragraph.add(tabla);
            documento.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
            Log.e("Crear tabla", e.toString());
        }
    }

    protected PdfPTable crearTabla(int columnas) {

        PdfPTable tabla = new PdfPTable(columnas);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(20);

        return tabla;

    }

    protected PdfPTable crearTabla(int columnas, int espacioAntes) {

        PdfPTable tabla = new PdfPTable(columnas);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(espacioAntes);

        return tabla;

    }

    protected PdfPTable crearTabla(int columnas, int espacioAntes, int porcentaje) {

        PdfPTable tabla = new PdfPTable(columnas);
        tabla.setWidthPercentage(porcentaje);
        tabla.setSpacingBefore(espacioAntes);

        return tabla;

    }

    protected void addTablaCab(PdfPTable tabla, String[] cabeceras, int[] colspan, int[] rowspan, int[] alinh,
                               int[] alinv, int altoceldas, Font fuentecab, int colorcab, int borde) {

        int indexC = 0;
        while (indexC < cabeceras.length) {

            PdfPCell pdfCell = new PdfPCell(new Phrase(cabeceras[indexC], fuentecab));
            pdfCell.setColspan(colspan[indexC]);
            pdfCell.setRowspan(rowspan[indexC]);
            pdfCell.setHorizontalAlignment(alinh[indexC]);
            pdfCell.setBackgroundColor(getColor(colorcab));
            pdfCell.setFixedHeight(altoceldas);
            pdfCell.setVerticalAlignment(alinv[indexC]);
            pdfCell.setHorizontalAlignment(alinh[indexC]);
            pdfCell.setBorder(borde);
            tabla.addCell(pdfCell);
            indexC++;
        }
    }

    protected void addCellDato(PdfPTable tabla, String dato, int colspan, int rowspan, int altocell,
                               int alinv, int alinh, int colorcell, int borde) {

        PdfPCell pdfCell = new PdfPCell(new Phrase(dato));
        pdfCell.setColspan(colspan);
        pdfCell.setRowspan(rowspan);
        pdfCell.setBackgroundColor(getColor(colorcell));
        pdfCell.setHorizontalAlignment(alinh);
        pdfCell.setVerticalAlignment(alinv);
        pdfCell.setFixedHeight(altocell);
        pdfCell.setBorder(borde);
        tabla.addCell(pdfCell);
    }

    protected void addCellTabla(PdfPTable tabla, PdfPTable tablaCell, int colspan, int rowspan, int altocell,
                                int alinv, int alinh, int colorcell, int borde) {

        PdfPCell pdfCell = new PdfPCell(tablaCell);
        pdfCell.setColspan(colspan);
        pdfCell.setRowspan(rowspan);
        pdfCell.setBackgroundColor(getColor(colorcell));
        pdfCell.setHorizontalAlignment(alinh);
        pdfCell.setVerticalAlignment(alinv);
        pdfCell.setFixedHeight(altocell);
        pdfCell.setBorder(borde);
        tabla.addCell(pdfCell);
    }

    protected void addCellImage(PdfPTable tabla, String rutafoto, float ancho, float alto, int colspan, int rowspan, int altocell,
                                int alinv, int alinh, int colorcell, int borde) {

        if (rutafoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(rutafoto);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image imagen = null;
            try {
                imagen = Image.getInstance(stream.toByteArray());
                imagen.scaleToFit(ancho, alto);
            } catch (BadElementException | IOException e) {
                e.printStackTrace();
            }

            PdfPCell pdfCell = new PdfPCell(Image.getInstance(imagen));
            pdfCell.setColspan(colspan);
            pdfCell.setRowspan(rowspan);
            pdfCell.setBackgroundColor(getColor(colorcell));
            pdfCell.setHorizontalAlignment(alinh);
            pdfCell.setVerticalAlignment(alinv);
            pdfCell.setFixedHeight(altocell);
            pdfCell.setBorder(borde);
            tabla.addCell(pdfCell);
        }
    }

    protected void addListaTabla(PdfPTable tabla, int col, ArrayList<String[]> datos,
                                 int[] colspan, int[] alinh, int[] alinv, Font fuentefilas, int borde) {

        int indexC = 0;

        for (int i = 0; i < datos.size(); i++) {
            String[] row = datos.get(i);
            int maxRow = 1;
            int alto = 0;
            for (indexC = 0; indexC < col; indexC++) {

                if ((int) Math.round(((double) row[indexC].length()) / (colspan[indexC] * 3)) > maxRow) {
                    maxRow = (int) Math.round(((double) row[indexC].length()) / (colspan[indexC] * 3));
                }
            }
            alto = maxRow * 20;
            if (alto < 40) {
                alto = 40;
            }

            for (indexC = 0; indexC < col; indexC++) {

                PdfPCell pdfCell = new PdfPCell(new Phrase(row[indexC], fuentefilas));
                pdfCell.setColspan(colspan[indexC]);
                pdfCell.setRowspan(1);
                pdfCell.setHorizontalAlignment(alinh[indexC]);
                pdfCell.setVerticalAlignment(alinv[indexC]);
                pdfCell.setFixedHeight(alto);
                pdfCell.setBorder(borde);

                tabla.addCell(pdfCell);
            }
        }
    }

    protected void addListaImagenTabla(PdfPTable tabla, int col, ArrayList<String[]> datos, int colorCellImagen,
                                       int[] colspan, int[] alinh, int[] alinv, Font fuentefilas, int borde) {

        int indexC = 0;

        for (int i = 0; i < datos.size(); i++) {
            String[] row = datos.get(i);

            int maxRow = 1;
            int alto = 0;
            for (indexC = 1; indexC < col; indexC++) {

                if (row[indexC] != null && colspan[indexC] > 0 && (int) Math.round(((double) row[indexC].length()) / (colspan[indexC] * 3)) > maxRow) {
                    maxRow = (int) Math.round(((double) row[indexC].length()) / (colspan[indexC] * 3));
                }
            }
            alto = maxRow * 20;
            if (alto < 40) {
                alto = 40;
            }
            addCellImage(tabla, row[0], 40, 40, colspan[0], 1,
                    alto, alinv[0], alinh[0], colorCellImagen, borde);

            for (indexC = 1; indexC < col; indexC++) {

                PdfPCell pdfCell = new PdfPCell(new Phrase(row[indexC], fuentefilas));
                pdfCell.setColspan(colspan[indexC]);
                pdfCell.setRowspan(1);
                pdfCell.setHorizontalAlignment(alinh[indexC]);
                pdfCell.setVerticalAlignment(alinv[indexC]);
                pdfCell.setFixedHeight(alto);
                tabla.addCell(pdfCell);
            }
        }
    }

    protected void dibujarTabla(Paragraph paragraph, PdfPTable tabla) {

        try {
            paragraph.add(tabla);
            documento.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
            Log.e("Crear tabla", e.toString());
        }
    }

    protected void visorPdf(Class clase, Context context) {

        buscarPDF();
        if (fPDF == 1) {
            Intent intent = new Intent(context, clase);
            intent.putExtra("path", archivoPDF.getAbsolutePath());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            Toast.makeText(context, "Si existe archivo PDF para LEER", Toast.LENGTH_LONG).show();
        } else if (fPDF == 0) {
            Toast.makeText(context, "No existe archivo PDF para LEER", Toast.LENGTH_LONG).show();
        }

    }

    public void marcaAgua(String marca) {

        ColumnText.showTextAligned(writer.getDirectContentUnder(),
                ALINEACION_CENTRO, new Paragraph(
                        marca, fuente), 297.5f, 421,
                writer.getPageNumber() % 2 == 1 ? 45 : -45);
    }

    private void addChildP(Paragraph paragraph, Paragraph childParagraph, int alineacion) {

        childParagraph.setAlignment(alineacion);
        paragraph.add(childParagraph);
    }


    private boolean crearPdf(String nombrePdf) {

        // Creamos el documento.
        documento = new Document(PageSize.A4);
        this.nombrePdf = nombrePdf;

        try {

            // Creamos el fichero con el nombre que deseemos.
            archivoPDF = crearFicheroPdf(nombrePdf);

            // Creamos el flujo de datos de salida para el fichero donde
            // guardaremos el pdf.
            FileOutputStream ficheroPdf = new FileOutputStream(
                    archivoPDF.getAbsolutePath());

            // Asociamos el flujo que acabamos de crear al documento.
            writer = PdfWriter.getInstance(documento, ficheroPdf);

            return true;

        } catch (DocumentException e) {

            Log.e(ETIQUETA_ERROR, e.getMessage());

        } catch (IOException e) {

            Log.e(ETIQUETA_ERROR, e.getMessage());

        }

        return false;
    }

    public void cerrarPdf() {

        // Cerramos el documento.
        documento.close();

    }


    public static File crearFicheroPdf(String nombreFichero) throws IOException {
        File ruta = getRutaPdf();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero + ".pdf");
        return fichero;
    }

    public static File getRutaPdf() {

        // El fichero será almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    NOMBRE_DIRECTORIO);

            if (!ruta.mkdirs()) {
                if (!ruta.exists()) {
                    return null;
                }
            }
        }

        return ruta;
    }

    public static void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

}
