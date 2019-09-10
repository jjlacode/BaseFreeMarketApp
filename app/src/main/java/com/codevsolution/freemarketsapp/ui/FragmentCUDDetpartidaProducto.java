package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codevsolution.base.util.JavaUtil;
import com.codevsolution.base.util.android.controls.EditMaterial;
import com.codevsolution.base.util.android.controls.ImagenLayout;
import com.codevsolution.base.util.crud.CRUDutil;
import com.codevsolution.base.util.crud.FragmentCUD;
import com.codevsolution.base.util.models.Modelo;
import com.codevsolution.base.util.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import static com.codevsolution.base.util.sqlite.ConsultaBD.queryObjectDetalle;

public class FragmentCUDDetpartidaProducto extends FragmentCUD implements Interactor.ConstantesPry,
        ContratoPry.Tablas, Interactor.TiposDetPartida, Interactor.TiposEstados {

    private EditMaterial nombre;
    private EditMaterial descripcion;
    private EditMaterial precio;
    private EditMaterial cantidad;
    private EditMaterial descProv;
    private EditMaterial refProv;
    private TextView tipoDetPartida;
    private String tipo;
    private Modelo proyecto;
    private Modelo partida;
    private String idDetPartida;

    private String idProyecto_Partida;
    private int secuenciaPartida;
    private ProgressBar progressBarPartida;
    private EditMaterial completadaPartida;

    private CheckBox partida_completada;
    private Modelo producto;
    private double completada;
    private EditMaterial cantidadPartida;
    private String idProv;
    private EditMaterial nomProv;


    public FragmentCUDDetpartidaProducto() {
        // Required empty public constructor
    }


    @Override
    protected void setNuevo() {

    }

    @Override
    protected void setTabla() {

        tabla = TABLA_DETPARTIDA;

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

        proyecto = (Modelo) bundle.getSerializable(PROYECTO);
        partida = (Modelo) bundle.getSerializable(PARTIDA);
        producto = (Modelo) bundle.getSerializable(PRODUCTO);

        if (partida != null) {
            secuenciaPartida = partida.getInt(PARTIDA_SECUENCIA);
            idProyecto_Partida = partida.getString(PARTIDA_ID_PROYECTO);
        }
        tipo = TIPOPRODUCTO;

        if (producto != null) {
            nombre.setText(producto.getString(PRODUCTO_NOMBRE));
            descripcion.setText(producto.getString(PRODUCTO_DESCRIPCION));
            precio.setText(JavaUtil.formatoMonedaLocal(producto.getDouble(PRODUCTO_PRECIO)));
            refProv.setText(producto.getString(PRODUCTO_REFERENCIA));
            nomProv.setText(producto.getString(PRODUCTO_NOMBREPROV));
            idDetPartida = producto.getString(PRODUCTO_ID_PRODUCTO);
            idProv = producto.getString(PRODUCTO_ID_PROVEEDOR);
            path = producto.getString(PRODUCTO_RUTAFOTO);
        }

    }

    @Override
    protected void setDatos() {

        modelo = CRUDutil.setModelo(campos, id, secuencia);

        tipoDetPartida.setText(tipo.toUpperCase());

        completadaPartida.setVisibility(View.VISIBLE);
        progressBarPartida.setVisibility(View.VISIBLE);
        completada = modelo.getDouble(DETPARTIDA_COMPLETADA);


        if (modelo.getString(DETPARTIDA_RUTAFOTO) != null) {
            path = modelo.getString(DETPARTIDA_RUTAFOTO);
            imagen.setImageUri(modelo.getString(DETPARTIDA_RUTAFOTO));
        }

    }

    @Override
    protected void setAcciones() {


    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.detpartida;
        tituloPlural = tituloSingular;
        tituloNuevo = R.string.nuevo_detalle_partida;
    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_cud_detpartida_producto;

    }

    @Override
    protected void setInicio() {

        descripcion = (EditMaterial) ctrl(R.id.etdesccdetpartida_prod, DETPARTIDA_DESCRIPCION);
        precio = (EditMaterial) ctrl(R.id.etpreciocdetpartida_prod, DETPARTIDA_PRECIO);
        cantidad = (EditMaterial) ctrl(R.id.etcantcdetpartida_prod, DETPARTIDA_CANTIDAD);
        cantidadPartida = (EditMaterial) ctrl(R.id.etcanttotpartida_prod);
        nombre = (EditMaterial) ctrl(R.id.etnombredetpartida_prod, DETPARTIDA_NOMBRE);
        imagen = (ImagenLayout) ctrl(R.id.imgcdetpartida_prod);
        refProv = (EditMaterial) ctrl(R.id.tvrefprovcdetpartida_prod, DETPARTIDA_REFPROVEEDOR);
        descProv = (EditMaterial) ctrl(R.id.etporcdesprovcdetpartida_prod, DETPARTIDA_DESCUENTOPROVEEDOR);
        tipoDetPartida = (TextView) ctrl(R.id.tvtipocdetpartida_prod, DETPARTIDA_TIPO);
        partida_completada = (CheckBox) ctrl(R.id.cbox_hacer_detpartida_completa_prod);
        progressBarPartida = (ProgressBar) ctrl(R.id.progressBardetpartida_prod);
        completadaPartida = (EditMaterial) ctrl(R.id.etcompletadadetpartida_prod);

    }




    @Override
    protected void setContenedor() {

        setDato(DETPARTIDA_ID_DETPARTIDA, idDetPartida);
        setDato(DETPARTIDA_ID_PARTIDA, id);
        setDato(DETPARTIDA_TIPO, tipo);

        if (partida_completada.isChecked()) {
            setDato(DETPARTIDA_COMPLETA, 1);
        } else {
            setDato(DETPARTIDA_COMPLETA, 0);

        }

        Interactor.Calculos.actualizarPartidaProyecto(id);

    }


    @Override
    protected boolean update() {
        if (super.update()) {
            return true;
        }
        return false;
    }

    @Override
    protected void setcambioFragment() {

        if (origen.equals(PARTIDA)) {
            bundle = new Bundle();
            new Interactor.Calculos.TareaActualizaProy().execute(idProyecto_Partida);
            partida = queryObjectDetalle(CAMPOS_PARTIDA, idProyecto_Partida, secuenciaPartida);
            bundle.putSerializable(MODELO, partida);
            bundle.putSerializable(PROYECTO, proyecto);
            bundle.putString(ORIGEN, DETPARTIDA);
            bundle.putString(SUBTITULO, subTitulo);
            bundle.putString(CAMPO_ID, idProyecto_Partida);
            bundle.putInt(CAMPO_SECUENCIA, secuenciaPartida);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaProyecto());
            bundle = null;
        }

    }

}