package com.codevsolution.freemarketsapp.ui;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltro;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.nosql.FragmentMasterDetailNoSQL;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.model.ProdProv;
import com.codevsolution.freemarketsapp.model.Proveedores;

import java.util.ArrayList;
import java.util.List;

import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.CAMPOS_DETPARTIDABASE;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.DETPARTIDABASE_ID_DETPARTIDABASE;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.PARTIDABASE_ID_PARTIDABASE;
import static com.codevsolution.base.sqlite.ContratoPry.Tablas.TABLA_PARTIDABASE;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PARTIDA;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PROYECTO;

public class CatalogoProdProvWeb extends FragmentMasterDetailNoSQL {

    private EditMaterial nombre;
    private EditMaterial descripcionCorta;
    private EditMaterial precio;
    private EditMaterial descProv;
    private EditMaterial refProv;
    private EditMaterial nomProv;
    private EditMaterial descripProv;
    private EditMaterial direccion;
    private EditMaterial email;
    private EditMaterial telefono;
    private ImagenLayout imgProv;
    private Button addPartida;
    private String web;
    private String idDetPartida;
    private DatabaseReference dbProductos;
    private Query query;
    private Modelo partida;
    private Modelo proyecto;
    private ProdProv prodProv;
    private Proveedores proveedor;
    private DatabaseReference dbProveedores;
    private Query queryProv;
    private WebView browser;
    private String pathProv;


    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected void setDatos() {

        nombre.setText(prodProv.getNombre());
        descripcionCorta.setText(prodProv.getDescripcion());
        refProv.setText(prodProv.getRefprov());
        precio.setText(JavaUtil.formatoMonedaLocal(prodProv.getPrecio()));
        descProv.setText(JavaUtil.getDecimales(prodProv.getDescProv()));
        path = prodProv.getRutafoto();
        web = prodProv.getWeb();

        if (path != null) {
            //setImagenFireStore(contexto, path, imagen);
            imagen.setImageFirestore(path);
        }


        dbProveedores = FirebaseDatabase.getInstance().getReference();

        queryProv = dbProveedores.child("proveedores");

        ValueEventListener eventListenerProv = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    proveedor = ds.getValue(Proveedores.class);
                    proveedor.setId(ds.getRef().getKey());
                    if (proveedor.getNombre().equals(prodProv.getProveedor())) {
                        break;
                    }
                }

                if (proveedor != null) {
                    nomProv.setText(proveedor.getNombre());
                    descripProv.setText(proveedor.getDescripcion());
                    direccion.setText(proveedor.getDireccion());
                    email.setText(proveedor.getEmail());
                    telefono.setText(proveedor.getTelefono());
                    pathProv = proveedor.getRutafoto();

                    if (pathProv != null) {
                        imgProv.setImageFirestore(pathProv);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };

        queryProv.addValueEventListener(eventListenerProv);

        if (web != null && JavaUtil.isValidURL(web)) {

            browser.getSettings().setJavaScriptEnabled(true);
            browser.getSettings().setBuiltInZoomControls(true);
            browser.getSettings().setDisplayZoomControls(false);

            browser.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            // Cargamos la web
            browser.loadUrl(web);
        }

    }


    @Override
    protected ListaAdaptadorFiltro setAdaptadorAuto(Context context, int layoutItem, ArrayList lista) {
        return new AdapterFiltro(context, layoutItem, lista);
    }

    @Override
    protected void setLista() {

        lista = new ArrayList<ProdProv>();

        dbProductos = FirebaseDatabase.getInstance().getReference();

        query = dbProductos.child("productos");

        ValueEventListener eventListenerProd = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ProdProv prodProv = ds.getValue(ProdProv.class);
                    prodProv.setId(ds.getRef().getKey());
                    lista.add(prodProv);
                }

