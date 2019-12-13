package com.codevsolution.freemarketsapp.ui;
// Created by jjlacode on 10/06/19. 

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.chargebee.models.Subscription;
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.android.controls.ViewImagenLayout;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposDetPartida.TIPOPRODUCTO;

public class FragmentCRUDProducto extends FragmentCRUD implements Interactor.ConstantesPry, ContratoPry.Tablas {

    private ModeloSQL proveedor;
    private EditMaterialLayout nombreProv;
    private EditMaterialLayout nombre;
    private Button addPartida;
    private CheckBox fire;
    private CheckBox firePro;
    private ViewGroupLayout vistaForm;
    private ViewGroupLayout lyPro;
    private ViewGroupLayout lyCli;
    private OnSetDatosCli onSetDatosCliListener;
    private OnSetDatosPro onSetDatosProListener;
    private ViewImagenLayout imagenPro;
    private ViewImagenLayout imagenCli;
    private int idViewGroupCli;
    private int idViewGroupPro;
    private boolean iniciado;
    private FrameLayout flCli;
    private FrameLayout flPro;
    private RadioGroup rg;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioGroup rgPro;
    private RadioButton rb1Pro;
    private RadioButton rb2Pro;
    private RadioGroup rgAct;
    private RadioButton rb1Act;
    private RadioButton rb2Act;
    private RadioGroup rgProAct;
    private RadioButton rb1ProAct;
    private RadioButton rb2ProAct;
    private boolean suscripcionOk;
    private ViewGroupLayout lySus;
    private EditMaterialLayout prodActDis;
    private EditMaterialLayout prodActUsados;
    private EditMaterialLayout prodResDis;
    private EditMaterialLayout prodResUsados;
    private EditMaterialLayout prodTotDis;
    private EditMaterialLayout prodTotUsados;
    private Button btnSus;
    private int contadorProdTotal;
    private int contadorProdActivo;
    private int limiteProdActivos;
    private long limiteProdTotal;
    private String tipo;

    public FragmentCRUDProducto() {
        // Required empty public constructor
        fragment = this;
    }

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
    protected void setTabla() {

        tabla = TABLA_PRODUCTO;

    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.producto;
        tituloPlural = R.string.productos;
        tituloNuevo = R.string.nuevo_producto;

    }

    @Override
    protected void setActual() {
        actual = PRODUCTO;
    }

    @Override
    protected void setDatos() {

        if (proveedor != null) {
            update();
            nombreProv.setText(proveedor.getString(PROVEEDOR_NOMBRE));
        }
        if (nnn(nombre.getTexto())){
            nombre.reproducir();
        }

        String idPartidabase = AndroidUtil.getSharePreference(contexto, PERSISTENCIA, PARTIDABASE_ID_PARTIDABASE, NULL);
        if (nnn(idPartidabase)) {

            visible(addPartida);

        } else {

            gone(addPartida);

        }

        if (modeloSQL.getInt(PRODUCTO_FIRE) == 1 || modeloSQL.getInt(PRODUCTO_FIREPRO) == 1) {
            /* Comprobar que el cliente está suscrito y tiene disponibles productos web para configurar*/
            if (!suscripcionOk) {
                comprobarSuscripciones();
                actualizarProdWeb();
            } else {
                actualizarProdWeb();
            }
        }

        iniciado = true;

    }

