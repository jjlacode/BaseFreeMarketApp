package jjlacode.com.freelanceproject.util.android;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jjlacode.com.freelanceproject.BuildConfig;
import jjlacode.com.freelanceproject.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AppActivity extends Application {

    private static Context context;
    private static String FILEPROVIDER;

    public void onCreate() {
        super.onCreate();
        AppActivity.context = getApplicationContext();
        FILEPROVIDER = getNombreApp() + ".provider";
    }

    public static Context getAppContext() {
        return AppActivity.context;
    }

    public static void reconocimientoVoz(MainActivityBase activityBase, String idioma, int code){

        Intent intentActionRecognizeSpeech = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Configura el Lenguaje (Español-México)
        intentActionRecognizeSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, idioma);
        try {
            activityBase.startActivityForResult(intentActionRecognizeSpeech,
                    code,null);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(context,
                    "Tú dispositivo no soporta el reconocimiento por voz",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void mostrarPDF(String rutaPdf) {

        Toast.makeText(context, R.string.visualiza_pdf, Toast.LENGTH_LONG).show();

        if (rutaPdf!=null) {
            File file = new File(rutaPdf);
            Uri uri = AppActivity.getUriFromFile(file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/pdf");

            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, R.string.no_app_pdf, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Uri getUriFromFile(File arch){
        FILEPROVIDER = getNombreApp() + ".provider";
        return FileProvider.getUriForFile(context, FILEPROVIDER, arch);

    }

    public static Uri getUriFromFile(String path){
        File arch = new File(path);
        FILEPROVIDER = getNombreApp() + ".provider";
        return FileProvider.getUriForFile(context, FILEPROVIDER, arch);

    }

    public static String getPackage(){
        return context.getPackageName();
    }

    public static String getNombreApp() {
        return BuildConfig.APPLICATION_ID;
    }

    public static String getFileProvider(){
        return FILEPROVIDER;
    }
    
    public static void hacerLlamada(Context context, String phoneNo){

        if(!TextUtils.isEmpty(phoneNo)) {
            Uri uri = Uri.parse("tel:" + phoneNo);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else {
            Toast.makeText(context, R.string.num_tel_invalido, Toast.LENGTH_SHORT).show();
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

    public static void enviarEmail(Context context, String direccion, String subject, String texto, String path) {

        Uri uri;
        if (path != null) {
            File file = new File(path);
            uri = AppActivity.getUriFromFile(file);

        String[] dir = {direccion};
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        if (!TextUtils.isEmpty(direccion)) {
            intent.putExtra(Intent.EXTRA_EMAIL, dir);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, texto);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
            try {
                context.startActivity(Intent.createChooser(intent, "Send mail..."));
                Log.e("Test email:", "Fin envio email");

            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "La dirección de email no es valida", Toast.LENGTH_SHORT).show();
        }
    }else{
            enviarEmail(context,direccion,subject,texto);
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
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

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
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

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
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

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

        Uri uri = null;
        if (path!=null) {
            File file = new File(path);
            uri = AppActivity.getUriFromFile(file);
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        if (uri!=null && !TextUtils.isEmpty(path)) {
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

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

            Uri uri = null;
            if (path!=null) {
                File file = new File(path);
                uri = AppActivity.getUriFromFile(file);
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            if (uri!=null && !TextUtils.isEmpty(path)) {
                intent.setType(tipo);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

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
