package com.codevsolution.base.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.codevsolution.base.android.controls.ViewImagenLayout;
import com.codevsolution.base.login.LoginActivity;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.freemarketsapp.R;

public class SplashActivity extends AppCompatActivity {

    ViewImagenLayout imagen;
    private DisplayMetrics metrics;
    private boolean land;
    private boolean tablet;
    private float densidad;
    private int anchoReal;
    private int altoReal;
    private FrameLayout main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        land = getResources().getBoolean(R.bool.esLand);
        tablet = getResources().getBoolean(R.bool.esTablet);
        densidad = metrics.density;
        anchoReal = metrics.widthPixels;
        altoReal = metrics.heightPixels;

        main = findViewById(R.id.splash_main);
        imagen = new ViewImagenLayout(main, this);

        //imagen = findViewById(R.id.imgsplash);
        imagen.setActivity(this);

        imagen.setImageFirestore("/Config/logo_cds_512_A.png");
        //imagen.setImageResource(getResources().
        //                getIdentifier("logo_cds_512_a", "drawable",
        //                        getApplicationContext().getPackageName())
        //        , (int) (anchoReal * 0.9), (int) (altoReal * 0.5));
        Estilos.setLayoutParams(main, imagen.getLinearLayoutCompat(),
                (int) (anchoReal * 0.9), (int) (altoReal * 0.5), Gravity.CENTER);
        imagen.setVisibleTitulo(true);
        imagen.setVisiblePie(true);
        imagen.setTextPie(getResources().
                getIdentifier("auth", "string", getApplicationContext().getPackageName()));
        imagen.setTextTitulo(getResources().
                getIdentifier("app_name", "string", getApplicationContext().getPackageName()));
        imagen.setColorTextoTitulo(getResources().getColor(R.color.colorSecondaryDark));
        imagen.setColorTextoPie(getResources().getColor(R.color.colorSecondaryDark));
        imagen.setTextAutoSizeTitulo(this, 3f);
        imagen.setTextAutoSizePie(this, 1.5f);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                //intent.putExtra(INICIO,1);
                startActivity(intent);

                finish();

            }
        }, 1000);



    }


}
