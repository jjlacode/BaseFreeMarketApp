package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.codevsolution.freemarketsapp.model.ProdProv;

import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.base.sqlite.ConsultaBD.queryObjectDetalle;

public class FragmentCUDDetpartidaProdProvCat extends FragmentCUD implements Interactor.ConstantesPry,
        ContratoPry.Tablas, Interactor.TiposDetPartida, Interactor.TiposEstados {

    private EditMaterial descripcion;
    private EditMaterial precio;
    private EditMaterial cantidad;
    private EditMaterial descProv;
    private EditMaterial refProv;
    private TextView tipoDetPartida;
    private String tipo;
    private ModeloSQL proyecto;
    private ModeloSQL partida;
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
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected void setNuevo() {


    }

    @Override
    protected void setTabla() {

        tabla = TABLA_DETPARTIDA;

    }

    @Override
    protected void setBundle() {

        proyecto = (ModeloSQL) bundle.getSerializable(PROYECTO);
        partida = (ModeloSQL) bundle.getSerializable(PARTIDA);
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

        modeloSQL = CRUDutil.updateModelo(campos, id, secuencia);

        tipo = modeloSQL.getString(DETPARTIDA_TIPO);
        btndelete.setVisibility(View.VISIBLE);
        tipoDetPartida.setText(tipo.toUpperCase());
        cantidadTot.setText(JavaUtil.getDecimales(((partida.getDouble(PARTIDA_CANTIDAD)) * (modeloSQL.getDouble(DETPARTIDA_CANTIDAD)))));

        if (modeloSQL.getString(DETPARTIDA_RUTAFOTO) != null) {
            mediaUtil = new MediaUtil(contexto);
            path = modeloSQL.getString(DETPARTIDA_RUTAFOTO);
            imagen.setImageFirestoreCircle(path);
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

        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalle);

        imagen = vistaForm.addViewImagenLayout();

        descripcion = (EditMaterial) ctrl(R.id.etdesccdetpartida_cat, DETPARTIDA_DESCRIPCION);
        precio = (EditMaterial) ctrl(R.id.etpreciocdetpartida_cat, DETPARTIDA_PRECIO);
        cantidad = (EditMaterial) ctrl(R.id.etcantcdetpartida_cat, DETPARTIDA_CANTIDAD);
        nombre = (EditMaterial) ctrl(R.id.etnombredetpartida_cat, DETPARTIDA_NOMBRE);
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

        putDato(valores,DETPARTIDA_NOMBRE, nombre.getText().toString());
        putDato(valores,DETPARTIDA_DESCRIPCION, descripcion.getText().toString());
        putDato(valores,DETPARTIDA_CANTIDAD, cantidad.getText().toString());

        putDato(valores,DETPARTIDA_RUTAFOTO, path);
        putDato(valores,DETPARTIDA_ID_DETPARTIDA, idDetPartida);
        putDato(valores,DETPARTIDA_ID_PARTIDA, id);
        putDato(valores,DETPARTIDA_REFPROVCAT, idProv);
        putDato(valores,DETPARTIDA_TIPO, tipo);


        if (partida_completada.isChecked()) {
            putDato(valores,DETPARTIDA_COMPLETA, 1);
        } else {
            putDato(valores,DETPARTIDA_COMPLETA, 0);

        }

        putDato(valores,DETPARTIDA_PRECIO, precio.getText().toString());
        putDato(valores,DETPARTIDA_DESCUENTOPROVCAT, descProv.getText().toString());
        putDato(valores,DETPARTIDA_REFPROVEEDOR, refProv.getText().toString());


        Interactor.Calculos.actualizarPartidaProyecto(id);

    }

    @Override
    protected boolean onBack() {
        tipo = null;
        return super.onBack();
    }

    @Override
    protected void setcambioFragment() {

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