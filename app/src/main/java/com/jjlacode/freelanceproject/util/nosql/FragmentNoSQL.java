package com.jjlacode.freelanceproject.util.nosql;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.util.android.FragmentBase;
import com.jjlacode.freelanceproject.util.crud.CRUDutil;
import com.jjlacode.freelanceproject.util.media.MediaUtil;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public abstract class FragmentNoSQL extends FragmentBase {
    private MediaUtil mediaUtil;
    protected String path;
    final protected int COD_FOTO = 10;
    final protected int COD_SELECCIONA = 20;
    protected ImageView imagen;

    @Override
    public void onResume() {
        super.onResume();

        acciones();
    }

    @Override
    protected void acciones() {
        super.acciones();

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogoOpcionesImagen(contexto);
            }
        });
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
                        CRUDutil.setSharePreference(contexto, PERSISTENCIA, PATH, path);

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

                    path = CRUDutil.getSharePreference(contexto, PERSISTENCIA, PATH, path);
                    guardarImagen();

                case COD_SELECCIONA:

                    if (data != null && data.getData() != null) {
                        path = mediaUtil.getPath(data.getData());
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

    protected void setImagenUri(Context contexto, String rutaFoto) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUri(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUri(MediaUtil imagenUtil, String rutaFoto) {

        imagenUtil.setImageUri(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUri(Context contexto, String rutaFoto, int drawable) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUri(rutaFoto, imagen, drawable);

    }

    protected void setImagenUri(MediaUtil imagenUtil, String rutaFoto, int drawable) {

        imagenUtil.setImageUri(rutaFoto, imagen, drawable);

    }

    protected void setImagenUri(Context contexto, String rutaFoto, ImageView imagen, int drawable) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUri(rutaFoto, imagen, drawable);

    }

    protected void setImagenUri(MediaUtil imagenUtil, String rutaFoto, ImageView imagen, int drawable) {

        imagenUtil.setImageUri(rutaFoto, imagen, drawable);

    }

    protected void setImagenUri(Context contexto, String rutaFoto, ImageView imagen) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUri(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUri(MediaUtil imagenUtil, String rutaFoto, ImageView imagen) {

        imagenUtil.setImageUri(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStore(Context contexto, String rutaFoto, int drawable) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStore(rutaFoto, imagen, drawable);

    }

    protected void setImagenFireStore(MediaUtil imagenUtil, String rutaFoto, int drawable) {

        imagenUtil.setImageFireStore(rutaFoto, imagen, drawable);

    }

    protected void setImagenFireStore(Context contexto, String rutaFoto, ImageView imagen, int drawable) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStore(rutaFoto, imagen, drawable);

    }

    protected void setImagenFireStore(MediaUtil imagenUtil, String rutaFoto, ImageView imagen, int drawable) {

        imagenUtil.setImageFireStore(rutaFoto, imagen, drawable);

    }

    protected void setImagenFireStore(Context contexto, String rutaFoto, ImageView imagen) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStore(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStore(MediaUtil imagenUtil, String rutaFoto, ImageView imagen) {

        imagenUtil.setImageFireStore(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUriCircle(Context contexto, String rutaFoto) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUriCircle(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUriCircle(MediaUtil imagenUtil, String rutaFoto) {

        imagenUtil.setImageUriCircle(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUriCircle(Context contexto, String rutaFoto, int drawable) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUriCircle(rutaFoto, imagen, drawable);

    }

    protected void setImagenUriCircle(MediaUtil imagenUtil, String rutaFoto, int drawable) {

        imagenUtil.setImageUriCircle(rutaFoto, imagen, drawable);

    }

    protected void setImagenUriCircle(Context contexto, String rutaFoto, ImageView imagen, int drawable) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUriCircle(rutaFoto, imagen, drawable);

    }

    protected void setImagenUriCircle(MediaUtil imagenUtil, String rutaFoto, ImageView imagen, int drawable) {

        imagenUtil.setImageUriCircle(rutaFoto, imagen, drawable);

    }

    protected void setImagenUriCircle(Context contexto, String rutaFoto, ImageView imagen) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUriCircle(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUriCircle(MediaUtil imagenUtil, String rutaFoto, ImageView imagen) {

        imagenUtil.setImageUriCircle(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStoreCircle(Context contexto, String rutaFoto, int drawable) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStoreCircle(rutaFoto, imagen, drawable);

    }

    protected void setImagenFireStoreCircle(MediaUtil imagenUtil, String rutaFoto, int drawable) {

        imagenUtil.setImageFireStoreCircle(rutaFoto, imagen, drawable);

    }

    protected void setImagenFireStoreCircle(Context contexto, String rutaFoto, ImageView imagen, int drawable) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStoreCircle(rutaFoto, imagen, drawable);

    }

    protected void setImagenFireStoreCircle(MediaUtil imagenUtil, String rutaFoto, ImageView imagen, int drawable) {

        imagenUtil.setImageFireStoreCircle(rutaFoto, imagen, drawable);

    }

    protected void setImagenFireStoreCircle(Context contexto, String rutaFoto, ImageView imagen) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStoreCircle(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStoreCircle(MediaUtil imagenUtil, String rutaFoto, ImageView imagen) {

        imagenUtil.setImageFireStoreCircle(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }
}
