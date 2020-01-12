package com.codevsolution.base.models;

import androidx.fragment.app.Fragment;

public class DestinosVoz {

    String destino;
    String actual;
    String tabla;
    ListaModeloSQL listaModeloSQL;
    Fragment fragment;
    boolean nuevo;

    public DestinosVoz() {
    }

    public DestinosVoz(String destino, Fragment fragment) {
        this.destino = destino;
        this.fragment = fragment;
    }

    public DestinosVoz(String destino, Fragment fragment, boolean nuevo) {
        this.destino = destino;
        this.fragment = fragment;
        this.nuevo = nuevo;
    }

    public DestinosVoz(String destino, Fragment fragment, String actual) {
        this.destino = destino;
        this.fragment = fragment;
        this.actual = actual;
    }

    public DestinosVoz(String destino, Fragment fragment, String actual, String tabla) {
        this.destino = destino;
        this.fragment = fragment;
        this.actual = actual;
        this.tabla = tabla;
        listaModeloSQL = new ListaModeloSQL(tabla);
    }

    public DestinosVoz(String destino, Fragment fragment, String actual, boolean nuevo) {
        this.destino = destino;
        this.fragment = fragment;
        this.actual = actual;
        this.nuevo = nuevo;
    }

    public DestinosVoz(String destino, Fragment fragment, String actual, String tabla, boolean nuevo) {
        this.destino = destino;
        this.fragment = fragment;
        this.actual = actual;
        this.tabla = tabla;
        this.nuevo = nuevo;
        listaModeloSQL = new ListaModeloSQL(tabla);
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public ListaModeloSQL getListaModeloSQL() {
        return listaModeloSQL;
    }

    public void setListaModeloSQL(ListaModeloSQL listaModeloSQL) {
        this.listaModeloSQL = listaModeloSQL;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }
}
