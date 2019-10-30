package com.codevsolution.freemarketsapp.ui;

import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public abstract class AltaProductosFirebase extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb
        implements Interactor.ConstantesPry, ContratoPry.Tablas {

    private ModeloSQL prodCrud;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {

        View viewFB = inflater.inflate(R.layout.cabecera_alta_productos_firebase, container, false);
        if (viewFB.getParent() != null) {
            ((ViewGroup) viewFB.getParent()).removeView(viewFB); // <- fix
        }
        frdetalleExtrasante.addView(viewFB);

        super.setOnCreateView(view, inflater, container);
    }

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

        limitProdAct = (EditMaterial) ctrl(R.id.etlimitprodact);
        limitProd = (EditMaterial) ctrl(R.id.etlimitprod);
        prodActUsados = (EditMaterial) ctrl(R.id.etprodact);
        prodUsados = (EditMaterial) ctrl(R.id.etprod);


        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child(INDICE + PRODUCTOPRO).child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                        if (contadorProdTotal < limiteProdTotal) {
                            activityBase.fabNuevo.show();
                        } else {
                            activityBase.fabNuevo.hide();
                        }

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

    @Override
    protected void alComprobarSuscripciones() {
        super.alComprobarSuscripciones();

        limitProdAct.setText(String.valueOf(limiteProdActivos));
        limitProd.setText(String.valueOf(limiteProdTotal));

    }

    @Override
    protected void setDatos() {
        super.setDatos();

        visible(descuento);
    }

    @Override
    protected String setTipoForm() {
        return NUEVO;
    }

    protected void sincronizarClon(final Productos prodProv) {

        final String tipo = prodProv.getCategoria();

        if (nn(prodProv) && nn(prodProv.getIdClon()) && !prodProv.getIdClon().isEmpty()) {

            final String id = prodProv.getId();
            final String idClon = prodProv.getIdClon();

            if (nn(id) && nn(idClon)) {

                final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                Query querydb = db.child(PRODUCTOPRO).child(idClon);
                querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                    private Productos prodProvClon;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        prodProvClon = dataSnapshot.getValue(Productos.class);
                        db.child(tipo).child(id).setValue(prodProvClon);
                        ImagenUtil.copyImageFirestore(idClon, id);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        if (nn(bundle)) {

            prodCrud = (ModeloSQL) getBundleSerial(CRUD);
            if (prodCrud != null) {
                prodProv = convertirProdCrud(prodCrud);
                if (prodProv != null) {
                    prodProv.setTipo(tipo);
                    System.out.println("prodProv = " + prodProv);
                    if (prodProv.getId() == null) {
                        esDetalle = true;
                    }
                }
            }
        }
    }

    private Productos convertirProdCrud(ModeloSQL prodCrud) {

        boolean activo = false;
        boolean sincro = false;
        if (prodCrud.getInt(PRODUCTO_ACTIVO) == 1) {
            activo = true;
        }
        if (prodCrud.getInt(PRODUCTO_SINCRO) == 1) {
            sincro = true;
        }

        Productos prodProv = new Productos(null, prodCrud.getString(PRODUCTO_ID_PRODUCTO),
                prodCrud.getString(PRODUCTO_REFERENCIA), prodCrud.getString(PRODUCTO_NOMBRE),
                prodCrud.getString(PRODUCTO_DESCRIPCION), prodCrud.getString(PRODUCTO_WEB),
                prodCrud.getDouble(PRODUCTO_DESCPROV), prodCrud.getDouble(PRODUCTO_PRECIO),
                prodCrud.getString(PRODUCTO_ID_PROVEEDOR), tipo,
                prodCrud.getString(PRODUCTO_ID_PROVFIRE), prodCrud.getString(PRODUCTO_ALCANCE),
                prodCrud.getString(PRODUCTO_TIPO), activo,
                prodCrud.getString(PRODUCTO_ID_CLON), prodCrud.getString(PRODUCTO_RUTAFOTO),
                sincro, prodCrud.getLong(PRODUCTO_TIMESTAMP));

        return prodProv;
    }

    private void guardarProdCrud(Productos prodprov) {

        String idCrud = null;

        ContentValues values = new ContentValues();
        if (tipo.equals(PRODUCTOCLI)) {
            values.put(PRODUCTO_ID_PRODFIRE, prodprov.getId());
        } else if (tipo.equals(PRODUCTOPRO)) {
            values.put(PRODUCTO_ID_PRODFIREPRO, prodprov.getId());
        }
        values.put(PRODUCTO_ID_PRODUCTO, prodprov.getIdCrud());
        values.put(PRODUCTO_REFERENCIA, prodprov.getRefprov());
        values.put(PRODUCTO_NOMBRE, prodprov.getNombre());
        values.put(PRODUCTO_DESCRIPCION, prodprov.getDescripcion());
        values.put(PRODUCTO_WEB, prodprov.getWeb());
        values.put(PRODUCTO_DESCPROV, prodprov.getDescProv());
        values.put(PRODUCTO_PRECIO, prodprov.getPrecio());
        values.put(PRODUCTO_ID_PROVFIRE, prodprov.getIdprov());
        values.put(PRODUCTO_CATEGORIA, prodprov.getCategoria());
        values.put(PRODUCTO_NOMBREPROV, prodprov.getProveedor());
        values.put(PRODUCTO_ALCANCE, prodprov.getAlcance());
        values.put(PRODUCTO_TIPO, prodprov.getTipo());
        values.put(PRODUCTO_ID_CLON, prodprov.getIdClon());
        values.put(PRODUCTO_RUTAFOTO, prodprov.getRutafoto());

        if (prodprov.isActivo()) {
            values.put(PRODUCTO_ACTIVO, 1);
        } else {
            values.put(PRODUCTO_ACTIVO, 0);
        }

        if (prodprov.isSincronizado()) {
            values.put(PRODUCTO_SINCRO, 1);
        } else {
            values.put(PRODUCTO_SINCRO, 0);
        }

        if (nn(prodprov.getIdCrud())) {
            CRUDutil.actualizarRegistro(TABLA_PRODUCTO, prodprov.getIdCrud(), values);
        } else {

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            db.child(tipo).child(prodprov.getId()).setValue(prodprov);
        }

    }

    @Override
    protected void alGuardar(Productos prodProv) {
        super.alGuardar(prodProv);

        guardarProdCrud(prodProv);
    }
}
