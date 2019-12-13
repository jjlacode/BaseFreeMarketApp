package com.codevsolution.freemarketsapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.android.controls.ViewImagenLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.base.sqlite.ConsultaBD.queryList;

public class FragmentCRUDCliente extends FragmentCRUD implements Interactor.ConstantesPry,
        ContratoPry.Tablas {


    private EditMaterialLayout nombreCliente;
    private EditMaterialLayout direccionCliente;
    private EditMaterialLayout telefonoCliente;
    private EditMaterialLayout emailCliente;
    private EditMaterialLayout webCliente;
    private EditMaterialLayout contactoCliente;
    private EditMaterialLayout tipoCliente;
    private ImageButton btnevento;
    private Button btnclientes;
    private Button btnprospectos;
    private Button crearPresup;
    private CheckBox activo;
    private String idTipoCliente = null;
    private ArrayList<ModeloSQL> objTiposCli;
    private long fechaInactivo;

    int peso;
    private ModeloSQL proyecto;
    private ImageButton btnVerEventos;
    private ImageButton btnNota;
    private ImageButton btnVerNotas;
    private Button crearProyecto;
    private TextView clienteWeb;
    private Button btnAsignarAEvento;
    private ModeloSQL evento;


    public FragmentCRUDCliente() {
        // Required empty public constructor
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {

        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    @Override
    protected void setInicio() {

        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalle);

        btnAsignarAEvento = vistaForm.addButtonPrimary(R.string.asignar_evento);
        btnAsignarAEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                putBundle(CLIENTE, modeloSQL);
                putBundle(MODELO, evento);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
            }
        });
        gone(btnAsignarAEvento);
        imagen = vistaForm.addViewImagenLayout();

        clienteWeb = vistaForm.addTextView(getString(R.string.clienteweb));
        gone(clienteWeb);
        tipoCliente = vistaForm.addEditMaterialLayout(getString(R.string.tipo_cliente));
        tipoCliente.setActivo(false);
        tipoCliente.btnAccion3Enable(true);

        nombreCliente = vistaForm.addEditMaterialLayout(getString(R.string.nombre), CLIENTE_NOMBRE, null, null);
        nombreCliente.setObligatorio(true);
        direccionCliente = vistaForm.addEditMaterialLayout(getString(R.string.direccion), CLIENTE_DIRECCION, ViewGroupLayout.MAPA, activityBase);
        telefonoCliente = vistaForm.addEditMaterialLayout(getString(R.string.telefono), CLIENTE_TELEFONO, ViewGroupLayout.LLAMADA, activityBase);
        emailCliente = vistaForm.addEditMaterialLayout(getString(R.string.email), CLIENTE_EMAIL, ViewGroupLayout.MAIL, activityBase);
        webCliente = vistaForm.addEditMaterialLayout(getString(R.string.web), CLIENTE_WEB, ViewGroupLayout.WEB, activityBase);
        contactoCliente = vistaForm.addEditMaterialLayout(getString(R.string.contacto), CLIENTE_CONTACTO, null, null);
        activo = (CheckBox) vistaForm.addVista(new CheckBox(contexto));
        activo.setText(R.string.inactivo);
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

        ViewGroupLayout vistabtn = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistabtn.setOrientacion(LinearLayoutCompat.HORIZONTAL);

        btnevento = vistabtn.addImageButtonSecundary(R.drawable.ic_evento_indigo, 1);
        btnevento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = new Bundle();
                bundle.putSerializable(CLIENTE, modeloSQL);
                bundle.putString(SUBTITULO, modeloSQL.getString(CLIENTE_NOMBRE));
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevoEvento());
            }

        });
        btnVerEventos = vistabtn.addImageButtonSecundary(R.drawable.ic_lista_eventos_indigo, 1);
        btnVerEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = new Bundle();
                bundle.putString(SUBTITULO, modeloSQL.getString(CLIENTE_NOMBRE));
                bundle.putSerializable(LISTA, new ListaModeloSQL(CAMPOS_EVENTO, EVENTO_CLIENTEREL, id));
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
            }
        });
        btnNota = vistabtn.addImageButtonSecundary(R.drawable.ic_nueva_nota_indigo, 1);
        btnNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = new Bundle();
                bundle.putString(IDREL, modeloSQL.getString(CLIENTE_ID_CLIENTE));
                bundle.putString(SUBTITULO, modeloSQL.getString(CLIENTE_NOMBRE));
                bundle.putString(ORIGEN, CLIENTE);
                bundle.putSerializable(MODELO, null);
                bundle.putSerializable(LISTA, null);
                bundle.putString(CAMPO_ID, null);
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevaNota());
            }
        });
        btnVerNotas = vistabtn.addImageButtonSecundary(R.drawable.ic_lista_notas_indigo, 1);
        btnVerNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarBundle();
                bundle.putString(IDREL, modeloSQL.getString(CLIENTE_ID_CLIENTE));
                bundle.putString(SUBTITULO, modeloSQL.getString(CLIENTE_NOMBRE));
                bundle.putString(ORIGEN, CLIENTE);
                bundle.putString(ACTUAL, NOTA);
                bundle.putSerializable(LISTA, null);
                bundle.putSerializable(MODELO, null);
                bundle.putString(CAMPO_ID, null);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
            }
        });
        actualizarArrays(vistabtn);

        ViewGroupLayout vistabtnProy = new ViewGroupLayout(contexto, vistaForm.getViewGroup());

        crearPresup = vistabtnProy.addButtonSecondary(R.string.crear_presup);
        crearPresup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = new Bundle();
                putBundle(NUEVOREGISTRO, true);
                putBundle(ACTUAL, PRESUPUESTO);
                putBundle(CLIENTE, modeloSQL);
                putBundle(CAMPO_ID, null);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());

            }
        });
        crearProyecto = vistabtnProy.addButtonSecondary(R.string.crear_proyecto);
        crearProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = new Bundle();
                putBundle(NUEVOREGISTRO, true);
                putBundle(ACTUAL, PROYECTO);
                putBundle(CLIENTE, modeloSQL);
                putBundle(CAMPO_ID, null);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());

            }
        });
        actualizarArrays(vistabtnProy);
        actualizarArrays(vistaForm);

        ViewGroupLayout vistaCab = new ViewGroupLayout(contexto, frCabecera);
        vistaCab.setOrientacion(LinearLayoutCompat.HORIZONTAL);

        btnclientes = vistaCab.addButtonSecondary(R.string.clientes, 1);
        btnclientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actual = CLIENTE;
                activityBase.toolbar.setSubtitle(R.string.clientes);
                enviarAct();
                listaRV();
            }
        });

        btnprospectos = vistaCab.addButtonSecondary(R.string.prospectos, 1);
        btnprospectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actual = PROSPECTO;
                activityBase.toolbar.setSubtitle(R.string.prospectos);
                enviarAct();
                listaRV();
            }
        });

        actualizarArrays(vistaCab);


    }

    @Override
    protected void setLista() {

        if (actual == null){
            actual = PROSPECTO;
        }

        if (actual.equals(PROSPECTO)) {

            activityBase.toolbar.setSubtitle(R.string.prospectos);

            lista = cruDutil.setListaModelo(CLIENTE_DESCRIPCIONTIPOCLI, PROSPECTO);

        } else if (actual.equals(CLIENTE)) {

            activityBase.toolbar.setSubtitle(R.string.clientes);

            lista = cruDutil.setListaModelo(CLIENTE_DESCRIPCIONTIPOCLI, PROSPECTO, DIFERENTE);

        }

    }

    @Override
    protected void setOnRightSwipe() {
        super.setOnRightSwipe();
        if (actual.equals(PROSPECTO)){
            actual = CLIENTE;
            activityBase.toolbar.setSubtitle(R.string.clientes);
            enviarAct();
            selector();
        }
    }

    @Override
    protected void setOnLeftSwipe() {
        super.setOnLeftSwipe();
        if (actual.equals(CLIENTE)){
            actual = PROSPECTO;
            activityBase.toolbar.setSubtitle(R.string.prospectos);
            enviarAct();
            selector();
        }
    }

    @Override
    protected void setNuevo() {

        if (origen != null && ((origen.equals(PROYECTO)) || (origen.equals(PRESUPUESTO)))) {
            visible(btnback);
        }

        objTiposCli = CRUDutil.setListaModelo(CAMPOS_TIPOCLIENTE).getLista();//queryList(CAMPOS_TIPOCLIENTE);

        btnevento.setVisibility(View.GONE);

        try {

            if (actual.equals(PROSPECTO)) {

                tipoCliente.setText(getString(R.string.prospecto));

                for (int i = 0; i < objTiposCli.size(); i++) {

                    System.out.println("objTiposCli.get(i).getString(TIPOCLIENTE_DESCRIPCION) = " + objTiposCli.get(i).getString(TIPOCLIENTE_DESCRIPCION));
                    if (objTiposCli.get(i).getString(TIPOCLIENTE_DESCRIPCION).equals(PROSPECTO)) {

                        idTipoCliente = objTiposCli.get(i).getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                        peso = objTiposCli.get(i).getInt(TIPOCLIENTE_PESO);
                    }
                }
            } else if (actual.equals(CLIENTE)) {

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
        ListaModeloSQL listaEventos = CRUDutil.setListaModelo(CAMPOS_EVENTO, EVENTO_CLIENTEREL, id);
        if (listaEventos.sizeLista() > 0) {
            btnVerEventos.setVisibility(View.VISIBLE);
        } else {
            btnVerEventos.setVisibility(View.GONE);
        }

        ListaModeloSQL listaNotas = CRUDutil.setListaModelo(CAMPOS_NOTA, NOTA_ID_RELACIONADO, id);

        if (listaNotas.sizeLista() > 0) {
            btnVerNotas.setVisibility(View.VISIBLE);
        } else {
            btnVerNotas.setVisibility(View.GONE);
        }

        if (peso > 6) {
            tipoCliente.setImgBtnAccion3(R.drawable.clientev);
        } else if (peso > 3) {
            tipoCliente.setImgBtnAccion3(R.drawable.clientea);

        } else if (peso > 0) {
            tipoCliente.setImgBtnAccion3(R.drawable.clienter);

        } else {
            tipoCliente.setImgBtnAccion3(R.drawable.cliente);

        }

        onUpdate();
    }

    @Override
    protected void setTabla() {

        tabla = TABLA_CLIENTE;

    }

    @Override
    protected void setBundle() {

        proyecto = (ModeloSQL) bundle.getSerializable(TABLA_PROYECTO);
        if (actual == null){
            actual = PROSPECTO;
        }
        if (actual.equals(PROSPECTO)){
            tituloNuevo = R.string.nuevo_prospecto;
            tituloSingular = R.string.prospecto;
            tituloPlural = R.string.prospectos;
        }else {
            tituloNuevo = R.string.nuevo_cliente;
            tituloSingular = R.string.cliente;
            tituloPlural = R.string.clientes;
        }
        evento = (ModeloSQL) bundle.getSerializable(EVENTO);
        if (evento != null) {
            visible(btnAsignarAEvento);
        }
    }

    @Override
    protected void setDatos() {

        if (nnn(nombreCliente.getTexto())){
            nombreCliente.reproducir();
        }

        if (nnn(modeloSQL.getString(CLIENTE_NOMBRE))) {
            activityBase.toolbar.setSubtitle(modeloSQL.getString(CLIENTE_NOMBRE));
        }
        visible(btnevento);
        visible(btnNota);

        if (nnn(modeloSQL.getString(CLIENTE_TIPOFIRE))) {
            visible(clienteWeb);
            clienteWeb.setText(modeloSQL.getString(CLIENTE_TIPOFIRE));
            sincronizarClienteWeb();
            nombreCliente.setActivo(false);
            direccionCliente.setActivo(false);
            emailCliente.setActivo(false);
            telefonoCliente.setActivo(false);
            contactoCliente.setActivo(false);
            webCliente.setActivo(false);
        }
        idTipoCliente = modeloSQL.getString(CLIENTE_ID_TIPOCLIENTE);
        tipoCliente.getEditText().setTextSize(sizeText * 2);
        tipoCliente.setGravedad(Gravity.CENTER_HORIZONTAL);
        tipoCliente.getEditText().setText(modeloSQL.getString(CLIENTE_DESCRIPCIONTIPOCLI).toUpperCase());

        id = modeloSQL.getString(CLIENTE_ID_CLIENTE);
        peso = modeloSQL.getInt(CLIENTE_PESOTIPOCLI);
        fechaInactivo = modeloSQL.getLong(CLIENTE_ACTIVO);
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
            tipoCliente.setImgBtnAccion3(R.drawable.clientev);
        } else if (peso > 3) {
            tipoCliente.setImgBtnAccion3(R.drawable.clientea);

        } else if (peso > 0) {
            tipoCliente.setImgBtnAccion3(R.drawable.clienter);

        } else {
            tipoCliente.setImgBtnAccion3(R.drawable.cliente);

        }

        ListaModeloSQL listaEventos = CRUDutil.setListaModelo(CAMPOS_EVENTO, EVENTO_CLIENTEREL, id, IGUAL);
        if (listaEventos.sizeLista() > 0) {
            btnVerEventos.setVisibility(View.VISIBLE);
        } else {
            btnVerEventos.setVisibility(View.GONE);
        }

        ListaModeloSQL listaNotas = CRUDutil.setListaModelo(CAMPOS_NOTA, NOTA_ID_RELACIONADO, id, IGUAL);

        if (listaNotas.sizeLista() > 0) {
            btnVerNotas.setVisibility(View.VISIBLE);
        }else {
            btnVerNotas.setVisibility(View.GONE);
        }

    }

    private void sincronizarClienteWeb() {

    }

    @Override
    protected void setAcciones() {


    }

    @Override
    protected void setTitulo() {

        if (nnn(actual)){

            if (actual.equals(PROSPECTO)){
                tituloNuevo = R.string.nuevo_prospecto;
                tituloSingular = R.string.prospecto;
                tituloPlural = R.string.prospectos;
            }else {
                tituloNuevo = R.string.nuevo_cliente;
                tituloSingular = R.string.cliente;
                tituloPlural = R.string.clientes;
            }
        }

    }

    @Override
    protected void setLayout() {

        cabecera = true;
        layoutItem = R.layout.item_list_layout;

    }

    @Override
    protected void setContenedor() {

        putDato(valores,CLIENTE_ID_TIPOCLIENTE, idTipoCliente);
        putDato(valores,CLIENTE_PESOTIPOCLI, peso);
        putDato(valores,CLIENTE_ACTIVO, fechaInactivo);

    }

    @Override
    protected boolean onBack() {

        if (origen != null && (origen.equals(PROYECTO) || origen.equals(PRESUPUESTO))) {
            bundle = new Bundle();
            bundle.putString(ORIGEN, actual);
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

            if (resultCode == RESULT_OK && requestCode == RECOGNIZE_SPEECH_ACTIVITY) {
                ArrayList<String> speech = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                grabarVoz = speech.get(0);

                switch (grabarVoz) {
                    case "mapa":
                    case "direccion":

                        if (!direccionCliente.getText().toString().equals("")) {

                            AppActivity.viewOnMapA(contexto, direccionCliente.getText().toString());

                        }
                        break;
                    case "email":
                    case "imeil":
                    case "correo":

                        if (!emailCliente.getText().toString().equals("")) {
                            AppActivity.enviarEmail(getContext(), emailCliente.getText().toString());
                        }
                        break;
                    case "llamar":
                    case "llamada":
                    case "llamar cliente":

                        if (!telefonoCliente.getText().toString().equals("")) {
                            AppActivity.hacerLlamada(AppActivity.getAppContext()
                                    , telefonoCliente.getText().toString(), activityBase);
                        }
                        break;
                    case "marcar":

                        if (!telefonoCliente.getText().toString().equals("")) {
                            AppActivity.hacerLlamada(AppActivity.getAppContext()
                                    , telefonoCliente.getText().toString());
                        }
                        break;

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

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView nombre, direccion, telefono, email, contacto;
        ViewImagenLayout imagen, imagenPeso;
        CardView card;
        RelativeLayout relativeLayout;

        public ViewHolderRV(View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.ry_item_list);
        }

        @Override
        public void bind(ModeloSQL modeloSQL) {

            ViewGroupLayout vistaCard = new ViewGroupLayout(contexto,relativeLayout,new CardView(contexto));
            card = (CardView) vistaCard.getViewGroup();
            LinearLayoutCompat mainLinear = (LinearLayoutCompat) vistaCard.addVista(new LinearLayoutCompat(contexto));
            mainLinear.setOrientation(ViewGroupLayout.ORI_LLC_HORIZONTAL);
            ViewGroupLayout vistaImagen = new ViewGroupLayout(contexto,mainLinear);
            vistaImagen.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL,1.5f);
            imagen = vistaImagen.addViewImagenLayout();
            imagenPeso = vistaImagen.addViewImagenLayout();
            ViewGroupLayout vistaForm = new ViewGroupLayout(contexto,mainLinear);
            vistaForm.setOrientacion(ViewGroupLayout.ORI_LL_VERTICAL,1);
            nombre = vistaForm.addTextView(modeloSQL.getString(CLIENTE_NOMBRE));
            direccion = vistaForm.addTextView(modeloSQL.getString(CLIENTE_DIRECCION));
            telefono = vistaForm.addTextView(modeloSQL.getString(CLIENTE_TELEFONO));
            email = vistaForm.addTextView(modeloSQL.getString(CLIENTE_EMAIL));
            contacto = vistaForm.addTextView(modeloSQL.getString(CLIENTE_CONTACTO));

            imagen.setImageUriCard(activityBase, modeloSQL.getString(CLIENTE_RUTAFOTO), 20);
            int peso = modeloSQL.getInt(CLIENTE_PESOTIPOCLI);
            if (peso > 6) {
                imagenPeso.setImageResourceCard(activityBase, R.drawable.clientev,10);
            } else if (peso > 3) {
                imagenPeso.setImageResourceCard(activityBase, R.drawable.clientea,10);
            } else if (peso > 0) {
                imagenPeso.setImageResourceCard(activityBase, R.drawable.clienter,10);
            } else {
                imagenPeso.setImageResourceCard(activityBase, R.drawable.cliente,10);
            }

            if (modeloSQL.getLong(CLIENTE_ACTIVO) > 0) {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
            } else {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_defecto));
            }

            super.bind(modeloSQL);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

}
