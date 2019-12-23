package com.codevsolution.base.crud;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.MainActivityBase;
import com.codevsolution.base.android.controls.ViewImagenLayout;
import com.codevsolution.base.interfaces.ICFragmentos;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.style.Estilos;

import java.util.ArrayList;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.CAMPO_SECUENCIA;

public class CRUDutil {

    private int sizeTextD;
    private float sizeText;
    private int densidadDpi;
    private int alto;
    private int ancho;
    private int altoReal;
    private int anchoReal;
    private float densidad;
    private DisplayMetrics metrics;
    private ViewImagenLayout imagen;
    private boolean nuevo;
    private String[] campos;
    private String tabCab;
    private String campoId;
    private String tabla;
    private String id;
    private int secuencia;
    private String secuenciaStr;
    private ModeloSQL modeloSQL;
    private ContentValues valores;
    private Bundle bundle;
    private String TAG;
    private String campoImagen;
    private String path;
    private MainActivityBase activityBase;
    private ICFragmentos icFragmentos;
    private FragmentBaseCRUD fragmentBaseCRUD;

    public CRUDutil(String tabla) {

        campos = ConsultaBD.obtenerCampos(tabla);
        tabCab = ConsultaBD.obtenerTabCab(tabla);
        campoId = campos[2];
    }

    public CRUDutil(FragmentBaseCRUD fragmentBaseCRUD) {

        this.fragmentBaseCRUD = fragmentBaseCRUD;
        tabla = fragmentBaseCRUD.getTabla();
        campos = ConsultaBD.obtenerCampos(tabla);
        tabCab = ConsultaBD.obtenerTabCab(tabla);
        campoId = campos[2];
        TAG = fragmentBaseCRUD.getTAG();
        activityBase = fragmentBaseCRUD.getActivityBase();
        icFragmentos = fragmentBaseCRUD.getIcFragmentos();
        setParamsFragment();
        setMetrics();
        fragmentBaseCRUD.setCallbackDatos(new FragmentBase.CallbackDatos() {
            @Override
            public void onAfterSetDatos() {
                System.out.println("onAfterSetDatos");

            }

            @Override
            public void onBeforeSetdatos() {

                System.out.println("onBeforeSetDatos");
                setParamsFragment();
            }
        });
    }

    private void setParamsFragment() {

        id = fragmentBaseCRUD.getIdCrud();
        secuencia = fragmentBaseCRUD.getSecuencia();
        secuenciaStr = String.valueOf(secuencia);
        modeloSQL = fragmentBaseCRUD.getModeloSQL();
        if (modeloSQL == null && nnn(id)) {

            if (secuencia > 0) {
                modeloSQL = updateModelo(campos, id, secuencia);
            } else {
                modeloSQL = updateModelo(campos, id);
            }
        }
        valores = fragmentBaseCRUD.getValores();
        bundle = fragmentBaseCRUD.getBundle();
        campoImagen = fragmentBaseCRUD.getCampoImagen();
        path = fragmentBaseCRUD.getPath();
        nuevo = fragmentBaseCRUD.getNuevo();
        imagen = fragmentBaseCRUD.getImagen();
    }

    private void setMetrics() {

        metrics = new DisplayMetrics();
        activityBase.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        densidad = metrics.density;
        anchoReal = metrics.widthPixels;
        altoReal = metrics.heightPixels;
        ancho = (int) (metrics.widthPixels / densidad);
        alto = (int) (metrics.heightPixels / densidad);
        densidadDpi = metrics.densityDpi;

        sizeText = ((float) (ancho + alto + densidadDpi) / (100));
        System.out.println("sizeText = " + sizeText);
        sizeTextD = (int) (sizeText * densidad);
    }

    public String getCampoId() {
        return campoId;
    }

    public String getTabCab() {
        return tabCab;
    }

    public static ListaModeloSQL setListaModelo(String[] campos) {
        return new ListaModeloSQL(campos);
    }

