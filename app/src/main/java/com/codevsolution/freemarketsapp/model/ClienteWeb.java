package com.codevsolution.freemarketsapp.model;

import java.io.Serializable;

public class ClienteWeb implements Serializable {

    private String nombref;
    private String descripcionf;
    private String direccionf;
    private String emailf;
    private String telefonof;
    private String webf;
    private String clavesf;
    private String idchatf;

    public ClienteWeb() {
    }

    public String getNombref() {
        return nombref;
    }

    public void setNombref(String nombref) {
        this.nombref = nombref;
    }

    public String getDescripcionf() {
        return descripcionf;
    }

    public void setDescripcionf(String descripcionf) {
        this.descripcionf = descripcionf;
    }

    public String getDireccionf() {
        return direccionf;
    }

    public void setDireccionf(String direccionf) {
        this.direccionf = direccionf;
    }

    public String getEmailf() {
        return emailf;
    }

    public void setEmailf(String emailf) {
        this.emailf = emailf;
    }

    public String getTelefonof() {
        return telefonof;
    }

    public void setTelefonof(String telefonof) {
        this.telefonof = telefonof;
    }

    public String getWebf() {
        return webf;
    }

    public void setWebf(String webf) {
        this.webf = webf;
    }

    public String getClavesf() {
        return clavesf;
    }

    public void setClavesf(String clavesf) {
        this.clavesf = clavesf;
    }

    public String getIdchatf() {
        return idchatf;
    }

    public void setIdchatf(String idchatf) {
        this.idchatf = idchatf;
    }

}
