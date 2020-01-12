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
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.codevsolution.freemarketsapp.sqlite.ContratoPry;

import java.util.ArrayList;

import static com.codevsolution.base.android.AppActivity.viewOnMapA;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.EVENTO;

public class DiaCalCalendario extends DiaCalHorario implements ContratoPry.Tablas,
        JavaUtil.Constantes, Interactor.TiposEvento {


    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected void setOnBack() {

        bundle = new Bundle();
        bundle.putString(ORIGEN, CALENDARIO);
        bundle.putString(ACTUAL, CALENDARIO);
        bundle.putString(CAMPO_ID, null);
        bundle.putSerializable(LISTA, null);
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
        bundle.putBoolean(NUEVOREGISTRO, true);
        bundle.putString(ORIGEN, CALENDARIO);
        bundle.putLong(FECHA, fecha);
        bundle.putLong(HORACAL, horaCal);
        bundle.putString(ACTUAL, EVENTO);
        bundle.putString(CAMPO_ID, null);
        bundle.putSerializable(LISTA, null);
        bundle.putSerializable(MODELO, null);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevoEvento());
    }

    @Override
    protected ListaModeloSQL setListaDia(long fecha) {
        super.setListaDia(fecha);

        return CalendarioEventos.listaEventosFecha(fecha);
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

    public class ViewHolderRVcont extends BaseViewHolder implements TipoViewHolder {

        TextView descripcion, hora, telefono, email, lugar, horafin, fechafin, fechaini;
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
        public void bind(final ModeloSQL modeloSQL) {

            gone(card);
            long horaIniEvento = modeloSQL.getLong(EVENTO_HORAINIEVENTO);
            long horaFinEvento = modeloSQL.getLong(EVENTO_HORAFINEVENTO);
            long fechaIniEvento = modeloSQL.getLong(EVENTO_FECHAINIEVENTO);
            long fechaFinEvento = modeloSQL.getLong(EVENTO_FECHAFINEVENTO);
            //Si la fecha coincide con la fecha del dia
            if (!getDescanso() && fechaIniEvento > 0 && fechaFinEvento > 0 && fechaIniEvento <= fecha && fechaFinEvento >= fecha) {
                //Si la hora coincide con el intervalo de la celda
                if ((fechaIniEvento < fecha && fechaFinEvento > fecha) ||
                        (fechaIniEvento < fecha && fechaFinEvento == fecha && (horaFinEvento > 0
                                && horaFinEvento >= horaCal + (30 * MINUTOSLONG)) ||
                                (fechaIniEvento == fecha && fechaFinEvento > fecha && horaIniEvento > 0 && horaIniEvento <= horaCal) ||
                                (fechaIniEvento == fecha && fechaFinEvento == fecha && horaIniEvento > 0 &&
                                        horaFinEvento > 0 && horaFinEvento >= horaCal + (30 * MINUTOSLONG)
                                        && horaIniEvento <= horaCal))) {

                    visible(card);
                    final String tipoevento = modeloSQL.getString(EVENTO_TIPO);
                    telefono.setVisibility(View.GONE);
                    lugar.setVisibility(View.GONE);
                    email.setVisibility(View.GONE);
                    fechafin.setVisibility(View.GONE);
                    fechaini.setVisibility(View.GONE);
                    horafin.setVisibility(View.GONE);

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
                    double completada = modeloSQL.getDouble(EVENTO_COMPLETADA);
                    descripcion.setText(modeloSQL.getString(EVENTO_DESCRIPCION));
                    hora.setText(modeloSQL.getString(EVENTO_HORAINIEVENTOF));
                    telefono.setText(modeloSQL.getString(EVENTO_TELEFONO));
                    lugar.setText(modeloSQL.getString(EVENTO_DIRECCION));
                    email.setText(modeloSQL.getString(EVENTO_EMAIL));
                    pbar.setProgress((int) completada);
                    horafin.setText(modeloSQL.getString(EVENTO_HORAFINEVENTOF));
                    fechafin.setText(modeloSQL.getString(EVENTO_FECHAFINEVENTOF));
                    fechaini.setText(modeloSQL.getString(EVENTO_FECHAINIEVENTOF));

                    if (completada > 0) {
                        visible(pbar);
                    } else {
                        gone(pbar);
                    }

                    if (completada < 100) {

                        long retraso = JavaUtil.hoy() - modeloSQL.getLong(EVENTO_FECHAINIEVENTO);

                        if (!tipoevento.equals(TIPOEVENTOTAREA)) {
                            if (retraso > 3 * Interactor.DIASLONG) {
                                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
                            } else if (retraso > Interactor.DIASLONG) {
                                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));
                            } else {
                                card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
                            }//imgret.setImageResource(R.drawable.alert_box_v);}
                        } else {
                            retraso = JavaUtil.hoy() - modeloSQL.getLong(EVENTO_FECHAFINEVENTO);
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

                                if (!modeloSQL.getString(EVENTO_DIRECCION).equals("")) {

                                    viewOnMapA(getContext(), modeloSQL.getString(EVENTO_DIRECCION));
                                }
                            } else if (tipoevento.equals(TIPOEVENTOLLAMADA)) {

                                AppActivity.hacerLlamada(AppActivity.getAppContext()
                                        , modeloSQL.getString(EVENTO_TELEFONO));
                            } else if (tipoevento.equals(TIPOEVENTOEMAIL)) {

                                String path = null;
                                if (modeloSQL.getString(EVENTO_RUTAADJUNTO) != null) {
                                    path = modeloSQL.getString(EVENTO_RUTAADJUNTO);
                                    AppActivity.enviarEmail(AppActivity.getAppContext(),
                                            modeloSQL.getString(EVENTO_EMAIL), modeloSQL.getString(EVENTO_ASUNTO),
                                            modeloSQL.getString(EVENTO_MENSAJE), path);
                                } else {
                                    AppActivity.enviarEmail(AppActivity.getAppContext(),
                                            modeloSQL.getString(EVENTO_EMAIL), modeloSQL.getString(EVENTO_ASUNTO),
                                            modeloSQL.getString(EVENTO_MENSAJE));
                                }
                            }
                        }
                    });

                    ver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            bundle = new Bundle();
                            bundle.putSerializable(MODELO, modeloSQL);
                            bundle.putString(CAMPO_ID, modeloSQL.getString(EVENTO_ID_EVENTO));
                            bundle.putString(ORIGEN, CALENDARIO);
                            bundle.putString(SUBTITULO, JavaUtil.getDate(fecha));
                            bundle.putString(ACTUAL, TIPOEVENTOEVENTO);
                            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());

                        }
                    });
                }
            }
            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVcont(view);
        }
    }
}
