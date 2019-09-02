package com.jjlacode.base.util.nosql;

import android.content.ContentValues;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.jjlacode.base.util.Models.FirebaseFormBase;
import com.jjlacode.base.util.Models.Marcador;
import com.jjlacode.base.util.adapter.BaseViewHolder;
import com.jjlacode.base.util.adapter.ListaAdaptadorFiltro;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.base.util.android.controls.ImagenLayout;
import com.jjlacode.base.util.android.controls.LockableScrollView;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.crud.ListaModelo;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.localizacion.LocalizacionUtils;
import com.jjlacode.base.util.localizacion.MapUtil;
import com.jjlacode.base.util.media.ImagenUtil;
import com.jjlacode.base.util.sqlite.ContratoPry;
import com.jjlacode.base.util.time.TimeDateUtil;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.logica.Interactor;
import com.jjlacode.freelanceproject.model.ProdProv;
import com.jjlacode.um.base.ui.FragmentChat;

import java.util.ArrayList;
import java.util.List;

public abstract class FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb
        extends FragmentMasterDetailNoSQLFirebaseRatingWeb implements ContratoPry.Tablas {

    protected EditMaterial nombre;
    protected EditMaterial descripcion;
    protected EditMaterial referencia;
    protected EditMaterial proveedor;
    protected EditMaterial precio;
    protected EditMaterial claves;
    protected EditMaterial etWeb;
    protected EditMaterial zona;
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
    protected ProdProv prodProv;
    protected DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    protected Query query;
    protected boolean nuevo;
    protected ImageButton btnEnviar;
    protected Switch chActivo;
    protected boolean activo;
    protected int contadorProdActivo;
    protected int contadorProdTotal;
    protected int limiteProdActivos = 100;
    protected int limiteProdTotal = 150;
    protected String id;
    protected String perfil;
    protected FirebaseFormBase firebaseFormBase;
    private LinearLayout lyMap;
    private LinearLayout lyMapCab;
    private MapUtil mapa;
    private MapUtil mapaCab;
    private Marcador marcador;
    private boolean clickLocation;
    private Address paisUser;
    private ArrayList<Marcador> listaMarcadores;
    private long latUser;
    private long lonUser;
    private int alcance;
    private ArrayList<String> zonaList;
    private LockableScrollView scrollCab;
    private ToggleButton opcionesZonaCab;
    protected Switch miUbicacionDef;
    private LocalizacionUtils localizacionUtils;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        View viewFB = inflater.inflate(R.layout.fragment_formulario_prodprov, container, false);
        if (viewFB.getParent() != null) {
            ((ViewGroup) viewFB.getParent()).removeView(viewFB); // <- fix
        }
        frdetalleExtrasante.addView(viewFB);
        View viewMap = inflater.inflate(R.layout.layout_map, container, false);
        if (viewMap.getParent() != null) {
            ((ViewGroup) viewMap.getParent()).removeView(viewMap); // <- fix
        }
        frdetalleExtrasante.addView(viewMap);

        nombre = (EditMaterial) ctrl(R.id.etnombreformprodprov);
        descripcion = (EditMaterial) ctrl(R.id.etdescformprodprov);
        referencia = (EditMaterial) ctrl(R.id.etrefformprodprov);
        proveedor = (EditMaterial) ctrl(R.id.etproveedorformprodprov);
        precio = (EditMaterial) ctrl(R.id.etprecioformprodprov);
        chActivo = (Switch) ctrl(R.id.chactivoprodprov);
        claves = (EditMaterial) ctrl(R.id.etclavesformprodprov);
        etWeb = (EditMaterial) ctrl(R.id.etwebformprodprov);
        zona = (EditMaterial) ctrl(R.id.etzonaprod);
        btnEnviar = (ImageButton) ctrl(R.id.btn_enviarprod);
        imagen = (ImagenLayout) ctrl(R.id.imgformprodprov);
        imagen.setIcfragmentos(icFragmentos);
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
        zona.grabarEnable(false);

        if (tipoForm.equals(NUEVO)) {
            gone(frCabecera);
            nombre.setActivo(true);
            descripcion.setActivo(true);
            referencia.setActivo(true);
            proveedor.setActivo(true);
            precio.setActivo(true);
            etWeb.setActivo(true);
            zona.setActivo(true);
        } else {
            gone(btnEnviar);
            gone(chActivo);
        }

        tipo = setTipo();
        perfil = setPerfil();
        getFirebaseFormBase();

        miUbicacionDef.setChecked(CRUDutil.getSharePreference(contexto, PREFERENCIAS, "miUbicacion", false));

        if (miUbicacionDef.isChecked() && tipoForm.equals(LISTA)) {

            localizacionUtils = new LocalizacionUtils();
            localizacionUtils.toggleBestUpdates(true, activityBase);
            localizacionUtils.setOnLocalizationChange(new LocalizacionUtils.OnBestLocalizationChange() {
                @Override
                public void onBestLocalizationChange(double lat, double lon) {
                    paisUser = LocalizacionUtils.getAddressGeoCoord(contexto, lat, lon);
                    selector();
                }
            });
        }

    }


    @Override
    protected void setLayoutItem() {

        layoutItem = R.layout.item_list_firebase_formproducto_rating_web;


    }

    @Override
    protected void setLayout() {


    }

    @Override
    protected void setLayoutExtra() {
        super.setLayoutExtra();
        layoutCabecera = R.layout.cabecera_elegir_ubicacion;


    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRVFormProdProvRatingWeb(view);
    }

    @Override
    protected ListaAdaptadorFiltro setAdaptadorAuto(Context context, int layoutItem, ArrayList lista) {
        return new AdapterFiltroFormProdProvRatingWeb(context, layoutItem, lista);
    }

    @Override
    protected void setLista() {

        if (tipoForm.equals(LISTA) && paisUser != null) {

            lista = new ArrayList<ProdProv>();

            String lugar = "";

            for (int i = 5; i >= getAlcance(); i--) {

                switch (i) {

                    case 5:
                        lugar = paisUser.getPostalCode();
                        break;
                    case 4:
                        lugar = paisUser.getLocality().toLowerCase();
                        break;
                    case 3:
                        lugar = paisUser.getSubAdminArea().toLowerCase();
                        break;
                    case 2:
                        lugar = paisUser.getAdminArea().toLowerCase();
                        break;
                    case 1:
                        lugar = paisUser.getCountryName().toLowerCase();
                        break;
                    case 0:
                        lugar = "mundial";
                        break;

                }

                System.out.println("lugar = " + lugar);

                db = FirebaseDatabase.getInstance().getReference().
                        child("lugares" + tipo).child(lugar);

                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot prod : dataSnapshot.getChildren()) {

                            if (prod.getValue(Boolean.class)) {

                                DatabaseReference dbproductosprov = FirebaseDatabase.getInstance().getReference().
                                        child(tipo).child(prod.getKey());

                                dbproductosprov.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {


                                        ProdProv prodProv = dataSnapshot2.getValue(ProdProv.class);
                                        lista.add(prodProv);

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

            gone(btnEnviar);
            gone(chActivo);
            gone(verVoto);
            gone(frCabecera);

            lista = new ArrayList<ProdProv>();

            db = FirebaseDatabase.getInstance().getReference().
                    child("indice" + tipo).child(idUser);

            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot prod : dataSnapshot.getChildren()) {

                        contadorProdTotal++;

                        if ((boolean) prod.getValue()) {
                            contadorProdActivo++;

                            db = FirebaseDatabase.getInstance().getReference().
                                    child(tipo).child(prod.getKey());

                            //query = db.orderByKey().equalTo(prod.getKey());
                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {


                                    lista.add(dataSnapshot2.getValue(ProdProv.class));

                                    setRv();

                                    if (contadorProdTotal < limiteProdTotal) {
                                        activityBase.fabNuevo.show();
                                    } else {
                                        activityBase.fabNuevo.hide();
                                    }

                                    if (contadorProdActivo < limiteProdActivos) {
                                        chActivo.setEnabled(true);
                                    } else {
                                        chActivo.setEnabled(false);
                                        chActivo.setChecked(false);
                                    }

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

            if (lista.size() == 0) {
                activityBase.fabNuevo.show();
                activityBase.fabInicio.hide();
            }

        }

        int numero = 100;
        System.out.println("Numero aleatorio hasta " + numero + " = " + Math.round((Math.random() % numero) * numero));


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

            imagen.setImageFirestorePerfil(activityBase, prodProv.getId() + tipo);


        }
    }


    @Override
    protected void guardarImagen() {

        path = CRUDutil.getSharePreference(contexto, PERSISTENCIA, PATH, path);

        if (nn(path) && nn(id)) {


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child(id + tipo);
            UploadTask uploadTask = storageRef.putStream(imagen.getInputStreamAuto(path));

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(contexto, "Imagen subida OK", Toast.LENGTH_SHORT).show();

                    imagen.setImageFirestorePerfil(activityBase, id + tipo);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(contexto, "Fallo al subir imagen", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    protected void selector() {


        setNombreVoto();
        lista = new ArrayList();
        maestroDetalle();

        if (esDetalle) {
            setDatos();
            if (!tipoForm.equals(NUEVO)) {
                idRating = setIdRating();
                tipoRating = setTipoRating();
                recuperarVotos(ratingBar, tipoRating, idRating);
                recuperarComentarios(tipoRating, idRating);
                recuperarVotoUsuario(ratingBarUser, contexto, tipoRating, idRating);
            }
            gone(lyvoto);
            onSetDatos();
        }

        if (subTitulo == null) {
            activityBase.toolbar.setSubtitle(Interactor.setNamefdef());
        }

        if (tipoForm.equals(NUEVO)) {
            activityBase.fabInicio.hide();
            activityBase.fabNuevo.show();

        } else {
            activityBase.fabNuevo.hide();
            activityBase.fabInicio.show();
        }
        acciones();

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
                                            6, "Mi ubicación", "", true);

                                    mapaCab.moverMarcador(mark, googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude);
                                    clickLocation = false;
                                    paisUser = (LocalizacionUtils.getAddressGeoCoord(contexto, googleMap.getCameraPosition().target.latitude,
                                            googleMap.getCameraPosition().target.longitude));
                                    selector();
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

                        paisUser = (LocalizacionUtils.getAddressGeoCoord(contexto, ((double) latitud / 1000), ((double) longitud / 1000)));
                        selector();
                        frameAnimation.setActivo(true);
                        scrollCab.setScrollingEnabled(true);
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    }
                });


            }
        });
    }


    protected void setDatos() {

        if (tipoForm.equals(NUEVO)) {

            visible(btnEnviar);
            visible(btndelete);
            visible(chActivo);
            visible(claves);
            visible(etWeb);
            gone(verVoto);
            zona.setActivo(false);
            visible(opcionesZona);
            gone(lyMap);

        }

        if (prodProv != null) {

            nombre.setText(prodProv.getNombre());
            referencia.setText(prodProv.getRefprov());
            proveedor.setText(prodProv.getProveedor());
            precio.setText(JavaUtil.formatoMonedaLocal(prodProv.getPrecio()));
            descripcion.setText(prodProv.getDescripcion());
            claves.setText(prodProv.getAlcance());
            web = prodProv.getWeb();
            etWeb.setText(prodProv.getWeb());
            id = prodProv.getId();

            activityBase.toolbar.setTitle(prodProv.getNombre());
            accionesImagen();

        } else if (nuevo) {

            vaciarControles();

        }

    }

    protected void onSetDatos() {

        if (web != null && JavaUtil.isValidURL(web)) {

            visible(lyweb);

            // Cargamos la web
            Thread th = new Thread() {
                public void run() {

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

                    activityBase.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

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

                        }
                    });
                }
            };
            th.start();

        } else {
            gone(lyweb);
        }

        if (tipoForm.equals(NUEVO) && !nuevo) {
            chActivo.setChecked(prodProv.isActivo());
        }

        if (tipoForm.equals(NUEVO)) {

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
                                db = FirebaseDatabase.getInstance().getReference();
                                for (String s : zonaList) {
                                    db.child("lugares" + marcador.getTipo()).child(s).child(marcador.getId()).removeValue();
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


    protected void setMarcadores() {

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                listaMarcadores = new ArrayList<>();

                DatabaseReference dbMarc = FirebaseDatabase.getInstance().getReference().
                        child("indicemarc" + tipo).child(id);

                dbMarc.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            DatabaseReference dbMarc = FirebaseDatabase.getInstance().getReference().
                                    child("marc" + tipo).child(child.getKey());

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
        });
        th.start();

    }


    @Override
    protected void acciones() {
        super.acciones();

        if (tipoForm.equals(NUEVO) && firebaseFormBase != null) {

            activityBase.fabNuevo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    nuevo = true;
                    esDetalle = true;
                    vaciarControles();
                    selector();
                }
            });

            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (id != null) {

                        db.child(tipo).child(id).removeValue();
                        db.child("indice" + tipo).child(idUser).child(id).removeValue();
                        db.child("rating").child(tipo).child(id).removeValue();
                        db.child("indicemarc" + tipo).child(id).removeValue();
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

            btnEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    prodProv = new ProdProv();
                    prodProv.setCategoria(tipo);
                    prodProv.setNombre(nombre.getTexto());
                    prodProv.setIdprov(idUser);
                    prodProv.setActivo(chActivo.isChecked());
                    prodProv.setDescripcion(descripcion.getTexto());
                    prodProv.setPrecio(JavaUtil.comprobarDouble(precio.getTexto()));
                    prodProv.setRefprov(referencia.getTexto());
                    prodProv.setProveedor(firebaseFormBase.getNombreBase());
                    prodProv.setAlcance(claves.getTexto());
                    prodProv.setWeb(etWeb.getTexto());

                    if (nuevo) {
                        id = db.child(tipo).push().getKey();
                        prodProv.setId(id);
                    } else if (id != null) {
                        prodProv.setId(id);
                    }

                    if (id != null) {

                        db.child(tipo).child(id).setValue(prodProv).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                nuevo = false;
                                esDetalle = false;
                                selector();

                            }
                        });

                        if (chActivo.isChecked()) {
                            db.child("indice" + tipo).child(idUser).child(id).setValue(true);
                        } else {
                            db.child("indice" + tipo).child(idUser).child(id).setValue(false);
                        }


                    } else {
                        Toast.makeText(contexto, "No se ha podido subir el registro", Toast.LENGTH_SHORT).show();
                    }
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
                            db.child("lugares" + marcador.getTipo()).child(s).child(marcador.getId()).removeValue();
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

                    CRUDutil.setSharePreference(contexto, PREFERENCIAS, "miUbicacion", b);

                    if (b) {
                        localizacionUtils = new LocalizacionUtils();
                        localizacionUtils.toggleBestUpdates(true, activityBase);
                        localizacionUtils.setOnLocalizationChange(new LocalizacionUtils.OnBestLocalizationChange() {
                            @Override
                            public void onBestLocalizationChange(double lat, double lon) {
                                paisUser = LocalizacionUtils.getAddressGeoCoord(contexto, lat, lon);
                                selector();
                            }
                        });
                    } else {
                        localizacionUtils.toggleBestUpdates(false, activityBase);
                    }
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
        db = FirebaseDatabase.getInstance().getReference();

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
                    zonaList.add("mundial");
                    zona.setText("Mundial");
                    marcador.setAlcance(alcance);
                    updateMarcador(marcador);
                    db.child("lugares" + marcador.getTipo()).child(zonaList.get(0)).child(marcador.getId()).setValue(true);
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
                db.child("lugares" + marcador.getTipo()).child(s).child(marcador.getId()).setValue(true);
                textoZona.append(s).append(" ");
            }
            zona.setText(textoZona.toString());
            marcador.setAlcance(alcance);
            updateMarcador(marcador);

        }

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

        db = FirebaseDatabase.getInstance().getReference();

        String key = db.child("marc" + tipo).push().getKey();
        marc.setKey(key);
        marc.setId(id);
        marc.setIdMark(mark.getId());
        marc.setLatitud(latitud);
        marc.setLongitud(longitud);
        marc.setTipo(tipo);
        marc.setActivo(true);

        db.child("marc" + tipo).child(key).setValue(marc);
        db.child("indicemarc" + tipo).child(id).child(key).setValue(true);

        listaMarcadores.add(marc);
        mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        return marc;

    }


    protected void updateMarcador(Marcador marcador) {

        if (db != null) {
            db.child("marc" + marcador.getTipo()).child(marcador.getKey()).setValue(marcador);
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
        db = FirebaseDatabase.getInstance().getReference();
        this.marcador = marcador;
        setLugarZona();
        if (db != null) {
            if (zonaList != null) {
                for (String s : zonaList) {
                    db.child("lugares" + tipo).child(s).child(marcador.getId()).removeValue();
                }
            }
            db.child("marc" + marcador.getTipo()).child(marcador.getKey()).removeValue();
            db.child("indicemarc" + tipo).child(marcador.getId()).child(marcador.getKey()).removeValue();
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

    @Override
    protected String setTipoRating() {
        return prodProv.getCategoria();
    }

    @Override
    protected String setIdRating() {
        return prodProv.getId();
    }

    protected abstract String setTipo();

    protected abstract String setPerfil();

    @Override
    public void setOnClickRV(Object object) {

        prodProv = (ProdProv) object;
        id = prodProv.getId();
        esDetalle = true;
        selector();

    }

    protected void getFirebaseFormBase() {

        idUser = CRUDutil.getSharePreference(contexto, USERID, USERID, NULL);

        if (!idUser.equals(NULL)) {

            DatabaseReference dbFirebase = FirebaseDatabase.getInstance().getReference()
                    .child(perfil).child(idUser);

            ValueEventListener eventListenerProd = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    firebaseFormBase = dataSnapshot.getValue(FirebaseFormBase.class);

                    if (firebaseFormBase == null && tipoForm.equals(NUEVO)) {
                        Toast.makeText(contexto, "Debe tener un perfil de " + perfil, Toast.LENGTH_SHORT).show();
                        rv.setBackground(getResources().getDrawable(R.drawable.alert_box_r));
                    } else {
                        //selector();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            dbFirebase.addValueEventListener(eventListenerProd);

        }

    }

    protected void setMaestroDetalleTabletPort() {
        maestroDetalleSeparados = true;
    }


    private class AdapterFiltroFormProdProvRatingWeb extends ListaAdaptadorFiltro {


        public AdapterFiltroFormProdProvRatingWeb(Context contexto, int R_layout_IdView, ArrayList entradas) {
            super(contexto, R_layout_IdView, entradas);

        }

        @Override
        public void onEntrada(Object entrada, View view) {

            ImageView imagen = view.findViewById(R.id.imglformprodprovratingweb);
            TextView nombre = view.findViewById(R.id.tvnomlformprodprovratingweb);
            TextView descripcion = view.findViewById(R.id.tvdesclformprodprovratingweb);
            TextView referencia = view.findViewById(R.id.tvreflformprodprovratingweb);
            TextView proveedor = view.findViewById(R.id.tvprovlformprodprovratingweb);
            TextView precio = view.findViewById(R.id.tvpreciolformprodprovratingweb);

            nombre.setText(((ProdProv) entrada).getNombre());
            descripcion.setText(((ProdProv) entrada).getDescripcion());
            referencia.setText(((ProdProv) entrada).getRefprov());
            proveedor.setText(((ProdProv) entrada).getProveedor());
            precio.setText(JavaUtil.formatoMonedaLocal(((ProdProv) entrada).getPrecio()));

            if (nn(((ProdProv) entrada).getId()) && !((ProdProv) entrada).getId().equals("")) {
                ImagenUtil.setImageFireStoreCircle(((ProdProv) entrada).getId() +
                        ((ProdProv) entrada).getCategoria(), imagen);
            }

        }

        @Override
        public List onFilter(ArrayList entradas, CharSequence constraint) {

            List suggestion = new ArrayList();

            System.out.println("constraint = " + constraint.toString());

            for (Object entrada : entradas) {

                if (((ProdProv) entrada).getNombre() != null && ((ProdProv) entrada).getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((ProdProv) entrada).getRefprov() != null && ((ProdProv) entrada).getRefprov().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((ProdProv) entrada).getDescripcion() != null && ((ProdProv) entrada).getDescripcion().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((ProdProv) entrada).getAlcance() != null && ((ProdProv) entrada).getAlcance().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                }
            }
            return suggestion;
        }
    }

    private class ViewHolderRVFormProdProvRatingWeb extends BaseViewHolder implements TipoViewHolder {

        ImageView imagen;
        TextView nombre;
        TextView descripcion;
        TextView proveedor;
        TextView referencia;
        TextView precio;
        ImageButton btnchat;
        WebView webView;
        ProgressBar progressBarWebCard;
        RatingBar ratingBarCard;
        RatingBar ratingBarUserCard;
        NestedScrollView lylweb;
        String webprod;

        public ViewHolderRVFormProdProvRatingWeb(View view) {
            super(view);

            imagen = view.findViewById(R.id.imglformprodprovratingweb);
            nombre = view.findViewById(R.id.tvnomlformprodprovratingweb);
            descripcion = view.findViewById(R.id.tvdesclformprodprovratingweb);
            referencia = view.findViewById(R.id.tvreflformprodprovratingweb);
            proveedor = view.findViewById(R.id.tvprovlformprodprovratingweb);
            precio = view.findViewById(R.id.tvpreciolformprodprovratingweb);
            webView = view.findViewById(R.id.browserweblformprodprovratingweb);
            progressBarWebCard = view.findViewById(R.id.progressBarWebCard);
            btnchat = view.findViewById(R.id.btnchatlformprodprovratingweb);
            ratingBarCard = view.findViewById(R.id.ratingBarCardformprodprovratingweb);
            ratingBarUserCard = view.findViewById(R.id.ratingBarUserCardformprodprovratingweb);

            lylweb = view.findViewById(R.id.lylwebformprodprovratingweb);

        }

        @Override
        public void bind(ArrayList<?> lista, int position) {

            final ProdProv prodProv = (ProdProv) lista.get(position);

            nombre.setText(prodProv.getNombre());
            descripcion.setText(prodProv.getDescripcion());
            proveedor.setText(prodProv.getProveedor());
            referencia.setText(prodProv.getRefprov());
            precio.setText(JavaUtil.formatoMonedaLocal(prodProv.getPrecio()));
            webprod = prodProv.getWeb();

            if (nn(prodProv.getId()) && !prodProv.getId().equals("")) {
                ImagenUtil.setImageFireStoreCircle(prodProv.getId() +
                        prodProv.getCategoria(), imagen);
            }

            if (webprod != null && JavaUtil.isValidURL(webprod)) {

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
                Thread th = new Thread() {
                    public void run() {
                        activityBase.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

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

                            }
                        });
                    }
                };
                th.start();
            } else {
                gone(lylweb);
            }

            if (!tipoForm.equals(NUEVO)) {
                btnchat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        perfilUser = CRUDutil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
                        ListaModelo listaChats = new ListaModelo(CAMPOS_CHAT);
                        String idChat = null;
                        for (Modelo chat : listaChats.getLista()) {
                            if (chat.getString(CHAT_USUARIO).equals(prodProv.getIdprov())) {
                                idChat = chat.getString(CHAT_ID_CHAT);
                            }
                        }
                        if (idChat == null) {
                            ContentValues values = new ContentValues();
                            values.put(CHAT_NOMBRE, firebaseFormBase.getNombreBase());
                            values.put(CHAT_USUARIO, firebaseFormBase.getIdchatBase());
                            values.put(CHAT_TIPO, perfil);
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
            } else {
                gone(btnchat);
                gone(ratingBarUserCard);
                gone(proveedor);
            }

            ratingBarCard.setRating(0);
            recuperarVotos(ratingBarCard, prodProv.getCategoria(), prodProv.getId());
            ratingBarCard.setIsIndicator(true);

            ratingBarUserCard.setRating(0);
            recuperarVotoUsuario(ratingBarUserCard, contexto, prodProv.getCategoria(), prodProv.getId());
            ratingBarUserCard.setIsIndicator(true);


            super.bind(lista, position);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVFormProdProvRatingWeb(view);
        }
    }

}
