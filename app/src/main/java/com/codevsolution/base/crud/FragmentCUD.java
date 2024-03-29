package com.codevsolution.base.crud;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codevsolution.base.android.CheckPermisos;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.style.Dialogos;
import com.codevsolution.base.style.Estilos;

import java.util.Arrays;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public abstract class FragmentCUD extends FragmentBaseCRUD {

    public FragmentCUD() {
    }

    @Override
    public void onResume() {
        Log.d(TAG, getMetodo());

        super.onResume();


        selector();

    }

    protected void selector() {
        Log.d(TAG, getMetodo());

        if (crudUtil == null) {
            crudUtil = new CRUDutil((FragmentBaseCRUD) setFragment());
        }

        if (nuevo) {

            if (tablaCab == null) {
                id = null;
            }
            modeloSQL = null;
            secuencia = 0;
            if (tituloNuevo > 0) {
                icFragmentos.showSubTitle(tituloNuevo);
                reproducir(getString(tituloNuevo));
            }
            vaciarControles();
            path = null;
            setNuevo();
            crudUtil.setImagen(contexto);
        } else {
            crudUtil.setImagen(contexto);
            datos();
        }
        back = false;

        acciones();

    }

    @Override
    protected boolean setBack() {
        return back;
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

        ListaModeloSQL lista = crudUtil.setListaModelo(campos);
        boolean valido = false;
        for (ModeloSQL modeloSQLSW : lista.getLista()) {
            if (modeloSQLSW != null && valido) {
                id = modeloSQLSW.getString(campoID);
                if (tablaCab != null) {
                    secuencia = modeloSQLSW.getInt(CAMPO_SECUENCIA);
                    modeloSQL = crudUtil.updateModelo(campos, id, secuencia);
                } else {
                    modeloSQL = crudUtil.updateModelo(campos, id);
                }
                selector();
                break;
            }
            if (modeloSQLSW.getString(campoID).equals(id)) {
                valido = true;
            }
        }
    }

    @Override
    protected void setOnRigthSwipeCuerpo() {
        super.setOnRigthSwipeCuerpo();
        Log.d(TAG, getMetodo());

        ListaModeloSQL lista = crudUtil.setListaModelo(campos);
        ModeloSQL modeloSQLAnt = null;
        for (ModeloSQL modeloSQLSW : lista.getLista()) {
            if (modeloSQLSW.getString(campoID).equals(id)) {
                if (modeloSQLAnt != null) {
                    id = modeloSQLAnt.getString(campoID);
                    if (tablaCab != null) {
                        secuencia = modeloSQLAnt.getInt(CAMPO_SECUENCIA);
                        modeloSQL = crudUtil.updateModelo(campos, id, secuencia);

                    } else {
                        modeloSQL = crudUtil.updateModelo(campos, id);
                    }
                }
                selector();
                break;
            }
            if (modeloSQLSW != null) {
                modeloSQLAnt = modeloSQLSW.clonar(false);
            }
        }

    }

    @Override
    protected boolean onUpdate() {
        Log.d(TAG, getMetodo());

        if (comprobarDatos() && update()) {

            datos();
            return true;
        }
        return false;
    }

    protected boolean onDelete() {
        Log.d(TAG, getMetodo());

        if (delete()) {

            back = true;

            cambiarFragment();

            return true;
        }

        return false;
    }

    protected boolean onBack() {
        Log.d(TAG, getMetodo());

        back = true;

        cambiarFragment();

        return true;
    }

    protected void dialogoBack() {

        String titulo = Estilos.getString(contexto, "datos_no_guardados");
        String mensaje = Estilos.getString(contexto, "pregunta_salir_sin_guardar");

        Dialogos.DialogoTexto dialogoTexto = new Dialogos.DialogoTexto(titulo, mensaje, contexto, new Dialogos.DialogoTexto.OnClick() {
            @Override
            public void onConfirm() {

                onBack();

            }

            @Override
            public void onCancel() {
                System.out.println("cancelado");
            }
        });
        dialogoTexto.show(getActivity().getSupportFragmentManager(), "dialogoback");
    }

    protected void acciones() {
        super.acciones();
        Log.d(TAG, getMetodo());


        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, getMetodo());

                if (nn(modeloSQL) && modeloSQL.noModificado()) {
                    onBack();
                } else {
                    dialogoBack();
                    visible(btnsave);
                }


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

                if (onUpdate()) {
                    modeloSQL = crudUtil.updateModelo(modeloSQL);
                }

            }
        });

        activityBase.fabNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onClickNuevo();

            }
        });

        if (CheckPermisos.validarPermisos(activityBase, READ_EXTERNAL_STORAGE, 100) &&
                CheckPermisos.validarPermisos(activityBase, WRITE_EXTERNAL_STORAGE, 100) &&
                CheckPermisos.validarPermisos(activityBase, CAMERA, 100)) {
            if (imagen != null) {
                imagen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (id != null) {
                            mostrarDialogoOpcionesImagen(contexto);
                        } else {
                            Toast.makeText(contexto, "El registro debe estar creado para elegir la imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }

    protected void setNuevo() {
        Log.d(TAG, getMetodo());

        path = null;
        if (tituloNuevo > 0) {
            subTitulo = getString(tituloNuevo);
            icFragmentos.showSubTitle(subTitulo);
            reproducir(subTitulo);
        }

    }

    protected boolean delete() {
        Log.d(TAG, getMetodo());

        if (tablaCab != null) {

            if (consultaBD.deleteRegistroDetalle(tabla, id, secuencia) > 0) {
                Toast.makeText(getContext(), "Registro detalle borrado", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else if (consultaBD.deleteRegistro(tabla, id) > 0) {
            Toast.makeText(getContext(), "Registro borrado", Toast.LENGTH_SHORT).show();
            return true;
        } else {

            Toast.makeText(getContext(), "Error al borrar registro", Toast.LENGTH_SHORT).show();
            return false;
        }

        return false;
    }

    protected boolean registrar() {
        Log.d(TAG, getMetodo());

        try {

            if (tablaCab != null) {

                secuencia = consultaBD.secInsertRegistroDetalle(campos, id, valores);

                modeloSQL = consultaBD.queryObjectDetalle(campos, id, secuencia);

                if (secuencia > 0) {

                    Toast.makeText(getContext(), "Registro detalle creado", Toast.LENGTH_SHORT).show();
                    System.out.println("Registro creado");
                    System.out.println("modeloSQL = " + modeloSQL);
                    nuevo = false;
                    return true;
                }
                return false;

            } else {

                id = consultaBD.idInsertRegistro(tabla, valores);
                System.out.println("campos = " + Arrays.toString(campos));
                System.out.println("id = " + id);
                modeloSQL = consultaBD.queryObject(campos, id);
                //DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                //db.child(idUserCode).child(tabla).child(id).setValue(convertirModelo(modeloSQL));
                if (id != null) {
                    System.out.println("Registro creado");
                    System.out.println("modeloSQL = " + modeloSQL);

                    Toast.makeText(getContext(), "Registro creado", Toast.LENGTH_SHORT).show();
                    nuevo = false;
                    return true;
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al crear registro", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    protected abstract void setContenedor();

    protected void cambiarFragment() {
        Log.d(TAG, getMetodo());

        activityBase.toolbar.setSubtitle(tituloPlural);
        setcambioFragment();
        if (bundle != null) {
            enviarBundle();
            enviarAct();
        }
    }

    protected void setcambioFragment() {
        Log.d(TAG, getMetodo());

        id = null;
        modeloSQL = null;
        secuencia = 0;
        bundle = new Bundle();

    }

    protected boolean update() {
        Log.d(TAG, getMetodo());

        valores = new ContentValues();

        comprobarRutaFoto();
        setContenedor();

        if (tablaCab != null && modeloSQL != null) {
            secuencia = modeloSQL.getInt(campoSecuencia);
        }

        if ((id != null || modeloSQL != null) && (tablaCab == null || secuencia > 0)) {

            if (id == null) {
                id = modeloSQL.getString(campoID);
            }
            try {
                if (tablaCab != null) {

                    if (consultaBD.updateRegistroDetalle(tabla, id, secuencia, valores) > 0) {

                        modeloSQL = consultaBD.queryObjectDetalle(campos, id, secuencia);
                        Toast.makeText(getContext(), "Registro detalle guardado", Toast.LENGTH_SHORT).show();
                        nuevo = false;
                        return true;
                    }

                } else if (consultaBD.updateRegistro(tabla, id, valores) > 0) {

                    modeloSQL = consultaBD.queryObject(campos, id);

                    Toast.makeText(getContext(), "Registro guardado", Toast.LENGTH_SHORT).show();
                    nuevo = false;
                    return true;

                } else {

                    Toast.makeText(getContext(), "Error al guardar registro", Toast.LENGTH_SHORT).show();
                    return false;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (modeloSQL == null) {
            return registrar();
        }

        return false;

    }

    private void comprobarRutaFoto() {

        for (String campo : campos) {
            if (campo.equals(CAMPO_RUTAFOTO)) {
                consultaBD.putDato(valores, CAMPO_RUTAFOTO, path);
            }
        }

    }

    @Override
    public void onConfigurationChanged(Configuration myConfig) {
        super.onConfigurationChanged(myConfig);
        Log.d(TAG, getMetodo());

        int orientation = getResources().getConfiguration().orientation;
        SharedPreferences persistencia = getActivity().getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = persistencia.edit();

        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                // Con la orientación en horizontal actualizamos el adaptador

            case Configuration.ORIENTATION_PORTRAIT:
                // Con la orientación en vertical actualizamos el adaptador

                break;
        }
    }


}
