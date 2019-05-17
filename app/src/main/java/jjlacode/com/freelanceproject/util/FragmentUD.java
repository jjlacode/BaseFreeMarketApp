package jjlacode.com.freelanceproject.util;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import java.io.FileNotFoundException;
import java.io.IOException;

import jjlacode.com.freelanceproject.R;

public abstract class FragmentUD extends FragmentBaseCRUD {

    protected Button btnsave;
    protected Button btnback;
    protected Button btndelete;
    protected ImageView imagen;
    protected String path;
    protected ImagenUtil imagenUtil;
    protected ContentValues valores;
    private final int COD_FOTO = 10;
    private final int COD_SELECCIONA = 20;
    protected String id;

    public FragmentUD() {
    }

    protected abstract void update();

    protected abstract void delete();

    @Override
    public void onResume() {

        bundle = getArguments();

        if (bundle != null) {
            origen = bundle.getString(ORIGEN);
            subTitulo = bundle.getString(SUBTITULO);
        }

        cargarBundle();

            if (id != null) {

                setDatos();

            }

        super.onResume();
    }

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

        if (requestCode>0) {

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

    protected void setImagenUri(String rutaFoto){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageUri(rutaFoto,imagen, R.drawable.ico_calendario_reloj);

    }

    protected void setImagenUri(String rutaFoto, int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageUri(rutaFoto,imagen,drawable);

    }

    protected void setImagenUri(String rutaFoto, ImageView imagen, int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageUri(rutaFoto,imagen,drawable);

    }

    protected void setImagenUri(String rutaFoto, ImageView imagen){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageUri(rutaFoto,imagen, R.drawable.ico_calendario_reloj);

    }

    protected void setImagenFireStore(String rutaFoto, int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageFireStore(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStore(String rutaFoto, ImageView imagen,int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageFireStore(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStore(String rutaFoto, ImageView imagen){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageFireStore(rutaFoto,imagen,R.drawable.ico_calendario_reloj);

    }

    protected void setImagenUriCircle(String rutaFoto){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageUriCircle(rutaFoto,imagen, R.drawable.ico_calendario_reloj);

    }

    protected void setImagenUriCircle(String rutaFoto, int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageUriCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenUriCircle(String rutaFoto, ImageView imagen, int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageUriCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenUriCircle(String rutaFoto, ImageView imagen){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageUriCircle(rutaFoto,imagen, R.drawable.ico_calendario_reloj);

    }

    protected void setImagenFireStoreCircle(String rutaFoto, int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStoreCircle(String rutaFoto, ImageView imagen,int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStoreCircle(String rutaFoto, ImageView imagen){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,R.drawable.ico_calendario_reloj);

    }

}
