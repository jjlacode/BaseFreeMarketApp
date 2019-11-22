package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.codevsolution.base.android.controls.EditMaterial;
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

    private EditMaterial descripcion;
    private EditMaterial precio;
    private EditMaterial cantidad;
    private TextView tipoDetPartida;
    private EditMaterial tiempoDet;
    private String tipo;
    private ModeloSQL trabajo;

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


        visible(tiempoDet);
        visible(cantidad);
        gone(btnsave);

        modeloSQL = CRUDutil.updateModelo(campos, id, secuencia);
        trabajo = CRUDutil.updateModelo(CAMPOS_TRABAJO, modeloSQL.getString(DETPARTIDABASE_ID_DETPARTIDABASE));

        tipo = TRABAJO;
        tipoDetPartida.setText(tipo.toUpperCase());
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
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_cud_detpartidabase;

    }

    @Override
    protected void setInicio() {

        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalle);

        imagen = vistaForm.addViewImagenLayout();

        descripcion = (EditMaterial) ctrl(R.id.etdesccdetpartidabase);
        precio = (EditMaterial) ctrl(R.id.etpreciocdetpartidabase);
        cantidad = (EditMaterial) ctrl(R.id.etcantcdetpartidabase,DETPARTIDABASE_CANTIDAD);
        nombre = (EditMaterial) ctrl(R.id.etnomcdetpartidabase);
        tiempoDet = (EditMaterial) ctrl(R.id.ettiempocdetpartidabase);
        tipoDetPartida = (TextView) ctrl(R.id.tvtipocdetpartidabase);
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