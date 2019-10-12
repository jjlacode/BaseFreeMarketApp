package com.codevsolution.freemarketsapp.ui;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.base.time.TimePickerFragment;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

import static com.codevsolution.base.javautil.JavaUtil.getTime;
import static com.codevsolution.base.javautil.JavaUtil.hoy;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PERFILACTIVO;

public class FragmentCRUDPerfil extends FragmentCRUD implements ContratoPry.Tablas {


    private EditText nombre;
    private EditText descripcion;
    private EditMaterialLayout lunes;
    private EditMaterialLayout martes;
    private EditMaterialLayout miercoles;
    private EditMaterialLayout jueves;
    private EditMaterialLayout viernes;
    private EditMaterialLayout sabado;
    private EditMaterialLayout domingo;
    private EditText vacaciones;
    private EditText sueldo;
    private Button btnperfilact;
    private TextView activo;
    private LinearLayoutCompat lyGridBtn;
    private Button [][] listaBtn = new Button[4][8];


    private static ConsultaBD consulta = new ConsultaBD();

    public FragmentCRUDPerfil() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view){

        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    @Override
    protected void setLista() {

    }

    @Override
    protected void setImagen() {
    }

    @Override
    protected void setTabla() {

        tabla = TABLA_PERFIL;

    }

    @Override
    protected void setTablaCab() {

        tablaCab = ContratoPry.getTabCab(tabla);
    }

    @Override
    protected void setCampos() {

        campos = ContratoPry.obtenerCampos(tabla);

    }

    @Override
    protected void setDatos() {

        obtenerDatosBtns();
        nombre.setText(modelo.getString(PERFIL_NOMBRE));
        descripcion.setText(modelo.getString(PERFIL_DESCRIPCION));
        lunes.setText(modelo.getString(PERFIL_HORASLUNES));
        martes.setText(modelo.getString(PERFIL_HORASMARTES));
        miercoles.setText(modelo.getString(PERFIL_HORASMIERCOLES));
        jueves.setText(modelo.getString(PERFIL_HORASJUEVES));
        viernes.setText(modelo.getString(PERFIL_HORASVIERNES));
        sabado.setText(modelo.getString(PERFIL_HORASSABADO));
        domingo.setText(modelo.getString(PERFIL_HORASDOMINGO));
        vacaciones.setText(modelo.getString(PERFIL_VACACIONES));
        sueldo.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(PERFIL_SUELDO)));
        btndelete.setVisibility(View.VISIBLE);
        btnperfilact.setVisibility(View.VISIBLE);

        if (modelo.getString(PERFIL_NOMBRE) != null && modelo.getString(PERFIL_NOMBRE).equals(Interactor.perfila)) {

            activo.setVisibility(View.VISIBLE);
            btnperfilact.setVisibility(View.GONE);

        }else{

            activo.setVisibility(View.GONE);
            btnperfilact.setVisibility(View.VISIBLE);
        }


    }

    private void obtenerDatosBtns() {

        long lunesIM = 0, lunesFM = 0, lunesIT = 0, lunesFT = 0;
        long martesIM = 0, martesFM = 0, martesIT = 0, martesFT = 0;
        long miercolesIM = 0, miercolesFM = 0, miercolesIT = 0, miercolesFT = 0;
        long juevesIM = 0, juevesFM = 0, juevesIT = 0, juevesFT = 0;
        long viernesIM = 0, viernesFM = 0, viernesIT = 0, viernesFT = 0;
        long sabadoIM = 0, sabadoFM = 0, sabadoIT = 0, sabadoFT = 0;
        long domingoIM = 0, domingoFM = 0, domingoIT = 0, domingoFT = 0;


        lunesIM = modelo.getLong(PERFIL_HORAIMLUNES);
        lunesFM = modelo.getLong(PERFIL_HORAFMLUNES);
        lunesIT = modelo.getLong(PERFIL_HORAITLUNES);
        lunesFT = modelo.getLong(PERFIL_HORAFTLUNES);
        martesIM = modelo.getLong(PERFIL_HORAIMMARTES);
        martesFM = modelo.getLong(PERFIL_HORAFMMARTES);
        martesIT = modelo.getLong(PERFIL_HORAITMARTES);
        martesFT = modelo.getLong(PERFIL_HORAFTMARTES);
        miercolesIM = modelo.getLong(PERFIL_HORAIMMIERCOLES);
        miercolesFM = modelo.getLong(PERFIL_HORAFMMIERCOLES);
        miercolesIT = modelo.getLong(PERFIL_HORAITMIERCOLES);
        miercolesFT = modelo.getLong(PERFIL_HORAFTMIERCOLES);
        juevesIM = modelo.getLong(PERFIL_HORAIMJUEVES);
        juevesFM = modelo.getLong(PERFIL_HORAFMJUEVES);
        juevesIT = modelo.getLong(PERFIL_HORAITJUEVES);
        juevesFT = modelo.getLong(PERFIL_HORAFTJUEVES);
        viernesIM = modelo.getLong(PERFIL_HORAIMVIERNES);
        viernesFM = modelo.getLong(PERFIL_HORAFMVIERNES);
        viernesIT = modelo.getLong(PERFIL_HORAITVIERNES);
        viernesFT = modelo.getLong(PERFIL_HORAFTVIERNES);
        sabadoIM = modelo.getLong(PERFIL_HORAIMSABADO);
        sabadoFM = modelo.getLong(PERFIL_HORAFMSABADO);
        sabadoIT = modelo.getLong(PERFIL_HORAITSABADO);
        sabadoFT = modelo.getLong(PERFIL_HORAFTSABADO);
        domingoIM = modelo.getLong(PERFIL_HORAIMDOMINGO);
        domingoFM = modelo.getLong(PERFIL_HORAFMDOMINGO);
        domingoIT = modelo.getLong(PERFIL_HORAITDOMINGO);
        domingoFT = modelo.getLong(PERFIL_HORAFTDOMINGO);

        llenarTextosBtns(listaBtn [0][1],(lunesIM));
        llenarTextosBtns(listaBtn [1][1],(lunesFM));
        llenarTextosBtns(listaBtn [2][1],(lunesIT));
        llenarTextosBtns(listaBtn [3][1],(lunesFT));
        llenarTextosBtns(listaBtn [0][2],(martesIM));
        llenarTextosBtns(listaBtn [1][2],(martesFM));
        llenarTextosBtns(listaBtn [2][2],(martesIT));
        llenarTextosBtns(listaBtn [3][2],(martesFT));
        llenarTextosBtns(listaBtn [0][3],(miercolesIM));
        llenarTextosBtns(listaBtn [1][3],(miercolesFM));
        llenarTextosBtns(listaBtn [2][3],(miercolesIT));
        llenarTextosBtns(listaBtn [3][3],(miercolesFT));
        llenarTextosBtns(listaBtn [0][4],(juevesIM));
        llenarTextosBtns(listaBtn [1][4],(juevesFM));
        llenarTextosBtns(listaBtn [2][4],(juevesIT));
        llenarTextosBtns(listaBtn [3][4],(juevesFT));
        llenarTextosBtns(listaBtn [0][5],(viernesIM));
        llenarTextosBtns(listaBtn [1][5],(viernesFM));
        llenarTextosBtns(listaBtn [2][5],(viernesIT));
        llenarTextosBtns(listaBtn [3][5],(viernesFT));
        llenarTextosBtns(listaBtn [0][6],(sabadoIM));
        llenarTextosBtns(listaBtn [1][6],(sabadoFM));
        llenarTextosBtns(listaBtn [2][6],(sabadoIT));
        llenarTextosBtns(listaBtn [3][6],(sabadoFT));
        llenarTextosBtns(listaBtn [0][7],(domingoIM));
        llenarTextosBtns(listaBtn [1][7],(domingoFM));
        llenarTextosBtns(listaBtn [2][7],(domingoIT));
        llenarTextosBtns(listaBtn [3][7],(domingoFT));

        lunes.setText(String.valueOf(modelo.getInt(PERFIL_HORASLUNES)));
        martes.setText(String.valueOf(modelo.getInt(PERFIL_HORASMARTES)));
        miercoles.setText(String.valueOf(modelo.getInt(PERFIL_HORASMIERCOLES)));
        jueves.setText(String.valueOf(modelo.getInt(PERFIL_HORASJUEVES)));
        viernes.setText(String.valueOf(modelo.getInt(PERFIL_HORASVIERNES)));
        sabado.setText(String.valueOf(modelo.getInt(PERFIL_HORASSABADO)));
        domingo.setText(String.valueOf(modelo.getInt(PERFIL_HORASDOMINGO)));

    }

    private void llenarTextosBtns(Button boton, long hora){

        boton.setText(JavaUtil.getTime(hora));
        if (hora>=0){
            boton.setBackgroundColor(getResources().getColor(R.color.Color_card_ok));
            boton.setText(JavaUtil.getTime(hora));
        }else{
            boton.setBackgroundColor(getResources().getColor(R.color.Color_card_notok));
            boton.setText(JavaUtil.getTime(0));
        }
    }

    @Override
    protected void setAcciones() {

        btnperfilact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Interactor.perfila = nombre.getText().toString();
                activo.setVisibility(View.VISIBLE);
                btnperfilact.setVisibility(View.GONE);

                SharedPreferences preferences=getContext().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString(PERFILACTIVO, Interactor.perfila);
                editor.apply();

                new Interactor.Calculos.Tareafechas().execute();

                subTitulo = Interactor.setNamefdef();
                enviarAct();
            }
        });


    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.perfil;
        tituloPlural = R.string.perfiles;
        tituloNuevo = R.string.nuevo_perfil;
    }


    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_perfil;
        layoutItem = R.layout.item_list_perfil;

    }

    @Override
    protected void setInicio() {

        nombre = (EditText) ctrl(R.id.etnomudperfil, PERFIL_NOMBRE);
        descripcion = (EditText) ctrl(R.id.etdescudperfil, PERFIL_DESCRIPCION);
        vacaciones = (EditText) ctrl(R.id.etvacaudperfil, PERFIL_VACACIONES);
        sueldo = (EditText) ctrl(R.id.etsueldocudperfil, PERFIL_SUELDO);
        btnperfilact = (Button) ctrl(R.id.btnperfilactudperfil);
        activo = (TextView) ctrl(R.id.tvpactivocudperfil);
        lyGridBtn = (LinearLayoutCompat) ctrl(R.id.lygridbtn);


        ViewGroupLayout vistaCab = new ViewGroupLayout(contexto,lyGridBtn);
        vistaCab.setOrientacion(LinearLayoutCompat.HORIZONTAL);

        vistaCab.addTextView(null);
        vistaCab.addTextView(getString(R.string.abr_lunes));
        vistaCab.addTextView(getString(R.string.abr_martes));
        vistaCab.addTextView(getString(R.string.abr_miercoles));
        vistaCab.addTextView(getString(R.string.abr_jueves));
        vistaCab.addTextView(getString(R.string.abr_viernes));
        vistaCab.addTextView(getString(R.string.abr_sabado));
        vistaCab.addTextView(getString(R.string.abr_domingo));

        actualizarArrays(vistaCab);
        ViewGroupLayout vistaGridIM = new ViewGroupLayout(contexto,lyGridBtn);
        vistaGridIM.setOrientacion(LinearLayoutCompat.HORIZONTAL);

        listaBtn [0][0] = vistaGridIM.addButtonPrimary("IM");
        listaBtn [0][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contexto, "Hora inicio horario de mañana", Toast.LENGTH_SHORT).show();
            }
        });

        for (int i = 1; i<=7; i++) {

            listaBtn [0][i] = vistaGridIM.addButtonTrans(TimeDateUtil.getTimeString(9*HORASLONG));
            final int finalI = i;
            listaBtn [0][i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTimePickerDialogfin(listaBtn[0][finalI],0, finalI);
                    listaBtn[0][finalI].setBackgroundColor(getResources().getColor(R.color.Color_card_ok));

                }
            });
            listaBtn [0][i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    deshabilitarHora(listaBtn[0][finalI],0,finalI);
                    listaBtn[0][finalI].setBackgroundColor(getResources().getColor(R.color.Color_card_notok));
                    return true;
                }
            });

        }
        actualizarArrays(vistaGridIM);
        ViewGroupLayout vistaGridFM = new ViewGroupLayout(contexto,lyGridBtn);
        vistaGridFM.setOrientacion(LinearLayoutCompat.HORIZONTAL);

        listaBtn [1][0] = vistaGridFM.addButtonPrimary("FM");
        listaBtn [1][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contexto, "Hora fin horario de mañana", Toast.LENGTH_SHORT).show();
            }
        });


        for (int i = 1; i<=7; i++) {

            listaBtn [1][i] = vistaGridFM.addButtonTrans(TimeDateUtil.getTimeString(14*HORASLONG));
            final int finalI = i;
            listaBtn [1][i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTimePickerDialogfin(listaBtn[1][finalI],1, finalI);
                    listaBtn[1][finalI].setBackgroundColor(getResources().getColor(R.color.Color_card_ok));

                }
            });
            listaBtn [1][i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    deshabilitarHora(listaBtn[1][finalI],1,finalI);
                    listaBtn[1][finalI].setBackgroundColor(getResources().getColor(R.color.Color_card_notok));
                    return true;
                }
            });

        }
        actualizarArrays(vistaGridFM);
        ViewGroupLayout vistaGridIT = new ViewGroupLayout(contexto,lyGridBtn);
        vistaGridIT.setOrientacion(LinearLayoutCompat.HORIZONTAL);

        listaBtn [2][0] = vistaGridIT.addButtonPrimary("IT");
        listaBtn [2][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contexto, "Hora inicio horario de tarde", Toast.LENGTH_SHORT).show();
            }
        });


        for (int i = 1; i<=7; i++) {

            listaBtn [2][i] = vistaGridIT.addButtonTrans(TimeDateUtil.getTimeString(16*HORASLONG));
            final int finalI = i;
            listaBtn [2][i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTimePickerDialogfin(listaBtn[2][finalI],2, finalI);
                    listaBtn[2][finalI].setBackgroundColor(getResources().getColor(R.color.Color_card_ok));

                }
            });
            listaBtn [2][i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    deshabilitarHora(listaBtn[2][finalI],2,finalI);
                    listaBtn[2][finalI].setBackgroundColor(getResources().getColor(R.color.Color_card_notok));
                    return true;
                }
            });

        }
        actualizarArrays(vistaGridIT);
        ViewGroupLayout vistaGridFT = new ViewGroupLayout(contexto,lyGridBtn);
        vistaGridFT.setOrientacion(LinearLayoutCompat.HORIZONTAL);

        listaBtn [3][0] = vistaGridFT.addButtonPrimary("FT");
        listaBtn [3][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contexto, "Hora fin horario de tarde", Toast.LENGTH_SHORT).show();
            }
        });


        for (int i = 1; i<=7; i++) {

            listaBtn [3][i] = vistaGridFT.addButtonTrans(TimeDateUtil.getTimeString(19*HORASLONG));
            final int finalI = i;
            listaBtn [3][i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (listaBtn [3][finalI].isLongClickable()) {
                        showTimePickerDialogfin(listaBtn[3][finalI], 3, finalI);
                        listaBtn[3][finalI].setBackgroundColor(getResources().getColor(R.color.Color_card_ok));
                    }

                }
            });
            listaBtn [3][i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    deshabilitarHora(listaBtn[3][finalI],3,finalI);
                    listaBtn[3][finalI].setBackgroundColor(getResources().getColor(R.color.Color_card_notok));
                    return true;
                }
            });

        }
        actualizarArrays(vistaGridFT);

        ViewGroupLayout vistaTot = new ViewGroupLayout(contexto,lyGridBtn);
        vistaTot.setOrientacion(LinearLayoutCompat.HORIZONTAL);

        vistaTot.addTextView(null);
        lunes = vistaTot.addEditMaterialLayout(R.string.lunes);
        martes = vistaTot.addEditMaterialLayout(R.string.martes);
        miercoles = vistaTot.addEditMaterialLayout(R.string.miercoles);
        jueves = vistaTot.addEditMaterialLayout(R.string.jueves);
        viernes = vistaTot.addEditMaterialLayout(R.string.viernes);
        sabado = vistaTot.addEditMaterialLayout(R.string.sabado);
        domingo = vistaTot.addEditMaterialLayout(R.string.domingo);

        lunes.setActivo(false);
        martes.setActivo(false);
        miercoles.setActivo(false);
        jueves.setActivo(false);
        viernes.setActivo(false);
        sabado.setActivo(false);
        domingo.setActivo(false);

        actualizarArrays(vistaTot);

    }

    public void showTimePickerDialogfin(final Button boton, final int fila, final int columna){

        TimePickerFragment newFragment = TimePickerFragment.newInstance
                (hoy(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        long hfinEvento = JavaUtil.sumaHoraMin(JavaUtil.horaALong(hourOfDay,minute));
                        String selectedHour = getTime(hfinEvento);
                        boton.setText(selectedHour);
                        valores = new ContentValues();

                        if (fila == 0){
                            switch (columna){

                                case 1:
                                    setDato(PERFIL_HORAIMLUNES,hfinEvento);
                                    break;
                                case 2:
                                    setDato(PERFIL_HORAIMMARTES,hfinEvento);
                                    break;
                                case 3:
                                    setDato(PERFIL_HORAIMMIERCOLES,hfinEvento);
                                    break;
                                case 4:
                                    setDato(PERFIL_HORAIMJUEVES,hfinEvento);
                                    break;
                                case 5:
                                    setDato(PERFIL_HORAIMVIERNES,hfinEvento);
                                    break;
                                case 6:
                                    setDato(PERFIL_HORAIMSABADO,hfinEvento);
                                    break;
                                case 7:
                                    setDato(PERFIL_HORAIMDOMINGO,hfinEvento);
                                    break;
                            }
                        }else if (fila == 1){
                            switch (columna){

                                case 1:
                                    setDato(PERFIL_HORAFMLUNES,hfinEvento);
                                    break;
                                case 2:
                                    setDato(PERFIL_HORAFMMARTES,hfinEvento);
                                    break;
                                case 3:
                                    setDato(PERFIL_HORAFMMIERCOLES,hfinEvento);
                                    break;
                                case 4:
                                    setDato(PERFIL_HORAFMJUEVES,hfinEvento);
                                    break;
                                case 5:
                                    setDato(PERFIL_HORAFMVIERNES,hfinEvento);
                                    break;
                                case 6:
                                    setDato(PERFIL_HORAFMSABADO,hfinEvento);
                                    break;
                                case 7:
                                    setDato(PERFIL_HORAFMDOMINGO,hfinEvento);
                                    break;
                            }
                        }else if (fila == 2){
                            switch (columna){

                                case 1:
                                    setDato(PERFIL_HORAITLUNES,hfinEvento);
                                    break;
                                case 2:
                                    setDato(PERFIL_HORAITMARTES,hfinEvento);
                                    break;
                                case 3:
                                    setDato(PERFIL_HORAITMIERCOLES,hfinEvento);
                                    break;
                                case 4:
                                    setDato(PERFIL_HORAITJUEVES,hfinEvento);
                                    break;
                                case 5:
                                    setDato(PERFIL_HORAITVIERNES,hfinEvento);
                                    break;
                                case 6:
                                    setDato(PERFIL_HORAITSABADO,hfinEvento);
                                    break;
                                case 7:
                                    setDato(PERFIL_HORAITDOMINGO,hfinEvento);
                                    break;
                            }
                        }else if (fila == 3){
                            switch (columna){

                                case 1:
                                    setDato(PERFIL_HORAFTLUNES,hfinEvento);
                                    break;
                                case 2:
                                    setDato(PERFIL_HORAFTMARTES,hfinEvento);
                                    break;
                                case 3:
                                    setDato(PERFIL_HORAFTMIERCOLES,hfinEvento);
                                    break;
                                case 4:
                                    setDato(PERFIL_HORAFTJUEVES,hfinEvento);
                                    break;
                                case 5:
                                    setDato(PERFIL_HORAFTVIERNES,hfinEvento);
                                    break;
                                case 6:
                                    setDato(PERFIL_HORAFTSABADO,hfinEvento);
                                    break;
                                case 7:
                                    setDato(PERFIL_HORAFTDOMINGO,hfinEvento);
                                    break;
                            }
                        }

                        CRUDutil.actualizarRegistro(modelo,valores);
                        modelo = CRUDutil.updateModelo(modelo);
                        calcularHorasTotales();

                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(),"timePicker");

    }

    private void deshabilitarHora(final Button boton, final int fila, final int columna){

        boton.setText(getTime(0));
        long hfinEvento = -1;
        valores = new ContentValues();

        if (fila == 0){
            switch (columna){

                case 1:
                    setDato(PERFIL_HORAIMLUNES,hfinEvento);
                    break;
                case 2:
                    setDato(PERFIL_HORAIMMARTES,hfinEvento);
                    break;
                case 3:
                    setDato(PERFIL_HORAIMMIERCOLES,hfinEvento);
                    break;
                case 4:
                    setDato(PERFIL_HORAIMJUEVES,hfinEvento);
                    break;
                case 5:
                    setDato(PERFIL_HORAIMVIERNES,hfinEvento);
                    break;
                case 6:
                    setDato(PERFIL_HORAIMSABADO,hfinEvento);
                    break;
                case 7:
                    setDato(PERFIL_HORAIMDOMINGO,hfinEvento);
                    break;
            }
        }else if (fila == 1){
            switch (columna){

                case 1:
                    setDato(PERFIL_HORAFMLUNES,hfinEvento);
                    break;
                case 2:
                    setDato(PERFIL_HORAFMMARTES,hfinEvento);
                    break;
                case 3:
                    setDato(PERFIL_HORAFMMIERCOLES,hfinEvento);
                    break;
                case 4:
                    setDato(PERFIL_HORAFMJUEVES,hfinEvento);
                    break;
                case 5:
                    setDato(PERFIL_HORAFMVIERNES,hfinEvento);
                    break;
                case 6:
                    setDato(PERFIL_HORAFMSABADO,hfinEvento);
                    break;
                case 7:
                    setDato(PERFIL_HORAFMDOMINGO,hfinEvento);
                    break;
            }
        }else if (fila == 2){
            switch (columna){

                case 1:
                    setDato(PERFIL_HORAITLUNES,hfinEvento);
                    break;
                case 2:
                    setDato(PERFIL_HORAITMARTES,hfinEvento);
                    break;
                case 3:
                    setDato(PERFIL_HORAITMIERCOLES,hfinEvento);
                    break;
                case 4:
                    setDato(PERFIL_HORAITJUEVES,hfinEvento);
                    break;
                case 5:
                    setDato(PERFIL_HORAITVIERNES,hfinEvento);
                    break;
                case 6:
                    setDato(PERFIL_HORAITSABADO,hfinEvento);
                    break;
                case 7:
                    setDato(PERFIL_HORAITDOMINGO,hfinEvento);
                    break;
            }
        }else if (fila == 3){
            switch (columna){

                case 1:
                    setDato(PERFIL_HORAFTLUNES,hfinEvento);
                    break;
                case 2:
                    setDato(PERFIL_HORAFTMARTES,hfinEvento);
                    break;
                case 3:
                    setDato(PERFIL_HORAFTMIERCOLES,hfinEvento);
                    break;
                case 4:
                    setDato(PERFIL_HORAFTJUEVES,hfinEvento);
                    break;
                case 5:
                    setDato(PERFIL_HORAFTVIERNES,hfinEvento);
                    break;
                case 6:
                    setDato(PERFIL_HORAFTSABADO,hfinEvento);
                    break;
                case 7:
                    setDato(PERFIL_HORAFTDOMINGO,hfinEvento);
                    break;
            }
        }

        CRUDutil.actualizarRegistro(modelo,valores);
        modelo = CRUDutil.updateModelo(modelo);
        calcularHorasTotales();
    }

    private void calcularHorasTotales(){

        long mlunes=0,mmartes=0,mmiercoles=0,mjueves=0,mviernes=0,msabado=0,mdomingo=0;
        long tlunes=0,tmartes=0,tmiercoles=0,tjueves=0,tviernes=0,tsabado=0,tdomingo=0;

        if (modelo.getLong(PERFIL_HORAFMLUNES)>=0){
            mlunes = Math.abs(modelo.getLong(PERFIL_HORAFMLUNES)-modelo.getLong(PERFIL_HORAIMLUNES));
        }
        if (modelo.getLong(PERFIL_HORAFTLUNES)>=0){
            tlunes = Math.abs(modelo.getLong(PERFIL_HORAFTLUNES)-modelo.getLong(PERFIL_HORAITLUNES));
        }
        int horasLunes = Math.round((mlunes+tlunes)/HORASLONG);

        if (modelo.getLong(PERFIL_HORAFMMARTES)>=0){
            mmartes = Math.abs(modelo.getLong(PERFIL_HORAFMMARTES)-modelo.getLong(PERFIL_HORAIMMARTES));
        }
        if (modelo.getLong(PERFIL_HORAFTMARTES)>=0){
            tmartes = Math.abs(modelo.getLong(PERFIL_HORAFTMARTES)-modelo.getLong(PERFIL_HORAITMARTES));
        }
        int horasMartes = Math.round((mmartes+tmartes)/HORASLONG);

        if (modelo.getLong(PERFIL_HORAFMMIERCOLES)>=0){
            mmiercoles = Math.abs(modelo.getLong(PERFIL_HORAFMMIERCOLES)-modelo.getLong(PERFIL_HORAIMMIERCOLES));
        }
        if (modelo.getLong(PERFIL_HORAFTMIERCOLES)>=0){
            tmiercoles = Math.abs(modelo.getLong(PERFIL_HORAFTMIERCOLES)-modelo.getLong(PERFIL_HORAITMIERCOLES));
        }
        int horasMiercoles = Math.round((mmiercoles+tmiercoles)/HORASLONG);

        if (modelo.getLong(PERFIL_HORAFMJUEVES)>=0){
            mjueves = Math.abs(modelo.getLong(PERFIL_HORAFMJUEVES)-modelo.getLong(PERFIL_HORAIMJUEVES));
        }
        if (modelo.getLong(PERFIL_HORAFTJUEVES)>=0){
            tjueves = Math.abs(modelo.getLong(PERFIL_HORAFTJUEVES)-modelo.getLong(PERFIL_HORAITJUEVES));
        }
        int horasJueves = Math.round((mjueves+tjueves)/HORASLONG);

        if (modelo.getLong(PERFIL_HORAFMVIERNES)>=0){
            mviernes = Math.abs(modelo.getLong(PERFIL_HORAFMVIERNES)-modelo.getLong(PERFIL_HORAIMVIERNES));
        }
        if (modelo.getLong(PERFIL_HORAFTVIERNES)>=0){
            tviernes = Math.abs(modelo.getLong(PERFIL_HORAFTVIERNES)-modelo.getLong(PERFIL_HORAITVIERNES));
        }
        int horasViernes = Math.round((mviernes+tviernes)/HORASLONG);

        if (modelo.getLong(PERFIL_HORAFMSABADO)>=0){
            msabado = Math.abs(modelo.getLong(PERFIL_HORAFMSABADO)-modelo.getLong(PERFIL_HORAIMSABADO));
        }
        if (modelo.getLong(PERFIL_HORAFTSABADO)>=0){
            tsabado = Math.abs(modelo.getLong(PERFIL_HORAFTSABADO)-modelo.getLong(PERFIL_HORAITSABADO));
        }
        int horasSabado = Math.round((msabado+tsabado)/HORASLONG);

        if (modelo.getLong(PERFIL_HORAFMDOMINGO)>=0){
            mdomingo = Math.abs(modelo.getLong(PERFIL_HORAFMDOMINGO)-modelo.getLong(PERFIL_HORAIMDOMINGO));
        }
        if (modelo.getLong(PERFIL_HORAFTDOMINGO)>=0){
            tdomingo = Math.abs(modelo.getLong(PERFIL_HORAFTDOMINGO)-modelo.getLong(PERFIL_HORAITDOMINGO));
        }
        int horasDomingo = Math.round((mdomingo+tdomingo)/HORASLONG);

        valores = new ContentValues();

        setDato(PERFIL_HORASLUNES,horasLunes);
        setDato(PERFIL_HORASMARTES,horasMartes);
        setDato(PERFIL_HORASMIERCOLES,horasMiercoles);
        setDato(PERFIL_HORASJUEVES,horasJueves);
        setDato(PERFIL_HORASVIERNES,horasViernes);
        setDato(PERFIL_HORASSABADO,horasSabado);
        setDato(PERFIL_HORASDOMINGO,horasDomingo);

        lunes.setText(String.valueOf(horasLunes));
        martes.setText(String.valueOf(horasMartes));
        miercoles.setText(String.valueOf(horasMiercoles));
        jueves.setText(String.valueOf(horasJueves));
        viernes.setText(String.valueOf(horasViernes));
        sabado.setText(String.valueOf(horasSabado));
        domingo.setText(String.valueOf(horasDomingo));


        CRUDutil.actualizarRegistro(modelo,valores);
        modelo = CRUDutil.updateModelo(modelo);


    }


    @Override
    protected void setNuevo() {

        activo.setVisibility(View.GONE);
        btnperfilact.setVisibility(View.GONE);

    }

    @Override
    protected boolean update() {

        super.update();

        new Interactor.Calculos.Tareafechas().execute();

        Interactor.setNamefdef();

        return true;

    }

    @Override
    protected boolean delete() {


        if (!modelo.getString(PERFIL_NOMBRE).equals(Interactor.perfila)) {
            super.delete();
            ConsultaBD.deleteRegistro(tabla, id);
        }else{
            Toast.makeText(getContext(),"No se puede borrar el modelo setActivo", Toast.LENGTH_SHORT).show();
        }

        return true;

    }


    @Override
    protected void setContenedor() {

        ConsultaBD.putDato(valores,campos,PERFIL_NOMBRE,nombre.getText().toString());
        ConsultaBD.putDato(valores,campos,PERFIL_DESCRIPCION,descripcion.getText().toString());
        ConsultaBD.putDato(valores, campos, PERFIL_HORASLUNES, JavaUtil.comprobarInteger(lunes.getText().toString()));
        ConsultaBD.putDato(valores, campos, PERFIL_HORASMARTES, JavaUtil.comprobarInteger(martes.getText().toString()));
        ConsultaBD.putDato(valores, campos, PERFIL_HORASMIERCOLES, JavaUtil.comprobarInteger(miercoles.getText().toString()));
        ConsultaBD.putDato(valores, campos, PERFIL_HORASJUEVES, JavaUtil.comprobarInteger(jueves.getText().toString()));
        ConsultaBD.putDato(valores, campos, PERFIL_HORASVIERNES, JavaUtil.comprobarInteger(viernes.getText().toString()));
        ConsultaBD.putDato(valores, campos, PERFIL_HORASSABADO, JavaUtil.comprobarInteger(sabado.getText().toString()));
        ConsultaBD.putDato(valores, campos, PERFIL_HORASDOMINGO, JavaUtil.comprobarInteger(domingo.getText().toString()));
        ConsultaBD.putDato(valores, campos, PERFIL_VACACIONES, JavaUtil.comprobarInteger(vacaciones.getText().toString()));
        ConsultaBD.putDato(valores, campos, PERFIL_SUELDO, JavaUtil.comprobarDouble(sueldo.getText().toString()));


    }

    protected void cambiarFragment(){

        super.cambiarFragment();

    }

    @Override
    protected void setcambioFragment() {

    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {


        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<Modelo> entrada) {

            TextView nombre,descripcion;
            CardView card;

            nombre = itemView.findViewById(R.id.tvnomlperfil);
            descripcion = itemView.findViewById(R.id.tvdesclperfil);
            card = itemView.findViewById(R.id.cardlperfil);

            nombre.setText(entrada.get(posicion).getString(PERFIL_NOMBRE));
            descripcion.setText(entrada.get(posicion).getString(PERFIL_DESCRIPCION));

            if (entrada.get(posicion).getString(PERFIL_NOMBRE).equals(Interactor.perfila)) {

                card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_ok));
            }

            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView nombre,descripcion;
        CardView card;

        public ViewHolderRV(View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.tvnomlperfil);
            descripcion = itemView.findViewById(R.id.tvdesclperfil);
            card = itemView.findViewById(R.id.cardlperfil);
        }

        @Override
        public void bind(Modelo modelo) {

            nombre.setText(modelo.getString(PERFIL_NOMBRE));
            descripcion.setText(modelo.getString(PERFIL_DESCRIPCION));

            if (modelo.getString(PERFIL_NOMBRE).equals(Interactor.perfila)) {

                card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_ok));
            }
            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }


    }