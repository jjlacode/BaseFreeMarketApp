package com.codevsolution.base.util.nosql;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.codevsolution.base.util.JavaUtil;
import com.codevsolution.base.util.adapter.BaseViewHolder;
import com.codevsolution.base.util.adapter.ListaAdaptadorFiltro;
import com.codevsolution.base.util.adapter.RVAdapter;
import com.codevsolution.base.util.adapter.TipoViewHolder;
import com.codevsolution.base.util.android.AndroidUtil;
import com.codevsolution.base.util.android.controls.EditMaterial;
import com.codevsolution.base.util.android.controls.ImagenLayout;
import com.codevsolution.base.util.crud.CRUDutil;
import com.codevsolution.base.util.logica.InteractorBase;
import com.codevsolution.base.util.media.ImagenUtil;
import com.codevsolution.base.util.models.FirebaseFormBase;
import com.codevsolution.base.util.models.ListaModelo;
import com.codevsolution.base.util.models.Marcador;
import com.codevsolution.base.util.models.Modelo;
import com.codevsolution.base.util.models.Productos;
import com.codevsolution.base.util.sqlite.ContratoSystem;
import com.codevsolution.base.util.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.ui.FragmentChat;

import java.util.ArrayList;
import java.util.List;

public abstract class FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb
        extends FragmentMasterDetailNoSQLFirebaseRatingWebMapSus implements ContratoSystem.Tablas, InteractorBase.Constantes {

    protected EditMaterial nombre;
    protected EditMaterial descripcion;
    protected EditMaterial direccion;
    protected EditMaterial email;
    protected EditMaterial telefono;
    protected EditMaterial claves;
    protected EditMaterial etWeb;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        View viewFB = inflater.inflate(R.layout.fragment_formulario_basico, container, false);
        if (viewFB.getParent() != null) {
            ((ViewGroup) viewFB.getParent()).removeView(viewFB); // <- fix
        }
        frdetalleExtrasante.addView(viewFB);


        nombre = (EditMaterial) ctrl(R.id.etnombreformbase);
        descripcion = (EditMaterial) ctrl(R.id.etdescformbase);
        direccion = (EditMaterial) ctrl(R.id.etdireccionformbase);
        email = (EditMaterial) ctrl(R.id.etemailformbase);
        telefono = (EditMaterial) ctrl(R.id.ettelefonoformbase);
        claves = (EditMaterial) ctrl(R.id.etclavesformpbase);
        etWeb = (EditMaterial) ctrl(R.id.etwebformbase);
        imagen = (ImagenLayout) ctrl(R.id.imgformbase);
        imagen.setIcfragmentos(icFragmentos);


        if (tipoForm.equals(NUEVO)) {
            gone(frCabecera);
            nombre.setActivo(true);
            descripcion.setActivo(true);
            direccion.setActivo(true);
            telefono.setActivo(true);
            email.setActivo(true);
            etWeb.setActivo(true);
            zona.setActivo(true);
        } else {
            gone(btnEnviarNoticias);
            gone(chActivo);
        }

    }


    @Override
    protected void setLayoutItem() {

        layoutItem = R.layout.item_list_firebase_formbase_rating_web;


    }

    @Override
    protected void setLayout() {


    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRVFormBaseRatingWeb(view);
    }

    @Override
    protected ListaAdaptadorFiltro setAdaptadorAuto(Context context, int layoutItem, ArrayList lista) {
        return new AdapterFiltroFormBaseRatingWeb(context, layoutItem, lista);
    }

    @Override
    protected void setLista() {

        gone(lyChat);

        if (tipoForm.equals(LISTA) && paisUser != null) {

            lista = new ArrayList<Productos>();

            String lugar = "";

            for (int i = 5; i >= getAlcance(); i--) {

                switch (i) {

                    case 5:
                        lugar = (String) paisUser.get(4);
                        break;
                    case 4:
                        lugar = (String) paisUser.get(3);
                        break;
                    case 3:
                        lugar = (String) paisUser.get(2);
                        break;
                    case 2:
                        lugar = (String) paisUser.get(1);
                        break;
                    case 1:
                        lugar = (String) paisUser.get(0);
                        break;
                    case 0:
                        lugar = MUNDIAL;
                        break;

                }

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                Query querydb = db.child(LUGARES + tipo).child(lugar.toLowerCase());

                querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot prod : dataSnapshot.getChildren()) {

                            if (prod.getValue(Boolean.class)) {

                                DatabaseReference dbproductosprov = FirebaseDatabase.getInstance().getReference().
                                        child(tipo).child(prod.getKey());

                                dbproductosprov.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {


                                        FirebaseFormBase firebaseFormBase = dataSnapshot2.getValue(FirebaseFormBase.class);
                                        lista.add(firebaseFormBase);

                                        setRv();

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

        } else if (tipoForm.equals(NUEVO)) {

            gone(verVoto);
            gone(frCabecera);
            gone(rv);

            lista = new ArrayList<Productos>();

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            Query querydb = db.child(tipo).child(idUser);

            querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    firebaseFormBase = dataSnapshot.getValue(FirebaseFormBase.class);
                    if (nn(firebaseFormBase)) {
                        id = firebaseFormBase.getIdchatBase();
                    }
                    esDetalle = true;
                    selector();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

            if (lista.size() == 0) {
                activityBase.fabNuevo.show();
                activityBase.fabInicio.hide();
            }

        }

        int numero = 100;
        System.out.println("Numero aleatorio hasta " + numero + " = " + Math.round((Math.random() % numero) * numero));


    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();
        activityBase.fabNuevo.hide();
        activityBase.fabInicio.show();
    }


    protected void setDatos() {

        if (tipoForm.equals(NUEVO)) {

            visible(btnEnviarNoticias);
            visible(btndelete);
            visible(btnsave);
            visible(chActivo);
            visible(claves);
            visible(etWeb);

            gone(verVoto);
            zona.setActivo(false);
            visible(opcionesZona);
            gone(lyMap);
            gone(suscripcion);
            visible(suscritos);
            gone(lyChat);
            gone(btnback);

        } else {
            gone(opcionesZona);
            comprobarSuscripcion();
            visible(suscripcion);
            gone(suscritos);
            gone(btndelete);
            gone(btnsave);
            visible(lyChat);

            if (idChat == null) {
                ListaModelo listaChats = CRUDutil.setListaModelo(CAMPOS_CHAT);
                for (Modelo chat : listaChats.getLista()) {
                    if (chat.getString(CHAT_USUARIO).equals(id)) {
                        idChat = chat.getString(CHAT_ID_CHAT);
                    }
                }
            }

            listaMsgChat = CRUDutil.setListaModeloDetalle(CAMPOS_DETCHAT, idChat, TABLA_CHAT, null, DETCHAT_FECHA + " DESC");

            RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat_base);
            rvMsgChat.setAdapter(adaptadorDetChat);
            visible(rvMsgChat);
            noticias.setChecked(true);

        }

        if (firebaseFormBase != null) {

            nombre.setText(firebaseFormBase.getNombreBase());
            direccion.setText(firebaseFormBase.getDireccionBase());
            telefono.setText(firebaseFormBase.getTelefonoBase());
            email.setText(firebaseFormBase.getEmailBase());
            descripcion.setText(firebaseFormBase.getDescripcionBase());
            claves.setText(firebaseFormBase.getClavesBase());
            web = firebaseFormBase.getWebBase();
            etWeb.setText(web);
            if (firebaseFormBase.isMulti()) {
                visible(multitxt);
            }
            chActivo.setChecked(firebaseFormBase.isActivo());

            activityBase.toolbar.setTitle(firebaseFormBase.getNombreBase());
            accionesImagen();

        }

    }


    @Override
    protected void acciones() {
        super.acciones();

        if (tipoForm.equals(NUEVO)) {


            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (idUser != null) {

                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        db.child(tipo).child(idUser).removeValue();
                        db.child(PERFILES).child(idUser).child(tipo).removeValue();
                        db.child(RATING).child(tipo).child(idUser).removeValue();
                        db.child(INDICEMARC + tipo).child(idUser).removeValue();
                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    deleteMarcador(child.getValue(Marcador.class), mapa);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            });


        }

    }

    protected void guardar() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        firebaseFormBase = new FirebaseFormBase();
        firebaseFormBase.setTipoBase(tipo);
        firebaseFormBase.setNombreBase(nombre.getTexto());
        firebaseFormBase.setActivo(chActivo.isChecked());
        firebaseFormBase.setDescripcionBase(descripcion.getTexto());
        firebaseFormBase.setTelefonoBase(telefono.getTexto());
        firebaseFormBase.setEmailBase(email.getTexto());
        firebaseFormBase.setDireccionBase(direccion.getTexto());
        firebaseFormBase.setClavesBase(claves.getTexto());
        firebaseFormBase.setWebBase(etWeb.getTexto());
        if (tipo.equals(PERFILMULTI)) {
            firebaseFormBase.setMulti(true);
        } else {
            firebaseFormBase.setMulti(false);
        }
        firebaseFormBase.setIdchatBase(idUser);

        db.child(tipo).child(idUser).setValue(firebaseFormBase, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

            }
        });
        db.child(PERFILES).child(idUser).child(tipo).setValue(true);

    }


    @Override
    protected String setTipoRating() {
        return firebaseFormBase.getTipoBase();
    }

    @Override
    protected String setIdRating() {
        return firebaseFormBase.getIdchatBase();
    }


    @Override
    public void setOnClickRV(Object object) {

        firebaseFormBase = (FirebaseFormBase) object;
        id = firebaseFormBase.getIdchatBase();
        esDetalle = true;
        selector();

    }

    protected void setMaestroDetalleTabletPort() {
        maestroDetalleSeparados = true;
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

                    perfilUser = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
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
                        values.put(CHAT_TIPORETORNO, perfilUser);
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
