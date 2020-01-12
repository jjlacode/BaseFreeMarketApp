package com.codevsolution.base.time.calendar;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.codevsolution.base.android.FragmentGrid;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.animation.OneFrameLayout;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.TimeDateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SemCalBase extends FragmentGrid implements ContratoPry.Tablas {

    protected long im = 0;
    protected long fm = 0;
    protected long it = 0;
    protected long ft = 0;

    private long imDef = 9;
    private long fmDef = 14;
    private long itDef = 16;
    private long ftDef = 19;

    protected long imLunes = imDef;
    protected long imMartes = imDef;
    protected long imMiercoles = imDef;
    protected long imJueves = imDef;
    protected long imViernes = imDef;
    protected long imSabado = imDef;
    protected long imDomingo = imDef;

    protected long itLunes = itDef;
    protected long itMartes = itDef;
    protected long itMiercoles = itDef;
    protected long itJueves = itDef;
    protected long itViernes = itDef;
    protected long itSabado = itDef;
    protected long itDomingo = itDef;

    protected long fmLunes = fmDef;
    protected long fmMartes = fmDef;
    protected long fmMiercoles = fmDef;
    protected long fmJueves = fmDef;
    protected long fmViernes = fmDef;
    protected long fmSabado = fmDef;
    protected long fmDomingo = fmDef;

    protected long ftLunes = ftDef;
    protected long ftMartes = ftDef;
    protected long ftMiercoles = ftDef;
    protected long ftJueves = ftDef;
    protected long ftViernes = ftDef;
    protected long ftSabado = ftDef;
    protected long ftDomingo = ftDef;

    private String MODELO_BTN = "Btn";

    private String BTN_ID = MOD_ID + MODELO_BTN;
    private String BTN_TEXTO = "texto" + MODELO_BTN;
    private String BTN_FECHA = "fecha" + MODELO_BTN;

    private String[] CAMPOS_BTN = {
            MODELO_BTN,
            BTN_ID, JavaUtil.Constantes.STRING,
            BTN_TEXTO, JavaUtil.Constantes.STRING,
            BTN_FECHA, JavaUtil.Constantes.LONG
    };

    private Button[][] botonesHora;
    protected int fila = 0;
    private long fecha = TimeDateUtil.ahora();
    protected long fechaBtn = 0;

    protected int columnasCard;
    protected Button btnDia;
    protected ViewGroupLayout vistaBtn;

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        if (nn(bundle)) {
            fecha = bundle.getLong(FECHA);
        }
    }

    @Override
    protected void setColumnasFilas() {
        super.setColumnasFilas();

        columnas = 1;
        filas = 7;
    }

    protected void setHorarios() {

    }

    @Override
    protected ViewGroupLayout setVistaMain(Context contexto, ViewGroup viewGroup, final Modelo modelo) {

        String texto = "";

        setHorarios();

        switch (modelo.getString(BTN_TEXTO)) {

            case LUNES:
                im = imLunes;
                fm = fmLunes;
                it = itLunes;
                ft = ftLunes;
                fila = 0;
                break;
            case MARTES:
                im = imMartes;
                fm = fmMartes;
                it = itMartes;
                ft = ftMartes;
                fila = 1;
                break;
            case MIERCOLES:
                im = imMiercoles;
                fm = fmMiercoles;
                it = itMiercoles;
                ft = ftMiercoles;
                fila = 2;
                break;
            case JUEVES:
                im = imJueves;
                fm = fmJueves;
                it = itJueves;
                ft = ftJueves;
                fila = 3;
                break;
            case VIERNES:
                im = imViernes;
                fm = fmViernes;
                it = itViernes;
                ft = ftViernes;
                fila = 4;
                break;
            case SABADO:
                im = imSabado;
                fm = fmSabado;
                it = itSabado;
                ft = ftSabado;
                fila = 5;
                break;
            case DOMINGO:
                im = imDomingo;
                fm = fmDomingo;
                it = itDomingo;
                ft = ftDomingo;
                fila = 6;
                break;

        }

        texto = TimeDateUtil.getDateString(modelo.getLong(BTN_FECHA));

        columnasCard = (int) ((((double) (TimeDateUtil.soloHora(getHoraSalida())) / HORASLONG)) -
                ((double) (TimeDateUtil.soloHora(getHoraEntrada())) / HORASLONG));
        botonesHora = new Button[filas][columnasCard];


        vistaBtn = new ViewGroupLayout(contexto, viewGroup);
        btnDia = vistaBtn.addButtonSecondary(texto);


        if (im > 0 || it > 0) {

            ViewGroupLayout vistaHoras = new ViewGroupLayout(contexto, vistaBtn.getViewGroup());
            vistaHoras.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
            for (int i = 0; i < columnasCard; i++) {
                botonesHora[fila][i] = vistaHoras.addButtonTrans(TimeDateUtil.getTimeString(im + (i * HORASLONG)), 1);
                botonesHora[fila][i].setTextSize(sizeText / 2);
                Estilos.setLayoutParams(vistaHoras.getViewGroup(), botonesHora[fila][i], Estilos.Constantes.MATCH_PARENT, sizeTextD * 2, 1, 5);

                if ((fm > 0 && (im + (i * HORASLONG)) < fm) || (ft > 0 && (im + (i * HORASLONG)) < ft && (im + (i * HORASLONG)) >= it)) {
                    botonesHora[fila][i].setBackgroundColor(Estilos.colorSecondary(contexto));
                }
            }
            fechaBtn = modelo.getLong(BTN_FECHA);
            setViewGroupDatos();

        }


        return vistaBtn;
    }

    protected void setViewGroupDatos() {

    }

    private long getHoraEntrada() {

        long horaEntrada = 24 * HORASLONG;

        if (imLunes >= 0 && imLunes < horaEntrada) {
            horaEntrada = imLunes;
        }
        if (itLunes >= 0 && itLunes < horaEntrada) {
            horaEntrada = itLunes;
        }
        if (imMartes >= 0 && imMartes < horaEntrada) {
            horaEntrada = imMartes;
        }
        if (itMartes >= 0 && itMartes < horaEntrada) {
            horaEntrada = itMartes;
        }
        if (imMiercoles >= 0 && imMiercoles < horaEntrada) {
            horaEntrada = imMiercoles;
        }
        if (itMiercoles >= 0 && itMiercoles < horaEntrada) {
            horaEntrada = itMiercoles;
        }
        if (imJueves >= 0 && imJueves < horaEntrada) {
            horaEntrada = imJueves;
        }
        if (itJueves >= 0 && itJueves < horaEntrada) {
            horaEntrada = itJueves;
        }
        if (imViernes >= 0 && imViernes < horaEntrada) {
            horaEntrada = imViernes;
        }
        if (itViernes >= 0 && itViernes < horaEntrada) {
            horaEntrada = itViernes;
        }
        if (imSabado >= 0 && imSabado < horaEntrada) {
            horaEntrada = imSabado;
        }
        if (itSabado >= 0 && itSabado < horaEntrada) {
            horaEntrada = itSabado;
        }
        if (imDomingo >= 0 && imDomingo < horaEntrada) {
            horaEntrada = imDomingo;
        }
        if (itDomingo >= 0 && itDomingo < horaEntrada) {
            horaEntrada = itDomingo;
        }

        return horaEntrada;

    }

    private long getHoraSalida() {

        long horaSalida = 0;

        if (fmLunes >= 0) {
            horaSalida = fmLunes;
        }
        if (ftLunes >= 0 && ftLunes > horaSalida) {
            horaSalida = ftLunes;
        }
        if (fmMartes >= 0 && fmMartes > horaSalida) {
            horaSalida = fmMartes;
        }
        if (ftMartes >= 0 && ftMartes > horaSalida) {
            horaSalida = ftMartes;
        }
        if (fmMiercoles >= 0 && fmMiercoles > horaSalida) {
            horaSalida = fmMiercoles;
        }
        if (ftMiercoles >= 0 && ftMiercoles > horaSalida) {
            horaSalida = ftMiercoles;
        }
        if (fmJueves >= 0 && fmJueves > horaSalida) {
            horaSalida = fmJueves;
        }
        if (ftJueves >= 0 && ftJueves > horaSalida) {
            horaSalida = ftJueves;
        }
        if (fmViernes >= 0 && fmViernes > horaSalida) {
            horaSalida = fmViernes;
        }
        if (ftViernes >= 0 && ftViernes > horaSalida) {
            horaSalida = ftViernes;
        }
        if (fmSabado >= 0 && fmSabado > horaSalida) {
            horaSalida = fmSabado;
        }
        if (ftSabado >= 0 && ftSabado > horaSalida) {
            horaSalida = ftSabado;
        }
        if (fmDomingo >= 0 && fmDomingo > horaSalida) {
            horaSalida = fmDomingo;
        }
        if (ftDomingo >= 0 && ftDomingo > horaSalida) {
            horaSalida = ftDomingo;
        }

        return horaSalida;
    }

    @Override
    protected void setLista() {
        super.setLista();

        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fecha);
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        if (diaSemana == Calendar.SUNDAY) {
            fecha -= 6 * DIASLONG;
        } else {
            fecha -= (diaSemana - 2) * DIASLONG;
        }

        lista = new ArrayList<Modelo>();

        lista.add(new Modelo(CAMPOS_BTN, new String[]{null, LUNES, String.valueOf(fecha)}));
        lista.add(new Modelo(CAMPOS_BTN, new String[]{null, MARTES, String.valueOf(fecha + DIASLONG)}));
        lista.add(new Modelo(CAMPOS_BTN, new String[]{null, MIERCOLES, String.valueOf(fecha + (DIASLONG * 2))}));
        lista.add(new Modelo(CAMPOS_BTN, new String[]{null, JUEVES, String.valueOf(fecha + (DIASLONG * 3))}));
        lista.add(new Modelo(CAMPOS_BTN, new String[]{null, VIERNES, String.valueOf(fecha + (DIASLONG * 4))}));
        lista.add(new Modelo(CAMPOS_BTN, new String[]{null, SABADO, String.valueOf(fecha + (DIASLONG * 5))}));
        lista.add(new Modelo(CAMPOS_BTN, new String[]{null, DOMINGO, String.valueOf(fecha + (DIASLONG * 6))}));

    }

    @Override
    protected void onClickRV(View v) {
        super.onClickRV(v);
    }

    @Override
    protected void setInicio() {
        super.setInicio();

        fragment_container.setOnSwipeListener(new OneFrameLayout.OnSwipeListener() {
            @Override
            public void rightSwipe() {
                fecha -= 7 * DIASLONG;
                selector();
            }

            @Override
            public void leftSwipe() {

                fecha += 7 * DIASLONG;
                selector();
            }
        });
    }

    protected void mostrarDialogNuevo(final long fecha, long hora) {

        final CharSequence[] opciones = {Estilos.getString(contexto, "crear_nuevo"),
                Estilos.getString(contexto, "cancelar")};
        final AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle(Estilos.getString(contexto, "nuevo"));
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals(Estilos.getString(contexto, "crear_nuevo"))) {

                    onNew(fecha, hora);

                } else {

                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    protected void onNew(long fecha, long hora) {

    }
}
