package com.jjlacode.base.util.nosql;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.Models.FirebaseFormBase;
import com.jjlacode.base.util.adapter.BaseViewHolder;
import com.jjlacode.base.util.adapter.ListaAdaptadorFiltro;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.crud.ListaModelo;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.media.ImagenUtil;
import com.jjlacode.base.util.sqlite.ContratoPry;
import com.jjlacode.base.util.time.TimeDateUtil;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.um.base.ui.FragmentChat;

import java.util.ArrayList;
import java.util.List;

public class FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb
        extends FragmentMasterDetailNoSQLFormBaseFirebaseRating implements ContratoPry.Tablas {

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
        frdetalleExtraspost.addView(viewWeb);

        browser = (WebView) view.findViewById(R.id.webBrowser);
        lyweb = view.findViewById(R.id.lywebBrowser);


    }

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

    }

    @Override
    protected void setDatos() {
        super.setDatos();

        web = firebaseFormBase.getWebBase();

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

    @Override
    protected void setLayoutItem() {

        layoutItem = R.layout.item_list_firebase_formbase_rating_web;

    }


    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRVFormBaseRatingWeb(view);
    }

    @Override
    protected ListaAdaptadorFiltro setAdaptadorAuto(Context context, int layoutItem, ArrayList lista) {
        return new AdapterFiltroFormBaseRatingWeb(context, layoutItem, lista);
    }

    private class AdapterFiltroFormBaseRatingWeb extends ListaAdaptadorFiltro {


        public AdapterFiltroFormBaseRatingWeb(Context contexto, int R_layout_IdView, ArrayList entradas) {
            super(contexto, R_layout_IdView, entradas);

        }

        @Override
        public void onEntrada(Object entrada, View view) {

            ImageView imagen = view.findViewById(R.id.imglformbaseratingweb);
            TextView nombre = view.findViewById(R.id.tvnomlformbaseratingweb);
            TextView descripcion = view.findViewById(R.id.tvdesclformbaseratingweb);
            TextView direccion = view.findViewById(R.id.tvdirlformbaseratingweb);
            TextView telefono = view.findViewById(R.id.tvtelefonolformbaseratingweb);
            TextView email = view.findViewById(R.id.tvemaillformbaseratingweb);

            nombre.setText(((FirebaseFormBase) entrada).getNombreBase());
            descripcion.setText(((FirebaseFormBase) entrada).getDescripcionBase());
            direccion.setText(((FirebaseFormBase) entrada).getDireccionBase());
            telefono.setText(((FirebaseFormBase) entrada).getTelefonoBase());
            email.setText(((FirebaseFormBase) entrada).getEmailBase());

            if (nn(((FirebaseFormBase) entrada).getIdchatBase()) && !((FirebaseFormBase) entrada).getIdchatBase().equals("")) {
                ImagenUtil.setImageFireStoreCircle(((FirebaseFormBase) entrada).getIdchatBase() +
                        ((FirebaseFormBase) entrada).getTipoBase(), imagen);
            }

        }

        @Override
        public List onFilter(ArrayList entradas, CharSequence constraint) {

            List suggestion = new ArrayList();

            System.out.println("constraint = " + constraint.toString());

            for (Object entrada : entradas) {

                if (((FirebaseFormBase) entrada).getNombreBase() != null && ((FirebaseFormBase) entrada).getNombreBase().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((FirebaseFormBase) entrada).getDireccionBase() != null && ((FirebaseFormBase) entrada).getDireccionBase().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((FirebaseFormBase) entrada).getDescripcionBase() != null && ((FirebaseFormBase) entrada).getDescripcionBase().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((FirebaseFormBase) entrada).getClavesBase() != null && ((FirebaseFormBase) entrada).getClavesBase().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                }
            }
            return suggestion;
        }
    }

    private class ViewHolderRVFormBaseRatingWeb extends BaseViewHolder implements TipoViewHolder {

        ImageView imagen;
        TextView nombre;
        TextView descripcion;
        TextView direccion;
        TextView telefono;
        TextView email;
        ImageButton btnchat;
        WebView webView;
        RatingBar ratingBarCard;
        RatingBar ratingBarUserCard;
        NestedScrollView lylweb;
        String web;

        public ViewHolderRVFormBaseRatingWeb(View view) {
            super(view);

            imagen = view.findViewById(R.id.imglformbaseratingweb);
            nombre = view.findViewById(R.id.tvnomlformbaseratingweb);
            descripcion = view.findViewById(R.id.tvdesclformbaseratingweb);
            direccion = view.findViewById(R.id.tvdirlformbaseratingweb);
            telefono = view.findViewById(R.id.tvtelefonolformbaseratingweb);
            email = view.findViewById(R.id.tvemaillformbaseratingweb);
            webView = view.findViewById(R.id.browserweblformbaseratingweb);
            btnchat = view.findViewById(R.id.btnchatlformbaseratingweb);
            ratingBarCard = view.findViewById(R.id.ratingBarCardformbaseratingweb);
            ratingBarUserCard = view.findViewById(R.id.ratingBarUserCardformbaseratingweb);

            lylweb = view.findViewById(R.id.lylwebformbaseratingweb);

        }

        @Override
        public void bind(ArrayList<?> lista, int position) {

            final FirebaseFormBase firebaseFormBase = (FirebaseFormBase) lista.get(position);

            nombre.setText(firebaseFormBase.getNombreBase());
            descripcion.setText(firebaseFormBase.getDescripcionBase());
            direccion.setText(firebaseFormBase.getDireccionBase());
            telefono.setText(firebaseFormBase.getTelefonoBase());
            email.setText(firebaseFormBase.getEmailBase());
            web = firebaseFormBase.getWebBase();

            if (nn(firebaseFormBase.getIdchatBase()) && !firebaseFormBase.getIdchatBase().equals("")) {
                ImagenUtil.setImageFireStoreCircle(firebaseFormBase.getIdchatBase() +
                        firebaseFormBase.getTipoBase(), imagen);
            }

            if (web != null && JavaUtil.isValidURL(web)) {

                visible(lylweb);

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setDisplayZoomControls(false);

                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                // Cargamos la web
                webView.loadUrl(web);
            } else {
                gone(lylweb);
            }

            btnchat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ListaModelo listaChats = new ListaModelo(CAMPOS_CHAT);
                    String idChat = null;
                    for (Modelo chat : listaChats.getLista()) {
                        if (chat.getString(CHAT_USUARIO).equals(firebaseFormBase.getIdchatBase())) {
                            idChat = chat.getString(CHAT_ID_CHAT);
                        }
                    }
                    if (idChat == null) {
                        ContentValues values = new ContentValues();
                        values.put(CHAT_NOMBRE, firebaseFormBase.getNombreBase());
                        values.put(CHAT_USUARIO, firebaseFormBase.getIdchatBase());
                        values.put(CHAT_TIPO, firebaseFormBase.getTipoBase());
                        values.put(CHAT_TIPORETORNO, CLIENTEWEB);
                        values.put(CHAT_CREATE, TimeDateUtil.ahora());
                        values.put(CHAT_TIMESTAMP, TimeDateUtil.ahora());

                        idChat = CRUDutil.crearRegistroId(TABLA_CHAT, values);
                    }
                    if (idChat != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(CAMPO_ID, idChat);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentChat());
                    }
                }
            });

            ratingBarCard.setRating(0);
            recuperarVotos(ratingBarCard, firebaseFormBase.getTipoBase(), firebaseFormBase.getIdchatBase());
            ratingBarCard.setIsIndicator(true);

            ratingBarUserCard.setRating(0);
            recuperarVotoUsuario(ratingBarUserCard, contexto, firebaseFormBase.getTipoBase(), firebaseFormBase.getIdchatBase());
            ratingBarUserCard.setIsIndicator(true);


            super.bind(lista, position);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVFormBaseRatingWeb(view);
        }
    }
}
