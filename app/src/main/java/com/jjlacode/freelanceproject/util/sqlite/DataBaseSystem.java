package com.jjlacode.freelanceproject.util.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DataBaseSystem extends DataBaseBase {

    private static final String NOMBRE_BASE_DATOS = "system.db";

    private static final int VERSION_ACTUAL = 1;

    private final Context contexto;

    public DataBaseSystem(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    protected void setOnCreate(SQLiteDatabase db) {
        super.setOnCreate(db);

        cargarDatosinicio(db);
    }

    @Override
    protected ArrayList<String[]> setListaCampos() {

        ArrayList<String[]> listaCampos = new ArrayList<>();

        return listaCampos;
    }

    private void cargarDatosinicio(SQLiteDatabase db) {

        /*
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
        */

    }

}

