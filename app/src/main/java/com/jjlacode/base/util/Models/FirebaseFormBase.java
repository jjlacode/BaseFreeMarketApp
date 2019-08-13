package com.jjlacode.base.util.Models;

import java.io.Serializable;

public class FirebaseFormBase implements Serializable {

    private String nombreBase;
    private String descripcionBase;
    private String direccionBase;
    private String emailBase;
    private String telefonoBase;
    private String webBase;
    private String clavesBase;
    private String idchatBase;
    private String tipoBase;

    public FirebaseFormBase() {
    }

    public String getNombreBase() {
        return nombreBase;
    }

    public void setNombreBase(String nombreBase) {
        this.nombreBase = nombreBase;
    }

    public String getDescripcionBase() {
        return descripcionBase;
    }

    public void setDescripcionBase(String descripcionBase) {
        this.descripcionBase = descripcionBase;
    }

    public String getDireccionBase() {
        return direccionBase;
    }

    public void setDireccionBase(String direccionBase) {
        this.direccionBase = direccionBase;
    }

    public String getEmailBase() {
        return emailBase;
    }

    public void setEmailBase(String emailBase) {
        this.emailBase = emailBase;
    }

    public String getTelefonoBase() {
        return telefonoBase;
    }

    public void setTelefonoBase(String telefonoBase) {
        this.telefonoBase = telefonoBase;
    }

    public String getWebBase() {
        return webBase;
    }

    public void setWebBase(String webBase) {
        this.webBase = webBase;
    }

    public String getClavesBase() {
        return clavesBase;
    }

    public void setClavesBase(String clavesBase) {
        this.clavesBase = clavesBase;
    }

    public String getIdchatBase() {
        return idchatBase;
    }

    public void setIdchatBase(String idchatBase) {
        this.idchatBase = idchatBase;
    }

    public String getTipoBase() {
        return tipoBase;
    }

    public void setTipoBase(String tipoBase) {
        this.tipoBase = tipoBase;
    }
}
