package com.codevsolution.base.style;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

public class Dialogos {

    public static class DialogoTexto extends DialogFragment {

        private String titulo;
        private String mensaje;
        private Context context;
        private OnClick listener;

        public DialogoTexto(String titulo, String mensaje, Context context, OnClick listener) {

            this.titulo = titulo;
            this.mensaje = mensaje;
            this.context = context;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            return crearDialogoTexto();
        }

        private AlertDialog crearDialogoTexto() {


            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            //LayoutInflater inflater = getActivity().getLayoutInflater();
            //View v = inflater.inflate(Estilos.getIdLayout(context, "dialog_fragment"), null);

            //LinearLayoutCompat main = v.findViewById(R.id.container_dialog);
            //View v = new CanvasBase(context);
            View v = new LinearLayoutCompat(context);
            Estilos.setLayoutParams((ViewGroup) v.getParent(), v, ViewGroup.LayoutParams.MATCH_PARENT,
                    500);

            builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (listener != null) {
                        listener.onConfirm();
                    }
                }
            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                    if (listener != null) {
                        listener.onCancel();
                    }
                }
            });

            builder.setView(v);

            builder.setTitle(titulo)
                    .setMessage(mensaje);

            return builder.show();
        }

        public interface OnClick {

            void onConfirm();

            void onCancel();
        }

    }

    public static class DialogoEdit extends DialogFragment {

        private String titulo;
        private String mensaje;
        private Context context;
        private OnClick listener;
        private String hint;
        private int tipoDato;

        public DialogoEdit(String titulo, String mensaje, int tipoDato, int hint, Context context, OnClick listener) {

            this.titulo = titulo;
            this.mensaje = mensaje;
            this.hint = getString(hint);
            this.tipoDato = tipoDato;
            this.context = context;
            this.listener = listener;
        }

        public DialogoEdit(String titulo, String mensaje, int tipoDato, String hint, Context context, OnClick listener) {

            this.titulo = titulo;
            this.mensaje = mensaje;
            this.hint = hint;
            this.tipoDato = tipoDato;
            this.context = context;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            return crearDialogoEdit();
        }

        private AlertDialog crearDialogoEdit() {


            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            //LayoutInflater inflater = getActivity().getLayoutInflater();
            //View v = inflater.inflate(Estilos.getIdLayout(context, "dialog_fragment"), null);

            //LinearLayoutCompat main = v.findViewById(R.id.container_dialog);
            //View v = new CanvasBase(context);
            LinearLayoutCompat v = new LinearLayoutCompat(context);
            v.setOrientation(LinearLayoutCompat.VERTICAL);
            Estilos.setLayoutParams((ViewGroup) v.getParent(), v, ViewGroup.LayoutParams.MATCH_PARENT,
                    500);
            EditMaterialLayout edit = new EditMaterialLayout(v, context, hint);
            edit.setTipo(tipoDato);

            builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (listener != null) {
                        listener.onConfirm(edit.getTexto());
                    }
                }
            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                    if (listener != null) {
                        listener.onCancel();
                    }
                }
            });

            builder.setView(v);

            builder.setTitle(titulo)
                    .setMessage(mensaje);

            return builder.show();
        }

        public interface OnClick {

            void onConfirm(String text);

            void onCancel();
        }

    }


    public static class PaletaColoresDialog extends DialogFragment {

        private Context context;
        protected int columnas = 8;
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
        private String colorSet;
        private RVAdapter adaptadorRV;

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

            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LinearLayoutCompat main = new LinearLayoutCompat(context);
            main.setOrientation(LinearLayoutCompat.VERTICAL);

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
            int ancho = (int) ((double) metrics.widthPixels / densidad);
            int alto = (int) ((double) metrics.heightPixels / densidad);
            int densidadDpi = metrics.densityDpi;

            float sizeText = ((float) (ancho + alto + densidadDpi) / (100));

            anchoimg = (int) ((double) ancho) / columnas;
            altoimg = (int) ((double) alto) / filas;
            padancho = (int) ((double) (anchoimg) / (4));
            padalto = (int) ((double) (altoimg) / (4));
            padtxt = (int) ((double) padancho / 2);

            sizeT = (int) ((double) (sizeText * 4)) / 5;

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, columnas);


            for (int i = 0; i < colores.length; i++) {
                lista.add(new GridModel(colores[i], 0, Estilos.colorPrimary(context), null, true));
            }

            Estilos.setLayoutParams((ViewGroup) main.getParent(), main, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);


            rv = new RecyclerView(context);

            main.addView(rv);

            rv.setLayoutManager(layoutManager);
            int layoutItem = R.layout.item_grid_list;
            adaptadorRV = new RVAdapter(new ViewHolderRV(main), lista, layoutItem);
            rv.setAdapter(adaptadorRV);

            builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
            builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onClick(colorSet);
                    }
                }
            });
            builder.setView(main);

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

                final ViewGroupLayout vistaGrid = new ViewGroupLayout(context, main);
                Button button = vistaGrid.addButtonTrans(gridModel.nombre);


                vistaGrid.getViewGroup().setBackgroundColor(Estilos.colorPrimary);

                button.setBackgroundColor(Color.parseColor(gridModel.getColorHex()));
                button.setTextColor(gridModel.getColorTexto());
                vistaGrid.getViewGroup().setPadding(5, 5, 5, 5);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        colorSet = gridModel.colorHex;

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

}
