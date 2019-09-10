package com.jjlacode.base.util.crud;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jjlacode.base.util.android.CheckPermisos;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.base.util.models.ListaModelo;
import com.jjlacode.base.util.models.Modelo;
import com.jjlacode.base.util.sqlite.ConsultaBD;
import com.jjlacode.base.util.time.TimeDateUtil;

import java.util.GregorianCalendar;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.jjlacode.freelanceproject.logica.Interactor.setNamefdef;

public abstract class FragmentCUD extends FragmentBaseCRUD {

    public FragmentCUD() {
    }

    @Override
    public void onResume() {
        Log.d(TAG, getMetodo());

        super.onResume();


        selector();

    }

    protected void selector(){
        Log.d(TAG, getMetodo());

        if (nuevo){

            if (tablaCab==null) {
                id = null;
            }
            modelo = null;
            secuencia=0;
            icFragmentos.showSubTitle(tituloNuevo);
            vaciarControles();
            path = null;
            setNuevo();
            setImagen();
        }else{
            datos();
        }
        back = false;

        acciones();

    }

    protected void onClickNuevo() {
        nuevo = true;
        setOnClickNuevo();
        selector();
    }

    protected void setOnClickNuevo() {

    }


    @Override
    protected void setOnLeftSwipeCuerpo() {
        super.setOnLeftSwipeCuerpo();
        Log.d(TAG, getMetodo());

        ListaModelo lista = CRUDutil.setListaModelo(campos);
        boolean valido = false;
        for (Modelo modeloSW : lista.getLista()) {
            if (modeloSW!=null && valido){
                id = modeloSW.getString(campoID);
                if (tablaCab!=null){
                    secuencia = modeloSW.getInt(CAMPO_SECUENCIA);
                    modelo = CRUDutil.setModelo(campos,id,secuencia);
                }else{
                    modelo = CRUDutil.setModelo(campos,id);
                }
                System.out.println("swipe derecha");
                selector();
                break;
            }
            if (modeloSW.getString(campoID).equals(id)){
                valido = true;
            }
        }
    }

    @Override
    protected void setOnRigthSwipeCuerpo() {
        super.setOnRigthSwipeCuerpo();
        Log.d(TAG, getMetodo());

        ListaModelo lista = CRUDutil.setListaModelo(campos);
        Modelo modeloAnt = null;
        for (Modelo modeloSW : lista.getLista()) {
            if (modeloSW.getString(campoID).equals(id)){
                if (modeloAnt!=null){
                    id = modeloAnt.getString(campoID);
                    if (tablaCab!=null){
                        secuencia = modeloAnt.getInt(CAMPO_SECUENCIA);
                        modelo = CRUDutil.setModelo(campos,id,secuencia);

                    }else{
                        modelo = CRUDutil.setModelo(campos,id);
                    }
                }
                selector();
                System.out.println("swipe izquierda");
                break;
            }
            if (modeloSW!=null) {
                modeloAnt = modeloSW.clonar(false);
            }
        }

    }

    @Override
    protected boolean onUpdate(){
        Log.d(TAG, getMetodo());

        if (update()) {
            datos();
            return true;
        }
        return false;
    }

    protected boolean onDelete(){
        Log.d(TAG, getMetodo());

            if (delete()) {

                back = true;

                cambiarFragment();

                return true;
            }

        return false;
    }

    protected boolean onBack(){
        Log.d(TAG, getMetodo());

        back = true;

        cambiarFragment();

        return true;
    }

    @Override
    protected void alCambiarCampos(EditMaterial editMaterial) {
        super.alCambiarCampos(editMaterial);

    }

    protected void acciones(){
        super.acciones();
        Log.d(TAG, getMetodo());


        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, getMetodo());

