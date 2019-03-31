package jjlacode.com.freelanceproject.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.model.AgendaPresup;
import jjlacode.com.freelanceproject.utilities.Common;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.utilidades.Utilidades;


public class AdaptadorAgendaPresup extends RecyclerView.Adapter<AdaptadorAgendaPresup.AgendaPresupViewHolder> implements View.OnClickListener {

    ArrayList<AgendaPresup> listaAgendaPresup;
    private View.OnClickListener listener;

    public AdaptadorAgendaPresup(ArrayList<AgendaPresup> listaAgendaPresup) {

        this.listaAgendaPresup = listaAgendaPresup;
    }

    @NonNull
    @Override
    public AgendaPresupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_agenda_presup, null, false);

        view.setOnClickListener(this);


        return new AgendaPresupViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull AgendaPresupViewHolder agendaPresupViewHolder, int position) {

            agendaPresupViewHolder.descripcion.setText(listaAgendaPresup.get(position).getDescripcion());
            agendaPresupViewHolder.presupuesto.setText(listaAgendaPresup.get(position).getPresupuesto());
            agendaPresupViewHolder.cliente.setText(listaAgendaPresup.get(position).getCliente());
            agendaPresupViewHolder.estado.setText(listaAgendaPresup.get(position).getEstado());
            String tipoEstado = listaAgendaPresup.get(position).getTipoEstado();
            if (Integer.parseInt(tipoEstado) >0 && Integer.parseInt(tipoEstado) <3) {
                agendaPresupViewHolder.fecha.setText(Utilidades.getDate(Long.parseLong(listaAgendaPresup.get(position).getFechaEntrada())));
            }else if (Integer.parseInt(tipoEstado) == 3) {
                agendaPresupViewHolder.fecha.setText(Utilidades.getDate(Long.parseLong(listaAgendaPresup.get(position).getFechaEntregaPresup())));
            }else if (Integer.parseInt(tipoEstado) == 7){
                agendaPresupViewHolder.fecha.setText(Utilidades.getDate(Long.parseLong(listaAgendaPresup.get(position).getFechaFinal())));
            }

            if (listaAgendaPresup.get(position).getRutaFoto()!=null){

                agendaPresupViewHolder.imagenPresup.setImageURI(Uri.parse(listaAgendaPresup.get(position).getRutaFoto()));

            }
        String retraso = listaAgendaPresup.get(position).getRetraso();
        if (Long.parseLong(retraso) > 3 * Common.DIASLONG) {
            agendaPresupViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_r);
        } else if (Long.parseLong(retraso) > Common.DIASLONG) {
            agendaPresupViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_a);
        } else {
            agendaPresupViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_v);
        }

        String peso = listaAgendaPresup.get(position).getPeso();
        if (Integer.parseInt(peso) >6) {
            agendaPresupViewHolder.imagenCliente.setImageResource(R.drawable.clientev);
        } else if (Integer.parseInt(peso) > 3) {
            agendaPresupViewHolder.imagenCliente.setImageResource(R.drawable.clientea);
        } else if (Integer.parseInt(peso) > 0) {
            agendaPresupViewHolder.imagenCliente.setImageResource(R.drawable.clienter);
        } else {
            agendaPresupViewHolder.imagenCliente.setImageResource(R.drawable.cliente);
        }
    }

    @Override
    public int getItemCount() {

        return listaAgendaPresup.size();
    }

    @Override
    public void onClick(View v) {

        if (listener != null) {

            listener.onClick(v);


        }

    }

    public class AgendaPresupViewHolder extends RecyclerView.ViewHolder {

            ImageView imagenPresup, imagenCliente, imagenEstado;
            TextView presupuesto, descripcion,cliente,estado,fecha;

        public AgendaPresupViewHolder(@NonNull View itemView) {
            super(itemView);

            descripcion = itemView.findViewById(R.id.tvdescagendapresup);
            presupuesto = itemView.findViewById(R.id.tvpresupagendapresup);
            cliente = itemView.findViewById(R.id.tvclienteagendapresup);
            estado = itemView.findViewById(R.id.tvestadoagendapresup);
            fecha = itemView.findViewById(R.id.tvfechaentagendapresup);
            imagenPresup = itemView.findViewById(R.id.imgrvagendapresup);
            imagenCliente = itemView.findViewById(R.id.imgcliagendapresup);
            imagenEstado = itemView.findViewById(R.id.imgestadoagendapresup);

        }
    }
}