package com.codevsolution.freemarketsapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

import static com.codevsolution.base.sqlite.ConsultaBD.checkQueryList;
import static com.codevsolution.base.sqlite.ConsultaBD.putDato;

public class FragmentCRUDProveedor extends FragmentCRUD implements Interactor.ConstantesPry {

    private CheckBox activo;
    private long fechaInactivo;
    private ImageButton btnVerNotas;
    private ImageButton btnNota;
    private ImageButton btnevento;
    private ImageButton btnVerEventos;
    private ModeloSQL producto;
    private Button addProducto;
    private ModeloSQL partida;
    private ModeloSQL proyecto;
    private EditMaterialLayout nombre;

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    @Override
    protected void setBundle() {
        super.setBundle();

        producto = (ModeloSQL) getBundleSerial(PRODUCTO);
        partida = (ModeloSQL) getBundleSerial(PARTIDA);
        proyecto = (ModeloSQL) getBundleSerial(PROYECTO);

    }

    @Override
    protected void setContenedor() {

        putDato(valores,PROVEEDOR_ACTIVO, fechaInactivo);
    }


    @Override
    protected void setTabla() {

        tabla = TABLA_PROVEEDOR;
    }

    @Override
    protected void setTitulo() {

        tituloSingular = R.string.proveedor;
        tituloPlural = R.string.proveedores;
        tituloNuevo = R.string.nuevo_proveedor;
    }

    @Override
    protected void setLayout() {

        //layoutCuerpo = R.layout.fragment_crud_proveedor;
        layoutItem = R.layout.item_list_proveedor;
    }

