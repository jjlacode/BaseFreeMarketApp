package com.codevsolution.freemarketsapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.codevsolution.base.sqlite.ConsultaBD.checkQueryList;
import static com.codevsolution.base.sqlite.ConsultaBD.queryList;
import static com.codevsolution.freemarketsapp.logica.Interactor.setNamefdef;

public class FragmentCRUDCliente extends FragmentCRUD implements Interactor.ConstantesPry,
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
    private Button crearPresup;
    private CheckBox activo;
    private String idTipoCliente = null;
    private ArrayList<Modelo> objTiposCli;
    private long fechaInactivo;

    int peso;
    private Modelo proyecto;
    private ImageButton btnVerEventos;
    private String actualtemp;
    private ImageButton btnNota;
    private ImageButton btnVerNotas;
    private ImagenLayout imagenPeso;
    private Button crearProyecto;


    public FragmentCRUDCliente() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {

        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    @Override
    protected void setLista() {


        if (actualtemp.equals(PROSPECTO)) {

            lista = CRUDutil.setListaModelo(campos, CLIENTE_DESCRIPCIONTIPOCLI, PROSPECTO, IGUAL);

        } else if (actualtemp.equals(CLIENTE)) {

            lista = CRUDutil.setListaModelo(campos, CLIENTE_DESCRIPCIONTIPOCLI, PROSPECTO, DIFERENTE);

        }

    }

    @Override
    protected void setOnRightSwipe() {
        super.setOnRightSwipe();
        if (actualtemp.equals(PROSPECTO)){
            actualtemp = CLIENTE;
            activityBase.toolbar.setTitle(R.string.clientes);
            enviarAct();
            selector();
        }
    }

    @Override
    protected void setOnLeftSwipe() {
        super.setOnLeftSwipe();
        if (actualtemp.equals(CLIENTE)){
            actualtemp = PROSPECTO;
            activityBase.toolbar.setTitle(R.string.prospectos);
            enviarAct();
            selector();
        }
    }

    @Override
    protected void setNuevo() {

        if (origen != null && ((origen.equals(PROYECTO)) || (origen.equals(PRESUPUESTO)))) {
            visible(btnback);
        }

        objTiposCli = queryList(CAMPOS_TIPOCLIENTE, null, null);

        //btndelete.setVisibility(View.GONE);
        btnevento.setVisibility(View.GONE);

        try {

            if (actualtemp.equals(PROSPECTO)) {

                tipoCliente.setText(getString(R.string.prospecto));

                for (int i = 0; i < objTiposCli.size(); i++) {

                    if (objTiposCli.get(i).getString(TIPOCLIENTE_DESCRIPCION).equals(PROSPECTO)) {

                        idTipoCliente = objTiposCli.get(i).getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                        peso = objTiposCli.get(i).getInt(TIPOCLIENTE_PESO);
                    }
                }
            } else if (actualtemp.equals(CLIENTE)) {

                tipoCliente.setText(getString(R.string.cliente));

                for (int i = 0; i < objTiposCli.size(); i++) {

                    if (objTiposCli.get(i).getString(TIPOCLIENTE_DESCRIPCION).equals(NUEVO)) {

                        idTipoCliente = objTiposCli.get(i).getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                        peso = objTiposCli.get(i).getInt(TIPOCLIENTE_PESO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String seleccion = EVENTO_CLIENTEREL + " = '" + id + "'";
        if (checkQueryList(CAMPOS_EVENTO, seleccion, null)) {
            btnVerEventos.setVisibility(View.VISIBLE);
        } else {
            btnVerEventos.setVisibility(View.GONE);
        }

        if (peso > 6) {
            //imagen.setImageResource(R.drawable.clientev);
            //imagenPeso.setImageResource(R.drawable.clientev,(int)((double)(anchoReal)/4),(int)((double)(altoReal)/6));
            imagenPeso.setImageResourcePerfil(activityBase, R.drawable.clientev);
        } else if (peso > 3) {
            //imagen.setImageResource(R.drawable.clientea);
            //imagenPeso.setImageResource(R.drawable.clientea,(int)((double)(anchoReal)/4),(int)((double)(altoReal)/6));
            imagenPeso.setImageResourcePerfil(activityBase, R.drawable.clientea);

        } else if (peso > 0) {
            //imagen.setImageResource(R.drawable.clienter);
            //imagenPeso.setImageResource(R.drawable.clienter,(int)((double)(anchoReal)/4),(int)((double)(altoReal)/6));
            imagenPeso.setImageResourcePerfil(activityBase, R.drawable.clienter);

        } else {
            //imagen.setImageResource(R.drawable.cliente);
            //imagenPeso.setImageResource(R.drawable.cliente,(int)((double)(anchoReal)/4),(int)((double)(altoReal)/6));
            imagenPeso.setImageResourcePerfil(activityBase, R.drawable.cliente);

        }


    }

    @Override
    protected void setTabla() {

        tabla = TABLA_CLIENTE;

    }

    @Override
    protected void setCampos() {

        campos = ContratoPry.obtenerCampos(TABLA_CLIENTE);

        System.out.println("campos cliente = " + campos);

    }

    @Override
    protected void setTablaCab() {

        tablaCab = ContratoPry.getTabCab(TABLA_CLIENTE);

    }

    @Override
    protected void setBundle() {

        proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
        if (actualtemp == null) {
            actualtemp = actual;
        }
    }

    @Override
    protected void setDatos() {

        activityBase.toolbar.setSubtitle(modelo.getString(CLIENTE_NOMBRE));
        visible(btnevento);
        visible(btnNota);

        nombreCliente.setText(modelo.getString(CLIENTE_NOMBRE));
        direccionCliente.setText(modelo.getString(CLIENTE_DIRECCION));
        telefonoCliente.setText(modelo.getString(CLIENTE_TELEFONO));
        emailCliente.setText(modelo.getString(CLIENTE_EMAIL));
        contactoCliente.setText(modelo.getString(CLIENTE_CONTACTO));
        idTipoCliente = modelo.getString(CLIENTE_ID_TIPOCLIENTE);
        id = modelo.getString(CLIENTE_ID_CLIENTE);
        tipoCliente.setText(modelo.getString(CLIENTE_DESCRIPCIONTIPOCLI));
        tipoCliente.setTextSize(sizeText * 2);
        peso = modelo.getInt(CLIENTE_PESOTIPOCLI);
        fechaInactivo = modelo.getLong(CLIENTE_ACTIVO);
        if (fechaInactivo > 0) {
            activo.setChecked(true);
        } else {
            activo.setChecked(false);
        }


        if (origen != null && (origen.equals(PROYECTO) || origen.equals(PRESUPUESTO)
                || origen.equals(AGENDA))) {

            btndelete.setVisibility(View.GONE);
            visible(btnback);

        } else {

            btndelete.setVisibility(View.VISIBLE);

        }

        if (peso > 6) {
            //imagen.setImageResource(R.drawable.clientev);
            //imagenPeso.setImageResource(R.drawable.clientev,(int)((double)(anchoReal)/4),(int)((double)(altoReal)/6));
            imagenPeso.setImageResourcePerfil(activityBase, R.drawable.clientev);
        } else if (peso > 3) {
            //imagen.setImageResource(R.drawable.clientea);
            //imagenPeso.setImageResource(R.drawable.clientea,(int)((double)(anchoReal)/4),(int)((double)(altoReal)/6));
            imagenPeso.setImageResourcePerfil(activityBase, R.drawable.clientea);

        } else if (peso > 0) {
            //imagen.setImageResource(R.drawable.clienter);
            //imagenPeso.setImageResource(R.drawable.clienter,(int)((double)(anchoReal)/4),(int)((double)(altoReal)/6));
            imagenPeso.setImageResourcePerfil(activityBase, R.drawable.clienter);

        } else {
            //imagen.setImageResource(R.drawable.cliente);
            //imagenPeso.setImageResource(R.drawable.cliente,(int)((double)(anchoReal)/4),(int)((double)(altoReal)/6));
            imagenPeso.setImageResourcePerfil(activityBase, R.drawable.cliente);

        }

        String seleccion = EVENTO_CLIENTEREL + " = '" + id + "'";
        if (checkQueryList(CAMPOS_EVENTO, seleccion, null)) {
            btnVerEventos.setVisibility(View.VISIBLE);
        } else {
            btnVerEventos.setVisibility(View.GONE);
        }

        seleccion = NOTA_ID_RELACIONADO+" = '"+id+"'";
        if (checkQueryList(CAMPOS_NOTA,seleccion,null)){
            btnVerNotas.setVisibility(View.VISIBLE);
        }else {
            btnVerNotas.setVisibility(View.GONE);
        }

    }

    @Override
    protected void setAcciones() {


        btnevento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(CLIENTE, modelo);
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevoEvento());
            }
        });

        btnVerEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(LISTA, new ListaModelo(CAMPOS_EVENTO, EVENTO_CLIENTEREL, id, null, IGUAL, null));
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
            }
        });


        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!direccionCliente.getText().toString().equals("")) {

                    AppActivity.viewOnMapA(contexto, direccionCliente.getText().toString());

                }
            }
        });

        llamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppActivity.hacerLlamada(AppActivity.getAppContext()
                        , telefonoCliente.getText().toString(), activityBase);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppActivity.enviarEmail(getContext(), emailCliente.getText().toString());
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

        btnNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putString(IDREL,modelo.getString(CLIENTE_ID_CLIENTE));
                bundle.putString(SUBTITULO, modelo.getString(CLIENTE_NOMBRE));
                bundle.putString(ORIGEN, CLIENTE);
                bundle.putSerializable(MODELO, null);
                bundle.putSerializable(LISTA, null);
                bundle.putString(CAMPO_ID, null);
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevaNota());
            }
        });

        btnVerNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarBundle();
                bundle.putString(IDREL,modelo.getString(CLIENTE_ID_CLIENTE));
                bundle.putString(SUBTITULO, modelo.getString(CLIENTE_NOMBRE));
                bundle.putString(ORIGEN, CLIENTE);
                bundle.putString(ACTUAL,NOTA);
                bundle.putSerializable(LISTA,null);
                bundle.putSerializable(MODELO,null);
                bundle.putString(CAMPO_ID,null);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
            }
        });

        activo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    fechaInactivo = JavaUtil.hoy();
                } else {
                    fechaInactivo = 0;
                }
            }
        });

        crearPresup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = new Bundle();
                putBundle(NUEVOREGISTRO, true);
                putBundle(ACTUAL, PRESUPUESTO);
                putBundle(CLIENTE, modelo);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
            }
        });

        crearProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = new Bundle();
                putBundle(NUEVOREGISTRO, true);
                putBundle(ACTUAL, PROYECTO);
                putBundle(CLIENTE, modelo);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
            }
        });

    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.cliente;
        tituloPlural = R.string.clientes;
        if (actualtemp==null){
            actualtemp=PROSPECTO;
        }
        if (actualtemp.equals(PROSPECTO)){
            tituloNuevo = R.string.nuevo_prospecto;
        }else {
            tituloNuevo = R.string.nuevo_cliente;
        }
    }


    @Override
    protected void setInicio() {

        btnclientes = (Button) ctrl(R.id.btnclientes);
        btnprospectos = (Button) ctrl(R.id.btnprospectos);
        nombreCliente = (EditMaterial) ctrl(R.id.etnombreudcliente, CLIENTE_NOMBRE);
        direccionCliente = (EditMaterial) ctrl(R.id.etdirudcliente, CLIENTE_DIRECCION);
        telefonoCliente = (EditMaterial) ctrl(R.id.etteludcliente, CLIENTE_TELEFONO);
        emailCliente = (EditMaterial) ctrl(R.id.etemailudcliente, CLIENTE_EMAIL);
        contactoCliente = (EditMaterial) ctrl(R.id.etcontudcliente, CLIENTE_CONTACTO);
        btnevento = (ImageButton) ctrl(R.id.btneventoudcliente);
        tipoCliente = (TextView) ctrl(R.id.tvtipoudcliente);
        imagen = (ImagenLayout) ctrl(R.id.imgcliente);
        imagenPeso = (ImagenLayout) ctrl(R.id.imgpesocliente);
        mapa = (ImageButton) ctrl(R.id.imgbtndirudcliente);
        llamada = (ImageButton) ctrl(R.id.imgbtnteludcliente);
        mail = (ImageButton) ctrl(R.id.imgbtnmailudcliente);
        btnVerEventos = (ImageButton) ctrl(R.id.btnvereventoudcliente);
        btnNota = (ImageButton) ctrl(R.id.btn_crearnota_cliente);
        btnVerNotas = (ImageButton) ctrl(R.id.btn_vernotas_cliente);
        activo = (CheckBox) ctrl(R.id.chactivocliente);
        crearPresup = (Button) ctrl(R.id.btn_crear_presup);
        crearProyecto = (Button) ctrl(R.id.btn_crear_proy);

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_cliente;
        layoutCabecera = R.layout.cabecera_crud_cliente;
        layoutItem = R.layout.item_list_cliente;

    }

    @Override
    protected void setContenedor() {

        //setDato(CLIENTE_NOMBRE, nombreCliente.getText().toString());
        //setDato(CLIENTE_DIRECCION, direccionCliente.getText().toString());
        //setDato(CLIENTE_TELEFONO, telefonoCliente.getText().toString());
        //setDato(CLIENTE_EMAIL, emailCliente.getText().toString());
        //setDato(CLIENTE_CONTACTO, contactoCliente.getText().toString());
        setDato(CLIENTE_ID_TIPOCLIENTE, idTipoCliente);
        setDato(CLIENTE_PESOTIPOCLI, peso);
        setDato(CLIENTE_ACTIVO, fechaInactivo);

    }

    @Override
    protected boolean onBack() {

        if (origen != null && (origen.equals(PROYECTO) || origen.equals(PRESUPUESTO))) {
            bundle = new Bundle();
            bundle.putString(ORIGEN, actualtemp);
            bundle.putString(CLIENTE,id);
            bundle.putString(ACTUAL, origen);
            bundle.putString(ACTUALTEMP, origen);
            bundle.putBoolean(NUEVOREGISTRO, true);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
        }else {
            super.onBack();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, getMetodo());

        System.out.println("requestCode = " + requestCode);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case RECOGNIZE_SPEECH_ACTIVITY:

                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    grabarVoz = speech.get(0);

                    String ir = null;
                    String destino = null;

                    if (grabarVoz.equals("mapa") || grabarVoz.equals("direccion")) {

                        if (!direccionCliente.getText().toString().equals("")) {

                            AppActivity.viewOnMapA(contexto, direccionCliente.getText().toString());

                        }
                    } else if (grabarVoz.equals("email") || grabarVoz.equals("imeil") || grabarVoz.equals("correo")) {

                        if (!emailCliente.getText().toString().equals("")) {
                            AppActivity.enviarEmail(getContext(), emailCliente.getText().toString());
                        }
                    } else if (grabarVoz.equals("llamar") || grabarVoz.equals("llamada")) {

                        if (!telefonoCliente.getText().toString().equals("")) {
                            AppActivity.hacerLlamada(AppActivity.getAppContext()
                                    , telefonoCliente.getText().toString(), activityBase);
                        }
                    } else if (grabarVoz.equals("marcar")) {

                        if (!telefonoCliente.getText().toString().equals("")) {
                            AppActivity.hacerLlamada(AppActivity.getAppContext()
                                    , telefonoCliente.getText().toString());
                        }
                    } else if (grabarVoz.equals("llamar cliente")) {

                        if (!telefonoCliente.getText().toString().equals("")) {
                            AppActivity.hacerLlamada(AppActivity.getAppContext()
                                    , telefonoCliente.getText().toString(), activityBase);
                        }
                    }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void setcambioFragment() {

        subTitulo = getString(tituloSingular);
        activityBase.toolbar.setSubtitle(subTitulo);
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<Modelo> entrada) {
            ImagenLayout imgcli = view.findViewById(R.id.imglcliente);
            ImagenLayout imgcliPeso = view.findViewById(R.id.imglclientepeso);
            TextView nombreCli = view.findViewById(R.id.tvnomclilcliente);
            TextView contactoCli = view.findViewById(R.id.tvcontacclilcliente);
            TextView telefonoCli = view.findViewById(R.id.tvtelclilcliente);
            TextView emailCli = view.findViewById(R.id.tvemailclilcliente);
            TextView dirCli = view.findViewById(R.id.tvdirclilcliente);

            int peso = entrada.get(posicion).getInt((CLIENTE_PESOTIPOCLI));

            if (peso > 6) {
                imgcliPeso.setImageResource(R.drawable.clientev);
            } else if (peso > 3) {
                imgcliPeso.setImageResource(R.drawable.clientea);
            } else if (peso > 0) {
                imgcliPeso.setImageResource(R.drawable.clienter);
            } else {
                imgcliPeso.setImageResource(R.drawable.cliente);
            }

            nombreCli.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.CLIENTE_NOMBRE));
            contactoCli.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.CLIENTE_CONTACTO));
            telefonoCli.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.CLIENTE_TELEFONO));
            emailCli.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.CLIENTE_EMAIL));
            dirCli.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.CLIENTE_DIRECCION));
            imgcli.setImageUriPerfil(activityBase, entrada.get(posicion).getString(CLIENTE_RUTAFOTO));
            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView nombre, direccion, telefono, email, contacto;
        ImagenLayout imagen, imagenPeso;
        CardView card;

        public ViewHolderRV(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvnomclilcliente);
            direccion = itemView.findViewById(R.id.tvdirclilcliente);
            telefono = itemView.findViewById(R.id.tvtelclilcliente);
            email = itemView.findViewById(R.id.tvemailclilcliente);
            contacto = itemView.findViewById(R.id.tvcontacclilcliente);
            imagen = itemView.findViewById(R.id.imglcliente);
            imagenPeso = itemView.findViewById(R.id.imglclientepeso);
            card = itemView.findViewById(R.id.cardcliente);
        }

        @Override
        public void bind(Modelo modelo) {

            nombre.setText(modelo.getCampos(CLIENTE_NOMBRE));
            direccion.setText(modelo.getCampos(CLIENTE_DIRECCION));
            telefono.setText(modelo.getCampos(CLIENTE_TELEFONO));
            email.setText(modelo.getCampos(CLIENTE_EMAIL));
            contacto.setText(modelo.getCampos(CLIENTE_CONTACTO));
            imagen.setImageUriCard(activityBase, modelo.getString(CLIENTE_RUTAFOTO));
            int peso = modelo.getInt(CLIENTE_PESOTIPOCLI);
            if (peso > 6) {
                imagenPeso.setImageResourceCard(activityBase, R.drawable.clientev);
            } else if (peso > 3) {
                imagenPeso.setImageResourceCard(activityBase, R.drawable.clientea);
            } else if (peso > 0) {
                imagenPeso.setImageResourceCard(activityBase, R.drawable.clienter);
            } else {
                imagenPeso.setImageResourceCard(activityBase, R.drawable.cliente);
            }

            if (modelo.getLong(CLIENTE_ACTIVO) > 0) {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
            } else {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_defecto));
            }

            super.bind(modelo);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

}
