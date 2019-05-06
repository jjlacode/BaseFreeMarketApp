package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.AppActivity;
import jjlacode.com.freelanceproject.util.FragmentCRUD;
import jjlacode.com.freelanceproject.util.ListaAdaptadorFiltro;
import jjlacode.com.freelanceproject.util.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.CommonPry;

import static jjlacode.com.freelanceproject.util.CommonPry.namesubdef;
import static jjlacode.com.freelanceproject.util.CommonPry.setNamefdef;


public class FragmentCRUDCliente extends FragmentCRUD implements CommonPry.Constantes, ContratoPry.Tablas {


    private EditText nombreCliente;
    private EditText direccionCliente;
    private EditText telefonoCliente;
    private EditText emailCliente;
    private EditText contactoCliente;
    private TextView tipoCliente;
    private Button btnevento;
    private ImageButton mapa;
    private ImageButton llamada;
    private ImageButton mail;
    private Button btnclientes;
    private Button btnprospectos;
    private String idTipoCliente = null;
    private ArrayList <Modelo> objTiposCli;

    int peso;
    private Modelo proyecto;

    public FragmentCRUDCliente() {
        // Required empty public constructor
    }

    @Override
    protected void setLista() {

        actualizarConsultasRV();

        AdaptadorCliente adapter = new AdaptadorCliente(lista);

        rv.setAdapter(adapter);

        onSetAdapter(lista);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickRV(v);
            }
        });

        setAdaptadorClientes(auto);

        setOnItemClickAuto();

    }

    @Override
    protected void actualizarConsultasRV() {

        if (namef.equals(PROSPECTO)){

            lista = consulta.queryListCampo
                    (CAMPOS_CLIENTE,CLIENTE_DESCRIPCIONTIPOCLI,PROSPECTO, null);

        }else if (namef.equals(CLIENTE)) {

            lista = consulta.queryList(CAMPOS_CLIENTE,CLIENTE_DESCRIPCIONTIPOCLI,
                    PROSPECTO,null, DIFERENTE,null);
        }
    }

    @Override
    protected void setNuevo() {

        objTiposCli = consulta.queryList(CAMPOS_TIPOCLIENTE,null,null);

        btndelete.setVisibility(View.GONE);
        btnevento.setVisibility(View.GONE);

        try{

            if (namef.equals(PROSPECTO) ){

                tipoCliente.setText(PROSPECTO);

                for (int i=0;i < objTiposCli.size();i++){

                    if (objTiposCli.get(i).getString(TIPOCLIENTE_DESCRIPCION).equals(PROSPECTO)){

                        idTipoCliente = objTiposCli.get(i).getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                        peso = objTiposCli.get(i).getInt(TIPOCLIENTE_PESO);
                    }
                }
            }else if (namef.equals(CLIENTE) ){

                tipoCliente.setText(CLIENTE);

                for (int i=0;i < objTiposCli.size();i++){

                    if (objTiposCli.get(i).getString(TIPOCLIENTE_DESCRIPCION).equals(NUEVO)){

                        idTipoCliente = objTiposCli.get(i).getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                        peso = objTiposCli.get(i).getInt(TIPOCLIENTE_PESO);
                    }
                }
            }else if (namesub.equals(PROSPECTO)){
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                icFragmentos.enviarBundleAActivity(bundle);
                bundle = null;

                tipoCliente.setText(PROSPECTO);

                for (int i=0;i < objTiposCli.size();i++){

                    if (objTiposCli.get(i).getString(TIPOCLIENTE_DESCRIPCION).equals(PROSPECTO)){

                        idTipoCliente = objTiposCli.get(i).getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                        peso = objTiposCli.get(i).getInt(TIPOCLIENTE_PESO);
                    }
                }
            }else if (namesub.equals(CLIENTE) ){

                tipoCliente.setText(CLIENTE);

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                icFragmentos.enviarBundleAActivity(bundle);
                bundle = null;


                for (int i=0;i < objTiposCli.size();i++){

                    if (objTiposCli.get(i).getString(TIPOCLIENTE_DESCRIPCION).equals(NUEVO)){

                        idTipoCliente = objTiposCli.get(i).getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                        peso = objTiposCli.get(i).getInt(TIPOCLIENTE_PESO);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setMaestroDetallePort() {
        maestroDetalleSeparados = true;
    }

    @Override
    protected void setMaestroDetalleLand() { maestroDetalleSeparados = false; }

    @Override
    protected void setMaestroDetalleTabletLand() { maestroDetalleSeparados = false; }

    @Override
    protected void setMaestroDetalleTabletPort() { maestroDetalleSeparados = false; }

    @Override
    protected void setTabla() {

        tabla = TABLA_CLIENTE;

    }

    @Override
    protected void setTablaCab() {

        tablaCab = null;
    }

    @Override
    protected void setContext() {

        contexto = getContext();
    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_CLIENTE;
    }

    @Override
    protected void setCampoID() {
        campoID = CLIENTE_ID_CLIENTE;
    }

    @Override
    protected void setBundle() {

        proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
    }

    @Override
    protected void setDatos() {

            nombreCliente.setText(modelo.getString(CLIENTE_NOMBRE));
            direccionCliente.setText(modelo.getString(CLIENTE_DIRECCION));
            telefonoCliente.setText(modelo.getString(CLIENTE_TELEFONO));
            emailCliente.setText(modelo.getString(CLIENTE_EMAIL));
            contactoCliente.setText(modelo.getString(CLIENTE_CONTACTO));
            idTipoCliente = modelo.getString(CLIENTE_ID_TIPOCLIENTE);
            id = modelo.getString(CLIENTE_ID_CLIENTE);
            tipoCliente.setText(modelo.getString(CLIENTE_DESCRIPCIONTIPOCLI));
            peso = modelo.getInt(CLIENTE_PESOTIPOCLI);


            if (namef.equals(PROYECTO) || namef.equals(PRESUPUESTO)
                    || namef.equals(AGENDA)) {

                btndelete.setVisibility(View.GONE);

            } else {

                btndelete.setVisibility(View.VISIBLE);

            }

            if (peso > 6) {
                imagen.setImageResource(R.drawable.clientev);
            } else if (peso > 3) {
                imagen.setImageResource(R.drawable.clientea);
            } else if (peso > 0) {
                imagen.setImageResource(R.drawable.clienter);
            } else {
                imagen.setImageResource(R.drawable.cliente);
            }


        }

    @Override
    protected void setAcciones() {


        btnevento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(TABLA_CLIENTE, modelo);
                bundle.putString(NAMEF,namef);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCRUDEvento());
            }
        });


        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!direccionCliente.getText().toString().equals("")){

                    AppActivity.viewOnMapA(contexto,direccionCliente.getText().toString());

                }
            }
        });

        llamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppActivity.hacerLlamada(AppActivity.getAppContext()
                        ,telefonoCliente.getText().toString());
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppActivity.enviarEmail(getContext(),emailCliente.getText().toString());
            }
        });

        btnclientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                namef = CLIENTE;
                enviarAct();
                setLista();
            }
        });

        btnprospectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                namef = PROSPECTO;
                enviarAct();
                setLista();
            }
        });

    }


    @Override
    protected void setInicio() {

        lista = new ArrayList<>();
        btnclientes = view.findViewById(R.id.btnclientes);
        btnprospectos = view.findViewById(R.id.btnprospectos);
        nombreCliente = view.findViewById(R.id.etnombreudcliente);
        direccionCliente = view.findViewById(R.id.etdirudcliente);
        telefonoCliente = view.findViewById(R.id.etteludcliente);
        emailCliente = view.findViewById(R.id.etemailudcliente);
        contactoCliente = view.findViewById(R.id.etcontudcliente);
        btnevento = view.findViewById(R.id.btneventoudcliente);
        tipoCliente = view.findViewById(R.id.tvtipoudcliente);
        imagen = view.findViewById(R.id.imgudcliente);
        mapa = view.findViewById(R.id.imgbtndirudcliente);
        llamada = view.findViewById(R.id.imgbtnteludcliente);
        mail = view.findViewById(R.id.imgbtnmailudcliente);

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_cud_cliente;
        layoutCabecera = R.layout.cabecera_crud_cliente;

    }

    @Override
    protected void setContenedor() {

        consulta.putDato(valores,campos,CLIENTE_NOMBRE,nombreCliente.getText().toString());
        consulta.putDato(valores,campos,CLIENTE_DIRECCION,direccionCliente.getText().toString());
        consulta.putDato(valores,campos,CLIENTE_TELEFONO,telefonoCliente.getText().toString());
        consulta.putDato(valores,campos,CLIENTE_EMAIL,emailCliente.getText().toString());
        consulta.putDato(valores,campos,CLIENTE_CONTACTO,contactoCliente.getText().toString());
        consulta.putDato(valores,campos,CLIENTE_ID_TIPOCLIENTE,idTipoCliente);
        consulta.putDato(valores,campos,CLIENTE_PESOTIPOCLI,peso);

    }

    @Override
    protected void setcambioFragment() {

        if ((namef.equals(PROYECTO))||(namef.equals(PRESUPUESTO))){

            bundle = new Bundle();
            bundle.putString(NAMEF, namef);
            bundle.putString(ID,proyecto.getString(PROYECTO_ID_PROYECTO));
            if(id!=null) {
                proyecto.setCampos(PROYECTO_ID_CLIENTE, id);
            }
            bundle.putSerializable(MODELO,proyecto);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDProyecto());


        }else if (namesubclass.equals(NUEVOCLIENTE)){
            namesubclass = namesubdef = setNamefdef();
            namef = CLIENTE;
        }else if (namesubclass.equals(NUEVOPROSPECTO)){
            namesubclass = namesubdef = setNamefdef();
            namef = PROSPECTO;
        }else{
            namesubclass = namesubdef = setNamefdef();

        }
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

    private void setAdaptadorClientes(final AutoCompleteTextView autoCompleteTextView) {

        autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(
                getContext(),R.layout.item_list_cliente, lista,CLIENTE_NOMBRE) {
            @Override
            public void onEntrada(Modelo entrada, View view) {

                ImageView imgcli = view.findViewById(R.id.imgclilcliente);
                TextView nombreCli = view.findViewById(R.id.tvnomclilcliente);
                TextView contactoCli = view.findViewById(R.id.tvcontacclilcliente);
                TextView telefonoCli = view.findViewById(R.id.tvtelclilcliente);
                TextView emailCli = view.findViewById(R.id.tvemailclilcliente);
                TextView dirCli = view.findViewById(R.id.tvdirclilcliente);

                int peso = entrada.getInt((CLIENTE_PESOTIPOCLI));

                if (peso > 6) {
                    imgcli.setImageResource(R.drawable.clientev);
                } else if (peso > 3) {
                    imgcli.setImageResource(R.drawable.clientea);
                } else if (peso > 0) {
                    imgcli.setImageResource(R.drawable.clienter);
                } else {
                    imgcli.setImageResource(R.drawable.cliente);
                }

                nombreCli.setText(entrada.getCampos(ContratoPry.Tablas.CLIENTE_NOMBRE));
                contactoCli.setText(entrada.getCampos(ContratoPry.Tablas.CLIENTE_CONTACTO));
                telefonoCli.setText(entrada.getCampos(ContratoPry.Tablas.CLIENTE_TELEFONO));
                emailCli.setText(entrada.getCampos(ContratoPry.Tablas.CLIENTE_EMAIL));
                dirCli.setText(entrada.getCampos(ContratoPry.Tablas.CLIENTE_DIRECCION));


            }

        });

    }


}
