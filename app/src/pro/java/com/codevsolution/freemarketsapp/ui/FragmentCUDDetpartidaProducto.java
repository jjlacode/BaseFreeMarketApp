package com.codevsolution.freemarketsapp.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.Timer;
import java.util.TimerTask;

import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.base.sqlite.ConsultaBD.queryObjectDetalle;

public class FragmentCUDDetpartidaProducto extends FragmentCUD implements Interactor.ConstantesPry,
        ContratoPry.Tablas, Interactor.TiposDetPartida, Interactor.TiposEstados {

    private EditMaterialLayout nombre;
    private EditMaterialLayout descripcion;
    private EditMaterialLayout precio;
    private EditMaterialLayout cantidad;
    private EditMaterialLayout descProv;
    private EditMaterialLayout refProv;
    private TextView tipoDetPartida;
    private String tipo;
    private ModeloSQL proyecto;
    private ModeloSQL partida;
    private String idDetPartida;

    private String idProyecto_Partida;
    private int secuenciaPartida;
    private ProgressBar progressBarPartida;
    private EditMaterialLayout completadaPartida;

    private CheckBox partida_completada;
    private ModeloSQL producto;
    private double completada;
    private EditMaterialLayout cantidadPartida;
    private String idProv;
    private EditMaterialLayout nomProv;
    private EditMaterialLayout preciotot;
    private EditMaterialLayout etBeneficio;
    private double cantPart;
    private double cant;


    public FragmentCUDDetpartidaProducto() {
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
        partida = (ModeloSQL) bundle.getSerializable(TABLA_PARTIDA);
        if (nn(partida) && partida.getInt(PARTIDA_TIPO_ESTADO)==TNUEVOPRESUP) {
            producto = CRUDutil.updateModelo(CAMPOS_PRODUCTO, modeloSQL.getString(DETPARTIDA_ID_DETPARTIDA));
        }

        if (nn(partida)) {
            secuenciaPartida = partida.getInt(PARTIDA_SECUENCIA);
            idProyecto_Partida = partida.getString(PARTIDA_ID_PROYECTO);
            cantPart = partida.getDouble(PARTIDA_CANTIDAD);
            cantidadPartida.setText(JavaUtil.getDecimales(cantPart));

        }
        tipo = TIPOPRODUCTO;


    }

    @Override
    protected void setDatos() {

        modeloSQL = CRUDutil.updateModelo(campos, id, secuencia);

        tipoDetPartida.setText(tipo.toUpperCase());

        completadaPartida.getLinearLayout().setVisibility(View.VISIBLE);
        completada = modeloSQL.getDouble(DETPARTIDA_COMPLETADA);
        AndroidUtil.bars(contexto, progressBarPartida, null, false, 100, 90, 120, completada,
                completadaPartida.getEditText(), null, R.color.Color_contador_ok, R.color.Color_contador_acept,
                R.color.Color_contador_notok);
        progressBarPartida.setProgress((int) completada);
        cant = modeloSQL.getDouble(DETPARTIDA_CANTIDAD);
        cantidad.setText(JavaUtil.getDecimales(cant));
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

            nombre.setText(modeloSQL.getString(DETPARTIDA_NOMBRE));
            descripcion.setText(modeloSQL.getString(DETPARTIDA_DESCRIPCION));
            precio.setText(JavaUtil.formatoMonedaLocal(modeloSQL.getDouble(DETPARTIDA_PRECIO)));
            refProv.setText(modeloSQL.getString(DETPARTIDA_REFPROVEEDOR));
            nomProv.setText(modeloSQL.getString(DETPARTIDA_PROVEEDOR));
            descProv.setText(modeloSQL.getString(DETPARTIDA_DESCUENTOPROVEEDOR));
            idDetPartida = modeloSQL.getString(DETPARTIDA_ID_DETPARTIDA);
            idProv = modeloSQL.getString(DETPARTIDA_ID_PROVEEDOR);
            path = modeloSQL.getString(DETPARTIDA_RUTAFOTO);

        }

        if (modeloSQL.getString(DETPARTIDA_RUTAFOTO) != null) {
            path = modeloSQL.getString(DETPARTIDA_RUTAFOTO);
            imagen.setImageUri(modeloSQL.getString(DETPARTIDA_RUTAFOTO));
        }
        if (nn(partida) && nn(producto)) {
            preciotot.setText(JavaUtil.formatoMonedaLocal
                    ((cantPart * producto.getDouble(PRODUCTO_PRECIO)
                            * cant)));
        }

        valores = new ContentValues();
        if (partida_completada.isChecked()) {
            CRUDutil.actualizarCampo(modeloSQL, DETPARTIDA_COMPLETA, 1);
        } else {
            CRUDutil.actualizarCampo(modeloSQL, DETPARTIDA_COMPLETA, 0);
        }


    }

    @Override
    protected void setAcciones() {

        if (partida.getInt(PARTIDA_TIPO_ESTADO) == TNUEVOPRESUP) {

            etBeneficio.setAlCambiarListener(new EditMaterialLayout.AlCambiarListener() {
                @Override
                public void antesCambio(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void cambiando(CharSequence s, int start, int before, int count) {

                    if (timer != null) {
                        timer.cancel();
                    }
                }

                @Override
                public void despuesCambio(Editable s) {

                    preciotot.setText(JavaUtil.formatoMonedaLocal(modeloSQL.getDouble(DETPARTIDA_PRECIO) *
                            cantPart * cant * (1 + ((JavaUtil.comprobarDouble(s.toString())) / 100))));

                    final Editable temp = s;
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            activityBase.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (temp.toString().equals("")) {
                                        etBeneficio.setText("0 %");
                                    }

                                    valores = new ContentValues();
                                    putDato(valores,DETPARTIDA_BENEFICIO, JavaUtil.comprobarDouble(etBeneficio.getTexto()));
                                    CRUDutil.actualizarRegistro(modeloSQL, valores);
                                    modeloSQL = CRUDutil.updateModelo(modeloSQL);
                                    Interactor.Calculos.actualizarPartidaProyecto(id);

                                }
                            });


                        }
                    }, 2000);

                }
            });
        } else {
            etBeneficio.setActivo(false);
        }

        partida_completada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                valores = new ContentValues();
                if (b) {
                    CRUDutil.actualizarCampo(modeloSQL, DETPARTIDA_COMPLETA, 1);
                } else {
                    CRUDutil.actualizarCampo(modeloSQL, DETPARTIDA_COMPLETA, 0);
                }
            }
        });

    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.detpartida;
        tituloPlural = tituloSingular;
        tituloNuevo = R.string.nuevo_detalle_partida;
    }

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

        visible(frdetalle);
        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalle);

        tipoDetPartida = vistaForm.addTextView(null);
        imagen = vistaForm.addViewImagenLayout();
        imagen.getLinearLayoutCompat().setFocusable(false);
        imagen.getImagen().setClickable(false);
        imagen.setTextTitulo(tituloSingular);
        nombre = vistaForm.addEditMaterialLayout(getString(R.string.nombre));
        nombre.setActivo(false);
        refProv = vistaForm.addEditMaterialLayout(getString(R.string.referencia_proveedor));
        refProv.setActivo(false);
        nomProv = vistaForm.addEditMaterialLayout(getString(R.string.nombre_producto_proveedor));
        nomProv.setActivo(false);
        descripcion = vistaForm.addEditMaterialLayout(getString(R.string.descripcion));
        descripcion.setActivo(false);

        ViewGroupLayout vistaCant = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaCant.setOrientacion(Estilos.Constantes.ORI_LLC_HORIZONTAL);
        cantidad = vistaCant.addEditMaterialLayout(R.string.cantidad, DETPARTIDA_CANTIDAD, 1);
        cantidadPartida = vistaCant.addEditMaterialLayout(R.string.cantidad_partida, 1);
        cantidadPartida.setActivo(false);
        descProv = vistaCant.addEditMaterialLayout(R.string.descuento_proveedor, DETPARTIDA_DESCUENTOPROVEEDOR, 1);
        actualizarArrays(vistaCant);

        ViewGroupLayout vistaPrecio = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaPrecio.setOrientacion(Estilos.Constantes.ORI_LLC_HORIZONTAL);
        precio = vistaPrecio.addEditMaterialLayout(R.string.importe, 1);
        precio.setActivo(false);
        etBeneficio = vistaPrecio.addEditMaterialLayout(R.string.beneficio, 1);
        preciotot = vistaPrecio.addEditMaterialLayout(R.string.importe_total, 1);
        preciotot.setActivo(false);
        actualizarArrays(vistaPrecio);

        completadaPartida = vistaForm.addEditMaterialLayout(R.string.completada, DETPARTIDA_COMPLETADA);
        progressBarPartida = (ProgressBar) vistaForm.addVista(new ProgressBar(contexto, null, Estilos.pBarStyleAcept(contexto)));

        partida_completada = (CheckBox) vistaForm.addVista(new CheckBox(contexto));
        partida_completada.setText(R.string.completa);

        actualizarArrays(vistaForm);

    }

    @Override
    protected void setContenedor() {

    }

    @Override
    protected boolean onUpdate() {

        Interactor.Calculos.actualizarPartidaProyecto(id);

        return super.onUpdate();
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