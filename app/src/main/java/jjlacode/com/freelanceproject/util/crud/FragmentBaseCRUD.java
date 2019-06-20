package jjlacode.com.freelanceproject.util.crud;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.io.IOException;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.util.android.AndroidUtil;
import jjlacode.com.freelanceproject.util.android.FragmentBase;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.media.MediaUtil;

public abstract class FragmentBaseCRUD extends FragmentBase implements JavaUtil.Constantes {


    protected int tituloPlural;
    protected int tituloSingular;
    protected int tituloNuevo;
    protected String tabla;
    protected String[] campos;
    protected String id;
    protected String idAOrigen;
    protected int secuencia;
    protected String tablaCab;
    protected Context contexto;
    protected Modelo modelo;
    protected String campoID;
    protected String campoSecuencia;
    protected String campoImagen;
    protected String campoTimeStamp;

    protected ConsultaBD consulta = new ConsultaBD();
    protected ContentValues valores;
    protected ListaModelo lista;
    protected ListaModelo listab;
    protected String origen;
    protected String actual;
    protected String actualtemp;
    protected String subTitulo;
    protected boolean nuevo;

    protected RelativeLayout frPrincipal;
    protected LinearLayout frdetalle;
    protected LinearLayout frPie;
    protected LinearLayout frCabecera;
    protected View viewCabecera;
    protected View viewCuerpo;
    protected View viewBotones;
    protected int layoutCuerpo;
    protected int layoutCabecera;

    protected ImageButton btnsave;
    protected ImageButton btnback;
    protected ImageButton btndelete;
    protected ImageView imagen;


    protected String path;
    protected MediaUtil mediaUtil = new MediaUtil(contexto);
    final protected int COD_FOTO = 10;
    final protected int COD_SELECCIONA = 20;

