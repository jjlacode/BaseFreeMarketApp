package com.codevsolution.base.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static com.codevsolution.base.logica.InteractorBase.Constantes.SYSTEM;

public class DataBaseSystem extends DataBaseBase {

    private static final String NOMBRE_BASE_DATOS = SYSTEM;

    private static final int VERSION_ACTUAL = 1;

    private final Context contexto;

    public DataBaseSystem(Context contexto, String idUser, String pathDb) {
        super(contexto, pathDb + NOMBRE_BASE_DATOS + idUser + ".db", VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    protected void setOnCreate(SQLiteDatabase db) {
        super.setOnCreate(db);

        cargarDatosinicio(db);
    }

    @Override
    protected ArrayList<String[]> setListaCampos() {

        return ContratoSystem.obtenerListaCampos();
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

