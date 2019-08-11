package com.jjlacode.base.util.crud;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.android.AndroidUtil;
import com.jjlacode.base.util.android.FragmentBase;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.base.util.media.MediaUtil;
import com.jjlacode.base.util.sqlite.ConsultaBD;
import com.jjlacode.freelanceproject.CommonPry;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.sqlite.ContratoPry;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;
import static com.jjlacode.base.util.sqlite.ConsultaBD.putDato;

public abstract class FragmentBaseCRUD extends FragmentBase implements ContratoPry.Tablas, CommonPry.Constantes {


    protected int tituloPlural;
    protected int tituloSingular;
    protected int tituloNuevo;
    protected String tabla;
    protected String[] campos;
    protected String id;
    protected int secuencia;
    protected String tablaCab;
    protected Modelo modelo;
    protected String campoID;
    protected String campoSecuencia;
    protected String campoImagen;
    protected String campoTimeStamp;
    protected String campoCreate;

    protected ContentValues valores;
    protected ListaModelo lista;
    protected ListaModelo listab;
    protected boolean nuevo;

    protected ImageView imagen;


    protected String path;
    protected MediaUtil mediaUtil = new MediaUtil(contexto);
    final protected int COD_FOTO = 10;
    final protected int COD_SELECCIONA = 20;
    protected boolean back;
    private int secuenciatemp;
    private String idtemp;

