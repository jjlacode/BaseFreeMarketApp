package com.codevsolution.base.sqlite;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.codevsolution.base.encrypt.EncryptUtil;
import com.codevsolution.base.interfaces.TipoConsultaBD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.time.TimeDateUtil;

import java.util.ArrayList;

import static com.codevsolution.base.android.AppActivity.getAppContext;

public class ConsultaBDBase implements JavaUtil.Constantes {

    private ContentResolver resolver = getAppContext().getContentResolver();
    private TipoConsultaBD tipoConsultaBD;

    public ConsultaBDBase(TipoConsultaBD tipoConsultaBD) {
        this.tipoConsultaBD = tipoConsultaBD;
    }

    public Uri obtenerUriContenido(String tabla) {

        if (ContratoSystem.obtenerCampos(tabla) != null) {


            return ContratoSystem.obtenerUriContenido(
                    tabla);

        }
        return tipoConsultaBD.obtenerUriContenido(tabla);
    }

    public String obtenerIdTabla(Uri uri, String tabla) {

        if (ContratoSystem.obtenerCampos(tabla) != null) {


            return ContratoSystem.obtenerIdTabla(uri);

        }
        return tipoConsultaBD.obtenerIdTabla(uri, tabla);
    }


    public Uri crearUriTabla(String id, String tabla) {

        if (ContratoSystem.obtenerCampos(tabla) != null) {


            return ContratoSystem.crearUriTabla(id,
                    tabla);

        }
        return tipoConsultaBD.crearUriTabla(id, tabla);
    }

    public Uri crearUriTablaDetalle(String id, String secuencia, String tabla) {

        if (ContratoSystem.obtenerCampos(tabla) != null) {


            return ContratoSystem.crearUriTablaDetalle(id, secuencia,
                    tabla);

        }
        return tipoConsultaBD.crearUriTablaDetalle(id, secuencia, tabla);
    }

    public Uri crearUriTablaDetalle(String id, int secuencia, String tabla) {

        if (ContratoSystem.obtenerCampos(tabla) != null) {


            return ContratoSystem.crearUriTablaDetalle(id, secuencia,
                    tabla);

        }
        return tipoConsultaBD.crearUriTablaDetalle(id, secuencia, tabla);
    }

    public Uri crearUriTablaDetalleId(String id, String tabla, String tablaCab) {

        if (ContratoSystem.obtenerCampos(tabla) != null) {


            return ContratoSystem.crearUriTablaDetalleId(id, tabla,
                    tablaCab);

        }
        return tipoConsultaBD.crearUriTablaDetalleId(id, tabla, tablaCab);
    }

    public String obtenerTabCab(String tabla) {

        if (ContratoSystem.obtenerCampos(tabla) != null) {


            return ContratoSystem.getTabCab(tabla);

        }
        return tipoConsultaBD.obtenerTabCab(tabla);
    }

    public String[] obtenerCampos(String tabla) {

        if (ContratoSystem.obtenerCampos(tabla) != null) {


            return ContratoSystem.obtenerCampos(tabla);

        }
        return tipoConsultaBD.obtenerCampos(tabla);
    }

    public ArrayList<String[]> obtenerListaCampos(String tabla) {

        if (ContratoSystem.obtenerCampos(tabla) != null) {


            return ContratoSystem.obtenerListaCampos();

        }
        return tipoConsultaBD.obtenerListaCampos();
    }

