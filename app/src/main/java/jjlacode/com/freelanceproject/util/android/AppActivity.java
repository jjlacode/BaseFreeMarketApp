package jjlacode.com.freelanceproject.util.android;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public static void grabarAudio(MediaRecorder grabadora, boolean grabando){

        String path= null;

        if(!grabando){
            if (grabadora != null) {
                grabadora.stop();
                grabadora.release();
            }
        }else{
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = "3gp_" + timeStamp + "_";
                File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
                File file = File.createTempFile(
                        fileName,  /* prefix */
                        ".3gp",         /* suffix */
                        storageDir      /* directory */
                );

                path = "contex:/"+file.getAbsolutePath();
                //path = context.getContentResolver().insert(
                //        MediaStore.Media.EXTERNAL_CONTENT_URI);
                System.out.println("path = " + path);
                grabadora.reset();
                grabadora.setAudioSource(MediaRecorder.AudioSource.MIC);
                grabadora.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                grabadora.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                grabadora.setOutputFile(path);
                grabadora.prepare();
                grabadora.start();
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void reproducirAudio(MediaPlayer reproductor, String path, boolean reproduciendo){

        if (reproduciendo) {
            try {
                reproductor.setDataSource(path);
                reproductor.prepare();
                reproductor.start();
            } catch (IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }
        }else{
            reproductor.stop();
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

    public static void enviarEmail(Context context, String[] direcciones, String subject, String texto){

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

    public static void enviarEmail(Context context, String direccion, String subject, String texto){

        String[]dir = {direccion};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        if (!TextUtils.isEmpty(direccion)) {
            intent.putExtra(Intent.EXTRA_EMAIL,dir);
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

        String[]dir = {direccion};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        if (!TextUtils.isEmpty(direccion)) {
            intent.putExtra(Intent.EXTRA_EMAIL,dir);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "La dirección de email no es valida", Toast.LENGTH_SHORT).show();
        }
    }

    public static void enviarEmail(Context context, String direccion, String subject, String texto, String path){

        Uri uri = Uri.fromFile(new File(path));

        String[]dir = {direccion};
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        if (!TextUtils.isEmpty(direccion)) {
            intent.putExtra(Intent.EXTRA_EMAIL,dir);
            intent.putExtra(Intent.EXTRA_SUBJECT,subject);
            intent.putExtra(Intent.EXTRA_TEXT,texto);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            if (uri!=null) {
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
            }
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

    public static void enviarEmail(Context context, String direccion, String subject, String texto, ArrayList<Uri> uriPdf){


        String[]dir = {direccion};
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setData(Uri.parse("mailto:"));
        if (!TextUtils.isEmpty(direccion)) {
            intent.putExtra(Intent.EXTRA_EMAIL,dir);
            intent.putExtra(Intent.EXTRA_SUBJECT,subject);
            intent.putExtra(Intent.EXTRA_TEXT,texto);
            if (uriPdf!=null) {
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_STREAM, uriPdf);
            }
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


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        if (direccion!=null) {
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

    public static void enviarEmail(String[] direccion, String subject, String texto, ArrayList<Uri> uriPdf){


        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setData(Uri.parse("mailto:"));
        if (direccion!=null) {
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

    public static void compartirPdf(String path){

        Uri uri = Uri.fromFile(new File(path));

        Intent intent = new Intent(Intent.ACTION_SEND);
        if (!TextUtils.isEmpty(path)) {
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            try {
                context.startActivity(Intent.createChooser(intent, "Compartiendo..."));

            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "No hay disponible ninguna app", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "No hay pdf para compartir", Toast.LENGTH_SHORT).show();
        }
    }

    public static void compartir(String path, String tipo){

            Uri uri = Uri.fromFile(new File(path));

            Intent intent = new Intent(Intent.ACTION_SEND);
            if (!TextUtils.isEmpty(path)) {
                intent.setType(tipo);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                try {
                    context.startActivity(Intent.createChooser(intent, "Compartiendo..."));

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "No hay disponible ninguna app", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "No hay archivo para compartir", Toast.LENGTH_SHORT).show();
            }
    }


    public static void viewOnMapA(Context context, String address) {

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
            if (isIntentSafe){
                context.startActivity(intent);

            }else{
                System.out.println("urlMap = " + urlMap);
                intent = new Intent(Intent.ACTION_VIEW,Uri.parse(String.format("%s",
                        URLEncoder.encode(urlMap, "UTF-8"))));
                context.startActivity(intent);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("error maps = " + e);
        }

    }



}
