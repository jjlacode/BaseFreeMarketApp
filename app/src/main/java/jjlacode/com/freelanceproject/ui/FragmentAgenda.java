package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.adapter.AdaptadorAgendaPresup;
import jjlacode.com.freelanceproject.adapter.AdaptadorAgendaTareas;
import jjlacode.com.freelanceproject.interfaces.ICFragmentos;
import jjlacode.com.freelanceproject.model.AgendaPresup;
import jjlacode.com.freelanceproject.model.AgendaTarea;
import jjlacode.com.freelanceproject.model.Modelo;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;
import jjlacode.com.utilidades.Utilidades;

import static jjlacode.com.utilidades.Utilidades.Constantes.DIASLONG;

public class FragmentAgenda extends Fragment implements Common.TiposEvento, Contract.Tablas {

    private String idProyecto;
    private String namef;
    private String secuenciaPartida;

    View vista;
    RecyclerView rvAgenda;
    Button btnTareas;
    Button btnPresup;
    Button btnEspera;
    Button btnCobros;
    Button btnEventos;
    TextView tituloTab;
    ArrayList<AgendaTarea> listaAgendaTarea = new ArrayList<>();
    ArrayList<AgendaPresup> listaAgendaPresup = new ArrayList<>();
    ICFragmentos icFragments;
    Activity activity;
    Bundle bundle;


    public FragmentAgenda() {
        // Required empty public constructor
    }


    public static FragmentAgenda newInstance() {
        FragmentAgenda fragment = new FragmentAgenda();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_agenda, container, false);

        rvAgenda = vista.findViewById(R.id.rvagenda);
        rvAgenda.setLayoutManager(new LinearLayoutManager(getContext()));
        btnEventos = vista.findViewById(R.id.btneventosagenda);
        btnTareas = vista.findViewById(R.id.btntareaagenda);
        btnPresup = vista.findViewById(R.id.btnpresupagenda);
        btnEspera = vista.findViewById(R.id.btnenesperaagenda);
        btnCobros = vista.findViewById(R.id.btnpendcobroagenda);
        tituloTab = vista.findViewById(R.id.tvtitagenda);

        bundle = getArguments();

        namef = bundle.getString("namef");
        bundle = null;

        tituloTab.setText(R.string.proximos_eventos);

        obtenerEventos();

        btnEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tituloTab.setText(R.string.proximos_eventos);

