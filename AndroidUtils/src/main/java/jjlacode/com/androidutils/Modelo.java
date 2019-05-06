package jjlacode.com.androidutils;

import android.content.ContentValues;
import android.net.Uri;

import java.io.Serializable;

public class Modelo implements Serializable {

    private String[] campos;

    private String[] estructura;

    private String[] valores;

    private String nombreTabla;

    private int numcampos;

    private int campostabla;


    public Modelo(String[] campos) {

        estructura = campos;
        numcampos = (campos.length/3);
        nombreTabla = campos[1];
        campostabla = (Integer.parseInt(campos[0])-2)/3;
        this.campos = new String[numcampos];
        valores = new String[numcampos];

        for (int i = 0,x = 2; i < numcampos ;i++,x+=3) {

            if (campos[x]!=null) {
                this.campos[i] = campos[x];
            }

        }
    }

    public Modelo(String[] campos, String[] valores) {

        estructura = campos;
        numcampos = (campos.length/3);
        nombreTabla = campos[1];
        campostabla = (Integer.parseInt(campos[0])-2)/3;
        this.campos = new String[numcampos];
        this.valores = new String[numcampos];

        for (int i = 0,x = 2; i < numcampos ;i++,x+=3) {

            if (campos[x]!=null) {
                this.campos[i] = campos[x];
            }
            if (valores[i]!=null) {
                this.valores[i] = valores[i];
            }

        }


    }

    public Modelo(String[] campos, String[] valores, boolean ref) {

        estructura = campos;
        numcampos = (campos.length/3);
        nombreTabla = campos[1];
        campostabla = (Integer.parseInt(campos[0])-2)/3;
        if (ref){numcampos = campostabla;}
        this.campos = new String[numcampos];
        this.valores = new String[numcampos];

        for (int i = 0,x = 2; i < numcampos ;i++,x+=3) {

            if (campos[x]!=null) {
                this.campos[i] = campos[x];
            }
            if (valores[i]!=null) {
                this.valores[i] = valores[i];
            }

        }


    }

    public Modelo clonar(boolean ref){

        return new Modelo(estructura,valores,ref);

    }

    public ContentValues contenido(){

        ContentValues values = new ContentValues();

        int size = campostabla;

        for (int i = 0; i < size; i++) {

            values.put(campos[i],valores[i]);
        }

        return values;

    }

    public String getCampos(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)){
                return valores[i];
            }
        }
        return null;
    }

    public String getString(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)){
                return valores[i];
            }
        }
        return null;
    }

    public Uri getUri(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)){
                if (valores[i]!=null) {
                    return Uri.parse(valores[i]);
                }
            }
        }
        return null;
    }

    public int getInt(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)){
                return JavaUtil.comprobarInteger(valores[i]);
            }
        }
        return 0;
    }

    public double getDouble(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)){
                return JavaUtil.comprobarDouble(valores[i]);
            }
        }
        return 0.0;
    }

    public long getLong(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)){
                return JavaUtil.comprobarLong(valores[i]);
            }
        }
        return 0;
    }

    public float getFloat(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)){
                return JavaUtil.comprobarFloat(valores[i]);
            }
        }
        return 0.0F;
    }

    public short getShort(String campo) {

        for (int i = 0; i < numcampos; i++) {

            if (campos[i].equals(campo)){
                return JavaUtil.comprobarShort(valores[i]);
            }
        }
        return 0;
    }

    public void setCampos(String campo, String valor) {

        if (valores!=null) {
            for (int i = 0; i < numcampos; i++) {

                if (campos[i].equals(campo)) {

                    valores[i] = valor;
                    System.out.println(campo + " = "+valor );
                    break;
                }
            }

        }
    }

    public void setCampos(String campo, int valor) {

        if (valores!=null) {
            for (int i = 0; i < numcampos; i++) {

                if (campos[i].equals(campo)) {

                    valores[i] = String.valueOf(valor);
                    break;
                }
            }
        }
    }

    public void setCampos(String campo, double valor) {

        if (valores!=null) {
            for (int i = 0; i < numcampos; i++) {

                if (campos[i].equals(campo)) {

                    valores[i] = String.valueOf(valor);
                    break;
                }
            }
        }
    }

    public void setCampos(String campo, long valor) {

        if (valores!=null) {
            for (int i = 0; i < numcampos; i++) {

                if (campos[i].equals(campo)) {

                    valores[i] = String.valueOf(valor);
                    break;
                }
            }
        }
    }

    public void setCampos(String campo, float valor) {

        if (valores!=null) {
            for (int i = 0; i < numcampos; i++) {

                if (campos[i].equals(campo)) {

                    valores[i] = String.valueOf(valor);
                    break;
                }
            }
        }
    }

    public void setCampos(String campo, short valor) {

        if (valores!=null) {
            for (int i = 0; i < numcampos; i++) {

                if (campos[i].equals(campo)) {

                    valores[i] = String.valueOf(valor);
                    break;
                }
            }
        }
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
