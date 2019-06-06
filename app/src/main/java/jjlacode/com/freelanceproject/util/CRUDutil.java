package jjlacode.com.freelanceproject.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Set;

import jjlacode.com.freelanceproject.sqlite.ConsultaBD;

public class CRUDutil {

    public static ListaModelo setListaModelo(String[] campos){
        return new ListaModelo(campos);
    }

    public static ListaModelo clonaListaModelo(String[] campos,ListaModelo list){
        ListaModelo lista = new ListaModelo(campos);
        lista.clearAddAll(list.getLista());
        return lista;
    }

    public static ListaModelo clonaListaModelo(String[] campos, ArrayList<Modelo> list){
        ListaModelo lista = new ListaModelo(campos);
        lista.clearAddAll(list);
        return lista;
    }

    public static ListaModelo setListaModeloDetalle(String[] campos, String id, String tablaCab){
        return new ListaModelo(campos,id,tablaCab,null,null);
    }

    public static ListaModelo setListaModelo(String[] campos, String seleccion){
        return new ListaModelo(campos,seleccion,null);
    }

    public static ListaModelo setListaModeloDetalle(String[] campos, String id, String tablaCab, String seleccion){
        return new ListaModelo(campos,id,tablaCab,seleccion,null);
    }

    public static ListaModelo setListaModelo(String[] campos, String seleccion, String orden){
        return new ListaModelo(campos,seleccion,orden);
    }

    public static ListaModelo setListaModeloDetalle(String[] campos, String id, String tablaCab,
                                                String seleccion, String orden){
        return new ListaModelo(campos,id,tablaCab,seleccion,orden);
    }

    public static ListaModelo setListaModelo(String[] campos, String campo, String valor, int flag){
        return new ListaModelo(campos,campo,valor,null,flag,null);
    }

    public static Modelo setModelo(String[] campos, String id){

        return new ConsultaBD().queryObject(campos,id);
    }

    public static Modelo setModelo(String[] campos, String id, int secuencia){

        return new ConsultaBD().queryObjectDetalle(campos,id,secuencia);
    }

    public static Modelo setModelo(String[] campos, String id, String secuencia){
        return new ConsultaBD().queryObjectDetalle(campos,id,secuencia);
    }

    public static int actualizarRegistro(String tabla, String id, ContentValues valores){

        return new ConsultaBD().updateRegistro(tabla, id, valores);
    }

    public static int actualizarRegistro(String tabla, String id, int secuencia, ContentValues valores){

            return new ConsultaBD().updateRegistroDetalle(tabla, id, secuencia,valores);
    }

    public static int actualizarRegistro(String tabla, String id, String secuencia, ContentValues valores){

        return new ConsultaBD().updateRegistroDetalle(tabla, id, secuencia,valores);
    }

    public static Uri crearRegistro(String tabla, ContentValues valores){

            return new ConsultaBD().insertRegistro(tabla,valores);
    }

    public static Uri crearRegistro(String tabla, String[] campos, String tablacab, String id, ContentValues valores){

        return new ConsultaBD().insertRegistroDetalle(campos,id,tablacab,valores);
    }

    public static String crearRegistroId(String tabla, ContentValues valores){

        return new ConsultaBD().idInsertRegistro(tabla,valores);
    }

    public static int crearRegistroSec(String[] campos, String id, String tablaCab, ContentValues valores){

        return new ConsultaBD().secInsertRegistroDetalle(campos,id,tablaCab,valores);
    }

    public static int borrarRegistro(String tabla, String id ){

        return new ConsultaBD().deleteRegistro(tabla, id);
    }

    public static int borrarRegistro(String tabla, String id, int secuencia){

        return new ConsultaBD().deleteRegistroDetalle(tabla,id,secuencia);
    }

    public static String getID(Modelo modelo, String campoID){

        return modelo.getString(campoID);
    }

    public static int getSecuencia(Modelo modelo){
        return modelo.getInt("secuencia");
    }


    public static void setSharePreference(Context contexto, String sharePreference, String key, String valor){

        SharedPreferences persistencia=contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=persistencia.edit();
        editor.putString(key, valor);
        editor.apply();

    }

    public static void setSharePreference(Context contexto, String sharePreference, String key, int valor){

        SharedPreferences persistencia=contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=persistencia.edit();
        editor.putInt(key, valor);
        editor.apply();

    }

    public static void setSharePreference(Context contexto, String sharePreference, String key, long valor){

        SharedPreferences persistencia=contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=persistencia.edit();
        editor.putLong(key, valor);
        editor.apply();

    }

    public static void setSharePreference(Context contexto, String sharePreference, String key, boolean valor){

        SharedPreferences persistencia=contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=persistencia.edit();
        editor.putBoolean(key, valor);
        editor.apply();

    }

    public static void setSharePreference(Context contexto, String sharePreference, String key, float valor){

        SharedPreferences persistencia=contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=persistencia.edit();
        editor.putFloat(key, valor);
        editor.apply();

    }

    public static void setSharePreference(Context contexto, String sharePreference, String key, Set<String> valor){

        SharedPreferences persistencia=contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=persistencia.edit();
        editor.putStringSet(key, valor);
        editor.apply();

    }

    public static String getSharePreference(Context contexto, String sharePreference, String key, String defecto){

        SharedPreferences persistencia=contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);

        return persistencia.getString(key,defecto);

    }

    public static int getSharePreference(Context contexto, String sharePreference, String key, int defecto){

        SharedPreferences persistencia=contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);

        return persistencia.getInt(key,defecto);

    }

    public static long getSharePreference(Context contexto, String sharePreference, String key, long defecto){

        SharedPreferences persistencia=contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);

        return persistencia.getLong(key,defecto);

    }

    public static boolean getSharePreference(Context contexto, String sharePreference, String key, boolean defecto){

        SharedPreferences persistencia=contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);

        return persistencia.getBoolean(key,defecto);

    }

    public static float getSharePreference(Context contexto, String sharePreference, String key, float defecto){

        SharedPreferences persistencia=contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);

        return persistencia.getFloat(key,defecto);

    }

    public static Set<String> getSharePreference(Context contexto, String sharePreference, String key, Set<String> defecto){

        SharedPreferences persistencia=contexto.getSharedPreferences(sharePreference, Context.MODE_PRIVATE);

        return persistencia.getStringSet(key,defecto);

    }
}
