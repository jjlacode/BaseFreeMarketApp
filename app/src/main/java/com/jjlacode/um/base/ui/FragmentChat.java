package com.jjlacode.um.base.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.jjlacode.base.util.adapter.BaseViewHolder;
import com.jjlacode.base.util.adapter.ListaAdaptadorFiltroModelo;
import com.jjlacode.base.util.adapter.RVAdapter;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.crud.FragmentCRUD;
import com.jjlacode.base.util.crud.ListaModelo;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.time.TimeDateUtil;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.sqlite.ContratoPry;
import com.jjlacode.um.base.model.MsgChat;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class FragmentChat extends FragmentCRUD {

    private String tipoChatOrigen = NULL;
    private String tipoChatRetorno = NULL;
    private String idchat;
    private RecyclerView rvMsgChat;
    private EditText msgEnv;
    private ImageButton btnEnviar;
    private ListaModelo listaMsgChat;
    private ImageButton btnClienteWeb;
    private ImageButton btnFreelance;
    private ImageButton btnComercial;
    private ImageButton btnEcommerce;
    private ImageButton btnLugar;
    private ImageButton btnEmpresa;
    private ImageButton btnProveedorWeb;
    private LinearLayout lyEnvMsg;
    private TextView actuar;

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new ListaAdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    @Override
    protected void setTabla() {

        tabla = TABLA_CHAT;
    }

    @Override
    protected void setTablaCab() {

        tablaCab = ContratoPry.getTabCab(tabla);
    }

    @Override
    protected void setCampos() {

        campos = ContratoPry.obtenerCampos(tabla);

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_chat;
        layoutItem = R.layout.item_list_chat;
        layoutCabecera = R.layout.cabecera_fragment_chat;

    }

    @Override
    protected void setBundle() {
        super.setBundle();

        //id = getStringBundle(CAMPO_ID,NULL);

        if (id != null) {

            modelo = CRUDutil.setModelo(campos, id);
            tipoChatOrigen = modelo.getString(CHAT_TIPO);
            tipoChatRetorno = modelo.getString(CHAT_TIPORETORNO);
            idchat = modelo.getString(CHAT_USUARIO);
            CRUDutil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, modelo.getString(CHAT_USUARIO));

            System.out.println("idchat = " + idchat);
            System.out.println("tipoChatOrigen = " + tipoChatOrigen);


            switch (tipoChatOrigen) {

                case CLIENTEWEB:
                    btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    break;

                case FREELANCE:
                    btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    break;

                case ECOMMERCE:
                    btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    break;

                case LUGAR:
                    btnLugar.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    break;

                case PROVEEDORWEB:
                    btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    break;

                case COMERCIAL:
                    btnComercial.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    break;

                case EMPRESA:
                    btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    break;

            }
        } else {
            tipoChatRetorno = CRUDutil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
        }

    }

    @Override
    protected void setLista() {
        super.setLista();

        selectorChat(ORIGEN);

        if (!tipoChatOrigen.equals(NULL)) {
            ListaModelo listaTemp = new ListaModelo();

            for (Modelo chat : lista.getLista()) {
                if (chat.getString(CHAT_TIPO).equals(tipoChatOrigen)) {
                    listaTemp.addModelo(chat);
                }
            }
            lista.clearAddAllLista(listaTemp);
            activityBase.toolbar.setTitle(CHAT + " " + tipoChatOrigen);

        } else {
            activityBase.toolbar.setTitle(CHAT);
        }
        gone(activityBase.fabNuevo);
        gone(lyEnvMsg);
        gone(actuar);


    }

    @Override
    protected void onClickRV(View v) {
        super.onClickRV(v);

        tipoChatRetorno = modelo.getString(CHAT_TIPORETORNO);

        if (tipoChatRetorno == null || tipoChatRetorno.equals(NULL)) {
            tipoChatRetorno = CRUDutil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
        }

    }

    @Override
    protected void setContenedor() {

    }

    @Override
    protected void setImagen() {

    }

    @Override
    protected void setDatos() {

        CRUDutil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, modelo.getString(CHAT_USUARIO));

        System.out.println("idChat = " + id);
        listaMsgChat = CRUDutil.setListaModeloDetalle(CAMPOS_DETCHAT, id, TABLA_CHAT, null, DETCHAT_FECHA + " DESC");

        RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat);
        rvMsgChat.setAdapter(adaptadorDetChat);
        gone(activityBase.fabNuevo);

        modelo = CRUDutil.setModelo(campos, id);
        activityBase.toolbar.setTitle(modelo.getString(CHAT_NOMBRE));
        activityBase.toolbar.setSubtitle(modelo.getString(CHAT_TIPO));
        idchat = modelo.getString(CHAT_USUARIO);

        visible(lyEnvMsg);
        visible(actuar);
        visible(frCabecera);

        selectorChat(NULL);

        if (tipoChatRetorno != null && !tipoChatRetorno.equals(NULL)) {

            actuar.setText("Chatear como : " + tipoChatRetorno);

            switch (tipoChatRetorno) {

                case CLIENTEWEB:
                    btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    break;

                case FREELANCE:
                    btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    break;

                case ECOMMERCE:
                    btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    break;

                case LUGAR:
                    btnLugar.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    break;

                case PROVEEDORWEB:
                    btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    break;

                case COMERCIAL:
                    btnComercial.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    break;

                case EMPRESA:
                    btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    break;
            }
        }

    }

    @Override
    protected void setAcciones() {
        super.setAcciones();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChatRetorno != null && !tipoChatRetorno.equals(NULL)) {
                    enviarMensaje();
                } else {
                    Toast.makeText(contexto, "Debe elegir un perfil con el que chatear", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void selectorChat(final String tipoChat) {

        btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnLugar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnComercial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        btnClienteWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChat.equals(ORIGEN)) {
                    tipoChatOrigen = CLIENTEWEB;
                    selector();
                } else {
                    tipoChatRetorno = CLIENTEWEB;
                    cambiarTipoChatRetorno();
                }

                btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnLugar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnComercial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
        });

        btnFreelance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChat.equals(ORIGEN)) {
                    tipoChatOrigen = FREELANCE;
                    selector();
                } else {
                    tipoChatRetorno = FREELANCE;
                    cambiarTipoChatRetorno();
                }

                btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnLugar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnComercial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        btnEcommerce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChat.equals(ORIGEN)) {
                    tipoChatOrigen = ECOMMERCE;
                    selector();
                } else {
                    tipoChatRetorno = ECOMMERCE;
                    cambiarTipoChatRetorno();
                }

                btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                btnLugar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnComercial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        btnLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChat.equals(ORIGEN)) {
                    tipoChatOrigen = LUGAR;
                    selector();
                } else {
                    tipoChatRetorno = LUGAR;
                    cambiarTipoChatRetorno();
                }

                btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnLugar.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnComercial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
        });

        btnProveedorWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChat.equals(ORIGEN)) {
                    tipoChatOrigen = PROVEEDORWEB;
                    selector();
                } else {
                    tipoChatRetorno = PROVEEDORWEB;
                    cambiarTipoChatRetorno();
                }

                btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnLugar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnComercial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
        });

        btnComercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChat.equals(ORIGEN)) {
                    tipoChatOrigen = COMERCIAL;
                    selector();
                } else {
                    tipoChatRetorno = COMERCIAL;
                    cambiarTipoChatRetorno();
                }

                btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnLugar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnComercial.setBackgroundColor(getResources().getColor(R.color.colorSecondary));

            }
        });

        btnEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChat.equals(ORIGEN)) {
                    tipoChatOrigen = EMPRESA;
                    selector();
                } else {
                    tipoChatRetorno = EMPRESA;
                    cambiarTipoChatRetorno();
                }

                btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnLugar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnComercial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorSecondary));


            }
        });


    }

    private void cambiarTipoChatRetorno() {

        if (modelo.getString(tipoChatRetorno) == null || !modelo.getString(tipoChatRetorno).equals(tipoChatRetorno)) {
            ListaModelo listaChatUser = CRUDutil.setListaModelo(campos, CHAT_TIPORETORNO, tipoChatRetorno, IGUAL);
            boolean existeChat = false;
            for (Modelo chat : listaChatUser.getLista()) {
                if (chat.getString(CHAT_USUARIO).equals(modelo.getString(CHAT_USUARIO)) &&
                        chat.getString(CHAT_TIPORETORNO).equals(tipoChatRetorno)) {
                    id = chat.getString(CHAT_ID_CHAT);
                    modelo = CRUDutil.setModelo(campos, id);
                    existeChat = true;
                    break;
                }
            }
            if (!existeChat) {
                valores = new ContentValues();
                valores.put(CHAT_USUARIO, modelo.getString(CHAT_USUARIO));
                valores.put(CHAT_NOMBRE, modelo.getString(CHAT_NOMBRE));
                valores.put(CHAT_TIPO, modelo.getString(CHAT_TIPO));
                valores.put(CHAT_CREATE, TimeDateUtil.ahora());
                valores.put(CHAT_TIMESTAMP, TimeDateUtil.ahora());
                valores.put(CHAT_TIPORETORNO, tipoChatRetorno);
                id = CRUDutil.crearRegistroId(TABLA_CHAT, valores);
                modelo = CRUDutil.setModelo(campos, id);
            }

        }

        System.out.println("idChat = " + id);
        listaMsgChat = CRUDutil.setListaModeloDetalle(CAMPOS_DETCHAT, id, TABLA_CHAT, null, DETCHAT_FECHA + " DESC");

        RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat);
        rvMsgChat.setAdapter(adaptadorDetChat);
        actuar.setText("Chatear como : " + tipoChatRetorno);

    }

    private void enviarMensaje() {

        if (msgEnv.getText() != null && !msgEnv.getText().toString().equals("")) {

            valores = new ContentValues();
            valores.put(DETCHAT_MENSAJE, msgEnv.getText().toString());
            valores.put(DETCHAT_TIPO, ENVIADO);
            valores.put(DETCHAT_FECHA, TimeDateUtil.ahora());
            valores.put(DETCHAT_CREATE, TimeDateUtil.ahora());
            valores.put(DETCHAT_TIMESTAMP, TimeDateUtil.ahora());
            valores.put(DETCHAT_NOTIFICADO, 1);
            valores.put(DETCHAT_ID_CHAT, id);
            CRUDutil.crearRegistro(TABLA_DETCHAT, CAMPOS_DETCHAT, TABLA_CHAT, id, valores);
            listaMsgChat = CRUDutil.setListaModeloDetalle(CAMPOS_DETCHAT, id, TABLA_CHAT, null, DETCHAT_FECHA + " DESC");
            RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat);
            rvMsgChat.setAdapter(adaptadorDetChat);

            Modelo chat = CRUDutil.setModelo(campos, id);

            MsgChat msgChat = new MsgChat();
            msgChat.setMensaje(msgEnv.getText().toString());
            msgChat.setNombre(chat.getString(CHAT_NOMBRE));
            msgChat.setFecha(TimeDateUtil.ahora());
            msgChat.setIdDestino(chat.getString(CHAT_USUARIO));
            msgChat.setIdOrigen(CRUDutil.getSharePreference(contexto, PREFERENCIAS, USERID, ""));
            msgChat.setTipo(tipoChatRetorno);
            msgChat.setTipoRetorno(tipoChatOrigen);

            FirebaseDatabase.getInstance().getReference().child(CHAT).child(chat.getString(CHAT_USUARIO)).push().setValue(msgChat);
            msgEnv.setText("");
        }
    }

    @Override
    protected void setTitulo() {

        tituloSingular = R.string.chat;
    }

    @Override
    protected void setInicio() {

        rvMsgChat = view.findViewById(R.id.rvdetmsgchat);
        msgEnv = view.findViewById(R.id.msgchatenv);
        btnEnviar = view.findViewById(R.id.btn_envmsgchat);
        btnClienteWeb = view.findViewById(R.id.btn_cab_chat_clienteweb);
        btnFreelance = view.findViewById(R.id.btn_cab_chat_freelance);
        btnEcommerce = view.findViewById(R.id.btn_cab_chat_ecommerce);
        btnLugar = view.findViewById(R.id.btn_cab_chat_lugar);
        btnProveedorWeb = view.findViewById(R.id.btn_cab_chat_proveedorweb);
        btnComercial = view.findViewById(R.id.btn_cab_chat_comercial);
        btnEmpresa = view.findViewById(R.id.btn_cab_chat_empresa);
        lyEnvMsg = view.findViewById(R.id.lyevnmsg);
        actuar = view.findViewById(R.id.tvchatactuar);

        gone(btnsave);

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, getMetodo());

        System.out.println("requestCode = " + requestCode);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case RECOGNIZE_SPEECH_ACTIVITY:

                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    grabarVoz = speech.get(0).toLowerCase();
                    String orden = null;
                    String mensaje = null;

                    if (grabarVoz.equals("enviar")) {
                            enviarMensaje();

                    } else if (grabarVoz.equals("borrar")) {
                        msgEnv.setText("");

                    } else {
                        msgEnv.setText(grabarVoz);

                    }
            }


        }
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<Modelo> entrada) {

            TextView nombreCli = view.findViewById(R.id.tvnomclilcliente);


            nombreCli.setText(entrada.get(posicion).getCampos(CHAT_NOMBRE));
            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView nombre;
        ImageView imgchat;
        CardView card;

        public ViewHolderRV(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvnombrechat);
            imgchat = itemView.findViewById(R.id.imgchat);
            card = itemView.findViewById(R.id.cardchat);
        }

        @Override
        public void bind(Modelo modelo) {

            nombre.setText(modelo.getCampos(CHAT_NOMBRE));


            if (modelo.getString(CHAT_TIPO).equals("")) {

                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));

            } else {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_defecto));
                try {
                    setImagenFireStoreCircle(contexto, modelo.getString(CHAT_USUARIO) + modelo.getString(CHAT_TIPO), imgchat);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            super.bind(modelo);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    public class ViewHolderRVMsgChat extends BaseViewHolder implements TipoViewHolder {

        TextView mensaje, fecha;
        CardView card;

        public ViewHolderRVMsgChat(View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.tvmsgchat);
            fecha = itemView.findViewById(R.id.tvmsgchatfecha);
            card = itemView.findViewById(R.id.cardmsgchat);

        }

        @Override
        public void bind(Modelo modelo) {

            int tipo = modelo.getInt(DETCHAT_TIPO);

            mensaje.setText(modelo.getString(DETCHAT_MENSAJE));
            fecha.setText(TimeDateUtil.getDateTimeString(modelo.getLong(DETCHAT_FECHA)));

            if (tipo == ENVIADO) {

                card.setCardBackgroundColor(getResources().getColor(R.color.Color_msg_enviado));
                card.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                params.setMargins((int) (double) (ancho * densidad) / 5, (int) (10 * densidad), (int) (10 * densidad), 0);

                card.setLayoutParams(params);


                visible(card);

            } else if (tipo == RECIBIDO) {

                card.setCardBackgroundColor(getResources().getColor(R.color.Color_msg_recibido));
                card.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                params.setMargins((int) (10 * densidad), (int) (10 * densidad), (int) (double) (ancho * densidad) / 5, 0);

                card.setLayoutParams(params);

                visible(card);

            } else {
                gone(card);

            }

            super.bind(modelo);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVMsgChat(view);
        }
    }
}
