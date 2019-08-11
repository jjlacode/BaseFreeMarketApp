package com.jjlacode.base.util.web;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.android.FragmentBase;
import com.jjlacode.freelanceproject.R;

public class FragmentWebView extends FragmentBase {

    String web;
    WebView browser;

    @Override
    protected void setLayout() {

        layout = R.layout.fragment_webview;
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        if (bundle != null) {
            web = bundle.getString(WEB);

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
                browser.loadUrl(web);
            }
        }
    }

    @Override
    protected void setInicio() {

        browser = view.findViewById(R.id.browserweb);

    }
}
