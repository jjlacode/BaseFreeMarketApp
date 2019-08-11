package com.jjlacode.freelanceproject.util.nosql;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.core.widget.NestedScrollView;

import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.util.JavaUtil;

public abstract class FragmentMasterDetailNoSQLFirebaseRatingWeb extends FragmentMasterDetailNoSQLFirebaseRating {

    private View viewWeb;
    protected WebView browser;
    protected NestedScrollView lyweb;
    protected String web;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        viewWeb = inflater.inflate(R.layout.layout_webview, container, false);
        if (viewWeb.getParent() != null) {
            ((ViewGroup) viewWeb.getParent()).removeView(viewWeb); // <- fix
        }
        frdetalleExtras.addView(viewWeb);

        browser = (WebView) view.findViewById(R.id.webBrowser);
        lyweb = view.findViewById(R.id.lywebBrowser);

    }

    @Override
    protected void onSetDatos() {
        super.onSetDatos();

        web = setWeb();

        if (web != null && JavaUtil.isValidURL(web)) {

            visible(lyweb);

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
        } else {
            gone(lyweb);
        }
    }

    protected String setWeb() {
        return null;
    }
}
