package jjlacode.com.androidutils;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.IOException;

public abstract class FragmentCUD extends FragmentBaseCRUD implements JavaUtil.Constantes{

    protected Button btnsave;
    protected Button btnback;
    protected Button btndelete;
    protected ImageView imagen;

    protected String path;
    protected ImagenUtil imagenUtil;
    protected ContentValues valores;
    final protected int COD_FOTO = 10;
    final protected int COD_SELECCIONA = 20;

    public FragmentCUD() {
    }

    @Override
    public void onResume() {

        super.onResume();

        if (id != null) {

                setDatos();

            } else {

                setNuevo();

            }

        acciones();

    }

    protected void acciones(){

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cambiarFragment();
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delete();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id !=null) {

                    update();

                }else{

                    registrar();

                }
            }
        });

    }

    protected abstract void setNuevo();

    protected void delete(){

        Toast.makeText(getContext(),"Registro borrado", Toast.LENGTH_SHORT).show();
        cambiarFragment();
    }

    protected void registrar(){

        valores = new ContentValues();

        setContenedor();

    }

    protected abstract void setContenedor();

    protected void cambiarFragment(){

        enviarBundle();
        setcambioFragment();
    }

    protected abstract void setcambioFragment();

    protected void update(){

        valores = new ContentValues();

        setContenedor();

    }


    protected void mostrarDialogoOpcionesImagen(final Context contexto) {

        final CharSequence[] opciones = {"Hacer foto desde cámara",
                "Elegir de la galería", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                imagenUtil = new ImagenUtil(contexto);


                if (opciones[which].equals("Hacer foto desde cámara")) {

                    path = null;
                    try {
                        startActivityForResult(imagenUtil.takePhotoIntent(), COD_FOTO);
                        imagenUtil.addToGallery();

                    } catch (IOException e) {
                        Log.e ("DialogoOpcionesImagen",e.toString());
                    }

                } else if (opciones[which].equals("Elegir de la galería")) {

                    path = null;

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

        if (requestCode>0) {

            switch (requestCode) {

                case COD_SELECCIONA:
                    imagenUtil.setPhotoUri(data.getData());
                    path = imagenUtil.getPath();
                    if (id!=null){update();}
                    setImagenUri(path);

                    break;
                case COD_FOTO:

                    path = imagenUtil.getPath();
                    if (id!=null){update();}
                    setImagenUri(path);

                    break;

            }
        }
    }

    protected void setImagenUri(String rutaFoto){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageUri(rutaFoto,imagen,R.drawable.ico_calendario_reloj);

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
        imagenUtil.setImageUri(rutaFoto,imagen,R.drawable.ico_calendario_reloj);

    }

    protected void setImagenFireStore(String rutaFoto){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageFireStore(rutaFoto,imagen);

    }

    protected void setImagenFireStore(String rutaFoto, ImageView imagen){

        ImagenUtil imagenUtil = new ImagenUtil(getContext());
        imagenUtil.setImageFireStore(rutaFoto,imagen);

    }

}
