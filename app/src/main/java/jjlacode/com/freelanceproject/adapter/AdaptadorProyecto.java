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

import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.utilities.Common;
import jjlacode.com.freelanceproject.R;

import static jjlacode.com.freelanceproject.utilities.Common.Constantes.PROYECTO;

public class AdaptadorProyecto extends RecyclerView.Adapter<AdaptadorProyecto.ProyectoViewHolder>
        implements View.OnClickListener, Contract.Tablas {


    private ArrayList<Modelo> listaProyecto;
    private String namef;

    private View.OnClickListener listener;

    public AdaptadorProyecto(ArrayList<Modelo> listaProyecto, String namef) {
        this.listaProyecto = listaProyecto;
        this.namef = namef;
    }

    @NonNull
    @Override
    public ProyectoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_proyecto,null,false);

        view.setOnClickListener(this);

        return new ProyectoViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorProyecto.ProyectoViewHolder proyectoViewHolder, int position) {


        proyectoViewHolder.nombreProyecto.setText(listaProyecto.get(position).getCampos(PROYECTO_NOMBRE));
        proyectoViewHolder.descripcionProyecto.setText(listaProyecto.get(position).getCampos(PROYECTO_DESCRIPCION));
        proyectoViewHolder.clienteProyecto.setText(listaProyecto.get(position).getCampos(PROYECTO_CLIENTE_NOMBRE));
        proyectoViewHolder.estadoProyecto.setText(listaProyecto.get(position).getCampos(PROYECTO_DESCRIPCION_ESTADO));
        if (namef.equals(PROYECTO)){

            proyectoViewHolder.progressBarProyecto.setProgress(Integer.parseInt(listaProyecto.get(position).getCampos(PROYECTO_TOTCOMPLETADO)));

            long retraso = Long.parseLong(listaProyecto.get(position).getCampos(PROYECTO_RETRASO));
            if (retraso > 3 * Common.DIASLONG){proyectoViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_r);}
            else if (retraso > Common.DIASLONG){proyectoViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_a);}
            else {proyectoViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_v);}

        }else{

            proyectoViewHolder.progressBarProyecto.setVisibility(View.GONE);
            proyectoViewHolder.imagenEstado.setVisibility(View.GONE);

        }
        System.out.println("ruta foto pry = " + listaProyecto.get(position).getCampos(PROYECTO_RUTAFOTO));
        if (listaProyecto.get(position).getCampos(PROYECTO_RUTAFOTO)!=null) {
            proyectoViewHolder.imagenProyecto.setImageURI(Uri.parse(listaProyecto.get(position).getCampos(PROYECTO_RUTAFOTO)));
        }
        int peso = Integer.parseInt(listaProyecto.get(position).getCampos(PROYECTO_CLIENTE_PESOTIPOCLI));

        if (peso>6){proyectoViewHolder.imagenCliente.setImageResource(R.drawable.clientev);}
        else if (peso>3){proyectoViewHolder.imagenCliente.setImageResource(R.drawable.clientea);}
        else if (peso>0){proyectoViewHolder.imagenCliente.setImageResource(R.drawable.clienter);}
        else {proyectoViewHolder.imagenCliente.setImageResource(R.drawable.cliente);}



    }

    @Override
    public int getItemCount() {
        if (listaProyecto==null){
            return 0;
        }
        return listaProyecto.size();
    }

    @Override
    public void onClick(View v) {

        if (listener!= null){

            listener.onClick(v);


        }

    }

    public class ProyectoViewHolder extends RecyclerView.ViewHolder {

        ImageView imagenProyecto, imagenEstado, imagenCliente;
        TextView nombreProyecto,descripcionProyecto,clienteProyecto, estadoProyecto;
        ProgressBar progressBarProyecto;

        public ProyectoViewHolder(@NonNull View itemView) {
            super(itemView);

            imagenProyecto = itemView.findViewById(R.id.imglistaproyectos);
            imagenCliente = itemView.findViewById(R.id.imgclientelistaproyectos);
            imagenEstado = itemView.findViewById(R.id.imgestadolistaproyectos);
            nombreProyecto = itemView.findViewById(R.id.tvnombrelistaproyectos);
            descripcionProyecto = itemView.findViewById(R.id.tvdesclistaproyectos);
            clienteProyecto = itemView.findViewById(R.id.tvnombreclientelistaproyectos);
            estadoProyecto = itemView.findViewById(R.id.tvestadolistaproyectos);
            progressBarProyecto = itemView.findViewById(R.id.progressBarlistaproyectos);

        }
    }
}
