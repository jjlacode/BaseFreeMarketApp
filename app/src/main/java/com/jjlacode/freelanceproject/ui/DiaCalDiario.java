package com.jjlacode.freelanceproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.jjlacode.freelanceproject.CommonPry;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.sqlite.ContratoPry;
import com.jjlacode.freelanceproject.util.JavaUtil;
import com.jjlacode.freelanceproject.util.adapter.BaseViewHolder;
import com.jjlacode.freelanceproject.util.adapter.ListaAdaptadorFiltroModelo;
import com.jjlacode.freelanceproject.util.adapter.TipoViewHolder;
import com.jjlacode.freelanceproject.util.android.AppActivity;
import com.jjlacode.freelanceproject.util.crud.Modelo;
import com.jjlacode.freelanceproject.util.media.MediaUtil;
import com.jjlacode.freelanceproject.util.time.TimeDateUtil;
import com.jjlacode.freelanceproject.util.time.calendar.clases.DiaCalBase;

import java.util.ArrayList;

import static com.jjlacode.freelanceproject.CommonPry.Constantes.CLIENTE;
import static com.jjlacode.freelanceproject.CommonPry.Constantes.DIARIO;
import static com.jjlacode.freelanceproject.CommonPry.Constantes.EVENTO;
import static com.jjlacode.freelanceproject.CommonPry.Constantes.PROYECTO;

public class DiaCalDiario extends DiaCalBase implements ContratoPry.Tablas,
        JavaUtil.Constantes, CommonPry.TiposEvento {


    @Override
    protected void setOnBack() {

        bundle = new Bundle();
        bundle.putString(ORIGEN,DIARIO);
        bundle.putString(ACTUAL, DIARIO);
        bundle.putString(CAMPO_ID,null);
        bundle.putSerializable(LISTA,null);
        bundle.putSerializable(MODELO, null);

        icFragmentos.enviarBundleAFragment(bundle, new Diario());
    }

    @Override
    protected int setLayoutRvDia() {
        return R.layout.item_list_nota_diario;
    }

    @Override
    protected TipoViewHolder setViewHolderDia(View view) {
        return new ViewHolderRVcont(view);
    }

    @Override
    protected void onClickHora(DiaCal diaCal) {


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

        TextView descripcion, fechaDiario, rel;
        ImageView imagen,ver;
        CardView card;

        public ViewHolderRVcont(View itemView) {
            super(itemView);
            rel = itemView.findViewById(R.id.tvreldiario);
            descripcion = itemView.findViewById(R.id.tvdescdiario);
            imagen = itemView.findViewById(R.id.imagendiario);
            fechaDiario = itemView.findViewById(R.id.tvfechadiario);
            ver = itemView.findViewById(R.id.btnverdiario);
            card = itemView.findViewById(R.id.carddiario);

        }

        @Override
        public void bind(final Modelo modelo) {

            descripcion.setText(modelo.getString(DIARIO_DESCRIPCION));
            fecha = modelo.getLong(DIARIO_CREATE);
            fechaDiario.setText(TimeDateUtil.getDateString(fecha));
            rel.setText(modelo.getString(DIARIO_NOMBREREL));

            String path;
            if (modelo.getString(DIARIO_RUTAFOTO) != null) {
                imagen.setVisibility(View.VISIBLE);
                path = modelo.getString(DIARIO_RUTAFOTO);
                MediaUtil imagenUtil = new MediaUtil(AppActivity.getAppContext());
                imagenUtil.setImageUriCircle(path, imagen);
            } else {
                //imagenTarea.setVisibility(View.GONE);
                imagen.setImageResource(R.drawable.ic_nota_imagen_indigo);
            }

            ver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    bundle = new Bundle();
                    bundle.putSerializable(MODELO, modelo);
                    bundle.putString(CAMPO_ID,modelo.getString(DIARIO_ID_RELACIONADO));
                    bundle.putString(ORIGEN, DIARIO);
                    String destinoRel = modelo.getString(DIARIO_REL);
                    bundle.putString(ACTUAL, destinoRel);

                    switch (destinoRel){

                        case EVENTO :
                            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
                            break;

                        case PROYECTO :
                            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
                            break;

                        case CLIENTE :
                            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDCliente());

                    }

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
