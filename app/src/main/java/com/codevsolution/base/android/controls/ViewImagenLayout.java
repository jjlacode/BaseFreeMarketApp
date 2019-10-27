package com.codevsolution.base.android.controls;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentActivity;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.interfaces.ICFragmentos;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.media.VisorImagen;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.TimeDateUtil;

import java.io.ByteArrayInputStream;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.MINUTOSLONG;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.PATH;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.PERSISTENCIA;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.TIPO;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.TSIMG;

public class ViewImagenLayout implements Estilos.Constantes {

    ViewGroupLayout linearLayoutCompat;
    TextView titulo;
    TextView pie;
    ImageView imagen;
    ImageButton btn;
    Button btnTxt;
    DisplayMetrics metrics = new DisplayMetrics();
    AppCompatActivity mainActivity;
    ICFragmentos icFragmentos;
    private String path;
    private boolean fire;
    long timestamp;
    Context context;
    ViewGroup viewGroup;

    public ViewImagenLayout(ViewGroup viewGroup, Context context) {
        this.context = context;
        this.viewGroup = viewGroup;
        inicializar();
    }

    private void inicializar() {

        setViewGroup();
        asignarEventos();

    }

    public void setViewGroup() {

        linearLayoutCompat = new ViewGroupLayout(context,viewGroup);
        linearLayoutCompat.setOrientacion(LinearLayoutCompat.VERTICAL);

        titulo = linearLayoutCompat.addTextView(null);
        titulo.setVisibility(View.GONE);

        imagen = new ImageView(context);
        imagen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        linearLayoutCompat.addVista(imagen);

        pie = linearLayoutCompat.addTextView(null);
        pie.setVisibility(View.GONE);

        btn = linearLayoutCompat.addImageButtonSecundary(context.getResources().
                getIdentifier("ic_search_black_24dp", DRAWABLE, context.getPackageName()));
        btn.setVisibility(View.GONE);
        btn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        btnTxt = linearLayoutCompat.addButtonSecondary(null);
        btnTxt.setVisibility(View.GONE);

    }

    private void asignarEventos() {

        if (mainActivity != null) {

            if (pie.getVisibility() == View.VISIBLE) {
                setTextAutoSizePie(mainActivity);
            }
            if (titulo.getVisibility() == View.VISIBLE) {
                setTextAutoSizeTitulo(mainActivity);
            }
        }

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
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

    public LinearLayoutCompat getLinearLayoutCompat(){
        return (LinearLayoutCompat) linearLayoutCompat.getViewGroup();
    }

    public ImageView getImagen() {
        return imagen;
    }

    public TextView getTitulo() {
        return titulo;
    }

    public TextView getPie() {
        return pie;
    }

    public ImageButton getBtn() {
        return btn;
    }

    public Button getBtnTxt() {
        return btnTxt;
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
        titulo.setVisibility(View.VISIBLE);
        titulo.setText(txtTitulo);
    }

    public void setTextTitulo(int string) {

        titulo.setVisibility(View.VISIBLE);
        titulo.setText(string);
    }

    public void setTextPie(String txtPie) {
        pie.setVisibility(View.VISIBLE);
        pie.setText(txtPie);
    }

    public void setTextPie(int string) {
        pie.setVisibility(View.VISIBLE);
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

    public void setVisibleBtnTxt() {

        btnTxt.setVisibility(View.VISIBLE);
    }

    public void setGoneBtnTxt() {

        btnTxt.setVisibility(View.GONE);
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

        float size = ((alto * ancho) / (metrics.densityDpi * 300));
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

        float size = ((alto * ancho) / (metrics.densityDpi * 300));
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

        float size = ((alto * ancho) / (metrics.densityDpi * 300));
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

        float size = ((alto * ancho) / (metrics.densityDpi * 300));
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
            titulo.setVisibility(View.VISIBLE);
        } else {
            titulo.setVisibility(View.GONE);
        }

    }

    public void setVisiblePie(boolean visible) {

        if (visible) {
            titulo.setVisibility(View.VISIBLE);
        } else {
            titulo.setVisibility(View.GONE);
        }

    }

    public void setBackground(String color){

        getLinearLayoutCompat().setBackground(context.getResources().getDrawable(context.getResources().
                getIdentifier(color, COLOR,
                        context.getPackageName())));
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
            ImagenUtil.setImageUri(uri, imagen, (int) (ancho), (int) (alto * 0.40));
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
            ImagenUtil.setImageUri(recurso, imagen, (int) (ancho), (int) (alto * 0.40));
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
            long ts = AndroidUtil.getSharePreference(AppActivity.getAppContext(), PERSISTENCIA, TSIMG, TimeDateUtil.ahora());
            if (ts + (15 * MINUTOSLONG) > TimeDateUtil.ahora()) {
                ImagenUtil.setImageFireStore(uri, imagen, (int) (ancho), (int) (alto * 0.40), true);
            } else {
                ImagenUtil.setImageFireStore(uri, imagen, (int) (ancho), (int) (alto * 0.40));
            }
            System.out.println("imagenAuto");

        } else {
            ImagenUtil.setImageFireStore(uri, imagen);
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
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.
                    LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            imagen.setLayoutParams(params);
            ImagenUtil.setImageUriCircle(uri, imagen);
            imagen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imagen.setAdjustViewBounds(true);

            System.out.println("imagenAuto");

        } else {
            ImagenUtil.setImageUriCircle(uri, imagen);
            System.out.println("imagenNormal");

        }
    }

