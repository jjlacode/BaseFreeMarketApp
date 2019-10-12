package com.codevsolution.freemarketsapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.base.time.calendar.clases.DiaCalBase;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

import static com.codevsolution.base.android.AppActivity.viewOnMapA;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.EVENTO;

public class DiaCalCalendario extends HorarioPerfil implements ContratoPry.Tablas,
        JavaUtil.Constantes, Interactor.TiposEvento {



    @Override
    protected void setOnBack() {

        bundle = new Bundle();
        bundle.putString(ORIGEN,CALENDARIO);
        bundle.putString(ACTUAL, CALENDARIO);
        bundle.putString(CAMPO_ID,null);
        bundle.putSerializable(LISTA,null);
        bundle.putSerializable(MODELO, null);

        icFragmentos.enviarBundleAFragment(bundle, new CalendarioEventos());
    }

    @Override
    protected int setLayoutRvDia() {
        return R.layout.item_list_evento_calendario;
    }

    @Override
    protected TipoViewHolder setViewHolderDia(View view) {
        return new ViewHolderRVcont(view);
    }

    @Override
    protected void onClickHora(DiaCal diaCal) {

        horaCal = diaCal.getHoraCal();

        bundle = new Bundle();
        bundle.putBoolean(NUEVOREGISTRO,true);
        bundle.putString(ORIGEN,CALENDARIO);
        bundle.putLong(FECHA,fecha);
        bundle.putLong(HORACAL,horaCal);
        bundle.putString(ACTUAL, EVENTO);
        bundle.putString(CAMPO_ID,null);
        bundle.putSerializable(LISTA,null);
        bundle.putSerializable(MODELO, null);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevoEvento());
    }

    @Override
    protected ListaModelo setListaDia(long fecha) {
        super.setListaDia(fecha);

        return CalendarioEventos.listaEventosFecha(fecha);
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

    public class ViewHolderRVcont extends BaseViewHolder implements TipoViewHolder {

        TextView descripcion,hora,telefono,email,lugar,horafin,fechafin,fechaini;
        ProgressBar pbar;
        ImageView imagen;
        ImageButton ver;
        CardView card;

        public ViewHolderRVcont(View itemView) {
            super(itemView);

            descripcion = itemView.findViewById(R.id.tvdesceventocalendario);
            hora = itemView.findViewById(R.id.tvhinieventocalendario);
            telefono = itemView.findViewById(R.id.tvtelefonoeventocalendario);
            lugar = itemView.findViewById(R.id.tvlugareventocalendario);
            email = itemView.findViewById(R.id.tvemaieventocalendario);
            imagen = itemView.findViewById(R.id.imgeventocalendario);
            pbar = itemView.findViewById(R.id.pbareventocalendario);
            ver = itemView.findViewById(R.id.btnvereventocalendario);
            horafin = itemView.findViewById(R.id.tvhfineventocalendario);
            fechafin = itemView.findViewById(R.id.tvffineventocalendario);
            fechaini = itemView.findViewById(R.id.tvfinieventocalendario);
            card = itemView.findViewById(R.id.cardeventocalendario);
        }

        @Override
        public void bind(final Modelo modelo) {

            gone(card);
            //Si la fecha coincide con la fecha del dia
            if (modelo.getString(EVENTO_FECHAINIEVENTOF).equals(TimeDateUtil.getDateString(fecha)) ||
                    (modelo.getString(EVENTO_TIPO).equals(TIPOEVENTOTAREA) &&
                            modelo.getString(EVENTO_FECHAFINEVENTOF).equals(TimeDateUtil.getDateString(fecha)))) {
                //Si la hora coincide con el intervalo de la celda
                if ((modelo.getLong(EVENTO_HORAFINEVENTO) > 0 && modelo.getLong(EVENTO_HORAFINEVENTO) >= horaCal
                        && modelo.getLong(EVENTO_HORAFINEVENTO) < horaCal + (30 * MINUTOSLONG)) ||
                        (modelo.getLong(EVENTO_HORAINIEVENTO) > 0 && modelo.getLong(EVENTO_HORAINIEVENTO) >= horaCal
                                && modelo.getLong(EVENTO_HORAINIEVENTO) < horaCal + (30 * MINUTOSLONG)) ||
                        (modelo.getLong(EVENTO_HORAINIEVENTO) > 0 && modelo.getLong(EVENTO_HORAFINEVENTO) >= horaCal
                                && modelo.getLong(EVENTO_HORAINIEVENTO) < horaCal + (30 * MINUTOSLONG) &&
                                modelo.getLong(EVENTO_HORAFINEVENTO) > 0)) {

                    visible(card);
                    final String tipoevento = modelo.getString(EVENTO_TIPO);
                    telefono.setVisibility(View.GONE);
                    lugar.setVisibility(View.GONE);
                    email.setVisibility(View.GONE);
                    fechafin.setVisibility(View.GONE);
                    fechaini.setVisibility(View.GONE);
                    horafin.setVisibility(View.GONE);

                    System.out.println("hora = " + JavaUtil.getTime(modelo.getLong(EVENTO_HORAFINEVENTO)));

                    if (tipoevento.equals(TIPOEVENTOCITA)) {
                        imagen.setImageResource(R.drawable.ic_place_black_24dp);
                        lugar.setVisibility(View.VISIBLE);
                    } else if (tipoevento.equals(TIPOEVENTOLLAMADA)) {
                        imagen.setImageResource(R.drawable.ic_phone_in_talk_black_24dp);
                        telefono.setVisibility(View.VISIBLE);
                    } else if (tipoevento.equals(TIPOEVENTOEMAIL)) {
                        imagen.setImageResource(R.drawable.ic_mail_outline_black_24dp);
                        email.setVisibility(View.VISIBLE);
                    } else if (tipoevento.equals(TIPOEVENTOEVENTO)) {
                        imagen.setImageResource(R.drawable.ic_event_note_black_24dp);
                        fechafin.setVisibility(View.VISIBLE);
                        horafin.setVisibility(View.VISIBLE);
                        fechaini.setVisibility(View.VISIBLE);
                    }
                    double completada = modelo.getDouble(EVENTO_COMPLETADA);
                    descripcion.setText(modelo.getString(EVENTO_DESCRIPCION));
                    hora.setText(modelo.getString(EVENTO_HORAINIEVENTOF));
                    telefono.setText(modelo.getString(EVENTO_TELEFONO));
                    lugar.setText(modelo.getString(EVENTO_DIRECCION));
                    email.setText(modelo.getString(EVENTO_EMAIL));
                    pbar.setProgress((int) completada);
                    horafin.setText(modelo.getString(EVENTO_HORAFINEVENTOF));
                    fechafin.setText(modelo.getString(EVENTO_FECHAFINEVENTOF));
                    fechaini.setText(modelo.getString(EVENTO_FECHAINIEVENTOF));

                    if (completada > 0) {
                        visible(pbar);
                    } else {
                        gone(pbar);
                    }

                    if (completada < 100) {

                        long retraso = JavaUtil.hoy() - modelo.getLong(EVENTO_FECHAINIEVENTO);

                        if (!tipoevento.equals(TIPOEVENTOTAREA)) {
                            if (retraso > 3 * Interactor.DIASLONG) {
                                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
                            } else if (retraso > Interactor.DIASLONG) {
                                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));
                            } else {
                                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
                            }//imgret.setImageResource(R.drawable.alert_box_v);}
                        } else {
                            retraso = JavaUtil.hoy() - modelo.getLong(EVENTO_FECHAFINEVENTO);
                            if (retraso > 3 * Interactor.DIASLONG) {
                                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
                            } else if (retraso > Interactor.DIASLONG) {
                                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));
                            } else {
                                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
                            }
                        }

                    } else {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
                    }

                    imagen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (tipoevento.equals(TIPOEVENTOCITA)) {

                                if (!modelo.getString(EVENTO_DIRECCION).equals("")) {

                                    viewOnMapA(getContext(), modelo.getString(EVENTO_DIRECCION));
                                }
                            } else if (tipoevento.equals(TIPOEVENTOLLAMADA)) {

                                AppActivity.hacerLlamada(AppActivity.getAppContext()
                                        , modelo.getString(EVENTO_TELEFONO));
                            } else if (tipoevento.equals(TIPOEVENTOEMAIL)) {

                                String path = null;
                                if (modelo.getString(EVENTO_RUTAADJUNTO) != null) {
                                    path = modelo.getString(EVENTO_RUTAADJUNTO);
                                    AppActivity.enviarEmail(AppActivity.getAppContext(),
                                            modelo.getString(EVENTO_EMAIL), modelo.getString(EVENTO_ASUNTO),
                                            modelo.getString(EVENTO_MENSAJE), path);
                                } else {
                                    AppActivity.enviarEmail(AppActivity.getAppContext(),
                                            modelo.getString(EVENTO_EMAIL), modelo.getString(EVENTO_ASUNTO),
                                            modelo.getString(EVENTO_MENSAJE));
                                }
                            }
                        }
                    });

                    ver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            bundle = new Bundle();
                            bundle.putSerializable(MODELO, modelo);
                            bundle.putString(CAMPO_ID, modelo.getString(EVENTO_ID_EVENTO));
                            bundle.putString(ORIGEN, CALENDARIO);
                            bundle.putString(SUBTITULO, JavaUtil.getDate(fecha));
                            bundle.putString(ACTUAL, TIPOEVENTOEVENTO);
                            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());

                        }
                    });
                }
            }
            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVcont(view);
        }
    }
}
