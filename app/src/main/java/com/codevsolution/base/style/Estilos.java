package com.codevsolution.base.style;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.codevsolution.base.android.MainActivityBase;
import com.codevsolution.freemarketsapp.R;

import static com.codevsolution.base.style.Estilos.Constantes.ATTR;
import static com.codevsolution.base.style.Estilos.Constantes.BOOL;
import static com.codevsolution.base.style.Estilos.Constantes.BTNPRIMARY;
import static com.codevsolution.base.style.Estilos.Constantes.BTNSECONDARY;
import static com.codevsolution.base.style.Estilos.Constantes.BTNTRANSPARENTE;
import static com.codevsolution.base.style.Estilos.Constantes.COLOR;
import static com.codevsolution.base.style.Estilos.Constantes.COLORPRIMARY;
import static com.codevsolution.base.style.Estilos.Constantes.COLORSECONDARY;
import static com.codevsolution.base.style.Estilos.Constantes.COLORSECONDARYDARK;
import static com.codevsolution.base.style.Estilos.Constantes.DIMENS;
import static com.codevsolution.base.style.Estilos.Constantes.DRAWABLE;
import static com.codevsolution.base.style.Estilos.Constantes.ID;
import static com.codevsolution.base.style.Estilos.Constantes.LAYOUT;
import static com.codevsolution.base.style.Estilos.Constantes.MATCH_PARENT;
import static com.codevsolution.base.style.Estilos.Constantes.MENU;
import static com.codevsolution.base.style.Estilos.Constantes.PBARSTYLEACEPT;
import static com.codevsolution.base.style.Estilos.Constantes.RAW;
import static com.codevsolution.base.style.Estilos.Constantes.STRING;
import static com.codevsolution.base.style.Estilos.Constantes.STYLE;
import static com.codevsolution.base.style.Estilos.Constantes.WRAP_CONTENT;
import static com.codevsolution.base.style.Estilos.Constantes.XML;

public class Estilos {

    protected boolean multiPanel;
    protected int sizeTextD;
    public static int colorPrimary = Color.parseColor("#ffffff");
    public static int colorSecondary = Color.parseColor("#00ffff");
    public static int colorSecondaryDark = Color.parseColor("#3f51b5");

    public interface Constantes {

        int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
        int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
        int ORI_LLC_VERTICAL = LinearLayoutCompat.VERTICAL;
        int ORI_LLC_HORIZONTAL = LinearLayoutCompat.HORIZONTAL;
        int ORI_LL_VERTICAL = LinearLayout.VERTICAL;
        int ORI_LL_HORIZONTAL = LinearLayout.HORIZONTAL;
        String COLOR = "color";
        String DRAWABLE = "drawable";
        String STRING = "string";
        String BOOL = "bool";
        String LAYOUT = "layout";
        String STYLE = "style";
        String DIMENS = "dimen";
        String RAW = "raw";
        String XML = "xml";
        String MENU = "menu";
        String ID = "id";
        String ATTR = "attr";
        String BTNPRIMARY = "boton_redondo_primary";
        String BTNSECONDARY = "boton_redondo_secondary";
        String BTNTRANSPARENTE = "boton_redondo_blanco";
        String COLORPRIMARY = "colorPrimary";
        String COLORSECONDARY = "colorSecondary";
        String COLORSECONDARYDARK = "colorSecondaryDark";
        String COLOREDITVACIO = "Color_edit_vacio";
        String COLORTEXTO = "Color_texto";
        String PBARSTYLEACEPT = "ProgressBarStyleAcept";

    }


    private static DisplayMetrics getMetrics(MainActivityBase activityBase) {

        DisplayMetrics metrics = new DisplayMetrics();
        activityBase.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics;

    }

    public static boolean getLand(Context context) {
        return context.getResources().getBoolean(R.bool.esLand);
    }

    public static boolean getTablet(Context context) {

        return context.getResources().getBoolean(R.bool.esTablet);
    }

    public static float getSizeText(MainActivityBase activityBase) {

        DisplayMetrics metrics = getMetrics(activityBase);
        return ((float) (metrics.widthPixels + metrics.heightPixels + metrics.densityDpi) / (100));

    }

    public static float getAltoBoton(MainActivityBase activityBase, float vecesTexto) {

        DisplayMetrics metrics = getMetrics(activityBase);
        float altoBtn = ((float) (metrics.widthPixels + metrics.heightPixels + metrics.densityDpi) / 100)
                * vecesTexto * metrics.density;
        if (altoBtn < 30) {
            altoBtn = 30;
        }
        return altoBtn;
    }

