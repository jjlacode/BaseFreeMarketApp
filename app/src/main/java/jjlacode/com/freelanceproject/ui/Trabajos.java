package jjlacode.com.freelanceproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.crud.ListaModelo;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.media.MediaUtil;
import jjlacode.com.freelanceproject.util.time.Day;
import jjlacode.com.freelanceproject.util.time.ListaDays;
import jjlacode.com.freelanceproject.util.time.TimeDateUtil;
import jjlacode.com.freelanceproject.util.time.calendar.fragments.FragmentMes;


public class Trabajos extends FragmentMes {

    @Override
    protected ListaModelo setListaDia(long fecha) {

        ListaModelo listaDia = new ListaModelo();
        ListaModelo listatemp = new ListaModelo(CAMPOS_PROYECTO);
        listabase = new ListaModelo();

        for (Modelo modelo : listatemp.getLista()) {
            if ((modelo.getInt(PROYECTO_TIPOESTADO) >= TNUEVOPRESUP) &&
                    (modelo.getInt(PROYECTO_TIPOESTADO) < TPROYECTPENDCOBRO)){
                listabase.addModelo(modelo);
            }
        }

        for (Modelo modelo : listabase.getLista()) {

            if (TimeDateUtil.getDateString(modelo.getLong(PROYECTO_FECHAENTREGAACORDADA))
                    .equals(TimeDateUtil.getDateString(fecha))){

                listaDia.addModelo(modelo);
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
        campo = PROYECTO_FECHAENTREGAACORDADA;

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
    protected ListaAdaptadorFiltroRV setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new ListaAdaptadorFiltroRV(context,layoutItem,lista,campos);
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
            return new ViewHolderRV(view);
        }
    }

}
