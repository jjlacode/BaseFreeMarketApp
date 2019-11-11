package com.codevsolution.base.android.controls;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codevsolution.base.style.Estilos;

public class RatingBarLayout implements Estilos.Constantes {

    ViewGroup parent;
    Context context;
    RatingBar ratingBarMain;
    ViewGroupLayout vistaMain;
    TextView textView;
    ImageButton imageButton;
    boolean small;
    boolean indicador;
    private float votoUser;
    private ViewGroupLayout vistaRatingBarMain;


    public RatingBarLayout(Context context, ViewGroup parent, boolean indicador, boolean small) {

        this.context = context;
        this.parent = parent;
        this.indicador = indicador;
        this.small = small;
        inicio();
    }

    protected void inicio() {

        vistaMain = new ViewGroupLayout(context, parent);
        vistaMain.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
        vistaRatingBarMain = new ViewGroupLayout(context, vistaMain.getViewGroup());
        Estilos.setLayoutParams(vistaMain.getViewGroup(), vistaRatingBarMain.getViewGroup(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT, 1, 0);

        if (small) {

            ratingBarMain = (RatingBar) vistaRatingBarMain.addVista(new RatingBar(context, null, android.R.attr.ratingBarStyleSmall));
            ratingBarMain.setIsIndicator(indicador);
            Estilos.setLayoutParams(vistaRatingBarMain.getViewGroup(), ratingBarMain, ViewGroupLayout.WRAP_CONTENT, ViewGroupLayout.WRAP_CONTENT);
            ratingBarMain.setNumStars(5);
            ratingBarMain.setRating(0);
            ratingBarMain.setStepSize(1);
            ratingBarMain.setProgressBackgroundTintList(ColorStateList.valueOf(Estilos.getColor(context, "color_star_defecto")));
            ratingBarMain.setSecondaryProgressTintList(ColorStateList.valueOf(Estilos.getColor(context, "Color_star_small_ok")));
            ratingBarMain.setProgressTintList(ColorStateList.valueOf(Estilos.getColor(context, "Color_star_ok")));

        } else {
            ratingBarMain = (RatingBar) vistaRatingBarMain.addVista(new RatingBar(context));
            ratingBarMain.setIsIndicator(indicador);
            Estilos.setLayoutParams(vistaRatingBarMain.getViewGroup(), ratingBarMain, ViewGroupLayout.WRAP_CONTENT, ViewGroupLayout.WRAP_CONTENT);
            ratingBarMain.setNumStars(5);
            ratingBarMain.setRating(0);
            ratingBarMain.setStepSize(1);
            ratingBarMain.setSecondaryProgressTintList(ColorStateList.valueOf(Estilos.getColor(context, "color_star_defecto")));

        }

        ratingBarMain.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean fromUser) {

                if (fromUser) {

                    String color = "";
                    if (v > 3) {
                        color = "Color_star_ok";
                    } else if (v > 1.5) {
                        color = "Color_star_acept";
                    } else if (v > 0) {
                        color = "Color_star_notok";
                    } else {
                        color = "color_star_defecto";
                    }

                    ratingBarMain.setProgressTintList(ColorStateList.valueOf(Estilos.getColor(context, color)));

                    textView.setText(String.valueOf((int) (v)));
                    votoUser = v;
                }
            }
        });
        textView = vistaMain.addTextView("clasificacion", 2);
        textView.setVisibility(View.GONE);
        imageButton = vistaMain.addImageButtonSecundary(Estilos.getIdDrawable(context, "ic_votar_indigo"), 2);
        imageButton.setVisibility(View.GONE);

    }

    public void setBarNumStars(int numStars) {
        ratingBarMain.setNumStars(numStars);
    }

    public void setBarRating(float rating) {

        ratingBarMain.setRating(rating);
        String color = "";
        if (rating > 3) {
            color = "Color_star_ok";
        } else if (rating > 1.5) {
            color = "Color_star_acept";
        } else if (rating > 0) {
            color = "Color_star_notok";
        } else {
            color = "color_star_defecto";
        }

        ratingBarMain.setProgressTintList(ColorStateList.valueOf(Estilos.getColor(context, color)));
    }

    public void setBarStepSize(float stepSize) {
        ratingBarMain.setStepSize(stepSize);
    }

    public Drawable getRatingBarProgressDrawable() {
        return ratingBarMain.getProgressDrawable();
    }

    public void setSecondaryProgressTintList(String color) {
        ratingBarMain.setSecondaryProgressTintList(ColorStateList.valueOf(Estilos.getColor(context, color)));
    }

    public void setProgressTintList(String color) {
        ratingBarMain.setProgressTintList(ColorStateList.valueOf(Estilos.getIdColor(context, color)));
    }

    public void setProgressBackgoundTintList(String color) {
        ratingBarMain.setProgressBackgroundTintList(ColorStateList.valueOf(Estilos.getColor(context, color)));
    }

    public void setTextViewGravity(int gravity) {
        textView.setGravity(gravity);
    }

    public void setVisibilidadVoto(boolean visible) {

        if (visible) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public void setVoto(float voto) {
        votoUser = voto;
        textView.setText(String.valueOf(voto));
        setBarRating(voto);
    }

    public void setVisibilidadTexto(boolean visible) {
        if (visible) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public void setVisibilidadImageButton(boolean visible) {
        if (visible) {
            imageButton.setVisibility(View.VISIBLE);
        } else {
            imageButton.setVisibility(View.GONE);
        }
    }

    public void setRecursoImageButton(int recurso) {
        imageButton.setImageResource(recurso);
    }

    public void setDrawableImageButton(Drawable drawable) {
        imageButton.setImageDrawable(drawable);
    }

    public void setTextViewText(String texto) {
        textView.setText(texto);
    }

    public void setTextViewText(int recursoString) {
        textView.setText(recursoString);
    }

    public void setTextViewBackground(String color) {
        textView.setBackgroundColor(Estilos.getColor(context, color));
    }

    public void setTextViewTextColor(String color) {
        textView.setTextColor(ColorStateList.valueOf(Estilos.getColor(context, color)));
    }

    public void setImageButtonOnClickListener(View.OnClickListener onClickListener) {
        imageButton.setOnClickListener(onClickListener);
    }

    public void setOnRatingBarChangeListener(RatingBar.OnRatingBarChangeListener onRatingBarChangeListener) {
        ratingBarMain.setOnRatingBarChangeListener(onRatingBarChangeListener);
    }

    public float getVotoUser() {
        return votoUser;
    }

    public View getViewGroup() {
        return vistaMain.getViewGroup();
    }
}
