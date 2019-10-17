package com.codevsolution.freemarketsapp.services;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.models.MsgChat;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.services.JobServiceBase;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.sqlite.SQLiteUtil;
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
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Jobs extends JobServiceBase implements JavaUtil.Constantes, Interactor.ConstantesPry, ContratoPry.Tablas {

    private static long time0 = 0;
    private static long time1 = 0;
    private static long time2 = 0;
    private static long timeSorteoCli = 0;
    private static long timeSorteoPro = 0;
    private static long timeClon = 0;
    private static int intentos = 0;
    private static long ganadorCli = 0;
    private static long ganadorPro = 0;

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

            ArrayList<ModeloSQL> listaEventos = Interactor.Calculos.comprobarEventos();

            for (ModeloSQL evento : listaEventos) {


                if (evento != null && evento.getLong(EVENTO_AVISO) > 0 &&
                        evento.getDouble(EVENTO_COMPLETADA) < 100) {

                    Intent intent = new Intent(ACCION_AVISOEVENTO).putExtra(EVENTO, evento);

                    sendBroadcast(intent);
                }
            }

            time2 = ahora + MINUTOSLONG;

        }

        if (ahora > timeSorteoCli) {

            final String iduser = AndroidUtil.getSharePreference(getBaseContext(), USERID, USERID, NULL);
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            Query querydb = db.child(INDICE + SORTEOCLI).child(iduser);
            querydb.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    System.out.println("paso 1");
                    for (final DataSnapshot sorteo : dataSnapshot.getChildren()) {

                        DatabaseReference dbfin = FirebaseDatabase.getInstance().getReference();
                        Query querydbfin = dbfin.child(GANADORSORTEO).child(sorteo.getKey());
                        querydbfin.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() == null) {
                                    System.out.println("paso 2");

                                    DatabaseReference dbnum = FirebaseDatabase.getInstance().getReference();
                                    Query querydbnum = dbnum.child(SORTEO).child(sorteo.getKey());
                                    querydbnum.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot1) {
                                            System.out.println("paso 3");

                                            DatabaseReference dbSus = FirebaseDatabase.getInstance().getReference();
                                            Query querydbSus = dbSus.child(SUSCRIPCIONES).child(sorteo.getKey());
                                            querydbSus.addListenerForSingleValueEvent(new ValueEventListener() {

                                                private long numSus;
                                                private String idGanador;

                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                                                    for (DataSnapshot sus : dataSnapshot2.getChildren()) {

                                                        numSus++;
                                                        long fecha = dataSnapshot1.getValue(long.class);
                                                        System.out.println("paso 4");

                                                        if (numSus > 0 && fecha > 0 && TimeDateUtil.ahora() >= fecha + (MINUTOSLONG * 2) &&
                                                                ganadorCli == 0) {

                                                            while (ganadorCli == 0) {
                                                                ganadorCli = Math.round((Math.random() % numSus) * numSus);
                                                            }

                                                            numSus = 0;

                                                            DatabaseReference dbSusg = FirebaseDatabase.getInstance().getReference();
                                                            Query querydbSusg = dbSusg.child(SUSCRIPCIONES).child(sorteo.getKey());
                                                            querydbSusg.addListenerForSingleValueEvent(new ValueEventListener() {

                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {

                                                                    for (final DataSnapshot participante : dataSnapshot3.getChildren()) {

                                                                        numSus++;

                                                                        if (numSus == ganadorCli) {
                                                                            idGanador = participante.getKey();

                                                                            DatabaseReference dbSusgan = FirebaseDatabase.getInstance().getReference();
                                                                            Query querydbSusgan = dbSusgan.child(SORTEOCLI).child(sorteo.getKey());
                                                                            querydbSusgan.addListenerForSingleValueEvent(new ValueEventListener() {

                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot4) {

                                                                                    Productos prod = dataSnapshot4.getValue(Productos.class);
                                                                                    StringBuilder msg = new StringBuilder();
                                                                                    msg.append(getBaseContext().getString(R.string.mensaje_ganador));
                                                                                    msg.append(" ");
                                                                                    msg.append(prod.getNombre());
                                                                                    msg.append("!!");
                                                                                    MsgChat msgChat = new MsgChat();
                                                                                    msgChat.setMensaje(msg.toString());
                                                                                    msgChat.setTipo(SORTEOCLI);
                                                                                    msgChat.setNombre(prod.getProveedor());
                                                                                    msgChat.setFecha(TimeDateUtil.ahora());
                                                                                    msgChat.setIdDestino(idGanador);
                                                                                    msgChat.setIdOrigen(prod.getId());

                                                                                    FirebaseDatabase.getInstance().getReference().child(CHAT).child(idGanador).push().setValue(msgChat);
                                                                                    FirebaseDatabase.getInstance().getReference().child(GANADORSORTEO).child(sorteo.getKey()).setValue(idGanador);
                                                                                    ganadorCli = 0;

                                                                                    Intent intent = new Intent(ACCION_AVISOSORTEO).putExtra(GANADORSORTEO, idGanador).
                                                                                            putExtra(SORTEO, prod.getId()).putExtra(PRODUCTO, prod.getNombre()).putExtra(ACTUAL, SORTEOCLI);

                                                                                    sendBroadcast(intent);

                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                        } else {

                                                                            DatabaseReference dbSusgan = FirebaseDatabase.getInstance().getReference();
                                                                            Query querydbSusgan = dbSusgan.child(SORTEOCLI).child(sorteo.getKey());
                                                                            querydbSusgan.addListenerForSingleValueEvent(new ValueEventListener() {

                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot4) {

                                                                                    Productos prod = dataSnapshot4.getValue(Productos.class);
                                                                                    MsgChat msgChat = new MsgChat();
                                                                                    StringBuilder msg = new StringBuilder();
                                                                                    msg.append(getBaseContext().getString(R.string.mensaje_no_ganador));
                                                                                    msg.append(" de ");
                                                                                    msg.append(prod.getNombre());
                                                                                    msgChat.setMensaje(msg.toString());
                                                                                    msgChat.setTipo(SORTEOCLI);
                                                                                    msgChat.setNombre(prod.getProveedor());
                                                                                    msgChat.setFecha(TimeDateUtil.ahora());
                                                                                    msgChat.setIdDestino(participante.getKey());
                                                                                    msgChat.setIdOrigen(prod.getId());

                                                                                    FirebaseDatabase.getInstance().getReference().child(CHAT).child(participante.getKey()).push().setValue(msgChat);

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
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            timeSorteoCli = ahora + MINUTOSLONG;

        }

        if (ahora > timeSorteoPro) {

            final String iduser = AndroidUtil.getSharePreference(getBaseContext(), USERID, USERID, NULL);
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            Query querydb = db.child(INDICE + SORTEOPRO).child(iduser);
            querydb.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    System.out.println("paso 1");
                    for (final DataSnapshot sorteo : dataSnapshot.getChildren()) {

                        DatabaseReference dbfin = FirebaseDatabase.getInstance().getReference();
                        Query querydbfin = dbfin.child(GANADORSORTEO).child(sorteo.getKey());
                        querydbfin.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() == null) {
                                    System.out.println("paso 2");

                                    DatabaseReference dbnum = FirebaseDatabase.getInstance().getReference();
                                    Query querydbnum = dbnum.child(SORTEO).child(sorteo.getKey());
                                    querydbnum.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot1) {
                                            System.out.println("paso 3");

                                            DatabaseReference dbSus = FirebaseDatabase.getInstance().getReference();
                                            Query querydbSus = dbSus.child(SUSCRIPCIONES).child(sorteo.getKey());
                                            querydbSus.addListenerForSingleValueEvent(new ValueEventListener() {

                                                private long numSus;
                                                private String idGanador;

                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                                                    for (DataSnapshot sus : dataSnapshot2.getChildren()) {

                                                        numSus++;
                                                        long fecha = dataSnapshot1.getValue(long.class);
                                                        System.out.println("paso 4");

                                                        if (numSus > 0 && fecha > 0 && TimeDateUtil.ahora() >= fecha + (MINUTOSLONG * 2) &&
                                                                ganadorPro == 0) {

                                                            while (ganadorPro == 0) {
                                                                ganadorPro = Math.round((Math.random() % numSus) * numSus);
                                                            }

                                                            numSus = 0;

                                                            DatabaseReference dbSusg = FirebaseDatabase.getInstance().getReference();
                                                            Query querydbSusg = dbSusg.child(SUSCRIPCIONES).child(sorteo.getKey());
                                                            querydbSusg.addListenerForSingleValueEvent(new ValueEventListener() {

                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {

                                                                    for (final DataSnapshot participante : dataSnapshot3.getChildren()) {

                                                                        numSus++;

                                                                        if (numSus == ganadorPro) {
                                                                            idGanador = participante.getKey();

                                                                            DatabaseReference dbSusgan = FirebaseDatabase.getInstance().getReference();
                                                                            Query querydbSusgan = dbSusgan.child(SORTEOPRO).child(sorteo.getKey());
                                                                            querydbSusgan.addListenerForSingleValueEvent(new ValueEventListener() {

                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot4) {

                                                                                    Productos prod = dataSnapshot4.getValue(Productos.class);
                                                                                    StringBuilder msg = new StringBuilder();
                                                                                    msg.append(getBaseContext().getString(R.string.mensaje_ganador));
                                                                                    msg.append(" ");
                                                                                    msg.append(prod.getNombre());
                                                                                    msg.append("!!");
                                                                                    MsgChat msgChat = new MsgChat();
                                                                                    msgChat.setMensaje(msg.toString());
                                                                                    msgChat.setTipo(SORTEOPRO);
                                                                                    msgChat.setNombre(prod.getProveedor());
                                                                                    msgChat.setFecha(TimeDateUtil.ahora());
                                                                                    msgChat.setIdDestino(idGanador);
                                                                                    msgChat.setIdOrigen(prod.getId());

                                                                                    FirebaseDatabase.getInstance().getReference().child(CHAT).child(idGanador).push().setValue(msgChat);
                                                                                    FirebaseDatabase.getInstance().getReference().child(GANADORSORTEO).child(sorteo.getKey()).setValue(idGanador);
                                                                                    ganadorPro = 0;

                                                                                    Intent intent = new Intent(ACCION_AVISOSORTEO).putExtra(GANADORSORTEO, idGanador).
                                                                                            putExtra(SORTEO, prod.getId()).putExtra(PRODUCTO, prod.getNombre()).putExtra(ACTUAL, SORTEOPRO);

                                                                                    sendBroadcast(intent);

                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                        } else {

                                                                            DatabaseReference dbSusgan = FirebaseDatabase.getInstance().getReference();
                                                                            Query querydbSusgan = dbSusgan.child(SORTEOPRO).child(sorteo.getKey());
                                                                            querydbSusgan.addListenerForSingleValueEvent(new ValueEventListener() {

                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot4) {

                                                                                    Productos prod = dataSnapshot4.getValue(Productos.class);
                                                                                    MsgChat msgChat = new MsgChat();
                                                                                    StringBuilder msg = new StringBuilder();
                                                                                    msg.append(getBaseContext().getString(R.string.mensaje_no_ganador));
                                                                                    msg.append(" de ");
                                                                                    msg.append(prod.getNombre());
                                                                                    msgChat.setMensaje(msg.toString());
                                                                                    msgChat.setTipo(SORTEOPRO);
                                                                                    msgChat.setNombre(prod.getProveedor());
                                                                                    msgChat.setFecha(TimeDateUtil.ahora());
                                                                                    msgChat.setIdDestino(participante.getKey());
                                                                                    msgChat.setIdOrigen(prod.getId());

                                                                                    FirebaseDatabase.getInstance().getReference().child(CHAT).child(participante.getKey()).push().setValue(msgChat);

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
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            timeSorteoPro = ahora + MINUTOSLONG;

        }

        if (ahora > timeClon) {


            DatabaseReference db = FirebaseDatabase.getInstance().getReference();

            db.child(INDICE + PRODUCTOCLI).child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot prodCli : dataSnapshot.getChildren()) {
                        System.out.println("prodCli = " + prodCli.getKey());

                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        db.child(PRODUCTOCLI).child(prodCli.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                final Productos prodProv = dataSnapshot1.getValue(Productos.class);

                                System.out.println("service prodProv = " + prodProv.isSincronizado());
                                if (prodProv != null && prodProv.isSincronizado() && prodProv.getIdClon() != null) {

                                    DatabaseReference dbclon = FirebaseDatabase.getInstance().getReference();
                                    dbclon.child(PRODUCTOPRO).child(prodProv.getIdClon()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            sincronizarClon(prodProv);
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


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            db = FirebaseDatabase.getInstance().getReference();
            db.child(INDICE + PRODUCTOPRO).child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot prodCli : dataSnapshot.getChildren()) {
                        System.out.println("prodCli = " + prodCli.getKey());

                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        db.child(PRODUCTOPRO).child(prodCli.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                final Productos prodProv = dataSnapshot1.getValue(Productos.class);

                                System.out.println("service prodProv = " + prodProv.isSincronizado());
                                if (prodProv != null && prodProv.isSincronizado() && prodProv.getIdClon() != null) {

                                    DatabaseReference dbclon = FirebaseDatabase.getInstance().getReference();
                                    dbclon.child(PRODUCTOPRO).child(prodProv.getIdClon()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            sincronizarClon(prodProv);
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

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            timeClon = ahora + MINUTOSLONG;
        }

        AutoArranquePro.scheduleJob(getApplicationContext());

    }

    protected void sincronizarClon(final Productos prodProv) {

        final String tipo = prodProv.getCategoria();

        if (prodProv.getIdClon() != null && !prodProv.getIdClon().isEmpty()) {

            System.out.println("sync");
            final String id = prodProv.getId();
            final String idClon = prodProv.getIdClon();

            if (id != null && idClon != null) {

                final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                Query querydb = db.child(PRODUCTOPRO).child(idClon);
                querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                    private Productos prodProvClon;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        prodProvClon = dataSnapshot.getValue(Productos.class);
                        if (prodProvClon.getTimeStamp() > prodProv.getTimeStamp()) {
                            System.out.println("sincronizando prod");
                            prodProvClon.setDescProv(prodProv.getDescProv());
                            prodProvClon.setIdClon(prodProvClon.getId());
                            prodProvClon.setId(id);
                            prodProvClon.setSincronizado(true);
                            prodProvClon.setTipo(prodProv.getTipo());
                            prodProvClon.setCategoria(tipo);
                            prodProvClon.setActivo(prodProv.isActivo());
                            prodProvClon.setWeb(prodProv.getWeb());
                            prodProvClon.setRefprov(prodProv.getRefprov());
                            prodProvClon.setTimeStamp(TimeDateUtil.ahora());
                            db.child(tipo).child(id).setValue(prodProvClon);
                            ImagenUtil.copyImageFirestore(idClon, id);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }
    }

}