    public static float getSizeTextReal(MainActivityBase activityBase) {

        DisplayMetrics metrics = getMetrics(activityBase);
        return ((float) (metrics.widthPixels + metrics.heightPixels + metrics.densityDpi) / (100))
                * metrics.density;
    }

    public static float getSizeTextReal(MainActivityBase activityBase, float vecesTexto) {

        DisplayMetrics metrics = getMetrics(activityBase);
        return ((float) (metrics.widthPixels + metrics.heightPixels + metrics.densityDpi) / (100))
                * metrics.density * vecesTexto;
    }

    public static float getDensidad(MainActivityBase activityBase) {

        DisplayMetrics metrics = getMetrics(activityBase);
        return metrics.density;

    }

    public static int getAncho(MainActivityBase activityBase) {

        DisplayMetrics metrics = getMetrics(activityBase);
        return (int) (metrics.widthPixels / metrics.density);

    }

    public static int getAlto(MainActivityBase activityBase) {

        DisplayMetrics metrics = getMetrics(activityBase);
        return (int) (metrics.heightPixels / metrics.density);

    }

    public static int getAnchoReal(MainActivityBase activityBase) {

        DisplayMetrics metrics = getMetrics(activityBase);
        return metrics.widthPixels;

    }

    public static int getAltoReal(MainActivityBase activityBase) {

        DisplayMetrics metrics = getMetrics(activityBase);
        return metrics.heightPixels;

    }

    public static int getDensidadDpi(MainActivityBase activityBase) {

        DisplayMetrics metrics = getMetrics(activityBase);
        return metrics.densityDpi;

    }

    public boolean esMultiPanel(MainActivityBase activityBase) {
        DisplayMetrics metrics = getMetrics(activityBase);
        return ((float) metrics.densityDpi / (float) metrics.widthPixels) < 0.30;
    }

    public static void setLayoutParams(View view) {

        ViewGroup viewGroup = ((ViewGroup) view.getParent());

        if (viewGroup instanceof LinearLayoutCompat) {
            LinearLayoutCompat.LayoutParams params;
            params = new LinearLayoutCompat.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof LinearLayout) {
            LinearLayout.LayoutParams params;
            params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof CardView) {
            CardView.LayoutParams params;
            params = new CardView.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof FrameLayout) {
            FrameLayout.LayoutParams params;
            params = new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        }

    }

