package jjlacode.com.freelanceproject.util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


public class JavaUtil {

    public interface Constantes {

        String NAMEF = "namef";
        String NAMESUB = "namesub";
        String NAMEFTEMP = "nameftemp";
        String NAMESUBTEMP = "namesubtemp";
        String LISTA = "lista";
        String TIPO = "tipo";
        String ID = "id";
        String IDREL = "idrel";
        String SECUENCIA = "secuencia";
        String MODELO = "modelo";
        String NUEVOREGISTRO = "nuevoreg";


        long SEGUNDOSLONG = (1000);
        long MINUTOSLONG = (60 * SEGUNDOSLONG);
        long HORASLONG = (60 * MINUTOSLONG);
        long DIASLONG = (24 * HORASLONG);
        long SEMANASLONG = (7 * DIASLONG);
        long MESESLONG = (30 * DIASLONG);
        long ANIOSLONG = (12 * MESESLONG);

        String STRING = "String";
        String INT = "int";
        String DOUBLE = "double";
        String LONG = "long";
        String FLOAT = "float";
        String SHORT = "short";
        String NULL = "null";
        String NONNULL = "nonnull";

        int IGUAL = 0;
        int DIFERENTE = 1;
        int ENTRE = 2;
        int MAYOR = 3;
        int MENOR = 4;
        int MAYORIGUAL = 5;
        int MENORIGUAL = 6;
        int ASCENDENTE = 7;
        int DESCENDENTE = 8;

        int AUDIORECORD = 1;
        int VIDEORECORD = 2;


    }

