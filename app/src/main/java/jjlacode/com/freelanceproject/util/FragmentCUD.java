package jjlacode.com.freelanceproject.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.IOException;

import jjlacode.com.freelanceproject.R;

import static jjlacode.com.freelanceproject.util.CommonPry.permiso;

public abstract class FragmentCUD extends FragmentBaseCRUD implements JavaUtil.Constantes{

    protected Button btnsave;
    protected Button btnback;
    protected Button btndelete;
    protected ImageView imagen;

    protected String path;
    protected ImagenUtil imagenUtil = new ImagenUtil(contexto);
    protected ContentValues valores;
    final protected int COD_FOTO = 10;
    final protected int COD_SELECCIONA = 20;


    public FragmentCUD() {
    }

    @Override
    public void onResume() {

        super.onResume();

        selector();

        acciones();

    }

    protected void selector(){

        if (modelo != null) {

            setDatos();

        } else {

            setNuevo();

        }
    }

    protected boolean onUpdate(){

        if (update()) {
            nuevo = false;
            if(tablaCab!=null){
                namesubclass = namesubtemp;
            }
            enviarAct();
            return true;
        }
        return false;
    }

    protected boolean onDelete(){

        if (delete()){
            if (tablaCab==null) {
                id = null;
                modelo=null;
            }else{
                secuencia = 0;
                modelo=null;
            }
            cambiarFragment();

            return true;
        }

        return false;
    }

    protected boolean onBack(){

        if (nuevo) {
            nuevo = false;
            namesubclass = namesubtemp;
        }
        if (tablaCab==null) {
            id = null;
            modelo=null;

        }else{
            secuencia = 0;
            modelo=null;
        }
        cambiarFragment();

        return true;
    }

