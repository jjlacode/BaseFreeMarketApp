package com.jjlacode.base.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ListaAdaptadorFiltro extends ArrayAdapter {

    private ArrayList entradas;
    private ArrayList entradasfiltro;
    private int R_layout_IdView;
    private Context contexto;

    public ListaAdaptadorFiltro(Context contexto, int R_layout_IdView, ArrayList entradas) {
        super(contexto, R_layout_IdView, entradas);
        this.contexto = contexto;
        this.entradas = new ArrayList(entradas);
        this.entradasfiltro = new ArrayList(entradas);
        this.R_layout_IdView = R_layout_IdView;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup pariente) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }
        onEntrada(entradasfiltro.get(posicion), view);
        return view;
    }

    @Override
    public int getCount() {
        return entradasfiltro.size();
    }

    public ArrayList getLista() {
        return entradasfiltro;
    }


    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List suggestion;
                if (constraint != null) {

                    suggestion = onFilter(entradas, constraint);
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
                    for (Object item : (List) results.values) {
                        entradasfiltro.add(item);
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {
                    // no filter, addModelo entire original list back in
                    entradasfiltro.addAll(entradas);
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }


    @Override
    public Object getItem(int posicion) {
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
    public abstract void onEntrada(Object entrada, View view);

    public abstract List onFilter(ArrayList entradas, CharSequence constraint);
}
