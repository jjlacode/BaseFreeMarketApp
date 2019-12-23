package com.codevsolution.base.nosql;

import android.content.Context;
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
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.LockableScrollView;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.chat.EnviarNoticias;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.firebase.ContratoFirebase;
import com.codevsolution.base.firebase.FirebaseUtil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.localization.LocalizacionUtils;
import com.codevsolution.base.localization.MapZona;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.models.FirebaseFormBase;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.models.Rating;
import com.codevsolution.base.module.RatingVotoUserComents;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import static com.codevsolution.base.sqlite.ContratoSystem.Tablas.ZONA_ALCANCE;
import static com.codevsolution.base.sqlite.ContratoSystem.Tablas.ZONA_NOMBRE;

public abstract class FragmentMasterDetailNoSQLFirebaseRatingWebMapSus extends FragmentMasterDetailNoSQL {

    private View viewWeb;
    protected WebView browser;
    protected ProgressBar progressBarWeb;
    protected NestedScrollView lyweb;
    protected String web;
    protected String stemp = "";
    protected String idRating;
    private int posicion;
    protected String nombreVoto;
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
    protected RatingVotoUserComents ratingBase;
    protected Button btnWeb;
    private View viewSC;
    private View viewChatRec;


    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        tipoForm = setTipoForm();
        tipo = setTipo();

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

        viewSC = addVista(Estilos.getIdLayout(contexto, "layout_suscripcion"), frdetalleExtraspost);

