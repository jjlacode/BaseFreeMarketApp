package com.codevsolution.base.models;

import android.content.ContentValues;
import android.net.Uri;

import com.codevsolution.base.javautil.JavaUtil;

import java.io.Serializable;
import java.util.Map;

public class Modelo implements Serializable {

    private String[] campos;

    private String[] estructura;

    private String[] valores;

    private String nombreModelo;

    private int numcampos;

    private int posicionLista;

    private int indiceLista;

    private boolean enLista;

    public Modelo() {
    }

    public Modelo(String modelo) {

        estructura = ContratoModels.obtenerCampos(modelo);
        numcampos = (estructura.length - 1) / 2;
        nombreModelo = estructura[0];
        this.campos = new String[numcampos];
        valores = new String[numcampos];
        valores[0] = ContratoModels.generarIdModelo(nombreModelo);

        for (int i = 0, x = 1; i < numcampos; i++, x += 2) {

            if (estructura[x] != null) {
                this.campos[i] = estructura[x];
            }

        }
    }

    public Modelo(String[] campos) {

        estructura = campos;
        numcampos = (campos.length - 1) / 2;
        nombreModelo = campos[0];
        this.campos = new String[numcampos];
        valores = new String[numcampos];

        for (int i = 0, x = 1; i < numcampos; i++, x += 2) {

            if (campos[x] != null) {
                this.campos[i] = campos[x];
            }

        }
    }

    public Modelo(String[] campos, String[] valores) {

        estructura = campos;
        numcampos = (campos.length - 1) / 2;
        nombreModelo = campos[0];
        this.campos = new String[numcampos];
        this.valores = new String[numcampos];
        this.valores[0] = ContratoModels.generarIdModelo(nombreModelo);

        for (int i = 0, x = 1; i < numcampos; i++, x += 2) {

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

    public void setNombreModelo(String nombreModelo) {
        this.nombreModelo = nombreModelo;
    }

    public void setNumcampos(int numcampos) {
        this.numcampos = numcampos;
    }

    public Modelo clonar() {

        return new Modelo(estructura, valores);

    }

    public ContentValues contenido() {

        ContentValues values = new ContentValues();

        int size = numcampos;

        for (int i = 0; i < size; i++) {

            values.put(campos[i], valores[i]);
        }

        return values;

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

    public String getNombreModelo() {
        return nombreModelo;
    }

    public int getNumcampos() {
        return numcampos;
    }

    public Map toJason() {

        Map jason = null;

        for (int i = 0; i < numcampos; i++) {
            jason.put(campos[i], valores[i]);
        }
        return jason;
    }

}
