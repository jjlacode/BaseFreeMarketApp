package com.codevsolution.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

import com.codevsolution.base.models.ModeloSQL;

import java.util.ArrayList;
import java.util.List;

public class ListaAdaptadorFiltroModelo extends ArrayAdapter<ModeloSQL> {

    protected ArrayList<ModeloSQL> entradas;
    protected ArrayList<ModeloSQL> entradasfiltro;
    private int R_layout_IdView;
    private Context contexto;
    private String[] campos;

    public ListaAdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas,
                                      String[] campos) {
        super(contexto, R_layout_IdView, entradas);
        this.contexto = contexto;
        this.entradas = new ArrayList<>(entradas);
        this.entradasfiltro = new ArrayList<>(entradas);
        this.campos = campos;
        this.R_layout_IdView = R_layout_IdView;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup pariente) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }
        setEntradas(posicion, view, entradasfiltro);
        return view;
    }

    protected void setEntradas(int posicion, View view, ArrayList<ModeloSQL> entrada) {

    }

    @Override
    public int getCount() {
        return entradasfiltro.size();
    }

    public ArrayList<ModeloSQL> getLista() {

        return entradasfiltro;
    }

    public void addAll(ArrayList<ModeloSQL> lista) {
        entradas.addAll(lista);
        notifyDataSetChanged();
    }

    public void clearAddAll(ArrayList<ModeloSQL> lista) {
        entradas.clear();
        entradas.addAll(lista);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<ModeloSQL> suggestion = new ArrayList<>();
                if (constraint != null) {

                    for (ModeloSQL item : entradas) {

                        for (int i = 2; i < campos.length; i += 3) {


                            if (item.getString(campos[i]) != null && !item.getString(campos[i]).equals("")) {
                                if (item.getString(campos[i]).toLowerCase().contains(constraint.toString().toLowerCase())) {

                                    suggestion.add(item);
                                    break;
                                }
                            }
                        }

                    }
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
                    for (ModeloSQL item : (List<ModeloSQL>) results.values) {
                        entradasfiltro.add(item);
                    }
                    notifyDataSetChanged();
                    System.out.println("results = " + results.count);
                } else if (constraint == null) {
                    // no filter, addModelo entire original list back in
                    System.out.println("Cadena nula");
                    entradasfiltro.addAll(entradas);
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }


    @Override
    public ModeloSQL getItem(int posicion) {
        return entradasfiltro.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }
}
