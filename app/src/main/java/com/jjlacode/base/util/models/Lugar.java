package com.jjlacode.base.util.models;

public class Lugar {

    String id;
    String idMark;
    String key;
    String place;
    boolean activo;

    public Lugar() {
    }

    public Lugar(String id, String idMark, String key, String place, boolean activo) {
        this.id = id;
        this.idMark = idMark;
        this.key = key;
        this.place = place;
        this.activo = activo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdMark() {
        return idMark;
    }

    public void setIdMark(String idMark) {
        this.idMark = idMark;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