    public ListaModeloSQL setListaModelo() {
        return new ListaModeloSQL(campos);
    }

    public static ListaModeloSQL clonaListaModelo(String[] campos, ListaModeloSQL list) {
        ListaModeloSQL lista = new ListaModeloSQL(campos);
        lista.clearAddAllLista(list.getLista());
        return lista;
    }

    public ListaModeloSQL clonaListaModelo(ListaModeloSQL list) {
        ListaModeloSQL lista = new ListaModeloSQL(campos);
        lista.clearAddAllLista(list.getLista());
        return lista;
    }

    public static ListaModeloSQL clonaListaModelo(String[] campos, ArrayList<ModeloSQL> list) {
        ListaModeloSQL lista = new ListaModeloSQL(campos);
        lista.clearAddAllLista(list);
        return lista;
    }

    public ListaModeloSQL clonaListaModelo(ArrayList<ModeloSQL> list) {
        ListaModeloSQL lista = new ListaModeloSQL(campos);
        lista.clearAddAllLista(list);
        return lista;
    }

    public static ListaModeloSQL setListaModeloDetalle(String[] campos, String id) {
        return new ListaModeloSQL(campos, id);
    }

    public ListaModeloSQL setListaModeloDetalle() {
        return new ListaModeloSQL(campos, id);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, String valor) {
        return new ListaModeloSQL(campos, campo, valor);
    }

    public ListaModeloSQL setListaModelo(String campo, String valor) {
        return new ListaModeloSQL(campos, campo, valor);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, String valor, int flag) {
        return new ListaModeloSQL(campos, campo, valor, flag);
    }

    public ListaModeloSQL setListaModelo(String campo, String valor, int flag) {
        return new ListaModeloSQL(campos, campo, valor, flag);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, int valor, int valor2, int flag) {
        return new ListaModeloSQL(campos, campo, valor, valor2, flag);
    }

    public ListaModeloSQL setListaModelo(String campo, int valor, int valor2, int flag) {
        return new ListaModeloSQL(campos, campo, valor, valor2, flag);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, long valor, long valor2, int flag) {
        return new ListaModeloSQL(campos, campo, valor, valor2, flag);
    }

    public ListaModeloSQL setListaModelo(String campo, long valor, long valor2, int flag) {
        return new ListaModeloSQL(campos, campo, valor, valor2, flag);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, double valor, double valor2, int flag) {
        return new ListaModeloSQL(campos, campo, valor, valor2, flag);
    }

    public ListaModeloSQL setListaModelo(String campo, double valor, double valor2, int flag) {
        return new ListaModeloSQL(campos, campo, valor, valor2, flag);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, float valor, float valor2, int flag) {
        return new ListaModeloSQL(campos, campo, valor, valor2, flag);
    }

    public ListaModeloSQL setListaModelo(String campo, float valor, float valor2, int flag) {
        return new ListaModeloSQL(campos, campo, valor, valor2, flag);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, short valor, short valor2, int flag) {
        return new ListaModeloSQL(campos, campo, valor, valor2, flag);
    }

