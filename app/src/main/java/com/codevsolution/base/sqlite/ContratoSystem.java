package com.codevsolution.base.sqlite;

import android.net.Uri;

import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.encrypt.EncryptUtil;
import com.codevsolution.base.javautil.JavaUtil;

import java.util.ArrayList;
import java.util.UUID;

import static com.codevsolution.base.logica.InteractorBase.Constantes.CHAT;
import static com.codevsolution.base.logica.InteractorBase.Constantes.DETCHAT;
import static com.codevsolution.base.logica.InteractorBase.Constantes.LOG;
import static com.codevsolution.base.logica.InteractorBase.Constantes.MARCADOR;
import static com.codevsolution.base.logica.InteractorBase.Constantes.USERID;
import static com.codevsolution.base.logica.InteractorBase.Constantes.USERIDCODE;
import static com.codevsolution.base.logica.InteractorBase.Constantes.USERS;
import static com.codevsolution.base.logica.InteractorBase.Constantes.ZONA;


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
        String TABLA_USERS = USERS;
        String TABLA_DETCHAT = DETCHAT;
        String TABLA_MARCADOR = MARCADOR;
        String TABLA_ZONA = ZONA;
        String TABLA_LOG = LOG;


        //COLUMNAS--------------------------------------------------------


        String CHAT_ID_CHAT = CAMPO_ID + TABLA_CHAT;
        String CHAT_USUARIO = CAMPO_USUARIO + TABLA_CHAT;
        String CHAT_NOMBRE = CAMPO_NOMBRE + TABLA_CHAT;
        String CHAT_TIPO = CAMPO_TIPO + TABLA_CHAT;
        String CHAT_CREATE = CAMPO_CREATEREG;
        String CHAT_TIMESTAMP = CAMPO_TIMESTAMP;

        String USERS_ID_USERS = CAMPO_ID + TABLA_USERS;
        String USERS_USERID = USERID + TABLA_USERS;
        String USERS_USERIDCODE = USERIDCODE + TABLA_USERS;
        String USERS_CREATE = CAMPO_CREATEREG;
        String USERS_TIMESTAMP = CAMPO_TIMESTAMP;

        String DETCHAT_ID_CHAT = CAMPO_ID + TABLA_DETCHAT + TABLA_CHAT;
        String DETCHAT_SECUENCIA = CAMPO_SECUENCIA;
        String DETCHAT_TIPO = CAMPO_TIPO + TABLA_DETCHAT;
        String DETCHAT_MENSAJE = "mensaje_" + TABLA_DETCHAT;
        String DETCHAT_URL = "url" + TABLA_DETCHAT;
        String DETCHAT_NOTIFICADO = CAMPO_NOTIFICADO + TABLA_DETCHAT;
        String DETCHAT_FECHA = CAMPO_FECHA + TABLA_DETCHAT;
        String DETCHAT_CREATE = CAMPO_CREATEREG;
        String DETCHAT_TIMESTAMP = CAMPO_TIMESTAMP;

        String MARCADOR_ID_MARCADOR = CAMPO_ID + TABLA_MARCADOR;
        String MARCADOR_ID_REL = CAMPO_IDREL + TABLA_MARCADOR;
        String MARCADOR_ID_MARK = CAMPO_ID + "_mark_" + TABLA_MARCADOR;
        String MARCADOR_ALCANCE = "alcance_" + TABLA_MARCADOR;
        String MARCADOR_TIPO = CAMPO_TIPO + TABLA_MARCADOR;
        String MARCADOR_ACTIVO = CAMPO_ACTIVO + TABLA_MARCADOR;
        String MARCADOR_LATITUD = "latitud_" + TABLA_MARCADOR;
        String MARCADOR_LONGITUD = "longitud_" + TABLA_MARCADOR;
        String MARCADOR_CREATE = CAMPO_CREATEREG;
        String MARCADOR_TIMESTAMP = CAMPO_TIMESTAMP;

        String ZONA_ID_ZONA = CAMPO_ID + TABLA_ZONA;
        String ZONA_ID_REL = CAMPO_IDREL + TABLA_ZONA;
        String ZONA_NOMBRE = CAMPO_NOMBRE + TABLA_ZONA;
        String ZONA_ALCANCE = "alcance_" + TABLA_ZONA;
        String ZONA_CREATE = CAMPO_CREATEREG;
        String ZONA_TIMESTAMP = CAMPO_TIMESTAMP;

        String LOG_ID_LOG = CAMPO_ID + TABLA_LOG;
        String LOG_MENSAJE = "mensaje_" + TABLA_LOG;
        String LOG_CREATE = CAMPO_CREATEREG;
        String LOG_TIMESTAMP = CAMPO_TIMESTAMP;


        //REFERENCIAS----------------------------------------------------------

        String ID_CHAT = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                TABLA_CHAT, CHAT_ID_CHAT);

        //CAMPOS----------------------------------------------------------------


        String[] CAMPOS_CHAT = {"20", TABLA_CHAT,
                CHAT_ID_CHAT, "TEXT NON NULL UNIQUE", STRING,
                CHAT_USUARIO, "TEXT NON NULL", STRING,
                CHAT_NOMBRE, "TEXT NON NULL", STRING,
                CHAT_TIPO, "TEXT NON NULL DEFAULT 'chat'", STRING,
                CHAT_CREATE, "TEXT NON NULL DEFAULT 0", LONG,
                CHAT_TIMESTAMP, "TEXT NON NULL DEFAULT 0", LONG
        };

        String[] CAMPOS_USERS = {"17", TABLA_USERS,
                USERS_ID_USERS, "TEXT NON NULL", STRING,
                USERS_USERID, "TEXT NON NULL", STRING,
                USERS_USERIDCODE, "TEXT NON NULL", STRING,
                USERS_CREATE, "TEXT NON NULL DEFAULT 0", LONG,
                USERS_TIMESTAMP, "TEXT NON NULL DEFAULT 0", LONG
        };

        String[] CAMPOS_DETCHAT = {"29", TABLA_DETCHAT,
                DETCHAT_ID_CHAT, String.format("TEXT NON NULL %s", ID_CHAT), STRING,
                DETCHAT_SECUENCIA, "TEXT NON NULL", INT,
                DETCHAT_TIPO, "TEXT NON NULL", INT,
                DETCHAT_MENSAJE, "TEXT", STRING,
                DETCHAT_URL, "TEXT", STRING,
                DETCHAT_NOTIFICADO, "TEXT NON NULL DEFAULT 0", INT,
                DETCHAT_FECHA, "TEXT NON NULL DEFAULT 0", LONG,
                DETCHAT_CREATE, "TEXT NON NULL DEFAULT 0", LONG,
                DETCHAT_TIMESTAMP, "TEXT NON NULL DEFAULT 0", LONG
        };

        String[] CAMPOS_MARCADOR = {"32", TABLA_MARCADOR,
                MARCADOR_ID_MARCADOR, "TEXT NON NULL UNIQUE", STRING,
                MARCADOR_ID_REL, "TEXT NON NULL", STRING,
                MARCADOR_ID_MARK, "TEXT NON NULL", STRING,
                MARCADOR_ALCANCE, "TEXT NON NULL DEFAULT 1", INT,
                MARCADOR_ACTIVO, "TEXT NON NULL DEFAULT 1", INT,
                MARCADOR_LATITUD, "TEXT NON NULL", LONG,
                MARCADOR_LONGITUD, "TEXT NON NULL", LONG,
                MARCADOR_TIPO, "TEXT NON NULL", STRING,
                MARCADOR_CREATE, "TEXT NON NULL DEFAULT 0", LONG,
                MARCADOR_TIMESTAMP, "TEXT NON NULL DEFAULT 0", LONG

        };

        String[] CAMPOS_ZONA = {"20", TABLA_ZONA,
                ZONA_ID_ZONA, "TEXT NON NULL UNIQUE", STRING,
                ZONA_ID_REL, "TEXT NON NULL", STRING,
                ZONA_NOMBRE, "TEXT NON NULL", STRING,
                ZONA_ALCANCE, "TEXT NON NULL", STRING,
                ZONA_CREATE, "TEXT NON NULL DEFAULT 0", LONG,
                ZONA_TIMESTAMP, "TEXT NON NULL DEFAULT 0", LONG

        };

        String[] CAMPOS_LOG = {"14", TABLA_LOG,
                LOG_ID_LOG, String.format("TEXT NON NULL %s", ID_CHAT), STRING,
                LOG_MENSAJE, "TEXT", STRING,
                LOG_CREATE, "TEXT NON NULL DEFAULT 0", LONG,
                LOG_TIMESTAMP, "TEXT NON NULL DEFAULT 0", LONG
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
        listaCampos.add(Tablas.CAMPOS_MARCADOR);
        listaCampos.add(Tablas.CAMPOS_ZONA);
        listaCampos.add(Tablas.CAMPOS_LOG);
        listaCampos.add(Tablas.CAMPOS_USERS);

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
        return EncryptUtil.codificaStr(tabla + UUID.randomUUID().toString());
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
