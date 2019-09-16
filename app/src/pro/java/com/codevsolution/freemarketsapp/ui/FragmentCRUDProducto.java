package com.codevsolution.freemarketsapp.ui;
// Created by jjlacode on 10/06/19. 

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

public class FragmentCRUDProducto extends FragmentCRUD implements Interactor.ConstantesPry, ContratoPry.Tablas {

    EditMaterial nombre;
    EditMaterial descripcion;
    EditMaterial precio;
    EditMaterial nombreProv;
    EditMaterial referencia;
    EditMaterial descProv;
    ImageView btnProv;
    Button addPartida;

    private Modelo proveedor;
    private Modelo partida;
    private Modelo proyecto;

    public FragmentCRUDProducto() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    @Override
    protected void setTabla() {

        tabla = TABLA_PRODUCTO;

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
        tituloSingular = R.string.producto;
        tituloPlural = R.string.productos;
        tituloNuevo = R.string.nuevo_producto;
    }

    @Override
    protected void setDatos() {
        if (proveedor != null) {
            update();
            nombreProv.setText(proveedor.getString(PROVEEDOR_NOMBRE));
        }
        if (origen.equals(PARTIDA)) {

            visible(addPartida);

        } else {

            gone(addPartida);

        }

    }

    @Override
    protected void setNuevo() {
        super.setNuevo();
        if (proveedor != null) {
            nombreProv.setText(proveedor.getString(PROVEEDOR_NOMBRE));
        }
        gone(addPartida);
    }

    @Override
    protected void onClickRV(View v) {

        super.onClickRV(v);

    }

    @Override
    protected void setBundle() {
        super.setBundle();
        proveedor = (Modelo) getBundleSerial(PROVEEDOR);
        partida = (Modelo) getBundleSerial(PARTIDA);
        proyecto = (Modelo) getBundleSerial(PROYECTO);

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_producto;
        layoutItem = R.layout.item_list_producto;
        //layoutCabecera = ;

    }

    @Override
    protected void setInicio() {

        nombre = (EditMaterial) ctrl(R.id.etnombreproductos, PRODUCTO_NOMBRE);
        descripcion = (EditMaterial) ctrl(R.id.etdescripcionproductos, PRODUCTO_DESCRIPCION);
        precio = (EditMaterial) ctrl(R.id.etimporte_crud_productos, PRODUCTO_PRECIO);
        nombreProv = (EditMaterial) ctrl(R.id.etnomprovproducto, PRODUCTO_NOMBREPROV);
        referencia = (EditMaterial) ctrl(R.id.etrefproductos, PRODUCTO_REFERENCIA);
        descProv = (EditMaterial) ctrl(R.id.etdescprovproductos, PRODUCTO_DESCPROV);
        btnProv = (ImageView) ctrl(R.id.imgbtnprovproducto);
        addPartida = (Button) ctrl(R.id.btn_add_producto_partida);
        imagen = (ImagenLayout) ctrl(R.id.imgproducto);
    }

    @Override
    protected void setAcciones() {
        super.setAcciones();

        btnProv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle = new Bundle();

                if (origen == PARTIDA) {
                    putBundle(PARTIDA, partida);
                    putBundle(PROYECTO, proyecto);
                } else {
                    origen = PRODUCTO;
                }
                putBundle(PRODUCTO, modelo);
                putBundle(ORIGEN, origen);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProveedor());
            }
        });

        addPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = new Bundle();
                putBundle(PARTIDA, partida);
                putBundle(PROYECTO, proyecto);
                putBundle(PRODUCTO, modelo);
                putBundle(ORIGEN, PARTIDA);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaProducto());

            }
        });
    }

    @Override
    protected void setContenedor() {

        if (proveedor != null) {
            setDato(PRODUCTO_ID_PROVEEDOR, proveedor.getString(PROVEEDOR_ID_PROVEEDOR));
            setDato(PRODUCTO_NOMBREPROV, proveedor.getString(PROVEEDOR_NOMBRE));
        }

    }

    @Override
    protected void setcambioFragment() {
        super.setcambioFragment();

        if (origen.equals(PARTIDA)) {
            bundle = new Bundle();
            putBundle(PARTIDA, partida);
            putBundle(PRODUCTO, modelo);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaProducto());
        }
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<Modelo> entrada) {

            TextView nombreProd = itemView.findViewById(R.id.tvnombrelproductos);
            TextView descripcionProd = itemView.findViewById(R.id.tvdescripcionlproductos);
            TextView importeProd = itemView.findViewById(R.id.tvimportelproductos);
            ImageView imagenProd = itemView.findViewById(R.id.imglproductos);

            nombreProd.setText(entrada.get(posicion).getString(PRODUCTO_NOMBRE));
            descripcionProd.setText(entrada.get(posicion).getString(PRODUCTO_DESCRIPCION));
            importeProd.setText(entrada.get(posicion).getString(PRODUCTO_PRECIO));
            String path = entrada.get(posicion).getString(PRODUCTO_RUTAFOTO);
            if (path != null) {
                setImagenUriCircle(contexto, path, imagenProd);
            }

            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView nombreProd, descripcionProd, importeProd;
        ImageView imagenProd;

        public ViewHolderRV(View itemView) {
            super(itemView);

            nombreProd = itemView.findViewById(R.id.tvnombrelproductos);
            descripcionProd = itemView.findViewById(R.id.tvdescripcionlproductos);
            importeProd = itemView.findViewById(R.id.tvimportelproductos);
            imagenProd = itemView.findViewById(R.id.imglproductos);

        }

        @Override
        public void bind(Modelo modelo) {

            nombreProd.setText(modelo.getString(PRODUCTO_NOMBRE));
            descripcionProd.setText(modelo.getString(PRODUCTO_DESCRIPCION));
            importeProd.setText(modelo.getString(PRODUCTO_PRECIO));
            String path = modelo.getString(PRODUCTO_RUTAFOTO);
            if (path != null) {
                setImagenUriCircle(contexto, path, imagenProd);
            }

            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }


}
