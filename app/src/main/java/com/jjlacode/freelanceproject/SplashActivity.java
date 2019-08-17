package com.jjlacode.freelanceproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import com.jjlacode.base.util.android.controls.ImagenLayout;
import com.jjlacode.base.util.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    ImagenLayout imagen;
    private DisplayMetrics metrics;
    private boolean land;
    private boolean tablet;
    private float densidad;
    private int anchoReal;
    private int altoReal;


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

        imagen = findViewById(R.id.imgsplash);
        imagen.setActivity(this);

        imagen.setImageResource(R.drawable.logo, anchoReal / 2, altoReal / 3);
        imagen.setVisibleTitulo(true);
        imagen.setVisiblePie(true);
        imagen.setTextPie(R.string.auth);
        imagen.setTextTitulo(R.string.app_name);
        imagen.setColorTextoTitulo(getResources().getColor(R.color.colorPrimary));
        imagen.setColorTextoPie(getResources().getColor(R.color.colorPrimary));
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
            }, 500);

    }


}
