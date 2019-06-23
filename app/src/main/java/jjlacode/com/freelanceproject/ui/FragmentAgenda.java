package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.android.FragmentBase;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.crud.ListaModelo;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.CommonPry;

import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ACTUAL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.DIASLONG;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ID;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.IGUAL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.LISTA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.MODELO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ORIGEN;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.SUBTITULO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.VERLISTA;


public class FragmentAgenda extends FragmentBase implements CommonPry.TiposEvento,
        ContratoPry.Tablas, CommonPry.Constantes {


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

            String origen = bundle.getString(ORIGEN);
            bundle=null;
        }

        //activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());

        ImageView proxEvent = view.findViewById(R.id.imgagendaeventos);
        ImageView notasGen = view.findViewById(R.id.imgagendanotas);
        ImageView partidasPend = view.findViewById(R.id.imgagendapartidas);
        TextView tvProxEvent = view.findViewById(R.id.tvagendaeventos);
        TextView tvNotasGen = view.findViewById(R.id.tvagendanotas);
        TextView tvPartidasPend = view.findViewById(R.id.tvagendapartidas);
        ImageView presupEntrega = view.findViewById(R.id.imgagendapresupentrega);
        ImageView presupEspera = view.findViewById(R.id.imgagendapresupespera);
        ImageView presupCobros = view.findViewById(R.id.imgagendapresupcobros);
        TextView tvPresupEntrega = view.findViewById(R.id.tvagendapresupentrega);
        TextView tvPresupEspera = view.findViewById(R.id.tvagendapresupespera);
        TextView tvPresupCobros = view.findViewById(R.id.tvagendapresupcobros);
        ImageView calendario = view.findViewById(R.id.imgagendacalendario);
        TextView tvcalendario = view.findViewById(R.id.tvagendacalendario);
        ImageView traking = view.findViewById(R.id.imgagendatraking);
        TextView tvTraking = view.findViewById(R.id.tvagendatraking);
        ImageView salir = view.findViewById(R.id.imgagendasalir);
        TextView tvsalir = view.findViewById(R.id.tvagendasalir);


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

        calendario.setMinimumHeight(altoimg);
        calendario.setPadding(padimg, padimg,0,0);
        tvcalendario.setPadding(padimg,0,0,0);
        tvcalendario.setTextSize(sizef);
        proxEvent.setMinimumHeight(altoimg);
        proxEvent.setPadding(padimg, padimg,padimg,0);
        tvProxEvent.setPadding(padimg,0,padimg,0);
        tvProxEvent.setTextSize(sizef);
        notasGen.setMinimumHeight(altoimg);
        notasGen.setPadding(0, padimg, padimg,0);
        tvNotasGen.setPadding(0,0, padimg,0);
        tvNotasGen.setTextSize(sizef);


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

        partidasPend.setMinimumHeight(altoimg);
        partidasPend.setPadding(padimg, padimg, 0,0);
        tvPartidasPend.setPadding(padimg,0, 0,0);
        tvPartidasPend.setTextSize(sizef);
        traking.setMinimumHeight(altoimg);
        traking.setPadding(padimg, padimg, padimg,0);
        tvTraking.setPadding(padimg,0, padimg,0);
        tvTraking.setTextSize(sizef);
        salir.setMinimumHeight(altoimg);
        salir.setPadding(0, padimg, padimg,0);
        tvsalir.setPadding(0,0, padimg,0);
        tvsalir.setTextSize(sizef);

        proxEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //obtenerEventos();
                activityBase.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main,new Trabajos()).addToBackStack(null).commit();
            }
        });

        notasGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //obtenerNotas();
                activityBase.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main,new Diario()).addToBackStack(null).commit();
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

                activityBase.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main,new Calendario()).addToBackStack(null).commit();

            }
        });

        traking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                obtenerDeptpartidasTraking();

            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityBase.finish();
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

        lista = ConsultaBD.queryList(CAMPOS_PARTIDA);

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

    private void obtenerDeptpartidasTraking(){

        ArrayList<Modelo> lista ;
        ListaModelo listaPartidasSinCompletar = new ListaModelo(CAMPOS_DETPARTIDA);
        listaPartidasSinCompletar.clear();

        lista = ConsultaBD.queryList(CAMPOS_DETPARTIDA);

        for (Modelo item : lista) {

            if (item.getLong(DETPARTIDA_CONTADOR) > 0 ){

                listaPartidasSinCompletar.add(item);
            }
        }

        if (listaPartidasSinCompletar.chech()){

            bundle = new Bundle();
            bundle.putBoolean(VERLISTA,true);
            bundle.putSerializable(LISTA,listaPartidasSinCompletar);
            bundle.putString(ORIGEN,INICIO);
            bundle.putString(SUBTITULO,"Tareas Traking");

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartida());

        }else{
            Toast.makeText(getContext(), "No hay Tareas en Traking", Toast.LENGTH_SHORT).show();
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
        ArrayList<Modelo> lista = ConsultaBD.queryList(CAMPOS_EVENTO, null, null);

        for (Modelo item : lista) {

            if ((item.getString(EVENTO_TIPOEVENTO) != null &&
                    item.getString(EVENTO_TIPOEVENTO).equals(TIPOEVENTOTAREA) &&
                    item.getDouble(EVENTO_COMPLETADA) < 100)
                    || (item.getLong(EVENTO_FECHAINIEVENTO) >0 &&
                    item.getLong(EVENTO_FECHAINIEVENTO) > diaspasadosEventos &&
                    item.getLong(EVENTO_FECHAINIEVENTO) < diasfuturosEventos &&
                    item.getDouble(EVENTO_COMPLETADA) < 100)) {

                listaEventos.add(item);
            }
        }

        if (listaEventos.chech()) {

            bundle = new Bundle();
            bundle.putSerializable(LISTA, listaEventos);
            bundle.putString(ACTUAL, EVENTO);
            bundle.putSerializable(MODELO,null);
            bundle.putString(ID,null);
            bundle.putString(SUBTITULO, getString(R.string.proximos_eventos));
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
        }else{
            Toast.makeText(getContext(), getString(R.string.no_eventos_proximos), Toast.LENGTH_SHORT).show();

        }

    }

}
