package jjlacode.com.freelanceproject.model;

import java.io.Serializable;

public class ProdProv implements Serializable {

    private String id;

    private String refprov;

    private String nombre;

    private String descripcion;

    private String web;

    private double descProv;

    private double precio;

    private String rutafoto;

    private String proveedor;

    private String categoria;

    private String idprov;

    private String alcance;


    public ProdProv() {
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

    public String getRutafoto() {
        return rutafoto;
    }

    public void setRutafoto(String rutafoto) {
        this.rutafoto = rutafoto;
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
}

