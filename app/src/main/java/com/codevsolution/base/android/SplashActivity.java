package com.codevsolution.base.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.login.LoginActivity;
import com.codevsolution.freemarketsapp.R;

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

        imagen.setImageResource(getResources().
                        getIdentifier("logo_cds_512_a", "drawable",
                                getApplicationContext().getPackageName())
                , (int) (anchoReal * 0.9), (int) (altoReal * 0.5));
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
        }, 500);

    }


}
