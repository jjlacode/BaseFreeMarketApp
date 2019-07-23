package jjlacode.com.freelanceproject.util.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeDateUtil {

    static long SEGUNDOSLONG = (1000);
    static long MINUTOSLONG = (60 * SEGUNDOSLONG);
    static long HORASLONG = (60 * MINUTOSLONG);
    static long DIASLONG = (24 * HORASLONG);
    static long SEMANASLONG = (7 * DIASLONG);
    static long MESESLONG = (30 * DIASLONG);
    static long ANIOSLONG = (12 * MESESLONG);

    public static ListaDays listaDias(Calendar fechaIni, Calendar fechaEnd) {

        ListaDays days = new ListaDays();

        int yearIni = fechaIni.get(Calendar.YEAR);
        int yearEnd = fechaEnd.get(Calendar.YEAR);
        int monthIni = fechaIni.get(Calendar.MONTH);
        int monthEnd = fechaEnd.get(Calendar.MONTH);
        int dayIni = fechaIni.get(Calendar.DAY_OF_MONTH);
        int dayEnd = fechaEnd.get(Calendar.DAY_OF_MONTH);
        int mesFinal = 11;
        int diaFinal;

        for (int year = yearIni;year <= yearEnd; year++) {

            if (year == yearEnd){mesFinal = monthEnd;}

            for (int month = monthIni; month <=mesFinal; month++) {

                diaFinal = getNumberOfDaysMonthYear(yearIni,monthIni);

                if (month == monthEnd){diaFinal = dayEnd;}

                for (int day = dayIni; day <= diaFinal; day++) {

                    Calendar calendar = new GregorianCalendar(year,month,day);

                    days.add(new Day(calendar));

                }

                dayIni = 1;

            }

            monthEnd = 0;
        }

        return days;
    }

    public static ListaDays listaDias(int yearIni, int monthIni, int dayIni, int yearEnd, int monthEnd, int dayEnd) {

        ListaDays days = new ListaDays();

        int mesFinal = 11;
        int diaFinal;

        for (int year = yearIni;year <= yearEnd; year++) {

            if (year == yearEnd){mesFinal = monthEnd;}

            for (int month = monthIni; month <= mesFinal; month++) {

                diaFinal = getNumberOfDaysMonthYear(yearIni,monthIni);

                if (month == monthEnd){diaFinal = dayEnd;}

                for (int day = dayIni; day <= diaFinal; day++) {

                    Calendar calendar = new GregorianCalendar(year,month,day);

                    days.add(new Day(calendar));

                }

                dayIni = 1;

            }

            monthEnd = 0;
        }

        return days;
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    /**
     * retorna el año actual
     *
     * @return
     */
    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * retorna el dia del mes actual
     *
     * @return
     */
    public static int getCurrentDayMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * calcula el numero de dias que tiene un mes de una año especifico
     *
     * @param year
     * @param month
     * @return
     */
    public static int getNumberOfDaysMonthYear(int year, int month) {
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
    public static String getNameDay(int day, int month, int year) {
        Date date1 = (new GregorianCalendar(year, month, day)).getTime();
        // Then get the day of week from the Date based on specific locale.
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date1);
    }

    public static Calendar getDateCal(long date){

        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        return c;
    }

    public static Calendar getDateCal(Date date){

        Calendar c = new GregorianCalendar();
        c.setTime(date);
        return c;
    }

    public static Date getDateDate(int dia, int mes, int anio) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.DAY_OF_MONTH, dia);
        c.set(Calendar.MONTH, mes);
        c.set(Calendar.YEAR, anio);
        return c.getTime();
    }

    public static Date getDateDate(Calendar c) {

        return c.getTime();
    }

    public static Date getDateDate(long date) {
        return new Date(date);
    }

    public static long getDateLong(Calendar c){

        return c.getTimeInMillis();
    }

    public static long getDateLong(Date date){

        return date.getTime();
    }

    public static String getDateString(long date){

        Date fecha = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy ", Locale.getDefault());

        return sdf.format(fecha);
    }

    public static String getDateTimeString(long datetime) {
        Date date = new Date(datetime);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy HH:mm", Locale.getDefault());

        return sdf.format(date);
    }

    public static String getTimeString(long time) {
        Date date = new Date(time-3600000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm ", Locale.getDefault());

        return sdf.format(date);
    }

    public static String getDateString(Date date){

        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy ", Locale.getDefault());

        return sdf.format(date);
    }

    public static String getDateTimeString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy HH:mm", Locale.getDefault());

        return sdf.format(date);
    }

    public static String getTimeString(Date date) {

        long time = date.getTime();
        Date date2 = new Date(time-3600000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm ", Locale.getDefault());

        return sdf.format(date2);
    }

    public static String getDateString(Calendar calendar){

        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy ", Locale.getDefault());

        return sdf.format(date);
    }

    public static String getDateTimeString(Calendar calendar) {
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy HH:mm", Locale.getDefault());

        return sdf.format(date);
    }

    public static String getTimeString(Calendar calendar) {

        long time = calendar.getTimeInMillis();
        Date date = new Date(time-3600000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm ", Locale.getDefault());

        return sdf.format(date);
    }

    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(new Date());
    }

    /**
     * Pattern "yyyyMMddHHmmss"
     * @param date
     * @return
     */
    public static Date getDateDesdeString(String date){

        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date2 = null;
        try {
            date2 = (Date) formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date2;
    }

    /**
     * Pattern "yyyyMMddHHmmss"
     * @param date
     * @return
     */
    public static long getDateLongDesdeString(String date){

        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date2 = null;
        try {
            date2 = (Date) formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date2.getTime();
    }

    public static Calendar getCalendarDesdeString(String date){

        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date2 = null;
        try {
            date2 = (Date) formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = new GregorianCalendar();
        c.setTime(date2);

        return c;

    }

    public static long soloHora(long date){

        Calendar c = new GregorianCalendar(Locale.getDefault());
        c.setTimeInMillis(date);
        long horas = c.get(Calendar.HOUR_OF_DAY);
        long minutos = c.get(Calendar.MINUTE);
        return (horas*HORASLONG)+(minutos*MINUTOSLONG);
    }

    public static long soloFecha(long date){

        Calendar c = new GregorianCalendar(Locale.getDefault());
        c.setTimeInMillis(date);
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return (c.getTimeInMillis());
    }

}
