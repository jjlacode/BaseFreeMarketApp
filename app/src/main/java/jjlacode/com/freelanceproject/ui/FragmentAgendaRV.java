package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.model.AgendaPresup;
import jjlacode.com.freelanceproject.model.AgendaTarea;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.AppActivity;
import jjlacode.com.freelanceproject.util.CommonPry;
import jjlacode.com.freelanceproject.util.FragmentBase;
import jjlacode.com.freelanceproject.util.ImagenUtil;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.Modelo;

import static jjlacode.com.freelanceproject.util.AppActivity.getAppContext;
import static jjlacode.com.freelanceproject.util.AppActivity.viewOnMapA;
import static jjlacode.com.freelanceproject.util.CommonPry.Constantes.PARTIDA;
import static jjlacode.com.freelanceproject.util.CommonPry.Constantes.PRESUPUESTO;
import static jjlacode.com.freelanceproject.util.CommonPry.Constantes.PROYECTO;
import static jjlacode.com.freelanceproject.util.CommonPry.TiposNota.NOTAAUDIO;
import static jjlacode.com.freelanceproject.util.CommonPry.TiposNota.NOTAIMAGEN;
import static jjlacode.com.freelanceproject.util.CommonPry.TiposNota.NOTATEXTO;
import static jjlacode.com.freelanceproject.util.CommonPry.TiposNota.NOTAVIDEO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.DIASLONG;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ID;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.IGUAL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.MODELO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ORIGEN;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.SUBTITULO;


public class FragmentAgendaRV extends FragmentBase implements CommonPry.TiposEvento, ContratoPry.Tablas {

    private String idProyecto;
    private String secuenciaPartida;

    RecyclerView rvAgenda;
    Button btnTareas;
    Button btnPresup;
    Button btnEspera;
    Button btnCobros;
    Button btnEventos;
    Button btnNotas;
    String namef;
    boolean[] expandido = new boolean[50];
    ArrayList<AgendaTarea> listaAgendaTarea = new ArrayList<>();
    ArrayList<AgendaPresup> listaAgendaPresup = new ArrayList<>();

    private static ConsultaBD consulta = new ConsultaBD();
    private String namesub;

    public FragmentAgendaRV() {
        // Required empty public constructor
    }

    @Override
    protected void setLayout() {

        layout = R.layout.fragment_agenda;

    }

    protected void enviarAct(){

        enviarBundle();
        icFragmentos.enviarBundleAActivity(bundle);
        System.out.println("bundle enviado a activityBase");
    }

    protected void enviarBundle(){

        bundle = new Bundle();
        bundle.putString(ORIGEN,namef);
        bundle.putString(SUBTITULO,namesub);

    }

