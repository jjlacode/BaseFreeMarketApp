package com.codevsolution.base.nosql;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.chat.FragmentChatBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltro;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.models.FirebaseFormBase;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.Marcador;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.sqlite.ContratoSystem;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;

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
        imagen.setVisibleBtn();


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
                Query querydb = db.child(LUGARES).child(tipo).child(lugar.toLowerCase());

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
            id = idUser;

            //lista = new ArrayList<Productos>();

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            Query querydb = db.child(tipo).child(id);

            querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    firebaseFormBase = dataSnapshot.getValue(FirebaseFormBase.class);
                    if (firebaseFormBase == null) {
                        nuevo = true;
                    }
                    esDetalle = true;
                    selector();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    nuevo = true;
                }

            });

            if (lista.size() == 0) {
                activityBase.fabNuevo.show();
                activityBase.fabInicio.hide();
            }

        }

    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();
        activityBase.fabNuevo.hide();
        activityBase.fabInicio.show();
    }


    protected void setDatos() {

        if (tipoForm.equals(NUEVO)) {


            activityBase.fabNuevo.hide();
            activityBase.fabInicio.show();
            visible(btnEnviarNoticias);
            gone(btndelete);
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
            if (id == null) {
                id = idUser;
            }

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
                    if (chat.getString(CHAT_USUARIO).equals(id) && chat.getString(CHAT_TIPO).equals(tipo)) {
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

            chActivo.setChecked(firebaseFormBase.isActivo());

            accionesImagen();
        } else {
            guardar();
        }

    }

    @Override
    protected void onGuardarImagen(String path) {
        super.onGuardarImagen(path);

        ImagenUtil.setImageFireStoreCircle(idUser, activityBase.imagenPerfil);
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
                        db.child(PERFIL).child(tipo).child(idUser).removeValue();
                        db.child(RATING).child(tipo).child(idUser).removeValue();
                        ImagenUtil.deleteImagefirestore(idUser);

                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (final DataSnapshot child : dataSnapshot.getChildren()) {

                                    final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                    Query querydb = db.child(MARC + tipo).child(child.getKey());
                                    querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            deleteMarcador(dataSnapshot.getValue(Marcador.class), mapa);
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
            });


        }

    }

    protected void guardar() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        firebaseFormBase = new FirebaseFormBase();
        firebaseFormBase.setTipoBase(tipo);
        if (nombre.getTexto().isEmpty()) {
            firebaseFormBase.setNombreBase(ANON);
        } else {
            firebaseFormBase.setNombreBase(nombre.getTexto());
        }
        firebaseFormBase.setActivo(chActivo.isChecked());
        firebaseFormBase.setDescripcionBase(descripcion.getTexto());
        firebaseFormBase.setTelefonoBase(telefono.getTexto());
        firebaseFormBase.setEmailBase(email.getTexto());
        firebaseFormBase.setDireccionBase(direccion.getTexto());
        firebaseFormBase.setClavesBase(claves.getTexto());
        firebaseFormBase.setWebBase(etWeb.getTexto());
        firebaseFormBase.setIdchatBase(idUser);

        db.child(tipo).child(idUser).setValue(firebaseFormBase, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                nuevo = false;
            }
        });
        db.child(PERFIL).child(tipo).child(idUser).setValue(true);

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
                        values.put(CHAT_CREATE, TimeDateUtil.ahora());
                        values.put(CHAT_TIMESTAMP, TimeDateUtil.ahora());

                        idChat = CRUDutil.crearRegistroId(TABLA_CHAT, values);
                    }
                    if (idChat != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(CAMPO_ID, idChat);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentChatBase());
                    }
                }
            });

            ratingBarCard.setRating(0);
            recuperarVotos(ratingBarCard, firebaseFormBase.getIdchatBase());
            ratingBarCard.setIsIndicator(true);

            ratingBarUserCard.setRating(0);
            recuperarVotoUsuario(ratingBarUserCard, contexto, firebaseFormBase.getIdchatBase());
            ratingBarUserCard.setIsIndicator(true);


            super.bind(lista, position);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVFormBaseRatingWeb(view);
        }
    }

    public static class ViewHolderRVMsgChat extends BaseViewHolder implements TipoViewHolder {

        TextView mensaje, fecha;
        CardView card;
        WebView webView;
        ProgressBar progressBarWebCard;
        NestedScrollView lylweb;

        public ViewHolderRVMsgChat(View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.tvmsgchat_base);
            fecha = itemView.findViewById(R.id.tvmsgchatfecha_base);
            card = itemView.findViewById(R.id.cardmsgchat_base);
            webView = itemView.findViewById(R.id.browserwebl_chat_base);
            progressBarWebCard = itemView.findViewById(R.id.progressBarWebCardchat);
            lylweb = itemView.findViewById(R.id.lylweb_chat_base);

        }

        @Override
        public void bind(Modelo modelo) {

            int tipo = modelo.getInt(DETCHAT_TIPO);

            mensaje.setText(modelo.getString(DETCHAT_MENSAJE));
            fecha.setText(TimeDateUtil.getDateTimeString(modelo.getLong(DETCHAT_FECHA)));

            if (tipo == RECIBIDO) {

                card.setCardBackgroundColor(getContext().getResources().getColor(R.color.Color_msg_recibido));
                card.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                params.setMargins((int) (10 * densidad), (int) (10 * densidad), (int) (10 * densidad), 0);

                card.setLayoutParams(params);

                card.setVisibility(View.VISIBLE);

            } else {
                card.setVisibility(View.GONE);

            }

            String webprod = modelo.getString(DETCHAT_URL);


            if (webprod != null && JavaUtil.isValidURL(webprod)) {

                lylweb.setVisibility(View.VISIBLE);

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


                webView.loadUrl(webprod);
                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int progress) {
                        progressBarWebCard.setProgress(0);
                        progressBarWebCard.setVisibility(View.VISIBLE);
                        progressBarWebCard.setProgress(progress * 1000);

                        progressBarWebCard.incrementProgressBy(progress);

                        if (progress == 100) {
                            progressBarWebCard.setVisibility(View.GONE);
                        }
                    }
                });

            } else {
                lylweb.setVisibility(View.GONE);
            }

            super.bind(modelo);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVMsgChat(view);
        }
    }
}
