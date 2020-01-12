package com.codevsolution.freemarketsapp.ui;

import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.codevsolution.freemarketsapp.sqlite.ContratoPry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public abstract class AltaProductosFirebase extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb
        implements Interactor.ConstantesPry, ContratoPry.Tablas {

    protected boolean iniciado;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {

        if (!modulo) {
            View viewFB = inflater.inflate(R.layout.cabecera_alta_productos_firebase, container, false);
            if (viewFB.getParent() != null) {
                ((ViewGroup) viewFB.getParent()).removeView(viewFB); // <- fix
            }
            frdetalleExtrasante.addView(viewFB);
        }

        super.setOnCreateView(view, inflater, container);
    }

    @Override
    protected void setLayout() {

    }



    @Override
    protected void setInicio() {


        if (!modulo) {

            limitProdAct = (EditMaterial) ctrl(R.id.etlimitprodact);
            limitProd = (EditMaterial) ctrl(R.id.etlimitprod);
            prodActUsados = (EditMaterial) ctrl(R.id.etprodact);
            prodUsados = (EditMaterial) ctrl(R.id.etprod);


            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            db.child(INDICE + PRODUCTOPRO).child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    contadorProdActivo = 0;
                    contadorProdTotal = 0;

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        contadorProdTotal++;
                        if (child.getValue(Boolean.class)) {
                            contadorProdActivo++;
                        }
                    }
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child(INDICE + PRODUCTOCLI).child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                contadorProdTotal++;
                                if (child.getValue(Boolean.class)) {
                                    contadorProdActivo++;
                                }
                            }

                            prodActUsados.setText(String.valueOf(contadorProdActivo));
                            prodUsados.setText(String.valueOf(contadorProdTotal));
                            //if (contadorProdTotal < limiteProdTotal) {

                            //} else {
                            //    activityBase.fabNuevo.hide();
                            //}

                            chActivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                    if (b && contadorProdActivo >= limiteProdActivos) {
                                        chActivo.setChecked(false);
                                    } else if (!b) {
                                        contadorProdActivo--;
                                        prodActUsados.setText(String.valueOf(contadorProdActivo));
                                    } else {
                                        contadorProdActivo++;
                                        prodActUsados.setText(String.valueOf(contadorProdActivo));

                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    protected void alComprobarSuscripciones() {
        super.alComprobarSuscripciones();

        activityBase.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                limitProdAct.setText(String.valueOf(limiteProdActivos));
                limitProd.setText(String.valueOf(limiteProdTotal));

            }
        });

    }

    @Override
    protected void setDatos() {
        super.setDatos();
        if (!modulo) {
            gone(imagen.getLinearLayoutCompat());
        }

    }

    @Override
    protected String setTipoForm() {
        return NUEVO;
    }


    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        if (nn(bundle) && !modulo) {

            prodCrud = (ModeloSQL) bundle.getSerializable(CRUD);
            if (prodCrud != null) {
                prodProv = convertirProdCrud(prodCrud);
                if (prodProv != null) {
                    prodProv.setTipo(tipo);
                    esDetalle = true;
                }
            }

        }
        if (modulo) {
            cargarDatos();

        }
    }

    protected void cargarDatos() {

    }

    @Override
    protected void setModuloInicio() {
        /*
        gone(lupa);
        gone(auto);
        gone(renovar);
        gone(radioGroupProd);
        gone(radioButtonProd1);
        gone(radioButtonProd2);

         */
        activityBase.fabNuevo.show();
        activityBase.fabInicio.hide();

    }

    protected Productos convertirProdCrud(ModeloSQL prodCrud) {

        System.out.println("Convirtiendo prodCrud en prodProv");

        boolean activo = false;


        if (tipo.equals(PRODUCTOPRO)) {
            id = prodCrud.getString(PRODUCTO_ID_PRODFIREPRO);
            if (prodCrud.getInt(PRODUCTO_ACTIVOPRO) == 1) {
                activo = true;
            }
            Productos prodProv = new Productos(id,
                    prodCrud.getString(PRODUCTO_REFERENCIAPRO),
                    prodCrud.getString(PRODUCTO_NOMBREPRO),
                    prodCrud.getString(PRODUCTO_DESCRIPCIONPRO),
                    prodCrud.getString(PRODUCTO_CATEGORIAPRO),
                    prodCrud.getString(PRODUCTO_SUBCATEGORIAPRO),
                    prodCrud.getString(PRODUCTO_WEBPRO),
                    prodCrud.getDouble(PRODUCTO_DESCUENTOPRO),
                    prodCrud.getDouble(PRODUCTO_PRECIO),
                    prodCrud.getString(PRODUCTO_NOMBREPROV),
                    idUser,
                    prodCrud.getString(PRODUCTO_ALCANCEPRO),
                    prodCrud.getString(PRODUCTO_TIPO),
                    activo,
                    prodCrud.getLong(PRODUCTO_TIMESTAMP));

            return prodProv;
        } else if (tipo.equals(PRODUCTOCLI)) {
            id = prodCrud.getString(PRODUCTO_ID_PRODFIRE);
            if (prodCrud.getInt(PRODUCTO_ACTIVO) == 1) {
                activo = true;
            }
            Productos prodProv = new Productos(id,
                    prodCrud.getString(PRODUCTO_REFERENCIA),
                    prodCrud.getString(PRODUCTO_NOMBRE),
                    prodCrud.getString(PRODUCTO_DESCRIPCION),
                    prodCrud.getString(PRODUCTO_CATEGORIA),
                    prodCrud.getString(PRODUCTO_SUBCATEGORIA),
                    prodCrud.getString(PRODUCTO_WEB),
                    prodCrud.getDouble(PRODUCTO_DESCUENTO),
                    prodCrud.getDouble(PRODUCTO_PRECIO),
                    prodCrud.getString(PRODUCTO_NOMBREPROV),
                    idUser,
                    prodCrud.getString(PRODUCTO_ALCANCE),
                    prodCrud.getString(PRODUCTO_TIPO),
                    activo,
                    prodCrud.getLong(PRODUCTO_TIMESTAMP));

            return prodProv;
        }

        return null;
    }

    @Override
    protected void actualizarProdCrud(Productos prodprov, ModeloSQL prodCrud) {

        ContentValues values = new ContentValues();
        if (tipo.equals(PRODUCTOCLI)) {
            values.put(PRODUCTO_ID_PRODFIRE, prodprov.getId());
            if (prodprov.isActivo()) {
                values.put(PRODUCTO_ACTIVO, 1);
            } else {
                values.put(PRODUCTO_ACTIVO, 0);
            }
        } else if (tipo.equals(PRODUCTOPRO)) {
            values.put(PRODUCTO_ID_PRODFIREPRO, prodprov.getId());
            if (prodprov.isActivo()) {
                values.put(PRODUCTO_ACTIVOPRO, 1);
            } else {
                values.put(PRODUCTO_ACTIVOPRO, 0);
            }
        }

        actualizarRegistro(prodCrud, values);

    }

    protected void sincronizarClon(final Productos prodProv, ModeloSQL producto) {

        final String tipo = producto.getString(PRODUCTO_TIPO);
        String idClon = producto.getString(PRODUCTO_ID_CLON);
        Productos prod = new Productos();
        String id = null;
        String nombre = prodProv.getNombre();
        String descripcion = prodProv.getDescripcion();
        String alcance = prodProv.getAlcance();
        String categoria = prodProv.getCategoria();
        String subCategoria = prodProv.getSubCategoria();
        String referencia = prodProv.getRefprov();
        double precio = prodProv.getPrecio();
        double descuento = prodProv.getDescProv();

        if (idClon != null && !idClon.isEmpty() && idClon.equals(prodProv.getId())) {

            System.out.println("sincronizando prod");
            ContentValues valores = new ContentValues();

            if (tipo.equals(PRODUCTOCLI)) {

                id = producto.getString(PRODUCTO_ID_PRODFIRE);
                prod.setId(id);
                prod.setTipo(PRODUCTOCLI);
                if (producto.getInt(PRODUCTO_ACTIVO) == 1) {
                    prod.setActivo(true);
                } else {
                    prod.setActivo(false);
                }

                if (producto.getInt(PRODUCTO_SINCRONOMBRE) == 1) {
                    valores.put(PRODUCTO_NOMBRE, nombre);
                    prod.setNombre(nombre);
                }
                if (producto.getInt(PRODUCTO_SINCRODESCRIPCION) == 1) {
                    valores.put(PRODUCTO_DESCRIPCION, descripcion);
                    prod.setDescripcion(descripcion);
                }
                if (producto.getInt(PRODUCTO_SINCROALCANCE) == 1) {
                    valores.put(PRODUCTO_ALCANCE, alcance);
                    prod.setAlcance(alcance);
                }
                if (producto.getInt(PRODUCTO_SINCROCATEGORIA) == 1) {
                    valores.put(PRODUCTO_CATEGORIA, categoria);
                    prod.setCategoria(categoria);
                }
                if (producto.getInt(PRODUCTO_SINCROSUBCATEGORIA) == 1) {
                    valores.put(PRODUCTO_SUBCATEGORIA, subCategoria);
                    prod.setSubCategoria(subCategoria);
                }
                if (producto.getInt(PRODUCTO_SINCROREFERENCIA) == 1) {
                    valores.put(PRODUCTO_REFERENCIA, referencia);
                    prod.setRefprov(referencia);
                }
                if (producto.getInt(PRODUCTO_SINCROIMAGEN) == 1) {
                    ImagenUtil.copyImageFirestoreToCrud(idClon + PRODUCTOPRO, producto, "");
                }

            } else if (tipo.equals(PRODUCTOPRO)) {

                id = producto.getString(PRODUCTO_ID_PRODFIREPRO);
                prod.setId(id);
                prod.setTipo(PRODUCTOPRO);
                if (producto.getInt(PRODUCTO_ACTIVOPRO) == 1) {
                    prod.setActivo(true);
                } else {
                    prod.setActivo(false);
                }


                if (producto.getInt(PRODUCTO_SINCRONOMBREPRO) == 1) {
                    valores.put(PRODUCTO_NOMBREPRO, nombre);
                    prod.setNombre(nombre);
                }
                if (producto.getInt(PRODUCTO_SINCRODESCRIPCIONPRO) == 1) {
                    valores.put(PRODUCTO_DESCRIPCIONPRO, descripcion);
                    prod.setDescripcion(descripcion);
                }
                if (producto.getInt(PRODUCTO_SINCROALCANCEPRO) == 1) {
                    valores.put(PRODUCTO_ALCANCEPRO, alcance);
                    prod.setAlcance(alcance);
                }
                if (producto.getInt(PRODUCTO_SINCROCATEGORIAPRO) == 1) {
                    valores.put(PRODUCTO_CATEGORIAPRO, categoria);
                    prod.setCategoria(categoria);
                }
                if (producto.getInt(PRODUCTO_SINCROSUBCATEGORIAPRO) == 1) {
                    valores.put(PRODUCTO_SUBCATEGORIAPRO, subCategoria);
                    prod.setSubCategoria(subCategoria);
                }
                if (producto.getInt(PRODUCTO_SINCROREFERENCIAPRO) == 1) {
                    valores.put(PRODUCTO_REFERENCIAPRO, referencia);
                    prod.setRefprov(referencia);
                }
                if (producto.getInt(PRODUCTO_SINCROIMAGENPRO) == 1) {
                    ImagenUtil.copyImageFirestoreToCrud(idClon + PRODUCTOPRO, producto, PRO);
                }
            }

            valores.put(PRODUCTO_PRECIO, precio);
            valores.put(PRODUCTO_DESCPROV, descuento);
            valores.put(PRODUCTO_ULTIMASINCRO, TimeDateUtil.ahora());
            prod.setTimeStamp(TimeDateUtil.ahora());
            prod.setIdprov(idUser);

            actualizarRegistro(producto, valores);

            if (id != null) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child(PRODUCTOS).child(id).setValue(prod);
            }

        }
    }

    @Override
    protected void alGuardar(Productos prodProv, ModeloSQL prodCrud) {
        super.alGuardar(prodProv, prodCrud);

        actualizarProdCrud(prodProv, prodCrud);
    }
}
