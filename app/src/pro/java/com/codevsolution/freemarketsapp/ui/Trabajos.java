package com.codevsolution.freemarketsapp.ui;

import android.content.Context;
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
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.time.Day;
import com.codevsolution.base.time.ListaDays;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.TiposEstados.TPRESUPACEPTADO;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposEstados.TPROYECTPENDCOBRO;

public class Trabajos extends FragmentMesHorario {

    @Override
    protected ListaModelo setListaDia(long fecha) {

        ListaModelo listaDia = new ListaModelo();
        ListaModelo listatemp = new ListaModelo(CAMPOS_PROYECTO);
        listabase = new ListaModelo();

        for (ModeloSQL modeloSQL : listatemp.getLista()) {
            if ((modeloSQL.getInt(PROYECTO_TIPOESTADO) >= TPRESUPACEPTADO) &&
                    (modeloSQL.getInt(PROYECTO_TIPOESTADO) < TPROYECTPENDCOBRO)) {
                listabase.addModelo(modeloSQL);
            }
        }

        for (ModeloSQL modeloSQL : listabase.getLista()) {
            if (TimeDateUtil.soloFecha(modeloSQL.getLong(PROYECTO_FECHAINICIOCALCULADA))
                    <= TimeDateUtil.soloFecha(fecha) &&
                    TimeDateUtil.soloFecha(modeloSQL.getLong(PROYECTO_FECHAENTREGACALCULADA))
                            >= TimeDateUtil.soloFecha(fecha)) {

                listaDia.addModelo(modeloSQL);
            }
        }

        return listaDia;
    }

    @Override
    protected void setVerDia(long fecha, ListaModelo listaModelo) {

        bundle = new Bundle();
        bundle.putString(ORIGEN,TRABAJOS);
        bundle.putString(ACTUAL, PROYECTO);
        bundle.putSerializable(LISTA,listaModelo);
        bundle.putLong(FECHA,fecha);

        icFragmentos.enviarBundleAFragment(bundle, new DiaCalTrabajos());
    }

    @Override
    protected void setLayoutItem() {

        layoutItem = R.layout.item_list_trabajos_en_curso;

    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_PROYECTO;
        campo = PROYECTO_FECHAENTREGACALCULADA;
        campoCard = PROYECTO_NOMBRE;
        campoId = PROYECTO_ID_PROYECTO;


    }

    @Override
    protected void setOnCreate() {
        super.setOnCreate();
        setSinDia(true);
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
    protected void setVerLista(ListaModelo listaModelo, ListaDays listaDays) {

        bundle = new Bundle();
        bundle.putString(ORIGEN,TRABAJOS);
        bundle.putString(ACTUAL, PROYECTO);
        bundle.putSerializable(LISTA,listaModelo);
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
        public void bind(final ModeloSQL modeloSQL) {


            double completada = modeloSQL.getDouble(PROYECTO_TOTCOMPLETADO);
            descripcion.setText(modeloSQL.getString(PROYECTO_DESCRIPCION));
            pbar.setProgress((int)completada);
            cliente.setText(modeloSQL.getString(PROYECTO_CLIENTE_NOMBRE));
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


            long retraso = modeloSQL.getLong(PROYECTO_RETRASO);

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
                    bundle.putString(CAMPO_ID, modeloSQL.getString(PROYECTO_ID_PROYECTO));
                    bundle.putString(ORIGEN, TRABAJOS);
                    bundle.putString(SUBTITULO, modeloSQL.getString(PROYECTO_NOMBRE));
                    bundle.putString(ACTUAL, PROYECTO);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());

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

            ViewGroupLayout vistaCard = new ViewGroupLayout(contexto, relativeLayout, new CardView(contexto));
            card = (CardView) vistaCard.getViewGroup();
            LinearLayoutCompat mainLinear = (LinearLayoutCompat) vistaCard.addVista(new LinearLayoutCompat(contexto));
            ViewGroupLayout vistaLinear = new ViewGroupLayout(contexto, vistaCard.getViewGroup());
            btnNombre = vistaLinear.addButtonSecondary(modeloSQL.getString(PROYECTO_NOMBRE));
            btnNombre.setTextSize(sizeText / 2);
            LinearLayoutCompat.LayoutParams layoutParamsrv = new LinearLayoutCompat.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.altobtn) / 2);
            btnNombre.setLayoutParams(layoutParamsrv);


            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVCard(view);
        }
    }

}
