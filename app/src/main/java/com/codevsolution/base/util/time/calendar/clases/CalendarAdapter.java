package com.codevsolution.base.util.time.calendar.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.util.time.Day;
import com.codevsolution.base.util.time.ListaDays;
import com.codevsolution.freemarketsapp.R;

import java.util.Calendar;


/**
 * Created by DARWIN on 1/3/2017.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewDayHolder> {

    private Context context;
    private ListaDays listaDias;
    private int textColorSelectedDay, backgroundColorSelectedDay;
    private int textColorInicioDay, backgroundColorInicioDay;
    private int textColorFinDay, backgroundColorFinDay;
    private int textColorMultiDay, backgroundColorMultiDay;
    private int textColorBuscaDay, backgroundColorBuscaDay;


    private int mes;

    public CalendarAdapter(Context context, int mes, ListaDays listaDias, int textColorSelectedDay, int backgroundColorSelectedDay) {
        this.context = context;
        this.listaDias = listaDias;
        this.textColorSelectedDay = textColorSelectedDay;
        this.backgroundColorSelectedDay = backgroundColorSelectedDay;
        this.textColorInicioDay = context.getResources().getColor(R.color.Color_contador_acept);
        this.backgroundColorInicioDay = context.getResources().getColor(R.color.Color_contador_acept);
        this.textColorFinDay = context.getResources().getColor(R.color.Color_contador_ok);
        this.backgroundColorFinDay = context.getResources().getColor(R.color.Color_contador_ok);
        this.textColorMultiDay = context.getResources().getColor(R.color.colorSecondaryDark);
        this.backgroundColorMultiDay = context.getResources().getColor(R.color.colorSecondaryDark);
        this.textColorBuscaDay = context.getResources().getColor(R.color.Color_busqueda);
        this.backgroundColorBuscaDay = context.getResources().getColor(R.color.Color_busqueda);

        this.mes = mes;
        System.out.println("constructor adapter");
    }

    @Override
    public ViewDayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_calendar, parent, false);

        return new ViewDayHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewDayHolder holder, final int position) {

        final Day dia = listaDias.get(position);
        Calendar cal = dia.getDate();//Calendar.getInstance();
        //cal.setTime(dia.getDate());
        int nday = cal.get(Calendar.DAY_OF_MONTH);
        holder.dia.setText(nday + "");

        if (dia.isBusca()) {
            holder.dia.setTextColor(textColorBuscaDay);
            holder.itemView.setBackgroundColor(backgroundColorBuscaDay);
        }else if (dia.isInicio()) {
            holder.dia.setTextColor(textColorInicioDay);
            holder.itemView.setBackgroundColor(backgroundColorInicioDay);
        } else if (dia.isFin()) {
            holder.dia.setTextColor(textColorFinDay);
            holder.itemView.setBackgroundColor(backgroundColorFinDay);
        }else if (dia.isMulti()) {
            holder.dia.setTextColor(textColorMultiDay);
            holder.itemView.setBackgroundColor(backgroundColorMultiDay);
        }else if (dia.isSelected()) {
            holder.dia.setTextColor(textColorSelectedDay);
            holder.itemView.setBackgroundColor(backgroundColorSelectedDay);
        } else if (dia.isValid()) {
            holder.dia.setTextColor(dia.getTextColor());
            holder.itemView.setBackgroundColor(dia.getBackgroundColor());
        } else {
            holder.dia.setTextColor(dia.getTextColorNV());
            holder.itemView.setBackgroundColor(dia.getBackgroundColorNV());
        }

        holder.dia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dayOnClickListener.dayOnClick(dia, position);
            }
        });

        holder.dia.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dayOnClickListener.dayOnLongClik(dia, position);
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return listaDias.size();
    }

    public class ViewDayHolder extends RecyclerView.ViewHolder {

        Button dia;

        public ViewDayHolder(View itemView) {
            super(itemView);
            dia = (Button) itemView.findViewById(R.id.textViewDay);

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
