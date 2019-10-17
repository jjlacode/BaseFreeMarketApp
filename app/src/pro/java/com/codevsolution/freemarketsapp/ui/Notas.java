package com.codevsolution.freemarketsapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.time.Day;
import com.codevsolution.base.time.ListaDays;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.base.time.calendar.fragments.FragmentMes;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

public class Notas extends FragmentMes {

    @Override
    protected ListaModelo setListaDia(long fecha) {

        return getListaNotas(fecha);
    }

    public static ListaModelo getListaNotas(long fecha){

        ListaModelo listaDia = new ListaModelo();
        ListaModelo listaCompleta = new ListaModelo(CAMPOS_NOTA);

        for (ModeloSQL modeloSQL : listaCompleta.getLista()) {

            if (TimeDateUtil.getDateString(modeloSQL.getLong(NOTA_FECHA)).equals(TimeDateUtil.getDateString(fecha))) {

                listaDia.addModelo(modeloSQL);
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
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos) {
        return new ListaAdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder, Interactor.TiposNota {

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
        public void bind(final ModeloSQL modeloSQL) {

            descripcion.setText(modeloSQL.getString(NOTA_TITULO));
            fecha = modeloSQL.getLong(NOTA_FECHA);
            fechanota.setText(JavaUtil.getDateTime(fecha));
            String tipo = modeloSQL.getString(NOTA_TIPO);
            rel.setText(modeloSQL.getString(NOTA_NOMBREREL));
            System.out.println("modeloSQL = " + modeloSQL.getString(NOTA_NOMBREREL));
            if (modeloSQL.getString(NOTA_NOMBREREL) != null) {
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
                        if (modeloSQL.getString(NOTA_RUTAFOTO) != null) {
                            path = modeloSQL.getString(NOTA_RUTAFOTO);
                        }

                        break;

                    case NOTAVIDEO:

                        imagen.setImageResource(R.drawable.ic_nota_video_indigo);
                        if (modeloSQL.getString(NOTA_RUTAFOTO) != null) {
                            path = modeloSQL.getString(NOTA_RUTAFOTO);
                        }

                        break;

                    case NOTAIMAGEN:

                        if (modeloSQL.getString(NOTA_RUTAFOTO) != null) {
                            imagen.setVisibility(View.VISIBLE);
                            path = modeloSQL.getString(NOTA_RUTAFOTO);
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
                    bundle.putSerializable(MODELO, modeloSQL);
                    bundle.putString(CAMPO_ID, modeloSQL.getString(NOTA_ID_NOTA));
                    bundle.putString(ORIGEN, NOTAS);
                    if (modeloSQL.getString(NOTA_ID_RELACIONADO) != null) {
                        bundle.putString(IDREL, modeloSQL.getString(NOTA_ID_RELACIONADO));
                        bundle.putString(SUBTITULO, modeloSQL.getString(NOTA_NOMBREREL));
                    }else{
                    bundle.putString(SUBTITULO, JavaUtil.getDate(fecha));
                    }
                    bundle.putString(ACTUAL, NOTA);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());

                }
            });


            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }
}
