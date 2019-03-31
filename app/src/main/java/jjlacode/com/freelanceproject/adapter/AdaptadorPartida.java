package jjlacode.com.freelanceproject.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.model.Modelo;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.utilities.Common;
import jjlacode.com.freelanceproject.R;

public class AdaptadorPartida extends RecyclerView.Adapter<AdaptadorPartida.PartidaViewHolder>
        implements View.OnClickListener, Common.Constantes, Contract.Tablas {

    ArrayList<Modelo> listaPartidas;
    private View.OnClickListener listener;
    private String namef;

    public AdaptadorPartida(ArrayList<Modelo> listaPartidas, String namef){

        this.listaPartidas=listaPartidas;
        this.namef = namef;
    }

    @NonNull
    @Override
    public PartidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_partida,null,false);

        view.setOnClickListener(this);


        return new PartidaViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull PartidaViewHolder partidaViewHolder, int position) {

        partidaViewHolder.descripcionPartida.setText(listaPartidas.get(position).getCampos(PARTIDA_DESCRIPCION));
        partidaViewHolder.tiempoPartida.setText(listaPartidas.get(position).getCampos(PARTIDA_TIEMPO));
        partidaViewHolder.cantidadPartida.setText(listaPartidas.get(position).getCampos(PARTIDA_CANTIDAD));
        partidaViewHolder.completadaPartida.setText(listaPartidas.get(position).getCampos(PARTIDA_COMPLETADA));
        partidaViewHolder.progressBarPartida.setProgress(Integer.parseInt(listaPartidas.get(position).getCampos(PARTIDA_COMPLETADA)));
        if (namef.equals(AGENDA)) {

            partidaViewHolder.imagenPartida.setVisibility(View.VISIBLE);
            String retraso = listaPartidas.get(position).getCampos(PARTIDA_PROYECTO_RETRASO);
            if (Integer.parseInt(retraso) > 3 * Common.DIASLONG) {
                partidaViewHolder.imagenPartida.setImageResource(R.drawable.alert_box_r);
            } else if (Integer.parseInt(retraso) > 0) {
                partidaViewHolder.imagenPartida.setImageResource(R.drawable.alert_box_a);
            } else {
                partidaViewHolder.imagenPartida.setImageResource(R.drawable.alert_box_v);
            }
        }else{
                partidaViewHolder.imagenPartida.setVisibility(View.GONE);
        }
        if (namef.equals(PRESUPUESTO)){

            partidaViewHolder.progressBarPartida.setVisibility(View.GONE);
        }else{

            partidaViewHolder.progressBarPartida.setVisibility(View.VISIBLE);
        }
        System.out.println("listaPartidas: "+ listaPartidas.size() + " registros");

    }

    @Override
    public int getItemCount() {
        return listaPartidas.size();
    }

    @Override
    public void onClick(View v) {

        if (listener!= null){

            listener.onClick(v);


        }


    }

    public class PartidaViewHolder extends RecyclerView.ViewHolder{

        ImageView imagenPartida;
        TextView descripcionPartida,tiempoPartida,cantidadPartida,completadaPartida,proyectoPartida;
        ProgressBar progressBarPartida;

        public PartidaViewHolder(@NonNull View itemView) {
            super(itemView);

            imagenPartida = itemView.findViewById(R.id.imgpar);
            descripcionPartida = itemView.findViewById(R.id.tvdescripcioncpartida);
            tiempoPartida = itemView.findViewById(R.id.tvtiempopartida);
            cantidadPartida = itemView.findViewById(R.id.tvcantidadpartida);
            completadaPartida = itemView.findViewById(R.id.tvcompletadapartida);
            progressBarPartida = itemView.findViewById(R.id.progressBarpartida);

        }

    }
}