    public ListaModeloSQL setListaModelo(String campo, short valor, short valor2, int flag) {
        return new ListaModeloSQL(campos, campo, valor, valor2, flag);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, String valor, String campoOrden, int flagOrden) {

        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, campo, valor);
        return listaModeloSQL.sort(campoOrden, flagOrden);
    }

    public ListaModeloSQL setListaModelo(String campo, String valor, String campoOrden, int flagOrden) {

        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, campo, valor);
        return listaModeloSQL.sort(campoOrden, flagOrden);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, int valor, int valor2, int flag, String campoOrden, int flagOrden) {
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, campo, valor, valor2, flag);
        return listaModeloSQL.sort(campoOrden, flagOrden);
    }

    public ListaModeloSQL setListaModelo(String campo, int valor, int valor2, int flag, String campoOrden, int flagOrden) {
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, campo, valor, valor2, flag);
        return listaModeloSQL.sort(campoOrden, flagOrden);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, long valor, long valor2, int flag, String campoOrden, int flagOrden) {
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, campo, valor, valor2, flag);
        return listaModeloSQL.sort(campoOrden, flagOrden);
    }

    public ListaModeloSQL setListaModelo(String campo, long valor, long valor2, int flag, String campoOrden, int flagOrden) {
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, campo, valor, valor2, flag);
        return listaModeloSQL.sort(campoOrden, flagOrden);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, double valor, double valor2, int flag, String campoOrden, int flagOrden) {
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, campo, valor, valor2, flag);
        return listaModeloSQL.sort(campoOrden, flagOrden);
    }

    public ListaModeloSQL setListaModelo(String campo, double valor, double valor2, int flag, String campoOrden, int flagOrden) {
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, campo, valor, valor2, flag);
        return listaModeloSQL.sort(campoOrden, flagOrden);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, float valor, float valor2, int flag, String campoOrden, int flagOrden) {
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, campo, valor, valor2, flag);
        return listaModeloSQL.sort(campoOrden, flagOrden);
    }

    public ListaModeloSQL setListaModelo(String campo, float valor, float valor2, int flag, String campoOrden, int flagOrden) {
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, campo, valor, valor2, flag);
        return listaModeloSQL.sort(campoOrden, flagOrden);
    }

    public static ListaModeloSQL setListaModelo(String[] campos, String campo, short valor, short valor2, int flag, String campoOrden, int flagOrden) {
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, campo, valor, valor2, flag);
        return listaModeloSQL.sort(campoOrden, flagOrden);
    }

    public ListaModeloSQL setListaModelo(String campo, short valor, short valor2, int flag, String campoOrden, int flagOrden) {
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, campo, valor, valor2, flag);
        return listaModeloSQL.sort(campoOrden, flagOrden);
    }

    public static ModeloSQL updateModelo(ModeloSQL modeloSQL) {

        if (modeloSQL.getInt(CAMPO_SECUENCIA) > 0) {
            return ConsultaBD.queryObjectDetalle(modeloSQL.getEstructura(), modeloSQL.getString(modeloSQL.getCampoID()), modeloSQL.getInt(CAMPO_SECUENCIA));
        }
        return ConsultaBD.queryObject(modeloSQL.getEstructura(), modeloSQL.getString(modeloSQL.getCampoID()));
    }

    public ModeloSQL updateModelo() {

        if (modeloSQL.getInt(CAMPO_SECUENCIA) > 0) {
            return ConsultaBD.queryObjectDetalle(modeloSQL.getEstructura(), modeloSQL.getString(modeloSQL.getCampoID()), modeloSQL.getInt(CAMPO_SECUENCIA));
        }
        return ConsultaBD.queryObject(modeloSQL.getEstructura(), modeloSQL.getString(modeloSQL.getCampoID()));
    }

    public static ModeloSQL updateModelo(String[] campos, String id) {

        return ConsultaBD.queryObject(campos, id);
    }

    public static ModeloSQL updateModeloCampo(String[] campos, String campo, String valor) {

        return ConsultaBD.queryObject(campos, campo, valor);
    }

    public ModeloSQL updateModeloCampo(String campo, String valor) {

        return ConsultaBD.queryObject(campos, campo, valor);
    }

    public static ModeloSQL updateModeloCampo(String[] campos, String campo, int valor) {

        return ConsultaBD.queryObject(campos, campo, valor);
    }

    public ModeloSQL updateModeloCampo(String campo, int valor) {

        return ConsultaBD.queryObject(campos, campo, valor);
    }

    public static ModeloSQL updateModeloCampo(String[] campos, String campo, long valor) {

        return ConsultaBD.queryObject(campos, campo, valor);
    }

    public ModeloSQL updateModeloCampo(String campo, long valor) {

        return ConsultaBD.queryObject(campos, campo, valor);
    }

    public static ModeloSQL updateModeloCampo(String[] campos, String campo, double valor) {

        return ConsultaBD.queryObject(campos, campo, valor);
    }

    public ModeloSQL updateModeloCampo(String campo, double valor) {

        return ConsultaBD.queryObject(campos, campo, valor);
    }

    public static ModeloSQL updateModeloCampo(String[] campos, String campo, float valor) {

        return ConsultaBD.queryObject(campos, campo, valor);
    }

    public ModeloSQL updateModeloCampo(String campo, float valor) {

        return ConsultaBD.queryObject(campos, campo, valor);
    }

    public static ModeloSQL updateModeloCampo(String[] campos, String campo, short valor) {

        return ConsultaBD.queryObject(campos, campo, valor);
    }

    public ModeloSQL updateModeloCampo(String campo, short valor) {

        return ConsultaBD.queryObject(campos, campo, valor);
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

    public int actualizarRegistro(ContentValues valores) {

        if (secuencia > 0) {
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);
        } else {
            return ConsultaBD.updateRegistro(tabla, id, valores);
        }
    }

    public static int actualizarRegistro(String tabla, String id, int secuencia, ContentValues valores) {

        return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);
    }

    public static int actualizarRegistro(ModeloSQL modeloSQL, ContentValues valores) {

        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
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

        if (ConsultaBD.obtenerTabCab(tabla) != null) {

            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public int actualizarCampoNull(String campo) {

        ContentValues valores = new ContentValues();
        valores.putNull(campo);

        if (secuencia > 0) {
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
            ConsultaBD.putDato(valores, campo, valor);
            System.out.println("valores = " + valores);
        }

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public int actualizarCampo(String campo, String valor) {

        ContentValues valores = new ContentValues();
        if (valor == null) {
            valores.putNull(campo);
        } else {
            ConsultaBD.putDato(valores, campo, valor);
        }

        if (secuencia > 0) {
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampoEncode(ModeloSQL modeloSQL, String campo, String valor) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        if (valor == null) {
            valores.putNull(campo);
        } else {
            ConsultaBD.putDatoEncodeStr(valores, campo, valor);
        }

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public int actualizarCampoEncode(String campo, String valor) {

        ContentValues valores = new ContentValues();
        if (valor == null) {
            valores.putNull(campo);
        } else {
            ConsultaBD.putDatoEncodeStr(valores, campo, valor);
        }

        if (secuencia > 0) {
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, int valor) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        valores.put(campo, valor);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public int actualizarCampo(String campo, int valor) {

        ContentValues valores = new ContentValues();
        valores.put(campo, valor);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);
        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, long valor) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        valores.put(campo, valor);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public int actualizarCampo(String campo, long valor) {

        ContentValues valores = new ContentValues();
        valores.put(campo, valor);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);
        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, float valor) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        valores.put(campo, valor);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public int actualizarCampo(String campo, float valor) {

        ContentValues valores = new ContentValues();
        valores.put(campo, valor);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);
        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, double valor) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        valores.put(campo, valor);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public int actualizarCampo(String campo, double valor) {

        ContentValues valores = new ContentValues();
        valores.put(campo, valor);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);
        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, short valor) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        valores.put(campo, valor);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public int actualizarCampo(String campo, short valor) {

        ContentValues valores = new ContentValues();
        valores.put(campo, valor);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);
        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, String valor, boolean code) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        if (valor == null) {
            valores.putNull(campo);
        } else {
            ConsultaBD.putDato(valores, campo, valor, code);
        }

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, int valor, boolean code) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        ConsultaBD.putDato(valores, campo, valor, code);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, long valor, boolean code) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        ConsultaBD.putDato(valores, campo, valor, code);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, float valor, boolean code) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        ConsultaBD.putDato(valores, campo, valor, code);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, double valor, boolean code) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        ConsultaBD.putDato(valores, campo, valor, code);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
            int secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            return ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);

        }
        return ConsultaBD.updateRegistro(tabla, id, valores);
    }

    public static int actualizarCampo(ModeloSQL modeloSQL, String campo, short valor, boolean code) {

        ContentValues valores = new ContentValues();
        String tabla = modeloSQL.getNombreTabla();
        String id = modeloSQL.getString(modeloSQL.getCampoID());
        ConsultaBD.putDato(valores, campo, valor, code);

        if (ConsultaBD.obtenerTabCab(tabla) != null) {
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

    public Uri crearRegistro(ContentValues valores) {

        if (id != null) {
            return ConsultaBD.insertRegistroDetalle(campos, id, valores);
        }
        return ConsultaBD.insertRegistro(tabla, valores);
    }

    public static Uri crearRegistro(String[] campos, String id, ContentValues valores) {

        return ConsultaBD.insertRegistroDetalle(campos, id, valores);
    }

    public static String crearRegistroId(String tabla, ContentValues valores) {

        return ConsultaBD.idInsertRegistro(tabla, valores);
    }


    public static int crearRegistroSec(String[] campos, String id, ContentValues valores) {

        return ConsultaBD.secInsertRegistroDetalle(campos, id, valores);
    }


    public static int borrarRegistro(String tabla, String id) {

        return ConsultaBD.deleteRegistro(tabla, id);
    }

    public int borrarRegistro() {

        if (secuencia > 0) {
            return ConsultaBD.deleteRegistroDetalle(tabla, id, secuencia);
        }
        return ConsultaBD.deleteRegistro(tabla, id);
    }

    public static int borrarRegistro(String tabla, String id, int secuencia) {

        return ConsultaBD.deleteRegistroDetalle(tabla, id, secuencia);
    }

    public static boolean nn(Object object) {
        return AndroidUtil.nn(object);
    }

    public static boolean nnn(String string) {
        return AndroidUtil.nnn(string);
    }

    public void setImagen(Context context) {
        setImagen(context, imagen);
    }

    public void setImagen(Context context, ViewImagenLayout imagen) {
        Log.d(TAG, AndroidUtil.getMetodo());

        try {

            if (modeloSQL != null && modeloSQL.getString(campoImagen) != null) {

                path = modeloSQL.getString(campoImagen);
                System.out.println("path = " + path);


            }

            if (nnn(path)) {

                System.out.println("path = " + path);
                //imagen.setImageUri(path, (int)(anchoReal*0.25f),(int)(altoReal*0.15f));
                imagen.setImageUriPerfil(activityBase, path);
                imagen.setIcfragmentos(icFragmentos);
                imagen.setVisibleBtn();
                imagen.getLinearLayoutCompat().setFocusable(false);

            } else {

                try {

                    imagen.setImageResource(Estilos.getIdDrawable(context, "logo"), (int) (anchoReal * 0.25f), (int) (altoReal * 0.15f));
                    imagen.setGoneBtn();
                    imagen.getLinearLayoutCompat().setFocusable(false);

                } catch (Exception er) {
                    er.printStackTrace();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            if (path != null) {

                //imagen.setImageUri(path, (int)(anchoReal*0.25f),(int)(altoReal*0.15f));
                imagen.setImageUriPerfil(activityBase, path);
                imagen.setIcfragmentos(icFragmentos);
                imagen.setVisibleBtn();
                imagen.getLinearLayoutCompat().setFocusable(false);


            } else {

                try {

                    imagen.setImageResource(Estilos.getIdDrawable(context, "logo"), (int) (anchoReal * 0.25f), (int) (altoReal * 0.15f));
                    imagen.setGoneBtn();
                    imagen.getLinearLayoutCompat().setFocusable(false);


                } catch (Exception er) {
                    er.printStackTrace();
                }
            }
        }
    }


}
