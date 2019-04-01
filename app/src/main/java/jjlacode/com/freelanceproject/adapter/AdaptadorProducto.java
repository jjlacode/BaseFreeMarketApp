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

public class AdaptadorProducto extends RecyclerView.Adapter<AdaptadorProducto.ProductoViewHolder>
        implements View.OnClickListener, Contract.Tablas {

    ArrayList<Modelo> listaProductos;
    View.OnClickListener listener;

    public AdaptadorProducto(ArrayList<Modelo> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public void setOnClickListener(View.OnClickListener listener){

        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_producto,null,false);

        view.setOnClickListener(this);

        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder productoViewHolder, int position) {

        productoViewHolder.descripcion.setText(listaProductos.get(position).getCampos(PRODUCTO_DESCRIPCION));
        productoViewHolder.importe.setText(listaProductos.get(position).getCampos(PRODUCTO_IMPORTE));

    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    @Override
    public void onClick(View v) {

        if (listener!=null){

            listener.onClick(v);
        }

    }

    public class ProductoViewHolder extends RecyclerView.ViewHolder {

        TextView descripcion,importe;
        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);

            descripcion = itemView.findViewById(R.id.tvdescripcionlproductos);
            importe = itemView.findViewById(R.id.tvimportelproductos);
        }
    }
}
