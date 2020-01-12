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

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.base.time.TimePickerFragment;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.codevsolution.freemarketsapp.sqlite.ConsultaBD;
import com.codevsolution.freemarketsapp.sqlite.ContratoPry;

import java.util.ArrayList;

import static com.codevsolution.base.javautil.JavaUtil.getTime;
import static com.codevsolution.base.javautil.JavaUtil.hoy;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.DEFECTO;
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
    private String perfilAct = AndroidUtil.getSharePreference(contexto,PREFERENCIAS,PERFILACTIVO,DEFECTO);


    private static ConsultaBD consulta = new ConsultaBD();

    public FragmentCRUDPerfil() {
        // Required empty public constructor
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected TipoViewHolder setViewHolder(View view){

        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos) {
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
    protected void setDatos() {

        obtenerDatosBtns();
        nombre.setText(modeloSQL.getString(PERFIL_NOMBRE));
        descripcion.setText(modeloSQL.getString(PERFIL_DESCRIPCION));
        lunes.setText(modeloSQL.getString(PERFIL_HORASLUNES));
        martes.setText(modeloSQL.getString(PERFIL_HORASMARTES));
        miercoles.setText(modeloSQL.getString(PERFIL_HORASMIERCOLES));
        jueves.setText(modeloSQL.getString(PERFIL_HORASJUEVES));
        viernes.setText(modeloSQL.getString(PERFIL_HORASVIERNES));
        sabado.setText(modeloSQL.getString(PERFIL_HORASSABADO));
        domingo.setText(modeloSQL.getString(PERFIL_HORASDOMINGO));
        vacaciones.setText(modeloSQL.getString(PERFIL_VACACIONES));
        sueldo.setText(JavaUtil.formatoMonedaLocal(modeloSQL.getDouble(PERFIL_SUELDO)));
        btndelete.setVisibility(View.VISIBLE);
        btnperfilact.setVisibility(View.VISIBLE);

        if (modeloSQL.getString(PERFIL_NOMBRE) != null && modeloSQL.getString(PERFIL_NOMBRE).equals(perfilAct)) {

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


        lunesIM = modeloSQL.getLong(PERFIL_HORAIMLUNES);
        lunesFM = modeloSQL.getLong(PERFIL_HORAFMLUNES);
        lunesIT = modeloSQL.getLong(PERFIL_HORAITLUNES);
        lunesFT = modeloSQL.getLong(PERFIL_HORAFTLUNES);
        martesIM = modeloSQL.getLong(PERFIL_HORAIMMARTES);
        martesFM = modeloSQL.getLong(PERFIL_HORAFMMARTES);
        martesIT = modeloSQL.getLong(PERFIL_HORAITMARTES);
        martesFT = modeloSQL.getLong(PERFIL_HORAFTMARTES);
        miercolesIM = modeloSQL.getLong(PERFIL_HORAIMMIERCOLES);
        miercolesFM = modeloSQL.getLong(PERFIL_HORAFMMIERCOLES);
        miercolesIT = modeloSQL.getLong(PERFIL_HORAITMIERCOLES);
        miercolesFT = modeloSQL.getLong(PERFIL_HORAFTMIERCOLES);
        juevesIM = modeloSQL.getLong(PERFIL_HORAIMJUEVES);
        juevesFM = modeloSQL.getLong(PERFIL_HORAFMJUEVES);
        juevesIT = modeloSQL.getLong(PERFIL_HORAITJUEVES);
        juevesFT = modeloSQL.getLong(PERFIL_HORAFTJUEVES);
        viernesIM = modeloSQL.getLong(PERFIL_HORAIMVIERNES);
        viernesFM = modeloSQL.getLong(PERFIL_HORAFMVIERNES);
        viernesIT = modeloSQL.getLong(PERFIL_HORAITVIERNES);
        viernesFT = modeloSQL.getLong(PERFIL_HORAFTVIERNES);
        sabadoIM = modeloSQL.getLong(PERFIL_HORAIMSABADO);
        sabadoFM = modeloSQL.getLong(PERFIL_HORAFMSABADO);
        sabadoIT = modeloSQL.getLong(PERFIL_HORAITSABADO);
        sabadoFT = modeloSQL.getLong(PERFIL_HORAFTSABADO);
        domingoIM = modeloSQL.getLong(PERFIL_HORAIMDOMINGO);
        domingoFM = modeloSQL.getLong(PERFIL_HORAFMDOMINGO);
        domingoIT = modeloSQL.getLong(PERFIL_HORAITDOMINGO);
        domingoFT = modeloSQL.getLong(PERFIL_HORAFTDOMINGO);

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

        lunes.setText(String.valueOf(modeloSQL.getInt(PERFIL_HORASLUNES)));
        martes.setText(String.valueOf(modeloSQL.getInt(PERFIL_HORASMARTES)));
        miercoles.setText(String.valueOf(modeloSQL.getInt(PERFIL_HORASMIERCOLES)));
        jueves.setText(String.valueOf(modeloSQL.getInt(PERFIL_HORASJUEVES)));
        viernes.setText(String.valueOf(modeloSQL.getInt(PERFIL_HORASVIERNES)));
        sabado.setText(String.valueOf(modeloSQL.getInt(PERFIL_HORASSABADO)));
        domingo.setText(String.valueOf(modeloSQL.getInt(PERFIL_HORASDOMINGO)));

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

                perfilAct = nombre.getText().toString();
                activo.setVisibility(View.VISIBLE);
                btnperfilact.setVisibility(View.GONE);

                SharedPreferences preferences=getContext().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString(PERFILACTIVO, perfilAct);
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

        listaBtn[0][0] = vistaGridIM.addButtonPrimary("IM", 1);
        listaBtn [0][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contexto, "Hora inicio horario de mañana", Toast.LENGTH_SHORT).show();
            }
        });

        for (int i = 1; i<=7; i++) {

            listaBtn[0][i] = vistaGridIM.addButtonTrans(TimeDateUtil.getTimeString(9 * HORASLONG), 1);
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

        listaBtn[1][0] = vistaGridFM.addButtonPrimary("FM", 1);
        listaBtn [1][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contexto, "Hora fin horario de mañana", Toast.LENGTH_SHORT).show();
            }
        });


        for (int i = 1; i<=7; i++) {

            listaBtn[1][i] = vistaGridFM.addButtonTrans(TimeDateUtil.getTimeString(14 * HORASLONG), 1);
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

        listaBtn[2][0] = vistaGridIT.addButtonPrimary("IT", 1);
        listaBtn [2][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contexto, "Hora inicio horario de tarde", Toast.LENGTH_SHORT).show();
            }
        });


        for (int i = 1; i<=7; i++) {

            listaBtn[2][i] = vistaGridIT.addButtonTrans(TimeDateUtil.getTimeString(16 * HORASLONG), 1);
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

        listaBtn[3][0] = vistaGridFT.addButtonPrimary("FT", 1);
        listaBtn [3][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contexto, "Hora fin horario de tarde", Toast.LENGTH_SHORT).show();
            }
        });


        for (int i = 1; i<=7; i++) {

            listaBtn[3][i] = vistaGridFT.addButtonTrans(TimeDateUtil.getTimeString(19 * HORASLONG), 1);
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
                                    putDato(valores,PERFIL_HORAIMLUNES,hfinEvento);
                                    break;
                                case 2:
                                    putDato(valores,PERFIL_HORAIMMARTES,hfinEvento);
                                    break;
                                case 3:
                                    putDato(valores,PERFIL_HORAIMMIERCOLES,hfinEvento);
                                    break;
                                case 4:
                                    putDato(valores,PERFIL_HORAIMJUEVES,hfinEvento);
                                    break;
                                case 5:
                                    putDato(valores,PERFIL_HORAIMVIERNES,hfinEvento);
                                    break;
                                case 6:
                                    putDato(valores,PERFIL_HORAIMSABADO,hfinEvento);
                                    break;
                                case 7:
                                    putDato(valores,PERFIL_HORAIMDOMINGO,hfinEvento);
                                    break;
                            }
                        }else if (fila == 1){
                            switch (columna){

                                case 1:
                                    putDato(valores,PERFIL_HORAFMLUNES,hfinEvento);
                                    break;
                                case 2:
                                    putDato(valores,PERFIL_HORAFMMARTES,hfinEvento);
                                    break;
                                case 3:
                                    putDato(valores,PERFIL_HORAFMMIERCOLES,hfinEvento);
                                    break;
                                case 4:
                                    putDato(valores,PERFIL_HORAFMJUEVES,hfinEvento);
                                    break;
                                case 5:
                                    putDato(valores,PERFIL_HORAFMVIERNES,hfinEvento);
                                    break;
                                case 6:
                                    putDato(valores,PERFIL_HORAFMSABADO,hfinEvento);
                                    break;
                                case 7:
                                    putDato(valores,PERFIL_HORAFMDOMINGO,hfinEvento);
                                    break;
                            }
                        }else if (fila == 2){
                            switch (columna){

                                case 1:
                                    putDato(valores,PERFIL_HORAITLUNES,hfinEvento);
                                    break;
                                case 2:
                                    putDato(valores,PERFIL_HORAITMARTES,hfinEvento);
                                    break;
                                case 3:
                                    putDato(valores,PERFIL_HORAITMIERCOLES,hfinEvento);
                                    break;
                                case 4:
                                    putDato(valores,PERFIL_HORAITJUEVES,hfinEvento);
                                    break;
                                case 5:
                                    putDato(valores,PERFIL_HORAITVIERNES,hfinEvento);
                                    break;
                                case 6:
                                    putDato(valores,PERFIL_HORAITSABADO,hfinEvento);
                                    break;
                                case 7:
                                    putDato(valores,PERFIL_HORAITDOMINGO,hfinEvento);
                                    break;
                            }
                        }else if (fila == 3){
                            switch (columna){

                                case 1:
                                    putDato(valores,PERFIL_HORAFTLUNES,hfinEvento);
                                    break;
                                case 2:
                                    putDato(valores,PERFIL_HORAFTMARTES,hfinEvento);
                                    break;
                                case 3:
                                    putDato(valores,PERFIL_HORAFTMIERCOLES,hfinEvento);
                                    break;
                                case 4:
                                    putDato(valores,PERFIL_HORAFTJUEVES,hfinEvento);
                                    break;
                                case 5:
                                    putDato(valores,PERFIL_HORAFTVIERNES,hfinEvento);
                                    break;
                                case 6:
                                    putDato(valores,PERFIL_HORAFTSABADO,hfinEvento);
                                    break;
                                case 7:
                                    putDato(valores,PERFIL_HORAFTDOMINGO,hfinEvento);
                                    break;
                            }
                        }

                        actualizarRegistro(modeloSQL, valores);
                        modeloSQL = updateModelo(modeloSQL);
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
                    putDato(valores,PERFIL_HORAIMLUNES,hfinEvento);
                    break;
                case 2:
                    putDato(valores,PERFIL_HORAIMMARTES,hfinEvento);
                    break;
                case 3:
                    putDato(valores,PERFIL_HORAIMMIERCOLES,hfinEvento);
                    break;
                case 4:
                    putDato(valores,PERFIL_HORAIMJUEVES,hfinEvento);
                    break;
                case 5:
                    putDato(valores,PERFIL_HORAIMVIERNES,hfinEvento);
                    break;
                case 6:
                    putDato(valores,PERFIL_HORAIMSABADO,hfinEvento);
                    break;
                case 7:
                    putDato(valores,PERFIL_HORAIMDOMINGO,hfinEvento);
                    break;
            }
        }else if (fila == 1){
            switch (columna){

                case 1:
                    putDato(valores,PERFIL_HORAFMLUNES,hfinEvento);
                    break;
                case 2:
                    putDato(valores,PERFIL_HORAFMMARTES,hfinEvento);
                    break;
                case 3:
                    putDato(valores,PERFIL_HORAFMMIERCOLES,hfinEvento);
                    break;
                case 4:
                    putDato(valores,PERFIL_HORAFMJUEVES,hfinEvento);
                    break;
                case 5:
                    putDato(valores,PERFIL_HORAFMVIERNES,hfinEvento);
                    break;
                case 6:
                    putDato(valores,PERFIL_HORAFMSABADO,hfinEvento);
                    break;
                case 7:
                    putDato(valores,PERFIL_HORAFMDOMINGO,hfinEvento);
                    break;
            }
        }else if (fila == 2){
            switch (columna){

                case 1:
                    putDato(valores,PERFIL_HORAITLUNES,hfinEvento);
                    break;
                case 2:
                    putDato(valores,PERFIL_HORAITMARTES,hfinEvento);
                    break;
                case 3:
                    putDato(valores,PERFIL_HORAITMIERCOLES,hfinEvento);
                    break;
                case 4:
                    putDato(valores,PERFIL_HORAITJUEVES,hfinEvento);
                    break;
                case 5:
                    putDato(valores,PERFIL_HORAITVIERNES,hfinEvento);
                    break;
                case 6:
                    putDato(valores,PERFIL_HORAITSABADO,hfinEvento);
                    break;
                case 7:
                    putDato(valores,PERFIL_HORAITDOMINGO,hfinEvento);
                    break;
            }
        }else if (fila == 3){
            switch (columna){

                case 1:
                    putDato(valores,PERFIL_HORAFTLUNES,hfinEvento);
                    break;
                case 2:
                    putDato(valores,PERFIL_HORAFTMARTES,hfinEvento);
                    break;
                case 3:
                    putDato(valores,PERFIL_HORAFTMIERCOLES,hfinEvento);
                    break;
                case 4:
                    putDato(valores,PERFIL_HORAFTJUEVES,hfinEvento);
                    break;
                case 5:
                    putDato(valores,PERFIL_HORAFTVIERNES,hfinEvento);
                    break;
                case 6:
                    putDato(valores,PERFIL_HORAFTSABADO,hfinEvento);
                    break;
                case 7:
                    putDato(valores,PERFIL_HORAFTDOMINGO,hfinEvento);
                    break;
            }
        }

        actualizarRegistro(modeloSQL, valores);
        modeloSQL = updateModelo(modeloSQL);
        calcularHorasTotales();
    }

    private void calcularHorasTotales(){

        long mlunes=0,mmartes=0,mmiercoles=0,mjueves=0,mviernes=0,msabado=0,mdomingo=0;
        long tlunes=0,tmartes=0,tmiercoles=0,tjueves=0,tviernes=0,tsabado=0,tdomingo=0;

        if (modeloSQL.getLong(PERFIL_HORAFMLUNES) >= 0) {
            mlunes = Math.abs(modeloSQL.getLong(PERFIL_HORAFMLUNES) - modeloSQL.getLong(PERFIL_HORAIMLUNES));
        }
        if (modeloSQL.getLong(PERFIL_HORAFTLUNES) >= 0) {
            tlunes = Math.abs(modeloSQL.getLong(PERFIL_HORAFTLUNES) - modeloSQL.getLong(PERFIL_HORAITLUNES));
        }
        int horasLunes = Math.round((mlunes+tlunes)/HORASLONG);

        if (modeloSQL.getLong(PERFIL_HORAFMMARTES) >= 0) {
            mmartes = Math.abs(modeloSQL.getLong(PERFIL_HORAFMMARTES) - modeloSQL.getLong(PERFIL_HORAIMMARTES));
        }
        if (modeloSQL.getLong(PERFIL_HORAFTMARTES) >= 0) {
            tmartes = Math.abs(modeloSQL.getLong(PERFIL_HORAFTMARTES) - modeloSQL.getLong(PERFIL_HORAITMARTES));
        }
        int horasMartes = Math.round((mmartes+tmartes)/HORASLONG);

        if (modeloSQL.getLong(PERFIL_HORAFMMIERCOLES) >= 0) {
            mmiercoles = Math.abs(modeloSQL.getLong(PERFIL_HORAFMMIERCOLES) - modeloSQL.getLong(PERFIL_HORAIMMIERCOLES));
        }
        if (modeloSQL.getLong(PERFIL_HORAFTMIERCOLES) >= 0) {
            tmiercoles = Math.abs(modeloSQL.getLong(PERFIL_HORAFTMIERCOLES) - modeloSQL.getLong(PERFIL_HORAITMIERCOLES));
        }
        int horasMiercoles = Math.round((mmiercoles+tmiercoles)/HORASLONG);

        if (modeloSQL.getLong(PERFIL_HORAFMJUEVES) >= 0) {
            mjueves = Math.abs(modeloSQL.getLong(PERFIL_HORAFMJUEVES) - modeloSQL.getLong(PERFIL_HORAIMJUEVES));
        }
        if (modeloSQL.getLong(PERFIL_HORAFTJUEVES) >= 0) {
            tjueves = Math.abs(modeloSQL.getLong(PERFIL_HORAFTJUEVES) - modeloSQL.getLong(PERFIL_HORAITJUEVES));
        }
        int horasJueves = Math.round((mjueves+tjueves)/HORASLONG);

        if (modeloSQL.getLong(PERFIL_HORAFMVIERNES) >= 0) {
            mviernes = Math.abs(modeloSQL.getLong(PERFIL_HORAFMVIERNES) - modeloSQL.getLong(PERFIL_HORAIMVIERNES));
        }
        if (modeloSQL.getLong(PERFIL_HORAFTVIERNES) >= 0) {
            tviernes = Math.abs(modeloSQL.getLong(PERFIL_HORAFTVIERNES) - modeloSQL.getLong(PERFIL_HORAITVIERNES));
        }
        int horasViernes = Math.round((mviernes+tviernes)/HORASLONG);

        if (modeloSQL.getLong(PERFIL_HORAFMSABADO) >= 0) {
            msabado = Math.abs(modeloSQL.getLong(PERFIL_HORAFMSABADO) - modeloSQL.getLong(PERFIL_HORAIMSABADO));
        }
        if (modeloSQL.getLong(PERFIL_HORAFTSABADO) >= 0) {
            tsabado = Math.abs(modeloSQL.getLong(PERFIL_HORAFTSABADO) - modeloSQL.getLong(PERFIL_HORAITSABADO));
        }
        int horasSabado = Math.round((msabado+tsabado)/HORASLONG);

        if (modeloSQL.getLong(PERFIL_HORAFMDOMINGO) >= 0) {
            mdomingo = Math.abs(modeloSQL.getLong(PERFIL_HORAFMDOMINGO) - modeloSQL.getLong(PERFIL_HORAIMDOMINGO));
        }
        if (modeloSQL.getLong(PERFIL_HORAFTDOMINGO) >= 0) {
            tdomingo = Math.abs(modeloSQL.getLong(PERFIL_HORAFTDOMINGO) - modeloSQL.getLong(PERFIL_HORAITDOMINGO));
        }
        int horasDomingo = Math.round((mdomingo+tdomingo)/HORASLONG);

        valores = new ContentValues();

        putDato(valores,PERFIL_HORASLUNES,horasLunes);
        putDato(valores,PERFIL_HORASMARTES,horasMartes);
        putDato(valores,PERFIL_HORASMIERCOLES,horasMiercoles);
        putDato(valores,PERFIL_HORASJUEVES,horasJueves);
        putDato(valores,PERFIL_HORASVIERNES,horasViernes);
        putDato(valores,PERFIL_HORASSABADO,horasSabado);
        putDato(valores,PERFIL_HORASDOMINGO,horasDomingo);

        lunes.setText(String.valueOf(horasLunes));
        martes.setText(String.valueOf(horasMartes));
        miercoles.setText(String.valueOf(horasMiercoles));
        jueves.setText(String.valueOf(horasJueves));
        viernes.setText(String.valueOf(horasViernes));
        sabado.setText(String.valueOf(horasSabado));
        domingo.setText(String.valueOf(horasDomingo));


        actualizarRegistro(modeloSQL, valores);
        modeloSQL = updateModelo(modeloSQL);


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


        if (!modeloSQL.getString(PERFIL_NOMBRE).equals(perfilAct)) {
            super.delete();
            borrarRegistro(tabla, id);
        }else{
            Toast.makeText(getContext(), "No se puede borrar el modeloSQL setActivo", Toast.LENGTH_SHORT).show();
        }

        return true;

    }


    @Override
    protected void setContenedor() {

        putDato(valores,PERFIL_NOMBRE,nombre.getText().toString());
        putDato(valores,PERFIL_DESCRIPCION,descripcion.getText().toString());
        putDato(valores, PERFIL_HORASLUNES, JavaUtil.comprobarInteger(lunes.getText().toString()));
        putDato(valores, PERFIL_HORASMARTES, JavaUtil.comprobarInteger(martes.getText().toString()));
        putDato(valores, PERFIL_HORASMIERCOLES, JavaUtil.comprobarInteger(miercoles.getText().toString()));
        putDato(valores, PERFIL_HORASJUEVES, JavaUtil.comprobarInteger(jueves.getText().toString()));
        putDato(valores, PERFIL_HORASVIERNES, JavaUtil.comprobarInteger(viernes.getText().toString()));
        putDato(valores, PERFIL_HORASSABADO, JavaUtil.comprobarInteger(sabado.getText().toString()));
        putDato(valores, PERFIL_HORASDOMINGO, JavaUtil.comprobarInteger(domingo.getText().toString()));
        putDato(valores, PERFIL_VACACIONES, JavaUtil.comprobarInteger(vacaciones.getText().toString()));
        putDato(valores, PERFIL_SUELDO, JavaUtil.comprobarDouble(sueldo.getText().toString()));


    }

    protected void cambiarFragment(){

        super.cambiarFragment();

    }

    @Override
    protected void setcambioFragment() {

    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {


        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<ModeloSQL> entrada) {

            TextView nombre,descripcion;
            CardView card;

            nombre = itemView.findViewById(R.id.tvnomlperfil);
            descripcion = itemView.findViewById(R.id.tvdesclperfil);
            card = itemView.findViewById(R.id.cardlperfil);

            nombre.setText(entrada.get(posicion).getString(PERFIL_NOMBRE));
            descripcion.setText(entrada.get(posicion).getString(PERFIL_DESCRIPCION));

            if (entrada.get(posicion).getString(PERFIL_NOMBRE).equals(perfilAct)) {

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
        public void bind(ModeloSQL modeloSQL) {

            nombre.setText(modeloSQL.getString(PERFIL_NOMBRE));
            descripcion.setText(modeloSQL.getString(PERFIL_DESCRIPCION));
            if (modeloSQL.getString(PERFIL_NOMBRE).equals(perfilAct)) {

                card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_ok));
            }
            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }


    }