package com.jjlacode.freelanceproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.adapter.BaseViewHolder;
import com.jjlacode.base.util.adapter.ListaAdaptadorFiltroModelo;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.media.MediaUtil;
import com.jjlacode.base.util.time.calendar.clases.DiaCalBase;
import com.jjlacode.freelanceproject.CommonPry;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.sqlite.ContratoPry;

import java.util.ArrayList;

import static com.jjlacode.freelanceproject.CommonPry.Constantes.PROYECTO;
import static com.jjlacode.freelanceproject.CommonPry.Constantes.PROYECTOS;
import static com.jjlacode.freelanceproject.CommonPry.Constantes.TRABAJOS;

public class DiaCalTrabajos extends DiaCalBase implements ContratoPry.Tablas,
        JavaUtil.Constantes, CommonPry.TiposEvento {


    @Override
    protected void setOnBack() {

        bundle = new Bundle();
        bundle.putString(ORIGEN,TRABAJOS);
        bundle.putString(ACTUAL, TRABAJOS);
        bundle.putString(CAMPO_ID,null);
        bundle.putSerializable(LISTA,null);
        bundle.putSerializable(MODELO, null);

        icFragmentos.enviarBundleAFragment(bundle, new Trabajos());
    }

    @Override
    protected int setLayoutRvDia() {
        return R.layout.item_list_trabajos_en_curso;
    }

    @Override
    protected TipoViewHolder setViewHolderDia(View view) {
        return new ViewHolderRVcont(view);
    }

    @Override
    protected void onClickHora(DiaCal diaCal) {

        horaCal = modelo.getLong(HORACAL);

        bundle = new Bundle();
        bundle.putBoolean(NUEVOREGISTRO,true);
        bundle.putString(ORIGEN,TRABAJOS);
        bundle.putLong(FECHA,fecha);
        bundle.putLong(HORACAL,horaCal);
        bundle.putString(ACTUAL, PROYECTOS);
        bundle.putString(CAMPO_ID,null);
        bundle.putSerializable(LISTA,null);
        bundle.putSerializable(MODELO, null);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new ListaAdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<Modelo> entrada) {

            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRVcont extends BaseViewHolder implements TipoViewHolder, CommonPry.TiposNota {

        TextView descripcion,cliente;
        ProgressBar pbar;
        ImageView imagen;
        ImageButton ver;
        CardView card;

        public ViewHolderRVcont(View itemView) {
            super(itemView);

            descripcion = itemView.findViewById(R.id.tvdesctrabajos);
            imagen = itemView.findViewById(R.id.imgtrabajos);
            pbar = itemView.findViewById(R.id.pbartrabajos);
            ver = itemView.findViewById(R.id.btnvertrabajos);
            card = itemView.findViewById(R.id.cardtrabajos);
            cliente = itemView.findViewById(R.id.tvclientetrabajos);

        }

        @Override
        public void bind(final Modelo modelo) {


            double completada = modelo.getDouble(PROYECTO_TOTCOMPLETADO);
            descripcion.setText(modelo.getString(PROYECTO_DESCRIPCION));
            pbar.setProgress((int)completada);
            cliente.setText(modelo.getString(PROYECTO_CLIENTE_NOMBRE));
            MediaUtil imagenUtil = new MediaUtil(getContext());
            try{
                imagenUtil.setImageUri(modelo.getString(PROYECTO_RUTAFOTO),imagen);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (completada>0){
                visible(pbar);
            }else{
                gone(pbar);
            }


            long retraso = modelo.getLong(PROYECTO_RETRASO);

            if (retraso > 3 * CommonPry.DIASLONG) {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
            } else if (retraso > CommonPry.DIASLONG) {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));
            } else {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
            }//imgret.setImageResource(R.drawable.alert_box_v);}


            ver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bundle = new Bundle();
                    bundle.putSerializable(MODELO, modelo);
                    bundle.putString(CAMPO_ID,modelo.getString(PROYECTO_ID_PROYECTO));
                    bundle.putString(ORIGEN, TRABAJOS);
                    bundle.putString(SUBTITULO, modelo.getString(PROYECTO_NOMBRE));
                    bundle.putString(ACTUAL, PROYECTO);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());

                }
            });
            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVcont(view);
        }
    }
}
