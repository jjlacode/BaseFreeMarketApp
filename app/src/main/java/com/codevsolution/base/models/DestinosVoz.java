package com.codevsolution.base.models;

import androidx.fragment.app.Fragment;

public class DestinosVoz {

    String destino;
    Fragment fragment;

    public DestinosVoz() {
    }

    public DestinosVoz(String destino, Fragment fragment) {
        this.destino = destino;
        this.fragment = fragment;
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
}
