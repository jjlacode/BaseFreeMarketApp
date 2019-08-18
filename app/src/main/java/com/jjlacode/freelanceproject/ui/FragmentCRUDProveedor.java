package com.jjlacode.freelanceproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.jjlacode.base.util.adapter.BaseViewHolder;
import com.jjlacode.base.util.adapter.ListaAdaptadorFiltroModelo;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.base.util.android.AppActivity;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.base.util.android.controls.ImagenLayout;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.crud.FragmentCRUD;
import com.jjlacode.base.util.crud.ListaModelo;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.media.MediaUtil;
import com.jjlacode.base.util.sqlite.ContratoPry;
import com.jjlacode.freelanceproject.R;

import java.util.ArrayList;

import static com.jjlacode.base.util.sqlite.ConsultaBD.checkQueryList;

public class FragmentCRUDProveedor extends FragmentCRUD {

    private EditMaterial nombre;
    private EditMaterial direccion;
    private EditMaterial telefono;
    private EditMaterial email;
    private EditMaterial contacto;
    private CheckBox activo;
    private long fechaInactivo;
    private ImageButton btnVerNotas;
    private ImageButton btnNota;
    private ImageButton btnevento;
    private ImageButton btnVerEventos;
    private ImageButton mail;
    private ImageButton llamada;
    private ImageButton mapa;
    private Modelo producto;
    private Button addProducto;
    private Modelo partida;
    private Modelo proyecto;


    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    @Override
    protected void setBundle() {
        super.setBundle();
        producto = (Modelo) getBundleSerial(PRODUCTO);
        partida = (Modelo) getBundleSerial(PARTIDA);
        proyecto = (Modelo) getBundleSerial(PROYECTO);

    }

    @Override
    protected void setContenedor() {

        setDato(PROVEEDOR_ACTIVO, fechaInactivo);
    }

    @Override
    protected void setDatos() {

        System.out.println("origen proveedor= " + origen);
        if ((origen.equals(PARTIDA) || origen.equals(PRODUCTO))) {
            visible(addProducto);
        } else {
            gone(addProducto);
        }

        visible(btnevento);
        visible(btnNota);

        fechaInactivo = modelo.getLong(PROVEEDOR_ACTIVO);
        if (fechaInactivo > 0) {
            activo.setChecked(true);
        } else {
            activo.setChecked(false);
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

    @Override
    protected void setTabla() {

        tabla = TABLA_PROVEEDOR;
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
    protected void setTitulo() {

        tituloSingular = R.string.proveedor;
        tituloPlural = R.string.proveedores;
        tituloNuevo = R.string.nuevo_proveedor;
    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_proveedor;
        layoutItem = R.layout.item_list_proveedor;
    }

    @Override
    protected void setInicio() {

        nombre = (EditMaterial) ctrl(R.id.etnombreproveedor, PROVEEDOR_NOMBRE);
        direccion = (EditMaterial) ctrl(R.id.etdirproveedor, PROVEEDOR_DIRECCION);
        telefono = (EditMaterial) ctrl(R.id.ettelproveedor, PROVEEDOR_TELEFONO);
        email = (EditMaterial) ctrl(R.id.etemailproveedor, PROVEEDOR_EMAIL);
        contacto = (EditMaterial) ctrl(R.id.etcontproveedor, PROVEEDOR_CONTACTO);
        activo = (CheckBox) ctrl(R.id.chactivoproveedor);
        btnevento = (ImageButton) ctrl(R.id.btneventoproveedor);
        btnVerEventos = (ImageButton) ctrl(R.id.btnvereventoproveedor);
        btnNota = (ImageButton) ctrl(R.id.btn_crearnota_proveedor);
        btnVerNotas = (ImageButton) ctrl(R.id.btn_vernotas_proveedor);
        imagen = (ImagenLayout) ctrl(R.id.imgproveedor);
        mapa = (ImageButton) ctrl(R.id.imgbtndirproveedor);
        llamada = (ImageButton) ctrl(R.id.imgbtntelproveedor);
        mail = (ImageButton) ctrl(R.id.imgbtnmailproveedor);
        addProducto = (Button) ctrl(R.id.btn_add_proveedor_producto);
        mapa.setFocusable(false);
        llamada.setFocusable(false);
        mail.setFocusable(false);
        imagen.setFocusable(false);

    }

    @Override
    protected void setNuevo() {
        super.setNuevo();
        gone(btnevento);
        gone(btnNota);
        gone(btnVerEventos);
        gone(btnVerNotas);

    }

    @Override
    protected void setAcciones() {
        super.setAcciones();

        btnevento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(CLIENTE, modelo);
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevoEvento());
            }
        });

        btnVerEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(LISTA, new ListaModelo(CAMPOS_EVENTO, EVENTO_CLIENTEREL, id, null, IGUAL, null));
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
            }
        });

        btnNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putString(IDREL, modelo.getString(PROVEEDOR_ID_PROVEEDOR));
                bundle.putString(SUBTITULO, modelo.getString(PROVEEDOR_NOMBRE));
                bundle.putString(ORIGEN, PROVEEDOR);
                bundle.putSerializable(MODELO, null);
                bundle.putSerializable(LISTA, null);
                bundle.putString(CAMPO_ID, null);
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevaNota());
            }
        });

        btnVerNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarBundle();
                bundle.putString(IDREL, modelo.getString(PROVEEDOR_ID_PROVEEDOR));
                bundle.putString(SUBTITULO, modelo.getString(PROVEEDOR_NOMBRE));
                bundle.putString(ORIGEN, PROVEEDOR);
                bundle.putString(ACTUAL, NOTA);
                bundle.putSerializable(LISTA, null);
                bundle.putSerializable(MODELO, null);
                bundle.putString(CAMPO_ID, null);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
            }
        });

        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!direccion.getText().toString().equals("")) {

                    AppActivity.viewOnMapA(contexto, direccion.getText().toString());

                }
            }
        });

        llamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppActivity.hacerLlamada(AppActivity.getAppContext()
                        , telefono.getText().toString());
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppActivity.enviarEmail(getContext(), email.getText().toString());
            }
        });

        addProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelo = CRUDutil.setModelo(campos, id);
                bundle = new Bundle();
                putBundle(MODELO, producto);
                putBundle(PROVEEDOR, modelo);
                putBundle(PARTIDA, partida);
                putBundle(PROYECTO, proyecto);
                putBundle(ORIGEN, origen);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProducto());
            }
        });
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
        public void bind(Modelo modelo) {

            nombre.setText(modelo.getCampos(PROVEEDOR_NOMBRE));
            direccion.setText(modelo.getCampos(PROVEEDOR_DIRECCION));
            telefono.setText(modelo.getCampos(PROVEEDOR_TELEFONO));
            email.setText(modelo.getCampos(PROVEEDOR_EMAIL));
            contacto.setText(modelo.getCampos(PROVEEDOR_CONTACTO));

            String path = modelo.getString(PROVEEDOR_RUTAFOTO);
            if (path != null) {
                new MediaUtil(contexto).setImageUriCircle(path, imagenProveedor);
            }
            if (modelo.getLong(PROVEEDOR_ACTIVO) > 0) {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
            } else {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_defecto));
            }

            super.bind(modelo);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<Modelo> entrada) {

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
