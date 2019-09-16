package com.codevsolution.base.models;

public class Marcador {

    String id;
    String key;
    String idMark;
    String tipo;
    int alcance;
    long latitud;
    long longitud;
    boolean activo;

    public Marcador() {
    }

    public Marcador(String id, String key, String idMark, String tipo, int alcance, long latitud, long longitud, boolean activo) {
        this.id = id;
        this.key = key;
        this.idMark = idMark;
        this.tipo = tipo;
        this.alcance = alcance;
        this.latitud = latitud;
        this.longitud = longitud;
        this.activo = activo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIdMark() {
        return idMark;
    }

    public void setIdMark(String idMark) {
        this.idMark = idMark;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getLatitud() {
        return latitud;
    }

    public void setLatitud(long latitud) {
        this.latitud = latitud;
    }

    public long getLongitud() {
        return longitud;
    }

    public void setLongitud(long longitud) {
        this.longitud = longitud;
    }

    public int getAlcance() {
        return alcance;
    }

    public void setAlcance(int alcance) {
        this.alcance = alcance;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
