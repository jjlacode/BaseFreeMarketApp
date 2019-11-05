package com.codevsolution.base.nosql;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.chat.EnviarNoticias;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.localizacion.LocalizacionUtils;
import com.codevsolution.base.localizacion.MapZona;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.models.FirebaseFormBase;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.models.Rating;
import com.codevsolution.base.rating.RatingBase;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.codevsolution.base.sqlite.ContratoSystem.Tablas.CAMPOS_CHAT;
import static com.codevsolution.base.sqlite.ContratoSystem.Tablas.CHAT_ID_CHAT;
import static com.codevsolution.base.sqlite.ContratoSystem.Tablas.CHAT_TIPO;
import static com.codevsolution.base.sqlite.ContratoSystem.Tablas.ZONA_NOMBRE;

public abstract class FragmentMasterDetailNoSQLFirebaseRatingWebMapSus extends FragmentMasterDetailNoSQL {

    private View viewWeb;
    protected WebView browser;
    protected ProgressBar progressBarWeb;
    protected NestedScrollView lyweb;
    protected String web;
    protected String stemp = "";
    //protected ImageButton votar;
    //protected ImageButton verVoto;
    //private EditMaterial voto;
    //private EditMaterial comentario;
    //protected LinearLayout lyvoto;
    //protected RatingBar ratingBar;
    //protected RatingBar ratingBarUser;
    //private float rating, nVotos, st1, st2, st3, st4, st5;
    //private TextView tvst1, tvst2, tvst3, tvst4, tvst5, totVotos;
    //private RecyclerView rvcoment;
    //private RelativeLayout rlcoment;
    //private ImageButton verComents;
    //protected LinearLayout lystars;
    //private ArrayList<Rating> listaVotos;
    //private float votoUser;
    protected String idRating;
    //private ArrayList<Rating> listaComents;
    //private TextView ultimoVoto;
    private int posicion;
    //protected String nombreVoto;
    protected String tipo;
    protected String perfil;
    protected String tipoForm;
    private DatabaseReference db;
    protected RecyclerView rvMsgChat;
    protected Query query;
    protected boolean nuevo;
    protected Button btnEnviarNoticias;
    protected Switch chActivo;
    protected boolean activo;
    protected String id;
    protected FirebaseFormBase firebaseFormBase;
    protected LinearLayout lyChat;
    protected ArrayList<String> paisUser;
    protected long latUser;
    protected long lonUser;
    protected Switch suscripcion;
    private LocalizacionUtils localizacionUtils;
    protected ListaModeloSQL listaMsgChat;
    protected String idChat;
    protected EditMaterial suscritos;
    protected String nombreChat;
    protected ToggleButton noticias;
    protected Button chatProv;
    protected boolean location;
    protected MapZona mapaZona;
    protected Button btnPublicar;
    protected RatingBase ratingBase;


    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        tipoForm = setTipoForm();
        tipo = setTipo();

        View viewSC = inflater.inflate(R.layout.layout_suscripcion, container, false);
        if (viewSC.getParent() != null) {
            ((ViewGroup) viewSC.getParent()).removeView(viewSC); // <- fix
        }
        frdetalleExtraspost.addView(viewSC);
        View viewRB = inflater.inflate(R.layout.layout_rating, container, false);
        if (viewRB.getParent() != null) {
            ((ViewGroup) viewRB.getParent()).removeView(viewRB); // <- fix
        }
        frdetalleExtraspost.addView(viewRB);

