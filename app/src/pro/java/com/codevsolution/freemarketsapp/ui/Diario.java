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
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.time.Day;
import com.codevsolution.base.time.ListaDays;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.base.time.calendar.fragments.FragmentMes;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

public class Diario extends FragmentMes {

    @Override
    protected ListaModelo setListaDia(long fecha) {

        ListaModelo listaDia = new ListaModelo();
        listabase = new ListaModelo(CAMPOS_DIARIO);

        for (Modelo modelo : listabase.getLista()) {

            if (modelo.getLong(CAMPO_CREATEREG)==fecha){

                listaDia.addModelo(modelo);
            }
        }

        return listaDia;
    }

    @Override
    protected void setVerDia(long fecha, ListaModelo listaModelo) {

        bundle = new Bundle();
        bundle.putString(ORIGEN,DIARIO);
        bundle.putString(ACTUAL, DIARIO);
        bundle.putSerializable(LISTA,listaModelo);
        bundle.putLong(FECHA,fecha);

        icFragmentos.enviarBundleAFragment(bundle, new DiaCalDiario());
    }

    @Override
    protected void setLayoutItem() {

        layoutItem = R.layout.item_list_diario;



    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_DIARIO;
        campo = DIARIO_CREATE;
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

    }

    @Override
    protected void setVerLista(ListaModelo listaModelo, ListaDays listaDays) {

    }

    @Override
    protected void setOnPrevMonth() {

    }

    @Override
    protected void setOnNextMonth() {

    }

    @Override
    protected void setOnInicio() {

        gone(nuevo);
        gone(verLista);

        setSinNuevo(true);
        setSinLista(true);
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new ListaAdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder, Interactor.TiposNota {

        TextView descripcion, fechaDiario, rel;
        ImageView imagen,ver;
        CardView card;

        public ViewHolderRV(View itemView) {
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
            return new ViewHolderRV(view);
        }
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<Modelo> entrada) {

            TextView descripcion, fechaDiario, rel;
            ImageView imagen,ver;
            CardView card;

            rel = itemView.findViewById(R.id.tvreldiario);
            descripcion = itemView.findViewById(R.id.tvdescdiario);
            imagen = itemView.findViewById(R.id.imagendiario);
            fechaDiario = itemView.findViewById(R.id.tvfechadiario);
            ver = itemView.findViewById(R.id.btnverdiario);
            card = itemView.findViewById(R.id.carddiario);
            gone(ver);

            descripcion.setText(entrada.get(posicion).getString(DIARIO_DESCRIPCION));
            fecha = entrada.get(posicion).getLong(DIARIO_CREATE);
            fechaDiario.setText(TimeDateUtil.getDateString(fecha));
            rel.setText(entrada.get(posicion).getString(DIARIO_NOMBREREL));

            String path;
            if (entrada.get(posicion).getString(DIARIO_RUTAFOTO) != null) {
                imagen.setVisibility(View.VISIBLE);
                path = entrada.get(posicion).getString(DIARIO_RUTAFOTO);
                MediaUtil imagenUtil = new MediaUtil(AppActivity.getAppContext());
                imagenUtil.setImageUriCircle(path, imagen);
            } else {
                //imagenTarea.setVisibility(View.GONE);
                imagen.setImageResource(R.drawable.ic_nota_imagen_indigo);
            }

            super.setEntradas(posicion, itemView, entrada);
        }
    }
}
