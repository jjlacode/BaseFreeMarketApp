package com.codevsolution.base.json;

public class ModeloJson {

    private String[] estructura;
    private String[] valores;

    public ModeloJson() {
    }

    public ModeloJson(String[] estructura, String[] valores) {
        this.estructura = estructura;
        this.valores = valores;
    }

    public String[] getEstructura() {
        return estructura;
    }

    public void setEstructura(String[] estructura) {
        this.estructura = estructura;
    }

    public String[] getValores() {
        return valores;
    }

    public void setValores(String[] valores) {
        this.valores = valores;
    }
}
