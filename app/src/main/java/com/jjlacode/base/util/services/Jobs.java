package com.jjlacode.base.util.services;

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
import com.jjlacode.base.util.sqlite.ConsultaBD;
import com.jjlacode.base.util.sqlite.ContratoPry;
import com.jjlacode.base.util.sqlite.SQLiteUtil;
import com.jjlacode.base.util.time.TimeDateUtil;
import com.jjlacode.freelanceproject.logica.Interactor;
import com.jjlacode.um.base.model.MsgChat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Jobs extends JobServiceBase implements JavaUtil.Constantes, Interactor.Constantes, ContratoPry.Tablas {

    private String idUser;
    private String idChat;
    private DatabaseReference dbFirebase;
    private static long time0 = 0;
    private static long time1 = 0;
    private static long time2 = 0;
    private static String ultimoIdChild = NULL;
    private static int intentos = 0;

    public Jobs() {
    }

    @Override
    protected void setJob() {
        super.setJob();

        long ahora = TimeDateUtil.ahora();
        int minutosCopia = 5;

        if (ahora > time0) {
            long timeStamp = CRUDutil.getSharePreference(getApplicationContext(), PREFERENCIAS, TIMESTAMP, 0L);

            if (ahora < timeStamp + (MINUTOSLONG * minutosCopia)) {

                try {
                    if (SQLiteUtil.BD_backup(null, true)) {
                        System.out.println("Creada copia de bd");
                        time0 = ahora + (MINUTOSLONG * (minutosCopia - 1));
                        CRUDutil.setSharePreference(getApplicationContext(), PREFERENCIAS, TIMESTAMP, 0L);
                    } else {
                        System.out.println("Falló la copia de seguridad");
                        intentos++;
                        if (intentos >= 10) {
                            intentos = 0;
                            time0 = ahora + (MINUTOSLONG * (minutosCopia - 1));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        Calendar c = new GregorianCalendar();

        if (ahora > time1 && c.get(Calendar.HOUR_OF_DAY) == 0) {
            long timeStamp = CRUDutil.getSharePreference(getApplicationContext(), PREFERENCIAS, TIMESTAMPDIA, 0L);

            if (ahora < timeStamp + (DIASLONG)) {

                try {
                    if (SQLiteUtil.BD_backup(null, true)) {
                        System.out.println("Creada copia de bd diaria");
                        time1 = ahora + (DIASLONG);
                    } else {
                        System.out.println("Falló la copia de seguridad diaria");
                        intentos++;
                        if (intentos >= 10) {
                            intentos = 0;
                            time1 = ahora + (DIASLONG);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        if (ahora > time2) {

            ArrayList<Modelo> listaEventos = Interactor.Calculos.comprobarEventos();

            for (Modelo evento : listaEventos) {


                if (evento != null && evento.getLong(EVENTO_AVISO) > 0 &&
                        evento.getDouble(EVENTO_COMPLETADA) < 100) {

                    Intent intent = new Intent(ACCION_AVISOEVENTO).putExtra(EVENTO, evento);

                    sendBroadcast(intent);
                }
            }

            time2 = ahora + MINUTOSLONG;

        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        idUser = CRUDutil.getSharePreference(getApplicationContext(), PREFERENCIAS, USERID, NULL);


        if (auth.getUid() != null && auth.getUid().equals(idUser)) {

            dbFirebase = FirebaseDatabase.getInstance().getReference().child(CHAT).child(idUser);

            ValueEventListener eventListenerProd = new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        MsgChat msgChat = child.getValue(MsgChat.class);
                        String idChild = child.getKey();

                        if (!ultimoIdChild.equals(idChild)) {
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
                                            ultimoIdChild = idChild;

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
                                    ultimoIdChild = idChild;

                                }
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
