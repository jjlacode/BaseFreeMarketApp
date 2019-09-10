package com.jjlacode.base.util.nosql;

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.adapter.BaseViewHolder;
import com.jjlacode.base.util.adapter.RVAdapter;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.base.util.android.AndroidUtil;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.base.util.android.controls.LockableScrollView;
import com.jjlacode.base.util.chat.FragmentChatProductoEnviar;
import com.jjlacode.base.util.localizacion.LocalizacionUtils;
import com.jjlacode.base.util.localizacion.MapUtil;
import com.jjlacode.base.util.models.FirebaseFormBase;
import com.jjlacode.base.util.models.ListaModelo;
import com.jjlacode.base.util.models.Marcador;
import com.jjlacode.base.util.models.Modelo;
import com.jjlacode.base.util.models.Rating;
import com.jjlacode.base.util.time.TimeDateUtil;
import com.jjlacode.freelanceproject.R;

import java.util.ArrayList;
import java.util.List;

import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.DETCHAT_FECHA;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.DETCHAT_MENSAJE;
import static com.jjlacode.base.util.sqlite.ContratoSystem.Tablas.DETCHAT_TIPO;

public abstract class FragmentMasterDetailNoSQLFirebaseRatingWebMapSus extends FragmentMasterDetailNoSQL {

