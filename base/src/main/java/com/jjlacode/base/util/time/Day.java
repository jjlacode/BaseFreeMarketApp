package com.jjlacode.base.util.time;

import android.graphics.Color;

import com.jjlacode.base.util.crud.ListaModelo;
import com.jjlacode.base.util.crud.Modelo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by DARWIN on 1/3/2017.
 */

public class Day {

    private Calendar date;
    private boolean valid, selected, inicio, fin, multi, busca;
    private int textColor = Color.parseColor("#0099cc");
    private int textColorNV = Color.parseColor("#d2d2d2");
    private int backgroundColor = Color.parseColor("#FFF5F5F5");
    private int backgroundColorNV = Color.parseColor("#FFF5F5F5");
    private ArrayList<Modelo> lista;
    private ListaModelo listaModelo;
    private int posicionListaSimple;
    private int posicionListaMulti;
    private int posicionListaFinal;
    private int posicionCal;
    private int month;
    private int day;
    private int year;
    private long fechaLong;
    private Date fechaDate;
    private String fechaString;
    private String nombre;
    private String estado;

    public Day(Calendar date) {
        this.date = date;
        valid = true;
        posicionListaSimple = -1;
        posicionListaMulti = -1;
        posicionListaFinal = -1;
        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);
        fechaLong = date.getTimeInMillis();
        fechaDate = date.getTime();
        fechaString = TimeDateUtil.getDateString(date);

    }

    public Day(long date) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        this.date = c;
        valid = true;
        posicionListaSimple = -1;
        posicionListaMulti = -1;
        posicionListaFinal = -1;
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        fechaLong = date;
        fechaDate = c.getTime();
        fechaString = TimeDateUtil.getDateString(fechaDate);

    }

    public Day(Calendar date, int posicionCal) {
        this.date = date;
        valid = true;
        this.posicionCal = posicionCal;
        posicionListaSimple = -1;
        posicionListaMulti = -1;
        posicionListaFinal = -1;
        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);
        fechaLong = date.getTimeInMillis();
        fechaDate = date.getTime();
        fechaString = TimeDateUtil.getDateString(fechaDate);

    }

    public Day(Calendar date, boolean inicio, boolean fin, boolean multi) {
        this.date = date;
        valid = true;
        posicionListaSimple = -1;
        posicionListaMulti = -1;
        posicionListaFinal = -1;
        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);
        fechaLong = date.getTimeInMillis();
        fechaDate = date.getTime();
        fechaString = TimeDateUtil.getDateString(fechaDate);
        this.inicio = inicio;
        this.fin = fin;
        this.multi = multi;

    }

    public Day(Calendar date, int textColor, int backgroundColor, int posicionCal) {
        this.date = date;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        valid = true;
        this.posicionCal = posicionCal;
        posicionListaSimple = -1;
        posicionListaMulti = -1;
        posicionListaFinal = -1;
        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);
        fechaLong = date.getTimeInMillis();
        fechaDate = date.getTime();
        fechaString = TimeDateUtil.getDateString(fechaDate);

    }

    public Day(Calendar date, int textColor, int backgroundColor, ArrayList<Modelo> lista, int posicionCal) {
        this.date = date;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        valid = true;
        this.lista = lista;
        this.posicionCal = posicionCal;
        posicionListaSimple = -1;
        posicionListaMulti = -1;
        posicionListaFinal = -1;
        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);
        fechaLong = date.getTimeInMillis();
        fechaDate = date.getTime();
        fechaString = TimeDateUtil.getDateString(fechaDate);

    }

    public Day(Calendar date, boolean valid, int textColorNV, int backgroundColorNV, int posicionCal) {
        this.date = date;
        this.valid = valid;
        this.textColorNV = textColorNV;
        this.backgroundColorNV = backgroundColorNV;
        this.posicionCal = posicionCal;
        posicionListaSimple = -1;
        posicionListaMulti = -1;
        posicionListaFinal = -1;
        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);
        fechaLong = date.getTimeInMillis();
        fechaDate = date.getTime();
        fechaString = TimeDateUtil.getDateString(fechaDate);

    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public String getFechaString() {
        return fechaString;
    }

    public long getFechaLong() {
        return fechaLong;
    }

    public long getSoloFechaLong() {
        return TimeDateUtil.soloFecha(fechaLong);
    }

    public Date getFechaDate() {
        return fechaDate;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public boolean isInicio() {
        return inicio;
    }

    public void setInicio(boolean inicio) {
        this.inicio = inicio;
    }

    public boolean isFin() {
        return fin;
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public boolean isBusca() {
        return busca;
    }

    public void setBusca(boolean busca) {
        this.busca = busca;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextColorNV() {
        return textColorNV;
    }

    public void setTextColorNV(int textColorNV) {
        this.textColorNV = textColorNV;
    }

    public int getBackgroundColorNV() {
        return backgroundColorNV;
    }

    public void setBackgroundColorNV(int backgroundColorNV) {
        this.backgroundColorNV = backgroundColorNV;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setLista(ArrayList<Modelo> lista) {
        this.lista = lista;
    }

    public ArrayList<Modelo> getLista() {
        return lista;
    }

    public int getPosicionCal() {
        return posicionCal;
    }

    public void setPosicionCal(int posicionCal) {
        this.posicionCal = posicionCal;
    }

    public int getPosicionListaSimple() {
        return posicionListaSimple;
    }

    public void setPosicionListaSimple(int posicionListaSimple) {
        this.posicionListaSimple = posicionListaSimple;
    }

    public int getPosicionListaMulti() {
        return posicionListaMulti;
    }

    public void setPosicionListaMulti(int posicionListaMulti) {
        this.posicionListaMulti = posicionListaMulti;
    }

    public int getPosicionListaFinal() {
        return posicionListaFinal;
    }

    public void setPosicionListaFinal(int posicionListaFinal) {
        this.posicionListaFinal = posicionListaFinal;
    }

    public String getEstado() {

        estado = "Inicio = " + inicio + " Fin = " + fin + " Multi = " + multi;

        return estado;
    }

    public ListaModelo getListaModelo() {
        return listaModelo;
    }

    public void setListaModelo(ListaModelo listaModelo) {
        this.listaModelo = listaModelo;
    }
}
