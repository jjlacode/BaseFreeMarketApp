package com.codevsolution.freemarketsapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.time.Day;
import com.codevsolution.base.time.ListaDays;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

public class Trabajos extends FragmentMesHorario {

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected ListaModeloSQL setListaDia(long fecha) {
        return getListaDia(fecha);
    }

    public static ListaModeloSQL getListaDia(long fecha) {

        ListaModeloSQL listaDia = new ListaModeloSQL();
        ListaModeloSQL listatemp = new ListaModeloSQL(CAMPOS_AGENDA);

        for (ModeloSQL modeloSQL : listatemp.getLista()) {
            System.out.println("fecha entrada = " + TimeDateUtil.getDateString(TimeDateUtil.soloFecha(modeloSQL.getLong(AGENDA_VALORENTRADA))));
            System.out.println("fecha = " + TimeDateUtil.getDateString(TimeDateUtil.soloFecha(fecha)));
            System.out.println("fecha salida = " + TimeDateUtil.getDateString(TimeDateUtil.soloFecha(modeloSQL.getLong(AGENDA_VALORSALIDA))));

            if (TimeDateUtil.soloFecha(modeloSQL.getLong(AGENDA_VALORENTRADA))
                    <= TimeDateUtil.soloFecha(fecha) &&
                    TimeDateUtil.soloFecha(modeloSQL.getLong(AGENDA_VALORSALIDA))
                            >= TimeDateUtil.soloFecha(fecha)) {

                listaDia.addModelo(modeloSQL);
                System.out.println("listaDia = " + listaDia.sizeLista());

            }
        }
        return listaDia;
    }

    @Override
    protected void setVerDia(long fecha, ListaModeloSQL listaModeloSQL) {

        bundle = new Bundle();
        bundle.putLong(FECHA,fecha);

        icFragmentos.enviarBundleAFragment(bundle, new DiaCalTrabajos());
    }

    @Override
    protected void setLayoutItem() {

        layoutItem = R.layout.item_list_trabajos_en_curso;

    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_AGENDA;
        campo = AGENDA_VALORENTRADA;
        campoCard = AGENDA_ID_PARTIDA;
        campoId = AGENDA_ID_AGENDA;
        campoColor = AGENDA_COLOR;


    }

    @Override
    protected void setOnCreate() {
        super.setOnCreate();
        //setSinDia(true);
    }

    @Override
    protected void setTitulo() {

        titulo = R.string.proyectos_en_curso;

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
        bundle.putString(ORIGEN,TRABAJOS);
        bundle.putLong(FECHA,fecha);
        bundle.putString(ACTUAL, PROYECTO);
        bundle.putString(CAMPO_ID,null);
        bundle.putSerializable(LISTA,null);
        bundle.putSerializable(MODELO, null);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
    }

    @Override
    protected void setVerLista(ListaModeloSQL listaModeloSQL, ListaDays listaDays) {

        bundle = new Bundle();
        bundle.putString(ORIGEN,TRABAJOS);
        bundle.putString(ACTUAL, PROYECTO);
        bundle.putSerializable(LISTA, listaModeloSQL);
        bundle.putString(CAMPO_ID,null);
        bundle.putSerializable(MODELO, null);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
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
    protected void abrirSemana(long fecha) {
        super.abrirSemana(fecha);

        bundle = new Bundle();
        bundle.putLong(FECHA, fecha);
        icFragmentos.enviarBundleAFragment(bundle, new TrabajosSem());
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos) {
        return new ListaAdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView descripcion,cliente;
        ProgressBar pbar;
        ImageView imagen;
        ImageButton ver;
        CardView card;

        public ViewHolderRV(View itemView) {
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

            final ModeloSQL modeloSQL = updateModelo(CAMPOS_DETPARTIDA,
                    segmento.getString(AGENDA_ID_DETPARTIDA), segmento.getInt(AGENDA_SECUENCIA_DETPARTIDA));

            double completada = modeloSQL.getDouble(DETPARTIDA_COMPLETADA);
            descripcion.setText(modeloSQL.getString(DETPARTIDA_DESCRIPCION));
            pbar.setProgress((int)completada);
            ModeloSQL proyecto = updateModelo(CAMPOS_PROYECTO, segmento.getString(AGENDA_ID_PARTIDA));
            cliente.setText(proyecto.getString(PROYECTO_CLIENTE_NOMBRE));
            MediaUtil imagenUtil = new MediaUtil(getContext());
            try{
                imagenUtil.setImageUri(modeloSQL.getString(DETPARTIDA_RUTAFOTO), imagen);
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

            ver.setBackgroundColor(Color.parseColor(segmento.getString(AGENDA_COLOR)));

            ver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bundle = new Bundle();
                    bundle.putSerializable(MODELO, modeloSQL);
                    bundle.putString(CAMPO_ID, modeloSQL.getString(DETPARTIDA_ID_PARTIDA));
                    bundle.putInt(CAMPO_SECUENCIA, modeloSQL.getInt(DETPARTIDA_SECUENCIA));
                    bundle.putString(ORIGEN, TRABAJOS);
                    bundle.putString(SUBTITULO, modeloSQL.getString(DETPARTIDA_NOMBRE));
                    bundle.putString(ACTUAL, PROYECTO);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaTrabajo());

                }
            });

            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    @Override
    protected TipoViewHolder setViewHolderCard(View view) {
        return new ViewHolderRVCard(view);
    }

    public class ViewHolderRVCard extends BaseViewHolder implements TipoViewHolder {

        RelativeLayout relativeLayout;
        CardView card;
        private Button btnNombre;

        public ViewHolderRVCard(View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.ry_item_list);

        }

        @Override
        public void bind(ModeloSQL modeloSQL) {

            ModeloSQL detPartida = updateModelo(CAMPOS_DETPARTIDA,
                    modeloSQL.getString(AGENDA_ID_DETPARTIDA), modeloSQL.getInt(AGENDA_SECUENCIA_DETPARTIDA));
            ViewGroupLayout vistaCard = new ViewGroupLayout(contexto, relativeLayout, new CardView(contexto));
            card = (CardView) vistaCard.getViewGroup();
            ViewGroupLayout vistaLinear = new ViewGroupLayout(contexto, vistaCard.getViewGroup());
            btnNombre = vistaLinear.addButtonSecondary(detPartida.getString(DETPARTIDA_NOMBRE));
            btnNombre.setTextSize(sizeText / 2);
            btnNombre.setBackgroundColor(Color.parseColor(detPartida.getString(DETPARTIDA_COLOR)));
            LinearLayoutCompat.LayoutParams layoutParamsrv = new LinearLayoutCompat.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, (int) ((double) getResources().getDimension(R.dimen.altobtn)) / 3);
            btnNombre.setLayoutParams(layoutParamsrv);

            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVCard(view);
        }
    }

}
