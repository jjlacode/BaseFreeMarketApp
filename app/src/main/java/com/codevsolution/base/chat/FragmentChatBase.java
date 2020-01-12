package com.codevsolution.base.chat;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.FirebaseFormBase;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.models.MsgChat;
import com.codevsolution.base.sqlite.ContratoSystem;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class FragmentChatBase extends FragmentCRUD implements ContratoSystem.Tablas {

    protected String idchat;
    protected RecyclerView rvMsgChat;
    protected EditText msgEnv;
    protected EditText url;
    protected ImageButton btnEnviar;
    protected ListaModeloSQL listaMsgChat;
    protected LinearLayout lyEnvMsg;
    protected TextView actuar;
    protected FirebaseFormBase firebaseFormBase;
    protected String nombreChat;
    protected String nombre;
    protected String tipo;

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
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_chat_base;
        layoutItem = R.layout.item_list_chat_base;

    }

    @Override
    protected void setBundle() {
        super.setBundle();

        idchat = getStringBundle(IDCHATF, NULL);
        nombre = getStringBundle(NOMBRECHAT, NULL);
        tipo = getStringBundle(TIPO, NULL);

        if (nn(id) && !id.equals(NULL)) {

            modeloSQL = updateModelo(campos, id);
            idchat = modeloSQL.getString(CHAT_USUARIO);
            AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, modeloSQL.getString(CHAT_USUARIO));

            System.out.println("idchat = " + idchat);

        } else if (nn(idchat) && !idchat.equals(NULL)) {

            ListaModeloSQL listaChats = setListaModelo(CAMPOS_CHAT);
            for (ModeloSQL chat : listaChats.getLista()) {
                if (chat.getString(CHAT_TIPO).equals(tipo) && chat.getString(CHAT_USUARIO).equals(idchat)) {
                    id = chat.getString(CHAT_ID_CHAT);
                    break;
                }
            }

            if (id == null) {
                valores = new ContentValues();
                consultaBD.putDato(valores, CHAT_USUARIO, idchat);
                consultaBD.putDato(valores, CHAT_NOMBRE, nombre);
                consultaBD.putDato(valores, CHAT_CREATE, TimeDateUtil.ahora());
                consultaBD.putDato(valores, CHAT_TIMESTAMP, TimeDateUtil.ahora());
                consultaBD.putDato(valores, CHAT_TIPO, tipo);
                id = crudUtil.crearRegistroId(TABLA_CHAT, valores);
                modeloSQL = crudUtil.updateModelo(campos, id);
            }
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

        subTitulo = CHAT;
        activityBase.toolbar.setSubtitle(subTitulo);
        ListaModeloSQL listaTemp = new ListaModeloSQL();

        for (ModeloSQL chat : lista.getLista()) {
            if (chat.getString(CHAT_TIPO).equals(CHAT)) {
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

        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, modeloSQL.getString(CHAT_USUARIO));

        if (nombreChat == null || nombreChat.equals(NULL) || nombreChat.equals("")) {
            getNombreChat();
        }

        System.out.println("idChat = " + id);
        listaMsgChat = crudUtil.setListaModeloDetalle(CAMPOS_DETCHAT, id);
        listaMsgChat = listaMsgChat.sort(DETCHAT_FECHA, DESCENDENTE);

        RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat_base);
        rvMsgChat.setAdapter(adaptadorDetChat);
        gone(activityBase.fabNuevo);

        modeloSQL = crudUtil.updateModelo(campos, id);
        activityBase.fabVoz.setSize(FloatingActionButton.SIZE_NORMAL);
        activityBase.fabInicio.setSize(FloatingActionButton.SIZE_NORMAL);
        tipo = modeloSQL.getString(CHAT_TIPO);

        System.out.println("chat nombre " + modeloSQL.getString(CHAT_NOMBRE));
        subTitulo = modeloSQL.getString(CHAT_NOMBRE);
        activityBase.toolbar.setSubtitle(subTitulo);
        idchat = modeloSQL.getString(CHAT_USUARIO);

        visible(lyEnvMsg);
        visible(actuar);
        visible(frCabecera);

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
            consultaBD.putDato(valores, DETCHAT_MENSAJE, msgEnv.getText().toString());
            consultaBD.putDato(valores, DETCHAT_URL, url.getText().toString());
            consultaBD.putDato(valores, DETCHAT_TIPO, ENVIADO);
            consultaBD.putDato(valores, DETCHAT_FECHA, TimeDateUtil.ahora());
            consultaBD.putDato(valores, DETCHAT_CREATE, TimeDateUtil.ahora());
            consultaBD.putDato(valores, DETCHAT_TIMESTAMP, TimeDateUtil.ahora());
            consultaBD.putDato(valores, DETCHAT_NOTIFICADO, 1);
            consultaBD.putDato(valores, DETCHAT_ID_CHAT, id);
            crudUtil.crearRegistro(CAMPOS_DETCHAT, id, valores);
            listaMsgChat = crudUtil.setListaModeloDetalle(CAMPOS_DETCHAT, id);
            listaMsgChat = listaMsgChat.sort(DETCHAT_FECHA, DESCENDENTE);
            RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat_base);
            rvMsgChat.setAdapter(adaptadorDetChat);

            ModeloSQL chat = crudUtil.updateModelo(campos, id);

            MsgChat msgChat = new MsgChat();
            msgChat.setMensaje(msgEnv.getText().toString());
            msgChat.setTipo(chat.getString(CHAT_TIPO));
            msgChat.setUrl(url.getText().toString());
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
        tituloPlural = tituloSingular;
    }

    @Override
    protected void setInicio() {

        rvMsgChat = view.findViewById(R.id.rvdetmsgchat_base);
        msgEnv = view.findViewById(R.id.msgchatenv_base);
        btnEnviar = view.findViewById(R.id.btn_envmsgchat_base);
        lyEnvMsg = view.findViewById(R.id.lyevnmsg_base);
        actuar = view.findViewById(R.id.tvchatactuar_base);
        url = view.findViewById(R.id.urlchatenv_base);

        gone(btnsave);

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    /*
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
                    String clave = getPref(PreferenciasBase.CLAVEVOZ, "");
                    if (speech != null && clave != null && (clave.equals("") || speech.get(0).contains(clave))) {

                        if (speech.get(0).contains(clave)) {
                            grabarVoz = speech.get(0).replace(clave, "").toLowerCase();
                        } else {
                            grabarVoz = speech.get(0).toLowerCase();
                        }

                        if (grabarVoz.contains(Estilos.getString(contexto, "enviar"))) {
                            enviarMensaje();

                        } else if (grabarVoz.contains(Estilos.getString(contexto, "borrar"))) {
                            msgEnv.setText("");

                        } else {
                            msgEnv.setText(grabarVoz);

                        }
                    }
            }


        }
    }

     */

    @Override
    protected void speechProcess(String speech) {
        super.speechProcess(speech);

        String[] results = speech.split(Pattern.quote(" "));
        for (String result : results) {
            System.out.println("result = " + result);

            if (result.equalsIgnoreCase(Estilos.getString(contexto, "enviar"))) {

                enviarMensaje();
                break;
            }

            if (result.equalsIgnoreCase(Estilos.getString(contexto, "borrar"))) {

                msgEnv.setText("");
                break;
            }
        }
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<ModeloSQL> entrada) {

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
        public void bind(ModeloSQL modeloSQL) {

            nombre.setText(modeloSQL.getCampos(CHAT_NOMBRE));


            card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_defecto));
            try {
                setImagenFireStoreCircle(contexto, modeloSQL.getString(CHAT_USUARIO), imgchat);
            } catch (Exception e) {
                e.printStackTrace();
            }


            super.bind(modeloSQL);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    public static class ViewHolderRVMsgChat extends BaseViewHolder implements TipoViewHolder {

        TextView mensaje, fecha;
        CardView card;
        WebView webView;
        ProgressBar progressBarWebCard;
        NestedScrollView lylweb;

        public ViewHolderRVMsgChat(View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.tvmsgchat_base);
            fecha = itemView.findViewById(R.id.tvmsgchatfecha_base);
            card = itemView.findViewById(R.id.cardmsgchat_base);
            webView = itemView.findViewById(R.id.browserwebl_chat_base);
            progressBarWebCard = itemView.findViewById(R.id.progressBarWebCardchat);
            lylweb = itemView.findViewById(R.id.lylweb_chat_base);

        }

        @Override
        public void bind(ModeloSQL modeloSQL) {

            int tipo = modeloSQL.getInt(DETCHAT_TIPO);
            String idChat = modeloSQL.getString(DETCHAT_ID_CHAT);
            CRUDutil crudUtil = new CRUDutil();
            ModeloSQL chat = crudUtil.updateModelo(CAMPOS_CHAT, idChat);
            String tipoChat = chat.getString(CHAT_TIPO);

            if (tipoChat.equals(CHAT)) {
                mensaje.setText(modeloSQL.getString(DETCHAT_MENSAJE));
                fecha.setText(TimeDateUtil.getDateTimeString(modeloSQL.getLong(DETCHAT_FECHA)));

                if (tipo == ENVIADO) {

                    card.setCardBackgroundColor(getContext().getResources().getColor(R.color.Color_msg_enviado));
                    card.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                    params.setMargins((int) (double) (ancho * densidad) / 5, (int) (10 * densidad), (int) (10 * densidad), 0);

                    card.setLayoutParams(params);


                    card.setVisibility(View.VISIBLE);

                } else if (tipo == RECIBIDO) {

                    card.setCardBackgroundColor(getContext().getResources().getColor(R.color.Color_msg_recibido));
                    card.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_START);
                    params.setMargins((int) (10 * densidad), (int) (10 * densidad), (int) (double) (ancho * densidad) / 5, 0);

                    card.setLayoutParams(params);

                    card.setVisibility(View.VISIBLE);

                } else {
                    card.setVisibility(View.GONE);

                }

                String webprod = modeloSQL.getString(DETCHAT_URL);


                if (webprod != null && JavaUtil.isValidURL(webprod)) {

                    lylweb.setVisibility(View.VISIBLE);

                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setBuiltInZoomControls(true);
                    webView.getSettings().setDisplayZoomControls(false);

                    webView.setWebViewClient(new WebViewClient() {

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });
                    // Cargamos la web


                    webView.loadUrl(webprod);
                    webView.setWebChromeClient(new WebChromeClient() {
                        @Override
                        public void onProgressChanged(WebView view, int progress) {
                            progressBarWebCard.setProgress(0);
                            progressBarWebCard.setVisibility(View.VISIBLE);
                            progressBarWebCard.setProgress(progress * 1000);

                            progressBarWebCard.incrementProgressBy(progress);

                            if (progress == 100) {
                                progressBarWebCard.setVisibility(View.GONE);
                            }
                        }
                    });

                } else {
                    lylweb.setVisibility(View.GONE);
                }
            }

            super.bind(modeloSQL);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVMsgChat(view);
        }
    }
}
