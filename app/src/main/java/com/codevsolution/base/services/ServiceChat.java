package com.codevsolution.base.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.models.MsgChat;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ConsultaBDBase;
import com.codevsolution.base.sqlite.ContratoSystem;
import com.codevsolution.base.time.TimeDateUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ServiceChat extends JobService implements ContratoSystem.Tablas,
        JavaUtil.Constantes, InteractorBase.Constantes {

    protected String idUserCode;
    protected String idUser;
    private String idChat;
    private DatabaseReference dbFirebase;
    private static String ultimoIdChild = NULL;

    public ServiceChat() {

    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.d(this.getClass().getSimpleName(), "onStartJob");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        idUser = AndroidUtil.getSharePreference(getApplicationContext(), USERID, USERID, NULL);
        idUserCode = AndroidUtil.getSharePreference(getApplicationContext(), USERID, USERIDCODE, NULL);
        ConsultaBDBase consultaBD = new ConsultaBDBase(new ConsultaBD());

        if (auth.getUid() != null && auth.getUid().equals(idUser)) {

            Handler mHandler = new Handler(getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {


                    if (idUserCode != null && !idUserCode.equals(NULL)) {
                        dbFirebase = FirebaseDatabase.getInstance().getReference().child(CHAT).child(idUserCode);

                        ValueEventListener eventListenerProd = new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {

                                for (DataSnapshot child : dataSnapshot.getChildren()) {

                                    MsgChat msgChat = child.getValue(MsgChat.class);
                                    String idChild = child.getKey();

                                    if (!ultimoIdChild.equals(idChild)) {
                                        ContentValues values;
                                        ListaModeloSQL listaChats = new ListaModeloSQL(CAMPOS_CHAT);
                                        int cChat = 0;
                                        boolean primerReg = false;
                                        for (ModeloSQL chat : listaChats.getLista()) {
                                            String seleccion = DETCHAT_TIPO + " = '" + RECIBIDO + "'";
                                            ListaModeloSQL listaDetChat = new ListaModeloSQL(CAMPOS_DETCHAT, chat.getString(CHAT_ID_CHAT), DETCHAT_TIPO, RECIBIDO);
                                            listaDetChat = listaDetChat.sort(DETCHAT_FECHA, DESCENDENTE);
                                            ModeloSQL detChat = null;
                                            if (listaDetChat.getLista().size() > 0) {
                                                detChat = listaDetChat.getLista().get(0);
                                            } else {
                                                primerReg = true;
                                            }

                                            if (chat.getString(CHAT_USUARIO).equals(msgChat.getIdOrigen()) && chat.getString(CHAT_TIPO).equals(msgChat.getTipo())) {

                                                values = new ContentValues();
                                                consultaBD.putDato(values, CHAT_USUARIO, msgChat.getIdOrigen());
                                                consultaBD.putDato(values, CHAT_NOMBRE, msgChat.getNombre());
                                                consultaBD.putDato(values, CHAT_TIPO, msgChat.getTipo());
                                                consultaBD.putDato(values, CHAT_CREATE, TimeDateUtil.ahora());
                                                consultaBD.putDato(values, CHAT_TIMESTAMP, TimeDateUtil.ahora());
                                                CRUDutil cruDutil = new CRUDutil();
                                                cruDutil.actualizarRegistro(chat, values);
                                                if (primerReg || msgChat.getFecha() > detChat.getLong(DETCHAT_FECHA)) {
                                                    idChat = chat.getString(CHAT_ID_CHAT);
                                                    values = new ContentValues();
                                                    consultaBD.putDato(values, DETCHAT_ID_CHAT, idChat);
                                                    consultaBD.putDato(values, DETCHAT_MENSAJE, msgChat.getMensaje());
                                                    consultaBD.putDato(values, DETCHAT_URL, msgChat.getUrl());
                                                    consultaBD.putDato(values, DETCHAT_FECHA, msgChat.getFecha());
                                                    consultaBD.putDato(values, DETCHAT_CREATE, TimeDateUtil.ahora());
                                                    consultaBD.putDato(values, DETCHAT_TIMESTAMP, TimeDateUtil.ahora());
                                                    consultaBD.putDato(values, DETCHAT_TIPO, RECIBIDO);
                                                    int sec = consultaBD.secInsertRegistroDetalle(CAMPOS_DETCHAT, idChat, values);
                                                    if (sec > 0) {
                                                        detChat = consultaBD.queryObjectDetalle(CAMPOS_DETCHAT, idChat, sec);
                                                        FirebaseDatabase.getInstance().getReference().child(CHAT).child(idUserCode).child(idChild).removeValue();
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
                                            consultaBD.putDato(values, CHAT_USUARIO, msgChat.getIdOrigen());
                                            consultaBD.putDato(values, CHAT_NOMBRE, msgChat.getNombre());
                                            consultaBD.putDato(values, CHAT_TIPO, msgChat.getTipo());
                                            consultaBD.putDato(values, CHAT_CREATE, TimeDateUtil.ahora());
                                            consultaBD.putDato(values, CHAT_TIMESTAMP, TimeDateUtil.ahora());
                                            idChat = consultaBD.idInsertRegistro(TABLA_CHAT, values);
                                            values = new ContentValues();
                                            consultaBD.putDato(values, DETCHAT_ID_CHAT, idChat);
                                            consultaBD.putDato(values, DETCHAT_MENSAJE, msgChat.getMensaje());
                                            consultaBD.putDato(values, DETCHAT_URL, msgChat.getUrl());
                                            consultaBD.putDato(values, DETCHAT_FECHA, msgChat.getFecha());
                                            consultaBD.putDato(values, DETCHAT_CREATE, TimeDateUtil.ahora());
                                            consultaBD.putDato(values, DETCHAT_TIMESTAMP, TimeDateUtil.ahora());
                                            consultaBD.putDato(values, DETCHAT_TIPO, RECIBIDO);
                                            int sec = consultaBD.secInsertRegistroDetalle(CAMPOS_DETCHAT, idChat, values);
                                            if (sec > 0) {
                                                ModeloSQL detChat = consultaBD.queryObjectDetalle(CAMPOS_DETCHAT, idChat, sec);
                                                FirebaseDatabase.getInstance().getReference().child(CHAT).child(idUserCode).child(idChild).removeValue();
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

                    setJob();

                    jobFinished(params, false);

                }

            });

            AutoArranqueChat.scheduleJob(getApplicationContext());
            return true;
        }
        return false;
    }

    protected void setJob() {


    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}
