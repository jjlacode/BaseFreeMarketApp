package jjlacode.com.freelanceproject.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.Contract;

public class AdaptadorGasto extends RecyclerView.Adapter<AdaptadorGasto.GastoViewHolder>
        implements View.OnClickListener, Contract.Tablas {

    ArrayList<Modelo> listaGastos;
    private View.OnClickListener listener;

    public AdaptadorGasto(ArrayList<Modelo> listaGastos) {
        this.listaGastos = listaGastos;
    }



    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_gastos,null,false);

        view.setOnClickListener(this);

        return new GastoViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener){

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder gastoViewHolder, int position) {

        gastoViewHolder.descripcion.setText(listaGastos.get(position).getCampos(GASTO_DESCRIPCION));
        double impor = Double.parseDouble(listaGastos.get(position).getCampos(GASTO_IMPORTE));
        gastoViewHolder.importe.setText(String.format(Locale.getDefault(),"%#.2f %s",impor, JavaUtil.monedaLocal()));
        gastoViewHolder.cantidad.setText(listaGastos.get(position).getCampos(GASTO_CANTIDAD));
        double benef = Double.parseDouble(listaGastos.get(position).getCampos(GASTO_BENEFICIO));
        gastoViewHolder.beneficio.setText(String.format(Locale.getDefault(),"%#.2f %s",benef,"%"));
        gastoViewHolder.totalgastos.setText(String.format(Locale.getDefault(),"%#.2f",(impor+((impor/100)*benef))));

    }

    @Override
    public int getItemCount() {
        return listaGastos.size();
    }

    @Override
    public void onClick(View v) {

        if (listener!=null){

            listener.onClick(v);
        }

    }

    public class GastoViewHolder extends RecyclerView.ViewHolder {

        TextView descripcion,importe,cantidad,limporte,totalgastos,beneficio;
        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);

            descripcion = itemView.findViewById(R.id.tvdescripcionlgastos);
            importe = itemView.findViewById(R.id.tvimportelgastos);
            cantidad = itemView.findViewById(R.id.tvcantidadlgastos);
            totalgastos = itemView.findViewById(R.id.tvimportetotlgastos);
            beneficio = itemView.findViewById(R.id.tvbenflgastos);
        }
    }
}