    public FragmentBaseCRUD() {
    }

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container){
        super.setOnCreateView(view,inflater,container);
        Log.d(TAG, getMetodo());

        btnback = view.findViewById(R.id.btn_back);
        btnsave = view.findViewById(R.id.btn_save);
        btndelete = view.findViewById(R.id.btn_del);
        System.out.println("view = " + view);

    }

    @Override
    protected void setLayoutExtra() {
        super.setLayoutExtra();
        Log.d(TAG, getMetodo());

        layoutPie = R.layout.btn_sdb;
        setTabla();
        setTablaCab();
        setCampos();
        setCampoID();
        setContext();
        setTitulo();

        campoTimeStamp = CAMPO_TIMESTAMP;

        campoCreate = CAMPO_CREATEREG;

        campoImagen = CAMPO_RUTAFOTO;



    }


    @Override
    protected void setTAG() {
        TAG = getClass().getSimpleName();
    }

    protected boolean onUpdate(){
        Log.d(TAG, getMetodo());

        return false;
    }

    /**
     * Carga el Bundle de entrada de la transisión entre Fragments ó Activity
     */
    protected void cargarBundle(){
        Log.d(TAG, getMetodo());

        super.cargarBundle();

        listab = null;

        if (bundle != null) {
            origen = bundle.getString(ORIGEN);
            subTitulo = bundle.getString(SUBTITULO);
            actual = bundle.getString(ACTUAL);
            actualtemp = bundle.getString(ACTUALTEMP);
            nuevo = bundle.getBoolean(NUEVOREGISTRO);
            if (subTitulo==null){
                subTitulo = CommonPry.setNamefdef();
            }
            listab = (ListaModelo) bundle.getSerializable(LISTA);
            modelo = (Modelo) bundle.getSerializable(MODELO);
            if (id==null) {
                id = bundle.getString(CAMPO_ID);
                idtemp = id;
            }
            if (secuencia==0) {
                secuencia = bundle.getInt(CAMPO_SECUENCIA);
                secuenciatemp = secuencia;
            }

            setBundle();

        }
    }

    protected abstract void setTabla();

    protected abstract void setTablaCab();

    protected abstract void setCampos();

    protected void setCampoID(){
        Log.d(TAG, getMetodo());

        campoID = campos[2];
            campoSecuencia = CAMPO_SECUENCIA;

    }

    protected void setBundle(){
        Log.d(TAG, getMetodo());

    }

    protected abstract void setDatos();

    protected void setAcciones(){
        Log.d(TAG, getMetodo());

    }

    protected abstract void setTitulo();

    protected void datos(){
        Log.d(TAG, getMetodo());

        setImagen();
        try {
            if (tablaCab==null) {

                id = modelo.getString(campoID);
                modelo = CRUDutil.setModelo(campos, id);

            }else {

                id = modelo.getString(campoID);
                secuencia = modelo.getInt(campoSecuencia);
                modelo = CRUDutil.setModelo(campos, id, secuencia);

            }
            setDatos();
            obtenerDatosEdit();
        }catch (Exception e) {

            e.printStackTrace();

        }
    }

    protected void setImagen(){
        Log.d(TAG, getMetodo());


        try {

            if (modelo == null && id != null) {
                modelo = CRUDutil.setModelo(campos, id);
            }

            if (modelo != null && modelo.getString(campoImagen) != null) {

                path = modelo.getString(campoImagen);

            }

            if (path != null) {

                setImagenUri(contexto, path);

            } else {

                try {

                    imagen.setImageResource(R.drawable.ic_add_a_photo_black_24dp);

                } catch (Exception er) {
                    er.printStackTrace();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            if (path != null) {

                setImagenUri(contexto, path);

            } else {

                try {

                    imagen.setImageResource(R.drawable.ic_add_a_photo_black_24dp);

                } catch (Exception er) {
                    er.printStackTrace();
                }
            }
        }
    }

    protected void acciones(){
        Log.d(TAG, getMetodo());
        super.acciones();

        for (final EditMaterial editMaterial : materialEdits) {
            editMaterial.setCambioFocoListener(new EditMaterial.CambioFocoEdit() {
                @Override
                public void alPerderFoco(View view) {
                    alCambiarCampos(editMaterial);
                    if (editMaterial.getNextFocusDownId() == 0) {
                        AndroidUtil.ocultarTeclado(contexto, view);
                    }
                }

                @Override
                public void alRecibirFoco(View view) {
                    editMaterial.finalTexto();
                }
            });

            editMaterial.setAlCambiarListener(new EditMaterial.AlCambiarListener() {
                @Override
                public void antesCambio(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void cambiando(CharSequence s, int start, int before, int count) {

                    if (timer != null) {
                        timer.cancel();
                    }
                }

                @Override
                public void despuesCambio(Editable s) {
                    final Editable temp = s;
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (((id != null && (tablaCab == null || secuencia > 0)) || back) && !temp.toString().equals("")) {
                                guardarEdit(editMaterial);
                            }
                        }
                    }, 1000);
                }
            });
        }

        setAcciones();

    }

    @Override
    protected void alCambiarCampos(EditMaterial editMaterial) {
        super.alCambiarCampos(editMaterial);

        if ((id != null && (tablaCab == null || secuencia > 0))) {
            guardarEdit(editMaterial);
        } else {
            if (!back) {
                update();
                if (id != null && (tablaCab == null || secuencia > 0)) {
                    guardarEdit(editMaterial);
                }
            }
        }
    }

    protected void guardarEdit(EditMaterial editMaterial) {
        for (Object o : camposEdit) {
            if (((Map) o).get("materialEdit") == editMaterial) {
                valores = new ContentValues();
                valores.put((String) ((Map) o).get("campoEdit"), editMaterial.getTexto());
                if (secuencia > 0) {
                    int i = ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);
                    System.out.println("guardados = " + i);
                } else {
                    int x = ConsultaBD.updateRegistro(tabla, id, valores);
                    System.out.println("guardados = " + x);
                }
            }
        }
    }

    protected void obtenerDatosEdit() {

        for (EditMaterial materialEdit : materialEdits) {


            for (Object o : camposEdit) {

                if (((Map) o).get("materialEdit") == materialEdit) {
                    materialEdit.setText(modelo.getString((String) ((Map) o).get("campoEdit")));

                }
            }
        }
    }

    protected void enviarAct(){
        Log.d(TAG, getMetodo());

            bundle = new Bundle();
            bundle.putString(ORIGEN, origen);
            bundle.putString(ACTUAL, actual);
            bundle.putString(ACTUALTEMP, actualtemp);
            bundle.putSerializable(MODELO, modelo);
            bundle.putString(CAMPO_ID, id);
            bundle.putString(SUBTITULO, subTitulo);
            bundle.putInt(CAMPO_SECUENCIA, secuencia);
            System.out.println("Enviando bundle a MainActivity");
            icFragmentos.enviarBundleAActivity(bundle);
    }

    protected void enviarBundle(){
        Log.d(TAG, getMetodo());

            bundle = new Bundle();
            bundle.putString(ORIGEN, actual);
            bundle.putString(ACTUAL, destino);
            bundle.putString(ACTUALTEMP, actualtemp);
            bundle.putSerializable(MODELO, modelo);
            bundle.putString(CAMPO_ID, id);
            bundle.putString(SUBTITULO, subTitulo);
            bundle.putInt(CAMPO_SECUENCIA, secuencia);

    }

    @Override
    public void onConfigurationChanged(Configuration myConfig) {
        super.onConfigurationChanged(myConfig);
        Log.d(TAG, getMetodo());

        int orientation = getResources().getConfiguration().orientation;
        SharedPreferences persistencia=getActivity().getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=persistencia.edit();

        switch(orientation ) {
            case Configuration.ORIENTATION_LANDSCAPE:
                // Con la orientación en horizontal actualizamos el adaptador
                editor.putString(ORIGEN, origen);
                editor.putString(ACTUAL, actual);
                editor.putString(ACTUALTEMP, actualtemp);
                editor.putString(SUBTITULO, subTitulo);
                editor.putString(CAMPO_ID,id);
                editor.putInt(CAMPO_SECUENCIA,secuencia);
                editor.putBoolean(NUEVOREGISTRO,nuevo);
                editor.apply();
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                // Con la orientación en vertical actualizamos el adaptador
                editor.putString(ORIGEN, origen);
                editor.putString(ACTUAL, actual);
                editor.putString(ACTUALTEMP, actualtemp);
                editor.putString(SUBTITULO, subTitulo);
                editor.putString(CAMPO_ID,id);
                editor.putInt(CAMPO_SECUENCIA,secuencia);
                editor.putBoolean(NUEVOREGISTRO,nuevo);
                editor.apply();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, getMetodo());
        //cargarBundle();

        if (bundle!=null && bundle.containsKey(PERSISTENCIA) && bundle.getBoolean(PERSISTENCIA)) {

            SharedPreferences persistencia = getActivity().getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = persistencia.edit();
            if (id == null) {
                id = persistencia.getString(CAMPO_ID, null);
            }
            if (secuencia == 0) {
                secuencia = persistencia.getInt(CAMPO_SECUENCIA, 0);
            }

            editor.putString(CAMPO_ID, null);
            editor.putInt(CAMPO_SECUENCIA, 0);
            editor.apply();
        }


        if (bundle!=null) {
            enviarBundle();
            enviarAct();
        }

    }

    protected void mostrarDialogoOpcionesImagen(final Context contexto) {
        Log.d(TAG, getMetodo());

        final CharSequence[] opciones = {"Hacer foto desde cámara",
                "Elegir de la galería", "Quitar foto","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mediaUtil = new MediaUtil(contexto);


                if (opciones[which].equals("Hacer foto desde cámara")) {

                    try {
                        if (onUpdate()) {
                            startActivityForResult(mediaUtil.takePhotoIntent(), COD_FOTO);
                            mediaUtil.addPhotoToGallery();
                            path = mediaUtil.getPath(mediaUtil.getPhotoUri());
                            CRUDutil.setSharePreference(contexto, PERSISTENCIA, PATH, path);
                            onUpdate();
                        }

                    } catch (IOException e) {
                        Log.e("DialogoOpcionesImagen", e.toString());
                    }

                } else if (opciones[which].equals("Elegir de la galería")) {

                    if (onUpdate()) {
                        startActivityForResult(mediaUtil.openGalleryIntent(), COD_SELECCIONA);
                    }

                }else if (opciones[which].equals("Quitar foto")) {

                    path = null;
                    onUpdate();

                }else {
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

                    path = CRUDutil.getSharePreference(contexto,PERSISTENCIA,PATH,path);

                case COD_SELECCIONA:

                    if (data!=null && data.getData()!=null) {
                        path = mediaUtil.getPath(data.getData());
                    }
                    onUpdate();
                    break;

                case AUDIORECORD:

                    if (data.getData()!=null) {
                        path = mediaUtil.getPath(data.getData());
                    }
                    onUpdate();
                    break;


            }
        }
    }

    protected void visibleSoloBtnBack() {
        Log.d(TAG, getMetodo());
        visible(frPie);
        gone(btnsave);
        gone(btndelete);
        visible(btnback);
    }

    protected void visibleSoloBtnSave() {
        Log.d(TAG, getMetodo());
        visible(frPie);
        gone(btnback);
        gone(btndelete);
        visible(btnsave);
    }

    protected void visibleSoloBtnDel() {
        Log.d(TAG, getMetodo());
        visible(frPie);
        gone(btnsave);
        gone(btnback);
        visible(btndelete);
    }

    protected void visibleBtnBackSave() {
        Log.d(TAG, getMetodo());
        visible(frPie);
        visible(btnsave);
        gone(btndelete);
        visible(btnback);
    }

    protected void visibleBtnSaveDel() {
        Log.d(TAG, getMetodo());
        visible(frPie);
        gone(btnback);
        visible(btndelete);
        visible(btnsave);
    }

    protected void visiblePie() {
        Log.d(TAG, getMetodo());
        visible(frPie);
        visible(btnsave);
        visible(btndelete);
        visible(btnback);
    }

    protected void gonePie() {
        Log.d(TAG, getMetodo());
        gone(frPie);
    }

    protected void imagenMediaPantalla(){
        Log.d(TAG, getMetodo());

        if (!land) {
            imagen.setMinimumHeight((int) ((double) alto / 2));
            imagen.setMinimumWidth(ancho);
            imagen.setMaxHeight((int) ((double) alto / 2));
            imagen.setMaxWidth(ancho);
        }else{
            imagen.setMinimumWidth((int) ((double) ancho / 2));
            imagen.setMinimumHeight((int) ((double) alto / 2));
            imagen.setMaxWidth((int) ((double) ancho / 2));
            imagen.setMaxHeight((int) ((double) alto / 2));

        }

    }

    protected void imagenPantalla(int falto, int fancho){
        Log.d(TAG, getMetodo());

        if (!land) {
            imagen.setMinimumHeight((int) ((double) alto / falto));
            imagen.setMinimumWidth((int) ((double)ancho/fancho));
            imagen.setMaxHeight((int) ((double) alto / falto));
            imagen.setMaxWidth((int) ((double)ancho/fancho));
        }else{
            imagen.setMinimumWidth((int) ((double) ancho / (falto+fancho)));
            imagen.setMinimumHeight((int) ((double) alto / falto));
            imagen.setMaxWidth((int) ((double) ancho / (falto+fancho)));
            imagen.setMaxHeight((int) ((double) alto / falto));

        }

    }



    protected void setImagenUri(Context contexto, String rutaFoto){

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUri(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUri(MediaUtil imagenUtil, String rutaFoto){

        imagenUtil.setImageUri(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUri(Context contexto, String rutaFoto, int drawable){

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUri(rutaFoto,imagen,drawable);

    }

    protected void setImagenUri(MediaUtil imagenUtil, String rutaFoto, int drawable){

        imagenUtil.setImageUri(rutaFoto,imagen,drawable);

    }

    protected void setImagenUri(Context contexto, String rutaFoto, ImageView imagen, int drawable){

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUri(rutaFoto,imagen,drawable);

    }

    protected void setImagenUri(MediaUtil imagenUtil, String rutaFoto, ImageView imagen, int drawable){

        imagenUtil.setImageUri(rutaFoto,imagen,drawable);

    }

    protected void setImagenUri(Context contexto, String rutaFoto, ImageView imagen){

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUri(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUri(MediaUtil imagenUtil, String rutaFoto, ImageView imagen){

        imagenUtil.setImageUri(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStore(Context contexto, String rutaFoto, int drawable){

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStore(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStore(MediaUtil imagenUtil, String rutaFoto, int drawable){

        imagenUtil.setImageFireStore(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStore(Context contexto, String rutaFoto, ImageView imagen,int drawable){

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStore(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStore(MediaUtil imagenUtil, String rutaFoto, ImageView imagen, int drawable){

        imagenUtil.setImageFireStore(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStore(Context contexto, String rutaFoto, ImageView imagen){

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStore(rutaFoto,imagen,R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStore(MediaUtil imagenUtil, String rutaFoto, ImageView imagen){

        imagenUtil.setImageFireStore(rutaFoto,imagen,R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUriCircle(Context contexto, String rutaFoto){

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUriCircle(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUriCircle(MediaUtil imagenUtil, String rutaFoto){

        imagenUtil.setImageUriCircle(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUriCircle(Context contexto, String rutaFoto, int drawable){

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUriCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenUriCircle(MediaUtil imagenUtil, String rutaFoto, int drawable){

        imagenUtil.setImageUriCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenUriCircle(Context contexto, String rutaFoto, ImageView imagen, int drawable){

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUriCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenUriCircle(MediaUtil imagenUtil, String rutaFoto, ImageView imagen, int drawable){

        imagenUtil.setImageUriCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenUriCircle(Context contexto, String rutaFoto, ImageView imagen){

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUriCircle(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenUriCircle(MediaUtil imagenUtil, String rutaFoto, ImageView imagen){

        imagenUtil.setImageUriCircle(rutaFoto,imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStoreCircle(Context contexto, String rutaFoto, int drawable){

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStoreCircle(MediaUtil imagenUtil, String rutaFoto, int drawable){

        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStoreCircle(Context contexto, String rutaFoto, ImageView imagen,int drawable){

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStoreCircle(MediaUtil imagenUtil, String rutaFoto, ImageView imagen, int drawable){

        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,drawable);

    }

    protected void setImagenFireStoreCircle(Context contexto, String rutaFoto, ImageView imagen){

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStoreCircle(MediaUtil imagenUtil, String rutaFoto, ImageView imagen){

        imagenUtil.setImageFireStoreCircle(rutaFoto,imagen,R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setDato(String campo, String valor){

        putDato(valores,campos,campo,valor);
    }

    protected void setDato(String campo, String valor, String tipo){
        Log.d(TAG, getMetodo()+" "+campo);


        switch (tipo){

            case INT:
                putDato(valores,campos,campo, JavaUtil.comprobarInteger(valor));
                break;
            case LONG:
                putDato(valores,campos,campo, JavaUtil.comprobarLong(valor));
                break;
            case DOUBLE:
                putDato(valores,campos,campo, JavaUtil.comprobarDouble(valor));
                break;
            case FLOAT:
                putDato(valores,campos,campo, JavaUtil.comprobarFloat(valor));
                break;
            case SHORT:
                putDato(valores,campos,campo, JavaUtil.comprobarShort(valor));
                break;
            case NONNULL:
                putDato(valores,campos,campo, JavaUtil.noNuloString(valor));

        }
    }

    protected void setDato(String campo, int valor){

        putDato(valores,campos,campo,valor);
    }

    protected void setDato(String campo, long valor){

        putDato(valores,campos,campo,valor);
    }

    protected void setDato(String campo, double valor){

        putDato(valores,campos,campo,valor);
    }

    protected void setDato(String campo, float valor){

        putDato(valores,campos,campo,valor);
    }

    protected void setDato(String campo, short valor){

        putDato(valores,campos,campo,valor);
    }

    protected boolean onDelete(){
        Log.d(TAG, getMetodo());
        return true;
    }

    protected void mostrarDialogDelete(){
        Log.d(TAG, getMetodo());

        String selDelete = null;

        if (nuevo){
            selDelete = getString(R.string.limpiar_formulario);

        }else{
            selDelete = getString(R.string.confirmar_borrado);
        }

        final CharSequence[] opciones = {selDelete,getString(R.string.cancelar)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.borrar));
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals(getString(R.string.limpiar_formulario))){

                    onDelete();

                }else if (opciones[which].equals(getString(R.string.confirmar_borrado))){

                    onDelete();

                }else{
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

}
