package com.codevsolution.freemarketsapp.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.models.Productos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;
import java.util.Objects;

import static com.codevsolution.base.sqlite.ConsultaBD.insertRegistroDetalle;
import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.base.sqlite.ConsultaBD.queryList;
import static com.codevsolution.base.sqlite.ConsultaBD.queryListDetalle;
import static com.codevsolution.base.sqlite.ConsultaBD.queryObject;

public class FragmentCRUDPartidaBase extends FragmentCRUD implements Interactor.ConstantesPry,
        ContratoPry.Tablas, Interactor.TiposDetPartida {

    private AutoCompleteTextView autoNombrePartida;
    private EditMaterial nombrePartida;
    private EditMaterial descripcionPartida;
    private EditMaterial tiempoPartida;
    private EditMaterial importePartida;
    private ImageButton btnNuevaTarea;
    private ImageButton btnNuevoProd;
    private ImageButton btnNuevoProdProv;
    private Button btnpart;

    private RecyclerView rvdetalles;
    private ListaModelo listaDetpartidas;
    private Modelo partidabase;
    private double precioprodProv;
    private String idPartida;
    private int secPartida;


    public FragmentCRUDPartidaBase() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view){

        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }


    @Override
    protected void setNuevo() {

        update();

    }

    @Override
    protected void setLista() {

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_partidabase;
        layoutItem = R.layout.item_list_partidabase;
    }

    @Override
    protected void setInicio() {

        nombrePartida = (EditMaterial) ctrl(R.id.etnomudpartidabase, PARTIDABASE_NOMBRE);
        descripcionPartida = (EditMaterial) ctrl(R.id.etdescripcionUDpartidabase, PARTIDABASE_DESCRIPCION);
        tiempoPartida = (EditMaterial) ctrl(R.id.ettiempoUDpartidabase);
        importePartida = (EditMaterial) ctrl(R.id.etprecioUDpartidabase);
        btnNuevaTarea = (ImageButton) ctrl(R.id.btntareaudpartidabase);
        btnNuevoProd = (ImageButton) ctrl(R.id.btnprodudpartidabase);
        btnNuevoProdProv = (ImageButton) ctrl(R.id.btnprovudpartidabase);
        btnpart = (Button) ctrl(R.id.btn_asignar_a_partidaproy_partidabase);
        imagen = (ImagenLayout) ctrl(R.id.imgudpartidabase);
        rvdetalles = (RecyclerView) ctrl(R.id.rvdetalleUDpartidabase);
        autoNombrePartida = (AutoCompleteTextView) view.findViewById(R.id.autonompartidabase);


    }

    @Override
    protected void setTabla() {

        tabla = TABLA_PARTIDABASE;

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
        super.setBundle();

        System.out.println("id = " + id);
        if (nn(id)){
            modelo = CRUDutil.updateModelo(campos,id);
        }
    }

    @Override
    protected void setDatos() {

        calcularPrecioProdProv(id);
        visible(autoNombrePartida);
        autoNombrePartida.setText("");
        if (lista != null && lista.sizeLista() == 0) {
            gone(autoNombrePartida);
        }
        visible(btnNuevaTarea);
        visible(btnNuevoProd);
        visible(btnNuevoProdProv);
        visible(nombrePartida);
        visible(btndelete);

        idPartida = AndroidUtil.getSharePreference(contexto, PERSISTENCIA, PARTIDA_ID_PARTIDA, NULL);
        secPartida = AndroidUtil.getSharePreference(contexto, PERSISTENCIA, PARTIDA_SECUENCIA, 0);
        if (nnn(idPartida)) {
            visible(btnpart);
            if (secPartida > 0) {
                btnpart.setText(getString(R.string.sincronizar_clon_partida));
            }
        }
        descripcionPartida.setText(modelo.getString(PARTIDABASE_DESCRIPCION));
        nombrePartida.setText(modelo.getString(PARTIDABASE_NOMBRE));

        listaDetpartidas = new ListaModelo(CAMPOS_DETPARTIDABASE, id,TABLA_PARTIDABASE,null,null);

        System.out.println("listaDetpartidas = " + listaDetpartidas.sizeLista());
        if (listaDetpartidas.chechLista()) {

            visible(rvdetalles);
            rvdetalles.setLayoutManager(new LinearLayoutManager(getContext()));

            RVAdapter adapter = new RVAdapter(new ViewHolderDetPartida(view), listaDetpartidas.getLista(), R.layout.item_list_detpartidabase);

            rvdetalles.setAdapter(adapter);

            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String tipo = (listaDetpartidas.getItem(rvdetalles.getChildAdapterPosition(v)).
                            getString(DETPARTIDABASE_TIPO));
                    Modelo detpartidabase = listaDetpartidas.getItem(rvdetalles.getChildAdapterPosition(v));
                    bundle = new Bundle();
                    bundle.putString(CAMPO_ID, detpartidabase.getString(DETPARTIDABASE_ID_PARTIDABASE));
                    bundle.putInt(CAMPO_SECUENCIA, detpartidabase.getInt(DETPARTIDABASE_SECUENCIA));
                    bundle.putString(ORIGEN, PARTIDABASE);

                    switch (tipo) {
                        case TIPOTRABAJO:
                            icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaBaseTrabajo());
                            break;
                        case TIPOPRODUCTO:
                            icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaBaseProducto());
                            break;
                        case TIPOPRODUCTOPROV:
                            icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaBaseProdProvCat());
                            break;
                    }


                }
            });

        }else{

            rvdetalles.setVisibility(View.GONE);
        }

    }

    private double calcularPrecio(String id) {

        double precio = 0;
        ListaModelo listadet = CRUDutil.setListaModeloDetalle(CAMPOS_DETPARTIDABASE, id, TABLA_PARTIDABASE);
        for (Modelo detPartida : listadet.getLista()) {

            if (detPartida.getString(DETPARTIDABASE_TIPO).equals(TIPOTRABAJO)) {
                Modelo trabajo = CRUDutil.updateModelo(CAMPOS_TRABAJO, detPartida.getString(DETPARTIDABASE_ID_DETPARTIDABASE));
                precio += (trabajo.getDouble(TRABAJO_TIEMPO) * Interactor.hora);
            } else if (detPartida.getString(DETPARTIDABASE_TIPO).equals(TIPOPRODUCTO)) {
                Modelo producto = CRUDutil.updateModelo(CAMPOS_PRODUCTO, detPartida.getString(DETPARTIDABASE_ID_DETPARTIDABASE));
                precio += producto.getDouble(PRODUCTO_PRECIO)*detPartida.getDouble(DETPARTIDABASE_CANTIDAD);
            }
        }

        return precio;
    }

    private void calcularPrecioProdProv(final String id) {

        final ListaModelo listadet = CRUDutil.setListaModeloDetalle(CAMPOS_DETPARTIDABASE, id, TABLA_PARTIDABASE);
        for (final Modelo detPartida : listadet.getLista()) {

            if (detPartida.getString(DETPARTIDABASE_TIPO).equals(TIPOPRODUCTOPROV)) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child(PRODUCTOPRO).child(detPartida.getString(DETPARTIDABASE_ID_DETPARTIDABASE))
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        System.out.println("dataSnapshot = " + dataSnapshot.getValue());
                        Productos prodProv = dataSnapshot.getValue(Productos.class);
                        if (nn(prodProv)) {
                            precioprodProv += prodProv.getPrecio() * detPartida.getDouble(DETPARTIDABASE_CANTIDAD);
                            imagen.setImageFirestore(prodProv.getId());

                            tiempoPartida.setText(JavaUtil.getDecimales(calcularTiempo(detPartida.getString(DETPARTIDABASE_ID_PARTIDABASE))));
                            importePartida.setText(JavaUtil.formatoMonedaLocal(
                                    calcularPrecio(detPartida.getString(DETPARTIDABASE_ID_PARTIDABASE))+precioprodProv));
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
            }else {
                tiempoPartida.setText(JavaUtil.getDecimales(calcularTiempo(detPartida.getString(DETPARTIDABASE_ID_PARTIDABASE))));
                importePartida.setText(JavaUtil.formatoMonedaLocal(calcularPrecio(detPartida.getString(DETPARTIDABASE_ID_PARTIDABASE))));

            }
        }
    }

    private double calcularTiempo(String id) {

        double tiempo = 0;
        ListaModelo listadet = CRUDutil.setListaModeloDetalle(CAMPOS_DETPARTIDABASE, id, TABLA_PARTIDABASE);
        for (Modelo detPartida : listadet.getLista()) {

            if (detPartida.getString(DETPARTIDABASE_TIPO).equals(TIPOTRABAJO)) {
                Modelo trabajo = CRUDutil.updateModelo(CAMPOS_TRABAJO, detPartida.getString(DETPARTIDABASE_ID_DETPARTIDABASE));
                if (nn(trabajo)) {
                    tiempo += (trabajo.getDouble(TRABAJO_TIEMPO));
                }
            }
        }

        return tiempo;
    }

    @Override
    protected void setAcciones() {

        setAdaptadorAuto(autoNombrePartida);

        autoNombrePartida.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Modelo partidaclon = (Modelo) autoNombrePartida.getAdapter().getItem(position);
                autoNombrePartida.setText(partidaclon.getString(PARTIDABASE_NOMBRE));
                mostrarDialogoClonarPartidabase(partidaclon);
            }
        });

        btnNuevaTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PARTIDABASE_ID_PARTIDABASE, id);
                bundle = new Bundle();
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDTrabajo());


            }

        });

        btnNuevoProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PARTIDABASE_ID_PARTIDABASE, id);
                bundle = new Bundle();
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProducto());

            }

        });

        btnpart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                asignarAPartidaProy();

            }

        });

        btnNuevoProdProv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PARTIDABASE_ID_PARTIDABASE, id);
                bundle = new Bundle();
                putBundle(ORIGEN, PARTIDABASE);
                icFragmentos.enviarBundleAFragment(bundle, new ListadoProductosPro());

            }

        });


    }

    private void asignarAPartidaProy() {

        try {

            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {

                    modelo = CRUDutil.updateModelo(campos, id);
                    idPartida = AndroidUtil.getSharePreference(contexto, PERSISTENCIA, PARTIDA_ID_PARTIDA, NULL);
                    secPartida = AndroidUtil.getSharePreference(contexto, PERSISTENCIA, PARTIDA_SECUENCIA, 0);

                    ContentValues valores = new ContentValues();

                    putDato(valores, CAMPOS_PARTIDA, PARTIDA_NOMBRE, modelo.getString(PARTIDABASE_NOMBRE));
                    putDato(valores, CAMPOS_PARTIDA, PARTIDA_DESCRIPCION, modelo.getString(PARTIDABASE_DESCRIPCION));
                    putDato(valores, CAMPOS_PARTIDA, PARTIDA_RUTAFOTO, modelo.getString(PARTIDABASE_RUTAFOTO));

                    if (nn(secPartida) && secPartida > 0) {
                        CRUDutil.actualizarRegistro(TABLA_PARTIDA, idPartida, secPartida, valores);
                    } else {
                        putDato(valores, CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA, ContratoPry.generarIdTabla(TABLA_PARTIDA));
                        putDato(valores, CAMPOS_PARTIDA, PARTIDA_ID_PROYECTO, idPartida);
                        putDato(valores, CAMPOS_PARTIDA, PARTIDA_ID_PARTIDABASE, modelo.getString(PARTIDABASE_ID_PARTIDABASE));
                        Modelo proyecto = CRUDutil.updateModelo(CAMPOS_PROYECTO, idPartida);
                        putDato(valores, CAMPOS_PARTIDA, PARTIDA_ID_ESTADO, proyecto.getString(PROYECTO_ID_ESTADO));
                        System.out.println("valores = " + valores);

                        secPartida = CRUDutil.crearRegistroSec(CAMPOS_PARTIDA, idPartida, TABLA_PROYECTO, valores);
                    }

                    Modelo partida = CRUDutil.updateModelo(CAMPOS_PARTIDA, idPartida, secPartida);
                    String iddetpartida = partida.getString(PARTIDA_ID_PARTIDA);
                    ListaModelo listaDetPartidabase = CRUDutil.setListaModeloDetalle(CAMPOS_DETPARTIDABASE, id, TABLA_PARTIDABASE);
                    for (Modelo detPartidaBase : listaDetPartidabase.getLista()) {

                        valores = new ContentValues();

                        putDato(valores, CAMPOS_DETPARTIDA, DETPARTIDA_ID_PARTIDA, iddetpartida);
                        putDato(valores, CAMPOS_DETPARTIDA, DETPARTIDA_ID_DETPARTIDA, detPartidaBase.getString(DETPARTIDABASE_ID_DETPARTIDABASE));
                        putDato(valores, CAMPOS_DETPARTIDA, DETPARTIDA_TIPO, detPartidaBase.getString(DETPARTIDABASE_TIPO));
                        putDato(valores, CAMPOS_DETPARTIDA, DETPARTIDA_CANTIDAD, detPartidaBase.getString(DETPARTIDABASE_CANTIDAD));
                        boolean detnuevo = true;
                        ListaModelo listaDetPartida = CRUDutil.setListaModeloDetalle(CAMPOS_DETPARTIDA, iddetpartida, TABLA_PARTIDA);
                        for (Modelo detPartida : listaDetPartida.getLista()) {

                            if (detPartida.getString(DETPARTIDA_ID_DETPARTIDA).equals(detPartidaBase.getString(DETPARTIDABASE_ID_DETPARTIDABASE))) {
                                CRUDutil.actualizarRegistro(TABLA_DETPARTIDA, iddetpartida, detPartida.getInt(DETPARTIDA_SECUENCIA), valores);
                                detnuevo = false;
                            }
                        }
                        if (detnuevo) {
                            CRUDutil.crearRegistroSec(CAMPOS_DETPARTIDA, iddetpartida, TABLA_PARTIDA, valores);

                        }
                    }

                    ListaModelo listaDetPartida = CRUDutil.setListaModeloDetalle(CAMPOS_DETPARTIDA, iddetpartida, TABLA_PARTIDA);
                    for (Modelo detPartida : listaDetPartida.getLista()) {
                        boolean cambio = true;
                        for (Modelo detPartidaBase : listaDetPartidabase.getLista()) {

                            if (detPartida.getString(DETPARTIDA_ID_DETPARTIDA).equals(detPartidaBase.getString(DETPARTIDABASE_ID_DETPARTIDABASE))) {
                                cambio = false;
                            }
                        }
                        if (cambio) {
                            CRUDutil.borrarRegistro(TABLA_DETPARTIDA, detPartida.getString(DETPARTIDA_ID_PARTIDA), detPartida.getInt(DETPARTIDA_SECUENCIA));
                        }
                    }

                    bundle = new Bundle();
                    putBundle(CAMPO_ID, idPartida);
                    putBundle(CAMPO_SECUENCIA, secPartida);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaProyecto());

                }

            });
            th.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setTitulo() {

        tituloSingular = R.string.partida_base;
        tituloPlural = R.string.partidas_base;
        tituloNuevo = R.string.nueva_partidabase;
    }

    @Override
    protected void setcambioFragment() {


    }


    @Override
    protected void setContenedor() {

        if (id == null && lista.sizeLista() > 0) {
            setDato(PARTIDABASE_NOMBRE,autoNombrePartida.getText().toString());
        }else {
            setDato(PARTIDABASE_NOMBRE, nombrePartida.getText().toString());
        }
        setDato(PARTIDABASE_RUTAFOTO,path);
        setDato(PARTIDABASE_DESCRIPCION,descripcionPartida.getText().toString());

    }

    private void mostrarDialogoClonarPartidabase(final Modelo clon) {

        final CharSequence[] opciones = {"Clonar partida base","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Clonar partida base")){

                        valores = clon.contenido();
                        valores.remove(PARTIDABASE_ID_PARTIDABASE);
                        String nombre = valores.getAsString(PARTIDABASE_NOMBRE);
                        valores.remove(PARTIDABASE_NOMBRE);
                        valores.put(PARTIDABASE_NOMBRE,nombre + " clon");

                    if (nn(id)) {
                        CRUDutil.actualizarRegistro(tabla, id, valores);
                        partidabase = CRUDutil.updateModelo(campos,id);
                    } else {
                        Uri uri = CRUDutil.crearRegistro(tabla, valores);
                        partidabase = queryObject(campos, uri);
                        id = partidabase.getString(PARTIDABASE_ID_PARTIDABASE);

                    }


                    if (nnn(partidabase.getString(PARTIDABASE_RUTAFOTO))){
                        imagen.setImageUriPerfil(activityBase,partidabase.getString(PARTIDABASE_RUTAFOTO));
                        path = partidabase.getString(PARTIDABASE_RUTAFOTO);
                    }

                        ArrayList<Modelo> listaclon = queryListDetalle
                                (CAMPOS_DETPARTIDABASE,clon.getString(PARTIDABASE_ID_PARTIDABASE),TABLA_PARTIDABASE);

                        for (Modelo clonpart : listaclon) {

                            valores = clonpart.contenido();//AndroidUtil.clonarSinRef(clonpart);
                            valores.remove(DETPARTIDABASE_ID_PARTIDABASE);
                            valores.put(DETPARTIDABASE_ID_PARTIDABASE,id);
                            valores.remove(DETPARTIDABASE_SECUENCIA);

                            insertRegistroDetalle(CAMPOS_DETPARTIDABASE,id
                                    ,TABLA_PARTIDABASE,valores);
                        }
                    nuevo = false;
                    selector();

                }else {
                    dialog.dismiss();

                }
            }
        });
        builder.show();
    }


    class ViewHolderDetPartida extends BaseViewHolder implements TipoViewHolder {

        TextView tipo, nombre, ltiempo, lcantidad, limporte, tiempo, cantidad, importe;
        ImagenLayout imagen;

        public ViewHolderDetPartida(View itemView) {
            super(itemView);
            tipo = itemView.findViewById(R.id.tvtipoldetpaetidabase);
            nombre = itemView.findViewById(R.id.tvnomldetpartidabase);
            ltiempo = itemView.findViewById(R.id.ltiempoldetpartidabase);
            lcantidad =itemView.findViewById(R.id.lcantidadldetpartidabase);
            limporte = itemView.findViewById(R.id.limpldetpartidabase);
            tiempo = itemView.findViewById(R.id.tvtiempoldetpartidabase);
            importe = itemView.findViewById(R.id.tvimpldetpartidabase);
            cantidad = itemView.findViewById(R.id.tvcantidadldetpartidabase);
            imagen = itemView.findViewById(R.id.imgldetpartidabase);
        }

        @Override
        public void bind(final Modelo modelo) {

            String tipodetpartida = modelo.getString(DETPARTIDABASE_TIPO);
            String idDetPartidaBase = modelo.getString(DETPARTIDABASE_ID_DETPARTIDABASE);

            if (tipodetpartida.equals(TIPOTRABAJO)) {

                Modelo trabajo = CRUDutil.updateModelo(CAMPOS_TRABAJO, idDetPartidaBase);
                gone(cantidad);
                nombre.setText(trabajo.getString(TRABAJO_NOMBRE));
                tiempo.setText(JavaUtil.getDecimales(trabajo.getDouble(TRABAJO_TIEMPO)));
                importe.setText(JavaUtil.formatoMonedaLocal(
                        (trabajo.getDouble(TRABAJO_TIEMPO) * Interactor.hora)));

                String path = trabajo.getString(TRABAJO_RUTAFOTO);
                if (nn(path)) {
                    imagen.setImageUriCard(activityBase, path);
                }


            } else if (tipodetpartida.equals(TIPOPRODUCTO)) {

                Modelo producto = CRUDutil.updateModelo(CAMPOS_PRODUCTO, idDetPartidaBase);
                cantidad.setText(JavaUtil.getDecimales(modelo.getDouble(DETPARTIDABASE_CANTIDAD)));
                nombre.setText(producto.getString(PRODUCTO_NOMBRE));
                importe.setText(JavaUtil.formatoMonedaLocal(producto.getDouble(PRODUCTO_PRECIO)*
                        modelo.getDouble(DETPARTIDABASE_CANTIDAD)));
                String path = producto.getString(PRODUCTO_RUTAFOTO);
                if (nn(path)) {
                    imagen.setImageUriCard(activityBase, path);
                }

            } else if (tipodetpartida.equals(TIPOPRODUCTOPROV)) {

                DatabaseReference dbproductosprov = FirebaseDatabase.getInstance().getReference();

                dbproductosprov.child(PRODUCTOPRO).child(idDetPartidaBase)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Productos prodProv = dataSnapshot.getValue(Productos.class);

                        if (prodProv != null) {

                            nombre.setText(prodProv.getNombre());
                            cantidad.setText(JavaUtil.getDecimales(modelo.getDouble(DETPARTIDABASE_CANTIDAD)));
                            importe.setText(JavaUtil.formatoMonedaLocal
                                    ((prodProv.getPrecio()*modelo.getDouble(DETPARTIDABASE_CANTIDAD))));
                            String path = prodProv.getId();

                            if (path != null) {
                                imagen.setImageFirestoreCard(activityBase,path);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            tipo.setText(tipodetpartida.toUpperCase());

            if (!tipodetpartida.equals(TIPOTRABAJO)){

                ltiempo.setVisibility(View.GONE);
                tiempo.setVisibility(View.GONE);
            }

            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderDetPartida(view);
        }
    }

    class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        ImagenLayout imagenPartida;
        TextView descripcionPartida,tiempoPartida,importePartida;

        public ViewHolderRV(View itemView) {
            super(itemView);
            imagenPartida = itemView.findViewById(R.id.imglpartidabase);
            descripcionPartida = itemView.findViewById(R.id.tvdescripcionpartidabase);
            tiempoPartida = itemView.findViewById(R.id.tvtiempopartidabase);
            importePartida = itemView.findViewById(R.id.tvimppartidabase);
        }

        @Override
        public void bind(Modelo modelo) {

            String id = modelo.getString(PARTIDABASE_ID_PARTIDABASE);
            descripcionPartida.setText(modelo.getString(PARTIDABASE_DESCRIPCION));
            System.out.println("descripcionPartida = " + descripcionPartida);
            if (nnn(modelo.getString(PARTIDABASE_RUTAFOTO))) {

                imagenPartida.setImageUriCard(activityBase, modelo.getString(PARTIDABASE_RUTAFOTO));
            } else {
                gone(imagenPartida);
            }

            calcularPrecioProdProvCard(id);

            super.bind(modelo);
        }

        private void calcularPrecioProdProvCard(final String id) {

            final ListaModelo listadet = CRUDutil.setListaModeloDetalle(CAMPOS_DETPARTIDABASE, id, TABLA_PARTIDABASE);
            for (final Modelo detPartida : listadet.getLista()) {

                if (detPartida.getString(DETPARTIDABASE_TIPO).equals(TIPOPRODUCTOPROV)) {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child(PRODUCTOPRO).child(id).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Productos prodProv = dataSnapshot.getValue(Productos.class);
                            if (nn(prodProv)) {
                                precioprodProv += prodProv.getPrecio()* detPartida.getDouble(DETPARTIDABASE_CANTIDAD);
                                tiempoPartida.setText(JavaUtil.getDecimales(calcularTiempo(id)));
                                importePartida.setText(JavaUtil.formatoMonedaLocal(calcularPrecio(id)));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
                } else {
                    tiempoPartida.setText(JavaUtil.getDecimales(calcularTiempo(id)));
                    importePartida.setText(JavaUtil.formatoMonedaLocal(calcularPrecio(id)));

                }
            }
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    private void setAdaptadorAuto(AutoCompleteTextView autoCompleteTextView) {

        ListaModelo lista = CRUDutil.setListaModelo(campos);

        AdaptadorFiltroModelo adaptadorPartida = new AdaptadorFiltroModelo(contexto,
                    layoutItem,lista.getLista(),campos);
            autoCompleteTextView.setAdapter(adaptadorPartida);

    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {


        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<Modelo> entrada) {

            ImagenLayout imagenPartida = view.findViewById(R.id.imglpartidabase);
            TextView descripcionPartida = view.findViewById(R.id.tvdescripcionpartidabase);
            final TextView tiempoPartida = view.findViewById(R.id.tvtiempopartidabase);
            final TextView importePartida = view.findViewById(R.id.tvimppartidabase);
            final String id = entrada.get(posicion).getString(PARTIDABASE_ID_PARTIDABASE);
            descripcionPartida.setText(entrada.get(posicion).getString(PARTIDABASE_DESCRIPCION));
            System.out.println("descripcionPartida = " + descripcionPartida);
            if (nnn(entrada.get(posicion).getString(PARTIDABASE_RUTAFOTO))) {

                imagenPartida.setImageUriCard(activityBase, entrada.get(posicion).getString(PARTIDABASE_RUTAFOTO));

            } else {
                gone(imagenPartida);
            }

            final ListaModelo listadet = CRUDutil.setListaModeloDetalle(CAMPOS_DETPARTIDABASE, id, TABLA_PARTIDABASE);
            for (Modelo detPartida : listadet.getLista()) {

                if (detPartida.getString(DETPARTIDABASE_TIPO).equals(TIPOPRODUCTOPROV)) {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child(PRODUCTOPRO).child(id).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Productos prodProv = dataSnapshot.getValue(Productos.class);
                            precioprodProv += prodProv.getPrecio();
                            tiempoPartida.setText(JavaUtil.getDecimales(calcularTiempo(id)));
                            importePartida.setText(JavaUtil.formatoMonedaLocal(calcularPrecio(id)));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
                } else {
                    tiempoPartida.setText(JavaUtil.getDecimales(calcularTiempo(id)));
                    importePartida.setText(JavaUtil.formatoMonedaLocal(calcularPrecio(id)));

                }
            }

            super.setEntradas(posicion, view, entrada);
        }

    }

}
