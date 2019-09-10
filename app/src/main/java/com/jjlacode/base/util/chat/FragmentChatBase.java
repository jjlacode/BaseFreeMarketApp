package com.jjlacode.base.util.chat;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjlacode.base.util.adapter.BaseViewHolder;
import com.jjlacode.base.util.adapter.ListaAdaptadorFiltroModelo;
import com.jjlacode.base.util.adapter.RVAdapter;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.base.util.android.AndroidUtil;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.crud.FragmentCRUD;
import com.jjlacode.base.util.models.FirebaseFormBase;
import com.jjlacode.base.util.models.ListaModelo;
import com.jjlacode.base.util.models.Modelo;
import com.jjlacode.base.util.models.MsgChat;
import com.jjlacode.base.util.sqlite.ContratoSystem;
import com.jjlacode.base.util.time.TimeDateUtil;
import com.jjlacode.freelanceproject.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.CAMPOS_DETCHAT;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.CHAT_CREATE;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.CHAT_ESPECIAL;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.CHAT_ID_CHAT;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.CHAT_NOMBRE;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.CHAT_TIMESTAMP;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.CHAT_TIPO;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.CHAT_TIPORETORNO;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.CHAT_USUARIO;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.DETCHAT_CREATE;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.DETCHAT_FECHA;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.DETCHAT_ID_CHAT;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.DETCHAT_MENSAJE;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.DETCHAT_NOTIFICADO;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.DETCHAT_TIMESTAMP;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.DETCHAT_TIPO;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.TABLA_CHAT;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.TABLA_DETCHAT;

public abstract class FragmentChatBase extends FragmentCRUD {

