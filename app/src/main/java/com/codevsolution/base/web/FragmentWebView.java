package com.codevsolution.base.web;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.codevsolution.base.R;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.javautil.JavaUtil;

public class FragmentWebView extends FragmentBase {

    String web;
    WebView browser;
    ProgressBar progressBar;

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected void setLayout() {

        layout = R.layout.fragment_webview;
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        if (bundle != null) {
            web = bundle.getString(WEB);
            System.out.println("Web = " + web);


            if (web != null && JavaUtil.isValidURL(web)) {

                browser.getSettings().setJavaScriptEnabled(true);
                browser.getSettings().setBuiltInZoomControls(true);
                browser.getSettings().setDisplayZoomControls(false);

                browser.setWebViewClient(new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                // Cargamos la web


                Thread th = new Thread() {
                    public void run() {
                        activityBase.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                browser.loadUrl(web);
                                browser.setWebChromeClient(new WebChromeClient() {
                                    @Override
                                    public void onProgressChanged(WebView view, int progress) {
                                        progressBar.setProgress(0);
                                        progressBar.setVisibility(View.VISIBLE);
                                        progressBar.setProgress(progress * 1000);

                                        progressBar.incrementProgressBy(progress);

                                        if (progress == 100) {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });

                            }
                        });
                    }
                };
                th.start();


            }
        }
    }

    @Override
    protected void setInicio() {

        browser = view.findViewById(R.id.browserweb);
        progressBar = view.findViewById(R.id.progressBarWeb);

    }
}
