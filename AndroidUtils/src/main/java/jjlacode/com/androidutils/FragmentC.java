package jjlacode.com.androidutils;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import java.io.FileNotFoundException;
import java.io.IOException;

public abstract  class FragmentC extends FragmentBase {


    protected Button btnsave;
    protected Button btnback;
    protected ImageView imagen;
    protected String path;
    protected ImagenUtil imagenUtil;
    protected ContentValues valores;
    final protected int COD_FOTO = 10;
    final protected int COD_SELECCIONA = 20;

    public FragmentC() {
        // Required empty public constructor
    }


    protected abstract boolean registrar();

    protected void mostrarDialogoOpcionesImagen() {

        final CharSequence[] opciones = {"Hacer foto desde cámara",
                "Elegir de la galería", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                imagenUtil = new ImagenUtil(getContext());

                if (opciones[which].equals("Hacer foto desde cámara")) {

                    try {
                        startActivityForResult(imagenUtil.takePhotoIntent(), COD_FOTO);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imagenUtil.addToGallery();

                } else if (opciones[which].equals("Elegir de la galería")) {

                    startActivityForResult(imagenUtil.openGalleryIntent(), COD_SELECCIONA);

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

        String photoPath;

        switch (requestCode) {

            case COD_SELECCIONA:
                imagenUtil.setPhotoUri(data.getData());
                photoPath = imagenUtil.getPath();
                try {
                    Bitmap bitmap = ImagenUtil.ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    imagen.setImageBitmap(bitmap);
                    path = photoPath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case COD_FOTO:
                photoPath = imagenUtil.getPhotoPath();
                try {
                    Bitmap bitmap = ImagenUtil.ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    imagen.setImageBitmap(bitmap); //imageView is your ImageView
                    path = photoPath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

        }
    }


}