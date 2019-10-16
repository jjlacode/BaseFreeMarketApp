package com.codevsolution.freemarketsapp.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.base.sqlite.ConsultaBD.queryList;
import static com.codevsolution.base.sqlite.ConsultaBD.queryListDetalle;
import static com.codevsolution.base.sqlite.ConsultaBD.queryObjectDetalle;
import static com.codevsolution.base.sqlite.ConsultaBD.updateRegistroDetalle;

public class FragmentCRUDPartidaProyecto extends FragmentCRUD implements Interactor.ConstantesPry,
        ContratoPry.Tablas, Interactor.TiposDetPartida, Interactor.TiposEstados {

    private Long retraso;
    private EditMaterialLayout nombrePartida;
    private EditMaterialLayout descripcionPartida;
    private EditMaterialLayout tiempoPartida;
    private EditMaterialLayout importePartida;
    private EditMaterialLayout cantidadPartida;
    private EditMaterialLayout completadaPartida;
    private EditMaterialLayout etOrden;
    private EditMaterialLayout etFechaInicioCalculada;
    private EditMaterialLayout etHoraInicioCalculada;
    private EditMaterialLayout etFechaCalculada;

    private Button btnPartidaBase;

    private ImageView imagenret;
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
    private long horaInicioCalculada;
    private long fechaInicioCalculada;
    private long fechaCalculada;

    public FragmentCRUDPartidaProyecto() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {

        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    @Override
    protected void setLista() {

        if (nn(id) || nn(proyecto)) {
            visible(btnVolverProy);
            visible(frCabecera);
            activityBase.toolbar.setSubtitle(proyecto.getString(PROYECTO_NOMBRE));

        }

    }

    @Override
    protected void setAcciones() {

        btnVolverProy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Interactor.Calculos.TareaActualizaProy().execute(id);
                bundle.putSerializable(MODELO, proyecto);
                bundle.putString(ACTUAL,origen);
                bundle.putString(ACTUALTEMP,origen);
                bundle.putString(ORIGEN,PARTIDA);
                bundle.putString(CAMPO_ID,id);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
            }
        });

        btnPartidaBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PARTIDA_ID_PARTIDA, id);
                AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PARTIDA_SECUENCIA, secuencia);

                System.out.println("modelo.getString(PARTIDA_ID_PARTIDABASE) = " + modelo.getString(PARTIDA_ID_PARTIDABASE));
                putBundle(CAMPO_ID, modelo.getString(PARTIDA_ID_PARTIDABASE));
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaBase());
            }
        });

        cantidadPartida.setAlCambiarListener(new EditMaterialLayout.AlCambiarListener() {
            @Override
            public void antesCambio(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void cambiando(CharSequence s, int start, int before, int count) {

                if (nn(timer)){
                    timer.cancel();
                }
            }

            @Override
            public void despuesCambio(Editable s) {

                final Editable temp = s;
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        activityBase.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (!temp.toString().equals("")) {

                                    update();
                                    setRvDetallePartida();
                                }

                            }
                        });


                    }
                }, 2000);

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


        bundle = new Bundle();
        AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PARTIDA_ID_PARTIDA, id);
        AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PARTIDA_SECUENCIA, 0);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaBase());

    }

    @Override
    protected void setLayout() {

        //layoutCuerpo = R.layout.fragment_crud_partida_proyecto;
        //layoutCabecera = R.layout.cabecera_crud_partida;
        layoutItem = R.layout.item_list_partida;
        cabecera = true;
    }

    @Override
    protected void setInicio() {

        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalle);

        imagen = (ImagenLayout) vistaForm.addVista(new ImagenLayout(contexto));
        imagen.setFocusable(false);
        imagen.getImagen().setClickable(false);
        imagen.setTextTitulo(tituloSingular);
        btnPartidaBase = vistaForm.addButtonPrimary(R.string.modificar_partidabase);
        nombrePartida = vistaForm.addEditMaterialLayout(getString(R.string.nombre));
        nombrePartida.setActivo(false);
        nombrePartida.btnInicioVisible(false);
        descripcionPartida = vistaForm.addEditMaterialLayout(getString(R.string.descripcion));
        descripcionPartida.setActivo(false);
        descripcionPartida.btnInicioVisible(false);

        ViewGroupLayout vistaCant = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaCant.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        imagenret = (ImageView) vistaCant.addVista(new ImageView(contexto));
        imagenret.setFocusable(false);
        cantidadPartida = vistaCant.addEditMaterialLayout(getString(R.string.cantidad), PARTIDA_CANTIDAD);
        tiempoPartida = vistaCant.addEditMaterialLayout(R.string.tiempo);
        tiempoPartida.setActivo(false);
        tiempoPartida.btnInicioVisible(false);
        importePartida = vistaCant.addEditMaterialLayout(R.string.importe);
        importePartida.setActivo(false);
        importePartida.btnInicioVisible(false);

        actualizarArrays(vistaCant);

        etOrden = vistaForm.addEditMaterialLayout(R.string.orden_ejecucion);
        ViewGroupLayout vistaAcordada = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaAcordada.setOrientacion(LinearLayoutCompat.HORIZONTAL);
        etFechaInicioCalculada = vistaAcordada.addEditMaterialLayout(R.string.fecha_acordada);
        etFechaInicioCalculada.setActivo(false);
        etFechaInicioCalculada.btnInicioVisible(false);

        etHoraInicioCalculada = vistaAcordada.addEditMaterialLayout(R.string.hora_acordada);
        etHoraInicioCalculada.setActivo(false);
        etHoraInicioCalculada.btnInicioVisible(false);

        actualizarArrays(vistaAcordada);

        etFechaCalculada = vistaForm.addEditMaterialLayout(R.string.fecha_calculada);
        etFechaCalculada.setActivo(false);
        etFechaCalculada.btnInicioVisible(false);
        progressBarPartida = (ProgressBar) vistaForm.addVista(new ProgressBar(contexto, null, R.style.ProgressBarStyleAcept));
        completadaPartida = vistaForm.addEditMaterialLayout(R.string.completada);

        rvdetalles = (RecyclerView) vistaForm.addVista(new RecyclerView(contexto));
        actualizarArrays(vistaForm);

        ViewGroupLayout vistaCab = new ViewGroupLayout(contexto, frCabecera);
        btnVolverProy = vistaCab.addButtonPrimary(R.string.volver_a_proyecto);
        actualizarArrays(vistaCab);

    }



    @Override
    protected void setTabla() {

        tabla = TABLA_PARTIDA;

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

        System.out.println("bundle = " + bundle);
        AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PARTIDA_ID_PARTIDA, NULL);
        AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PARTIDA_SECUENCIA, 0);

        idpartidabase = getStringBundle(IDREL,null);
        proyecto = (Modelo) bundle.getSerializable(PROYECTO);
        System.out.println("proyecto = " + proyecto);
        if (proyecto == null && nn(id)) {
            proyecto = CRUDutil.updateModelo(CAMPOS_PROYECTO, id);
        }
        if (nn(proyecto)) {
            activityBase.toolbar.setSubtitle(proyecto.getString(PROYECTO_NOMBRE));
        }

    }

    @Override
    protected void setDatos() {

        if (nn(proyecto)) {
            activityBase.toolbar.setSubtitle(proyecto.getString(PROYECTO_NOMBRE));
        }
        imagen.setTextTitulo(getString(R.string.partida)+" "+secuencia);
        completadaPartida.getLinearLayout().setVisibility(View.VISIBLE);
        nombrePartida.getLinearLayout().setVisibility(View.VISIBLE);
        rvdetalles.setVisibility(View.VISIBLE);
        progressBarPartida.setVisibility(View.VISIBLE);

        gone(buscar);
        tiempo = (modelo.getDouble(PARTIDA_TIEMPO)*HORASLONG)/1000;
        completada = modelo.getDouble(PARTIDA_COMPLETADA);
        completadaPartida.setText(JavaUtil.getDecimales(completada));
        tiemporeal = (modelo.getDouble(PARTIDA_TIEMPOREAL)*HORASLONG)/1000;
        secuenciatemp = secuencia;
        horaInicioCalculada = modelo.getLong(PARTIDA_HORAINICIOCALCULADA);
        fechaInicioCalculada = modelo.getLong(PARTIDA_HORAINICIOCALCULADA);
        fechaCalculada = modelo.getLong(PARTIDA_FECHAENTREGACALCULADA);

        if (horaInicioCalculada == 0) {
            etHoraInicioCalculada.setText(getString(R.string.sin_asignar));
        } else {
            etHoraInicioCalculada.setText(TimeDateUtil.getTimeString(horaInicioCalculada));
        }

        if (fechaInicioCalculada == 0) {

            etFechaInicioCalculada.setText(getString(R.string.sin_asignar));
            etFechaCalculada.setText(JavaUtil.getDateTime(fechaCalculada));


        } else {

            etFechaCalculada.setText(JavaUtil.getDateTime(fechaCalculada));
            etFechaInicioCalculada.setText(TimeDateUtil.getDateString(fechaInicioCalculada));

        }

        etOrden.setText(String.valueOf(modelo.getInt(DETPARTIDA_ORDEN)));
        int orden = 0;
        if (modelo.getInt(PARTIDA_ORDEN)==0){

            if (lista == null) {
                lista = CRUDutil.setListaModelo(campos);
            }

            if (lista.sizeLista() > 0) {
                for (Modelo partida : lista.getLista()) {
                    if (partida.getInt(PARTIDA_ORDEN) > orden) {
                        orden = partida.getInt(PARTIDA_ORDEN);
                    }
                }
            }
            valores = new ContentValues();
            putDato(valores,campos,PARTIDA_ORDEN,orden+1);
            CRUDutil.actualizarRegistro(modelo,valores);
            modelo = CRUDutil.updateModelo(modelo);
            etOrden.setText(String.valueOf(modelo.getInt(DETPARTIDA_ORDEN)));

        }

        if (getTipoEstado(modelo.getString(PARTIDA_ID_ESTADO))<=TPRESUPACEPTADO) {

            progressBarPartida.setVisibility(View.GONE);
            completadaPartida.getLinearLayout().setVisibility(View.GONE);
        }else{

            progressBarPartida.setVisibility(View.VISIBLE);
            completadaPartida.getLinearLayout().setVisibility(View.VISIBLE);

            if (modelo.getInt(PARTIDA_COMPLETA) == 1) {
                tiempoPartida.setText(JavaUtil.getDecimales(modelo.getDouble(PARTIDA_TIEMPOREAL)));
            }

        }


        nombrePartida.setText(modelo.getString(PARTIDA_NOMBRE));
        descripcionPartida.setText(modelo.getString(PARTIDA_DESCRIPCION));
        tiempoPartida.setText(JavaUtil.getDecimales((modelo.getDouble(PARTIDA_TIEMPO) *
                modelo.getDouble(PARTIDA_CANTIDAD))));
        importePartida.setText(JavaUtil.formatoMonedaLocal((modelo.getDouble(PARTIDA_PRECIO) *
                modelo.getDouble(PARTIDA_CANTIDAD))));
        cantidadPartida.setText(modelo.getString(PARTIDA_CANTIDAD));
        idDetPartida = modelo.getString(PARTIDA_ID_PARTIDA);

        retraso = modelo.getLong(PARTIDA_PROYECTO_RETRASO);
        if (retraso < 1) {
            imagenret.setImageResource(R.drawable.alert_box_r);
        } else if (retraso < 3) {
            imagenret.setImageResource(R.drawable.alert_box_a);
        } else {
            imagenret.setImageResource(R.drawable.alert_box_v);
        }

        setRvDetallePartida();

    }

    private void setRvDetallePartida() {

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


                    }
                }
            });

        } else {

            rvdetalles.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setOnClickRV(Modelo modelo) {
        super.setOnClickRV(modelo);

        if (nn(id) && secuencia > 0) {
            new TareaSincroPartidaProy().execute(id, String.valueOf(secuencia));
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class TareaSincroPartidaProy extends AsyncTask<String, Float, Modelo> {

        @Override
        protected Modelo doInBackground(String... strings) {

            Interactor.Calculos.sincroPartidaBaseToPartidaProy(strings[0], strings[1]);
            return CRUDutil.updateModelo(CAMPOS_PARTIDA, strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(Modelo modelo) {
            super.onPostExecute(modelo);
            new TareaActualizaProdProvPartidaProy().execute(modelo);

        }

    }

    @SuppressLint("StaticFieldLeak")
    public class TareaActualizaProdProvPartidaProy extends AsyncTask<Modelo, Float, Modelo> {

        @Override
        protected Modelo doInBackground(Modelo... modelos) {

            Interactor.Calculos.actualizarProdProvPartidaProy(modelos[0].getString(PARTIDA_ID_PARTIDA));
            return modelos[0];
        }

        @Override
        protected void onPostExecute(Modelo modelo) {
            super.onPostExecute(modelo);
            new TareaActualizaPartidaProy().execute(modelo.getString(PARTIDA_ID_PARTIDA));

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class TareaActualizaPartidaProy extends AsyncTask<String, Float, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {

            Interactor.Calculos.actualizarPartidaProyecto(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            selector();
        }
    }

    @Override
    protected void setcambioFragment() {

        new Interactor.Calculos.TareaActualizaProy().execute(id);

    }


    @Override
    protected boolean delete() {

        new Interactor.Calculos.Tareafechas().execute(false);
        new Interactor.Calculos.TareaActualizaProy().execute(id);

        return super.delete();
    }

    @Override
    protected void setContenedor() {

        setDato(PARTIDA_CANTIDAD, JavaUtil.comprobarDouble(cantidadPartida.getText().toString()));
        setDato(PARTIDA_COMPLETADA, JavaUtil.comprobarDouble(completadaPartida.getText().toString()));
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

        new Interactor.Calculos.Tareafechas().execute(false);
        new Interactor.Calculos.TareaActualizaProy().execute(id);

        if (Double.parseDouble(cantidadPartida.getTexto())==0){
            valores = new ContentValues();
            setDato(PARTIDA_CANTIDAD,1);
            CRUDutil.actualizarRegistro(modelo,valores);
            modelo = CRUDutil.updateModelo(modelo);
        }
        super.update();

        if (proyecto==null && id!=null){
            proyecto = CRUDutil.updateModelo(CAMPOS_PROYECTO,id);
        }
        if (proyecto != null && id != null && Interactor.getTipoEstado(proyecto.getString(PROYECTO_ID_ESTADO)) >= TPRESUPPENDENTREGA) {
            new FragmentCRUDProyecto.TareaGenerarPdf().execute(id);
        }
        return true;
    }


    public class AdaptadorDetpartida extends RecyclerView.Adapter<AdaptadorDetpartida.DetpartidaViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas, Interactor.TiposDetPartida {

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
            detpartidaViewHolder.cantidad.setText(JavaUtil.getDecimales(
                        listDetpartida.get(position).
                                getDouble(DETPARTIDA_CANTIDAD) * Double.parseDouble(cantidadPartida.getText().toString())));
            if (listDetpartida.get(position).getString(DETPARTIDA_TIPO).equals(TIPOTRABAJO)) {
                detpartidaViewHolder.importe.setText(JavaUtil.formatoMonedaLocal(
                        (listDetpartida.get(position).getDouble(DETPARTIDA_TIEMPO)
                        * listDetpartida.get(position).getDouble(DETPARTIDA_CANTIDAD)
                        * proyecto.getDouble(PROYECTO_PRECIOHORA)
                        * Double.parseDouble(cantidadPartida.getText().toString())
                        *(1+((listaDetpartidas.get(position).getDouble(DETPARTIDA_BENEFICIO))/100)))));
                System.out.println("listDetpartida.get(position).getDouble(DETPARTIDA_CANTIDAD) = " + listDetpartida.get(position).getDouble(DETPARTIDA_CANTIDAD));
            } else {
                detpartidaViewHolder.importe.setText(JavaUtil.formatoMonedaLocal((
                        listDetpartida.get(position).getDouble(DETPARTIDA_PRECIO)
                        * listDetpartida.get(position).getDouble(DETPARTIDA_CANTIDAD)
                        * Double.parseDouble(cantidadPartida.getText().toString())
                        *(1+((listaDetpartidas.get(position).getDouble(DETPARTIDA_BENEFICIO))/100)))));
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


    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
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

            descripcionPartida.setText(entrada.get(posicion).getString(PARTIDA_DESCRIPCION));
            tiempoPartida.setText(JavaUtil.getDecimales(entrada.get(posicion).getDouble(PARTIDA_TIEMPO)));
            cantidadPartida.setText(JavaUtil.getDecimales(entrada.get(posicion).getDouble(PARTIDA_CANTIDAD)));
            importePartida.setText(JavaUtil.formatoMonedaLocal(entrada.get(posicion).getDouble(PARTIDA_PRECIO)));
            completadaPartida.setText(JavaUtil.getDecimales(entrada.get(posicion).getDouble(PARTIDA_COMPLETADA)));
            progressBarPartida.setProgress(entrada.get(posicion).getInt(PARTIDA_COMPLETADA));

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

            descripcionPartida.setText(modelo.getString(PARTIDA_DESCRIPCION));
            tiempoPartida.setText(JavaUtil.getDecimales(modelo.getDouble(PARTIDA_TIEMPO)));
            cantidadPartida.setText(JavaUtil.getDecimales(modelo.getDouble(PARTIDA_CANTIDAD)));
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

            if (proyecto.getInt(PROYECTO_TIPOESTADO) < TPROYECTEJECUCION) {

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
