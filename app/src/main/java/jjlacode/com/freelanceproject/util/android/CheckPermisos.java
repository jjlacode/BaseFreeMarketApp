package jjlacode.com.freelanceproject.util.android;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;


public class CheckPermisos {


    public static boolean validarPermisos(final MainActivityBase activity, final String permisoSolicitado, final int code) {


        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M ){

            return true;
        }else if (activity.checkSelfPermission(permisoSolicitado)== PackageManager.PERMISSION_GRANTED){

            return true;
        }else if (activity.shouldShowRequestPermissionRationale(permisoSolicitado)){

            AlertDialog.Builder dialogo = new AlertDialog.Builder(activity);
            dialogo.setTitle("Permisos desactivados");
            dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la app");

            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    activity.requestPermissions(new String[]
                            {permisoSolicitado}, code);
                }
            });
            dialogo.show();

        }

        return false;
    }


}
