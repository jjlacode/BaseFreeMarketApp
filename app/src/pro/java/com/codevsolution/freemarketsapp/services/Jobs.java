package com.codevsolution.freemarketsapp.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentValues;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.models.MsgChat;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.sqlite.ConsultaBD;
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

import static com.codevsolution.base.logica.InteractorBase.Constantes.CHAT;
import static com.codevsolution.base.logica.InteractorBase.Constantes.INDICE;
import static com.codevsolution.base.logica.InteractorBase.Constantes.PRODUCTOS;
import static com.codevsolution.base.logica.InteractorBase.Constantes.SORTEO;
import static com.codevsolution.base.logica.InteractorBase.Constantes.SORTEOCLI;
import static com.codevsolution.base.logica.InteractorBase.Constantes.SORTEOPRO;
import static com.codevsolution.base.logica.InteractorBase.Constantes.SUSCRIPCIONES;
import static com.codevsolution.base.logica.InteractorBase.Constantes.SYSTEM;
import static com.codevsolution.base.logica.InteractorBase.Constantes.USERID;


public class Jobs extends JobService implements JavaUtil.Constantes, Interactor.ConstantesPry, ContratoPry.Tablas {

    protected String idUserCode;
    protected String idUser;

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
    public boolean onStartJob(JobParameters params) {

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
                    if (SQLiteUtil.BD_backup(null, false) &&
                            SQLiteUtil.BD_backup(SYSTEM, idUser, false)) {
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

                    System.out.println("intent aviso evento");
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

                    //System.out.println("paso 1");
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


            ListaModeloSQL listaProd = CRUDutil.setListaModelo(CAMPOS_PRODUCTO);
            for (final ModeloSQL producto : listaProd.getLista()) {

                if (producto.getInt(PRODUCTO_SINCRO) == 1) {

                    DatabaseReference dbclon = FirebaseDatabase.getInstance().getReference();
                    dbclon.child(PRODUCTOPRO).child(producto.getString(PRODUCTO_ID_CLON)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Productos prodProv = dataSnapshot.getValue(Productos.class);
                            if (prodProv != null && producto.getLong(PRODUCTO_ULTIMASINCRO) < prodProv.getTimeStamp()) {
                                sincronizarClon(prodProv, producto);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            timeClon = ahora + MINUTOSLONG;
        }

        AutoArranquePro.scheduleJob(getApplicationContext());

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    protected void sincronizarClon(final Productos prodProv, ModeloSQL producto) {

        final String tipo = producto.getString(PRODUCTO_TIPO);
        String idClon = producto.getString(PRODUCTO_ID_CLON);
        Productos prod = new Productos();
        String id = null;
        String nombre = prodProv.getNombre();
        String descripcion = prodProv.getDescripcion();
        String alcance = prodProv.getAlcance();
        String categoria = prodProv.getCategoria();
        String subCategoria = prodProv.getSubCategoria();
        String referencia = prodProv.getRefprov();
        double precio = prodProv.getPrecio();
        double descuento = prodProv.getDescProv();

        if (idClon != null && !idClon.isEmpty() && idClon.equals(prodProv.getId())) {

            System.out.println("sincronizando prod");
            ContentValues valores = new ContentValues();

            if (tipo.equals(PRODUCTOCLI)) {

                id = producto.getString(PRODUCTO_ID_PRODFIRE);
                prod.setId(id);
                prod.setTipo(PRODUCTOCLI);
                if (producto.getInt(PRODUCTO_ACTIVO) == 1) {
                    prod.setActivo(true);
                } else {
                    prod.setActivo(false);
                }

                if (producto.getInt(PRODUCTO_SINCRONOMBRE) == 1) {
                    ConsultaBD.putDato(valores, PRODUCTO_NOMBRE, nombre);
                    prod.setNombre(nombre);
                }
                if (producto.getInt(PRODUCTO_SINCRODESCRIPCION) == 1) {
                    ConsultaBD.putDato(valores, PRODUCTO_DESCRIPCION, descripcion);
                    prod.setDescripcion(descripcion);
                }
                if (producto.getInt(PRODUCTO_SINCROALCANCE) == 1) {
                    ConsultaBD.putDato(valores, PRODUCTO_ALCANCE, alcance);
                    prod.setAlcance(alcance);
                }
                if (producto.getInt(PRODUCTO_SINCROCATEGORIA) == 1) {
                    ConsultaBD.putDato(valores, PRODUCTO_CATEGORIA, categoria);
                    prod.setCategoria(categoria);
                }
                if (producto.getInt(PRODUCTO_SINCROSUBCATEGORIA) == 1) {
                    ConsultaBD.putDato(valores, PRODUCTO_SUBCATEGORIA, subCategoria);
                    prod.setSubCategoria(subCategoria);
                }
                if (producto.getInt(PRODUCTO_SINCROREFERENCIA) == 1) {
                    ConsultaBD.putDato(valores, PRODUCTO_REFERENCIA, referencia);
                    prod.setRefprov(referencia);
                }
                if (producto.getInt(PRODUCTO_SINCROIMAGEN) == 1) {
                    ImagenUtil.copyImageFirestoreToCrud(idClon + PRODUCTOPRO, producto, "");
                }

            } else if (tipo.equals(PRODUCTOPRO)) {

                id = producto.getString(PRODUCTO_ID_PRODFIREPRO);
                prod.setId(id);
                prod.setTipo(PRODUCTOPRO);
                if (producto.getInt(PRODUCTO_ACTIVOPRO) == 1) {
                    prod.setActivo(true);
                } else {
                    prod.setActivo(false);
                }


                if (producto.getInt(PRODUCTO_SINCRONOMBREPRO) == 1) {
                    ConsultaBD.putDato(valores, PRODUCTO_NOMBREPRO, nombre);
                    prod.setNombre(nombre);
                }
                if (producto.getInt(PRODUCTO_SINCRODESCRIPCIONPRO) == 1) {
                    ConsultaBD.putDato(valores, PRODUCTO_DESCRIPCIONPRO, descripcion);
                    prod.setDescripcion(descripcion);
                }
                if (producto.getInt(PRODUCTO_SINCROALCANCEPRO) == 1) {
                    ConsultaBD.putDato(valores, PRODUCTO_ALCANCEPRO, alcance);
                    prod.setAlcance(alcance);
                }
                if (producto.getInt(PRODUCTO_SINCROCATEGORIAPRO) == 1) {
                    ConsultaBD.putDato(valores, PRODUCTO_CATEGORIAPRO, categoria);
                    prod.setCategoria(categoria);
                }
                if (producto.getInt(PRODUCTO_SINCROSUBCATEGORIAPRO) == 1) {
                    ConsultaBD.putDato(valores, PRODUCTO_SUBCATEGORIAPRO, subCategoria);
                    prod.setSubCategoria(subCategoria);
                }
                if (producto.getInt(PRODUCTO_SINCROREFERENCIAPRO) == 1) {
                    ConsultaBD.putDato(valores, PRODUCTO_REFERENCIAPRO, referencia);
                    prod.setRefprov(referencia);
                }
                if (producto.getInt(PRODUCTO_SINCROIMAGENPRO) == 1) {
                    ImagenUtil.copyImageFirestoreToCrud(idClon + PRODUCTOPRO, producto, PRO);
                }
            }

            ConsultaBD.putDato(valores, PRODUCTO_PRECIO, precio);
            ConsultaBD.putDato(valores, PRODUCTO_DESCPROV, descuento);
            ConsultaBD.putDato(valores, PRODUCTO_ULTIMASINCRO, TimeDateUtil.ahora());
            prod.setTimeStamp(TimeDateUtil.ahora());
            prod.setIdprov(idUserCode);

            CRUDutil.actualizarRegistro(producto, valores);

            if (id != null) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child(PRODUCTOS).child(id).setValue(prod);
            }

        }
    }

}
