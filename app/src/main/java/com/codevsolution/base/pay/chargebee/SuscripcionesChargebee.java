package com.codevsolution.base.pay.chargebee;

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

import androidx.core.widget.NestedScrollView;

import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.InteractorSuscriptions;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class SuscripcionesChargebee extends FragmentBase {

    private String planMensual;
    private String planTrimestral;
    private String planSemestral;
    private String planAnual;
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
    private InteractorSuscriptions interactor;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        viewWeb = inflater.inflate(R.layout.layout_webview, container, false);
        if (viewWeb.getParent() != null) {
            ((ViewGroup) viewWeb.getParent()).removeView(viewWeb); // <- fix
        }
        frWeb.addView(viewWeb);

        browser = view.findViewById(R.id.webBrowser);
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

        interactor = new InteractorSuscriptions(contexto, new InteractorSuscriptions.OnCreateSesionPortal() {
            @Override
            public void onCreateSesionOk(String web) {
                lanzarWeb(web);
            }

            @Override
            public void onCreateSesionFailed(String mensajeError) {
                Toast.makeText(contexto, mensajeError, Toast.LENGTH_SHORT).show();
            }
        }, new InteractorSuscriptions.OnNotSubscriptions() {
            @Override
            public void onNotSubscriptions() {
                crearsuscripcion();
            }
        }, new InteractorSuscriptions.OnGetPlanes() {
            @Override
            public void onGetOk() {
                planMensual = interactor.getPlanMensual();
                planTrimestral = interactor.getPlanTrimestral();
                planSemestral = interactor.getPlanSemestral();
                planAnual = interactor.getPlanAnual();
                planId = planTrimestral;
            }
        });
        acciones();
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected void setLayout() {

        layout = R.layout.suscripciones;
    }

    @Override
    protected void setInicio() {

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

                if (planId != null) {
                    precio.setText(String.valueOf(interactor.calculoPrecio(JavaUtil.comprobarInteger(cantProd.getTexto()), planId)));
                }

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
                                    if (planId != null) {
                                        precio.setText(String.valueOf(interactor.calculoPrecio(JavaUtil.comprobarInteger(cantProd.getTexto()), planId)));
                                    }
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
                        lanzarWeb(interactor.pagoSuscripcion(JavaUtil.comprobarInteger(cantProd.getTexto()), planId));
                    }
                });
                th.start();
            }
        });
    }

    private void lanzarWeb(final String web) {

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


}
