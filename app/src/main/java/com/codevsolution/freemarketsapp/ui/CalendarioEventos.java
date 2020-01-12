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
import com.codevsolution.base.time.Day;
import com.codevsolution.base.time.ListaDays;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

import static com.codevsolution.base.android.AppActivity.viewOnMapA;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposEvento.TIPOEVENTOCITA;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposEvento.TIPOEVENTOEMAIL;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposEvento.TIPOEVENTOEVENTO;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposEvento.TIPOEVENTOLLAMADA;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposEvento.TIPOEVENTOTAREA;

public class CalendarioEventos extends FragmentMesHorario {

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected ListaModeloSQL setListaDia(long fecha) {
        return listaEventosFecha(fecha);
    }

    public static ListaModeloSQL listaEventosFecha(long fecha) {

        ListaModeloSQL listaDia = new ListaModeloSQL();
        ListaModeloSQL listaCompleta = new ListaModeloSQL(CAMPOS_EVENTO);

        for (ModeloSQL modeloSQL : listaCompleta.getLista()) {

            if (modeloSQL.getLong(EVENTO_FECHAINIEVENTO) <= fecha &&
                    modeloSQL.getLong(EVENTO_FECHAFINEVENTO) >= fecha) {

                listaDia.addModelo(modeloSQL);
            }
        }

        return listaDia;

    }

    @Override
    protected ListaModeloSQL setListaFija() {

        ListaModeloSQL listaDia = new ListaModeloSQL();
        ListaModeloSQL listaCompleta = new ListaModeloSQL(CAMPOS_EVENTO);

        for (ModeloSQL modeloSQL : listaCompleta.getLista()) {
            if (modeloSQL.getString(EVENTO_TIPO).equals(TIPOEVENTOTAREA) &&
                    modeloSQL.getDouble(EVENTO_COMPLETADA) < 100) {
                listaDia.addModelo(modeloSQL);
            }
        }

        return listaDia;

    }

    @Override
    protected void setVerDia(long fecha, ListaModeloSQL listaModeloSQL) {

        bundle = new Bundle();
        bundle.putString(ORIGEN, CALENDARIO);
        bundle.putString(ACTUAL, CALENDARIO);
        bundle.putString(CAMPO, EVENTO_FECHAINIEVENTO);
        bundle.putLong(FECHA, fecha);
        icFragmentos.enviarBundleAFragment(bundle, new DiaCalCalendario());
    }

    @Override
    protected void setLayoutItem() {

        layoutItem = R.layout.item_list_evento_calendario;

    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_EVENTO;
        campo = EVENTO_FECHAINIEVENTO;

    }

    @Override
    protected void setTitulo() {

        titulo = R.string.calendario;

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
        bundle.putString(ORIGEN, CALENDARIO);
        bundle.putLong(FECHA, fecha);
        bundle.putString(ACTUAL, EVENTO);
        bundle.putString(CAMPO_ID, null);
        bundle.putSerializable(LISTA, null);
        bundle.putSerializable(MODELO, null);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevoEvento());
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();


    }

    @Override
    protected void setVerLista(ListaModeloSQL listaModeloSQL, ListaDays listaDays) {

        bundle = new Bundle();
        bundle.putString(ORIGEN, CALENDARIO);
        bundle.putString(ACTUAL, EVENTO);
        bundle.putSerializable(LISTA, listaModeloSQL);
        bundle.putString(CAMPO_ID, null);
        bundle.putSerializable(MODELO, null);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
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
        icFragmentos.enviarBundleAFragment(bundle, new EventosSem());
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<ModeloSQL> entrada) {

            TextView descripcion, hora, telefono, email, lugar, horafin, fechafin, fechaini;
            ProgressBar pbar;
            ImageView imagen;
            ImageButton ver;
            CardView card;

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
            gone(ver);

            final String tipoevento = entrada.get(posicion).getString(EVENTO_TIPO);
            telefono.setVisibility(View.GONE);
            lugar.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            fechafin.setVisibility(View.GONE);
            //fechaini.setVisibility(View.GONE);
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
            double completada = entrada.get(posicion).getDouble(EVENTO_COMPLETADA);
            descripcion.setText(entrada.get(posicion).getString(EVENTO_DESCRIPCION));
            hora.setText(entrada.get(posicion).getString(EVENTO_HORAINIEVENTOF));
            telefono.setText(entrada.get(posicion).getString(EVENTO_TELEFONO));
            lugar.setText(entrada.get(posicion).getString(EVENTO_DIRECCION));
            email.setText(entrada.get(posicion).getString(EVENTO_EMAIL));
            pbar.setProgress((int) completada);
            horafin.setText(entrada.get(posicion).getString(EVENTO_HORAFINEVENTOF));
            fechafin.setText(entrada.get(posicion).getString(EVENTO_FECHAFINEVENTOF));
            fechaini.setText(entrada.get(posicion).getString(EVENTO_FECHAINIEVENTOF));

            if (completada > 0) {
                visible(pbar);
            } else {
                gone(pbar);
            }

            if (completada < 100) {

                long retraso = JavaUtil.hoy() - entrada.get(posicion).getLong(EVENTO_FECHAINIEVENTO);

                if (!tipoevento.equals(TIPOEVENTOTAREA)) {
                    if (retraso > 3 * Interactor.DIASLONG) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
                    } else if (retraso > Interactor.DIASLONG) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));
                    } else {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
                    }//imgret.setImageResource(R.drawable.alert_box_v);}
                } else {
                    retraso = JavaUtil.hoy() - entrada.get(posicion).getLong(EVENTO_FECHAFINEVENTO);
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

            super.setEntradas(posicion, itemView, entrada);
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView descripcion, hora, telefono, email, lugar, horafin, fechafin, fechaini;
        ProgressBar pbar;
        ImageView imagen;
        ImageButton ver;
        CardView card;

        public ViewHolderRV(View itemView) {
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

            final String tipoevento = modeloSQL.getString(EVENTO_TIPO);
            telefono.setVisibility(View.GONE);
            lugar.setVisibility(View.GONE);
            email.setVisibility(View.GONE);

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

            if (modeloSQL.getString(EVENTO_FECHAFINEVENTOF).equals(modeloSQL.getString(EVENTO_FECHAINIEVENTOF)) &&
                    modeloSQL.getString(EVENTO_HORAFINEVENTOF).equals(modeloSQL.getString(EVENTO_HORAINIEVENTOF))) {
                gone(fechafin);
                gone(horafin);
            }

            if (tipoevento.equals(TIPOEVENTOTAREA)) {
                imagen.setImageResource(R.drawable.ic_tareas_indigo);
                fechaini.setVisibility(View.GONE);
            } else if (tipoevento.equals(TIPOEVENTOCITA)) {
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
            }

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

            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

}
