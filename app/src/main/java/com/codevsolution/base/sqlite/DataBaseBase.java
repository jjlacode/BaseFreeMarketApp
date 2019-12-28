package com.codevsolution.base.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.CAMPO_ID;


public abstract class DataBaseBase extends SQLiteOpenHelper {

    String TABLA_TABLAS = "tablas";

    String TABLAS_ID_TABLA = CAMPO_ID + TABLA_TABLAS;
    String TABLAS_TABLA = "tabla_" + TABLA_TABLAS;
    String TABLAS_CAMPO = "campo_" + TABLA_TABLAS;
    String TABLAS_PARAMETROS = "parametros_" + TABLA_TABLAS;
    String TABLAS_TIPODATO = "tipo_dato_" + TABLA_TABLAS;

    private final Context contexto;
    protected String nombrebd;
    protected int version;

    public DataBaseBase(Context contexto, String nombrebd, int version) {
        super(contexto, nombrebd, null, version);
        this.contexto = contexto;
        this.nombrebd = nombrebd;
        this.version = version;
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        System.out.println("db = " + db);

        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NON NULL," +
                        "%s TEXT NON NULL" +
                        ")",
                TABLA_TABLAS, BaseColumns._ID,
                TABLAS_ID_TABLA,
                TABLAS_TABLA,
                TABLAS_CAMPO,
                TABLAS_PARAMETROS,
                TABLAS_TIPODATO

        ));

        Log.d("Construir db", "Creada tabla tablas");
        cargarDatosTabla(db);

        String[] proyeccion = {TABLAS_TABLA};
        Cursor cursor = db.query(true, TABLA_TABLAS, proyeccion, null, null,
                null, null, null, null);

        StringBuilder insert = null;

        while (cursor.moveToNext()) {

            String tbl = cursor.getString(cursor.getColumnIndex(TABLAS_TABLA));
            String seleccion = TABLAS_TABLA + " = '" + tbl + "'";

            insert = new StringBuilder(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,", tbl, BaseColumns._ID));

            Cursor campos = db.query(TABLA_TABLAS, null, seleccion,
                    null, null, null, null);

            while (campos.moveToNext()) {

                String nomCampo = campos.getString(campos.getColumnIndex(TABLAS_CAMPO));
                String parametrosCampo = campos.getString(campos.getColumnIndex(TABLAS_PARAMETROS));

                if (campos.isLast()) {
                    insert.append(nomCampo).append(" ").append(parametrosCampo).append(")");
                } else {
                    insert.append(nomCampo).append(" ").append(parametrosCampo).append(",");
                }
            }

            campos.close();
            db.execSQL(insert.toString());
            Log.d("Construir db", "Creada tabla " + tbl);

        }

        cursor.close();

        setOnCreate(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String[] proyeccion = {TABLAS_TABLA};
        Cursor cursor = db.query(true, TABLA_TABLAS, proyeccion, null, null,
                null, null, null, null);

        if (cursor.moveToNext()) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLAS_TABLA);

        }

        cursor.close();
        onCreate(db);

    }

    protected void setOnCreate(SQLiteDatabase db) {


    }

    private void cargarDatosTabla(SQLiteDatabase db) {

        ArrayList<String[]> listaCampos = setListaCampos();

        for (String[] campos : listaCampos) {

            construirTabla(db, campos);
        }

    }

    protected abstract ArrayList<String[]> setListaCampos();

    private void construirTabla(SQLiteDatabase db, String[] args) {

        ContentValues valores = new ContentValues();

        int fin = Integer.parseInt(args[0]);

        for (int i = 2; i < fin; i += 3) {

            valores.put(TABLAS_TABLA, args[1]);
            valores.put(TABLAS_CAMPO, args[i]);
            valores.put(TABLAS_PARAMETROS, args[i + 1]);
            valores.put(TABLAS_TIPODATO, args[i + 2]);
            valores.put(TABLAS_ID_TABLA, TABLA_TABLAS + UUID.randomUUID().toString());
            long res = db.insertOrThrow(TABLA_TABLAS, null, valores);
            if (res > 0) {
                Log.d("Construir db", nombrebd + " version: " + version +
                        " : Creado campo " + args[i] + " de tabla " + args[1]);
            } else {
                Log.d("Construir db", nombrebd + " version: " + version +
                        " : Error al Crear campo " + args[i] + " de tabla " + args[1]);
            }

        }
    }

}

