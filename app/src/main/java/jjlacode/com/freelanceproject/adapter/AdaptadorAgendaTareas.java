package jjlacode.com.freelanceproject.adapter;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.model.AgendaTarea;
import jjlacode.com.freelanceproject.utilities.Common;
import jjlacode.com.freelanceproject.R;

public class AdaptadorAgendaTareas extends RecyclerView.Adapter<AdaptadorAgendaTareas.AgendaViewHolder> implements View.OnClickListener{

    ArrayList<AgendaTarea> listaPartidas;
    private View.OnClickListener listener;

    public AdaptadorAgendaTareas(ArrayList<AgendaTarea> listaPartidas) {

        this.listaPartidas = listaPartidas;
    }

    @NonNull
    @Override
    public AgendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_agenda_tareas,null,false);

        view.setOnClickListener(this);


        return new AgendaViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull AgendaViewHolder agendaViewHolder, int position) {

        agendaViewHolder.descripcionPartida.setText(listaPartidas.get(position).getDescripcion());
        agendaViewHolder.tiempoPartida.setText(listaPartidas.get(position).getTiempo());
        agendaViewHolder.cantidadPartida.setText(listaPartidas.get(position).getCantidad());
        agendaViewHolder.completadaPartida.setText(listaPartidas.get(position).getCompletada());
        agendaViewHolder.proyectoPartida.setText(listaPartidas.get(position).getNombreProyecto());
        agendaViewHolder.progressBarPartida.setProgress(Integer.parseInt(listaPartidas.get(position).getCompletada()));
        agendaViewHolder.nombreCliente.setText(listaPartidas.get(position).getNombreCliente());
        agendaViewHolder.tipoCliente.setText(listaPartidas.get(position).getTipoCliente());

        if (listaPartidas.get(position).getRutaFoto()!=null) {
            agendaViewHolder.imagenProyecto.setImageURI(Uri.parse(listaPartidas.get(position).getRutaFoto()));
        }
        long retraso = Long.parseLong(listaPartidas.get(position).getRetraso());
        if (retraso > 3 * Common.DIASLONG) {
            agendaViewHolder.imagenPartida.setImageResource(R.drawable.alert_box_r);
        } else if (retraso > Common.DIASLONG) {
            agendaViewHolder.imagenPartida.setImageResource(R.drawable.alert_box_a);
        } else {
            agendaViewHolder.imagenPartida.setImageResource(R.drawable.alert_box_v);
        }

        String peso = listaPartidas.get(position).getPeso();
        if (Integer.parseInt(peso) >6) {
            agendaViewHolder.imagenCliente.setImageResource(R.drawable.clientev);
        } else if (Integer.parseInt(peso) > 3) {
            agendaViewHolder.imagenCliente.setImageResource(R.drawable.clientea);
        } else if (Integer.parseInt(peso) > 0) {
        agendaViewHolder.imagenCliente.setImageResource(R.drawable.clienter);
        } else {
            agendaViewHolder.imagenCliente.setImageResource(R.drawable.cliente);
        }


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

    public class AgendaViewHolder extends RecyclerView.ViewHolder {

        ImageView imagenPartida, imagenProyecto, imagenCliente;
        TextView descripcionPartida,tiempoPartida,cantidadPartida,completadaPartida,
                nombreCliente,tipoCliente,proyectoPartida;
        ProgressBar progressBarPartida;

        public AgendaViewHolder(@NonNull View itemView) {
            super(itemView);

            imagenPartida = itemView.findViewById(R.id.imgparagenda);
            imagenProyecto = itemView.findViewById(R.id.imgpryagenda);
            imagenCliente = itemView.findViewById(R.id.imgtipocliagenda);
            tipoCliente = itemView.findViewById(R.id.tvtipocliagenda);
            nombreCliente = itemView.findViewById(R.id.tvclienteagenda);
            proyectoPartida = itemView.findViewById(R.id.tvnomproyagenda);
            descripcionPartida = itemView.findViewById(R.id.tvdescripcioncpartidaagenda);
            tiempoPartida = itemView.findViewById(R.id.tvtiempopartidaagenda);
            cantidadPartida = itemView.findViewById(R.id.tvcantidadpartidaagenda);
            completadaPartida = itemView.findViewById(R.id.tvcompletadapartidaagenda);
            progressBarPartida = itemView.findViewById(R.id.progressBarpartidaagenda);

        }
    }
}
