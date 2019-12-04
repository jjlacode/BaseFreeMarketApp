package com.codevsolution.base.module;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewImagenLayout;
import com.codevsolution.base.style.Estilos;

public class FormProfileModule extends BaseModule {

    protected EditMaterialLayout nombre;
    protected EditMaterialLayout descripcion;
    protected EditMaterialLayout direccion;
    protected EditMaterialLayout email;
    protected EditMaterialLayout telefono;
    protected EditMaterialLayout claves;
    protected EditMaterialLayout web;
    private ViewImagenLayout imagen;

    public FormProfileModule(ViewGroup parent, Context context, Fragment fragment) {
        super(parent, context, fragment);
        init();
    }


    @Override
    public void init() {

        imagen = vistaMain.addViewImagenLayout();
        nombre = vistaMain.addEditMaterialLayout(Estilos.getString(context, "nombre"));
        descripcion = vistaMain.addEditMaterialLayout(Estilos.getString(context, "descripcion"));
        direccion = vistaMain.addEditMaterialLayout(Estilos.getString(context, "direccion"));
        email = vistaMain.addEditMaterialLayout(Estilos.getString(context, "email"));
        telefono = vistaMain.addEditMaterialLayout(Estilos.getString(context, "telefono"));
        claves = vistaMain.addEditMaterialLayout(Estilos.getString(context, "palabras_clave"));
        web = vistaMain.addEditMaterialLayout(Estilos.getString(context, "web"));
        ((FragmentBase) fragmentParent).actualizarArrays(vistaMain);
        imagen.setIcfragmentos(((FragmentBase) fragmentParent).getIcFragmentos());
        imagen.setVisibleBtn();

        if (fragmentParent != null) {
            ((FragmentBase) fragmentParent).actualizarArrays(vistaMain);
        }

    }

    public void setVisibility(boolean nombreEnable, boolean descripcionEnable, boolean direccionEnable,
                              boolean emailEnable, boolean telefonoEnable, boolean clavesEnable,
                              boolean webEnable, boolean imagenEnable) {
        if (nombreEnable) {
            nombre.getViewGroup().setVisibility(View.VISIBLE);
        } else {
            nombre.getViewGroup().setVisibility(View.GONE);
        }
        nombre.setActivo(nombreEnable);

        if (descripcionEnable) {
            descripcion.getViewGroup().setVisibility(View.VISIBLE);
        } else {
            descripcion.getViewGroup().setVisibility(View.GONE);
        }
        descripcion.setActivo(descripcionEnable);

        if (direccionEnable) {
            direccion.getViewGroup().setVisibility(View.VISIBLE);
        } else {
            direccion.getViewGroup().setVisibility(View.GONE);
        }
        direccion.setActivo(direccionEnable);

        if (emailEnable) {
            email.getViewGroup().setVisibility(View.VISIBLE);
        } else {
            email.getViewGroup().setVisibility(View.GONE);
        }
        email.setActivo(emailEnable);

        if (telefonoEnable) {
            telefono.getViewGroup().setVisibility(View.VISIBLE);
        } else {
            telefono.getViewGroup().setVisibility(View.GONE);
        }
        telefono.setActivo(telefonoEnable);

        if (clavesEnable) {
            claves.getViewGroup().setVisibility(View.VISIBLE);
        } else {
            claves.getViewGroup().setVisibility(View.GONE);
        }
        claves.setActivo(clavesEnable);

        if (webEnable) {
            web.getViewGroup().setVisibility(View.VISIBLE);
        } else {
            web.getViewGroup().setVisibility(View.GONE);
        }
        web.setActivo(webEnable);

        if (imagenEnable) {
            imagen.getLinearLayoutCompat().setVisibility(View.VISIBLE);
        } else {
            imagen.getLinearLayoutCompat().setVisibility(View.GONE);
        }

    }

    public EditMaterialLayout getNombre() {
        return nombre;
    }

    public void setNombre(EditMaterialLayout nombre) {
        this.nombre = nombre;
    }

    public EditMaterialLayout getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(EditMaterialLayout descripcion) {
        this.descripcion = descripcion;
    }

    public EditMaterialLayout getDireccion() {
        return direccion;
    }

    public void setDireccion(EditMaterialLayout direccion) {
        this.direccion = direccion;
    }

    public EditMaterialLayout getEmail() {
        return email;
    }

    public void setEmail(EditMaterialLayout email) {
        this.email = email;
    }

    public EditMaterialLayout getTelefono() {
        return telefono;
    }

    public void setTelefono(EditMaterialLayout telefono) {
        this.telefono = telefono;
    }

    public EditMaterialLayout getClaves() {
        return claves;
    }

    public void setClaves(EditMaterialLayout claves) {
        this.claves = claves;
    }

    public EditMaterialLayout getWeb() {
        return web;
    }

    public void setWeb(EditMaterialLayout web) {
        this.web = web;
    }

    public ViewImagenLayout getImagen() {
        return imagen;
    }

    public void setImagen(ViewImagenLayout imagen) {
        this.imagen = imagen;
    }
}
