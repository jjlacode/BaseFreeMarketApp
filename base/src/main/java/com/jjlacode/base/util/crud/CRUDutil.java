package com.jjlacode.base.util.crud;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.jjlacode.base.util.sqlite.ConsultaBD;
import com.jjlacode.freelanceproject.sqlite.ContratoPry;

import java.util.ArrayList;
import java.util.Set;

import static com.jjlacode.base.util.JavaUtil.Constantes.CAMPO_SECUENCIA;

public class CRUDutil {

    public static ListaModelo setListaModelo(String[] campos) {
        return new ListaModelo(campos);
    }

    public static ListaModelo clonaListaModelo(String[] campos, ListaModelo list) {
        ListaModelo lista = new ListaModelo(campos);
        lista.clearAddAllLista(list.getLista());
        return lista;
    }

    public static ListaModelo clonaListaModelo(String[] campos, ArrayList<Modelo> list) {
        ListaModelo lista = new ListaModelo(campos);
        lista.clearAddAllLista(list);
        return lista;
    }

    public static ListaModelo setListaModeloDetalle(String[] campos, String id, String tablaCab) {
        return new ListaModelo(campos, id, tablaCab, null, null);
    }

    public static ListaModelo setListaModelo(String[] campos, String seleccion) {
        return new ListaModelo(campos, seleccion, null);
    }

    public static ListaModelo setListaModeloDetalle(String[] campos, String id, String tablaCab, String seleccion) {
        return new ListaModelo(campos, id, tablaCab, seleccion, null);
    }

    public static ListaModelo setListaModelo(String[] campos, String seleccion, String orden) {
        return new ListaModelo(campos, seleccion, orden);
    }

    public static ListaModelo setListaModeloDetalle(String[] campos, String id, String tablaCab,
                                                    String seleccion, String orden) {
        return new ListaModelo(campos, id, tablaCab, seleccion, orden);
    }

    public static ListaModelo setListaModelo(String[] campos, String campo, String valor, int flag) {
        return new ListaModelo(campos, campo, valor, null, flag, null);
    }

    public static Modelo setModelo(String[] campos, String id) {

        return ConsultaBD.queryObject(campos, id);
    }

    public static Modelo setModelo(String[] campos, String id, int secuencia) {

        return ConsultaBD.queryObjectDetalle(campos, id, secuencia);
    }

    public static Modelo setModelo(String[] campos, String id, String secuencia) {
        return ConsultaBD.queryObjectDetalle(campos, id, secuencia);
    }

    public static int actualizarRegistro(String tabla, String id, ContentValues valores) {

        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarRegistro(String tabla, String id, int secuencia, ContentValues valores) {

        return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);
    }

    public static int actualizarRegistro(Modelo modelo, ContentValues valores) {

        String tabla = modelo.getNombreTabla();
        System.out.println("tabla = " + tabla);
        String id = modelo.getString(modelo.getCampoID());

        if (ContratoPry.getTabCab(tabla) != null) {

            int secuencia = modelo.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        System.out.println("id = " + id);
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarRegistro(String tabla, String id, String secuencia, ContentValues valores) {

        return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);
    }

    public static Uri crearRegistro(String tabla, ContentValues valores) {

        return ConsultaBD.insertRegistro(tabla, valores);
    }

    public static Uri crearRegistro(String tabla, String[] campos, String tablacab, String id, ContentValues valores) {

        return ConsultaBD.insertRegistroDetalle(campos, id, tablacab, valores);
    }

    public static String crearRegistroId(String tabla, ContentValues valores) {

        return ConsultaBD.idInsertRegistro(tabla, valores);
    }

    public static int crearRegistroSec(String[] campos, String id, String tablaCab, ContentValues valores) {

        return ConsultaBD.secInsertRegistroDetalle(campos, id, tablaCab, valores);
    }

    public static int borrarRegistro(String tabla, String id) {

        return ConsultaBD.deleteRegistro(tabla, id);
    }

    public static int borrarRegistro(String tabla, String id, int secuencia) {

        return ConsultaBD.deleteRegistroDetalle(tabla, id, secuencia);
    }

    public static String getID(Modelo modelo, String campoID) {

        return modelo.getString(campoID);
    }

    public static int getSecuencia(Modelo modelo) {
        return modelo.getInt("secuencia");
    }


    public static void setSharePreference(Context contexto, String sharePreference, String key, String valor) {

        SharedPreferences persistencia = contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = persistencia.edit();
        editor.putString(key, valor);
        editor.apply();

    }

    public static void setSharePreference(Context contexto, String sharePreference, String key, int valor) {

        SharedPreferences persistencia = contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = persistencia.edit();
        editor.putInt(key, valor);
        editor.apply();

    }

    public static void setSharePreference(Context contexto, String sharePreference, String key, long valor) {

        SharedPreferences persistencia = contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = persistencia.edit();
        editor.putLong(key, valor);
        editor.apply();

    }

    public static void setSharePreference(Context contexto, String sharePreference, String key, boolean valor) {

        SharedPreferences persistencia = contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = persistencia.edit();
        editor.putBoolean(key, valor);
        editor.apply();

    }

    public static void setSharePreference(Context contexto, String sharePreference, String key, float valor) {

        SharedPreferences persistencia = contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = persistencia.edit();
        editor.putFloat(key, valor);
        editor.apply();

    }

    public static void setSharePreference(Context contexto, String sharePreference, String key, Set<String> valor) {

        SharedPreferences persistencia = contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = persistencia.edit();
        editor.putStringSet(key, valor);
        editor.apply();

    }

    public static String getSharePreference(Context contexto, String sharePreference, String key, String defecto) {

        SharedPreferences persistencia = contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);

        return persistencia.getString(key, defecto);

    }

    public static int getSharePreference(Context contexto, String sharePreference, String key, int defecto) {

        SharedPreferences persistencia = contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);

        return persistencia.getInt(key, defecto);

    }

    public static long getSharePreference(Context contexto, String sharePreference, String key, long defecto) {

        SharedPreferences persistencia = contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);

        return persistencia.getLong(key, defecto);

    }

    public static boolean getSharePreference(Context contexto, String sharePreference, String key, boolean defecto) {

        SharedPreferences persistencia = contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);

        return persistencia.getBoolean(key, defecto);

    }

    public static float getSharePreference(Context contexto, String sharePreference, String key, float defecto) {

        SharedPreferences persistencia = contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);

        return persistencia.getFloat(key, defecto);

    }

    public static Set<String> getSharePreference(Context contexto, String sharePreference, String key, Set<String> defecto) {

        SharedPreferences persistencia = contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);

        return persistencia.getStringSet(key, defecto);

    }
}
