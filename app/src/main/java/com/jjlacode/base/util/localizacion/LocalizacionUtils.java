package com.jjlacode.base.util.localizacion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jjlacode.base.util.android.AppActivity;
import com.jjlacode.base.util.android.CheckPermisos;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocalizacionUtils {

    Context contexto = AppActivity.getAppContext();
    LocationManager locationManager = (LocationManager) contexto.getSystemService(Context.LOCATION_SERVICE);
    double longitudeBest, latitudeBest;
    double longitudeGPS, latitudeGPS;
    double longitudeNetwork, latitudeNetwork;

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(contexto);
        dialog.setTitle("Enable Location")
                .setMessage("Su ubicación esta desactivada.\npor favor active su ubicación " +
                        "si quiere usarla en la app")
                .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        contexto.startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    public void toggleGPSUpdates(boolean localizando, AppCompatActivity activity) {

        boolean permiso = false;

        if (!checkLocation())
            return;

        if (!localizando) {
            locationManager.removeUpdates(locationListenerGPS);

        } else {

            permiso = CheckPermisos.validarPermisos(activity, ACCESS_FINE_LOCATION, 100) ||
                    CheckPermisos.validarPermisos(activity, ACCESS_COARSE_LOCATION, 100);


            if (permiso) {


                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 2 * 20 * 1000, 10, locationListenerGPS);
            }

        }
    }

    @SuppressLint("MissingPermission")
    public void toggleBestUpdates(boolean localizando, AppCompatActivity activity) {

        boolean permiso = false;

        if (!checkLocation())
            return;
        if (!localizando) {

            locationManager.removeUpdates(locationListenerBest);
        } else {

            permiso = CheckPermisos.validarPermisos(activity, ACCESS_FINE_LOCATION, 100) ||
                    CheckPermisos.validarPermisos(activity, ACCESS_COARSE_LOCATION, 100);

            if (permiso) {


                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(true);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                String provider = locationManager.getBestProvider(criteria, true);
                if (provider != null) {
                    locationManager.requestLocationUpdates(provider, 2 * 20 * 1000, 10, locationListenerBest);
                    Toast.makeText(contexto, "Best Provider is " + provider, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void toggleNetworkUpdates(boolean localizando, AppCompatActivity activity) {

        boolean permiso = false;

        if (!checkLocation())
            return;
        if (!localizando) {

            locationManager.removeUpdates(locationListenerNetwork);
        } else {


            permiso = CheckPermisos.validarPermisos(activity, ACCESS_FINE_LOCATION, 100) ||
                    CheckPermisos.validarPermisos(activity, ACCESS_COARSE_LOCATION, 100);


            if (permiso) {

                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 20 * 1000, 10, locationListenerNetwork);
                Toast.makeText(contexto, "Network provider started running", Toast.LENGTH_LONG).show();
            }
        }
    }

    private LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeBest = location.getLongitude();
            latitudeBest = location.getLatitude();

            if (listenerBestLocalization != null) {
                listenerBestLocalization.onBestLocalizationChange(latitudeBest, longitudeBest);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    private LocationListener locationListenerNetwork = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            longitudeNetwork = location.getLongitude();
            latitudeNetwork = location.getLatitude();
            System.out.println("longitudeNetwork = " + longitudeNetwork);
            System.out.println("latitudeNetwork = " + latitudeNetwork);

            if (listenerNetLocalization != null) {
                listenerNetLocalization.onLocalizationNetChange(latitudeNetwork, longitudeNetwork);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private LocationListener locationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeGPS = location.getLongitude();
            latitudeGPS = location.getLatitude();

            if (listenerGpsLocalization != null) {
                listenerGpsLocalization.onLocalizationGpsChange(latitudeGPS, longitudeGPS);
            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    public void setOnLocalizationGpsChange(OnLocalizationGpsChange listenerGpsLocalization) {

        this.listenerGpsLocalization = listenerGpsLocalization;
    }

    OnLocalizationGpsChange listenerGpsLocalization;

    public interface OnLocalizationGpsChange {

        void onLocalizationGpsChange(double lat, double lon);
    }

    public void setOnLocalizationNetChange(OnLocalizationNetChange listenerNetLocalization) {

        this.listenerNetLocalization = listenerNetLocalization;
    }

    OnLocalizationNetChange listenerNetLocalization;

    public interface OnLocalizationNetChange {

        void onLocalizationNetChange(double lat, double lon);
    }

    public void setOnLocalizationChange(OnBestLocalizationChange listenerBestLocalization) {

        this.listenerBestLocalization = listenerBestLocalization;
    }

    OnBestLocalizationChange listenerBestLocalization;

    public interface OnBestLocalizationChange {

        void onBestLocalizationChange(double lat, double lon);
    }

    public static List<Address> getAddressListGeoCoord(Context context, double latitude, double longitude) {

        Geocoder geocoder;
        geocoder = new Geocoder(context, Locale.getDefault());
        if (latitude > 90) {
            latitude = 90;
        }
        if (latitude < -90) {
            latitude = -90;
        }
        if (longitude > 180) {
            longitude = 180;
        }
        if (longitude < -180) {
            longitude = -180;
        }

        try {
            return geocoder.getFromLocation(latitude, longitude, 3); // 1 representa la cantidad de resultados a obtener
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Address getAddressGeoCoord(Context context, double latitude, double longitude) {

        Geocoder geocoder;
        geocoder = new Geocoder(context, Locale.getDefault());
        if (latitude > 90) {
            latitude = 90;
        }
        if (latitude < -90) {
            latitude = -90;
        }
        if (longitude > 180) {
            longitude = 180;
        }
        if (longitude < -180) {
            longitude = -180;
        }

        try {
            return geocoder.getFromLocation(latitude, longitude, 1).get(0); // 1 representa la cantidad de resultados a obtener
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getCountryGeoCoord(Context context, double latitude, double longitude) {

        Geocoder geocoder;
        List<Address> direccion = null;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            direccion = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (direccion != null && direccion.size() > 0) {
            return direccion.get(0).getCountryName();
        }

        return null;
    }

    public static String getCountryCodeGeoCoord(Context context, double latitude, double longitude) throws IOException {

        Geocoder geocoder;
        List<Address> direccion;
        geocoder = new Geocoder(context, Locale.getDefault());

        direccion = geocoder.getFromLocation(latitude, longitude, 1);
        if (direccion != null && direccion.size() > 0) {
            return direccion.get(0).getCountryCode();
        }

        return null;
    }

    public static String getPostalCodeGeoCoord(Context context, double latitude, double longitude) throws IOException {

        Geocoder geocoder;
        List<Address> direccion;
        geocoder = new Geocoder(context, Locale.getDefault());

        direccion = geocoder.getFromLocation(latitude, longitude, 1);
        if (direccion != null && direccion.size() > 0) {
            return direccion.get(0).getPostalCode();
        }

        return null;

    }

    public static String getCityGeoCoord(Context context, double latitude, double longitude) throws IOException {

        Geocoder geocoder;
        List<Address> direccion;
        geocoder = new Geocoder(context, Locale.getDefault());

        direccion = geocoder.getFromLocation(latitude, longitude, 1);
        if (direccion != null && direccion.size() > 0) {
            return direccion.get(0).getLocality();
        }

        return null;

    }

    public static String getProvGeoCoord(Context context, double latitude, double longitude) throws IOException {

        Geocoder geocoder;
        List<Address> direccion;
        geocoder = new Geocoder(context, Locale.getDefault());

        direccion = geocoder.getFromLocation(latitude, longitude, 1);
        if (direccion != null && direccion.size() > 0) {
            return direccion.get(0).getSubAdminArea();
        }

        return null;

    }

    public static String getSubCityGeoCoord(Context context, double latitude, double longitude) throws IOException {

        Geocoder geocoder;
        List<Address> direccion;
        geocoder = new Geocoder(context, Locale.getDefault());

        direccion = geocoder.getFromLocation(latitude, longitude, 1);
        if (direccion != null && direccion.size() > 0) {
            return direccion.get(0).getSubLocality();
        }

        return null;

    }

    public static String getStateGeoCoord(Context context, double latitude, double longitude) throws IOException {

        Geocoder geocoder;
        List<Address> direccion;
        geocoder = new Geocoder(context, Locale.getDefault());

        direccion = geocoder.getFromLocation(latitude, longitude, 1);
        if (direccion != null && direccion.size() > 0) {
            return direccion.get(0).getAdminArea();
        }

        return null;

    }
}
