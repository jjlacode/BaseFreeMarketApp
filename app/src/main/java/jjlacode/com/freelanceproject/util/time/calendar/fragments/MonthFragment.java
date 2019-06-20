package jjlacode.com.freelanceproject.util.time.calendar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.crud.ListaModelo;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.time.calendar.clases.CalendarAdapter;
import jjlacode.com.freelanceproject.util.time.calendar.clases.Day;

import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.CAMPOS_EVENTO;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.Tablas.EVENTO_FECHAINIEVENTO;

/**
 * Created by DARWIN on 3/3/2017.
 */

public class MonthFragment extends Fragment implements CalendarAdapter.DayOnClickListener {

    public static final String MONTH = "jjlacode.com.freelanceproject.util.time.calendar.monthfragemnt.month";
    public static final String YEAR = "jjlacode.com.freelanceproject.util.time.calendar.monthfragemnt.year";
    public static final String BCDays = "jjlacode.com.freelanceproject.util.time.calendar.monthfragemnt.BCDAYS";
    public static final String BCDaysNV = "jjlacode.com.freelanceproject.util.time.calendar.monthfragemnt.BCDAYSNV";
    public static final String BCCDay = "jjlacode.com.freelanceproject.util.time.calendar.monthfragemnt.BCCDAY";
    public static final String TCDAYS = "jjlacode.com.freelanceproject.util.time.calendar.monthfragemnt.TCDAYS";
    public static final String TCDAYSNV = "jjlacode.com.freelanceproject.util.time.calendar.monthfragemnt.TCDAYSNV";
    public static final String TCSDAY = "jjlacode.com.freelanceproject.util.time.calendar.monthfragemnt.TCSDAY";
    public static final String BCSDAY = "jjlacode.com.freelanceproject.util.time.calendar.monthfragemnt.BCSDAY";


    private Context context;
    private View rootView;
    private RecyclerView recyclerViewDays;
    private CalendarAdapter calendarAdapter;
    private ArrayList<Day> days = new ArrayList<>();
    private int imonth, iyear, currentDay, backgroundColorDays, backgroundColorDaysNV, backgroundColorCurrentDay, textColorCurrentDayDay, textColorDays, textColorDaysNV;
    private String campo;
    private ListaModelo listaModelo;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        imonth = getCurrentMonth();
        iyear = getCurrentYear();
        currentDay = getCurrentDayMonth();

