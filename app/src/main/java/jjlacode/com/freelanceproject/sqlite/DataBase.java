package jjlacode.com.freelanceproject.sqlite;

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

import jjlacode.com.freelanceproject.CommonPry;

import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.obtenerListaCampos;

public class DataBase extends SQLiteOpenHelper
        implements CommonPry.Constantes, Tablas, CommonPry.Estados, CommonPry.TiposEstados {

    private static final String NOMBRE_BASE_DATOS = "freelanceproject.db";

    private static final int VERSION_ACTUAL = 1;

    private final Context contexto;

    public DataBase(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
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
                        "%s TEXT"+
                        ")",
                TABLA_TABLAS, BaseColumns._ID,
                TABLAS_ID_TABLA,
                TABLAS_TABLA,
                TABLAS_CAMPO,
                TABLAS_PARAMETROS

        ));

        Log.d("db", "Creada tablaModelo tablaModelo");
        cargarDatosTabla(db);

        String[] proyeccion = {TABLAS_TABLA};
        Cursor cursor = db.query(true, TABLA_TABLAS, proyeccion,null,null,
                null,null,null,null);

        StringBuilder insert = null;

        while (cursor.moveToNext()){

            String tbl = cursor.getString(cursor.getColumnIndex(TABLAS_TABLA));
            String seleccion = TABLAS_TABLA + " = '" + tbl+"'";

            insert = new StringBuilder(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,", tbl, BaseColumns._ID));

            Cursor campos = db.query(TABLA_TABLAS,null,seleccion,
                    null,null,null,null);

            while (campos.moveToNext()){

                String nomCampo = campos.getString(campos.getColumnIndex(TABLAS_CAMPO));
                String parametrosCampo = campos.getString(campos.getColumnIndex(TABLAS_PARAMETROS));

                if (campos.isLast()) {
                    insert.append(nomCampo).append(" ").append(parametrosCampo).append(")");
                }else{
                    insert.append(nomCampo).append(" ").append(parametrosCampo).append(",");
                }
            }

            campos.close();
            db.execSQL(insert.toString());
            Log.d("db", "Creada tablaModelo "+tbl);

        }
        cursor.close();

        cargarDatosTipoCliente(db);
        cargarDatosEstados(db);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String[] proyeccion = {TABLAS_TABLA};
        Cursor cursor = db.query(true, TABLA_TABLAS, proyeccion,null,null,
                null,null,null,null);

        if (cursor.moveToNext()) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLAS_TABLA);

        }

        cursor.close();
        onCreate(db);

    }

    private void cargarDatosTipoCliente(SQLiteDatabase db) {

        long i = 0;

        ContentValues valores = new ContentValues();
        valores.put(TIPOCLIENTE_DESCRIPCION, PRINCIPAL);
        valores.put(TIPOCLIENTE_PESO, 15);
        valores.put(TIPOCLIENTE_ID_TIPOCLIENTE, TABLA_TIPOCLIENTE + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_TIPOCLIENTE, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto TiposCliente");
            i = 0;
        }

        valores.put(TIPOCLIENTE_DESCRIPCION, HABITUAL);
        valores.put(TIPOCLIENTE_PESO, 10);
        valores.put(TIPOCLIENTE_ID_TIPOCLIENTE, TABLA_TIPOCLIENTE + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_TIPOCLIENTE, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto TiposCliente");
            i = 0;
        }


        valores.put(TIPOCLIENTE_DESCRIPCION, NUEVO);
        valores.put(TIPOCLIENTE_PESO, 6);
        valores.put(TIPOCLIENTE_ID_TIPOCLIENTE, TABLA_TIPOCLIENTE + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_TIPOCLIENTE, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto TiposCliente");
            i = 0;
        }

        valores.put(TIPOCLIENTE_DESCRIPCION, OCASIONAL);
        valores.put(TIPOCLIENTE_PESO, 3);
        valores.put(TIPOCLIENTE_ID_TIPOCLIENTE, TABLA_TIPOCLIENTE + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_TIPOCLIENTE, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto TiposCliente");
            i = 0;
        }

        valores.put(TIPOCLIENTE_DESCRIPCION, PROSPECTO);
        valores.put(TIPOCLIENTE_PESO, 0);
        valores.put(TIPOCLIENTE_ID_TIPOCLIENTE, TABLA_TIPOCLIENTE + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_TIPOCLIENTE, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto TiposCliente");
            i = 0;
        }

    }

    private void cargarDatosEstados(SQLiteDatabase db) {

        long i;

        ContentValues valores = new ContentValues();
        valores.put(ESTADO_DESCRIPCION, NUEVOPRESUP);
        valores.put(ESTADO_TIPOESTADO, TNUEVOPRESUP);
        valores.put(ESTADO_ID_ESTADO, TABLA_ESTADO + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_ESTADO, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto Estado");
            i = 0;
        }

        valores.put(ESTADO_DESCRIPCION, PRESUPPENDENTREGA);
        valores.put(ESTADO_TIPOESTADO, TPRESUPPENDENTREGA);
        valores.put(ESTADO_ID_ESTADO, TABLA_ESTADO + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_ESTADO, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto Estado");
            i = 0;
        }

        valores.put(ESTADO_DESCRIPCION, PRESUPESPERA);
        valores.put(ESTADO_TIPOESTADO, TPRESUPESPERA);
        valores.put(ESTADO_ID_ESTADO, TABLA_ESTADO + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_ESTADO, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto Estado");
            i = 0;
        }

        valores.put(ESTADO_DESCRIPCION, PRESUPACEPTADO);
        valores.put(ESTADO_TIPOESTADO, TPRESUPACEPTADO);
        valores.put(ESTADO_ID_ESTADO, TABLA_ESTADO + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_ESTADO, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto Estado");
            i = 0;
        }

        valores.put(ESTADO_DESCRIPCION, PROYECTEJECUCION);
        valores.put(ESTADO_TIPOESTADO, TPROYECTEJECUCION);
        valores.put(ESTADO_ID_ESTADO, TABLA_ESTADO + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_ESTADO, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto Estado");
            i = 0;
        }

        valores.put(ESTADO_DESCRIPCION, PROYECPENDENTREGA);
        valores.put(ESTADO_TIPOESTADO, TPROYECPENDENTREGA);
        valores.put(ESTADO_ID_ESTADO, TABLA_ESTADO + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_ESTADO, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto Estado");
            i = 0;
        }

        valores.put(ESTADO_DESCRIPCION, PROYECTPENDCOBRO);
        valores.put(ESTADO_TIPOESTADO, TPROYECTPENDCOBRO);
        valores.put(ESTADO_ID_ESTADO, TABLA_ESTADO + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_ESTADO, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto Estado");
            i = 0;
        }

        valores.put(ESTADO_DESCRIPCION, PROYECTCOBRADO);
        valores.put(ESTADO_TIPOESTADO, TPROYECTCOBRADO);
        valores.put(ESTADO_ID_ESTADO, TABLA_ESTADO + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_ESTADO, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto Estado");
            i = 0;
        }

        valores.put(ESTADO_DESCRIPCION, PROYECTHISTORICO);
        valores.put(ESTADO_TIPOESTADO, TPROYECTHISTORICO);
        valores.put(ESTADO_ID_ESTADO, TABLA_ESTADO + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_ESTADO, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto Estado");
            i = 0;
        }

        valores.put(ESTADO_DESCRIPCION, PRESUPNOACEPTADO);
        valores.put(ESTADO_TIPOESTADO, TPRESUPNOACEPTADO);
        valores.put(ESTADO_ID_ESTADO, TABLA_ESTADO + UUID.randomUUID().toString());
        i = db.insertOrThrow(TABLA_ESTADO, null, valores);
        if (i > 0) {
            Log.d("datos_iniciales", "Insertados datos defecto Estado");
            i = 0;
        }

    }


    private void cargarDatosTabla(SQLiteDatabase db) {

        ArrayList<String[]> listaCampos = obtenerListaCampos();

        for (String[] campos : listaCampos) {

            construirTabla(db,campos);
        }

    }

    private void construirTabla(SQLiteDatabase db, String[] args){

        ContentValues valores = new ContentValues();

        int fin = Integer.parseInt(args[0]);

        for (int i = 2;i<fin;i+=3){

            valores.put(TABLAS_TABLA, args[1]);
            valores.put(TABLAS_CAMPO, args[i]);
            valores.put(TABLAS_PARAMETROS, args[i+1]);
            valores.put(TABLAS_ID_TABLA, TABLA_TABLAS + UUID.randomUUID().toString());
            long res = db.insertOrThrow(TABLA_TABLAS, null, valores);
            if (res > 0) {
                Log.d("datos_iniciales", "Insertado " + args[i]);
            } else {
                Log.d("datos_iniciales", "Error al Insertar " + args[i]);
            }

        }
    }

}

