package com.codevsolution.freemarketsapp.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;
import java.util.Locale;

import static com.codevsolution.base.javautil.JavaUtil.hoy;
import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.base.sqlite.ConsultaBD.queryList;
import static com.codevsolution.base.sqlite.ConsultaBD.queryObject;
import static com.codevsolution.base.sqlite.ConsultaBD.queryObjectDetalle;
import static com.codevsolution.base.sqlite.ConsultaBD.updateRegistroDetalle;

public class FragmentCUDDetpartidaBaseTrabajo extends FragmentCUD implements Interactor.ConstantesPry,
        ContratoPry.Tablas, Interactor.TiposDetPartida, Interactor.TiposEstados {

    private EditMaterial descripcion;
    private EditMaterial precio;
    private TextView tipoDetPartida;
    private EditMaterial tiempoDet;
    private String tipo;
    private Modelo trabajo;

    private EditMaterial nombre;

    public FragmentCUDDetpartidaBaseTrabajo() {
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
        visible(tiempoDet);
        visible(precio);
        visible(tipoDetPartida);
        gone(btnsave);

        modelo = CRUDutil.setModelo(campos, id, secuencia);
        trabajo = CRUDutil.setModelo(CAMPOS_TRABAJO, modelo.getString(DETPARTIDABASE_ID_DETPARTIDABASE));

        tipo = TRABAJO;
        tipoDetPartida.setText(tipo.toUpperCase());
        nombre.setText(trabajo.getString(TRABAJO_NOMBRE));
        descripcion.setText(trabajo.getString(TRABAJO_DESCRIPCION));
        precio.setText(JavaUtil.formatoMonedaLocal(trabajo.getDouble(TRABAJO_TIEMPO) * Interactor.hora));
        tiempoDet.setText(trabajo.getString(TRABAJO_TIEMPO));
        path = trabajo.getString(TRABAJO_RUTAFOTO);

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
        tiempoDet = (EditMaterial) ctrl(R.id.ettiempocdetpartidabase);
        imagen = (ImagenLayout) ctrl(R.id.imgcdetpartidabase);
        tipoDetPartida = (TextView) ctrl(R.id.tvtipocdetpartidabase);
        imagen.setClickable(false);

    }

    @Override
    protected void setContenedor() {

        putDato(valores, campos, DETPARTIDABASE_CANTIDAD, 1);
    }

    @Override
    protected void setcambioFragment() {

        bundle = new Bundle();
        putBundle(CAMPO_ID, id);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaBase());

    }

}