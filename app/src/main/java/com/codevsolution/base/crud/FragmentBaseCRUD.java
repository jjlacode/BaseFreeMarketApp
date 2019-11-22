package com.codevsolution.base.crud;

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

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.android.controls.ViewImagenLayout;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ContratoPry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;
import static com.codevsolution.base.sqlite.ConsultaBD.putDato;

public abstract class FragmentBaseCRUD extends FragmentBase implements ContratoPry.Tablas {


    protected int tituloPlural;
    protected int tituloSingular;
    protected int tituloNuevo;
    protected String tabla;
    protected String[] campos;
    protected String id;
    protected int secuencia;
    protected String tablaCab;
    protected ModeloSQL modeloSQL;
    protected String campoID;
    protected String campoSecuencia;
    protected String campoImagen;
    protected String campoTimeStamp;
    protected String campoCreate;

    protected ContentValues valores;
    protected ListaModeloSQL listab;
    protected boolean nuevo;

    protected ViewImagenLayout imagen;


    protected String path;
    protected MediaUtil mediaUtil = new MediaUtil(contexto);
    final protected int COD_FOTO = 10;
    final protected int COD_SELECCIONA = 20;
    protected boolean back;
    private int secuenciatemp;
    private String idtemp;
    private Editable tempEdit;
    protected String idrelacionado;

