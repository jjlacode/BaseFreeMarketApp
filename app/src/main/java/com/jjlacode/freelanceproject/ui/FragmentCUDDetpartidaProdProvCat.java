package com.jjlacode.freelanceproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.crud.FragmentCUD;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.media.MediaUtil;
import com.jjlacode.freelanceproject.CommonPry;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.model.ProdProv;
import com.jjlacode.freelanceproject.sqlite.ContratoPry;

import static com.jjlacode.base.util.sqlite.ConsultaBD.queryObjectDetalle;

public class FragmentCUDDetpartidaProdProvCat extends FragmentCUD implements CommonPry.Constantes,
        ContratoPry.Tablas, CommonPry.TiposDetPartida, CommonPry.TiposEstados {

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
    private EditMaterial nombre;
    private ProdProv prodProv;
    private String idProv;
    private EditMaterial cantidadTot;


    public FragmentCUDDetpartidaProdProvCat() {
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
        prodProv = (ProdProv) bundle.getSerializable(PRODPROVCAT);

        if (partida != null) {
            secuenciaPartida = partida.getInt(PARTIDA_SECUENCIA);
            idProyecto_Partida = partida.getString(PARTIDA_ID_PROYECTO);
            id = partida.getString(PARTIDA_ID_PARTIDA);
        }
        tipo = TIPOPRODUCTOPROV;

        if (prodProv != null) {

            nombre.setText(prodProv.getNombre());
            descripcion.setText(prodProv.getDescripcion());
            refProv.setText(prodProv.getRefprov());
            precio.setText(JavaUtil.formatoMonedaLocal(prodProv.getPrecio()));
            descProv.setText(JavaUtil.getDecimales(prodProv.getDescProv()));
            idProv = prodProv.getIdprov();
            idDetPartida = prodProv.getId();
            path = prodProv.getRutafoto();

            onUpdate();

            prodProv = null;
        }

    }

    @Override
    protected void setDatos() {

        modelo = CRUDutil.setModelo(campos, id, secuencia);

        tipo = modelo.getString(DETPARTIDA_TIPO);
        btndelete.setVisibility(View.VISIBLE);
        tipoDetPartida.setText(tipo.toUpperCase());
        cantidadTot.setText(JavaUtil.getDecimales(((partida.getDouble(PARTIDA_CANTIDAD)) * (modelo.getDouble(DETPARTIDA_CANTIDAD)))));

        if (modelo.getString(DETPARTIDA_RUTAFOTO) != null) {
            mediaUtil = new MediaUtil(contexto);
            path = modelo.getString(DETPARTIDA_RUTAFOTO);
            setImagenFireStoreCircle(contexto, path, imagen);
        }
    }

    @Override
    protected void setImagen() {

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

        layoutCuerpo = R.layout.fragment_cud_detpartida_prodprovcat;

    }

    @Override
    protected void setInicio() {

        descripcion = (EditMaterial) ctrl(R.id.etdesccdetpartida_cat, DETPARTIDA_DESCRIPCION);
        precio = (EditMaterial) ctrl(R.id.etpreciocdetpartida_cat, DETPARTIDA_PRECIO);
        cantidad = (EditMaterial) ctrl(R.id.etcantcdetpartida_cat, DETPARTIDA_CANTIDAD);
        nombre = (EditMaterial) ctrl(R.id.etnombredetpartida_cat, DETPARTIDA_NOMBRE);
        imagen = (ImageView) ctrl(R.id.imgcdetpartida_cat);
        refProv = (EditMaterial) ctrl(R.id.tvrefprovcdetpartida_cat, DETPARTIDA_REFPROVEEDOR);
        descProv = (EditMaterial) ctrl(R.id.etporcdesprovcdetpartida_cat, DETPARTIDA_DESCUENTOPROVEEDOR);
        tipoDetPartida = (TextView) ctrl(R.id.tvtipocdetpartida_cat);
        partida_completada = (CheckBox) ctrl(R.id.cbox_hacer_detpartida_completa_cat);
        progressBarPartida = (ProgressBar) ctrl(R.id.progressBardetpartida_cat);
        completadaPartida = (EditMaterial) ctrl(R.id.etcompletadadetpartida_cat);
        cantidadTot = (EditMaterial) ctrl(R.id.etcanttotpartida_cat);

    }




    @Override
    protected void setContenedor() {

        setDato(DETPARTIDA_NOMBRE, nombre.getText().toString());
        setDato(DETPARTIDA_DESCRIPCION, descripcion.getText().toString());
        setDato(DETPARTIDA_CANTIDAD, cantidad.getText().toString());

        setDato(DETPARTIDA_RUTAFOTO, path);
        setDato(DETPARTIDA_ID_DETPARTIDA, idDetPartida);
        setDato(DETPARTIDA_ID_PARTIDA, id);
        setDato(DETPARTIDA_REFPROVCAT, idProv);
        setDato(DETPARTIDA_TIPO, tipo);


        if (partida_completada.isChecked()) {
            setDato(DETPARTIDA_COMPLETA, 1);
        } else {
            setDato(DETPARTIDA_COMPLETA, 0);

        }

        setDato(DETPARTIDA_PRECIO, precio.getText().toString());
        setDato(DETPARTIDA_DESCUENTOPROVCAT, descProv.getText().toString());
        setDato(DETPARTIDA_REFPROVEEDOR, refProv.getText().toString());


        CommonPry.Calculos.actualizarPartidaProyecto(id);

    }

    @Override
    protected boolean onBack() {
        tipo = null;
        return super.onBack();
    }

    @Override
    protected void setcambioFragment() {

        bundle = new Bundle();
        new CommonPry.Calculos.TareaActualizaProy().execute(idProyecto_Partida);
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