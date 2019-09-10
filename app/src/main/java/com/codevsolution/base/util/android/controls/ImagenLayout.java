package com.codevsolution.base.util.android.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.codevsolution.base.util.interfaces.ICFragmentos;
import com.codevsolution.base.util.media.ImagenUtil;
import com.codevsolution.base.util.media.VisorImagen;
import com.codevsolution.freemarketsapp.R;

import java.io.ByteArrayInputStream;

import static com.codevsolution.base.util.JavaUtil.Constantes.PATH;
import static com.codevsolution.base.util.JavaUtil.Constantes.TIPO;

public class ImagenLayout extends RelativeLayout {

    TextView titulo;
    TextView pie;
    ImageView imagen;
    ImageButton btn;
    DisplayMetrics metrics = new DisplayMetrics();
    AppCompatActivity mainActivity;
    ICFragmentos icFragmentos;
    private String path;
    private boolean fire;

    public ImagenLayout(Context context) {
        super(context);
        inicializar();
    }

    public ImagenLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inicializar();

        setAtributos(attrs);
    }

    public ImagenLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inicializar();

        setAtributos(attrs);

    }

    private void inicializar() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li =
                (LayoutInflater) getContext().getSystemService(infService);
        li.inflate(R.layout.imagen_layout, this, true);

        imagen = findViewById(R.id.imglayout);
        titulo = findViewById(R.id.tituloImgLy);
        pie = findViewById(R.id.pieImgLy);
        btn = findViewById(R.id.btn_img);

        asignarEventos();

    }


    private void setAtributos(AttributeSet attrs) {

        TypedArray a =
                getContext().obtainStyledAttributes(attrs,
                        R.styleable.ImagenLayout);


        String txtTitulo = a.getString(R.styleable.ImagenLayout_titulo);
        String txtPie = a.getString(R.styleable.ImagenLayout_pie);
        int recTitulo = a.getInt(
                R.styleable.ImagenLayout_rectitulo, 0);
        int recPie = a.getInt(
                R.styleable.ImagenLayout_recpie, 0);


        if (recTitulo == 0) {
            setTextTitulo(txtTitulo);
        } else {
            setTextTitulo(recTitulo);
        }

        if (recPie == 0) {
            setTextPie(txtPie);
        } else {
            setTextPie(recPie);
        }

        a.recycle();

    }

    private void asignarEventos() {

        if (mainActivity != null) {

            setTextAutoSizePie(mainActivity);
            setTextAutoSizeTitulo(mainActivity);
        }

        imagen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString(PATH, path);
                bundle.putBoolean(TIPO, fire);
                System.out.println("fire = " + fire);

                if (icFragmentos != null) {
                    icFragmentos.enviarBundleAFragment(bundle, new VisorImagen());
                }

            }
        });
    }

    public void setMaxHeight(int maxHeight) {

        imagen.setMaxHeight(maxHeight);
    }

    public void setMaxWidth(int maxWidth) {
        imagen.setMaxWidth(maxWidth);
    }

    public void setFondoTitulo(int colorFondo) {

        titulo.setBackgroundColor(colorFondo);

    }

    public void setFondoPie(int colorFondo) {

        pie.setBackgroundColor(colorFondo);

    }

    public void setTextTitulo(String txtTitulo) {
        titulo.setText(txtTitulo);
    }

    public void setTextTitulo(int string) {
        titulo.setText(string);
    }

    public void setTextPie(String txtPie) {
        pie.setText(txtPie);
    }

    public void setTextPie(int string) {
        pie.setText(string);
    }

    public void setColorTextoTitulo(int colorTexto) {

        titulo.setTextColor(colorTexto);

    }

    public void setColorTextoPie(int colorTexto) {

        pie.setTextColor(colorTexto);

    }

    public void setActivity(AppCompatActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setIcfragmentos(ICFragmentos icfragmentos) {

        this.icFragmentos = icfragmentos;
    }

    public void setVisibleBtn() {

        btn.setVisibility(View.VISIBLE);
    }

    public void setGoneBtn() {

        btn.setVisibility(View.GONE);
    }

    public void setSizeTextTitulo(float sizeTextTitulo) {

        titulo.setTextSize(sizeTextTitulo);
    }

    public void setSizeTextPie(float sizeTextPie) {

        pie.setTextSize(sizeTextPie);
    }

    public void setGravedad(int gravedad) {

    }

    public void setTextAutoSizeTitulo(AppCompatActivity activityCompat, float multiplicador) {

        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int ancho = metrics.widthPixels;
        int alto = metrics.heightPixels;

        float size = ((float) ((alto * ancho) / (metrics.densityDpi * 300)) * multiplicador);
        titulo.setTextSize(size);

    }

    public void setTextAutoSizeTitulo(AppCompatActivity activityCompat) {

        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float ancho = metrics.widthPixels;
        float alto = metrics.heightPixels;

        float size = ((float) (alto * ancho) / (metrics.densityDpi * 300));
        titulo.setTextSize(size);

    }

    public void setTextAutoSizePie(AppCompatActivity activityCompat, float multiplicador) {

        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int ancho = metrics.widthPixels;
        int alto = metrics.heightPixels;

        float size = ((float) ((alto * ancho) / (metrics.densityDpi * 300)) * multiplicador);
        pie.setTextSize(size);
    }

    public void setTextAutoSizePie(AppCompatActivity activityCompat) {

        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float ancho = metrics.widthPixels;
        float alto = metrics.heightPixels;

        float size = ((float) (alto * ancho) / (metrics.densityDpi * 300));
        pie.setTextSize(size);

    }

    public void setTextAutoSizeTitulo(FragmentActivity activityCompat, float multiplicador) {

        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int ancho = metrics.widthPixels;
        int alto = metrics.heightPixels;

        float size = ((float) ((alto * ancho) / (metrics.densityDpi * 300)) * multiplicador);
        titulo.setTextSize(size);

    }

    public void setTextAutoSizeTitulo(FragmentActivity activityCompat) {

        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float ancho = metrics.widthPixels;
        float alto = metrics.heightPixels;

        float size = ((float) (alto * ancho) / (metrics.densityDpi * 300));
        titulo.setTextSize(size);

    }

    public void setTextAutoSizePie(FragmentActivity activityCompat, float multiplicador) {

        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int ancho = metrics.widthPixels;
        int alto = metrics.heightPixels;

        float size = ((float) ((alto * ancho) / (metrics.densityDpi * 300)) * multiplicador);
        pie.setTextSize(size);
    }

    public void setTextAutoSizePie(FragmentActivity activityCompat) {

        activityCompat.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float ancho = metrics.widthPixels;
        float alto = metrics.heightPixels;

        float size = ((float) (alto * ancho) / (metrics.densityDpi * 300));
        pie.setTextSize(size);

    }


    public CharSequence getTextTitulo() {
        return titulo.getText();
    }

    public CharSequence getTextPie() {
        return pie.getText();
    }

    public void setVisibleTitulo(boolean visible) {

        if (visible) {
            titulo.setVisibility(VISIBLE);
        } else {
            titulo.setVisibility(GONE);
        }

    }

    public void setVisiblePie(boolean visible) {

        if (visible) {
            titulo.setVisibility(VISIBLE);
        } else {
            titulo.setVisibility(GONE);
        }

    }

    public void setImage(String uri) {

        path = uri;
        ImagenUtil.setImage(uri, imagen);
    }

    public void setImageUri(String uri) {

        path = uri;
        ImagenUtil.setImageUri(uri, imagen);
    }

    public void setImageUri(String uri, float multiplicador) {

        path = uri;
        ImagenUtil.setImageUri(uri, imagen, multiplicador);
    }

    public void setImageUri(String uri, int ancho, int alto) {

        path = uri;
        ImagenUtil.setImageUri(uri, imagen, ancho, alto);
    }

    public void setImageUriPerfil(AppCompatActivity mainActivity, String uri) {

        path = uri;
        float ancho;
        float alto;

        if (mainActivity != null) {

            mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            ancho = metrics.widthPixels;
            alto = metrics.heightPixels;
            ImagenUtil.setImageUri(uri, imagen, (int) (ancho * 0.25), (int) (alto * 0.20));
            System.out.println("imagenAuto");

        } else {
            ImagenUtil.setImageUri(uri, imagen);
            System.out.println("imagenNormal");

        }
    }

    public void setImageResourcePerfil(AppCompatActivity mainActivity, int recurso) {

        float ancho;
        float alto;

        if (mainActivity != null) {

            mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            ancho = metrics.widthPixels;
            alto = metrics.heightPixels;
            ImagenUtil.setImageUri(recurso, imagen, (int) (ancho * 0.25), (int) (alto * 0.20));
            System.out.println("imagenAuto");

        } else {
            ImagenUtil.setImageUri(recurso, imagen, 1);
            System.out.println("imagenNormal");

        }
    }

    public void setImageFirestorePerfil(AppCompatActivity mainActivity, String uri) {

        path = uri;
        fire = true;
        float ancho;
        float alto;

        if (mainActivity != null) {

            mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            ancho = metrics.widthPixels;
            alto = metrics.heightPixels;
            ImagenUtil.setImageFireStore(uri, imagen, (int) (ancho * 0.25), (int) (alto * 0.20));
            System.out.println("imagenAuto");

        } else {
            ImagenUtil.setImageUri(uri, imagen);
            System.out.println("imagenNormal");

        }
    }

    public void setImageUriCard(AppCompatActivity mainActivity, String uri) {

        path = uri;
        float ancho;
        float alto;

        if (mainActivity != null) {

            mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            ancho = metrics.widthPixels;
            alto = metrics.heightPixels;
            ImagenUtil.setImageUriCircle(uri, imagen, (int) (ancho * 0.25), (int) (alto * 0.20));
            System.out.println("imagenAuto");

        } else {
            ImagenUtil.setImageUriCircle(uri, imagen);
            System.out.println("imagenNormal");

        }
    }

    public void setImageResourceCard(AppCompatActivity mainActivity, int recurso) {

        float ancho;
        float alto;

        if (mainActivity != null) {

            mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            ancho = metrics.widthPixels;
            alto = metrics.heightPixels;
            ImagenUtil.setImageUriCircle(recurso, imagen, (int) (ancho * 0.25), (int) (alto * 0.20));
            System.out.println("imagenAuto");

        } else {
            ImagenUtil.setImageUriCircle(recurso, imagen, 1);
            System.out.println("imagenNormal");

        }
    }


    public void setImageUri(String uri, int resourcedef) {

        path = uri;
        ImagenUtil.setImageUri(uri, imagen, resourcedef);
    }

    public void setImageResource(int recurso) {

        ImagenUtil.setImageUri(recurso, imagen, 1f);
    }

    public void setImageResource(int recurso, int ancho, int alto) {

        ImagenUtil.setImageUri(recurso, imagen, ancho, alto);
    }

    public void setImageResource(int recurso, float multiplicador) {

        ImagenUtil.setImageUri(recurso, imagen, multiplicador);
    }

    public void setImageFirestore(String uri) {

        path = uri;
        fire = true;
        ImagenUtil.setImageFireStore(uri, imagen);
    }

    public void setImageFirestoreCircle(String uri) {

        path = uri;
        fire = true;
        ImagenUtil.setImageFireStoreCircle(uri, imagen);
    }

    public void setImageFirestore(String uri, int drabable) {

        path = uri;
        fire = true;
        ImagenUtil.setImageFireStore(uri, imagen, drabable);
    }

    public void setImageUriCircle(String uri) {

        path = uri;
        ImagenUtil.setImageUriCircle(uri, imagen);
    }

    public ByteArrayInputStream getInputStream(int ancho, int alto) {

        return ImagenUtil.bitmapToInputStream(imagen, ancho, alto);
    }

    public ByteArrayInputStream getInputStream(String path, double factorAncho, double factorAlto) {

        this.path = path;
        imagen.setImageURI(Uri.parse(path));
        return ImagenUtil.bitmapToInputStream(imagen, factorAncho, factorAlto);
    }

    public ByteArrayInputStream getInputStream(String path, double factorReduccion) {

        this.path = path;
        imagen.setImageURI(Uri.parse(path));
        return ImagenUtil.bitmapToInputStream(imagen, factorReduccion);
    }

    public ByteArrayInputStream getInputStreamAuto(String path) {

        this.path = path;
        imagen.setImageURI(Uri.parse(path));

        int ancho = imagen.getWidth();
        System.out.println("ancho = " + ancho);
        float factorReduccion = 1.0f;

        if (ancho > 2000) {
            factorReduccion = 0.1f;
        } else if (ancho > 1000) {
            factorReduccion = 0.2f;
        } else if (ancho > 500) {
            factorReduccion = 0.6f;
        } else if (ancho > 250) {
            factorReduccion = 0.8f;
        }

        System.out.println("factorReduccion = " + factorReduccion);

        return ImagenUtil.bitmapToInputStream(imagen, 250, 250);
    }

    View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

}