    public FragmentBaseCRUD() {
    }

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);
        Log.d(TAG, getMetodo());

        btnback = view.findViewById(getId("btn_back"));
        btnsave = view.findViewById(getId("btn_save"));
        btndelete = view.findViewById(getId("btn_del"));

        if (autoGuardado){
            gone(btnsave);
        }

        //activityBase.fabInicio.hide();

    }

    @Override
    protected void setLayoutExtra() {
        super.setLayoutExtra();
        Log.d(TAG, getMetodo());

        layoutPie = getIdLayout("btn_sdb");
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
    protected String setTAG() {
        return getClass().getSimpleName();
    }

    protected boolean onUpdate() {
        Log.d(TAG, getMetodo());

        return false;
    }

    /**
     * Carga el Bundle de entrada de la transisión entre Fragments ó Activity
     */
    protected void cargarBundle() {
        Log.d(TAG, getMetodo());

        super.cargarBundle();

        listab = null;

        if (bundle != null) {
            origen = bundle.getString(ORIGEN);
            subTitulo = bundle.getString(SUBTITULO);
            actual = bundle.getString(ACTUAL);
            actualtemp = bundle.getString(ACTUALTEMP);
            nuevo = bundle.getBoolean(NUEVOREGISTRO);
            if (subTitulo == null) {
                subTitulo = getString(tituloPlural);
            }
            listab = (ListaModeloSQL) bundle.getSerializable(LISTA);

            modeloSQL = (ModeloSQL) bundle.getSerializable(MODELO);
            if (id == null) {
                id = bundle.getString(CAMPO_ID);
                idtemp = id;
            }
            if (secuencia == 0) {
                secuencia = bundle.getInt(CAMPO_SECUENCIA);
                secuenciatemp = secuencia;
            }

            setBundle();

        }
    }

    protected abstract void setTabla();

    protected abstract void setTablaCab();

    protected abstract void setCampos();

    protected void setCampoID() {
        Log.d(TAG, getMetodo());

        campoID = campos[2];
        campoSecuencia = CAMPO_SECUENCIA;

    }

    protected void setBundle() {
        Log.d(TAG, getMetodo());

    }

    protected abstract void setDatos();

    protected void setAcciones() {
        Log.d(TAG, getMetodo());

    }

    protected abstract void setTitulo();

    protected void datos() {
        Log.d(TAG, getMetodo());

        if (origen == null) {
            origen = tabla;
        }

        setImagen();
        try {
            if (tablaCab == null) {

                if (nn(modeloSQL)) {
                    id = modeloSQL.getString(campoID);
                }
                if (nn(id)) {
                    modeloSQL = CRUDutil.updateModelo(campos, id);
                }

            } else {

                if (nn(modeloSQL)) {
                    id = modeloSQL.getString(campoID);
                    secuencia = modeloSQL.getInt(campoSecuencia);
                }
                if (nn(id) && secuencia > 0) {
                    modeloSQL = CRUDutil.updateModelo(campos, id, secuencia);
                }
                System.out.println("modeloSQL = " + modeloSQL);
            }
            setDatos();
            obtenerDatosEdit();
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    protected void setImagen() {
        Log.d(TAG, getMetodo());

        try {

            if (modeloSQL == null && id != null && tablaCab == null) {
                modeloSQL = CRUDutil.updateModelo(campos, id);
            } else if (modeloSQL == null && id != null && secuencia > 0) {
                modeloSQL = CRUDutil.updateModelo(campos, id, secuencia);
            }

            if (modeloSQL != null && modeloSQL.getString(campoImagen) != null) {

                path = modeloSQL.getString(campoImagen);

            }

            if (nnn(path)) {

                System.out.println("path = " + path);
                //imagen.setImageUri(path, (int)(anchoReal*0.25f),(int)(altoReal*0.15f));
                imagen.setImageUriPerfil(activityBase, path);
                imagen.setIcfragmentos(icFragmentos);
                imagen.setVisibleBtn();
                imagen.getLinearLayoutCompat().setFocusable(false);

            } else {

                try {

                    imagen.setImageResource(getIdDrawable("logo"), (int) (anchoReal * 0.25f), (int) (altoReal * 0.15f));
                    imagen.setGoneBtn();
                    imagen.getLinearLayoutCompat().setFocusable(false);

                } catch (Exception er) {
                    er.printStackTrace();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            if (path != null) {

                //imagen.setImageUri(path, (int)(anchoReal*0.25f),(int)(altoReal*0.15f));
                imagen.setImageUriPerfil(activityBase, path);
                imagen.setIcfragmentos(icFragmentos);
                imagen.setVisibleBtn();
                imagen.getLinearLayoutCompat().setFocusable(false);



            } else {

                try {

                    imagen.setImageResource(getIdDrawable("logo"), (int) (anchoReal * 0.25f), (int) (altoReal * 0.15f));
                    imagen.setGoneBtn();
                    imagen.getLinearLayoutCompat().setFocusable(false);


                } catch (Exception er) {
                    er.printStackTrace();
                }
            }
        }
    }

    protected void setImagen(ImagenLayout imagen, String campoImagen) {
        Log.d(TAG, getMetodo());

        try {

            if (modeloSQL == null && id != null && tablaCab == null) {
                modeloSQL = CRUDutil.updateModelo(campos, id);
            } else if (modeloSQL == null && id != null && secuencia > 0) {
                modeloSQL = CRUDutil.updateModelo(campos, id, secuencia);
            }

            if (modeloSQL != null && modeloSQL.getString(campoImagen) != null) {

                path = modeloSQL.getString(campoImagen);

            }

            if (nnn(path)) {

                System.out.println("path = " + path);
                //imagen.setImageUri(path, (int)(anchoReal*0.25f),(int)(altoReal*0.15f));
                imagen.setImageUriPerfil(activityBase, path);
                imagen.setIcfragmentos(icFragmentos);
                imagen.setVisibleBtn();
                imagen.setFocusable(false);

            } else {

                try {

                    imagen.setImageResource(getIdDrawable("logo"), (int) (anchoReal * 0.25f), (int) (altoReal * 0.15f));
                    imagen.setGoneBtn();
                    imagen.setFocusable(false);

                } catch (Exception er) {
                    er.printStackTrace();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            if (path != null) {

                //imagen.setImageUri(path, (int)(anchoReal*0.25f),(int)(altoReal*0.15f));
                imagen.setImageUriPerfil(activityBase, path);
                imagen.setIcfragmentos(icFragmentos);
                imagen.setVisibleBtn();
                imagen.setFocusable(false);


            } else {

                try {

                    imagen.setImageResource(getIdDrawable("logo"), (int) (anchoReal * 0.25f), (int) (altoReal * 0.15f));
                    imagen.setGoneBtn();
                    imagen.setFocusable(false);


                } catch (Exception er) {
                    er.printStackTrace();
                }
            }
        }
    }

    protected void acciones() {
        Log.d(TAG, getMetodo());
        super.acciones();

        for (final EditMaterial editMaterial : materialEdits) {
            editMaterial.setCambioFocoListener(new EditMaterial.CambioFocoEdit() {
                @Override
                public void alPerderFoco(View view) {
                    if (editMaterial.getNextFocusDownId() == 0) {
                        AndroidUtil.ocultarTeclado(contexto, view);
                    }
                }

                @Override
                public void alRecibirFoco(View view) {
                    editMaterial.finalTexto();
                }
            });

            if (autoGuardado) {
                editMaterial.setAlCambiarListener(new EditMaterial.AlCambiarListener() {
                    @Override
                    public void antesCambio(CharSequence s, int start, int count, int after) {

                        if (start == 0) {
                            tempEdit = editMaterial.getText();
                        }
                    }

                    @Override
                    public void cambiando(CharSequence s, int start, int before, int count) {

                        if (timer != null) {
                            timer.cancel();
                        }
                    }

                    @Override
                    public void despuesCambio(Editable s) {
                        if (!s.toString().equals("") && s != tempEdit) {
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    activityBase.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            alCambiarCampos(editMaterial);
                                        }
                                    });
                                }
                            }, tiempoGuardado * 1000);
                        }
                    }
                });
            }
        }

        for (final EditMaterialLayout editMaterial : materialEditLayouts) {
            editMaterial.setCambioFocoListener(new EditMaterialLayout.CambioFocoEdit() {
                @Override
                public void alPerderFoco(View view) {
                    if (editMaterial.getLinearLayout().getNextFocusDownId() == 0) {
                        AndroidUtil.ocultarTeclado(contexto, view);
                    }
                }

                @Override
                public void alRecibirFoco(View view) {
                    editMaterial.finalTexto();
                }
            });

            if (autoGuardado) {
                editMaterial.setAlCambiarListener(new EditMaterialLayout.AlCambiarListener() {
                    private int contDes;
                    private int contAnt;

                    @Override
                    public void antesCambio(CharSequence s, int start, int count, int after) {

                        contAnt = count;

                    }

                    @Override
                    public void cambiando(CharSequence s, int start, int before, int count) {

                        if (timer != null) {
                            timer.cancel();
                        }
                        contDes = count;
                    }

                    @Override
                    public void despuesCambio(Editable s) {
                        System.out.println("s.toString() = " + s.toString());
                        if (!s.toString().equals("") && contAnt != contDes) {
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    activityBase.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            alCambiarCampos(editMaterial);
                                        }
                                    });
                                }
                            }, tiempoGuardado * 1000);
                        }
                    }
                });
            }
        }

        setAcciones();

    }

    protected boolean setBack() {
        return back;
    }

    @Override
    protected void alCambiarCampos(EditMaterial editMaterial) {
        super.alCambiarCampos(editMaterial);

        if ((id != null && (tablaCab == null || secuencia > 0))) {
            guardarEdit(editMaterial);
        } else {

            if (!setBack()) {
                onUpdate();
                if (id != null && (tablaCab == null || secuencia > 0)) {
                    guardarEdit(editMaterial);
                }
            }
        }
        alGuardarCampos();
        alGuardarCampo(editMaterial);

    }

    @Override
    protected void alCambiarCampos(EditMaterialLayout editMaterial) {
        super.alCambiarCampos(editMaterial);

        if ((id != null && (tablaCab == null || secuencia > 0))) {
            guardarEdit(editMaterial);
        } else {
            if (!setBack()) {
                onUpdate();
                if (id != null && (tablaCab == null || secuencia > 0)) {
                    guardarEdit(editMaterial);
                }
            }
        }
        alGuardarCampos();
        alGuardarCampo(editMaterial);

    }

    protected void alGuardarCampos() {

    }

    protected void alGuardarCampo(EditMaterialLayout editMaterialLayout) {

    }

    protected void alGuardarCampo(EditMaterial editMaterialLayout) {

    }

    protected void guardarEdit(EditMaterial editMaterial) {

        //if (editMaterial.getValido()) {
        for (Object o : camposEdit) {
            if (((Map) o).get("materialEdit") == editMaterial) {

                if (nn(modeloSQL)) {
                    CRUDutil.actualizarCampo(modeloSQL, (String) ((Map) o).get("campoEdit"), editMaterial.getTexto());
                    modeloSQL = CRUDutil.updateModelo(modeloSQL);

                } else {
                    valores = new ContentValues();
                    valores.put((String) ((Map) o).get("campoEdit"), editMaterial.getTexto());
                    if (secuencia > 0) {
                        int i = ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);
                        modeloSQL = CRUDutil.updateModelo(campos, id, secuencia);
                        System.out.println("guardados = " + i);
                    } else {
                        int x = ConsultaBD.updateRegistro(tabla, id, valores);
                        modeloSQL = CRUDutil.updateModelo(campos, id);
                        System.out.println("guardados = " + x);
                    }
                }
            }
        }
        //}
    }

    protected void guardarEdit(EditMaterialLayout editMaterial) {

        //if (editMaterial.getValido()) {
        for (Object o : camposEdit) {
            if (((Map) o).get("materialEdit") == editMaterial) {
                if (nn(modeloSQL)) {
                    int z = CRUDutil.actualizarCampo(modeloSQL, (String) ((Map) o).get("campoEdit"), editMaterial.getTexto());
                    System.out.println("guardados = " + z);
                    System.out.println("editMaterial = " + editMaterial.getTexto());
                    modeloSQL = CRUDutil.updateModelo(modeloSQL);
                } else {

                    valores = new ContentValues();
                    valores.put((String) ((Map) o).get("campoEdit"), editMaterial.getTexto());
                    if (secuencia > 0) {
                        int i = ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores);
                        modeloSQL = CRUDutil.updateModelo(campos, id, secuencia);
                        System.out.println("guardados = " + i);
                    } else {
                        int x = ConsultaBD.updateRegistro(tabla, id, valores);
                        modeloSQL = CRUDutil.updateModelo(campos, id);
                        System.out.println("guardados = " + x);
                    }
                }
            }
        }
        //}
    }

    protected void guardarEdit(ArrayList<Map> camposEdit, EditMaterial editMaterial, String tabla, String id, int secuencia) {
        for (Object o : camposEdit) {
            if (((Map) o).get("materialEdit") == editMaterial) {
                if (nn(modeloSQL)) {
                    CRUDutil.actualizarCampo(modeloSQL, (String) ((Map) o).get("campoEdit"), editMaterial.getTexto());
                } else {

                    ContentValues valores = new ContentValues();
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
    }

    protected void obtenerDatosEdit() {

        for (EditMaterial materialEdit : materialEdits) {


            for (Object o : camposEdit) {

                if (((Map) o).get("materialEdit") == materialEdit) {
                    materialEdit.setText(modeloSQL.getString((String) ((Map) o).get("campoEdit")));

                }
            }
        }

        for (EditMaterialLayout materialEdit : materialEditLayouts) {


            for (Object o : camposEdit) {

                if (((Map) o).get("materialEdit") == materialEdit) {
                    materialEdit.setText(modeloSQL.getString((String) ((Map) o).get("campoEdit")));

                }
            }
        }
    }

    protected boolean comprobarDatos() {


        if (id != null) {

            for (EditMaterial editMaterial : materialEdits) {
                if (editMaterial.getTexto().equals("")) {
                    return false;
                }
            }
        }

        return true;
    }

    protected void enviarAct() {
        Log.d(TAG, getMetodo());

        bundle = new Bundle();
        bundle.putString(ORIGEN, origen);
        bundle.putString(ACTUAL, actual);
        bundle.putString(ACTUALTEMP, actualtemp);
        bundle.putSerializable(MODELO, modeloSQL);
        bundle.putString(CAMPO_ID, id);
        bundle.putString(SUBTITULO, subTitulo);
        bundle.putInt(CAMPO_SECUENCIA, secuencia);
        System.out.println("Enviando bundle a MainActivity");
        icFragmentos.enviarBundleAActivity(bundle);
    }

    protected void enviarBundle() {
        Log.d(TAG, getMetodo());

        bundle = new Bundle();
        bundle.putString(ORIGEN, actual);
        bundle.putString(ACTUAL, destino);
        bundle.putString(ACTUALTEMP, actualtemp);
        bundle.putSerializable(MODELO, modeloSQL);
        bundle.putString(CAMPO_ID, id);
        bundle.putString(SUBTITULO, subTitulo);
        bundle.putInt(CAMPO_SECUENCIA, secuencia);

    }

    @Override
    public void onConfigurationChanged(Configuration myConfig) {
        super.onConfigurationChanged(myConfig);
        Log.d(TAG, getMetodo());

        int orientation = getResources().getConfiguration().orientation;
        SharedPreferences persistencia = getActivity().getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = persistencia.edit();

        editor.putString(TAGPERS, setTAG());
        editor.putString(ORIGEN, origen);
        editor.putString(ACTUAL, actual);
        editor.putString(ACTUALTEMP, actualtemp);
        editor.putString(SUBTITULO, subTitulo);
        editor.putString(CAMPO_ID, id);
        editor.putString(IDREL, idrelacionado);
        editor.putInt(CAMPO_SECUENCIA, secuencia);
        editor.putBoolean(NUEVOREGISTRO, nuevo);
        editor.apply();

        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                // Con la orientación en horizontal actualizamos el adaptador
            case Configuration.ORIENTATION_PORTRAIT:
                // Con la orientación en vertical actualizamos el adaptador

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, getMetodo());
        //cargarBundle();

        if (bundle != null) {
            enviarBundle();
            enviarAct();
        }


    }

    @Override
    protected void setPersistencia(SharedPreferences.Editor editor) {
        super.setPersistencia(editor);

        editor.putString(CAMPO_ID, id);
        editor.putInt(CAMPO_SECUENCIA, secuencia);
        editor.putString(IDREL, idrelacionado);
        editor.putBoolean(NUEVOREGISTRO, nuevo);

    }

    @Override
    protected void setRecuperarPersistencia(SharedPreferences persistencia) {

        id = persistencia.getString(CAMPO_ID, null);
        secuencia = persistencia.getInt(CAMPO_SECUENCIA, 0);
        idrelacionado = persistencia.getString(IDREL, null);
        nuevo = persistencia.getBoolean(NUEVOREGISTRO, false);

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
                        if (onUpdate()) {
                            startActivityForResult(mediaUtil.takePhotoIntent(), COD_FOTO);
                            mediaUtil.addPhotoToGallery();
                            path = mediaUtil.getPath(mediaUtil.getPhotoUri());
                            AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PATH, path);
                            onUpdate();
                        }

                    } catch (IOException e) {
                        Log.e("DialogoOpcionesImagen", e.toString());
                    }

                } else if (opciones[which].equals("Elegir de la galería")) {

                    if (onUpdate()) {
                        startActivityForResult(mediaUtil.openGalleryIntent(), COD_SELECCIONA);
                    }

                } else if (opciones[which].equals("Quitar foto")) {

                    path = null;
                    onUpdate();

                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void mostrarDialogoOpcionesImagen(final Context contexto, String campoImagen) {
        Log.d(TAG, getMetodo());

        this.campoImagen = campoImagen;
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
                        if (onUpdate()) {
                            startActivityForResult(mediaUtil.takePhotoIntent(), COD_FOTO);
                            mediaUtil.addPhotoToGallery();
                            path = mediaUtil.getPath(mediaUtil.getPhotoUri());
                            AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PATH, path);
                            onUpdate();
                        }

                    } catch (IOException e) {
                        Log.e("DialogoOpcionesImagen", e.toString());
                    }

                } else if (opciones[which].equals("Elegir de la galería")) {

                    if (onUpdate()) {
                        startActivityForResult(mediaUtil.openGalleryIntent(), COD_SELECCIONA);
                    }

                } else if (opciones[which].equals("Quitar foto")) {

                    path = null;
                    onUpdate();

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
                    if (nn(modeloSQL) && nnn(path) && nnn(campoImagen)) {
                        CRUDutil.actualizarCampo(modeloSQL, campoImagen, path);
                    }

                case COD_SELECCIONA:

                    if (data != null && data.getData() != null) {
                        path = mediaUtil.getPath(data.getData());
                        if (nn(modeloSQL) && nnn(path) && nnn(campoImagen)) {
                            CRUDutil.actualizarCampo(modeloSQL, campoImagen, path);
                        }

                    }
                    onUpdate();
                    break;

                case AUDIORECORD:

                    if (data.getData() != null) {
                        path = mediaUtil.getPath(data.getData());
                    }
                    onUpdate();
                    break;


            }
        }

        campoImagen = CAMPO_RUTAFOTO;
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

    protected void imagenMediaPantalla() {
        Log.d(TAG, getMetodo());

        if (!land) {
            imagen.getLinearLayoutCompat().setMinimumHeight((int) ((double) alto / 2));
            imagen.getLinearLayoutCompat().setMinimumWidth(ancho);
            imagen.setMaxHeight((int) ((double) alto / 2));
            imagen.setMaxWidth(ancho);
        } else {
            imagen.getLinearLayoutCompat().setMinimumWidth((int) ((double) ancho / 2));
            imagen.getLinearLayoutCompat().setMinimumHeight((int) ((double) alto / 2));
            imagen.setMaxWidth((int) ((double) ancho / 2));
            imagen.setMaxHeight((int) ((double) alto / 2));

        }

    }

    protected void imagenPantalla(int falto, int fancho) {
        Log.d(TAG, getMetodo());

        if (!land) {
            imagen.getLinearLayoutCompat().setMinimumHeight((int) ((double) alto / falto));
            imagen.getLinearLayoutCompat().setMinimumWidth((int) ((double) ancho / fancho));
            imagen.setMaxHeight((int) ((double) alto / falto));
            imagen.setMaxWidth((int) ((double) ancho / fancho));
        } else {
            imagen.getLinearLayoutCompat().setMinimumWidth((int) ((double) ancho / (falto + fancho)));
            imagen.getLinearLayoutCompat().setMinimumHeight((int) ((double) alto / falto));
            imagen.setMaxWidth((int) ((double) ancho / (falto + fancho)));
            imagen.setMaxHeight((int) ((double) alto / falto));

        }

    }


    protected void setImagenUri(Context contexto, String rutaFoto) {

        imagen.setImageUri(rutaFoto);

    }

    protected void setImagenUri(MediaUtil imagenUtil, String rutaFoto) {

        imagen.setImageUri(rutaFoto);

    }


    protected void setImagenUriCircle(Context contexto, String rutaFoto) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagen.setImageUriCircle(rutaFoto);

    }


    protected void setImagenUriCircle(Context contexto, String rutaFoto, ImageView imagen) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageUriCircle(rutaFoto, imagen, getIdDrawable("ic_add_a_photo_black_24dp"));

    }


    protected void setImagenFireStoreCircle(Context contexto, String rutaFoto, ImageView imagen) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStoreCircle(rutaFoto, imagen, getIdDrawable("ic_add_a_photo_black_24dp"));

    }

    protected void setDato(String campo, String valor) {

        putDato(valores, campos, campo, valor);
    }

    protected void setDato(String campo, String valor, String tipo) {
        Log.d(TAG, getMetodo() + " " + campo);


        switch (tipo) {

            case INT:
                putDato(valores, campos, campo, JavaUtil.comprobarInteger(valor));
                break;
            case LONG:
                putDato(valores, campos, campo, JavaUtil.comprobarLong(valor));
                break;
            case DOUBLE:
                putDato(valores, campos, campo, JavaUtil.comprobarDouble(valor));
                break;
            case FLOAT:
                putDato(valores, campos, campo, JavaUtil.comprobarFloat(valor));
                break;
            case SHORT:
                putDato(valores, campos, campo, JavaUtil.comprobarShort(valor));
                break;
            case NONNULL:
                putDato(valores, campos, campo, JavaUtil.noNuloString(valor));

        }
    }

    protected void setDato(String campo, int valor) {

        putDato(valores, campos, campo, valor);
    }

    protected void setDato(String campo, long valor) {

        putDato(valores, campos, campo, valor);
    }

    protected void setDato(String campo, double valor) {

        putDato(valores, campos, campo, valor);
    }

    protected void setDato(String campo, float valor) {

        putDato(valores, campos, campo, valor);
    }

    protected void setDato(String campo, short valor) {

        putDato(valores, campos, campo, valor);
    }

    protected boolean onDelete() {
        Log.d(TAG, getMetodo());
        return true;
    }

    protected void mostrarDialogDelete() {
        Log.d(TAG, getMetodo());

        String selDelete;

        if (nuevo) {
            selDelete = getString(getIdString("limpiar_formulario"));

        } else {
            selDelete = getString(getIdString("confirmar_borrado"));
        }

        final CharSequence[] opciones = {selDelete, getString(getIdString("cancelar"))};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(getIdString("borrar")));
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals(getString(getIdString("limpiar_formulario")))) {

                    onDelete();

                } else if (opciones[which].equals(getString(getIdString("confirmar_borrado")))) {

                    onDelete();

                } else {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

}
