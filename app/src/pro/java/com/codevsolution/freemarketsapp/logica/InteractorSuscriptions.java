package com.codevsolution.freemarketsapp.logica;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chargebee.Environment;
import com.chargebee.ListResult;
import com.chargebee.Result;
import com.chargebee.models.Customer;
import com.chargebee.models.HostedPage;
import com.chargebee.models.PortalSession;
import com.chargebee.models.Subscription;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.models.FirebaseFormBase;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.freemarketsapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InteractorSuscriptions extends Interactor {

    private String siteName;
    String apiCB;
    String idUser;
    String perfil;
    Context contexto;
    private FirebaseFormBase firebaseFormBase;
    private Customer customer;
    private String planMensual;
    private ArrayList<Subscription> listaSus;
    private PortalSession portalSession;
    private OnCreateSesionPortal onCreateSesionPortalListener;
    private OnNotSubscriptions onNotSubscriptionsListener;
    private String msgErrorSesionPortal = "Error creando la seseion del portal";
    private String planTrimestral;
    private String planSemestral;
    private String planAnual;
    private OnGetPlanes onGetPlanesListener;

    public InteractorSuscriptions(Context contexto) {
        this.contexto = contexto;
        init();
    }

    public InteractorSuscriptions(Context contexto, OnCreateSesionPortal onCreateSesionPortalListener,
                                  OnNotSubscriptions onNotSubscriptionsListener, OnGetPlanes onGetPlanesListener) {
        this.contexto = contexto;
        this.onCreateSesionPortalListener = onCreateSesionPortalListener;
        this.onNotSubscriptionsListener = onNotSubscriptionsListener;
        this.onGetPlanesListener = onGetPlanesListener;
        init();
    }

    public void setOnNotSubscriptionsListener(OnNotSubscriptions onNotSubscriptionsListener) {
        this.onNotSubscriptionsListener = onNotSubscriptionsListener;
    }

    public void setOnCreateSesionPortalListener(OnCreateSesionPortal onCreateSesionPortalListener) {
        this.onCreateSesionPortalListener = onCreateSesionPortalListener;
    }

    public void setOnGetPlanesListener(OnGetPlanes onGetPlanesListener) {
        this.onGetPlanesListener = onGetPlanesListener;
    }

    public String getPlanMensual() {
        return planMensual;
    }

    public String getPlanTrimestral() {
        return planTrimestral;
    }

    public String getPlanSemestral() {
        return planSemestral;
    }

    public String getPlanAnual() {
        return planAnual;
    }

    public void setMsgErrorSesionPortal(String msgErrorSesionPortal) {
        this.msgErrorSesionPortal = msgErrorSesionPortal;
    }

    private void init() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(CONFIG);
        setEnviroment(db);
        getPlanes(db);
    }

    protected void getFirebaseFormBase() {

        idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);
        perfil = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);

        if (!idUser.equals(NULL)) {

            DatabaseReference dbFirebase = FirebaseDatabase.getInstance().getReference()
                    .child(perfil).child(idUser);

            ValueEventListener eventListenerProd = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    firebaseFormBase = dataSnapshot.getValue(FirebaseFormBase.class);

                    if (firebaseFormBase == null) {
                        Toast.makeText(contexto, contexto.getString(R.string.debe_tener_perfil) + perfil, Toast.LENGTH_SHORT).show();
                    } else {

                        Thread th = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                onFirebaseFormBase();
                            }
                        });
                        th.start();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            dbFirebase.addValueEventListener(eventListenerProd);

        }

    }

    private void onFirebaseFormBase() {

        Result result = null;
        try {
            //Environment.configure("codevsolution-test","test_RqYREPeEdnp7KP16xeQTDRfzAg7cdB7xt");
            result = Customer.retrieve(idUser).request();
            customer = result.customer();
            comprobarSuscripciones(new CheckSubscriptions() {
                @Override
                public void onNotSubscriptions() {

                    if (onNotSubscriptionsListener != null) {
                        onNotSubscriptionsListener.onNotSubscriptions();
                    }
                }

                @Override
                public void onProductLimit() {

                }

                @Override
                public void onError(String msgError) {

                }

                @Override
                public void onCheckSuscriptionsOk(ArrayList<Subscription> listaSuscripciones) {

                    crearSesionPortal(new CheckSesionPortal() {
                        @Override
                        public void onChekOk(String web) {

                            if (onCreateSesionPortalListener != null) {
                                onCreateSesionPortalListener.onCreateSesionOk(web);
                            }
                        }

                        @Override
                        public void onCheckFailed(String mensajeError) {
                            if (onCreateSesionPortalListener != null) {
                                onCreateSesionPortalListener.onCreateSesionFailed(mensajeError);
                            }
                        }
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            crearCustomer();
        }

    }

    public interface OnNotSubscriptions {

        void onNotSubscriptions();
    }

    public interface OnCreateSesionPortal {

        void onCreateSesionOk(String web);

        void onCreateSesionFailed(String mensajeError);
    }

    public void comprobarSuscripciones(final CheckSubscriptions checkSubscriptionsListener) {

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(CONFIG);
        db.child(SITENAME).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String siteName = dataSnapshot.getValue(String.class);
                db.child(APICB).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String apiCB = dataSnapshot.getValue(String.class);
                        Environment.configure(siteName, apiCB);

                        final String idUser = AndroidUtil.getSharePreference(AppActivity.getAppContext(), USERID, USERID, NULL);
                        listaSus = new ArrayList<>();

                        //Environment.configure("codevsolution-test","test_RqYREPeEdnp7KP16xeQTDRfzAg7cdB7xt");


                        ListResult result = null;
                        try {
                            result = Subscription.list()
                                    .customerId().is(idUser)
                                    .request();

                            for (ListResult.Entry entry : result) {
                                Subscription subscription = entry.subscription();
                                if (!subscription.status().equals(Subscription.Status.CANCELLED)) {
                                    listaSus.add(subscription);
                                }
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(idUser);
                                db.child(SUSESTADO).setValue(subscription.status());

                            }

                            int totProd = 0;
                            int totProdCliente = 0;
                            if (listaSus != null && listaSus.size() > 0) {
                                for (Subscription subscription : listaSus) {
                                    if (!subscription.status().equals(Subscription.Status.CANCELLED)) {
                                        totProd += subscription.planQuantity();
                                    }
                                }
                            } else {
                                if (checkSubscriptionsListener != null) {
                                    checkSubscriptionsListener.onNotSubscriptions();
                                }
                            }
                            totProd *= 1.5;
                            ListaModeloSQL listaProd = CRUDutil.setListaModelo(CAMPOS_PRODUCTO);
                            for (ModeloSQL prod : listaProd.getLista()) {
                                if (prod.getInt(PRODUCTO_FIRE) == 1) {
                                    totProdCliente++;
                                }
                                if (prod.getInt(PRODUCTO_FIREPRO) == 1) {
                                    totProdCliente++;
                                }
                            }
                            if (totProd > totProdCliente) {

                                if (checkSubscriptionsListener != null) {
                                    checkSubscriptionsListener.onCheckSuscriptionsOk(listaSus);
                                }

                            } else {
                                if (checkSubscriptionsListener != null) {
                                    checkSubscriptionsListener.onProductLimit();
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            if (checkSubscriptionsListener != null) {
                                checkSubscriptionsListener.onError(e.getMessage());
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

    public interface CheckSubscriptions {

        void onNotSubscriptions();

        void onProductLimit();

        void onError(String msgError);

        void onCheckSuscriptionsOk(ArrayList<Subscription> listaSuscripciones);
    }

    private void crearCustomer() {

        try {
            //Environment.configure("codevsolution-test","test_RqYREPeEdnp7KP16xeQTDRfzAg7cdB7xt");
            Result result = Customer.create()
                    .id(idUser)
                    .firstName(firebaseFormBase.getNombreBase())
                    .email(firebaseFormBase.getEmailBase())
                    .request();
            customer = result.customer();
            crearSesionPortal(new CheckSesionPortal() {
                @Override
                public void onChekOk(String web) {

                    if (onCreateSesionPortalListener != null) {
                        onCreateSesionPortalListener.onCreateSesionOk(web);
                    }
                }

                @Override
                public void onCheckFailed(String mensajeError) {

                    if (onCreateSesionPortalListener != null) {
                        onCreateSesionPortalListener.onCreateSesionFailed(mensajeError);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param checkSesionPortalListener
     */
    private void crearSesionPortal(CheckSesionPortal checkSesionPortalListener) {

        Result result = null;
        try {
            System.out.println("Crear sesion portal");
            //Environment.configure("codevsolution-test","test_RqYREPeEdnp7KP16xeQTDRfzAg7cdB7xt");
            result = PortalSession.create()
                    .customerId(customer.id())
                    .redirectUrl("https://codevsolution.com")
                    .request();
            portalSession = result.portalSession();

            if (checkSesionPortalListener != null) {
                checkSesionPortalListener.onChekOk(portalSession.accessUrl());
            }

        } catch (Exception e) {
            if (checkSesionPortalListener != null) {
                checkSesionPortalListener.onCheckFailed(msgErrorSesionPortal);
            }
            e.printStackTrace();
        }
    }

    private void recuperarSesionPortal(CheckSesionPortal checkSesionPortalListener) {

        System.out.println("Recuperar sesion portal");
        String idPortalSesion = portalSession.id();
        if (idPortalSesion != null && !idPortalSesion.equals(NULL)) {
            Result result = null;
            try {
                System.out.println("portalSession = " + portalSession.status());
                //Environment.configure("codevsolution-test","test_RqYREPeEdnp7KP16xeQTDRfzAg7cdB7xt");
                result = PortalSession.retrieve(idPortalSesion).request();
                portalSession = result.portalSession();
                System.out.println("portalSession = " + portalSession.status());
                if (checkSesionPortalListener != null) {
                    checkSesionPortalListener.onChekOk(portalSession.accessUrl());
                }
                if (portalSession.status().equals(PortalSession.Status.LOGGED_OUT)) {
                    crearSesionPortal(new CheckSesionPortal() {
                        @Override
                        public void onChekOk(String web) {

                            if (onCreateSesionPortalListener != null) {
                                onCreateSesionPortalListener.onCreateSesionOk(web);
                            }
                        }

                        @Override
                        public void onCheckFailed(String mensajeError) {

                            if (onCreateSesionPortalListener != null) {
                                onCreateSesionPortalListener.onCreateSesionFailed(mensajeError);
                            }
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                crearSesionPortal(new CheckSesionPortal() {
                    @Override
                    public void onChekOk(String web) {

                        if (onCreateSesionPortalListener != null) {
                            onCreateSesionPortalListener.onCreateSesionOk(web);
                        }
                    }

                    @Override
                    public void onCheckFailed(String mensajeError) {

                        if (onCreateSesionPortalListener != null) {
                            onCreateSesionPortalListener.onCreateSesionFailed(mensajeError);
                        }
                    }
                });
            }
        } else {
            crearSesionPortal(new CheckSesionPortal() {
                @Override
                public void onChekOk(String web) {

                    if (onCreateSesionPortalListener != null) {
                        onCreateSesionPortalListener.onCreateSesionOk(web);
                    }
                }

                @Override
                public void onCheckFailed(String mensajeError) {

                    if (onCreateSesionPortalListener != null) {
                        onCreateSesionPortalListener.onCreateSesionFailed(mensajeError);
                    }
                }
            });
        }

    }

    public interface CheckSesionPortal {

        void onChekOk(String web);

        void onCheckFailed(String mensajeError);
    }

    public void getPlanes(final DatabaseReference db) {

        db.child(PLANMENSUAL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                planMensual = dataSnapshot.getValue(String.class);
                db.child(PLANTRIMESTRAL).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        planTrimestral = dataSnapshot.getValue(String.class);
                        db.child(PLANSEMESTRAL).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                planSemestral = dataSnapshot.getValue(String.class);
                                db.child(PLANANUAL).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        planAnual = dataSnapshot.getValue(String.class);
                                        if (onGetPlanesListener != null) {
                                            onGetPlanesListener.onGetOk();
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

    public interface OnGetPlanes {
        void onGetOk();
    }

    public void setEnviroment(final DatabaseReference db) {

        db.child(SITENAME).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                siteName = dataSnapshot.getValue(String.class);
                db.child(APICB).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        apiCB = dataSnapshot.getValue(String.class);
                        Environment.configure(siteName, apiCB);
                        getFirebaseFormBase();

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

    public String pagoSuscripcion(int cant, String planId) {


        Result result = null;
        try {
            result = HostedPage.checkoutNew()
                    .subscriptionPlanId(planId)
                    .subscriptionPlanQuantity(cant)
                    .customerId(idUser)
                    .request();
            HostedPage checkOutPage = result.hostedPage();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(idUser);
            db.child(NUMPRODSUS).setValue(cant);
            return checkOutPage.url();

        } catch (Exception e) {
            e.printStackTrace();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(idUser);
            db.child(NUMPRODSUS).setValue(0);
        }

        return null;
    }

    public double calculoPrecio(int cant, String planId) {

        int canttmp = 0;
        double importe = 0;
        if (cant <= 10) {
            cant = 10;
        }

        if (planId.equals(planMensual)) {

            if (cant > 1000) {
                canttmp = cant - 1000;
                importe += (canttmp * 15);
                cant = 1000;
            }
            if (cant > 500) {
                canttmp = cant - 500;
                importe += (canttmp * 20);
                cant = 500;
            }
            if (cant > 200) {
                canttmp = cant - 200;
                importe += (canttmp * 25);
                cant = 200;
            }
            if (cant > 100) {
                canttmp = cant - 100;
                importe += (canttmp * 30);
                cant = 100;
            }
            if (cant > 50) {
                canttmp = cant - 50;
                importe += (canttmp * 35);
                cant = 50;
            }
            if (cant > 25) {
                canttmp = cant - 25;
                importe += (canttmp * 40);
                cant = 25;
            }
            if (cant > 10) {
                canttmp = cant - 10;
                importe += (canttmp * 45);
            }
            importe += 500;

        } else if (planId.equals(planTrimestral)) {
            if (cant > 1000) {
                canttmp = cant - 1000;
                importe += (canttmp * 10);
                cant = 1000;
            }
            if (cant > 500) {
                canttmp = cant - 500;
                importe += (canttmp * 15);
                cant = 500;
            }
            if (cant > 200) {
                canttmp = cant - 200;
                importe += (canttmp * 20);
                cant = 200;
            }
            if (cant > 100) {
                canttmp = cant - 100;
                importe += (canttmp * 25);
                cant = 100;
            }
            if (cant > 50) {
                canttmp = cant - 50;
                importe += (canttmp * 30);
                cant = 50;
            }
            if (cant > 25) {
                canttmp = cant - 25;
                importe += (canttmp * 35);
                cant = 25;
            }
            if (cant > 10) {
                canttmp = cant - 10;
                importe += (canttmp * 40);
            }
            importe += 450;
            importe *= 3;
        } else if (planId.equals(planSemestral)) {
            if (cant > 1000) {
                canttmp = cant - 1000;
                importe += (canttmp * 8);
                cant = 1000;
            }
            if (cant > 500) {
                canttmp = cant - 500;
                importe += (canttmp * 10);
                cant = 500;
            }
            if (cant > 200) {
                canttmp = cant - 200;
                importe += (canttmp * 15);
                cant = 200;
            }
            if (cant > 100) {
                canttmp = cant - 100;
                importe += (canttmp * 20);
                cant = 100;
            }
            if (cant > 50) {
                canttmp = cant - 50;
                importe += (canttmp * 25);
                cant = 50;
            }
            if (cant > 25) {
                canttmp = cant - 25;
                importe += (canttmp * 30);
                cant = 25;
            }
            if (cant > 10) {
                canttmp = cant - 10;
                importe += (canttmp * 35);
            }
            importe += 400;
            importe *= 6;
        } else if (planId.equals(planAnual)) {
            if (cant > 1000) {
                canttmp = cant - 1000;
                importe += (canttmp * 6);
                cant = 1000;
            }
            if (cant > 500) {
                canttmp = cant - 500;
                importe += (canttmp * 8);
                cant = 500;
            }
            if (cant > 200) {
                canttmp = cant - 200;
                importe += (canttmp * 10);
                cant = 200;
            }
            if (cant > 100) {
                canttmp = cant - 100;
                importe += (canttmp * 15);
                cant = 100;
            }
            if (cant > 50) {
                canttmp = cant - 50;
                importe += (canttmp * 20);
                cant = 50;
            }
            if (cant > 25) {
                canttmp = cant - 25;
                importe += (canttmp * 25);
                cant = 25;
            }
            if (cant > 10) {
                canttmp = cant - 10;
                importe += (canttmp * 30);
            }
            importe += 350;
            importe *= 12;
        }

        System.out.println("importe = " + importe / 100);
        return (importe / 100);
    }
}
