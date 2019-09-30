package com.codevsolution.base.android.controls;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class ViewRelativeLayout extends ViewLayout {
    private RelativeLayout lyViewRelativeLayout;

    public ViewRelativeLayout(Context context, ViewGroup viewGroup) {
        super(context, viewGroup);
    }

    @Override
    protected void setViewGroup() {

        viewGroup = new RelativeLayout(context);
    }

    protected void inicializar() {

    }

    protected void asignarEventos() {


    }

    @Override
    protected void setLayoutParams(ViewGroup viewGroup, View view) {

        RelativeLayout.LayoutParams params;
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        params.setMargins(5, 5, 5, 5);
        view.setLayoutParams(params);
    }
}
