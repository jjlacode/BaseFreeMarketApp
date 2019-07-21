package jjlacode.com.freelanceproject.ui;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import jjlacode.com.freelanceproject.util.android.controls.EditMaterial;
import jjlacode.com.freelanceproject.util.crud.FragmentCUD;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltro;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.model.ProdProv;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.CommonPry;

import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.*;

public class FragmentCUDDetpartidaBase extends FragmentCUD implements CommonPry.Constantes,
        ContratoPry.Tablas, CommonPry.TiposDetPartida{

    private EditMaterial descripcion;
    private EditMaterial precio;
    private EditMaterial descProv;
    private TextView refprov;
    private TextView tipoDetPartida;
    private AutoCompleteTextView nombre;
    private EditMaterial tiempo;
    private RecyclerView rvDetpartida;
    private String tipo;
    private Modelo partidabase;
    private ArrayList<Modelo> lista;

    private ArrayList<ProdProv> listaProdProv;
    private AdaptadorProdProv mAdapter;
    private AutoCompleteTextView autoCat;
    private AutoCompleteTextView autoProv;
    private String iddet;
    private double pvp;

    public FragmentCUDDetpartidaBase() {
        // Required empty public constructor
    }

    @Override
    protected void setNuevo() {

        refprov.setVisibility(View.GONE);
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
            nombre.setText(modelo.getString(DETPARTIDABASE_NOMBRE));
            descripcion.setText(modelo.getString(DETPARTIDABASE_DESCRIPCION));
            pvp = modelo.getDouble(DETPARTIDABASE_TIEMPO);
            precio.setText(JavaUtil.formatoMonedaLocal(pvp * CommonPry.hora));
            tiempo.setText(modelo.getString(DETPARTIDABASE_TIEMPO));
            descProv.setText(modelo.getString(DETPARTIDABASE_DESCUENTOPROVCAT));
            refprov.setText(modelo.getString(DETPARTIDABASE_REFPROVCAT));
            if (modelo.getString(DETPARTIDABASE_RUTAFOTO)!=null){
                path = modelo.getString(DETPARTIDABASE_RUTAFOTO);
                setImagenUri(contexto,path);
            }

            nombre.setThreshold(50);
            gone(refprov);
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
        setAdaptadorAuto(nombre);


        /*
        btnNuevaTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (descripcion.getText()!=null && tiempo.getText()!=null && nombre.getText()!=null){

                    int cont = 0;

                    if (lista!=null && lista.sizeLista()>0) {

                        for (Modelo tarea : lista) {
                            if (tarea.getString(TRABAJO_NOMBRE).equals(nombre.getText().toString())) {
                                Toast.makeText(getContext(), "La tarea ya existe", Toast.LENGTH_SHORT).show();
                                cont++;
                            }
                        }
                    }
                    if (cont == 0) {

                        try {
                            valores = new ContentValues();

                            consulta.putDato(valores,CAMPOS_TRABAJO,TRABAJO_DESCRIPCION,descripcion.getText().toString());
                            consulta.putDato(valores,CAMPOS_TRABAJO,TRABAJO_TIEMPO,tiempo.getText().toString());
                            consulta.putDato(valores,CAMPOS_TRABAJO,TRABAJO_NOMBRE,nombre.getText().toString());
                            consulta.putDato(valores,CAMPOS_TRABAJO,TRABAJO_RUTAFOTO,path);

                            consulta.insertRegistro(TABLA_TRABAJO,valores);

                            Toast.makeText(getContext(), "Nueva tarea guardada", Toast.LENGTH_SHORT).show();

                            rv();

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error al guardar tarea", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });

        btnNuevoProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (descripcion.getText()!=null && precio.getText()!=null && nombre.getText()!=null){

                    int cont = 0;

                    if (lista!=null && lista.sizeLista()>0) {

                        for (Modelo producto : lista) {
                            if (producto.getString(PRODUCTO_NOMBRE).equals(nombre.getText().toString())) {
                                Toast.makeText(getContext(), "El producto ya existe", Toast.LENGTH_SHORT).show();
                                cont++;
                            }
                        }
                    }
                    if (cont == 0) {

                        try {
                            valores = new ContentValues();

                            consulta.putDato(valores,CAMPOS_PRODUCTO,PRODUCTO_DESCRIPCION,descripcion.getText().toString());
                            consulta.putDato(valores,CAMPOS_PRODUCTO,PRODUCTO_PRECIO,precio.getText().toString());
                            consulta.putDato(valores,CAMPOS_PRODUCTO,PRODUCTO_NOMBRE,nombre.getText().toString());
                            consulta.putDato(valores,CAMPOS_PRODUCTO,PRODUCTO_RUTAFOTO,path);

                            consulta.insertRegistro(TABLA_PRODUCTO,valores);

                            Toast.makeText(getContext(), "Nuevo producto guardado", Toast.LENGTH_SHORT).show();

                            rv();

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error al guardar producto", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });
        */

        nombre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (tipo) {


                    case TIPOTRABAJO:

                        Modelo tarea = (Modelo) nombre.getAdapter().getItem(position);
                        iddet = tarea.getString(TRABAJO_ID_TRABAJO);
                        System.out.println("id = " + iddet);
                        nombre.setText(tarea.getString(TRABAJO_NOMBRE));
                        descripcion.setText(tarea.getString(TRABAJO_DESCRIPCION));
                        tiempo.setText(tarea.getString(TRABAJO_TIEMPO));
                        if (tarea.getString(TRABAJO_RUTAFOTO) != null) {
                            setImagenUri(contexto,tarea.getString(TRABAJO_RUTAFOTO));
                        }

                        break;

                    case TIPOPRODUCTO:

                        Modelo producto = (Modelo) nombre.getAdapter().getItem(position);
                        iddet = producto.getString(PRODUCTO_ID_PRODUCTO);
                        System.out.println("id = " + iddet);
                        nombre.setText(producto.getString(PRODUCTO_NOMBRE));
                        descripcion.setText(producto.getString(PRODUCTO_DESCRIPCION));
                        precio.setText(producto.getString(PRODUCTO_PRECIO));
                        if (producto.getString(PRODUCTO_RUTAFOTO) != null) {
                            setImagenUri(contexto,producto.getString(PRODUCTO_RUTAFOTO));
                        }

                        break;

                    case TIPOPRODUCTOPROV:

                        ProdProv prodprov = (ProdProv) nombre.getAdapter().getItem(position);
                        iddet = prodprov.getId();
                        System.out.println("id = " + iddet);
                        nombre.setText(prodprov.getNombre());
                        descripcion.setText(prodprov.getDescripcion());
                        precio.setText(String.valueOf(prodprov.getPrecio()));
                        refprov.setText(prodprov.getRefprov());
                        if (prodprov.getRutafoto() != null) {
                            setImagenFireStoreCircle(contexto,prodprov.getRutafoto(),imagen);

                        }

                        break;

                    case TIPOPARTIDA:

                        Modelo partida = (Modelo) nombre.getAdapter().getItem(position);
                        iddet = partida.getString(PARTIDABASE_ID_PARTIDABASE);
                        System.out.println("id = " + iddet);
                        nombre.setText(partida.getString(PARTIDABASE_NOMBRE));
                        descripcion.setText(partida.getString(PARTIDABASE_DESCRIPCION));
                        precio.setText(partida.getString(PARTIDABASE_PRECIO));
                        tiempo.setText(partida.getString(PARTIDABASE_TIEMPO));
                        if (partida.getString(PARTIDABASE_RUTAFOTO) != null) {
                            setImagenUri(contexto,partida.getString(PARTIDABASE_RUTAFOTO));
                        }


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
        nombre = (AutoCompleteTextView) ctrl(R.id.etnomcdetpartidabase);
        tiempo = (EditMaterial) ctrl(R.id.ettiempocdetpartidabase);
        imagen = (ImageView) ctrl(R.id.imgcdetpartidabase);
        refprov = (TextView) ctrl(R.id.tvrefprovcdetpartidabase);
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
                        nombre.setText(tarea.getString(TRABAJO_NOMBRE));
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
                        nombre.setText(producto.getString(PRODUCTO_NOMBRE));
                        descripcion.setText(producto.getString(PRODUCTO_DESCRIPCION));
                        precio.setText(producto.getString(PRODUCTO_PRECIO));

                        if (producto.getString(PRODUCTO_RUTAFOTO) != null) {
                            path = producto.getString(PRODUCTO_RUTAFOTO);
                            setImagenUri(contexto,path);

                        }

                    }
                });

                break;

            case TIPOPARTIDA:

                precio.setVisibility(View.VISIBLE);
                tiempo.setVisibility(View.VISIBLE);
                lista = queryList(CAMPOS_PARTIDABASE);
                AdaptadorPartidaBase adaptadorPartidaBase = new AdaptadorPartidaBase(lista);
                rvDetpartida.setAdapter(adaptadorPartidaBase);
                adaptadorPartidaBase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iddet = lista.get(rvDetpartida.getChildAdapterPosition(v))
                                .getString(PARTIDABASE_ID_PARTIDABASE);
                        System.out.println("id = " + iddet);
                        Modelo partida = lista.get(rvDetpartida.getChildAdapterPosition(v));
                        nombre.setText(partida.getString(PARTIDABASE_NOMBRE));
                        descripcion.setText(partida.getString(PARTIDABASE_DESCRIPCION));
                        precio.setText(partida.getString(PARTIDABASE_PRECIO));
                        tiempo.setText(partida.getString(PARTIDABASE_TIEMPO));

                        if (partida.getString(PARTIDABASE_RUTAFOTO) != null) {
                            path = partida.getString(PARTIDABASE_RUTAFOTO);
                            setImagenUri(contexto,path);

                        }

                    }
                });

                break;

            case TIPOPRODUCTOPROV:

                precio.setVisibility(View.VISIBLE);
                refprov.setVisibility(View.VISIBLE);
                descProv.setVisibility(View.VISIBLE);
                autoCat.setVisibility(View.VISIBLE);
                autoProv.setVisibility(View.VISIBLE);

                listaProdProv = new ArrayList<>();

                DatabaseReference dbProveedor =
                        FirebaseDatabase.getInstance().getReference()
                                .child("productos");

                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            ProdProv prodProv = ds.getValue(ProdProv.class);
                            prodProv.setId(ds.getRef().getKey());

                            listaProdProv.add(prodProv);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                dbProveedor.addValueEventListener(eventListener);


                FirebaseRecyclerOptions<ProdProv> options =
                        new FirebaseRecyclerOptions.Builder<ProdProv>()
                                .setQuery(dbProveedor, ProdProv.class)
                                .build();

                mAdapter = new AdaptadorProdProv(options);

                rvDetpartida.setAdapter(mAdapter);

                mAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AdaptadorProdProv.ProdProvHolder holder = (AdaptadorProdProv.ProdProvHolder) rvDetpartida.getChildViewHolder(v);

                        int i = rvDetpartida.getChildViewHolder(v).getAdapterPosition();
                        ProdProv prodProv = holder.getItem();

                        iddet = listaProdProv.get(i).getId();
                        System.out.println("id = " + iddet);
                        refprov.setText(prodProv.getRefprov());
                        nombre.setText(prodProv.getNombre());
                        descripcion.setText(prodProv.getDescripcion());
                        precio.setText(String.valueOf(prodProv.getPrecio()));

                        path = prodProv.getRutafoto();

                        setImagenFireStoreCircle(contexto,path,imagen);
                    }
                });


        }

    }

    //Start Listening Adapter
    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter!=null) {
            mAdapter.startListening();
        }
    }

    //Stop Listening Adapter
    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter!=null) {
            mAdapter.stopListening();
        }
    }

    private void nuevaTarea() {

        if (descripcion.getText() != null && tiempo.getText() != null && nombre.getText() != null) {

            int cont = 0;

            ContentValues valorestarea = new ContentValues();

            putDato(valorestarea, CAMPOS_TRABAJO, TRABAJO_DESCRIPCION, descripcion.getText().toString());
            putDato(valorestarea, CAMPOS_TRABAJO, TRABAJO_TIEMPO, tiempo.getText().toString());
            putDato(valorestarea, CAMPOS_TRABAJO, TRABAJO_NOMBRE, nombre.getText().toString());
            putDato(valorestarea, CAMPOS_TRABAJO, TRABAJO_RUTAFOTO, path);

            if (lista != null && lista.size() > 0) {

                for (Modelo tarea : lista) {
                    if (tarea.getString(TRABAJO_NOMBRE).equals(nombre.getText().toString())) {
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

        if (descripcion.getText() != null && precio.getText() != null && nombre.getText() != null) {

            int cont = 0;

            ContentValues valoresprod = new ContentValues();

            putDato(valoresprod, CAMPOS_PRODUCTO, PRODUCTO_DESCRIPCION, descripcion.getText().toString());
            putDato(valoresprod, CAMPOS_PRODUCTO, PRODUCTO_PRECIO, precio.getText().toString());
            putDato(valoresprod, CAMPOS_PRODUCTO, PRODUCTO_NOMBRE, nombre.getText().toString());
            putDato(valoresprod, CAMPOS_PRODUCTO, PRODUCTO_RUTAFOTO, path);

            if (lista != null && lista.size() > 0) {

                for (Modelo producto : lista) {
                    if (producto.getString(PRODUCTO_NOMBRE).equals(nombre.getText().toString())) {
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

            CommonPry.Calculos.sincronizarPartidaBase(id);
            partidabase = queryObject(CAMPOS_PARTIDABASE,id);

            super.registrar();

            return true;

    }

    @Override
    protected void setContenedor() {

        setDato(DETPARTIDABASE_NOMBRE, nombre.getText().toString());
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

            case TIPOPARTIDA:

                setDato(DETPARTIDABASE_TIEMPO, tiempo.getText().toString());
                setDato(DETPARTIDABASE_PRECIO, precio.getText().toString());

                break;

            case TIPOPRODUCTOPROV:

                setDato(DETPARTIDABASE_PRECIO, precio.getText().toString());
                setDato(DETPARTIDABASE_DESCUENTOPROVCAT, descProv.getText().toString());
                setDato(DETPARTIDABASE_REFPROVCAT, refprov.getText().toString());


        }
    }

    @Override
    protected boolean update() {

        partidabase = queryObject(CAMPOS_PARTIDABASE,id);

        String idclon = partidabase.getString(PARTIDABASE_ID_PARTIDAORIGEN);
        if (idclon==null) {
            super.update();
        }

        CommonPry.Calculos.actualizarPartidaBase(id);
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

    private void setAdaptadorAuto(AutoCompleteTextView autoCompleteTextView) {

        switch (tipo) {

            case TIPOTRABAJO:
                autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(getContext(),
                        R.layout.item_list_trabajo, lista, CAMPOS_TRABAJO) {

                    @Override
                    public void onEntrada(Modelo entrada, View view) {

                        ImageView imagenTarea = view.findViewById(R.id.imgltarea);
                        TextView nombreTarea = view.findViewById(R.id.tvnomltarea);
                        TextView descTarea = view.findViewById(R.id.tvdescripcionltareas);
                        TextView tiempoTarea = view.findViewById(R.id.tvtiempoltareas);


                        nombreTarea.setText(entrada.getString(TRABAJO_NOMBRE));
                        descTarea.setText(entrada.getString(TRABAJO_DESCRIPCION));
                        tiempoTarea.setText(entrada.getString(TRABAJO_TIEMPO));

                        if (entrada.getString(TRABAJO_RUTAFOTO) != null) {
                            setImagenUri(contexto,entrada.getString(TRABAJO_RUTAFOTO),imagenTarea);
                        }

                    }

                });
                break;

            case TIPOPRODUCTO:

                autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(getContext(),
                        R.layout.item_list_producto, lista, CAMPOS_PRODUCTO) {

                    @Override
                    public void onEntrada(Modelo entrada, View view) {

                        ImageView imagen = view.findViewById(R.id.imglproductos);
                        TextView nombre = view.findViewById(R.id.tvnombrelproductos);
                        TextView descripcion = view.findViewById(R.id.tvdescripcionlproductos);
                        TextView importe = view.findViewById(R.id.tvimportelproductos);


                        nombre.setText(entrada.getString(PRODUCTO_NOMBRE));
                        descripcion.setText(entrada.getString(PRODUCTO_DESCRIPCION));
                        importe.setText(entrada.getString(PRODUCTO_PRECIO));

                        if (entrada.getString(PRODUCTO_RUTAFOTO) != null) {
                            setImagenUri(contexto,entrada.getString(PRODUCTO_RUTAFOTO));
                        }

                    }
                });

                break;

            case TIPOPARTIDA:

                autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(getContext(),
                        R.layout.item_list_partidabase, lista, CAMPOS_PARTIDABASE) {

                    @Override
                    public void onEntrada(Modelo entrada, View view) {

                        ImageView imagen = view.findViewById(R.id.imglpartidabase);
                        TextView descripcion = view.findViewById(R.id.tvdescripcionpartidabase);
                        TextView importe = view.findViewById(R.id.tvimppartidabase);


                        nombre.setText(entrada.getString(PARTIDABASE_NOMBRE));
                        descripcion.setText(entrada.getString(PARTIDABASE_DESCRIPCION));
                        importe.setText(entrada.getString(PARTIDABASE_PRECIO));

                        if (entrada.getString(PARTIDABASE_RUTAFOTO) != null) {
                            setImagenUri(contexto,entrada.getString(PARTIDABASE_RUTAFOTO));
                        }

                    }
                });

                break;


            case TIPOPRODUCTOPROV:

                autoCompleteTextView.setAdapter(new ListaAdaptadorFiltroProdProv(getContext(),
                        R.layout.item_list_prodprov, listaProdProv) {
                    @Override
                    public void onEntrada(ProdProv entrada, View view) {

                        ImageView imagen = view.findViewById(R.id.imagenprov);
                        TextView nombre = view.findViewById(R.id.tvnomprov);
                        TextView descripcion = view.findViewById(R.id.tvdescprov);
                        TextView importe = view.findViewById(R.id.tvprecioprov);
                        TextView refProv = view.findViewById(R.id.tvrefprov);

                        nombre.setText(entrada.getNombre());
                        descripcion.setText(entrada.getDescripcion());
                        importe.setText(String.valueOf(entrada.getPrecio()));
                        refProv.setText(entrada.getRefprov());
                        String rutafoto = entrada.getRutafoto();
                        id = entrada.getId();

                        if (entrada.getRutafoto() != null) {
                            setImagenFireStoreCircle(contexto,rutafoto,imagen);
                        }

                    }
                });


                break;


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

    public static class AdaptadorPartidaBase extends RecyclerView.Adapter<AdaptadorPartidaBase.PartidaViewHolder>
            implements View.OnClickListener, CommonPry.Constantes, ContratoPry.Tablas {

        ArrayList<Modelo> listaPartidas;
        private View.OnClickListener listener;

        public AdaptadorPartidaBase(ArrayList<Modelo> listaPartidas){

            this.listaPartidas=listaPartidas;
        }

        @NonNull
        @Override
        public PartidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_partidabase,null,false);

            view.setOnClickListener(this);


            return new PartidaViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull PartidaViewHolder partidaViewHolder, int position) {

            partidaViewHolder.descripcionPartida.setText(listaPartidas.get(position).getCampos(PARTIDABASE_DESCRIPCION));
            partidaViewHolder.tiempoPartida.setText(listaPartidas.get(position).getCampos(PARTIDABASE_TIEMPO));
            partidaViewHolder.importePartida.setText(JavaUtil.formatoMonedaLocal(listaPartidas.get(position).getDouble(PARTIDABASE_PRECIO)));

            if (listaPartidas.get(position).getString(PARTIDABASE_RUTAFOTO)!=null){

                partidaViewHolder.imagenPartida.setImageURI(listaPartidas.get(position).getUri(PARTIDABASE_RUTAFOTO));
            }


            System.out.println("listaPartidas: "+ listaPartidas.size() + " registros");

        }

        @Override
        public int getItemCount() {
            return listaPartidas.size();
        }

        @Override
        public void onClick(View v) {

            if (listener!= null){

                listener.onClick(v);


            }


        }

        public class PartidaViewHolder extends RecyclerView.ViewHolder{

            ImageView imagenPartida;
            TextView descripcionPartida,tiempoPartida,importePartida;

            public PartidaViewHolder(@NonNull View itemView) {
                super(itemView);

                imagenPartida = itemView.findViewById(R.id.imglpartidabase);
                descripcionPartida = itemView.findViewById(R.id.tvdescripcionpartidabase);
                tiempoPartida = itemView.findViewById(R.id.tvtiempopartidabase);
                importePartida = itemView.findViewById(R.id.tvimppartidabase);

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