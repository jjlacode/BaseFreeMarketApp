package com.codevsolution.base.localization;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.codevsolution.base.android.CheckPermisos;
import com.codevsolution.base.interfaces.ICFragmentos;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapBase {

    public final int LOCATION_REQUEST_CODE = 333;
    public final static double KMTGR = (2 * Math.PI * 6366) / 360;
    private GoogleMap gMap;
    private FragmentMap mMap;
    private ArrayList<Circle> listaCirculos;
    private ArrayList<Marker> listaMarkers;
    private ArrayList<Polygon> listaPoligonos;

    public MapBase(int layoutMap, ICFragmentos icFragmentos, AppCompatActivity activityBase) {

        boolean permiso = CheckPermisos.validarPermisos(activityBase, ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE) ||
                CheckPermisos.validarPermisos(activityBase, ACCESS_COARSE_LOCATION, LOCATION_REQUEST_CODE);

        if (permiso) {

            mMap = new FragmentMap();

            icFragmentos.addFragment(mMap, layoutMap);
            mMap.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    if (mMap != null) {

                        gMap = googleMap;


                        if (onReadyMap != null) {

                            onReadyMap.onReady(googleMap);
                        }


                    }
                }
            });
            listaCirculos = new ArrayList<>();
            listaMarkers = new ArrayList<>();
            listaPoligonos = new ArrayList<>();
        }
    }

    public MapBase(FragmentMap map) {


        mMap = map;

        mMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                if (mMap != null) {

                    gMap = googleMap;


                    if (onReadyMap != null) {

                        onReadyMap.onReady(googleMap);
                    }


                }
            }
        });
        listaCirculos = new ArrayList<>();
        listaMarkers = new ArrayList<>();
        listaPoligonos = new ArrayList<>();
    }

    public ArrayList<Circle> getListaCirculos() {
        return listaCirculos;
    }

    public ArrayList<Marker> getListaMarkers() {
        return listaMarkers;
    }

    public ArrayList<Polygon> getListaPoligonos() {
        return listaPoligonos;
    }

    public void moverCamara(double latitud, double longitud, int zoom, boolean ctrZomm, boolean gestos) {

        if (gMap != null) {
            LatLng latLng = new LatLng(latitud, longitud);

            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(latLng)
                    .zoom(zoom)
                    .build();

            gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            UiSettings uiSettings = gMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(ctrZomm);
            uiSettings.isMyLocationButtonEnabled();
            uiSettings.setAllGesturesEnabled(gestos);
        }

    }


    public Marker crearMarcadorMap(double latitud, double longitud, int zoom, String titulo, String texto, boolean dragable, String tag) {

        Marker marker;
        System.out.println("Creando marcador");
        if (gMap != null) {
            LatLng latLng = new LatLng(latitud, longitud);
            MarkerOptions markerOptions =
                    new MarkerOptions()
                            .position(latLng)
                            .title(titulo)
                            .snippet(texto)
                            .draggable(dragable);

            marker = gMap.addMarker(markerOptions);
            marker.setTag(tag);

            listaMarkers.add(marker);

            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(latLng)
                    .zoom(zoom)
                    .build();

            gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            UiSettings uiSettings = gMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setAllGesturesEnabled(true);

            return marker;

        }

        return null;

    }

    public void actualizaMark(Marker marker) {

        for (Marker mark : listaMarkers) {

            if (mark.getId().equals(marker.getId())) {
                listaMarkers.set(listaMarkers.indexOf(mark), marker);
                break;
            }
        }
    }

    public void moverMarcador(Marker marker, double latitud, double longitud) {

        if (marker != null) {
            LatLng latLng = new LatLng(latitud, longitud);
            marker.setPosition(latLng);
        }
    }

    public void borrarMarcador(Marker marker) {

        if (marker != null) {
            listaMarkers.remove(marker);
            marker.remove();
        }
    }

    public void borrarCirculo(Circle circle) {

        if (circle != null) {
            listaCirculos.remove(circle);
            circle.remove();
        }
    }

    public void borrarPoligono(Polygon polygon) {

        if (polygon != null) {
            listaPoligonos.remove(polygon);
            polygon.remove();
        }
    }

    public double getRadianes(float valor) {

        return (Math.PI / 180) * valor;
    }

    public double getRadianes(double valor) {

        return (Math.PI / 180) * valor;
    }

    public double distanciaKmts(LatLng origen, LatLng destino) {

        double difLatitud = getRadianes(destino.latitude - origen.latitude);
        double difLongitud = getRadianes(destino.longitude - origen.longitude);

        double a = Math.pow(Math.sin(difLatitud / 2), 2) +
                Math.cos(getRadianes(origen.latitude)) *
                        Math.cos(getRadianes(destino.latitude)) *
                        Math.pow(Math.sin(difLongitud / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371 * c;
    }

    public double sumarKmtsLon(double latOrigen, double lonOrigen, long kmt) {

        //double lonDestino = ((Math.sqrt( Math.pow(kmt,2)) - Math.sqrt(Math.abs((Math.pow((lonOrigen)*(KMTGR-Math.abs(latOrigen)),2)))))/(KMTGR-Math.abs(latOrigen)));
        System.out.println("kmtsCorregidos(latOrigen) = " + kmtsCorregidos(latOrigen));
        return (lonOrigen + ((double) (kmt) / (kmtsCorregidos(latOrigen))));

    }

    public double restarKmtsLon(double latOrigen, double lonOrigen, long kmt) {

        //double lonDestino = ((Math.sqrt( Math.pow(kmt,2)) - Math.sqrt(Math.abs((Math.pow((lonOrigen)*(KMTGR-Math.abs(latOrigen)),2)))))/(KMTGR-Math.abs(latOrigen)));
        System.out.println("Math.cos(" + (latOrigen) + ") = " + Math.cos(getRadianes(latOrigen)));
        System.out.println("Math.sin(" + (latOrigen) + ") = " + Math.sin(getRadianes(latOrigen)));
        System.out.println("Math.acos(" + (latOrigen) + ") = " + Math.acos(getRadianes(latOrigen)));
        System.out.println("Math.asin(" + (latOrigen) + ") = " + Math.asin(getRadianes(latOrigen)));
        System.out.println("Math.tan(" + (latOrigen) + ") = " + Math.tan(getRadianes(latOrigen)));
        System.out.println("Math.atan(" + (latOrigen) + ") = " + Math.atan(getRadianes(latOrigen)));

        return (lonOrigen - ((double) kmt / (kmtsCorregidos(latOrigen))));
    }

    public double sumarKmtsLat(double latOrigen, long kmt) {

        return (latOrigen + ((double) kmt / (KMTGR)));
    }

    public double restarKmtsLat(double latOrigen, long kmt) {

        return (latOrigen - ((double) kmt / (KMTGR)));
    }

    public Polygon crearPoligonoMap(double latitud, double longitud, double ancho, double alto) {


        LatLng p1 = new LatLng(sumarKmtsLat(latitud, (long) alto / 2),
                restarKmtsLon(latitud + ((alto / 2) / (kmtsCorregidos(latitud))), longitud, (long) (ancho / 2)));
        LatLng p2 = new LatLng(sumarKmtsLat(latitud, (long) alto / 2),
                sumarKmtsLon(latitud + ((alto / 2) / (kmtsCorregidos(latitud))), longitud, (long) (ancho / 2)));
        LatLng p3 = new LatLng(restarKmtsLat(latitud, (long) alto / 2),
                sumarKmtsLon(latitud - ((alto / 2) / (kmtsCorregidos(latitud))), longitud, (long) (ancho / 2)));
        LatLng p4 = new LatLng(restarKmtsLat(latitud, (long) alto / 2),
                restarKmtsLon(latitud - ((alto / 2) / (kmtsCorregidos(latitud))), longitud, (long) (ancho / 2)));

        System.out.println("Distancia 1-2= " + distanciaKmts(p1, p2));
        System.out.println("Distancia 2-3= " + distanciaKmts(p2, p3));
        System.out.println("Distancia 3-4= " + distanciaKmts(p3, p4));
        System.out.println("Distancia 4-1= " + distanciaKmts(p4, p1));


        if (gMap != null) {

            PolygonOptions polygonOptions = new PolygonOptions()
                    .add(p1, p2, p3, p4, p1)
                    .strokeColor(Color.parseColor("#0D47A1"))
                    .strokeWidth(4)
                    .fillColor(Color.argb(32, 33, 150, 243));

            Polygon polygon = gMap.addPolygon(polygonOptions);

            listaPoligonos.add(polygon);

            return polygon;

        }

        return null;
    }

    public Circle crearCirculoMap(double latitud, double longitud, double radio) {

        if (gMap != null) {

            LatLng center = new LatLng(latitud, longitud);
            CircleOptions circleOptions = new CircleOptions()
                    .center(center)
                    .radius(radio * 1000)
                    .strokeColor(Color.parseColor("#0D47A1"))
                    .strokeWidth(4)
                    .fillColor(Color.argb(32, 33, 150, 243));

            Circle circle = gMap.addCircle(circleOptions);

            listaCirculos.add(circle);

            return circle;
        }
        return null;
    }

    public void moverCirculoMap(Circle circle, double latitud, double longitud, double radio) {

        if (circle != null) {
            LatLng center = new LatLng(latitud, longitud);
            circle.setCenter(center);
        }

    }

    public void cambiarRadioCirculoMap(Circle circle, double newRadio) {

        if (circle != null) {
            circle.setRadius(newRadio * 1000);
        }

    }

    public void moverPoligono(Polygon polygon, double latitud, double longitud, double ancho, double alto) {

        List<LatLng> latLngList = new ArrayList<>();

        LatLng p1 = new LatLng(sumarKmtsLat(latitud, (long) alto / 2),
                restarKmtsLon(latitud + ((alto / 2) / (kmtsCorregidos(latitud))), longitud, (long) (ancho / 2)));
        LatLng p2 = new LatLng(sumarKmtsLat(latitud, (long) alto / 2),
                sumarKmtsLon(latitud + ((alto / 2) / (kmtsCorregidos(latitud))), longitud, (long) (ancho / 2)));
        LatLng p3 = new LatLng(restarKmtsLat(latitud, (long) alto / 2),
                sumarKmtsLon(latitud - ((alto / 2) / (kmtsCorregidos(latitud))), longitud, (long) (ancho / 2)));
        LatLng p4 = new LatLng(restarKmtsLat(latitud, (long) alto / 2),
                restarKmtsLon(latitud - ((alto / 2) / (kmtsCorregidos(latitud))), longitud, (long) (ancho / 2)));

        System.out.println("Distancia 1-2= " + distanciaKmts(p1, p2));
        System.out.println("Distancia 2-3= " + distanciaKmts(p2, p3));
        System.out.println("Distancia 3-4= " + distanciaKmts(p3, p4));
        System.out.println("Distancia 4-1= " + distanciaKmts(p4, p1));

        latLngList.add(p1);
        latLngList.add(p2);
        latLngList.add(p3);
        latLngList.add(p4);

        /*
        latLngList.add(new LatLng(latitud+((alto/2)/(KMTGR-((latitud*(KMTGR/90))*(Math.abs(Math.sin(getRadianes(latitud))))))),restarKmtsLon(latitud+((alto/2)/(KMTGR-((latitud*(KMTGR/90))*(Math.abs(Math.cos(getRadianes(latitud))))))),longitud,(long)(ancho/2))));
        latLngList.add(new LatLng(latitud+((alto/2)/(KMTGR-((latitud*(KMTGR/90))*(Math.abs(Math.sin(getRadianes(latitud))))))),sumarKmtsLon(latitud+((alto/2)/(KMTGR-((latitud*(KMTGR/90))*(Math.abs(Math.cos(getRadianes(latitud))))))),longitud,(long)(ancho/2))));
        latLngList.add(new LatLng(latitud-((alto/2)/(KMTGR-((latitud*(KMTGR/90))*(Math.abs(Math.sin(getRadianes(latitud))))))),sumarKmtsLon(latitud-((alto/2)/(KMTGR-((latitud*(KMTGR/90))*(Math.abs(Math.cos(getRadianes(latitud))))))),longitud,(long)(ancho/2))));
        latLngList.add(new LatLng(latitud-((alto/2)/(KMTGR-((latitud*(KMTGR/90))*(Math.abs(Math.sin(getRadianes(latitud))))))),restarKmtsLon(latitud-((alto/2)/(KMTGR-((latitud*(KMTGR/90))*(Math.abs(Math.cos(getRadianes(latitud))))))),longitud,(long)(ancho/2))));

         */


        if (polygon != null) {

            polygon.setPoints(latLngList);
        }
    }

    public double kmtsCorregidos(double lat) {

        //return KMTGR-((lat*(KMTGR/90))*(Math.abs(Math.cos(getRadianes(lat)))));
        return (2 * Math.PI * 6371 * (Math.abs(Math.cos(getRadianes(lat))))) / 360;
    }


    public void setMyLocation(boolean myLocation) {

        if (gMap != null) {
            gMap.setMyLocationEnabled(myLocation);
        }
    }

    OnReadyMap onReadyMap;

    public void setOnReadyMap(OnReadyMap onReadyMap) {

        this.onReadyMap = onReadyMap;
    }

    public void borrarObjetos() {

        try {
            for (Marker marker : listaMarkers) {
                marker.remove();
            }
            listaMarkers = new ArrayList<>();
        } catch (Exception e) {

        }
        try {
            for (Circle circulo : listaCirculos) {
                circulo.remove();
            }
            listaCirculos = new ArrayList<>();
        } catch (Exception e) {

        }
        try {
            for (Polygon poligono : listaPoligonos) {
                poligono.remove();
            }
            listaPoligonos = new ArrayList<>();
        } catch (Exception e) {

        }
    }

    public interface OnReadyMap {

        void onReady(GoogleMap googleMap);

    }

    public static class FragmentMap extends SupportMapFragment {

        public FragmentMap() {
        }

        public static FragmentMap newInstance() {
            return new FragmentMap();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View root = super.onCreateView(inflater, container, savedInstanceState);

            return root;
        }

    }
}
