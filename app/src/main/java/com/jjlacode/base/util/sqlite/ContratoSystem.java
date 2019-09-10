package com.jjlacode.base.util.sqlite;

import android.net.Uri;

import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.android.AppActivity;

import java.util.ArrayList;
import java.util.UUID;

import static com.jjlacode.base.util.logica.InteractorBase.Constantes.CHAT;
import static com.jjlacode.base.util.logica.InteractorBase.Constantes.DETCHAT;
import static com.jjlacode.base.util.logica.InteractorBase.Constantes.LOG;


public class ContratoSystem implements JavaUtil.Constantes {

    public static final String AUTORIDAD_CONTENIDO =
            AppActivity.getNombreApp() + ".system";

    public static final Uri URI_BASE = Uri.parse("content://" + AUTORIDAD_CONTENIDO);

    public static final String BASE_CONTENIDOS = AUTORIDAD_CONTENIDO + ".";

    public static final String TIPO_CONTENIDO = "vnd.android.cursor.dir/vnd."
            + BASE_CONTENIDOS;

    public static final String TIPO_CONTENIDO_ITEM = "vnd.android.cursor.item/vnd."
            + BASE_CONTENIDOS;


    public interface Tablas {

        //TABLAS-----------------------------------------------------


        String TABLA_CHAT = CHAT;
        String TABLA_DETCHAT = DETCHAT;
        String TABLA_LOG = LOG;

        //COLUMNAS--------------------------------------------------------


        String CHAT_ID_CHAT = CAMPO_ID + TABLA_CHAT;
        String CHAT_USUARIO = CAMPO_USUARIO + TABLA_CHAT;
        String CHAT_NOMBRE = CAMPO_NOMBRE + TABLA_CHAT;
        String CHAT_TIPO = CAMPO_TIPO + TABLA_CHAT;
        String CHAT_TIPORETORNO = CAMPO_TIPORETORNO + TABLA_CHAT;
        String CHAT_ESPECIAL = "especial_" + TABLA_CHAT;
        String CHAT_CREATE = CAMPO_CREATEREG;
        String CHAT_TIMESTAMP = CAMPO_TIMESTAMP;

        String DETCHAT_ID_CHAT = CAMPO_ID + TABLA_DETCHAT + TABLA_CHAT;
        String DETCHAT_SECUENCIA = CAMPO_SECUENCIA;
        String DETCHAT_TIPO = CAMPO_TIPO + TABLA_DETCHAT;
        String DETCHAT_MENSAJE = "mensaje_" + TABLA_DETCHAT;
        String DETCHAT_NOTIFICADO = CAMPO_NOTIFICADO + TABLA_DETCHAT;
        String DETCHAT_FECHA = CAMPO_FECHA + TABLA_DETCHAT;
        String DETCHAT_CREATE = CAMPO_CREATEREG;
        String DETCHAT_TIMESTAMP = CAMPO_TIMESTAMP;

        String LOG_ID_LOG = CAMPO_ID + TABLA_LOG;
        String LOG_MENSAJE = "mensaje_" + TABLA_LOG;
        String LOG_CREATE = CAMPO_CREATEREG;
        String LOG_TIMESTAMP = CAMPO_TIMESTAMP;


        //REFERENCIAS----------------------------------------------------------

        String ID_CHAT = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                TABLA_CHAT, CHAT_ID_CHAT);

        //CAMPOS----------------------------------------------------------------


        String[] CAMPOS_CHAT = {"26", TABLA_CHAT,
                CHAT_ID_CHAT, "TEXT NON NULL UNIQUE", STRING,
                CHAT_USUARIO, "TEXT NON NULL", STRING,
                CHAT_NOMBRE, "TEXT NON NULL", STRING,
                CHAT_TIPO, "TEXT NON NULL", STRING,
                CHAT_TIPORETORNO, "TEXT NON NULL", STRING,
                CHAT_ESPECIAL, "INTEGER NON NULL DEFAULT 0", INT,
                CHAT_CREATE, "INTEGER NON NULL DEFAULT 0", LONG,
                CHAT_TIMESTAMP, "INTEGER NON NULL DEFAULT 0", LONG
        };

        String[] CAMPOS_DETCHAT = {"26", TABLA_DETCHAT,
                DETCHAT_ID_CHAT, String.format("TEXT NON NULL %s", ID_CHAT), STRING,
                DETCHAT_SECUENCIA, "INTEGER NON NULL", INT,
                DETCHAT_TIPO, "INTEGER NON NULL", INT,
                DETCHAT_MENSAJE, "TEXT", STRING,
                DETCHAT_NOTIFICADO, "INTEGER NON NULL DEFAULT 0", INT,
                DETCHAT_FECHA, "INTEGER NON NULL DEFAULT 0", LONG,
                DETCHAT_CREATE, "INTEGER NON NULL DEFAULT 0", LONG,
                DETCHAT_TIMESTAMP, "INTEGER NON NULL DEFAULT 0", LONG
        };

        String[] CAMPOS_LOG = {"14", TABLA_LOG,
                LOG_ID_LOG, String.format("TEXT NON NULL %s", ID_CHAT), STRING,
                LOG_MENSAJE, "TEXT", STRING,
                LOG_CREATE, "INTEGER NON NULL DEFAULT 0", LONG,
                LOG_TIMESTAMP, "INTEGER NON NULL DEFAULT 0", LONG
        };


    }

    public static final String PARAMETRO_FILTRO = "filtro";
    public static final String FILTRO_CLIENTE = "cliente";
    public static final String FILTRO_TOTAL = "total";
    public static final String FILTRO_FECHA = "fecha";
    public static final String FILTRO_ESTADO = "estado";
    public static final String FILTRO_RETRASO = "retraso";

    public static ArrayList<String[]> obtenerListaCampos() {

        ArrayList<String[]> listaCampos = new ArrayList<>();

        listaCampos.add(Tablas.CAMPOS_CHAT);
        listaCampos.add(Tablas.CAMPOS_DETCHAT);
        listaCampos.add(Tablas.CAMPOS_LOG);
        return listaCampos;
    }

    public static String[] obtenerCampos(String tabla) {

        ArrayList<String[]> listaCampos = obtenerListaCampos();

        for (String[] campo : listaCampos) {
            if (campo[1].equals(tabla)) {
                return campo;
            }
        }

        return null;
    }

    public static String getTabCab(String tabla) {

        switch (tabla) {


            case Tablas.TABLA_DETCHAT:

                return Tablas.TABLA_CHAT;

        }

        return null;
    }

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


    private ContratoSystem() {
    }
}
