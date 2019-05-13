package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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

import jjlacode.com.freelanceproject.model.Categorias;
import jjlacode.com.freelanceproject.model.Proveedores;
import jjlacode.com.freelanceproject.util.FragmentCUD;
import jjlacode.com.freelanceproject.util.ImagenUtil;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.ListaAdaptadorFiltro;
import jjlacode.com.freelanceproject.util.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.model.ProdProv;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.CommonPry;

public class FragmentCUDDetpartida extends FragmentCUD implements CommonPry.Constantes,
        ContratoPry.Tablas, CommonPry.TiposDetPartida {

    private EditText descripcion;
    private EditText precio;
    private EditText cantidad;
    private EditText descProv;
    private TextView refprov;
    private TextView tipoDetPartida;
    private AutoCompleteTextView nombre;
    private EditText tiempo;
    private Button btnNuevaTarea;
    private Button btnNuevoProd;
    private RecyclerView rvDetpartida;
    private String tipo;
    private Modelo proyecto;
    private Modelo partida;
    private ArrayList<Modelo> lista;
    private String idDetPartida;

    private String idProyecto_Partida;
    private int secuenciaPartida;
    private AdaptadorProdProv mAdapter;
    private ArrayList<ProdProv> listaProdProv;
    private ArrayList<Proveedores> listaProv;
    private ArrayList<Categorias> listaCat;
    private AutoCompleteTextView autoCat;
    private AutoCompleteTextView autoProv;
    private String proveedor = TODOS;
    private String categoria = TODAS;

    public FragmentCUDDetpartida() {
        // Required empty public constructor
    }

    @Override
    protected void setNuevo() {

        btndelete.setVisibility(View.GONE);
        tipoDetPartida.setText(tipo.toUpperCase());
        refprov.setVisibility(View.GONE);
        precio.setVisibility(View.GONE);
        tiempo.setVisibility(View.GONE);
        descProv.setVisibility(View.GONE);
        btnNuevaTarea.setVisibility(View.GONE);
        btnNuevoProd.setVisibility(View.GONE);
        autoCat.setVisibility(View.GONE);
        autoProv.setVisibility(View.GONE);

    }

    @Override
    protected void setTabla() {

        tabla = TABLA_DETPARTIDA;

    }

    @Override
    protected void setTablaCab() {

        tablaCab = TABLA_PARTIDA;

    }

    @Override
    protected void setContext() {

        contexto = getContext();

    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_DETPARTIDA;

    }

    @Override
    protected void setCampoID() {

        campoID = DETPARTIDA_ID_PARTIDA;

    }

    @Override
    protected void setBundle() {

        proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
        partida = (Modelo) bundle.getSerializable(TABLA_PARTIDA);
        if (bundle.containsKey(MODELO)) {
            modelo = (Modelo) bundle.getSerializable(MODELO);
            secuencia = bundle.getInt(SECUENCIA);
            id = bundle.getString(ID);
        }
            secuenciaPartida = partida.getInt(PARTIDA_SECUENCIA);
            idProyecto_Partida = partida.getString(PARTIDA_ID_PROYECTO);
            tipo = bundle.getString(TIPO);

    }

    @Override
    protected void setDatos() {

        btndelete.setVisibility(View.VISIBLE);
        tipoDetPartida.setText(tipo.toUpperCase());
        refprov.setVisibility(View.GONE);
        precio.setVisibility(View.GONE);
        tiempo.setVisibility(View.GONE);
        descProv.setVisibility(View.GONE);
        btnNuevaTarea.setVisibility(View.GONE);
        btnNuevoProd.setVisibility(View.GONE);
        autoCat.setVisibility(View.GONE);
        autoProv.setVisibility(View.GONE);
        nombre.setText(modelo.getString(DETPARTIDA_NOMBRE));
        descripcion.setText(modelo.getString(DETPARTIDA_DESCRIPCION));
        precio.setText(modelo.getString(DETPARTIDA_PRECIO));
        cantidad.setText(modelo.getString(DETPARTIDA_CANTIDAD));
        tiempo.setText(modelo.getString(DETPARTIDA_TIEMPO));
        descProv.setText(modelo.getString(DETPARTIDA_DESCUENTOPROV));
        refprov.setText(modelo.getString(DETPARTIDA_REFPROV));
        if (tipo.equals(TIPOPRODUCTOPROV) && modelo.getString(DETPARTIDA_RUTAFOTO)!=null){
            imagenUtil = new ImagenUtil(contexto);
            path = modelo.getString(DETPARTIDA_RUTAFOTO);
            setImagenFireStoreCircle(contexto,path,imagen);
        }
        else if (modelo.getString(DETPARTIDA_RUTAFOTO)!=null){
            imagenUtil = new ImagenUtil(contexto);
            path = modelo.getString(DETPARTIDA_RUTAFOTO);
            imagenUtil.setImageUriCircle(modelo.getString(DETPARTIDA_RUTAFOTO),imagen);
        }

    }

    @Override
    protected void setAcciones() {

        rv();

        setAdaptadorAuto(nombre);

        setAdaptadorAutoCat();

        setAdaptadorAutoProv();


        btnNuevaTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (descripcion.getText()!=null && tiempo.getText()!=null && nombre.getText()!=null){

                    int cont = 0;

                    if (lista!=null && lista.size()>0) {

                        for (Modelo tarea : lista) {
                            if (tarea.getString(TAREA_NOMBRE).equals(nombre.getText().toString())) {
                                Toast.makeText(getContext(), "La tarea ya existe", Toast.LENGTH_SHORT).show();
                                cont++;
                            }
                        }
                    }
                    if (cont == 0) {

                        try {
                            valores = new ContentValues();

                            consulta.putDato(valores,CAMPOS_TAREA,TAREA_DESCRIPCION,descripcion.getText().toString());
                            consulta.putDato(valores,CAMPOS_TAREA,TAREA_TIEMPO,tiempo.getText().toString());
                            consulta.putDato(valores,CAMPOS_TAREA,TAREA_NOMBRE,nombre.getText().toString());
                            consulta.putDato(valores,CAMPOS_TAREA,TAREA_RUTAFOTO,path);

                            consulta.insertRegistro(TABLA_TAREA,valores);

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

                    if (lista!=null && lista.size()>0) {

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
                            consulta.putDato(valores,CAMPOS_PRODUCTO,PRODUCTO_IMPORTE,precio.getText().toString());
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

        nombre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (tipo) {

                    case TIPOTAREA:

                        Modelo tarea = (Modelo) nombre.getAdapter().getItem(position);
                        idDetPartida = tarea.getString(TAREA_ID_TAREA);
                        nombre.setText(tarea.getString(TAREA_NOMBRE));
                        descripcion.setText(tarea.getString(TAREA_DESCRIPCION));
                        tiempo.setText(tarea.getString(TAREA_TIEMPO));

                        if (tarea.getString(TAREA_RUTAFOTO) != null) {
                            imagen.setImageURI(tarea.getUri(TAREA_RUTAFOTO));
                            path = tarea.getString(TAREA_RUTAFOTO);
                        }

                        break;

                    case TIPOPRODUCTO:

                        Modelo producto = (Modelo) nombre.getAdapter().getItem(position);
                        idDetPartida = producto.getString(PRODUCTO_ID_PRODUCTO);
                        nombre.setText(producto.getString(PRODUCTO_NOMBRE));
                        descripcion.setText(producto.getString(PRODUCTO_DESCRIPCION));
                        precio.setText(producto.getString(PRODUCTO_IMPORTE));
                        if (producto.getString(PRODUCTO_RUTAFOTO) != null) {
                            imagen.setImageURI(producto.getUri(PRODUCTO_RUTAFOTO));
                            path = producto.getString(PRODUCTO_RUTAFOTO);
                        }
                        break;

                    case TIPOPARTIDA:

                        Modelo partida = (Modelo) nombre.getAdapter().getItem(position);
                        idDetPartida = partida.getString(PARTIDABASE_ID_PARTIDABASE);
                        nombre.setText(partida.getString(PARTIDABASE_NOMBRE));
                        descripcion.setText(partida.getString(PARTIDABASE_DESCRIPCION));
                        precio.setText(partida.getString(PARTIDABASE_PRECIO));
                        tiempo.setText(partida.getString(PARTIDABASE_TIEMPO));
                        if (partida.getString(PARTIDABASE_RUTAFOTO) != null) {
                            imagen.setImageURI(partida.getUri(PARTIDABASE_RUTAFOTO));
                            path = partida.getString(PARTIDABASE_RUTAFOTO);
                        }
                        break;

                    case TIPOPRODUCTOPROV:

                        ProdProv prodprov = (ProdProv) nombre.getAdapter().getItem(position);
                        idDetPartida = prodprov.getId();
                        nombre.setText(prodprov.getNombre());
                        descripcion.setText(prodprov.getDescripcion());
                        precio.setText(String.valueOf(prodprov.getPrecio()));
                        refprov.setText(prodprov.getRefprov());
                        if (prodprov.getRutafoto() != null) {

                            setImagenFireStoreCircle(contexto,prodprov.getRutafoto(),imagen);
                            path = prodprov.getRutafoto();
                        }



                }
            }

        });

    }

    @Override
    protected void setLayout() {

        layout = R.layout.fragment_cud_detpartida;

    }

    @Override
    protected void setInicio() {

        btnNuevaTarea = view.findViewById(R.id.btnntareacdetpartida);
        btnNuevoProd = view.findViewById(R.id.btnnprodcdetpartida);
        rvDetpartida = view.findViewById(R.id.rvcdetpartida);
        descripcion = view.findViewById(R.id.etdesccdetpartida);
        precio = view.findViewById(R.id.etpreciocdetpartida);
        cantidad = view.findViewById(R.id.etcantcdetpartida);
        nombre = view.findViewById(R.id.etnomcdetpartida);
        tiempo = view.findViewById(R.id.ettiempocdetpartida);
        imagen = view.findViewById(R.id.imgcdetpartida);
        refprov = view.findViewById(R.id.tvrefprovcdetpartida);
        descProv = view.findViewById(R.id.etdescprovcdetpartida);
        tipoDetPartida = view.findViewById(R.id.tvtipocdetpartida);
        btnsave = view.findViewById(R.id.detpartida_c_btn_save);
        btndelete = view.findViewById(R.id.detpartida_c_btn_delete);
        btnback = view.findViewById(R.id.detpartida_c_btn_back);
        autoCat = view.findViewById(R.id.autocat);
        autoProv = view.findViewById(R.id.autoprov);


    }

    private void rv(){


        imagenUtil = new ImagenUtil(contexto);

        switch (tipo) {

            case TIPOTAREA:

                rvDetpartida.setLayoutManager(new LinearLayoutManager(getContext()));
                rvDetpartida.setHasFixedSize(true);

                tiempo.setVisibility(View.VISIBLE);
                btnNuevaTarea.setVisibility(View.VISIBLE);
                lista = consulta.queryList(CAMPOS_TAREA);
                AdaptadorTareas adaptadorTareas = new AdaptadorTareas(lista);
                rvDetpartida.setAdapter(adaptadorTareas);
                adaptadorTareas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        idDetPartida = lista.get(rvDetpartida.getChildAdapterPosition(v))
                                .getString(TAREA_ID_TAREA);
                        Modelo tarea = consulta.queryObject(CAMPOS_TAREA, idDetPartida);
                        nombre.setText(tarea.getString(TAREA_NOMBRE));
                        descripcion.setText(tarea.getString(TAREA_DESCRIPCION));
                        tiempo.setText(tarea.getString(TAREA_TIEMPO));
                        if (tarea.getString(TAREA_RUTAFOTO) != null) {
                            path = tarea.getString(TAREA_RUTAFOTO);
                            imagenUtil.setImageUriCircle(path,imagen);
                        }
                    }
                });
                break;

            case TIPOPRODUCTO:

                rvDetpartida.setLayoutManager(new LinearLayoutManager(getContext()));
                rvDetpartida.setHasFixedSize(true);

                precio.setVisibility(View.VISIBLE);
                btnNuevoProd.setVisibility(View.VISIBLE);
                lista = consulta.queryList(CAMPOS_PRODUCTO);
                AdaptadorProducto adaptadorProducto = new AdaptadorProducto(lista);
                rvDetpartida.setAdapter(adaptadorProducto);
                adaptadorProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        idDetPartida = lista.get(rvDetpartida.getChildAdapterPosition(v))
                                .getString(PRODUCTO_ID_PRODUCTO);
                        Modelo producto = consulta.queryObject(CAMPOS_PRODUCTO, idDetPartida);
                        nombre.setText(producto.getString(PRODUCTO_NOMBRE));
                        descripcion.setText(producto.getString(PRODUCTO_DESCRIPCION));
                        precio.setText(producto.getString(PRODUCTO_IMPORTE));

                        if (producto.getString(PRODUCTO_RUTAFOTO) != null) {
                            path = producto.getString(PRODUCTO_RUTAFOTO);
                            imagenUtil.setImageUriCircle(path,imagen);
                        }

                    }
                });

                break;

            case TIPOPARTIDA:

                rvDetpartida.setLayoutManager(new LinearLayoutManager(getContext()));
                rvDetpartida.setHasFixedSize(true);

                precio.setVisibility(View.VISIBLE);
                tiempo.setVisibility(View.VISIBLE);
                lista = consulta.queryList(CAMPOS_PARTIDABASE);
                AdaptadorPartida adaptadorPartida = new AdaptadorPartida(lista);
                rvDetpartida.setAdapter(adaptadorPartida);
                adaptadorPartida.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        idDetPartida = lista.get(rvDetpartida.getChildAdapterPosition(v))
                                .getString(PARTIDABASE_ID_PARTIDABASE);
                        Modelo partidabase = lista.get(rvDetpartida.getChildAdapterPosition(v));
                        nombre.setText(partidabase.getString(PARTIDABASE_NOMBRE));
                        descripcion.setText(partidabase.getString(PARTIDABASE_DESCRIPCION));
                        precio.setText(partidabase.getString(PARTIDABASE_PRECIO));

                        if (partidabase.getString(PARTIDABASE_RUTAFOTO) != null) {
                            path = partidabase.getString(PARTIDABASE_RUTAFOTO);
                            imagenUtil.setImageUriCircle(path,imagen);
                        }

                    }
                });

                break;

            case TIPOPRODUCTOPROV:

                rvDetpartida.setLayoutManager(new LinearLayoutManager(getContext()));
                rvDetpartida.setHasFixedSize(true);

                precio.setVisibility(View.VISIBLE);
                refprov.setVisibility(View.VISIBLE);
                descProv.setVisibility(View.VISIBLE);
                autoCat.setVisibility(View.VISIBLE);
                autoProv.setVisibility(View.VISIBLE);




                listaProdProv = new ArrayList<>();

                DatabaseReference dbProductos =
                        FirebaseDatabase.getInstance().getReference()
                                .child("productos");

                ValueEventListener eventListenerProd = new ValueEventListener() {
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

                dbProductos.addValueEventListener(eventListenerProd);

                ArrayList<ProdProv> prodProvtemp =new ArrayList<>();

                for (ProdProv prodProv : listaProdProv) {

                    if ((prodProv.getCategoria().equals(categoria) || categoria.equals(TODAS)) &&
                            (prodProv.getProveedor().equals(proveedor)) || proveedor.equals(TODOS)){
                        prodProvtemp.add(prodProv);
                    }

                }
                listaProdProv.clear();
                listaProdProv.addAll(prodProvtemp);
                prodProvtemp.clear();

                /*
                FirebaseRecyclerOptions<ProdProv> options =
                        new FirebaseRecyclerOptions.Builder<ProdProv>()
                                .setQuery(dbProductos, ProdProv.class)
                                .build();

                 */

                //mAdapter = new AdaptadorProdProv(options);

                AdaptadorProveedor provAdapter = new AdaptadorProveedor(listaProdProv);

                //rvDetpartida.setAdapter(mAdapter);
                rvDetpartida.setAdapter(provAdapter);

                System.out.println("lista prov: "+ listaProdProv.size());

                /*
                mAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AdaptadorProdProv.ProdProvHolder holder = (AdaptadorProdProv.ProdProvHolder) rvDetpartida.getChildViewHolder(v);

                        int i = rvDetpartida.getChildViewHolder(v).getAdapterPosition();
                        ProdProv prodProv = holder.getItem();

                        idDetPartida = listaProdProv.get(i).getId();
                        System.out.println("idDetPartida = " + idDetPartida);

                        refprov.setText(prodProv.getRefprov());
                        nombre.setText(prodProv.getNombre());
                        descripcion.setText(prodProv.getDescripcion());
                        precio.setText(String.valueOf(prodProv.getPrecio()));

                        path = prodProv.getRutafoto();
                        if (path!=null) {
                            setImagenFireStoreCircle(path, imagen);
                        }


                    }
                });

                 */

                provAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int i = rvDetpartida.getChildViewHolder(v).getAdapterPosition();
                        idDetPartida = listaProdProv.get(i).getId();
                        System.out.println("idDetPartida = " + idDetPartida);

                        refprov.setText(listaProdProv.get(i).getRefprov());
                        nombre.setText(listaProdProv.get(i).getNombre());
                        descripcion.setText(listaProdProv.get(i).getDescripcion());
                        precio.setText(String.valueOf(listaProdProv.get(i).getPrecio()));

                        path = listaProdProv.get(i).getRutafoto();
                        if (path!=null){
                            setImagenFireStoreCircle(contexto,path,imagen);
                        }


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

    @Override
    protected void setContenedor() {

        consulta.putDato(valores,CAMPOS_DETPARTIDA,DETPARTIDA_NOMBRE,nombre.getText().toString());
        consulta.putDato(valores,CAMPOS_DETPARTIDA,DETPARTIDA_DESCRIPCION,descripcion.getText().toString());
        consulta.putDato(valores,CAMPOS_DETPARTIDA,DETPARTIDA_CANTIDAD,cantidad.getText().toString());
        consulta.putDato(valores,CAMPOS_DETPARTIDA,DETPARTIDA_RUTAFOTO,path);
        consulta.putDato(valores,CAMPOS_DETPARTIDA,DETPARTIDA_ID_DETPARTIDA,idDetPartida);
        consulta.putDato(valores,CAMPOS_DETPARTIDA,DETPARTIDA_ID_PARTIDA, id);
        consulta.putDato(valores,CAMPOS_DETPARTIDA,DETPARTIDA_TIPO,tipo);

        switch (tipo){

            case TIPOTAREA:

                consulta.putDato(valores,CAMPOS_DETPARTIDA,DETPARTIDA_TIEMPO,tiempo.getText().toString());

                break;

            case TIPOPRODUCTO:

                consulta.putDato(valores,CAMPOS_DETPARTIDA,DETPARTIDA_PRECIO,precio.getText().toString());

                break;

            case TIPOPARTIDA:

                consulta.putDato(valores,CAMPOS_DETPARTIDABASE,DETPARTIDABASE_TIEMPO,tiempo.getText().toString());
                consulta.putDato(valores,CAMPOS_DETPARTIDABASE,DETPARTIDABASE_PRECIO,precio.getText().toString());

                break;


            case TIPOPRODUCTOPROV:

                consulta.putDato(valores,CAMPOS_DETPARTIDA,DETPARTIDA_PRECIO,precio.getText().toString());
                consulta.putDato(valores,CAMPOS_DETPARTIDA,DETPARTIDA_DESCUENTOPROV,descProv.getText().toString());
                consulta.putDato(valores,CAMPOS_DETPARTIDA,DETPARTIDA_REFPROV,refprov.getText().toString());
                break;

        }


    }

    protected void cambiarFragment(){

        bundle = new Bundle();
        new CommonPry.Calculos.TareaActualizaProy().execute(idProyecto_Partida);
        partida = consulta.queryObjectDetalle(CAMPOS_PARTIDA,idProyecto_Partida,secuenciaPartida);
        CommonPry.Calculos.actualizarPartidaProyecto(id);
        bundle.putSerializable(MODELO,partida);
        bundle.putSerializable(TABLA_PROYECTO,proyecto);
        bundle.putString(TIPO, tipo);
        bundle.putString(NAMEF, namef);
        bundle.putString(NAMESUB, namesubclass);
        bundle.putString(ID,idProyecto_Partida);
        bundle.putInt(SECUENCIA,secuenciaPartida);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaProyecto());
        bundle = null;
    }

    @Override
    protected void setcambioFragment() {

    }

    private void setAdaptadorAuto(AutoCompleteTextView autoCompleteTextView) {


        switch (tipo) {

            case TIPOTAREA:

                autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(getContext(),
                        R.layout.item_list_tarea, lista, CAMPOS_TAREA) {

                    @Override
                    public void onEntrada(Modelo entrada, View view) {

                        ImageView imagenTarea = view.findViewById(R.id.imgltarea);
                        TextView nombreTarea = view.findViewById(R.id.tvnomltarea);
                        TextView descTarea = view.findViewById(R.id.tvdescripcionltareas);
                        TextView tiempoTarea = view.findViewById(R.id.tvtiempoltareas);


                        nombreTarea.setText(entrada.getString(TAREA_NOMBRE));
                        descTarea.setText(entrada.getString(TAREA_DESCRIPCION));
                        tiempoTarea.setText(entrada.getString(TAREA_TIEMPO));

                        if (entrada.getString(TAREA_RUTAFOTO) != null) {
                            imagenTarea.setImageURI(entrada.getUri(TAREA_RUTAFOTO));
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
                        importe.setText(entrada.getString(PRODUCTO_IMPORTE));

                        if (entrada.getString(PRODUCTO_RUTAFOTO) != null) {
                            imagen.setImageURI(entrada.getUri(PRODUCTO_RUTAFOTO));
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
                            imagen.setImageURI(entrada.getUri(PARTIDABASE_RUTAFOTO));
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

                        if (entrada.getRutafoto() != null) {

                            setImagenFireStoreCircle(contexto,rutafoto,imagen);
                            //FirebaseStorage storage = FirebaseStorage.getInstance();
                            //StorageReference storageRef = storage.getReference();
                            //StorageReference spaceRef = storageRef.child(rutafoto);
                            //GlideApp.with(getContext())
                            //        .load(spaceRef)
                            //        .into(imagen);
                        }

                    }
                });


                break;


        }

    }

    private void setAdaptadorAutoCat(){

        listaCat = new ArrayList<>();

        DatabaseReference dbCategorias =
                FirebaseDatabase.getInstance().getReference()
                        .child("categorias");

        ValueEventListener eventListenerCat = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Categorias categoria = ds.getValue(Categorias.class);
                    categoria.setId(ds.getRef().getKey());
                    listaCat.add(categoria);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        dbCategorias.addValueEventListener(eventListenerCat);


        ListaAdaptadorFiltroCat adapterCat = new ListaAdaptadorFiltroCat
                (contexto,R.layout.item_list_categoria,listaCat);

        autoCat.setAdapter(adapterCat);

        autoCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                categoria = listaCat.get(position).getNombre();
                autoCat.setText(categoria);
                rv();

            }
        });

    }

    private void setAdaptadorAutoProv(){

        listaProv = new ArrayList<>();

        DatabaseReference dbProveedor =
                FirebaseDatabase.getInstance().getReference()
                        .child("proveedores");

        ValueEventListener eventListenerProv = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Proveedores proveedor = ds.getValue(Proveedores.class);
                    proveedor.setId(ds.getRef().getKey());
                    listaProv.add(proveedor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        dbProveedor.addValueEventListener(eventListenerProv);


        ListaAdaptadorFiltroProv adapterProv = new ListaAdaptadorFiltroProv
                (contexto,R.layout.item_list_proveedor,listaProv);

        autoProv.setAdapter(adapterProv);

        autoProv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


               proveedor = listaProv.get(position).getNombre();
               autoProv.setText(proveedor);
               rv();
            }
        });
    }

    public static class AdaptadorPartida extends RecyclerView.Adapter<AdaptadorPartida.PartidaViewHolder>
            implements View.OnClickListener, CommonPry.Constantes, ContratoPry.Tablas {

        ArrayList<Modelo> listaPartidas;
        private View.OnClickListener listener;

        public AdaptadorPartida(ArrayList<Modelo> listaPartidas){

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


        }

        @Override
        public void onError(@NonNull DatabaseError error) {
            super.onError(error);
            Log.e(TAG, error.toString());
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

                return prodProv;

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
                this.precio.setText(JavaUtil.formatoMonedaLocal(precio));
            }

            public void setRutafoto(String rutafoto) {
                //FirebaseStorage storage = FirebaseStorage.getInstance();
                //StorageReference storageRef = storage.getReference();
                //StorageReference spaceRef = storageRef.child(rutafoto);
                imagenUtil = new ImagenUtil(getContext());
                imagen = mView.findViewById(R.id.imagenprov);
                setImagenFireStoreCircle(contexto,rutafoto,imagen);
                path = rutafoto;
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

        public void setOnClickListener(View.OnClickListener listener){

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
            if (lista.get(position).getRutafoto()!=null){

                ImagenUtil imagenUtil = new ImagenUtil(contexto);
                imagenUtil.setImageFireStoreCircle(lista.get(position).getRutafoto(),imagen);
            }
        }

        @Override
        public int getItemCount() {
            if (lista!=null) {
                return lista.size();
            }
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView refprov,nombre,descripcion,precio;
            private ImageView imagen;
            private String path;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                refprov = itemView.findViewById(R.id.tvrefprov);
                nombre = itemView.findViewById(R.id.tvnomprov);
                descripcion = itemView.findViewById(R.id.tvdescprov);
                precio = itemView.findViewById(R.id.tvprecioprov);
                imagen = itemView.findViewById(R.id.imagenprov);

            }
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

            productoViewHolder.importe.setText(listaProductos.get(position).getString(PRODUCTO_IMPORTE));
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

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_tarea_detpartida,null,false);

            view.setOnClickListener(this);

            return new TareaViewHolder(view) ;
        }

        public void setOnClickListener(View.OnClickListener listener){

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull TareaViewHolder tareaViewHolder, int position) {

            tareaViewHolder.tiempo.setText(listaTareas.get(position).getCampos(TAREA_TIEMPO));
            tareaViewHolder.nombre.setText(listaTareas.get(position).getString(TAREA_NOMBRE));
            if (listaTareas.get(position).getString(TAREA_RUTAFOTO)!=null){

                tareaViewHolder.imagen.setImageURI(listaTareas.get(position).getUri(TAREA_RUTAFOTO));
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
                                }else if(item.getDescripcion().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                    suggestion.add(item);
                                }else if(item.getRefprov().toLowerCase().contains(constraint.toString().toLowerCase())) {

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
                        // no filter, add entire original list back in
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

    public class ListaAdaptadorFiltroProv extends ArrayAdapter<Proveedores> {

        private ArrayList<Proveedores> entradas;
        private ArrayList<Proveedores> entradasfiltro;
        private int R_layout_IdView;
        private Context contexto;

        ListaAdaptadorFiltroProv(Context contexto, int R_layout_IdView, ArrayList<Proveedores> entradas) {
            super(contexto,R_layout_IdView,entradas);
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

            TextView nombre,descripcion,direccion;
            ImageView imagenProv;

            nombre = view.findViewById(R.id.tvnomproveedor);
            descripcion = view.findViewById(R.id.tvdescproveedor);
            direccion = view.findViewById(R.id.tvdirproveedor);
            imagenProv = view.findViewById(R.id.imagenproveedor);

            nombre.setText(entradasfiltro.get(position).getNombre());
            descripcion.setText(entradasfiltro.get(position).getDescripcion());
            direccion.setText(entradasfiltro.get(position).getDireccion());
            String pathprov = entradasfiltro.get(position).getRutafoto();
            if (pathprov!=null){
                setImagenFireStoreCircle(contexto,pathprov,imagenProv);
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

                        for (Proveedores item :entradas) {

                            if(item.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            }else if(item.getDescripcion().toLowerCase().contains(constraint.toString().toLowerCase())) {

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
                        // no filter, add entire original list back in
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
            super(contexto,R_layout_IdView,entradas);
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

            TextView nombre,descripcion;
            ImageView imagencat;

            nombre = view.findViewById(R.id.tvnomcat);
            descripcion = view.findViewById(R.id.tvdesccat);
            imagencat = view.findViewById(R.id.imagencat);

            nombre.setText(entradasfiltro.get(position).getNombre());
            descripcion.setText(entradasfiltro.get(position).getDescripcion());

            String pathcat = entradasfiltro.get(position).getRutafoto();
            if (pathcat!=null){
                setImagenFireStoreCircle(contexto,pathcat,imagencat);
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

                        for (Categorias item :entradas) {

                            if(item.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            }else if(item.getDescripcion().toLowerCase().contains(constraint.toString().toLowerCase())) {

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
                        // no filter, add entire original list back in
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

}