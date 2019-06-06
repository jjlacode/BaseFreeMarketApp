package jjlacode.com.freelanceproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.CommonPry;

import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.PERSISTENCIA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.PREFERENCIAS;


public class SplashActivity extends AppCompatActivity implements ContratoPry.Tablas, CommonPry.Constantes {

    private static ConsultaBD consulta = new ConsultaBD();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!comprobarInicio()){

                    SharedPreferences preferences=getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();

                    //try {
                        ContentValues valoresPer = new ContentValues();

                        consulta.putDato(valoresPer,CAMPOS_PERFIL,PERFIL_NOMBRE,"Defecto");
                        consulta.putDato(valoresPer,CAMPOS_PERFIL,PERFIL_DESCRIPCION,
                                "Perfil por defecto, jornada normal de 8 horas diarias" +
                                        " de lunes a viernes y 30 dias de vacaciones al año, " +
                                        " y un sueldo anual de "+ JavaUtil.formatoMonedaLocal(20000));
                        Uri reg = consulta.insertRegistro(TABLA_PERFIL,valoresPer);
                        System.out.println(reg);
/*
                    }catch (Exception e){

                        Toast.makeText(getApplicationContext(),"Error al crear base de datos",Toast.LENGTH_LONG).show();
                        System.out.println("error al crear base");
                        getApplicationContext().deleteDatabase(BASEDATOS);
                        if (preferences.contains(PERFILACTIVO)) {

                            editor.remove(PERFILACTIVO);
                            editor.apply();
                        }
                        finish();
                    }
*/
                    try{

                        editor.putString(PERFILACTIVO, "Defecto");
                        editor.putBoolean(PRIORIDAD, true);
                        editor.putInt(DIASPASADOS,20);
                        editor.putInt(DIASFUTUROS,90);
                        editor.apply();

                    }catch (Exception e){

                        if (preferences.contains(PERFILACTIVO)) {

                            editor.remove(PERFILACTIVO);
                            editor.apply();

                        }
                        getApplicationContext().deleteDatabase(BASEDATOS);
                        System.out.println("error al crear base");
                        finish();

                    }
                    CommonPry.prioridad = true;
                    CommonPry.perfila = "Defecto";
                    CommonPry.diasfuturos = 90;
                    CommonPry.diaspasados = 20;
                    CommonPry.hora = CommonPry.Calculos.calculoPrecioHora();
                    CommonPry.setNamefdef();
                }



                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra(INICIO,1);
                startActivity(intent);
                finish();
            }
        },500);
    }

    private Boolean comprobarInicio() {

        SharedPreferences preferences=getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);

        if (getDatabasePath(BASEDATOS)!=null && preferences.contains(PERFILACTIVO)){

            CommonPry.perfila = preferences.getString(PERFILACTIVO,"Defecto");
            CommonPry.prioridad = preferences.getBoolean(PRIORIDAD,true);
            CommonPry.diaspasados = preferences.getInt(DIASPASADOS,20);
            CommonPry.diasfuturos = preferences.getInt(DIASFUTUROS,90);
            CommonPry.hora = CommonPry.Calculos.calculoPrecioHora();
            CommonPry.setNamefdef();

            Log.d("inicio", "Inicio correcto");

            SharedPreferences persistencia=getSharedPreferences(PERSISTENCIA, MODE_PRIVATE);
            SharedPreferences.Editor editor=persistencia.edit();
            editor.clear();
            editor.apply();

            return true;
        }

        if (preferences.contains(PERFILACTIVO)){

            SharedPreferences.Editor editor=preferences.edit();
            editor.remove(PERFILACTIVO);
            editor.apply();

            Log.d("inicio", "borrado perfil activo de preferencias");
        }
        if (getDatabasePath(BASEDATOS)!=null){

            deleteDatabase(BASEDATOS);

            Log.d("inicio", "borrada" + BASEDATOS);
        }

        return false;
    }




}
