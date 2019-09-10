package com.codevsolution.base.util.sqlite;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.codevsolution.base.util.android.AndroidUtil;
import com.codevsolution.base.util.time.TimeDateUtil;

import static com.codevsolution.base.util.JavaUtil.Constantes.PREFERENCIAS;
import static com.codevsolution.base.util.JavaUtil.Constantes.TIMESTAMP;
import static com.codevsolution.base.util.JavaUtil.Constantes.TIMESTAMPDIA;
import static com.codevsolution.base.util.sqlite.ContratoSystem.AUTORIDAD_CONTENIDO;
import static com.codevsolution.base.util.sqlite.ContratoSystem.FILTRO_CLIENTE;
import static com.codevsolution.base.util.sqlite.ContratoSystem.FILTRO_FECHA;
import static com.codevsolution.base.util.sqlite.ContratoSystem.Tablas;
import static com.codevsolution.base.util.sqlite.ContratoSystem.crearUriTabla;
import static com.codevsolution.base.util.sqlite.ContratoSystem.crearUriTablaDetalle;
import static com.codevsolution.base.util.sqlite.ContratoSystem.generarIdTabla;
import static com.codevsolution.base.util.sqlite.ContratoSystem.generarMime;
import static com.codevsolution.base.util.sqlite.ContratoSystem.generarMimeItem;
import static com.codevsolution.base.util.sqlite.ContratoSystem.obtenerIdTabla;
import static com.codevsolution.base.util.sqlite.ContratoSystem.obtenerIdTablaDetalle;
import static com.codevsolution.base.util.sqlite.ContratoSystem.obtenerIdTablaDetalleId;

