package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.Productos;
import com.codevsolution.freemarketsapp.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentCUDDetpartidaBaseProdProvCat extends FragmentCUD implements Interactor.ConstantesPry,
        ContratoPry.Tablas, Interactor.TiposDetPartida, Interactor.TiposEstados {

    private EditMaterial descripcion;
    private EditMaterial precio;
    private EditMaterial descProv;
    private EditMaterial refProv;
    private EditMaterial cantidad;
    private TextView tipoDetPartida;
    private String tipo;

    private EditMaterial nombre;
    private Productos prodProv;
    private EditMaterial nomProv;


    public FragmentCUDDetpartidaBaseProdProvCat() {
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

        tabla = TABLA_DETPARTIDABASE;

    }

    @Override
    protected void setBundle() {

    }

    private void obtenerProdProv(String idProdProv) {

        DatabaseReference dbproductosprov = FirebaseDatabase.getInstance().getReference().
                child(PRODUCTOPRO).child(idProdProv);

        dbproductosprov.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                prodProv = dataSnapshot.getValue(Productos.class);

                if (prodProv != null) {

                    nombre.setText(prodProv.getNombre());
                    descripcion.setText(prodProv.getDescripcion());
                    refProv.setText(prodProv.getRefprov());
                    precio.setText(JavaUtil.formatoMonedaLocal(prodProv.getPrecio()));
                    descProv.setText(JavaUtil.getDecimales(prodProv.getDescProv()));
                    nomProv.setText(prodProv.getProveedor());
                    cantidad.setText(JavaUtil.getDecimales(modeloSQL.getDouble(DETPARTIDABASE_CANTIDAD)));

                    path = prodProv.getId();

                    if (path != null) {
                        imagen.setImageFirestore(path);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void setDatos() {


        visible(refProv);
        visible(nomProv);
        visible(cantidad);
        gone(btnsave);

        System.out.println("id = " + id);
        System.out.println("secuencia = " + secuencia);
        modeloSQL = updateModelo(campos, id, secuencia);

        obtenerProdProv(modeloSQL.getString(DETPARTIDABASE_ID_DETPARTIDABASE));

        tipo = TIPOPRODUCTOPROV;

        btndelete.setVisibility(View.VISIBLE);
        tipoDetPartida.setText(tipo.toUpperCase());


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
        nombre = (EditMaterial) ctrl(R.id.etnomcdetpartidabase);
        precio = (EditMaterial) ctrl(R.id.etpreciocdetpartidabase);
        tipoDetPartida = (TextView) ctrl(R.id.tvtipocdetpartidabase);
        nomProv = (EditMaterial) ctrl(R.id.tvnomprovcdetpartidabase);
        refProv = (EditMaterial) ctrl(R.id.tvrefprovcdetpartidabase);
        descProv = (EditMaterial) ctrl(R.id.etdescprovcdetpartidabase);
        cantidad = (EditMaterial) ctrl(R.id.etcantcdetpartidabase,DETPARTIDABASE_CANTIDAD);
        imagen.getImagen().setClickable(false);

    }

    @Override
    protected void setContenedor() {

        putDato(valores, DETPARTIDABASE_CANTIDAD, JavaUtil.comprobarDouble(cantidad.getTexto()));
    }

    @Override
    protected void setcambioFragment() {

        bundle = new Bundle();
        putBundle(CAMPO_ID, id);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaBase());

    }


}