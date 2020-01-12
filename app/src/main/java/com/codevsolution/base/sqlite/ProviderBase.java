package com.codevsolution.base.sqlite;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.encrypt.EncryptUtil;
import com.codevsolution.base.time.TimeDateUtil;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.PREFERENCIAS;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.TIMESTAMP;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.TIMESTAMPDIA;
import static com.codevsolution.freemarketsapp.sqlite.ContratoPry.FILTRO_CLIENTE;
import static com.codevsolution.freemarketsapp.sqlite.ContratoPry.FILTRO_FECHA;
import static com.codevsolution.freemarketsapp.sqlite.ContratoPry.Tablas;
import static com.codevsolution.freemarketsapp.sqlite.ContratoPry.generarIdTabla;
import static com.codevsolution.freemarketsapp.sqlite.ContratoPry.obtenerIdTablaDetalle;
import static com.codevsolution.freemarketsapp.sqlite.ContratoPry.obtenerIdTablaDetalleId;

public class ProviderBase extends ContentProvider
        implements Tablas {

    protected ContentResolver resolver;
    protected SQLiteOpenHelper bd;

    public ProviderBase() {

    }

    @Override
    public boolean onCreate() {
        bd = setDataBase();
        return bd != null;
    }

    protected SQLiteOpenHelper setDataBase() {
        return null;
    }

    protected ContentValues matcherUri(Uri uri) {
        return null;
    }

    public Uri obtenerUriContenido(String tabla) {

        return null;
    }

    public String obtenerIdTabla(Uri uri) {

        return null;
    }


    public Uri crearUriTabla(String id, String tabla) {

        return null;
    }

    public Uri crearUriTablaDetalle(String id, String secuencia, String tabla) {

        return null;
    }

    public Uri crearUriTablaDetalle(String id, int secuencia, String tabla) {

        return null;
    }

    public Uri crearUriTablaDetalleId(String id, String tabla, String tablaCab) {

        return null;
    }

    public String obtenerTabCab(String tabla) {

        return null;
    }

    public String[] obtenerCampos(String tabla) {

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = null;
        if (bd != null) {
            db = bd.getWritableDatabase();
        } else {
            if (!onCreate()) {
                return null;
            }
            db = bd.getWritableDatabase();

        }

        ContentValues valores = matcherUri(uri);

        String secuencia = values.getAsString("secuencia");
        String tabla = valores.getAsString("tablaModelo");
        String idTabla = valores.getAsString("idTabla");
        String id = generarIdTabla(tabla);


        if (tabla != null) {
            if (secuencia == null || Integer.parseInt(EncryptUtil.decodificaStr(secuencia)) == 0) {
                values.put(idTabla, id);
            }
            db.insertOrThrow(tabla, null, values);
            notificarCambio(uri);
            AndroidUtil.setSharePreference(getContext(), PREFERENCIAS, TIMESTAMP, TimeDateUtil.ahora());
            AndroidUtil.setSharePreference(getContext(), PREFERENCIAS, TIMESTAMPDIA, TimeDateUtil.ahora());


            if (secuencia != null && Integer.parseInt(EncryptUtil.decodificaStr(secuencia)) > 0) {
                id = values.getAsString(idTabla);
                return crearUriTablaDetalle(id, EncryptUtil.codificaStr(secuencia), tabla);
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
        SQLiteDatabase db = null;
        if (bd != null) {
            db = bd.getWritableDatabase();
        } else {
            if (!onCreate()) {
                return null;
            }
            db = bd.getWritableDatabase();

        }

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

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = null;
        if (bd != null) {
            db = bd.getWritableDatabase();
        } else {
            if (!onCreate()) {
                return 0;
            }
            db = bd.getWritableDatabase();

        }

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

        SQLiteDatabase db = null;
        if (bd != null) {
            db = bd.getWritableDatabase();
        } else {
            if (!onCreate()) {
                return 0;
            }
            db = bd.getWritableDatabase();

        }

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
