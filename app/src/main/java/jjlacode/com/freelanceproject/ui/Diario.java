package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.android.AppActivity;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.media.MediaUtil;
import jjlacode.com.freelanceproject.util.time.calendar.clases.CalendarioBase;
import jjlacode.com.freelanceproject.util.time.calendar.clases.Day;

import static jjlacode.com.freelanceproject.CommonPry.Constantes.DIARIO;
import static jjlacode.com.freelanceproject.CommonPry.Constantes.NOTA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ACTUAL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.FECHA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ID;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.IDREL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.MODELO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.NUEVOREGISTRO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ORIGEN;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.SUBTITULO;

public class Diario extends CalendarioBase {

    @Override
    protected void setLayoutItem() {

        layoutItem = R.layout.item_list_nota_diario;

    }

    @Override
    protected boolean setIfLista(Modelo modelo) {

        return true;
    }

    @Override
    protected boolean setIfListaHoy(Modelo modelo, long hoy) {

        return JavaUtil.getDate(modelo.getLong(NOTA_FECHA)).equals(JavaUtil.getDate(hoy));
    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_NOTA;
        campo = NOTA_FECHA;
    }

    @Override
    protected void setTitulo() {

        titulo = R.string.diario;
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
        bundle.putString(ORIGEN,DIARIO);
        bundle.putLong(FECHA,fecha);
        bundle.putString(ACTUAL, NOTA);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());

    }

    @Override
    protected void setVerLista() {

        bundle = new Bundle();
        bundle.putString(ORIGEN,DIARIO);
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
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
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
                        if (modelo.getString(NOTA_RUTA) != null) {
                            path = modelo.getString(NOTA_RUTA);
                        }

                        break;

                    case NOTAVIDEO:

                        imagen.setImageResource(R.drawable.ic_nota_video_indigo);
                        if (modelo.getString(NOTA_RUTA) != null) {
                            path = modelo.getString(NOTA_RUTA);
                        }

                        break;

                    case NOTAIMAGEN:

                        if (modelo.getString(NOTA_RUTA) != null) {
                            imagen.setVisibility(View.VISIBLE);
                            path = modelo.getString(NOTA_RUTA);
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
                    bundle.putString(ID,modelo.getString(NOTA_ID_NOTA));
                    bundle.putString(ORIGEN, DIARIO);
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
