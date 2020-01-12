package com.codevsolution.base.time.calendar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.animation.OneFrameLayout;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.Day;
import com.codevsolution.base.time.ListaDays;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.codevsolution.freemarketsapp.sqlite.ContratoPry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by DARWIN on 3/3/2017.
 */

public abstract class FragmentMes extends FragmentBase implements
        JavaUtil.Constantes, ContratoPry.Tablas, Interactor.ConstantesPry {


    private int month, year;
    //meses por defecto en español
    private String enero = "Enero";
    private String febrero = "Febrero";
    private String marzo = "Marzo";
    private String abril = "Abril";
    private String mayo = "Mayo";
    private String junio = "Junio";
    private String julio = "Julio";
    private String agosto = "Agosto";
    private String septiembre = "Septiembre";
    private String octubre = "Octubre";
    private String noviembre = "Noviembre";
    private String diciembre = "Deciembre";


    //para el estilo del calendar
    int mainBackgroundColor = Color.parseColor("#3949AB");//Color.parseColor("#0239a9");
    int calendarBackgroundColor = Color.parseColor("#FFF5F5F5");
    int currentDayBackgroundColor = Color.parseColor("#0099cc");
    int backgroundColorDaysOfMonth = Color.parseColor("#FFF5F5F5");
    int backgroundColorDaysOfAnotherMonth = Color.parseColor("#FFF5F5F5");
    int textColorDaysOfMonth = Color.parseColor("#0099cc");
    int textColorDaysOfAnotherMonth = Color.parseColor("#d2d2d2");
    int textColorMonthAndYear = Color.parseColor("#0099cc");
    int textColorSelectedDay = Color.parseColor("#D81B60");
    int textColorCurrentDayDay = Color.parseColor("#000000");
    int backgroundColorSelectedDay = Color.parseColor("#D81B60");

    private ListaModeloSQL lista;
    protected long fecha;
    protected String[] campos;
    protected String campo;
    protected String campoCard;
    protected String campoColor;
    protected String campoId;
    protected int layoutItem;
    protected ListaModeloSQL listabase;
    private RVAdapter adaptadorRV;
    private RVAdapter rvAdapter;
    private long fechaHoy;
    protected int titulo;
    private ListaModeloSQL listaModeloSQLMulti;
    private ListaModeloSQL listaModeloSQLSimple;
    private ListaModeloSQL listaModeloSQLFinal;
    private ArrayList<Long> seleccionInicial = new ArrayList<>();
    private ArrayList<Long> seleccionFinal = new ArrayList<>();
    private int seleccion = 0;
    protected ImageButton nuevo;
    protected ImageButton verLista;
    protected ImageButton verDia;
    protected ImageButton verSemana;
    private ImageView lupa;
    private ImageView renovar;
    private ListaDays listadias;
    private ListaDays listaSeleccionadosMulti;
    private int seleccionados;
    private ListaDays listaSeleccionadosSimple;
    private ListaDays listaSeleccionadosFinal;
    private ListaDays listaTotal;
    private int multi = 0;
    private int multiant = 0;
    private boolean sinDia;
    private boolean sinNuevo;
    private boolean sinLista;
    private boolean sinBusqueda;
    private boolean buscando;
    private AutoCompleteTextView buscar;
    private ListaAdaptadorFiltroModelo adaptadorFiltroRV;
    private GregorianCalendar cbusca;
    protected String tipoRV;

    private OneFrameLayout fragment_container;
    private LinearLayoutCompat main;
    private RecyclerView recyclerViewDays;
    protected RecyclerView rv;
    private ListaDays days = new ListaDays();
    private ImageButton buttonUp, buttonDown;
    private TextView textViewMY;
    private TextView textViewD, textViewL, textViewM, textViewX, textViewJ, textViewV, textViewS;
    private int currentDay;
    protected int layoutOpciones;
    private View viewOpciones;
    private LinearLayoutCompat frOpciones;
    private int lenguaje;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contexto = getActivity();

        month = getCurrentMonth();
        year = getCurrentYear();
        currentDay = getCurrentDayMonth();

        setOnCreate();

    }

    protected void setOnCreate() {

    }

    @Override
    protected void setLayout() {

        layout = R.layout.fragment_mes;
        setLayoutItem();

    }

    @Override
    protected void setInicio() {

        frOpciones = view.findViewById(R.id.frOpciones);

        if (layoutOpciones > 0) {
            viewOpciones = inflaterMain.inflate(layoutOpciones, containerMain, false);
            if (viewOpciones.getParent() != null) {
                ((ViewGroup) viewOpciones.getParent()).removeView(viewOpciones); // <- fix
            }
            if (viewOpciones != null) {
                frOpciones.addView(viewOpciones);
                visible(frOpciones);
            }

        }

        fragment_container = view.findViewById(R.id.fragment_animation);
        main = view.findViewById(R.id.main);
        recyclerViewDays = view.findViewById(R.id.recyclerView);
        recyclerViewDays.setLayoutManager(new GridLayoutManager(contexto, 7));
        rv = (RecyclerView) ctrl(R.id.rvcalendario);
        nuevo = (ImageButton) ctrl(R.id.btn_nuevo_item_calendario);
        verLista = (ImageButton) ctrl(R.id.btn_ver_lista_calendario);
        verDia = (ImageButton) ctrl(R.id.btn_ver_dia_calendario);
        buscar = (AutoCompleteTextView) ctrl(R.id.buscarcalendario);
        lupa = (ImageView) ctrl(R.id.imgsearchcal);
        renovar = (ImageView) ctrl(R.id.imgrenovarcal);
        buttonDown = (ImageButton) ctrl(R.id.imageButtonDown);
        buttonUp = (ImageButton) ctrl(R.id.imageButtonUp);
        textViewMY = (TextView) ctrl(R.id.textMY);

        textViewL = (TextView) ctrl(R.id.textD);
        textViewM = (TextView) ctrl(R.id.textL);
        textViewX = (TextView) ctrl(R.id.textM);
        textViewJ = (TextView) ctrl(R.id.textX);
        textViewV = (TextView) ctrl(R.id.textJ);
        textViewS = (TextView) ctrl(R.id.textV);
        textViewD = (TextView) ctrl(R.id.textS);

        String locales = AndroidUtil.getSystemLocale();

        if (locales.equals("en")) {

            lenguaje = 1;
            textViewD = (TextView) ctrl(R.id.textD);
            textViewL = (TextView) ctrl(R.id.textL);
            textViewM = (TextView) ctrl(R.id.textM);
            textViewX = (TextView) ctrl(R.id.textX);
            textViewJ = (TextView) ctrl(R.id.textJ);
            textViewV = (TextView) ctrl(R.id.textV);
            textViewS = (TextView) ctrl(R.id.textS);

        } else if (locales.equals("pt")) {
            lenguaje = 2;
        }

        setLanguage(lenguaje);

        textViewMY.setTextColor(textColorMonthAndYear);

        activityBase.fabNuevo.hide();
        activityBase.fabVoz.show();

        main.setBackgroundColor(mainBackgroundColor);
        fragment_container.setBackgroundColor(calendarBackgroundColor);


        fragment_container.setOnSwipeListener(new OneFrameLayout.OnSwipeListener() {

            @Override
            public void rightSwipe() {
                prevMonth();
                onCalendarChangeListener.prevMonth();
            }

            @Override
            public void leftSwipe() {
                nextMoth();
                onCalendarChangeListener.nextMonth();
            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevMonth();
                onCalendarChangeListener.prevMonth();
            }
        });

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextMoth();
                onCalendarChangeListener.nextMonth();
            }
        });

        setCampos();
        setTitulo();

        activityBase.toolbar.setTitle(titulo);
        fechaHoy = JavaUtil.hoyFecha();
        fecha = fechaHoy;
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(fechaHoy);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        activityBase.toolbar.setSubtitle(JavaUtil.getDate(fechaHoy));

        listaTotal = getlistaTotal();

        lista = setListaDia(fechaHoy);
        lista.addAllLista(setListaFija().getLista());

        if (campos == null) {
            campos = new String[]{"", "", CAMPO_CREATEREG};
        } else {
            listabase = new ListaModeloSQL(campos);
        }
        adaptadorFiltroRV = setAdaptadorAuto(getContext(), layoutItem, listabase.getLista(), campos);

        buscar.setAdapter(adaptadorFiltroRV);

        buscar.setThreshold(3);

        buscar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                buscar.setText("");
                ModeloSQL modeloSQL = adaptadorFiltroRV.getItem(i);
                if (campo == null) {
                    campo = CAMPO_CREATEREG;
                }
                long fechab = modeloSQL.getLong(campo);
                cbusca = new GregorianCalendar();
                cbusca.setTimeInMillis(fechab);
                month = cbusca.get(Calendar.MONTH);
                year = cbusca.get(Calendar.YEAR);
                buscando = true;

                fillUpMonth();

            }
        });

        if (tipoRV != null && tipoRV.equals(LISTA)) {

            adaptadorRV = new RVAdapter(setViewHolder(view),
                    lista, layoutItem);

        } else {
            adaptadorRV = new RVAdapter(setViewHolder(view),
                    lista.getLista(), layoutItem);
        }

        rv.setAdapter(adaptadorRV);
        setCampo(campo);
        listaModeloSQLSimple = new ListaModeloSQL();
        listaSeleccionadosSimple = new ListaDays();
        listaModeloSQLFinal = new ListaModeloSQL();
        listaSeleccionadosFinal = new ListaDays();
        seleccionInicial.add(seleccion, 0L);
        seleccionFinal.add(seleccion, 0L);


        setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void dayOnClick(Day day, int position) {


                regenerarPosicionesLista();
                asignarMulti();
                fecha = day.getFechaLong();


                for (Day dia : listadias) {
                    if (fecha > dia.getFechaLong()) {
                        continue;
                    }

                    if (!dia.isSelected()) {


                        if (!dia.isMulti()) {

                            System.out.println(dia.getEstado());

                            listaSeleccionadosSimple.add(dia);
                            day.setPosicionListaSimple(listaSeleccionadosSimple.indexOf(dia));


                            if (dia.getLista() != null) {
                                for (ModeloSQL modeloSQL : dia.getLista()) {
                                    boolean nuevo = true;
                                    System.out.println("campoId = " + campoId);
                                    if (campoId != null) {
                                        for (ModeloSQL modeloSQLTmp : listaModeloSQLSimple.getLista()) {
                                            System.out.println("modeloSQL.getString(campoId) = " + modeloSQL.getString(campoId));
                                            System.out.println("modeloSQLTmp.getString(campoId) = " + modeloSQLTmp.getString(campoId));
                                            if (modeloSQL.getString(campoId).equals(modeloSQLTmp.getString(campoId))) {
                                                nuevo = false;
                                            }
                                        }
                                    }
                                    System.out.println("nuevo = " + nuevo);
                                    if (nuevo) {
                                        listaModeloSQLSimple.addModelo(modeloSQL);
                                    }
                                }
                            }
                        }

                    } else {


                        if (!dia.isMulti()) {

                            if (dia.getPosicionListaSimple() >= 0) {

                                if (dia.getLista() != null) {
                                    for (ModeloSQL modeloSQL : dia.getLista()) {
                                        listaModeloSQLSimple.removeModelo(modeloSQL);
                                    }
                                }

                                listaSeleccionadosSimple.remove(dia.getPosicionListaSimple());

                            }

                            removeItemSelected(position);

                        }
                    }
                    break;
                }

                regenerarPosicionesLista();


                for (Day dia : listadias) {

                    if (dia.isSelected() && dia.isMulti()) {
                        if (dia.getLista() != null && dia.getLista().size() > 0) {
                            for (ModeloSQL modeloSQL : dia.getLista()) {
                                listaModeloSQLSimple.removeModelo(modeloSQL);
                            }
                        }
                        if (listaSeleccionadosSimple.size() > dia.getPosicionListaSimple() &&
                                dia.getPosicionListaSimple() >= 0) {
                            listaSeleccionadosSimple.remove(dia.getPosicionListaSimple());

                            regenerarPosicionesLista();

                        }
                    }

                }

                listaModeloSQLFinal = new ListaModeloSQL();
                listaModeloSQLFinal.addAllLista(listaModeloSQLSimple.getLista());

                if (listaModeloSQLMulti != null) {
                    listaModeloSQLFinal.addAllLista(listaModeloSQLMulti.getLista());
                }

                ListaModeloSQL listaFija = setListaFija();
                for (ModeloSQL modeloSQL : listaFija.getLista()) {
                    boolean nuevo = true;
                    System.out.println("campoId = " + campoId);
                    if (campoId != null) {
                        for (ModeloSQL modeloSQLTmp : listaModeloSQLFinal.getLista()) {
                            System.out.println("modeloSQL.getString(campoId) = " + modeloSQL.getString(campoId));
                            System.out.println("modeloSQLTmp.getString(campoId) = " + modeloSQLTmp.getString(campoId));
                            if (modeloSQL.getString(campoId).equals(modeloSQLTmp.getString(campoId))) {
                                nuevo = false;
                            }
                        }
                    }
                    System.out.println("nuevo = " + nuevo);
                    if (nuevo) {
                        listaModeloSQLFinal.addModelo(modeloSQL);
                    }
                }

                listaSeleccionadosFinal = new ListaDays();
                listaSeleccionadosFinal.addAll(listaSeleccionadosSimple);

                if (listaSeleccionadosMulti != null && listaSeleccionadosMulti.size() > 0) {
                    listaSeleccionadosFinal.addAll(listaSeleccionadosMulti);
                }

                if (listaSeleccionadosFinal.size() > 0) {
                    for (Day dia : listaSeleccionadosFinal) {

                        for (Day dialista : listadias) {


                            if (dia.getFechaLong() == dialista.getFechaLong()) {
                                addItemSelected(dialista.getPosicionCal());
                            }
                        }

                    }
                }

                seleccionados = listaSeleccionadosFinal.size();

                if (seleccionados == 1) {

                    activityBase.toolbar.setSubtitle(JavaUtil.getDate(
                            listaSeleccionadosFinal.get(0).getFechaLong()));
                    if (!sinNuevo) {
                        visible(nuevo);
                    }
                    if (!sinDia) {
                        visible(verDia);
                    }
                } else if (seleccionados > 1) {
                    activityBase.toolbar.setSubtitle("Seleccion multiple");
                    gone(nuevo);
                    gone(verDia);
                } else {
                    activityBase.toolbar.setSubtitle("Haga su selección");
                    gone(nuevo);
                    gone(verDia);

                }

                if (tipoRV != null && tipoRV.equals(LISTA)) {

                    adaptadorRV = new RVAdapter(setViewHolder(view),
                            listaModeloSQLFinal, layoutItem);

                } else {

                    adaptadorRV = new RVAdapter(setViewHolder(view),
                            listaModeloSQLFinal.getLista(), layoutItem);
                }
                rv.setAdapter(adaptadorRV);

                if (seleccionFinal.get(seleccion) > 0 && seleccionInicial.get(seleccion) > 0) {
                    seleccion++;
                    seleccionInicial.add(seleccion, 0L);
                    seleccionFinal.add(seleccion, 0L);

                }

                setOnDayClick(day, position);

                setDays(listadias);
            }

            @Override
            public void dayOnLongClik(Day day, int position) {

                fecha = day.getDate().getTimeInMillis();
                int selfin = 0;
                multiant = asignarMulti();

                for (int sel = seleccion; sel >= 0; sel--) {

                    selfin = sel;

                    if (seleccionInicial.get(sel) < day.getFechaLong() &&
                            seleccionFinal.get(sel) == 0 &&
                            seleccionInicial.get(sel) > 0 && !day.isSelected()) {
                        seleccionFinal.set(sel, day.getFechaLong());
                        break;
                    } else if (!day.isSelected() &&
                            seleccionInicial.get(sel) == 0) {
                        seleccionInicial.set(sel, day.getFechaLong());
                        break;
                    } else if (seleccionFinal.get(sel) == day.getFechaLong() ||
                            seleccionInicial.get(sel) == day.getFechaLong()) {

                        seleccionInicial.set(sel, 0L);
                        seleccionFinal.set(sel, 0L);
                        break;
                    }
                }

                listaModeloSQLMulti = new ListaModeloSQL();
                listaSeleccionadosMulti = new ListaDays();

                for (int sel = seleccion; sel >= 0; sel--) {


                    if (seleccionInicial.get(sel) > 0 && seleccionFinal.get(sel) > 0) {

                        Calendar cini = new GregorianCalendar();
                        cini.setTimeInMillis(seleccionInicial.get(sel));
                        Calendar cfin = new GregorianCalendar();
                        cfin.setTimeInMillis(seleccionFinal.get(sel));
                        ListaDays listadias = TimeDateUtil.listaDias(cini, cfin);

                        for (Day dia : listadias) {
                            ListaModeloSQL lista = setListaDia(dia.getFechaLong());
                            if (lista.getLista() != null) {
                                for (ModeloSQL modeloSQL : lista.getLista()) {
                                    boolean nuevo = true;
                                    if (campoId != null) {
                                        for (ModeloSQL modeloSQLTmp : listaModeloSQLMulti.getLista()) {
                                            if (modeloSQL.getString(campoId) == (modeloSQLTmp.getString(campoId))) {
                                                nuevo = false;
                                            }
                                        }
                                    }
                                    if (nuevo) {
                                        listaModeloSQLMulti.addModelo(modeloSQL);
                                    }
                                }
                            }
                        }

                    }
                }

                for (Day dia : listaTotal) {

                    dia.setInicio(false);
                    dia.setFin(false);
                    dia.setMulti(false);
                    boolean fin = false;

                    for (int sel = seleccion; sel >= 0; sel--) {


                        if (seleccionInicial.get(sel) > 0 && seleccionFinal.get(sel) > 0) {

                            if (dia.getFechaLong() < seleccionInicial.get(sel)) {
                                continue;
                            }
                            if (dia.getFechaLong() > seleccionFinal.get(sel)) {
                                break;
                            }


                            if (dia.getFechaLong() == seleccionInicial.get(sel)) {
                                dia.setInicio(true);
                            } else if (dia.getFechaLong() == seleccionFinal.get(sel)) {
                                dia.setFin(true);
                            } else if (dia.getFechaLong() > seleccionInicial.get(sel) &&
                                    dia.getFechaLong() < seleccionFinal.get(sel)) {
                                dia.setMulti(true);
                            }
                            listaSeleccionadosMulti.add(dia);

                        } else if (seleccionInicial.get(sel) > 0 && seleccionFinal.get(sel) == 0) {

                            if (dia.getFechaString().equals(TimeDateUtil.getDateString(seleccionInicial.get(sel)))) {

                                dia.setInicio(true);
                                dia.setMulti(true);
                                listaSeleccionadosMulti.add(dia);

                                fin = true;
                                break;
                            }
                        }

                    }

                    if (fin) {
                        break;
                    }

                }

                multi = asignarMulti();

                if (multiant > multi) {

                    for (Day listadia : listadias) {
                        if (listadia.isSelected()) {
                            removeItemSelected(listadia.getPosicionCal());
                        }
                    }

                    listaModeloSQLFinal = new ListaModeloSQL();
                    listaModeloSQLFinal.addAllLista(listaModeloSQLSimple.getLista());

                    if (listaModeloSQLMulti != null) {
                        listaModeloSQLFinal.addAllLista(listaModeloSQLMulti.getLista());
                    }

                    listaSeleccionadosFinal = new ListaDays();
                    listaSeleccionadosFinal.addAll(listaSeleccionadosSimple);

                    if (listaSeleccionadosMulti != null && listaSeleccionadosMulti.size() > 0) {
                        listaSeleccionadosFinal.addAll(listaSeleccionadosMulti);
                    }

                    if (listaSeleccionadosFinal.size() > 0) {
                        for (Day dia : listaSeleccionadosFinal) {

                            for (Day dialista : listadias) {


                                if (dia.getFechaLong() == dialista.getFechaLong()) {
                                    addItemSelected(dialista.getPosicionCal());
                                }
                            }

                        }
                    }

                    seleccionados = listaSeleccionadosFinal.size();

                    if (seleccionados == 1) {

                        activityBase.toolbar.setSubtitle(JavaUtil.getDate(
                                listaSeleccionadosFinal.get(0).getFechaLong()));
                        if (!sinNuevo) {
                            visible(nuevo);
                        }
                        if (!sinDia) {
                            visible(verDia);
                        }
                    } else if (seleccionados > 1) {
                        activityBase.toolbar.setSubtitle("Seleccion multiple");
                        gone(nuevo);
                        gone(verDia);
                    } else {
                        activityBase.toolbar.setSubtitle("Haga su selección");
                        gone(nuevo);
                        gone(verDia);

                    }
                }

                if (tipoRV != null && tipoRV.equals(LISTA)) {

                    adaptadorRV = new RVAdapter(setViewHolder(view),
                            listaModeloSQLFinal, layoutItem);

                } else {

                    adaptadorRV = new RVAdapter(setViewHolder(view),
                            listaModeloSQLFinal.getLista(), layoutItem);
                }
                rv.setAdapter(adaptadorRV);

                setOnDayLongClick(day, position);
            }

            @Override
            public void onCreateCal(ListaDays listaDays) {

                listadias = new ListaDays();
                listadias.clear();
                listadias.addAll(listaDays);

                asignarMulti();

                Calendar c = new GregorianCalendar();

                if (listaSeleccionadosFinal.size() > 0) {

                    for (Day dialista : listadias) {

                        for (Day dia : listaSeleccionadosFinal) {

                            if (dia.getFechaLong() == dialista.getFechaLong()) {

                                addItemSelected(dialista.getPosicionCal());
                            }

                        }
                    }


                }

            }

            @Override
            public void onAdapterAttach() {


            }


        });

        setOnCalendarChangeListener(new OnCalendarChangeListener() {
            @Override
            public void prevMonth() {

                alCambiarMes();
                setOnPrevMonth();
            }

            @Override
            public void nextMonth() {

                alCambiarMes();
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

                setVerLista(listaModeloSQLFinal, listaSeleccionadosFinal);
            }
        });

        verDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVerDia(fecha, listaModeloSQLFinal);
            }
        });

        renovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removerTodo();
            }
        });


        setOnInicio();

        if (!sinBusqueda) {
            visible(buscar);
            visible(lupa);
            visible(renovar);
        }
        if (sinLista) {
            gone(verLista);
        }


        fillUpMonth();

    }

    protected void setRV() {
        if (tipoRV != null && tipoRV.equals(LISTA)) {

            adaptadorRV = new RVAdapter(setViewHolder(view),
                    listaModeloSQLFinal, layoutItem);

        } else {

            adaptadorRV = new RVAdapter(setViewHolder(view),
                    listaModeloSQLFinal.getLista(), layoutItem);
        }
        rv.setAdapter(adaptadorRV);
    }

    private void removerTodo() {

        if (nn(listaSeleccionadosMulti)) {
            for (Day day : listaSeleccionadosMulti) {
                if (day.getMonth() == month) {
                    removeItemSelected(day.getPosicionCal());
                }
            }
            listaSeleccionadosMulti.clear();
        }
        if (nn(listaModeloSQLFinal)) {
            listaModeloSQLFinal.clearLista();
        }
        if (nn(listaSeleccionadosFinal)) {
            listaSeleccionadosFinal.clear();
        }
        if (nn(listaModeloSQLSimple)) {
            listaModeloSQLSimple.clearLista();
        }
        if (nn(listaModeloSQLMulti)) {
            listaModeloSQLMulti.clearLista();
        }
        if (nn(listaSeleccionadosSimple)) {
            listaSeleccionadosSimple.clear();
        }
        if (nn(adaptadorRV)) {
            adaptadorRV.clear();
        }

    }

    private int asignarMulti() {

        int multi = 0;


        for (Day dia : listadias) {

            dia.setInicio(false);
            dia.setFin(false);
            dia.setMulti(false);

            for (int sel = seleccion; sel >= 0; sel--) {

                if (dia.getFechaLong() >= seleccionInicial.get(sel) &&
                        (dia.getFechaLong() <= seleccionFinal.get(sel) || seleccionFinal.get(sel) == 0)) {

                    if (dia.getFechaLong() == seleccionInicial.get(sel)) {
                        dia.setInicio(true);
                        dia.setMulti(true);
                        multi++;
                    } else if (dia.getFechaLong() == seleccionFinal.get(sel)) {
                        dia.setFin(true);
                        dia.setMulti(true);
                        multi++;
                    } else if (dia.getFechaLong() > seleccionInicial.get(sel) &&
                            dia.getFechaLong() < seleccionFinal.get(sel)) {
                        dia.setMulti(true);
                        multi++;
                    }
                }

            }
            listadias.set(dia.getPosicionCal(), dia);
        }

        return multi;
    }

    private void alCambiarMes() {

        seleccionados = 0;
        listaModeloSQLMulti = new ListaModeloSQL();
        listaSeleccionadosMulti = new ListaDays();

        for (int sel = seleccion; sel >= 0; sel--) {


            if (seleccionInicial.get(sel) > 0 && seleccionFinal.get(sel) > 0) {

                Calendar cini = new GregorianCalendar();
                cini.setTimeInMillis(seleccionInicial.get(sel));
                Calendar cfin = new GregorianCalendar();
                cfin.setTimeInMillis(seleccionFinal.get(sel));
                ListaDays listadias = TimeDateUtil.listaDias(cini, cfin);

                for (Day dia : listadias) {
                    ListaModeloSQL lista = setListaDia(dia.getFechaLong());
                    if (lista.getLista() != null) {
                        for (ModeloSQL modeloSQL : lista.getLista()) {
                            boolean nuevo = true;
                            if (campoId != null) {
                                for (ModeloSQL modeloSQLTmp : listaModeloSQLMulti.getLista()) {
                                    if (modeloSQL.getString(campoId).equals(modeloSQLTmp.getString(campoId))) {
                                        nuevo = false;
                                    }
                                }
                            }
                            if (nuevo) {
                                listaModeloSQLMulti.addModelo(modeloSQL);
                            }
                        }
                    }
                }
            }
        }

        for (Day dia : listaTotal) {

            dia.setInicio(false);
            dia.setFin(false);
            dia.setMulti(false);

            for (int sel = seleccion; sel >= 0; sel--) {


                if (seleccionInicial.get(sel) > 0 && seleccionFinal.get(sel) > 0) {

                    if (dia.getFechaLong() < seleccionInicial.get(sel)) {
                        continue;
                    }
                    if (dia.getFechaLong() > seleccionFinal.get(sel)) {
                        break;
                    }


                    if (dia.getFechaLong() == seleccionInicial.get(sel)) {
                        dia.setInicio(true);
                    } else if (dia.getFechaLong() == seleccionFinal.get(sel)) {
                        dia.setFin(true);
                    } else if (dia.getFechaLong() > seleccionInicial.get(sel) &&
                            dia.getFechaLong() < seleccionFinal.get(sel)) {
                        dia.setMulti(true);
                    }
                    listaSeleccionadosMulti.add(dia);
                    seleccionados++;

                }

            }

        }

        listaModeloSQLFinal = new ListaModeloSQL();
        listaModeloSQLFinal.addAllLista(listaModeloSQLSimple.getLista());
        if (listaModeloSQLMulti != null) {
            listaModeloSQLFinal.addAllLista(listaModeloSQLMulti.getLista());
        }

        ListaModeloSQL listaFija = setListaFija();
        for (ModeloSQL modeloSQL : listaFija.getLista()) {
            boolean nuevo = true;
            if (campoId != null) {
                for (ModeloSQL modeloSQLTmp : listaModeloSQLFinal.getLista()) {
                    if (modeloSQL.getString(campoId).equals(modeloSQLTmp.getString(campoId))) {
                        nuevo = false;
                    }
                }
                if (nuevo) {
                    listaModeloSQLFinal.addModelo(modeloSQL);
                }
            }
        }


        listaSeleccionadosFinal = new ListaDays();
        listaSeleccionadosFinal.addAll(listaSeleccionadosSimple);

        if (listaSeleccionadosMulti != null && listaSeleccionadosMulti.size() > 0) {
            listaSeleccionadosFinal.addAll(listaSeleccionadosMulti);
        }


        if (seleccionados == 1) {

            activityBase.toolbar.setSubtitle(JavaUtil.getDate(
                    listaSeleccionadosSimple.get(0).getFechaLong()));
            if (!sinNuevo) {
                visible(nuevo);
            }
            if (!sinDia) {
                visible(verDia);
            }

        } else if (seleccionados > 1) {
            activityBase.toolbar.setSubtitle("Seleccion multiple");
            gone(nuevo);
            gone(verDia);
        } else {
            activityBase.toolbar.setSubtitle("Haga su selección");
            gone(nuevo);
            gone(verDia);

        }

        if (tipoRV != null && tipoRV.equals(LISTA)) {

            adaptadorRV = new RVAdapter(setViewHolder(view),
                    listaModeloSQLFinal, layoutItem);

        } else {
            adaptadorRV = new RVAdapter(setViewHolder(view),
                    listaModeloSQLFinal.getLista(), layoutItem);
        }
        rv.setAdapter(adaptadorRV);
    }

    private void regenerarPosicionesLista() {

        seleccionados = 0;

        for (Day day : listadias) {

            day.setPosicionListaFinal(-1);
            day.setPosicionListaSimple(-1);
            day.setPosicionListaMulti(-1);

            if (listaSeleccionadosFinal != null && listaSeleccionadosFinal.size() > 0) {

                for (Day daySel : listaSeleccionadosFinal) {

                    if (day.getFechaLong() == daySel.getFechaLong()) {

                        day.setPosicionListaFinal(listaSeleccionadosFinal.indexOf(daySel));


                    }

                }
            }

            if (listaSeleccionadosSimple != null && listaSeleccionadosSimple.size() > 0) {


                for (Day daySel : listaSeleccionadosSimple) {

                    if (day.getFechaLong() == daySel.getFechaLong()) {

                        day.setPosicionListaSimple(listaSeleccionadosSimple.indexOf(daySel));


                    }

                }
                seleccionados = listaSeleccionadosSimple.size();
            }

            if (listaSeleccionadosMulti != null && listaSeleccionadosMulti.size() > 0) {


                for (Day daySel : listaSeleccionadosMulti) {

                    if (day.getFechaLong() == daySel.getFechaLong()) {

                        day.setPosicionListaMulti(listaSeleccionadosMulti.indexOf(daySel));

                    }

                }
                seleccionados += listaSeleccionadosMulti.size();

            }
        }

    }


    protected long getFecha() {

        return fecha;
    }

    protected void setSinNuevo(boolean sinNuevo) {

        this.sinNuevo = sinNuevo;
    }

    protected void setSinLista(boolean sinLista) {

        this.sinLista = sinLista;
    }

    protected void setSinBusqueda(boolean sinBusqueda) {

        this.sinBusqueda = sinBusqueda;
    }

    public void setSinDia(boolean sinDia) {
        this.sinDia = sinDia;
    }

    protected ListaModeloSQL setListaFija() {
        return new ListaModeloSQL();
    }

    protected abstract ListaModeloSQL setListaDia(long fecha);

    protected abstract void setVerDia(long fecha, ListaModeloSQL listaModeloSQL);

    protected abstract void setLayoutItem();

    protected abstract void setCampos();

    protected abstract void setTitulo();

    protected abstract void setOnDayClick(Day day, int position);

    protected abstract void setOnDayLongClick(Day day, int position);

    protected abstract void setNuevo(long fecha);

    protected abstract void setVerLista(ListaModeloSQL listaModeloSQL, ListaDays listaDays);

    protected abstract void setOnPrevMonth();

    protected abstract void setOnNextMonth();

    protected abstract void setOnInicio();

    protected abstract TipoViewHolder setViewHolder(View view);

    protected abstract ListaAdaptadorFiltroModelo setAdaptadorAuto
            (Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos);


    public void setCampo(String campo) {
        this.campo = campo;
    }

    protected void fillUpMonth() {

        days = new ListaDays();
        Calendar hoy = new GregorianCalendar();

        //almaceno el nombre del primer  dia del mes y año en cuestion
        String nameFirstDay = getNameDay(1, month, year);

        System.out.println("año " + year + " mes " + month);

        int blankSpaces = 0;
        switch (nameFirstDay) {

            case "Sunday":
                if (lenguaje == 1) {
                    blankSpaces = 0;
                } else {
                    blankSpaces = 6;
                }
                break;
            case "Monday":
                if (lenguaje == 1) {
                    blankSpaces = 1;
                } else {
                    blankSpaces = 0;
                }
                break;
            case "Tuesday":
                if (lenguaje == 1) {
                    blankSpaces = 2;
                } else {
                    blankSpaces = 1;
                }
                break;
            case "Wednesday":
                if (lenguaje == 1) {
                    blankSpaces = 3;
                } else {
                    blankSpaces = 2;
                }
                break;
            case "Thursday":
                if (lenguaje == 1) {
                    blankSpaces = 4;
                } else {
                    blankSpaces = 3;
                }
                break;
            case "Friday":
                if (lenguaje == 1) {
                    blankSpaces = 5;
                } else {
                    blankSpaces = 4;
                }
                break;
            case "Saturday":
                if (lenguaje == 1) {
                    blankSpaces = 6;
                } else {
                    blankSpaces = 5;
                }
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

                Calendar date = new GregorianCalendar(tyear, tmonth, i);

                long fecha = date.getTimeInMillis();
                lista = new ListaModeloSQL();

                lista = setListaDia(fecha);

                if (lista.sizeLista() > 0) {

                    days.add(new Day(date, false,
                            textColorDaysOfAnotherMonth, contexto.getResources().getColor(R.color.Color_card_notok), squares));
                } else {
                    days.add(new Day(date, false,
                            textColorDaysOfAnotherMonth, backgroundColorDaysOfAnotherMonth, squares));
                }
                squares++;
            }
        }


        int numberOfDaysMonthYear = getNumberOfDaysMonthYear(year, month);

        for (int i = 1; i <= numberOfDaysMonthYear; i++) {

            Calendar c = new GregorianCalendar(year, month, i);

            //Date date = new Date(year,month,i);
            long fecha = c.getTimeInMillis();
            lista = new ListaModeloSQL();

            lista = setListaDia(fecha);


            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(fecha);


            if (lista.sizeLista() > 0) {
                days.add(new Day(calendar, contexto.getResources().getColor(R.color.Color_card_notok), contexto.getResources().getColor(R.color.Color_card_notok), lista.getLista(), squares));
            } else if (hoy.get(Calendar.YEAR) == year && hoy.get(Calendar.MONTH) == month && this.currentDay == i) {
                days.add(new Day(calendar, textColorCurrentDayDay, currentDayBackgroundColor, squares));
            } else {
                days.add(new Day(calendar, textColorDaysOfMonth, backgroundColorDaysOfMonth, squares));
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

                Calendar c = new GregorianCalendar(tyear, tmonth, i);

                //Date date = new Date(year,month,i);
                long fecha = c.getTimeInMillis();
                lista = new ListaModeloSQL();

                lista = setListaDia(fecha);

                if (lista.sizeLista() > 0) {

                    days.add(new Day(new GregorianCalendar(tyear, tmonth, i), false,
                            textColorDaysOfAnotherMonth, contexto.getResources().getColor(R.color.Color_card_notok), squares));
                } else {

                    days.add(new Day(new GregorianCalendar(tyear, tmonth, i), false,
                            textColorDaysOfAnotherMonth, backgroundColorDaysOfAnotherMonth, squares));
                }
                squares++;
                if (squares == 42) {
                    break;
                }
            }
        }

        if (buscando) {

            Day day = new Day(cbusca);
            ListaModeloSQL listaBusca = setListaDia(day.getFechaLong());

            if (listaBusca != null) {

                for (ModeloSQL modeloSQL : listaBusca.getLista()) {

                    boolean nuevo = true;
                    if (campoId != null) {
                        for (ModeloSQL modeloSQLTmp : listaModeloSQLSimple.getLista()) {
                            if (modeloSQL.getString(campoId).equals(modeloSQLTmp.getString(campoId))) {
                                nuevo = false;
                            }
                        }
                    }
                    if (nuevo) {
                        listaModeloSQLSimple.addModelo(modeloSQL);
                    }
                }
            }

            for (Day day1 : days) {

                if (day1.getFechaLong() == day.getSoloFechaLong()) {
                    day1.setBusca(true);
                    addItemSelected(day1.getPosicionCal());
                    listaSeleccionadosSimple.add(day1);
                    day1.setPosicionListaSimple(listaSeleccionadosSimple.indexOf(day1));
                    break;
                }
            }

            if (tipoRV != null && tipoRV.equals(LISTA)) {

                adaptadorRV = new RVAdapter(setViewHolder(view),
                        listaModeloSQLSimple, layoutItem);

            } else {
                adaptadorRV = new RVAdapter(setViewHolder(view),
                        listaModeloSQLSimple.getLista(), layoutItem);
            }
            rv.setAdapter(adaptadorRV);

            AndroidUtil.ocultarTeclado(getContext(), view);
            buscando = false;
            activityBase.toolbar.setSubtitle(day.getFechaString());

        }

        setAdapter();

    }

    protected void setAdapter() {

        onDayClickListener.onCreateCal(days);

        String locales = AndroidUtil.getSystemLocale();

        if (locales.equals("en")) {

            lenguaje = 1;

        } else if (locales.equals("pt")) {

            lenguaje = 2;
        }

        setLanguage(lenguaje);

        rvAdapter = new RVAdapter(setViewHolderCal(view), days, R.layout.item_list_layout);
        recyclerViewDays.setAdapter(rvAdapter);
        onDayClickListener.onAdapterAttach();
    }

    protected TipoViewHolder setViewHolderCal(View view) {
        return new ViewHolderRV(view);
    }

    protected ListaDays getlistaTotal() {

        Calendar hoy = new GregorianCalendar();
        ListaDays days = new ListaDays();

        int yearInicio = hoy.get(Calendar.YEAR) - 1;
        int yearFin = yearInicio + 2;
        int month = hoy.get(Calendar.MONTH);
        int year = yearInicio;

        for (int y = yearInicio; y <= yearFin; y++) {

            for (int m = 0; m <= 11; m++) {

                //almaceno el nombre del primer  dia del mes y año en cuestion
                String nameFirstDay = getNameDay(1, month, year);

                int blankSpaces = 0;
                switch (nameFirstDay) {

                    case "Sunday":
                        if (lenguaje == 1) {
                            blankSpaces = 0;
                        } else {
                            blankSpaces = 6;
                        }
                        break;
                    case "Monday":
                        if (lenguaje == 1) {
                            blankSpaces = 1;
                        } else {
                            blankSpaces = 0;
                        }
                        break;
                    case "Tuesday":
                        if (lenguaje == 1) {
                            blankSpaces = 2;
                        } else {
                            blankSpaces = 1;
                        }
                        break;
                    case "Wednesday":
                        if (lenguaje == 1) {
                            blankSpaces = 3;
                        } else {
                            blankSpaces = 2;
                        }
                        break;
                    case "Thursday":
                        if (lenguaje == 1) {
                            blankSpaces = 4;
                        } else {
                            blankSpaces = 3;
                        }
                        break;
                    case "Friday":
                        if (lenguaje == 1) {
                            blankSpaces = 5;
                        } else {
                            blankSpaces = 4;
                        }
                        break;
                    case "Saturday":
                        if (lenguaje == 1) {
                            blankSpaces = 6;
                        } else {
                            blankSpaces = 5;
                        }
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
                        squares++;
                    }
                }

                int numberOfDaysMonthYear = getNumberOfDaysMonthYear(year, month);

                Calendar calendar = new GregorianCalendar();

                for (int i = 1; i <= numberOfDaysMonthYear; i++) {

                    calendar = new GregorianCalendar(year, month, i);

                    days.add(new Day(calendar, squares));

                    squares++;
                }

                month++;
                if (month == 12) {
                    month = 0;
                    year++;
                }
                if (year == yearFin && calendar.get(Calendar.MONTH) == hoy.get(Calendar.MONTH)) {
                    break;
                }

            }
        }

        return days;
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
     * retorna el mes visible en el calendario
     *
     * @return
     */
    public int getMonth() {
        return month;
    }

    /**
     * retorna el año del mes visible en el calendario
     *
     * @return
     */
    public int getYear() {
        return year;
    }

    /*
    /**
     * un objeto de tipo day para obtener la fecha (año,mes,dia) con un objeto calendar
     * <p>
     * otros metodos como setBackgroundColor(int backgroundColor) y getBackgroundColor() color del fondo del numero de dia del mes
     * setTextColor(int textColor) y getTextColor() color del texto numero de dia del mes
     *
     * @param day
     * @param position
     */
    /*
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
    /*
    @Override
    public void dayOnLongClik(Day day, int position) {

        onDayClickListener.dayOnLongClik(day, position);
    }
    */

    protected void setListabase() {

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

        void onCreateCal(ListaDays listaDays);

        void onAdapterAttach();
    }


    protected OnDayClickListener onDayClickListener;

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
    }


    public void setItemSelected(int position) {
        days.get(position).setSelected(true);
        for (int i = 0; i < days.size(); i++) {
            if (i != position)
                days.get(i).setSelected(false);
        }
        rvAdapter.notifyItemChanged(0, 41);
        rvAdapter.notifyDataSetChanged();

    }


    /**
     * marca un dia en el calendario como seleccionado
     *
     * @param position
     */
    public void addItemSelected(int position) {
        days.get(position).setSelected(true);
        rvAdapter.notifyItemChanged(position);
        rvAdapter.notifyDataSetChanged();
    }


    /**
     * remueve un dia en el calendario como seleccionado
     *
     * @param position
     */
    public void removeItemSelected(int position) {
        days.get(position).setSelected(false);
        rvAdapter.notifyItemChanged(position);
        rvAdapter.notifyDataSetChanged();
    }

    public ListaDays getDays() {
        return days;
    }

    public void setDays(ListaDays listadias) {
        days = listadias;
    }


    public void setLanguage(int language) {

        System.out.println("language = " + language);

        if (language == 1) {//si el idioma es el ingles
            textViewL.setText("M");
            textViewM.setText("T");
            textViewX.setText("W");
            textViewJ.setText("T");
            textViewV.setText("F");
            textViewS.setText("S");
            textViewD.setText("S");

            enero = "January";
            febrero = "February";
            marzo = "March";
            abril = "April";
            mayo = "May";
            junio = "June";
            julio = "July";
            agosto = "August";
            septiembre = "September";
            octubre = "October";
            noviembre = "November";
            diciembre = "December";

        } else if (language == 2) {//si el idioma es el portugues
            textViewL.setText("S");
            textViewM.setText("T");
            textViewX.setText("Q");
            textViewJ.setText("Q");
            textViewV.setText("S");
            textViewS.setText("S");
            textViewD.setText("D");

            enero = "Janeiro";
            febrero = "Fevereiro";
            marzo = "Março";
            abril = "Abril";
            mayo = "Maio";
            junio = "Junho";
            julio = "Julho";
            agosto = "Agosto";
            septiembre = "Setembro";
            octubre = "Outubro";
            noviembre = "Novembro";
            diciembre = "Dezembro";

        } else {

            textViewL.setText("L");
            textViewM.setText("M");
            textViewX.setText("X");
            textViewJ.setText("J");
            textViewV.setText("V");
            textViewS.setText("S");
            textViewD.setText("D");

            enero = "Enero";
            febrero = "Febrero";
            marzo = "Marzo";
            abril = "Abril";
            mayo = "Mayo";
            junio = "Junio";
            julio = "Julio";
            agosto = "Agosto";
            septiembre = "Septiembre";
            octubre = "Octubre";
            noviembre = "Noviembre";
            diciembre = "Deciembre";
        }

        textViewMY.setText(getStringMonth(month) + " " + year);


    }

    /**
     * retorna un mes como un string de pendiendo del idioma establecido en el OneCalendar
     *
     * @param numMonth numero del mes iniciando desde 0,1,2...
     * @return mes en texto segun el idioma elegido
     */
    public String getStringMonth(int numMonth) {
        switch (numMonth) {
            case 0:
                return enero;

            case 1:
                return febrero;

            case 2:
                return marzo;

            case 3:
                return abril;

            case 4:
                return mayo;

            case 5:
                return junio;

            case 6:
                return julio;

            case 7:
                return agosto;

            case 8:
                return septiembre;

            case 9:
                return octubre;

            case 10:
                return noviembre;

            case 11:
                return diciembre;

        }
        return enero;
    }

    private void nextMoth() {
        if (month == 11) {
            month = 0;
            year++;
        } else {
            month++;
        }

        fillUpMonth();

    }

    private void prevMonth() {
        if (month == 0) {
            month = 11;
            year--;
        } else {
            month--;
        }

        fillUpMonth();

    }


    public interface OnCalendarChangeListener {
        /**
         * notifica al usuario que el calendario a cambiado al mes anterior
         */
        void prevMonth();

        /**
         * notifica al usuario que el calendario a cambiado al mes siguiente
         */
        void nextMonth();
    }


    private OnCalendarChangeListener onCalendarChangeListener;

    public void setOnCalendarChangeListener(OnCalendarChangeListener onCalendarChangeListener) {
        this.onCalendarChangeListener = onCalendarChangeListener;
    }

    public ListaModeloSQL getListaModeloSQLFinal() {
        return listaModeloSQLFinal;
    }

    public ListaDays getListaSeleccionadosFinal() {
        return listaSeleccionadosFinal;
    }

    protected void abrirSemana(long fecha) {

    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        private Button btnDia;
        private ImageButton verDia;
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
            //btnDia = itemView.findViewById(R.id.textViewDay);
            this.textColorSelectedDay = contexto.getResources().getColor(R.color.Color_contador_notok);
            this.backgroundColorSelectedDay = contexto.getResources().getColor(R.color.Color_contador_notok);
            this.textColorInicioDay = contexto.getResources().getColor(R.color.Color_contador_acept);
            this.backgroundColorInicioDay = contexto.getResources().getColor(R.color.Color_contador_acept);
            this.textColorFinDay = contexto.getResources().getColor(R.color.Color_contador_ok);
            this.backgroundColorFinDay = contexto.getResources().getColor(R.color.Color_contador_ok);
            this.textColorMultiDay = contexto.getResources().getColor(R.color.colorSecondaryDark);
            this.backgroundColorMultiDay = contexto.getResources().getColor(R.color.colorSecondaryDark);
            this.textColorBuscaDay = contexto.getResources().getColor(R.color.Color_busqueda);
            this.backgroundColorBuscaDay = contexto.getResources().getColor(R.color.Color_busqueda);
        }

        @Override
        public void bind(ArrayList<?> lista, final int position) {

            final Day dia = (Day) lista.get(position);
            final Calendar cal = dia.getDate();//Calendar.getInstance();
            //cal.setTime(dia.getDate());
            int nday = cal.get(Calendar.DAY_OF_MONTH);
            int pad = (int) sizeText / 2;

            ViewGroupLayout vistaCard = new ViewGroupLayout(contexto, relativeLayout, new CardView(contexto));
            card = (CardView) vistaCard.getViewGroup();
            LinearLayoutCompat mainLinear = (LinearLayoutCompat) vistaCard.addVista(new LinearLayoutCompat(contexto));
            mainLinear.setOrientation(ViewGroupLayout.ORI_LLC_HORIZONTAL);
            ViewGroupLayout vistaLinear = new ViewGroupLayout(contexto, mainLinear);
            verDia = vistaLinear.addImageButtonSecundary(Estilos.getIdDrawable(contexto, "ic_ver_indigo"));
            Estilos.setLayoutParams(vistaLinear.getViewGroup(), verDia, Estilos.Constantes.MATCH_PARENT,
                    (int) ((double) getResources().getDimension(Estilos.getIdDimens(contexto, "altobtn")) / 3));
            btnDia = vistaLinear.addButtonTrans(null);
            Estilos.setLayoutParams(vistaLinear.getViewGroup(), btnDia, Estilos.Constantes.MATCH_PARENT,
                    (int) ((double) getResources().getDimension(Estilos.getIdDimens(contexto, "altobtn")) / 3));

            recyclerView = (RecyclerView) vistaLinear.addVista(new RecyclerView(contexto));
            recyclerView.setLayoutManager(new LinearLayoutManager(contexto));
            ListaModeloSQL listaEvento = setListaDia(cal.getTimeInMillis());
            RVAdapter adaptadorRV = new RVAdapter(setViewHolderCard(itemView),
                    listaEvento.getLista(), R.layout.item_list_layout);
            recyclerView.setAdapter(adaptadorRV);
            LinearLayoutCompat.LayoutParams layoutParamsrv = new LinearLayoutCompat.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            recyclerView.setLayoutParams(layoutParamsrv);

            btnDia.setText(nday + "");
            btnDia.setTextSize(sizeText);

            if (dia.isBusca()) {
                btnDia.setTextColor(textColorBuscaDay);
                itemView.setBackgroundColor(backgroundColorBuscaDay);
            } else if (dia.isInicio()) {
                btnDia.setTextColor(textColorInicioDay);
                itemView.setBackgroundColor(backgroundColorInicioDay);
            } else if (dia.isFin()) {
                btnDia.setTextColor(textColorFinDay);
                itemView.setBackgroundColor(backgroundColorFinDay);
            } else if (dia.isMulti()) {
                btnDia.setTextColor(textColorMultiDay);
                itemView.setBackgroundColor(backgroundColorMultiDay);
            } else if (dia.isSelected()) {
                btnDia.setTextColor(textColorSelectedDay);
                itemView.setBackgroundColor(backgroundColorSelectedDay);
            } else if (dia.isValid()) {
                btnDia.setTextColor(dia.getTextColor());
                itemView.setBackgroundColor(dia.getBackgroundColor());
            } else {
                btnDia.setTextColor(dia.getTextColorNV());
                itemView.setBackgroundColor(dia.getBackgroundColorNV());
                btnDia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        abrirSemana(cal.getTimeInMillis());
                    }
                });
            }

            if (dia.getFechaLong() == TimeDateUtil.soloFecha(JavaUtil.hoy())) {
                btnDia.setBackgroundColor(contexto.getResources().getColor(R.color.colorSecondaryDark));
                btnDia.setTextColor(contexto.getResources().getColor(R.color.colorPrimary));
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
        public void bind(ModeloSQL modeloSQL) {

            ViewGroupLayout vistaCard = new ViewGroupLayout(contexto, relativeLayout, new CardView(contexto));
            card = (CardView) vistaCard.getViewGroup();
            ViewGroupLayout vistaLinear = new ViewGroupLayout(contexto, vistaCard.getViewGroup());
            btnNombre = vistaLinear.addButtonSecondary(null);
            if (campoCard != null) {
                btnNombre.setText(modeloSQL.getString(campoCard));
            }
            if (campoColor != null) {
                btnNombre.setBackgroundColor(Color.parseColor(modeloSQL.getString(campoColor)));
            }
            btnNombre.setTextSize(sizeText / 2);
            LinearLayoutCompat.LayoutParams layoutParamsrv = new LinearLayoutCompat.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,
                            (int) ((double) getResources().getDimension(Estilos.getIdDimens(contexto, "altobtn")) / 3));
            btnNombre.setLayoutParams(layoutParamsrv);


            super.bind(modeloSQL);
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
