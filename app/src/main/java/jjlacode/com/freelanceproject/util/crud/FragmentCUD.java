package jjlacode.com.freelanceproject.util.crud;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.JavaUtil;

import static jjlacode.com.freelanceproject.CommonPry.permiso;
import static jjlacode.com.freelanceproject.CommonPry.setNamefdef;

public abstract class FragmentCUD extends FragmentBaseCRUD implements JavaUtil.Constantes {

    public FragmentCUD() {
    }

    @Override
    public void onResume() {

        super.onResume();

        selector();

    }

    protected void selector(){

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
            btndelete.setVisibility(View.GONE);
            activityBase.fab.hide();
            activityBase.fab2.hide();
        }else{
            btndelete.setVisibility(View.VISIBLE);
            datos();
        }

        activityBase.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id=null;
                modelo = null;
                secuencia=0;
                activityBase.toolbar.setSubtitle(tituloNuevo);
                vaciarControles();
                setNuevo();
                btndelete.setVisibility(View.GONE);
                activityBase.fab.hide();
                activityBase.fab2.hide();

            }
        });

        activityBase.fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityBase.fab2.hide();
                activityBase.fab.show();
                onUpdate();
            }
        });

        acciones();

    }


    @Override
    protected boolean onUpdate(){

        if (update()) {

            datos();
            return true;
        }
        return false;
    }

    protected boolean onDelete(){

        if ((id==null && tablaCab==null)||(tablaCab!=null && secuencia==0)){
            modelo = new Modelo(campos);
            datos();
            modelo = null;
            setNuevo();
        }else {
            if (delete()) {

                modelo = null;
                if (tablaCab==null) {
                    id = null;
                }
                secuencia=0;
                selector();
                cambiarFragment();

                return true;
            }
        }

        return false;
    }

    protected boolean onBack(){

        modelo=null;
        if(tablaCab==null) {
            id = null;
        }
        secuencia=0;
        selector();
        cambiarFragment();

        return true;
    }

    protected void acciones(){

        super.acciones();

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               onBack();

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrarDialogDelete();
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

                        mostrarDialogoOpcionesImagen(contexto);
                    }
                });
            }
        }

    }

    protected void setNuevo(){

        path = null;

    }

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

        setDato(campoTimeStamp,JavaUtil.hoy());

        if (tablaCab != null && secuencia == 0 && modelo == null) {

            secuencia = consulta.secInsertRegistroDetalle(campos, id, tablaCab, valores);

            modelo = consulta.queryObjectDetalle(campos, id, secuencia);

            Toast.makeText(getContext(), "Registro detalle creado", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == null && modelo == null) {

            id = consulta.idInsertRegistro(tabla, valores);
            modelo = consulta.queryObject(campos, id);
            idAOrigen = id;

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

    protected void setcambioFragment() {

        activityBase.toolbar.setSubtitle(setNamefdef());

    }

    protected boolean update(){

        valores = new ContentValues();

        setContenedor();

        setDato(campoTimeStamp,JavaUtil.hoy());

        if (tablaCab!=null && modelo!=null){
            secuencia = modelo.getInt(campoSecuencia);
        }

        if ((id!=null || modelo!=null) && (tablaCab==null||secuencia>0)){

            if (id==null){
                id = modelo.getString(campoID);
            }
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

        }else if (modelo==null){
            return registrar();
        }

        return false;

    }



}
