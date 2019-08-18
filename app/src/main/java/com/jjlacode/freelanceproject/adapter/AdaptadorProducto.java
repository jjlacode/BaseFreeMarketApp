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

public class AdaptadorProducto extends RecyclerView.Adapter<AdaptadorProducto.ProductoViewHolder>
        implements View.OnClickListener, ContratoPry.Tablas {

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

        productoViewHolder.descripcion.setText(listaProductos.get(position).getString(PRODUCTO_DESCRIPCION));
        productoViewHolder.importe.setText(listaProductos.get(position).getString(PRODUCTO_PRECIO));
        productoViewHolder.nombre.setText(listaProductos.get(position).getString(PRODUCTO_NOMBRE));

        if (listaProductos.get(position).getString(PRODUCTO_RUTAFOTO)!=null){

            productoViewHolder.imagen.setImageURI(listaProductos.get(position).getUri(PRODUCTO_RUTAFOTO));
        }

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

        TextView descripcion,importe,nombre;
        ImageView imagen;
        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);

            descripcion = itemView.findViewById(R.id.tvdescripcionlproductos);
            importe = itemView.findViewById(R.id.tvimportelproductos_detpartida);
            nombre = itemView.findViewById(R.id.tvnombrelproductos_detpartida);
            imagen = itemView.findViewById(R.id.imglproductos_detpartida);
        }
    }
}