    @Override
    protected void setInicio() {

        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalle);
        addProducto = vistaForm.addButtonPrimary(R.string.add_producto);
        addProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeloSQL = CRUDutil.updateModelo(campos, id);
                bundle = new Bundle();
                putBundle(MODELO, producto);
                putBundle(PROVEEDOR, modeloSQL);
                putBundle(PARTIDA, partida);
                putBundle(PROYECTO, proyecto);
                putBundle(ORIGEN, origen);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProducto());
            }
        });
        imagen = vistaForm.addViewImagenLayout();
        imagen.getLinearLayoutCompat().setFocusable(false);
        nombre = vistaForm.addEditMaterialLayout(getString(R.string.nombre), PROVEEDOR_NOMBRE, null, null);
        vistaForm.addEditMaterialLayout(getString(R.string.direccion), PROVEEDOR_DIRECCION, ViewGroupLayout.MAPA, null);
        vistaForm.addEditMaterialLayout(getString(R.string.email), PROVEEDOR_EMAIL, ViewGroupLayout.MAIL, null);
        vistaForm.addEditMaterialLayout(getString(R.string.telefono), PROVEEDOR_TELEFONO, ViewGroupLayout.LLAMADA, activityBase);
        vistaForm.addEditMaterialLayout(getString(R.string.contacto), PROVEEDOR_CONTACTO, null, null);
        activo = (CheckBox) vistaForm.addVista(new CheckBox(contexto));
        activo.setText(R.string.activo);
        activo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!b) {
                    fechaInactivo = TimeDateUtil.ahora();
                } else {
                    fechaInactivo = 0;
                }
            }
        });

        actualizarArrays(vistaForm);

        ViewGroupLayout vistaBotones = new ViewGroupLayout(contexto, frdetalle);
        vistaBotones.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        btnevento = vistaBotones.addImageButtonSecundary(R.drawable.ic_evento_indigo);
        btnevento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(CLIENTE, modeloSQL);
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevoEvento());
            }
        });

        btnVerEventos = vistaBotones.addImageButtonSecundary(R.drawable.ic_lista_eventos_indigo);
        btnVerEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(LISTA, new ListaModeloSQL(CAMPOS_EVENTO, EVENTO_CLIENTEREL, id));
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
            }
        });
        btnNota = vistaBotones.addImageButtonSecundary(R.drawable.ic_nueva_nota_indigo);
        btnNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putString(IDREL, modeloSQL.getString(PROVEEDOR_ID_PROVEEDOR));
                bundle.putString(SUBTITULO, modeloSQL.getString(PROVEEDOR_NOMBRE));
                bundle.putString(ORIGEN, PROVEEDOR);
                bundle.putSerializable(MODELO, null);
                bundle.putSerializable(LISTA, null);
                bundle.putString(CAMPO_ID, null);
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevaNota());
            }
        });
        btnVerNotas = vistaBotones.addImageButtonSecundary(R.drawable.ic_lista_notas_indigo);
        btnVerNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarBundle();
                bundle.putString(IDREL, modeloSQL.getString(PROVEEDOR_ID_PROVEEDOR));
                bundle.putString(SUBTITULO, modeloSQL.getString(PROVEEDOR_NOMBRE));
                bundle.putString(ORIGEN, PROVEEDOR);
                bundle.putString(ACTUAL, NOTA);
                bundle.putSerializable(LISTA, null);
                bundle.putSerializable(MODELO, null);
                bundle.putString(CAMPO_ID, null);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
            }
        });

        actualizarArrays(vistaBotones);
    }

    @Override
    protected void setNuevo() {
        super.setNuevo();
        gone(btnevento);
        gone(btnNota);
        gone(btnVerEventos);
        gone(btnVerNotas);
        nombre.setText(getString(R.string.nuevo_proveedor));

    }

    @Override
    protected void setDatos() {

        if (origen == null) {
            origen = PROVEEDOR;
        }

        System.out.println("origen proveedor= " + origen);
        if ((origen.equals(PARTIDA) || origen.equals(PRODUCTO))) {
            visible(addProducto);
        } else {
            gone(addProducto);
        }

        visible(btnevento);
        visible(btnNota);

        fechaInactivo = modeloSQL.getLong(PROVEEDOR_ACTIVO);
        if (fechaInactivo > 0) {
            activo.setChecked(false);
        } else {
            activo.setChecked(true);
        }

        String seleccion = EVENTO_CLIENTEREL + " = '" + id + "'";
        if (checkQueryList(CAMPOS_EVENTO, seleccion, null)) {
            btnVerEventos.setVisibility(View.VISIBLE);
        } else {
            btnVerEventos.setVisibility(View.GONE);
        }

        seleccion = NOTA_ID_RELACIONADO + " = '" + id + "'";
        if (checkQueryList(CAMPOS_NOTA, seleccion, null)) {
            btnVerNotas.setVisibility(View.VISIBLE);
        } else {
            btnVerNotas.setVisibility(View.GONE);
        }


    }


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView nombre, direccion, telefono, email, contacto;
        ImageView imagenProveedor;
        CardView card;

        public ViewHolderRV(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvnomlproveedor);
            direccion = itemView.findViewById(R.id.tvdirlproveedor);
            telefono = itemView.findViewById(R.id.tvtellproveedor);
            email = itemView.findViewById(R.id.tvemaillproveedor);
            contacto = itemView.findViewById(R.id.tvcontaclproveedor);
            imagenProveedor = itemView.findViewById(R.id.imglproveedor);
            card = itemView.findViewById(R.id.cardproveedor);
        }

        @Override
        public void bind(ModeloSQL modeloSQL) {

            nombre.setText(modeloSQL.getCampos(PROVEEDOR_NOMBRE));
            direccion.setText(modeloSQL.getCampos(PROVEEDOR_DIRECCION));
            telefono.setText(modeloSQL.getCampos(PROVEEDOR_TELEFONO));
            email.setText(modeloSQL.getCampos(PROVEEDOR_EMAIL));
            contacto.setText(modeloSQL.getCampos(PROVEEDOR_CONTACTO));

            String path = modeloSQL.getString(PROVEEDOR_RUTAFOTO);
            if (path != null) {
                new MediaUtil(contexto).setImageUriCircle(path, imagenProveedor);
            }
            if (modeloSQL.getLong(PROVEEDOR_ACTIVO) > 0) {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
            } else {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_defecto));
            }

            super.bind(modeloSQL);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<ModeloSQL> entrada) {

            ImageView imagenProveedor = view.findViewById(R.id.imglproveedor);
            TextView nombre = view.findViewById(R.id.tvnomlproveedor);
            TextView contacto = view.findViewById(R.id.tvcontaclproveedor);
            TextView telefono = view.findViewById(R.id.tvtellproveedor);
            TextView email = view.findViewById(R.id.tvemaillproveedor);
            TextView dir = view.findViewById(R.id.tvdirlproveedor);

            nombre.setText(entrada.get(posicion).getCampos(PROVEEDOR_NOMBRE));
            contacto.setText(entrada.get(posicion).getCampos(PROVEEDOR_CONTACTO));
            telefono.setText(entrada.get(posicion).getCampos(PROVEEDOR_TELEFONO));
            email.setText(entrada.get(posicion).getCampos(PROVEEDOR_EMAIL));
            dir.setText(entrada.get(posicion).getCampos(PROVEEDOR_DIRECCION));

            String path = entrada.get(posicion).getString(PROVEEDOR_RUTAFOTO);
            if (path != null) {
                new MediaUtil(contexto).setImageUriCircle(path, imagenProveedor);
            }

            super.setEntradas(posicion, view, entrada);
        }
    }
}
