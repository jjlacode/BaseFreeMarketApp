package com.jjlacode.freelanceproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.sqlite.ContratoPry;
import com.jjlacode.freelanceproject.R;

import java.util.ArrayList;

public class AdaptadorTareas extends RecyclerView.Adapter<AdaptadorTareas.TareaViewHolder>
        implements View.OnClickListener, ContratoPry.Tablas {

    ArrayList<Modelo> listaTareas;
    private View.OnClickListener listener;

    public AdaptadorTareas(ArrayList<Modelo> listaTareas) {
        this.listaTareas = listaTareas;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_trabajo,null,false);

        view.setOnClickListener(this);

        return new TareaViewHolder(view) ;
    }

    public void setOnClickListener(View.OnClickListener listener){

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder tareaViewHolder, int position) {

        tareaViewHolder.descripcion.setText(listaTareas.get(position).getCampos(TRABAJO_DESCRIPCION));
        tareaViewHolder.tiempo.setText(listaTareas.get(position).getCampos(TRABAJO_TIEMPO));
        tareaViewHolder.nombre.setText(listaTareas.get(position).getString(TRABAJO_NOMBRE));
        if (listaTareas.get(position).getString(TRABAJO_RUTAFOTO)!=null){

            tareaViewHolder.imagen.setImageURI(listaTareas.get(position).getUri(TRABAJO_RUTAFOTO));
        }

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

        TextView descripcion, tiempo, nombre;
        ImageView imagen;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);

            descripcion = itemView.findViewById(R.id.tvdescripcionltareas);
            tiempo = itemView.findViewById(R.id.tvtiempoltareas_detpartida);
            imagen = itemView.findViewById(R.id.imgltarea_detpartida);
            nombre = itemView.findViewById(R.id.tvnomltarea_detpartida);
        }
    }
}
