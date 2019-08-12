package com.jjlacode.freelanceproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.android.AppActivity;
import com.jjlacode.base.util.login.LoginActivity;
import com.jjlacode.base.util.sqlite.ConsultaBD;
import com.jjlacode.freelanceproject.logica.Interactor;
import com.jjlacode.freelanceproject.sqlite.ContratoPry;

import static com.jjlacode.base.util.JavaUtil.Constantes.NULL;
import static com.jjlacode.base.util.JavaUtil.Constantes.PERSISTENCIA;
import static com.jjlacode.base.util.JavaUtil.Constantes.PREFERENCIAS;

public class SplashActivity extends AppCompatActivity implements ContratoPry.Tablas, Interactor.Constantes {

    EditText pass;
    EditText etcorreo;
    String correo, contrasena;
    Button btnRegistrar;
    Button btnLogin;
    LinearLayoutCompat lyRegistro;
    FirebaseAuth firebaseAuth;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        etcorreo = findViewById(R.id.etcorreo);
        pass = findViewById(R.id.etpass);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnLogin = findViewById(R.id.btnLogin);
        firebaseAuth = FirebaseAuth.getInstance();
        lyRegistro = findViewById(R.id.lyregistro);
        context = AppActivity.getAppContext();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correo = etcorreo.getText().toString().trim();
                contrasena = pass.getText().toString().trim();
                firebaseAuth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            iniciarDB(firebaseAuth.getUid());
                            Toast.makeText(context, "El usuario se registro con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "El usuario no se registro.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iniciarDB(firebaseAuth.getUid());
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        if (!comprobarInicio()) {

            SharedPreferences preferences = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
            String userID = preferences.getString(USERID, NULL);

            if (userID.equals(NULL)) {

                lyRegistro.setVisibility(View.VISIBLE);

            } else {

                iniciarDB(userID);
            }

        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    //intent.putExtra(INICIO,1);
                    startActivity(intent);
                    finish();
                }
            }, 500);
        }


    }

    private void iniciarDB(String userID) {

        SharedPreferences preferences = getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        ContentValues valoresPer = new ContentValues();

        ConsultaBD.putDato(valoresPer, CAMPOS_PERFIL, PERFIL_NOMBRE, "Defecto");
        ConsultaBD.putDato(valoresPer, CAMPOS_PERFIL, PERFIL_DESCRIPCION,
                "Perfil por defecto, jornada normal de 8 horas diarias" +
                        " de lunes a viernes y 30 dias de vacaciones al año, " +
                        " y un sueldo anual de " + JavaUtil.formatoMonedaLocal(20000));
        Uri reg = ConsultaBD.insertRegistro(TABLA_PERFIL, valoresPer);
        System.out.println(reg);

        try {

            editor.putString(USERID, userID);
            editor.putString(PERFILACTIVO, "Defecto");
            editor.putBoolean(PRIORIDAD, true);
            editor.putInt(DIASPASADOS, 20);
            editor.putInt(DIASFUTUROS, 90);
            editor.apply();

        } catch (Exception e) {

            if (preferences.contains(PERFILACTIVO)) {

                editor.remove(PERFILACTIVO);
                editor.apply();

            }
            getApplicationContext().deleteDatabase(BASEDATOS);
            System.out.println("error al crear base");
            finish();

        }
        Interactor.prioridad = true;
        Interactor.perfila = "Defecto";
        Interactor.diasfuturos = 90;
        Interactor.diaspasados = 20;
        Interactor.hora = Interactor.Calculos.calculoPrecioHora();
        Interactor.setNamefdef();

    }

    private Boolean comprobarInicio() {

        SharedPreferences preferences=getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);

        if (getDatabasePath(BASEDATOS)!=null && preferences.contains(PERFILACTIVO)){

            Interactor.perfila = preferences.getString(PERFILACTIVO, "Defecto");
            Interactor.prioridad = preferences.getBoolean(PRIORIDAD, true);
            Interactor.diaspasados = preferences.getInt(DIASPASADOS, 20);
            Interactor.diasfuturos = preferences.getInt(DIASFUTUROS, 90);
            Interactor.hora = Interactor.Calculos.calculoPrecioHora();
            Interactor.setNamefdef();

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

            Log.d("inicio", "borrado perfil setActivo de preferencias");
        }
        if (getDatabasePath(BASEDATOS)!=null){

            deleteDatabase(BASEDATOS);

            Log.d("inicio", "borrada" + BASEDATOS);
        }

        return false;
    }




}
