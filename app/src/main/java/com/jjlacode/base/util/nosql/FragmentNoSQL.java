package com.jjlacode.base.util.nosql;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.GenericTypeIndicator;
import com.jjlacode.base.util.android.AndroidUtil;
import com.jjlacode.base.util.android.FragmentBase;
import com.jjlacode.base.util.android.controls.ImagenLayout;
import com.jjlacode.base.util.media.MediaUtil;

import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public abstract class FragmentNoSQL extends FragmentBase {

    private MediaUtil mediaUtil;
    protected String path;
    final protected int COD_FOTO = 10;
    final protected int COD_SELECCIONA = 20;
    protected ImagenLayout imagen;
    protected GenericTypeIndicator<ArrayList<String>> tipoArrayListsString;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        tipoArrayListsString = new GenericTypeIndicator<ArrayList<String>>() {
        };
    }

    protected void mostrarDialogoOpcionesImagen(final Context contexto) {
        Log.d(TAG, getMetodo());

        final CharSequence[] opciones = {"Hacer foto desde cámara",
                "Elegir de la galería", "Quitar foto", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mediaUtil = new MediaUtil(contexto);


                if (opciones[which].equals("Hacer foto desde cámara")) {

                    try {
                        startActivityForResult(mediaUtil.takePhotoIntent(), COD_FOTO);
                        mediaUtil.addPhotoToGallery();
                        path = mediaUtil.getPath(mediaUtil.getPhotoUri());
                        AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PATH, path);

                    } catch (IOException e) {
                        Log.e("DialogoOpcionesImagen", e.toString());
                    }

                } else if (opciones[which].equals("Elegir de la galería")) {

                    startActivityForResult(mediaUtil.openGalleryIntent(), COD_SELECCIONA);

                } else if (opciones[which].equals("Quitar foto")) {

                    path = null;

                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, getMetodo());

        mediaUtil = new MediaUtil(contexto);
        System.out.println("requestCode = " + requestCode);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case COD_FOTO:

                    path = AndroidUtil.getSharePreference(contexto, PERSISTENCIA, PATH, path);
                    guardarImagen();

                case COD_SELECCIONA:

                    if (data != null && data.getData() != null) {
                        path = mediaUtil.getPath(data.getData());
                        AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PATH, path);
                        System.out.println("path = " + path);
                        guardarImagen();
                    }
                    break;

                case AUDIORECORD:

                    if (data.getData() != null) {
                        path = mediaUtil.getPath(data.getData());
                    }
                    break;


            }
        }
    }

    protected void guardarImagen() {

    }

}
