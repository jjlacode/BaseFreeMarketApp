package com.codevsolution.base.sqlite;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;

import static com.codevsolution.base.logica.InteractorBase.Constantes.USERID;
import static com.codevsolution.base.logica.InteractorBase.Constantes.USERIDCODE;
import static com.codevsolution.base.sqlite.ContratoSystem.AUTORIDAD_CONTENIDO;
import static com.codevsolution.base.sqlite.ContratoSystem.NULL;
import static com.codevsolution.base.sqlite.ContratoSystem.Tablas;
import static com.codevsolution.base.sqlite.ContratoSystem.generarMime;
import static com.codevsolution.base.sqlite.ContratoSystem.generarMimeItem;

public class ProviderSystem extends ProviderBase
        implements Tablas {

    private DataBaseSystem bd;

    private ContentResolver resolver;

    public static final UriMatcher uriMatcher;

    // Casos

    public static final int CHAT = 201;
    public static final int CHAT_ID = 202;
    public static final int CHAT_ID_DETCHAT = 203;

    public static final int DETCHAT = 204;
    public static final int DETCHAT_ID = 205;

    public static final int MARCADOR = 206;
    public static final int MARCADOR_ID = 207;

    public static final int ZONA = 208;
    public static final int ZONA_ID = 209;

    public static final int LOG = 210;
    public static final int LOG_ID = 211;

    public static final int USERS = 212;
    public static final int USERS_ID = 213;

    public static final String AUTORIDAD = AUTORIDAD_CONTENIDO;//"jjlacode.com.freelanceproject2";

    static {

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


        uriMatcher.addURI(AUTORIDAD, TABLA_CHAT, CHAT);
        uriMatcher.addURI(AUTORIDAD, TABLA_CHAT + "/*", CHAT_ID);
        uriMatcher.addURI(AUTORIDAD, TABLA_CHAT + "/*/" + TABLA_DETCHAT, CHAT_ID_DETCHAT);

        uriMatcher.addURI(AUTORIDAD, TABLA_DETCHAT, DETCHAT);
        uriMatcher.addURI(AUTORIDAD, TABLA_DETCHAT + "/*", DETCHAT_ID);

        uriMatcher.addURI(AUTORIDAD, TABLA_MARCADOR, MARCADOR);
        uriMatcher.addURI(AUTORIDAD, TABLA_MARCADOR + "/*", MARCADOR_ID);

        uriMatcher.addURI(AUTORIDAD, TABLA_ZONA, ZONA);
        uriMatcher.addURI(AUTORIDAD, TABLA_ZONA + "/*", ZONA_ID);

        uriMatcher.addURI(AUTORIDAD, TABLA_LOG, LOG);
        uriMatcher.addURI(AUTORIDAD, TABLA_LOG + "/*", LOG_ID);

    }


    public ProviderSystem() {

    }

    @Override
    public boolean onCreate() {

        if (super.onCreate()) {
            resolver = getContext().getContentResolver();
            return true;
        }
        return false;
    }

    @Override
    protected SQLiteOpenHelper setDataBase() {
        String idUser = AndroidUtil.getSharePreference(getContext(), USERID, USERIDCODE, NULL);
        String pathDb = Environment.getDataDirectory().getPath() + "/data/" + AppActivity.getPackage(getContext()) + "/databases/";
        if (idUser != null && !idUser.equals(NULL)) {

            return new DataBaseSystem(getContext(), idUser, pathDb);
        }
        return super.setDataBase();
    }



    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {

            case CHAT:
                return generarMime(TABLA_CHAT);
            case CHAT_ID:
                return generarMimeItem(TABLA_CHAT);
            case DETCHAT:
                return generarMime(TABLA_DETCHAT);
            case DETCHAT_ID:
                return generarMimeItem(TABLA_DETCHAT);
            case MARCADOR:
                return generarMime(TABLA_MARCADOR);
            case MARCADOR_ID:
                return generarMimeItem(TABLA_MARCADOR);
            case ZONA:
                return generarMime(TABLA_ZONA);
            case ZONA_ID:
                return generarMimeItem(TABLA_ZONA);
            case LOG:
                return generarMime(TABLA_LOG);
            case LOG_ID:
                return generarMimeItem(TABLA_LOG);

            default:
                throw new UnsupportedOperationException("Uri desconocida =>" + uri);
        }
    }

    protected ContentValues matcherUri(Uri uri) {

        String tabla = null;
        String idTabla = null;
        String setTablas = null;
        String proyeccion = null;
        boolean esDetalle = false;
        boolean esId = false;

        ContentValues values = new ContentValues();

        switch (uriMatcher.match(uri)) {


            case CHAT:
                tabla = TABLA_CHAT;
                setTablas = tabla;
                proyeccion = tabla + ".*";
                idTabla = CHAT_ID_CHAT;
                esDetalle = false;
                esId = false;
                break;

            case CHAT_ID:

                tabla = TABLA_CHAT;
                setTablas = tabla;
                proyeccion = tabla + ".*";
                idTabla = CHAT_ID_CHAT;
                esDetalle = false;
                esId = true;
                break;

            case CHAT_ID_DETCHAT:

                tabla = TABLA_DETCHAT;
                setTablas = tabla;
                proyeccion = tabla + ".*";
                idTabla = DETCHAT_ID_CHAT;
                esDetalle = true;
                esId = false;
                break;

            case DETCHAT:
                tabla = TABLA_DETCHAT;
                setTablas = tabla;
                proyeccion = tabla + ".*";
                idTabla = DETCHAT_ID_CHAT;
                esId = false;
                esDetalle = false;

                break;
            case DETCHAT_ID:

                tabla = TABLA_DETCHAT;
                setTablas = tabla;
                proyeccion = tabla + ".*";
                idTabla = DETCHAT_ID_CHAT;
                esDetalle = true;
                esId = true;
                break;
            case MARCADOR:
                tabla = TABLA_MARCADOR;
                setTablas = tabla;
                proyeccion = tabla + ".*";
                idTabla = MARCADOR_ID_MARCADOR;
                esDetalle = false;
                esId = false;
                break;

            case MARCADOR_ID:

                tabla = TABLA_MARCADOR;
                setTablas = tabla;
                proyeccion = tabla + ".*";
                idTabla = MARCADOR_ID_MARCADOR;
                esDetalle = false;
                esId = true;
                break;
            case ZONA:
                tabla = TABLA_ZONA;
                setTablas = tabla;
                proyeccion = tabla + ".*";
                idTabla = ZONA_ID_ZONA;
                esDetalle = false;
                esId = false;
                break;

            case ZONA_ID:

                tabla = TABLA_ZONA;
                setTablas = tabla;
                proyeccion = tabla + ".*";
                idTabla = ZONA_ID_ZONA;
                esDetalle = false;
                esId = true;
                break;

            case LOG:
                tabla = TABLA_LOG;
                setTablas = tabla;
                proyeccion = tabla + ".*";
                idTabla = LOG_ID_LOG;
                esDetalle = false;
                esId = false;
                break;

            case LOG_ID:

                tabla = TABLA_LOG;
                setTablas = tabla;
                proyeccion = tabla + ".*";
                idTabla = LOG_ID_LOG;
                esDetalle = false;
                esId = true;
                break;


        }

        values.put("tablaModelo", tabla);
        values.put("idTabla", idTabla);
        values.put("proyeccion", proyeccion);
        values.put("setTablas", setTablas);
        values.put("esDetalle", esDetalle);
        values.put("esId", esId);

        return values;
    }

    public Uri obtenerUriContenido(String tabla) {

        return ContratoSystem.obtenerUriContenido(
                tabla);

    }

    public String obtenerIdTabla(Uri uri) {

        return ContratoSystem.obtenerIdTabla(uri);

    }


    public Uri crearUriTabla(String id, String tabla) {

        return ContratoSystem.crearUriTabla(id,
                tabla);

    }

    public Uri crearUriTablaDetalle(String id, String secuencia, String tabla) {

        return ContratoSystem.crearUriTablaDetalle(id, secuencia,
                tabla);

    }

    public Uri crearUriTablaDetalle(String id, int secuencia, String tabla) {


        return ContratoSystem.crearUriTablaDetalle(id, secuencia,
                tabla);

    }

    public Uri crearUriTablaDetalleId(String id, String tabla, String tablaCab) {

        return ContratoSystem.crearUriTablaDetalleId(id, tabla,
                tablaCab);

    }

    public String obtenerTabCab(String tabla) {


        return ContratoSystem.getTabCab(tabla);

    }

    public String[] obtenerCampos(String tabla) {

        return ContratoSystem.obtenerCampos(tabla);

    }
}
