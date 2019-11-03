package com.codevsolution.base.android;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;


public class CheckPermisos {


    public static boolean validarPermisos(final AppCompatActivity activity, final String permisoSolicitado, final int code) {


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        } else if (activity.checkSelfPermission(permisoSolicitado) == PackageManager.PERMISSION_GRANTED) {

            return true;
        } else if (activity.shouldShowRequestPermissionRationale(permisoSolicitado)) {

            System.out.println("debe tener los permisos dialogo, permiso " + permisoSolicitado);
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

        } else {

            activity.requestPermissions(new String[]
                    {permisoSolicitado}, code);

        }

        return false;
    }

    public static boolean validarPermisos(final FragmentActivity activity, final String permisoSolicitado, final int code) {


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        } else if (activity.checkSelfPermission(permisoSolicitado) == PackageManager.PERMISSION_GRANTED) {

            return true;
        } else if (activity.shouldShowRequestPermissionRationale(permisoSolicitado)) {

            System.out.println("debe tener los permisos dialogo, permiso " + permisoSolicitado);
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

        } else {

            activity.requestPermissions(new String[]
                    {permisoSolicitado}, code);

        }

        return false;
    }

}
