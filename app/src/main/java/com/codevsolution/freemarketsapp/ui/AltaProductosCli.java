package com.codevsolution.freemarketsapp.ui;

import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.freemarketsapp.logica.Interactor;

public class AltaProductosCli extends AltaProductosFirebase
        implements Interactor.ConstantesPry, ContratoPry.Tablas {

    private ViewGroup viewGroup;
    private FragmentBase frParent;
    private AppCompatActivity activityBase;

    public AltaProductosCli() {
    }

    public AltaProductosCli(FragmentBase frParent, ViewGroup viewGroup, AppCompatActivity activityBase, String id) {
        this.viewGroup = viewGroup;
        this.frParent = frParent;
        this.activityBase = activityBase;
        this.id = id;
        int idViewGroup = this.viewGroup.getId();
        System.out.println("idViewGroup = " + idViewGroup);
        if (idViewGroup < 0) {
            idViewGroup = ViewGroup.generateViewId();
        }
        this.viewGroup.setId(idViewGroup);
        this.frParent.getIcFragmentos().addFragment(this, idViewGroup);
        esDetalle = true;
        Estilos.setLayoutParams((ViewGroup) this.viewGroup.getParent(), this.viewGroup, ViewGroupLayout.MATCH_PARENT, (int) ((double) this.frParent.getAltoReal()));
    }

    @Override
    protected String setTipo() {
        return PRODUCTOCLI;
    }

    @Override
    protected String setTipoSorteo() {
        return SORTEOCLI;
    }



}