    public FragmentBaseCRUD() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.contenido, container, false);
        land = getResources().getBoolean(R.bool.esLand);
        tablet = getResources().getBoolean(R.bool.esTablet);
        System.out.println("land = " + land);
        System.out.println("tablet = " + tablet);

        frPrincipal = view.findViewById(R.id.contenedor);
        frdetalle = view.findViewById(R.id.layout_detalle);

        frPie = view.findViewById(R.id.layout_pie);
        viewBotones = inflater.inflate(R.layout.btn_sdb,container,false);
        viewCuerpo = inflater.inflate(layoutCuerpo,container,false);
        frCabecera = view.findViewById(R.id.layout_cabecera);
        if (layoutCabecera>0) {
            viewCabecera = inflater.inflate(layoutCabecera, container,false);
            if(viewCabecera.getParent() != null) {
                ((ViewGroup)viewCabecera.getParent()).removeView(viewCabecera); // <- fix
            }
            if (viewCabecera!=null) {
                frCabecera.addView(viewCabecera);
            }
        }
        if(viewBotones.getParent() != null) {
            ((ViewGroup)viewBotones.getParent()).removeView(viewBotones); // <- fix
        }
        if(viewCuerpo.getParent() != null) {
            ((ViewGroup)viewCuerpo.getParent()).removeView(viewCuerpo); // <- fix
        }


        frdetalle.addView(viewCuerpo);
        frPie.addView(viewBotones);

        btnback = view.findViewById(R.id.btn_back);
        btnsave = view.findViewById(R.id.btn_save);
        btndelete = view.findViewById(R.id.btn_del);

        setInicio();

        AndroidUtil.ocultarTeclado(activityBase, view);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences persistencia=getActivity().getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=persistencia.edit();
        editor.putString(ORIGEN, origen);
        editor.putString(ACTUAL, actual);
        editor.putString(ACTUALTEMP, actualtemp);
        editor.putString(SUBTITULO, subTitulo);
        editor.putString(ID,id);
        editor.putInt(SECUENCIA,secuencia);
        editor.apply();
        System.out.println("Guardado onPause");
        System.out.println("id = " + id);
    }


    protected boolean onUpdate(){

        return false;
    }

    /**
     * Carga el Bundle de entrada de la transisión entre Fragments ó Activity
     */
    protected void cargarBundle(){

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
                id = bundle.getString(ID);
            }
            if (secuencia==0) {
                secuencia = bundle.getInt(SECUENCIA);
            }

        }

        setBundle();
    }

    protected void setTabla(){

    }

    protected void setTablaCab(){

        tablaCab = ContratoPry.getTabCab(tabla);

    }

    protected void setContext(){

        contexto = activityBase;

    }

    protected void setCampos(){

        campos = ContratoPry.obtenerCampos(tabla);

    }

    protected void setCampoID(){

            campoID = campos[2];
            campoSecuencia = SECUENCIA;

    }

    protected void setBundle(){

    }

    protected abstract void setDatos();

    protected void setAcciones(){

    }

    protected abstract void setTitulo();

    protected void datos(){

        setImagen();
        setDatos();
    }

    protected void setImagen(){

        try {

            if (modelo==null && id!=null){
                modelo = CRUDutil.setModelo(campos, id);
            }

            if (modelo!=null && modelo.getString(campoImagen) != null) {

                path = modelo.getString(campoImagen);

            }

            if (path != null) {

                setImagenUri(contexto, path);

            }else {

                try {

                    imagen.setImageResource(R.drawable.ic_add_a_photo_black_24dp);

                }catch (Exception er){
                    er.printStackTrace();
                }
            }

        }catch (Exception e){

            e.printStackTrace();

            if (path != null) {

                setImagenUri(contexto, path);

            }else {

                try {

                    imagen.setImageResource(R.drawable.ic_add_a_photo_black_24dp);

                }catch (Exception er){
                    er.printStackTrace();
                }
            }
        }

    }

    protected void acciones(){

        setAcciones();

    }

    protected void enviarAct(){

        icFragmentos.enviarBundleAActivity(bundle);
        System.out.println("bundle enviado a activityBase desde "+ getString(tituloPlural));
        System.out.println("bundle = " + bundle);
    }

    protected void enviarBundle(){

        bundle.putString(ORIGEN, origen);
        bundle.putString(ACTUAL,actual);
        bundle.putString(ACTUALTEMP, actualtemp);
        bundle.putSerializable(MODELO,modelo);
        bundle.putString(ID,id);
        bundle.putString(SUBTITULO,subTitulo);
        bundle.putInt(SECUENCIA, secuencia);

    }

    @Override
    public void onResume() {

        super.onResume();

        cargarBundle();

        if (bundle.containsKey(PERSISTENCIA) && bundle.getBoolean(PERSISTENCIA)) {

            SharedPreferences persistencia = getActivity().getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = persistencia.edit();
            if (id == null) {
                id = persistencia.getString(ID, null);
            }
            if (secuencia == 0) {
                secuencia = persistencia.getInt(SECUENCIA, 0);
            }

            editor.putString(ID, null);
            editor.putInt(SECUENCIA, 0);
            editor.apply();
        }

        System.out.println("onResume id = " + id);

        setTabla();
        setTablaCab();
        setCampos();
        setCampoID();
        setContext();
        setTitulo();

        campoTimeStamp = TIMESTAMP;

        campoImagen = "rutafoto_"+ tabla;

        if (tablaCab==null && id!=null){
            modelo = CRUDutil.setModelo(campos,id);
        }
        else if (id!=null && secuencia>0){
            modelo = CRUDutil.setModelo(campos,id,secuencia);
        }

        System.out.println("onResume modelo = " + modelo);

        if (tituloPlural>0) {
            activityBase.toolbar.setTitle(tituloPlural);
        }
        if (subTitulo!=null) {
            activityBase.toolbar.setSubtitle(subTitulo);
        }

        enviarBundle();
        enviarAct();

    }

    protected void mostrarDialogoOpcionesImagen(final Context contexto) {

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
                        onUpdate();
                        startActivityForResult(mediaUtil.takePhotoIntent(), COD_FOTO);
                        mediaUtil.addPhotoToGallery();
                        path = mediaUtil.getPath(mediaUtil.getPhotoUri());
                        CRUDutil.setSharePreference(contexto,PERSISTENCIA,PATH,path);
                        onUpdate();

                    } catch (IOException e) {
                        Log.e("DialogoOpcionesImagen", e.toString());
                    }

                } else if (opciones[which].equals("Elegir de la galería")) {

                    onUpdate();
                    startActivityForResult(mediaUtil.openGalleryIntent(), COD_SELECCIONA);

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

        mediaUtil = new MediaUtil(contexto);

        if (requestCode>0) {

            switch (requestCode) {

                case COD_FOTO:

                    path = CRUDutil.getSharePreference(contexto,PERSISTENCIA,PATH,path);

                case COD_SELECCIONA:

                    if (data.getData()!=null) {
                        path = mediaUtil.getPath(data.getData());
                    }
                    onUpdate();
                    break;

                case AUDIORECORD:

                    if (data.getData()!=null) {
                        path = mediaUtil.getPath(data.getData());
                    }
                    onUpdate();

            }
        }
    }

    protected void visibleSoloBtnBack() {
        visible(frPie);
        gone(btnsave);
        gone(btndelete);
        visible(btnback);
    }

    protected void visibleSoloBtnSave() {
        visible(frPie);
        gone(btnback);
        gone(btndelete);
        visible(btnsave);
    }

    protected void visibleSoloBtnDel() {
        visible(frPie);
        gone(btnsave);
        gone(btnback);
        visible(btndelete);
    }

    protected void visibleBtnBackSave() {
        visible(frPie);
        visible(btnsave);
        gone(btndelete);
        visible(btnback);
    }

    protected void visibleBtnSaveDel() {
        visible(frPie);
        gone(btnback);
        visible(btndelete);
        visible(btnsave);
    }

    protected void visiblePie() {
        visible(frPie);
        visible(btnsave);
        visible(btndelete);
        visible(btnback);
    }

    protected void gonePie() {
        gone(frPie);
    }

    protected void imagenMediaPantalla(){

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

    protected void imagenMediaPantalla(ImageView imagen){

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

    protected void imagenPantalla(ImageView imagen, int falto, int fancho){

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

    protected boolean onDelete(){
        return true;
    }

    protected void mostrarDialogDelete(){

        final CharSequence[] opciones = {getString(R.string.confirmar_borrado),getString(R.string.cancelar)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.borrar));
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals(getString(R.string.confirmar_borrado))){

                    onDelete();

                }else{
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }




}