                onBack();

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, getMetodo());

                mostrarDialogDelete();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, getMetodo());

                onUpdate();

            }
        });

        activityBase.fabNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onClickNuevo();

            }
        });

        activityBase.fabVoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reconocimientoVoz(RECOGNIZE_SPEECH_ACTIVITY);
            }
        });

        if (CheckPermisos.validarPermisos(activityBase, READ_EXTERNAL_STORAGE, 100) &&
                CheckPermisos.validarPermisos(activityBase, WRITE_EXTERNAL_STORAGE, 100) &&
                CheckPermisos.validarPermisos(activityBase, CAMERA, 100)) {
            if (imagen!=null) {
                imagen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mostrarDialogoOpcionesImagen(contexto);
                    }
                });
            }
        }

    }

    protected void setNuevo(){
        Log.d(TAG, getMetodo());

        path = null;

    }

    protected boolean delete(){
        Log.d(TAG, getMetodo());

        if (tablaCab!=null){

            if (ConsultaBD.deleteRegistroDetalle(tabla,id,secuencia)>0){
                Toast.makeText(getContext(),"Registro detalle borrado", Toast.LENGTH_SHORT).show();
                return true;
            }
        }else if (ConsultaBD.deleteRegistro(tabla,id)>0) {
            Toast.makeText(getContext(),"Registro borrado", Toast.LENGTH_SHORT).show();
            return true;
        }else{

            Toast.makeText(getContext(), "Error al borrar registro", Toast.LENGTH_SHORT).show();
            return false;
        }

        return false;
    }

    protected boolean registrar() {
        Log.d(TAG, getMetodo());

        //valores = new ContentValues();

        //setDato(CAMPO_TIMESTAMP, TimeDateUtil.getDateLong(new GregorianCalendar()));
        setDato(CAMPO_CREATEREG, TimeDateUtil.getDateLong(new GregorianCalendar()));
        //setContenedor();

        try {

            if (tablaCab != null) {

                secuencia = ConsultaBD.secInsertRegistroDetalle(campos, id, tablaCab, valores);

                modelo = ConsultaBD.queryObjectDetalle(campos, id, secuencia);

                Toast.makeText(getContext(), "Registro detalle creado", Toast.LENGTH_SHORT).show();
                nuevo = false;
                return true;

            } else {

                id = ConsultaBD.idInsertRegistro(tabla, valores);
                modelo = ConsultaBD.queryObject(campos, id);
                //DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                //db.child(idUser).child(tabla).child(id).setValue(convertirModelo(modelo));

                Toast.makeText(getContext(), "Registro creado", Toast.LENGTH_SHORT).show();
                nuevo = false;
                return true;

            }
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al crear registro", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    protected abstract void setContenedor();

    protected void cambiarFragment(){
        Log.d(TAG, getMetodo());

        icFragmentos.fabVisible();
        activityBase.toolbar.setSubtitle(setNamefdef());
        setcambioFragment();
        if (bundle!=null) {
            enviarBundle();
            enviarAct();
        }
    }

    protected void setcambioFragment() {
        Log.d(TAG, getMetodo());

        bundle = new Bundle();

    }

    protected boolean update(){
        Log.d(TAG, getMetodo());

        valores = new ContentValues();

        setDato(CAMPO_TIMESTAMP, TimeDateUtil.getDateLong(new GregorianCalendar()));
        comprobarRutaFoto();
        setContenedor();

        if (tablaCab!=null && modelo!=null){
            secuencia = modelo.getInt(campoSecuencia);
        }

        if ((id!=null || modelo!=null) && (tablaCab==null||secuencia>0)){

            if (id==null){
                id = modelo.getString(campoID);
            }
            try {
                if (tablaCab != null) {

                    if (ConsultaBD.updateRegistroDetalle(tabla, id, secuencia, valores) > 0) {

                        modelo = ConsultaBD.queryObjectDetalle(campos, id, secuencia);
                        Toast.makeText(getContext(), "Registro detalle guardado", Toast.LENGTH_SHORT).show();
                        nuevo = false;
                        return true;
                    }

                } else if (ConsultaBD.updateRegistro(tabla, id, valores) > 0) {

                    modelo = ConsultaBD.queryObject(campos, id);
                    //idUser = AndroidUtil.getSharePreference(contexto,USERID,USERID,NULL);
                    //System.out.println("idUser = " + idUser);
                    //DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    //db.child(idUser).child(tabla).child(id).setValue(convertirModelo(modelo));

                    Toast.makeText(getContext(), "Registro guardado", Toast.LENGTH_SHORT).show();
                    nuevo = false;
                    return true;

                } else {

                    Toast.makeText(getContext(), "Error al guardar registro", Toast.LENGTH_SHORT).show();
                    return false;

                }
            }catch (Exception e){e.printStackTrace();}

        }else if (modelo==null){
            return registrar();
        }

        return false;

    }

    private void comprobarRutaFoto() {

        for (String campo : campos) {
            if (campo.equals(CAMPO_RUTAFOTO)) {
                setDato(CAMPO_RUTAFOTO, path);
            }
        }

    }

    @Override
    public void onConfigurationChanged(Configuration myConfig) {
        super.onConfigurationChanged(myConfig);
        Log.d(TAG, getMetodo());

        int orientation = getResources().getConfiguration().orientation;
        SharedPreferences persistencia=getActivity().getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=persistencia.edit();

        update();
        switch(orientation ) {
            case Configuration.ORIENTATION_LANDSCAPE:
                // Con la orientación en horizontal actualizamos el adaptador

            case Configuration.ORIENTATION_PORTRAIT:
                // Con la orientación en vertical actualizamos el adaptador
                editor.putString(ORIGEN, origen);
                editor.putString(ACTUAL, actual);
                editor.putString(ACTUALTEMP, actualtemp);
                editor.putString(SUBTITULO, subTitulo);
                editor.putString(CAMPO_ID,id);
                editor.putInt(CAMPO_SECUENCIA,secuencia);
                editor.apply();
                break;
        }
    }


}