    public static String getDecimales(double value){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);
    }

    public static int comprobarInteger(String dato){

        if (null==dato){return 0;}

        try {
            dato = sinFormato(dato);
            int res = Integer.parseInt(dato)/Integer.parseInt(dato);
            if (res==1)return Integer.parseInt(dato);

        }catch (Exception e){
        }
        return  0;
    }

    public static long comprobarLong(String dato){

        if (null==dato){return 0;}

        try {
            dato = sinFormato(dato);
            long res = Long.parseLong(dato)/Long.parseLong(dato);
            if (res==1)return Long.parseLong(dato);

        }catch (Exception e){
        }
        return  0;
    }

    public static double comprobarDouble(String dato){

        if (null==dato){return 0;}

        try {
            dato = sinFormato(dato);
            double res = Double.parseDouble(dato)/Double.parseDouble(dato);
            if (res==1)return Double.parseDouble(dato);

        }catch (Exception e){
        }
        return  0;
    }

    public static Float comprobarFloat(String dato){

        if (null==dato){return 0f;}


        try {
            dato = sinFormato(dato);
            float res = Float.parseFloat(dato)/Float.parseFloat(dato);
            if (res==1)return Float.parseFloat(dato);

        }catch (Exception e){
        }
        return  0f;
    }

    public static short comprobarShort(String dato){

        if (null==dato){return 0;}

        try {
            dato = sinFormato(dato);
            short res = (short) (Short.parseShort(dato)/Short.parseShort(dato));
            if (res==1)return Short.parseShort(dato);

        }catch (Exception e){
        }
        return  0;
    }

    public static String sinFormato(String dato){
        char espacio = ' ';
        for (int i=0;i<dato.length();i++){
            char caracter = dato.charAt(i);
            int ascii = dato.codePointAt(i);
            //System.out.println("ascii = " + ascii);
            if ((ascii>57 || ascii<48) && ascii!=44 && ascii!=46){
                dato = dato.replace(caracter,espacio);
            }
            if (ascii==44){
                dato = dato.replace(caracter,'.');
            }
        }
        return dato.trim();
    }

    public static String noNuloString(String dato){

        if (dato!=null){return dato;}

        return "";
    }

    public static String getDecimales(double value, String patron){
        DecimalFormat df = new DecimalFormat(patron);
        return df.format(value);
    }

    public static long fechaALong(int anio, int mes, int dia){

        //Date date = new Date(anio,mes,dia);
        Calendar calendar = new GregorianCalendar(anio,mes,dia);

        return calendar.getTimeInMillis();//date.getTime();
    }

    public static long horaALong(int hora, int minuto){

        Calendar calendar = new GregorianCalendar(0,0,0,hora,minuto);

        return calendar.getTimeInMillis();//date.getTime();
    }

    public static String longAFecha(long fecha){

        Date date = new Date(fecha);
        return date.toString();
    }

    public static String diaSemanaStr(int dia, int mes, int anio) throws ParseException
    {
        String inputDateStr = String.format("%s/%s/%s", dia, mes, anio);
        Date inputDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(inputDateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()).toUpperCase();
    }

    public static int diaSemana(int dia, int mes, int anio){

        Calendar c = Calendar.getInstance();
        c.set(anio,mes-1,dia); // vairables int
        int nd = c.get(Calendar.DAY_OF_WEEK)-1;
        return nd;

    }

    public static int diaSemanahoy(){

        Calendar c = Calendar.getInstance();
        int nd = c.get(Calendar.DAY_OF_WEEK)-1;
        return nd;

    }

    public static long hoy(){

        TimeZone timezone = TimeZone.getDefault();
        Calendar calendar = new GregorianCalendar(timezone);
        return calendar.getTimeInMillis();
    }

    public static int semanasAnio(){

        TimeZone timezone = TimeZone.getDefault();
        Calendar calendar = new GregorianCalendar(timezone);
        return 52;
    }

    public static String getDate(long date) {
        Date fecha = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy ", Locale.getDefault());

        return sdf.format(fecha);
    }

    public static String getDateTime(long datetime) {
        Date date = new Date(datetime);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy HH:mm", Locale.getDefault());

        return sdf.format(date);
    }

    public static String getTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm ", Locale.getDefault());

        return sdf.format(date);
    }

    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(new Date());
    }

    public static Date getDateDesdeString(String date){//Pattern "yyyyMMddHHmmss"

        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date2 = null;
        try {
            date2 = (Date) formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date2;
    }

    public static Date getCurrentDate() {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return instance.getTime();
    }

    public static Date createDate(int dia, int mes, int anio) {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DAY_OF_MONTH, dia);
        instance.set(Calendar.MONTH, mes);
        instance.set(Calendar.YEAR, anio);
        return instance.getTime();
    }

    public static String formatDateForUi(int year, int month, int dayOfMonth) {
        return formatDateForUi(createDate(year, month, dayOfMonth));
    }

    public static String formatDateForUi(Date date) {
        SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat("EEE dd MMM yyy", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static String twoDigits(int n) {

        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    public static String formatoMonedaLocal(double importe){

        //obtenemos el simbolo de moneda local
        Currency curr = Currency.getInstance(Locale.getDefault());
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        nf.setCurrency(curr);

        return nf.format(importe);
    }

    public static String monedaLocal(){

        //obtenemos el simbolo de moneda local
        Currency curr = Currency.getInstance(Locale.getDefault());
        return curr.getSymbol();
        //NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        //nf.setCurrency(curr);

        //return nf.format(importe);
    }

    public static boolean isValidURL(String url) {

        URL u = null;

        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }
        try {
            u.toURI();
        } catch (URISyntaxException e) {
            return false;
        }

        return true;
    }

    public static long[] longA_ddhhmm(long flong){

        long lfechaaviso = flong;
        long ldias = lfechaaviso / Constantes.DIASLONG;
        long lhoras = (lfechaaviso - (ldias* Constantes.DIASLONG)) / Constantes.HORASLONG;
        long lminutos = (lfechaaviso - (lhoras* Constantes.HORASLONG) -
                (ldias* Constantes.DIASLONG)) / Constantes.MINUTOSLONG;
        long[] res = {ldias,lhoras,lminutos};

        return res;
    }

    public static long[] longA_AAMMdd(long flong){

        long lfecha = flong;
        long lanios = lfecha / Constantes.ANIOSLONG;
        long lmeses = (lfecha - (lanios* Constantes.ANIOSLONG)) / Constantes.MESESLONG;
        long ldias = (lfecha - (lmeses* Constantes.MESESLONG) -
                (lanios* Constantes.ANIOSLONG)) / Constantes.DIASLONG;
        long[] res = {lanios,lmeses,ldias};

        return res;
    }


}
