package com.jjlacode.base.util.sqlite;

import android.net.Uri;

import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.android.AppActivity;

import java.util.UUID;


public class ContratoBaseSQLite implements JavaUtil.Constantes {

    public static final String AUTORIDAD_CONTENIDO =
            AppActivity.getNombreApp();

    public static final Uri URI_BASE = Uri.parse("content://" + AUTORIDAD_CONTENIDO);

    public static final String BASE_CONTENIDOS = AUTORIDAD_CONTENIDO + ".";

    public static final String TIPO_CONTENIDO = "vnd.android.cursor.dir/vnd."
            + BASE_CONTENIDOS;

    public static final String TIPO_CONTENIDO_ITEM = "vnd.android.cursor.item/vnd."
            + BASE_CONTENIDOS;


    public static final String PARAMETRO_FILTRO = "filtro";
    public static final String FILTRO_CLIENTE = "cliente";
    public static final String FILTRO_TOTAL = "total";
    public static final String FILTRO_FECHA = "fecha";
    public static final String FILTRO_ESTADO = "estado";
    public static final String FILTRO_RETRASO = "retraso";


    public static Uri obtenerUriContenido(String tabla) {

        return URI_BASE.buildUpon().appendPath(tabla).build();
    }

    public static Uri crearUriTabla(String id, String tabla) {

        Uri URI_CONTENIDO = obtenerUriContenido(tabla);

        return URI_CONTENIDO.buildUpon().appendPath(id).build();
    }

    public static Uri crearUriTablaDetalle(String id, String secuencia, String tabla) {
        // Uri de la forma 'gasto/:id#:secuencia'
        Uri URI_CONTENIDO = obtenerUriContenido(tabla);
        return URI_CONTENIDO.buildUpon()
                .appendPath(String.format("%s#%s", id, secuencia))
                .build();
    }

    public static Uri crearUriTablaDetalle(String id, int secuencia, String tabla) {
        // Uri de la forma 'gasto/:id#:secuencia'
        Uri URI_CONTENIDO = obtenerUriContenido(tabla);
        return URI_CONTENIDO.buildUpon()
                .appendPath(String.format("%s#%s", id, String.valueOf(secuencia)))
                .build();
    }

    public static Uri crearUriTablaDetalleId(String id, String tabla, String tablaCab) {

        Uri URI_CONTENIDO = obtenerUriContenido(tablaCab);
        return URI_CONTENIDO.buildUpon().appendPath(id).appendPath(tabla).build();

    }

    public static String obtenerIdTablaDetalleId(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    public static String[] obtenerIdTablaDetalle(Uri uri) {
        return uri.getLastPathSegment().split("#");
    }

    public static String generarIdTabla(String tabla) {
        return tabla + UUID.randomUUID().toString();
    }

    public static String obtenerIdTabla(Uri uri) {
        return uri.getLastPathSegment();
    }

    public static boolean tieneFiltro(Uri uri) {
        return uri != null && uri.getQueryParameter(PARAMETRO_FILTRO) != null;
    }


    public static String generarMime(String id) {
        if (id != null) {
            return TIPO_CONTENIDO + id;
        } else {
            return null;
        }
    }

    public static String generarMimeItem(String id) {
        if (id != null) {
            return TIPO_CONTENIDO_ITEM + id;
        } else {
            return null;
        }
    }

}
