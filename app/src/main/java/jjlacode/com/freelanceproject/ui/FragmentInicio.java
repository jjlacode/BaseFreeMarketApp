package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.android.FragmentGrid;
import jjlacode.com.freelanceproject.util.crud.ListaModelo;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.sqlite.ConsultaBD;

import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ACTUAL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.LISTA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ORIGEN;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.SUBTITULO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.VERLISTA;

public class FragmentInicio extends FragmentGrid {

    private String proximosEventos;
    private String proyectosCurso;
    private String notas;
    private String pendienteEntrega;
    private String enEspera;
    private String pendienteCobro;
    private String tareasEjecucion;
    private String treking;
    private String diario;
    private String salir;
    private String informes;
    private String tablas;

    @Override
    protected void setContext() {
        contexto = getContext();
    }


    @Override
    protected void setLista() {

        proximosEventos = getString(R.string.proximos_eventos);
        proyectosCurso = getString(R.string.proyectos_en_curso);
        notas = getString(R.string.notas);
        pendienteEntrega = getString(R.string.presupuesto_pendiente_entrega);
        enEspera = getString(R.string.presupuesto_en_espera_aceptar);
        pendienteCobro = getString(R.string.pendiente_cobro);
        tareasEjecucion = getString(R.string.tareas_ejecucion);
        treking = getString(R.string.treking);
        diario = getString(R.string.diario);
        salir = getString(R.string.salir);
        informes = getString(R.string.informes);
        tablas = getString(R.string.tablas);

        lista = new ArrayList<>();

        lista.add(new GridModel(R.drawable.ic_evento_indigo, proximosEventos));
        lista.add(new GridModel(R.drawable.ic_proy_curso_indigo, proyectosCurso));
        lista.add(new GridModel(R.drawable.ic_lista_notas_indigo, notas));
        lista.add(new GridModel(R.drawable.ic_pte_entrega_indigo, pendienteEntrega));
        lista.add(new GridModel(R.drawable.ic_espera_indigo, enEspera));
        lista.add(new GridModel(R.drawable.ic_cobros_indigo, pendienteCobro));
        lista.add(new GridModel(R.drawable.ic_tareas_indigo, tareasEjecucion));
        lista.add(new GridModel(R.drawable.ic_treking_indigo, treking));
        lista.add(new GridModel(R.drawable.ic_registro_indigo, diario));
        lista.add(new GridModel(R.drawable.ic_informes_indigo, informes));
        lista.add(new GridModel(R.drawable.ic_tablas_indigo, tablas));
        lista.add(new GridModel(R.drawable.ic_apagar_indigo, salir));

    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();

        if (nombre.equals(proximosEventos)){

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main,new CalendarioEventos()).addToBackStack(null).commit();
        }else if (nombre.equals(proyectosCurso)){

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main,new Trabajos()).addToBackStack(null).commit();
        }else if (nombre.equals(notas)){

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main,new Notas()).addToBackStack(null).commit();

        }else if (nombre.equals(pendienteEntrega)){

            obtenerPresupPendienteEntrega();

        }else if (nombre.equals(enEspera)){

            obtenerPresupEspera();

        }else if (nombre.equals(pendienteCobro)){

            obtenerPresupPendienteCobro();

        }else if (nombre.equals(tareasEjecucion)){

            obtenerTareas();

        }else if (nombre.equals(treking)){

            obtenerDeptpartidasTreking();

        }else if (nombre.equals(diario)){

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main,new Diario()).addToBackStack(null).commit();

        }else if (nombre.equals(informes)){

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main,new Informes()).addToBackStack(null).commit();

        }else if (nombre.equals(tablas)){

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main,new FragmentTablas()).addToBackStack(null).commit();

        }else if (nombre.equals(salir)){

            activityBase.finish();

        }


    }

    private void obtenerTareas(){

        ArrayList<Modelo> lista ;
        ListaModelo listaPartidasSinCompletar = new ListaModelo(CAMPOS_PARTIDA);
        listaPartidasSinCompletar.clearLista();

        lista = ConsultaBD.queryList(CAMPOS_PARTIDA);

        for (Modelo item : lista) {

            if (item.getInt(PARTIDA_CONTADOR) > 0 ){

                listaPartidasSinCompletar.addModelo(item);
            }
        }

        if (listaPartidasSinCompletar.chechLista()){

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

    private void obtenerDeptpartidasTreking(){

        ArrayList<Modelo> lista ;
        ListaModelo listaPartidasSinCompletar = new ListaModelo(CAMPOS_DETPARTIDA);
        listaPartidasSinCompletar.clearLista();

        lista = ConsultaBD.queryList(CAMPOS_DETPARTIDA);

        for (Modelo item : lista) {

            if (item.getLong(DETPARTIDA_CONTADOR) > 0 ){

                listaPartidasSinCompletar.addModelo(item);
            }
        }

        if (listaPartidasSinCompletar.chechLista()){

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

        if (lista.chechLista()){

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

        if (lista.chechLista()) {

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

        if (lista.chechLista()) {

            bundle = new Bundle();
            bundle.putSerializable(LISTA, lista);
            bundle.putString(ACTUAL,PRESUPUESTO);
            bundle.putString(SUBTITULO, "Presup espera aceptar");
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
        }else{
            Toast.makeText(getContext(), "No hay ninugún presupuesto en espera de aceptar", Toast.LENGTH_SHORT).show();
        }


    }
}