                obtenerEventos();
            }
        });

        btnTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listaAgendaPresup = new ArrayList<>();
                listaAgendaTarea = new ArrayList<>();

                tituloTab.setText(R.string.tareas_pendientes);

        ArrayList<Modelo> lista ;
        ArrayList<Modelo> listaPartidasSinCompletar = new ArrayList<>();

            lista = QueryDB.queryList(CAMPOS_PARTIDA);

                for (Modelo item : lista) {

                    if (item.getInt(PARTIDA_COMPLETADA) < 100 &&
                            item.getInt(PARTIDA_TIPO_ESTADO) >= 4){

                        listaPartidasSinCompletar.add(item);
                    }
                }

                for (Modelo partida : listaPartidasSinCompletar) {

                    AgendaTarea agendaTarea = new AgendaTarea();

                    agendaTarea.setIdProyecto(partida.getString(PARTIDA_ID_PROYECTO));
                    agendaTarea.setSecuencia(partida.getString(PARTIDA_SECUENCIA));
                    agendaTarea.setDescripcion(partida.getCampos(PARTIDA_DESCRIPCION));
                    agendaTarea.setCantidad(partida.getCampos(PARTIDA_CANTIDAD));
                    agendaTarea.setTiempo(partida.getCampos(PARTIDA_TIEMPO));
                    agendaTarea.setCompletada(partida.getCampos(PARTIDA_COMPLETADA));
                    agendaTarea.setIdEstado(partida.getCampos(PARTIDA_ID_ESTADO));
                    agendaTarea.setTipoEstado(partida.getCampos(PARTIDA_TIPO_ESTADO));
                    agendaTarea.setRetraso(partida.getCampos(PARTIDA_PROYECTO_RETRASO));

                    Modelo proyecto = QueryDB.queryObject
                            (CAMPOS_PROYECTO,partida.getCampos(PARTIDA_ID_PROYECTO));

                        agendaTarea.setPeso(proyecto.getCampos(PROYECTO_CLIENTE_PESOTIPOCLI));
                        agendaTarea.setNombreProyecto(proyecto.getCampos(PROYECTO_NOMBRE));
                        agendaTarea.setNombreCliente(proyecto.getCampos(PROYECTO_CLIENTE_NOMBRE));
                        agendaTarea.setRutaFoto(proyecto.getCampos(PROYECTO_RUTAFOTO));
                        agendaTarea.setFechaEntrada(proyecto.getCampos(PROYECTO_FECHAENTRADA));
                        agendaTarea.setFechaEntregaPresup(proyecto.getCampos(PROYECTO_FECHAENTREGAPRESUP));
                        agendaTarea.setFechaAcordada(proyecto.getCampos(PROYECTO_FECHAENTREGAACORDADA));
                        agendaTarea.setFechaCalculada(proyecto.getCampos(PROYECTO_FECHAENTREGACALCULADA));
                        agendaTarea.setFechaFinal(proyecto.getCampos(PROYECTO_FECHAFINAL));

                         Modelo cliente = QueryDB.queryObject
                                 (CAMPOS_CLIENTE,proyecto.getString(PROYECTO_ID_CLIENTE));

                        agendaTarea.setTipoCliente(cliente.getString(CLIENTE_DESCRIPCIONTIPOCLI));


                    listaAgendaTarea.add(agendaTarea);
                }


        AdaptadorAgendaTareas adaptadorAgenda = new AdaptadorAgendaTareas(listaAgendaTarea);

        rvAgenda.setAdapter(adaptadorAgenda);

        adaptadorAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idProyecto = listaAgendaTarea.get(rvAgenda.getChildAdapterPosition(v)).getIdProyecto();
                secuenciaPartida = listaAgendaTarea.get(rvAgenda.getChildAdapterPosition(v)).getSecuencia();
                System.out.println("idProyecto = " + idProyecto);
                System.out.println("secuenciaPartida = " + secuenciaPartida);

                Modelo partida = QueryDB.queryObjectDetalle(CAMPOS_PARTIDA,idProyecto,secuenciaPartida);

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PARTIDA,partida);
                bundle.putString("namef",namef);
                icFragments.enviarBundleAFragment(bundle,new FragmentUDPartidaProyecto());
                bundle = null;
            }
        });
            }
        });

        btnPresup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listaAgendaPresup = new ArrayList<>();
                listaAgendaTarea = new ArrayList<>();

                tituloTab.setText(R.string.presupuestos_pendientes);

                String seleccion = ESTADO_TIPOESTADO + " = 1 OR "+
                       ESTADO_TIPOESTADO + " = 2";
                String orden = "'"+ PROYECTO_FECHAENTRADA+"' ASC";

                ArrayList<Modelo> lista = null;

                    lista = QueryDB.queryList(CAMPOS_PROYECTO,seleccion,orden);

                for (Modelo proyecto : lista) {

                    AgendaPresup agendaPresup = new AgendaPresup();
                    agendaPresup.setId_presupuesto(proyecto.getCampos(Contract.Tablas.PROYECTO_ID_PROYECTO));
                    agendaPresup.setPresupuesto(proyecto.getCampos(Contract.Tablas.PROYECTO_NOMBRE));
                    agendaPresup.setDescripcion(proyecto.getCampos(Contract.Tablas.PROYECTO_DESCRIPCION));
                    agendaPresup.setCliente(proyecto.getCampos(Contract.Tablas.PROYECTO_CLIENTE_NOMBRE));
                    agendaPresup.setEstado(proyecto.getCampos(Contract.Tablas.PROYECTO_DESCRIPCION_ESTADO));
                    agendaPresup.setFechaEntrada(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAENTRADA));
                    agendaPresup.setFechaEntregaPresup(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAENTREGAPRESUP));
                    agendaPresup.setFechaAcordada(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAENTREGAACORDADA));
                    agendaPresup.setFechaCalculada(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAENTREGACALCULADA));
                    agendaPresup.setFechaFinal(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAFINAL));
                    agendaPresup.setPeso(proyecto.getCampos(Contract.Tablas.PROYECTO_CLIENTE_PESOTIPOCLI));
                    agendaPresup.setRetraso(proyecto.getCampos(Contract.Tablas.PROYECTO_RETRASO));
                    agendaPresup.setRutaFoto(proyecto.getCampos(Contract.Tablas.PROYECTO_RUTAFOTO));
                    agendaPresup.setTipoEstado(proyecto.getCampos(Contract.Tablas.PROYECTO_TIPOESTADO));

                    listaAgendaPresup.add(agendaPresup);
                }

                AdaptadorAgendaPresup adaptadorAgendaPresup = new AdaptadorAgendaPresup(listaAgendaPresup);

                rvAgenda.setAdapter(adaptadorAgendaPresup);

                adaptadorAgendaPresup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        idProyecto = listaAgendaPresup.get(rvAgenda.getChildAdapterPosition(v)).getId_presupuesto();

                        Modelo proyecto =  QueryDB.queryObject(CAMPOS_PROYECTO,idProyecto);

                        bundle = new Bundle();
                        bundle.putSerializable(TABLA_PROYECTO,proyecto);
                        bundle.putString("namef",namef);
                        icFragments.enviarBundleAFragment(bundle,new FragmentUDProyecto());
                        bundle = null;

                    }
                });
            }
        });

        btnEspera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listaAgendaPresup = new ArrayList<>();
                listaAgendaTarea = new ArrayList<>();

                tituloTab.setText(R.string.presupuestos_en_espera);

                String seleccion = ESTADO_TIPOESTADO + " = 3";
                String orden = "'"+ PROYECTO_FECHAENTREGAPRESUP+"' ASC";

                ArrayList<Modelo> lista = null;

                lista = QueryDB.queryList(CAMPOS_PROYECTO,seleccion,orden);

                for (Modelo proyecto : lista) {

                    AgendaPresup agendaPresup = new AgendaPresup();
                    agendaPresup.setId_presupuesto(proyecto.getCampos(Contract.Tablas.PROYECTO_ID_PROYECTO));
                    agendaPresup.setPresupuesto(proyecto.getCampos(Contract.Tablas.PROYECTO_NOMBRE));
                    agendaPresup.setDescripcion(proyecto.getCampos(Contract.Tablas.PROYECTO_DESCRIPCION));
                    agendaPresup.setCliente(proyecto.getCampos(Contract.Tablas.PROYECTO_CLIENTE_NOMBRE));
                    agendaPresup.setEstado(proyecto.getCampos(Contract.Tablas.PROYECTO_DESCRIPCION_ESTADO));
                    agendaPresup.setFechaEntrada(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAENTRADA));
                    agendaPresup.setFechaEntregaPresup(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAENTREGAPRESUP));
                    agendaPresup.setFechaAcordada(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAENTREGAACORDADA));
                    agendaPresup.setFechaCalculada(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAENTREGACALCULADA));
                    agendaPresup.setFechaFinal(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAFINAL));
                    agendaPresup.setPeso(proyecto.getCampos(Contract.Tablas.PROYECTO_CLIENTE_PESOTIPOCLI));
                    agendaPresup.setRetraso(proyecto.getCampos(Contract.Tablas.PROYECTO_RETRASO));
                    agendaPresup.setRutaFoto(proyecto.getCampos(Contract.Tablas.PROYECTO_RUTAFOTO));
                    agendaPresup.setTipoEstado(proyecto.getCampos(Contract.Tablas.PROYECTO_TIPOESTADO));

                    listaAgendaPresup.add(agendaPresup);
                }

                AdaptadorAgendaPresup adaptadorAgendaPresup = new AdaptadorAgendaPresup(listaAgendaPresup);

                rvAgenda.setAdapter(adaptadorAgendaPresup);

                adaptadorAgendaPresup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        idProyecto = listaAgendaPresup.get(rvAgenda.getChildAdapterPosition(v)).getId_presupuesto();

                        Modelo proyecto =  QueryDB.queryObject(CAMPOS_PROYECTO,idProyecto);

                        bundle = new Bundle();
                        bundle.putSerializable(TABLA_PROYECTO,proyecto);
                        bundle.putString("namef",namef);
                        icFragments.enviarBundleAFragment(bundle,new FragmentUDProyecto());
                        bundle = null;

                    }
                });
            }
        });

        btnCobros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listaAgendaPresup = new ArrayList<>();
                listaAgendaTarea = new ArrayList<>();

                tituloTab.setText(R.string.pendiente_cobro);

                String seleccion = ESTADO_TIPOESTADO + " = 7";
                String orden = "'"+ PROYECTO_FECHAFINAL+"' ASC";

                ArrayList<Modelo> lista = null;

                lista = QueryDB.queryList(CAMPOS_PROYECTO,seleccion,orden);

                for (Modelo proyecto : lista) {

                    AgendaPresup agendaPresup = new AgendaPresup();
                    agendaPresup.setId_presupuesto(proyecto.getCampos(Contract.Tablas.PROYECTO_ID_PROYECTO));
                    agendaPresup.setPresupuesto(proyecto.getCampos(Contract.Tablas.PROYECTO_NOMBRE));
                    agendaPresup.setDescripcion(proyecto.getCampos(Contract.Tablas.PROYECTO_DESCRIPCION));
                    agendaPresup.setCliente(proyecto.getCampos(Contract.Tablas.PROYECTO_CLIENTE_NOMBRE));
                    agendaPresup.setEstado(proyecto.getCampos(Contract.Tablas.PROYECTO_DESCRIPCION_ESTADO));
                    agendaPresup.setFechaEntrada(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAENTRADA));
                    agendaPresup.setFechaEntregaPresup(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAENTREGAPRESUP));
                    agendaPresup.setFechaAcordada(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAENTREGAACORDADA));
                    agendaPresup.setFechaCalculada(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAENTREGACALCULADA));
                    agendaPresup.setFechaFinal(proyecto.getCampos(Contract.Tablas.PROYECTO_FECHAFINAL));
                    agendaPresup.setPeso(proyecto.getCampos(Contract.Tablas.PROYECTO_CLIENTE_PESOTIPOCLI));
                    agendaPresup.setRetraso(proyecto.getCampos(Contract.Tablas.PROYECTO_RETRASO));
                    agendaPresup.setRutaFoto(proyecto.getCampos(Contract.Tablas.PROYECTO_RUTAFOTO));
                    agendaPresup.setTipoEstado(proyecto.getCampos(Contract.Tablas.PROYECTO_TIPOESTADO));

                    listaAgendaPresup.add(agendaPresup);
                }

                AdaptadorAgendaPresup adaptadorAgendaPresup = new AdaptadorAgendaPresup(listaAgendaPresup);

                rvAgenda.setAdapter(adaptadorAgendaPresup);

                adaptadorAgendaPresup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        idProyecto = listaAgendaPresup.get(rvAgenda.getChildAdapterPosition(v)).getId_presupuesto();

                        Modelo proyecto =  QueryDB.queryObject(CAMPOS_PROYECTO,idProyecto);

                        bundle = new Bundle();
                        bundle.putSerializable(TABLA_PROYECTO,proyecto);
                        bundle.putString("namef",namef);
                        icFragments.enviarBundleAFragment(bundle,new FragmentUDProyecto());
                        bundle = null;

                    }
                });
            }
        });

        return vista;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.activity = (Activity) context;
            icFragments = (ICFragmentos) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void obtenerEventos() {

        int diasAgenda = 90; //TODO poner esto en preferencias

        long diasEventos = Utilidades.hoy() + (diasAgenda * DIASLONG);

        ArrayList<Modelo> listaEventos = new ArrayList<>();
        ArrayList<Modelo> lista = QueryDB.queryList(CAMPOS_EVENTO, null, null);
        int i = 0;
        for (Modelo item : lista) {

            System.out.println("lista.get(i).getTipoevento() = " + lista.get(i).getCampos
                    (Contract.Tablas.EVENTO_TIPOEVENTO));

            if (((lista.get(i).getCampos(Contract.Tablas.EVENTO_TIPOEVENTO) != null &&
                    lista.get(i).getCampos(Contract.Tablas.EVENTO_TIPOEVENTO).equals(TAREA))
                    || (item.getCampos(Contract.Tablas.EVENTO_FECHAINIEVENTO) != null &&
                    Long.parseLong(item.getCampos(Contract.Tablas.EVENTO_FECHAINIEVENTO)) < diasEventos)) &&
                    Double.parseDouble(lista.get(i).getCampos(Contract.Tablas.EVENTO_COMPLETADA)) < 100) {

                listaEventos.add(item);
            }
            i++;
        }

        AdaptadorEventoInt adaptadorEvento = new AdaptadorEventoInt(listaEventos,namef);
        rvAgenda.setAdapter(adaptadorEvento);
    }

    class AdaptadorEventoInt extends RecyclerView.Adapter<AdaptadorEventoInt.EventoViewHolder>
            implements Common.TiposEvento,View.OnClickListener {

        ArrayList<Modelo> listaEvento;
        private View.OnClickListener listener;
        private String namef;

        public AdaptadorEventoInt(ArrayList<Modelo> listaEvento, String namef) {

            this.listaEvento = listaEvento;
            this.namef = namef;
        }

        @NonNull
        @Override
        public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_evento, null, false);

            view.setOnClickListener(this);


            return new EventoViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull final EventoViewHolder eventoViewHolder, final int position) {

            eventoViewHolder.descripcion.setText(listaEvento.get(position).getCampos
                    (EVENTO_DESCRIPCION));
            eventoViewHolder.telefono.setText(listaEvento.get(position).getCampos
                    (EVENTO_TELEFONO));
            eventoViewHolder.lugar.setText(listaEvento.get(position).getCampos
                    (EVENTO_LUGAR));
            eventoViewHolder.email.setText(listaEvento.get(position).getCampos
                    (EVENTO_EMAIL));
            eventoViewHolder.nomPryRel.setText(listaEvento.get(position).getCampos
                    (EVENTO_NOMPROYECTOREL));
            eventoViewHolder.nomCliRel.setText(listaEvento.get(position).getCampos
                    (EVENTO_NOMCLIENTEREL));
            eventoViewHolder.fechaini.setText(Utilidades.getDate(Long.parseLong(listaEvento.get(position).getCampos
                    (EVENTO_FECHAINIEVENTO))));
            eventoViewHolder.fechafin.setText(Utilidades.getDate(Long.parseLong(listaEvento.get(position).getCampos
                    (EVENTO_FECHAFINEVENTO))));
            eventoViewHolder.horaini.setText(Utilidades.getTime(Long.parseLong(listaEvento.get(position).getCampos
                    (EVENTO_HORAINIEVENTO))));
            eventoViewHolder.horafin.setText(Utilidades.getTime(Long.parseLong(listaEvento.get(position).getCampos
                    (EVENTO_HORAFINEVENTO))));
            eventoViewHolder.pbar.setProgress(Integer.parseInt(listaEvento.get(position).getCampos
                    (EVENTO_COMPLETADA)));
            eventoViewHolder.porccompleta.setText(String.format("%s %s",listaEvento.get(position).getCampos
                    (EVENTO_COMPLETADA),"%"));

            String tipoEvento = listaEvento.get(position).getCampos
                    (Contract.Tablas.EVENTO_TIPOEVENTO);

            eventoViewHolder.fechaini.setVisibility(View.GONE);
            eventoViewHolder.fechafin.setVisibility(View.GONE);
            eventoViewHolder.horaini.setVisibility(View.GONE);
            eventoViewHolder.horafin.setVisibility(View.GONE);
            eventoViewHolder.btnllamada.setVisibility(View.GONE);
            eventoViewHolder.btnmapa.setVisibility(View.GONE);
            eventoViewHolder.btnemail.setVisibility(View.GONE);
            eventoViewHolder.telefono.setVisibility(View.GONE);
            eventoViewHolder.lugar.setVisibility(View.GONE);
            eventoViewHolder.email.setVisibility(View.GONE);

            if (listaEvento.get(position).getCampos
                    (EVENTO_NOMCLIENTEREL)!=null){

                eventoViewHolder.nomCliRel.setVisibility(View.VISIBLE);
            }else{

                eventoViewHolder.nomCliRel.setVisibility(View.GONE);
            }
            if (listaEvento.get(position).getCampos
                    (EVENTO_NOMPROYECTOREL)!=null){

                eventoViewHolder.nomPryRel.setVisibility(View.VISIBLE);
            }else {

                eventoViewHolder.nomPryRel.setVisibility(View.GONE);
            }

            switch (tipoEvento){

                case TAREA:


                    break;

                case CITA:
                    eventoViewHolder.lugar.setVisibility(View.VISIBLE);
                    eventoViewHolder.fechaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.horaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.btnmapa.setVisibility(View.VISIBLE);
                    break;

                case EMAIL:
                    eventoViewHolder.btnemail.setVisibility(View.VISIBLE);
                    eventoViewHolder.fechaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.horaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.email.setVisibility(View.VISIBLE);
                    break;

                case LLAMADA:
                    eventoViewHolder.telefono.setVisibility(View.VISIBLE);
                    eventoViewHolder.fechaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.horaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.btnllamada.setVisibility(View.VISIBLE);
                    break;

                case EVENTO:
                    eventoViewHolder.fechaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.horaini.setVisibility(View.VISIBLE);
                    eventoViewHolder.fechafin.setVisibility(View.VISIBLE);
                    eventoViewHolder.horafin.setVisibility(View.VISIBLE);
                    break;

            }
            if (listaEvento.get(position).getCampos
                    (Contract.Tablas.EVENTO_RUTAFOTO)!=null) {
                eventoViewHolder.foto.setImageURI(Uri.parse(listaEvento.get(position).getCampos
                        (Contract.Tablas.EVENTO_RUTAFOTO)));
            }

            eventoViewHolder.btnllamada.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Common.AppActivity.hacerLlamada(Common.AppActivity.getAppContext()
                            ,eventoViewHolder.telefono.getText().toString());
                }
            });

            eventoViewHolder.btnemail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                }
            });

            eventoViewHolder.btnmapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                }
            });

            eventoViewHolder.completa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    ContentValues valores = new ContentValues();

                    valores.put(Contract.Tablas.EVENTO_COMPLETADA,"100");

                    Common.AppActivity.getAppContext().getContentResolver().update(Contract.crearUriTabla
                                    (listaEvento.get(position).getCampos
                                            (Contract.Tablas.EVENTO_ID_EVENTO),TABLA_EVENTO)
                            ,valores,null,null);

                }
            });

            eventoViewHolder.btneditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String idEvento = listaEvento.get(position).getCampos
                            (Contract.Tablas.EVENTO_ID_EVENTO);

                    Modelo evento = QueryDB.queryObject(CAMPOS_EVENTO,idEvento);

                    bundle = new Bundle();
                    bundle.putSerializable(TABLA_EVENTO,evento);
                    bundle.putString("namef",namef);
                    icFragments.enviarBundleAFragment(bundle, new FragmentUDEvento());
                    bundle = null;
                }
            });


        }

        @Override
        public int getItemCount() {

            return listaEvento.size();
        }

        @Override
        public void onClick(View v) {

            if (listener!=null){

                listener.onClick(v);
            }

        }

        class EventoViewHolder extends RecyclerView.ViewHolder {

            TextView descripcion,fechaini,telefono,lugar,nomPryRel,nomCliRel,
                    fechafin, horaini,horafin,porccompleta,email;
            ImageButton btnllamada, btnmapa, btnemail;
            ProgressBar pbar;
            ImageView foto,imgret;
            CheckBox completa;
            Button btneditar;

            public EventoViewHolder(@NonNull View itemView) {
                super(itemView);

                descripcion = itemView.findViewById(R.id.tvdesclevento);
                fechaini = itemView.findViewById(R.id.tvfinilevento);
                fechafin = itemView.findViewById(R.id.tvffinlevento);
                horaini = itemView.findViewById(R.id.tvhinilevento);
                horafin = itemView.findViewById(R.id.tvhfinlevento);
                telefono = itemView.findViewById(R.id.tvtelefonoevento);
                lugar = itemView.findViewById(R.id.tvlugarevento);
                email = itemView.findViewById(R.id.tvemaillevento);
                nomPryRel = itemView.findViewById(R.id.tvnompryrellevento);
                nomCliRel = itemView.findViewById(R.id.tvnomclirelevento);
                porccompleta = itemView.findViewById(R.id.tvcompporcevento);
                btnllamada = itemView.findViewById(R.id.imgbtnllamadaevento);
                btnmapa = itemView.findViewById(R.id.imgbtnmapaevento);
                btnemail = itemView.findViewById(R.id.imgbtnemaillevento);
                pbar = itemView.findViewById(R.id.pbarevento);
                foto = itemView.findViewById(R.id.imgnevento);
                imgret = itemView.findViewById(R.id.imgretrasoevento);
                completa = itemView.findViewById(R.id.cBoxcompletlevento);
                btneditar = itemView.findViewById(R.id.btneditevento);

            }
        }



    }

}
