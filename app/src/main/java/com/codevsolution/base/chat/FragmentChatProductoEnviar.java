package com.codevsolution.base.chat;

import android.content.ContentValues;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.models.MsgChat;
import com.codevsolution.base.sqlite.ContratoSystem;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;


public class FragmentChatProductoEnviar extends FragmentChatBase implements
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


    }

    @Override
    protected void setInicio() {

        super.setInicio();


        gone(btnsave);
        gone(btndelete);
        listaUsers = new ArrayList<>();

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
            valores.put(DETCHAT_TIPO, ENVIADO);
            valores.put(DETCHAT_FECHA, TimeDateUtil.ahora());
            valores.put(DETCHAT_CREATE, TimeDateUtil.ahora());
            valores.put(DETCHAT_TIMESTAMP, TimeDateUtil.ahora());
            valores.put(DETCHAT_NOTIFICADO, 1);
            valores.put(DETCHAT_ID_CHAT, id);
            secuencia = CRUDutil.crearRegistroSec(CAMPOS_DETCHAT, id, TABLA_CHAT, valores);

            Modelo chat = CRUDutil.setModelo(campos, id);

            MsgChat msgChat = new MsgChat();
            msgChat.setMensaje(msgEnv.getText().toString());
            msgChat.setNombre(nombreChat);
            msgChat.setFecha(TimeDateUtil.ahora());
            msgChat.setIdDestino(chat.getString(CHAT_USUARIO));
            msgChat.setIdOrigen(idchatsp);

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
            listaMsgChat = CRUDutil.setListaModeloDetalle(CAMPOS_DETCHAT, id, TABLA_CHAT, null, DETCHAT_FECHA + " DESC");
            RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat_base);
            rvMsgChat.setAdapter(adaptadorDetChat);

        }
    }

}
