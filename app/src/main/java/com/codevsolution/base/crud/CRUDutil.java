package com.codevsolution.base.crud;

import android.content.ContentValues;
import android.net.Uri;

import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ContratoPry;

import java.util.ArrayList;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.CAMPO_SECUENCIA;

public class CRUDutil {

    public static ListaModeloSQL setListaModelo(String[] campos) {
        return new ListaModeloSQL(campos);
    }


    public static ListaModeloSQL clonaListaModelo(String[] campos, ListaModeloSQL list) {
        ListaModeloSQL lista = new ListaModeloSQL(campos);
        lista.clearAddAllLista(list.getLista());
        return lista;
    }

    public static ListaModeloSQL clonaListaModelo(String[] campos, ArrayList<ModeloSQL> list) {
        ListaModeloSQL lista = new ListaModeloSQL(campos);
        lista.clearAddAllLista(list);
        return lista;
    }

    public static ListaModeloSQL setListaModeloDetalle(String[] campos, String id, String tablaCab) {
        return new ListaModeloSQL(campos, id, tablaCab, null, null);
    }


    public static ListaModeloSQL setListaModelo(String[] campos, String seleccion) {
        return new ListaModeloSQL(campos, seleccion, null);
    }

    public static ListaModeloSQL setListaModeloDetalle(String[] campos, String id, String tablaCab, String seleccion) {
        return new ListaModeloSQL(campos, id, tablaCab, seleccion, null);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String seleccion, String orden) {
        return new ListaModeloSQL(campos, seleccion, orden);
    }

    public static ListaModeloSQL setListaModeloDetalle(String[] campos, String id, String tablaCab,
                                                       String seleccion, String orden) {
        return new ListaModeloSQL(campos, id, tablaCab, seleccion, orden);
    }


    public static ListaModeloSQL setListaModelo(String[] campos, String campo, String valor, int flag) {
        return new ListaModeloSQL(campos, campo, valor, null, flag, null);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, String valor, int flag, String orden) {
        return new ListaModeloSQL(campos, campo, valor, null, flag, orden);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, String valor, String valor2, int flag) {
        return new ListaModeloSQL(campos, campo, valor, valor2, flag, null);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, String valor, String valor2, int flag, String orden) {
        return new ListaModeloSQL(campos, campo, valor, valor2, flag, orden);
    }

    public static ModeloSQL updateModelo(ModeloSQL modeloSQL) {

        if (modeloSQL.getInt(CAMPO_SECUENCIA) > 0) {
            return ConsultaBD.queryObjectDetalle(modeloSQL.getEstructura(), modeloSQL.getString(modeloSQL.getCampoID()), modeloSQL.getInt(CAMPO_SECUENCIA));
        }
        return ConsultaBD.queryObject(modeloSQL.getEstructura(), modeloSQL.getString(modeloSQL.getCampoID()));
    }

    public static ModeloSQL updateModelo(String[] campos, String id) {

        return ConsultaBD.queryObject(campos, id);
    }

    public static ModeloSQL updateModelo(String[] campos, String campo, String valor, String valor2, int flag, String orden) {

        return ConsultaBD.queryObject(campos, campo,valor,valor2,flag,orden);
    }

    public static ModeloSQL updateModelo(String[] campos, String campo, int valor, int valor2, int flag, String orden) {

        return ConsultaBD.queryObject(campos, campo,valor,valor2,flag,orden);
    }

    public static ModeloSQL updateModelo(String[] campos, String campo, long valor, long valor2, int flag, String orden) {

        return ConsultaBD.queryObject(campos, campo,valor,valor2,flag,orden);
    }

    public static ModeloSQL updateModelo(String[] campos, String campo, double valor, double valor2, int flag, String orden) {

        return ConsultaBD.queryObject(campos, campo,valor,valor2,flag,orden);
    }

    public static ModeloSQL updateModelo(String[] campos, String campo, float valor, float valor2, int flag, String orden) {

        return ConsultaBD.queryObject(campos, campo,valor,valor2,flag,orden);
    }

    public static ModeloSQL updateModelo(String[] campos, String campo, short valor, short valor2, int flag, String orden) {

        return ConsultaBD.queryObject(campos, campo,valor,valor2,flag,orden);
    }

    public static ModeloSQL updateModelo(String[] campos, String id, int secuencia) {

        return ConsultaBD.queryObjectDetalle(campos, id, secuencia);
    }

    public static ModeloSQL updateModelo(String[] campos, String id, String secuencia) {
        return ConsultaBD.queryObjectDetalle(campos, id, secuencia);
    }

    public static int actualizarRegistro(String tabla, String id, ContentValues valores) {

        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarRegistro(String tabla, String id, int secuencia, ContentValues valores) {

        return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);
    }

    public static int actualizarRegistro(ModeloSQL modeloSQL, ContentValues valores) {

        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());

        if (ContratoPry.getTabCab(tabla) != null) {

            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampoNull(ModeloSQL modeloSQL, String campo) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        valores.putNull(campo);

        if (ContratoPry.getTabCab(tabla) != null) {

            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, String valor) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        if (valor == null) {
            valores.putNull(campo);
        } else {
            valores.put(campo, valor);
        }

        if (ContratoPry.getTabCab(tabla) != null) {

            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, int valor) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        valores.put(campo, valor);

        if (ContratoPry.getTabCab(tabla) != null) {

            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, long valor) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        valores.put(campo, valor);

        if (ContratoPry.getTabCab(tabla) != null) {

            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, float valor) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        valores.put(campo, valor);

        if (ContratoPry.getTabCab(tabla) != null) {

            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, double valor) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        valores.put(campo, valor);

        if (ContratoPry.getTabCab(tabla) != null) {

            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, short valor) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        valores.put(campo, valor);

        if (ContratoPry.getTabCab(tabla) != null) {

            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
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

    public static String getID(ModeloSQL modeloSQL, String campoID) {

        return modeloSQL.getString(campoID);
    }

    public static int getSecuencia(ModeloSQL modeloSQL) {
        return modeloSQL.getInt("secuencia");
    }


}
