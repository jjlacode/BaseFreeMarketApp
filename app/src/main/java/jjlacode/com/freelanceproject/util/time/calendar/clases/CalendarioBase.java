package jjlacode.com.freelanceproject.util.time.calendar.clases;

import android.view.View;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.RVAdapter;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.android.FragmentBase;
import jjlacode.com.freelanceproject.util.crud.ListaModelo;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.time.calendar.views.OneCalendarView;

import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.DIASLONG;

public abstract class CalendarioBase extends FragmentBase implements ContratoPry.Tablas {


    private OneCalendarView calendarView;
    private RecyclerView rv;
    private ListaModelo lista;
    protected long fecha;
    protected String[] campos;
    protected String campo;
    protected int layoutItem;
    private ListaModelo listaModelo;
    private ListaModelo listatemp;
    private RVAdapter adaptadorRV;
    private long fechaHoy;
    protected int titulo;
    private ListaModelo listaMulti;
    private long seleccionInicial;
    private long seleccionFinal;
    private ImageButton nuevo;
    private ImageButton verLista;

    @Override
    protected void setLayout() {

        layout = R.layout.calendario;

    }

    @Override
    protected void setInicio() {

        calendarView = view.findViewById(R.id.oneCalendar);
        rv = view.findViewById(R.id.rvcalendario);
        nuevo = view.findViewById(R.id.btn_nuevo_item_calendario);
        verLista = view.findViewById(R.id.btn_ver_lista_calendario);
        setLayoutItem();
        setCampos();
        setTitulo();
        activityBase.toolbar.setTitle(titulo);
        lista = new ListaModelo(campos);
        fechaHoy = JavaUtil.hoyFecha();
        activityBase.toolbar.setSubtitle(JavaUtil.getDate(fechaHoy));
        listaMulti = new ListaModelo(campos);
        listaModelo = new ListaModelo(campos);
        listaModelo.clear();
        listatemp = new ListaModelo(campos);

        for (Modelo modelotemp : listatemp.getLista()) {

            if (setIfLista(modelotemp)){

                listaModelo.add(modelotemp);
            }
        }
            lista.clear();

            if (listaModelo.chech()) {
                for (Modelo modelo : listaModelo.getLista()) {

                    if (setIfListaHoy(modelo, fechaHoy)) {

                        lista.add(modelo);

                    }

                }
            }

        adaptadorRV = new RVAdapter(setViewHolder(view),
                lista.getLista(),layoutItem, null);
        rv.setAdapter(adaptadorRV);

        System.out.println("listaModelo = " + listaModelo.size());
        calendarView.setLista(listaModelo);
        calendarView.setCampo(campo);

        calendarView.setOneCalendarClickListener(new OneCalendarView.OneCalendarClickListener() {

            ArrayList<Day> dias = calendarView.getDays();
            @Override
            public void dateOnClick(Day day, int position) {

                listaMulti = new ListaModelo(campos);
                listaMulti.clear();
                fecha = day.getDate().getTimeInMillis();

                adaptadorRV = new RVAdapter(setViewHolder(view),
                        listaMulti.getLista(),layoutItem, null);
                rv.setAdapter(adaptadorRV);
                int seleccionados = 0;

                if (!day.isSelected()){
                    calendarView.addDaySelected(position);
                }else {
                    calendarView.removeDaySeleted(position);
                }

                for (Day dia : dias) {

                    if (dia.isSelected()){
                        if (dia.getLista() != null) {
                            listaMulti.addAll(dia.getLista());
                        }
                        seleccionados++;
                    }
                }

                if (seleccionados==1) {

                    activityBase.toolbar.setSubtitle(JavaUtil.getDate(fecha));
                    visible(nuevo);
                }else{
                    activityBase.toolbar.setSubtitle("Seleccion multiple");
                    gone(nuevo);
                }

                adaptadorRV = new RVAdapter(setViewHolder(view),
                        listaMulti.getLista(),layoutItem, null);
                rv.setAdapter(adaptadorRV);

                setOnDayClick(day,position);

            }

            @Override
            public void dateOnLongClick(Day day, int position) {

                listaMulti = new ListaModelo(campos);
                listaMulti.clear();
                fecha = day.getDate().getTimeInMillis();

                seleccionInicial = 0;
                seleccionFinal = day.getDate().getTimeInMillis();
                int seleccionados = 0;

                if (!day.isSelected()) {
                    //day.setSelected(true);
                    for (Day dia : dias) {

                        if (dia.isSelected() && dia.getDate().getTimeInMillis() > seleccionInicial &&
                                dia.getDate().getTimeInMillis() < seleccionFinal) {
                            seleccionInicial = dia.getDate().getTimeInMillis();
                        }
                        if (dia.getDate().getTimeInMillis() == seleccionFinal) {
                            break;
                        }
                    }
                    if (seleccionInicial==0){
                        for (Day dia : dias) {

                                dia.setSelected(false);

                        }
                    }else {
                        for (Day dia : dias) {
                            if (dia.getDate().getTimeInMillis() >= seleccionInicial) {
                                dia.setSelected(true);
                            }
                            if (dia.getDate().getTimeInMillis() >= seleccionFinal - DIASLONG) {
                                break;
                            }
                        }
                        for (Day dia : dias) {

                            if (dia.isSelected()) {
                                if (dia.getLista() != null) {
                                    listaMulti.addAll(dia.getLista());
                                }
                                seleccionados++;
                            }
                        }
                    }
                }else {
                    for (Day dia : dias) {

                        if (dia.getDate().getTimeInMillis()>=seleccionFinal){
                            dia.setSelected(false);
                        }

                    }
                }

                if (seleccionados==1) {

                    activityBase.toolbar.setSubtitle(JavaUtil.getDate(fecha));
                    visible(nuevo);
                }else{
                    activityBase.toolbar.setSubtitle("Seleccion multiple");
                    gone(nuevo);
                }

                adaptadorRV = new RVAdapter(setViewHolder(view),
                        listaMulti.getLista(),layoutItem, null);
                rv.setAdapter(adaptadorRV);

                setOnDayLongClick(day,position);

            }
        });

        calendarView.setOnCalendarChangeListener(new OneCalendarView.OnCalendarChangeListener() {
            @Override
            public void prevMonth() {

                calendarView.setLista(listaModelo);
                calendarView.setCampo(campo);
                lista = new ListaModelo(campos);
                fechaHoy = JavaUtil.hoyFecha();
                activityBase.toolbar.setSubtitle(JavaUtil.getDate(fechaHoy));

                lista.clear();

                if (listaModelo.chech()) {
                    for (Modelo modelo : listaModelo.getLista()) {

                        if (setIfListaHoy(modelo, fechaHoy)) {

                            lista.add(modelo);

                        }

                    }
                }
                adaptadorRV = new RVAdapter(setViewHolder(view),
                        lista.getLista(),layoutItem, null);
                rv.setAdapter(adaptadorRV);

                setOnPrevMonth();
            }

            @Override
            public void nextMonth() {

                calendarView.setLista(listaModelo);
                calendarView.setCampo(campo);
                lista = new ListaModelo(campos);
                fechaHoy = JavaUtil.hoyFecha();
                activityBase.toolbar.setSubtitle(JavaUtil.getDate(fechaHoy));

                lista.clear();

                if (listaModelo.chech()) {
                    for (Modelo modelo : listaModelo.getLista()) {

                        if (setIfListaHoy(modelo, fechaHoy)) {

                            lista.add(modelo);

                        }

                    }
                }
                adaptadorRV = new RVAdapter(setViewHolder(view),
                        lista.getLista(),layoutItem, null);
                rv.setAdapter(adaptadorRV);

                setOnNextMonth();
            }
        });

        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setNuevo(fecha);

            }
        });

        verLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setVerLista();
            }
        });

    }

    protected long getFecha(){

        return fecha;
    }

    protected abstract void setLayoutItem();

    protected abstract boolean setIfLista(Modelo modelo);

    protected abstract boolean setIfListaHoy(Modelo modelo, long hoy);

    protected abstract void setCampos();

    protected abstract void setTitulo();

    protected abstract void setOnDayClick(Day day, int position);

    protected abstract void setOnDayLongClick(Day day, int position);

    protected abstract void setNuevo(long fecha);

    protected abstract void setVerLista();

    protected abstract void setOnPrevMonth();

    protected abstract void setOnNextMonth();

    protected abstract TipoViewHolder setViewHolder(View view);


}
