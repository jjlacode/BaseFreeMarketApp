package com.codevsolution.base.models;

import android.content.ContentValues;
import android.net.Uri;

import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ContratoPry;

import java.io.Serializable;
import java.util.Arrays;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.CAMPO_SECUENCIA;

public class ModeloSQL implements Serializable {

    private String[] campos;

    private String[] estructura;

    private String[] valores;

    private String nombreTabla;

    private int numcampos;

    private int campostabla;

    private int posicionLista;

    private int indiceLista;

    private boolean enLista;

    public ModeloSQL() {
    }

    public ModeloSQL(String tabla) {

        campos = ContratoPry.obtenerCampos(tabla);

        estructura = campos;
        numcampos = (campos.length / 3);
        nombreTabla = campos[1];
        campostabla = (Integer.parseInt(campos[0]) - 2) / 3;
        this.campos = new String[numcampos];
        valores = new String[numcampos];

        for (int i = 0, x = 2; i < numcampos; i++, x += 3) {

            if (campos[x] != null) {
                this.campos[i] = campos[x];
            }

        }
    }

    public ModeloSQL(String[] campos) {

        estructura = campos;
        numcampos = (campos.length / 3);
        nombreTabla = campos[1];
        campostabla = (Integer.parseInt(campos[0]) - 2) / 3;
        this.campos = new String[numcampos];
        valores = new String[numcampos];

        for (int i = 0, x = 2; i < numcampos; i++, x += 3) {

            if (campos[x] != null) {
                this.campos[i] = campos[x];
            }

        }
    }

    public ModeloSQL(String[] campos, String id) {

        ModeloSQL modeloSQL = ConsultaBD.queryObject(campos, id);
        this.campos = campos;
        this.valores = modeloSQL.getValores();
        estructura = campos;
        numcampos = (campos.length / 3);
        nombreTabla = campos[1];
        campostabla = (Integer.parseInt(campos[0]) - 2) / 3;
    }

    public ModeloSQL(String[] campos, String id, int secuencia) {

        ModeloSQL modeloSQL = ConsultaBD.queryObjectDetalle(campos, id, secuencia);
        this.campos = campos;
        this.valores = modeloSQL.getValores();
        estructura = campos;
        numcampos = (campos.length / 3);
        nombreTabla = campos[1];
        campostabla = (Integer.parseInt(campos[0]) - 2) / 3;
    }

    public ModeloSQL(String[] campos, String id, String secuencia) {

        ModeloSQL modeloSQL = ConsultaBD.queryObjectDetalle(campos, id, secuencia);
        this.campos = campos;
        this.valores = modeloSQL.getValores();
        estructura = campos;
        numcampos = (campos.length / 3);
        nombreTabla = campos[1];
        campostabla = (Integer.parseInt(campos[0]) - 2) / 3;
    }

    public ModeloSQL(String[] campos, String[] valores) {

        estructura = campos;
        numcampos = (campos.length / 3);
        nombreTabla = campos[1];
        campostabla = (Integer.parseInt(campos[0]) - 2) / 3;
        this.campos = new String[numcampos];
        this.valores = new String[numcampos];

        for (int i = 0, x = 2; i < numcampos; i++, x += 3) {

            if (campos[x] != null) {
                this.campos[i] = campos[x];
            }
            if (valores[i] != null) {
                this.valores[i] = valores[i];
            }

        }


    }

    public ModeloSQL(String[] campos, String[] valores, boolean ref) {

        estructura = campos;
        numcampos = (campos.length / 3);
        nombreTabla = campos[1];
        campostabla = (Integer.parseInt(campos[0]) - 2) / 3;
        if (ref) {
            numcampos = campostabla;
        }
        this.campos = new String[numcampos];
        this.valores = new String[numcampos];

        for (int i = 0, x = 2; i < numcampos; i++, x += 3) {

            if (campos[x] != null) {
                this.campos[i] = campos[x];
            }
            if (valores[i] != null) {
                this.valores[i] = valores[i];
            }

        }


    }

    public void setCampos(String[] campos) {
        this.campos = campos;
    }

    public String[] getEstructura() {
        return estructura;
    }

    public void setEstructura(String[] estructura) {
        this.estructura = estructura;
    }

