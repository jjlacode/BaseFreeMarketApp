package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.android.AppActivity;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.android.FragmentBase;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.crud.ListaModelo;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.adapter.RVAdapter;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.time.calendar.clases.Day;
import jjlacode.com.freelanceproject.util.time.calendar.views.OneCalendarView;

import static jjlacode.com.freelanceproject.util.android.AppActivity.viewOnMapA;
import static jjlacode.com.freelanceproject.CommonPry.TiposEvento.TIPOEVENTOCITA;
import static jjlacode.com.freelanceproject.CommonPry.TiposEvento.TIPOEVENTOEMAIL;
import static jjlacode.com.freelanceproject.CommonPry.TiposEvento.TIPOEVENTOEVENTO;
import static jjlacode.com.freelanceproject.CommonPry.TiposEvento.TIPOEVENTOLLAMADA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ACTUAL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.CALENDARIO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.FECHA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ID;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.MODELO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.NUEVOREGISTRO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ORIGEN;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.SUBTITULO;

public class Calendario extends FragmentBase implements ContratoPry.Tablas {


    OneCalendarView calendarView;
    RecyclerView rv;
    ListaModelo lista;
    long fecha;

    @Override
    protected void setLayout() {

        layout = R.layout.calendario;

    }

    @Override
    protected void setInicio() {

        calendarView = view.findViewById(R.id.oneCalendar);
        rv = view.findViewById(R.id.rveventocalendario);
        lista = new ListaModelo(CAMPOS_EVENTO);
        RVAdapter adaptadorRV = new RVAdapter(new ViewHolderRV(view),
                lista.getLista(),R.layout.item_list_evento_calendario, null);
        rv.setAdapter(adaptadorRV);

        calendarView.setOneCalendarClickListener(new OneCalendarView.OneCalendarClickListener() {
            @Override
            public void dateOnClick(Day day, int position) {

                lista = day.getLista();
                if (lista==null){lista = new ListaModelo(CAMPOS_EVENTO);lista.clear();}
                fecha = day.getDate().getTime();
                RVAdapter adaptadorRV = new RVAdapter(new ViewHolderRV(view),
                        lista.getLista(),R.layout.item_list_evento_calendario, null);
                rv.setAdapter(adaptadorRV);
                /*
                if (lista!=null && lista.size()>0) {
                    bundle = new Bundle();
                    bundle.putSerializable(LISTA, lista);
                    bundle.putString(ORIGEN, CALENDARIO);
                    bundle.putString(ACTUAL, CommonPry.TiposEvento.TIPOEVENTOEVENTO);
                    bundle.putLong(FECHA,day.getDate().getTime());
                    bundle.putString(SUBTITULO, JavaUtil.getDate(day.getDate().getTime()));
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
                }

                 */

            }

            @Override
            public void dateOnLongClick(Day day, int position) {

                bundle = new Bundle();
                bundle.putBoolean(NUEVOREGISTRO,true);
                bundle.putString(ORIGEN,CALENDARIO);
                bundle.putLong(FECHA,day.getDate().getTime());
                bundle.putString(ACTUAL, TIPOEVENTOEVENTO);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());

            }
        });

        calendarView.setOnCalendarChangeListener(new OneCalendarView.OnCalendarChangeListener() {
            @Override
            public void prevMonth() {


            }

            @Override
            public void nextMonth() {


            }
        });


    }


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView descripcion,hora,telefono,email,lugar,horafin,fechafin,fechaini;
        ProgressBar pbar;
        ImageView imagen;
        ImageButton ver;

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

        }

        @Override
        public void bind(final Modelo modelo) {

            final String tipoevento = modelo.getString(EVENTO_TIPOEVENTO);
            telefono.setVisibility(View.GONE);
            lugar.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            fechafin.setVisibility(View.GONE);
            fechaini.setVisibility(View.GONE);
            horafin.setVisibility(View.GONE);

            if (tipoevento.equals(TIPOEVENTOCITA)){
                imagen.setImageResource(R.drawable.ic_place_black_24dp);
                lugar.setVisibility(View.VISIBLE);
            }
            else if (tipoevento.equals(TIPOEVENTOLLAMADA)){
                imagen.setImageResource(R.drawable.ic_phone_in_talk_black_24dp);
                telefono.setVisibility(View.VISIBLE);
            }
            else if (tipoevento.equals(TIPOEVENTOEMAIL)){
                imagen.setImageResource(R.drawable.ic_mail_outline_black_24dp);
                email.setVisibility(View.VISIBLE);
            }
            else if (tipoevento.equals(TIPOEVENTOEVENTO)){
                imagen.setImageResource(R.drawable.ic_event_note_black_24dp);
                fechafin.setVisibility(View.VISIBLE);
                horafin.setVisibility(View.VISIBLE);
                fechaini.setVisibility(View.VISIBLE);
            }
            descripcion.setText(modelo.getString(EVENTO_DESCRIPCION));
            hora.setText(modelo.getString(EVENTO_HORAINIEVENTOF));
            telefono.setText(modelo.getString(EVENTO_TELEFONO));
            lugar.setText(modelo.getString(EVENTO_LUGAR));
            email.setText(modelo.getString(EVENTO_EMAIL));
            pbar.setProgress((int)modelo.getDouble(EVENTO_COMPLETADA));
            horafin.setText(modelo.getString(EVENTO_HORAFINEVENTOF));
            fechafin.setText(modelo.getString(EVENTO_FECHAFINEVENTOF));
            fechaini.setText(modelo.getString(EVENTO_FECHAINIEVENTOF));

            imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (tipoevento.equals(TIPOEVENTOCITA)){

                        if (!modelo.getString(EVENTO_LUGAR).equals("")){

                            viewOnMapA(getContext(),modelo.getString(EVENTO_LUGAR));
                        }
                    }else if (tipoevento.equals(TIPOEVENTOLLAMADA)){

                        AppActivity.hacerLlamada(AppActivity.getAppContext()
                                ,modelo.getString(EVENTO_TELEFONO));
                    }else if (tipoevento.equals(TIPOEVENTOEMAIL)){

                        String path =null;
                        if (modelo.getString(EVENTO_RUTAADJUNTO)!=null){
                            path = modelo.getString(EVENTO_RUTAADJUNTO);
                        }
                        AppActivity.enviarEmail(AppActivity.getAppContext(),
                                modelo.getString(EVENTO_EMAIL), modelo.getString(EVENTO_ASUNTO),
                                modelo.getString(EVENTO_MENSAJE), path);
                    }
                }
            });

            ver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bundle = new Bundle();
                    bundle.putSerializable(MODELO, modelo);
                    bundle.putString(ID,modelo.getString(EVENTO_ID_EVENTO));
                    bundle.putString(ORIGEN, CALENDARIO);
                    bundle.putString(SUBTITULO, JavaUtil.getDate(fecha));
                    bundle.putString(ACTUAL, TIPOEVENTOEVENTO);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());

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
