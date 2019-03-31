package jjlacode.com.freelanceproject.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.model.Modelo;

public class AdaptadorAmortizacion extends RecyclerView.Adapter<AdaptadorAmortizacion.AmortizacionViewHolder> implements View.OnClickListener {

    private ArrayList<Modelo> listaAmortizacion;
    private View.OnClickListener listener;
    private String namef;

    public AdaptadorAmortizacion(ArrayList<Modelo> listaAmortizacion, String namef) {

        this.listaAmortizacion = listaAmortizacion;
        this.namef = namef;
    }

    @NonNull
    @Override
    public AmortizacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_amortizacion, null, false);

        view.setOnClickListener(this);


        return new AmortizacionViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull AmortizacionViewHolder amortizacionViewHolder, int position) {

        //amortizacionViewHolder.descpipcionAmortizacion.setText(listaAmortizacion.get(position).getDescripcion_tipo_cliente();

    }

    @Override
    public int getItemCount() {

        return listaAmortizacion.size();
    }

    @Override
    public void onClick(View v) {

        if (listener != null) {

            listener.onClick(v);


        }

    }

    class AmortizacionViewHolder extends RecyclerView.ViewHolder {


        //TextView descripcionAmortizacion;

        AmortizacionViewHolder(@NonNull View itemView) {
            super(itemView);


            //descripcionAmortizacion = itemView.findViewById(R.id.tvdescripcionAmortizacion);


        }
    }
}