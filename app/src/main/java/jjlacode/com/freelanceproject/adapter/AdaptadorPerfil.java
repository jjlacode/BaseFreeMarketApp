package jjlacode.com.freelanceproject.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.model.Modelo;

public class AdaptadorPerfil extends RecyclerView.Adapter<AdaptadorPerfil.PerfilViewHolder> implements View.OnClickListener {

    private ArrayList<Modelo> listaPerfil;
    private View.OnClickListener listener;
    private String namef;

    public AdaptadorPerfil(ArrayList<Modelo> listaPerfil, String namef) {

        this.listaPerfil = listaPerfil;
        this.namef = namef;
    }

    @NonNull
    @Override
    public PerfilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_perfil, null, false);

        view.setOnClickListener(this);


        return new PerfilViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull PerfilViewHolder perfilViewHolder, int position) {

        //perfilViewHolder.descpipcionPerfil.setText(listaPerfil.get(position).getDescripcion_tipo_cliente();

    }

    @Override
    public int getItemCount() {

        return listaPerfil.size();
    }

    @Override
    public void onClick(View v) {

        if (listener != null) {

            listener.onClick(v);


        }

    }

    class PerfilViewHolder extends RecyclerView.ViewHolder {


        //TextView descripcionPerfil;

        PerfilViewHolder(@NonNull View itemView) {
            super(itemView);


            //descripcionPerfil = itemView.findViewById(R.id.tvdescripcionPerfil);


        }
    }
}