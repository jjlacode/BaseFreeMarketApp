package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.AppActivity;
import jjlacode.com.freelanceproject.util.BaseViewHolder;
import jjlacode.com.freelanceproject.util.CommonPry;
import jjlacode.com.freelanceproject.util.FragmentCRUD;
import jjlacode.com.freelanceproject.util.ImagenUtil;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.Modelo;
import jjlacode.com.freelanceproject.util.TipoViewHolder;

public class FragmentCRUDPartidaProyecto extends FragmentCRUD implements CommonPry.Constantes,
        ContratoPry.Tablas, CommonPry.TiposDetPartida {

    private Long retraso;
    private EditText nombrePartida;
    private AutoCompleteTextView autoNombrePartida;
    private EditText descripcionPartida;
    private EditText tiempoPartida;
    private EditText importePartida;
    private EditText cantidadPartida;
    private EditText completadaPartida;
    private Button btnNuevaTarea;
    private Button btnNuevoProd;
    private Button btnNuevoProdProv;
    private Button btnNuevaPartida;
    private ImageView imagenret;

    private RecyclerView rvdetalles;
    private ProgressBar progressBarPartida;
    private TextView labelCompletada;
    private ArrayList<Modelo> listaDetpartidas;

    private Modelo proyecto;
    private String idDetPartida;
    private String idPartida;
    private Modelo partida;
    private Uri uri;
    private Button btnVolverProy;

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

    }

    @Override
    protected void setAcciones() {

        btnNuevaTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                modelo = consulta.queryObjectDetalle(CAMPOS_PARTIDA, id, secuencia);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO, proyecto);
                bundle.putSerializable(TABLA_PARTIDA, modelo);
                bundle.putString(ID, modelo.getString(PARTIDA_ID_PARTIDA));
                bundle.putString(NAMEF, namef);
                bundle.putString(NAMESUB, namesubclass);
                bundle.putString(TIPO, TIPOTAREA);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartida());

            }

        });

        btnNuevoProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                modelo = consulta.queryObjectDetalle(CAMPOS_PARTIDA, id, secuencia);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO, proyecto);
                bundle.putSerializable(TABLA_PARTIDA, modelo);
                bundle.putString(ID, modelo.getString(PARTIDA_ID_PARTIDA));
                bundle.putString(NAMEF, namef);
                bundle.putString(NAMESUB, namesubclass);
                bundle.putString(TIPO, TIPOPRODUCTO);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartida());

            }

        });

        btnNuevoProdProv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                modelo = consulta.queryObjectDetalle(CAMPOS_PARTIDA, id, secuencia);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO, proyecto);
                bundle.putSerializable(TABLA_PARTIDA, modelo);
                bundle.putString(ID, modelo.getString(PARTIDA_ID_PARTIDA));
                bundle.putString(NAMEF, namef);
                bundle.putString(NAMESUB, namesubclass);
                bundle.putString(TIPO, TIPOPRODUCTOPROV);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartida());

            }

        });

        btnNuevaPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                modelo = consulta.queryObjectDetalle(CAMPOS_PARTIDA, id, secuencia);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO, proyecto);
                bundle.putSerializable(TABLA_PARTIDA, modelo);
                bundle.putString(ID, modelo.getString(PARTIDA_ID_PARTIDA));
                bundle.putString(NAMEF, namef);
                bundle.putString(NAMESUB, namesubclass);
                bundle.putString(TIPO, TIPOPARTIDA);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartida());

            }

        });

        btnVolverProy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonPry.Calculos.TareaActualizaProy().execute(id);
                bundle.putSerializable(MODELO, proyecto);
                bundle.putString(NAMEF, namesubclass);
                bundle.putString(NAMESUB, CommonPry.setNamefdef());
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
            }
        });

    }


    @Override
    protected void setNuevo() {

        tiempoPartida.setVisibility(View.GONE);
        importePartida.setVisibility(View.GONE);
        completadaPartida.setVisibility(View.GONE);
        btndelete.setVisibility(View.GONE);
        labelCompletada.setVisibility(View.GONE);
        rvdetalles.setVisibility(View.GONE);
        btnNuevaTarea.setVisibility(View.GONE);
        btnNuevoProd.setVisibility(View.GONE);
        btnNuevoProdProv.setVisibility(View.GONE);
        btnNuevaPartida.setVisibility(View.GONE);
        progressBarPartida.setVisibility(View.GONE);
        imagenret.setVisibility(View.GONE);

        setAdaptadorAuto(autoNombrePartida);

        autoNombrePartida.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                modelo = (Modelo) autoNombrePartida.getAdapter().getItem(position);

                mostrarDialogoClonarPartida();

            }

        });


    }

    @Override
    protected void setMaestroDetallePort() {
        maestroDetalleSeparados = true;

    }

    @Override
    protected void setMaestroDetalleLand() {
        maestroDetalleSeparados = false;

    }

    @Override
    protected void setMaestroDetalleTabletLand() {
        maestroDetalleSeparados = false;

    }

    @Override
    protected void setMaestroDetalleTabletPort() {
        maestroDetalleSeparados = false;

    }


    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_cud_partida_proyecto;
        layoutCabecera = R.layout.cabecera_crud_partida;
        layoutitem = R.layout.item_list_partida;

    }

    @Override
    protected void setInicio() {

        nombrePartida = view.findViewById(R.id.etnomudpartida);
        autoNombrePartida = view.findViewById(R.id.autonomudpartida);
        descripcionPartida = view.findViewById(R.id.etdescripcionUDpartida);
        tiempoPartida = view.findViewById(R.id.ettiempoUDpartida);
        importePartida = view.findViewById(R.id.etprecioUDpartida);
        cantidadPartida = view.findViewById(R.id.etcantidadUDpartida);
        completadaPartida = view.findViewById(R.id.etcompletadaUDpartida);
        btnNuevaTarea = view.findViewById(R.id.btntareaudpartida);
        btnNuevoProd = view.findViewById(R.id.btnprodudpartida);
        btnNuevoProdProv = view.findViewById(R.id.btnprovudpartida);
        btnNuevaPartida = view.findViewById(R.id.btnpartudpartida);
        progressBarPartida = view.findViewById(R.id.progressBarUDpartida);
        imagen = view.findViewById(R.id.imgudpartida);
        imagenret = view.findViewById(R.id.imgretudpartida);
        labelCompletada = view.findViewById(R.id.lcompletadaUDpartida);
        rvdetalles = view.findViewById(R.id.rvdetalleUDpartida);
        btnVolverProy = view.findViewById(R.id.btn_volverproy);

    }

    @Override
    protected void setTabla() {

        tabla = TABLA_PARTIDA;

    }

    @Override
    protected void setTablaCab() {

        tablaCab = TABLA_PROYECTO;
    }

    @Override
    protected void setContext() {

        contexto = getContext();
    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_PARTIDA;

    }

    @Override
    protected void setCampoID() {
        campoID = PARTIDA_ID_PROYECTO;
    }

    @Override
    protected void setBundle() {

        if (bundle != null) {
            proyecto = (Modelo) bundle.getSerializable(PROYECTO);

        }

    }

    @Override
    protected void setDatos() {

        tiempoPartida.setVisibility(View.VISIBLE);
        importePartida.setVisibility(View.VISIBLE);
        completadaPartida.setVisibility(View.VISIBLE);
        btndelete.setVisibility(View.VISIBLE);
        labelCompletada.setVisibility(View.VISIBLE);
        rvdetalles.setVisibility(View.VISIBLE);
        btnNuevaTarea.setVisibility(View.VISIBLE);
        btnNuevoProd.setVisibility(View.VISIBLE);
        //btnNuevoProdProv.setVisibility(View.VISIBLE);
        btnNuevoProdProv.setVisibility(View.GONE);
        btnNuevaPartida.setVisibility(View.VISIBLE);
        progressBarPartida.setVisibility(View.VISIBLE);
        imagenret.setVisibility(View.VISIBLE);

        if (modelo.getString(PARTIDA_RUTAFOTO) != null) {

            path = modelo.getString(PARTIDA_RUTAFOTO);
            ImagenUtil imagenUtil = new ImagenUtil(contexto);
            imagenUtil.setImageUriCircle(path, imagen);

        }

        if (namesubclass.equals(PRESUPUESTO)) {

            progressBarPartida.setVisibility(View.GONE);
            completadaPartida.setVisibility(View.GONE);
            labelCompletada.setVisibility(View.GONE);
        } else {

            progressBarPartida.setVisibility(View.VISIBLE);
            completadaPartida.setVisibility(View.VISIBLE);
            labelCompletada.setVisibility(View.VISIBLE);
        }

        nombrePartida.setText(modelo.getString(PARTIDA_NOMBRE));
        descripcionPartida.setText(modelo.getString(PARTIDA_DESCRIPCION));
        tiempoPartida.setText(modelo.getString(PARTIDA_TIEMPO));
        importePartida.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(PARTIDA_PRECIO)));
        cantidadPartida.setText(modelo.getString(PARTIDA_CANTIDAD));
        completadaPartida.setText(modelo.getString(PARTIDA_COMPLETADA));
        progressBarPartida.setProgress(modelo.getInt(PARTIDA_COMPLETADA));
        idDetPartida = modelo.getString(PARTIDA_ID_PARTIDA);

        retraso = modelo.getLong(PARTIDA_PROYECTO_RETRASO);
        if (retraso < 1) {
            imagenret.setImageResource(R.drawable.alert_box_r);
        } else if (retraso < 3) {
            imagenret.setImageResource(R.drawable.alert_box_a);
        } else {
            imagenret.setImageResource(R.drawable.alert_box_v);
        }

        listaDetpartidas = consulta.queryListDetalle(CAMPOS_DETPARTIDA, idDetPartida, tabla);

        if (listaDetpartidas != null && listaDetpartidas.size() > 0) {

            rvdetalles.setLayoutManager(new LinearLayoutManager(getContext()));

            AdaptadorDetpartida adapter = new AdaptadorDetpartida(listaDetpartidas, namef);

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
                    bundle.putString(ID, idDetPartida);
                    bundle.putInt(SECUENCIA, secuenciadetpartida);
                    bundle.putString(NAMEF, namef);
                    bundle.putString(NAMESUB, namesubclass);
                    bundle.putString(TIPO, tipo);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartida());

                }
            });

        } else {

            rvdetalles.setVisibility(View.GONE);
        }

        setAdaptadorAuto(autoNombrePartida);

        autoNombrePartida.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                modelo = (Modelo) autoNombrePartida.getAdapter().getItem(position);
                mostrarDialogoClonarPartida();
            }
        });
    }

    @Override
    protected void setcambioFragment() {

        if (namef.equals(AGENDA)) {

            icFragmentos.enviarBundleAFragment(bundle, new FragmentAgenda());

        }

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

        consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_NOMBRE, nombrePartida.getText().toString());
        consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_DESCRIPCION, descripcionPartida.getText().toString());
        consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_TIEMPO, JavaUtil.comprobarDouble(tiempoPartida.getText().toString()));
        consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_PRECIO, JavaUtil.comprobarDouble(importePartida.getText().toString()));
        consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_CANTIDAD, JavaUtil.comprobarDouble(cantidadPartida.getText().toString()));
        consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_COMPLETADA, JavaUtil.comprobarInteger(completadaPartida.getText().toString()));
        consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_RUTAFOTO, path);
        consulta.putDato(valores, campos, PARTIDA_ID_PROYECTO, id);
        if (idDetPartida == null && nuevo) {
            System.out.println("Generar idetPartida");
            idDetPartida = ContratoPry.generarIdTabla(tabla);
            System.out.println("idDetPartida = " + idDetPartida);
        }
        if (nuevo) {
            consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA, idDetPartida);
            proyecto = consulta.queryObject(CAMPOS_PROYECTO, id);
            consulta.putDato(valores, campos, PARTIDA_ID_ESTADO, proyecto.getString(PROYECTO_ID_ESTADO));

        }


    }

    @Override
    protected boolean update() {

        new CommonPry.Calculos.Tareafechas().execute();
        new CommonPry.Calculos.TareaActualizaProy().execute(id);

        return super.update();

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
            detpartidaViewHolder.cantidad.setText(listDetpartida.get(position).getString(DETPARTIDA_CANTIDAD));
            if (listDetpartida.get(position).getString(DETPARTIDA_TIPO).equals(CommonPry.TiposDetPartida.TIPOTAREA)) {
                detpartidaViewHolder.importe.setText(JavaUtil.formatoMonedaLocal(
                        (listDetpartida.get(position).getDouble(DETPARTIDA_TIEMPO) * CommonPry.hora *
                                listDetpartida.get(position).getDouble(DETPARTIDA_CANTIDAD))));
            } else {
                detpartidaViewHolder.importe.setText(listDetpartida.get(position).getString(DETPARTIDA_PRECIO));
            }
            if (listDetpartida.get(position).getString(DETPARTIDA_RUTAFOTO) != null) {
                if (tipodetpartida.equals(TIPOPRODUCTOPROV)) {
                    ImagenUtil imagenUtil = new ImagenUtil(context);
                    imagenUtil.setImageFireStoreCircle(listDetpartida.get(position).getString(DETPARTIDA_RUTAFOTO), detpartidaViewHolder.imagen);

                } else {
                    detpartidaViewHolder.imagen.setImageURI(listDetpartida.get(position).getUri(DETPARTIDA_RUTAFOTO));
                }
            }

            if (!tipodetpartida.equals(TIPOTAREA)) {

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


        ArrayList<Modelo> lista = consulta.queryList(CAMPOS_PARTIDA);
        ArrayList<Modelo> listabase = consulta.queryList(CAMPOS_PARTIDABASE);
        lista.addAll(listabase);


        autoCompleteTextView.setAdapter(new ListaAdaptadorFiltroPartidas(getContext(),
                R.layout.item_list_partida, lista) {
            @Override
            public void onEntrada(Modelo entrada, View view) {

                ImageView imagen = view.findViewById(R.id.imglpartida);
                TextView descripcion = view.findViewById(R.id.tvdescripcionpartida);
                TextView tiempo = view.findViewById(R.id.tvtiempopartida);
                TextView cantidad = view.findViewById(R.id.tvcantidadpartida);
                TextView importe = view.findViewById(R.id.tvimppartida);

                String tabla = entrada.getNombreTabla();
                System.out.println("tabla = " + tabla);

                if (tabla.equals(TABLA_PARTIDA)) {

                    descripcion.setText(entrada.getString(PARTIDA_DESCRIPCION));
                    tiempo.setText(entrada.getString(PARTIDA_TIEMPO));
                    cantidad.setText(entrada.getString(PARTIDA_CANTIDAD));
                    importe.setText(entrada.getString(PARTIDA_PRECIO));

                    if (entrada.getString(PARTIDA_RUTAFOTO) != null) {
                        ImagenUtil imagenUtil = new ImagenUtil(contexto);
                        imagenUtil.setImageUriCircle(entrada.getString(PARTIDA_RUTAFOTO), imagen);
                        //imagen.setImageURI(entrada.getUri(PARTIDA_RUTAFOTO));
                    }
                } else if (tabla.equals(TABLA_PARTIDABASE)) {

                    descripcion.setText(entrada.getString(PARTIDABASE_DESCRIPCION));
                    tiempo.setText(entrada.getString(PARTIDABASE_TIEMPO));
                    importe.setText(entrada.getString(PARTIDABASE_PRECIO));

                    if (entrada.getString(PARTIDABASE_RUTAFOTO) != null) {
                        ImagenUtil imagenUtil = new ImagenUtil(contexto);
                        imagenUtil.setImageUriCircle(entrada.getString(PARTIDABASE_RUTAFOTO), imagen);
                        //imagen.setImageURI(entrada.getUri(PARTIDABASE_RUTAFOTO));
                    }

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

                            String tabla = item.getNombreTabla();
                            System.out.println("tabla = " + tabla);

                            if (tabla.equals(TABLA_PARTIDABASE)) {

                                if (item.getCampos(PARTIDABASE_NOMBRE).toLowerCase().contains(constraint.toString().toLowerCase())) {

                                    suggestion.add(item);
                                } else if (item.getCampos(PARTIDABASE_DESCRIPCION).toLowerCase().contains(constraint.toString().toLowerCase())) {

                                    suggestion.add(item);
                                }

                            } else if (tabla.equals(TABLA_PARTIDA)) {

                                if (item.getCampos(PARTIDA_NOMBRE).toLowerCase().contains(constraint.toString().toLowerCase())) {

                                    suggestion.add(item);
                                } else if (item.getCampos(PARTIDA_DESCRIPCION).toLowerCase().contains(constraint.toString().toLowerCase())) {

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
                        // no filter, add entire original list back in
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

    private void mostrarDialogoClonarPartida() {

        final CharSequence[] opciones = {"Clonar partida", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Clonar partida")) {

                    if (modelo.getNombreTabla().equals(TABLA_PARTIDA)) {

                        valores = modelo.contenido();
                        idPartida = ContratoPry.generarIdTabla(TABLA_PARTIDA);
                        valores.put(PARTIDA_ID_PROYECTO, id);
                        valores.put(PARTIDA_ID_PARTIDA, idPartida);
                        valores.put(PARTIDA_CANTIDAD, 0);
                        valores.remove(PARTIDA_SECUENCIA);

                    } else if (modelo.getNombreTabla().equals(TABLA_PARTIDABASE)) {

                        valores = new ContentValues();
                        idPartida = modelo.getString(PARTIDABASE_ID_PARTIDABASE);
                        valores.put(PARTIDA_CANTIDAD, 0);
                        valores.put(PARTIDA_DESCRIPCION, modelo.getString(PARTIDABASE_DESCRIPCION));
                        valores.put(PARTIDA_NOMBRE, modelo.getString(PARTIDABASE_NOMBRE));
                        valores.put(PARTIDA_PRECIO, modelo.getString(PARTIDABASE_PRECIO));
                        valores.put(PARTIDA_TIEMPO, modelo.getString(PARTIDABASE_TIEMPO));
                        valores.put(PARTIDA_RUTAFOTO, modelo.getString(PARTIDABASE_RUTAFOTO));
                        valores.put(PARTIDA_ID_PROYECTO, id);
                        valores.put(PARTIDA_ID_ESTADO, proyecto.getString(PROYECTO_ID_ESTADO));
                        valores.put(PARTIDA_ID_PARTIDA, idPartida);
                    }

                    uri = consulta.insertRegistroDetalle(CAMPOS_PARTIDA, id,
                            TABLA_PROYECTO, valores);
                    System.out.println("uri = " + uri);

                    partida = consulta.queryObjectDetalle(CAMPOS_PARTIDA, uri);
                    secuencia = partida.getInt(PARTIDA_SECUENCIA);

                    nombrePartida.setText(partida.getString(PARTIDA_NOMBRE));
                    descripcionPartida.setText(partida.getString(PARTIDA_DESCRIPCION));

                    if (partida.getString(PARTIDA_RUTAFOTO) != null) {
                        setImagenUri(contexto,partida.getString(PARTIDA_RUTAFOTO));
                        path = partida.getString(PARTIDA_RUTAFOTO);
                    }

                    if (modelo.getNombreTabla().equals(TABLA_PARTIDABASE)) {

                        ArrayList<Modelo> listaclon = consulta.queryListDetalle
                                (CAMPOS_DETPARTIDABASE, modelo.getString(PARTIDABASE_ID_PARTIDABASE), TABLA_PARTIDABASE);

                        for (Modelo clonpart : listaclon) {

                            valores = new ContentValues();
                            valores.put(DETPARTIDA_ID_PARTIDA, idPartida);
                            valores.put(DETPARTIDA_NOMBRE, clonpart.getString(DETPARTIDABASE_NOMBRE));
                            valores.put(DETPARTIDA_DESCRIPCION, clonpart.getString(DETPARTIDABASE_DESCRIPCION));
                            valores.put(DETPARTIDA_BENEFICIO, clonpart.getString(DETPARTIDABASE_BENEFICIO));
                            valores.put(DETPARTIDA_CANTIDAD, clonpart.getString(DETPARTIDABASE_CANTIDAD));
                            valores.put(DETPARTIDA_DESCUENTOPROV, clonpart.getString(DETPARTIDABASE_DESCUENTOPROV));
                            valores.put(DETPARTIDA_PRECIO, clonpart.getString(DETPARTIDABASE_PRECIO));
                            valores.put(DETPARTIDA_ID_DETPARTIDA, clonpart.getString(DETPARTIDABASE_ID_DETPARTIDABASE));
                            valores.put(DETPARTIDA_REFPROV, clonpart.getString(DETPARTIDABASE_REFPROV));
                            valores.put(DETPARTIDA_TIEMPO, clonpart.getString(DETPARTIDABASE_TIEMPO));
                            valores.put(DETPARTIDA_RUTAFOTO, clonpart.getString(DETPARTIDABASE_RUTAFOTO));
                            valores.put(DETPARTIDA_TIPO, clonpart.getString(DETPARTIDABASE_TIPO));
                            valores.remove(DETPARTIDABASE_SECUENCIA);

                            consulta.insertRegistroDetalle(CAMPOS_DETPARTIDA, idPartida
                                    , TABLA_PARTIDA, valores);
                        }

                        nombrePartida.setText(modelo.getString(PARTIDABASE_NOMBRE));

                    } else if (modelo.getNombreTabla().equals(TABLA_PARTIDA)) {

                        ArrayList<Modelo> listaclon = consulta.queryListDetalle
                                (CAMPOS_DETPARTIDA, modelo.getString(PARTIDA_ID_PARTIDA), TABLA_PARTIDA);

                        System.out.println("listaclon = " + listaclon.toString());

                        for (Modelo clonpart : listaclon) {

                            valores = clonpart.contenido();//AndroidUtil.clonarSinRef(clonpart);
                            valores.put(DETPARTIDA_ID_PARTIDA, idPartida);
                            valores.remove(DETPARTIDA_SECUENCIA);

                            consulta.insertRegistroDetalle(CAMPOS_DETPARTIDA, idPartida
                                    , TABLA_PARTIDA, valores);
                        }
                        nombrePartida.setText(modelo.getString(PARTIDA_NOMBRE));
                    }

                    modelo = consulta.queryObjectDetalle(campos, id, secuencia);
                    nuevo = false;

                } else {
                    dialog.dismiss();
                }

                autoNombrePartida.setText("");

            }
        });
        builder.show();
    }

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

            if (namef.equals(PRESUPUESTO)) {

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

        }

        @Override
        public void bind(Modelo modelo) {

            descripcionPartida.setText(modelo.getCampos(PARTIDA_DESCRIPCION));
            tiempoPartida.setText(modelo.getCampos(PARTIDA_TIEMPO));
            cantidadPartida.setText(modelo.getCampos(PARTIDA_CANTIDAD));
            importePartida.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(PARTIDA_PRECIO)));
            completadaPartida.setText(modelo.getCampos(PARTIDA_COMPLETADA));
            progressBarPartida.setProgress(Integer.parseInt(modelo.getCampos(PARTIDA_COMPLETADA)));

            if (modelo.getString(PARTIDA_RUTAFOTO) != null) {

                imagenPartida.setImageURI(modelo.getUri(PARTIDA_RUTAFOTO));
            }

            long retraso = modelo.getLong(PARTIDA_PROYECTO_RETRASO);
            if (retraso > 3 * DIASLONG) {
                imagenret.setImageResource(R.drawable.alert_box_r);
            } else if (retraso > DIASLONG) {
                imagenret.setImageResource(R.drawable.alert_box_a);
            } else {
                imagenret.setImageResource(R.drawable.alert_box_v);
            }

            if (namef.equals(PRESUPUESTO)) {

                progressBarPartida.setVisibility(View.GONE);

            } else {

                progressBarPartida.setVisibility(View.VISIBLE);
            }

            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }


    }
}
