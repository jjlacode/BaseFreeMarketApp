package com.codevsolution.base.media;

import android.view.View;
import android.widget.ImageView;

import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.freemarketsapp.R;

public class VisorImagen extends FragmentBase {

    private String path;
    private boolean fire;
    private ImageView image;
    private boolean ampliada;

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.visor_imagen;
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        path = bundle.getString(PATH);
        fire = bundle.getBoolean(TIPO);
        System.out.println("path = " + path);
        System.out.println("fire = " + fire);

        if (fire) {
            ImagenUtil.setImageFireStore(path, image, anchoReal, altoReal);
        } else {
            ImagenUtil.setImageUri(path, image, anchoReal, altoReal);
        }

        acciones();
    }

    @Override
    protected void setInicio() {

        image = view.findViewById(R.id.imgvisor);

    }

    @Override
    protected void acciones() {
        super.acciones();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ampliada) {
                    if (fire) {
                        ImagenUtil.setImageFireStore(path, image, anchoReal * 2, altoReal * 2);
                    } else {
                        ImagenUtil.setImageUri(path, image, anchoReal * 2, altoReal * 2);
                    }
                    ampliada = true;
                } else {
                    if (fire) {
                        ImagenUtil.setImageFireStore(path, image, anchoReal, altoReal);
                    } else {
                        ImagenUtil.setImageUri(path, image, anchoReal, altoReal);
                    }
                    ampliada = false;

                }

            }
        });
    }
}
