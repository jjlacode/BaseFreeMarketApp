package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chargebee.models.Subscription;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.codevsolution.freemarketsapp.model.ProdProv;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.base.sqlite.ConsultaBD.queryObjectDetalle;

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
    protected void setNuevo() {


    }

    @Override
    protected void setTabla() {

        tabla = TABLA_DETPARTIDABASE;

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
                    cantidad.setText(JavaUtil.getDecimales(modelo.getDouble(DETPARTIDABASE_CANTIDAD)));

                    path = prodProv.getRutafoto();

                    if (path != null) {
                        imagen.setImageFirestoreCircle(path);
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

        allGone();

        visible(nombre);
        visible(descripcion);
        visible(refProv);
        visible(nomProv);
        visible(precio);
        visible(tipoDetPartida);
        visible(cantidad);
        gone(btnsave);

        modelo = CRUDutil.setModelo(campos, id, secuencia);

        obtenerProdProv(modelo.getString(PARTIDABASE_ID_PARTIDABASE));

        tipo = TIPOPRODUCTOPROV;

        btndelete.setVisibility(View.VISIBLE);
        tipoDetPartida.setText(tipo.toUpperCase());


    }

    @Override
    protected void setImagen() {

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

        descripcion = (EditMaterial) ctrl(R.id.etdesccdetpartidabase);
        precio = (EditMaterial) ctrl(R.id.etpreciocdetpartidabase);
        nombre = (EditMaterial) ctrl(R.id.etnomcdetpartidabase);
        precio = (EditMaterial) ctrl(R.id.etpreciocdetpartidabase);
        imagen = (ImagenLayout) ctrl(R.id.imgcdetpartidabase);
        tipoDetPartida = (TextView) ctrl(R.id.tvtipocdetpartidabase);
        nomProv = (EditMaterial) ctrl(R.id.tvnomprovcdetpartidabase);
        refProv = (EditMaterial) ctrl(R.id.tvrefprovcdetpartidabase);
        descProv = (EditMaterial) ctrl(R.id.etdescprovcdetpartidabase);
        cantidad = (EditMaterial) ctrl(R.id.etcantcdetpartidabase);
        imagen.setClickable(false);

    }

    @Override
    protected void setContenedor() {

        putDato(valores, campos, DETPARTIDABASE_CANTIDAD, JavaUtil.comprobarDouble(cantidad.getTexto()));
    }

    @Override
    protected void setcambioFragment() {

        bundle = new Bundle();
        putBundle(CAMPO_ID, id);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaBase());

    }


}