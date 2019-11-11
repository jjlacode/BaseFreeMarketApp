package com.codevsolution.freemarketsapp.ui;
// Created by jjlacode on 10/06/19. 

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chargebee.models.Subscription;
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.style.Dialogos;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.codevsolution.freemarketsapp.logica.InteractorSuscriptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposDetPartida.TIPOPRODUCTO;

public class FragmentCRUDProducto extends FragmentCRUD implements Interactor.ConstantesPry, ContratoPry.Tablas {

    private ModeloSQL proveedor;
    private EditMaterialLayout nombreProv;
    private Button addPartida;
    private CheckBox fire;
    private CheckBox firePro;
    private ViewGroupLayout vistaForm;
    private ViewGroupLayout lyPro;
    private ViewGroupLayout lyCli;
    private OnSetDatosCli onSetDatosCliListener;
    private OnSetDatosPro onSetDatosProListener;
    private ImagenLayout imagenPro;


    public FragmentCRUDProducto() {
        // Required empty public constructor
        fragment = this;
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

        if (modeloSQL.getInt(PRODUCTO_FIRE) == 1) {
            fire.setChecked(true);
            actualizarProdCli();
        } else {
            fire.setChecked(false);
            gone(lyCli.getViewGroup());
        }

        if (modeloSQL.getInt(PRODUCTO_FIREPRO) == 1) {
            firePro.setChecked(true);
            actualizarProdPro();
        } else {
            firePro.setChecked(false);
            gone(lyPro.getViewGroup());
        }


        //imagen.setTextTitulo(modeloSQL.getString(PRODUCTO_CATEGORIA).toUpperCase());

        /*
        if (!modeloSQL.getString(PRODUCTO_CATEGORIA).equals(PRODUCTOLOCAL)) {

            imagen.getImagen().setClickable(false);
            imagen.setImageFirestore(modeloSQL.getString(PRODUCTO_ID_PRODFIRE));
        }

         */

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
        proveedor = (ModeloSQL) getBundleSerial(PROVEEDOR);
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

        vistaForm = new ViewGroupLayout(contexto, frdetalleExtrasante);

        imagen = (ImagenLayout) vistaForm.addVista(new ImagenLayout(contexto));
        imagen.setFocusable(false);
        /* Comprobar que el cliente está suscrito y tiene disponibles productos web para configurar*/
        comprobarSuscripciones();
        /* Checkbox de productos web a clientes finales, si esta chekeado muestra el
           boton de opciones de configuracion de productos web CLI */
        fire = (CheckBox) vistaForm.addVista(new CheckBox(contexto));
        fire.setText(R.string.producto_web);
        fire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    actualizarProdCli();

                } else {

                    abriDialogoBorrarProdCli();
                }
            }
        });
        gone(fire);
        /* Checkbox de productos web a clientes profesionales, si esta chekeado muestra el
           boton de opciones de configuracion de productos web PRO */
        firePro = (CheckBox) vistaForm.addVista(new CheckBox(contexto));
        firePro.setText(R.string.producto_web_pro);
        firePro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    actualizarProdPro();

                } else {

                    abriDialogoBorrarProdPro();
                }
            }
        });
        gone(firePro);
        vistaForm.addEditMaterialLayout(getString(R.string.nombre), PRODUCTO_NOMBRE);
        vistaForm.addEditMaterialLayout(getString(R.string.descripcion), PRODUCTO_DESCRIPCION);
        EditMaterialLayout precio = vistaForm.addEditMaterialLayout(getString(R.string.importe), PRODUCTO_PRECIO);
        precio.setTipo(EditMaterialLayout.NUMERO | EditMaterialLayout.DECIMAL);
        nombreProv = vistaForm.addEditMaterialLayout(getString(R.string.proveedor), PRODUCTO_NOMBREPROV);
        nombreProv.setImgBtnAccion(R.drawable.ic_clientes_indigo);
        nombreProv.setActivo(false);
        nombreProv.setClickAccion(new EditMaterialLayout.ClickAccion() {
            @Override
            public void onClickAccion(View view) {
                bundle = new Bundle();

                putBundle(PRODUCTO, modeloSQL);
                putBundle(ORIGEN, PRODUCTO);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProveedor());
            }
        });
        vistaForm.addEditMaterialLayout(getString(R.string.referencia_proveedor), PRODUCTO_REFERENCIA);
        vistaForm.addEditMaterialLayout(getString(R.string.descuento_proveedor), PRODUCTO_DESCPROV);
        vistaForm.addEditMaterialLayout(getString(R.string.palabras_clave), PRODUCTO_ALCANCE);
        vistaForm.addEditMaterialLayout(getString(R.string.web), PRODUCTO_WEB);

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

        lyCli = new ViewGroupLayout(contexto, vistaForm.getViewGroup());//(LinearLayoutCompat) vistaForm.addVista(new LinearLayoutCompat(contexto));
        Estilos.setLayoutParams(vistaForm.getViewGroup(), lyCli.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);

        lyCli.addEditMaterialLayout(getString(R.string.descuento_cli), PRODUCTO_DESCUENTO);

        int idViewGroup = lyCli.getViewGroup().getId();
        if (idViewGroup < 0) {
            idViewGroup = View.generateViewId();
        }

        lyCli.getViewGroup().setId(idViewGroup);
        bundle = new Bundle();
        putBundle(MODULO, true);
        addFragment(bundle, new AltaProductosCli(this), idViewGroup);

        gone(lyCli.getViewGroup());

        lyPro = new ViewGroupLayout(contexto, vistaForm.getViewGroup());//(LinearLayoutCompat) vistaForm.addVista(new LinearLayoutCompat(contexto));
        Estilos.setLayoutParams(vistaForm.getViewGroup(), lyPro.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);

        imagenPro = (ImagenLayout) lyPro.addVista(new ImagenLayout(contexto));
        imagenPro.setFocusable(false);
        imagenPro.setTextTitulo(R.string.producto_web_pro);
        imagenPro.setFondoTitulo(Estilos.colorSecondary);
        imagenPro.setSizeTextTitulo(sizeText * 1.5f);
        imagenPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoOpcionesImagen(contexto, PRODUCTO_RUTAFOTOPRO);
            }
        });
        lyPro.addEditMaterialLayout(getString(R.string.nombrepro), PRODUCTO_NOMBREPRO);
        lyPro.addEditMaterialLayout(getString(R.string.descripcionpro), PRODUCTO_DESCRIPCIONPRO);
        lyPro.addEditMaterialLayout(getString(R.string.referencia_proveedorpro), PRODUCTO_REFERENCIAPRO);
        lyPro.addEditMaterialLayout(getString(R.string.descuento_pro), PRODUCTO_DESCUENTOPRO);
        lyPro.addEditMaterialLayout(getString(R.string.palabras_clavepro), PRODUCTO_ALCANCEPRO);
        lyPro.addEditMaterialLayout(getString(R.string.webpro), PRODUCTO_WEBPRO);

        idViewGroup = lyPro.getViewGroup().getId();
        if (idViewGroup < 0) {
            idViewGroup = View.generateViewId();
        }

        lyPro.getViewGroup().setId(idViewGroup);
        bundle = new Bundle();
        putBundle(MODULO, true);
        addFragment(bundle, new AltaProductosPro(this), idViewGroup);

        gone(lyPro.getViewGroup());

        actualizarArrays(vistaForm);

    }

    private void actualizarProdCli() {

        System.out.println("Actualiza prodCli");
        visible(lyCli.getViewGroup());
        bundle = new Bundle();
        putBundle(CRUD, modeloSQL);
        if (onSetDatosCliListener != null) {
            onSetDatosCliListener.onSetDatos(bundle);
        }

        // Guarda el valor de producto cli a true y actualiza el modelo
        CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_FIRE, 1);
        modeloSQL = CRUDutil.updateModelo(modeloSQL);
    }

    private void actualizarProdPro() {

        System.out.println("Actualiza prodPro");
        visible(lyPro.getViewGroup());
        setImagen(imagenPro, PRODUCTO_RUTAFOTOPRO);
        bundle = new Bundle();
        putBundle(CRUD, modeloSQL);
        if (onSetDatosProListener != null) {
            onSetDatosProListener.onSetDatos(bundle);
        }

        // Guarda el valor de producto cli a true y actualiza el modelo
        CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_FIREPRO, 1);
        modeloSQL = CRUDutil.updateModelo(modeloSQL);

    }

    private void comprobarSuscripciones() {
        InteractorSuscriptions interactorSuscriptions = new InteractorSuscriptions(contexto);
        interactorSuscriptions.comprobarSuscripciones(new InteractorSuscriptions.CheckSubscriptions() {

            @Override
            public void onNotSubscriptions() {

                activityBase.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(contexto, "Necesita una suscripcion para poder usar los productos en web", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProductLimit() {
                activityBase.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(contexto, "Ha llegado al limite de productos suscritos", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(final String msgError) {
                activityBase.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(contexto, msgError, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCheckSuscriptionsOk(ArrayList<Subscription> listaSuscripciones) {

                activityBase.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        visible(fire);
                        visible(firePro);

                    }
                });

            }

        });

    }

    private void abriDialogoBorrarProdCli() {

        String titulo = "Confirmar borrado";
        String mensaje = "Esta seguro de querer borrar la configuracion del producto CLI, " +
                "se perderan el rating y las suscripciones de clientes y tendrá que volver a configurarlo si quiere habilitarlo más tarde";
        new Dialogos.DialogoTexto(titulo, mensaje, contexto, new Dialogos.DialogoTexto.OnClick() {
            @Override
            public void onConfirm() {
                gone(lyCli.getViewGroup());
                CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_FIRE, 0);
                modeloSQL = CRUDutil.updateModelo(modeloSQL);
                borrarProdFire(modeloSQL, PRODUCTOCLI);
            }

            @Override
            public void onCancel() {
                fire.setChecked(true);
            }
        }).show(getFragmentManager(), "productcli");
    }

    private void borrarProdFire(ModeloSQL modeloSQL, String tipo) {

        String id = null;
        if (tipo.equals(PRODUCTOCLI)) {

            id = modeloSQL.getString(PRODUCTO_ID_PRODFIRE);

        } else if (tipo.equals(PRODUCTOPRO)) {

            id = modeloSQL.getString(PRODUCTO_ID_PRODFIREPRO);

        }
        if (id != null) {

            String idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);
            if (nnn(idUser)) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();

                db.child(PRODUCTOS).child(id).removeValue();
                db.child(INDICE + tipo).child(idUser).child(id).removeValue();
                db.child(RATING).child(tipo).child(id).removeValue();
                ImagenUtil.deleteImagefirestore(id);
            }
        }
    }

    private void abriDialogoBorrarProdPro() {

        String titulo = "Confirmar borrado";
        String mensaje = "Esta seguro de querer borrar la configuracion del producto PRO, " +
                "se perderan el rating y las suscripciones de clientes y tendrá que volver a configurarlo si quiere habilitarlo más tarde";
        new Dialogos.DialogoTexto(titulo, mensaje, contexto, new Dialogos.DialogoTexto.OnClick() {
            @Override
            public void onConfirm() {
                gone(lyPro.getViewGroup());
                CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_FIREPRO, 0);
                modeloSQL = CRUDutil.updateModelo(modeloSQL);
                borrarProdFire(modeloSQL, PRODUCTOPRO);
            }

            @Override
            public void onCancel() {
                firePro.setChecked(true);
            }
        }).show(getFragmentManager(), "productpro");
    }

    private int crearProductoBase(String idPartidabase) {
        ContentValues valores = new ContentValues();
        putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_ID_DETPARTIDABASE, modeloSQL.getString(PRODUCTO_ID_PRODUCTO));
        putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_TIPO, TIPOPRODUCTO);
        putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_ID_PARTIDABASE, idPartidabase);
        return CRUDutil.crearRegistroSec(CAMPOS_DETPARTIDABASE, idPartidabase, TABLA_PARTIDABASE, valores);

    }

    @Override
    protected void setContenedor() {

        if (proveedor != null) {
            setDato(PRODUCTO_ID_PROVEEDOR, proveedor.getString(PROVEEDOR_ID_PROVEEDOR));
            setDato(PRODUCTO_NOMBREPROV, proveedor.getString(PROVEEDOR_NOMBRE));
            setDato(PRODUCTO_CATEGORIA, PRODUCTOLOCAL);
        }

    }

    @Override
    protected void setcambioFragment() {
        super.setcambioFragment();

    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<ModeloSQL> entrada) {

            TextView nombreProd = itemView.findViewById(R.id.tvnombrelproductos);
            TextView descripcionProd = itemView.findViewById(R.id.tvdescripcionlproductos);
            TextView importeProd = itemView.findViewById(R.id.tvimportelproductos);
            ImageView imagenProd = itemView.findViewById(R.id.imglproductos);

            nombreProd.setText(entrada.get(posicion).getString(PRODUCTO_NOMBRE));
            descripcionProd.setText(entrada.get(posicion).getString(PRODUCTO_DESCRIPCION));
            importeProd.setText(entrada.get(posicion).getString(PRODUCTO_PRECIO));
            String path = entrada.get(posicion).getString(PRODUCTO_RUTAFOTO);
            if (nnn(path)) {
                setImagenUriCircle(contexto, path, imagenProd);
            }

            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView nombreProd, descripcionProd, importeProd;
        ImagenLayout imagenProd;

        public ViewHolderRV(View itemView) {
            super(itemView);

            nombreProd = itemView.findViewById(R.id.tvnombrelproductos);
            descripcionProd = itemView.findViewById(R.id.tvdescripcionlproductos);
            importeProd = itemView.findViewById(R.id.tvimportelproductos);
            imagenProd = itemView.findViewById(R.id.imglproductos);

        }

        @Override
        public void bind(ModeloSQL modeloSQL) {

            nombreProd.setText(modeloSQL.getString(PRODUCTO_NOMBRE));
            descripcionProd.setText(modeloSQL.getString(PRODUCTO_DESCRIPCION));
            importeProd.setText(modeloSQL.getString(PRODUCTO_PRECIO));
            String path = modeloSQL.getString(PRODUCTO_RUTAFOTO);
            System.out.println("path = " + path);
            if (nnn(path)) {
                imagenProd.setImageUriCard(activityBase,path);
            } else {
                gone(imagenProd);
            }

            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    public interface OnSetDatosCli {
        void onSetDatos(Bundle bundle);
    }

    public void setOnSetDatosCliListener(OnSetDatosCli onSetDatosCliListener) {
        this.onSetDatosCliListener = onSetDatosCliListener;
    }

    public interface OnSetDatosPro {
        void onSetDatos(Bundle bundle);
    }

    public void setOnSetDatosProListener(OnSetDatosPro onSetDatosProListener) {
        this.onSetDatosProListener = onSetDatosProListener;
    }
}
