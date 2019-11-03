package com.codevsolution.base.localizacion;

import android.content.ContentValues;
import android.content.Context;
import android.location.Address;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.CheckPermisos;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoSystem;
import com.codevsolution.base.style.Estilos;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.IGUAL;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.PREFERENCIAS;
import static com.codevsolution.base.logica.InteractorBase.Constantes.MIUBICACION;
import static com.codevsolution.base.logica.InteractorBase.Constantes.MUNDIAL;

public class MapZona extends Fragment implements ContratoSystem.Tablas {

    private RadioGroup radioGroupMap;
    private RadioButton radioButtonMap;
    private RadioButton radioButtonMap1;
    private RadioButton radioButtonMap2;
    private RadioButton radioButtonMap3;
    private RadioButton radioButtonMap4;
    private RadioButton radioButtonMap5;
    private Button crearMarc;
    private Button borraMarc;
    private ToggleButton opcionesZona;
    private LinearLayout lyMap;
    private MapBase mapa;
    private ModeloSQL marcador;
    private boolean clickLocation;
    private ListaModeloSQL listaMarcadores;
    private long latUser;
    private long lonUser;
    private int alcance;
    private ArrayList<String> zonaList;
    private LocalizacionUtils localizacionUtils;
    private EditMaterialLayout zona;
    private boolean location;
    private String id;
    private String tipo;
    public final int LOCATION_REQUEST_CODE = 333;
    private ViewGroupLayout vistaMain;
    private FragmentBase frParent;
    private ViewGroup viewGroup;
    private Context contexto;
    private OnMarcadorEvent onMarcadorEventListener;
    private ListaModeloSQL listaZonasIdOld;
    private ListaModeloSQL listaZonasId;
    private Switch miUbicacionDef;
    private OnLocalizacionDef onLocalizacionDefListener;
    private AppCompatActivity activityBase;
    private ToggleButton verMapaCab;

    public MapZona(FragmentBase frParent, ViewGroup viewGroup, AppCompatActivity activityBase) {
        this.frParent = frParent;
        this.viewGroup = viewGroup;
        this.activityBase = activityBase;
        contexto = frParent.getContext();
        setInicio();
        acciones();
    }

