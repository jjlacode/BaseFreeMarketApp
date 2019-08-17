package com.jjlacode.freelanceproject.services;

import android.content.ContentValues;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.crud.ListaModelo;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.services.JobServiceBase;
import com.jjlacode.base.util.sqlite.ConsultaBD;
import com.jjlacode.base.util.time.TimeDateUtil;
import com.jjlacode.freelanceproject.logica.Interactor;
import com.jjlacode.freelanceproject.sqlite.ContratoPry;
import com.jjlacode.um.base.model.MsgChat;


public class AvisoMsgChat extends JobServiceBase implements JavaUtil.Constantes, Interactor.Constantes, ContratoPry.Tablas {

    private String idUser;
    private String idChat;
    private DatabaseReference dbFirebase;

    public AvisoMsgChat() {
    }

    @Override
    protected void setJob() {
        super.setJob();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        idUser = CRUDutil.getSharePreference(getApplicationContext(), PREFERENCIAS, USERID, NULL);


        if (auth.getUid() != null && auth.getUid().equals(idUser)) {

            dbFirebase = FirebaseDatabase.getInstance().getReference().child(CHAT).child(idUser);

            ValueEventListener eventListenerProd = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        MsgChat msgChat = child.getValue(MsgChat.class);
                        String idChild = child.getKey();

                        ContentValues values;
                        ListaModelo listaChats = new ListaModelo(CAMPOS_CHAT);
                        int cChat = 0;
                        boolean primerReg = false;
                        for (Modelo chat : listaChats.getLista()) {
                            String seleccion = DETCHAT_TIPO + " = '" + RECIBIDO + "'";
                            ListaModelo listaDetChat = new ListaModelo(CAMPOS_DETCHAT, chat.getString(CHAT_ID_CHAT), TABLA_CHAT, seleccion, DETCHAT_FECHA + " DESC");
                            Modelo detChat = null;
                            if (listaDetChat.getLista().size() > 0) {
                                detChat = listaDetChat.getLista().get(0);
                            } else {
                                primerReg = true;
                            }

                            if (chat.getString(CHAT_USUARIO).equals(msgChat.getIdOrigen()) &&
                                    msgChat.getTipoRetorno().equals(chat.getString(CHAT_TIPORETORNO)) &&
                                    msgChat.getTipo().equals(chat.getString(CHAT_TIPO))) {

                                values = new ContentValues();
                                values.put(CHAT_USUARIO, msgChat.getIdOrigen());
                                values.put(CHAT_NOMBRE, msgChat.getNombre());
                                values.put(CHAT_TIPO, msgChat.getTipo());
                                values.put(CHAT_TIPORETORNO, msgChat.getTipoRetorno());
                                values.put(CHAT_CREATE, TimeDateUtil.ahora());
                                values.put(CHAT_TIMESTAMP, TimeDateUtil.ahora());
                                CRUDutil.actualizarRegistro(chat, values);
                                if (primerReg || msgChat.getFecha() > detChat.getLong(DETCHAT_FECHA)) {
                                    idChat = chat.getString(CHAT_ID_CHAT);
                                    values = new ContentValues();
                                    values.put(DETCHAT_ID_CHAT, idChat);
                                    values.put(DETCHAT_MENSAJE, msgChat.getMensaje());
                                    values.put(DETCHAT_FECHA, msgChat.getFecha());
                                    values.put(DETCHAT_CREATE, TimeDateUtil.ahora());
                                    values.put(DETCHAT_TIMESTAMP, TimeDateUtil.ahora());
                                    values.put(DETCHAT_TIPO, RECIBIDO);
                                    int sec = ConsultaBD.secInsertRegistroDetalle(CAMPOS_DETCHAT, idChat, TABLA_CHAT, values);
                                    if (sec > 0) {
                                        detChat = ConsultaBD.queryObjectDetalle(CAMPOS_DETCHAT, idChat, sec);
                                        FirebaseDatabase.getInstance().getReference().child(CHAT).child(idUser).child(idChild).removeValue();
                                        Intent intent = new Intent(ACCION_AVISOMSGCHAT).putExtra(CHAT, detChat);
                                        sendBroadcast(intent);
                                    }
                                }
                                cChat++;
                            }
                        }
                        if (cChat == 0) {
                            values = new ContentValues();
                            values.put(CHAT_USUARIO, msgChat.getIdOrigen());
                            values.put(CHAT_NOMBRE, msgChat.getNombre());
                            values.put(CHAT_TIPO, msgChat.getTipo());
                            values.put(CHAT_TIPORETORNO, msgChat.getTipoRetorno());
                            values.put(CHAT_CREATE, TimeDateUtil.ahora());
                            values.put(CHAT_TIMESTAMP, TimeDateUtil.ahora());
                            idChat = ConsultaBD.idInsertRegistro(TABLA_CHAT, values);
                            values = new ContentValues();
                            values.put(DETCHAT_ID_CHAT, idChat);
                            values.put(DETCHAT_MENSAJE, msgChat.getMensaje());
                            values.put(DETCHAT_FECHA, msgChat.getFecha());
                            values.put(DETCHAT_CREATE, TimeDateUtil.ahora());
                            values.put(DETCHAT_TIMESTAMP, TimeDateUtil.ahora());
                            values.put(DETCHAT_TIPO, RECIBIDO);
                            int sec = ConsultaBD.secInsertRegistroDetalle(CAMPOS_DETCHAT, idChat, TABLA_CHAT, values);
                            if (sec > 0) {
                                Modelo detChat = ConsultaBD.queryObjectDetalle(CAMPOS_DETCHAT, idChat, sec);
                                FirebaseDatabase.getInstance().getReference().child(CHAT).child(idUser).child(idChild).removeValue();
                                Intent intent = new Intent(ACCION_AVISOMSGCHAT).putExtra(CHAT, detChat);
                                sendBroadcast(intent);

                            }
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            dbFirebase.addValueEventListener(eventListenerProd);

        }
    }

}