public class ProviderSystem extends ContentProvider
        implements Tablas {

    private DataBaseSystem bd;

    private ContentResolver resolver;

    public static final UriMatcher uriMatcher;

    // Casos

    public static final int CHAT = 155;
    public static final int CHAT_ID = 156;
    public static final int CHAT_ID_DETCHAT = 157;

    public static final int DETCHAT = 158;
    public static final int DETCHAT_ID = 159;

    public static final String AUTORIDAD = AUTORIDAD_CONTENIDO;//"jjlacode.com.freelanceproject2";

    static {

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


        uriMatcher.addURI(AUTORIDAD, TABLA_CHAT, CHAT);
        uriMatcher.addURI(AUTORIDAD, TABLA_CHAT + "/*", CHAT_ID);
        uriMatcher.addURI(AUTORIDAD, TABLA_CHAT + "/*/" + TABLA_DETCHAT, CHAT_ID_DETCHAT);

        uriMatcher.addURI(AUTORIDAD, TABLA_DETCHAT, DETCHAT);
        uriMatcher.addURI(AUTORIDAD, TABLA_DETCHAT + "/*", DETCHAT_ID);


    }


    public ProviderSystem() {

    }

    @Override
    public boolean onCreate() {


        bd = new DataBaseSystem(getContext());
        resolver = getContext().getContentResolver();
        return true;
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
            default:
                throw new UnsupportedOperationException("Uri desconocida =>" + uri);
        }
    }

    private ContentValues matcherUri(Uri uri) {

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
        }

        values.put("tablaModelo", tabla);
        values.put("idTabla", idTabla);
        values.put("proyeccion", proyeccion);
        values.put("setTablas", setTablas);
        values.put("esDetalle", esDetalle);
        values.put("esId", esId);

        return values;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = bd.getWritableDatabase();


        ContentValues valores = matcherUri(uri);

        String secuencia = values.getAsString("secuencia");
        String tabla = valores.getAsString("tablaModelo");
        String idTabla = valores.getAsString("idTabla");
        String id = generarIdTabla(tabla);

        System.out.println("valores = " + valores);


        if (tabla != null) {
            if (secuencia == null || Integer.parseInt(secuencia) == 0) {
                values.put(idTabla, id);
            }
            System.out.println("values = " + values);
            db.insertOrThrow(tabla, null, values);
            notificarCambio(uri);
            AndroidUtil.setSharePreference(getContext(), PREFERENCIAS, TIMESTAMP, TimeDateUtil.ahora());
            AndroidUtil.setSharePreference(getContext(), PREFERENCIAS, TIMESTAMPDIA, TimeDateUtil.ahora());


            if (secuencia != null && Integer.parseInt(secuencia) > 0) {
                id = values.getAsString(idTabla);
                return crearUriTablaDetalle(id, secuencia, tabla);
            } else {
                return crearUriTabla(id, tabla);
            }
        } else {
            throw new UnsupportedOperationException("Uri no soportada");
        }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Obtener base de datos
        SQLiteDatabase db = bd.getReadableDatabase();

        Cursor c;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        ContentValues valores = matcherUri(uri);

        String proy = valores.getAsString("proyeccion");
        String[] proyeccion = new String[]{proy};//proy.split(",");
        String setTablas = valores.getAsString("setTablas");
        String idTabla = valores.getAsString("idTabla");
        String[] ids = null;
        boolean esId = valores.getAsBoolean("esId");
        boolean esDetalle = valores.getAsBoolean("esDetalle");
        String tabla = valores.getAsString("tablaModelo");

        if (selection == null) {

            if (esDetalle && esId) {

                ids = obtenerIdTablaDetalle(uri);
                String id = ids[0];
                String secuencia = ids[1];
                selection = tabla + "." + idTabla + " = '" + id + "' AND " +
                        "secuencia = '" + secuencia + "'";
                System.out.println("secuencia = " + secuencia);
                System.out.println("id = " + id);
                System.out.println("selection = " + selection);

            } else if (esDetalle) {

                String id = obtenerIdTablaDetalleId(uri);
                selection = tabla + "." + idTabla + " = '" + id + "'";

            } else if (esId) {

                String id = obtenerIdTabla(uri);
                selection = idTabla + " = '" + id + "'";

            }
        }


        if (setTablas != null) {
            builder.setTables(setTablas);
            c = builder.query(db, proyeccion, selection,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(resolver, uri);

            return c;

        } else {

            throw new UnsupportedOperationException("Uri no soportada");
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = bd.getWritableDatabase();

        ContentValues valores = matcherUri(uri);

        String tabla = valores.getAsString("tablaModelo");
        String idTabla = valores.getAsString("idTabla");
        boolean esId = valores.getAsBoolean("esId");
        boolean esDetalle = valores.getAsBoolean("esDetalle");
        String id = null;
        String[] ids = null;
        String secuencia = null;
        String seleccion = null;

        if (selection == null) {
            if (!esDetalle) {
                id = obtenerIdTabla(uri);
                seleccion = idTabla + " = '" + id + "'";

            } else if (esDetalle && !esId) {

                id = obtenerIdTablaDetalleId(uri);
                seleccion = tabla + "." + idTabla + " = '" + id + "'";

            } else {
                ids = obtenerIdTablaDetalle(uri);
                id = ids[0];
                secuencia = ids[1];
                seleccion = idTabla + " = '" + id + "' AND " +
                        "secuencia = '" + secuencia + "'";
            }
        } else {
            seleccion = selection;
        }

        if (tabla != null) {

            notificarCambio(uri);
            AndroidUtil.setSharePreference(getContext(), PREFERENCIAS, TIMESTAMP, TimeDateUtil.ahora());
            AndroidUtil.setSharePreference(getContext(), PREFERENCIAS, TIMESTAMPDIA, TimeDateUtil.ahora());

            return db.update(tabla, values,
                    seleccion,
                    selectionArgs);

        } else {

            throw new UnsupportedOperationException("Uri no soportada");

        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = bd.getWritableDatabase();

        ContentValues valores = matcherUri(uri);

        String tabla = valores.getAsString("tablaModelo");
        String idTabla = valores.getAsString("idTabla");
        boolean esId = valores.getAsBoolean("esId");
        boolean esDetalle = valores.getAsBoolean("esDetalle");
        String id = null;
        String[] ids = null;
        String secuencia = null;
        String seleccion = null;

        if (selection == null) {

            if (!esDetalle) {
                id = obtenerIdTabla(uri);
                seleccion = idTabla + " = '" + id + "'";

            } else if (esDetalle && !esId) {

                id = obtenerIdTablaDetalleId(uri);
                seleccion = tabla + "." + idTabla + " = '" + id + "'";

            } else {
                ids = obtenerIdTablaDetalle(uri);
                id = ids[0];
                secuencia = ids[1];
                seleccion = idTabla + " = '" + id + "' AND " +
                        "secuencia = '" + secuencia + "'";
            }
        } else {
            seleccion = selection;
        }

        if (tabla != null) {
            notificarCambio(uri);
            AndroidUtil.setSharePreference(getContext(), PREFERENCIAS, TIMESTAMP, TimeDateUtil.ahora());
            AndroidUtil.setSharePreference(getContext(), PREFERENCIAS, TIMESTAMPDIA, TimeDateUtil.ahora());

            return db.delete(tabla, seleccion,
                    selectionArgs);
        } else {

            throw new UnsupportedOperationException("Uri no soportada");
        }
    }

    private void notificarCambio(Uri uri) {

        resolver.notifyChange(uri, null);
    }

    private String construirFiltro(String filtro) {

        String sentencia = null;

        switch (filtro) {
            case FILTRO_CLIENTE:
                sentencia = "cliente.nombres";
                break;
            case FILTRO_FECHA:
                sentencia = "proyecto.fecha";
                break;
        }

        return sentencia;
    }

}
