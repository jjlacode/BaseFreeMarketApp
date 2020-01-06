package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.COBROS;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PARTIDA;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRESUPUESTO;

public class MenuGestionProyectos extends FragmentGridImagen implements ContratoPry.Tablas {

    private String productos;
    private String proyectosCurso;
    private String proveedores;
    private String pendienteEntrega;
    private String enEspera;
    private String pendienteCobro;
    private String tareasEjecucion;
    private String treking;
    private String trabajos;
    private String clientes;
    private String partidasBase;
    private String proyectos;

    @Override
    protected void setContext() {
        contexto = getContext();
    }

    @Override
    protected String setAyudaWeb() {
        return "gestion-de-proyectos";
    }

    @Override
    protected void setInicio() {
        super.setInicio();

        icFragmentos.showSubTitle(R.string.gestion_proyectos);
        reproducir(getString(R.string.gestion_proyectos));
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
        clientes = getString(R.string.clientes);
        partidasBase = getString(R.string.partidas_base);
        proyectos = getString(R.string.proyectos_y_presupuestos);

        lista = new ArrayList<GridModel>();

        lista.add(new GridModel(R.drawable.logofp, proyectos));
        lista.add(new GridModel(R.drawable.ic_clientes_indigo, clientes));
        lista.add(new GridModel(R.drawable.ic_proveedor_indigo, proveedores));
        lista.add(new GridModel(R.drawable.ic_proy_curso_indigo, proyectosCurso));
        lista.add(new GridModel(R.drawable.ic_tareas_indigo, tareasEjecucion));
        lista.add(new GridModel(R.drawable.ic_treking_indigo, treking));
        lista.add(new GridModel(R.drawable.ic_pte_entrega_indigo, pendienteEntrega));
        lista.add(new GridModel(R.drawable.ic_espera_indigo, enEspera));
        lista.add(new GridModel(R.drawable.ic_cobros_indigo, pendienteCobro));
        lista.add(new GridModel(R.drawable.ic_tareas_indigo, trabajos));
        lista.add(new GridModel(R.drawable.ic_producto_indigo, productos));
        lista.add(new GridModel(R.drawable.ic_lista_notas_indigo, partidasBase));

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

        } else if (nombre.equals(partidasBase)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentCRUDPartidaBase()).addToBackStack(null).commit();

        } else if (nombre.equals(proyectos)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentCRUDProyecto()).addToBackStack(null).commit();

        } else if (nombre.equals(clientes)) {

            activityBase.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new FragmentCRUDCliente()).addToBackStack(null).commit();

        }


    }

    @Override
    public void setOnClickRV(Object object) {

    }

    private void obtenerTareas() {

        ArrayList<ModeloSQL> lista;
        ListaModeloSQL listaPartidasSinCompletar = new ListaModeloSQL(CAMPOS_PARTIDA);
        listaPartidasSinCompletar.clearLista();

        lista = ConsultaBD.queryList(CAMPOS_PARTIDA);

        for (ModeloSQL item : lista) {

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

        ArrayList<ModeloSQL> lista;
        ListaModeloSQL listaPartidasSinCompletar = new ListaModeloSQL(CAMPOS_DETPARTIDA);
        listaPartidasSinCompletar.clearLista();

        lista = ConsultaBD.queryList(CAMPOS_DETPARTIDA);

        for (ModeloSQL item : lista) {

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

        ListaModeloSQL lista = new ListaModeloSQL(CAMPOS_PROYECTO, seleccion, orden);

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

        ListaModeloSQL lista = new ListaModeloSQL(CAMPOS_PROYECTO, seleccion, orden);

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

        ListaModeloSQL lista = new ListaModeloSQL(CAMPOS_PROYECTO, seleccion, orden);

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
