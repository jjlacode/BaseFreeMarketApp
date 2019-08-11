package com.jjlacode.freelanceproject.ui;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.adapter.BaseViewHolder;
import com.jjlacode.base.util.adapter.ListaAdaptadorFiltro;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.crud.ListaModelo;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.nosql.FragmentMasterDetailNoSQLFirebaseRatingWeb;
import com.jjlacode.base.util.time.TimeDateUtil;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.model.Freelance;

import java.util.ArrayList;
import java.util.List;

import static com.jjlacode.freelanceproject.CommonPry.Constantes.FREELANCE;
import static com.jjlacode.freelanceproject.sqlite.ContratoPry.Tablas.CAMPOS_CHAT;
import static com.jjlacode.freelanceproject.sqlite.ContratoPry.Tablas.CHAT_CREATE;
import static com.jjlacode.freelanceproject.sqlite.ContratoPry.Tablas.CHAT_ID_CHAT;
import static com.jjlacode.freelanceproject.sqlite.ContratoPry.Tablas.CHAT_NOMBRE;
import static com.jjlacode.freelanceproject.sqlite.ContratoPry.Tablas.CHAT_TIMESTAMP;
import static com.jjlacode.freelanceproject.sqlite.ContratoPry.Tablas.CHAT_TIPO;
import static com.jjlacode.freelanceproject.sqlite.ContratoPry.Tablas.CHAT_USUARIO;
import static com.jjlacode.freelanceproject.sqlite.ContratoPry.Tablas.TABLA_CHAT;

public class Freelancers extends FragmentMasterDetailNoSQLFirebaseRatingWeb {

    private EditMaterial nomProv;
    private EditMaterial descripProv;
    private EditMaterial direccion;
    private EditMaterial email;
    private EditMaterial telefono;
    private DatabaseReference db;
    private Query query;
    private Freelance freelance;
    private String idFreelance;
    private ProgressDialog progressDialog;


    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected void setDatos() {

        nomProv.setText(freelance.getNombref());
        descripProv.setText(freelance.getDescripcionf());
        direccion.setText(freelance.getDireccionf());
        email.setText(freelance.getEmailf());
        telefono.setText(freelance.getTelefonof());
        web = freelance.getWebf();
        idFreelance = freelance.getIdchatf();


        setImagenFireStore(contexto, freelance.getIdchatf(), imagen);

    }

    @Override
    protected String setWeb() {
        return web;
    }

    @Override
    protected String setIdRating() {
        return idFreelance;
    }

    @Override
    protected String setTipoRating() {
        return FREELANCE;
    }

    @Override
    protected ListaAdaptadorFiltro setAdaptadorAuto(Context context, int layoutItem, ArrayList lista) {
        return new AdapterFiltro(context, layoutItem, lista);
    }

    @Override
    protected void setLista() {

        progressDialog = ProgressDialog.show(contexto, "Cargando lista de freelance", "Por favor espere...", false, false);

        lista = new ArrayList<Freelance>();

        db = FirebaseDatabase.getInstance().getReference();

        query = db.child(FREELANCE);

        ValueEventListener eventListenerProd = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Freelance freelance = ds.getValue(Freelance.class);
                    lista.add(freelance);
                }

                System.out.println("lista freelance: " + lista.size());
                setRv();

                progressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };

        query.addValueEventListener(eventListenerProd);
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        if (bundle != null) {

            origen = getStringBundle(ORIGEN, "");

        }

    }

    @Override
    public void setOnClickRV(Object object) {

        freelance = (Freelance) object;
        esDetalle = true;
        selector();

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_detalle_freelance;

    }

    @Override
    protected void setLayoutItem() {
        layoutItem = R.layout.item_list_freelance;
        System.out.println("layoutItem = " + layoutItem);
    }

    @Override
    protected void setInicio() {

        imagen = (ImageView) ctrl(R.id.imgdetfreelance);
        nomProv = (EditMaterial) ctrl(R.id.etnombredetfreelance);
        descripProv = (EditMaterial) ctrl(R.id.etdescdetfreelance);
        direccion = (EditMaterial) ctrl(R.id.etdirecciondetfreelance);
        email = (EditMaterial) ctrl(R.id.etemaildetfreelance);
        telefono = (EditMaterial) ctrl(R.id.ettelefonodetfreelance);

    }

    @Override
    protected void setMaestroDetalleTabletPort() {
        maestroDetalleSeparados = true;
    }

    @Override
    protected void setAcciones() {


    }

    private class AdapterFiltro extends ListaAdaptadorFiltro {


        public AdapterFiltro(Context contexto, int R_layout_IdView, ArrayList entradas) {
            super(contexto, R_layout_IdView, entradas);

        }

        @Override
        public void onEntrada(Object entrada, View view) {

            ImageView imagen = view.findViewById(R.id.imglfreelance);
            TextView nombre = view.findViewById(R.id.tvnomlfreelance);
            TextView descripcion = view.findViewById(R.id.tvdesclfreelance);
            TextView direccion = view.findViewById(R.id.tvdirlfreelance);
            TextView telefono = view.findViewById(R.id.tvtelefonolfreelance);
            TextView email = view.findViewById(R.id.tvemaillfreelance);

            nombre.setText(((Freelance) entrada).getNombref());
            descripcion.setText(((Freelance) entrada).getDescripcionf());
            direccion.setText(((Freelance) entrada).getDireccionf());
            telefono.setText(((Freelance) entrada).getTelefonof());
            email.setText(((Freelance) entrada).getEmailf());

            if (nn(((Freelance) entrada).getIdchatf()) && !((Freelance) entrada).getIdchatf().equals("")) {
                setImagenFireStoreCircle(contexto, ((Freelance) entrada).getIdchatf(), imagen);
            }

        }

        @Override
        public List onFilter(ArrayList entradas, CharSequence constraint) {

            List suggestion = new ArrayList();

            System.out.println("constraint = " + constraint.toString());

            for (Object entrada : entradas) {

                if (((Freelance) entrada).getNombref() != null && ((Freelance) entrada).getNombref().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((Freelance) entrada).getDireccionf() != null && ((Freelance) entrada).getDireccionf().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((Freelance) entrada).getDescripcionf() != null && ((Freelance) entrada).getDescripcionf().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((Freelance) entrada).getClavesf() != null && ((Freelance) entrada).getClavesf().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                }
            }
            return suggestion;
        }
    }

    private class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

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

        public ViewHolderRV(View view) {
            super(view);

            imagen = view.findViewById(R.id.imglfreelance);
            nombre = view.findViewById(R.id.tvnomlfreelance);
            descripcion = view.findViewById(R.id.tvdesclfreelance);
            direccion = view.findViewById(R.id.tvdirlfreelance);
            telefono = view.findViewById(R.id.tvtelefonolfreelance);
            email = view.findViewById(R.id.tvemaillfreelance);
            webView = view.findViewById(R.id.browserweblfreelance);
            btnchat = view.findViewById(R.id.btnchatlfreelance);
            ratingBarCard = view.findViewById(R.id.ratingBarCardFreelance);
            ratingBarUserCard = view.findViewById(R.id.ratingBarUserCardFreelance);

            lylweb = view.findViewById(R.id.lylwebfreelance);

        }

        @Override
        public void bind(ArrayList<?> lista, int position) {

            final Freelance freelance = (Freelance) lista.get(position);

            nombre.setText(freelance.getNombref());
            descripcion.setText(freelance.getDescripcionf());
            direccion.setText(freelance.getDireccionf());
            telefono.setText(freelance.getTelefonof());
            email.setText(freelance.getEmailf());
            web = freelance.getWebf();

            if (nn(freelance.getIdchatf()) && !freelance.getIdchatf().equals("")) {
                setImagenFireStoreCircle(contexto, freelance.getIdchatf(), imagen);
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
                        if (chat.getString(CHAT_USUARIO).equals(freelance.getIdchatf())) {
                            idChat = chat.getString(CHAT_ID_CHAT);
                        }
                    }
                    if (idChat == null) {
                        ContentValues values = new ContentValues();
                        values.put(CHAT_NOMBRE, freelance.getNombref());
                        values.put(CHAT_USUARIO, freelance.getIdchatf());
                        values.put(CHAT_TIPO, FREELANCE);
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
            recuperarVotos(ratingBarCard, FREELANCE, freelance.getIdchatf());
            ratingBarCard.setIsIndicator(true);

            ratingBarUserCard.setRating(0);
            recuperarVotoUsuario(ratingBarUserCard, contexto, FREELANCE, freelance.getIdchatf());
            ratingBarUserCard.setIsIndicator(true);


            super.bind(lista, position);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }


}
