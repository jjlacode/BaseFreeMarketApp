package com.codevsolution.freemarketsapp.ui;
// Created by jjlacode on 10/06/19. 

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.android.controls.ViewLinearLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposDetPartida.TIPOPRODUCTO;

public class FragmentCRUDProducto extends FragmentCRUD implements Interactor.ConstantesPry, ContratoPry.Tablas {

    private Modelo proveedor;
    private EditMaterialLayout nombreProv;
    private Button addPartida;

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
        String idPartidabase = AndroidUtil.getSharePreference(contexto, PERSISTENCIA, PARTIDABASE_ID_PARTIDABASE, NULL);
        if (nnn(idPartidabase)) {

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
        onUpdate();

    }

    @Override
    protected void onClickRV(View v) {

        super.onClickRV(v);

    }

    @Override
    protected void setBundle() {
        super.setBundle();
        proveedor = (Modelo) getBundleSerial(PROVEEDOR);

        if (origen == null) {
            origen = PRODUCTO;
        }
    }

    @Override
    protected void setLayout() {

        //layoutCuerpo = R.layout.fragment_crud_producto;
        layoutItem = R.layout.item_list_producto;
        //layoutCabecera = ;

    }

    @Override
    protected void setInicio() {

        ViewLinearLayout vistaForm = new ViewLinearLayout(contexto, frdetalleExtrasante);

        imagen = (ImagenLayout) vistaForm.addVista(new ImagenLayout(contexto));
        imagen.setFocusable(false);
        vistaForm.addEditMaterialLayout(getString(R.string.nombre), PRODUCTO_NOMBRE, null, null);
        vistaForm.addEditMaterialLayout(getString(R.string.descripcion), PRODUCTO_DESCRIPCION, null, null);
        EditMaterialLayout precio = vistaForm.addEditMaterialLayout(getString(R.string.importe), PRODUCTO_PRECIO, null, null);
        precio.setTipo(EditMaterialLayout.NUMERO | EditMaterialLayout.DECIMAL);
        nombreProv = vistaForm.addEditMaterialLayout(getString(R.string.nombre_producto_proveedor), PRODUCTO_NOMBREPROV, null, null);
        nombreProv.setImgBtnAccion(R.drawable.ic_clientes_indigo);
        nombreProv.setActivo(false);
        nombreProv.setClickAccion(new EditMaterialLayout.ClickAccion() {
            @Override
            public void onClickAccion(View view) {
                bundle = new Bundle();

                putBundle(PRODUCTO, modelo);
                putBundle(ORIGEN, PRODUCTO);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProveedor());
            }
        });
        vistaForm.addEditMaterialLayout(getString(R.string.referencia_proveedor), PRODUCTO_REFERENCIA, null, null);
        vistaForm.addEditMaterialLayout(getString(R.string.referencia_proveedor), PRODUCTO_DESCPROV, null, null);

        addPartida = vistaForm.addButtonPrimary(R.string.add_detpartida);
        addPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
                bundle = new Bundle();
                String idPartidabase = AndroidUtil.getSharePreference(contexto, PERSISTENCIA, PARTIDABASE_ID_PARTIDABASE, NULL);
                if (nnn(idPartidabase)) {
                    putBundle(CAMPO_ID, idPartidabase);
                    putBundle(CAMPO_SECUENCIA, crearProductoBase(idPartidabase));
                    AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PARTIDABASE_ID_PARTIDABASE, NULL);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaBaseProducto());
                }
            }
        });

        Button exportCLI = vistaForm.addButtonSecondary(R.string.add_prodwebcli);
        exportCLI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
                valores = new ContentValues();
                valores.put(PRODUCTO_CATEGORIA, PRODUCTOCLI);
                CRUDutil.actualizarRegistro(modelo, valores);
                bundle = new Bundle();
                putBundle(CRUD, modelo);
                icFragmentos.enviarBundleAFragment(bundle, new AltaProductosCli());
            }
        });

        Button exportPRO = vistaForm.addButtonSecondary(R.string.add_prodwebpro);
        exportPRO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
                valores.put(PRODUCTO_CATEGORIA, PRODUCTOPRO);
                CRUDutil.actualizarRegistro(modelo, valores);
                bundle = new Bundle();
                putBundle(CRUD, modelo);
                icFragmentos.enviarBundleAFragment(bundle, new AltaProductosPro());
            }
        });
        actualizarArrays(vistaForm);


    }

    private int crearProductoBase(String idPartidabase) {
        ContentValues valores = new ContentValues();
        putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_ID_DETPARTIDABASE, modelo.getString(PRODUCTO_ID_PRODUCTO));
        putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_TIPO, TIPOPRODUCTO);
        putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_ID_PARTIDABASE, idPartidabase);
        return CRUDutil.crearRegistroSec(CAMPOS_DETPARTIDABASE, idPartidabase, TABLA_PARTIDABASE, valores);

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
