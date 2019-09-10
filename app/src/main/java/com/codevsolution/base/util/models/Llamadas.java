package com.codevsolution.base.util.models;

public class Llamadas {

    private String tipo;
    private String numero;
    private long fecha;

    public Llamadas(String tipo, String numero, long fecha) {
        this.tipo = tipo;
        this.numero = numero;
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }
}