    public boolean checkQueryList
            (String[] campos, String campo, String valor) {

        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, campo, valor);
        return (listaModeloSQL.getLista().size() > 0);

    }

    public boolean checkQueryList
            (String[] campos, String campo, int valor) {

        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, campo, valor);
        return (listaModeloSQL.getLista().size() > 0);

    }

    public ArrayList<ModeloSQL> queryList(String[] campos) {


        ArrayList<ModeloSQL> list = new ArrayList<>();

        Cursor reg = resolver.query(obtenerUriContenido(
                campos[1]), null, null, null, null);


        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount() - 1];

                for (int i = 0, x = 2; i < reg.getColumnCount() - 1; i++, x += 3) {

                    insert[i] = EncryptUtil.decodificaStr(reg.getString(reg.getColumnIndex(campos[x])));

                }

                if (insert[0] != null) {
                    ModeloSQL modeloSQL = new ModeloSQL(campos, insert);
                    list.add(modeloSQL);
                }
            }
        }
        reg.close();

        return list;
    }

    public ArrayList<ModeloSQL> queryList(String tabla) {


        ArrayList<ModeloSQL> list = new ArrayList<>();

        Cursor reg = resolver.query(obtenerUriContenido(
                tabla), null, null, null, null);

        String[] campos = obtenerCampos(tabla);

        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount() - 1];

                for (int i = 0, x = 2; i < reg.getColumnCount() - 1; i++, x += 3) {

                    insert[i] = EncryptUtil.decodificaStr(reg.getString(reg.getColumnIndex(campos[x])));

                }

                if (insert[0] != null) {
                    ModeloSQL modeloSQL = new ModeloSQL(campos, insert);
                    list.add(modeloSQL);
                }
            }
        }
        reg.close();

        return list;
    }

    public ArrayList<ModeloSQL> queryListNoDecode(String[] campos) {


        ArrayList<ModeloSQL> list = new ArrayList<>();

        Cursor reg = resolver.query(obtenerUriContenido(
                campos[1]), null, null, null, null);


        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount() - 1];

                for (int i = 0, x = 2; i < reg.getColumnCount() - 1; i++, x += 3) {

                    insert[i] = reg.getString(reg.getColumnIndex(campos[x]));

                }

                if (insert[0] != null) {
                    ModeloSQL modeloSQL = new ModeloSQL(campos, insert);
                    list.add(modeloSQL);
                }
            }
        }
        reg.close();

        return list;
    }


    public ArrayList<ModeloSQL> queryList(String[] campos, boolean ref) {


        ArrayList<ModeloSQL> list = new ArrayList<>();

        Cursor reg = resolver.query(obtenerUriContenido(
                campos[1]), null, null, null, null);

        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount() - 1];

                for (int i = 0, x = 2; i < reg.getColumnCount() - 1; i++, x += 3) {

                    insert[i] = EncryptUtil.decodificaStr(reg.getString(reg.getColumnIndex(campos[x])));

                }

                if (insert[0] != null) {
                    ModeloSQL modeloSQL = new ModeloSQL(campos, insert, ref);
                    list.add(modeloSQL);
                }
            }
        }
        reg.close();

        return list;
    }

    public String queryStrField(String tabla, String id, String campo) {

        Cursor reg = resolver.query(crearUriTabla(id,
                tabla), new String[]{tabla + "." + campo}, null, null, null);
        String res = reg.getString(reg.getColumnIndex(campo));
        reg.close();
        return EncryptUtil.decodificaStr(res);
    }

    public String queryStrEncodeField(String tabla, String id, String campo, String passw) {

        Cursor reg = resolver.query(crearUriTabla(id,
                tabla), new String[]{tabla + "." + campo}, null, null, null);
        String res = reg.getString(reg.getColumnIndex(campo));
        reg.close();

        String decodeStr = null;

        try {
            decodeStr = EncryptUtil.desencriptarStrAES(res, passw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decodeStr;
    }


    public ModeloSQL queryObject(String[] campos, String id) {


        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos);
        for (ModeloSQL modeloSQL : listaModeloSQL.getLista()) {
            if (EncryptUtil.comprobarIsCode(id)) {
                id = EncryptUtil.decodificaStr(id);
            }
            if (modeloSQL.getString(campos[2]) != null && modeloSQL.getString(campos[2]).equals(id)) {
                return modeloSQL;
            }
        }

        return null;
    }

    public ModeloSQL queryObject(String[] campos, Uri uri) {

        ModeloSQL modeloSQL = null;

        Cursor reg = resolver.query(uri, null, null, null, null);

        if (reg != null) {
            reg.moveToFirst();
            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount() - 1];

                for (int i = 0, x = 2; i < reg.getColumnCount() - 1; i++, x += 3) {

                    insert[i] = EncryptUtil.decodificaStr(reg.getString(reg.getColumnIndex(campos[x])));

                }

                if (insert[0] != null) {
                    modeloSQL = new ModeloSQL(campos, insert);
                }
            }
            reg.close();
        }

        return modeloSQL;
    }

    public ArrayList<ModeloSQL> queryListCampo(String[] campos, String campo, String valor) {


        ArrayList<ModeloSQL> list = new ArrayList<>();

        String strValor = valor;
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos);
        for (ModeloSQL modeloSQL : listaModeloSQL.getLista()) {
            if (EncryptUtil.comprobarIsCode(valor)) {
                strValor = EncryptUtil.decodificaStr(valor);
            }
            if (modeloSQL.getString(campo).equals(strValor)) {
                list.add(modeloSQL);
            }
        }

        return list;
    }

    public ArrayList<ModeloSQL> queryListCampo(String[] campos, String campo, String valor, int flag) {

        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos);

        return setFlag(listaModeloSQL, campo, String.valueOf(valor), flag);

    }

    public ArrayList<ModeloSQL> queryListCampo(String[] campos, String campo, int valor, int flag) {


        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos);

        return setFlag(listaModeloSQL, campo, String.valueOf(valor), flag);

    }

    public ArrayList<ModeloSQL> queryListCampo(String[] campos, String campo, long valor, int flag) {


        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos);

        return setFlag(listaModeloSQL, campo, String.valueOf(valor), flag);

    }

    public ArrayList<ModeloSQL> queryListCampo(String[] campos, String campo, double valor, int flag) {


        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos);

        return setFlag(listaModeloSQL, campo, String.valueOf(valor), flag);

    }

    public ArrayList<ModeloSQL> queryListCampo(String[] campos, String campo, float valor, int flag) {


        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos);

        return setFlag(listaModeloSQL, campo, String.valueOf(valor), flag);

    }

    public ArrayList<ModeloSQL> queryListCampo(String[] campos, String campo, short valor, int flag) {

        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos);

        return setFlag(listaModeloSQL, campo, String.valueOf(valor), flag);

    }

    public ArrayList<ModeloSQL> queryListDetalleCampo(String[] campos, String id, String campo, String valor) {


        ArrayList<ModeloSQL> list = new ArrayList<>();

        String strValor = valor;
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, id);
        for (ModeloSQL modeloSQL : listaModeloSQL.getLista()) {
            if (EncryptUtil.comprobarIsCode(valor)) {
                strValor = EncryptUtil.decodificaStr(valor);
            }
            if (modeloSQL.getString(campo).equals(strValor)) {
                list.add(modeloSQL);
            }
        }

        return list;
    }

    public ArrayList<ModeloSQL> queryListCampo(String[] campos, String id, String campo, String valor, int flag) {

        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, id);

        return setFlag(listaModeloSQL, campo, String.valueOf(valor), flag);

    }

    public ArrayList<ModeloSQL> queryListCampo(String[] campos, String id, String campo, int valor, int flag) {


        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, id);

        return setFlag(listaModeloSQL, campo, String.valueOf(valor), flag);

    }

    public ArrayList<ModeloSQL> queryListCampo(String[] campos, String id, String campo, long valor, int flag) {


        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, id);

        return setFlag(listaModeloSQL, campo, String.valueOf(valor), flag);

    }

    public ArrayList<ModeloSQL> queryListCampo(String[] campos, String id, String campo, double valor, int flag) {


        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, id);

        return setFlag(listaModeloSQL, campo, String.valueOf(valor), flag);

    }

    public ArrayList<ModeloSQL> queryListCampo(String[] campos, String id, String campo, float valor, int flag) {


        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, id);

        return setFlag(listaModeloSQL, campo, String.valueOf(valor), flag);

    }

    public ArrayList<ModeloSQL> queryListCampo(String[] campos, String id, String campo, short valor, int flag) {

        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos, id);

        return setFlag(listaModeloSQL, campo, String.valueOf(valor), flag);

    }


    private ArrayList<ModeloSQL> setFlag(ListaModeloSQL listaModeloSQL, String campo, String valor, int flag) {

        ArrayList<ModeloSQL> list = new ArrayList<>();
        switch (flag) {

            case IGUAL:
                for (ModeloSQL modeloSQL : listaModeloSQL.getLista()) {
                    if (EncryptUtil.comprobarIsCode(valor)) {
                        valor = EncryptUtil.decodificaStr(valor);
                    }
                    if (modeloSQL.getString(campo).equals(valor)) {
                        list.add(modeloSQL);
                    }
                }
                break;
            case DIFERENTE:
                for (ModeloSQL modeloSQL : listaModeloSQL.getLista()) {
                    if (EncryptUtil.comprobarIsCode(valor)) {
                        valor = EncryptUtil.decodificaStr(valor);
                    }
                    if (!modeloSQL.getString(campo).equals(valor)) {
                        list.add(modeloSQL);
                    }
                }
                break;
        }

        return list;
    }

    public ArrayList<ModeloSQL> queryListDetalle(String[] campos, String id) {

        ArrayList<ModeloSQL> list = new ArrayList<>();
        if (!EncryptUtil.comprobarIsCode(id)) {
            id = EncryptUtil.codificaStr(id);
        }

        Uri uri = crearUriTablaDetalleId(id,
                campos[1], obtenerTabCab(campos[1]));
        System.out.println("uri = " + uri);
        Cursor reg = resolver.query(uri, null, null, null, null);

        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount() - 1];

                for (int i = 0, x = 2; i < reg.getColumnCount() - 1; i++, x += 3) {

                    insert[i] = EncryptUtil.decodificaStr(reg.getString(reg.getColumnIndex(campos[x])));

                }

                if (insert[0] != null) {
                    ModeloSQL modeloSQL = new ModeloSQL(campos, insert);
                    list.add(modeloSQL);
                }
            }
            reg.close();
        }

        return list;
    }

    public ModeloSQL queryObjectDetalle(String[] campos, String id, String secuencia) {


        String sec = String.valueOf(secuencia);
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos);
        for (ModeloSQL modeloSQL : listaModeloSQL.getLista()) {
            if (EncryptUtil.comprobarIsCode(id)) {
                id = EncryptUtil.codificaStr(id);
            }
            if (EncryptUtil.comprobarIsCode(sec)) {
                sec = EncryptUtil.codificaStr(sec);
            }
            if (modeloSQL.getString(campos[2]) != null && modeloSQL.getString(CAMPO_SECUENCIA) != null &&
                    modeloSQL.getString(campos[2]).equals(id) && modeloSQL.getString(CAMPO_SECUENCIA).equals(sec)) {
                return modeloSQL;
            }
        }

        return null;
    }

    public ModeloSQL queryObjectDetalle(String[] campos, String id, int secuencia) {

        String sec = String.valueOf(secuencia);
        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos);
        for (ModeloSQL modeloSQL : listaModeloSQL.getLista()) {
            if (EncryptUtil.comprobarIsCode(id)) {
                id = EncryptUtil.codificaStr(id);
            }
            if (EncryptUtil.comprobarIsCode(sec)) {
                sec = EncryptUtil.codificaStr(sec);
            }
            if (modeloSQL.getString(campos[2]) != null && modeloSQL.getString(CAMPO_SECUENCIA) != null &&
                    modeloSQL.getString(campos[2]).equals(id) && modeloSQL.getString(CAMPO_SECUENCIA).equals(sec)) {
                return modeloSQL;
            }
        }

        return null;
    }

    public ModeloSQL queryObjectDetalle(String[] campos, Uri uri) {

        ModeloSQL modeloSQL = null;

        Cursor reg = resolver.query(uri, null, null, null, null);

        while (reg.moveToNext()) {

            String[] insert = new String[reg.getColumnCount() - 1];

            for (int i = 0, x = 2; i < reg.getColumnCount() - 1; i++, x += 3) {

                insert[i] = EncryptUtil.decodificaStr(reg.getString(reg.getColumnIndex(campos[x])));

            }

            if (insert[0] != null) {
                modeloSQL = new ModeloSQL(campos, insert);
            }
        }
        reg.close();

        return modeloSQL;
    }

    public ModeloSQL queryObject(String[] campos, String campo, String valor) {

        return queryObjectValor(campos, campo, String.valueOf(valor));

    }

    public ModeloSQL queryObject(String[] campos, String campo, int valor) {

        return queryObjectValor(campos, campo, String.valueOf(valor));

    }

    public ModeloSQL queryObject(String[] campos, String campo, double valor) {

        return queryObjectValor(campos, campo, String.valueOf(valor));

    }

    public ModeloSQL queryObject
            (String[] campos, String campo, long valor) {

        return queryObjectValor(campos, campo, String.valueOf(valor));

    }

    public ModeloSQL queryObject
            (String[] campos, String campo, float valor) {

        return queryObjectValor(campos, campo, String.valueOf(valor));

    }

    public ModeloSQL queryObject
            (String[] campos, String campo, short valor) {

        return queryObjectValor(campos, campo, String.valueOf(valor));
    }

    public ModeloSQL queryObjectValor(String[] campos, String campo, String strValor) {

        ListaModeloSQL listaModeloSQL = new ListaModeloSQL(campos);
        System.out.println("listaModeloSQL = " + listaModeloSQL.sizeLista());
        for (ModeloSQL modeloSQL : listaModeloSQL.getLista()) {
            if (EncryptUtil.comprobarIsCode(strValor)) {
                strValor = EncryptUtil.decodificaStr(strValor);
            }
            if (modeloSQL.getString(campo).equals(strValor)) {
                return modeloSQL;
            }
        }

        return null;
    }


    public ArrayList<ModeloSQL> queryListCampoIgual
            (String[] campos, String campo, String valor) {

        ArrayList<ModeloSQL> listFinal = new ArrayList<>();
        ArrayList<ModeloSQL> list = queryList(campos);

        for (ModeloSQL modeloSQL : list) {

            if (modeloSQL.getString(campo).equals(valor)) {
                listFinal.add(modeloSQL);
            }
        }

        return listFinal;
    }

    public ArrayList<ModeloSQL> queryListCampoDiferente
            (String[] campos, String campo, String valor) {

        ArrayList<ModeloSQL> listFinal = new ArrayList<>();
        ArrayList<ModeloSQL> list = queryList(campos);

        for (ModeloSQL modeloSQL : list) {


            if (!modeloSQL.getString(campo).equals(valor)) {
                listFinal.add(modeloSQL);
            }

        }

        return listFinal;
    }

    public ArrayList<ModeloSQL> queryList(String[] campos, String campo, int valor, int valor2, int flag) {


        ArrayList<ModeloSQL> listFinal = new ArrayList<>();
        ArrayList<ModeloSQL> list = queryList(campos);

        for (ModeloSQL modeloSQL : list) {

            switch (flag) {

                case IGUAL:
                    if (modeloSQL.getInt(campo) == valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case DIFERENTE:
                    if (modeloSQL.getInt(campo) != valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case ENTRE:
                    if (modeloSQL.getInt(campo) > valor &&
                            modeloSQL.getInt(campo) < valor2) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MAYOR:
                    if (modeloSQL.getInt(campo) > valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MAYORIGUAL:
                    if (modeloSQL.getInt(campo) >= valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MENOR:
                    if (modeloSQL.getInt(campo) < valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MENORIGUAL:
                    if (modeloSQL.getInt(campo) <= valor) {
                        listFinal.add(modeloSQL);
                    }

            }
        }

        return listFinal;
    }

    public ArrayList<ModeloSQL> queryList(String[] campos, String campo, double valor, double valor2, int flag) {


        ArrayList<ModeloSQL> listFinal = new ArrayList<>();
        ArrayList<ModeloSQL> list = queryList(campos);

        for (ModeloSQL modeloSQL : list) {

            switch (flag) {

                case IGUAL:
                    if (modeloSQL.getDouble(campo) == valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case DIFERENTE:
                    if (modeloSQL.getDouble(campo) != valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case ENTRE:
                    if (modeloSQL.getDouble(campo) > valor &&
                            modeloSQL.getDouble(campo) < valor2) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MAYOR:
                    if (modeloSQL.getDouble(campo) > valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MAYORIGUAL:
                    if (modeloSQL.getDouble(campo) >= valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MENOR:
                    if (modeloSQL.getDouble(campo) < valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MENORIGUAL:
                    if (modeloSQL.getDouble(campo) <= valor) {
                        listFinal.add(modeloSQL);
                    }

            }
        }

        return listFinal;
    }


    public ArrayList<ModeloSQL> queryList(String[] campos, String campo, long valor, long valor2, int flag) {


        ArrayList<ModeloSQL> listFinal = new ArrayList<>();
        ArrayList<ModeloSQL> list = queryList(campos);

        for (ModeloSQL modeloSQL : list) {

            switch (flag) {

                case IGUAL:
                    if (modeloSQL.getLong(campo) == valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case DIFERENTE:
                    if (modeloSQL.getLong(campo) != valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case ENTRE:
                    if (modeloSQL.getLong(campo) > valor &&
                            modeloSQL.getLong(campo) < valor2) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MAYOR:
                    if (modeloSQL.getLong(campo) > valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MAYORIGUAL:
                    if (modeloSQL.getLong(campo) >= valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MENOR:
                    if (modeloSQL.getLong(campo) < valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MENORIGUAL:
                    if (modeloSQL.getLong(campo) <= valor) {
                        listFinal.add(modeloSQL);
                    }

            }
        }

        return listFinal;
    }

    public ArrayList<ModeloSQL> queryList(String[] campos, String campo, float valor, float valor2, int flag) {


        ArrayList<ModeloSQL> listFinal = new ArrayList<>();
        ArrayList<ModeloSQL> list = queryList(campos);

        for (ModeloSQL modeloSQL : list) {

            switch (flag) {

                case IGUAL:
                    if (modeloSQL.getFloat(campo) == valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case DIFERENTE:
                    if (modeloSQL.getFloat(campo) != valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case ENTRE:
                    if (modeloSQL.getFloat(campo) > valor &&
                            modeloSQL.getFloat(campo) < valor2) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MAYOR:
                    if (modeloSQL.getFloat(campo) > valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MAYORIGUAL:
                    if (modeloSQL.getFloat(campo) >= valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MENOR:
                    if (modeloSQL.getFloat(campo) < valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MENORIGUAL:
                    if (modeloSQL.getFloat(campo) <= valor) {
                        listFinal.add(modeloSQL);
                    }

            }
        }

        return listFinal;
    }

    public ArrayList<ModeloSQL> queryList(String[] campos, String campo, short valor, short valor2, int flag) {


        ArrayList<ModeloSQL> listFinal = new ArrayList<>();
        ArrayList<ModeloSQL> list = queryList(campos);

        for (ModeloSQL modeloSQL : list) {

            switch (flag) {

                case IGUAL:
                    if (modeloSQL.getShort(campo) == valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case DIFERENTE:
                    if (modeloSQL.getShort(campo) != valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case ENTRE:
                    if (modeloSQL.getShort(campo) > valor &&
                            modeloSQL.getShort(campo) < valor2) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MAYOR:
                    if (modeloSQL.getShort(campo) > valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MAYORIGUAL:
                    if (modeloSQL.getShort(campo) >= valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MENOR:
                    if (modeloSQL.getShort(campo) < valor) {
                        listFinal.add(modeloSQL);
                    }
                    break;
                case MENORIGUAL:
                    if (modeloSQL.getShort(campo) <= valor) {
                        listFinal.add(modeloSQL);
                    }

            }
        }

        return listFinal;
    }

    public ArrayList<ModeloSQL> queryList
            (String[] campos, String seleccion, String campoOrden, int flagOrden) {


        ArrayList<ModeloSQL> list = new ArrayList<>();

        String orden = null;

        switch (flagOrden) {

            case ASCENDENTE:
                orden = campoOrden + " ASC";
                break;
            case DESCENDENTE:
                orden = campoOrden + " DESC";

        }

        Cursor reg = resolver.query(obtenerUriContenido(
                campos[1]), null, seleccion, null, orden);


        if (reg != null) {

            while (reg.moveToNext()) {

                String[] insert = new String[reg.getColumnCount() - 1];

                for (int i = 0, x = 2, y = 4; i < reg.getColumnCount() - 1; i++, x += 3, y += 3) {

                    switch (campos[y]) {

                        case STRING:
                            insert[i] = EncryptUtil.decodificaStr(reg.getString(reg.getColumnIndex(campos[x])));
                            break;
                        case INT:
                            insert[i] = String.valueOf(reg.getInt(reg.getColumnIndex(campos[x])));
                            break;
                        case LONG:
                            insert[i] = String.valueOf(reg.getLong(reg.getColumnIndex(campos[x])));
                            break;
                        case DOUBLE:
                            insert[i] = String.valueOf(reg.getDouble(reg.getColumnIndex(campos[x])));
                            break;
                        case FLOAT:
                            insert[i] = String.valueOf(reg.getFloat(reg.getColumnIndex(campos[x])));
                            break;
                        case SHORT:
                            insert[i] = String.valueOf(reg.getShort(reg.getColumnIndex(campos[x])));
                            break;
                        default:

                            insert[i] = EncryptUtil.decodificaStr(reg.getString(reg.getColumnIndex(campos[x])));

                    }

                }

                if (insert[0] != null) {
                    ModeloSQL modeloSQL = new ModeloSQL(campos, insert);
                    list.add(modeloSQL);
                }
            }
        }
        reg.close();

        return list;
    }

    public void putDatoEncodeStr(ContentValues valores, String campo, String valor) {


        valores.put(campo, EncryptUtil.codificaStr(valor));

    }

    public void putDatoEncodeStr(ContentValues valores, String campo, String valor, String passw) {

        try {
            valores.put(campo, EncryptUtil.encriptarStrAES(valor, passw));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void putDato(ContentValues valores, String campo, String valor) {

        putDatoEncodeStr(valores, campo, valor);

    }

    public void putDato(ContentValues valores, String campo, int valor) {

        putDatoEncodeStr(valores, campo, String.valueOf(valor));

    }

    public void putDato(ContentValues valores, String campo, long valor) {

        putDatoEncodeStr(valores, campo, String.valueOf(valor));
    }

    public void putDato(ContentValues valores, String campo, double valor) {

        putDatoEncodeStr(valores, campo, String.valueOf(valor));
    }

    public void putDato(ContentValues valores, String campo, float valor) {

        putDatoEncodeStr(valores, campo, String.valueOf(valor));
    }

    public void putDato(ContentValues valores, String campo, short valor) {

        putDatoEncodeStr(valores, campo, String.valueOf(valor));
    }

    public void putDato(ContentValues valores, String campo, String valor, boolean code) {

        putDatoEncodeStr(valores, campo, valor);
    }

    public void putDato(ContentValues valores, String campo, int valor, boolean code) {

        putDatoEncodeStr(valores, campo, String.valueOf(valor));
    }

    public void putDato(ContentValues valores, String campo, long valor, boolean code) {

        putDatoEncodeStr(valores, campo, String.valueOf(valor));
    }

    public void putDato(ContentValues valores, String campo, double valor, boolean code) {

        putDatoEncodeStr(valores, campo, String.valueOf(valor));
    }

    public void putDato(ContentValues valores, String campo, float valor, boolean code) {

        putDatoEncodeStr(valores, campo, String.valueOf(valor));
    }

    public void putDato(ContentValues valores, String campo, short valor, boolean code) {

        putDatoEncodeStr(valores, campo, String.valueOf(valor));
    }

    public int updateRegistro(String tabla, String id, ContentValues valores) {

        putDato(valores, CAMPO_TIMESTAMP, TimeDateUtil.ahora());

        Uri uri = crearUriTabla(EncryptUtil.codificaStr(id), tabla);
        System.out.println("uri = " + uri);

        return resolver.update(uri
                , valores, null, null);

    }

    public int updateRegistro(Uri uri, ContentValues valores) {

        putDato(valores, CAMPO_TIMESTAMP, TimeDateUtil.ahora());

        return resolver.update(uri, valores, null, null);

    }

    public int updateRegistroDetalle(String tabla, String id, String secuencia, ContentValues valores) {

        putDato(valores, CAMPO_TIMESTAMP, TimeDateUtil.ahora());

        Uri uri = crearUriTablaDetalle(EncryptUtil.codificaStr(id), EncryptUtil.codificaStr(secuencia), tabla);

        System.out.println("uri = " + uri);

        return resolver.update(uri
                , valores, null, null);

    }

    public int updateRegistroDetalle(String tabla, String id, int secuencia, ContentValues valores) {

        putDato(valores, CAMPO_TIMESTAMP, TimeDateUtil.ahora());

        Uri uri = crearUriTablaDetalle(EncryptUtil.codificaStr(id), EncryptUtil.codificaStr(String.valueOf(secuencia)), tabla);

        System.out.println("uri = " + uri);

        return resolver.update(uri
                , valores, null, null);

    }

    public int updateRegistros(String tabla, ContentValues valores, String campo, String valor) {

        putDato(valores, CAMPO_TIMESTAMP, TimeDateUtil.ahora());

        String seleccion = campo + " = '" + EncryptUtil.codificaStr(valor) + "'";

        return resolver.update(obtenerUriContenido(tabla)
                , valores, seleccion, null);

    }

    public int updateRegistros
            (String tabla, ContentValues valores, String campo, int valor, int valor2, int flag) {

        putDato(valores, CAMPO_TIMESTAMP, TimeDateUtil.ahora());

        String seleccion = null;

        switch (flag) {

            case IGUAL:
                seleccion = campo + " = '" + valor + "'";
                break;
            case DIFERENTE:
                seleccion = campo + " <> '" + valor + "'";
                break;
            case ENTRE:
                seleccion = campo + " BETWEEN '" + valor + "' AND '" + valor2 + "'";
                break;
            case MAYOR:
                seleccion = campo + " > '" + valor + "'";
                break;
            case MAYORIGUAL:
                seleccion = campo + " >= '" + valor + "'";
                break;
            case MENOR:
                seleccion = campo + " < '" + valor + "'";
                break;
            case MENORIGUAL:
                seleccion = campo + " <= '" + valor + "'";

        }
        return resolver.update(obtenerUriContenido(tabla)
                , valores, seleccion, null);

    }

    public int updateRegistros
            (String tabla, ContentValues valores, String campo, long valor, long valor2, int flag) {

        putDato(valores, CAMPO_TIMESTAMP, TimeDateUtil.ahora());

        String seleccion = null;

        switch (flag) {

            case IGUAL:
                seleccion = campo + " = '" + valor + "'";
                break;
            case DIFERENTE:
                seleccion = campo + " <> '" + valor + "'";
                break;
            case ENTRE:
                seleccion = campo + " BETWEEN '" + valor + "' AND '" + valor2 + "'";
                break;
            case MAYOR:
                seleccion = campo + " > '" + valor + "'";
                break;
            case MAYORIGUAL:
                seleccion = campo + " >= '" + valor + "'";
                break;
            case MENOR:
                seleccion = campo + " < '" + valor + "'";
                break;
            case MENORIGUAL:
                seleccion = campo + " <= '" + valor + "'";

        }
        return resolver.update(obtenerUriContenido(tabla)
                , valores, seleccion, null);

    }

    public int updateRegistros
            (String tabla, ContentValues valores, String campo, double valor, double valor2, int flag) {

        putDato(valores, CAMPO_TIMESTAMP, TimeDateUtil.ahora());

        String seleccion = null;

        switch (flag) {

            case IGUAL:
                seleccion = campo + " = '" + valor + "'";
                break;
            case DIFERENTE:
                seleccion = campo + " <> '" + valor + "'";
                break;
            case ENTRE:
                seleccion = campo + " BETWEEN '" + valor + "' AND '" + valor2 + "'";
                break;
            case MAYOR:
                seleccion = campo + " > '" + valor + "'";
                break;
            case MAYORIGUAL:
                seleccion = campo + " >= '" + valor + "'";
                break;
            case MENOR:
                seleccion = campo + " < '" + valor + "'";
                break;
            case MENORIGUAL:
                seleccion = campo + " <= '" + valor + "'";

        }
        return resolver.update(obtenerUriContenido(tabla)
                , valores, seleccion, null);

    }

    public int updateRegistros
            (String tabla, ContentValues valores, String campo, float valor, float valor2, int flag) {

        putDato(valores, CAMPO_TIMESTAMP, TimeDateUtil.ahora());

        String seleccion = null;

        switch (flag) {

            case IGUAL:
                seleccion = campo + " = '" + valor + "'";
                break;
            case DIFERENTE:
                seleccion = campo + " <> '" + valor + "'";
                break;
            case ENTRE:
                seleccion = campo + " BETWEEN '" + valor + "' AND '" + valor2 + "'";
                break;
            case MAYOR:
                seleccion = campo + " > '" + valor + "'";
                break;
            case MAYORIGUAL:
                seleccion = campo + " >= '" + valor + "'";
                break;
            case MENOR:
                seleccion = campo + " < '" + valor + "'";
                break;
            case MENORIGUAL:
                seleccion = campo + " <= '" + valor + "'";

        }
        return resolver.update(obtenerUriContenido(tabla)
                , valores, seleccion, null);

    }

    public int updateRegistros
            (String tabla, ContentValues valores, String campo, short valor, short valor2, int flag) {

        putDato(valores, CAMPO_TIMESTAMP, TimeDateUtil.ahora());

        String seleccion = null;

        switch (flag) {

            case IGUAL:
                seleccion = campo + " = '" + valor + "'";
                break;
            case DIFERENTE:
                seleccion = campo + " <> '" + valor + "'";
                break;
            case ENTRE:
                seleccion = campo + " BETWEEN '" + valor + "' AND '" + valor2 + "'";
                break;
            case MAYOR:
                seleccion = campo + " > '" + valor + "'";
                break;
            case MAYORIGUAL:
                seleccion = campo + " >= '" + valor + "'";
                break;
            case MENOR:
                seleccion = campo + " < '" + valor + "'";
                break;
            case MENORIGUAL:
                seleccion = campo + " <= '" + valor + "'";

        }
        return resolver.update(obtenerUriContenido(tabla)
                , valores, seleccion, null);

    }

    public int deleteRegistro(String tabla, String id) {

        int res = resolver.delete(crearUriTabla(id, tabla)
                , null, null);
        if (res > 0) {
            return res;
        } else {
            id = EncryptUtil.codificaStr(id);
        }
        return resolver.delete(crearUriTabla(id, tabla)
                , null, null);

    }

    public int deleteRegistros(String tabla, String seleccion) {

        return resolver.delete(obtenerUriContenido(tabla)
                , seleccion, null);

    }

    public int deleteRegistrosDetalle(String tabla, String id) {

        return resolver.delete(crearUriTabla(id, tabla)
                , null, null);

    }

    public int deleteRegistroDetalle(String tabla, String id, int secuencia) {

        return resolver.delete(crearUriTablaDetalle(id, secuencia, tabla)
                , null, null);

    }

    public int deleteRegistroDetalle(String tabla, String id, String secuencia) {

        return resolver.delete(crearUriTablaDetalle(id, secuencia, tabla)
                , null, null);

    }

    public int deleteRegistros(String tabla, String campo, String valor) {

        String seleccion = campo + " = '" + EncryptUtil.codificaStr(valor) + "'";

        return resolver.delete(obtenerUriContenido(tabla)
                , seleccion, null);

    }

    public Uri insertRegistro(String tabla, ContentValues valores) {

        putDato(valores, CAMPO_TIMESTAMP, TimeDateUtil.ahora());
        putDato(valores, CAMPO_CREATEREG, TimeDateUtil.ahora());

        System.out.println("Uri contenido = " + obtenerUriContenido(tabla));
        return resolver.insert(obtenerUriContenido(tabla), valores);

    }

    public String idInsertRegistro(String tabla, ContentValues valores) {

        putDato(valores, CAMPO_TIMESTAMP, TimeDateUtil.ahora());
        putDato(valores, CAMPO_CREATEREG, TimeDateUtil.ahora());
        Uri uri = null;
        try {

            System.out.println("valores = " + valores);
            uri = resolver.insert(obtenerUriContenido(tabla), valores);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (uri != null) {
            return obtenerIdTabla(uri, tabla);
        }
        return ERROR;

    }

    public Uri insertRegistroDetalle(String[] campos, String id, ContentValues valores) {

        putDato(valores, CAMPO_TIMESTAMP, JavaUtil.hoy());
        putDato(valores, CAMPO_CREATEREG, JavaUtil.hoy());

        ArrayList<ModeloSQL> lista = queryListDetalle(campos, id);

        int secuencia = 0;

        if (lista != null && lista.size() > 0) {
            secuencia = lista.size() + 1;
        } else {
            secuencia = 1;
        }

        putDato(valores, "secuencia", secuencia);

        return resolver.insert(crearUriTablaDetalle(id, secuencia, campos[1]), valores);

    }

    public int secInsertRegistroDetalle(String[] campos, String id, ContentValues valores) {

        putDato(valores, CAMPO_TIMESTAMP, JavaUtil.hoy());
        putDato(valores, CAMPO_CREATEREG, JavaUtil.hoy());

        ArrayList<ModeloSQL> lista = queryListDetalle(campos, id);

        int secuencia = 0;

        if (lista != null && lista.size() > 0) {
            secuencia = lista.size() + 1;
        } else {
            secuencia = 1;
        }

        putDato(valores, CAMPO_SECUENCIA, secuencia);

        Uri uri = resolver.insert(crearUriTablaDetalle(id, secuencia, campos[1]), valores);

        if (uri == null) {
            return 0;
        }

        return secuencia;

    }


}