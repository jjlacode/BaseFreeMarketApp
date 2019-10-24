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
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ImagenLayout;
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
import static com.codevsolution.base.sqlite.ConsultaBD.checkQueryList;
import static com.codevsolution.base.sqlite.ConsultaBD.queryList;

public class FragmentCRUDCliente extends FragmentCRUD implements Interactor.ConstantesPry,
        ContratoPry.Tablas {


    private EditMaterial nombreCliente;
    private EditMaterial direccionCliente;
    private EditMaterial telefonoCliente;
    private EditMaterial emailCliente;
    private EditMaterial contactoCliente;
    private EditMaterialLayout tipoCliente;
    private ImageButton btnevento;
    private ImageButton mapa;
    private ImageButton llamada;
    private ImageButton mail;
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


    public FragmentCRUDCliente() {
        // Required empty public constructor
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
    protected void setLista() {

        if (actual == null){
            actual = PROSPECTO;
        }

        if (actual.equals(PROSPECTO)) {

            activityBase.toolbar.setSubtitle(R.string.prospectos);

            lista = CRUDutil.setListaModelo(campos, CLIENTE_DESCRIPCIONTIPOCLI, PROSPECTO, IGUAL);

        } else if (actual.equals(CLIENTE)) {

            activityBase.toolbar.setSubtitle(R.string.clientes);

            lista = CRUDutil.setListaModelo(campos, CLIENTE_DESCRIPCIONTIPOCLI, PROSPECTO, DIFERENTE);

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

        objTiposCli = queryList(CAMPOS_TIPOCLIENTE, null, null);

        btnevento.setVisibility(View.GONE);

        try {

            if (actual.equals(PROSPECTO)) {

                tipoCliente.setText(getString(R.string.prospecto));

                for (int i = 0; i < objTiposCli.size(); i++) {

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
        String seleccion = EVENTO_CLIENTEREL + " = '" + id + "'";
        if (checkQueryList(CAMPOS_EVENTO, seleccion, null)) {
            btnVerEventos.setVisibility(View.VISIBLE);
        } else {
            btnVerEventos.setVisibility(View.GONE);
        }

        if (peso > 6) {
            tipoCliente.setImgBtnAccion2(R.drawable.clientev);
        } else if (peso > 3) {
            tipoCliente.setImgBtnAccion2(R.drawable.clientea);

        } else if (peso > 0) {
            tipoCliente.setImgBtnAccion2(R.drawable.clienter);

        } else {
            tipoCliente.setImgBtnAccion2(R.drawable.cliente);

        }

        onUpdate();
    }

    @Override
    protected void setTabla() {

        tabla = TABLA_CLIENTE;

    }

    @Override
    protected void setCampos() {

        campos = ContratoPry.obtenerCampos(TABLA_CLIENTE);

    }

    @Override
    protected void setTablaCab() {

        tablaCab = ContratoPry.getTabCab(TABLA_CLIENTE);

    }

    @Override
    protected void setBundle() {

        proyecto = (ModeloSQL) bundle.getSerializable(TABLA_PROYECTO);
        if (actual == null){
            actual = PROSPECTO;
        }
        if (actual.equals(PROSPECTO)){
            tituloNuevo = R.string.nuevo_prospecto;
        }else {
            tituloNuevo = R.string.nuevo_cliente;
        }
    }

    @Override
    protected void setDatos() {

        activityBase.toolbar.setSubtitle(modeloSQL.getString(CLIENTE_NOMBRE));
        visible(btnevento);
        visible(btnNota);

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
            tipoCliente.setImgBtnAccion2(R.drawable.clientev);
        } else if (peso > 3) {
            tipoCliente.setImgBtnAccion2(R.drawable.clientea);

        } else if (peso > 0) {
            tipoCliente.setImgBtnAccion2(R.drawable.clienter);

        } else {
            tipoCliente.setImgBtnAccion2(R.drawable.cliente);

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


    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.cliente;
        tituloPlural = R.string.clientes;

    }


    @Override
    protected void setInicio() {

        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalle);

        imagen = (ImagenLayout) vistaForm.addVista(new ImagenLayout(contexto));

        tipoCliente = vistaForm.addEditMaterialLayout(getString(R.string.tipo_cliente));
        tipoCliente.setActivo(false);
        tipoCliente.btnAccion2Enable(true);

        vistaForm.addEditMaterialLayout(getString(R.string.nombre), CLIENTE_NOMBRE, null, null);
        vistaForm.addEditMaterialLayout(getString(R.string.direccion), CLIENTE_DIRECCION, ViewGroupLayout.MAPA, activityBase);
        vistaForm.addEditMaterialLayout(getString(R.string.telefono), CLIENTE_TELEFONO, ViewGroupLayout.LLAMADA, activityBase);
        vistaForm.addEditMaterialLayout(getString(R.string.email), CLIENTE_EMAIL, ViewGroupLayout.MAIL, activityBase);
        vistaForm.addEditMaterialLayout(getString(R.string.contacto), CLIENTE_CONTACTO, null, null);
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

        btnevento = vistabtn.addImageButtonSecundary(R.drawable.ic_evento_indigo);
        btnevento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = new Bundle();
                bundle.putSerializable(CLIENTE, modeloSQL);
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevoEvento());
            }

        });
        btnVerEventos = vistabtn.addImageButtonSecundary(R.drawable.ic_lista_eventos_indigo);
        btnVerEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = new Bundle();
                bundle.putSerializable(LISTA, new ListaModeloSQL(CAMPOS_EVENTO, EVENTO_CLIENTEREL, id, null, IGUAL, null));
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
            }
        });
        btnNota = vistabtn.addImageButtonSecundary(R.drawable.ic_nueva_nota_indigo);
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
                icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevaNota());            }
        });
        btnVerNotas = vistabtn.addImageButtonSecundary(R.drawable.ic_lista_notas_indigo);
        btnVerNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarBundle();
                bundle.putString(IDREL, modeloSQL.getString(CLIENTE_ID_CLIENTE));
                bundle.putString(SUBTITULO, modeloSQL.getString(CLIENTE_NOMBRE));
                bundle.putString(ORIGEN, CLIENTE);
                bundle.putString(ACTUAL,NOTA);
                bundle.putSerializable(LISTA,null);
                bundle.putSerializable(MODELO,null);
                bundle.putString(CAMPO_ID,null);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());            }
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
    protected void setLayout() {

        cabecera = true;
        layoutItem = R.layout.item_list_layout;

    }

    @Override
    protected void setContenedor() {

        setDato(CLIENTE_ID_TIPOCLIENTE, idTipoCliente);
        setDato(CLIENTE_PESOTIPOCLI, peso);
        setDato(CLIENTE_ACTIVO, fechaInactivo);

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
            imagen = vistaImagen.addImagenLayout();
            imagenPeso = vistaImagen.addImagenLayout();
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
