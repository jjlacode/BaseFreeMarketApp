package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroModelo;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.android.AppActivity;
import jjlacode.com.freelanceproject.util.crud.CRUDutil;
import jjlacode.com.freelanceproject.util.crud.FragmentCRUD;
import jjlacode.com.freelanceproject.util.crud.ListaModelo;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.media.MediaUtil;

import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.insertRegistro;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.insertRegistroDetalle;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.queryList;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.queryListDetalle;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.queryObject;

public class FragmentCRUDPartidaBase extends FragmentCRUD implements CommonPry.Constantes,
        ContratoPry.Tablas, CommonPry.TiposDetPartida {

    private AutoCompleteTextView autoNombrePartida;
    private EditText nombrePartida;
    private EditText descripcionPartida;
    private TextView tiempoPartida;
    private TextView importePartida;
    private Button btnNuevaTarea;
    private Button btnNuevoProd;
    private Button btnNuevoProdProv;
    private Button btnpart;
    private ImageView imagenret;

    private RecyclerView rvdetalles;
    private ListaModelo listaDetpartidas;
    private Modelo partidabase;


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

        autoNombrePartida.setVisibility(View.VISIBLE);
        nombrePartida.setVisibility(View.GONE);
        btnNuevoProd.setVisibility(View.GONE);
        btnNuevaTarea.setVisibility(View.GONE);
        btnNuevoProdProv.setVisibility(View.GONE);
        btnpart.setVisibility(View.GONE);
        visible(btndelete);


    }

    @Override
    protected void setLista() {

        lista = new ListaModelo(campos);
        setAdaptadorAuto(autoNombrePartida);

        autoNombrePartida.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Modelo partidaclon = (Modelo) autoNombrePartida.getAdapter().getItem(position);
                mostrarDialogoClonarPartidabase(partidaclon);
            }
        });


    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_partidabase;
        layoutItem = R.layout.item_list_partidabase;
    }

    @Override
    protected void setInicio() {

        nombrePartida = view.findViewById(R.id.etnomudpartidabase);
        descripcionPartida = view.findViewById(R.id.etdescripcionUDpartidabase);
        tiempoPartida = view.findViewById(R.id.ettiempoUDpartidabase);
        importePartida = view.findViewById(R.id.etprecioUDpartidabase);
        btnNuevaTarea = view.findViewById(R.id.btntareaudpartidabase);
        btnNuevoProd = view.findViewById(R.id.btnprodudpartidabase);
        btnNuevoProdProv = view.findViewById(R.id.btnprovudpartidabase);
        btnpart = view.findViewById(R.id.btnpartudpartidabase);
        imagen = view.findViewById(R.id.imgudpartidabase);
        imagenret = view.findViewById(R.id.imgretudpartidabase);
        rvdetalles = view.findViewById(R.id.rvdetalleUDpartidabase);
        autoNombrePartida = view.findViewById(R.id.autonompartidabase);


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
    protected void setDatos() {

        autoNombrePartida.setVisibility(View.GONE);
        btnNuevoProd.setVisibility(View.GONE);
        btnNuevaTarea.setVisibility(View.GONE);
        btnNuevoProdProv.setVisibility(View.GONE);
        btnpart.setVisibility(View.GONE);
        nombrePartida.setVisibility(View.VISIBLE);
        visible(btndelete);

        if (modelo.getString(PARTIDABASE_ID_PARTIDAORIGEN)==null) {
            btnNuevaTarea.setVisibility(View.VISIBLE);
            btnNuevoProd.setVisibility(View.VISIBLE);
            btnpart.setVisibility(View.VISIBLE);
        }

        CommonPry.Calculos.actualizarPartidaBase(id);


        nombrePartida.setText(modelo.getString(PARTIDABASE_NOMBRE));
        descripcionPartida.setText(modelo.getString(PARTIDABASE_DESCRIPCION));
        tiempoPartida.setText(modelo.getString(PARTIDABASE_TIEMPO));
        importePartida.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(PARTIDABASE_PRECIO)));

        if (modelo.getString(PARTIDABASE_RUTAFOTO)!=null){

            path = modelo.getString(PARTIDABASE_RUTAFOTO);
            setImagenUriCircle(contexto,path);

        }

        imagenret.setVisibility(View.GONE);


        listaDetpartidas = new ListaModelo(CAMPOS_DETPARTIDABASE, id,TABLA_PARTIDABASE,null,null);

        if (listaDetpartidas.chechLista()) {

            rvdetalles.setLayoutManager(new LinearLayoutManager(getContext()));

            AdaptadorDetpartida adapter = new AdaptadorDetpartida(listaDetpartidas.getLista());

            rvdetalles.setAdapter(adapter);

            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String tipo = (listaDetpartidas.getItem(rvdetalles.getChildAdapterPosition(v)).
                            getString(DETPARTIDABASE_TIPO));
                    Modelo detpartidabase = listaDetpartidas.getItem(rvdetalles.getChildAdapterPosition(v));
                    bundle = new Bundle();
                    bundle.putSerializable(TABLA_PARTIDABASE, modelo);
                    bundle.putSerializable(MODELO, detpartidabase);
                    bundle.putString(CAMPO_ID, detpartidabase.getString(DETPARTIDABASE_ID_PARTIDABASE));
                    bundle.putInt(CAMPO_SECUENCIA, detpartidabase.getInt(DETPARTIDABASE_SECUENCIA));
                    bundle.putString(ORIGEN, PARTIDABASE);
                    bundle.putString(TIPO, tipo);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaBase());

                }
            });

        }else{

            rvdetalles.setVisibility(View.GONE);
        }

    }

    @Override
    protected void setAcciones() {

        btnNuevaTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                modelo = CRUDutil.setModelo(campos,id);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PARTIDABASE, modelo);
                bundle.putString(ORIGEN, PARTIDABASE);
                bundle.putString(SUBTITULO,getString(R.string.nueva_tarea));
                bundle.putString(TIPO, TIPOTRABAJO);
                bundle.putBoolean(NUEVOREGISTRO,true);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCUDDetpartidaBase());

            }

        });

        btnNuevoProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                CRUDutil.setModelo(campos,id);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PARTIDABASE, modelo);
                bundle.putString(ORIGEN, PARTIDABASE);
                bundle.putString(SUBTITULO,getString(R.string.nuevo_producto));
                bundle.putString(TIPO, TIPOPRODUCTO);
                bundle.putBoolean(NUEVOREGISTRO,true);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCUDDetpartidaBase());

            }

        });

        btnpart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                CRUDutil.setModelo(campos,id);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PARTIDABASE, modelo);
                bundle.putString(ORIGEN, PARTIDABASE);
                bundle.putString(SUBTITULO,getString(R.string.nueva_partidabase));
                bundle.putString(TIPO, TIPOPARTIDA);
                bundle.putBoolean(NUEVOREGISTRO,true);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCUDDetpartidaBase());

            }

        });

        btnNuevoProdProv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                CRUDutil.setModelo(campos,id);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PARTIDABASE, modelo);
                bundle.putString(ORIGEN, PARTIDABASE);
                bundle.putString(SUBTITULO,getString(R.string.nuevo_prodprov));
                bundle.putString(TIPO, TIPOPRODUCTOPROV);
                bundle.putBoolean(NUEVOREGISTRO,true);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCUDDetpartidaBase());

            }

        });

        DatabaseReference dbProveedor =
                FirebaseDatabase.getInstance().getReference()
                        .child("productos");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                CommonPry.Calculos.sincronizarPartidaBase(id);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        dbProveedor.addValueEventListener(eventListener);


    }

    @Override
    protected void setTitulo() {

        tituloSingular = R.string.partida_base;
        tituloPlural = R.string.partidas_base;
        tituloNuevo = R.string.nueva_partidabase;
    }

    @Override
    protected void setcambioFragment() {

        if (id!=null) {
            Thread th = new Thread() {
                @Override
                public void run() {

                    CommonPry.Calculos.actualizarPartidaBase(id);
                    System.out.println("En segundo plano");
                }
            };
            th.start();
        }

    }


    @Override
    protected void setContenedor() {

        if (id==null){
            setDato(PARTIDABASE_NOMBRE,autoNombrePartida.getText().toString());
        }else {
            setDato(PARTIDABASE_NOMBRE, nombrePartida.getText().toString());
        }
        setDato(PARTIDABASE_RUTAFOTO,path);
        setDato(PARTIDABASE_DESCRIPCION,descripcionPartida.getText().toString());
        setDato(PARTIDABASE_TIEMPO,JavaUtil.comprobarDouble(tiempoPartida.getText().toString()));
        setDato(PARTIDABASE_PRECIO,JavaUtil.comprobarDouble(importePartida.getText().toString()));

    }

    private void mostrarDialogoClonarPartidabase(final Modelo clon) {

        final CharSequence[] opciones = {"Clonar partida base","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Clonar partida base")){

                    if (clon.getNombreTabla().equals(TABLA_PARTIDA)) {

                        valores = new ContentValues();
                        valores.put(PARTIDABASE_DESCRIPCION, clon.getString(PARTIDA_DESCRIPCION));
                        valores.put(PARTIDABASE_NOMBRE, clon.getString(PARTIDA_NOMBRE));
                        valores.put(PARTIDABASE_PRECIO, clon.getString(PARTIDA_PRECIO));
                        valores.put(PARTIDABASE_TIEMPO, clon.getString(PARTIDA_TIEMPO));
                        valores.put(PARTIDABASE_RUTAFOTO, clon.getString(PARTIDA_RUTAFOTO));

                    }else if (clon.getNombreTabla().equals(TABLA_PARTIDABASE)){

                        valores = clon.contenido();
                        valores.remove(PARTIDABASE_ID_PARTIDABASE);
                    }

                    Uri uri = insertRegistro(TABLA_PARTIDABASE,valores);
                    partidabase = queryObject(CAMPOS_PARTIDABASE,uri);
                    id = partidabase.getString(PARTIDABASE_ID_PARTIDABASE);

                    autoNombrePartida.setText(partidabase.getString(PARTIDABASE_NOMBRE));
                    descripcionPartida.setText(partidabase.getString(PARTIDABASE_DESCRIPCION));
                    importePartida.setText(JavaUtil.formatoMonedaLocal(partidabase.getDouble(PARTIDABASE_PRECIO)));
                    tiempoPartida.setText(partidabase.getString(PARTIDABASE_TIEMPO));
                    if (partidabase.getString(PARTIDABASE_RUTAFOTO)!=null){
                        imagen.setImageURI(partidabase.getUri(PARTIDABASE_RUTAFOTO));
                        path = partidabase.getString(PARTIDABASE_RUTAFOTO);
                    }

                    if (clon.getNombreTabla().equals(TABLA_PARTIDA)) {

                        ArrayList<Modelo> listaclon = queryListDetalle
                                (CAMPOS_DETPARTIDA,clon.getString(PARTIDA_ID_PARTIDA),TABLA_PARTIDA);

                        for (Modelo clonpart : listaclon) {

                            valores = new ContentValues();
                            setDato(DETPARTIDABASE_ID_PARTIDABASE,id);
                            setDato(DETPARTIDABASE_NOMBRE,clonpart.getString(DETPARTIDA_NOMBRE));
                            setDato(DETPARTIDABASE_DESCRIPCION,clonpart.getString(DETPARTIDA_DESCRIPCION));
                            setDato(DETPARTIDABASE_BENEFICIO,clonpart.getString(DETPARTIDA_BENEFICIO));
                            setDato(DETPARTIDABASE_CANTIDAD,clonpart.getString(DETPARTIDA_CANTIDAD));
                            setDato(DETPARTIDABASE_DESCUENTOPROVCAT,clonpart.getString(DETPARTIDA_DESCUENTOPROVCAT));
                            setDato(DETPARTIDABASE_PRECIO,clonpart.getString(DETPARTIDA_PRECIO));
                            setDato(DETPARTIDABASE_ID_DETPARTIDABASE,clonpart.getString(DETPARTIDA_ID_DETPARTIDA));
                            setDato(DETPARTIDABASE_REFPROVCAT,clonpart.getString(DETPARTIDA_REFPROVCAT));
                            setDato(DETPARTIDABASE_TIEMPO,clonpart.getString(DETPARTIDA_TIEMPO));
                            setDato(DETPARTIDABASE_RUTAFOTO,clonpart.getString(DETPARTIDA_RUTAFOTO));
                            setDato(DETPARTIDABASE_TIPO,clonpart.getString(DETPARTIDA_TIPO));
                            valores.remove(DETPARTIDA_SECUENCIA);

                            insertRegistroDetalle(CAMPOS_DETPARTIDABASE,id
                                    ,TABLA_PARTIDABASE,valores);
                        }

                    }else if (clon.getNombreTabla().equals(TABLA_PARTIDABASE)){

                        ArrayList<Modelo> listaclon = queryListDetalle
                                (CAMPOS_DETPARTIDABASE,clon.getString(PARTIDABASE_ID_PARTIDABASE),TABLA_PARTIDABASE);

                        for (Modelo clonpart : listaclon) {

                            valores = clonpart.contenido();//AndroidUtil.clonarSinRef(clonpart);
                            valores.put(DETPARTIDABASE_ID_PARTIDABASE,id);
                            valores.remove(DETPARTIDABASE_SECUENCIA);

                            insertRegistroDetalle(CAMPOS_DETPARTIDABASE,id
                                    ,TABLA_PARTIDABASE,valores);
                        }
                    }


                }else {
                    dialog.dismiss();
                    autoNombrePartida.setText("");
                }
            }
        });
        builder.show();
    }

    public static class AdaptadorDetpartida extends RecyclerView.Adapter<AdaptadorDetpartida.DetpartidaViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas, CommonPry.TiposDetPartida {

        private ArrayList<Modelo> listDetpartida;
        private View.OnClickListener listener;
        private Context context = AppActivity.getAppContext();

        AdaptadorDetpartida(ArrayList<Modelo> listDetpartida) {

            this.listDetpartida = listDetpartida;
        }

        @NonNull
        @Override
        public DetpartidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_detpartidabase, null, false);

            view.setOnClickListener(this);


            return new DetpartidaViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull DetpartidaViewHolder detpartidaViewHolder, int position) {

            System.out.println("tipo detpartida = "+ listDetpartida.get(0).getString(DETPARTIDABASE_TIPO));

            String tipodetpartida = listDetpartida.get(position).getString(DETPARTIDABASE_TIPO);
            detpartidaViewHolder.tipo.setText(tipodetpartida.toUpperCase());
            detpartidaViewHolder.nombre.setText(listDetpartida.get(position).getString(DETPARTIDABASE_NOMBRE));
            detpartidaViewHolder.tiempo.setText(listDetpartida.get(position).getString(DETPARTIDABASE_TIEMPO));
            if (listDetpartida.get(position).getString(DETPARTIDABASE_TIPO).equals(CommonPry.TiposDetPartida.TIPOTRABAJO)) {
                detpartidaViewHolder.importe.setText(JavaUtil.formatoMonedaLocal(
                        (listDetpartida.get(position).getDouble(DETPARTIDABASE_TIEMPO)*CommonPry.hora)));
            }else{
                detpartidaViewHolder.importe.setText(listDetpartida.get(position).getString(DETPARTIDABASE_PRECIO));
            }
            if (listDetpartida.get(position).getString(DETPARTIDABASE_RUTAFOTO)!=null) {
                if (tipodetpartida.equals(TIPOPRODUCTOPROV)) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference spaceRef = storageRef.child(listDetpartida.get(position).getString(DETPARTIDABASE_RUTAFOTO));
                    //GlideApp.with(context)
                    //        .load(spaceRef)
                    //        .into(detpartidaViewHolder.imagenTarea);
                } else {
                    detpartidaViewHolder.imagen.setImageURI(listDetpartida.get(position).getUri(DETPARTIDABASE_RUTAFOTO));
                }
            }

            if (!tipodetpartida.equals(TIPOTRABAJO)){

                detpartidaViewHolder.ltiempo.setVisibility(View.GONE);
                detpartidaViewHolder.tiempo.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {

            return listDetpartida.size();
        }

        @Override
        public void onClick(View v) {

            if (listener != null) {

                listener.onClick(v);


            }

        }

        class DetpartidaViewHolder extends RecyclerView.ViewHolder {

            TextView tipo,nombre,ltiempo,limporte,tiempo,importe;
            ImageView imagen;

            DetpartidaViewHolder(@NonNull View itemView) {
                super(itemView);

                tipo = itemView.findViewById(R.id.tvtipoldetpaetidabase);
                nombre = itemView.findViewById(R.id.tvnomldetpartidabase);
                ltiempo = itemView.findViewById(R.id.ltiempoldetpartidabase);
                limporte = itemView.findViewById(R.id.limpldetpartidabase);
                tiempo = itemView.findViewById(R.id.tvtiempoldetpartidabase);
                importe = itemView.findViewById(R.id.tvimpldetpartidabase);
                imagen = itemView.findViewById(R.id.imgldetpartidabase);


            }
        }
    }

    public class ViewHolderDetPartida extends BaseViewHolder implements TipoViewHolder {

        TextView tipo,nombre,ltiempo,lcantidad,limporte,tiempo,cantidad,importe;
        ImageView imagen;

        public ViewHolderDetPartida(View itemView) {
            super(itemView);
            tipo = itemView.findViewById(R.id.tvtipoldetpaetida);
            nombre = itemView.findViewById(R.id.tvnomldetpartida);
            ltiempo = itemView.findViewById(R.id.ltiempoldetpartida);
            lcantidad = itemView.findViewById(R.id.lcantldetpartida);
            limporte = itemView.findViewById(R.id.limpldetpartida);
            tiempo = itemView.findViewById(R.id.tvtiempoldetpartida);
            cantidad = itemView.findViewById(R.id.tvcantldetpartida);
            importe = itemView.findViewById(R.id.tvimpldetpartida);
            imagen = itemView.findViewById(R.id.imgldetpartida);
        }

        @Override
        public void bind(Modelo modelo) {

            String tipodetpartida = modelo.getString(DETPARTIDABASE_TIPO);
            MediaUtil imagenUtil = new MediaUtil(contexto);

            tipo.setText(tipodetpartida.toUpperCase());
            nombre.setText(modelo.getString(DETPARTIDABASE_NOMBRE));
            tiempo.setText(modelo.getString(DETPARTIDABASE_TIEMPO));
            cantidad.setText(modelo.getString(DETPARTIDABASE_CANTIDAD));
            if (modelo.getString(DETPARTIDABASE_TIPO).equals(CommonPry.TiposDetPartida.TIPOTRABAJO)) {
                importe.setText(JavaUtil.formatoMonedaLocal(
                        (modelo.getDouble(DETPARTIDABASE_TIEMPO)*CommonPry.hora*
                                modelo.getDouble(DETPARTIDABASE_CANTIDAD))));
            }else{
                importe.setText(modelo.getString(DETPARTIDABASE_PRECIO));
            }
            if (modelo.getString(DETPARTIDABASE_RUTAFOTO)!=null) {
                if (tipodetpartida.equals(TIPOPRODUCTOPROV)) {
                    imagenUtil.setImageFireStoreCircle(modelo.getString(DETPARTIDABASE_RUTAFOTO),
                            imagen);
                } else {
                    imagenUtil.setImageUriCircle(modelo.getString(DETPARTIDABASE_RUTAFOTO),imagen);
                }
            }

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

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        ImageView imagenPartida;
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

            descripcionPartida.setText(modelo.getString(PARTIDABASE_DESCRIPCION));
            tiempoPartida.setText(modelo.getString(PARTIDABASE_TIEMPO));
            importePartida.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(PARTIDABASE_PRECIO)));

            if (modelo.getString(PARTIDABASE_RUTAFOTO)!=null){

                MediaUtil imagenUtil = new MediaUtil(AppActivity.getAppContext());
                imagenUtil.setImageUriCircle(modelo.getString(PARTIDABASE_RUTAFOTO),imagenPartida);
            }


            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

        private void setAdaptadorAuto(AutoCompleteTextView autoCompleteTextView) {

        ListaModelo lista = CRUDutil.setListaModelo(campos);
            ArrayList<Modelo> listaPartidasProy = queryList(CAMPOS_PARTIDA);
            lista.addAllLista(listaPartidasProy);

            AdaptadorFiltroModeloPartidas adaptadorPartida = new AdaptadorFiltroModeloPartidas(contexto,
                    layoutItem,lista.getLista(),campos);
            autoCompleteTextView.setAdapter(adaptadorPartida);

    }

    public class AdaptadorFiltroModeloPartidas extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModeloPartidas(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<Modelo> entrada) {

            ImageView imagen = view.findViewById(R.id.imglpartida);
            TextView descripcion = view.findViewById(R.id.tvdescripcionpartida);
            TextView tiempo = view.findViewById(R.id.tvtiempopartida);
            TextView cantidad = view.findViewById(R.id.tvcantidadpartida);
            TextView importe = view.findViewById(R.id.tvimppartida);

            String tabla = entrada.get(posicion).getNombreTabla();
            System.out.println("tablaModelo = " + tabla);

            if (tabla.equals(TABLA_PARTIDA)) {

                descripcion.setText(entrada.get(posicion).getString(PARTIDA_DESCRIPCION));
                tiempo.setText(entrada.get(posicion).getString(PARTIDA_TIEMPO));
                cantidad.setText(entrada.get(posicion).getString(PARTIDA_CANTIDAD));
                importe.setText(entrada.get(posicion).getString(PARTIDA_PRECIO));

                if (entrada.get(posicion).getString(PARTIDA_RUTAFOTO) != null) {
                    imagen.setImageURI(entrada.get(posicion).getUri(PARTIDA_RUTAFOTO));
                }
            }else if (tabla.equals(TABLA_PARTIDABASE)){

                descripcion.setText(entrada.get(posicion).getString(PARTIDABASE_DESCRIPCION));
                tiempo.setText(entrada.get(posicion).getString(PARTIDABASE_TIEMPO));
                importe.setText(entrada.get(posicion).getString(PARTIDABASE_PRECIO));

                if (entrada.get(posicion).getString(PARTIDABASE_RUTAFOTO) != null) {
                    imagen.setImageURI(entrada.get(posicion).getUri(PARTIDABASE_RUTAFOTO));
                }

            }

            super.setEntradas(posicion, view, entrada);
        }
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {


        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<Modelo> entrada) {

            ImageView imagen = view.findViewById(R.id.imglpartida);
            TextView descripcion = view.findViewById(R.id.tvdescripcionpartida);
            TextView tiempo = view.findViewById(R.id.tvtiempopartida);
            TextView cantidad = view.findViewById(R.id.tvcantidadpartida);
            TextView importe = view.findViewById(R.id.tvimppartida);

                descripcion.setText(entrada.get(posicion).getString(PARTIDABASE_DESCRIPCION));
                tiempo.setText(entrada.get(posicion).getString(PARTIDABASE_TIEMPO));
                importe.setText(entrada.get(posicion).getString(PARTIDABASE_PRECIO));

                if (entrada.get(posicion).getString(PARTIDABASE_RUTAFOTO) != null) {
                    imagen.setImageURI(entrada.get(posicion).getUri(PARTIDABASE_RUTAFOTO));
                }


            super.setEntradas(posicion, view, entrada);
        }
    }


        public abstract class ListaAdaptadorFiltroPartidas extends ArrayAdapter<Modelo> {

        private ArrayList<Modelo> entradas;
        private ArrayList<Modelo> entradasfiltro;
        private int R_layout_IdView;
        private Context contexto;

        ListaAdaptadorFiltroPartidas(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas) {
            super(contexto,R_layout_IdView,entradas);
            this.contexto = contexto;
            this.entradas = entradas;
            this.entradasfiltro = new ArrayList<>(entradas);
            this.R_layout_IdView = R_layout_IdView;
        }

        @NonNull
        @Override
        public View getView(int posicion, View view, @NonNull ViewGroup pariente) {
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
                    List<Modelo> suggestion = new ArrayList<>();
                    if (constraint != null) {

                        for (Modelo item :entradas) {

                            String tabla = item.getNombreTabla();
                            System.out.println("tablaModelo = " + tabla);

                            if (tabla.equals(TABLA_PARTIDABASE)){

                                if(item.getCampos(PARTIDABASE_NOMBRE)!=null &&
                                        item.getCampos(PARTIDABASE_NOMBRE).toLowerCase()
                                                .contains(constraint.toString().toLowerCase())){

                                    suggestion.add(item);
                                }else if(item.getCampos(PARTIDABASE_DESCRIPCION)!=null &&
                                        item.getCampos(PARTIDABASE_DESCRIPCION).toLowerCase()
                                                .contains(constraint.toString().toLowerCase())){

                                    suggestion.add(item);
                                }

                            }else if (tabla.equals(TABLA_PARTIDA)){

                                if(item.getCampos(PARTIDA_NOMBRE)!=null &&
                                        item.getCampos(PARTIDA_NOMBRE).toLowerCase()
                                                .contains(constraint.toString().toLowerCase())){

                                    suggestion.add(item);

                                }else if(item.getCampos(PARTIDA_DESCRIPCION)!=null &&
                                        item.getCampos(PARTIDA_DESCRIPCION).toLowerCase()
                                                .contains(constraint.toString().toLowerCase())){

                                    suggestion.add(item);
                                }
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
                        for (Modelo item : (List<Modelo>) results.values) {
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
        public Modelo getItem(int posicion) {
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
        public abstract void onEntrada (Modelo entrada, View view);
    }


}