    public static void setLayoutParams(View view, float peso) {

        ViewGroup viewGroup = ((ViewGroup) view.getParent());

        if (viewGroup instanceof LinearLayoutCompat) {
            LinearLayoutCompat.LayoutParams params;
            if (((LinearLayoutCompat) viewGroup).getOrientation() == Constantes.ORI_LLC_VERTICAL) {
                params = new LinearLayoutCompat.LayoutParams(MATCH_PARENT, MATCH_PARENT, peso);
            } else {
                params = new LinearLayoutCompat.LayoutParams(MATCH_PARENT, WRAP_CONTENT, peso);
            }
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof LinearLayout) {
            LinearLayout.LayoutParams params;
            if (((LinearLayout) viewGroup).getOrientation() == Constantes.ORI_LL_VERTICAL) {
                params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, peso);
            } else {
                params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, peso);
            }
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof CardView) {
            CardView.LayoutParams params;
            params = new CardView.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof FrameLayout) {
            FrameLayout.LayoutParams params;
            params = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        }

    }


    public static void setLayoutParams(ViewGroup viewGroup, View view) {

        if (viewGroup instanceof LinearLayoutCompat) {
            LinearLayoutCompat.LayoutParams params;
            params = new LinearLayoutCompat.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof LinearLayout) {
            LinearLayout.LayoutParams params;
            params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof CardView) {
            CardView.LayoutParams params;
            params = new CardView.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof FrameLayout) {
            FrameLayout.LayoutParams params;
            params = new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        }

    }

    public static void setLayoutParams(ViewGroup viewGroup, View view, float peso) {

        if (viewGroup instanceof LinearLayoutCompat) {
            LinearLayoutCompat.LayoutParams params;
            if (((LinearLayoutCompat) viewGroup).getOrientation() == Constantes.ORI_LLC_VERTICAL) {
                params = new LinearLayoutCompat.LayoutParams(MATCH_PARENT, MATCH_PARENT, peso);
            } else {
                params = new LinearLayoutCompat.LayoutParams(MATCH_PARENT, WRAP_CONTENT, peso);
            }
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof LinearLayout) {
            LinearLayout.LayoutParams params;
            if (((LinearLayout) viewGroup).getOrientation() == Constantes.ORI_LL_VERTICAL) {
                params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, peso);
            } else {
                params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, peso);
            }
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof CardView) {
            CardView.LayoutParams params;
            params = new CardView.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof FrameLayout) {
            FrameLayout.LayoutParams params;
            params = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        }

    }


    public static void setLayoutParams(ViewGroup viewGroup, View view, int ancho, int alto, float peso, int pad) {

        if (viewGroup instanceof LinearLayoutCompat) {
            LinearLayoutCompat.LayoutParams params;
            params = new LinearLayoutCompat.LayoutParams(ancho, alto, peso);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof LinearLayout) {
            LinearLayout.LayoutParams params;
            params = new LinearLayout.LayoutParams(ancho, alto, peso);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof CardView) {
            CardView.LayoutParams params;
            params = new CardView.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof FrameLayout) {
            FrameLayout.LayoutParams params;
            params = new FrameLayout.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        }


    }

    public static void setLayoutParams(ViewGroup viewGroup, View view, int ancho, int alto) {

        int pad = 5;
        if (viewGroup instanceof LinearLayoutCompat) {
            LinearLayoutCompat.LayoutParams params;
            params = new LinearLayoutCompat.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof LinearLayout) {
            LinearLayout.LayoutParams params;
            params = new LinearLayout.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof CardView) {
            CardView.LayoutParams params;
            params = new CardView.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof FrameLayout) {
            FrameLayout.LayoutParams params;
            params = new FrameLayout.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        }


    }

    public static void setLayoutParams(ViewGroup viewGroup, View view, int ancho, int alto, int gravity) {

        int pad = 5;
        if (viewGroup instanceof LinearLayoutCompat) {
            LinearLayoutCompat.LayoutParams params;
            params = new LinearLayoutCompat.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof LinearLayout) {
            LinearLayout.LayoutParams params;
            params = new LinearLayout.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof CardView) {
            CardView.LayoutParams params;
            params = new CardView.LayoutParams(ancho, alto, gravity);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof FrameLayout) {
            FrameLayout.LayoutParams params;
            params = new FrameLayout.LayoutParams(ancho, alto, gravity);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        }


    }

    public static void setLayoutParamsRelative(ViewGroup viewGroup, View view, int ancho, int alto, int metodo, int recurso) {

        int pad = 5;
        if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            if (recurso > 0) {
                params.addRule(metodo, recurso);
            } else {
                params.addRule(metodo);
            }
            viewGroup.addView(view, params);
        }


    }

    public static void setLayoutParamsRelative(ViewGroup viewGroup, View view, int ancho, int alto, int[] metodo, int[] recurso) {

        int pad = 5;
        if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            for (int i = 0; i < metodo.length; i++) {

                if (recurso[i] > 0) {
                    params.addRule(metodo[i], recurso[i]);
                } else {
                    params.addRule(metodo[i]);
                }
            }

            viewGroup.addView(view, params);
        }


    }

    public static void setLayoutParamsRelative(ViewGroup viewGroup, View view, int ancho, int alto, int[] metodo) {

        int pad = 5;
        if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            for (int i = 0; i < metodo.length; i++) {

                params.addRule(metodo[i]);
            }

            viewGroup.addView(view, params);
        }


    }

    public static void setLayoutParamsRelative(ViewGroup viewGroup, View view, int ancho, int alto, int metodo) {

        int pad = 5;
        if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(ancho, alto);
            params.setMargins(pad, pad, pad, pad);
            params.addRule(metodo);
            viewGroup.addView(view, params);
        }


    }

    public static int getColor(Context context, String color) {
        return context.getResources().getColor(getIdColor(context, color));
    }

    public static Drawable getDrawable(Context context, String drawable) {
        return ContextCompat.getDrawable(context, getIdDrawable(context, drawable));
    }

    public static String getString(Context context, String string) {
        return context.getResources().getString(getIdString(context, string));
    }

    public static boolean getBool(Context context, String string) {
        return context.getResources().getBoolean(getIdBool(context, string));
    }

    public static XmlResourceParser getLayout(Context context, String layout) {
        return context.getResources().getLayout(getIdLayout(context, layout));
    }

    public static int getIdDrawable(Context context, String nombre) {
        return context.getResources()
                .getIdentifier(nombre, DRAWABLE, context.getPackageName());
    }

    public static int getIdString(Context context, String nombre) {
        return context.getResources()
                .getIdentifier(nombre, STRING, context.getPackageName());
    }

    public static int getIdBool(Context context, String nombre) {
        return context.getResources()
                .getIdentifier(nombre, BOOL, context.getPackageName());
    }

    public static int getIdMenu(Context context, String nombre) {
        return context.getResources()
                .getIdentifier(nombre, MENU, context.getPackageName());
    }

    public static int getIdRaw(Context context, String nombre) {
        return context.getResources()
                .getIdentifier(nombre, RAW, context.getPackageName());
    }

    public static int getIdXml(Context context, String nombre) {
        return context.getResources()
                .getIdentifier(nombre, XML, context.getPackageName());
    }

    public static int getIdColor(Context context, String nombre) {
        return context.getResources()
                .getIdentifier(nombre, COLOR, context.getPackageName());
    }

    public static int getIdLayout(Context context, String nombre) {
        return context.getResources()
                .getIdentifier(nombre, LAYOUT, context.getPackageName());
    }

    public static int getIdStyle(Context context, String nombre) {
        return context.getResources()
                .getIdentifier(nombre, STYLE, context.getPackageName());
    }

    public static int getIdAttribute(Context context, String nombre) {
        return context.getResources().getIdentifier(nombre, ATTR, context.getPackageName());
    }

    public static int getIdDimens(Context context, String nombre) {
        return context.getResources()
                .getIdentifier(nombre, DIMENS, context.getPackageName());
    }

    public static int getIdResource(Context context, String nombre) {
        return context.getResources()
                .getIdentifier(nombre, ID, context.getPackageName());
    }


    public static Drawable btnSecondary(Context context) {
        return ContextCompat.getDrawable(context, getIdDrawable(context, BTNSECONDARY));
    }

    public static Drawable btnPrimary(Context context) {
        return ContextCompat.getDrawable(context, getIdDrawable(context, BTNPRIMARY));
    }

    public static Drawable btnTrans(Context context) {
        return ContextCompat.getDrawable(context, getIdDrawable(context, BTNTRANSPARENTE));
    }

    public static float getDimension(Context context, String nombre) {

        return context.getResources().getDimension(getIdDimens(context, nombre));
    }

    public static int colorPrimary(Context context) {
        return getIdColor(context, COLORPRIMARY);
    }

    public static int colorSecondary(Context context) {
        return getIdColor(context, COLORSECONDARY);
    }

    public static int colorSecondaryDark(Context context) {
        return getIdColor(context, COLORSECONDARYDARK);
    }

    public static int pBarStyleAcept(Context context) {
        return getIdStyle(context, PBARSTYLEACEPT);
    }

    public static ContextThemeWrapper dialogStyle(Context context) {
        return new ContextThemeWrapper(context, getIdStyle(context, "AlertDialogCustom"));
    }

    public static ContextThemeWrapper dialogStyle(Context context, String style) {
        return new ContextThemeWrapper(context, getIdStyle(context, style));
    }


    public static Drawable getBotonSecondaryDark() {

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(colorSecondaryDark);
        gd.setCornerRadius(25);
        gd.setStroke(5, colorSecondaryDark);

        return gd;
    }

    public static Drawable getBotonSecondary() {

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(colorSecondary);
        gd.setCornerRadius(25);
        gd.setStroke(5, colorSecondaryDark);

        return gd;

    }

    public static Drawable getBotonPrimary() {

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(colorPrimary);
        gd.setCornerRadius(25);
        gd.setStroke(5, colorPrimary);

        return gd;
    }

    public static Drawable getBotonPrimaryStroke() {

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(colorPrimary);
        gd.setCornerRadius(25);
        gd.setStroke(5, colorSecondaryDark);

        return gd;
    }

    public static Drawable getBotonSecondaryDark(MainActivityBase activityBase) {

        float densidad = getDensidad(activityBase);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(colorSecondaryDark);
        gd.setCornerRadius(5 * densidad);
        gd.setStroke(Math.round(3 * densidad), colorSecondaryDark);

        return gd;
    }

    public static Drawable getBotonSecondary(MainActivityBase activityBase) {

        float densidad = getDensidad(activityBase);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(colorSecondary);
        gd.setCornerRadius(5 * densidad);
        gd.setStroke(Math.round(3 * densidad), colorSecondaryDark);

        return gd;
    }

    public static Drawable getBotonPrimary(MainActivityBase activityBase) {

        int densidad = getDensidadDpi(activityBase);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(colorPrimary);
        gd.setCornerRadius(5 * densidad);
        gd.setStroke(3 * densidad, colorPrimary);

        return gd;
    }

}
