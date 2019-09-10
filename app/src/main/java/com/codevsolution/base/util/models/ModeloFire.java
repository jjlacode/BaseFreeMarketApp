package com.codevsolution.base.util.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModeloFire implements Serializable {

    private ArrayList<String> valores;


    public ModeloFire() {
    }

    public List getValores() {
        return valores;
    }

    public void setValores(ArrayList<String> valores) {
        this.valores = valores;
    }


}
