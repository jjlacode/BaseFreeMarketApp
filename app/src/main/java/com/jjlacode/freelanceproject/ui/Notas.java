package com.jjlacode.freelanceproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.adapter.BaseViewHolder;
import com.jjlacode.base.util.adapter.ListaAdaptadorFiltroModelo;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.base.util.android.AppActivity;
import com.jjlacode.base.util.crud.ListaModelo;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.media.MediaUtil;
import com.jjlacode.base.util.time.Day;
import com.jjlacode.base.util.time.ListaDays;
import com.jjlacode.base.util.time.TimeDateUtil;
import com.jjlacode.base.util.time.calendar.fragments.FragmentMes;
import com.jjlacode.freelanceproject.CommonPry;
import com.jjlacode.freelanceproject.R;

import java.util.ArrayList;

public class Notas extends FragmentMes {

    @Override
    protected ListaModelo setListaDia(long fecha) {

        return getListaNotas(fecha);
    }

    public static ListaModelo getListaNotas(long fecha){

        ListaModelo listaDia = new ListaModelo();
        ListaModelo listaCompleta = new ListaModelo(CAMPOS_NOTA);

        for (Modelo modelo : listaCompleta.getLista()) {

            if (TimeDateUtil.getDateString(modelo.getLong(NOTA_FECHA)).equals(TimeDateUtil.getDateString(fecha))){

                listaDia.addModelo(modelo);
            }
        }
        return listaDia;
    }

    @Override
    protected void setVerDia(long fecha, ListaModelo listaModelo) {

        bundle = new Bundle();
        bundle.putString(ORIGEN,NOTAS);
        bundle.putString(ACTUAL, NOTAS);
        bundle.putSerializable(LISTA,listaModelo);
        bundle.putLong(FECHA,fecha);

        icFragmentos.enviarBundleAFragment(bundle, new DiaCalNotas());
    }

    @Override
    protected void setLayoutItem() {

        layoutItem = R.layout.item_list_nota_diario;

    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_NOTA;
        campo = NOTA_FECHA;
    }

    @Override
    protected void setTitulo() {

        titulo = R.string.notas;
    }

    @Override
    protected void setOnDayClick(Day day, int position) {

    }

    @Override
    protected void setOnDayLongClick(Day day, int position) {

    }

    @Override
    protected void setNuevo(long fecha) {

        bundle = new Bundle();
        bundle.putBoolean(NUEVOREGISTRO,true);
        bundle.putString(ORIGEN,NOTAS);
        bundle.putLong(FECHA,fecha);
        bundle.putString(ACTUAL, NOTA);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevaNota());

    }

    @Override
    protected void setVerLista(ListaModelo listaModelo, ListaDays listaDays) {

        bundle = new Bundle();
        bundle.putString(ORIGEN,NOTAS);
        bundle.putSerializable(LISTA,listaModelo);
        bundle.putString(ACTUAL, NOTA);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
    }

    @Override
    protected void setOnPrevMonth() {

    }

    @Override
    protected void setOnNextMonth() {

    }

    @Override
    protected void setOnInicio() {

    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new ListaAdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder, CommonPry.TiposNota {

        TextView descripcion, fechanota, rel;
        ImageView imagen,ver;
        CardView card;

        public ViewHolderRV(View itemView) {
            super(itemView);
            rel = itemView.findViewById(R.id.tvrelnotadiario);
            descripcion = itemView.findViewById(R.id.tvdescnotadiario);
            imagen = itemView.findViewById(R.id.imagennotadiario);
            fechanota = itemView.findViewById(R.id.tvfechanotadiario);
            ver = itemView.findViewById(R.id.btnvernotadiario);
            card = itemView.findViewById(R.id.cardnotadiario);

        }

        @Override
        public void bind(final Modelo modelo) {

            descripcion.setText(modelo.getString(NOTA_TITULO));
            fecha = modelo.getLong(NOTA_FECHA);
            fechanota.setText(JavaUtil.getDateTime(fecha));
            String tipo = modelo.getString(NOTA_TIPO);
            rel.setText(modelo.getString(NOTA_NOMBREREL));
            System.out.println("modelo = " + modelo.getString(NOTA_NOMBREREL));
            if (modelo.getString(NOTA_NOMBREREL)!=null){
                visible(rel);
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
            }else{
                gone(rel);
            }
            String path;
            //imagenTarea.setVisibility(View.GONE);

            if (tipo != null) {

                switch (tipo) {

                    case NOTATEXTO:

                        imagen.setImageResource(R.drawable.ic_nota_texto_indigo);

                        break;

                    case NOTAAUDIO:

                        imagen.setImageResource(R.drawable.ic_nota_audio_indigo);
                        if (modelo.getString(NOTA_RUTAFOTO) != null) {
                            path = modelo.getString(NOTA_RUTAFOTO);
                        }

                        break;

                    case NOTAVIDEO:

                        imagen.setImageResource(R.drawable.ic_nota_video_indigo);
                        if (modelo.getString(NOTA_RUTAFOTO) != null) {
                            path = modelo.getString(NOTA_RUTAFOTO);
                        }

                        break;

                    case NOTAIMAGEN:

                        if (modelo.getString(NOTA_RUTAFOTO) != null) {
                            imagen.setVisibility(View.VISIBLE);
                            path = modelo.getString(NOTA_RUTAFOTO);
                            MediaUtil imagenUtil = new MediaUtil(AppActivity.getAppContext());
                            imagenUtil.setImageUriCircle(path, imagen);
                        } else {
                            //imagenTarea.setVisibility(View.GONE);
                            imagen.setImageResource(R.drawable.ic_nota_imagen_indigo);
                        }
                }
            }

            ver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    bundle = new Bundle();
                    bundle.putSerializable(MODELO, modelo);
                    bundle.putString(CAMPO_ID,modelo.getString(NOTA_ID_NOTA));
                    bundle.putString(ORIGEN, NOTAS);
                    if (modelo.getString(NOTA_ID_RELACIONADO)!=null){
                        bundle.putString(IDREL, modelo.getString(NOTA_ID_RELACIONADO));
                        bundle.putString(SUBTITULO,modelo.getString(NOTA_NOMBREREL));
                    }else{
                    bundle.putString(SUBTITULO, JavaUtil.getDate(fecha));
                    }
                    bundle.putString(ACTUAL, NOTA);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());

                }
            });


            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }
}
