package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

public class FragmentCUDDetpartidaBaseTrabajo extends FragmentCUD implements Interactor.ConstantesPry,
        ContratoPry.Tablas, Interactor.TiposDetPartida, Interactor.TiposEstados {

    private EditMaterialLayout descripcion;
    private EditMaterialLayout precio;
    private EditMaterialLayout cantidad;
    private EditMaterialLayout tiempoDet;
    private ModeloSQL trabajo;

    private EditMaterialLayout nombre;

    public FragmentCUDDetpartidaBaseTrabajo() {
        // Required empty public constructor
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected void setNuevo() {

    }

    @Override
    protected void setTabla() {

        tabla = TABLA_DETPARTIDABASE;

    }

    @Override
    protected void setBundle() {

    }

    @Override
    protected void setDatos() {


        visible(tiempoDet.getLinearLayout());
        visible(cantidad.getLinearLayout());
        gone(btnsave);

        modeloSQL = cruDutil.updateModelo();
        trabajo = CRUDutil.updateModelo(CAMPOS_TRABAJO, modeloSQL.getString(DETPARTIDABASE_ID_DETPARTIDABASE));

        //cantidad.setText(JavaUtil.getDecimales(modeloSQL.getDouble(DETPARTIDABASE_CANTIDAD)));
        nombre.setText(trabajo.getString(TRABAJO_NOMBRE));
        descripcion.setText(trabajo.getString(TRABAJO_DESCRIPCION));
        precio.setText(JavaUtil.formatoMonedaLocal(trabajo.getDouble(TRABAJO_TIEMPO) * Interactor.hora));
        tiempoDet.setText(trabajo.getString(TRABAJO_TIEMPO));
        path = trabajo.getString(TRABAJO_RUTAFOTO);

        if (nn(path)) {
            imagen.setImageUriPerfil(activityBase,path);
        }

    }

    @Override
    protected void setAcciones() {

    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.detpartidabase;
        tituloPlural = tituloSingular;
        tituloNuevo = R.string.nuevo_detalle_partida_base;
    }

    @Override
    protected void setInicio() {

        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalle);
        visible(frdetalle);

        imagen = vistaForm.addViewImagenLayout();
        imagen.setTextTitulo(getString(R.string.trabajo).toUpperCase());
        nombre = vistaForm.addEditMaterialLayout(R.string.nombre);
        nombre.setActivo(false);
        descripcion = vistaForm.addEditMaterialLayout(R.string.descripcion);
        descripcion.setActivo(false);
        ViewGroupLayout vistaPrecio = new ViewGroupLayout(contexto,vistaForm.getViewGroup());
        vistaPrecio.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        cantidad = vistaPrecio.addEditMaterialLayout(R.string.unidades, DETPARTIDABASE_CANTIDAD,1);
        tiempoDet = vistaPrecio.addEditMaterialLayout(R.string.tiempo,1);
        tiempoDet.setActivo(false);
        precio = vistaPrecio.addEditMaterialLayout(R.string.importe,1);
        precio.setActivo(false);
        actualizarArrays(vistaPrecio);
        actualizarArrays(vistaForm);
        imagen.getImagen().setClickable(false);

    }

    @Override
    protected void setContenedor() {

    }

    @Override
    protected void setcambioFragment() {

        bundle = new Bundle();
        putBundle(CAMPO_ID, id);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaBase());

    }

}