package jjlacode.com.freelanceproject.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.model.Modelo;
import jjlacode.com.freelanceproject.sqlite.Contract;


public class AdaptadorCliente extends RecyclerView.Adapter<AdaptadorCliente.ClienteViewHolder>
        implements View.OnClickListener, Contract.Tablas {

    ArrayList<Modelo> listaClientes;
    private View.OnClickListener listener;

    public AdaptadorCliente(ArrayList<Modelo> listaClientes) {
        this.listaClientes = listaClientes;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_cliente,null,false);

        view.setOnClickListener(this);


        return new ClienteViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder clienteViewHolder, int position) {

            clienteViewHolder.nombre.setText(listaClientes.get(position).getCampos(CLIENTE_NOMBRE));
            clienteViewHolder.direccion.setText(listaClientes.get(position).getCampos(CLIENTE_DIRECCION));
            clienteViewHolder.telefono.setText(listaClientes.get(position).getCampos(CLIENTE_TELEFONO));
            clienteViewHolder.email.setText(listaClientes.get(position).getCampos(CLIENTE_EMAIL));
            clienteViewHolder.contacto.setText(listaClientes.get(position).getCampos(CLIENTE_CONTACTO));
            int peso = Integer.parseInt(listaClientes.get(position).getCampos(CLIENTE_PESOTIPOCLI));
            if (peso > 6) {
                clienteViewHolder.imagen.setImageResource(R.drawable.clientev);
            } else if (peso > 3) {
                clienteViewHolder.imagen.setImageResource(R.drawable.clientea);
            } else if (peso > 0) {
                clienteViewHolder.imagen.setImageResource(R.drawable.clienter);
            } else {
                clienteViewHolder.imagen.setImageResource(R.drawable.cliente);
            }

    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    @Override
    public void onClick(View v) {

        if (listener!= null){

            listener.onClick(v);


        }

    }

    public class ClienteViewHolder extends RecyclerView.ViewHolder {

        TextView nombre,direccion,telefono,email,contacto;
        ImageView imagen;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.tvnomclilcliente);
            direccion = itemView.findViewById(R.id.tvdirclilcliente);
            telefono = itemView.findViewById(R.id.tvtelclilcliente);
            email = itemView.findViewById(R.id.tvemailclilcliente);
            contacto = itemView.findViewById(R.id.tvcontacclilcliente);
            imagen = itemView.findViewById(R.id.imgclilcliente);
        }
    }
}

