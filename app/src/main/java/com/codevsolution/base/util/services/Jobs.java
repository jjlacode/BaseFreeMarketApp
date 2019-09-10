package com.codevsolution.base.util.services;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.codevsolution.base.util.JavaUtil;
import com.codevsolution.base.util.android.AndroidUtil;
import com.codevsolution.base.util.models.Modelo;
import com.codevsolution.base.util.models.MsgChat;
import com.codevsolution.base.util.models.Productos;
import com.codevsolution.base.util.sqlite.ContratoPry;
import com.codevsolution.base.util.sqlite.SQLiteUtil;
import com.codevsolution.base.util.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Jobs extends JobServiceBase implements JavaUtil.Constantes, Interactor.ConstantesPry, ContratoPry.Tablas {

    private static long time0 = 0;
    private static long time1 = 0;
    private static long time2 = 0;
    private static long time3 = 0;
    private static int intentos = 0;
    private static long ganador = 0;

    public Jobs() {
    }

    @Override
    protected void setJob() {
        super.setJob();

        long ahora = TimeDateUtil.ahora();
        int minutosCopia = 5;

        if (ahora > time0) {
            long timeStamp = AndroidUtil.getSharePreference(getApplicationContext(), PREFERENCIAS, TIMESTAMP, 0L);

            if (ahora < timeStamp + (MINUTOSLONG * minutosCopia)) {

                try {
                    if (SQLiteUtil.BD_backup(null, true)) {
                        System.out.println("Creada copia de bd");
                        time0 = ahora + (MINUTOSLONG * (minutosCopia - 1));
                        AndroidUtil.setSharePreference(getApplicationContext(), PREFERENCIAS, TIMESTAMP, 0L);
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
            long timeStamp = AndroidUtil.getSharePreference(getApplicationContext(), PREFERENCIAS, TIMESTAMPDIA, 0L);

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

        if (ahora > time3) {

            final String iduser = AndroidUtil.getSharePreference(getBaseContext(), USERID, USERID, NULL);
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            Query querydb = db.child(INDICE + SORTEO).child(iduser);
            querydb.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (final DataSnapshot sorteo : dataSnapshot.getChildren()) {

                        DatabaseReference dbnum = FirebaseDatabase.getInstance().getReference();
                        Query querydbnum = dbnum.child(SORTEO).child(sorteo.getKey());
                        querydbnum.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot1) {

                                DatabaseReference dbSus = FirebaseDatabase.getInstance().getReference();
                                Query querydbSus = dbSus.child(SUSCRIPCIONES).child(sorteo.getKey());
                                querydbSus.addListenerForSingleValueEvent(new ValueEventListener() {

                                    private long numSus;
                                    private String idGanador;

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                                        for (DataSnapshot sus : dataSnapshot2.getChildren()) {

                                            numSus++;

                                            if (numSus >= dataSnapshot1.getValue(long.class) && ganador == 0) {

                                                ganador = Math.round((Math.random() % numSus) * numSus);

                                                numSus = 0;

                                                DatabaseReference dbSusg = FirebaseDatabase.getInstance().getReference();
                                                Query querydbSusg = dbSusg.child(SUSCRIPCIONES).child(sorteo.getKey());
                                                querydbSusg.addListenerForSingleValueEvent(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {

                                                        for (final DataSnapshot participantesTipo : dataSnapshot3.getChildren()) {

                                                            for (final DataSnapshot participante : participantesTipo.getChildren()) {

                                                                numSus++;

                                                                if (numSus == ganador) {

                                                                    idGanador = participante.getKey();

                                                                    DatabaseReference dbSusgan = FirebaseDatabase.getInstance().getReference();
                                                                    Query querydbSusgan = dbSusgan.child(PRODUCTOS).child(sorteo.getKey());
                                                                    querydbSusgan.addListenerForSingleValueEvent(new ValueEventListener() {

                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot4) {

                                                                            Productos prod = dataSnapshot4.getValue(Productos.class);
                                                                            StringBuilder msg = new StringBuilder();
                                                                            msg.append(getBaseContext().getString(R.string.mensaje_ganador));
                                                                            msg.append("\n");
                                                                            msg.append(prod.getNombre());
                                                                            msg.append("\n");
                                                                            msg.append(getBaseContext().getString(R.string.envio_prod_sorteo));
                                                                            MsgChat msgChat = new MsgChat();
                                                                            msgChat.setMensaje(msg.toString());
                                                                            msgChat.setNombre(prod.getProveedor());
                                                                            msgChat.setFecha(TimeDateUtil.ahora());
                                                                            msgChat.setIdDestino(idGanador);
                                                                            msgChat.setIdOrigen(prod.getIdprov());
                                                                            msgChat.setTipo(participantesTipo.getKey());
                                                                            msgChat.setTipoRetorno(prod.getTipo());
                                                                            ;

                                                                            FirebaseDatabase.getInstance().getReference().child(CHAT).child(idGanador).push().setValue(msgChat);
                                                                            FirebaseDatabase.getInstance().getReference().child(INDICE + SORTEOS).child(iduser).child(sorteo.getKey()).setValue(idGanador);

                                                                            Intent intent = new Intent(ACCION_AVISOSORTEO).putExtra(GANADORSORTEO, idGanador).
                                                                                    putExtra(SORTEO, prod.getId()).putExtra(PRODUCTO, prod.getNombre());

                                                                            sendBroadcast(intent);

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                } else {

                                                                    DatabaseReference dbSusgan = FirebaseDatabase.getInstance().getReference();
                                                                    Query querydbSusgan = dbSusgan.child(PRODUCTOS).child(sorteo.getKey());
                                                                    querydbSusgan.addListenerForSingleValueEvent(new ValueEventListener() {

                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot4) {

                                                                            Productos prod = dataSnapshot4.getValue(Productos.class);
                                                                            MsgChat msgChat = new MsgChat();
                                                                            StringBuilder msg = new StringBuilder();
                                                                            msg.append(getBaseContext().getString(R.string.mensaje_no_ganador));
                                                                            msg.append("\n");
                                                                            msg.append(prod.getNombre());
                                                                            msg.append("\n");
                                                                            msg.append(getBaseContext().getString(R.string.proximo_sorteo));
                                                                            msgChat.setMensaje(msg.toString());
                                                                            msgChat.setNombre(prod.getProveedor());
                                                                            msgChat.setFecha(TimeDateUtil.ahora());
                                                                            msgChat.setIdDestino(participante.getKey());
                                                                            msgChat.setIdOrigen(prod.getIdprov());
                                                                            msgChat.setTipo(participantesTipo.getKey());
                                                                            msgChat.setTipoRetorno(prod.getTipo());

                                                                            FirebaseDatabase.getInstance().getReference().child(CHAT).child(participante.getKey()).push().setValue(msgChat);

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });


                                                                }
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            time3 = ahora + MINUTOSLONG;

        }




    }

}
