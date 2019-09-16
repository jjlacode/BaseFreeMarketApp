package com.codevsolution.base.chat;

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
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.models.FirebaseFormBase;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.models.MsgChat;
import com.codevsolution.base.sqlite.ContratoSystem;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class FragmentChatBase extends FragmentCRUD implements ContratoSystem.Tablas {

    protected String idchat;
    protected RecyclerView rvMsgChat;
    protected EditText msgEnv;
    protected ImageButton btnEnviar;
    protected ListaModelo listaMsgChat;
    protected LinearLayout lyEnvMsg;
    protected TextView actuar;
    protected FirebaseFormBase firebaseFormBase;
    protected String nombreChat;
    protected String nombre;
    protected boolean especial;

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
        nombre = getStringBundle(NOMBRECHAT, NULL);

        if (nn(id) && !id.equals(NULL)) {

            modelo = CRUDutil.setModelo(campos, id);
            idchat = modelo.getString(CHAT_USUARIO);
            AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, modelo.getString(CHAT_USUARIO));

            System.out.println("idchat = " + idchat);

        } else {

            valores = new ContentValues();
            valores.put(CHAT_USUARIO, idchat);
            valores.put(CHAT_NOMBRE, nombre);
            valores.put(CHAT_CREATE, TimeDateUtil.ahora());
            valores.put(CHAT_TIMESTAMP, TimeDateUtil.ahora());
            valores.put(CHAT_ESPECIAL, 1);
            id = CRUDutil.crearRegistroId(TABLA_CHAT, valores);
            modelo = CRUDutil.setModelo(campos, id);
            AndroidUtil.setSharePreference(contexto, CHAT, ID_CHAT, id);
            especial = true;

        }

        if (nn(nombre)) {
            nombreChat = nombre;
        } else {
            getNombreChat();
        }

    }

    @Override
    protected void setLista() {
        super.setLista();


        activityBase.toolbar.setTitle(CHAT);
        ListaModelo listaTemp = new ListaModelo();

        for (Modelo chat : lista.getLista()) {
            if (chat.getInt(CHAT_ESPECIAL) == 0) {
                listaTemp.addModelo(chat);
            }
        }

        lista.clearAddAllLista(listaTemp);

        gone(activityBase.fabNuevo);
        gone(lyEnvMsg);
        gone(actuar);


    }

    @Override
    protected void onClickRV(View v) {
        super.onClickRV(v);

        if (nombreChat == null || nombreChat.equals(NULL) || nombreChat.equals("")) {
            getNombreChat();
        }

    }

    protected void getNombreChat() {

        idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);

        if (!idUser.equals(NULL)) {

            String tipo = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
            System.out.println("tipo = " + tipo);
            DatabaseReference dbFirebase = FirebaseDatabase.getInstance().getReference()
                    .child(tipo).child(idUser).child("nombreBase");

            ValueEventListener eventListenerProd = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    nombreChat = dataSnapshot.getValue(String.class);

                    if (nombreChat == null) {

                        Toast.makeText(contexto, "Debe tener un nombre en el perfil", Toast.LENGTH_SHORT).show();

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

        if (nombreChat == null || nombreChat.equals(NULL) || nombreChat.equals("")) {
            getNombreChat();
        }

        System.out.println("idChat = " + id);
        listaMsgChat = CRUDutil.setListaModeloDetalle(CAMPOS_DETCHAT, id, TABLA_CHAT, null, DETCHAT_FECHA + " DESC");

        RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat_base);
        rvMsgChat.setAdapter(adaptadorDetChat);
        gone(activityBase.fabNuevo);

        modelo = CRUDutil.setModelo(campos, id);
        activityBase.fabVoz.setSize(FloatingActionButton.SIZE_NORMAL);
        activityBase.fabInicio.setSize(FloatingActionButton.SIZE_NORMAL);
        if (especial) {
            activityBase.toolbar.setTitle(modelo.getString(CHAT_NOMBRE));
            activityBase.toolbar.setSubtitle(getString(R.string.envio_noticias));
        } else {
            System.out.println("chat nombre " + modelo.getString(CHAT_NOMBRE));
            activityBase.toolbar.setSubtitle(modelo.getString(CHAT_NOMBRE));
        }
        idchat = modelo.getString(CHAT_USUARIO);

        visible(lyEnvMsg);
        visible(actuar);
        visible(frCabecera);

        if (especial) {
            gone(btnback);
        }


    }

    @Override
    protected void setAcciones() {
        super.setAcciones();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("nombreChat = " + nombreChat);
                if (nombreChat != null && !nombreChat.equals(NULL)) {
                    enviarMensaje();
                } else {
                    Toast.makeText(contexto, getString(R.string.debe_tener_perfil), Toast.LENGTH_SHORT).show();
                }

            }
        });


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
            msgChat.setIdOrigen(AndroidUtil.getSharePreference(contexto, USERID, USERID, ""));

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

            TextView nombreCli = view.findViewById(R.id.tvnombrechat_base);


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


            card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_defecto));
            try {
                setImagenFireStoreCircle(contexto, modelo.getString(CHAT_USUARIO), imgchat);
            } catch (Exception e) {
                e.printStackTrace();
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
