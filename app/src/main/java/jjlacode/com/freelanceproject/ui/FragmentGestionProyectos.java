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

public class FragmentGestionProyectos extends FragmentGrid {

    private String productos;
    private String proyectosCurso;
    private String proveedores;
    private String pendienteEntrega;
    private String enEspera;
    private String pendienteCobro;
    private String tareasEjecucion;
    private String treking;
    private String trabajos;
    private String salir;
    private String catalogo;
    private String home;

    @Override
    protected void setContext() {
        contexto = getContext();
    }


    @Override
    protected void setLista() {

        productos = getString(R.string.productos);
        proyectosCurso = getString(R.string.proyectos_en_curso);
        proveedores = getString(R.string.proveedores);
        pendienteEntrega = getString(R.string.presupuesto_pendiente_entrega);
        enEspera = getString(R.string.presupuesto_en_espera_aceptar);
        pendienteCobro = getString(R.string.pendiente_cobro);
        tareasEjecucion = getString(R.string.tareas_ejecucion);
        treking = getString(R.string.treking);
        trabajos = getString(R.string.trabajos);
        salir = getString(R.string.salir);
        catalogo = getString(R.string.catalogo);
        home = getString(R.string.inicio);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.ic_proy_curso_indigo, proyectosCurso));
        lista.add(new GridModel(R.drawable.ic_tareas_indigo, tareasEjecucion));
        lista.add(new GridModel(R.drawable.ic_treking_indigo, treking));
        lista.add(new GridModel(R.drawable.ic_pte_entrega_indigo, pendienteEntrega));
        lista.add(new GridModel(R.drawable.ic_espera_indigo, enEspera));
        lista.add(new GridModel(R.drawable.ic_cobros_indigo, pendienteCobro));
        lista.add(new GridModel(R.drawable.ic_tareas_indigo, trabajos));
        lista.add(new GridModel(R.drawable.ic_producto_indigo, productos));
        lista.add(new GridModel(R.drawable.ic_catalogo_indigo, catalogo));
        lista.add(new GridModel(R.drawable.ic_proveedor_indigo, proveedores));
        lista.add(new GridModel(R.drawable.ic_inicio_black_24dp, home));
        lista.add(new GridModel(R.drawable.ic_salir_rojo, salir));

    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();

        if (nombre.equals(productos)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentCRUDProducto()).addToBackStack(null).commit();
        } else if (nombre.equals(proyectosCurso)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new Trabajos()).addToBackStack(null).commit();
        } else if (nombre.equals(proveedores)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentCRUDProveedor()).addToBackStack(null).commit();

        } else if (nombre.equals(pendienteEntrega)) {

            obtenerPresupPendienteEntrega();

        } else if (nombre.equals(enEspera)) {

            obtenerPresupEspera();

        } else if (nombre.equals(pendienteCobro)) {

            obtenerPresupPendienteCobro();

        } else if (nombre.equals(tareasEjecucion)) {

            obtenerTareas();

        } else if (nombre.equals(treking)) {

            obtenerDeptpartidasTreking();

        } else if (nombre.equals(trabajos)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentCRUDTrabajo()).addToBackStack(null).commit();

        } else if (nombre.equals(catalogo)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new Catalogo()).addToBackStack(null).commit();

        } else if (nombre.equals(home)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentInicio()).addToBackStack(null).commit();

        } else if (nombre.equals(salir)) {

            activityBase.finish();

        }


    }

    private void obtenerTareas() {

        ArrayList<Modelo> lista;
        ListaModelo listaPartidasSinCompletar = new ListaModelo(CAMPOS_PARTIDA);
        listaPartidasSinCompletar.clearLista();

        lista = ConsultaBD.queryList(CAMPOS_PARTIDA);

        for (Modelo item : lista) {

            if (item.getInt(PARTIDA_CONTADOR) > 0) {

                listaPartidasSinCompletar.addModelo(item);
            }
        }

        if (listaPartidasSinCompletar.chechLista()) {

            bundle = new Bundle();
            bundle.putBoolean(VERLISTA, true);
            bundle.putSerializable(LISTA, listaPartidasSinCompletar);
            bundle.putString(ORIGEN, PARTIDA);
            bundle.putString(SUBTITULO, "Partidas pendientes");

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaProyecto());

        } else {
            Toast.makeText(getContext(), "No hay partidas pendientes de completar", Toast.LENGTH_SHORT).show();
        }


    }

    private void obtenerDeptpartidasTreking() {

        ArrayList<Modelo> lista;
        ListaModelo listaPartidasSinCompletar = new ListaModelo(CAMPOS_DETPARTIDA);
        listaPartidasSinCompletar.clearLista();

        lista = ConsultaBD.queryList(CAMPOS_DETPARTIDA);

        for (Modelo item : lista) {

            if (item.getLong(DETPARTIDA_CONTADOR) > 0) {

                listaPartidasSinCompletar.addModelo(item);
            }
        }

        if (listaPartidasSinCompletar.chechLista()) {

            bundle = new Bundle();
            bundle.putSerializable(LISTA, listaPartidasSinCompletar);
            bundle.putString(ORIGEN, INICIO);
            bundle.putString(SUBTITULO, "Tareas Treking");

            icFragmentos.enviarBundleAFragment(bundle, new FragmentTrabajoTreking());

        } else {
            Toast.makeText(getContext(), "No hay Tareas en Traking", Toast.LENGTH_SHORT).show();
        }


    }

    private void obtenerPresupPendienteEntrega() {

        String seleccion = ESTADO_TIPOESTADO + " = 1 OR " +
                ESTADO_TIPOESTADO + " = 2";
        String orden = "'" + PROYECTO_FECHAENTRADA + "' ASC";

        ListaModelo lista = new ListaModelo(CAMPOS_PROYECTO, seleccion, orden);

        if (lista.chechLista()) {

            bundle = new Bundle();
            bundle.putSerializable(LISTA, lista);
            bundle.putString(ACTUAL, PRESUPUESTO);
            bundle.putString(SUBTITULO, "Presup pendiente entrega");

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
        } else {
            Toast.makeText(getContext(), "No hay ninugún presupuesto pendiente de entrega", Toast.LENGTH_SHORT).show();
        }

    }

    private void obtenerPresupPendienteCobro() {

        String seleccion = ESTADO_TIPOESTADO + " = 7";
        String orden = "'" + PROYECTO_FECHAENTREGAPRESUP + "' ASC";

        ListaModelo lista = new ListaModelo(CAMPOS_PROYECTO, seleccion, orden);

        if (lista.chechLista()) {

            bundle = new Bundle();
            bundle.putString(ACTUAL, COBROS);


            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
        } else {
            Toast.makeText(getContext(), "No hay ninugún presupuesto pendiente de cobro", Toast.LENGTH_SHORT).show();
        }


    }

    private void obtenerPresupEspera() {

        String seleccion = ESTADO_TIPOESTADO + " = 3";
        String orden = "'" + PROYECTO_FECHAENTREGAPRESUP + "' ASC";

        ListaModelo lista = new ListaModelo(CAMPOS_PROYECTO, seleccion, orden);

        if (lista.chechLista()) {

            bundle = new Bundle();
            bundle.putSerializable(LISTA, lista);
            bundle.putString(ACTUAL, PRESUPUESTO);
            bundle.putString(SUBTITULO, "Presup espera aceptar");
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
        } else {
            Toast.makeText(getContext(), "No hay ninugún presupuesto en espera de aceptar", Toast.LENGTH_SHORT).show();
        }


    }
}
