package com.codevsolution.base.chat;

import android.content.ContentValues;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.models.MsgChat;
import com.codevsolution.base.sqlite.ContratoSystem;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class EnviarNoticias extends FragmentChatBase implements
        ContratoSystem.Tablas, Interactor.ConstantesPry {

    private ArrayList<String> listaUsers;
    private String idchatsp;

    @Override
    protected void setLayout() {
        super.setLayout();

    }

    @Override
    protected void setBundle() {
        super.setBundle();

        if (nn(bundle)) {
            idchatsp = getStringBundle(IDCHATF, NULL);
        }

        comprobarUsers();

    }

    @Override
    protected void acciones() {
        super.acciones();
        visible(url);
    }

    @Override
    protected void setInicio() {

        super.setInicio();

        gone(btnsave);
        gone(btndelete);
        listaUsers = new ArrayList<>();

    }

    @Override
    protected void setDatos() {
        super.setDatos();

        activityBase.toolbar.setSubtitle(modeloSQL.getString(CHAT_NOMBRE) + " - " + getString(R.string.envio_noticias));
        gone(btnback);
        listaMsgChat = CRUDutil.setListaModeloDetalle(CAMPOS_DETCHAT, id);
        listaMsgChat = listaMsgChat.sort(DETCHAT_FECHA, DESCENDENTE);
        RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat_base);
        rvMsgChat.setAdapter(adaptadorDetChat);


    }

    protected void comprobarUsers() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query querydb = db.child(SUSCRIPCIONES).child(idchat);

        querydb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    listaUsers.add(child.getKey());
                }
                if (listaUsers.size() > 0) {
                    btnEnviar.setEnabled(true);
                } else {
                    btnEnviar.setEnabled(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void enviarMensaje() {

        if (msgEnv.getText() != null && !msgEnv.getText().toString().equals("")) {

            valores = new ContentValues();
            valores.put(DETCHAT_MENSAJE, msgEnv.getText().toString());
            valores.put(DETCHAT_URL, url.getText().toString());
            valores.put(DETCHAT_TIPO, ENVIADO);
            valores.put(DETCHAT_FECHA, TimeDateUtil.ahora());
            valores.put(DETCHAT_CREATE, TimeDateUtil.ahora());
            valores.put(DETCHAT_TIMESTAMP, TimeDateUtil.ahora());
            valores.put(DETCHAT_NOTIFICADO, 1);
            valores.put(DETCHAT_ID_CHAT, id);
            secuencia = CRUDutil.crearRegistroSec(CAMPOS_DETCHAT, id, valores);

            ModeloSQL chat = CRUDutil.updateModelo(campos, id);

            MsgChat msgChat = new MsgChat();
            msgChat.setMensaje(msgEnv.getText().toString());
            msgChat.setTipo(tipo);
            msgChat.setNombre(nombreChat);
            msgChat.setFecha(TimeDateUtil.ahora());
            msgChat.setIdDestino(chat.getString(CHAT_USUARIO));
            msgChat.setIdOrigen(idchatsp);
            msgChat.setUrl(url.getText().toString());

            for (String user : listaUsers) {
                FirebaseDatabase.getInstance().getReference().child(CHAT).child(user).push().setValue(msgChat);
            }
            StringBuilder msg = new StringBuilder();
            msg.append(msgEnv.getText().toString());
            msg.append("\n");

            msg.append(getString(R.string.mensajes_enviados));
            msg.append(" = ");
            msg.append(listaUsers.size());

            valores = new ContentValues();
            valores.put(DETCHAT_MENSAJE, msg.toString());
            CRUDutil.actualizarRegistro(TABLA_DETCHAT, id, secuencia, valores);
            msgEnv.setText("");
            listaMsgChat = CRUDutil.setListaModeloDetalle(CAMPOS_DETCHAT, id);
            listaMsgChat = listaMsgChat.sort(DETCHAT_FECHA, DESCENDENTE);
            RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat_base);
            rvMsgChat.setAdapter(adaptadorDetChat);

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

            mensaje.setText(modeloSQL.getString(DETCHAT_MENSAJE));
            fecha.setText(TimeDateUtil.getDateTimeString(modeloSQL.getLong(DETCHAT_FECHA)));

            if (tipo == ENVIADO) {

                card.setCardBackgroundColor(getContext().getResources().getColor(R.color.Color_msg_enviado));
                card.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                params.setMargins((int) (10 * densidad), (int) (10 * densidad), (int) (10 * densidad), 0);

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

            super.bind(modeloSQL);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVMsgChat(view);
        }
    }

}
