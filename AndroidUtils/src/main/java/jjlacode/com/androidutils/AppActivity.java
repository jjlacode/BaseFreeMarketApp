package jjlacode.com.androidutils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;

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

        Intent intent = new Intent(Intent.ACTION_SEND);
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

    public static void enviarEmail(Context context, String direccion, String subject, CharSequence texto){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"+direccion));
        if (!TextUtils.isEmpty(direccion)) {
            intent.putExtra(Intent.EXTRA_SUBJECT,subject);
            intent.putExtra(Intent.EXTRA_TEXT,texto);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "La dirección de email no es valida", Toast.LENGTH_SHORT).show();
        }
    }

    public static void enviarEmail(Context context, String direccion){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"+direccion));
        if (!TextUtils.isEmpty(direccion)) {
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "La dirección de email no es valida", Toast.LENGTH_SHORT).show();
        }
    }

    public static void enviarEmail(String direccion, String subject, String texto, Uri uriPdf){


        String[]dir = {direccion};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        if (!TextUtils.isEmpty(direccion)) {
            intent.putExtra(Intent.EXTRA_EMAIL,dir);
            intent.putExtra(Intent.EXTRA_SUBJECT,subject);
            intent.putExtra(Intent.EXTRA_TEXT,texto);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, uriPdf);
            try {
                context.startActivity(Intent.createChooser(intent, "Send mail..."));
                Log.e("Test email:", "Fin envio email");

            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "La dirección de email no es valida", Toast.LENGTH_SHORT).show();
        }
    }

    public static void enviarEmail(String[] direccion, String subject, String texto, Uri uriPdf){


        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        if (!TextUtils.isEmpty(direccion[0])) {
            intent.putExtra(Intent.EXTRA_EMAIL,direccion);
            intent.putExtra(Intent.EXTRA_SUBJECT,subject);
            intent.putExtra(Intent.EXTRA_TEXT,texto);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, uriPdf);
            try {
                context.startActivity(Intent.createChooser(intent, "Send mail..."));
                Log.e("Test email:", "Fin envio email");

            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "La dirección de email no es valida", Toast.LENGTH_SHORT).show();
        }
    }

    public static void compartirPdf(Uri uriPdf){


        Intent intent = new Intent(Intent.ACTION_SEND);
        if (!TextUtils.isEmpty(String.valueOf(uriPdf))) {
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, uriPdf);
            try {
                context.startActivity(Intent.createChooser(intent, "Compartiendo..."));

            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "No hay disponible ninguna app", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "No hay pdf para compartir", Toast.LENGTH_SHORT).show();
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



}