                System.out.println("lista prov: " + lista.size());
                setRv();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };

        query.addValueEventListener(eventListenerProd);
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        if (bundle != null) {

            partida = (Modelo) getBundleSerial(PARTIDA);
            proyecto = (Modelo) getBundleSerial(PROYECTO);
            origen = getStringBundle(ORIGEN, "");

        }

        if (origen != null && origen.equals(PARTIDA)) {
            visible(addPartida);
        } else {
            gone(addPartida);
        }
    }

    @Override
    public void setOnClickRV(Object object) {

        prodProv = (ProdProv) object;
        esDetalle = true;
        selector();

    }

    @Override
    protected void setLayout() {

        layoutItem = R.layout.item_list_catalogo_prod_prov_web;
        layoutCuerpo = R.layout.catalogo_prod_prov_web;

    }

    @Override
    protected void setInicio() {

        descripcionCorta = (EditMaterial) ctrl(R.id.etdesccatalogo);
        precio = (EditMaterial) ctrl(R.id.etpreciocatalogo);
        nombre = (EditMaterial) ctrl(R.id.etnombrecatalogo);
        imagen = (ImagenLayout) ctrl(R.id.imgcatalogo);
        refProv = (EditMaterial) ctrl(R.id.etrefprovcatalogo);
        descProv = (EditMaterial) ctrl(R.id.etporcdesprovcatalogo);
        addPartida = (Button) ctrl(R.id.btn_add_partida);
        nomProv = (EditMaterial) ctrl(R.id.etnombreprovcatalogo);
        descripProv = (EditMaterial) ctrl(R.id.etdescripprovcatalogo);
        direccion = (EditMaterial) ctrl(R.id.etdireccionprovcatalogo);
        email = (EditMaterial) ctrl(R.id.etemailprovcatalogo);
        telefono = (EditMaterial) ctrl(R.id.ettelefonoprovcatalogo);
        imgProv = (ImagenLayout) ctrl(R.id.imgprovcatalogo);
        browser = (WebView) view.findViewById(R.id.etwebcatalogo);


    }

    @Override
    protected void setMaestroDetalleTabletPort() {
        maestroDetalleSeparados = true;
    }

    @Override
    protected void setAcciones() {

        addPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle = new Bundle();
                String idPartidabase = AndroidUtil.getSharePreference(contexto, PERSISTENCIA, PARTIDABASE_ID_PARTIDABASE, NULL);
                putBundle(CAMPO_ID, idPartidabase);
                putBundle(CAMPO_SECUENCIA, crearProdProvBase(idPartidabase));
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaBaseProdProvCat());
            }
        });

    }

    private int crearProdProvBase(String idPartidabase) {
        ContentValues valores = new ContentValues();
        putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_ID_DETPARTIDABASE, prodProv.getId());
        return CRUDutil.crearRegistroSec(CAMPOS_DETPARTIDABASE, TABLA_PARTIDABASE, idPartidabase, valores);

    }

    private class AdapterFiltro extends ListaAdaptadorFiltro {


        public AdapterFiltro(Context contexto, int R_layout_IdView, ArrayList entradas) {
            super(contexto, R_layout_IdView, entradas);

        }

        @Override
        public void onEntrada(Object entrada, View view) {

            ImagenLayout imagen = view.findViewById(R.id.imagenprov);
            TextView nombre = view.findViewById(R.id.tvnomprov);
            TextView descripcion = view.findViewById(R.id.tvdescprov);
            TextView importe = view.findViewById(R.id.tvprecioprov);
            TextView refProv = view.findViewById(R.id.tvrefprov);

            nombre.setText(((ProdProv) entrada).getNombre());
            descripcion.setText(((ProdProv) entrada).getDescripcion());
            importe.setText(String.valueOf(((ProdProv) entrada).getPrecio()));
            refProv.setText(((ProdProv) entrada).getRefprov());
            String rutafoto = ((ProdProv) entrada).getRutafoto();

            if (((ProdProv) entrada).getRutafoto() != null) {

                imagen.setImageFirestoreCircle(rutafoto);
            }

        }

        @Override
        public List onFilter(ArrayList entradas, CharSequence constraint) {

            List suggestion = new ArrayList();

            System.out.println("constraint = " + constraint.toString());

            for (Object entrada : entradas) {

                if (((ProdProv) entrada).getNombre() != null && ((ProdProv) entrada).getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((ProdProv) entrada).getCategoria() != null && ((ProdProv) entrada).getCategoria().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((ProdProv) entrada).getDescripcion() != null && ((ProdProv) entrada).getDescripcion().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((ProdProv) entrada).getProveedor() != null && ((ProdProv) entrada).getProveedor().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((ProdProv) entrada).getRefprov() != null && ((ProdProv) entrada).getRefprov().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((ProdProv) entrada).getAlcance() != null && ((ProdProv) entrada).getAlcance().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                }
            }
            return suggestion;
        }
    }

    private class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        ImagenLayout imagen;
        TextView nombre;
        TextView descripcion;
        TextView importe;
        TextView refProv;

        public ViewHolderRV(View view) {
            super(view);

            imagen = view.findViewById(R.id.imagenprov);
            nombre = view.findViewById(R.id.tvnomprov);
            descripcion = view.findViewById(R.id.tvdescprov);
            importe = view.findViewById(R.id.tvprecioprov);
            refProv = view.findViewById(R.id.tvrefprov);

        }

        @Override
        public void bind(ArrayList<?> lista, int position) {

            ProdProv prodProv = (ProdProv) lista.get(position);

            nombre.setText(prodProv.getNombre());
            descripcion.setText(prodProv.getDescripcion());
            importe.setText(String.valueOf(prodProv.getPrecio()));
            refProv.setText(prodProv.getRefprov());
            String rutafoto = (prodProv.getRutafoto());

            if (prodProv.getRutafoto() != null) {

                imagen.setImageFirestoreCircle(rutafoto);
            }

            super.bind(lista, position);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }


}
