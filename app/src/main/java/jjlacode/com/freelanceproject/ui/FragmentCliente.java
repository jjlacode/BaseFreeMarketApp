package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import jjlacode.com.androidutils.FragmentBase;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.utilities.CommonPry;
import jjlacode.com.freelanceproject.R;

public class FragmentCliente extends FragmentBase
        implements CommonPry.Constantes, ContratoPry.Tablas, JavaUtil.Constantes {

    private RecyclerView rvClientes;
    private ArrayList<Modelo> objListaClientes;

    private static ConsultaBD consulta = new ConsultaBD();
    public FragmentCliente() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_cliente, container, false);

        objListaClientes = new ArrayList<>();
        rvClientes = vista.findViewById(R.id.rvCli);

        rvClientes.setLayoutManager(new LinearLayoutManager(getContext()));

        bundle = getArguments();

        if (bundle!=null) {
            namef = bundle.getString("namef");
            bundle = null;
            bundle = new Bundle();
            bundle.putString("namef",namef);
            icFragmentos.enviarBundleAActivity(bundle);
            bundle = null;
        }

        if (namef.equals(PROSPECTO)){

                objListaClientes = consulta.queryListCampo
                        (CAMPOS_CLIENTE,CLIENTE_DESCRIPCIONTIPOCLI,PROSPECTO, null);

        }else if (namef.equals(CLIENTE)) {

                objListaClientes = consulta.queryList(CAMPOS_CLIENTE,CLIENTE_DESCRIPCIONTIPOCLI,
                        PROSPECTO,null, DIFERENTE,null);
        }

        AdaptadorCliente adapter = new AdaptadorCliente(objListaClientes);

        rvClientes.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idCliente = objListaClientes.get
                        (rvClientes.getChildAdapterPosition(v)).getString(CLIENTE_ID_CLIENTE);

                Modelo cliente = consulta.queryObject(CAMPOS_CLIENTE,idCliente);

                System.out.println("idCliente = " + idCliente);
                System.out.println("idCliente = " + cliente.getString(CLIENTE_ID_CLIENTE));

                bundle = new Bundle();
                bundle.putSerializable(TABLA_CLIENTE,cliente);
                bundle.putString("namef",namef);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentUDCliente());
                bundle = null;

            }
        });

        return vista;
    }


    public static class AdaptadorCliente extends RecyclerView.Adapter<AdaptadorCliente.ClienteViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas {

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
}
