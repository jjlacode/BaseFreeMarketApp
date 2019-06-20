package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.adapter.RVAdapter;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.android.AppActivity;
import jjlacode.com.freelanceproject.util.android.FragmentBase;
import jjlacode.com.freelanceproject.util.crud.CRUDutil;
import jjlacode.com.freelanceproject.util.crud.ListaModelo;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.media.MediaUtil;
import jjlacode.com.freelanceproject.util.time.calendar.clases.Day;
import jjlacode.com.freelanceproject.util.time.calendar.views.OneCalendarView;

import static jjlacode.com.freelanceproject.CommonPry.Constantes.DIARIO;
import static jjlacode.com.freelanceproject.CommonPry.Constantes.EVENTO;
import static jjlacode.com.freelanceproject.CommonPry.Constantes.NOTA;
import static jjlacode.com.freelanceproject.CommonPry.TiposEvento.TIPOEVENTOCITA;
import static jjlacode.com.freelanceproject.CommonPry.TiposEvento.TIPOEVENTOEMAIL;
import static jjlacode.com.freelanceproject.CommonPry.TiposEvento.TIPOEVENTOEVENTO;
import static jjlacode.com.freelanceproject.CommonPry.TiposEvento.TIPOEVENTOLLAMADA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ACTUAL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.CALENDARIO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.FECHA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ID;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.IGUAL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.MODELO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.NUEVOREGISTRO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ORIGEN;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.SUBTITULO;
import static jjlacode.com.freelanceproject.util.android.AppActivity.viewOnMapA;

public class Diario extends FragmentBase implements ContratoPry.Tablas {


    OneCalendarView calendarView;
    RecyclerView rv;
    ListaModelo lista;
    long fecha;
    String[] campos;
    String campo;
    int layoutItem;
    private int positionOld;

    @Override
    protected void setLayout() {

        layout = R.layout.calendario;

    }

    @Override
    protected void setInicio() {

        calendarView = view.findViewById(R.id.oneCalendar);
        rv = view.findViewById(R.id.rveventocalendario);
        layoutItem = R.layout.item_list_nota;
        campos = CAMPOS_NOTA;
        campo = NOTA_FECHA;
        lista = new ListaModelo(campos);

            long fechaHoy = JavaUtil.hoyFecha();
            ListaModelo listaModelo = CRUDutil.setListaModelo(campos,NOTA_ID_RELACIONADO,null,IGUAL);
        System.out.println("listaModelo = " + listaModelo.size());
            lista.clear();

            if (listaModelo.chech()) {
                for (Modelo modelo : listaModelo.getLista()) {

                    System.out.println("fecha = " + JavaUtil.getDate(fechaHoy));

                    if (JavaUtil.getDate(modelo.getLong(campo)).equals(JavaUtil.getDate(fechaHoy))) {

                        lista.add(modelo);
                        System.out.println("modelo = " + JavaUtil.getDate(modelo.getLong(campo)));

                    }
                }
            }

        RVAdapter adaptadorRV = new RVAdapter(new ViewHolderRV(view),
                lista.getLista(),layoutItem, null);
        rv.setAdapter(adaptadorRV);

        calendarView.setLista(listaModelo);
        calendarView.setCampo(campo);

        calendarView.setOneCalendarClickListener(new OneCalendarView.OneCalendarClickListener() {
            @Override
            public void dateOnClick(Day day, int position) {

                lista = day.getLista();
                if (lista==null){
                    lista = new ListaModelo(campos);
                    lista.clear();
                }
                fecha = day.getDate().getTime();
                System.out.println("fecha day= " + JavaUtil.getDate(fecha));
                RVAdapter adaptadorRV = new RVAdapter(new ViewHolderRV(view),
                        lista.getLista(),layoutItem, null);
                rv.setAdapter(adaptadorRV);

                calendarView.removeDaySeleted(positionOld);
                calendarView.addDaySelected(position);
                positionOld = position;

            }

            @Override
            public void dateOnLongClick(Day day, int position) {

                bundle = new Bundle();
                bundle.putBoolean(NUEVOREGISTRO,true);
                bundle.putString(ORIGEN,DIARIO);
                bundle.putLong(FECHA,day.getDate().getTime());
                bundle.putString(ACTUAL, NOTA);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());

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


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder, CommonPry.TiposNota {

        TextView descripcion, fecha, tipoNota;
        ImageView imagen;

        public ViewHolderRV(View itemView) {
            super(itemView);
            tipoNota = itemView.findViewById(R.id.tvtipo_lnota);
            descripcion = itemView.findViewById(R.id.tvdesc_lnota);
            imagen = itemView.findViewById(R.id.imagen_lnota);
            fecha = itemView.findViewById(R.id.tvfechalnota);

        }

        @Override
        public void bind(Modelo modelo) {

            descripcion.setText(modelo.getString(NOTA_TITULO));
            fecha.setText(JavaUtil.getDateTime(modelo.getLong(NOTA_FECHA)));
            String tipo = modelo.getString(NOTA_TIPO);
            tipoNota.setText(tipo);
            tipoNota.setVisibility(View.GONE);
            String path;
            //imagenTarea.setVisibility(View.GONE);

            if (tipo != null) {

                switch (tipo) {

                    case NOTATEXTO:

                        imagen.setImageResource(R.drawable.ic_format_list_numbered_black_24dp);

                        break;

                    case NOTAAUDIO:

                        imagen.setImageResource(R.drawable.ic_mic_black_24dp);
                        if (modelo.getString(NOTA_RUTA) != null) {
                            path = modelo.getString(NOTA_RUTA);
                        }

                        break;

                    case NOTAVIDEO:

                        imagen.setImageResource(R.drawable.ic_videocam_black_24dp);
                        if (modelo.getString(NOTA_RUTA) != null) {
                            path = modelo.getString(NOTA_RUTA);
                        }

                        break;

                    case NOTAIMAGEN:

                        if (modelo.getString(NOTA_RUTA) != null) {
                            imagen.setVisibility(View.VISIBLE);
                            path = modelo.getString(NOTA_RUTA);
                            MediaUtil imagenUtil = new MediaUtil(AppActivity.getAppContext());
                            imagenUtil.setImageUriCircle(path, imagen);
                        } else {
                            //imagenTarea.setVisibility(View.GONE);
                            imagen.setImageResource(R.drawable.ic_add_a_photo_black_24dp);
                        }
                }
            }

            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }
}
