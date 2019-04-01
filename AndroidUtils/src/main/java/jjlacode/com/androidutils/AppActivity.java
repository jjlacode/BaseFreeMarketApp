package jjlacode.com.androidutils;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

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

    public static void hacerLlamada(Activity activity, Context context, String phoneNo, boolean permiso) {

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

}
