package jjlacode.com.androidutils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AppActivity extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        AppActivity.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return AppActivity.context;
    }

    public static void hacerLlamada(Context context, String phoneNo){

        if(!TextUtils.isEmpty(phoneNo)) {
            Uri uri = Uri.parse("tel:" + phoneNo);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else {
            Toast.makeText(context, "El numero no es valido", Toast.LENGTH_SHORT).show();
        }
    }

    public static void hacerLlamada(AppCompatActivity activity, Context context, String phoneNo, boolean permiso) {

        if (permiso) {

            if (!TextUtils.isEmpty(phoneNo)) {
                Uri uri = Uri.parse("tel:" + phoneNo);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            } else {
                Toast.makeText(context, "El numero no es valido", Toast.LENGTH_SHORT).show();
            }
        }else{

            hacerLlamada(context,phoneNo);
        }
    }

    public static void enviarEmail(Context context, String[] direcciones, String subject, CharSequence texto){

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, direcciones);
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,texto);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }else {
            Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
        }
    }

    public static void enviarEmail(Context context, String direccion){

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        if (!TextUtils.isEmpty(direccion)) {
            intent.putExtra(Intent.EXTRA_EMAIL, direccion);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "La dirección de email no es valida", Toast.LENGTH_SHORT).show();
        }
    }

    public static Intent viewOnMapA(String address) {

        address = address.replace(" ","+");
        String address2 = address.replace(",","%2C");
        String baseUrl = "https://www.google.com/maps/search/?api=1&query=";
        String urlMap = String.format("%s%s",baseUrl,address2);

        System.out.println("address = " + address);

        try {

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(String.format("geo:0,0?q=%s",
                            URLEncoder.encode(address, "UTF-8"))));
            PackageManager packageManager = context.getPackageManager();
            List activities = packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            boolean isIntentSafe = activities.size() > 0;
            if (isIntentSafe){return intent;}else{
                System.out.println("urlMap = " + urlMap);
                return new Intent(Intent.ACTION_VIEW,Uri.parse(String.format("%s",
                        URLEncoder.encode(urlMap, "UTF-8"))));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("error maps = " + e);
        }
        return null;
    }

    public static LatLng obtenerCoordenadas(Context context, String direccion){

        Geocoder geo = new Geocoder(context);
        int maxResultados = 1;
        List<Address> adress = null;
        try {
            adress = geo.getFromLocationName(direccion, maxResultados);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
