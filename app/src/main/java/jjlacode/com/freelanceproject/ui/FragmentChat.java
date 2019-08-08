package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.model.MsgChat;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroModelo;
import jjlacode.com.freelanceproject.util.adapter.RVAdapter;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.crud.CRUDutil;
import jjlacode.com.freelanceproject.util.crud.FragmentCRUD;
import jjlacode.com.freelanceproject.util.crud.ListaModelo;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.time.TimeDateUtil;

import static android.app.Activity.RESULT_OK;

public class FragmentChat extends FragmentCRUD {

    private String tipoChat = NULL;
    private String idchat;
    private RecyclerView rvMsgChat;
    private EditText msgEnv;
    private ImageButton btnEnviar;
    private ListaModelo listaMsgChat;

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
            tipoChat = modelo.getString(CHAT_TIPO);
            idchat = modelo.getString(CHAT_USUARIO);
            CRUDutil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, idchat);
            System.out.println("idchat = " + idchat);
            System.out.println("tipoChat = " + tipoChat);
        }

    }

    @Override
    protected void setLista() {
        super.setLista();

        if (!tipoChat.equals(NULL)) {
            ListaModelo listaTemp = new ListaModelo();

            for (Modelo chat : lista.getLista()) {
                if (chat.getString(CHAT_TIPO).equals(tipoChat)) {
                    listaTemp.addModelo(chat);
                }
            }
            lista.clearAddAllLista(listaTemp);
            activityBase.toolbar.setTitle(CHAT + " " + tipoChat);

        } else {
            activityBase.toolbar.setTitle(CHAT);
        }
        gone(activityBase.fab);
        gone(frCabecera);


    }

    @Override
    protected void onClickRV(View v) {
        super.onClickRV(v);

        activityBase.toolbar.setTitle(modelo.getString(CHAT_NOMBRE));
        activityBase.toolbar.setSubtitle(modelo.getString(CHAT_TIPO));

    }

    @Override
    protected void setContenedor() {

    }

    @Override
    protected void setImagen() {

    }

    @Override
    protected void setDatos() {

        listaMsgChat = CRUDutil.setListaModeloDetalle(CAMPOS_DETCHAT, id, TABLA_CHAT, null, DETCHAT_FECHA + " DESC");

        System.out.println("view = " + view);
        RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat);
        rvMsgChat.setAdapter(adaptadorDetChat);
        gone(activityBase.fab);

        modelo = CRUDutil.setModelo(campos, id);
        activityBase.toolbar.setTitle(modelo.getString(CHAT_NOMBRE));
        idchat = modelo.getString(CHAT_USUARIO);
        CRUDutil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, idchat);

        visible(frCabecera);

    }

    @Override
    protected void setAcciones() {
        super.setAcciones();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enviarMensaje();

            }
        });

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
            msgChat.setIdOrigen(CRUDutil.getSharePreference(contexto, PREFERENCIAS, IDFREELANCE, ""));
            msgChat.setTipo(FREELANCE);

            FirebaseDatabase.getInstance().getReference().child(chat.getString(CHAT_TIPO)).child(chat.getString(CHAT_USUARIO)).child(CHAT).push().setValue(msgChat);
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

        gone(btnsave);

    }

    @Override
    public void onPause() {
        super.onPause();
        CRUDutil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, NULL);

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

                    if (grabarVoz.length() >= 5) {
                        orden = grabarVoz.substring(0, 5);
                        mensaje = grabarVoz.substring(5);
                        if (orden.equals("chat ")) {
                            msgEnv.setText(mensaje);
                            enviarMensaje();

                        }
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

            setImagenFireStoreCircle(contexto, modelo.getString(CHAT_USUARIO), imgchat);

            if (modelo.getInt(CHAT_TIPO) > 0) {

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