    public void setImageUriCard(AppCompatActivity mainActivity, String uri, float multi) {

        path = uri;
        float ancho;
        float alto;

        if (mainActivity != null) {

            mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            ancho = metrics.widthPixels;
            alto = metrics.heightPixels;

            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.
                    LayoutParams((int)((ancho/100)*multi), ViewGroup.LayoutParams.MATCH_PARENT);
            getLinearLayoutCompat().setLayoutParams(params);

            ImagenUtil.setImageUriCircle(uri, imagen);
            imagen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imagen.setAdjustViewBounds(true);

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
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.
                    LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            imagen.setLayoutParams(params);
            ImagenUtil.setImageUriCircle(recurso, imagen);
            imagen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imagen.setAdjustViewBounds(true);

            System.out.println("imagenAuto");

        } else {
            ImagenUtil.setImageUriCircle(recurso, imagen, 1);
            System.out.println("imagenNormal");

        }
    }

    public void setImageResourceCard(AppCompatActivity mainActivity, int recurso, float multi) {

        float ancho;
        float alto;

        if (mainActivity != null) {

            mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            ancho = metrics.widthPixels;
            alto = metrics.heightPixels;
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.
                    LayoutParams((int)((ancho/100)*multi), ViewGroup.LayoutParams.MATCH_PARENT);
            getLinearLayoutCompat().setLayoutParams(params);

            params = new LinearLayoutCompat.
                    LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1);
            params.gravity = Gravity.CENTER;
            imagen.setLayoutParams(params);

            ImagenUtil.setImageUriCircle(recurso, imagen);


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

    public void setImageResource(Context context, int recurso) {

        ImagenUtil.setImageUri(context, recurso, imagen, 1f);
    }

    public void setImageResource(int recurso, int ancho, int alto) {

        ImagenUtil.setImageUri(recurso, imagen, ancho, alto);
    }

    public void setImageResource(int recurso, float multiplicador) {

        ImagenUtil.setImageUri(recurso, imagen, multiplicador);
    }

    public void setImageResource(Context context, int recurso, float multiplicador) {

        ImagenUtil.setImageUri(context, recurso, imagen, multiplicador);
    }

    public void setImageFirestore(String uri) {

        path = uri;
        fire = true;
        long ts = AndroidUtil.getSharePreference(AppActivity.getAppContext(), PERSISTENCIA, TSIMG, TimeDateUtil.ahora());
        if (ts + (15 * MINUTOSLONG) > TimeDateUtil.ahora()) {
            ImagenUtil.setImageFireStore(uri, imagen, true);
        } else {
            ImagenUtil.setImageFireStore(uri, imagen);
        }
    }

    public void setImageFirestore(String uri, boolean cache) {

        path = uri;
        fire = true;
        ImagenUtil.setImageFireStore(uri, imagen, cache);
    }

    public void setImageFirestoreCircle(String uri) {

        path = uri;
        fire = true;
        long ts = AndroidUtil.getSharePreference(AppActivity.getAppContext(), PERSISTENCIA, TSIMG, TimeDateUtil.ahora());
        if (ts + (15 * MINUTOSLONG) > TimeDateUtil.ahora()) {
            ImagenUtil.setImageFireStoreCircle(uri, imagen, true);
        } else {
            ImagenUtil.setImageFireStoreCircle(uri, imagen);
        }
    }

    public void setImageFirestoreCircle(String uri, boolean cache) {

        path = uri;
        fire = true;
        ImagenUtil.setImageFireStoreCircle(uri, imagen, cache);
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