    protected String tipoChatOrigen = NULL;
    protected String tipoChatRetorno = NULL;
    protected String idchat;
    protected RecyclerView rvMsgChat;
    protected EditText msgEnv;
    protected ImageButton btnEnviar;
    protected ListaModelo listaMsgChat;
    protected LinearLayout lyEnvMsg;
    protected TextView actuar;
    protected FirebaseFormBase firebaseFormBase;
    protected String nombreChat;
    protected String tipoChatRetornoOld;
    protected String tipo;
    protected String nombre;

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new ListaAdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        View viewCab = inflater.inflate(R.layout.cabecera_fragment_chat_base, container, false);
        if (viewCab.getParent() != null) {
            ((ViewGroup) viewCab.getParent()).removeView(viewCab); // <- fix
        }
        frdetalleExtrasante.addView(viewCab);
        activityBase.fabInicio.show();
    }

    @Override
    protected void setTabla() {

        tabla = TABLA_CHAT;
    }

    @Override
    protected void setTablaCab() {

        tablaCab = ContratoSystem.getTabCab(tabla);
    }

    @Override
    protected void setCampos() {

        campos = ContratoSystem.obtenerCampos(tabla);

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_chat_base;
        layoutItem = R.layout.item_list_chat_base;

    }

    @Override
    protected void setBundle() {
        super.setBundle();

        idchat = getStringBundle(IDCHATF, NULL);
        tipo = getStringBundle(TIPO, NULL);
        nombre = getStringBundle(NOMBRECHAT, NULL);

        if (nn(id) && !id.equals(NULL)) {

            modelo = CRUDutil.setModelo(campos, id);
            tipoChatOrigen = modelo.getString(CHAT_TIPO);
            tipoChatRetorno = modelo.getString(CHAT_TIPORETORNO);
            idchat = modelo.getString(CHAT_USUARIO);
            AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, modelo.getString(CHAT_USUARIO));

            System.out.println("idchat = " + idchat);
            System.out.println("tipoChatOrigen = " + tipoChatOrigen);

        } else {

            if (nn(tipo)) {
                valores = new ContentValues();
                valores.put(CHAT_USUARIO, idchat);
                valores.put(CHAT_NOMBRE, nombre);
                valores.put(CHAT_TIPO, tipo);
                valores.put(CHAT_CREATE, TimeDateUtil.ahora());
                valores.put(CHAT_TIMESTAMP, TimeDateUtil.ahora());
                valores.put(CHAT_TIPORETORNO, tipo);
                valores.put(CHAT_ESPECIAL, 1);
                id = CRUDutil.crearRegistroId(TABLA_CHAT, valores);
                modelo = CRUDutil.setModelo(campos, id);
                tipoChatOrigen = modelo.getString(CHAT_TIPO);
                tipoChatRetorno = modelo.getString(CHAT_TIPORETORNO);
                AndroidUtil.setSharePreference(contexto, CHAT, tipo, id);
            } else {
                tipoChatRetorno = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
            }


        }

        if (nn(tipo)) {
            nombreChat = nombre;
        } else {
            getNombreChat();
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
            ListaModelo listaTemp = new ListaModelo();

            for (Modelo chat : lista.getLista()) {
                if (chat.getInt(CHAT_ESPECIAL) == 0) {
                    listaTemp.addModelo(chat);
                }
            }
            lista.clearAddAllLista(listaTemp);
        }
        gone(activityBase.fabNuevo);
        gone(lyEnvMsg);
        gone(actuar);


    }

    @Override
    protected void onClickRV(View v) {
        super.onClickRV(v);

        tipoChatRetorno = modelo.getString(CHAT_TIPORETORNO);
        getNombreChat();

        if (tipoChatRetorno == null || tipoChatRetorno.equals(NULL)) {
            tipoChatRetorno = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
        }

    }

    protected void getNombreChat() {

        idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);

        if (!idUser.equals(NULL)) {

            DatabaseReference dbFirebase = FirebaseDatabase.getInstance().getReference()
                    .child(tipoChatRetorno).child(idUser).child("nombreBase");

            ValueEventListener eventListenerProd = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    nombreChat = dataSnapshot.getValue(String.class);

                    if (nombreChat != null) {
                        cambiarTipoChatRetorno();
                    } else {
                        Toast.makeText(contexto, "Debe tener un nombre en el perfil de " + tipoChatRetorno, Toast.LENGTH_SHORT).show();
                        tipoChatRetorno = tipoChatRetornoOld;
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            dbFirebase.addValueEventListener(eventListenerProd);

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

        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, modelo.getString(CHAT_USUARIO));

        System.out.println("idChat = " + id);
        listaMsgChat = CRUDutil.setListaModeloDetalle(CAMPOS_DETCHAT, id, TABLA_CHAT, null, DETCHAT_FECHA + " DESC");

        RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat_base);
        rvMsgChat.setAdapter(adaptadorDetChat);
        gone(activityBase.fabNuevo);

        modelo = CRUDutil.setModelo(campos, id);
        activityBase.fabVoz.setSize(FloatingActionButton.SIZE_NORMAL);
        activityBase.fabInicio.setSize(FloatingActionButton.SIZE_NORMAL);
        if (nn(tipo)) {
            activityBase.toolbar.setTitle(modelo.getString(CHAT_NOMBRE));
            activityBase.toolbar.setSubtitle(getString(R.string.envio_noticias));
        } else {
            activityBase.toolbar.setTitle(modelo.getString(CHAT_NOMBRE));
            activityBase.toolbar.setSubtitle(modelo.getString(CHAT_TIPO));
        }
        idchat = modelo.getString(CHAT_USUARIO);

        visible(lyEnvMsg);
        visible(actuar);
        visible(frCabecera);

        if (nn(tipo)) {
            gone(btnback);
        }

        selectorChat(NULL);

    }

    @Override
    protected void setAcciones() {
        super.setAcciones();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChatRetorno != null && !tipoChatRetorno.equals(NULL) && nombreChat != null) {
                    enviarMensaje();
                } else {
                    Toast.makeText(contexto, "Debe elegir un perfil con el que chatear", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    protected void selectorChat(String tipoChat) {


    }

    protected void cambiarTipoChatRetorno() {

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

        RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat_base);
        rvMsgChat.setAdapter(adaptadorDetChat);
        actuar.setText("Chatear como : " + tipoChatRetorno);

    }

    protected void enviarMensaje() {

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
            RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat_base);
            rvMsgChat.setAdapter(adaptadorDetChat);

            Modelo chat = CRUDutil.setModelo(campos, id);

            MsgChat msgChat = new MsgChat();
            msgChat.setMensaje(msgEnv.getText().toString());
            msgChat.setNombre(nombreChat);
            msgChat.setFecha(TimeDateUtil.ahora());
            msgChat.setIdDestino(chat.getString(CHAT_USUARIO));
            msgChat.setIdOrigen(AndroidUtil.getSharePreference(contexto, PREFERENCIAS, USERID, ""));
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

        rvMsgChat = view.findViewById(R.id.rvdetmsgchat_base);
        msgEnv = view.findViewById(R.id.msgchatenv_base);
        btnEnviar = view.findViewById(R.id.btn_envmsgchat_base);
        lyEnvMsg = view.findViewById(R.id.lyevnmsg_base);
        actuar = view.findViewById(R.id.tvchatactuar_base);

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
            nombre = itemView.findViewById(R.id.tvnombrechat_base);
            imgchat = itemView.findViewById(R.id.imgchat_base);
            card = itemView.findViewById(R.id.cardchat_base);
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
            mensaje = itemView.findViewById(R.id.tvmsgchat_base);
            fecha = itemView.findViewById(R.id.tvmsgchatfecha_base);
            card = itemView.findViewById(R.id.cardmsgchat_base);

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
