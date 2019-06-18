package jjlacode.com.freelanceproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.android.AppActivity;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.android.controls.EditMaterial;
import jjlacode.com.freelanceproject.util.crud.CRUDutil;
import jjlacode.com.freelanceproject.util.crud.FragmentCRUD;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.crud.ListaModelo;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;

import static jjlacode.com.freelanceproject.CommonPry.namesubdef;
import static jjlacode.com.freelanceproject.CommonPry.setNamefdef;


public class FragmentCRUDCliente extends FragmentCRUD implements CommonPry.Constantes,
        ContratoPry.Tablas {


    private EditMaterial nombreCliente;
    private EditMaterial direccionCliente;
    private EditMaterial telefonoCliente;
    private EditMaterial emailCliente;
    private EditMaterial contactoCliente;
    private TextView tipoCliente;
    private ImageButton btnevento;
    private ImageButton mapa;
    private ImageButton llamada;
    private ImageButton mail;
    private Button btnclientes;
    private Button btnprospectos;
    private String idTipoCliente = null;
    private ArrayList <Modelo> objTiposCli;


    int peso;
    private Modelo proyecto;
    private ImageButton btnVerEventos;
    private String actualtemp;


    public FragmentCRUDCliente() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected TipoViewHolder setViewHolder(View view){

        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroRV setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroRV(context,layoutItem,lista,campos);
    }

    @Override
    protected void setLista() {


        if (actualtemp.equals(PROSPECTO)){

            lista = CRUDutil.setListaModelo(campos,CLIENTE_DESCRIPCIONTIPOCLI,PROSPECTO,IGUAL);

        }else if (actualtemp.equals(CLIENTE)) {

            lista = CRUDutil.setListaModelo(campos,CLIENTE_DESCRIPCIONTIPOCLI,PROSPECTO,DIFERENTE);

        }

    }


    @Override
    protected void setNuevo() {

        objTiposCli = consulta.queryList(CAMPOS_TIPOCLIENTE,null,null);

        //btndelete.setVisibility(View.GONE);
        btnevento.setVisibility(View.GONE);

        try{

            if (actualtemp.equals(PROSPECTO) ){

                activityBase.toolbar.setSubtitle(getString(R.string.nuevo) + " " + getString(R.string.prospecto));

                tipoCliente.setText(getString(R.string.prospecto));

                for (int i=0;i < objTiposCli.size();i++){

                    if (objTiposCli.get(i).getString(TIPOCLIENTE_DESCRIPCION).equals(PROSPECTO)){

                        idTipoCliente = objTiposCli.get(i).getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                        peso = objTiposCli.get(i).getInt(TIPOCLIENTE_PESO);
                    }
                }
            }else if (actualtemp.equals(CLIENTE) ){

                activityBase.toolbar.setSubtitle(getString(R.string.nuevo) + " " + getString(R.string.cliente));

                tipoCliente.setText(getString(R.string.cliente));

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
        String seleccion = EVENTO_CLIENTEREL+" = '"+id+"'";
        if (consulta.checkQueryList(CAMPOS_EVENTO,seleccion,null)){
            btnVerEventos.setVisibility(View.VISIBLE);
        }else {
            btnVerEventos.setVisibility(View.GONE);
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
        if (actualtemp==null){actualtemp = actual;}
        System.out.println("actualtemp = " + actualtemp);
        System.out.println("actual = " + actual);
        System.out.println("origen = " + origen);
    }

    @Override
    protected void setDatos() {

        activityBase.toolbar.setSubtitle(modelo.getString(CLIENTE_NOMBRE));

            nombreCliente.setText(modelo.getString(CLIENTE_NOMBRE));
            direccionCliente.setText(modelo.getString(CLIENTE_DIRECCION));
            telefonoCliente.setText(modelo.getString(CLIENTE_TELEFONO));
            emailCliente.setText(modelo.getString(CLIENTE_EMAIL));
            contactoCliente.setText(modelo.getString(CLIENTE_CONTACTO));
            idTipoCliente = modelo.getString(CLIENTE_ID_TIPOCLIENTE);
            id = modelo.getString(CLIENTE_ID_CLIENTE);
            tipoCliente.setText(modelo.getString(CLIENTE_DESCRIPCIONTIPOCLI));
            peso = modelo.getInt(CLIENTE_PESOTIPOCLI);


            if (origen!=null && (origen.equals(PROYECTO) || origen.equals(PRESUPUESTO)
                    || origen.equals(AGENDA))) {

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

            String seleccion = EVENTO_CLIENTEREL+" = '"+id+"'";
            if (consulta.checkQueryList(CAMPOS_EVENTO,seleccion,null)){
                btnVerEventos.setVisibility(View.VISIBLE);
            }else {
                btnVerEventos.setVisibility(View.GONE);
            }


        }

    @Override
    protected void setAcciones() {


        btnevento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(TABLA_CLIENTE, modelo);
                bundle.putBoolean(NUEVOREGISTRO,true);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCRUDEvento());
            }
        });

        btnVerEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                //bundle.putSerializable(TABLA_CLIENTE, modelo);
                bundle.putSerializable(LISTA,new ListaModelo(CAMPOS_EVENTO,EVENTO_CLIENTEREL,id,null,IGUAL,null));
                //bundle.putString(IDREL,id);
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

                actualtemp = CLIENTE;
                activityBase.toolbar.setTitle(R.string.clientes);
                enviarAct();
                listaRV();
            }
        });

        btnprospectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actualtemp = PROSPECTO;
                activityBase.toolbar.setTitle(R.string.prospectos);
                enviarAct();
                listaRV();
            }
        });




    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.cliente;
        tituloPlural = R.string.clientes;
        if (actualtemp.equals(CLIENTE)) {
            tituloNuevo = R.string.nuevo_cliente;
        }else{
            tituloNuevo = R.string.nuevo_prospecto;

        }
    }


    @Override
    protected void setInicio() {

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
        btnVerEventos = view.findViewById(R.id.btnvereventoudcliente);

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_cud_cliente;
        layoutCabecera = R.layout.cabecera_crud_cliente;
        layoutItem = R.layout.item_list_cliente;

    }

    @Override
    protected void setContenedor() {

        setDato(CLIENTE_NOMBRE,nombreCliente.getText().toString());
        setDato(CLIENTE_DIRECCION,direccionCliente.getText().toString());
        setDato(CLIENTE_TELEFONO,telefonoCliente.getText().toString());
        setDato(CLIENTE_EMAIL,emailCliente.getText().toString());
        setDato(CLIENTE_CONTACTO,contactoCliente.getText().toString());
        setDato(CLIENTE_ID_TIPOCLIENTE,idTipoCliente);
        setDato(CLIENTE_PESOTIPOCLI,peso);

    }

    @Override
    protected void setcambioFragment() {

        if (origen!=null && ((origen.equals(PROYECTO))||(origen.equals(PRESUPUESTO)))){

            bundle = new Bundle();
            bundle.putString(ORIGEN, actualtemp);
            System.out.println("idCliente = " + idAOrigen);
            bundle.putString(CLIENTE, idAOrigen);
            bundle.putString(ACTUAL,origen);
            bundle.putBoolean(NUEVOREGISTRO,true);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());

        }else if (subTitulo.equals(NUEVOCLIENTE)){
            subTitulo = namesubdef = setNamefdef();
            actualtemp = CLIENTE;
        }else if (subTitulo.equals(NUEVOPROSPECTO)){
            subTitulo = setNamefdef();
            actualtemp = PROSPECTO;
        }else{
            subTitulo = setNamefdef();

        }
    }

    public class AdaptadorFiltroRV extends ListaAdaptadorFiltroRV{

        public AdaptadorFiltroRV(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<Modelo> entrada) {
            ImageView imgcli = view.findViewById(R.id.imgclilcliente);
            TextView nombreCli = view.findViewById(R.id.tvnomclilcliente);
            TextView contactoCli = view.findViewById(R.id.tvcontacclilcliente);
            TextView telefonoCli = view.findViewById(R.id.tvtelclilcliente);
            TextView emailCli = view.findViewById(R.id.tvemailclilcliente);
            TextView dirCli = view.findViewById(R.id.tvdirclilcliente);

            int peso = entrada.get(posicion).getInt((CLIENTE_PESOTIPOCLI));

            if (peso > 6) {
                imgcli.setImageResource(R.drawable.clientev);
            } else if (peso > 3) {
                imgcli.setImageResource(R.drawable.clientea);
            } else if (peso > 0) {
                imgcli.setImageResource(R.drawable.clienter);
            } else {
                imgcli.setImageResource(R.drawable.cliente);
            }

            nombreCli.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.CLIENTE_NOMBRE));
            contactoCli.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.CLIENTE_CONTACTO));
            telefonoCli.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.CLIENTE_TELEFONO));
            emailCli.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.CLIENTE_EMAIL));
            dirCli.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.CLIENTE_DIRECCION));
            super.setEntradas(posicion,view,entrada);
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView nombre,direccion,telefono,email,contacto;
        ImageView imagen;

        public ViewHolderRV(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvnomclilcliente);
            direccion = itemView.findViewById(R.id.tvdirclilcliente);
            telefono = itemView.findViewById(R.id.tvtelclilcliente);
            email = itemView.findViewById(R.id.tvemailclilcliente);
            contacto = itemView.findViewById(R.id.tvcontacclilcliente);
            imagen = itemView.findViewById(R.id.imgclilcliente);
        }

        @Override
        public void bind(Modelo modelo) {

            nombre.setText(modelo.getCampos(CLIENTE_NOMBRE));
            direccion.setText(modelo.getCampos(CLIENTE_DIRECCION));
            telefono.setText(modelo.getCampos(CLIENTE_TELEFONO));
            email.setText(modelo.getCampos(CLIENTE_EMAIL));
            contacto.setText(modelo.getCampos(CLIENTE_CONTACTO));
            int peso = modelo.getInt(CLIENTE_PESOTIPOCLI);
            if (peso > 6) {
                imagen.setImageResource(R.drawable.clientev);
            } else if (peso > 3) {
                imagen.setImageResource(R.drawable.clientea);
            } else if (peso > 0) {
                imagen.setImageResource(R.drawable.clienter);
            } else {
                imagen.setImageResource(R.drawable.cliente);
            }

            super.bind(modelo);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

}