        if (tipoForm.equals(NUEVO)) {

            mapaZona = new MapZona(this, frdetalleExtraspost, activityBase);
            mapaZona.setOnReadyMap(new MapZona.OnReadyMap() {
                @Override
                public void onMapClickListener(ArrayList<Marker> listaMarkers) {

                }

                @Override
                public void onMapLongClickListener(ArrayList<Marker> listaMarkers) {

                }

                @Override
                public void onMyLocationClickListener(long latUserMap, long lonUserMap, ArrayList<String> paisUser) {

                    latUser = latUserMap;
                    lonUser = lonUserMap;
                }

                @Override
                public void onMarkerDragEnd(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd, ArrayList<String> paisUser) {

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    for (ModeloSQL zona : listaZonasDel.getLista()) {
                        db.child(LUGARES).child(tipo).child(zona.getString(ZONA_NOMBRE)).child(id).removeValue();
                    }

                    for (ModeloSQL zona : listaZonasAdd.getLista()) {
                        db.child(LUGARES).child(tipo).child(zona.getString(ZONA_NOMBRE)).child(id).setValue(true);
                    }

                }

            });

            mapaZona.setTipo(tipo);
            mapaZona.setOnMarcadorEventListener(new MapZona.OnMarcadorEvent() {
                @Override
                public void onCreateMarcador(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd) {

                    actualizarZonas(listaZonasDel, listaZonasAdd);

                }

                @Override
                public void onDeleteMarcador(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd) {
                    actualizarZonas(listaZonasDel, listaZonasAdd);
                }

                @Override
                public void onUpdateMarcador(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd) {
                    actualizarZonas(listaZonasDel, listaZonasAdd);
                }
            });

        } else if (tipoForm.equals(LISTA)) {

            mapaZona = new MapZona(this, frCabecera, activityBase);
            mapaZona.setOnReadyMap(new MapZona.OnReadyMap() {
                @Override
                public void onMapClickListener(ArrayList<Marker> listaMarkers) {

                }

                @Override
                public void onMapLongClickListener(ArrayList<Marker> listaMarkers) {

                }

                @Override
                public void onMyLocationClickListener(long latUser, long lonUser, ArrayList<String> paisUser) {

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child(idUser).child(LOCALIZACION).setValue(paisUser);
                }

                @Override
                public void onMarkerDragEnd(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd, ArrayList<String> paisUser) {

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child(idUser).child(LOCALIZACION).setValue(paisUser);
                }
            });

            mapaZona.setOnLocalizacionDefListener(new MapZona.OnLocalizacionDef() {
                @Override
                public void onEnable(ArrayList<String> listaUbicaciones) {

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child(idUser).child(LOCALIZACION).setValue(listaUbicaciones);
                }

                @Override
                public void onDisable() {

                }
            });
            idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);
            mapaZona.setId(idUser);
            mapaZona.setTipo(tipo);
        }

        ratingBase = new RatingBase(fragment, frdetalleExtraspost, activityBase);


        View viewChatRec = inflater.inflate(R.layout.fragment_chat_base, container, false);
        if (viewChatRec.getParent() != null) {
            ((ViewGroup) viewChatRec.getParent()).removeView(viewChatRec); // <- fix
        }
        frdetalleExtraspost.addView(viewChatRec);
        btnPublicar = new Button(contexto);
        btnPublicar.setBackground(Estilos.getBotonSecondary());
        btnPublicar.setText(Estilos.getString(contexto, "publicar"));
        frdetalleExtraspost.addView(btnPublicar);
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });

        chActivo = (Switch) ctrl(R.id.chactivo);
        //ratingBar = view.findViewById(R.id.ratingBarFreelance);
        //ratingBarUser = view.findViewById(R.id.ratingBarUserFreelance);
        //votar = (ImageButton) ctrl(R.id.btn_votarfreelance);
        //verVoto = (ImageButton) ctrl(R.id.btn_vervotofreelance);
        //voto = (EditMaterial) ctrl(R.id.etvotodetfreelance);
        //comentario = (EditMaterial) ctrl(R.id.etcomentdetfreelance);
        //lyvoto = view.findViewById(R.id.lyvoto);
        //lystars = view.findViewById(R.id.lystars);
        //tvst1 = view.findViewById(R.id.tvstar1);
        //tvst2 = view.findViewById(R.id.tvstar2);
        //tvst3 = view.findViewById(R.id.tvstar3);
        //tvst4 = view.findViewById(R.id.tvstar4);
        //tvst5 = view.findViewById(R.id.tvstar5);
        //totVotos = view.findViewById(R.id.totalvotos);
        //ultimoVoto = view.findViewById(R.id.ultimovoto);
        //rvcoment = view.findViewById(R.id.rvcomentariosstar);
        //rlcoment = view.findViewById(R.id.rlcomentariosstar);
        //verComents = view.findViewById(R.id.btn_comentstar);
        btnEnviarNoticias = (Button) ctrl(R.id.btn_enviar_noticias);
        suscripcion = (Switch) ctrl(R.id.swsuscripcion);
        suscritos = (EditMaterial) ctrl(R.id.etsuscritos);
        rvMsgChat = (RecyclerView) ctrl(R.id.rvdetmsgchat_base);
        noticias = (ToggleButton) ctrl(R.id.btn_vernoticias);
        chatProv = (Button) ctrl(R.id.btn_chat_prov);
        lyChat = (LinearLayout) ctrl(R.id.ly_chat);
        gone(rvMsgChat);

        //gone(lyvoto);
        //ratingBar.setIsIndicator(true);

        viewWeb = inflater.inflate(R.layout.layout_webview, container, false);
        if (viewWeb.getParent() != null) {
            ((ViewGroup) viewWeb.getParent()).removeView(viewWeb); // <- fix
        }
        frWeb.addView(viewWeb);

        browser = view.findViewById(R.id.webBrowser);
        progressBarWeb = view.findViewById(R.id.progressBarWeb);
        lyweb = view.findViewById(R.id.lywebBrowser);

        idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);


        if (AndroidUtil.getSharePreference(contexto, PREFERENCIAS, MIUBICACION, false) && location) {

            localizacionUtils = new LocalizacionUtils();
            localizacionUtils.toggleBestUpdates(true, activityBase);
            localizacionUtils.setOnLocalizationChange(new LocalizacionUtils.OnBestLocalizationChange() {
                @Override
                public void onBestLocalizationChange(double lat, double lon) {
                    Address paisUser = LocalizacionUtils.getAddressGeoCoord(contexto, lat, lon);
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child(idUser).child(LOCALIZACION).setValue(addressToList(paisUser));
                }
            });


            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            Query querydbLocal = db.child(idUser).child(LOCALIZACION);
            querydbLocal.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    paisUser = dataSnapshot.getValue(tipoArrayListsString);
                    if (id == null) {
                        selector();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, (int) ((double) altoReal / 2)
        );
        lyChat.setLayoutParams(lp);

    }

    private void actualizarZonas(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        for (ModeloSQL zona : listaZonasDel.getLista()) {
            db.child(LUGARES).child(tipo).child(zona.getString(ZONA_NOMBRE)).child(id).removeValue();
        }

        for (ModeloSQL zona : listaZonasAdd.getLista()) {
            db.child(LUGARES).child(tipo).child(zona.getString(ZONA_NOMBRE)).child(id).setValue(true);
        }
    }

    protected abstract String setTipo();

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        if (nn(bundle)) {

            perfil = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
            titulo = bundle.getString(TITULO);

            if (bundle.getBoolean(AVISO)) {
                visible(lyChat);
                noticias.setChecked(true);
                esDetalle = true;
            } else {
                gone(lyChat);
            }
            idChat = bundle.getString(CAMPO_ID);
            id = bundle.getString(USERID);
        }
        getFirebaseFormBase();

    }

    @Override
    public void onPause() {
        super.onPause();
        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, NULL);

    }

    protected void selector() {

        setNombreVoto();
        lista = new ArrayList();
        maestroDetalle();

        if (esDetalle) {
            setDatos();
            if (!tipoForm.equals(NUEVO)) {

                recuperarVotoUsuario(ratingBarUser, contexto, idRating);
                AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, id);

            } else {

                AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, NULL);

            }
            if (!nuevo) {
                if (nn(firebaseFormBase)) {
                    idRating = setIdRating();
                    recuperarVotos(ratingBar, idRating);
                    recuperarComentarios(idRating);
                }
            }

            gone(lyvoto);
            onSetDatos();
        } else {
            AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, NULL);
        }

        activityBase.toolbar.setSubtitle(titulo);


        acciones();

    }

    protected void onSetDatos() {

        if (mapaZona != null) {
            mapaZona.setId(id);
        }

        if (web != null && !web.equals("") && JavaUtil.isValidURL(web)) {

            visible(lyweb);

            // Cargamos la web


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


        } else {
            gone(lyweb);
        }



    }

    protected void contarSuscritos() {

        if (id != null) {


            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            Query querydb = db.child(SUSCRIPCIONES).child(id).orderByKey();

            querydb.addValueEventListener(new ValueEventListener() {

                private int nunSuscriptores;

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        nunSuscriptores++;
                        suscritos.setText(String.valueOf(nunSuscriptores));

                    }

                    onContarSuscritos(nunSuscriptores);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    protected void onContarSuscritos(int suscriptores) {

    }

    protected void comprobarSuscripcion() {


        if (nn(id)) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            Query querydb = db.child(SUSCRIPCIONES).child(id).child(idUser);

            querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (nn(dataSnapshot.getValue())) {
                        suscripcion.setChecked(true);
                    } else {
                        suscripcion.setChecked(false);
                        DatabaseReference dbsus = FirebaseDatabase.getInstance().getReference();
                        dbsus.child(idUser).child(SUSCRIPCIONES + tipo).child(id).removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
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

                    if (firebaseFormBase == null && tipoForm.equals(NUEVO)) {
                        Toast.makeText(contexto, getString(R.string.debe_tener_perfil) + perfil, Toast.LENGTH_SHORT).show();
                        rv.setBackground(getResources().getDrawable(R.drawable.alert_box_r));
                    } else if (nn(firebaseFormBase) && tipoForm.equals(NUEVO)) {
                        nombreChat = firebaseFormBase.getNombreBase();
                        setRv();
                        onFirebaseFormBase();
                    } else if (nn(firebaseFormBase)) {
                        onFirebaseFormBase();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            dbFirebase.addValueEventListener(eventListenerProd);

        }

    }

    protected void onFirebaseFormBase() {

    }

    protected abstract String setIdRating();

    protected abstract String setTipoForm();

    protected void acciones() {

        super.acciones();

        ratingBarUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                if (b) {

                    int color = 0;
                    if (v > 3) {
                        color = R.color.Color_star_ok;
                    } else if (v > 1.5) {
                        color = R.color.Color_star_acept;
                    } else if (v > 0) {
                        color = R.color.Color_star_notok;
                    } else {
                        color = R.color.color_star_defecto;
                    }

                    ratingBarUser.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(contexto, color)));

                    voto.setText(String.valueOf((int) (v)));
                    votoUser = v;
                }
            }
        });

        votar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gone(lyvoto);
                enviarVoto(contexto, idRating, votoUser, comentario.getTexto());
            }
        });

        verVoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lyvoto.getVisibility() == View.VISIBLE) {
                    gone(lyvoto);
                } else {
                    visible(lyvoto);
                    recuperarVotoUsuario(ratingBarUser, contexto, idRating);
                }

            }
        });

        verComents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rlcoment.getVisibility() == View.VISIBLE) {
                    gone(rlcoment);
                    gone(lystars);
                } else {

                    recuperarComentarios(idRating);
                    visible(rlcoment);
                    visible(lystars);

                }
            }
        });

        if (tipoForm.equals(NUEVO)) {

            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    guardar();
                }
            });


            btnEnviarNoticias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    bundle = new Bundle();

                    ListaModeloSQL listaChats = CRUDutil.setListaModelo(CAMPOS_CHAT);
                    for (ModeloSQL chat : listaChats.getLista()) {
                        if (chat.getString(CHAT_TIPO).equals(tipo)) {
                            putBundle(CAMPO_ID, chat.getString(CHAT_ID_CHAT));

                        }
                    }
                    putBundle(TIPO, tipo);
                    putBundle(IDCHATF, id);
                    putBundle(NOMBRECHAT, nombreChat);
                    icFragmentos.enviarBundleAFragment(bundle, new EnviarNoticias());
                }
            });

        } else if (tipoForm.equals(LISTA)) {


            suscripcion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

                    if (nn(firebaseFormBase)) {
                        if (b && id != null) {

                            db.child(SUSCRIPCIONES).child(id).child(idUser).setValue(true);
                            db.child(idUser).child(SUSCRIPCIONES + tipo).child(id).setValue(true);

                        } else if (!b && id != null) {

                            db.child(SUSCRIPCIONES).child(id).child(idUser).removeValue();
                            db.child(idUser).child(SUSCRIPCIONES + tipo).child(id).removeValue();
                        }
                    } else {
                        Toast.makeText(contexto, getString(R.string.debe_tener_perfil), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            noticias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (noticias.isChecked()) {
                        visible(lyChat);
                    } else {
                        gone(lyChat);
                    }
                }
            });
        }

        setAcciones();

    }

    protected ArrayList<String> addressToList(Address direccion) {


        if (direccion != null) {

            ArrayList<String> listaLocal = new ArrayList<>();

            String country = direccion.getCountryName();
            if (country != null) {
                listaLocal.add(country);
            }
            String region = direccion.getAdminArea();
            if (region != null) {
                listaLocal.add(region);
            }
            String provincia = direccion.getSubAdminArea();
            if (provincia != null) {
                listaLocal.add(provincia);
            }
            String ciudad = direccion.getLocality();
            if (ciudad != null) {
                listaLocal.add(ciudad);
            }
            String postalCode = direccion.getPostalCode();
            if (postalCode != null) {
                listaLocal.add(postalCode);
            }

            return listaLocal;
        }

        return null;

    }


    protected void setNombreVoto() {

        perfilUser = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
        idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);

        if (!idUser.equals(NULL)) {

            DatabaseReference dbFirebase = FirebaseDatabase.getInstance().getReference()
                    .child(perfilUser).child(idUser).child("nombreBase");

            ValueEventListener eventListenerProd = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    nombreVoto = dataSnapshot.getValue(String.class);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            dbFirebase.addValueEventListener(eventListenerProd);

        }

    }

    protected void enviarVoto(Context contexto, final String id, float valor, String comentario) {

        perfilUser = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
        idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);

        if (nombreVoto != null) {

            Rating rat = new Rating(valor, perfilUser, id, idUser, nombreVoto, comentario, TimeDateUtil.ahora());
            FirebaseDatabase.getInstance().getReference().child(RATING).child(id).child(idUser + perfilUser).setValue(rat, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    recuperarVotos(ratingBar, id);
                }
            });
        } else {
            Toast.makeText(contexto, "Debe tener un perfil de " + perfilUser + " para votar", Toast.LENGTH_SHORT).show();
        }

    }

    protected void recuperarVotos(final RatingBar ratingBar, final String id) {

        ratingBar.setRating(0.0f);
        Drawable progressDrawable = ratingBar.getProgressDrawable();
        if (progressDrawable != null) {
            DrawableCompat.setTint(progressDrawable,
                    contexto.getResources().getColor(R.color.color_star_defecto));
        }

        if (id != null) {
            db = FirebaseDatabase.getInstance().getReference().child(RATING).child(id);


            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listaVotos = new ArrayList<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        System.out.println("child.getValue() = " + child.getValue());
                        listaVotos.add(child.getValue(Rating.class));

                    }

                    rating = 0.0f;
                    nVotos = 0.0f;
                    st1 = 0;
                    st2 = 0;
                    st3 = 0;
                    st4 = 0;
                    st5 = 0;
                    long ultimoVotoTemp = 0;


                    for (Rating rat : listaVotos) {

                        //System.out.println("rat tipo= " + rat.getTipo());
                        //System.out.println("tipo = " + tipo);
                        //if (rat.getTipo().equals(tipo)) {

                        float voto = rat.getValor();

                        System.out.println("voto = " + voto);

                        if (voto == 1) {
                            st1++;
                            nVotos++;
                        } else if (voto == 2) {
                            st2++;
                            nVotos++;
                        } else if (voto == 3) {
                            st3++;
                            nVotos++;
                        } else if (voto == 4) {
                            st4++;
                            nVotos++;
                        } else if (voto == 5) {
                            st5++;
                            nVotos++;
                        }

                        rating += voto;
                        //}
                        if (rat.getFecha() > ultimoVotoTemp) {
                            ultimoVotoTemp = rat.getFecha();
                        }
                    }

                    tvst5.setText(JavaUtil.getDecimales(st5, "0"));
                    tvst4.setText(JavaUtil.getDecimales(st4, "0"));
                    tvst3.setText(JavaUtil.getDecimales(st3, "0"));
                    tvst2.setText(JavaUtil.getDecimales(st2, "0"));
                    tvst1.setText(JavaUtil.getDecimales(st1, "0"));
                    totVotos.setText("Numero de valoraciones: " + JavaUtil.getDecimales(nVotos, "0"));
                    if (ultimoVotoTemp > 0) {
                        ultimoVoto.setText("Ultima valoración: " + TimeDateUtil.getDateString(ultimoVotoTemp));
                    } else {
                        ultimoVoto.setText("Sin valoraciones todavía");
                    }

                    if (nVotos > 0) {
                        rating /= nVotos;
                        ratingBar.setRating(rating);
                    } else {
                        ratingBar.setRating(0.0f);
                    }

                    int color = 0;
                    if (rating > 3) {
                        color = R.color.Color_star_ok;
                    } else if (rating > 1.5) {
                        color = R.color.Color_star_acept;
                    } else if (rating > 0) {
                        color = R.color.Color_star_notok;
                    } else {
                        color = R.color.color_star_defecto;
                    }

                    ratingBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(contexto, color)));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            db.addValueEventListener(valueEventListener);
        }

    }

    public void recuperarVotoUsuario(final Context contexto, final String id) {

        idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);
        perfilUser = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);

        votoUser = 0.0f;
        comentario.setText("");
        ratingBarUser.setRating(votoUser);

        if (id != null && tipo != null) {
            db = FirebaseDatabase.getInstance().getReference().child(RATING).child(id).child(idUser + perfilUser);


            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    Rating rat = dataSnapshot.getValue(Rating.class);

                    if (rat != null) {
                        //votoUser = rat.getValor();
                        //comentario.setText(rat.getComentario());
                        ratingBase.setVotoUser(rat.getValor());
                        ratingBase.setComentario(rat.getComentario());
                    }

                    //voto.setText(String.valueOf(votoUser));
                    // ratingBarUser.setRating(votoUser);
                    int color = 0;
                    if (votoUser > 3) {
                        color = R.color.Color_star_ok;
                    } else if (votoUser > 1.5) {
                        color = R.color.Color_star_acept;
                    } else if (votoUser > 0) {
                        color = R.color.Color_star_notok;
                    } else {
                        color = R.color.color_star_defecto;
                    }

                    ratingBarUser.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(contexto, color)));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            db.addValueEventListener(valueEventListener);

        }
    }

    protected void recuperarComentarios(final String id) {


        if (id != null) {
            db = FirebaseDatabase.getInstance().getReference().child(RATING).child(id);


            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listaComents = new ArrayList<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        listaComents.add(child.getValue(Rating.class));

                    }

                    RVAdapter adapter = new RVAdapter(new ViewHolderRVComents(view), listaComents, R.layout.item_coments);
                    rvcoment.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            db.addValueEventListener(valueEventListener);
        }

    }

    @Override
    protected void setOnLeftSwipeCuerpo() {
        super.setOnLeftSwipeCuerpo();

        if (posicion < lista.size() - 1) {
            posicion++;
            setOnClickRV(lista.get(posicion));
            System.out.println("posicion = " + posicion);
        }
    }

    @Override
    protected void setOnRigthSwipeCuerpo() {
        super.setOnRigthSwipeCuerpo();
        if (posicion > 0 && lista.size() > posicion) {
            posicion--;
            setOnClickRV(lista.get(posicion));
            System.out.println("posicion = " + posicion);
        }
    }

    protected void onClickRV(View v) {

        setOnClickRV(lista.get(rv.getChildAdapterPosition(v)));
        posicion = rv.getChildAdapterPosition(v);
    }

    @Override
    protected void setOnBack() {
        super.setOnBack();

        mapaZona.reiniciarObjetos();

    }

    @Override
    protected void guardarImagen(String path) {
        super.guardarImagen(path);

        ImagenUtil.guardarImageFirestore(id, imagen, path);
    }

    protected void accionesImagen() {

        imagen.setVisibleBtn();

        if (tipoForm.equals(NUEVO)) {
            if (id != null) {
                visible(imagen);
                imagen.setImageFirestorePerfil(activityBase, id);

            } else {
                gone(imagen);
            }

            imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mostrarDialogoOpcionesImagen(contexto);
                }
            });


        } else {

            imagen.setImageFirestorePerfil(activityBase, id);


        }
    }

    public class ViewHolderRVComents extends BaseViewHolder implements TipoViewHolder {

        TextView comentario;
        TextView nombreComent;
        TextView fechaComent;
        TextView tipoComment;
        RatingBar ratingBarComent;

        public ViewHolderRVComents(View itemView) {
            super(itemView);

            comentario = itemView.findViewById(R.id.tvcoments);
            nombreComent = itemView.findViewById(R.id.tvnombrecoments);
            fechaComent = itemView.findViewById(R.id.tvfechacoments);
            ratingBarComent = itemView.findViewById(R.id.ratingBarcoment);
            tipoComment = itemView.findViewById(R.id.tvtipocoments);
        }

        @Override
        public void bind(ArrayList<?> lista, int position) {
            super.bind(lista, position);

            Rating rating = (Rating) lista.get(position);

            comentario.setText(rating.getComentario());
            nombreComent.setText(rating.getNombreUser());
            fechaComent.setText(TimeDateUtil.getDateString(rating.getFecha()));
            tipoComment.setText(rating.getTipo());
            float ratUser = rating.getValor();
            ratingBarComent.setRating(ratUser);

            int color = 0;
            if (ratUser > 3) {
                color = R.color.Color_star_ok;
            } else if (ratUser > 1.5) {
                color = R.color.Color_star_acept;
            } else if (ratUser > 0) {
                color = R.color.Color_star_notok;
            } else {
                color = R.color.color_star_defecto;
            }

            ratingBarComent.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(contexto, color)));


        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVComents(view);
        }
    }

}
