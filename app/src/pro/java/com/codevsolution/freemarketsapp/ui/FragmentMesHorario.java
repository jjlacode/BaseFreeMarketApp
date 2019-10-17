package com.codevsolution.freemarketsapp.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.time.Day;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.base.time.calendar.fragments.FragmentMes;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by DARWIN on 3/3/2017.
 */

public abstract class FragmentMesHorario extends FragmentMes implements
        JavaUtil.Constantes, ContratoPry.Tablas, Interactor.ConstantesPry {//}, CalendarAdapter.DayOnClickListener {


    @Override
    protected TipoViewHolder setViewHolderCal(View view) {
        return new ViewHolderRV(view);
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        private Button btnDia;
        private int textColorSelectedDay, backgroundColorSelectedDay;
        private int textColorInicioDay, backgroundColorInicioDay;
        private int textColorFinDay, backgroundColorFinDay;
        private int textColorMultiDay, backgroundColorMultiDay;
        private int textColorBuscaDay, backgroundColorBuscaDay;
        RelativeLayout relativeLayout;
        RecyclerView recyclerView;
        CardView card;


        public ViewHolderRV(View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.ry_item_list);
            btnDia = itemView.findViewById(R.id.textViewDay);
            this.textColorSelectedDay = contexto.getResources().getColor(R.color.colorSecondaryDark);
            this.backgroundColorSelectedDay = contexto.getResources().getColor(R.color.Color_contador_notok);
            this.textColorInicioDay = contexto.getResources().getColor(R.color.colorSecondaryDark);
            this.backgroundColorInicioDay = contexto.getResources().getColor(R.color.Color_contador_acept);
            this.textColorFinDay = contexto.getResources().getColor(R.color.colorSecondaryDark);
            this.backgroundColorFinDay = contexto.getResources().getColor(R.color.Color_contador_ok);
            this.textColorMultiDay = contexto.getResources().getColor(R.color.colorPrimary);
            this.backgroundColorMultiDay = contexto.getResources().getColor(R.color.colorSecondaryDark);
            this.textColorBuscaDay = contexto.getResources().getColor(R.color.colorSecondaryDark);
            this.backgroundColorBuscaDay = contexto.getResources().getColor(R.color.Color_busqueda);
        }

        @Override
        public void bind(ArrayList<?> lista, final int position) {

            final Day dia = (Day) lista.get(position);
            Calendar cal = dia.getDate();//Calendar.getInstance();
            //cal.setTime(dia.getDate());
            int nday = cal.get(Calendar.DAY_OF_MONTH);
            int nomDay = cal.get(Calendar.DAY_OF_WEEK);
            boolean descanso = false;

            ViewGroupLayout vistaCard = new ViewGroupLayout(contexto, relativeLayout, new CardView(contexto));
            card = (CardView) vistaCard.getViewGroup();
            LinearLayoutCompat mainLinear = (LinearLayoutCompat) vistaCard.addVista(new LinearLayoutCompat(contexto));
            mainLinear.setOrientation(ViewGroupLayout.ORI_LLC_HORIZONTAL);
            ViewGroupLayout vistaLinear = new ViewGroupLayout(contexto, mainLinear);

            Modelo perfil = CRUDutil.updateModelo(CAMPOS_PERFIL, PERFIL_NOMBRE, Interactor.perfila, null, IGUAL, null);
            if ((nomDay == Calendar.SUNDAY && perfil.getDouble(PERFIL_HORASDOMINGO) == 0) ||
                    (nomDay == Calendar.MONDAY && perfil.getDouble(PERFIL_HORASLUNES) == 0) ||
                    (nomDay == Calendar.TUESDAY && perfil.getDouble(PERFIL_HORASMARTES) == 0) ||
                    (nomDay == Calendar.WEDNESDAY && perfil.getDouble(PERFIL_HORASMIERCOLES) == 0) ||
                    (nomDay == Calendar.THURSDAY && perfil.getDouble(PERFIL_HORASJUEVES) == 0) ||
                    (nomDay == Calendar.FRIDAY && perfil.getDouble(PERFIL_HORASVIERNES) == 0) ||
                    (nomDay == Calendar.SATURDAY && perfil.getDouble(PERFIL_HORASSABADO) == 0)) {
                btnDia = vistaLinear.addButtonPrimary(null);
                descanso = true;
            } else {
                btnDia = vistaLinear.addButtonTrans(null);
                if (campoCard != null) {
                    recyclerView = (RecyclerView) vistaLinear.addVista(new RecyclerView(contexto));
                    recyclerView.setLayoutManager(new LinearLayoutManager(contexto));
                    ListaModelo listaEvento = setListaDia(cal.getTimeInMillis());
                    RVAdapter adaptadorRV = new RVAdapter(setViewHolderCard(itemView),
                            listaEvento.getLista(), R.layout.item_list_layout);
                    recyclerView.setAdapter(adaptadorRV);
                    LinearLayoutCompat.LayoutParams layoutParamsrv = new LinearLayoutCompat.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    recyclerView.setLayoutParams(layoutParamsrv);
                }
            }

            btnDia.setText(nday + "");
            btnDia.setTextSize(sizeText);

            if (!descanso) {
                if (dia.isBusca()) {
                    btnDia.setTextColor(textColorBuscaDay);
                    card.setBackgroundColor(backgroundColorBuscaDay);
                    if (recyclerView != null)
                        recyclerView.setBackgroundColor(backgroundColorBuscaDay);
                } else if (dia.isInicio()) {
                    btnDia.setTextColor(textColorInicioDay);
                    card.setBackgroundColor(backgroundColorInicioDay);
                    if (recyclerView != null)
                        recyclerView.setBackgroundColor(backgroundColorInicioDay);
                } else if (dia.isFin()) {
                    btnDia.setTextColor(textColorFinDay);
                    card.setBackgroundColor(backgroundColorFinDay);
                    if (recyclerView != null)
                        recyclerView.setBackgroundColor(backgroundColorFinDay);
                } else if (dia.isMulti()) {
                    btnDia.setTextColor(textColorMultiDay);
                    card.setBackgroundColor(backgroundColorMultiDay);
                    if (recyclerView != null)
                        recyclerView.setBackgroundColor(backgroundColorMultiDay);
                } else if (dia.isSelected()) {
                    btnDia.setTextColor(textColorSelectedDay);
                    card.setBackgroundColor(backgroundColorSelectedDay);
                    if (recyclerView != null)
                        recyclerView.setBackgroundColor(backgroundColorSelectedDay);
                } else if (dia.isValid()) {
                    btnDia.setTextColor(dia.getTextColor());
                    card.setBackgroundColor(dia.getBackgroundColor());
                    if (recyclerView != null)
                        recyclerView.setBackgroundColor(dia.getBackgroundColor());
                } else {
                    btnDia.setTextColor(dia.getTextColorNV());
                    card.setBackgroundColor(dia.getBackgroundColorNV());
                    if (recyclerView != null)
                        recyclerView.setBackgroundColor(dia.getBackgroundColorNV());
                }
            } else {
                btnDia.setTextColor(dia.getTextColorNV());
                card.setBackgroundColor(dia.getBackgroundColorNV());
                if (recyclerView != null)
                    recyclerView.setBackgroundColor(dia.getBackgroundColorNV());

            }

            if (dia.getFechaLong() == TimeDateUtil.soloFecha(JavaUtil.hoy())) {
                btnDia.setBackgroundColor(contexto.getResources().getColor(R.color.colorSecondary));
                btnDia.setTextColor(contexto.getResources().getColor(R.color.colorSecondaryDark));
            }

            btnDia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //dayOnClickListener.dayOnClick(dia, position);
                    onDayClickListener.dayOnClick(dia, position);
                }
            });

            btnDia.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //dayOnClickListener.dayOnLongClik(dia, position);
                    onDayClickListener.dayOnLongClik(dia, position);
                    return false;
                }
            });

            super.bind(lista, position);
        }


        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

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
        public void bind(Modelo modelo) {

            ViewGroupLayout vistaCard = new ViewGroupLayout(contexto, relativeLayout, new CardView(contexto));
            card = (CardView) vistaCard.getViewGroup();
            LinearLayoutCompat mainLinear = (LinearLayoutCompat) vistaCard.addVista(new LinearLayoutCompat(contexto));
            ViewGroupLayout vistaLinear = new ViewGroupLayout(contexto, vistaCard.getViewGroup());
            btnNombre = vistaLinear.addButtonSecondary(campoCard);
            btnNombre.setTextSize(sizeText / 2);
            LinearLayoutCompat.LayoutParams layoutParamsrv = new LinearLayoutCompat.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.altobtn) / 2);
            btnNombre.setLayoutParams(layoutParamsrv);


            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVCard(view);
        }
    }


    public interface DayOnClickListener {
        /**
         * un objeto de tipo day para obtener la fecha (año,mes,dia) con un objeto calendar
         * <p>
         * otros metodos como setBackgroundColor(int backgroundColor) y getBackgroundColor() color del fondo del numero de dia del mes
         * setTextColor(int textColor) y getTextColor() color del texto numero de dia del mes
         *
         * @param day
         */
        void dayOnClick(Day day, int position);

        /**
         * similar a dayOnClick solo que este se ejecuta cuando haya un clic prolongado
         *
         * @param day
         */
        void dayOnLongClik(Day day, int position);

    }


    private DayOnClickListener dayOnClickListener;

    public void setDayOnClickListener(DayOnClickListener dayOnClickListener) {
        this.dayOnClickListener = dayOnClickListener;
    }

}
