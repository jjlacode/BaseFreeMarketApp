package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.base.sqlite.ConsultaBD.queryObjectDetalle;

public class FragmentCUDDetpartidaBaseProducto extends FragmentCUD implements Interactor.ConstantesPry,
        ContratoPry.Tablas, Interactor.TiposDetPartida, Interactor.TiposEstados {

    private EditMaterial nombre;
    private EditMaterial descripcion;
    private EditMaterial precio;
    private EditMaterial descProv;
    private EditMaterial refProv;
    private EditMaterial cantidad;
    private TextView tipoDetPartida;
    private String tipo;

    private Modelo producto;
    private EditMaterial nomProv;


    public FragmentCUDDetpartidaBaseProducto() {
        // Required empty public constructor
    }


    @Override
    protected void setNuevo() {

    }

    @Override
    protected void setTabla() {

        tabla = TABLA_DETPARTIDABASE;

    }

    @Override
    protected void setTablaCab() {

        tablaCab = ContratoPry.getTabCab(tabla);
    }

    @Override
    protected void setCampos() {

        campos = ContratoPry.obtenerCampos(tabla);

    }


    @Override
    protected void setBundle() {

    }

    @Override
    protected void setDatos() {

        allGone();

        visible(nombre);
        visible(descripcion);
        visible(refProv);
        visible(nomProv);
        visible(precio);
        visible(tipoDetPartida);
        visible(cantidad);
        gone(btnsave);

        modelo = CRUDutil.setModelo(campos, id, secuencia);

        producto = CRUDutil.setModelo(CAMPOS_PRODUCTO, modelo.getString(DETPARTIDABASE_ID_DETPARTIDABASE));

        tipo = TIPOPRODUCTO;

        if (nn(producto)) {
            nombre.setText(producto.getString(PRODUCTO_NOMBRE));
            descripcion.setText(producto.getString(PRODUCTO_DESCRIPCION));
            precio.setText(JavaUtil.formatoMonedaLocal(producto.getDouble(PRODUCTO_PRECIO)));
            refProv.setText(producto.getString(PRODUCTO_REFERENCIA));
            nomProv.setText(producto.getString(PRODUCTO_NOMBREPROV));
            descProv.setText(JavaUtil.formatoMonedaLocal(producto.getDouble(PRODUCTO_DESCPROV)));
            cantidad.setText(JavaUtil.getDecimales(modelo.getDouble(DETPARTIDABASE_CANTIDAD)));
            path = producto.getString(PRODUCTO_RUTAFOTO);
        }

        tipoDetPartida.setText(tipo.toUpperCase());

        if (nn(path)) {
            imagen.setImageUri(path);
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
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_cud_detpartidabase;

    }

    @Override
    protected void setInicio() {

        descripcion = (EditMaterial) ctrl(R.id.etdesccdetpartidabase);
        precio = (EditMaterial) ctrl(R.id.etpreciocdetpartidabase);
        nombre = (EditMaterial) ctrl(R.id.etnomcdetpartidabase);
        precio = (EditMaterial) ctrl(R.id.etpreciocdetpartidabase);
        imagen = (ImagenLayout) ctrl(R.id.imgcdetpartidabase);
        tipoDetPartida = (TextView) ctrl(R.id.tvtipocdetpartidabase);
        nomProv = (EditMaterial) ctrl(R.id.tvnomprovcdetpartidabase);
        refProv = (EditMaterial) ctrl(R.id.tvrefprovcdetpartidabase);
        descProv = (EditMaterial) ctrl(R.id.etdescprovcdetpartidabase);
        cantidad = (EditMaterial) ctrl(R.id.etcantcdetpartidabase, DETPARTIDABASE_CANTIDAD);

        imagen.setClickable(false);
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