    protected void setInicio() {

        vistaMain = new ViewGroupLayout(contexto, viewGroup);
        verMapaCab = (ToggleButton) vistaMain.addVista(new ToggleButton(contexto));
        verMapaCab.setText(Estilos.getString(contexto, "select_ubicacion"));
        verMapaCab.setTextOff(Estilos.getString(contexto, "select_ubicacion"));
        verMapaCab.setTextOn(Estilos.getString(contexto, "ocultar_mapa"));
        verMapaCab.setBackground(Estilos.btnSecondary(contexto));
        verMapaCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verMapaCab.isChecked()) {

                    lyMap.setVisibility(View.VISIBLE);
                    miUbicacionDef.setVisibility(View.VISIBLE);
                    opcionesZona.setVisibility(View.VISIBLE);


                } else {

                    lyMap.setVisibility(View.GONE);
                    miUbicacionDef.setVisibility(View.GONE);
                    opcionesZona.setVisibility(View.GONE);
                    zona.getLinearLayout().setVisibility(View.GONE);
                    crearMarc.setVisibility(View.GONE);
                    borraMarc.setVisibility(View.GONE);
                    radioGroupMap.setVisibility(View.GONE);
                }
            }
        });
        miUbicacionDef = (Switch) vistaMain.addVista(new Switch(contexto));
        miUbicacionDef.setText(Estilos.getString(contexto, "mi_ubicacion_por_defecto"));
        miUbicacionDef.setVisibility(View.GONE);
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
                            ArrayList<String> listaUbicaciones = addressToList(paisUser);
                            if (onLocalizacionDefListener != null) {
                                onLocalizacionDefListener.onEnable(listaUbicaciones);
                            }
                        }
                    });
                } else {
                    localizacionUtils.toggleBestUpdates(false, activityBase);
                    if (onLocalizacionDefListener != null) {
                        onLocalizacionDefListener.onDisable();
                    }
                }
            }
        });
        lyMap = (LinearLayout) vistaMain.addVista(new LinearLayout(contexto));
        setMap();
        lyMap.setVisibility(View.GONE);
        opcionesZona = (ToggleButton) vistaMain.addVista(new ToggleButton(contexto));
        opcionesZona.setText(Estilos.getString(contexto, "opciones_zona"));
        opcionesZona.setTextOff(Estilos.getString(contexto, "opciones_zona"));
        opcionesZona.setTextOn(Estilos.getString(contexto, "ocultar_opciones"));
        opcionesZona.setBackground(Estilos.btnSecondary(contexto));
        opcionesZona.setVisibility(View.GONE);
        opcionesZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (opcionesZona.isChecked()) {

                    zona.getLinearLayout().setVisibility(View.VISIBLE);
                    crearMarc.setVisibility(View.VISIBLE);
                    borraMarc.setVisibility(View.VISIBLE);
                    radioGroupMap.setVisibility(View.VISIBLE);

                } else {

                    zona.getLinearLayout().setVisibility(View.GONE);
                    crearMarc.setVisibility(View.GONE);
                    borraMarc.setVisibility(View.GONE);
                    radioGroupMap.setVisibility(View.GONE);
                }
            }
        });


        zona = vistaMain.addEditMaterialLayout(Estilos.getString(contexto, "zona"));
        zona.setActivo(false);
        zona.getLinearLayout().setVisibility(View.GONE);

        ViewGroupLayout vistaBtnZona = new ViewGroupLayout(contexto, vistaMain.getViewGroup());
        vistaBtnZona.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);

        crearMarc = vistaBtnZona.addButtonSecondary(Estilos.getString(contexto, "crear_marcador"), 1);
        crearMarc.setVisibility(View.GONE);

        borraMarc = vistaBtnZona.addButtonSecondary(Estilos.getString(contexto, "borrar_marcador"), 1);
        borraMarc.setVisibility(View.GONE);

        frParent.actualizarArrays(vistaBtnZona);

        radioGroupMap = (RadioGroup) vistaMain.addVista(new RadioGroup(contexto));
        radioGroupMap.setVisibility(View.GONE);
        radioButtonMap = new RadioButton(contexto);
        radioButtonMap.setText(Estilos.getString(contexto, "mundial"));
        radioGroupMap.addView(radioButtonMap);
        radioButtonMap1 = new RadioButton(contexto);
        radioButtonMap1.setText(Estilos.getString(contexto, "nacional"));
        radioGroupMap.addView(radioButtonMap1);
        radioButtonMap1.setChecked(true);
        radioButtonMap2 = new RadioButton(contexto);
        radioButtonMap2.setText(Estilos.getString(contexto, "regional"));
        radioGroupMap.addView(radioButtonMap2);
        radioButtonMap3 = new RadioButton(contexto);
        radioButtonMap3.setText(Estilos.getString(contexto, "provincial"));
        radioGroupMap.addView(radioButtonMap3);
        radioButtonMap4 = new RadioButton(contexto);
        radioButtonMap4.setText(Estilos.getString(contexto, "local"));
        radioGroupMap.addView(radioButtonMap4);
        radioButtonMap5 = new RadioButton(contexto);
        radioButtonMap5.setText(Estilos.getString(contexto, "codigo_postal"));
        radioGroupMap.addView(radioButtonMap5);

        frParent.actualizarArrays(vistaMain);
        listaMarcadores = new ListaModeloSQL();
        listaZonasIdOld = new ListaModeloSQL();


    }

    public void setOnMarcadorEventListener(OnMarcadorEvent onMarcadorEventListener) {
        this.onMarcadorEventListener = onMarcadorEventListener;
    }

    public void setOnLocalizacionDefListener(OnLocalizacionDef onLocalizacionDefListener) {
        this.onLocalizacionDefListener = onLocalizacionDefListener;
    }

    public void reiniciarObjetos() {
        mapa.borrarObjetos();

    }

    public void setId(String id) {
        this.id = id;

        listaMarcadores = new ListaModeloSQL();
        listaZonasIdOld = new ListaModeloSQL();
        marcador = null;
        zona.setText("");
        zonaList = new ArrayList<>();

        listaMarcadores = CRUDutil.setListaModelo(CAMPOS_MARCADOR, MARCADOR_ID_REL, id, IGUAL);
        for (ModeloSQL marcador : listaMarcadores.getLista()) {
            listaZonasIdOld.addAllLista(CRUDutil.setListaModelo(CAMPOS_ZONA, ZONA_ID_REL, marcador.getString(MARCADOR_ID_MARCADOR), IGUAL));
        }
        setMarcadores();
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    private void setMap() {

        MapBase.FragmentMap mMap = new MapBase.FragmentMap();
        boolean permiso = CheckPermisos.validarPermisos(frParent.getActivity(), ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE) ||
                CheckPermisos.validarPermisos(frParent.getActivity(), ACCESS_COARSE_LOCATION, LOCATION_REQUEST_CODE);

        if (permiso) {
            mapa = new MapBase(mMap);
            int idLayout = View.generateViewId();
            lyMap.setId(idLayout);
            frParent.getIcFragmentos().addFragment(mMap, idLayout);
            Estilos.setLayoutParams(vistaMain.getViewGroup(), lyMap, ViewGroupLayout.MATCH_PARENT, (int) ((double) frParent.getAltoReal() / 2));
        }

    }

    public void setOnReadyMap(final OnReadyMap onReadyMapListener) {

        mapa.setOnReadyMap(new MapBase.OnReadyMap() {
            @Override
            public void onReady(final GoogleMap googleMap) {

                final GoogleMap gMap = googleMap;

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        frParent.setActivoFrameAnimationCuerpo(true);
                        frParent.setScrollingDetalleEnable(true);

                        ArrayList<Marker> listaMarker = mapa.getListaMarkers();
                        if (onReadyMapListener != null) {
                            onReadyMapListener.onMapClickListener(listaMarker);
                        }
                        for (Marker marker : listaMarker) {
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        }
                        zona.setText("");

                    }
                });

                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {

                        frParent.setActivoFrameAnimationCuerpo(false);
                        frParent.setScrollingDetalleEnable(false);
                        ArrayList<Marker> listaMarker = mapa.getListaMarkers();
                        if (onReadyMapListener != null) {
                            onReadyMapListener.onMapLongClickListener(listaMarker);
                        }
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
                                    latUser = (long) (gMap.getCameraPosition().target.latitude * 100000);
                                    lonUser = (long) (gMap.getCameraPosition().target.longitude * 100000);
                                    Address paisUser = (LocalizacionUtils.getAddressGeoCoord(contexto, latUser / 100000,
                                            lonUser / 100000));
                                    if (onReadyMapListener != null) {
                                        onReadyMapListener.onMyLocationClickListener(latUser, lonUser, addressToList(paisUser));
                                    }
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

                        frParent.setActivoFrameAnimationCuerpo(false);
                        frParent.setScrollingDetalleEnable(false);
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {


                        marcador = setMarcador(marker);
                        long latitud = (long) (marker.getPosition().latitude * 100000);
                        long longitud = (long) (marker.getPosition().longitude * 100000);
                        CRUDutil.actualizarCampo(marcador, MARCADOR_LATITUD, latitud);
                        CRUDutil.actualizarCampo(marcador, MARCADOR_LONGITUD, longitud);
                        marcador = CRUDutil.updateModelo(marcador);
                        setLugarZona(marcador);
                        ListaModeloSQL listaZonasDel = zonasDel();
                        ListaModeloSQL listaZonasAdd = zonasAdd();
                        Address paisUser = (LocalizacionUtils.getAddressGeoCoord(contexto, latitud / 100000,
                                longitud / 100000));
                        if (onReadyMapListener != null) {
                            onReadyMapListener.onMarkerDragEnd(listaZonasDel, listaZonasAdd, addressToList(paisUser));
                        }

                        frParent.setActivoFrameAnimationCuerpo(true);
                        frParent.setScrollingDetalleEnable(true);
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        if (marcador == null) {

                            marcador = setMarcador(marker);
                            setAlcanceRadioGroup(marcador);
                            setLugarZona(marcador);
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

                            return false;

                        } else {

                            System.out.println("marker.getId() = " + marker.getId());
                            System.out.println("marcador.getLong(MARCADOR_ID_MARK) = " + marcador.getString(MARCADOR_ID_MARK));
                            if (!marker.getId().equals(marcador.getString(MARCADOR_ID_MARK))) {
                                Marker mark = getMarker(marcador);
                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                                if (mark != null) {
                                    mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                }
                                marcador = setMarcador(marker);
                                setAlcanceRadioGroup(marcador);
                                setLugarZona(marcador);

                                return false;

                            }
                        }
                        return false;
                    }
                });

            }
        });


    }

    private ListaModeloSQL zonasAdd() {

        ListaModeloSQL listaZonasAdd = new ListaModeloSQL();
        listaZonasId = new ListaModeloSQL();

        for (ModeloSQL marcador : listaMarcadores.getLista()) {

            ListaModeloSQL listaZonasMarcador = CRUDutil.setListaModelo(CAMPOS_ZONA, ZONA_ID_REL, marcador.getString(MARCADOR_ID_MARCADOR), IGUAL);
            for (ModeloSQL zona : listaZonasMarcador.getLista()) {
                listaZonasId.addModelo(zona);
            }
        }

        for (ModeloSQL zonaId : listaZonasId.getLista()) {
            boolean nuevo = true;
            for (ModeloSQL zonaIdOld : listaZonasIdOld.getLista()) {
                if (zonaIdOld.getString(ZONA_NOMBRE).equals(zonaId.getString(ZONA_NOMBRE))) {
                    nuevo = false;
                }
            }
            if (nuevo) {
                listaZonasAdd.addModelo(zonaId);
            }
        }

        return listaZonasAdd;
    }

    private ListaModeloSQL zonasDel() {

        ListaModeloSQL listaZonasDel = new ListaModeloSQL();
        listaZonasId = new ListaModeloSQL();

        for (ModeloSQL marcador : listaMarcadores.getLista()) {

            ListaModeloSQL listaZonasMarcador = CRUDutil.setListaModelo(CAMPOS_ZONA, ZONA_ID_REL, marcador.getString(MARCADOR_ID_MARCADOR), IGUAL);
            for (ModeloSQL zona : listaZonasMarcador.getLista()) {
                listaZonasId.addModelo(zona);
            }
        }

        for (ModeloSQL zonaIdOld : listaZonasIdOld.getLista()) {
            boolean nuevo = true;
            for (ModeloSQL zonaId : listaZonasId.getLista()) {
                if (zonaIdOld.getString(ZONA_NOMBRE).equals(zonaId.getString(ZONA_NOMBRE))) {
                    nuevo = false;
                }

            }
            if (nuevo) {
                listaZonasDel.addModelo(zonaIdOld);
            }
        }

        return listaZonasDel;
    }

    protected void acciones() {

        crearMarc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (id != null) {
                    System.out.println("latUser = " + latUser);
                    System.out.println("lonUser = " + lonUser);
                    marcador = crearMarcador(tipo, id, latUser, lonUser);
                    setLugarZona(marcador);

                } else {
                    Toast.makeText(contexto, "Debe introducir un nombre o seudonimo y guardar el registro, antes de crear la primera zona", Toast.LENGTH_SHORT).show();
                }

            }
        });

        borraMarc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                marcador = deleteMarcador(marcador);
            }
        });

        radioGroupMap.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (marcador != null && zonaList != null &&
                        marcador.getString(MARCADOR_TIPO) != null && marcador.getString(MARCADOR_ID_REL) != null) {

                    setLugarZona(marcador);
                }
            }
        });

        opcionesZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (opcionesZona.isChecked()) {
                    radioGroupMap.setVisibility(View.VISIBLE);
                    crearMarc.setVisibility(View.VISIBLE);
                    borraMarc.setVisibility(View.VISIBLE);
                    lyMap.setVisibility(View.VISIBLE);
                    zona.getLinearLayout().setVisibility(View.VISIBLE);

                } else {
                    radioGroupMap.setVisibility(View.GONE);
                    crearMarc.setVisibility(View.GONE);
                    borraMarc.setVisibility(View.GONE);
                    lyMap.setVisibility(View.GONE);
                    zona.getLinearLayout().setVisibility(View.GONE);
                }
            }
        });

    }

    protected Marker getMarker(ModeloSQL marcador) {

        for (Marker marker : mapa.getListaMarkers()) {
            if (marker.getId().equals(marcador.getString(MARCADOR_ID_MARK))) {
                return marker;
            }
        }

        return null;
    }

    public void setAlcanceRadioGroup(ModeloSQL marcador) {

        String alcanceTxt = "";
        switch (marcador.getInt(MARCADOR_ALCANCE)) {

            case 0:
                radioButtonMap.setChecked(true);
                alcanceTxt = Estilos.getString(contexto, "mundial");
                break;
            case 1:
                radioButtonMap1.setChecked(true);
                alcanceTxt = Estilos.getString(contexto, "nacional");
                break;
            case 2:
                radioButtonMap2.setChecked(true);
                alcanceTxt = Estilos.getString(contexto, "regional");
                break;
            case 3:
                radioButtonMap3.setChecked(true);
                alcanceTxt = Estilos.getString(contexto, "provincial");
                break;
            case 4:
                radioButtonMap4.setChecked(true);
                alcanceTxt = Estilos.getString(contexto, "local");
                break;
            case 5:
                radioButtonMap5.setChecked(true);
                alcanceTxt = Estilos.getString(contexto, "codigo_postal");
                break;
        }

        if (zonaList != null) {
            StringBuilder textoZona = new StringBuilder();
            for (String s : zonaList) {
                textoZona.append(s).append(" ");
            }
            zona.setText(textoZona.toString());
            setInfoMarker(marcador, alcanceTxt, textoZona.toString());
        }
    }


    protected boolean comprobarZona(String zona) {

        for (String s : zonaList) {
            if (s.equals(zona)) {
                return false;
            }
        }
        return true;
    }

    protected boolean comprobarZona(String zona, ArrayList<String> zonaList) {

        for (String s : zonaList) {
            if (s.equals(zona)) {
                return false;
            }
        }
        return true;
    }

    protected boolean comprobarZonaLista(String s) {

        ListaModeloSQL listaZonas = CRUDutil.setListaModelo(CAMPOS_ZONA, ZONA_ID_REL, id, IGUAL);
        for (ModeloSQL zona : listaZonas.getLista()) {
            if (s.equals(zona.getString(ZONA_NOMBRE))) {
                return false;
            }
        }

        return true;
    }

    protected void setLugarZona(ModeloSQL marcador) {

        CRUDutil.borrarRegistro(TABLA_ZONA, marcador.getString(MARCADOR_ID_MARCADOR));
        long latitud = marcador.getLong(MARCADOR_LATITUD);
        long longitud = marcador.getLong(MARCADOR_LONGITUD);
        String country;
        String region;
        String provincia;
        String ciudad;
        String postalCode;
        String alcanceTxt = "";
        List<Address> direcciones;
        zonaList = new ArrayList<>();
        System.out.println("latitud = " + latitud);
        System.out.println("longitud = " + longitud);

        direcciones = LocalizacionUtils.getAddressListGeoCoord(contexto,
                (double) (latitud) / 100000, (double) (longitud) / 100000);
        System.out.println("direcciones = " + direcciones.toString());
        if (direcciones != null && direcciones.size() > 0) {


            for (Address address : direcciones) {

                ciudad = address.getLocality();
                provincia = address.getSubAdminArea();
                region = address.getAdminArea();
                country = address.getCountryName();
                postalCode = address.getPostalCode();

                if (radioButtonMap.isChecked()) {
                    alcance = 0;
                    alcanceTxt = Estilos.getString(contexto, "mundial");
                    zonaList.add(MUNDIAL);
                    zona.setText(MUNDIAL);
                    CRUDutil.actualizarCampo(marcador, MARCADOR_ALCANCE, alcance);
                    updateListaMarcadores(marcador);
                    return;

                } else if (radioButtonMap1.isChecked()) {
                    alcance = 1;
                    alcanceTxt = Estilos.getString(contexto, "nacional");
                    if (comprobarZona(country.toLowerCase())) {
                        zonaList.add(country.toLowerCase());
                    }
                } else if (radioButtonMap2.isChecked()) {
                    alcance = 2;
                    alcanceTxt = Estilos.getString(contexto, "regional");
                    if (comprobarZona(region.toLowerCase())) {
                        zonaList.add(region.toLowerCase());
                    }
                } else if (radioButtonMap3.isChecked()) {
                    alcance = 3;
                    alcanceTxt = Estilos.getString(contexto, "provincial");
                    if (comprobarZona(provincia.toLowerCase())) {
                        zonaList.add(provincia.toLowerCase());
                    }
                } else if (radioButtonMap4.isChecked()) {
                    alcance = 4;
                    alcanceTxt = Estilos.getString(contexto, "local");
                    if (comprobarZona(ciudad.toLowerCase())) {
                        zonaList.add(ciudad.toLowerCase());
                    }
                } else if (radioButtonMap5.isChecked()) {
                    alcance = 5;
                    alcanceTxt = Estilos.getString(contexto, "codigo_postal");
                    if (comprobarZona(postalCode.toLowerCase())) {
                        zonaList.add(postalCode.toLowerCase());
                    }
                }

            }

            StringBuilder textoZona = new StringBuilder();

            System.out.println("zonaList.size() = " + zonaList.size());
            for (String s : zonaList) {
                textoZona.append(s).append(" ");
                if (comprobarZonaLista(s)) {
                    ContentValues values = new ContentValues();
                    values.put(ZONA_ID_REL, marcador.getString(MARCADOR_ID_MARCADOR));
                    values.put(ZONA_NOMBRE, s);
                    CRUDutil.crearRegistro(TABLA_ZONA, values);

                }

            }
            zona.setText(textoZona.toString());
            CRUDutil.actualizarCampo(marcador, MARCADOR_ALCANCE, alcance);
            setInfoMarker(marcador, alcanceTxt, textoZona.toString());

            if (onMarcadorEventListener != null) {
                onMarcadorEventListener.onUpdateMarcador(zonasDel(), zonasAdd());
            }
            updateListaMarcadores(marcador);

        }

    }

    public void setInfoMarker(ModeloSQL marcador, String titulo, String texto) {

        System.out.println("titulo marcador= " + titulo);
        System.out.println("texto marcador= " + texto);
        Marker marker = getMarker(marcador);
        marker.setTitle(titulo);
        marker.setSnippet(texto);
    }

    public int getAlcance() {

        if (radioButtonMap.isChecked()) {
            alcance = 0;

        } else if (radioButtonMap1.isChecked()) {
            alcance = 1;
        } else if (radioButtonMap2.isChecked()) {
            alcance = 2;
        } else if (radioButtonMap3.isChecked()) {
            alcance = 3;
        } else if (radioButtonMap4.isChecked()) {
            alcance = 4;
        } else if (radioButtonMap5.isChecked()) {
            alcance = 5;
        }

        return alcance;
    }

    public ModeloSQL getMarcador() {
        return marcador;
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

    protected ModeloSQL setMarcador(Marker marker) {

        for (ModeloSQL marcador : listaMarcadores.getLista()) {
            if (marcador.getString(MARCADOR_ID_MARK).equals(marker.getId())) {
                return marcador;
            }
        }
        return null;
    }

    public ModeloSQL crearMarcador(String tipo, String id, long latitud, long longitud) {

        ContentValues values = new ContentValues();
        values.put(MARCADOR_TIPO, tipo);
        values.put(MARCADOR_ID_REL, id);
        values.put(MARCADOR_LATITUD, latitud);
        values.put(MARCADOR_LONGITUD, longitud);
        Marker mark = mapa.crearMarcadorMap(((double) (latitud) / 100000), ((double) (longitud) / 100000), 5, "", "", true);
        values.put(MARCADOR_ID_MARK, mark.getId());
        String idMarc = CRUDutil.crearRegistroId(TABLA_MARCADOR, values);
        marcador = CRUDutil.updateModelo(CAMPOS_MARCADOR, idMarc);
        listaMarcadores.addModelo(marcador);
        setLugarZona(marcador);
        if (onMarcadorEventListener != null) {
            onMarcadorEventListener.onCreateMarcador(zonasDel(), zonasAdd());
        }

        mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        return marcador;

    }

    public ListaModeloSQL getListaMarcadores() {
        return listaMarcadores;
    }


    public void updateListaMarcadores(ModeloSQL marcador) {

        for (ModeloSQL marcadore : listaMarcadores.getLista()) {
            if (marcadore.getString(MARCADOR_ID_MARK).equals(marcador.getString(MARCADOR_ID_MARK))) {
                listaMarcadores.getLista().set(listaMarcadores.getLista().indexOf(marcadore), marcador);
                break;
            }
        }

    }

    public ModeloSQL deleteMarcador(ModeloSQL marcador) {


        setLugarZona(marcador);

        Marker marker = getMarker(marcador);
        mapa.borrarMarcador(marker);

        for (ModeloSQL marcadore : listaMarcadores.getLista()) {
            if (marcadore.getString(MARCADOR_ID_MARK).equals(marcador.getString(MARCADOR_ID_MARK))) {
                listaMarcadores.getLista().remove(marcadore);
                ListaModeloSQL listaZonasMarcador = CRUDutil.setListaModelo(CAMPOS_ZONA, ZONA_ID_REL, marcador.getString(MARCADOR_ID_MARCADOR), IGUAL);
                for (ModeloSQL zonaMarcador : listaZonasMarcador.getLista()) {
                    CRUDutil.borrarRegistro(TABLA_ZONA, zonaMarcador.getString(ZONA_ID_ZONA));
                }
                CRUDutil.borrarRegistro(TABLA_MARCADOR, marcador.getString(MARCADOR_ID_MARCADOR));
                break;
            }
        }
        zona.setText("");

        if (onMarcadorEventListener != null) {
            onMarcadorEventListener.onDeleteMarcador(zonasDel(), zonasAdd());
        }
        return null;

    }

    public void setMarcadores() {


        listaMarcadores = new ListaModeloSQL(CAMPOS_MARCADOR, MARCADOR_ID_REL, id, null);

        for (ModeloSQL marc : listaMarcadores.getLista()) {
            Marker marker = mapa.crearMarcadorMap(((double) (marc.getLong(MARCADOR_LATITUD)) / 100000), ((double) (marc.getLong(MARCADOR_LONGITUD)) / 100000), 5, "", "", true);
            if (marker != null) {
                CRUDutil.actualizarCampo(marc, MARCADOR_ID_MARK, marker.getId());
                marc = CRUDutil.updateModelo(marc);
                updateListaMarcadores(marc);
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }

        }

        listaMarcadores = new ListaModeloSQL(CAMPOS_MARCADOR, MARCADOR_ID_REL, id, null);

        if (listaMarcadores.sizeLista() > 0) {
            marcador = listaMarcadores.getLista().get(0);
            System.out.println("marcador.getString(MARCADOR_ID_MARK) = " + marcador.getString(MARCADOR_ID_MARK));

            Marker marker = getMarker(marcador);
            if (marker != null) {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                mapa.moverCamara(marker.getPosition().latitude, marker.getPosition().longitude, 5, true, true);
                setAlcanceRadioGroup(marcador);
                setLugarZona(marcador);
            }

        }

    }

    public interface OnReadyMap {

        void onMapClickListener(ArrayList<Marker> listaMarkers);

        void onMapLongClickListener(ArrayList<Marker> listaMarkers);

        void onMyLocationClickListener(long latUser, long lonUser, ArrayList<String> paisUser);

        void onMarkerDragEnd(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd, ArrayList<String> paisUser);

    }

    public interface OnMarcadorEvent {

        void onCreateMarcador(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd);

        void onDeleteMarcador(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd);

        void onUpdateMarcador(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd);

    }

    public interface OnLocalizacionDef {

        void onEnable(ArrayList<String> listaUbicaciones);

        void onDisable();
    }

}
