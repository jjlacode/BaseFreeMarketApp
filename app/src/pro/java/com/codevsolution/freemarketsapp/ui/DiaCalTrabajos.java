package com.codevsolution.freemarketsapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

public class DiaCalTrabajos extends DiaCalHorario implements ContratoPry.Tablas,
        JavaUtil.Constantes, Interactor.TiposEvento, Interactor.ConstantesPry {

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected ListaModeloSQL setListaDia(long fecha) {

        return Trabajos.getListaDia(fecha);
    }

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

        horaCal = modeloSQL.getLong(HORACAL);

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
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos) {
        return new ListaAdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<ModeloSQL> entrada) {

            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRVcont extends BaseViewHolder implements TipoViewHolder, Interactor.TiposNota {

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
        public void bind(final ModeloSQL segmento) {

            if (!getDescanso() &&
                    segmento.getLong(AGENDA_VALORENTRADA) <= horaCal + TimeDateUtil.soloFecha(fecha)
                    && segmento.getLong(AGENDA_VALORSALIDA) > horaCal + TimeDateUtil.soloFecha(fecha)) {
                visible(card);

            } else {
                gone(card);
            }

            final ModeloSQL modeloSQL = CRUDutil.updateModelo(CAMPOS_DETPARTIDA, segmento.
                    getString(AGENDA_ID_DETPARTIDA), segmento.getInt(AGENDA_SECUENCIA_DETPARTIDA));

            double completada = modeloSQL.getDouble(DETPARTIDA_COMPLETADA);
            descripcion.setText(modeloSQL.getString(DETPARTIDA_DESCRIPCION));
            pbar.setProgress((int)completada);
            final ModeloSQL proyecto = CRUDutil.updateModelo(CAMPOS_PROYECTO, segmento.getString(AGENDA_ID_PARTIDA));
            cliente.setText(proyecto.getString(PROYECTO_CLIENTE_NOMBRE));
            MediaUtil imagenUtil = new MediaUtil(getContext());
            try{
                imagenUtil.setImageUri(modeloSQL.getString(PROYECTO_RUTAFOTO), imagen);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (completada>0){
                visible(pbar);
            }else{
                gone(pbar);
            }


            long retraso = proyecto.getLong(PROYECTO_RETRASO);

            if (retraso > 3 * Interactor.DIASLONG) {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
            } else if (retraso > Interactor.DIASLONG) {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));
            } else {
                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
            }//imgret.setImageResource(R.drawable.alert_box_v);}


            ver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bundle = new Bundle();
                    bundle.putSerializable(MODELO, modeloSQL);
                    bundle.putString(CAMPO_ID, modeloSQL.getString(DETPARTIDA_ID_PARTIDA));
                    bundle.putInt(CAMPO_SECUENCIA, modeloSQL.getInt(DETPARTIDA_SECUENCIA));
                    bundle.putString(ORIGEN, TRABAJOS);
                    bundle.putString(SUBTITULO, modeloSQL.getString(DETPARTIDA_NOMBRE));
                    bundle.putString(ACTUAL, DETPARTIDA);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaTrabajo());

                }
            });
            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVcont(view);
        }
    }
}
