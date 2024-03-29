package com.codevsolution.base.nosql;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import com.codevsolution.base.R;
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltro;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.android.controls.ViewImagenLayout;
import com.codevsolution.base.chat.FragmentChatBase;
import com.codevsolution.base.firebase.FirebaseUtil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.models.FirebaseFormBase;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.models.Rating;
import com.codevsolution.base.module.FormProfileModule;
import com.codevsolution.base.module.RatingBarModule;
import com.codevsolution.base.sqlite.ContratoSystem;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.TimeDateUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public abstract class FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb
        extends FragmentMasterDetailNoSQLFirebaseRatingWebMapSus implements ContratoSystem.Tablas,
        InteractorBase.Constantes {

    protected EditMaterialLayout nombre;
    protected EditMaterialLayout descripcion;
    protected EditMaterialLayout direccion;
    protected EditMaterialLayout email;
    protected EditMaterialLayout telefono;
    protected EditMaterialLayout claves;
    protected EditMaterialLayout etWeb;


    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);
        Log.d(TAG, getMetodo());

        FormProfileModule form = new FormProfileModule(frdetalleExtrasante, contexto, this);
        nombre = form.getNombre();
        descripcion = form.getDescripcion();
        direccion = form.getDireccion();
        email = form.getEmail();
        telefono = form.getTelefono();
        claves = form.getClaves();
        etWeb = form.getWeb();
        imagen = form.getImagen();

        form.setVisibility(true, true, true, true,
                true, true, true, true);

        if (tipoForm.equals(NUEVO)) {
            gone(frCabecera);
            /*
            nombre.setActivo(true);
            descripcion.setActivo(true);
            direccion.setActivo(true);
            telefono.setActivo(true);
            email.setActivo(true);
            web.setActivo(true);

             */
        } else {
            gone(btnEnviarNoticias);
            gone(chActivo);
            visible(frCabecera);
        }

    }


    @Override
    protected void setLayoutItem() {
        Log.d(TAG, getMetodo());

        layoutItemRv = 0;
        layoutItemAuto = R.layout.item_list_firebase_formbase_rating_web;


    }

    @Override
    protected void setLayout() {
        Log.d(TAG, getMetodo());

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
        Log.d(TAG, getMetodo());

        gone(lyChat);

        if (tipoForm.equals(LISTA) && paisUser != null) {

            lista = new ArrayList<Productos>();

            String lugar = "";

            for (int i = 5; i >= mapaZona.getAlcance(); i--) {

                switch (i) {

                    case 5:
                        lugar = Estilos.getString(contexto, "codigo_postal") + paisUser.get(4);
                        break;
                    case 4:
                        lugar = Estilos.getString(contexto, "local") + paisUser.get(3);
                        break;
                    case 3:
                        lugar = Estilos.getString(contexto, "provincial") + paisUser.get(2);
                        break;
                    case 2:
                        lugar = Estilos.getString(contexto, "regional") + paisUser.get(1);
                        break;
                    case 1:
                        lugar = Estilos.getString(contexto, "nacional") + paisUser.get(0);
                        break;
                    case 0:
                        lugar = Estilos.getString(contexto, "mundial") + MUNDIAL;
                        break;

                }

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                Query querydb = db.child(LUGARES).child(tipo).child(lugar.toLowerCase());

                querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot prod : dataSnapshot.getChildren()) {

                            if (prod.getValue(Long.class) > 0) {

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

            //gone(verVoto);
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
        Log.d(TAG, getMetodo());

        if (!modulo) {
            activityBase.fabNuevo.hide();
            activityBase.fabInicio.show();
        }
    }


    protected void setDatos() {
        Log.d(TAG, getMetodo());

        if (tipoForm.equals(NUEVO)) {

            if (!modulo) {
                activityBase.fabNuevo.hide();
                activityBase.fabInicio.show();
            }
            visible(btnEnviarNoticias);
            gone(btndelete);
            visible(btnsave);
            visible(chActivo);
            visible(claves.getLinearLayout());
            visible(etWeb.getLinearLayout());

            ratingBase.setVisibilidadBtnVerVotoUser(false);
            gone(suscripcion);
            visible(suscritos);
            gone(lyChat);
            gone(btnback);
            if (id == null) {
                id = idUser;
            }

        } else {

            comprobarSuscripcion();
            visible(suscripcion);
            gone(suscritos);
            gone(btndelete);
            gone(btnsave);
            gone(claves.getLinearLayout());

            if (idChat == null) {
                ListaModeloSQL listaChats = setListaModelo(CAMPOS_CHAT);
                for (ModeloSQL chat : listaChats.getLista()) {
                    if (chat.getString(CHAT_USUARIO).equals(id) && chat.getString(CHAT_TIPO).equals(tipo)) {
                        idChat = chat.getString(CHAT_ID_CHAT);
                    }
                }
            }

            if (nn(idChat)) {
                listaMsgChat = setListaModeloDetalle(CAMPOS_DETCHAT, idChat);
                listaMsgChat = listaMsgChat.sort(DETCHAT_FECHA, DESCENDENTE);

                RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat_base);
                rvMsgChat.setAdapter(adaptadorDetChat);
                visible(rvMsgChat);
                noticias.setChecked(true);
            }

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
            if (tipoForm.equals(LISTA) && web.isEmpty()) {
                gone(etWeb.getLinearLayout());
            }

            setPref(CAMPO_NOMBRE, firebaseFormBase.getNombreBase());
            setPref(CAMPO_DIRECCION, firebaseFormBase.getDireccionBase());
            setPref(CAMPO_TELEFONO, firebaseFormBase.getTelefonoBase());
            setPref(CAMPO_EMAIL, firebaseFormBase.getEmailBase());
            setPref(CAMPO_WEB, firebaseFormBase.getWebBase());

            chActivo.setChecked(firebaseFormBase.isActivo());

            accionesImagen();

        } else {
            guardar();
        }

    }

    @Override
    protected void onGuardarImagen(String path) {
        super.onGuardarImagen(path);
        Log.d(TAG, getMetodo());
        ImagenUtil.guardarImageFirestore(SLASH + idUser + SLASH + id + CAMPO_ACTIVO, imagen, path);
        ImagenUtil.setImageFireStoreCircle(SLASH + idUser + SLASH + id + CAMPO_ACTIVO, activityBase.imagenPerfil);
    }

    @Override
    protected void eliminarImagen() {

        ImagenUtil.deleteImagefirestore(SLASH + idUser + SLASH + id + tipo);
        ImagenUtil.deleteImagefirestore(SLASH + idUser + SLASH + id + CAMPO_ACTIVO);

        super.eliminarImagen();
    }

    @Override
    protected void acciones() {
        super.acciones();
        Log.d(TAG, getMetodo());

        if (tipoForm.equals(NUEVO)) {


            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (idUser != null) {

                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        db.child(tipo).child(idUser).removeValue();
                        db.child(PERFIL).child(tipo).child(idUser).removeValue();
                        db.child(RATING).child(tipo).child(idUser).removeValue();
                        ImagenUtil.deleteImagefirestore(idUser + tipo);
                        ImagenUtil.deleteImagefirestore(idUser + CAMPO_ACTIVO);

                    }
                }
            });


        }

    }

    protected void guardar() {
        Log.d(TAG, getMetodo());

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

        String[] rutaTipo = {tipo};
        firebaseUtil.setValue(rutaTipo, idUser, firebaseFormBase, new FirebaseUtil.OnSetValue() {
            @Override
            public void onCreateOk(String key) {

                nuevo = false;
                System.out.println("Creado perfil ok");

            }

            @Override
            public void onSetValueOk(String key) {
                System.out.println("Guardado perfil ok");
            }

            @Override
            public void onSetValueFail(String key) {
                System.out.println("Fallo al crear o guardar perfil");
            }
        });
        String[] ruta = {PERFIL, tipo};
        firebaseUtil.setValue(ruta, idUser, true, null);
    }

    @Override
    protected String setIdRating() {
        Log.d(TAG, getMetodo());

        return firebaseFormBase.getIdchatBase();
    }


    @Override
    public void setOnClickRV(Object object) {
        Log.d(TAG, getMetodo());

        firebaseFormBase = (FirebaseFormBase) object;
        id = firebaseFormBase.getIdchatBase();
        esDetalle = true;
        selector();

    }

    protected void setMaestroDetalleTabletPort() {
        Log.d(TAG, getMetodo());

        maestroDetalleSeparados = true;
    }


    private class AdapterFiltroFormBaseRatingWeb extends ListaAdaptadorFiltro {


        public AdapterFiltroFormBaseRatingWeb(Context contexto, int R_layout_IdView, ArrayList entradas) {
            super(contexto, R_layout_IdView, entradas);
            Log.d(TAG, getMetodo());


        }

        @Override
        public void onEntrada(Object entrada, View view) {
            Log.d(TAG, getMetodo());

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
            Log.d(TAG, getMetodo());

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

        ViewImagenLayout imagen;
        TextView nombre;
        TextView descripcion;
        TextView direccion;
        TextView telefono;
        TextView email;
        ImageButton btnchat;
        WebView webView;
        RatingBarModule ratingBarCard;
        RatingBarModule ratingBarUserCard;
        NestedScrollView lylweb;
        String web;
        CardView card;
        RelativeLayout relativeLayout;

        public ViewHolderRVFormBaseRatingWeb(View view) {
            super(view);
            Log.d(TAG, getMetodo());

            relativeLayout = view.findViewById(Estilos.getIdResource(contexto, "ry_item_list"));

        }

        @Override
        public void bind(ArrayList<?> lista, int position) {
            Log.d(TAG, getMetodo());

            final FirebaseFormBase firebaseFormBase = (FirebaseFormBase) lista.get(position);

            ViewGroupLayout vistaCard = new ViewGroupLayout(contexto, relativeLayout, new CardView(contexto));
            card = (CardView) vistaCard.getViewGroup();
            LinearLayoutCompat mainLinear = (LinearLayoutCompat) vistaCard.addVista(new LinearLayoutCompat(contexto));
            mainLinear.setOrientation(LinearLayoutCompat.VERTICAL);
            ViewGroupLayout vistaDatos = new ViewGroupLayout(contexto, mainLinear);
            vistaDatos.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
            Estilos.setLayoutParams(mainLinear, vistaDatos.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);
            ViewGroupLayout vistaImagen = new ViewGroupLayout(contexto, vistaDatos.getViewGroup());
            vistaImagen.setOrientacion(ViewGroupLayout.ORI_LLC_VERTICAL, 2.5f);
            Estilos.setLayoutParams(vistaDatos.getViewGroup(), vistaImagen.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.MATCH_PARENT, 2.5f, 0);
            imagen = vistaImagen.addViewImagenLayout();
            Estilos.setLayoutParams(vistaImagen.getViewGroup(), imagen.getLinearLayoutCompat(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.MATCH_PARENT);
            ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, vistaDatos.getViewGroup());
            vistaForm.setOrientacion(ViewGroupLayout.ORI_LLC_VERTICAL, 1f);
            Estilos.setLayoutParams(vistaDatos.getViewGroup(), vistaForm.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT, 1, 0);
            nombre = vistaForm.addTextView(firebaseFormBase.getNombreBase());
            nombre.setTextSize(sizeText * 1.5f);
            nombre.setTypeface(Typeface.DEFAULT_BOLD);
            if (nnn(firebaseFormBase.getDescripcionBase())) {
                descripcion = vistaForm.addTextView(firebaseFormBase.getDescripcionBase());
            }
            direccion = vistaForm.addTextView(firebaseFormBase.getDireccionBase());
            telefono = vistaForm.addTextView(firebaseFormBase.getTelefonoBase());
            email = vistaForm.addTextView(firebaseFormBase.getEmailBase());
            ViewGroupLayout vistaChat = new ViewGroupLayout(contexto, vistaDatos.getViewGroup());
            vistaChat.setOrientacion(ViewGroupLayout.ORI_LLC_VERTICAL, 2.5f);
            Estilos.setLayoutParams(vistaDatos.getViewGroup(), vistaChat.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT, 2.5f, 0);
            btnchat = vistaChat.addImageButtonSecundary(Estilos.getIdDrawable(contexto, "ic_chat_indigo"));

            ratingBarCard = new RatingBarModule(mainLinear, contexto, null, true, false);
            Estilos.setLayoutParams(mainLinear, ratingBarCard.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);
            ratingBarUserCard = new RatingBarModule(mainLinear, contexto, null, true, true);
            Estilos.setLayoutParams(mainLinear, ratingBarUserCard.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);

            lylweb = new NestedScrollView(contexto);
            mainLinear.addView(lylweb);
            lylweb.setFillViewport(true);
            Estilos.setLayoutParams(mainLinear, lylweb, ViewGroupLayout.MATCH_PARENT, (int) ((double) (altoReal) / 3));
            webView = new WebView(contexto);
            lylweb.addView(webView);
            webView.setNestedScrollingEnabled(true);
            Estilos.setLayoutParams(lylweb, webView, ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);

            web = firebaseFormBase.getWebBase();


            if (nn(firebaseFormBase.getIdchatBase()) && !firebaseFormBase.getIdchatBase().equals("")) {

                imagen.setImageFirestorePerfil(activityBase, firebaseFormBase.getIdchatBase() +
                        firebaseFormBase.getTipoBase());
            }
            if (web != null && JavaUtil.isValidURL(web)) {

                visible(lylweb);

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setDisplayZoomControls(true);

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
                    ListaModeloSQL listaChats = new ListaModeloSQL(CAMPOS_CHAT);
                    String idChat = null;
                    for (ModeloSQL chat : listaChats.getLista()) {
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

                        idChat = crearRegistroId(TABLA_CHAT, values);
                    }
                    if (idChat != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(CAMPO_ID, idChat);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentChatBase());
                    }
                }
            });

            ratingBarCard.setBarRating(0);
            recuperarVotos(firebaseFormBase.getIdchatBase());

            ratingBarUserCard.setBarRating(0);
            recuperarVotoUsuario(contexto, firebaseFormBase.getIdchatBase());


            super.bind(lista, position);
        }

        protected void recuperarVotos(final String id) {
            Log.d(TAG, getMetodo());

            DatabaseReference db;

            if (id != null) {
                db = FirebaseDatabase.getInstance().getReference().child(RATING).child(id);


                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        ArrayList<Rating> listaVotos = new ArrayList<>();

                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            System.out.println("child.getValue() = " + child.getValue());
                            listaVotos.add(child.getValue(Rating.class));

                        }

                        ratingBarCard.setBarRating(0.0f);

                        float rating = 0.0f;
                        float nVotos = 0.0f;
                        long ultimoVotoTemp = 0;


                        for (Rating rat : listaVotos) {

                            float voto = rat.getValor();

                            System.out.println("voto = " + voto);

                            if (voto == 1) {
                                nVotos++;
                            } else if (voto == 2) {
                                nVotos++;
                            } else if (voto == 3) {
                                nVotos++;
                            } else if (voto == 4) {
                                nVotos++;
                            } else if (voto == 5) {
                                nVotos++;
                            }

                            rating += voto;

                            if (rat.getFecha() > ultimoVotoTemp) {
                                ultimoVotoTemp = rat.getFecha();
                            }
                        }

                        if (nVotos > 0) {
                            rating /= nVotos;
                            ratingBarCard.setBarRating(rating);
                        } else {
                            ratingBarCard.setBarRating(0.0f);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                db.addValueEventListener(valueEventListener);
            }

        }

        public void recuperarVotoUsuario(final Context contexto, final String id) {
            Log.d(TAG, getMetodo());

            idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);
            perfilUser = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
            DatabaseReference db;

            if (id != null && tipo != null) {
                db = FirebaseDatabase.getInstance().getReference().child(RATING).child(id).child(idUser + perfilUser);


                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        Rating rat = dataSnapshot.getValue(Rating.class);

                        if (rat != null) {
                            float rating = rat.getValor();
                            ratingBarUserCard.setBarRating(rating);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                db.addValueEventListener(valueEventListener);

            }
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
        public void bind(ModeloSQL modeloSQL) {

            int tipo = modeloSQL.getInt(DETCHAT_TIPO);

            mensaje.setText(modeloSQL.getString(DETCHAT_MENSAJE));
            fecha.setText(TimeDateUtil.getDateTimeString(modeloSQL.getLong(DETCHAT_FECHA)));

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

            String webprod = modeloSQL.getString(DETCHAT_URL);


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

            super.bind(modeloSQL);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVMsgChat(view);
        }
    }
}
