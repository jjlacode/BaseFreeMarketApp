package jjlacode.com.freelanceproject.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.Contract;

public class AdaptadorTareas extends RecyclerView.Adapter<AdaptadorTareas.TareaViewHolder>
        implements View.OnClickListener, Contract.Tablas {

    ArrayList<Modelo> listaTareas;
    private View.OnClickListener listener;

    public AdaptadorTareas(ArrayList<Modelo> listaTareas) {
        this.listaTareas = listaTareas;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_tarea,null,false);

        view.setOnClickListener(this);

        return new TareaViewHolder(view) ;
    }

    public void setOnClickListener(View.OnClickListener listener){

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder tareaViewHolder, int position) {

        tareaViewHolder.descripcion.setText(listaTareas.get(position).getCampos(TAREA_DESCRIPCION));
        tareaViewHolder.tiempo.setText(listaTareas.get(position).getCampos(TAREA_TIEMPO));

    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    @Override
    public void onClick(View v) {

        if (listener!=null){

            listener.onClick(v);
        }

    }

    public class TareaViewHolder extends RecyclerView.ViewHolder {

        TextView descripcion, tiempo;
        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);

            descripcion = itemView.findViewById(R.id.tvdescripcionltareas);
            tiempo = itemView.findViewById(R.id.tvtiempoltareas);
        }
    }
}
