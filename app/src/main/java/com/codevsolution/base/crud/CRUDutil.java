package com.codevsolution.base.crud;

import android.content.ContentValues;
import android.net.Uri;

import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ContratoPry;

import java.util.ArrayList;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.CAMPO_SECUENCIA;

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

            System.out.println("id = " + id);
            int secuencia = modelo.getInt(CAMPO_SECUENCIA);
            System.out.println("secuencia = " + secuencia);
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


}