        backgroundColorDays = getArguments().getInt(BCDays);
        backgroundColorDaysNV = getArguments().getInt(BCDaysNV);
        backgroundColorCurrentDay = getArguments().getInt(BCCDay);
        textColorCurrentDayDay = getArguments().getInt(TCSDAY);
        textColorDays = getArguments().getInt(TCDAYS);
        textColorDaysNV = getArguments().getInt(TCDAYSNV);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_month, container, false);
        recyclerViewDays = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerViewDays.setLayoutManager(new GridLayoutManager(context, 7));


        int month = getArguments().getInt(MONTH);
        int year = getArguments().getInt(YEAR);


        System.out.println("year = " + year);
        System.out.println("month = " + month);

        fillUpMonth(month, year);

        calendarAdapter = new CalendarAdapter(context, days, getArguments().getInt(TCSDAY), getArguments().getInt(BCSDAY));
        calendarAdapter.setDayOnClickListener(this);


        recyclerViewDays.setAdapter(calendarAdapter);




        return rootView;
    }

    public void setCampo(String campo){
        this.campo = campo;
    }

    private void fillUpMonth(int month, int year) {
        //almaceno el nombre del primer  dia del mes y año en cuestion
        String nameFirstDay = getNameDay(1, month, year);


        int blankSpaces = 0;
        switch (nameFirstDay) {
            case "Monday":
                blankSpaces = 1;
                break;
            case "Tuesday":
                blankSpaces = 2;
                break;
            case "Wednesday":
                blankSpaces = 3;
                break;
            case "Thursday":
                blankSpaces = 4;
                break;
            case "Friday":
                blankSpaces = 5;
                break;
            case "Saturday":
                blankSpaces = 6;
                break;
        }

        int squares = 0;

        if (blankSpaces > 0) {
            int tmonth;
            int tyear = year;
            if (month == 0) {
                tmonth = 11;
                tyear = year - 1;
            } else {
                tmonth = month - 1;
            }

            int numdays = getNumberOfDaysMonthYear(tyear, tmonth);

            for (int i = numdays - blankSpaces + 1; i <= numdays; i++) {
                Date date = new Date(tyear,tmonth,i);
                days.add(new Day(date, false, textColorDaysNV, backgroundColorDaysNV));
                squares++;
            }
        }


        int numberOfDaysMonthYear = getNumberOfDaysMonthYear(year, month);

        for (int i = 1; i <= numberOfDaysMonthYear; i++) {

            Calendar c = new GregorianCalendar(year, month, i);

            //Date date = new Date(year,month,i);
            long fecha = c.getTimeInMillis();
            ListaModelo lista = new ListaModelo(listaModelo.getLista());
            lista.clear();

            System.out.println("listaModelo diario = " + listaModelo.size());
            if (listaModelo.chech()) {
                for (Modelo modelo : listaModelo.getLista()) {

                    System.out.println("fecha campo = " +JavaUtil.soloFecha(modelo.getLong(campo)));
                    System.out.println("fecha calendario = "+ fecha);
                    if (JavaUtil.getDate(modelo.getLong(campo)).equals(JavaUtil.getDate(fecha))) {

                        lista.add(modelo);
                        System.out.println("Fecha evento cal = " + JavaUtil.getDate(modelo.getLong(campo)));

                    }
                }
            }

            if (lista.size()>0  && this.currentDay != i) {
                days.add(new Day(new Date(fecha), R.color.Color_card_notok,R.color.Color_card_notok, lista));
            }else if (lista.size()>0  && this.iyear == year && this.imonth == month && this.currentDay == i) {
                days.add(new Day(new Date(fecha), R.color.Color_card_notok, backgroundColorCurrentDay,lista));
            }else if (this.iyear == year && this.imonth == month && this.currentDay == i) {
                days.add(new Day(new Date(fecha), textColorCurrentDayDay, backgroundColorCurrentDay));
            } else {
                days.add(new Day(new Date(fecha), textColorDays, backgroundColorDays));
            }

            squares++;
        }

        if (squares < 42) {

            int tmonth;
            int tyear = year;
            if (month == 11) {
                tmonth = 0;
                tyear = year + 1;
            } else {
                tmonth = month + 1;
            }

            for (int i = 1; i < 20; i++) {
                days.add(new Day(new Date(tyear, tmonth, i), false, textColorDaysNV, backgroundColorDaysNV));
                squares++;
                if (squares == 42) {
                    break;
                }
            }
        }

    }


    /**
     * retorna el mes actual iniciando desde 0=enero
     *
     * @return
     */
    public int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    /**
     * retorna el año actual
     *
     * @return
     */
    public int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * retorna el dia del mes actual
     *
     * @return
     */
    public int getCurrentDayMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * calcula el numero de dias que tiene un mes de una año especifico
     *
     * @param year
     * @param month
     * @return
     */
    public int getNumberOfDaysMonthYear(int year, int month) {
        Calendar mycal = new GregorianCalendar(year, month, 1);
        return mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    /**
     * nos retorna el nombre de un dia especifico de una año (en ingles o español segun la configuracion)
     *
     * @param day
     * @param month
     * @param year
     * @return nombre del dia
     */
    public String getNameDay(int day, int month, int year) {
        Date date1 = (new GregorianCalendar(year, month, day)).getTime();
        // Then get the day of week from the Date based on specific locale.
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date1);
    }

    /**
     * un objeto de tipo day para obtener la fecha (año,mes,dia) con un objeto calendar
     * <p>
     * otros metodos como setBackgroundColor(int backgroundColor) y getBackgroundColor() color del fondo del numero de dia del mes
     * setTextColor(int textColor) y getTextColor() color del texto numero de dia del mes
     *
     * @param day
     * @param position
     */
    @Override
    public void dayOnClick(Day day, int position) {
        onDayClickListener.dayOnClick(day, position);
    }

    /**
     * similar a dayOnClick solo que este se ejecuta cuando haya un clic prolongado
     *
     * @param day
     * @param position
     */
    @Override
    public void dayOnLongClik(Day day, int position) {

        onDayClickListener.dayOnLongClik(day, position);
    }

    public void setLista(ListaModelo listaModelo) {
        this.listaModelo = listaModelo;
    }

    public interface OnSwipeListener {
        void rightSwipe();

        void leftSwipe();
    }


    private OnSwipeListener onSwipeListener;

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }


    public interface OnDayClickListener {
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


    private OnDayClickListener onDayClickListener;

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
    }


    public void setItemSelected(int position) {
        days.get(position).setSelected(true);
        for (int i = 0; i < days.size(); i++) {
            if (i != position)
                days.get(i).setSelected(false);
        }
        calendarAdapter.notifyItemChanged(0, 41);
        calendarAdapter.notifyDataSetChanged();
    }


    /**
     * marca un dia en el calendario como seleccionado
     *
     * @param position
     */
    public void addItemSelected(int position) {
        days.get(position).setSelected(true);
        calendarAdapter.notifyItemChanged(position);
        calendarAdapter.notifyDataSetChanged();
    }


    /**
     * remueve un dia en el calendario como seleccionado
     *
     * @param position
     */
    public void removeItemSelected(int position) {
        days.get(position).setSelected(false);
        calendarAdapter.notifyItemChanged(position);
        calendarAdapter.notifyDataSetChanged();
    }


    public ArrayList<Day> getDays() {
        return days;
    }
}
