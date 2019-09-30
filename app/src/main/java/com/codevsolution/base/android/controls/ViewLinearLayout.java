package com.codevsolution.base.android.controls;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.LinearLayoutCompat;

public class ViewLinearLayout extends ViewLayout {

    public static final int HORIZONTAL = LinearLayoutCompat.HORIZONTAL;
    public static final int VERTICAL = LinearLayoutCompat.VERTICAL;
    private int orientacion;

    public ViewLinearLayout(Context context, ViewGroup viewGroup) {
        super(context, viewGroup);

        this.context = context;

    }

    @Override
    protected void setViewGroup() {

        viewGroup = new LinearLayoutCompat(context);

    }

    protected void inicializar() {

        setOrientacion(VERTICAL);
    }

    protected void asignarEventos() {


    }

    public void setOrientacion(int orientacion) {

        if (viewGroupParent instanceof LinearLayoutCompat) {
            if (orientacion == VERTICAL) {
                LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                viewGroup.setLayoutParams(params);
                this.orientacion = VERTICAL;
                ((LinearLayoutCompat) viewGroup).setOrientation(VERTICAL);

            } else {

                LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT);
                viewGroup.setLayoutParams(params);
                this.orientacion = HORIZONTAL;
                ((LinearLayoutCompat) viewGroup).setOrientation(HORIZONTAL);

            }
        } else if (viewGroupParent instanceof LinearLayout) {
            if (orientacion == VERTICAL) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                viewGroup.setLayoutParams(params);
                this.orientacion = VERTICAL;
                ((LinearLayoutCompat) viewGroup).setOrientation(VERTICAL);

            } else {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                viewGroup.setLayoutParams(params);
                this.orientacion = HORIZONTAL;
                ((LinearLayoutCompat) viewGroup).setOrientation(HORIZONTAL);

            }
        }



    }

    protected void setLayoutParams(ViewGroup viewGroup, View view) {

        if (viewGroup instanceof LinearLayoutCompat) {
            LinearLayoutCompat.LayoutParams params;
            if (((LinearLayoutCompat) viewGroup).getOrientation() == VERTICAL) {
                params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT, 1);
            } else {
                params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1);

            }
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof LinearLayout) {
            LinearLayout.LayoutParams params;
            if (((LinearLayoutCompat) viewGroup).getOrientation() == VERTICAL) {
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
            } else {
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1);

            }
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        }


    }

    /*
    public void setWeigthButton(int position, int weigth) {

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT, weigth);
        buttons.get(position).setLayoutParams(params);
    }

    public void setWeigthButton(int position) {

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1);
        buttons.get(position).setLayoutParams(params);
    }

    public void setWeigthImageButton(int position, int weigth) {

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT, weigth);
        imageButtons.get(position).setLayoutParams(params);
    }

    public void setWeigthImageButton(int position) {

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1);
        imageButtons.get(position).setLayoutParams(params);
    }

     */
}
