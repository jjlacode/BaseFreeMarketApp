package com.jjlacode.freelanceproject.ui;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.base.util.android.controls.ImagenLayout;
import com.jjlacode.base.util.crud.FragmentCUD;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.media.MediaUtil;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.logica.Interactor;
import com.jjlacode.freelanceproject.model.Categorias;
import com.jjlacode.freelanceproject.model.ProdProv;
import com.jjlacode.freelanceproject.model.Proveedores;
import com.jjlacode.freelanceproject.sqlite.ContratoPry;

import java.util.ArrayList;
import java.util.List;

import static com.jjlacode.base.util.sqlite.ConsultaBD.idInsertRegistro;
import static com.jjlacode.base.util.sqlite.ConsultaBD.putDato;
import static com.jjlacode.base.util.sqlite.ConsultaBD.queryList;
import static com.jjlacode.base.util.sqlite.ConsultaBD.queryObject;
import static com.jjlacode.base.util.sqlite.ConsultaBD.updateRegistro;

public class FragmentCUDDetpartidaBase extends FragmentCUD implements Interactor.Constantes,
        ContratoPry.Tablas, Interactor.TiposDetPartida {

    private EditMaterial descripcion;
    private EditMaterial precio;
    private EditMaterial descProv;
    private TextView refProv;
    private TextView tipoDetPartida;
    private AutoCompleteTextView autoNombre;
    private EditMaterial tiempo;
    private RecyclerView rvDetpartida;
    private String tipo;
    private Modelo partidabase;
    private ArrayList<Modelo> lista;

    private ArrayList<ProdProv> listaProdProv;
    private AutoCompleteTextView autoCat;
    private AutoCompleteTextView autoProv;
    private String iddet;
    private double pvp;
    private ArrayList<Proveedores> listaProv;
    private ArrayList<Categorias> listaCat;
    private String proveedor = TODOS;
    private String categoria = TODAS;
    private DatabaseReference dbProductos;
    private Query query;

    public FragmentCUDDetpartidaBase() {
        // Required empty public constructor
    }

    @Override
    protected void setNuevo() {

        refProv.setVisibility(View.GONE);
        precio.setVisibility(View.GONE);
        tiempo.setVisibility(View.GONE);
        descProv.setVisibility(View.GONE);
        btndelete.setVisibility(View.GONE);
        autoCat.setVisibility(View.GONE);
        autoProv.setVisibility(View.GONE);


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

        if (bundle != null) {
            partidabase = (Modelo) bundle.getSerializable(TABLA_PARTIDABASE);
            if (modelo!=null){
            secuencia = modelo.getInt(DETPARTIDABASE_SECUENCIA);
            id = modelo.getString(DETPARTIDABASE_ID_PARTIDABASE);
            }
            tipo = bundle.getString(TIPO);
            bundle = null;
        }

    }

    @Override
    protected void setDatos() {

        if (secuencia >0){

            btndelete.setVisibility(View.VISIBLE);
            tipo = modelo.getString(DETPARTIDABASE_TIPO);
            autoNombre.setText(modelo.getString(DETPARTIDABASE_NOMBRE));
            descripcion.setText(modelo.getString(DETPARTIDABASE_DESCRIPCION));
            pvp = modelo.getDouble(DETPARTIDABASE_TIEMPO);
            precio.setText(JavaUtil.formatoMonedaLocal(pvp * Interactor.hora));
            tiempo.setText(modelo.getString(DETPARTIDABASE_TIEMPO));
            descProv.setText(modelo.getString(DETPARTIDABASE_DESCUENTOPROVCAT));
            refProv.setText(modelo.getString(DETPARTIDABASE_REFPROVCAT));
            if (modelo.getString(DETPARTIDABASE_RUTAFOTO)!=null){
                path = modelo.getString(DETPARTIDABASE_RUTAFOTO);
                setImagenUri(contexto,path);
            }

            autoNombre.setThreshold(3);
            gone(refProv);
            gone(descProv);
            gone(autoCat);
            gone(autoProv);
            gone(rvDetpartida);

        }


    }

    @Override
    protected void setAcciones() {

        tipoDetPartida.setText(tipo.toUpperCase());

        rv();


        autoNombre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (tipo) {


                    case TIPOTRABAJO:

                        Modelo tarea = (Modelo) autoNombre.getAdapter().getItem(position);
                        iddet = tarea.getString(TRABAJO_ID_TRABAJO);
                        System.out.println("id = " + iddet);
                        autoNombre.setText(tarea.getString(TRABAJO_NOMBRE));
                        descripcion.setText(tarea.getString(TRABAJO_DESCRIPCION));
                        tiempo.setText(tarea.getString(TRABAJO_TIEMPO));
                        if (tarea.getString(TRABAJO_RUTAFOTO) != null) {
                            setImagenUri(contexto,tarea.getString(TRABAJO_RUTAFOTO));
                        }

                        break;

                    case TIPOPRODUCTO:

                        Modelo producto = (Modelo) autoNombre.getAdapter().getItem(position);
                        iddet = producto.getString(PRODUCTO_ID_PRODUCTO);
                        System.out.println("id = " + iddet);
                        autoNombre.setText(producto.getString(PRODUCTO_NOMBRE));
                        descripcion.setText(producto.getString(PRODUCTO_DESCRIPCION));
                        precio.setText(producto.getString(PRODUCTO_PRECIO));
                        if (producto.getString(PRODUCTO_RUTAFOTO) != null) {
                            setImagenUri(contexto,producto.getString(PRODUCTO_RUTAFOTO));
                        }

                        break;

                    case TIPOPRODUCTOPROV:

                        ProdProv prodprov = (ProdProv) autoNombre.getAdapter().getItem(position);
                        iddet = prodprov.getId();
                        System.out.println("id = " + iddet);
                        autoNombre.setText(prodprov.getNombre());
                        descripcion.setText(prodprov.getDescripcion());
                        precio.setText(String.valueOf(prodprov.getPrecio()));
                        refProv.setText(prodprov.getRefprov());
                        if (prodprov.getRutafoto() != null) {
                            imagen.setImageFirestoreCircle(prodprov.getRutafoto());

                        }

                        break;


                }

            }

        });

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

        rvDetpartida = (RecyclerView) ctrl(R.id.rvcdetpartidabase);
        descripcion = (EditMaterial) ctrl(R.id.etdesccdetpartidabase);
        precio = (EditMaterial) ctrl(R.id.etpreciocdetpartidabase);
        autoNombre = (AutoCompleteTextView) ctrl(R.id.etnomcdetpartidabase);
        tiempo = (EditMaterial) ctrl(R.id.ettiempocdetpartidabase);
        imagen = (ImagenLayout) ctrl(R.id.imgcdetpartidabase);
        refProv = (TextView) ctrl(R.id.tvrefprovcdetpartidabase);
        descProv = (EditMaterial) ctrl(R.id.etdescprovcdetpartidabase);
        tipoDetPartida = (TextView) ctrl(R.id.tvtipocdetpartidabase);
        autoCat = (AutoCompleteTextView) ctrl(R.id.autocatbase);
        autoProv = (AutoCompleteTextView) ctrl(R.id.autoprovbase);

    }

    private void rv(){

        rvDetpartida.setLayoutManager(new LinearLayoutManager(getContext()));

        switch (tipo) {

            case TIPOTRABAJO:

                tiempo.setVisibility(View.VISIBLE);
                lista = queryList(CAMPOS_TRABAJO);
                AdaptadorTareas adaptadorTareas = new AdaptadorTareas(lista);
                rvDetpartida.setAdapter(adaptadorTareas);
                adaptadorTareas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iddet = lista.get(rvDetpartida.getChildAdapterPosition(v))
                                .getString(TRABAJO_ID_TRABAJO);
                        System.out.println("id = " + iddet);
                        Modelo tarea = queryObject(CAMPOS_TRABAJO, iddet);
                        autoNombre.setText(tarea.getString(TRABAJO_NOMBRE));
                        descripcion.setText(tarea.getString(TRABAJO_DESCRIPCION));
                        tiempo.setText(tarea.getString(TRABAJO_TIEMPO));

                        if (tarea.getString(TRABAJO_RUTAFOTO) != null) {
                            path = tarea.getString(TRABAJO_RUTAFOTO);
                            setImagenUri(contexto,path);

                        }
                    }
                });
                break;

            case TIPOPRODUCTO:

                precio.setVisibility(View.VISIBLE);
                lista = queryList(CAMPOS_PRODUCTO);
                AdaptadorProducto adaptadorProducto = new AdaptadorProducto(lista);
                rvDetpartida.setAdapter(adaptadorProducto);
                adaptadorProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iddet = lista.get(rvDetpartida.getChildAdapterPosition(v))
                                .getString(PRODUCTO_ID_PRODUCTO);
                        System.out.println("id = " + iddet);
                        Modelo producto = queryObject(CAMPOS_PRODUCTO, iddet);
                        autoNombre.setText(producto.getString(PRODUCTO_NOMBRE));
                        descripcion.setText(producto.getString(PRODUCTO_DESCRIPCION));
                        precio.setText(producto.getString(PRODUCTO_PRECIO));

                        if (producto.getString(PRODUCTO_RUTAFOTO) != null) {
                            path = producto.getString(PRODUCTO_RUTAFOTO);
                            setImagenUri(contexto,path);

                        }

                    }
                });

                break;


            case TIPOPRODUCTOPROV:

                precio.setVisibility(View.VISIBLE);
                refProv.setVisibility(View.VISIBLE);
                descProv.setVisibility(View.VISIBLE);
                autoCat.setVisibility(View.VISIBLE);
                autoProv.setVisibility(View.VISIBLE);

                listaProdProv = new ArrayList<>();

                dbProductos =
                        FirebaseDatabase.getInstance().getReference();

                query = dbProductos.child("productos");


                ValueEventListener eventListenerProd = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ProdProv prodProv = ds.getValue(ProdProv.class);
                            prodProv.setId(ds.getRef().getKey());
                            listaProdProv.add(prodProv);
                        }
                        ArrayList<ProdProv> prodProvtemp = new ArrayList<>();

                        for (ProdProv prodProv : listaProdProv) {

                            //if ((prodProv.getCategoria().equals(categoria) || categoria.equals(TODAS)) &&
                            //        (prodProv.getProveedor().equals(proveedor)) || proveedor.equals(TODOS)) {
                            prodProvtemp.add(prodProv);
                            //}

                        }
                        System.out.println("lista provtmp: " + prodProvtemp.size());

                        listaProdProv.clear();
                        listaProdProv.addAll(prodProvtemp);
                        prodProvtemp.clear();

                        System.out.println("lista prov: " + listaProdProv.size());
                        AdaptadorProveedor provAdapter = new AdaptadorProveedor(listaProdProv);

                        rvDetpartida.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvDetpartida.setHasFixedSize(true);

                        rvDetpartida.setAdapter(provAdapter);

                        provAdapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                int i = rvDetpartida.getChildViewHolder(v).getAdapterPosition();
                                iddet = listaProdProv.get(i).getId();

                                refProv.setText(listaProdProv.get(i).getRefprov());
                                autoNombre.setText(listaProdProv.get(i).getNombre());
                                descripcion.setText(listaProdProv.get(i).getDescripcion());
                                precio.setText(String.valueOf(listaProdProv.get(i).getPrecio()));

                                path = listaProdProv.get(i).getRutafoto();
                                if (path != null) {
                                    imagen.setImageFirestoreCircle(path);
                                }


                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                };

                query.addValueEventListener(eventListenerProd);


        }


    }

    //Start Listening Adapter

    private void nuevaTarea() {

        if (descripcion.getText() != null && tiempo.getText() != null && autoNombre.getText() != null) {

            int cont = 0;

            ContentValues valorestarea = new ContentValues();

            putDato(valorestarea, CAMPOS_TRABAJO, TRABAJO_DESCRIPCION, descripcion.getText().toString());
            putDato(valorestarea, CAMPOS_TRABAJO, TRABAJO_TIEMPO, tiempo.getText().toString());
            putDato(valorestarea, CAMPOS_TRABAJO, TRABAJO_NOMBRE, autoNombre.getText().toString());
            putDato(valorestarea, CAMPOS_TRABAJO, TRABAJO_RUTAFOTO, path);

            if (lista != null && lista.size() > 0) {

                for (Modelo tarea : lista) {
                    if (tarea.getString(TRABAJO_NOMBRE).equals(autoNombre.getText().toString())) {
                        updateRegistro(TABLA_TRABAJO, tarea.getString(TRABAJO_ID_TRABAJO), valorestarea);
                        Toast.makeText(getContext(), "tarea actualizada", Toast.LENGTH_SHORT).show();
                        cont++;
                    }
                }
            }
            if (cont == 0) {

                try {

                    iddet = idInsertRegistro(TABLA_TRABAJO, valorestarea);

                    Toast.makeText(getContext(), "Nueva tarea guardada", Toast.LENGTH_SHORT).show();

                    rv();

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error al guardar tarea", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void nuevoProducto() {

        if (descripcion.getText() != null && precio.getText() != null && autoNombre.getText() != null) {

            int cont = 0;

            ContentValues valoresprod = new ContentValues();

            putDato(valoresprod, CAMPOS_PRODUCTO, PRODUCTO_DESCRIPCION, descripcion.getText().toString());
            putDato(valoresprod, CAMPOS_PRODUCTO, PRODUCTO_PRECIO, precio.getText().toString());
            putDato(valoresprod, CAMPOS_PRODUCTO, PRODUCTO_NOMBRE, autoNombre.getText().toString());
            putDato(valoresprod, CAMPOS_PRODUCTO, PRODUCTO_RUTAFOTO, path);

            if (lista != null && lista.size() > 0) {

                for (Modelo producto : lista) {
                    if (producto.getString(PRODUCTO_NOMBRE).equals(autoNombre.getText().toString())) {
                        updateRegistro(TABLA_PRODUCTO, producto.getString(PRODUCTO_ID_PRODUCTO), valoresprod);
                        Toast.makeText(getContext(), "producto actualizdo", Toast.LENGTH_SHORT).show();
                        cont++;
                    }
                }
            }
            if (cont == 0) {

                try {


                    iddet = idInsertRegistro(TABLA_PRODUCTO, valoresprod);

                    Toast.makeText(getContext(), "Nuevo producto guardado", Toast.LENGTH_SHORT).show();

                    rv();

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error al guardar producto", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


    @Override
    protected boolean registrar() {

        Interactor.Calculos.sincronizarPartidaBase(id);
            partidabase = queryObject(CAMPOS_PARTIDABASE,id);

            super.registrar();

            return true;

    }

    @Override
    protected void setContenedor() {

        setDato(DETPARTIDABASE_NOMBRE, autoNombre.getText().toString());
        setDato(DETPARTIDABASE_DESCRIPCION, descripcion.getText().toString());
        setDato(DETPARTIDABASE_CANTIDAD, 1);
        setDato(DETPARTIDABASE_RUTAFOTO, path);
        setDato(DETPARTIDABASE_ID_DETPARTIDABASE, iddet);
        setDato(DETPARTIDABASE_ID_PARTIDABASE, id);
        setDato(DETPARTIDABASE_TIPO, tipo);

        switch (tipo) {

            case TIPOTRABAJO:

                nuevaTarea();
                setDato(DETPARTIDABASE_TIEMPO, tiempo.getText().toString());

                break;

            case TIPOPRODUCTO:

                nuevoProducto();
                setDato(DETPARTIDABASE_PRECIO, precio.getText().toString());

                break;


            case TIPOPRODUCTOPROV:

                setDato(DETPARTIDABASE_PRECIO, precio.getText().toString());
                setDato(DETPARTIDABASE_DESCUENTOPROVCAT, descProv.getText().toString());
                setDato(DETPARTIDABASE_REFPROVCAT, refProv.getText().toString());


        }
    }

    @Override
    protected boolean update() {

        partidabase = queryObject(CAMPOS_PARTIDABASE,id);

            super.update();

        Interactor.Calculos.actualizarPartidaBase(id);
        partidabase = queryObject(CAMPOS_PARTIDABASE,id);


        return true;
    }

    @Override
    protected void setcambioFragment() {
        super.setcambioFragment();

        enviarBundle();
        putBundleModelo(partidabase);
        putBundle(TIPO, tipo);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaBase());

    }




    public class ListaAdaptadorFiltroProv extends ArrayAdapter<Proveedores> {

        private ArrayList<Proveedores> entradas;
        private ArrayList<Proveedores> entradasfiltro;
        private int R_layout_IdView;
        private Context contexto;

        ListaAdaptadorFiltroProv(Context contexto, int R_layout_IdView, ArrayList<Proveedores> entradas) {
            super(contexto, R_layout_IdView, entradas);
            this.contexto = contexto;
            this.entradas = entradas;
            this.entradasfiltro = new ArrayList<>(entradas);
            this.R_layout_IdView = R_layout_IdView;
        }

        @Override
        public View getView(int position, View view, ViewGroup pariente) {
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R_layout_IdView, null);
            }

            TextView nombre, descripcion, direccion;
            ImageView imagenProv;

            nombre = view.findViewById(R.id.tvnomproveedor);
            descripcion = view.findViewById(R.id.tvdescproveedor);
            direccion = view.findViewById(R.id.tvdirproveedor);
            imagenProv = view.findViewById(R.id.imagenproveedor);

            nombre.setText(entradasfiltro.get(position).getNombre());
            descripcion.setText(entradasfiltro.get(position).getDescripcion());
            direccion.setText(entradasfiltro.get(position).getDireccion());
            String pathprov = entradasfiltro.get(position).getRutafoto();
            if (pathprov != null) {
                setImagenFireStoreCircle(contexto, pathprov, imagenProv);
            }


            return view;
        }

        @Override
        public int getCount() {
            return entradasfiltro.size();
        }


        @NonNull
        @Override
        public Filter getFilter() {

            Filter filter;
            filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    List<Proveedores> suggestion = new ArrayList<>();
                    if (constraint != null) {

                        for (Proveedores item : entradas) {

                            if (item.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            } else if (item.getDescripcion().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            }


                        }
                        // Query the autocomplete API for the entered constraint
                        // Results
                        results.values = suggestion;
                        results.count = suggestion.size();
                    }
                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    entradasfiltro.clear();

                    if (results != null && results.count > 0) {
                        for (Proveedores item : (List<Proveedores>) results.values) {
                            entradasfiltro.add(item);
                        }
                        notifyDataSetChanged();
                    } else if (constraint == null) {
                        // no filter, addModelo entire original list back in
                        entradasfiltro.addAll(entradas);
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        @Override
        public Proveedores getItem(int posicion) {
            return entradasfiltro.get(posicion);
        }

        @Override
        public long getItemId(int posicion) {
            return posicion;
        }

    }

    public class ListaAdaptadorFiltroCat extends ArrayAdapter<Categorias> {

        private ArrayList<Categorias> entradas;
        private ArrayList<Categorias> entradasfiltro;
        private int R_layout_IdView;
        private Context contexto;

        ListaAdaptadorFiltroCat(Context contexto, int R_layout_IdView, ArrayList<Categorias> entradas) {
            super(contexto, R_layout_IdView, entradas);
            this.contexto = contexto;
            this.entradas = entradas;
            this.entradasfiltro = new ArrayList<>(entradas);
            this.R_layout_IdView = R_layout_IdView;
        }

        @Override
        public View getView(int position, View view, ViewGroup pariente) {
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R_layout_IdView, null);
            }

            TextView nombre, descripcion;
            ImageView imagencat;

            nombre = view.findViewById(R.id.tvnomcat);
            descripcion = view.findViewById(R.id.tvdesccat);
            imagencat = view.findViewById(R.id.imagencat);

            nombre.setText(entradasfiltro.get(position).getNombre());
            descripcion.setText(entradasfiltro.get(position).getDescripcion());

            String pathcat = entradasfiltro.get(position).getRutafoto();
            if (pathcat != null) {
                setImagenFireStoreCircle(contexto, pathcat, imagencat);
            }
            return view;
        }

        @Override
        public int getCount() {
            return entradasfiltro.size();
        }


        @NonNull
        @Override
        public Filter getFilter() {

            Filter filter;
            filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    List<Categorias> suggestion = new ArrayList<>();
                    if (constraint != null) {

                        for (Categorias item : entradas) {

                            if (item.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            } else if (item.getDescripcion().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            }


                        }
                        // Query the autocomplete API for the entered constraint
                        // Results
                        results.values = suggestion;
                        results.count = suggestion.size();
                    }
                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    entradasfiltro.clear();

                    if (results != null && results.count > 0) {
                        for (Categorias item : (List<Categorias>) results.values) {
                            entradasfiltro.add(item);
                        }
                        notifyDataSetChanged();
                    } else if (constraint == null) {
                        // no filter, addModelo entire original list back in
                        entradasfiltro.addAll(entradas);
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        @Override
        public Categorias getItem(int posicion) {
            return entradasfiltro.get(posicion);
        }

        @Override
        public long getItemId(int posicion) {
            return posicion;
        }

    }


    public class AdaptadorProducto extends RecyclerView.Adapter<AdaptadorProducto.ProductoViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas {

        ArrayList<Modelo> listaProductos;
        View.OnClickListener listener;

        public AdaptadorProducto(ArrayList<Modelo> listaProductos) {
            this.listaProductos = listaProductos;
        }

        public void setOnClickListener(View.OnClickListener listener){

            this.listener = listener;
        }

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_producto_detpartida,null,false);

            view.setOnClickListener(this);

            return new ProductoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder productoViewHolder, int position) {

            productoViewHolder.importe.setText(listaProductos.get(position).getString(PRODUCTO_PRECIO));
            productoViewHolder.nombre.setText(listaProductos.get(position).getString(PRODUCTO_NOMBRE));

            if (listaProductos.get(position).getString(PRODUCTO_RUTAFOTO)!=null){

                productoViewHolder.imagen.setImageURI(listaProductos.get(position).getUri(PRODUCTO_RUTAFOTO));
            }

        }

        @Override
        public int getItemCount() {
            return listaProductos.size();
        }

        @Override
        public void onClick(View v) {

            if (listener!=null){

                listener.onClick(v);
            }

        }

        public class ProductoViewHolder extends RecyclerView.ViewHolder {

            TextView importe,nombre;
            ImageView imagen;
            public ProductoViewHolder(@NonNull View itemView) {
                super(itemView);

                importe = itemView.findViewById(R.id.tvimportelproductos_detpartida);
                nombre = itemView.findViewById(R.id.tvnombrelproductos_detpartida);
                imagen = itemView.findViewById(R.id.imglproductos_detpartida);
            }
        }
    }
    public class AdaptadorTareas extends RecyclerView.Adapter<AdaptadorTareas.TareaViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas {

        ArrayList<Modelo> listaTareas;
        private View.OnClickListener listener;

        public AdaptadorTareas(ArrayList<Modelo> listaTareas) {
            this.listaTareas = listaTareas;
        }

        @NonNull
        @Override
        public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_trabajo_detpartida,null,false);

            view.setOnClickListener(this);

            return new TareaViewHolder(view) ;
        }

        public void setOnClickListener(View.OnClickListener listener){

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull TareaViewHolder tareaViewHolder, int position) {

            tareaViewHolder.tiempo.setText(listaTareas.get(position).getCampos(TRABAJO_TIEMPO));
            tareaViewHolder.nombre.setText(listaTareas.get(position).getString(TRABAJO_NOMBRE));
            if (listaTareas.get(position).getString(TRABAJO_RUTAFOTO)!=null){

                tareaViewHolder.imagen.setImageURI(listaTareas.get(position).getUri(TRABAJO_RUTAFOTO));
            }

        }

        @Override
        public int getItemCount() {
            return listaTareas.size();
        }

        @Override
        public void onClick(View v) {

            if (listener!=null){

                listener.onClick(v);
            }

        }

        public class TareaViewHolder extends RecyclerView.ViewHolder {

            TextView tiempo, nombre;
            ImageView imagen;

            public TareaViewHolder(@NonNull View itemView) {
                super(itemView);


                tiempo = itemView.findViewById(R.id.tvtiempoltareas_detpartida);
                imagen = itemView.findViewById(R.id.imgltarea_detpartida);
                nombre = itemView.findViewById(R.id.tvnomltarea_detpartida);
            }
        }
    }

    public class AdaptadorProveedor extends RecyclerView.Adapter<AdaptadorProveedor.ViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas {

        View.OnClickListener listener;
        ArrayList<ProdProv> lista;

        public AdaptadorProveedor(ArrayList<ProdProv> lista) {
            this.lista = lista;
        }

        @Override
        public void onClick(View v) {

        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_prodprov, null, false);

            view.setOnClickListener(this);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.refprov.setText(lista.get(position).getRefprov());
            holder.nombre.setText(lista.get(position).getNombre());
            holder.descripcion.setText(lista.get(position).getDescripcion());
            holder.precio.setText(String.valueOf(lista.get(position).getPrecio()));
            if (lista.get(position).getRutafoto() != null) {

                MediaUtil imagenUtil = new MediaUtil(contexto);
                imagenUtil.setImageFireStoreCircle(lista.get(position).getRutafoto(), holder.imagenProv);
            }
        }

        @Override
        public int getItemCount() {
            if (lista != null) {
                return lista.size();
            }
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView refprov, nombre, descripcion, precio;
            private ImageView imagenProv;
            private String path;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                refprov = itemView.findViewById(R.id.tvrefprov);
                nombre = itemView.findViewById(R.id.tvnomprov);
                descripcion = itemView.findViewById(R.id.tvdescprov);
                precio = itemView.findViewById(R.id.tvprecioprov);
                imagenProv = itemView.findViewById(R.id.imagenprov);

            }
        }

    }


    public class AdaptadorProdProv extends FirebaseRecyclerAdapter<ProdProv, AdaptadorProdProv.ProdProvHolder>
            implements View.OnClickListener{

        View.OnClickListener listener;

        public AdaptadorProdProv(@NonNull FirebaseRecyclerOptions<ProdProv> options) {
            super(options);
        }

        public void setOnClickListener(View.OnClickListener listener){

            this.listener = listener;
        }

        @NonNull
        @Override
        public ProdProvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_prodprov, parent, false);

            view.setOnClickListener(this);
            return new ProdProvHolder(view);
        }

        @Override
        protected void onBindViewHolder(@NonNull ProdProvHolder prodProvHolder, int i, @NonNull ProdProv prodProv) {

            prodProvHolder.setRefprov(prodProv.getRefprov());
            prodProvHolder.setNombre(prodProv.getNombre());
            prodProvHolder.setDescripcion(prodProv.getDescripcion());
            prodProvHolder.setPrecio(prodProv.getPrecio());
            prodProvHolder.setRutafoto(prodProv.getRutafoto());
            prodProvHolder.setId(prodProv.getId());
            System.out.println("id prov" + prodProv.getId());

        }

        @Override
        public void onClick(View v) {

            if (listener!=null){

                listener.onClick(v);
            }

        }

        public class ProdProvHolder extends RecyclerView.ViewHolder {

            private View mView;
            private TextView refprov,nombre,descripcion,precio;
            private ImageView imagen;
            private String path;
            private String id;

            public ProdProvHolder(View itemView) {
                super(itemView);
                mView = itemView;
            }

            public ProdProv getItem(){

                ProdProv prodProv = new ProdProv();
                prodProv.setRefprov(refprov.getText().toString());
                prodProv.setNombre(nombre.getText().toString());
                prodProv.setDescripcion(descripcion.getText().toString());
                prodProv.setPrecio(JavaUtil.comprobarDouble(precio.getText().toString()));
                prodProv.setRutafoto(path);
                prodProv.setId(id);


                return prodProv;

            }

            public void setId(String id){
                this.id = id;
            }

            public void setRefprov(String refprov) {
                this.refprov = mView.findViewById(R.id.tvrefprov);
                this.refprov.setText(refprov);
            }

            public void setNombre(String nombre) {
                this.nombre = mView.findViewById(R.id.tvnomprov);
                this.nombre.setText(nombre);
            }

            public void setDescripcion(String descripcion) {
                this.descripcion = mView.findViewById(R.id.tvdescprov);
                this.descripcion.setText(descripcion);
            }

            public void setPrecio(double precio) {
                this.precio = mView.findViewById(R.id.tvprecioprov);
                this.precio.setText(String.valueOf(precio));
            }

            public void setRutafoto(String rutafoto) {
                imagen = mView.findViewById(R.id.imagenprov);
                path = rutafoto;
                setImagenFireStoreCircle(contexto,path,imagen);
            }
        }
    }


    public abstract class ListaAdaptadorFiltroProdProv extends ArrayAdapter<ProdProv> {

        private ArrayList<ProdProv> entradas;
        private ArrayList<ProdProv> entradasfiltro;
        private int R_layout_IdView;
        private Context contexto;

        ListaAdaptadorFiltroProdProv(Context contexto, int R_layout_IdView, ArrayList<ProdProv> entradas) {
            super(contexto,R_layout_IdView,entradas);
            this.contexto = contexto;
            this.entradas = entradas;
            this.entradasfiltro = new ArrayList<>(entradas);
            this.R_layout_IdView = R_layout_IdView;
        }

        @Override
        public View getView(int posicion, View view, ViewGroup pariente) {
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R_layout_IdView, null);
            }
            onEntrada (entradasfiltro.get(posicion), view);
            System.out.println("entradasfiltro = " + entradasfiltro.get(posicion));
            return view;
        }

        @Override
        public int getCount() {
            return entradasfiltro.size();
        }


        @NonNull
        @Override
        public Filter getFilter() {

            Filter filter;
            filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    List<ProdProv> suggestion = new ArrayList<>();
                    if (constraint != null) {

                        for (ProdProv item :entradas) {



                            if(item.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            }


                        }
                        // Query the autocomplete API for the entered constraint
                        // Results
                        results.values = suggestion;
                        results.count = suggestion.size();
                    }
                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    entradasfiltro.clear();

                    if (results != null && results.count > 0) {
                        for (ProdProv item : (List<ProdProv>) results.values) {
                            entradasfiltro.add(item);
                        }
                        notifyDataSetChanged();
                    } else if (constraint == null) {
                        // no filter, addModelo entire original list back in
                        entradasfiltro.addAll(entradas);
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        @Override
        public ProdProv getItem(int posicion) {
            return entradasfiltro.get(posicion);
        }

        @Override
        public long getItemId(int posicion) {
            return posicion;
        }

        /** Devuelve cada una de las entradas con cada una de las vistas a la que debe de ser asociada
         * @param entrada La entrada que será la asociada a la view. La entrada es del tipo del paquete/handler
         * @param view View particular que contendrá los datos del paquete/handler
         */
        public abstract void onEntrada (ProdProv entrada, View view);
    }
}