    private void actualizarProdWeb() {

        if (modeloSQL.getInt(PRODUCTO_FIRE) == 1) {
            fire.setChecked(true);
            setImagen(imagenCli, PRODUCTO_RUTAFOTOCLI);
            String prodCat = modeloSQL.getString(PRODUCTO_CATEGORIA);

            if (nn(prodCat) && prodCat.equals(getString(R.string.producto))) {
                rb1.setChecked(true);
                rb2.setChecked(false);
            } else if (nn(prodCat) && prodCat.equals(getString(R.string.servicio))) {
                rb2.setChecked(true);
                rb1.setChecked(false);
            }

            int activo = modeloSQL.getInt(PRODUCTO_ACTIVO);
            if (activo == 1) {
                rb1Act.setChecked(true);
                rb2Act.setChecked(false);
            } else {
                rb2Act.setChecked(true);
                rb1Act.setChecked(false);
            }

            actualizarProdCli();
        } else {
            if (fire.isChecked()) {
                fire.setChecked(false);
                gone(lyCli.getViewGroup());
            }
            if (!suscripcionOk) {
                fire.setEnabled(false);
            }
        }

        if (modeloSQL.getInt(PRODUCTO_FIREPRO) == 1) {
            firePro.setChecked(true);
            setImagen(imagenPro, PRODUCTO_RUTAFOTOPRO);
            String prodCatPro = modeloSQL.getString(PRODUCTO_CATEGORIAPRO);
            if (nn(prodCatPro) && prodCatPro.equals(getString(R.string.producto))) {
                rb1Pro.setChecked(true);
                rb2Pro.setChecked(false);
            } else if (nn(prodCatPro) && prodCatPro.equals(getString(R.string.servicio))) {
                rb2Pro.setChecked(true);
                rb1Pro.setChecked(false);
            }

            int activoPro = modeloSQL.getInt(PRODUCTO_ACTIVOPRO);
            if (activoPro == 1) {
                rb1ProAct.setChecked(true);
                rb2ProAct.setChecked(false);
            } else {
                rb2ProAct.setChecked(true);
                rb1ProAct.setChecked(false);
            }
            actualizarProdPro();

        } else {
            if (firePro.isChecked()) {
                firePro.setChecked(false);
                gone(lyPro.getViewGroup());
                if (!suscripcionOk) {
                    firePro.setEnabled(false);
                }
            }
        }
    }

