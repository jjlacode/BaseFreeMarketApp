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
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.time.Day;
import com.codevsolution.base.time.ListaDays;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.base.time.calendar.fragments.FragmentMes;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.TiposEstados.TNUEVOPRESUP;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposEstados.TPRESUPACEPTADO;


public class Presupuestos extends FragmentMes {

    @Override
    protected ListaModeloSQL setListaDia(long fecha) {

        ListaModeloSQL listaDia = new ListaModeloSQL();
        ListaModeloSQL listatemp = new ListaModeloSQL(CAMPOS_PROYECTO);
        listabase = new ListaModeloSQL();

        for (ModeloSQL modeloSQL : listatemp.getLista()) {
            if ((modeloSQL.getInt(PROYECTO_TIPOESTADO) >= TNUEVOPRESUP) &&
                    (modeloSQL.getInt(PROYECTO_TIPOESTADO) < TPRESUPACEPTADO)) {
                listabase.addModelo(modeloSQL);
            }
        }

        for (ModeloSQL modeloSQL : listabase.getLista()) {

            if (TimeDateUtil.soloFecha(modeloSQL.getLong(PROYECTO_FECHAENTRADA))
                    >= TimeDateUtil.soloFecha(fecha)) {

                listaDia.addModelo(modeloSQL);
            }
        }

        return listaDia;
    }

    @Override
    protected void setVerDia(long fecha, ListaModeloSQL listaModeloSQL) {

        bundle = new Bundle();
        bundle.putString(ORIGEN, TRABAJOS);
        bundle.putString(ACTUAL, PROYECTO);
        bundle.putSerializable(LISTA, listaModeloSQL);
        bundle.putLong(FECHA, fecha);

        icFragmentos.enviarBundleAFragment(bundle, new DiaCalTrabajos());
    }

    @Override
    protected void setLayoutItem() {

        layoutItem = R.layout.item_list_trabajos_en_curso;

    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_PROYECTO;
        campo = PROYECTO_FECHAENTRADA;

    }

    @Override
    protected void setOnCreate() {
        super.setOnCreate();
        setSinDia(true);
    }

    @Override
    protected void setTitulo() {

        titulo = R.string.presupuestos_pendientes;

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
        bundle.putBoolean(NUEVOREGISTRO, true);
        bundle.putString(ORIGEN, PRESUPUESTOS);
        bundle.putLong(FECHA, fecha);
        bundle.putString(ACTUAL, PRESUPUESTO);
        bundle.putString(CAMPO_ID, null);
        bundle.putSerializable(LISTA, null);
        bundle.putSerializable(MODELO, null);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
    }

    @Override
    protected void setVerLista(ListaModeloSQL listaModeloSQL, ListaDays listaDays) {

        bundle = new Bundle();
        bundle.putString(ORIGEN, PRESUPUESTOS);
        bundle.putString(ACTUAL, PRESUPUESTO);
        bundle.putSerializable(LISTA, listaModeloSQL);
        bundle.putString(CAMPO_ID, null);
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

        TextView descripcion, cliente;
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
            pbar.setProgress((int) completada);
            cliente.setText(modeloSQL.getString(PROYECTO_CLIENTE_NOMBRE));
            MediaUtil imagenUtil = new MediaUtil(getContext());
            try {
                imagenUtil.setImageUri(modeloSQL.getString(PROYECTO_RUTAFOTO), imagen);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (completada > 0) {
                visible(pbar);
            } else {
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
                    bundle.putString(ORIGEN, PRESUPUESTOS);
                    bundle.putString(SUBTITULO, modeloSQL.getString(PROYECTO_NOMBRE));
                    bundle.putString(ACTUAL, PRESUPUESTO);
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

}
