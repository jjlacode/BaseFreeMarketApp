package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.android.AppActivity;
import jjlacode.com.freelanceproject.util.android.controls.EditMaterial;
import jjlacode.com.freelanceproject.util.crud.CRUDutil;
import jjlacode.com.freelanceproject.util.crud.FragmentCRUD;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.media.MediaUtil;

import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.insertRegistroDetalle;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.putDato;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.queryList;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.queryListDetalle;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.queryObjectDetalle;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.updateRegistroDetalle;

public class FragmentCRUDPartidaProyecto extends FragmentCRUD implements CommonPry.Constantes,
        ContratoPry.Tablas, CommonPry.TiposDetPartida, CommonPry.TiposEstados {

    private Long retraso;
    private EditMaterial nombrePartida;
    private AutoCompleteTextView autoClonarPartida;
    private EditMaterial descripcionPartida;
    private EditMaterial tiempoPartida;
    private EditMaterial importePartida;
    private EditMaterial cantidadPartida;
    private EditMaterial completadaPartida;
    private Button btnNuevaTrabajo;
    private Button btnNuevoProd;
    private Button btnNuevoProdProv;
    private Button btnNuevaPartida;
    private Button btnNuevaPartidaBase;
    private LinearLayout lyImagen;

    private ImageView imagenret;
    private ImageView buscar;
    private RecyclerView rvdetalles;
    private ProgressBar progressBarPartida;
    private ArrayList<Modelo> listaDetpartidas;

    private Modelo proyecto;
    private String idDetPartida;
    private String idPartida;
    private Modelo partida;
    private Uri uri;
    private Button btnVolverProy;
    private double completada;
    private double tiempo;
    private double tiemporeal;
    private int secuenciatemp;
    private boolean clonada;
    private String idpartidabase;

    public FragmentCRUDPartidaProyecto() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {

        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroRV setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroRV(context, layoutItem, lista, campos);
    }

    @Override
    protected void setLista() {

        System.out.println("proyecto = " + proyecto);
        System.out.println("id = " + id);

        if (id!=null || proyecto!=null) {
            visible(btnVolverProy);
            visible(viewCabecera);
        }

    }

    @Override
    protected void setAcciones() {

        btnNuevaTrabajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                modelo = queryObjectDetalle(CAMPOS_PARTIDA, id, secuencia);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO, proyecto);
                bundle.putSerializable(TABLA_PARTIDA, modelo);
                bundle.putString(CAMPO_ID, modelo.getString(PARTIDA_ID_PARTIDA));
                System.out.println("id nueva tarea= " + modelo.getString(PARTIDA_ID_PARTIDA));
                bundle.putString(ORIGEN, PARTIDA);
                bundle.putString(SUBTITULO, proyecto.getString(PROYECTO_NOMBRE));
                bundle.putString(TIPO, TIPOTRABAJO);
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaTrabajo());

            }

        });

        btnNuevoProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                modelo = queryObjectDetalle(CAMPOS_PARTIDA, id, secuencia);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO, proyecto);
                bundle.putSerializable(TABLA_PARTIDA, modelo);
                bundle.putString(CAMPO_ID, modelo.getString(PARTIDA_ID_PARTIDA));
                bundle.putString(ORIGEN, PARTIDA);
                bundle.putString(SUBTITULO, subTitulo);
                bundle.putString(TIPO, TIPOPRODUCTO);
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaProducto());

            }

        });

        btnNuevoProdProv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                modelo = queryObjectDetalle(CAMPOS_PARTIDA, id, secuencia);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO, proyecto);
                bundle.putSerializable(TABLA_PARTIDA, modelo);
                bundle.putString(CAMPO_ID, modelo.getString(PARTIDA_ID_PARTIDA));
                bundle.putString(ORIGEN, PARTIDA);
                bundle.putString(SUBTITULO, subTitulo);
                bundle.putString(TIPO, TIPOPRODUCTOPROV);
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaProdProvCat());

            }

        });

        btnNuevaPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                modelo = queryObjectDetalle(CAMPOS_PARTIDA, id, secuencia);
                bundle = new Bundle();
                bundle.putSerializable(PROYECTO, proyecto);
                bundle.putSerializable(PARTIDA, modelo);
                bundle.putString(CAMPO_ID, modelo.getString(PARTIDA_ID_PARTIDA));
                bundle.putString(ORIGEN, PARTIDA);
                bundle.putString(SUBTITULO, subTitulo);
                bundle.putString(TIPO, TIPOPARTIDA);
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaTrabajo());

            }

        });

        btnVolverProy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonPry.Calculos.TareaActualizaProy().execute(id);
                System.out.println("origen = " + origen);
                bundle.putSerializable(MODELO, proyecto);
                bundle.putString(ACTUAL,origen);
                bundle.putString(ACTUALTEMP,origen);
                bundle.putString(ORIGEN,PARTIDA);
                System.out.println("id = " + id);
                bundle.putString(CAMPO_ID,id);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
            }
        });

        btnNuevaPartidaBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle.putString(ORIGEN,PARTIDA);
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaBase());
            }
        });

    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.partida;
        tituloPlural = R.string.partidas;
        tituloNuevo = R.string.nueva_partida;
    }


    @Override
    protected void setNuevo() {

        allGone();
        visible(autoClonarPartida);
        visible(btnNuevaPartidaBase);
        visible(nombrePartida);
        visible(descripcionPartida);
        visible(cantidadPartida);

        new CommonPry.Calculos.TareaSincronizarPartidasBase().execute();

        setAdaptadorAuto(autoClonarPartida);

        autoClonarPartida.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Modelo clon = (Modelo) autoClonarPartida.getAdapter().getItem(position);

                clonarPartidaBase(clon);

            }

        });

        if (idpartidabase!=null){

            gone(autoClonarPartida);
            clonarPartidaBase(CRUDutil.setModelo(CAMPOS_PARTIDABASE,idpartidabase));
        }


    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_partida_proyecto;
        layoutCabecera = R.layout.cabecera_crud_partida;
        layoutItem = R.layout.item_list_partida;

    }

    @Override
    protected void setInicio() {

        nombrePartida = (EditMaterial) ctrl(R.id.etnomudpartida);
        autoClonarPartida = (AutoCompleteTextView) ctrl(R.id.autonomudpartida);
        descripcionPartida = (EditMaterial) ctrl(R.id.etdescripcionUDpartida);
        tiempoPartida = (EditMaterial) ctrl(R.id.ettiempoUDpartida);
        importePartida = (EditMaterial) ctrl(R.id.etprecioUDpartida);
        cantidadPartida = (EditMaterial) ctrl(R.id.etcantidadUDpartida);
        completadaPartida = (EditMaterial) ctrl(R.id.etcompletadaUDpartida);
        btnNuevaPartidaBase = (Button) ctrl(R.id.btn_npartidabase);
        btnNuevaTrabajo = (Button) ctrl(R.id.btntareaudpartida);
        btnNuevoProd = (Button) ctrl(R.id.btnprodudpartida);
        btnNuevoProdProv = (Button) ctrl(R.id.btnprovudpartida);
        btnNuevaPartida = (Button) ctrl(R.id.btnpartudpartida);
        progressBarPartida = (ProgressBar) ctrl(R.id.progressBarUDpartida);
        imagen = (ImageView) ctrl(R.id.imgudpartida);
        imagenret = (ImageView) ctrl(R.id.imgretudpartida);
        buscar = (ImageView) ctrl(R.id.imgbuscarpartida);
        rvdetalles = (RecyclerView) ctrl(R.id.rvdetalleUDpartida);
        btnVolverProy = (Button) ctrl(R.id.btn_volverproy);
        lyImagen = (LinearLayout) ctrl(R.id.lyimgpartidaproy);
        gone(btnVolverProy);
        gone(btnNuevaPartida);
        tiempoPartida.setActivo(false);
        importePartida.setActivo(false);

    }



    @Override
    protected void setTabla() {

        tabla = TABLA_PARTIDA;

    }

    @Override
    protected void setBundle() {

        System.out.println("bundle = " + bundle);

        idpartidabase = getStringBundle(IDREL,null);
        proyecto = (Modelo) bundle.getSerializable(PROYECTO);
        System.out.println("proyecto = " + proyecto);
        activityBase.toolbar.setSubtitle(proyecto.getString(PROYECTO_NOMBRE));

    }

    @Override
    protected void setDatos() {

        visible(lyImagen);
        visible(imagen);
        tiempoPartida.setVisibility(View.VISIBLE);
        importePartida.setVisibility(View.VISIBLE);
        completadaPartida.setVisibility(View.VISIBLE);
        btndelete.setVisibility(View.VISIBLE);
        nombrePartida.setVisibility(View.VISIBLE);
        rvdetalles.setVisibility(View.VISIBLE);
        btnNuevaTrabajo.setVisibility(View.VISIBLE);
        btnNuevoProd.setVisibility(View.VISIBLE);
        btnNuevoProdProv.setVisibility(View.VISIBLE);
        //btnNuevoProdProv.setVisibility(View.GONE);
        //btnNuevaPartida.setVisibility(View.VISIBLE);
        progressBarPartida.setVisibility(View.VISIBLE);
        imagenret.setVisibility(View.VISIBLE);
        btnNuevaPartidaBase.setVisibility(View.GONE);
        autoClonarPartida.setVisibility(View.GONE);
        gone(buscar);
        tiempo = (modelo.getDouble(PARTIDA_TIEMPO)*HORASLONG)/1000;
        completada = modelo.getDouble(PARTIDA_COMPLETADA);
        completadaPartida.setText(JavaUtil.getDecimales(completada));
        tiemporeal = (modelo.getDouble(PARTIDA_TIEMPOREAL)*HORASLONG)/1000;
        secuenciatemp = secuencia;


        if (getTipoEstado(modelo.getString(PARTIDA_ID_ESTADO))<=TPRESUPACEPTADO) {

            progressBarPartida.setVisibility(View.GONE);
            completadaPartida.setVisibility(View.GONE);
        }else{

            progressBarPartida.setVisibility(View.VISIBLE);
            completadaPartida.setVisibility(View.VISIBLE);

            if (modelo.getInt(PARTIDA_COMPLETA) == 1) {
                tiempoPartida.setText(JavaUtil.getDecimales(modelo.getDouble(PARTIDA_TIEMPOREAL)));
            }

        }


        nombrePartida.setText(modelo.getString(PARTIDA_NOMBRE));
        descripcionPartida.setText(modelo.getString(PARTIDA_DESCRIPCION));
        tiempoPartida.setText(JavaUtil.getDecimales(modelo.getDouble(PARTIDA_TIEMPO)*
                modelo.getDouble(PARTIDA_CANTIDAD)));
        importePartida.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(PARTIDA_PRECIO)*
                modelo.getDouble(PARTIDA_CANTIDAD)));
        cantidadPartida.setText(modelo.getString(PARTIDA_CANTIDAD));
        //completadaPartida.setText(modelo.getString(PARTIDA_COMPLETADA));
        //progressBarPartida.setProgress(modelo.getInt(PARTIDA_COMPLETADA));
        idDetPartida = modelo.getString(PARTIDA_ID_PARTIDA);

        retraso = modelo.getLong(PARTIDA_PROYECTO_RETRASO);
        if (retraso < 1) {
            imagenret.setImageResource(R.drawable.alert_box_r);
        } else if (retraso < 3) {
            imagenret.setImageResource(R.drawable.alert_box_a);
        } else {
            imagenret.setImageResource(R.drawable.alert_box_v);
        }

        listaDetpartidas = queryListDetalle(CAMPOS_DETPARTIDA, idDetPartida, tabla);

        if (listaDetpartidas != null && listaDetpartidas.size() > 0) {

            rvdetalles.setLayoutManager(new LinearLayoutManager(getContext()));

            AdaptadorDetpartida adapter = new AdaptadorDetpartida(listaDetpartidas, actual);

            rvdetalles.setAdapter(adapter);

                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        idDetPartida = (listaDetpartidas.get(rvdetalles.getChildAdapterPosition(v)).
                                getString(DETPARTIDA_ID_PARTIDA));
                        int secuenciadetpartida = (listaDetpartidas.get(rvdetalles.getChildAdapterPosition(v)).
                                getInt(DETPARTIDA_SECUENCIA));
                        String tipo = (listaDetpartidas.get(rvdetalles.getChildAdapterPosition(v)).
                                getString(DETPARTIDA_TIPO));
                        Modelo detpartida = listaDetpartidas.get(rvdetalles.getChildAdapterPosition(v));

                        bundle = new Bundle();
                        bundle.putSerializable(TABLA_PROYECTO, proyecto);
                        bundle.putSerializable(TABLA_PARTIDA, modelo);
                        bundle.putSerializable(MODELO, detpartida);
                        bundle.putString(CAMPO_ID, idDetPartida);
                        bundle.putInt(CAMPO_SECUENCIA, secuenciadetpartida);
                        bundle.putString(ORIGEN, PARTIDA);
                        bundle.putString(SUBTITULO, subTitulo);
                        bundle.putString(TIPO, tipo);
                        switch (tipo) {

                            case TIPOTRABAJO:
                                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaTrabajo());
                                break;
                            case TIPOPRODUCTO:
                                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaProducto());
                                break;
                            case TIPOPRODUCTOPROV:
                                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaProdProvCat());
                                break;
                            case TIPOPARTIDA:
                                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaTrabajo());
                                break;

                        }
                    }
                });

        } else {

            rvdetalles.setVisibility(View.GONE);
        }


    }

    @Override
    protected void setcambioFragment() {

        new CommonPry.Calculos.TareaActualizaProy().execute(id);

    }


    @Override
    protected boolean delete() {

        new CommonPry.Calculos.Tareafechas().execute();
        new CommonPry.Calculos.TareaActualizaProy().execute(id);

        return super.delete();
    }

    @Override
    protected void setContenedor() {

        setDato(PARTIDA_NOMBRE, nombrePartida.getText().toString());
        setDato(PARTIDA_DESCRIPCION, descripcionPartida.getText().toString());
        setDato(PARTIDA_TIEMPO, JavaUtil.comprobarDouble(tiempoPartida.getText().toString()));
        setDato(PARTIDA_PRECIO, JavaUtil.comprobarDouble(importePartida.getText().toString()));
        setDato(PARTIDA_CANTIDAD, JavaUtil.comprobarDouble(cantidadPartida.getText().toString()));
        setDato(PARTIDA_COMPLETADA, JavaUtil.comprobarDouble(completadaPartida.getText().toString()));
        setDato(PARTIDA_RUTAFOTO, path);
        setDato(PARTIDA_ID_PROYECTO, id);
        System.out.println("id = " + id);
        System.out.println("idDetPartida = " + idDetPartida);

        if (idDetPartida == null ) {
            System.out.println("Generar idetPartida");
            idDetPartida = ContratoPry.generarIdTabla(tabla);
            System.out.println("idDetPartida = " + idDetPartida);
        }
        if (secuencia==0) {

            setDato(PARTIDA_ID_PARTIDA, idDetPartida);
            setDato(PARTIDA_ID_ESTADO, proyecto.getString(PROYECTO_ID_ESTADO));

        }


    }

    private int getTipoEstado(String idEstado){

        if (idEstado!=null) {

            ArrayList<Modelo> listaEstados = queryList(CAMPOS_ESTADO, null, null);

            for (Modelo estado : listaEstados) {

                if (estado.getString(ESTADO_ID_ESTADO).equals(idEstado)) {

                    return estado.getInt(ESTADO_TIPOESTADO);
                }

            }
        }
        return 0;
    }

    @Override
    protected boolean update() {

        new CommonPry.Calculos.Tareafechas().execute();
        new CommonPry.Calculos.TareaActualizaProy().execute(id);

        super.update();


            valores = new ContentValues();
            putDato(valores, campos, PARTIDA_ID_PARTIDABASE, idpartidabase);
            updateRegistroDetalle(tabla, id, secuencia, valores);
            modelo = queryObjectDetalle(campos,id,secuencia);

        if (proyecto==null && id!=null){
            proyecto = CRUDutil.setModelo(CAMPOS_PROYECTO,id);
        }
        if (proyecto!=null && id!=null && CommonPry.getTipoEstado(proyecto.getString(PROYECTO_ID_ESTADO))>=TPRESUPPENDENTREGA){
            new FragmentCRUDProyecto.TareaGenerarPdf().execute(id);
        }
        return true;
    }


    public class AdaptadorDetpartida extends RecyclerView.Adapter<AdaptadorDetpartida.DetpartidaViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas, CommonPry.TiposDetPartida {

        private ArrayList<Modelo> listDetpartida;
        private View.OnClickListener listener;
        private String namef;
        private Context context = AppActivity.getAppContext();

        public AdaptadorDetpartida(ArrayList<Modelo> listDetpartida, String namef) {

            this.listDetpartida = listDetpartida;
            this.namef = namef;
        }

        @NonNull
        @Override
        public DetpartidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_detpartida, null, false);

            view.setOnClickListener(this);


            return new DetpartidaViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull DetpartidaViewHolder detpartidaViewHolder, int position) {

            String tipodetpartida = listDetpartida.get(position).getString(DETPARTIDA_TIPO);
            detpartidaViewHolder.tipo.setText(tipodetpartida.toUpperCase());
            detpartidaViewHolder.nombre.setText(listDetpartida.get(position).getString(DETPARTIDA_NOMBRE));
            detpartidaViewHolder.tiempo.setText(listDetpartida.get(position).getString(DETPARTIDA_TIEMPO));
            if (tipodetpartida.equals(TIPOTRABAJO)) {
                detpartidaViewHolder.cantidad.setText(JavaUtil.getDecimales(
                        Double.parseDouble(listDetpartida.get(position).
                                getString(DETPARTIDA_TIEMPO)) * Double.parseDouble(cantidadPartida.getText().toString())));
            } else {
                detpartidaViewHolder.cantidad.setText(JavaUtil.getDecimales(
                        Double.parseDouble(listDetpartida.get(position).
                                getString(DETPARTIDA_CANTIDAD)) * Double.parseDouble(cantidadPartida.getText().toString())));

            }
            if (listDetpartida.get(position).getString(DETPARTIDA_TIPO).equals(TIPOTRABAJO)) {
                detpartidaViewHolder.importe.setText(JavaUtil.formatoMonedaLocal(
                        (listDetpartida.get(position).getDouble(DETPARTIDA_TIEMPO) * CommonPry.hora * Double.parseDouble(cantidadPartida.getText().toString()))));
            } else {
                detpartidaViewHolder.importe.setText(JavaUtil.formatoMonedaLocal(Double.parseDouble(
                        listDetpartida.get(position).getString(DETPARTIDA_PRECIO)) * Double.parseDouble(cantidadPartida.getText().toString())));
            }
            if (listDetpartida.get(position).getString(DETPARTIDA_RUTAFOTO) != null) {
                if (tipodetpartida.equals(TIPOPRODUCTOPROV)) {
                    MediaUtil imagenUtil = new MediaUtil(context);
                    imagenUtil.setImageFireStoreCircle(listDetpartida.get(position).getString(DETPARTIDA_RUTAFOTO), detpartidaViewHolder.imagen);

                } else {
                    detpartidaViewHolder.imagen.setImageURI(listDetpartida.get(position).getUri(DETPARTIDA_RUTAFOTO));
                }
            }

            if (!tipodetpartida.equals(TIPOTRABAJO)) {

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

            TextView tipo, nombre, ltiempo, lcantidad, limporte, tiempo, cantidad, importe;
            ImageView imagen;

            DetpartidaViewHolder(@NonNull View itemView) {
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
        }
    }

    private void setAdaptadorAuto(AutoCompleteTextView autoCompleteTextView) {


        ArrayList<Modelo> lista = queryList(CAMPOS_PARTIDABASE);
        //ArrayList<Modelo> listabase = consulta.queryList(CAMPOS_PARTIDABASE);
        //lista.addAllLista(listabase);


        autoCompleteTextView.setAdapter(new ListaAdaptadorFiltroPartidas(getContext(),
                R.layout.item_list_partidabase, lista) {
            @Override
            public void onEntrada(Modelo entrada, View view) {

                ImageView imagen = view.findViewById(R.id.imglpartidabase);
                TextView descripcion = view.findViewById(R.id.tvdescripcionpartidabase);
                TextView tiempo = view.findViewById(R.id.tvtiempopartidabase);
                TextView importe = view.findViewById(R.id.tvimppartidabase);
                TextView ltiempo = view.findViewById(R.id.lhoraspartidabase);
                TextView limporte = view.findViewById(R.id.limppartidabase);


                descripcion.setText(entrada.getString(PARTIDABASE_DESCRIPCION));
                tiempo.setText(entrada.getString(PARTIDABASE_TIEMPO));
                importe.setText(JavaUtil.formatoMonedaLocal(entrada.getDouble(PARTIDABASE_PRECIO)));
                ltiempo.setText("h.");
                limporte.setVisibility(View.GONE);

                if (entrada.getString(PARTIDABASE_RUTAFOTO) != null) {
                    MediaUtil imagenUtil = new MediaUtil(contexto);
                    imagenUtil.setImageUriCircle(entrada.getString(PARTIDABASE_RUTAFOTO), imagen);
                    //imagenTarea.setImageURI(entrada.getUri(PARTIDABASE_RUTAFOTO));
                }

            }

        });

    }

    public abstract class ListaAdaptadorFiltroPartidas extends ArrayAdapter<Modelo> {

        private ArrayList<Modelo> entradas;
        private ArrayList<Modelo> entradasfiltro;
        private int R_layout_IdView;
        private Context contexto;

        ListaAdaptadorFiltroPartidas(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas) {
            super(contexto, R_layout_IdView, entradas);
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
            onEntrada(entradasfiltro.get(posicion), view);
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

                        for (Modelo item : entradas) {

                                if (item.getCampos(PARTIDABASE_NOMBRE).toLowerCase().contains(constraint.toString().toLowerCase())) {

                                    suggestion.add(item);
                                } else if (item.getCampos(PARTIDABASE_DESCRIPCION).toLowerCase().contains(constraint.toString().toLowerCase())) {

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

        /**
         * Devuelve cada una de las entradas con cada una de las vistas a la que debe de ser asociada
         *
         * @param entrada La entrada que será la asociada a la view. La entrada es del tipo del paquete/handler
         * @param view    View particular que contendrá los datos del paquete/handler
         */
        public abstract void onEntrada(Modelo entrada, View view);
    }

    private void clonarPartidaBase(Modelo modelo) {


        valores = new ContentValues();
        String idPartidaBase = modelo.getString(PARTIDABASE_ID_PARTIDABASE);
        valores.put(PARTIDA_ID_PARTIDABASE, idPartidaBase);
        valores.put(PARTIDA_CANTIDAD, 0);
        valores.put(PARTIDA_DESCRIPCION, modelo.getString(PARTIDABASE_DESCRIPCION));
        valores.put(PARTIDA_NOMBRE, modelo.getString(PARTIDABASE_NOMBRE));
        valores.put(PARTIDA_PRECIO, modelo.getString(PARTIDABASE_PRECIO));
        valores.put(PARTIDA_TIEMPO, modelo.getString(PARTIDABASE_TIEMPO));
        valores.put(PARTIDA_RUTAFOTO, modelo.getString(PARTIDABASE_RUTAFOTO));
        valores.put(PARTIDA_ID_PROYECTO, id);
        valores.put(PARTIDA_ID_ESTADO, proyecto.getString(PROYECTO_ID_ESTADO));
        valores.put(PARTIDA_ID_PARTIDA, idPartida);

        uri = insertRegistroDetalle(CAMPOS_PARTIDA, id,
                TABLA_PROYECTO, valores);
        System.out.println("uri = " + uri);

        partida = queryObjectDetalle(CAMPOS_PARTIDA, uri);
        secuencia = partida.getInt(PARTIDA_SECUENCIA);

        nombrePartida.setText(partida.getString(PARTIDA_NOMBRE));
        descripcionPartida.setText(partida.getString(PARTIDA_DESCRIPCION));

        if (partida.getString(PARTIDA_RUTAFOTO) != null) {
            setImagenUri(contexto, partida.getString(PARTIDA_RUTAFOTO));
            path = partida.getString(PARTIDA_RUTAFOTO);
        }


        ArrayList<Modelo> listaclon = queryListDetalle
                (CAMPOS_DETPARTIDABASE, modelo.getString(PARTIDABASE_ID_PARTIDABASE), TABLA_PARTIDABASE);

        for (Modelo clonpart : listaclon) {

            valores = new ContentValues();
            valores.put(DETPARTIDA_ID_PARTIDA, idPartida);
            valores.put(DETPARTIDA_NOMBRE, clonpart.getString(DETPARTIDABASE_NOMBRE));
            valores.put(DETPARTIDA_DESCRIPCION, clonpart.getString(DETPARTIDABASE_DESCRIPCION));
            valores.put(DETPARTIDA_BENEFICIO, clonpart.getString(DETPARTIDABASE_BENEFICIO));
            valores.put(DETPARTIDA_DESCUENTOPROVCAT, clonpart.getString(DETPARTIDABASE_DESCUENTOPROVCAT));
            valores.put(DETPARTIDA_PRECIO, clonpart.getString(DETPARTIDABASE_PRECIO));
            valores.put(DETPARTIDA_ID_DETPARTIDA, clonpart.getString(DETPARTIDABASE_ID_DETPARTIDABASE));
            valores.put(DETPARTIDA_REFPROVCAT, clonpart.getString(DETPARTIDABASE_REFPROVCAT));
            valores.put(DETPARTIDA_TIEMPO, clonpart.getString(DETPARTIDABASE_TIEMPO));
            valores.put(DETPARTIDA_RUTAFOTO, clonpart.getString(DETPARTIDABASE_RUTAFOTO));
            valores.put(DETPARTIDA_TIPO, clonpart.getString(DETPARTIDABASE_TIPO));
            valores.remove(DETPARTIDABASE_SECUENCIA);

            insertRegistroDetalle(CAMPOS_DETPARTIDA, idPartida
                    , TABLA_PARTIDA, valores);
        }

        this.modelo = queryObjectDetalle(campos, id, secuencia);
        btnNuevaPartidaBase.setVisibility(View.GONE);
        selector();

    }

    /*
    private String actualizarPartidabase(final Modelo clon) {

        String idpartidabase = clon.getString(PARTIDA_ID_PARTIDABASE);
        System.out.println("Actualizando idpartidabase = " + idpartidabase);
        Modelo partidabase = null;

        valores = new ContentValues();

        valores.put(PARTIDABASE_DESCRIPCION, clon.getString(PARTIDA_DESCRIPCION));
        valores.put(PARTIDABASE_ID_PARTIDAORIGEN,clon.getString(PARTIDA_ID_PARTIDA));
        valores.put(PARTIDABASE_NOMBRE, clon.getString(PARTIDA_NOMBRE));
        valores.put(PARTIDABASE_PRECIO, 0);
        valores.put(PARTIDABASE_TIEMPO, clon.getString(PARTIDA_TIEMPO));
        valores.put(PARTIDABASE_RUTAFOTO, clon.getString(PARTIDA_RUTAFOTO));
        valores.put(PARTIDABASE_TIMESTAMP, TimeDateUtil.getDateLong(new GregorianCalendar()));

        if (idpartidabase==null) {
            valores.put(PARTIDABASE_CREATE, TimeDateUtil.getDateLong(new GregorianCalendar()));
            Uri uri = insertRegistro(TABLA_PARTIDABASE,valores);
            partidabase = queryObject(CAMPOS_PARTIDABASE,uri);
            idpartidabase = partidabase.getString(PARTIDABASE_ID_PARTIDABASE);
            System.out.println("Nueva partidabase creada = " + idpartidabase);
        }else{
            updateRegistro(TABLA_PARTIDABASE,idpartidabase,valores);
            partidabase = queryObject(CAMPOS_PARTIDABASE,idpartidabase);
            System.out.println("partidabase actualizada= " + partidabase.getString(PARTIDABASE_ID_PARTIDABASE));
        }

        ArrayList<Modelo> listaclon = queryListDetalle
                (CAMPOS_DETPARTIDA,clon.getString(PARTIDA_ID_PARTIDA),TABLA_PARTIDA);

        for (Modelo clonpart : listaclon) {

            valores = new ContentValues();
            putDato(valores,CAMPOS_DETPARTIDABASE,DETPARTIDABASE_ID_PARTIDABASE,idpartidabase);
            putDato(valores,CAMPOS_DETPARTIDABASE,DETPARTIDABASE_NOMBRE,clonpart.getString(DETPARTIDA_NOMBRE));
            putDato(valores,CAMPOS_DETPARTIDABASE,DETPARTIDABASE_DESCRIPCION,clonpart.getString(DETPARTIDA_DESCRIPCION));
            putDato(valores,CAMPOS_DETPARTIDABASE,DETPARTIDABASE_BENEFICIO,clonpart.getString(DETPARTIDA_BENEFICIO));
            putDato(valores,CAMPOS_DETPARTIDABASE, DETPARTIDABASE_DESCUENTOPROVCAT,clonpart.getString(DETPARTIDA_DESCUENTOPROVCAT));
            putDato(valores,CAMPOS_DETPARTIDABASE,DETPARTIDABASE_PRECIO,clonpart.getString(DETPARTIDA_PRECIO));
            putDato(valores,CAMPOS_DETPARTIDABASE,DETPARTIDABASE_ID_DETPARTIDABASE,clonpart.getString(DETPARTIDA_ID_DETPARTIDA));
            putDato(valores,CAMPOS_DETPARTIDABASE, DETPARTIDABASE_REFPROVCAT,clonpart.getString(DETPARTIDA_REFPROVCAT));
            putDato(valores,CAMPOS_DETPARTIDABASE,DETPARTIDABASE_TIEMPO,clonpart.getString(DETPARTIDA_TIEMPO));
            putDato(valores,CAMPOS_DETPARTIDABASE,DETPARTIDABASE_RUTAFOTO,clonpart.getString(DETPARTIDA_RUTAFOTO));
            putDato(valores,CAMPOS_DETPARTIDABASE,DETPARTIDABASE_TIPO,clonpart.getString(DETPARTIDA_TIPO));
            valores.remove(DETPARTIDA_SECUENCIA);

            ArrayList<Modelo>listaDetallePartidabase = queryListDetalle(CAMPOS_DETPARTIDABASE,idpartidabase,
                    TABLA_PARTIDABASE);

            boolean nuevoRegistro = true;

            for (Modelo detallePartidaBase : listaDetallePartidabase) {

                if (detallePartidaBase.getString(DETPARTIDABASE_ID_DETPARTIDABASE).equals(
                        clonpart.getString(DETPARTIDA_ID_DETPARTIDA))){
                    int res = updateRegistroDetalle(TABLA_DETPARTIDABASE,idpartidabase,
                            detallePartidaBase.getInt(DETPARTIDABASE_SECUENCIA),valores);
                    nuevoRegistro = false;
                    System.out.println("det Partida base actualizadas = " + res);
                    break;
                }
            }
            if (nuevoRegistro) {

                System.out.println("valores = " + valores);
                int res = secInsertRegistroDetalle(CAMPOS_DETPARTIDABASE, idpartidabase
                        , TABLA_PARTIDABASE, valores);
                System.out.println("Nuevo det partidabase creado = " + res);
            }
            CommonPry.Calculos.actualizarPartidaBase(idpartidabase);
        }
         return idpartidabase;
    }
    */


    public class AdaptadorFiltroRV extends ListaAdaptadorFiltroRV {

        public AdaptadorFiltroRV(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<Modelo> entrada) {

            ImageView imagenPartida, imagenret;
            TextView descripcionPartida, tiempoPartida, cantidadPartida, completadaPartida, importePartida;
            ProgressBar progressBarPartida;

            imagenPartida = itemView.findViewById(R.id.imglpartida);
            imagenret = itemView.findViewById(R.id.imgretlpartida);
            descripcionPartida = itemView.findViewById(R.id.tvdescripcionpartida);
            tiempoPartida = itemView.findViewById(R.id.tvtiempopartida);
            cantidadPartida = itemView.findViewById(R.id.tvcantidadpartida);
            importePartida = itemView.findViewById(R.id.tvimppartida);
            completadaPartida = itemView.findViewById(R.id.tvcompletadapartida);
            progressBarPartida = itemView.findViewById(R.id.progressBarpartida);

            descripcionPartida.setText(entrada.get(posicion).getCampos(PARTIDA_DESCRIPCION));
            tiempoPartida.setText(entrada.get(posicion).getCampos(PARTIDA_TIEMPO));
            cantidadPartida.setText(entrada.get(posicion).getCampos(PARTIDA_CANTIDAD));
            importePartida.setText(JavaUtil.formatoMonedaLocal(entrada.get(posicion).getDouble(PARTIDA_PRECIO)));
            completadaPartida.setText(entrada.get(posicion).getCampos(PARTIDA_COMPLETADA));
            progressBarPartida.setProgress(Integer.parseInt(entrada.get(posicion).getCampos(PARTIDA_COMPLETADA)));

            if (entrada.get(posicion).getString(PARTIDA_RUTAFOTO) != null) {

                imagenPartida.setImageURI(entrada.get(posicion).getUri(PARTIDA_RUTAFOTO));
            }

            long retraso = entrada.get(posicion).getLong(PARTIDA_PROYECTO_RETRASO);
            if (retraso > 3 * DIASLONG) {
                imagenret.setImageResource(R.drawable.alert_box_r);
            } else if (retraso > DIASLONG) {
                imagenret.setImageResource(R.drawable.alert_box_a);
            } else {
                imagenret.setImageResource(R.drawable.alert_box_v);
            }

            if (origen.equals(PRESUPUESTO)) {

                progressBarPartida.setVisibility(View.GONE);

            } else {

                progressBarPartida.setVisibility(View.VISIBLE);
            }


            super.setEntradas(posicion, view, entrada);
        }
    }


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        ImageView imagenPartida, imagenret;
        TextView descripcionPartida, tiempoPartida, cantidadPartida, completadaPartida, importePartida;
        ProgressBar progressBarPartida;
        CardView card;

        public ViewHolderRV(View itemView) {
            super(itemView);
            imagenPartida = itemView.findViewById(R.id.imglpartida);
            imagenret = itemView.findViewById(R.id.imgretlpartida);
            descripcionPartida = itemView.findViewById(R.id.tvdescripcionpartida);
            tiempoPartida = itemView.findViewById(R.id.tvtiempopartida);
            cantidadPartida = itemView.findViewById(R.id.tvcantidadpartida);
            importePartida = itemView.findViewById(R.id.tvimppartida);
            completadaPartida = itemView.findViewById(R.id.tvcompletadapartida);
            progressBarPartida = itemView.findViewById(R.id.progressBarpartida);
            card = itemView.findViewById(R.id.cardlpartida);

        }

        @Override
        public void bind(Modelo modelo) {

            descripcionPartida.setText(modelo.getCampos(PARTIDA_DESCRIPCION));
            tiempoPartida.setText(modelo.getCampos(PARTIDA_TIEMPO));
            cantidadPartida.setText(modelo.getCampos(PARTIDA_CANTIDAD));
            importePartida.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(PARTIDA_PRECIO)));
            completadaPartida.setText(JavaUtil.getDecimales(modelo.getDouble(PARTIDA_COMPLETADA)));
            progressBarPartida.setProgress(modelo.getInt(PARTIDA_COMPLETADA));

            if (modelo.getString(PARTIDA_RUTAFOTO) != null) {

                mediaUtil = new MediaUtil(contexto);
                mediaUtil.setImageUriCircle(modelo.getString(PARTIDA_RUTAFOTO),imagenPartida);
            }

            long retraso = modelo.getLong(PARTIDA_PROYECTO_RETRASO);
            if (retraso > 3 * DIASLONG) {
                imagenret.setImageResource(R.drawable.alert_box_r);
            } else if (retraso > DIASLONG) {
                imagenret.setImageResource(R.drawable.alert_box_a);
            } else {
                imagenret.setImageResource(R.drawable.alert_box_v);
            }

            if (origen.equals(PRESUPUESTO)) {

                progressBarPartida.setVisibility(View.GONE);

            } else {

                progressBarPartida.setVisibility(View.VISIBLE);
            }
            if ((getAdapterPosition()+1)%2==0){
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_defecto));
            }

            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }


    }
}