    private View viewWeb;
    protected WebView browser;
    protected ProgressBar progressBarWeb;
    protected NestedScrollView lyweb;
    protected String web;
    protected String stemp = "";
    protected ImageButton votar;
    protected ImageButton verVoto;
    private EditMaterial voto;
    private EditMaterial comentario;
    protected LinearLayout lyvoto;
    protected RatingBar ratingBar;
    protected RatingBar ratingBarUser;
    private float rating, nVotos, st1, st2, st3, st4, st5;
    private TextView tvst1, tvst2, tvst3, tvst4, tvst5, totVotos;
    private RecyclerView rvcoment;
    private RelativeLayout rlcoment;
    private ImageButton verComents;
    private LinearLayout lystars;
    private ArrayList<Rating> listaVotos;
    private float votoUser;
    protected String tipoRating;
    protected String idRating;
    private ArrayList<Rating> listaComents;
    private TextView ultimoVoto;
    private int posicion;
    protected String nombreVoto;
    protected String tipo;
    protected String perfil;
    protected String tipoForm;
    private DatabaseReference db;
    protected RadioGroup radioGroupMap;
    protected RadioButton radioButtonMap;
    protected RadioButton radioButtonMap1;
    protected RadioButton radioButtonMap2;
    protected RadioButton radioButtonMap3;
    protected RadioButton radioButtonMap4;
    protected RadioButton radioButtonMap5;
    protected RadioGroup radioGroupMapCab;
    protected RadioButton radioButtonMapCab;
    protected RadioButton radioButtonMap1Cab;
    protected RadioButton radioButtonMap2Cab;
    protected RadioButton radioButtonMap3Cab;
    protected RadioButton radioButtonMap4Cab;
    protected RadioButton radioButtonMap5Cab;
    protected ToggleButton verMapaCab;
    protected Button crearMarc;
    protected Button borraMarc;
    protected ToggleButton opcionesZona;
    protected RecyclerView rvMsgChat;
    protected Query query;
    protected boolean nuevo;
    protected Button btnEnviarNoticias;
    protected Switch chActivo;
    protected boolean activo;
    protected String id;
    protected FirebaseFormBase firebaseFormBase;
    protected LinearLayout lyMap;
    private LinearLayout lyMapCab;
    protected LinearLayout lyChat;
    protected MapUtil mapa;
    private MapUtil mapaCab;
    private Marcador marcador;
    private boolean clickLocation;
    protected ArrayList<String> paisUser;
    private ArrayList<Marcador> listaMarcadores;
    private long latUser;
    private long lonUser;
    private int alcance;
    private ArrayList<String> zonaList;
    private LockableScrollView scrollCab;
    private ToggleButton opcionesZonaCab;
    protected Switch miUbicacionDef;
    protected Switch suscripcion;
    private LocalizacionUtils localizacionUtils;
    protected ListaModelo listaMsgChat;
    protected String idChat;
    protected EditMaterial zona;
    protected EditMaterial suscritos;
    protected TextView multitxt;
    protected String nombreChat;
    protected ToggleButton noticias;


    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);


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

        View viewMap = inflater.inflate(R.layout.layout_map, container, false);
        if (viewMap.getParent() != null) {
            ((ViewGroup) viewMap.getParent()).removeView(viewMap); // <- fix
        }
        frdetalleExtraspost.addView(viewMap);

        View viewChatRec = inflater.inflate(R.layout.fragment_chat_base, container, false);
        if (viewChatRec.getParent() != null) {
            ((ViewGroup) viewChatRec.getParent()).removeView(viewChatRec); // <- fix
        }
        frdetalleExtraspost.addView(viewChatRec);

        chActivo = (Switch) ctrl(R.id.chactivo);
        ratingBar = view.findViewById(R.id.ratingBarFreelance);
        ratingBarUser = view.findViewById(R.id.ratingBarUserFreelance);
        votar = (ImageButton) ctrl(R.id.btn_votarfreelance);
        verVoto = (ImageButton) ctrl(R.id.btn_vervotofreelance);
        voto = (EditMaterial) ctrl(R.id.etvotodetfreelance);
        comentario = (EditMaterial) ctrl(R.id.etcomentdetfreelance);
        lyvoto = view.findViewById(R.id.lyvoto);
        lystars = view.findViewById(R.id.lystars);
        tvst1 = view.findViewById(R.id.tvstar1);
        tvst2 = view.findViewById(R.id.tvstar2);
        tvst3 = view.findViewById(R.id.tvstar3);
        tvst4 = view.findViewById(R.id.tvstar4);
        tvst5 = view.findViewById(R.id.tvstar5);
        totVotos = view.findViewById(R.id.totalvotos);
        ultimoVoto = view.findViewById(R.id.ultimovoto);
        rvcoment = view.findViewById(R.id.rvcomentariosstar);
        rlcoment = view.findViewById(R.id.rlcomentariosstar);
        verComents = view.findViewById(R.id.btn_comentstar);
        zona = (EditMaterial) ctrl(R.id.etzonaprod);
        btnEnviarNoticias = (Button) ctrl(R.id.btn_enviar_noticias);
        lyMap = (LinearLayout) ctrl(R.id.map);
        lyMapCab = (LinearLayout) ctrl(R.id.mapcap);
        verMapaCab = (ToggleButton) ctrl(R.id.btn_vermapacab);
        crearMarc = (Button) ctrl(R.id.btn_crear_marcador);
        borraMarc = (Button) ctrl(R.id.btn_borrar_marcador);
        opcionesZona = (ToggleButton) ctrl(R.id.btn_opciones_zona);
        opcionesZonaCab = (ToggleButton) ctrl(R.id.btn_opciones_zonaCab);
        radioGroupMap = (RadioGroup) ctrl(R.id.radio_group_map);
        radioButtonMap = (RadioButton) ctrl(R.id.radioButtonMap);
        radioButtonMap1 = (RadioButton) ctrl(R.id.radioButtonMap1);
        radioButtonMap2 = (RadioButton) ctrl(R.id.radioButtonMap2);
        radioButtonMap3 = (RadioButton) ctrl(R.id.radioButtonMap3);
        radioButtonMap4 = (RadioButton) ctrl(R.id.radioButtonMap4);
        radioButtonMap5 = (RadioButton) ctrl(R.id.radioButtonMap5);
        radioGroupMapCab = (RadioGroup) ctrl(R.id.radio_group_map_cab);
        radioButtonMapCab = (RadioButton) ctrl(R.id.radioButtonMap_cab);
        radioButtonMap1Cab = (RadioButton) ctrl(R.id.radioButtonMap1_cab);
        radioButtonMap2Cab = (RadioButton) ctrl(R.id.radioButtonMap2_cab);
        radioButtonMap3Cab = (RadioButton) ctrl(R.id.radioButtonMap3_cab);
        radioButtonMap4Cab = (RadioButton) ctrl(R.id.radioButtonMap4_cab);
        radioButtonMap5Cab = (RadioButton) ctrl(R.id.radioButtonMap5_cab);
        scrollCab = (LockableScrollView) ctrl(R.id.scrollcab);
        miUbicacionDef = (Switch) ctrl(R.id.chmiubicacion_cab);
        multitxt = (TextView) ctrl(R.id.tvmulti);
        suscripcion = (Switch) ctrl(R.id.swsuscripcion);
        suscritos = (EditMaterial) ctrl(R.id.etsuscritos);
        rvMsgChat = (RecyclerView) ctrl(R.id.rvdetmsgchat_base);
        noticias = (ToggleButton) ctrl(R.id.btn_vernoticias);
        lyChat = (LinearLayout) ctrl(R.id.ly_chat);
        zona.grabarEnable(false);
        gone(rvMsgChat);

        gone(lyvoto);
        ratingBar.setIsIndicator(true);

        viewWeb = inflater.inflate(R.layout.layout_webview, container, false);
        if (viewWeb.getParent() != null) {
            ((ViewGroup) viewWeb.getParent()).removeView(viewWeb); // <- fix
        }
        frWeb.addView(viewWeb);

        browser = (WebView) view.findViewById(R.id.webBrowser);
        progressBarWeb = view.findViewById(R.id.progressBarWeb);
        lyweb = view.findViewById(R.id.lywebBrowser);
        tipoForm = setTipoForm();

        miUbicacionDef.setChecked(AndroidUtil.getSharePreference(contexto, PREFERENCIAS, MIUBICACION, false));
        idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);


        if (miUbicacionDef.isChecked() && tipoForm.equals(LISTA)) {

            localizacionUtils = new LocalizacionUtils();
            localizacionUtils.toggleBestUpdates(true, activityBase);
            localizacionUtils.setOnLocalizationChange(new LocalizacionUtils.OnBestLocalizationChange() {
                @Override
                public void onBestLocalizationChange(double lat, double lon) {
                    Address paisUser = LocalizacionUtils.getAddressGeoCoord(contexto, lat, lon);
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child(USERS).child(idUser).child(LOCALIZACION).setValue(addressToList(paisUser));
                }
            });


            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            Query querydbLocal = db.child(USERS).child(idUser).child(LOCALIZACION);
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

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        if (nn(bundle)) {
            if (!tipoForm.equals(NUEVO)) {
                tipo = bundle.getString(TIPO);
            } else {
                tipo = "prod";
            }
            perfil = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
            tipo = "prod" + perfil.toLowerCase();

            titulo = bundle.getString(TITULO);
            if (bundle.getBoolean(AVISO)) {
                visible(lyChat);
                noticias.setChecked(true);
                esDetalle = true;
            } else {
                gone(lyChat);
            }
            if (perfil == null) {
                perfil = tipo;
            }
            getFirebaseFormBase();
            idChat = bundle.getString(CAMPO_ID);
            id = bundle.getString(USERID);
            System.out.println("id = " + id);
            System.out.println("idChat = " + idChat);
        }
    }

    @Override
    protected void setLayoutExtra() {
        super.setLayoutExtra();
        layoutCabecera = R.layout.cabecera_elegir_ubicacion;


    }

    @Override
    public void onPause() {
        super.onPause();
        AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, NULL);

    }

    protected void selector() {

        if (tipo.equals(PRODMULTI)) {
            visible(multitxt);
        }
        setNombreVoto();
        lista = new ArrayList();
        maestroDetalle();

        if (esDetalle) {
            setDatos();
            if (!tipoForm.equals(NUEVO)) {

                recuperarVotoUsuario(ratingBarUser, contexto, tipoRating, idRating);
                visible(noticias);
                AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, id);
            } else {
                AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, NULL);

            }
            if (!nuevo) {
                if (nn(firebaseFormBase)) {
                    idRating = setIdRating();
                    tipoRating = setTipoRating();
                    recuperarVotos(ratingBar, tipoRating, idRating);
                    recuperarComentarios(tipoRating, idRating);
                }
            }

            gone(lyvoto);
            onSetDatos();
        } else {
            AndroidUtil.setSharePreference(contexto, PREFERENCIAS, IDCHATF, NULL);
        }

        if (subTitulo == null) {
            activityBase.toolbar.setSubtitle(perfilUser);
        }

        activityBase.toolbar.setTitle(titulo);


        acciones();

    }

    protected void onSetDatos() {

        if (web != null && JavaUtil.isValidURL(web)) {

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

        if (tipoForm.equals(NUEVO)) {

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();


            mapa = new MapUtil(R.id.map, icFragmentos, activityBase);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, (int) ((double) altoReal / 2)
            );
            lyMap.setLayoutParams(lp);
            mapa.setOnReadyMap(new MapUtil.OnReadyMap() {
                @Override
                public void onReady(final GoogleMap googleMap) {

                    final GoogleMap gMap = googleMap;

                    setMarcadores();

                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {

                            frameAnimationCuerpo.setActivo(true);
                            scrollDetalle.setScrollingEnabled(true);
                            ArrayList<Marker> listaMarker = mapa.getListaMarkers();
                            for (Marker marker : listaMarker) {
                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            }

                        }
                    });

                    googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng latLng) {
                            frameAnimationCuerpo.setActivo(false);
                            scrollDetalle.setScrollingEnabled(false);
                            ArrayList<Marker> listaMarker = mapa.getListaMarkers();
                            for (Marker marker : listaMarker) {
                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                            }
                        }
                    });
                    mapa.setMyLocation(true);
                    googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {

                            clickLocation = true;

                            gMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                                @Override
                                public void onCameraIdle() {

                                    if (clickLocation) {
                                        latUser = (long) (gMap.getCameraPosition().target.latitude * 1000);
                                        lonUser = (long) (gMap.getCameraPosition().target.longitude * 1000);
                                        clickLocation = false;
                                    }

                                }
                            });

                            gMap.animateCamera(CameraUpdateFactory.zoomTo(5));

                            return false;
                        }
                    });

                    googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

                        @Override
                        public void onMarkerDragStart(Marker marker) {
                            frameAnimationCuerpo.setActivo(false);
                            scrollDetalle.setScrollingEnabled(false);
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {

                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {

                            if (tipoForm.equals(NUEVO)) {
                                marcador = setMarcador(marker);
                                long latitud = (long) (marker.getPosition().latitude * 1000);
                                long longitud = (long) (marker.getPosition().longitude * 1000);
                                marcador.setLatitud(latitud);
                                marcador.setLongitud(longitud);
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                for (String s : zonaList) {
                                    db.child(LUGARES + marcador.getTipo()).child(s).child(marcador.getId()).removeValue();
                                }
                                updateMarcador(marcador);
                                setLugarZona();

                                frameAnimationCuerpo.setActivo(true);
                                scrollDetalle.setScrollingEnabled(true);
                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                            }
                        }
                    });

                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            if (marcador == null) {

                                marcador = setMarcador(marker);
                                alcance = marcador.getAlcance();
                                setAlcance(marcador);
                                setLugarZona();
                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

                                return true;

                            } else {

                                if (!marker.getId().equals(marcador.getIdMark())) {
                                    Marker mark = getMarker(marcador, mapa);
                                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                                    mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                    marcador = setMarcador(marker);
                                    alcance = marcador.getAlcance();
                                    setAlcance(marcador);
                                    setLugarZona();

                                    return true;

                                } else {

                                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                                    return true;

                                }
                            }
                        }
                    });

                }
            });
        }


    }

    protected void elegirUbicacion() {

        mapaCab = new MapUtil(R.id.mapcap, icFragmentos, activityBase);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, (int) ((double) altoReal / 2)
        );
        lyMapCab.setLayoutParams(lp);
        if (marcador == null) {
            marcador = new Marcador();
        }
        mapaCab.setOnReadyMap(new MapUtil.OnReadyMap() {


            @Override
            public void onReady(final GoogleMap googleMap) {

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        frameAnimationCuerpo.setActivo(true);
                        scrollDetalle.setScrollingEnabled(true);

                    }
                });

                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        frameAnimationCuerpo.setActivo(false);
                        scrollDetalle.setScrollingEnabled(false);

                    }
                });
                mapaCab.setMyLocation(true);
                googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {

                        clickLocation = true;

                        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                            @Override
                            public void onCameraIdle() {

                                if (clickLocation) {
                                    long latitud = (long) (googleMap.getCameraPosition().target.latitude * 1000);
                                    long longitud = (long) (googleMap.getCameraPosition().target.longitude * 1000);
                                    marcador.setLatitud(latitud);
                                    marcador.setLongitud(longitud);
                                    Marker mark = mapaCab.crearMarcadorMap(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude,
                                            6, getString(R.string.mi_ubicacion), "", true);

                                    mapaCab.moverMarcador(mark, googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude);
                                    clickLocation = false;
                                    Address paisUser = (LocalizacionUtils.getAddressGeoCoord(contexto, googleMap.getCameraPosition().target.latitude,
                                            googleMap.getCameraPosition().target.longitude));
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                    db.child(USERS).child(idUser).child(LOCALIZACION).setValue(addressToList(paisUser));
                                }

                            }
                        });

                        return false;
                    }
                });

                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

                    @Override
                    public void onMarkerDragStart(Marker marker) {
                        frameAnimation.setActivo(false);
                        scrollCab.setScrollingEnabled(false);
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {

                        long latitud = (long) (marker.getPosition().latitude * 1000);
                        long longitud = (long) (marker.getPosition().longitude * 1000);
                        marcador.setLatitud(latitud);
                        marcador.setLongitud(longitud);

                        Address paisUser = (LocalizacionUtils.getAddressGeoCoord(contexto, ((double) latitud / 1000), ((double) longitud / 1000)));
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        db.child(USERS).child(idUser).child(LOCALIZACION).setValue(addressToList(paisUser));

                        frameAnimation.setActivo(true);
                        scrollCab.setScrollingEnabled(true);
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    }
                });


            }
        });
    }

    protected void setMarcadores() {

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                listaMarcadores = new ArrayList<>();

                if (id != null) {
                    DatabaseReference dbMarc = FirebaseDatabase.getInstance().getReference().
                            child(INDICEMARC + tipo).child(id);

                    dbMarc.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot child : dataSnapshot.getChildren()) {

                                DatabaseReference dbMarc = FirebaseDatabase.getInstance().getReference().
                                        child(MARC + tipo).child(child.getKey());

                                dbMarc.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                                        activityBase.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                Marcador marc = dataSnapshot.getValue(Marcador.class);
                                                Marker marker = mapa.crearMarcadorMap((double) (marc.getLatitud() / 1000), (double) (marc.getLongitud() / 1000), 5, "", "", true);
                                                marc.setIdMark(marker.getId());
                                                for (Marcador marcadore : listaMarcadores) {
                                                    Marker mark = getMarker(marcadore, mapa);
                                                    mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                                }
                                                listaMarcadores.add(marc);
                                                marcador = setMarcador(marker);
                                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                                                mapa.moverCamara(marker.getPosition().latitude, marker.getPosition().longitude, 5, true, true);
                                                alcance = marcador.getAlcance();
                                                setAlcance(marcador);
                                                setLugarZona();
                                                StringBuilder textoZona = new StringBuilder();
                                                for (String s : zonaList) {
                                                    textoZona.append(s).append(" ");
                                                }
                                                zona.setText(textoZona.toString());

                                            }
                                        });

                                        //}
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
        th.start();

    }

    protected void contarSuscritos() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query querydb = db.child(SUSCRIPCIONES).child(id).orderByKey();

        querydb.addValueEventListener(new ValueEventListener() {

            private int nunSuscriptores;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    for (DataSnapshot childChild : child.getChildren()) {
                        nunSuscriptores++;
                        suscritos.setText(String.valueOf(nunSuscriptores));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void comprobarSuscripcion() {

        if (nn(id)) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            Query querydb = db.child(SUSCRIPCIONES).child(id).child(perfilUser).child(idUser);

            querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (nn(dataSnapshot.getValue())) {
                        suscripcion.setChecked(dataSnapshot.getValue(boolean.class));
                    } else {
                        suscripcion.setChecked(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    protected Marker getMarker(Marcador marcador, MapUtil mapa) {

        for (Marker marker : mapa.getListaMarkers()) {
            if (marker.getId().equals(marcador.getIdMark())) {
                return marker;
            }
        }

        return null;
    }

    public void setAlcance(Marcador marcador) {

        switch (marcador.getAlcance()) {

            case 0:
                radioButtonMap.setChecked(true);
                break;
            case 1:
                radioButtonMap1.setChecked(true);
                break;
            case 2:
                radioButtonMap2.setChecked(true);
                break;
            case 3:
                radioButtonMap3.setChecked(true);
                break;
            case 4:
                radioButtonMap4.setChecked(true);
                break;
            case 5:
                radioButtonMap5.setChecked(true);
                break;
        }

        if (zonaList != null) {
            StringBuilder textoZona = new StringBuilder();
            for (String s : zonaList) {
                textoZona.append(s).append(" ");
            }
            zona.setText(textoZona.toString());
        }
    }

    protected int getAlcance() {

        if (radioButtonMapCab.isChecked()) {
            return 0;
        } else if (radioButtonMap1Cab.isChecked()) {
            return 1;
        } else if (radioButtonMap2Cab.isChecked()) {
            return 2;
        } else if (radioButtonMap3Cab.isChecked()) {
            return 3;
        } else if (radioButtonMap4Cab.isChecked()) {
            return 4;
        } else if (radioButtonMap5Cab.isChecked()) {
            return 5;
        }

        return 0;
    }

    protected boolean comprobarZona(String zona) {

        for (String s : zonaList) {
            if (s.equals(zona)) {
                return false;
            }
        }
        return true;
    }

    protected void setLugarZona() {

        long latitud = marcador.getLatitud();
        long longitud = marcador.getLongitud();
        String country;
        String region;
        String provincia;
        String ciudad;
        String postalCode;
        List<Address> direcciones;
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        direcciones = LocalizacionUtils.getAddressListGeoCoord(contexto,
                (double) (latitud) / 1000, (double) (longitud) / 1000);
        if (direcciones != null && direcciones.size() > 0) {

            zonaList = new ArrayList<>();
            for (Address address : direcciones) {

                ciudad = address.getLocality();
                provincia = address.getSubAdminArea();
                region = address.getAdminArea();
                country = address.getCountryName();
                postalCode = address.getPostalCode();

                if (radioButtonMap.isChecked()) {
                    alcance = 0;
                    zonaList.add(MUNDIAL);
                    zona.setText(MUNDIAL);
                    marcador.setAlcance(alcance);
                    updateMarcador(marcador);
                    db.child(LUGARES + marcador.getTipo()).child(zonaList.get(0)).child(marcador.getId()).setValue(true);
                    return;

                } else if (radioButtonMap1.isChecked()) {
                    alcance = 1;
                    if (comprobarZona(country.toLowerCase())) {
                        zonaList.add(country.toLowerCase());
                    }
                } else if (radioButtonMap2.isChecked()) {
                    alcance = 2;
                    if (comprobarZona(region.toLowerCase())) {
                        zonaList.add(region.toLowerCase());
                    }
                } else if (radioButtonMap3.isChecked()) {
                    alcance = 3;
                    if (comprobarZona(provincia.toLowerCase())) {
                        zonaList.add(provincia.toLowerCase());
                    }
                } else if (radioButtonMap4.isChecked()) {
                    alcance = 4;
                    if (comprobarZona(ciudad.toLowerCase())) {
                        zonaList.add(ciudad.toLowerCase());
                    }
                } else if (radioButtonMap5.isChecked()) {
                    alcance = 5;
                    if (comprobarZona(postalCode.toLowerCase())) {
                        zonaList.add(postalCode.toLowerCase());
                    }
                }

            }

            StringBuilder textoZona = new StringBuilder();

            for (String s : zonaList) {
                db.child(LUGARES + marcador.getTipo()).child(s).child(marcador.getId()).setValue(true);
                textoZona.append(s).append(" ");
            }
            zona.setText(textoZona.toString());
            marcador.setAlcance(alcance);
            updateMarcador(marcador);

        }

    }

    protected ArrayList<String> addressToList(Address direccion) {


        if (direccion != null) {

            ArrayList<String> listaLocal = new ArrayList<>();

            String country = direccion.getCountryName();
            if (nn(country)) {
                listaLocal.add(country);
            }
            String region = direccion.getAdminArea();
            if (nn(region)) {
                listaLocal.add(region);
            }
            String provincia = direccion.getSubAdminArea();
            if (nn(provincia)) {
                listaLocal.add(provincia);
            }
            String ciudad = direccion.getLocality();
            if (nn(ciudad)) {
                listaLocal.add(ciudad);
            }
            String postalCode = direccion.getPostalCode();
            if (nn(postalCode)) {
                listaLocal.add(postalCode);
            }

            return listaLocal;
        }

        return null;

    }

    protected Marcador setMarcador(Marker marker) {

        for (Marcador marcador : listaMarcadores) {
            if (marcador.getIdMark().equals(marker.getId())) {
                return marcador;
            }
        }
        return null;
    }

    protected Marcador crearMarcador(MapUtil mapa, String tipo, String id, long latitud, long longitud) {

        Marcador marc = new Marcador();
        Marker mark = mapa.crearMarcadorMap((double) ((latitud) / 1000), (double) ((longitud) / 1000), 5, "", "", true);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        String key = db.child(MARC + tipo).push().getKey();
        marc.setKey(key);
        marc.setId(id);
        marc.setIdMark(mark.getId());
        marc.setLatitud(latitud);
        marc.setLongitud(longitud);
        marc.setTipo(tipo);
        marc.setActivo(true);

        db.child(MARC + tipo).child(key).setValue(marc);
        db.child(INDICEMARC + tipo).child(id).child(key).setValue(true);

        listaMarcadores.add(marc);
        mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        return marc;

    }


    protected void updateMarcador(Marcador marcador) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        if (db != null) {
            db.child(MARC + marcador.getTipo()).child(marcador.getKey()).setValue(marcador);
            for (Marcador marcadore : listaMarcadores) {
                if (marcadore.getIdMark().equals(marcador.getIdMark())) {
                    listaMarcadores.set(listaMarcadores.indexOf(marcadore), marcador);
                    break;
                }
            }
        }

    }

    protected Marcador deleteMarcador(Marcador marcador, MapUtil mapa) {


        final String tipo = marcador.getTipo();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        this.marcador = marcador;
        setLugarZona();
        if (db != null) {
            if (zonaList != null) {
                for (String s : zonaList) {
                    db.child(LUGARES + tipo).child(s).child(marcador.getId()).removeValue();
                }
            }
            db.child(MARC + marcador.getTipo()).child(marcador.getKey()).removeValue();
            db.child(INDICEMARC + tipo).child(marcador.getId()).child(marcador.getKey()).removeValue();
        }

        Marker marker = getMarker(marcador, mapa);
        mapa.borrarMarcador(marker);

        for (Marcador marcadore : listaMarcadores) {
            if (marcadore.getIdMark().equals(marcador.getIdMark())) {
                listaMarcadores.remove(marcadore);
                break;
            }
        }

        return null;

    }

    protected void getFirebaseFormBase() {

        idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);

        if (!idUser.equals(NULL)) {

            DatabaseReference dbFirebase = FirebaseDatabase.getInstance().getReference()
                    .child(perfil).child(idUser);

            ValueEventListener eventListenerProd = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    firebaseFormBase = dataSnapshot.getValue(FirebaseFormBase.class);

                    if (firebaseFormBase == null && !tipo.equals(PRODMULTI) && tipoForm.equals(NUEVO)) {
                        Toast.makeText(contexto, getString(R.string.debe_tener_perfil) + perfil, Toast.LENGTH_SHORT).show();
                        rv.setBackground(getResources().getDrawable(R.drawable.alert_box_r));
                    } else if (nn(firebaseFormBase) && tipoForm.equals(NUEVO)) {
                        nombreChat = firebaseFormBase.getNombreBase();
                        setRv();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            dbFirebase.addValueEventListener(eventListenerProd);

        }

    }

    protected abstract String setTipoRating();

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
                enviarVoto(contexto, tipoRating, idRating, votoUser, comentario.getTexto());
            }
        });

        verVoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lyvoto.getVisibility() == View.VISIBLE) {
                    gone(lyvoto);
                } else {
                    visible(lyvoto);
                    recuperarVotoUsuario(ratingBarUser, contexto, tipoRating, idRating);
                }

            }
        });

        verComents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rlcoment.getVisibility() == View.VISIBLE) {
                    gone(rlcoment);
                } else {

                    recuperarComentarios(tipoRating, idRating);
                    visible(rlcoment);

                }
            }
        });

        if (tipoForm.equals(NUEVO)) {

            activityBase.fabNuevo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    nuevo = true;
                    esDetalle = true;
                    selector();
                }
            });

            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    guardar();
                }
            });

            crearMarc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    marcador = crearMarcador(mapa, tipo, id, latUser, lonUser);
                    setLugarZona();

                }
            });

            borraMarc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    marcador = deleteMarcador(marcador, mapa);
                }
            });

            radioGroupMap.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    if (tipoForm.equals(NUEVO) && marcador != null && zonaList != null) {

                        for (String s : zonaList) {
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                            db.child(LUGARES + marcador.getTipo()).child(s).child(marcador.getId()).removeValue();
                        }
                        setLugarZona();
                    } else if (tipoForm.equals(LISTA)) {
                        selector();
                    }
                }
            });

            opcionesZona.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (opcionesZona.isChecked()) {
                        visible(radioGroupMap);
                        visible(crearMarc);
                        visible(borraMarc);
                        visible(lyMap);
                        visible(zona);

                    } else {
                        gone(radioGroupMap);
                        gone(crearMarc);
                        gone(borraMarc);
                        gone(lyMap);
                        gone(zona);
                    }
                }
            });

            btnEnviarNoticias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("id not= " + id);
                    bundle = new Bundle();
                    putBundle(CAMPO_ID, AndroidUtil.getSharePreference(contexto, CHAT, tipo, NULL));
                    putBundle(TIPO, tipo);
                    putBundle(IDCHATF, id);
                    putBundle(NOMBRECHAT, nombreChat);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentChatProductoEnviar());
                }
            });

        } else if (tipoForm.equals(LISTA)) {


            verMapaCab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (verMapaCab.isChecked()) {
                        visible(lyMapCab);
                        visible(opcionesZonaCab);
                        elegirUbicacion();
                    } else {
                        gone(lyMapCab);
                        gone(opcionesZonaCab);
                        gone(radioGroupMapCab);
                        gone(miUbicacionDef);
                        opcionesZonaCab.setChecked(false);
                    }
                }
            });

            opcionesZonaCab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (opcionesZonaCab.isChecked()) {
                        visible(radioGroupMapCab);
                        visible(miUbicacionDef);

                    } else {
                        gone(radioGroupMapCab);
                        gone(miUbicacionDef);
                    }
                }
            });

            miUbicacionDef.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    AndroidUtil.setSharePreference(contexto, PREFERENCIAS, MIUBICACION, b);

                    if (b) {
                        localizacionUtils = new LocalizacionUtils();
                        localizacionUtils.toggleBestUpdates(true, activityBase);
                        localizacionUtils.setOnLocalizationChange(new LocalizacionUtils.OnBestLocalizationChange() {
                            @Override
                            public void onBestLocalizationChange(double lat, double lon) {
                                Address paisUser = LocalizacionUtils.getAddressGeoCoord(contexto, lat, lon);
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                db.child(USERS).child(idUser).child(LOCALIZACION).setValue(addressToList(paisUser));
                            }
                        });
                    } else {
                        localizacionUtils.toggleBestUpdates(false, activityBase);
                    }
                }
            });

            suscripcion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

                    if (nn(firebaseFormBase)) {
                        if (b && id != null) {

                            db.child(SUSCRIPCIONES).child(id).
                                    child(perfilUser).child(idUser).setValue(true);

                        } else if (!b && id != null) {
                            db.child(SUSCRIPCIONES).child(id).child(perfilUser).child(idUser).removeValue();
                        }
                    } else {
                        Toast.makeText(contexto, "Debe tener un perfil de " + perfilUser
                                + " con el nombre o seudonimo como mínimo para poder suscribirse", Toast.LENGTH_SHORT).show();
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
                    System.out.println("nombreVoto = " + nombreVoto);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            dbFirebase.addValueEventListener(eventListenerProd);

        }

    }

    public void enviarVoto(Context contexto, final String tipo, final String id, float valor, String comentario) {

        perfilUser = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
        idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);

        if (nombreVoto != null) {

            Rating rat = new Rating(valor, perfilUser, id, idUser, nombreVoto, comentario, TimeDateUtil.ahora());
            FirebaseDatabase.getInstance().getReference().child("rating").child(tipo).child(id).child(idUser + perfilUser).setValue(rat, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    recuperarVotos(ratingBar, tipo, id);
                }
            });
        } else {
            Toast.makeText(contexto, "Debe tener un perfil de " + perfilUser + " para votar", Toast.LENGTH_SHORT).show();
        }

    }

    protected void recuperarVotos(final RatingBar ratingBar, final String tipo, final String id) {

        ratingBar.setRating(0.0f);
        Drawable progressDrawable = ratingBar.getProgressDrawable();
        if (progressDrawable != null) {
            DrawableCompat.setTint(progressDrawable,
                    contexto.getResources().getColor(R.color.color_star_defecto));
        }

        if (id != null && tipo != null) {
            db = FirebaseDatabase.getInstance().getReference().child("rating").child(tipo).child(id);


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

    public void recuperarVotoUsuario(final RatingBar ratingBarUser, final Context contexto, final String tipo, final String id) {

        idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);
        perfilUser = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);

        votoUser = 0.0f;
        comentario.setText("");
        ratingBarUser.setRating(votoUser);

        if (id != null && tipo != null) {
            db = FirebaseDatabase.getInstance().getReference().child("rating").child(tipo).child(id).child(idUser + perfilUser);


            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    Rating rat = dataSnapshot.getValue(Rating.class);

                    if (rat != null) {
                        votoUser = rat.getValor();
                        comentario.setText(rat.getComentario());
                    }

                    voto.setText(String.valueOf(votoUser));
                    ratingBarUser.setRating(votoUser);
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

    protected void recuperarComentarios(final String tipo, final String id) {


        if (id != null && tipo != null) {
            db = FirebaseDatabase.getInstance().getReference().child("rating").child(tipo).child(id);


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
        if (posicion > 0) {
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
    protected void guardarImagen() {
        super.guardarImagen();

        if (nn(path)) {


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child(id + tipo);
            UploadTask uploadTask = storageRef.putStream(imagen.getInputStreamAuto(path));

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(contexto, "Imagen subida OK", Toast.LENGTH_SHORT).show();
                    if (id != null && !id.equals("") && !id.equals(NULL)) {

                        //setImagenFireStore(contexto, idUser, imagen);
                        imagen.setImageFirestore(id + tipo);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(contexto, "Fallo al subir imagen", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    protected void accionesImagen() {

        imagen.setVisibleBtn();

        if (tipoForm.equals(NUEVO)) {
            if (id != null) {
                visible(imagen);
                imagen.setImageFirestorePerfil(activityBase, id + tipo);

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

            imagen.setImageFirestorePerfil(activityBase, id + tipo);


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

    public class ViewHolderRVMsgChat extends BaseViewHolder implements TipoViewHolder {

        TextView mensaje, fecha;
        CardView card;

        public ViewHolderRVMsgChat(View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.tvmsgchat_base);
            fecha = itemView.findViewById(R.id.tvmsgchatfecha_base);
            card = itemView.findViewById(R.id.cardmsgchat_base);

        }

        @Override
        public void bind(Modelo modelo) {

            int tipo = modelo.getInt(DETCHAT_TIPO);

            mensaje.setText(modelo.getString(DETCHAT_MENSAJE));
            fecha.setText(TimeDateUtil.getDateTimeString(modelo.getLong(DETCHAT_FECHA)));

            if (tipo == ENVIADO) {

                card.setCardBackgroundColor(getResources().getColor(R.color.Color_msg_enviado));
                card.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                params.setMargins((int) (double) (ancho * densidad) / 5, (int) (10 * densidad), (int) (10 * densidad), 0);

                card.setLayoutParams(params);


                visible(card);

            } else if (tipo == RECIBIDO) {

                card.setCardBackgroundColor(getResources().getColor(R.color.Color_msg_recibido));
                card.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                params.setMargins((int) (10 * densidad), (int) (10 * densidad), (int) (double) (ancho * densidad) / 5, 0);

                card.setLayoutParams(params);

                visible(card);

            } else {
                gone(card);

            }

            super.bind(modelo);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVMsgChat(view);
        }
    }

    protected String setWeb() {
        return null;
    }
}