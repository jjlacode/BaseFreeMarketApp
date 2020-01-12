package com.codevsolution.freemarketsapp.ui;

import android.view.View;

import com.codevsolution.base.android.FragmentGridImagen;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

public class FragmentTablas extends FragmentGridImagen implements Interactor.ConstantesPry {

    private String eventos;
    private String proyectos;
    private String notas;
    private String amortizaciones;
    private String gastos;
    private String clientes;
    private String perfiles;
    private String partidasBase;
    private String tareas;
    private String productos;
    private String proveedores;
    private String home;

    @Override
    protected void setContext() {
        contexto = getContext();
    }


    @Override
    protected void setLista() {

        eventos = getString(R.string.eventos);
        proyectos = getString(R.string.proyectos);
        notas = getString(R.string.notas);
        amortizaciones = getString(R.string.amortizaciones);
        gastos = getString(R.string.gastos);
        clientes = getString(R.string.clientes);
        perfiles = getString(R.string.perfiles);
        partidasBase = getString(R.string.partidas_base);
        tareas = getString(R.string.tareas);
        productos = getString(R.string.productos);
        home = getString(R.string.inicio);
        proveedores = getString(R.string.proveedores);

        lista = new ArrayList<>();

        lista.add(new GridModel(R.drawable.ic_evento_indigo, eventos));
        lista.add(new GridModel(R.drawable.ic_proy_curso_indigo, proyectos));
        lista.add(new GridModel(R.drawable.ic_clientes_indigo, clientes));
        lista.add(new GridModel(R.drawable.ic_lista_notas_indigo, notas));
        lista.add(new GridModel(R.drawable.ic_cobros_indigo, amortizaciones));
        lista.add(new GridModel(R.drawable.ic_cobros_indigo, gastos));
        lista.add(new GridModel(R.drawable.ic_tareas_indigo, perfiles));
        lista.add(new GridModel(R.drawable.ic_treking_indigo, partidasBase));
        lista.add(new GridModel(R.drawable.ic_tareas_indigo, tareas));
        lista.add(new GridModel(R.drawable.ic_apagar_indigo, productos));
        lista.add(new GridModel(R.drawable.ic_proveedor_indigo, proveedores));
        lista.add(new GridModel(R.drawable.ic_inicio_black_24dp, home));


    }


    @Override
    protected void onClickRV(View v) {

        GridModel gridModel = (GridModel) lista.get(rv.getChildAdapterPosition(v));

        String nombre = gridModel.getNombre();

        actual = TABLAS;

        if (nombre.equals(eventos)) {

            destino = EVENTO;
            enviarBundle();

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());

        } else if (nombre.equals(proyectos)) {

            destino = PROYECTO;
            enviarBundle();

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());

        } else if (nombre.equals(notas)) {

            destino = NOTA;
            enviarBundle();

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());

        } else if (nombre.equals(amortizaciones)) {

            destino = AMORTIZACION;
            enviarBundle();

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDAmortizacion());

        } else if (nombre.equals(gastos)) {

            destino = GASTOFIJO;
            enviarBundle();

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDGastoFijo());

        } else if (nombre.equals(clientes)) {

            destino = CLIENTE;
            enviarBundle();

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDCliente());

        } else if (nombre.equals(perfiles)) {

            destino = PERFIL;
            enviarBundle();

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPerfil());

        } else if (nombre.equals(partidasBase)) {

            destino = PARTIDABASE;
            enviarBundle();

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaBase());

        } else if (nombre.equals(tareas)) {

            destino = TAREA;
            enviarBundle();

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDTrabajo());

        } else if (nombre.equals(home)) {

            destino = INICIO;
            enviarBundle();

            icFragmentos.enviarBundleAFragment(bundle, new MenuInicio());

        } else if (nombre.equals(productos)) {
            destino = PRODUCTO;
            enviarBundle();

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProducto());

        } else if (nombre.equals(proveedores)) {
            destino = PROVEEDOR;
            enviarBundle();

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProveedor());

        }

    }

    @Override
    public void setOnClickRV(Object object) {

    }

}