        if (tipoForm.equals(NUEVO)) {

            mapaZona = new MapZona(this, frdetalleExtraspost, activityBase);
            setEventsMapNuevo(mapaZona);
            mapaZona.setTipo(tipo);

        } else if (tipoForm.equals(LISTA)) {

            LockableScrollView scrollCab = new LockableScrollView(contexto);
            frCabecera.addView(scrollCab);
            mapaZona = new MapZona(this, scrollCab, activityBase);
            setEventsMapLista(mapaZona);
            idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);
            mapaZona.setId(idUser);
            mapaZona.setTipo(tipo);
        }

        ratingBase = new RatingVotoUserComents(this, frdetalleExtraspost, activityBase);
        Estilos.setLayoutParams(frdetalleExtraspost, ratingBase.getVievGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);

        ratingBase.setOnEnviarVotoListener(new RatingVotoUserComents.OnEnviarVoto() {
            @Override
            public void onClickEnviar(float votoUser, String comentario) {

                enviarVoto(contexto, id, votoUser, comentario);
            }
        });

        viewChatRec = addVista(Estilos.getIdLayout(contexto, "fragment_chat_base"), frdetalleExtraspost);

        btnWeb = new Button(contexto);
        btnWeb.setBackground(Estilos.getBotonSecondary());
        btnWeb.setText(Estilos.getString(contexto, "ver_web"));
        frdetalleExtraspost.addView(btnWeb);
        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lyweb.getVisibility() == View.GONE) {
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
        });


        chActivo = (Switch) ctrl(viewSC, Estilos.getIdResource(contexto, "chactivo"));
        btnEnviarNoticias = (Button) ctrl(viewSC, Estilos.getIdResource(contexto, "btn_enviar_noticias"));
        suscripcion = (Switch) ctrl(viewSC, Estilos.getIdResource(contexto, "swsuscripcion"));//R.id.swsuscripcion);
        suscritos = (EditMaterial) ctrl(viewSC, Estilos.getIdResource(contexto, "etsuscritos"));
        rvMsgChat = (RecyclerView) ctrl(viewChatRec, Estilos.getIdResource(contexto, "rvdetmsgchat_base"));
        noticias = (ToggleButton) ctrl(viewChatRec, Estilos.getIdResource(contexto, "btn_vernoticias"));
        chatProv = (Button) ctrl(viewChatRec, Estilos.getIdResource(contexto, "btn_chat_prov"));
        lyChat = (LinearLayout) ctrl(viewChatRec, Estilos.getIdResource(contexto, "ly_chat"));
        gone(rvMsgChat);
        if (modulo) {
            gone(chActivo);
        }

        //gone(lyvoto);
        //ratingBar.setIsIndicator(true);

        viewWeb = addVista(Estilos.getIdLayout(contexto, "layout_webview"), frWeb);

        browser = viewWeb.findViewById(R.id.webBrowser);
        progressBarWeb = viewWeb.findViewById(R.id.progressBarWeb);
        lyweb = viewWeb.findViewById(R.id.lywebBrowser);
        gone(lyweb);

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


            firebaseUtil.getValue(ContratoFirebase.getRutaUser(), LOCALIZACION, new FirebaseUtil.OnGetValue() {
                @Override
                public void onGetValue(Object objeto) {


                }

                @Override
                public void onGetValue(ArrayList listaObjetos) {

                    paisUser = (ArrayList<String>) listaObjetos;
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
        gone(lyChat);
        lyChat.setBackgroundColor(Estilos.colorSecondary(contexto));

    }

    protected void setEventsMapLista(MapZona mapaZona) {

        mapaZona.setOnReadyMap(new MapZona.OnReadyMap() {
            @Override
            public void onMapClickListener(ArrayList<Marker> listaMarkers) {

            }

            @Override
            public void onMapLongClickListener(ArrayList<Marker> listaMarkers) {

            }

            @Override
            public void onMyLocationClickListener(long latUser, long lonUser, ArrayList<String> paisUser) {

                //DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                //db.child(idUserCode).child(LOCALIZACION).setValue(paisUser);
                firebaseUtil.setValue(ContratoFirebase.getRutaUser(), LOCALIZACION, paisUser, null);
            }

            @Override
            public void onMarkerDragEnd(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd, ArrayList<String> paisUser) {

                //DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                //db.child(idUserCode).child(LOCALIZACION).setValue(paisUser);
                firebaseUtil.setValue(ContratoFirebase.getRutaUser(), LOCALIZACION, paisUser, null);
            }

            @Override
            public void onMarkerDragStart() {

            }
        });

        mapaZona.setOnLocalizacionDefListener(new MapZona.OnLocalizacionDef() {
            @Override
            public void onEnable(ArrayList<String> listaUbicaciones) {

                //DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                //db.child(idUserCode).child(LOCALIZACION).setValue(listaUbicaciones);
                firebaseUtil.setValue(ContratoFirebase.getRutaUser(), LOCALIZACION, listaUbicaciones, null);
            }

            @Override
            public void onDisable() {

            }
        });
    }

    protected void setEventsMapNuevo(MapZona mapaZona) {

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

                //DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                for (ModeloSQL zona : listaZonasDel.getLista()) {
                    //db.child(LUGARES).child(tipo).child(zona.getString(ZONA_NOMBRE)).child(id).removeValue();
                    firebaseUtil.removeValue(new String[]{LUGARES, tipo, (zona.getString(ZONA_ALCANCE) + zona.getString(ZONA_NOMBRE)).toLowerCase()}, id, null);
                    firebaseUtil.removeValue(new String[]{id, ZONAS}, (zona.getString(ZONA_ALCANCE) + zona.getString(ZONA_NOMBRE)).toLowerCase(), null);
                }

                for (ModeloSQL zona : listaZonasAdd.getLista()) {
                    //db.child(LUGARES).child(tipo).child(zona.getString(ZONA_NOMBRE)).child(id).setValue(true);
                    firebaseUtil.setValue(new String[]{LUGARES, tipo, (zona.getString(ZONA_ALCANCE) + zona.getString(ZONA_NOMBRE)).toLowerCase()}, id, TimeDateUtil.ahora(), null);
                    firebaseUtil.setValue(new String[]{id, ZONAS}, (zona.getString(ZONA_ALCANCE) + zona.getString(ZONA_NOMBRE)).toLowerCase(), TimeDateUtil.ahora(), null);
                }

            }

            @Override
            public void onMarkerDragStart() {

            }

        });
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
    }

    protected void actualizarZonas(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd) {

        if (nnn(id) && nnn(tipo)) {

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            for (ModeloSQL zona : listaZonasDel.getLista()) {
                db.child(LUGARES).child(tipo).child((zona.getString(ZONA_ALCANCE) + zona.getString(ZONA_NOMBRE)).toLowerCase()).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        System.out.println("zona borrada = " + (zona.getString(ZONA_ALCANCE) + zona.getString(ZONA_NOMBRE)).toLowerCase());
                    }
                });
                firebaseUtil.removeValue(new String[]{id, ZONAS}, (zona.getString(ZONA_ALCANCE) + zona.getString(ZONA_NOMBRE)).toLowerCase(), null);
            }

            for (ModeloSQL zona : listaZonasAdd.getLista()) {
                db.child(LUGARES).child(tipo).child((zona.getString(ZONA_ALCANCE) + zona.getString(ZONA_NOMBRE)).toLowerCase()).child(id).setValue(TimeDateUtil.ahora()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        System.out.println("zona añadida = " + (zona.getString(ZONA_ALCANCE) + zona.getString(ZONA_NOMBRE)).toLowerCase());
                    }
                });
                firebaseUtil.setValue(new String[]{id, ZONAS}, (zona.getString(ZONA_ALCANCE) + zona.getString(ZONA_NOMBRE)).toLowerCase(), TimeDateUtil.ahora(), null);
            }
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

                recuperarVotoUsuario(contexto, idRating);
                AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, id);
                gone(btnPublicar);

            } else {

                AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, NULL);

            }
            if (!nuevo) {
                if (nn(firebaseFormBase)) {
                    idRating = setIdRating();
                    recuperarVotos(idRating);
                    recuperarComentarios(idRating);
                }
            }

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

        recuperarVotoUsuario(contexto, idRating);

        if (web != null && !web.equals("") && JavaUtil.isValidURL(web)) {

            visible(btnWeb);

        } else {
            gone(btnWeb);
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
                        visible(noticias);

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

                            db.child(SUSCRIPCIONES).child(id).child(idUser).setValue(TimeDateUtil.ahora());
                            db.child(idUser).child(SUSCRIPCIONES + tipo).child(id).setValue(TimeDateUtil.ahora());

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
                    recuperarVotos(id);
                }
            });
        } else {
            Toast.makeText(contexto, "Debe tener un perfil de " + perfilUser + " para votar", Toast.LENGTH_SHORT).show();
        }

    }

    protected void recuperarVotos(final String id) {


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

                    ratingBase.recuperarVotos(listaVotos);
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


        if (id != null && tipo != null) {
            db = FirebaseDatabase.getInstance().getReference().child(RATING).child(id).child(idUser + perfilUser);


            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    Rating rat = dataSnapshot.getValue(Rating.class);

                    if (rat != null) {

                        ratingBase.recuperarVotosUsuario(rat.getValor(), rat.getComentario());
                    }

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

                    ArrayList<Rating> listaComents = new ArrayList<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        listaComents.add(child.getValue(Rating.class));

                    }

                    ratingBase.recuperarComentarios(listaComents);

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

        if (nnn(id) && nnn(tipo)) {
            ImagenUtil.guardarImageFirestore(SLASH + idUser + SLASH + id + tipo, imagen, path);
        }
    }

    protected void accionesImagen() {

        System.out.println("Acciones imagen");
        System.out.println("tipoForm = " + tipoForm);
        if (!modulo) {
            imagen.setVisibleBtn();
        }

        if (tipoForm.equals(NUEVO) && !modulo) {
            if (nnn(id) && nnn(tipo)) {
                visible(imagen.getLinearLayoutCompat());
                imagen.setImageFirestorePerfil(activityBase, SLASH + idUser + SLASH + id + tipo);

            } else {
                gone(imagen.getLinearLayoutCompat());
            }

            imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mostrarDialogoOpcionesImagen(contexto);
                }
            });


        } else if (tipoForm.equals(LISTA)) {

            if (nnn(id) && nnn(tipo)) {
                System.out.println("id+tipo = " + (SLASH + idUser + SLASH + id + tipo));
                imagen.setImageFirestorePerfil(activityBase, SLASH + idUser + SLASH + id + tipo);
            }


        }
    }


}
