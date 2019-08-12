package com.jjlacode.freelanceproject.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jjlacode.base.util.sqlite.DataBaseBase;
import com.jjlacode.freelanceproject.logica.Interactor;

import java.util.ArrayList;
import java.util.UUID;

import static com.jjlacode.freelanceproject.sqlite.ContratoPry.obtenerListaCampos;


public class DataBase extends DataBaseBase
        implements Interactor.Constantes, ContratoPry.Tablas, Interactor.Estados, Interactor.TiposEstados {

    private static final String NOMBRE_BASE_DATOS = "freelanceproject.db";

    private static final int VERSION_ACTUAL = 1;

    private final Context contexto;

    public DataBase(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    protected void setOnCreate(SQLiteDatabase db) {
        super.setOnCreate(db);

        cargarDatosTipoCliente(db);
        cargarDatosEstados(db);

    }

    @Override
    protected ArrayList<String[]> setListaCampos() {
        return obtenerListaCampos();
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

}

