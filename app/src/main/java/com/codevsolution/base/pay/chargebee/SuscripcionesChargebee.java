package com.codevsolution.base.pay.chargebee;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.FirebaseFormBase;
import com.chargebee.*;
import com.chargebee.models.*;
import com.chargebee.models.enums.*;
import com.chargebee.exceptions.*;
import com.codevsolution.freemarketsapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SuscripcionesChargebee extends FragmentBase {

    private String planMensual = "fma100mes";
    private String planTrimestral = "fma100tri";
    private String planSemestral = "fma100sem";
    private String planAnual = "fma100anu";
    private String perfil;
    private FirebaseFormBase firebaseFormBase;
    private Customer customer;
    private PortalSession portalSession;
    private View viewWeb;
    protected WebView browser;
    protected ProgressBar progressBarWeb;
    protected NestedScrollView lyweb;
    protected String web;
    private EditMaterial cantProd;
    private EditMaterial precio;
    private RadioGroup radioGroupSus;
    private RadioButton radioButtonMes;
    private RadioButton radioButtonTri;
    private RadioButton radioButtonSem;
    private RadioButton radioButtonAnu;
    private Button checkOut;
    private LinearLayout lySus;
    private String planId;
    private String siteName;
    private String apiCB;
    private ArrayList<Subscription> listaSus;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        viewWeb = inflater.inflate(R.layout.layout_webview, container, false);
        if (viewWeb.getParent() != null) {
            ((ViewGroup) viewWeb.getParent()).removeView(viewWeb); // <- fix
        }
        frWeb.addView(viewWeb);

        browser = (WebView) view.findViewById(R.id.webBrowser);
        progressBarWeb = view.findViewById(R.id.progressBarWeb);
        lyweb = view.findViewById(R.id.lywebBrowser);

        radioGroupSus = (RadioGroup) ctrl(R.id.radio_group_sus);
        radioButtonMes = (RadioButton) ctrl(R.id.radioButtonsus);
        radioButtonTri = (RadioButton) ctrl(R.id.radioButtonsus1);
        radioButtonSem = (RadioButton) ctrl(R.id.radioButtonsus2);
        radioButtonAnu = (RadioButton) ctrl(R.id.radioButtonsus3);
        cantProd = (EditMaterial) ctrl(R.id.etcant_suscripcion);
        precio = (EditMaterial) ctrl(R.id.etimporte_suscripcion);
        checkOut = (Button) ctrl(R.id.btn_checkout);
        lySus = (LinearLayout) ctrl(R.id.lysus);


        acciones();
    }

    @Override
    protected void setLayout() {

        layout = R.layout.suscripciones;
    }

    @Override
    protected void setInicio() {

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(CONFIG);
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

        db.child(PLANMENSUAL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                planMensual = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        db.child(PLANTRIMESTRAL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                planTrimestral = dataSnapshot.getValue(String.class);
                planId = planTrimestral;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        db.child(PLANSEMESTRAL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                planSemestral = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        db.child(PLANANUAL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                planAnual = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                        Toast.makeText(contexto, getString(R.string.debe_tener_perfil) + perfil, Toast.LENGTH_SHORT).show();
                    } else if (nn(firebaseFormBase)) {

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
            comprobarSuscripciones();

        } catch (Exception e) {
            e.printStackTrace();
            crearCustomer();
        }

    }

    private void crearsuscripcion() {

        activityBase.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                gone(lyweb);
                visible(lySus);

            }
        });
    }

    @Override
    protected void acciones() {
        super.acciones();

        radioGroupSus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (radioButtonMes.isChecked() && planMensual != null) {
                    planId = planMensual;
                } else if (radioButtonTri.isChecked() && planTrimestral != null) {
                    planId = planTrimestral;
                } else if (radioButtonSem.isChecked() && planSemestral != null) {
                    planId = planSemestral;
                } else if (radioButtonAnu.isChecked() && planAnual != null) {
                    planId = planAnual;
                }

                calculoPrecio();

            }
        });

        cantProd.setAlCambiarListener(new EditMaterial.AlCambiarListener() {
            @Override
            public void antesCambio(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void cambiando(CharSequence s, int start, int before, int count) {

                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void despuesCambio(Editable s) {

                final Editable temp = s;
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!temp.toString().equals("")) {
                            activityBase.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    calculoPrecio();
                                }
                            });

                        }
                    }
                }, 1000);
            }
        });

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        pagoSuscripcion();
                    }
                });
                th.start();
            }
        });
    }

    private void pagoSuscripcion() {

        int importe = JavaUtil.comprobarInteger(precio.getTexto()) * 100;
        int cant = JavaUtil.comprobarInteger(cantProd.getTexto());

        //Environment.configure("codevsolution-test","test_RqYREPeEdnp7KP16xeQTDRfzAg7cdB7xt");
        Result result = null;
        try {
            result = HostedPage.checkoutNew()
                    .subscriptionPlanId(planId)
                    .subscriptionPlanQuantity(cant)
                    .customerId(idUser)
                    .request();
            HostedPage checkOutPage = result.hostedPage();
            web = checkOutPage.url();
            lanzarWeb();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(idUser);
            db.child(NUMPRODSUS).setValue(cant);

        } catch (Exception e) {
            e.printStackTrace();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(idUser);
            db.child(NUMPRODSUS).setValue(0);
        }


    }

    /*
    private void calculoPrecio() {

        long cant = JavaUtil.comprobarLong(cantProd.getTexto());
        double importe = cant/10;
        if (importe<=5){importe = 5;}
        else{
            importe*=0.9;
        }

        if (planId.equals(PLANTRIMESTRAL)){
            importe*=3;
            importe*=0.9;
        }else if (planId.equals(PLANSEMESTRAL)){
            importe*=6;
            importe*=0.7;
        }else if (planId.equals(PLANANUAL)){
            importe*=12;
            importe*=0.5;
        }

        System.out.println("importe = " + importe);
        precio.setText(String.valueOf(Math.round(importe)));
    }

     */

    private void calculoPrecio() {

        int cant = JavaUtil.comprobarInteger(cantProd.getTexto());
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
        precio.setText(String.valueOf(importe / 100));
    }

    public void comprobarSuscripciones() {

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

                        Thread th = new Thread(new Runnable() {
                            @Override
                            public void run() {
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

                                    if (listaSus.size() > 0) {
                                        crearSesionPortal();
                                    } else {
                                        crearsuscripcion();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        th.start();

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

    private void lanzarWeb() {

        if (web != null && JavaUtil.isValidURL(web)) {

            activityBase.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    visible(lyweb);
                    gone(lySus);
                    // Cargamos la web


                    browser.getSettings().setJavaScriptEnabled(true);
                    browser.getSettings().setBuiltInZoomControls(true);
                    browser.getSettings().setDisplayZoomControls(false);
                    browser.getSettings().setDomStorageEnabled(true);

                    browser.setWebViewClient(new WebViewClient() {

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });


                    browser.loadUrl(web);
                    browser.setWebChromeClient(new WebChromeClient() {
                        @Override
                        public void onProgressChanged(WebView view, int progress) {
                            progressBarWeb.setProgress(0);
                            progressBarWeb.setVisibility(View.VISIBLE);
                            progressBarWeb.setProgress(progress * 1000);

                            progressBarWeb.incrementProgressBy(progress);

                            if (progress == 100) {
                                progressBarWeb.setVisibility(View.GONE);

                            }
                        }
                    });

                }
            });


        }
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
            crearSesionPortal();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void crearSesionPortal() {

        Result result = null;
        try {
            System.out.println("Crear sesion portal");
            //Environment.configure("codevsolution-test","test_RqYREPeEdnp7KP16xeQTDRfzAg7cdB7xt");
            result = PortalSession.create()
                    .customerId(customer.id())
                    .redirectUrl("https://codevsolution.com")
                    .request();
            portalSession = result.portalSession();
            web = portalSession.accessUrl();
            lanzarWeb();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recuperarSesionPortal() {

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
                web = portalSession.accessUrl();
                lanzarWeb();
                if (portalSession.status().equals(PortalSession.Status.LOGGED_OUT)) {
                    crearSesionPortal();
                }

            } catch (Exception e) {
                e.printStackTrace();
                crearSesionPortal();
            }
        } else {
            crearSesionPortal();
        }

    }


}
