package com.codevsolution.base.style;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.MainActivityBase;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

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
import static com.codevsolution.base.style.Estilos.Constantes.PBARSTYLEACEPT;
import static com.codevsolution.base.style.Estilos.Constantes.STRING;
import static com.codevsolution.base.style.Estilos.Constantes.STYLE;
import static com.codevsolution.base.style.Estilos.Constantes.WRAP_CONTENT;

public class Estilos {

    protected boolean multiPanel;
    protected int sizeTextD;
    public static int colorPrymary = Color.parseColor("#ffffff");
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
        String LAYOUT = "layout";
        String STYLE = "style";
        String DIMENS = "dimen";
        String ID = "id";
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

    public static void setLayoutParams(ViewGroup viewGroup, View view, int peso) {

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

    public static int getColor(Context context, String color) {
        return context.getResources().getColor(getIdColor(context, color));
    }

    public static Drawable getDrawable(Context context, String drawable) {
        return ContextCompat.getDrawable(context, getIdDrawable(context, drawable));
    }

    public static String getString(Context context, String string) {
        return context.getResources().getString(getIdStrig(context, string));
    }

    public static XmlResourceParser getLayout(Context context, String layout) {
        return context.getResources().getLayout(getIdLayout(context, layout));
    }

    public static int getIdDrawable(Context context, String nombre) {
        return context.getResources()
                .getIdentifier(nombre, DRAWABLE, context.getPackageName());
    }

    public static int getIdStrig(Context context, String nombre) {
        return context.getResources()
                .getIdentifier(nombre, STRING, context.getPackageName());
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

    public static class PaletaColoresDialog extends DialogFragment {

        private Context context;
        protected int columnas = 3;
        protected int filas = 4;
        protected int altoimg = 100;
        protected int anchoimg = 100;
        protected int padtxt = 20;
        protected int padalto = 10;
        protected int padancho = 10;
        protected double densidad;
        protected float sizeT;
        private String[] colores = {"#EEEEEE", "#DDDDDD", "#CCCCCC", "#BBBBBB", "#AAAAAA", "#999999", "#888888",
                "#777777", "#666666", "#555555", "#444444", "#333333", "#222222", "#111111", "#000000", "#FF0000",
                "#EE0000", "#DD0000", "#CC0000", "#BB0000", "#AA0000", "#990000", "#880000", "#770000", "#660000",
                "#550000", "#440000"};

        /*
    ,330000,220000,110000,FFFFFF,FFFFCC,FFFF99,
            FFFF66,FFFF33,FFFF00,CCFFFF,CCFFCC,CCFF99,CCFF66,CCFF33,CCFF00,99FFFF,99FFCC,99FF99,
            99FF66	99FF33	99FF00
        66FFFF	66FFCC	66FF99	66FF66	66FF33	66FF00
        33FFFF	33FFCC	33FF99	33FF66	33FF33	33FF00
        00FFFF	00FFCC	00FF99	00FF66	00FF33	00FF00
        FFCCFF	FFCCCC	FFCC99	FFCC66	FFCC33	FFCC00
        CCCCFF	CCCCCC	CCCC99	CCCC66	CCCC33	CCCC00
        99CCFF	99CCCC	99CC99	99CC66	99CC33	99CC00
        66CCFF	66CCCC	66CC99	66CC66	66CC33	66CC00
        33CCFF	33CCCC	33CC99	33CC66	33CC33	33CC00
        00CCFF	00CCCC	33CC66	33CC33	00CC99	00CC66
        00CC33	00CC00	FF99FF	FF99CC	FF9999	FF9966
        FF9933	FF9900	CC99FF	CC99CC	CC9999	CC9966
        CC9933	CC9900	9999FF	9999CC	999999	999966
        999933	999900	6699FF	6699CC	669999	669966
        669933	669900	3399FF	3399CC	339999	339966
        339933	339900	0099FF	0099CC	009999	009966
        009933	009900	FF66FF	FF66CC	FF6699	FF6666
        FF6633	FF6600	CC66FF	CC66CC	CC6699	CC6666
        CC6633	CC6600	9966FF	9966CC	996699	996666
        996633	996600	6666FF	6666CC	666699	666666
        666633	666600	3366FF	3366CC	336699	336666
        336633	336600	0066FF	0066CC	006699	006666
        006633	006600	FF33FF	FF33CC	FF3399	FF3366
        FF3333	FF3300	CC33FF	CC33CC	CC3399	CC3366
        CC3333	CC3300	9933FF	9933CC	993399	993366
        993333	993300	6633FF	6633CC	663399	663366
        663333	663300	3333FF	3333CC	333399	333366
        333333	333300	0033FF	FF3333	0033CC	003399
        003366	003333	003300	FF00FF	FF00CC	FF0099
        FF0066	FF0033	FF0000	CC00FF	CC00CC	CC0099
        CC0066	CC0033	CC0000	9900FF	9900CC	990099
        990066	990033	990000	6600FF	6600CC	660099
        660066	660033	660000	3300FF	3300CC	330099
        330066	330033	330000	0000FF	0000CC	000099
        000066	000033	00FF00	00EE00	00DD00	00CC00
        00BB00	00AA00	009900	008800	007700	006600
        005500	004400	003300	002200	001100	0000FF
        0000EE	0000DD	0000CC	0000BB	0000AA	000099
        000088	000077	000055	000044	000022	000011};

     */

        private ArrayList<GridModel> lista;
        private RecyclerView rv;
        OnClick listener;

        public PaletaColoresDialog(OnClick listener, Context context) {
            this.context = context;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return crearDialogoPaletaColores(context);
        }

        private AlertDialog crearDialogoPaletaColores(Context context) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            View v = inflater.inflate(getIdLayout(context, "dialog_fragment"), null);

            lista = new ArrayList<GridModel>();

            if (getResources().getBoolean(R.bool.esLand)) {

                for (int i = lista.size(); i > 0; i--) {
                    filas = i;
                    columnas = (int) ((double) lista.size() / i) + (lista.size() % i);
                    if (filas >= columnas - 1) {
                        continue;
                    }
                    break;
                }

            } else {
                for (int i = 1; i < lista.size(); i++) {
                    columnas = i;
                    filas = (int) ((double) lista.size() / i);
                    if (lista.size() % i > 0) {
                        filas++;
                    }
                    if (filas > columnas + 1) {
                        continue;
                    }
                    break;
                }
            }
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

            densidad = metrics.density;
            int ancho = (int) (metrics.widthPixels / densidad);
            int alto = (int) (metrics.heightPixels / densidad);
            int densidadDpi = metrics.densityDpi;

            float sizeText = ((float) (ancho + alto + densidadDpi) / (100));

            anchoimg = ancho / columnas;
            altoimg = alto / filas;
            padancho = (int) ((double) (anchoimg) / (4));
            padalto = (int) ((double) (altoimg) / (4));
            padtxt = (int) ((double) padancho / 2);

            sizeT = (sizeText * 4) / 5;

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, columnas);


            for (int i = 0; i < colores.length; i++) {
                lista.add(new GridModel(colores[i], 0, Estilos.colorPrimary(context), null, true));
            }

            LinearLayoutCompat main = v.findViewById(R.id.container_dialog);
            rv = new RecyclerView(context);
            main.addView(rv);

            int columnas = (int) (rv.getWidth() / (metrics.density * 300));
            if (columnas < 1) {
                columnas = 1;
            }
            rv.setLayoutManager(layoutManager);
            int layoutItem = R.layout.item_grid_list;
            RVAdapter adaptadorRV = new RVAdapter(new ViewHolderRV(v), lista, layoutItem);
            rv.setAdapter(adaptadorRV);

            Button btnCerrar = v.findViewById(R.id.btn_cerrar_dialogo_paleta);
            btnCerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            builder.setView(v);

            builder.setTitle("Paleta de Colores")
                    .setMessage("Elija un color");

            return builder.create();
        }

        public interface OnClick {

            void onClick(String color);
        }

        public class GridModel {

            private int color;
            private String colorHex;
            private int colorTexto;
            private String nombre;
            private boolean sinTexto;

            public GridModel(String colorHex, int color, int colorTexto, String nombre) {
                this.color = color;
                this.colorHex = colorHex;
                this.colorTexto = colorTexto;
                this.nombre = nombre;
                sinTexto = false;
            }

            public GridModel(String colorHex, int color, int colorTexto, String nombre, boolean sinTexto) {
                this.color = color;
                this.colorHex = colorHex;
                this.colorTexto = colorTexto;
                this.nombre = nombre;
                this.sinTexto = sinTexto;
            }

            public int getColor() {
                return color;
            }

            public void setColor(int color) {
                this.color = color;
            }

            public String getColorHex() {
                return colorHex;
            }

            public void setColorHex(String colorHex) {
                this.colorHex = colorHex;
            }

            public int getColorTexto() {
                return colorTexto;
            }

            public void setColorTexto(int colorTexto) {
                this.colorTexto = colorTexto;
            }

            public String getNombre() {
                return nombre;
            }

            public void setNombre(String nombre) {
                this.nombre = nombre;
            }

            public boolean isSinTexto() {
                return sinTexto;
            }

            public void setSinTexto(boolean sinTexto) {
                this.sinTexto = sinTexto;
            }
        }

        private class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

            LinearLayoutCompat main;
            GridModel gridModel;

            public ViewHolderRV(View view) {
                super(view);

                main = view.findViewById(R.id.main_item_grid);
            }

            @Override
            public void bind(ArrayList<?> lista, int position) {
                super.bind(lista, position);

                gridModel = (GridModel) lista.get(position);

                ViewGroupLayout vistaGrid = new ViewGroupLayout(context, main);
                Button button = vistaGrid.addButtonTrans(gridModel.nombre);

                LinearLayoutCompat.LayoutParams param = new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        (int) (((rv.getHeight()) - (padalto * densidad)) / filas));
                main.setLayoutParams(param);

                button.setBackgroundColor(Color.parseColor(gridModel.getColorHex()));
                button.setTextColor(gridModel.getColorTexto());
                button.setPadding(padancho, padalto, padancho, 0);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InteractorBase.colorTmp = gridModel.getColorHex();
                        if (listener != null) {
                            listener.onClick(gridModel.getColorHex());
                        }
                    }
                });

                if (!gridModel.isSinTexto()) {
                    button.setText(gridModel.getNombre());
                    button.setTextSize(sizeT);
                    button.setPadding(padtxt, 0, padtxt, 0);
                } else {
                    button.setText("");
                    button.setPadding(padancho, padalto, padancho, padalto);
                }

            }

            @Override
            public BaseViewHolder holder(View view) {
                return new ViewHolderRV(view);
            }
        }
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
        gd.setColor(colorPrymary);
        gd.setCornerRadius(25);
        gd.setStroke(5, colorPrymary);

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
        gd.setColor(colorPrymary);
        gd.setCornerRadius(5 * densidad);
        gd.setStroke(3 * densidad, colorPrymary);

        return gd;
    }

}
