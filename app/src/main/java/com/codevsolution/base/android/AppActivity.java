package com.codevsolution.base.android;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.CallLog;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.codevsolution.base.models.Contactos;
import com.codevsolution.base.models.Llamadas;
import com.codevsolution.freemarketsapp.BuildConfig;
import com.codevsolution.freemarketsapp.R;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.provider.ContactsContract.CommonDataKinds;

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

    public static void reconocimientoVoz(MainActivityBase activityBase, String idioma, int code) {

        if (CheckPermisos.validarPermisos(activityBase, READ_CONTACTS, 100) &&
                CheckPermisos.validarPermisos(activityBase, WRITE_CONTACTS, 100)) {

            Intent intentActionRecognizeSpeech = new Intent(
                    RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intentActionRecognizeSpeech.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL, idioma);
            try {
                activityBase.startActivityForResult(intentActionRecognizeSpeech,
                        code, null);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(context,
                        "Tú dispositivo no soporta el reconocimiento por voz",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void mostrarPDF(String rutaPdf) {

        Toast.makeText(context, R.string.visualiza_pdf, Toast.LENGTH_LONG).show();

        if (rutaPdf != null) {
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

    public static Uri getUriFromFile(File arch) {
        FILEPROVIDER = getNombreApp() + ".provider";
        return FileProvider.getUriForFile(context, FILEPROVIDER, arch);

    }

    public static Uri getUriFromFile(String path) {
        File arch = new File(path);
        FILEPROVIDER = getNombreApp() + ".provider";
        return FileProvider.getUriForFile(context, FILEPROVIDER, arch);

    }

    public static String getPackage() {
        return context.getPackageName();
    }

    public static String getNombreApp() {
        return BuildConfig.APPLICATION_ID;
    }

    public static String getFileProvider() {
        return FILEPROVIDER;
    }

    public static void hacerLlamada(Context context, String phoneNo) {

        if (!TextUtils.isEmpty(phoneNo)) {
            Uri uri = Uri.parse("tel:" + phoneNo);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.num_tel_invalido, Toast.LENGTH_SHORT).show();
        }
    }

    public static void grabarAudio(MediaRecorder grabadora, boolean grabando) {

        String path = null;

        if (!grabando) {
            if (grabadora != null) {
                grabadora.stop();
                grabadora.release();
            }
        } else {
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = "3gp_" + timeStamp + "_";
                File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
                File file = File.createTempFile(
                        fileName,  /* prefix */
                        ".3gp",         /* suffix */
                        storageDir      /* directory */
                );

                path = "contex:/" + file.getAbsolutePath();
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

    public static void reproducirAudio(MediaPlayer reproductor, String path, boolean reproduciendo) {

        if (reproduciendo) {
            try {
                reproductor.setDataSource(path);
                reproductor.prepare();
                reproductor.start();
            } catch (IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }
        } else {
            reproductor.stop();
        }
    }

    public static void hacerLlamada(Context context, String phoneNo, AppCompatActivity activity) {

        if (CheckPermisos.validarPermisos(activity, CALL_PHONE, 100)) {

            if (!TextUtils.isEmpty(phoneNo)) {
                Uri uri = Uri.parse("tel:" + phoneNo);
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "El numero no es valido", Toast.LENGTH_SHORT).show();
            }
        } else {

            hacerLlamada(context, phoneNo);
        }
    }

    public static void enviarEmail(Context context, String[] direcciones, String subject, String texto) {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, direcciones);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, texto);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
        }
    }

    public static void enviarEmail(Context context, String direccion, String subject, String texto) {

        String[] dir = {direccion};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        if (!TextUtils.isEmpty(direccion)) {
            intent.putExtra(Intent.EXTRA_EMAIL, dir);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, texto);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "La dirección de email no es valida", Toast.LENGTH_SHORT).show();
        }
    }

    public static void enviarEmail(Context context, String direccion) {

        String[] dir = {direccion};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        if (!TextUtils.isEmpty(direccion)) {
            intent.putExtra(Intent.EXTRA_EMAIL, dir);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
            }
        } else {
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

                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "La dirección de email no es valida", Toast.LENGTH_SHORT).show();
            }
        } else {
            enviarEmail(context, direccion, subject, texto);
        }
    }

    public static void enviarEmail(Context context, String direccion, String subject, String texto, ArrayList<Uri> uriPdf) {


        String[] dir = {direccion};
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setData(Uri.parse("mailto:"));
        if (!TextUtils.isEmpty(direccion)) {
            intent.putExtra(Intent.EXTRA_EMAIL, dir);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, texto);
            if (uriPdf != null) {
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_STREAM, uriPdf);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            }
            try {
                context.startActivity(Intent.createChooser(intent, "Send mail..."));
                Log.e("Test email:", "Fin envio email");

            } catch (ActivityNotFoundException ex) {
                Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "La dirección de email no es valida", Toast.LENGTH_SHORT).show();
        }
    }


    public static void enviarEmail(String[] direccion, String subject, String texto, Uri uriPdf) {


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        if (direccion != null) {
            intent.putExtra(Intent.EXTRA_EMAIL, direccion);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, texto);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, uriPdf);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                context.startActivity(Intent.createChooser(intent, "Send mail..."));
                Log.e("Test email:", "Fin envio email");

            } catch (ActivityNotFoundException ex) {
                Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "La dirección de email no es valida", Toast.LENGTH_SHORT).show();
        }
    }

    public static void enviarEmail(String[] direccion, String subject, String texto, ArrayList<Uri> uriPdf) {


        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setData(Uri.parse("mailto:"));
        if (direccion != null) {
            intent.putExtra(Intent.EXTRA_EMAIL, direccion);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, texto);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, uriPdf);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                context.startActivity(Intent.createChooser(intent, "Send mail..."));
                Log.e("Test email:", "Fin envio email");

            } catch (ActivityNotFoundException ex) {
                Toast.makeText(context, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "La dirección de email no es valida", Toast.LENGTH_SHORT).show();
        }
    }

    public static void compartirPdf(String path) {

        Uri uri = null;
        if (path != null) {
            File file = new File(path);
            uri = AppActivity.getUriFromFile(file);
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        if (uri != null && !TextUtils.isEmpty(path)) {
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                context.startActivity(Intent.createChooser(intent, "Compartiendo..."));

            } catch (ActivityNotFoundException ex) {
                Toast.makeText(context, "No hay disponible ninguna app", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "No hay pdf para compartir", Toast.LENGTH_SHORT).show();
        }
    }

    public static void compartir(String path, String tipo) {

        Uri uri = null;
        if (path != null) {
            File file = new File(path);
            uri = AppActivity.getUriFromFile(file);
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        if (uri != null && !TextUtils.isEmpty(path)) {
            intent.setType(tipo);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                context.startActivity(Intent.createChooser(intent, "Compartiendo..."));

            } catch (ActivityNotFoundException ex) {
                Toast.makeText(context, "No hay disponible ninguna app", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "No hay archivo para compartir", Toast.LENGTH_SHORT).show();
        }
    }


    public static void viewOnMapA(Context context, String address) {

        address = address.replace(" ", "+");
        String address2 = address.replace(",", "%2C");
        String baseUrl = "https://www.google.com/maps/search/?api=1&query=";
        String urlMap = String.format("%s%s", baseUrl, address2);

        System.out.println("address = " + address);

        try {

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(String.format("geo:0,0?q=%s",
                            URLEncoder.encode(address, "UTF-8"))));
            PackageManager packageManager = context.getPackageManager();
            List activities = packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            boolean isIntentSafe = activities.size() > 0;
            if (isIntentSafe) {
                context.startActivity(intent);

            } else {
                System.out.println("urlMap = " + urlMap);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s",
                        URLEncoder.encode(urlMap, "UTF-8"))));
                context.startActivity(intent);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("error maps = " + e);
        }

    }

    @SuppressLint("InlinedApi")
    public static void filePicker(FragmentActivity activity, int code) {

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(
                    Intent.createChooser(intent, "Select File"),
                    code);

        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            activity.startActivityForResult(intent, code);
        }
    }

    @SuppressLint("InlinedApi")
    public static void filePicker(FragmentActivity activity, int code, String mimeType) {

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            if (mimeType != null) {
                intent.setType(mimeType);

            } else {
                intent.setType("*/*");
            }
            activity.startActivityForResult(
                    Intent.createChooser(intent, "Select File"),
                    code);

        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            if (mimeType != null) {
                intent.setType(mimeType);

            } else {
                intent.setType("*/*");
            }
            activity.startActivityForResult(intent, code);
        }
    }

    public static ArrayList<Contactos> registroContactos(ContentResolver cr) {

        ArrayList<Contactos> arrayContactos = new ArrayList<Contactos>();
        String[] columnas = new String[]{CommonDataKinds.Phone.TYPE, CommonDataKinds.Phone.DISPLAY_NAME, CommonDataKinds.Phone.NUMBER};
        Cursor cursor = null;

        try {
            Contactos contactos = null;
            Uri identificadorContactos = CommonDataKinds.Phone.CONTENT_URI;
        /*Se declara e inicializa la clase ContentResolver, referenciándole el método getContentResolver(),
        que proporciona el acceso a los datos del Content Provider.*/

            cursor = cr.query(identificadorContactos, columnas, null, null, null);
            int columDatos, columNumero, columTipo = 0;
            if (cursor != null) {
            /*Se recorrerán los resultados almacenados de la consulta en el objeto
            Cursor, comprobando en cada iteración que existe el registro siguiente.*/
                if (cursor.moveToFirst()) {
                    columTipo = cursor.getColumnIndex(CommonDataKinds.Phone.TYPE);
                    columDatos = cursor.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME);
                    columNumero = cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER);
                    int tipoContacto = 0;

                    String numeroContacto = null;
                    String datosContacto = null;
                    String contactoTipo = null;

                    do {

                        tipoContacto = cursor.getInt(columTipo);
                        numeroContacto = cursor.getString(columNumero);
                        datosContacto = cursor.getString(columDatos);
                    /*Se controla el tipo de contacto para asignarle los valores de 'Teléfono Casa' o
                    'Teléfono móvil'.*/
                        if (tipoContacto == CommonDataKinds.Phone.TYPE_HOME) {
                            contactoTipo = "Casa";

                        } else if (tipoContacto == CommonDataKinds.Phone.TYPE_MOBILE) {
                            contactoTipo = "móvil";
                        }

                    /*Se crea un objeto de la clase Contactos por cada iteración, con los valores de tipo,
                    datos y número de contacto.*/
                        contactos = new Contactos(contactoTipo, datosContacto, numeroContacto);
                        arrayContactos.add(contactos);

                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayContactos;
    }

    public static ArrayList<Llamadas> registroLlamadas(ContentResolver cr) {
        ArrayList<Llamadas> lista = new ArrayList<Llamadas>();
        String[] columnas = new String[]{CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DATE};
        Cursor cursor = null;
        try {
            Llamadas llamadas = null;
            Uri identificadorLlamadas = CallLog.Calls.CONTENT_URI;
        /*Se declara e inicializa la clase ContentResolver, referenciándole el método getContentResolver(),
        que proporciona el acceso a los datos del Content Provider.*/


            cursor = cr.query(identificadorLlamadas, columnas, null, null, null);
            int columTipo, columNumero, columFecha = 0;
            if (cursor != null) {
            /*Se recorrerán los resultados almacenados de la consulta en el objeto
            Cursor, comprobando en cada iteración que existe el registro siguiente.*/
                if (cursor.moveToFirst()) {
                    columTipo = cursor.getColumnIndex(CallLog.Calls.TYPE);
                    columNumero = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                    /*Los datos de la columna Date de la tabla Calls se definen en milisegundos*/
                    columFecha = cursor.getColumnIndex(CallLog.Calls.DATE);
                    int tipoLlamada = 0;
                    String numeroLlamada = null;
                    long fechaLlamada = 0;
                    String llamadaTipo = null;

                    do {

                        tipoLlamada = cursor.getInt(columTipo);
                        numeroLlamada = cursor.getString(columNumero);

                        fechaLlamada = cursor.getLong(columFecha);

                    /*Se controla el tipo de llamada para asignarle los valores de 'Entrante', 'Perdida' o
                    'Saliente'.*/
                        if (tipoLlamada == CallLog.Calls.INCOMING_TYPE) {
                            llamadaTipo = "Entrante";
                        } else if (tipoLlamada == CallLog.Calls.MISSED_TYPE) {
                            llamadaTipo = "Perdida";
                        } else if (tipoLlamada == CallLog.Calls.OUTGOING_TYPE) {
                            llamadaTipo = "Saliente";
                        }

                    /*Se crea un objeto de la clase Llamadas por cada iteración, con los valores de tipo de
                    llamada, número y fecha (se convierte la fecha de milisegundos al formato HH:mm:ss dd-MM-YYYY).*/
                        llamadas = new Llamadas(llamadaTipo, numeroLlamada, fechaLlamada);
                        lista.add(llamadas);
                    } while (cursor.moveToNext());
                }
            }

        } catch (Exception ex) {
            ex.getMessage();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public static void enviarWhatsapp(String numero, String mensaje) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        String uri = "whatsapp://send?phone=" + numero + "&text=" + mensaje;//numero telefonico sin prefijo "+"!
        intent.setData(Uri.parse(uri));
        context.startActivity(intent);

    }

    private static void sendImageWhatsApp(String phoneNumber, String nombreImagen) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory() + "/" + nombreImagen));
            intent.putExtra("jid", phoneNumber + "@s.whatsapp.net"); //numero telefonico sin prefijo "+"!
            intent.setPackage("com.whatsapp");
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, "Whatsapp no esta instalado.", Toast.LENGTH_LONG).show();
        }
    }

    private static void sendImageWhatsApp(String phoneNumber, Uri uri) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra("jid", phoneNumber + "@s.whatsapp.net"); //numero telefonico sin prefijo "+"!
            intent.setPackage("com.whatsapp");
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, "Whatsapp no esta instalado.", Toast.LENGTH_LONG).show();
        }
    }

}
