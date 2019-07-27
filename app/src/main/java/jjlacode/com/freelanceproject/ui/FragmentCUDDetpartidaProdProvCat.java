package jjlacode.com.freelanceproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.model.Categorias;
import jjlacode.com.freelanceproject.model.ProdProv;
import jjlacode.com.freelanceproject.model.Proveedores;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.android.AndroidUtil;
import jjlacode.com.freelanceproject.util.android.controls.EditMaterial;
import jjlacode.com.freelanceproject.util.crud.CRUDutil;
import jjlacode.com.freelanceproject.util.crud.FragmentCUD;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.media.MediaUtil;

import static jjlacode.com.freelanceproject.util.JavaUtil.hoy;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.queryObject;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.queryObjectDetalle;

public class FragmentCUDDetpartidaProdProvCat extends FragmentCUD implements CommonPry.Constantes,
        ContratoPry.Tablas, CommonPry.TiposDetPartida, CommonPry.TiposEstados {

    private EditMaterial descripcion;
    private EditMaterial precio;
    private EditMaterial cantidad;
    private EditMaterial descProv;
    private EditMaterial refProv;
    private LinearLayout lyAutoNombre;
    private LinearLayout lyAutoCat;
    private LinearLayout lyAutoProv;
    private TextView tipoDetPartida;
    private AutoCompleteTextView autoNombre;
    private EditMaterial tiempoDet;
    private RecyclerView rvDetpartida;
    private String tipo;
    private Modelo proyecto;
    private Modelo partida;
    private String idDetPartida;

    private String idProyecto_Partida;
    private int secuenciaPartida;
    private ArrayList<ProdProv> listaProdProv;
    private ArrayList<Proveedores> listaProv;
    private ArrayList<Categorias> listaCat;
    private AutoCompleteTextView autoCat;
    private AutoCompleteTextView autoProv;
    private String proveedor = TODOS;
    private String categoria = TODAS;
    private ProgressBar progressBarPartida;
    private EditMaterial completadaPartida;


    private CheckBox partida_completada;
    private EditMaterial nombre;
    private DatabaseReference dbProductos;
    private Query query;
    private ListaAdaptadorFiltroCat adapterCat;
    private ListaAdaptadorFiltroProv adapterProv;
    private ListaAdaptadorFiltroProdProv adapteProdProv;
    private ArrayList<ProdProv> listaRvProdProv;
    private ArrayList<ProdProv> listaProdProvCompleta;


    public FragmentCUDDetpartidaProdProvCat() {
        // Required empty public constructor
    }

    @Override
    protected void setNuevo() {

        autoNombre.setThreshold(3);
        allGone();
        visible(tipoDetPartida);
        visible(lyAutoNombre);
        visible(autoNombre);
        visible(nombre);
        visible(descripcion);
        visible(rvDetpartida);
        tipoDetPartida.setText(tipo.toUpperCase());

        visible(cantidad);
        visible(refProv);
        visible(descProv);
        visible(lyAutoCat);
        visible(lyAutoProv);
        visible(autoCat);
        visible(autoProv);

    }

    @Override
    protected void setTabla() {

        tabla = TABLA_DETPARTIDA;

    }


    @Override
    protected void setBundle() {

        proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
        partida = (Modelo) bundle.getSerializable(TABLA_PARTIDA);

        if (partida != null) {
            secuenciaPartida = partida.getInt(PARTIDA_SECUENCIA);
            idProyecto_Partida = partida.getString(PARTIDA_ID_PROYECTO);
        }
        tipo = bundle.getString(TIPO);


    }

    @Override
    protected void setDatos() {

        modelo = CRUDutil.setModelo(campos, id, secuencia);

        allGone();
        tipo = modelo.getString(DETPARTIDA_TIPO);
        btndelete.setVisibility(View.VISIBLE);
        tipoDetPartida.setText(tipo.toUpperCase());
        nombre.setText(modelo.getString(DETPARTIDA_NOMBRE));
        descripcion.setText(modelo.getString(DETPARTIDA_DESCRIPCION));
        precio.setText(modelo.getString(DETPARTIDA_PRECIO));
        cantidad.setText(modelo.getString(DETPARTIDA_CANTIDAD));
        tiempoDet.setText(modelo.getString(DETPARTIDA_TIEMPO));
        descProv.setText(modelo.getString(DETPARTIDA_DESCUENTOPROVCAT));
        refProv.setText(modelo.getString(DETPARTIDA_REFPROVCAT));

        //Visualizamos campos comunes
        visible(nombre);
        visible(descripcion);
        visible(imagen);
        visible(tipoDetPartida);
        visible(precio);


        visible(refProv);
        visible(precio);
        visible(cantidad);
        visible(descProv);
        gone(lyAutoCat);
        gone(lyAutoProv);
        gone(lyAutoNombre);
        gone(rvDetpartida);

        if (tipo.equals(TIPOPRODUCTOPROV) && modelo.getString(DETPARTIDA_RUTAFOTO) != null) {
            mediaUtil = new MediaUtil(contexto);
            path = modelo.getString(DETPARTIDA_RUTAFOTO);
            setImagenFireStoreCircle(contexto, path, imagen);
        }
    }


    @Override
    protected void setAcciones() {


        if (tipo != null) {
            rv();

            setAdaptadorAuto(autoNombre);

            setAdaptadorAutoCat();

            setAdaptadorAutoProv();

            autoNombre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    ProdProv prodprov = (ProdProv) autoNombre.getAdapter().getItem(position);
                    idDetPartida = prodprov.getId();
                    nombre.setText(prodprov.getNombre());
                    descripcion.setText(prodprov.getDescripcion());
                    precio.setText(String.valueOf(prodprov.getPrecio()));
                    refProv.setText(prodprov.getRefprov());
                    cantidad.setActivo(true);

                    if (prodprov.getRutafoto() != null) {

                        setImagenFireStoreCircle(contexto, prodprov.getRutafoto(), imagen);
                        path = prodprov.getRutafoto();
                    }

                    autoNombre.setText("");

                }

            });
        }

        autoCat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {


            }
        });

        autoCat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (tipo.equals(TIPOPRODUCTOPROV)) {

                    listaProdProv = new ArrayList<>(listaProdProvCompleta);

                    ArrayList<ProdProv> prodProvtemp = new ArrayList<>();

                    System.out.println("categoria = " + categoria);
                    System.out.println("proveedor = " + proveedor);
                    for (ProdProv prodProv : listaProdProv) {

                        System.out.println("prodProv cat= " + prodProv.getCategoria());
                        System.out.println("prodProv prov= " + prodProv.getProveedor());
                        if ((prodProv.getCategoria().equals(categoria) ||
                                categoria.equals(TODAS) || categoria.equals("")) &&
                                (prodProv.getProveedor().equals(proveedor) ||
                                        proveedor.equals(TODOS) || proveedor.equals(""))) {
                            prodProvtemp.add(prodProv);
                        }

                    }
                    System.out.println("lista provtmp: " + prodProvtemp.size());

                    listaProdProv.clear();
                    listaProdProv.addAll(prodProvtemp);
                    prodProvtemp.clear();

                    System.out.println("lista prov: " + listaProdProv.size());
                    adapteProdProv = new ListaAdaptadorFiltroProdProv(getContext(),
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

                                setImagenFireStoreCircle(contexto, rutafoto, imagen);
                                //FirebaseStorage storage = FirebaseStorage.getInstance();
                                //StorageReference storageRef = storage.getReference();
                                //StorageReference spaceRef = storageRef.child(rutafoto);
                                //GlideApp.with(getContext())
                                //        .load(spaceRef)
                                //        .into(imagenTarea);
                            }

                        }
                    };

                    autoNombre.setAdapter(adapteProdProv);
                    autoNombre.setThreshold(50);
                    if (adapteProdProv.getLista() != null) {
                        listaRvProdProv = new ArrayList<>();
                        listaRvProdProv.clear();
                        listaRvProdProv.addAll(adapteProdProv.getLista());
                    }
                    rv();
                }
            }
        });

        autoProv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });

        autoProv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (tipo.equals(TIPOPRODUCTOPROV)) {

                    listaProdProv = new ArrayList<>(listaProdProvCompleta);

                    ArrayList<ProdProv> prodProvtemp = new ArrayList<>();

                    System.out.println("categoria = " + categoria);
                    System.out.println("proveedor = " + proveedor);
                    for (ProdProv prodProv : listaProdProv) {

                        if ((prodProv.getCategoria().equals(categoria) ||
                                categoria.equals(TODAS) || categoria.equals("")) &&
                                (prodProv.getProveedor().equals(proveedor) ||
                                        proveedor.equals(TODOS) || proveedor.equals(""))) {
                            prodProvtemp.add(prodProv);
                        }

                    }
                    System.out.println("lista provtmp: " + prodProvtemp.size());

                    listaProdProv.clear();
                    listaProdProv.addAll(prodProvtemp);
                    prodProvtemp.clear();

                    System.out.println("lista prov: " + listaProdProv.size());
                    adapteProdProv = new ListaAdaptadorFiltroProdProv(getContext(),
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

                                setImagenFireStoreCircle(contexto, rutafoto, imagen);
                                //FirebaseStorage storage = FirebaseStorage.getInstance();
                                //StorageReference storageRef = storage.getReference();
                                //StorageReference spaceRef = storageRef.child(rutafoto);
                                //GlideApp.with(getContext())
                                //        .load(spaceRef)
                                //        .into(imagenTarea);
                            }

                        }
                    };

                    autoNombre.setAdapter(adapteProdProv);
                    autoNombre.setThreshold(50);
                    if (adapteProdProv.getLista() != null) {
                        listaRvProdProv = new ArrayList<>();
                        listaRvProdProv.clear();
                        listaRvProdProv.addAll(adapteProdProv.getLista());
                    }
                    rv();
                }
            }
        });

        autoNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                autoNombre.setDropDownWidth(0);
                if (adapteProdProv.getLista() != null) {
                    listaRvProdProv = new ArrayList<>();
                    listaRvProdProv.clear();
                    listaRvProdProv.addAll(adapteProdProv.getLista());
                    rv();

                }
            }
        });
    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.detpartida;
        tituloPlural = tituloSingular;
        tituloNuevo = R.string.nuevo_detalle_partida;
    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_cud_detpartida;

    }

    @Override
    protected void setInicio() {

        rvDetpartida = (RecyclerView) ctrl(R.id.rvcdetpartida);
        descripcion = (EditMaterial) ctrl(R.id.etdesccdetpartida);
        precio = (EditMaterial) ctrl(R.id.etpreciocdetpartida);
        cantidad = (EditMaterial) ctrl(R.id.etcantcdetpartida);
        lyAutoNombre = (LinearLayout) ctrl(R.id.layout_autonomdetpartida);
        lyAutoCat = (LinearLayout) ctrl(R.id.layout_autocatdetpartida);
        lyAutoProv = (LinearLayout) ctrl(R.id.layout_autoprovdetpartida);
        autoNombre = (AutoCompleteTextView) ctrl(R.id.autonomdetpartida);
        nombre = (EditMaterial) ctrl(R.id.etnombredetpartida);
        tiempoDet = (EditMaterial) ctrl(R.id.ettiempocdetpartida);
        imagen = (ImageView) ctrl(R.id.imgcdetpartida);
        refProv = (EditMaterial) ctrl(R.id.tvrefprovcdetpartida);
        descProv = (EditMaterial) ctrl(R.id.etporcdesprovcdetpartida);
        tipoDetPartida = (TextView) ctrl(R.id.tvtipocdetpartida);
        autoCat = (AutoCompleteTextView) ctrl(R.id.autocat);
        autoProv = (AutoCompleteTextView) ctrl(R.id.autoprov);
        partida_completada = (CheckBox) ctrl(R.id.cbox_hacer_detpartida_completa);
        progressBarPartida = (ProgressBar) ctrl(R.id.progressBardetpartida);
        completadaPartida = (EditMaterial) ctrl(R.id.etcompletadadetpartida);
        cantidad.setText("1");

    }

    private void rv() {

        if (secuencia == 0) {

            mediaUtil = new MediaUtil(contexto);


            gone(refProv);
            gone(nombre);
            gone(descripcion);
            gone(precio);
            gone(cantidad);
            gone(descProv);


            AdaptadorProveedor provAdapter = new AdaptadorProveedor(listaRvProdProv);

            rvDetpartida.setLayoutManager(new LinearLayoutManager(getContext()));
            rvDetpartida.setHasFixedSize(true);

            rvDetpartida.setAdapter(provAdapter);

            provAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int i = rvDetpartida.getChildViewHolder(v).getAdapterPosition();
                    idDetPartida = listaRvProdProv.get(i).getId();
                    System.out.println("idDetPartida = " + idDetPartida);

                    visible(refProv);
                    visible(nombre);
                    visible(descripcion);
                    visible(precio);
                    visible(cantidad);
                    visible(descProv);
                    visible(imagen);
                    //gone(autoCat);
                    gone(lyAutoCat);
                    gone(lyAutoProv);
                    gone(lyAutoNombre);
                    //gone(autoProv);
                    //gone(autoNombre);
                    gone(rvDetpartida);
                    refProv.setText(listaRvProdProv.get(i).getRefprov());
                    nombre.setText(listaRvProdProv.get(i).getNombre());
                    descripcion.setText(listaRvProdProv.get(i).getDescripcion());
                    precio.setText(String.valueOf(listaRvProdProv.get(i).getPrecio()));

                    path = listaRvProdProv.get(i).getRutafoto();
                    if (path != null) {
                        setImagenFireStoreCircle(contexto, path, imagen);
                    }


                }
            });

        }

    }


    @Override
    protected void setContenedor() {

        setDato(DETPARTIDA_NOMBRE, nombre.getText().toString());
        setDato(DETPARTIDA_DESCRIPCION, descripcion.getText().toString());
        if (tipo.equals(TIPOTRABAJO)) {
            setDato(DETPARTIDA_CANTIDAD, 1);

        } else {
            setDato(DETPARTIDA_CANTIDAD, cantidad.getText().toString());
        }
        setDato(DETPARTIDA_RUTAFOTO, path);
        setDato(DETPARTIDA_ID_DETPARTIDA, idDetPartida);
        setDato(DETPARTIDA_ID_PARTIDA, id);
        setDato(DETPARTIDA_TIPO, tipo);


        if (partida_completada.isChecked()) {
            setDato(DETPARTIDA_COMPLETA, 1);
        } else {
            setDato(DETPARTIDA_COMPLETA, 0);

        }

        setDato(DETPARTIDA_PRECIO, precio.getText().toString());
        setDato(DETPARTIDA_DESCUENTOPROVCAT, descProv.getText().toString());
        setDato(DETPARTIDA_REFPROVCAT, refProv.getText().toString());


        CommonPry.Calculos.actualizarPartidaProyecto(id);

    }

    @Override
    protected boolean onBack() {
        tipo = null;
        return super.onBack();
    }

    @Override
    protected void setcambioFragment() {

        bundle = new Bundle();
        new CommonPry.Calculos.TareaActualizaProy().execute(idProyecto_Partida);
        partida = queryObjectDetalle(CAMPOS_PARTIDA, idProyecto_Partida, secuenciaPartida);
        bundle.putSerializable(MODELO, partida);
        bundle.putSerializable(PROYECTO, proyecto);
        bundle.putString(ORIGEN, DETPARTIDA);
        bundle.putString(SUBTITULO, subTitulo);
        bundle.putString(CAMPO_ID, idProyecto_Partida);
        bundle.putInt(CAMPO_SECUENCIA, secuenciaPartida);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaProyecto());
        bundle = null;

    }

    private void setAdaptadorAuto(AutoCompleteTextView autoCompleteTextView) {


        listaProdProvCompleta = new ArrayList<>();

        dbProductos =
                FirebaseDatabase.getInstance().getReference();

        query = dbProductos.child("productos");


        ValueEventListener eventListenerProd = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ProdProv prodProv = ds.getValue(ProdProv.class);
                    prodProv.setId(ds.getRef().getKey());
                    listaProdProvCompleta.add(prodProv);
                }

                System.out.println("lista prov: " + listaProdProvCompleta.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };

        query.addValueEventListener(eventListenerProd);


        adapteProdProv = new ListaAdaptadorFiltroProdProv(getContext(),
                R.layout.item_list_prodprov, listaProdProvCompleta) {
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

                    setImagenFireStoreCircle(contexto, rutafoto, imagen);
                    //FirebaseStorage storage = FirebaseStorage.getInstance();
                    //StorageReference storageRef = storage.getReference();
                    //StorageReference spaceRef = storageRef.child(rutafoto);
                    //GlideApp.with(getContext())
                    //        .load(spaceRef)
                    //        .into(imagenTarea);
                }

            }
        };

        autoNombre.setAdapter(adapteProdProv);

    }

    private void setAdaptadorAutoCat() {

        listaCat = new ArrayList<>();

        DatabaseReference dbCategorias =
                FirebaseDatabase.getInstance().getReference()
                        .child("categorias");

        ValueEventListener eventListenerCat = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
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


        adapterCat = new ListaAdaptadorFiltroCat
                (contexto, R.layout.item_list_categoria, listaCat);

        autoCat.setAdapter(adapterCat);

        autoCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                categoria = adapterCat.getItem(position).getNombre();//listaCat.get(position).getNombre();
                autoCat.setText(categoria);
                rv();

            }
        });

    }

    private void setAdaptadorAutoProv() {

        listaProv = new ArrayList<>();

        DatabaseReference dbProveedor =
                FirebaseDatabase.getInstance().getReference()
                        .child("proveedores");

        ValueEventListener eventListenerProv = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
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


        adapterProv = new ListaAdaptadorFiltroProv
                (contexto, R.layout.item_list_proveedorcat, listaProv);

        autoProv.setAdapter(adapterProv);

        autoProv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                proveedor = adapterProv.getItem(position).getNombre();//listaProv.get(position).getNombre();
                autoProv.setText(proveedor);
                rv();
            }
        });
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
            if (listener != null) {
                listener.onClick(v);
            }

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

    public abstract class ListaAdaptadorFiltroProdProv extends ArrayAdapter<ProdProv> {

        private ArrayList<ProdProv> entradas;
        private ArrayList<ProdProv> entradasfiltro;
        private int R_layout_IdView;
        private Context contexto;

        ListaAdaptadorFiltroProdProv(Context contexto, int R_layout_IdView, ArrayList<ProdProv> entradas) {
            super(contexto, R_layout_IdView, entradas);
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
            onEntrada(entradasfiltro.get(posicion), view);
            System.out.println("entradasfiltro = " + entradasfiltro.get(posicion));
            return view;
        }

        @Override
        public int getCount() {
            return entradasfiltro.size();
        }

        public ArrayList<ProdProv> getLista() {

            return entradasfiltro;
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

                        for (ProdProv item : entradas) {

                            if (item.getNombre() != null && item.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            } else if (item.getDescripcion() != null && item.getDescripcion().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            } else if (item.getRefprov() != null && item.getRefprov().toLowerCase().contains(constraint.toString().toLowerCase())) {

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

        /**
         * Devuelve cada una de las entradas con cada una de las vistas a la que debe de ser asociada
         *
         * @param entrada La entrada que será la asociada a la view. La entrada es del tipo del paquete/handler
         * @param view    View particular que contendrá los datos del paquete/handler
         */
        public abstract void onEntrada(ProdProv entrada, View view);
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

                            if ((item.getNombre() != null) && item.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            } else if ((item.getDescripcion() != null) && item.getDescripcion().toLowerCase().contains(constraint.toString().toLowerCase())) {

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

                            if (item.getNombre() != null && item.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            } else if (item.getDescripcion() != null && item.getDescripcion().toLowerCase().contains(constraint.toString().toLowerCase())) {

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

    public class AdaptadorFiltroRV extends ListaAdaptadorFiltroRV {

        public AdaptadorFiltroRV(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<Modelo> entrada) {

            TextView tipo, nombre, ltiempo, lcantidad, limporte, tiempo, cantidad, importe;
            ImageView imagen;

            tipo = itemView.findViewById(R.id.tvtipoldetpaetida);
            nombre = itemView.findViewById(R.id.tvnomldetpartida);
            ltiempo = itemView.findViewById(R.id.ltiempoldetpartida);
            lcantidad = itemView.findViewById(R.id.lcantldetpartida);
            limporte = itemView.findViewById(R.id.limpldetpartida);
            tiempo = itemView.findViewById(R.id.tvtiempoldetpartida);
            cantidad = itemView.findViewById(R.id.tvcantldetpartida);
            importe = itemView.findViewById(R.id.tvimpldetpartida);
            imagen = itemView.findViewById(R.id.imgldetpartida);

            String tipodetpartida = entrada.get(posicion).getString(DETPARTIDA_TIPO);
            tipo.setText(tipodetpartida.toUpperCase());
            nombre.setText(entrada.get(posicion).getString(DETPARTIDA_NOMBRE));
            tiempo.setText(entrada.get(posicion).getString(DETPARTIDA_TIEMPO));
            if (entrada.get(posicion).getString(DETPARTIDA_TIPO).equals(CommonPry.TiposDetPartida.TIPOTRABAJO)) {
                importe.setText(JavaUtil.formatoMonedaLocal(
                        (entrada.get(posicion).getDouble(DETPARTIDA_TIEMPO) * CommonPry.hora)));
            } else {
                importe.setText(entrada.get(posicion).getString(DETPARTIDA_PRECIO));
            }
            if (entrada.get(posicion).getString(DETPARTIDA_RUTAFOTO) != null) {
                if (tipodetpartida.equals(TIPOPRODUCTOPROV)) {
                    MediaUtil imagenUtil = new MediaUtil(contexto);
                    imagenUtil.setImageFireStoreCircle(entrada.get(posicion).getString(DETPARTIDA_RUTAFOTO), imagen);

                } else {
                    imagen.setImageURI(entrada.get(posicion).getUri(DETPARTIDA_RUTAFOTO));
                }
            }

            if (!tipodetpartida.equals(TIPOTRABAJO)) {

                ltiempo.setVisibility(View.GONE);
                tiempo.setVisibility(View.GONE);
            }


            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView nomPartida, nombre, proy, tvcomplet, ttiempo, treking;
        ImageView imagen, imgEstado;
        ProgressBar pbar, pbar2;
        CardView card;

        public ViewHolderRV(View itemView) {
            super(itemView);
            nomPartida = itemView.findViewById(R.id.tvpartldetpaetidat);
            nombre = itemView.findViewById(R.id.tvnomldetpartidat);
            proy = itemView.findViewById(R.id.tvproydetpartidat);
            imagen = itemView.findViewById(R.id.imgldetpartidat);
            pbar = itemView.findViewById(R.id.pbarldetpartidat);
            pbar2 = itemView.findViewById(R.id.pbar2ldetpartidat);
            ttiempo = itemView.findViewById(R.id.tvttotalldetpartidat);
            tvcomplet = itemView.findViewById(R.id.tvcompletadaldetpartidat);
            card = itemView.findViewById(R.id.carddetpartidat);
            treking = itemView.findViewById(R.id.tvtrekldetpaetidat);
            imgEstado = itemView.findViewById(R.id.imgestadoldetpartidat);
        }

        @Override
        public void bind(Modelo modelo) {

            String id = modelo.getString(DETPARTIDA_ID_PARTIDA);
            int secuencia = modelo.getInt(DETPARTIDA_SECUENCIA);
            modelo = queryObjectDetalle(CAMPOS_DETPARTIDA, id, secuencia);

            nombre.setText(modelo.getString(DETPARTIDA_NOMBRE));

            try {
                MediaUtil mediaUtil = new MediaUtil(contexto);
                mediaUtil.setImageUriCircle(modelo.getString(DETPARTIDA_RUTAFOTO), imagen);

            } catch (Exception e) {
                e.printStackTrace();
            }

            long ahora = hoy();
            Modelo partida = queryObject(CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA, id, null, IGUAL, null);
            nomPartida.setText(partida.getString(PARTIDA_NOMBRE));
            double cantidadTotal = partida.getDouble(PARTIDA_CANTIDAD);
            double tiempodet = (modelo.getDouble(DETPARTIDA_TIEMPO) * cantidadTotal * HORASLONG) / 1000;
            double tiemporeal = (modelo.getDouble(DETPARTIDA_TIEMPOREAL) * HORASLONG) / 1000;
            String idProyecto = partida.getString(PARTIDA_ID_PROYECTO);
            Modelo proyecto = queryObject(CAMPOS_PROYECTO, idProyecto);
            proy.setText(proyecto.getString(PROYECTO_NOMBRE));
            long contador = modelo.getLong(DETPARTIDA_CONTADOR);
            long pausa = modelo.getLong(DETPARTIDA_PAUSA);
            if (pausa > 0) {
                ahora = pausa;
                imgEstado.setImageResource(R.drawable.ic_pausa_indigo);
            } else if (contador > 0) {
                imgEstado.setImageResource(R.drawable.ic_play_indigo);
            } else {
                contador = ahora;
                imgEstado.setImageResource(R.drawable.ic_stop_indigo);
            }

            double count = ((double) (ahora - contador)) / 1000;


            double completada = (((tiemporeal * 100) / tiempodet) + ((count * 100) / tiempodet));

            String asText = JavaUtil.relojContador((ahora - contador) / 1000);
            treking.setText(String.format(Locale.getDefault(), "%s", asText));

            ttiempo.setText(String.format(Locale.getDefault(),
                    "%s %s %s %s %s", JavaUtil.getDecimales(((tiemporeal + count) / 3600)),
                    getString(R.string.horas), getString(R.string.de),
                    JavaUtil.getDecimales(tiempodet / 3600), getString(R.string.horas)));

            tvcomplet.setText(String.format(Locale.getDefault(),
                    "%s %s", JavaUtil.getDecimales(completada), "% completa"));

            AndroidUtil.barsCard(contexto, pbar, pbar2, false, 100, 90, 120, completada,
                    tvcomplet, treking, R.color.Color_contador_ok, R.color.Color_contador_acept,
                    R.color.Color_contador_notok, card, R.color.Color_card_ok, R.color.Color_card_acept,
                    R.color.Color_card_notok);

            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

}