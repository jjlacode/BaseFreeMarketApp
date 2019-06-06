package jjlacode.com.freelanceproject.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import jjlacode.com.freelanceproject.MainActivity;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class CheckPermisos extends Fragment {

    private boolean valido = false;
    private Context contexto;
    MainActivityBase activity = new MainActivityBase();

    public CheckPermisos(Context context) {
        this.contexto = context;
    }

    public boolean check(String permisoSolicitado){

        valido = false;

        if (validarPermisos(permisoSolicitado)) {valido = true;}

        return valido;
    }

    public boolean validarPermisos(final String permisoSolicitado) {


        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M ){

            return true;
        }else if (activity.checkSelfPermission(permisoSolicitado)== PackageManager.PERMISSION_GRANTED){

            return true;
        }else if (activity.shouldShowRequestPermissionRationale(permisoSolicitado)){

            AlertDialog.Builder dialogo = new AlertDialog.Builder(contexto);
            dialogo.setTitle("Permisos desactivados");
            dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la app");

            dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    activity.requestPermissions(new String[]
                            {permisoSolicitado},100);
                }
            });
            dialogo.show();

        }else {

            activity.requestPermissions(new String[]
                    {permisoSolicitado},100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100 && grantResults.length==1 && (grantResults[0]==PackageManager.PERMISSION_GRANTED)){

            valido = true;

        }else{

            Toast.makeText(contexto,"Debe aceptar todos los permisos para " +
                    "que la app se ejecute correctamente",Toast.LENGTH_LONG).show();
        }
    }
}
