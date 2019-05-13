package jjlacode.com.freelanceproject.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ListaAdaptadorFiltroRV extends ArrayAdapter<Modelo> {

    protected ArrayList<Modelo> entradas;
    protected ArrayList<Modelo> entradasfiltro;
    private int R_layout_IdView;
    private Context contexto;
    private String[] campos;

    public ListaAdaptadorFiltroRV(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas,
                                  String[] campos) {
        super(contexto,R_layout_IdView,entradas);
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

    protected void setEntradas(int posicion, View view, ArrayList<Modelo> entrada){

    }

    @Override
    public int getCount() {
        return entradasfiltro.size();
    }

    public ArrayList<Modelo> getLista(){

        return  entradasfiltro;
    }


    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<Modelo> suggestion = new ArrayList<>();
                if (constraint != null) {

                    for (Modelo item :entradas) {

                        for (int i = 0; i < campos.length; i++) {


                            if (item.getString(campos[i])!=null && !item.getString(campos[i]).equals("")) {
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
                    for (Modelo item : (List<Modelo>) results.values) {
                            entradasfiltro.add(item);
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    entradasfiltro.addAll(entradas);
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }


    @Override
    public Modelo getItem(int posicion) {
        return entradasfiltro.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }
}
