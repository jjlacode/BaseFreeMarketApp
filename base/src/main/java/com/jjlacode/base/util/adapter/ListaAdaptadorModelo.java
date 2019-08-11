package com.jjlacode.base.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jjlacode.base.util.crud.Modelo;

import java.util.ArrayList;

/**
 * Adaptador de ListView universal, para www.jarroba.com
 *
 * @author Ramon Invarato Menéndez
 */
public abstract class ListaAdaptadorModelo extends ArrayAdapter<Modelo> {

    private ArrayList<Modelo> entradas;
    private int R_layout_IdView;
    private Context contexto;

    public ListaAdaptadorModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas) {
        super(contexto, R_layout_IdView, entradas);
        this.contexto = contexto;
        this.entradas = entradas;
        this.R_layout_IdView = R_layout_IdView;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup pariente) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }
        onEntrada(entradas.get(posicion), view);
        return view;
    }

    @Override
    public int getCount() {
        return entradas.size();
    }

    @Override
    public Modelo getItem(int posicion) {
        return entradas.get(posicion);
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