    @Override
    protected void setNuevo() {
        super.setNuevo();
        if (proveedor != null) {
            nombreProv.setText(proveedor.getString(PROVEEDOR_NOMBRE));
        }
        iniciado = false;
        fire.setChecked(false);
        firePro.setChecked(false);
        gone(lyCli.getViewGroup());
        gone(lyPro.getViewGroup());
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
    protected void onSetImagen(String path) {

        if (nnn(id) && nnn(tipo)) {
            if (tipo.equals(PRODUCTOCLI)) {

                ImagenUtil.guardarImageFirestore(SLASH+idUser+SLASH+modeloSQL.getString(PRODUCTO_ID_PRODFIRE) + tipo, imagenCli, path);
                tipo = null;

            } else if (tipo.equals(PRODUCTOPRO)) {

                ImagenUtil.guardarImageFirestore(SLASH+idUser+SLASH+modeloSQL.getString(PRODUCTO_ID_PRODFIREPRO) + tipo, imagenPro, path);
                tipo = null;

            }
        }
    }


    @Override
    protected void setInicio() {

        iniciado = false;
        frdetalleExtrasante.setOrientation(LinearLayoutCompat.VERTICAL);
        vistaForm = new ViewGroupLayout(contexto, frdetalleExtrasante);

        imagen = vistaForm.addViewImagenLayout();
        imagen.getLinearLayoutCompat().setFocusable(false);


        nombre = vistaForm.addEditMaterialLayout(getString(R.string.nombre), PRODUCTO_NOMBRE);
        vistaForm.addEditMaterialLayout(getString(R.string.descripcion), PRODUCTO_DESCRIPCION);
        ViewGroupLayout vistaPrecio = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaPrecio.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        EditMaterialLayout precio = vistaPrecio.addEditMaterialLayout(getString(R.string.importe), PRODUCTO_PRECIO, 1);
        precio.setTipo(EditMaterialLayout.NUMERO | EditMaterialLayout.DECIMAL);
        vistaPrecio.addEditMaterialLayout(R.string.unidad, PRODUCTO_UNIDAD, 1);
        actualizarArrays(vistaPrecio);

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

        /* Checkbox de productos web a clientes finales, si esta chekeado muestra el
           boton de opciones de configuracion de productos web CLI */
        fire = (CheckBox) vistaForm.addVista(new CheckBox(contexto));
        fire.setText(R.string.producto_web);
        fire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (iniciado) {

                    if (buttonView.isChecked()) {

                        if (!suscripcionOk) {
                            comprobarSuscripciones();
                        } else {

                            actualizarProdCli();
                        }

                    } else {

                        abriDialogoBorrarProdCli();
                    }
                }
            }
        });
        //gone(fire);
        /* Checkbox de productos web a clientes profesionales, si esta chekeado muestra el
           boton de opciones de configuracion de productos web PRO */
        firePro = (CheckBox) vistaForm.addVista(new CheckBox(contexto));
        firePro.setText(R.string.producto_web_pro);
        firePro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (iniciado) {

                    if (buttonView.isChecked()) {

                        if (!suscripcionOk) {
                            comprobarSuscripciones();
                        } else {

                            actualizarProdPro();
                        }

                    } else {

                        abriDialogoBorrarProdPro();
                    }
                }
            }
        });
        //gone(firePro);

        lySus = new ViewGroupLayout(contexto, vistaForm.getViewGroup());//(LinearLayoutCompat) vistaForm.addVista(new LinearLayoutCompat(contexto));
        Estilos.setLayoutParams(vistaForm.getViewGroup(), lySus.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);

        btnSus = lySus.addButtonSecondary(activityBase, R.string.crear_suscripcion);
        btnSus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icFragmentos.enviarBundleAFragment(null, new MisSuscripcionesProductosPro());
            }
        });

        ViewGroupLayout lySusAct = new ViewGroupLayout(contexto, lySus.getViewGroup());//(LinearLayoutCompat) vistaForm.addVista(new LinearLayoutCompat(contexto));
        lySusAct.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        Estilos.setLayoutParams(lySus.getViewGroup(), lySusAct.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);
        prodActDis = lySusAct.addEditMaterialLayout(R.string.limitact, 1);
        prodActUsados = lySusAct.addEditMaterialLayout(R.string.limitactusados, 1);
        prodActDis.setActivo(false);
        prodActUsados.setActivo(false);
        actualizarArrays(lySusAct);

        ViewGroupLayout lyResAct = new ViewGroupLayout(contexto, lySus.getViewGroup());//(LinearLayoutCompat) vistaForm.addVista(new LinearLayoutCompat(contexto));
        lyResAct.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        Estilos.setLayoutParams(lySus.getViewGroup(), lyResAct.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);
        prodResDis = lyResAct.addEditMaterialLayout(R.string.limitres, 1);
        prodResUsados = lyResAct.addEditMaterialLayout(R.string.limitresusados, 1);
        prodResDis.setActivo(false);
        prodResUsados.setActivo(false);
        actualizarArrays(lyResAct);

        ViewGroupLayout lySusTot = new ViewGroupLayout(contexto, lySus.getViewGroup());//(LinearLayoutCompat) vistaForm.addVista(new LinearLayoutCompat(contexto));
        lySusTot.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        Estilos.setLayoutParams(lySus.getViewGroup(), lySusTot.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);
        prodTotDis = lySusTot.addEditMaterialLayout(R.string.limitprod, 1);
        prodTotUsados = lySusTot.addEditMaterialLayout(R.string.limitprodusados, 1);
        prodTotDis.setActivo(false);
        prodTotUsados.setActivo(false);
        actualizarArrays(lySusTot);
        gone(lySus.getViewGroup());
        actualizarArrays(lySus);

        lyCli = new ViewGroupLayout(contexto, vistaForm.getViewGroup());//(LinearLayoutCompat) vistaForm.addVista(new LinearLayoutCompat(contexto));
        Estilos.setLayoutParams(vistaForm.getViewGroup(), lyCli.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);
        imagenCli = lyCli.addViewImagenLayout();
        imagenCli.getLinearLayoutCompat().setFocusable(false);
        imagenCli.setTextTitulo(R.string.producto_web_cli);
        imagenCli.setFondoTitulo(Estilos.colorSecondary);
        imagenCli.setSizeTextTitulo(sizeText * 1.5f);
        imagenCli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = PRODUCTOCLI;
                mostrarDialogoOpcionesImagen(contexto, PRODUCTO_RUTAFOTOCLI);
            }
        });
        rgAct = new RadioGroup(contexto);
        rb1Act = new RadioButton(contexto);
        rb1Act.setText(R.string.visible_web);
        rb2Act = new RadioButton(contexto);
        rb2Act.setText(R.string.reserva);
        rgAct.addView(rb1Act);
        rgAct.addView(rb2Act);
        lyCli.addVista(rgAct);
        rgAct.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (rb1Act.isChecked()) {
                    CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_ACTIVO, 1);
                } else if (rb2Act.isChecked()) {
                    CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_ACTIVO, 0);
                }
                modeloSQL = CRUDutil.updateModelo(modeloSQL);
                actualizarProdCli();
            }
        });
        lyCli.addEditMaterialLayout(getString(R.string.descuento_cli), PRODUCTO_DESCUENTO);
        lyCli.addEditMaterialLayout(getString(R.string.palabras_clave), PRODUCTO_ALCANCE);
        lyCli.addEditMaterialLayout(getString(R.string.web), PRODUCTO_WEB);

        rg = new RadioGroup(contexto);
        rb1 = new RadioButton(contexto);
        rb1.setText(R.string.producto);
        rb2 = new RadioButton(contexto);
        rb2.setText(R.string.servicio);
        rg.addView(rb1);
        rg.addView(rb2);
        lyCli.addVista(rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (rb1.isChecked()) {
                    CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_CATEGORIA, getString(R.string.producto));
                } else if (rb2.isChecked()) {
                    CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_CATEGORIA, getString(R.string.servicio));
                }
                modeloSQL = CRUDutil.updateModelo(modeloSQL);
                actualizarProdCli();
            }
        });

        idViewGroupCli = lyCli.getViewGroup().getId();
        if (idViewGroupCli < 0) {
            idViewGroupCli = View.generateViewId();
        }

        lyCli.getViewGroup().setId(idViewGroupCli);

        bundle = new Bundle();
        putBundle(MODULO, true);
        addFragment(bundle, new AltaProductosCli(this), idViewGroupCli);
        actualizarArrays(lyCli);
        gone(lyCli.getViewGroup());

        lyPro = new ViewGroupLayout(contexto, vistaForm.getViewGroup());//(LinearLayoutCompat) vistaForm.addVista(new LinearLayoutCompat(contexto));
        Estilos.setLayoutParams(vistaForm.getViewGroup(), lyPro.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);
        flPro = new FrameLayout(contexto);
        idViewGroupPro = flPro.getId();
        if (idViewGroupPro < 0) {
            idViewGroupPro = View.generateViewId();
        }
        flPro.setId(idViewGroupPro);

        imagenPro = lyPro.addViewImagenLayout();
        imagenPro.getLinearLayoutCompat().setFocusable(false);
        imagenPro.setTextTitulo(R.string.producto_web_pro);
        imagenPro.setFondoTitulo(Estilos.colorSecondary);
        imagenPro.setSizeTextTitulo(sizeText * 1.5f);
        imagenPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = PRODUCTOPRO;
                mostrarDialogoOpcionesImagen(contexto, PRODUCTO_RUTAFOTOPRO);
            }
        });

        rgProAct = new RadioGroup(contexto);
        rb1ProAct = new RadioButton(contexto);
        rb1ProAct.setText(R.string.visible_web);
        rb2ProAct = new RadioButton(contexto);
        rb2ProAct.setText(R.string.reserva);
        rgProAct.addView(rb1ProAct);
        rgProAct.addView(rb2ProAct);
        lyPro.addVista(rgProAct);
        rgProAct.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (rb1ProAct.isChecked()) {
                    CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_ACTIVOPRO, 1);
                } else if (rb2ProAct.isChecked()) {
                    CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_ACTIVOPRO, 0);
                }
                modeloSQL = CRUDutil.updateModelo(modeloSQL);
                actualizarProdPro();
            }
        });
        lyPro.addEditMaterialLayout(getString(R.string.nombrepro), PRODUCTO_NOMBREPRO);
        lyPro.addEditMaterialLayout(getString(R.string.descripcionpro), PRODUCTO_DESCRIPCIONPRO);
        lyPro.addEditMaterialLayout(getString(R.string.referencia_proveedorpro), PRODUCTO_REFERENCIAPRO);
        lyPro.addEditMaterialLayout(getString(R.string.descuento_pro), PRODUCTO_DESCUENTOPRO);
        lyPro.addEditMaterialLayout(getString(R.string.palabras_clavepro), PRODUCTO_ALCANCEPRO);
        lyPro.addEditMaterialLayout(getString(R.string.webpro), PRODUCTO_WEBPRO);
        rgPro = new RadioGroup(contexto);
        rb1Pro = new RadioButton(contexto);
        rb1Pro.setText(R.string.producto);
        rb2Pro = new RadioButton(contexto);
        rb2Pro.setText(R.string.servicio);
        rgPro.addView(rb1Pro);
        rgPro.addView(rb2Pro);
        lyPro.addVista(rgPro);
        rgPro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (rb1Pro.isChecked()) {
                    CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_CATEGORIAPRO, getString(R.string.producto));
                } else if (rb2Pro.isChecked()) {
                    CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_CATEGORIAPRO, getString(R.string.servicio));
                }
                modeloSQL = CRUDutil.updateModelo(modeloSQL);
                actualizarProdPro();
            }
        });
        lyPro.addVista(flPro);
        Estilos.setLayoutParams(lyPro.getViewGroup(), flPro, ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);

        bundle = new Bundle();
        putBundle(MODULO, true);
        addFragment(bundle, new AltaProductosPro(this), idViewGroupPro);

        actualizarArrays(lyPro);
        gone(lyPro.getViewGroup());
        actualizarArrays(vistaForm);

    }

    @Override
    protected void onEliminarImagen() {
        super.onEliminarImagen();

        if (tipo.equals(PRODUCTOCLI)) {

            ImagenUtil.deleteImagefirestore(SLASH + idUser + SLASH + modeloSQL.getString(PRODUCTO_ID_PRODFIRE) + tipo);

        }else if (tipo.equals(PRODUCTOPRO)){

            ImagenUtil.deleteImagefirestore(SLASH + idUser + SLASH + modeloSQL.getString(PRODUCTO_ID_PRODFIREPRO) + tipo);

        }
    }

    private void actualizarProdCli() {

        System.out.println("Actualiza prodCli");

        visible(lyCli.getViewGroup());

        bundle = new Bundle();
        putBundle(CRUD, modeloSQL);
        if (onSetDatosCliListener != null) {
            onSetDatosCliListener.onSetDatos(bundle);
            System.out.println("onSetDatosCliListener = " + onSetDatosCliListener);
        }
        // Guarda el valor de producto cli a true y actualiza el modelo
        if (nn(modeloSQL)) {
            CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_FIRE, 1);
            modeloSQL = CRUDutil.updateModelo(modeloSQL);
        }
    }

    private void actualizarProdPro() {

        System.out.println("Actualiza prodPro");

        visible(lyPro.getViewGroup());
        bundle = new Bundle();
        putBundle(CRUD, modeloSQL);
        if (onSetDatosProListener != null) {
            onSetDatosProListener.onSetDatos(bundle);
            System.out.println("onSetDatosProListener = " + onSetDatosProListener);
        }

        // Guarda el valor de producto cli a true y actualiza el modelo
        if (nn(modeloSQL)) {
            CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_FIREPRO, 1);
            modeloSQL = CRUDutil.updateModelo(modeloSQL);
        }

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
                        suscripcionOk = false;
                        btnSus.setText(R.string.crear_suscripcion);
                    }
                });
            }

            @Override
            public void onProductLimit(ArrayList<Subscription> listaSuscripciones, int prodTotCli, int proActCli) {
                activityBase.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(contexto, "Ha llegado al limite de productos suscritos", Toast.LENGTH_SHORT).show();
                        suscripcionOk = false;
                        limiteProdActivos = 0;
                        limiteProdTotal = 0;
                        contadorProdActivo = proActCli;
                        contadorProdTotal = prodTotCli;
                        visible(lySus.getViewGroup());
                        for (Subscription subscription : listaSuscripciones) {
                            limiteProdActivos += subscription.planQuantity();
                            limiteProdTotal = Math.round(limiteProdActivos * 1.5);
                        }
                        prodActDis.setText(String.valueOf(limiteProdActivos));
                        prodResDis.setText(String.valueOf(limiteProdTotal - limiteProdActivos));
                        prodTotDis.setText(String.valueOf(limiteProdTotal));
                        prodActUsados.setText(String.valueOf(contadorProdActivo));
                        prodResUsados.setText(String.valueOf(contadorProdTotal - contadorProdActivo));
                        prodTotUsados.setText(String.valueOf(contadorProdTotal));
                        btnSus.setText(R.string.gestionar_sus);
                        actualizarProdWeb();
                    }
                });
            }

            @Override
            public void onError(final String msgError) {
                activityBase.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        suscripcionOk = false;
                        Toast.makeText(contexto, msgError, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCheckSuscriptionsOk(ArrayList<Subscription> listaSuscripciones, int prodTotCli, int proActCli) {

                activityBase.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        suscripcionOk = true;
                        limiteProdActivos = 0;
                        limiteProdTotal = 0;
                        contadorProdActivo = proActCli;
                        contadorProdTotal = prodTotCli;

                        visible(lySus.getViewGroup());
                        for (Subscription subscription : listaSuscripciones) {
                            limiteProdActivos += subscription.planQuantity();
                            limiteProdTotal = Math.round(limiteProdActivos * 1.5);
                        }
                        prodActDis.setText(String.valueOf(limiteProdActivos));
                        prodResDis.setText(String.valueOf(limiteProdTotal - limiteProdActivos));
                        prodTotDis.setText(String.valueOf(limiteProdTotal));
                        prodActUsados.setText(String.valueOf(contadorProdActivo));
                        prodResUsados.setText(String.valueOf(contadorProdTotal - contadorProdActivo));
                        prodTotUsados.setText(String.valueOf(contadorProdTotal));
                        btnSus.setText(R.string.gestionar_sus);
                        actualizarProdWeb();

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
                CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_ID_PRODFIRE, null);
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
                Query query = db.child(id).child(ZONAS);
                String finalId = id;
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot zona : dataSnapshot.getChildren()) {
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                            db.child(LUGARES).child(tipo).child(zona.getKey()).child(finalId).removeValue();
                        }
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        db.child(finalId).child(ZONAS).removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
                CRUDutil.actualizarCampo(modeloSQL, PRODUCTO_ID_PRODFIREPRO, null);
                modeloSQL = CRUDutil.updateModelo(modeloSQL);
                borrarProdFire(modeloSQL, PRODUCTOPRO);
            }

            @Override
            public void onCancel() {
                firePro.setChecked(true);
            }
        }).show(getFragmentManager(), "productpro");
    }

    @Override
    protected boolean onDelete() {

        borrarProdFire(modeloSQL, PRODUCTOCLI);
        borrarProdFire(modeloSQL, PRODUCTOPRO);
        return super.onDelete();
    }

    private int crearProductoBase(String idPartidabase) {
        ContentValues valores = new ContentValues();
        putDato(valores, DETPARTIDABASE_ID_DETPARTIDABASE, modeloSQL.getString(PRODUCTO_ID_PRODUCTO));
        putDato(valores, DETPARTIDABASE_TIPO, TIPOPRODUCTO);
        putDato(valores, DETPARTIDABASE_ID_PARTIDABASE, idPartidabase);
        return CRUDutil.crearRegistroSec(CAMPOS_DETPARTIDABASE, idPartidabase, valores);

    }

    @Override
    protected void setContenedor() {

        if (proveedor != null) {
            putDato(valores,PRODUCTO_ID_PROVEEDOR, proveedor.getString(PROVEEDOR_ID_PROVEEDOR));
            putDato(valores,PRODUCTO_NOMBREPROV, proveedor.getString(PROVEEDOR_NOMBRE));
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
