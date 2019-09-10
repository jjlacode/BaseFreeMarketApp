package com.jjlacode.base.util.models;

import java.io.Serializable;

public class Productos implements Serializable {

    private String id;

    private String refprov;

    private String nombre;

    private String descripcion;

    private String web;

    private double descProv;

    private double precio;

    private String proveedor;

    private String categoria;

    private String idprov;

    private String alcance;

    private String tipo;

    private boolean activo;

    private boolean multi;

    private String rutafoto;


    public Productos() {
    }

    public Productos(String id, String refprov, String nombre, String descripcion, String web,
                     double descProv, double precio, String proveedor, String categoria,
                     String idprov, String alcance, String tipo, boolean activo,
                     boolean multi, String rutafoto) {
        this.id = id;
        this.refprov = refprov;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.web = web;
        this.descProv = descProv;
        this.precio = precio;
        this.proveedor = proveedor;
        this.categoria = categoria;
        this.idprov = idprov;
        this.alcance = alcance;
        this.tipo = tipo;
        this.activo = activo;
        this.multi = multi;
        this.rutafoto = rutafoto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefprov() {
        return refprov;
    }

    public void setRefprov(String refprov) {
        this.refprov = refprov;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getDescProv() {
        return descProv;
    }

    public void setDescProv(double descProv) {
        this.descProv = descProv;
    }

    public String getIdprov() {
        return idprov;
    }

    public void setIdprov(String idprov) {
        this.idprov = idprov;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getAlcance() {
        return alcance;
    }

    public void setAlcance(String alcance) {
        this.alcance = alcance;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getRutafoto() {
        return rutafoto;
    }

    public void setRutafoto(String rutafoto) {
        this.rutafoto = rutafoto;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

