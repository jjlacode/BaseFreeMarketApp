package jjlacode.com.freelanceproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;

public class SplashActivity extends AppCompatActivity implements Contract.Tablas {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!comprobarInicio()){

                    SharedPreferences preferences=getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();

                    try {
                        ContentValues valoresPer = new ContentValues();

                        QueryDB.putDato(valoresPer,CAMPOS_PERFIL,PERFIL_NOMBRE,"Defecto");
                        QueryDB.putDato(valoresPer,CAMPOS_PERFIL,PERFIL_BENEFICIO,10);
                        System.out.println("Contract.Tablas.obtenerUriContenido(Contract.Tabla.PERFIL) = "
                                + Contract.obtenerUriContenido(TABLA_PERFIL));
                        Uri reg = QueryDB.insertRegistro(TABLA_PERFIL,valoresPer);
                        System.out.println(reg);

                        ContentValues valoresgstfijo = new ContentValues();

                        QueryDB.putDato(valoresgstfijo,CAMPOS_GASTOFIJO,GASTOFIJO_DESCRIPCION, "Sueldo");
                        QueryDB.putDato(valoresgstfijo,CAMPOS_GASTOFIJO,GASTOFIJO_IMPORTE, 30000);
                        QueryDB.putDato(valoresgstfijo,CAMPOS_GASTOFIJO,GASTOFIJO_CANTIDAD, 1.0);
                        QueryDB.putDato(valoresgstfijo,CAMPOS_GASTOFIJO,GASTOFIJO_ANYOS, 1);

                        QueryDB.insertRegistro(TABLA_GASTOFIJO,valoresgstfijo);

                    }catch (Exception e){

                        Toast.makeText(getApplicationContext(),"Error al crear base de datos",Toast.LENGTH_LONG).show();
                        System.out.println("error al crear base");
                        getApplicationContext().deleteDatabase("freelanceproject.db");
                        if (preferences.contains("perfil_activo")) {

                            editor.remove("perfil_activo");
                            editor.apply();
                        }
                        finish();
                    }

                    try{

                        editor.putString("perfil_activo", "Defecto");
                        editor.putInt("prioridad", 1);
                        editor.apply();

                    }catch (Exception e){

                        if (preferences.contains("perfil_activo")) {

                            editor.remove("perfil_activo");
                            editor.apply();

                        }
                        getApplicationContext().deleteDatabase("freelanceproject.db");
                        System.out.println("error al crear base");
                        finish();

                    }
                    Common.prioridad=1;
                    Common.perfila="Defecto";
                    Common.hora = Common.Calculos.calculoPrecioHora();
                    Common.beneficio = 10;
                }



                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }

    private Boolean comprobarInicio() {

        SharedPreferences preferences=getSharedPreferences("preferencias", Context.MODE_PRIVATE);

        if (getDatabasePath("freelanceproject.db")!=null && preferences.contains("perfil_activo")){

            Common.perfila = preferences.getString("perfil_activo","Defecto");
            Common.prioridad = preferences.getInt("prioridad",1);
            Common.hora = Common.Calculos.calculoPrecioHora();

            Log.d("inicio", "Inicio correcto");


            return true;
        }

        if (preferences.contains("perfil_activo")){

            SharedPreferences.Editor editor=preferences.edit();
            editor.remove("perfil_activo");
            editor.apply();

            Log.d("inicio", "borrado perfil activo de preferencias");
        }
        if (getDatabasePath("freelanceproject.db")!=null){

            deleteDatabase("freelanceproject.db");

            Log.d("inicio", "borrada freelanceproject.db");
        }

        return false;
    }




}
