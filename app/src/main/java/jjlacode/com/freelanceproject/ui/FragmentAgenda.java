package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.FragmentBase;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.ListaModelo;
import jjlacode.com.freelanceproject.util.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.CommonPry;

import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ACTUAL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.DIASLONG;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.IGUAL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.LISTA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ORIGEN;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.SUBTITULO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.VERLISTA;


public class FragmentAgenda extends FragmentBase implements CommonPry.TiposEvento,
        ContratoPry.Tablas, CommonPry.Constantes {


    private String origen;
    private ImageView proxEvent;
    private ImageView notasGen;
    private ImageView partidasPend;
    private ImageView presupEspera;
    private ImageView presupEntrega;
    private ImageView presupCobros;
    private ImageView calendario;
    private TextView tvProxEvent;
    private TextView tvNotasGen;
    private TextView tvPartidasPend;
    private TextView tvPresupEspera;
    private TextView tvPresupEntrega;
    private TextView tvPresupCobros;
    private TextView tvcalendario;


    private static ConsultaBD consulta = new ConsultaBD();

    public FragmentAgenda() {
        // Required empty public constructor
    }

    @Override
    protected void setLayout() {

        layout = R.layout.fragment_agenda;

    }


    @Override
    protected void setInicio() {


        bundle = getArguments();
        if (bundle!=null){

            origen = bundle.getString(ORIGEN);
            bundle=null;
        }

        //activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());

        proxEvent = view.findViewById(R.id.imgagendaeventos);
        notasGen = view.findViewById(R.id.imgagendanotas);
        partidasPend = view.findViewById(R.id.imgagendapartidas);
        tvProxEvent = view.findViewById(R.id.tvagendaeventos);
        tvNotasGen = view.findViewById(R.id.tvagendanotas);
        tvPartidasPend = view.findViewById(R.id.tvagendapartidas);
        presupEntrega = view.findViewById(R.id.imgagendapresupentrega);
        presupEspera = view.findViewById(R.id.imgagendapresupespera);
        presupCobros = view.findViewById(R.id.imgagendapresupcobros);
        tvPresupEntrega = view.findViewById(R.id.tvagendapresupentrega);
        tvPresupEspera = view.findViewById(R.id.tvagendapresupespera);
        tvPresupCobros = view.findViewById(R.id.tvagendapresupcobros);
        calendario = view.findViewById(R.id.imgagendacalendario);
        tvcalendario = view.findViewById(R.id.tvagendacalendario);

        /*
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int ancho = metrics.widthPixels;
        int altoimg = 100;
        int padimg = 10;
        float sizef = 0f;
        if (!land){
            altoimg = (int) ((double)ancho/3);
            padimg = (int) ((double)ancho/10);
            sizef = (float) ((double)ancho/100);
        }else {
            altoimg = (int) ((double)ancho/6);
            padimg = (int) ((double)ancho/20);
            sizef = (float) ((double)ancho/100);
        }

         */
        proxEvent.setMinimumHeight(altoimg);
        proxEvent.setPadding(padimg, padimg,0,0);
        tvProxEvent.setPadding(padimg,0,0,0);
        tvProxEvent.setTextSize(sizef);
        notasGen.setMinimumHeight(altoimg);
        notasGen.setPadding(padimg, padimg, padimg,0);
        tvNotasGen.setPadding(padimg,0, padimg,0);
        tvNotasGen.setTextSize(sizef);
        partidasPend.setMinimumHeight(altoimg);
        partidasPend.setPadding(0, padimg, padimg,0);
        tvPartidasPend.setPadding(0,0, padimg,0);
        tvPartidasPend.setTextSize(sizef);

        presupEntrega.setMinimumHeight(altoimg);
        presupEntrega.setPadding(padimg, padimg,0,0);
        tvPresupEntrega.setPadding(padimg,0,0,0);
        tvPresupEntrega.setTextSize(sizef);
        presupEspera.setMinimumHeight(altoimg);
        presupEspera.setPadding(padimg, padimg, padimg,0);
        tvPresupEspera.setPadding(padimg,0, padimg,0);
        tvPresupEspera.setTextSize(sizef);
        presupCobros.setMinimumHeight(altoimg);
        presupCobros.setPadding(0, padimg, padimg,0);
        tvPresupCobros.setPadding(0,0, padimg,0);
        tvPresupCobros.setTextSize(sizef);

        calendario.setMinimumHeight(altoimg);
        calendario.setPadding(padimg, padimg,0,0);
        tvcalendario.setPadding(padimg,0,0,0);
        tvcalendario.setTextSize(sizef);

        proxEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obtenerEventos();
            }
        });

        notasGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obtenerNotas();
            }
        });

        partidasPend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obtenerTareas();
            }
        });

        presupEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obtenerPresupPendienteEntrega();
            }
        });

        presupEspera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obtenerPresupEspera();
            }
        });

        presupCobros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obtenerPresupPendienteCobro();
            }
        });

        calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putString(ORIGEN,AGENDA);
                bundle.putString(SUBTITULO,"calendario");
                icFragmentos.enviarBundleAFragment(bundle,new Calendario());


            }
        });

    }



    private void obtenerNotas() {

        //final ArrayList<Modelo> listaNotas = consulta.queryList(CAMPOS_NOTA,NOTA_ID_RELACIONADO,null,null,IGUAL,null);

        ListaModelo listaModelo = new ListaModelo(CAMPOS_NOTA,NOTA_ID_RELACIONADO,null,null,IGUAL,null);

        bundle = new Bundle();
        bundle.putSerializable(LISTA,listaModelo);
        bundle.putString(ORIGEN,AGENDA);
        bundle.putString(SUBTITULO,"Notas generales");
        icFragmentos.enviarBundleAFragment(bundle,new FragmentCRUDNota());

    }

    private void obtenerTareas(){

        ArrayList<Modelo> lista ;
        ListaModelo listaPartidasSinCompletar = new ListaModelo(CAMPOS_PARTIDA);
        listaPartidasSinCompletar.clear();

        lista = consulta.queryList(CAMPOS_PARTIDA);

        for (Modelo item : lista) {

            if (item.getInt(PARTIDA_CONTADOR) > 0 ){

                listaPartidasSinCompletar.add(item);
            }
        }

        if (listaPartidasSinCompletar.chech()){

        bundle = new Bundle();
        bundle.putBoolean(VERLISTA,true);
        bundle.putSerializable(LISTA,listaPartidasSinCompletar);
        bundle.putString(ORIGEN,PARTIDA);
        bundle.putString(SUBTITULO,"Partidas pendientes");

        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaProyecto());

        }else{
            Toast.makeText(getContext(), "No hay partidas pendientes de completar", Toast.LENGTH_SHORT).show();
        }



    }
    private void obtenerPresupPendienteEntrega(){

        String seleccion = ESTADO_TIPOESTADO + " = 1 OR "+
                ESTADO_TIPOESTADO + " = 2";
        String orden = "'"+ PROYECTO_FECHAENTRADA+"' ASC";

        ListaModelo lista = new ListaModelo(CAMPOS_PROYECTO,seleccion,orden);

        if (lista.chech()){

        bundle = new Bundle();
        bundle.putSerializable(LISTA,lista);
        bundle.putString(ACTUAL,PRESUPUESTO);
        bundle.putString(SUBTITULO,"Presup pendiente entrega");

        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
        }else{
            Toast.makeText(getContext(), "No hay ninugún presupuesto pendiente de entrega", Toast.LENGTH_SHORT).show();
        }

    }

    private void obtenerPresupPendienteCobro(){

        String seleccion = ESTADO_TIPOESTADO + " = 7";
        String orden = "'"+ PROYECTO_FECHAENTREGAPRESUP+"' ASC";

        ListaModelo lista = new ListaModelo(CAMPOS_PROYECTO,seleccion,orden);

        if (lista.chech()) {

            bundle = new Bundle();
            bundle.putString(ACTUAL,COBROS);


            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
        }else{
            Toast.makeText(getContext(), "No hay ninugún presupuesto pendiente de cobro", Toast.LENGTH_SHORT).show();
        }


    }

    private void obtenerPresupEspera(){

        String seleccion = ESTADO_TIPOESTADO + " = 3";
        String orden = "'"+ PROYECTO_FECHAENTREGAPRESUP+"' ASC";

        ListaModelo lista = new ListaModelo(CAMPOS_PROYECTO,seleccion,orden);

        if (lista.chech()) {

            bundle = new Bundle();
            bundle.putSerializable(LISTA, lista);
            bundle.putString(ACTUAL,PRESUPUESTO);
            bundle.putString(SUBTITULO, "Presup espera aceptar");
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
        }else{
            Toast.makeText(getContext(), "No hay ninugún presupuesto en espera de aceptar", Toast.LENGTH_SHORT).show();
        }


    }

    private void obtenerEventos() {

        long diasfuturosEventos = JavaUtil.hoy() + (CommonPry.diasfuturos * DIASLONG);
        long diaspasadosEventos = JavaUtil.hoy() - (CommonPry.diaspasados * DIASLONG);

        ListaModelo listaEventos = new ListaModelo(CAMPOS_EVENTO);
        listaEventos.clear();
        ArrayList<Modelo> lista = consulta.queryList(CAMPOS_EVENTO, null, null);
        int i = 0;
        for (Modelo item : lista) {

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

        if (listaEventos.chech()) {

            bundle = new Bundle();
            bundle.putSerializable(LISTA, listaEventos);
            bundle.putString(ACTUAL, CommonPry.TiposEvento.EVENTO);
            bundle.putString(SUBTITULO, "Proximos eventos");
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
        }else{
            Toast.makeText(getContext(), "No hay ninugún evento próximo", Toast.LENGTH_SHORT).show();

        }

    }

    private void obtenerEventosGenerales() {

        long diasfuturosEventos = JavaUtil.hoy() + (CommonPry.diasfuturos * DIASLONG);
        long diaspasadosEventos = JavaUtil.hoy() - (CommonPry.diaspasados * DIASLONG);

        ListaModelo listaEventos = new ListaModelo(CAMPOS_EVENTO);
        listaEventos.clear();
        ArrayList<Modelo> lista = consulta.queryList(CAMPOS_EVENTO, null, null);
        int i = 0;
        for (Modelo item : lista) {

            if ((lista.get(i).getString(EVENTO_CLIENTEREL)==null &&
                    lista.get(i).getString(EVENTO_PROYECTOREL)==null &&
                    lista.get(i).getDouble(EVENTO_COMPLETADA) < 100)) {

                listaEventos.add(item);
            }
            i++;
        }

        if (listaEventos.chech()) {

            bundle = new Bundle();
            bundle.putSerializable(LISTA, listaEventos);
            bundle.putString(ORIGEN, CommonPry.Constantes.EVENTO);
            bundle.putString(SUBTITULO, "Proximos eventos");
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
        }else{
            Toast.makeText(getContext(), "No hay ninugún evento próximo", Toast.LENGTH_SHORT).show();

        }

    }




}
