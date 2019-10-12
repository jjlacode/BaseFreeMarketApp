package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCUD;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import static com.codevsolution.base.sqlite.ConsultaBD.queryObjectDetalle;

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
    private EditMaterial preciotot;


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
        partida = (Modelo) bundle.getSerializable(TABLA_PARTIDA);
        if (nn(partida) && partida.getInt(PARTIDA_TIPO_ESTADO)==TNUEVOPRESUP) {
            producto = CRUDutil.updateModelo(CAMPOS_PRODUCTO, modelo.getString(DETPARTIDA_ID_DETPARTIDA));
        }

        if (nn(partida)) {
            secuenciaPartida = partida.getInt(PARTIDA_SECUENCIA);
            idProyecto_Partida = partida.getString(PARTIDA_ID_PROYECTO);
            cantidadPartida.setText(JavaUtil.getDecimales(partida.getDouble(PARTIDA_CANTIDAD)));

        }
        tipo = TIPOPRODUCTO;


    }

    @Override
    protected void setDatos() {

        modelo = CRUDutil.updateModelo(campos, id, secuencia);

        tipoDetPartida.setText(tipo.toUpperCase());

        completadaPartida.setVisibility(View.VISIBLE);
        progressBarPartida.setVisibility(View.VISIBLE);
        completada = modelo.getDouble(DETPARTIDA_COMPLETADA);
        cantidad.setText(modelo.getString(DETPARTIDA_CANTIDAD));
        if (nn(producto)) {
            nombre.setText(producto.getString(PRODUCTO_NOMBRE));
            descripcion.setText(producto.getString(PRODUCTO_DESCRIPCION));
            precio.setText(JavaUtil.formatoMonedaLocal(producto.getDouble(PRODUCTO_PRECIO)));
            refProv.setText(producto.getString(PRODUCTO_REFERENCIA));
            nomProv.setText(producto.getString(PRODUCTO_NOMBREPROV));
            descProv.setText(producto.getString(PRODUCTO_DESCPROV));
            idDetPartida = producto.getString(PRODUCTO_ID_PRODUCTO);
            idProv = producto.getString(PRODUCTO_ID_PROVEEDOR);
            path = producto.getString(PRODUCTO_RUTAFOTO);

        } else{

            nombre.setText(modelo.getString(DETPARTIDA_NOMBRE));
            descripcion.setText(modelo.getString(DETPARTIDA_DESCRIPCION));
            precio.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(DETPARTIDA_PRECIO)));
            refProv.setText(modelo.getString(DETPARTIDA_REFPROVEEDOR));
            nomProv.setText(modelo.getString(DETPARTIDA_PROVEEDOR));
            descProv.setText(modelo.getString(DETPARTIDA_DESCUENTOPROVEEDOR));
            idDetPartida = modelo.getString(DETPARTIDA_ID_DETPARTIDA);
            idProv = modelo.getString(DETPARTIDA_ID_PROVEEDOR);
            path = modelo.getString(DETPARTIDA_RUTAFOTO);

        }

        if (modelo.getString(DETPARTIDA_RUTAFOTO) != null) {
            path = modelo.getString(DETPARTIDA_RUTAFOTO);
            imagen.setImageUri(modelo.getString(DETPARTIDA_RUTAFOTO));
        }
        if (nn(partida) && nn(producto)) {
            preciotot.setText(JavaUtil.formatoMonedaLocal
                    ((partida.getDouble(PARTIDA_CANTIDAD) * producto.getDouble(PRODUCTO_PRECIO)
                            *modelo.getDouble(DETPARTIDA_CANTIDAD))));
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

        descripcion = (EditMaterial) ctrl(R.id.etdesccdetpartida_prod);
        precio = (EditMaterial) ctrl(R.id.etpreciocdetpartida_prod);
        preciotot = (EditMaterial) ctrl(R.id.etpreciototcdetpartida_prod);
        cantidad = (EditMaterial) ctrl(R.id.etcantcdetpartida_prod);
        cantidadPartida = (EditMaterial) ctrl(R.id.etcanttotpartida_prod);
        nombre = (EditMaterial) ctrl(R.id.etnombredetpartida_prod);
        imagen = (ImagenLayout) ctrl(R.id.imgcdetpartida_prod);
        refProv = (EditMaterial) ctrl(R.id.tvrefprovcdetpartida_prod);
        nomProv = (EditMaterial) ctrl(R.id.tvnomprovcdetpartida_prod);
        descProv = (EditMaterial) ctrl(R.id.etporcdesprovcdetpartida_prod);
        tipoDetPartida = (TextView) ctrl(R.id.tvtipocdetpartida_prod);
        partida_completada = (CheckBox) ctrl(R.id.cbox_hacer_detpartida_completa_prod);
        progressBarPartida = (ProgressBar) ctrl(R.id.progressBardetpartida_prod);
        completadaPartida = (EditMaterial) ctrl(R.id.etcompletadadetpartida_prod);

    }




    @Override
    protected void setContenedor() {

        setDato(DETPARTIDA_ID_DETPARTIDA, idDetPartida);
        setDato(DETPARTIDA_ID_PARTIDA, id);
        setDato(DETPARTIDA_TIPO, tipo);
        setDato(DETPARTIDA_NOMBRE,nombre.getTexto());
        setDato(DETPARTIDA_DESCRIPCION,descripcion.getTexto());
        setDato(DETPARTIDA_PRECIO,JavaUtil.comprobarDouble(precio.getTexto()));
        setDato(DETPARTIDA_REFPROVEEDOR,refProv.getTexto());
        setDato(DETPARTIDA_PROVEEDOR,nomProv.getTexto());
        setDato(DETPARTIDA_DESCUENTOPROVEEDOR,descProv.getTexto());
        setDato(DETPARTIDA_ID_PROVEEDOR,idProv);
        setDato(DETPARTIDA_RUTAFOTO,path);

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