    protected void acciones(){

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               onBack();

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onDelete();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onUpdate();

            }
        });

        if (permiso) {
            if (imagen!=null) {
                imagen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onUpdate();
                        mostrarDialogoOpcionesImagen(contexto);
                    }
                });
            }
        }

        setAcciones();

    }



    protected abstract void setNuevo();

    protected boolean delete(){

        if (tablaCab!=null){

            if (consulta.deleteRegistroDetalle(tabla,id,secuencia)>0){
                Toast.makeText(getContext(),"Registro detalle borrado", Toast.LENGTH_SHORT).show();
                return true;
            }
        }else if (consulta.deleteRegistro(tabla,id)>0) {
            Toast.makeText(getContext(),"Registro borrado", Toast.LENGTH_SHORT).show();
            return true;
        }else{

            Toast.makeText(getContext(), "Error al borrar registro", Toast.LENGTH_SHORT).show();
            return false;
        }

        return false;
    }

    protected boolean registrar() {

        valores = new ContentValues();

        setContenedor();

        if (tablaCab != null && secuencia == 0 && modelo == null) {

            secuencia = consulta.secInsertRegistroDetalle(campos, id, tablaCab, valores);

            modelo = consulta.queryObjectDetalle(campos, id, secuencia);

            Toast.makeText(getContext(), "Registro detalle creado", Toast.LENGTH_SHORT).show();
            namesubclass = namesubtemp;
            return true;

        } else if (id == null && modelo == null) {

            id = consulta.idInsertRegistro(tabla, valores);
            modelo = consulta.queryObject(campos, id);
            System.out.println("idcliente registro = " + id);

            Toast.makeText(getContext(), "Registro creado", Toast.LENGTH_SHORT).show();
            return true;

        }

        Toast.makeText(getContext(), "Error al crear registro", Toast.LENGTH_SHORT).show();
        return false;
    }

    protected abstract void setContenedor();

    protected void cambiarFragment(){

        icFragmentos.fabVisible();
        setcambioFragment();
        enviarBundle();
        enviarAct();
    }

    protected abstract void setcambioFragment();

    protected boolean update(){

        valores = new ContentValues();

        setContenedor();

        if (id!=null && (tablaCab==null||secuencia>0)){

            if (tablaCab!=null){

                if (consulta.updateRegistroDetalle(tabla,id,secuencia,valores)>0){

                    modelo = consulta.queryObjectDetalle(campos,id,secuencia);
                    Toast.makeText(getContext(), "Registro detalle guardado", Toast.LENGTH_SHORT).show();
                    return true;
                }

            }else if (consulta.updateRegistro(tabla,id,valores)>0) {

                modelo = consulta.queryObject(campos,id);

                Toast.makeText(getContext(), "Registro guardado", Toast.LENGTH_SHORT).show();
                return true;

            }else{

                Toast.makeText(getContext(), "Error al guardar registro", Toast.LENGTH_SHORT).show();
                return false;

            }

        }else{
            return registrar();
        }

        return false;

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

                    try {
                        startActivityForResult(imagenUtil.takePhotoIntent(), COD_FOTO);
                        imagenUtil.addToGallery();

                    } catch (IOException e) {
                        Log.e ("DialogoOpcionesImagen",e.toString());
                    }

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

        if (requestCode>0) {

            switch (requestCode) {

                case COD_SELECCIONA:
                    imagenUtil.setPhotoUri(data.getData());
                    path = imagenUtil.getPath();
                    onUpdate();
                    setImagenUriCircle(imagenUtil,path);
                    break;

                case COD_FOTO:
                    path = imagenUtil.getPath();
                    onUpdate();
                    setImagenUriCircle(imagenUtil,path);
                    break;

                case AUDIORECORD:

                case VIDEORECORD:

                        path = String.valueOf(data.getData());
                        onUpdate();

            }
        }
    }

    protected void setImagenUri(Context contexto, String rutaFoto){

        ImagenUtil imagenUtil = new ImagenUtil(contexto);
        imagenUtil.setImageUri(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUri(ImagenUtil imagenUtil, String rutaFoto){

        imagenUtil.setImageUri(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUri(Context contexto, String rutaFoto, int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(contexto);
        imagenUtil.setImageUri(rutaFoto,imagen,drawable);

    }

    protected void setImagenUri(ImagenUtil imagenUtil, String rutaFoto, int drawable){

        imagenUtil.setImageUri(rutaFoto,imagen,drawable);

    }

    protected void setImagenUri(Context contexto, String rutaFoto, ImageView imagen, int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(contexto);
        imagenUtil.setImageUri(rutaFoto,imagen,drawable);

    }

    protected void setImagenUri(ImagenUtil imagenUtil, String rutaFoto, ImageView imagen, int drawable){

        imagenUtil.setImageUri(rutaFoto,imagen,drawable);

    }

    protected void setImagenUri(Context contexto, String rutaFoto, ImageView imagen){

        ImagenUtil imagenUtil = new ImagenUtil(contexto);
        imagenUtil.setImageUri(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUri(ImagenUtil imagenUtil, String rutaFoto, ImageView imagen){

        imagenUtil.setImageUri(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStore(Context contexto, String rutaFoto, int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(contexto);
        imagenUtil.setImageFireStore(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStore(ImagenUtil imagenUtil, String rutaFoto, int drawable){

        imagenUtil.setImageFireStore(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStore(Context contexto, String rutaFoto, ImageView imagen,int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(contexto);
        imagenUtil.setImageFireStore(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStore(ImagenUtil imagenUtil, String rutaFoto, ImageView imagen,int drawable){

        imagenUtil.setImageFireStore(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStore(Context contexto, String rutaFoto, ImageView imagen){

        ImagenUtil imagenUtil = new ImagenUtil(contexto);
        imagenUtil.setImageFireStore(rutaFoto,imagen,R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStore(ImagenUtil imagenUtil, String rutaFoto, ImageView imagen){

        imagenUtil.setImageFireStore(rutaFoto,imagen,R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUriCircle(Context contexto, String rutaFoto){

        ImagenUtil imagenUtil = new ImagenUtil(contexto);
        imagenUtil.setImageUriCircle(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUriCircle(ImagenUtil imagenUtil, String rutaFoto){

        imagenUtil.setImageUriCircle(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUriCircle(Context contexto, String rutaFoto, int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(contexto);
        imagenUtil.setImageUriCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenUriCircle(ImagenUtil imagenUtil, String rutaFoto, int drawable){

        imagenUtil.setImageUriCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenUriCircle(Context contexto, String rutaFoto, ImageView imagen, int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(contexto);
        imagenUtil.setImageUriCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenUriCircle(ImagenUtil imagenUtil, String rutaFoto, ImageView imagen, int drawable){

        imagenUtil.setImageUriCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenUriCircle(Context contexto, String rutaFoto, ImageView imagen){

        ImagenUtil imagenUtil = new ImagenUtil(contexto);
        imagenUtil.setImageUriCircle(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUriCircle(ImagenUtil imagenUtil, String rutaFoto, ImageView imagen){

        imagenUtil.setImageUriCircle(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStoreCircle(Context contexto, String rutaFoto, int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(contexto);
        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStoreCircle(ImagenUtil imagenUtil, String rutaFoto, int drawable){

        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStoreCircle(Context contexto, String rutaFoto, ImageView imagen,int drawable){

        ImagenUtil imagenUtil = new ImagenUtil(contexto);
        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStoreCircle(ImagenUtil imagenUtil, String rutaFoto, ImageView imagen,int drawable){

        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStoreCircle(Context contexto, String rutaFoto, ImageView imagen){

        ImagenUtil imagenUtil = new ImagenUtil(contexto);
        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStoreCircle(ImagenUtil imagenUtil, String rutaFoto, ImageView imagen){

        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setDato(String campo, String valor){

        consulta.putDato(valores,campos,campo,valor);
    }

    protected void setDato(String campo, String valor, String tipo){

        switch (tipo){

            case INT:
                consulta.putDato(valores,campos,campo, JavaUtil.comprobarInteger(valor));
                break;
            case LONG:
                consulta.putDato(valores,campos,campo, JavaUtil.comprobarLong(valor));
                break;
            case DOUBLE:
                consulta.putDato(valores,campos,campo, JavaUtil.comprobarDouble(valor));
                break;
            case FLOAT:
                consulta.putDato(valores,campos,campo, JavaUtil.comprobarFloat(valor));
                break;
            case SHORT:
                consulta.putDato(valores,campos,campo, JavaUtil.comprobarShort(valor));
                break;
            case NONNULL:
                consulta.putDato(valores,campos,campo, JavaUtil.noNuloString(valor));

        }
    }

    protected void setDato(String campo, int valor){

        consulta.putDato(valores,campos,campo,valor);
    }

    protected void setDato(String campo, long valor){

        consulta.putDato(valores,campos,campo,valor);
    }

    protected void setDato(String campo, double valor){

        consulta.putDato(valores,campos,campo,valor);
    }

    protected void setDato(String campo, float valor){

        consulta.putDato(valores,campos,campo,valor);
    }

    protected void setDato(String campo, short valor){

        consulta.putDato(valores,campos,campo,valor);
    }

}