    public void setValores(String[] valores) {
        this.valores = valores;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public void setNumcampos(int numcampos) {
        this.numcampos = numcampos;
    }

    public ModeloSQL clonar(boolean ref) {

        return new ModeloSQL(estructura, valores, ref);

    }

    public boolean esDetalle() {

        for (int i = 0; i < numcampos; i++) {
            if (campos[i].equals(CAMPO_SECUENCIA)) {
                return true;
            }
        }

        return false;
    }

    public ContentValues contenido() {

        ContentValues values = new ContentValues();

        int size = campostabla;

        for (int i = 0; i < size; i++) {

            values.put(campos[i], valores[i]);
        }

        return values;

    }

    public boolean noModificado() {

        ModeloSQL modeloSQL = CRUDutil.updateModelo(this);
        System.out.println("valores = " + Arrays.toString(valores));
        System.out.println("modeloSQL = " + Arrays.toString(modeloSQL.getValores()));

        for (int i = 0; i < valores.length; i++) {
            //if (!campos[i].equals(TIMESTAMP)) {
            if (valores[i] != null && modeloSQL.getValores()[i] != null) {
                if (!valores[i].equals(modeloSQL.getValores()[i])) {
                    System.out.println("valores = " + valores[i]);
                    System.out.println("modeloSQL = " + modeloSQL.getValores()[i]);
                    return false;
                }

            } else if (valores[i] == null && modeloSQL.getValores()[i] == null) {
                continue;
            } else {
                System.out.println("valores = " + valores[i]);
                System.out.println("modeloSQL = " + modeloSQL.getValores()[i]);

                return false;
            }
            //}
        }
        return true;
    }

    public String getCampoID() {
        return this.campos[0];
    }

    public String getCampos(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)) {
                return valores[i];
            }
        }
        return null;
    }

    public String getString(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)) {
                return valores[i];
            }
        }
        return null;
    }

    public Uri getUri(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)) {
                if (valores[i] != null) {
                    return Uri.parse(valores[i]);
                }
            }
        }
        return null;
    }

    public int getInt(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)) {
                return JavaUtil.comprobarInteger(valores[i]);
            }
        }
        return 0;
    }

    public double getDouble(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)) {
                return JavaUtil.comprobarDouble(valores[i]);
            }
        }
        return 0.0;
    }

    public long getLong(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)) {
                return JavaUtil.comprobarLong(valores[i]);
            }
        }
        return 0;
    }

    public float getFloat(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)) {
                return JavaUtil.comprobarFloat(valores[i]);
            }
        }
        return 0.0F;
    }

    public short getShort(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)) {
                return JavaUtil.comprobarShort(valores[i]);
            }
        }
        return 0;
    }

    public void setCampos(String campo, String valor) {

        if (valores != null) {
            for (int i = 0; i < numcampos; i++) {

                if (campos[i].equals(campo)) {

                    valores[i] = valor;
                    System.out.println(campo + " = " + valor);
                    break;
                }
            }

        }
    }

    public void setCampos(String campo, int valor) {

        if (valores != null) {
            for (int i = 0; i < numcampos; i++) {

                if (campos[i].equals(campo)) {

                    valores[i] = String.valueOf(valor);
                    break;
                }
            }
        }
    }

    public void setCampos(String campo, double valor) {

        if (valores != null) {
            for (int i = 0; i < numcampos; i++) {

                if (campos[i].equals(campo)) {

                    valores[i] = String.valueOf(valor);
                    break;
                }
            }
        }
    }

    public void setCampos(String campo, long valor) {

        if (valores != null) {
            for (int i = 0; i < numcampos; i++) {

                if (campos[i].equals(campo)) {

                    valores[i] = String.valueOf(valor);
                    break;
                }
            }
        }
    }

    public void setCampos(String campo, float valor) {

        if (valores != null) {
            for (int i = 0; i < numcampos; i++) {

                if (campos[i].equals(campo)) {

                    valores[i] = String.valueOf(valor);
                    break;
                }
            }
        }
    }

    public void setCampos(String campo, short valor) {

        if (valores != null) {
            for (int i = 0; i < numcampos; i++) {

                if (campos[i].equals(campo)) {

                    valores[i] = String.valueOf(valor);
                    break;
                }
            }
        }
    }

    public int getPosicionLista() {
        return posicionLista;
    }

    public void setPosicionLista(int posicionLista) {
        this.posicionLista = posicionLista;
    }

    public int getIndiceLista() {
        return indiceLista;
    }

    public void setIndiceLista(int indiceLista) {
        this.indiceLista = indiceLista;
    }

    public boolean isEnLista() {
        return enLista;
    }

    public void setEnLista(boolean enLista) {
        this.enLista = enLista;
    }

    public String[] getCampos() {
        return campos;
    }

    public String[] getValores() {
        return valores;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public int getNumcampos() {
        return numcampos;
    }

    public int getCampostabla() {
        return campostabla;
    }

    public void setCampostabla(int campostabla) {
        this.campostabla = campostabla;
    }
}
