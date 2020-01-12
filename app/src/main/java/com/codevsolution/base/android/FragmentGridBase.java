package com.codevsolution.base.android;

import androidx.recyclerview.widget.GridLayoutManager;

import com.codevsolution.base.R;
import com.codevsolution.base.android.controls.ViewGroupLayout;

public abstract class FragmentGridBase extends FragmentRV {

    protected int columnas = 3;
    protected int filas = 4;
    protected int altoimg = 100;
    protected int anchoimg = 100;
    protected int padtxt = 20;
    protected int padalto = 10;
    protected int padancho = 10;
    protected float sizeT;
    private float relScreen;
    protected ViewGroupLayout vistaMain;

    public FragmentGridBase() {
        // Required empty public constructor
    }

    @Override
    protected void setLayout() {

        layoutItem = R.layout.item_grid;
    }

    @Override
    protected void setInicio() {


        bundle = getArguments();
        if (bundle != null) {

            origen = bundle.getString(ORIGEN);
            bundle = null;
        }

        //frCuerpo.setOrientation(LinearLayout.VERTICAL);
        gone(frameAnimationCuerpo);
        gone(frPie);
        frCuerpo.setPadding(0, 0, 0, 0);

        relScreen = (float) alto / (float) ancho;

        setLista();

        if (land) {

            for (int i = lista.size(); i > 0; i--) {
                filas = i;
                columnas = (int) ((double) lista.size() / i) + (lista.size() % i);
                if (filas >= columnas - 1) {
                    continue;
                }
                break;
            }

        } else {
            for (int i = 1; i < lista.size(); i++) {
                columnas = i;
                filas = (int) ((double) lista.size() / i);
                if (lista.size() % i > 0) {
                    filas++;
                }
                if (filas > columnas + 1) {
                    continue;
                }
                break;
            }
        }

        setColumnasFilas();

        anchoimg = ancho / columnas;
        altoimg = alto / filas;
        padancho = (int) ((double) (anchoimg) / (4));
        padalto = (int) ((double) (altoimg) / (4));
        padtxt = (int) ((double) padancho / 2);

        sizeT = (sizeText * 4) / 5;
        contexto = getContext();

    }

    protected void setColumnasFilas() {

    }

    @Override
    protected void onSetRV() {
        super.onSetRV();
        gone(lupa);
        gone(auto);
        gone(renovar);
        gone(voz);
        gone(inicio);
        gone(activityBase.fabNuevo);
        visible(activityBase.fabVoz);

    }

    @Override
    protected void setManagerRV() {
        super.setManagerRV();

        layoutManager = new GridLayoutManager(contexto, columnas);

    }


}
