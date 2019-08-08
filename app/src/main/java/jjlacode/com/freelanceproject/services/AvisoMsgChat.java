package jjlacode.com.freelanceproject.services;

import android.content.ContentValues;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.model.MsgChat;
import jjlacode.com.freelanceproject.util.crud.CRUDutil;
import jjlacode.com.freelanceproject.util.crud.ListaModelo;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.services.JobServiceBase;
import jjlacode.com.freelanceproject.util.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.util.time.TimeDateUtil;

import static jjlacode.com.freelanceproject.CommonPry.Constantes.ACCION_AVISOMSGCHAT;
import static jjlacode.com.freelanceproject.CommonPry.Constantes.CHAT;
import static jjlacode.com.freelanceproject.CommonPry.Constantes.FREELANCE;
import static jjlacode.com.freelanceproject.CommonPry.Constantes.IDFREELANCE;
import static jjlacode.com.freelanceproject.CommonPry.Constantes.RECIBIDO;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.CAMPOS_CHAT;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.CAMPOS_DETCHAT;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.CHAT_CREATE;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.CHAT_ID_CHAT;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.CHAT_NOMBRE;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.CHAT_TIMESTAMP;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.CHAT_TIPO;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.CHAT_USUARIO;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.DETCHAT_CREATE;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.DETCHAT_FECHA;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.DETCHAT_ID_CHAT;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.DETCHAT_MENSAJE;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.DETCHAT_TIMESTAMP;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.DETCHAT_TIPO;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.TABLA_CHAT;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.NULL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.PREFERENCIAS;

public class AvisoMsgChat extends JobServiceBase {

    private ArrayList<Modelo> listaEventos;
    private Query dbFreelance;
    private String idFreelance;
    private String idChat;

    public AvisoMsgChat() {
    }

    @Override
    protected void setJob() {
        super.setJob();

        idFreelance = CRUDutil.getSharePreference(getApplicationContext(), PREFERENCIAS, IDFREELANCE, NULL);
        dbFreelance = FirebaseDatabase.getInstance().getReference().child(FREELANCE).child(idFreelance).child(CHAT);

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

                        if (chat.getString(CHAT_USUARIO).equals(msgChat.getIdOrigen())) {

                            values = new ContentValues();
                            values.put(CHAT_USUARIO, msgChat.getIdOrigen());
                            values.put(CHAT_NOMBRE, msgChat.getNombre());
                            values.put(CHAT_TIPO, msgChat.getTipo());
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
                                    FirebaseDatabase.getInstance().getReference().child(FREELANCE).child(idFreelance).child(CHAT).child(idChild).removeValue();
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
                            FirebaseDatabase.getInstance().getReference().child(FREELANCE).child(idFreelance).child(CHAT).child(idChild).removeValue();
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

        dbFreelance.addValueEventListener(eventListenerProd);


    }

}