    @Override
    protected void setInicio() {

        bundle = getArguments();
        if (bundle!=null){

            namef = bundle.getString(ORIGEN);
            namesub = bundle.getString(SUBTITULO);
            bundle=null;
        }

        rvAgenda = view.findViewById(R.id.rvagenda);
        if (!tablet) {
            rvAgenda.setLayoutManager(new LinearLayoutManager(getContext()));
        }else{
            rvAgenda.setLayoutManager(new GridLayoutManager(getContext(),3));
        }

        btnEventos = view.findViewById(R.id.btneventosagenda);
        btnTareas = view.findViewById(R.id.btntareaagenda);
        btnPresup = view.findViewById(R.id.btnpresupagenda);
        btnEspera = view.findViewById(R.id.btnenesperaagenda);
        btnCobros = view.findViewById(R.id.btnpendcobroagenda);
        btnNotas = view.findViewById(R.id.btnnotasagenda);

        if (namesub.equals(getString(R.string.proximos_eventos))) {
            clickEventos();
        }else if (namesub.equals(getString(R.string.notas))) {
            clickNotas();
        }else{
            clickEventos();
        }

        btnEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickEventos();
            }
        });

        btnNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickNotas();
            }
        });

        btnTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickTareas();
            }
        });

        btnPresup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickPresup();
            }
        });

        btnEspera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickEspera();
            }
        });

        btnCobros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickCobros();
            }
        });

    }

    private void clickCobros() {

        btnEspera.setVisibility(View.VISIBLE);
        btnCobros.setBackgroundResource(0);
        btnEspera.setText("");
        btnEspera.setBackgroundResource(R.drawable.ic_fast_rewind_white_24dp);
        btnPresup.setVisibility(View.GONE);
        btnTareas.setVisibility(View.GONE);
        btnEventos.setVisibility(View.GONE);
        btnNotas.setVisibility(View.GONE);
        btnCobros.setText(getString(R.string.pendiente_cobro));
        namesub = getString(R.string.pendiente_cobro);
        enviarAct();
        obtenerPresupPendienteCobro();
    }

    private void clickEspera() {

        btnPresup.setVisibility(View.VISIBLE);
        btnCobros.setVisibility(View.VISIBLE);
        btnEspera.setBackgroundResource(0);
        btnPresup.setText("");
        btnPresup.setBackgroundResource(R.drawable.ic_fast_rewind_white_24dp);
        btnCobros.setText("");
        btnCobros.setBackgroundResource(R.drawable.ic_fast_forward_white_24dp);
        btnEventos.setVisibility(View.GONE);
        btnTareas.setVisibility(View.GONE);
        btnNotas.setVisibility(View.GONE);
        btnEspera.setText( getString(R.string.presupuestos_en_espera));
        namesub = getString(R.string.presupuestos_en_espera);
        enviarAct();
        obtenerPresupEspera();
    }

    private void clickPresup() {

        btnEspera.setVisibility(View.VISIBLE);
        btnTareas.setVisibility(View.VISIBLE);
        btnPresup.setBackgroundResource(0);
        btnTareas.setText("");
        btnTareas.setBackgroundResource(R.drawable.ic_fast_rewind_white_24dp);
        btnEspera.setText("");
        btnEspera.setBackgroundResource(R.drawable.ic_fast_forward_white_24dp);
        btnEventos.setVisibility(View.GONE);
        btnCobros.setVisibility(View.GONE);
        btnNotas.setVisibility(View.GONE);
        btnPresup.setText(getString(R.string.presupuestos_pendientes));
        namesub = getString(R.string.presupuestos_pendientes);
        enviarAct();
        obtenerPresupPendienteEntrega();
    }

    private void clickTareas() {

        btnPresup.setVisibility(View.VISIBLE);
        btnNotas.setVisibility(View.VISIBLE);
        btnTareas.setText(getString(R.string.tareas_pendientes));
        btnTareas.setBackgroundResource(0);
        btnNotas.setText("");
        btnNotas.setBackgroundResource(R.drawable.ic_fast_rewind_white_24dp);
        btnPresup.setText("");
        btnPresup.setBackgroundResource(R.drawable.ic_fast_forward_white_24dp);
        btnEspera.setVisibility(View.GONE);
        btnCobros.setVisibility(View.GONE);
        btnEventos.setVisibility(View.GONE);
        namesub = getString(R.string.tareas_pendientes);
        enviarAct();
        obtenerTareas();
    }

    private void clickNotas() {

        btnEventos.setVisibility(View.VISIBLE);
        btnTareas.setVisibility(View.VISIBLE);
        btnNotas.setText(getString(R.string.notas));
        btnNotas.setBackgroundResource(0);
        btnEventos.setText("");
        btnEventos.setBackgroundResource(R.drawable.ic_fast_rewind_white_24dp);
        btnTareas.setText("");
        btnTareas.setBackgroundResource(R.drawable.ic_fast_forward_white_24dp);
        btnPresup.setVisibility(View.GONE);
        btnEspera.setVisibility(View.GONE);
        btnCobros.setVisibility(View.GONE);
        namesub = getString(R.string.notas);
        enviarAct();
        obtenerNotas();
    }

    private void clickEventos() {

        btnNotas.setVisibility(View.VISIBLE);
        btnNotas.setText("");
        btnEventos.setBackgroundResource(0);
        btnNotas.setBackgroundResource(R.drawable.ic_fast_forward_white_24dp);
        btnEventos.setText(getString(R.string.proximos_eventos));
        btnEspera.setVisibility(View.GONE);
        btnCobros.setVisibility(View.GONE);
        btnPresup.setVisibility(View.GONE);
        btnTareas.setVisibility(View.GONE);
        namesub = getString(R.string.proximos_eventos);
        enviarAct();
        obtenerEventos();
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    private void obtenerNotas() {

        final ArrayList<Modelo> listaNotas = consulta.queryList(CAMPOS_NOTA,NOTA_ID_RELACIONADO,null,null,IGUAL,null);

        AdaptadorRVNotas adaptadorRV = new AdaptadorRVNotas(listaNotas);
        rvAgenda.setAdapter(adaptadorRV);

        adaptadorRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Modelo nota = listaNotas.get(rvAgenda.getChildAdapterPosition(v));
                bundle = new Bundle();
                bundle.putString(SUBTITULO,namef);
                bundle.putSerializable(MODELO,nota);
                bundle.putString(ID,nota.getString(NOTA_ID_NOTA));
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
            }
        });
    }

    private void obtenerTareas(){

        listaAgendaPresup = new ArrayList<>();
        listaAgendaTarea = new ArrayList<>();


                ArrayList<Modelo> lista ;
                ArrayList<Modelo> listaPartidasSinCompletar = new ArrayList<>();

                lista = consulta.queryList(CAMPOS_PARTIDA);

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
                    agendaTarea.setTiempo(partida.getCampos(PARTIDA_PRECIO));
                    agendaTarea.setCompletada(partida.getCampos(PARTIDA_COMPLETADA));
                    agendaTarea.setIdEstado(partida.getCampos(PARTIDA_ID_ESTADO));
                    agendaTarea.setTipoEstado(partida.getCampos(PARTIDA_TIPO_ESTADO));
                    agendaTarea.setRetraso(partida.getCampos(PARTIDA_PROYECTO_RETRASO));

                    Modelo proyecto = consulta.queryObject
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

                    Modelo cliente = consulta.queryObject
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

                Modelo partida = consulta.queryObjectDetalle(CAMPOS_PARTIDA,idProyecto,secuenciaPartida);

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PARTIDA,partida);
                bundle.putString(ORIGEN,PARTIDA);
                bundle.putString(SUBTITULO,PROYECTO);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCRUDPartidaProyecto());
                bundle = null;
            }
        });
    }
    private void obtenerPresupPendienteEntrega(){
        listaAgendaPresup = new ArrayList<>();
        listaAgendaTarea = new ArrayList<>();

        String seleccion = ESTADO_TIPOESTADO + " = 1 OR "+
                ESTADO_TIPOESTADO + " = 2";
        String orden = "'"+ PROYECTO_FECHAENTRADA+"' ASC";

        ArrayList<Modelo> lista = null;

        lista = consulta.queryList(CAMPOS_PROYECTO,seleccion,orden);

        for (Modelo proyecto : lista) {

            AgendaPresup agendaPresup = new AgendaPresup();
            agendaPresup.setId_presupuesto(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_ID_PROYECTO));
            agendaPresup.setPresupuesto(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_NOMBRE));
            agendaPresup.setDescripcion(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_DESCRIPCION));
            agendaPresup.setCliente(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_CLIENTE_NOMBRE));
            agendaPresup.setEstado(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_DESCRIPCION_ESTADO));
            agendaPresup.setFechaEntrada(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAENTRADA));
            agendaPresup.setFechaEntregaPresup(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAENTREGAPRESUP));
            agendaPresup.setFechaAcordada(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAENTREGAACORDADA));
            agendaPresup.setFechaCalculada(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAENTREGACALCULADA));
            agendaPresup.setFechaFinal(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAFINAL));
            agendaPresup.setPeso(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_CLIENTE_PESOTIPOCLI));
            agendaPresup.setRetraso(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_RETRASO));
            agendaPresup.setRutaFoto(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_RUTAFOTO));
            agendaPresup.setTipoEstado(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_TIPOESTADO));

            listaAgendaPresup.add(agendaPresup);
        }

        AdaptadorAgendaPresup adaptadorAgendaPresup = new AdaptadorAgendaPresup(listaAgendaPresup);

        rvAgenda.setAdapter(adaptadorAgendaPresup);

        adaptadorAgendaPresup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idProyecto = listaAgendaPresup.get(rvAgenda.getChildAdapterPosition(v)).getId_presupuesto();

                Modelo proyecto =  consulta.queryObject(CAMPOS_PROYECTO,idProyecto);

                bundle = new Bundle();
                bundle.putSerializable(MODELO,proyecto);
                bundle.putString(ORIGEN,PRESUPUESTO);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCRUDProyecto());
                bundle = null;

            }
        });
    }

    private void obtenerPresupPendienteCobro(){

        listaAgendaPresup = new ArrayList<>();
        listaAgendaTarea = new ArrayList<>();

        String seleccion = ESTADO_TIPOESTADO + " = 7";
        String orden = "'"+ PROYECTO_FECHAFINAL+"' ASC";

        ArrayList<Modelo> lista = null;

        lista = consulta.queryList(CAMPOS_PROYECTO,seleccion,orden);

        for (Modelo proyecto : lista) {

            AgendaPresup agendaPresup = new AgendaPresup();
            agendaPresup.setId_presupuesto(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_ID_PROYECTO));
            agendaPresup.setPresupuesto(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_NOMBRE));
            agendaPresup.setDescripcion(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_DESCRIPCION));
            agendaPresup.setCliente(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_CLIENTE_NOMBRE));
            agendaPresup.setEstado(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_DESCRIPCION_ESTADO));
            agendaPresup.setFechaEntrada(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAENTRADA));
            agendaPresup.setFechaEntregaPresup(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAENTREGAPRESUP));
            agendaPresup.setFechaAcordada(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAENTREGAACORDADA));
            agendaPresup.setFechaCalculada(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAENTREGACALCULADA));
            agendaPresup.setFechaFinal(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAFINAL));
            agendaPresup.setPeso(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_CLIENTE_PESOTIPOCLI));
            agendaPresup.setRetraso(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_RETRASO));
            agendaPresup.setRutaFoto(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_RUTAFOTO));
            agendaPresup.setTipoEstado(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_TIPOESTADO));

            listaAgendaPresup.add(agendaPresup);
        }

        AdaptadorAgendaPresup adaptadorAgendaPresup = new AdaptadorAgendaPresup(listaAgendaPresup);

        rvAgenda.setAdapter(adaptadorAgendaPresup);

        adaptadorAgendaPresup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idProyecto = listaAgendaPresup.get(rvAgenda.getChildAdapterPosition(v)).getId_presupuesto();

                Modelo proyecto =  consulta.queryObject(CAMPOS_PROYECTO,idProyecto);

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString(ORIGEN,PROYECTO);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCRUDProyecto());
                bundle = null;

            }
        });
    }

    private void obtenerPresupEspera(){

        listaAgendaPresup = new ArrayList<>();
        listaAgendaTarea = new ArrayList<>();

        String seleccion = ESTADO_TIPOESTADO + " = 3";
        String orden = "'"+ PROYECTO_FECHAENTREGAPRESUP+"' ASC";

        ArrayList<Modelo> lista = null;

        lista = consulta.queryList(CAMPOS_PROYECTO,seleccion,orden);

        for (Modelo proyecto : lista) {

            AgendaPresup agendaPresup = new AgendaPresup();
            agendaPresup.setId_presupuesto(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_ID_PROYECTO));
            agendaPresup.setPresupuesto(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_NOMBRE));
            agendaPresup.setDescripcion(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_DESCRIPCION));
            agendaPresup.setCliente(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_CLIENTE_NOMBRE));
            agendaPresup.setEstado(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_DESCRIPCION_ESTADO));
            agendaPresup.setFechaEntrada(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAENTRADA));
            agendaPresup.setFechaEntregaPresup(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAENTREGAPRESUP));
            agendaPresup.setFechaAcordada(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAENTREGAACORDADA));
            agendaPresup.setFechaCalculada(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAENTREGACALCULADA));
            agendaPresup.setFechaFinal(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_FECHAFINAL));
            agendaPresup.setPeso(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_CLIENTE_PESOTIPOCLI));
            agendaPresup.setRetraso(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_RETRASO));
            agendaPresup.setRutaFoto(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_RUTAFOTO));
            agendaPresup.setTipoEstado(proyecto.getCampos(ContratoPry.Tablas.PROYECTO_TIPOESTADO));

            listaAgendaPresup.add(agendaPresup);
        }

        AdaptadorAgendaPresup adaptadorAgendaPresup = new AdaptadorAgendaPresup(listaAgendaPresup);

        rvAgenda.setAdapter(adaptadorAgendaPresup);

        adaptadorAgendaPresup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idProyecto = listaAgendaPresup.get(rvAgenda.getChildAdapterPosition(v)).getId_presupuesto();

                Modelo proyecto =  consulta.queryObject(CAMPOS_PROYECTO,idProyecto);

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString(ORIGEN,PRESUPUESTO);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCRUDProyecto());
                bundle = null;

            }
        });
    }

    private void obtenerEventos() {

        long diasfuturosEventos = JavaUtil.hoy() + (CommonPry.diasfuturos * DIASLONG);
        long diaspasadosEventos = JavaUtil.hoy() - (CommonPry.diaspasados * DIASLONG);

        ArrayList<Modelo> listaEventos = new ArrayList<>();
        ArrayList<Modelo> lista = consulta.queryList(CAMPOS_EVENTO, null, null);
        int i = 0;
        for (Modelo item : lista) {

            System.out.println("lista.get(i).getTipoevento() = " + lista.get(i).getCampos
                    (ContratoPry.Tablas.EVENTO_TIPOEVENTO));

            if ((lista.get(i).getCampos(ContratoPry.Tablas.EVENTO_TIPOEVENTO) != null &&
                    lista.get(i).getCampos(ContratoPry.Tablas.EVENTO_TIPOEVENTO).equals(TAREA) &&
                    Double.parseDouble(lista.get(i).getCampos(ContratoPry.Tablas.EVENTO_COMPLETADA)) < 100)
                    || (item.getCampos(ContratoPry.Tablas.EVENTO_FECHAINIEVENTO) != null &&
                    Long.parseLong(item.getCampos(ContratoPry.Tablas.EVENTO_FECHAINIEVENTO)) > diaspasadosEventos &&
                    Long.parseLong(item.getCampos(ContratoPry.Tablas.EVENTO_FECHAINIEVENTO)) < diasfuturosEventos &&
                    Double.parseDouble(lista.get(i).getCampos(ContratoPry.Tablas.EVENTO_COMPLETADA)) < 100)) {

                listaEventos.add(item);
            }
            i++;
        }

        AdaptadorEventoInt adaptadorEvento = new AdaptadorEventoInt(listaEventos,namef);
        rvAgenda.setAdapter(adaptadorEvento);
    }

    class AdaptadorEventoInt extends RecyclerView.Adapter<AdaptadorEventoInt.EventoViewHolder>
            implements CommonPry.TiposEvento,View.OnClickListener {

        ArrayList<Modelo> listaEvento;
        private View.OnClickListener listener;
        private String namef;

        AdaptadorEventoInt(ArrayList<Modelo> listaEvento, String namef) {

            this.listaEvento = listaEvento;
            this.namef = namef;
        }

        @NonNull
        @Override
        public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_evento_agenda, null, false);

            view.setOnClickListener(this);


            return new EventoViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull final EventoViewHolder eventoViewHolder, final int position) {

            eventoViewHolder.tipo.setText(listaEvento.get(position).getString(EVENTO_TIPOEVENTO).toUpperCase());
            eventoViewHolder.descripcion.setText(listaEvento.get(position).getString(EVENTO_DESCRIPCION));
            eventoViewHolder.telefono.setText(listaEvento.get(position).getString(EVENTO_TELEFONO));
            eventoViewHolder.lugar.setText(listaEvento.get(position).getCampos
                    (EVENTO_LUGAR));
            eventoViewHolder.email.setText(listaEvento.get(position).getCampos
                    (EVENTO_EMAIL));
            eventoViewHolder.nomPryRel.setText(listaEvento.get(position).getCampos
                    (EVENTO_NOMPROYECTOREL));
            eventoViewHolder.nomCliRel.setText(listaEvento.get(position).getCampos
                    (EVENTO_NOMCLIENTEREL));
            eventoViewHolder.fechaini.setText(JavaUtil.getDate(Long.parseLong(listaEvento.get(position).getCampos
                    (EVENTO_FECHAINIEVENTO))));
            eventoViewHolder.fechafin.setText(JavaUtil.getDate(Long.parseLong(listaEvento.get(position).getCampos
                    (EVENTO_FECHAFINEVENTO))));
            eventoViewHolder.horaini.setText(JavaUtil.getTime(Long.parseLong(listaEvento.get(position).getCampos
                    (EVENTO_HORAINIEVENTO))));
            eventoViewHolder.horafin.setText(JavaUtil.getTime(Long.parseLong(listaEvento.get(position).getCampos
                    (EVENTO_HORAFINEVENTO))));
            if (Integer.parseInt(listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_COMPLETADA))==0){
                eventoViewHolder.pbar.setVisibility(View.GONE);
                eventoViewHolder.porccompleta.setVisibility(View.GONE);
            }else if (Integer.parseInt(listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_COMPLETADA))>99) {
                eventoViewHolder.completa.setChecked(true);
            }else{
                eventoViewHolder.pbar.setProgress(Integer.parseInt(listaEvento.get(position).getCampos
                        (ContratoPry.Tablas.EVENTO_COMPLETADA)));
                eventoViewHolder.porccompleta.setText(String.format("%s %s",listaEvento.get(position).getCampos
                        (EVENTO_COMPLETADA),"%"));
            }
            ImagenUtil imagenUtil = new ImagenUtil(getContext());
            if (listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_RUTAFOTO)!=null) {
                imagenUtil.setImageUriCircle(listaEvento.get(position).getCampos
                        (ContratoPry.Tablas.EVENTO_RUTAFOTO),eventoViewHolder.foto);
                //eventoViewHolder.foto.setImageURI(Uri.parse(list.get(position).getCampos
                //        (ContratoPry.Tablas.EVENTO_RUTAFOTO)));
            }


            if (!expandido[position]) {

                //eventoViewHolder.layoutimg.setVisibility(View.GONE);
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
                eventoViewHolder.btneditar.setVisibility(View.GONE);
                //eventoViewHolder.imgret.setVisibility(View.GONE);
                //eventoViewHolder.foto.setVisibility(View.GONE);
                eventoViewHolder.completa.setVisibility(View.GONE);
                eventoViewHolder.porccompleta.setVisibility(View.GONE);
            }

            final String tipoEvento = listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_TIPOEVENTO);

            eventoViewHolder.expandir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    expandido[position]= !expandido[position];


            if (!expandido[position]) {

                //eventoViewHolder.layoutimg.setVisibility(View.GONE);
                eventoViewHolder.expandir.setImageResource(android.R.drawable.ic_input_add);
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
                eventoViewHolder.btneditar.setVisibility(View.GONE);
                //eventoViewHolder.imgret.setVisibility(View.GONE);
                //eventoViewHolder.foto.setVisibility(View.GONE);
                eventoViewHolder.completa.setVisibility(View.GONE);
                eventoViewHolder.porccompleta.setVisibility(View.GONE);
            }else {

                eventoViewHolder.expandir.setImageResource(android.R.drawable.ic_delete);

                eventoViewHolder.layoutimg.setVisibility(View.VISIBLE);
                eventoViewHolder.btneditar.setVisibility(View.VISIBLE);
                //eventoViewHolder.imgret.setVisibility(View.VISIBLE);
                eventoViewHolder.foto.setVisibility(View.VISIBLE);
                eventoViewHolder.completa.setVisibility(View.VISIBLE);

                if (Integer.parseInt(listaEvento.get(position).getCampos
                        (ContratoPry.Tablas.EVENTO_COMPLETADA))==0){
                    eventoViewHolder.pbar.setVisibility(View.GONE);
                    eventoViewHolder.porccompleta.setVisibility(View.GONE);
                }else if (Integer.parseInt(listaEvento.get(position).getCampos
                        (ContratoPry.Tablas.EVENTO_COMPLETADA))>99) {
                    eventoViewHolder.completa.setChecked(true);
                }else{
                    eventoViewHolder.pbar.setProgress(Integer.parseInt(listaEvento.get(position).getCampos
                            (ContratoPry.Tablas.EVENTO_COMPLETADA)));
                    eventoViewHolder.porccompleta.setVisibility(View.VISIBLE);
                    eventoViewHolder.porccompleta.setText(String.format("%s %s",listaEvento.get(position).getCampos
                            (EVENTO_COMPLETADA),"%"));
                }
                ImagenUtil imagenUtil = new ImagenUtil(getContext());
                if (listaEvento.get(position).getCampos
                        (ContratoPry.Tablas.EVENTO_RUTAFOTO)!=null) {
                    imagenUtil.setImageUriCircle(listaEvento.get(position).getCampos
                            (ContratoPry.Tablas.EVENTO_RUTAFOTO),eventoViewHolder.foto);
                    //eventoViewHolder.foto.setImageURI(Uri.parse(list.get(position).getCampos
                    //        (ContratoPry.Tablas.EVENTO_RUTAFOTO)));
                }
                if (listaEvento.get(position).getCampos
                        (EVENTO_NOMCLIENTEREL) != null) {

                    eventoViewHolder.nomCliRel.setVisibility(View.VISIBLE);
                } else {

                    eventoViewHolder.nomCliRel.setVisibility(View.GONE);
                }
                if (listaEvento.get(position).getCampos
                        (EVENTO_NOMPROYECTOREL) != null) {

                    eventoViewHolder.nomPryRel.setVisibility(View.VISIBLE);
                } else {

                    eventoViewHolder.nomPryRel.setVisibility(View.GONE);
                }

                switch (tipoEvento) {

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
            }
                }
            });


            long retraso = JavaUtil.hoy()-listaEvento.get(position).getLong(EVENTO_FECHAINIEVENTO);
            if (retraso > 3 * DIASLONG){eventoViewHolder.card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));}
            else if (retraso > DIASLONG){eventoViewHolder.card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));}
            else {eventoViewHolder.card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));}//imgret.setImageResource(R.drawable.alert_box_v);}
            if(tipoEvento.equals(TAREA))
            {eventoViewHolder.card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_tarea));}
            eventoViewHolder.btneditar.setText("EDITAR "+ tipoEvento.toUpperCase());

            eventoViewHolder.btnllamada.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppActivity.hacerLlamada(AppActivity.getAppContext()
                            ,eventoViewHolder.telefono.getText().toString());
                }
            });

            eventoViewHolder.btnemail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppActivity.enviarEmail(getContext(),eventoViewHolder.email.getText().toString());

                }
            });

            eventoViewHolder.btnmapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!eventoViewHolder.lugar.getText().toString().equals("")){

                        viewOnMapA(getContext(),eventoViewHolder.lugar.getText().toString());

                    }

                }
            });

            eventoViewHolder.completa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {

                        ContentValues valores = new ContentValues();

                        consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_COMPLETADA, "100");
                        consulta.updateRegistro(TABLA_EVENTO, listaEvento.get(position).getString
                                (EVENTO_ID_EVENTO), valores);
                        eventoViewHolder.completa.setVisibility(View.GONE);
                        eventoViewHolder.porccompleta.setText("100%");
                    }

                }
            });

            eventoViewHolder.btneditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String idEvento = listaEvento.get(position).getCampos
                            (ContratoPry.Tablas.EVENTO_ID_EVENTO);

                    Modelo evento = listaEvento.get(position);

                    bundle = new Bundle();
                    bundle.putSerializable(MODELO,evento);
                    bundle.putString(SUBTITULO,EVENTO);
                    bundle.putString(ID,idEvento);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
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
                    fechafin, horaini,horafin,porccompleta,email,tipo;
            ImageButton btnllamada, btnmapa, btnemail;
            ProgressBar pbar;
            ImageView foto,imgret,expandir;
            CheckBox completa;
            Button btneditar;
            CardView card;
            LinearLayout layoutimg;

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
                foto = itemView.findViewById(R.id.imglevento);
                completa = itemView.findViewById(R.id.cBoxcompletlevento);
                btneditar = itemView.findViewById(R.id.btneditevento);
                tipo = itemView.findViewById(R.id.tvtipolevento);
                card = itemView.findViewById(R.id.cardlevento);
                expandir = itemView.findViewById(R.id.imgexpandirevento);
                layoutimg = itemView.findViewById(R.id.linearimgagenda);

            }
        }



    }

    public static class AdaptadorAgendaPresup extends RecyclerView.Adapter<AdaptadorAgendaPresup.AgendaPresupViewHolder> implements View.OnClickListener {

        ArrayList<AgendaPresup> listaAgendaPresup;
        private View.OnClickListener listener;

        public AdaptadorAgendaPresup(ArrayList<AgendaPresup> listaAgendaPresup) {

            this.listaAgendaPresup = listaAgendaPresup;
        }

        @NonNull
        @Override
        public AgendaPresupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_agenda_presup, null, false);

            view.setOnClickListener(this);


            return new AgendaPresupViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull AgendaPresupViewHolder agendaPresupViewHolder, int position) {

                agendaPresupViewHolder.descripcion.setText(listaAgendaPresup.get(position).getDescripcion());
                agendaPresupViewHolder.presupuesto.setText(listaAgendaPresup.get(position).getPresupuesto());
                agendaPresupViewHolder.cliente.setText(listaAgendaPresup.get(position).getCliente());
                agendaPresupViewHolder.estado.setText(listaAgendaPresup.get(position).getEstado());
                String tipoEstado = listaAgendaPresup.get(position).getTipoEstado();
                if (Integer.parseInt(tipoEstado) >0 && Integer.parseInt(tipoEstado) <3) {
                    agendaPresupViewHolder.fecha.setText(JavaUtil.getDate(Long.parseLong(listaAgendaPresup.get(position).getFechaEntrada())));
                }else if (Integer.parseInt(tipoEstado) == 3) {
                    agendaPresupViewHolder.fecha.setText(JavaUtil.getDate(Long.parseLong(listaAgendaPresup.get(position).getFechaEntregaPresup())));
                }else if (Integer.parseInt(tipoEstado) == 7){
                    agendaPresupViewHolder.fecha.setText(JavaUtil.getDate(Long.parseLong(listaAgendaPresup.get(position).getFechaFinal())));
                }

                if (listaAgendaPresup.get(position).getRutaFoto()!=null){

                    //agendaPresupViewHolder.imagenPresup.setImageURI(Uri.parse(listaAgendaPresup.get(position).getRutaFoto()));
                    ImagenUtil imagenUtil = new ImagenUtil(getAppContext());
                    imagenUtil.setImageUriCircle(listaAgendaPresup.get(position).getRutaFoto(),agendaPresupViewHolder.imagenPresup);

                }
            String retraso = listaAgendaPresup.get(position).getRetraso();
            if (Long.parseLong(retraso) > 3 * DIASLONG) {
                agendaPresupViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_r);
            } else if (Long.parseLong(retraso) > DIASLONG) {
                agendaPresupViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_a);
            } else {
                agendaPresupViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_v);
            }

            String peso = listaAgendaPresup.get(position).getPeso();
            if (Integer.parseInt(peso) >6) {
                agendaPresupViewHolder.imagenCliente.setImageResource(R.drawable.clientev);
            } else if (Integer.parseInt(peso) > 3) {
                agendaPresupViewHolder.imagenCliente.setImageResource(R.drawable.clientea);
            } else if (Integer.parseInt(peso) > 0) {
                agendaPresupViewHolder.imagenCliente.setImageResource(R.drawable.clienter);
            } else {
                agendaPresupViewHolder.imagenCliente.setImageResource(R.drawable.cliente);
            }
        }

        @Override
        public int getItemCount() {

            return listaAgendaPresup.size();
        }

        @Override
        public void onClick(View v) {

            if (listener != null) {

                listener.onClick(v);


            }

        }

        public class AgendaPresupViewHolder extends RecyclerView.ViewHolder {

                ImageView imagenPresup, imagenCliente, imagenEstado;
                TextView presupuesto, descripcion,cliente,estado,fecha;

            public AgendaPresupViewHolder(@NonNull View itemView) {
                super(itemView);

                descripcion = itemView.findViewById(R.id.tvdescagendapresup);
                presupuesto = itemView.findViewById(R.id.tvpresupagendapresup);
                cliente = itemView.findViewById(R.id.tvclienteagendapresup);
                estado = itemView.findViewById(R.id.tvestadoagendapresup);
                fecha = itemView.findViewById(R.id.tvfechaentagendapresup);
                imagenPresup = itemView.findViewById(R.id.imgrvagendapresup);
                imagenCliente = itemView.findViewById(R.id.imgcliagendapresup);
                imagenEstado = itemView.findViewById(R.id.imgestadoagendapresup);

            }
        }
    }

    public static class AdaptadorAgendaTareas extends RecyclerView.Adapter<AdaptadorAgendaTareas.AgendaViewHolder> implements View.OnClickListener{

        ArrayList<AgendaTarea> listaPartidas;
        private View.OnClickListener listener;

        public AdaptadorAgendaTareas(ArrayList<AgendaTarea> listaPartidas) {

            this.listaPartidas = listaPartidas;
        }

        @NonNull
        @Override
        public AgendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_agenda_tareas,null,false);

            view.setOnClickListener(this);


            return new AgendaViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull AgendaViewHolder agendaViewHolder, int position) {

            agendaViewHolder.descripcionPartida.setText(listaPartidas.get(position).getDescripcion());
            agendaViewHolder.tiempoPartida.setText(listaPartidas.get(position).getTiempo());
            agendaViewHolder.cantidadPartida.setText(listaPartidas.get(position).getCantidad());
            agendaViewHolder.completadaPartida.setText(listaPartidas.get(position).getCompletada());
            agendaViewHolder.proyectoPartida.setText(listaPartidas.get(position).getNombreProyecto());
            agendaViewHolder.progressBarPartida.setProgress(Integer.parseInt(listaPartidas.get(position).getCompletada()));
            agendaViewHolder.nombreCliente.setText(listaPartidas.get(position).getNombreCliente());
            agendaViewHolder.tipoCliente.setText(listaPartidas.get(position).getTipoCliente());

            if (listaPartidas.get(position).getRutaFoto()!=null) {
                agendaViewHolder.imagenProyecto.setImageURI(Uri.parse(listaPartidas.get(position).getRutaFoto()));
                ImagenUtil imagenUtil = new ImagenUtil(getAppContext());
                imagenUtil.setImageUriCircle(listaPartidas.get(position).getRutaFoto(),agendaViewHolder.imagenProyecto);

            }
            long retraso = Long.parseLong(listaPartidas.get(position).getRetraso());
            if (retraso > 3 * DIASLONG) {
                agendaViewHolder.imagenPartida.setImageResource(R.drawable.alert_box_r);
            } else if (retraso > DIASLONG) {
                agendaViewHolder.imagenPartida.setImageResource(R.drawable.alert_box_a);
            } else {
                agendaViewHolder.imagenPartida.setImageResource(R.drawable.alert_box_v);
            }

            String peso = listaPartidas.get(position).getPeso();
            if (Integer.parseInt(peso) >6) {
                agendaViewHolder.imagenCliente.setImageResource(R.drawable.clientev);
            } else if (Integer.parseInt(peso) > 3) {
                agendaViewHolder.imagenCliente.setImageResource(R.drawable.clientea);
            } else if (Integer.parseInt(peso) > 0) {
            agendaViewHolder.imagenCliente.setImageResource(R.drawable.clienter);
            } else {
                agendaViewHolder.imagenCliente.setImageResource(R.drawable.cliente);
            }


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

        public class AgendaViewHolder extends RecyclerView.ViewHolder {

            ImageView imagenPartida, imagenProyecto, imagenCliente;
            TextView descripcionPartida,tiempoPartida,cantidadPartida,completadaPartida,
                    nombreCliente,tipoCliente,proyectoPartida;
            ProgressBar progressBarPartida;

            public AgendaViewHolder(@NonNull View itemView) {
                super(itemView);

                imagenPartida = itemView.findViewById(R.id.imgparagenda);
                imagenProyecto = itemView.findViewById(R.id.imgpryagenda);
                imagenCliente = itemView.findViewById(R.id.imgtipocliagenda);
                tipoCliente = itemView.findViewById(R.id.tvtipocliagenda);
                nombreCliente = itemView.findViewById(R.id.tvclienteagenda);
                proyectoPartida = itemView.findViewById(R.id.tvnomproyagenda);
                descripcionPartida = itemView.findViewById(R.id.tvdescripcioncpartidaagenda);
                tiempoPartida = itemView.findViewById(R.id.tvtiempopartidaagenda);
                cantidadPartida = itemView.findViewById(R.id.tvcantidadpartidaagenda);
                completadaPartida = itemView.findViewById(R.id.tvcompletadapartidaagenda);
                progressBarPartida = itemView.findViewById(R.id.progressBarpartidaagenda);

            }
        }
    }

    public static class AdaptadorRVNotas extends RecyclerView.Adapter<AdaptadorRVNotas.ViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas {

        ArrayList<Modelo> list;
        private View.OnClickListener listener;
        private boolean reproduciendo;
        private boolean grabando;
        private MediaPlayer play;
        private MediaRecorder rec;
        private String path;

        public AdaptadorRVNotas(ArrayList<Modelo> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_nota, null, false);

            view.setOnClickListener(this);


            return new ViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

            viewHolder.descripcion.setText(list.get(position).getString(NOTA_DESCRIPCION));
            viewHolder.fecha.setText(JavaUtil.getDateTime(list.get(position).getLong(NOTA_FECHA)));
            String tipo = list.get(position).getString(NOTA_TIPO);
            viewHolder.tipoNota.setText(tipo);
            viewHolder.playAudio.setVisibility(View.GONE);
            viewHolder.playVideo.setVisibility(View.GONE);
            viewHolder.imagen.setVisibility(View.GONE);

            if (tipo!=null) {


                switch (tipo) {

                    case NOTATEXTO:

                        break;

                    case NOTAAUDIO:

                        viewHolder.playAudio.setVisibility(View.VISIBLE);
                        path = list.get(position).getString(NOTA_RUTA);

                        break;

                    case NOTAVIDEO:

                        viewHolder.playVideo.setVisibility(View.VISIBLE);
                        path = list.get(position).getString(NOTA_RUTA);

                        break;

                    case NOTAIMAGEN:

                        viewHolder.imagen.setVisibility(View.VISIBLE);
                        ImagenUtil imagenUtil = new ImagenUtil(AppActivity.getAppContext());
                        imagenUtil.setImageUriCircle(list.get(position).getString(NOTA_RUTA), viewHolder.imagen);
                }
            }
            viewHolder.playAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    reproduciendo = !reproduciendo;
                    AppActivity.reproducirAudio(play,path,reproduciendo);
                    if (reproduciendo){
                        viewHolder.playAudio.setText("Stop");
                    }else{
                        viewHolder.playAudio.setText("Reproducir audio");
                    }
                }
            });




        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onClick(View v) {

            if (listener != null) {

                listener.onClick(v);


            }

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView descripcion,fecha,tipoNota;
            Button playAudio,playVideo;
            ImageView imagen;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tipoNota = itemView.findViewById(R.id.tvtipo_lnota);
                descripcion = itemView.findViewById(R.id.tvdesc_lnota);
                imagen = itemView.findViewById(R.id.imagen_lnota);
                playAudio =itemView.findViewById(R.id.btn_play_audiol);
                playVideo = itemView.findViewById(R.id.btn_play_videol);
                fecha = itemView.findViewById(R.id.tvfechalnota);



            }
        }
    }
}
