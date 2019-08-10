package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.android.AndroidUtil;
import jjlacode.com.freelanceproject.util.android.controls.EditMaterial;
import jjlacode.com.freelanceproject.util.crud.CRUDutil;
import jjlacode.com.freelanceproject.util.crud.FragmentCUD;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.media.MediaUtil;

import static jjlacode.com.freelanceproject.util.JavaUtil.hoy;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.putDato;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.queryList;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.queryObject;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.queryObjectDetalle;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.updateRegistroDetalle;

public class FragmentCUDDetpartidaTrabajo extends FragmentCUD implements CommonPry.Constantes,
        ContratoPry.Tablas, CommonPry.TiposDetPartida, CommonPry.TiposEstados {

    private EditMaterial descripcion;
    private EditMaterial precio;
    private TextView tipoDetPartida;
    private TextView tiempoTotalDetPartida;
    private Button btntrek;
    private Button btntrekPausa;
    private Button btntrekReset;
    private TextView trek;
    private EditMaterial tiempoDet;
    private String tipo;
    private Modelo proyecto;
    private Modelo partida;
    private Modelo trabajo;

    private String idProyecto_Partida;
    private int secuenciaPartida;
    private ProgressBar progressBarPartida;
    private ProgressBar progressBarPartida2;
    private EditMaterial completadaPartida;


    private boolean trekOn;
    private long contador;
    private Chronometer timer;
    private long countUp;
    private double completada;
    private double tiempo;
    private double tiemporeal;
    private double cantidadTotal;
    private CheckBox partida_completada;
    private boolean trekOnpausa;
    private EditMaterial cantidadPartida;
    private EditMaterial nombre;
    private String idDetPartida;


    public FragmentCUDDetpartidaTrabajo() {
        // Required empty public constructor
    }

    @Override
    protected void setNuevo() {

    }

    @Override
    protected void setTabla() {

        tabla = TABLA_DETPARTIDA;

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
    protected void setBundle() {

        proyecto = (Modelo) getBundleSerial(PROYECTO);
        partida = (Modelo) getBundleSerial(PARTIDA);
        trabajo = (Modelo) getBundleSerial(TRABAJO);
        origen = getStringBundle(ORIGEN, "");

        if (partida != null) {
            secuenciaPartida = partida.getInt(PARTIDA_SECUENCIA);
            idProyecto_Partida = partida.getString(PARTIDA_ID_PROYECTO);
            id = partida.getString(PARTIDA_ID_PARTIDA);

        }
        tipo = bundle.getString(TIPO);
        if (origen.equals(INICIO)){
            if (!isOnTimer()){
                startTimer();
            }
        }
        if (trabajo != null) {
            copiarTrabajo();
        }

    }

    private void copiarTrabajo() {
        nombre.setText(trabajo.getString(TRABAJO_NOMBRE));
        descripcion.setText(trabajo.getString(TRABAJO_DESCRIPCION));
        tiempoDet.setText(trabajo.getString(TRABAJO_TIEMPO));
        path = trabajo.getString(TRABAJO_RUTAFOTO);
        idDetPartida = trabajo.getString(TRABAJO_ID_TRABAJO);
        tipo = TIPOTRABAJO;
        onUpdate();
        trabajo = null;
    }

    public void cronometro() {

        modelo = CRUDutil.setModelo(campos, id, secuencia);
        contador = modelo.getLong(DETPARTIDA_CONTADOR);
        completada = modelo.getDouble(DETPARTIDA_COMPLETADA);
        cantidadTotal = partida.getDouble(PARTIDA_CANTIDAD);
        tiemporeal = (modelo.getDouble(DETPARTIDA_TIEMPOREAL) * HORASLONG) / 1000;
        tiempo = (modelo.getDouble(DETPARTIDA_TIEMPO) * cantidadTotal * HORASLONG) / 1000;

        System.out.println("contador = " + contador);


        //timer.setBase(contador);

        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {

                if (contador == 0) {
                    contador = hoy();
                    valores = new ContentValues();
                    putDato(valores, campos, DETPARTIDA_CONTADOR, contador);
                    updateRegistroDetalle(tabla, id, secuencia, valores);
                    modelo = CRUDutil.setModelo(campos, id, secuencia);
                }
                countUp = (hoy() - contador) / 1000;
                String asText = JavaUtil.relojContador(countUp);
                trek.setText(String.format(Locale.getDefault(), "%s", asText));
                completada = (((tiemporeal * 100) / tiempo) + ((countUp * 100) / tiempo));
                AndroidUtil.bars(contexto, progressBarPartida, progressBarPartida2, false,100, 90, 120, completada,
                        completadaPartida, trek, R.color.Color_contador_ok, R.color.Color_contador_acept,
                        R.color.Color_contador_notok);

                tiempoTotalDetPartida.setText(String.format(Locale.getDefault(),
                        "%s %s %s %s %s", JavaUtil.getDecimales(((tiemporeal + countUp) / 3600)),
                        getString(R.string.horas), getString(R.string.de),
                        JavaUtil.getDecimales(tiempo / 3600), getString(R.string.horas)));
            }
        });
        //timer.start();
    }


    @Override
    protected void setDatos() {

        if (isOnTimer()){
            stopTimer();
            timer.stop();
        }

        modelo = CRUDutil.setModelo(campos,id,secuencia);

        allGone();
        tipo = modelo.getString(DETPARTIDA_TIPO);
        tipoDetPartida.setText(tipo.toUpperCase());
        nombre.setText(modelo.getString(DETPARTIDA_NOMBRE));
        descripcion.setText(modelo.getString(DETPARTIDA_DESCRIPCION));
        precio.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(DETPARTIDA_PRECIO)));
        tiempoDet.setText(modelo.getString(DETPARTIDA_TIEMPO));

        //Visualizamos campos comunes
        visible(nombre);
        visible(descripcion);
        visible(imagen);
        visible(tipoDetPartida);
        visible(precio);
        visible(cantidadPartida);
        visible(tiempoDet);
        visible(tiempoTotalDetPartida);

        //Visualizamos campos dependiendo del tipo de detalle

                visible(btntrek);
        completada = modelo.getDouble(DETPARTIDA_COMPLETADA);
        tiemporeal = (modelo.getDouble(DETPARTIDA_TIEMPOREAL) * HORASLONG) / 1000;
                if (partida == null) {
                    partida = queryObject(CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA, id, null, IGUAL, null);
                }
                cantidadTotal = partida.getDouble(PARTIDA_CANTIDAD);
                tiempo = ((modelo.getDouble(DETPARTIDA_TIEMPO) * cantidadTotal * HORASLONG)) / 1000;
                tiempoTotalDetPartida.setText(String.format(Locale.getDefault(),
                        "%s %s %s %s %s", JavaUtil.getDecimales(((((tiempo / 100) * completada) + countUp) / 3600)),
                        getString(R.string.horas), getString(R.string.de),
                        JavaUtil.getDecimales(tiempo / 3600), getString(R.string.horas)));
                cantidadPartida.setText(String.format(Locale.getDefault(),
                        "%s %s", JavaUtil.getDecimales(cantidadTotal), getString(R.string.cant)));
                AndroidUtil.bars(contexto, progressBarPartida, progressBarPartida2, false,100,90, 120, completada,
                        completadaPartida, trek, R.color.Color_contador_ok, R.color.Color_contador_acept,
                        R.color.Color_contador_notok);

                if (getTipoEstado(partida.getString(PARTIDA_ID_ESTADO)) <= -1) {//TPRESUPACEPTADO) {

                    progressBarPartida.setVisibility(View.GONE);
                    completadaPartida.setVisibility(View.GONE);
                    partida_completada.setVisibility(View.GONE);
                    btntrek.setVisibility(View.GONE);
                } else {

                    progressBarPartida.setVisibility(View.VISIBLE);
                    completadaPartida.setVisibility(View.VISIBLE);
                    //labelCompletada.setVisibility(View.VISIBLE);
                    partida_completada.setVisibility(View.VISIBLE);

                    if (modelo.getInt(DETPARTIDA_COMPLETA) == 1) {
                        partida_completada.setChecked(true);
                        tiempoDet.setText(JavaUtil.getDecimales(modelo.getDouble(DETPARTIDA_TIEMPOREAL)));
                    } else {
                        partida_completada.setChecked(false);
                    }

                    if (tiempo > 0) {

                        trek.setVisibility(View.VISIBLE);
                        btntrek.setVisibility(View.VISIBLE);

                        if (modelo.getLong(DETPARTIDA_PAUSA) > 0) {

                            btntrek.setText(getString(R.string.stop_trek));
                            btntrekPausa.setVisibility(View.VISIBLE);
                            btntrekPausa.setText(getString(R.string.reanudar_trek));
                            trekOn = true;
                            trekOnpausa = true;
                            long pausa = modelo.getLong(DETPARTIDA_PAUSA);
                            long contador = modelo.getLong(DETPARTIDA_CONTADOR);
                            String asText = JavaUtil.relojContador((pausa - contador) / 1000);
                            trek.setText(String.format(Locale.getDefault(), "%s", asText));

                        } else if (modelo.getLong(DETPARTIDA_CONTADOR) > 0) {
                            btntrek.setText(getString(R.string.stop_trek));
                            btntrekPausa.setVisibility(View.VISIBLE);
                            btntrekPausa.setText(getString(R.string.pausa_trek));
                            //trek.setText(String.format(Locale.getDefault(), "%s %s",
                            //        JavaUtil.getDecimales(((tiemporeal + countUp) / 3600)), getString(R.string.horas)));
                            cronometro();
                            timer.start();
                            trekOn = true;
                            trekOnpausa = false;

                        } else {
                            btntrek.setText(getString(R.string.inicio_trek));
                            trek.setText(String.format(Locale.getDefault(), "%s %s",
                                    JavaUtil.getDecimales(((tiemporeal + countUp) / 3600)), getString(R.string.horas)));
                            trekOn = false;
                            trekOnpausa = false;
                            btntrekPausa.setText(getString(R.string.pausa_trek));

                        }
                    } else {
                        trek.setVisibility(View.GONE);
                        btntrek.setVisibility(View.GONE);
                        trekOn = false;
                        trekOnpausa = false;

                    }
                }


        completadaPartida.setVisibility(View.VISIBLE);
        progressBarPartida.setVisibility(View.VISIBLE);
        completada = modelo.getDouble(DETPARTIDA_COMPLETADA);


        if (modelo.getString(DETPARTIDA_RUTAFOTO) != null) {
            mediaUtil = new MediaUtil(contexto);
            path = modelo.getString(DETPARTIDA_RUTAFOTO);
            mediaUtil.setImageUri(modelo.getString(DETPARTIDA_RUTAFOTO), imagen);
        }

    }

    @Override
    protected void setAcciones() {


            btntrek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (trekOn) {
                        btntrek.setText(getString(R.string.inicio_trek));
                        btntrekPausa.setVisibility(View.GONE);

                        valores = new ContentValues();
                        putDato(valores, campos, DETPARTIDA_COMPLETADA, completada);
                        putDato(valores, campos, DETPARTIDA_TIEMPOREAL, (((tiemporeal + countUp) / 3600)));
                        trek.setText(String.format(Locale.getDefault(), "%s %s",
                                JavaUtil.getDecimales(((tiemporeal + countUp) / 3600)), getString(R.string.horas)));
                        trekOn = false;
                        timer.stop();
                        putDato(valores, campos, DETPARTIDA_CONTADOR, 0);
                        long pausa = modelo.getLong(DETPARTIDA_PAUSA);

                        if (pausa>0) {
                            contador = modelo.getLong(DETPARTIDA_CONTADOR);
                            pausa = modelo.getLong(DETPARTIDA_PAUSA);
                            long contadortemp = contador +(hoy() - (pausa));
                            putDato(valores, campos, DETPARTIDA_PAUSA, 0);
                            putDato(valores, campos, DETPARTIDA_CONTADOR, contadortemp);
                            trekOnpausa = false;
                        }

                        int i = updateRegistroDetalle(tabla, id, secuencia, valores);
                        modelo = queryObjectDetalle(campos, id, secuencia);

                    } else {
                        btntrek.setText(getString(R.string.stop_trek));
                        btntrekPausa.setVisibility(View.VISIBLE);
                        btntrekReset.setVisibility(View.VISIBLE);
                        cronometro();
                        timer.start();
                        trekOn = true;
                    }

                }
            });

            btntrekPausa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (trekOn && !trekOnpausa) {
                        btntrekPausa.setText(getString(R.string.reanudar_trek));

                        valores = new ContentValues();
                        putDato(valores, campos, DETPARTIDA_PAUSA, hoy());
                        putDato(valores, campos, DETPARTIDA_COMPLETADA, completada);
                        trekOnpausa = true;
                        timer.stop();

                        int i = updateRegistroDetalle(tabla, id, secuencia, valores);
                        modelo = CRUDutil.setModelo(campos, id, secuencia);
                        System.out.println("registros actualizados on pausa = " + i);
                        System.out.println("pausa on = " + modelo.getLong(DETPARTIDA_PAUSA));

                    } else if (trekOn) {
                        btntrekPausa.setText(getString(R.string.pausa_trek));
                        modelo = CRUDutil.setModelo(campos, id, secuencia);
                        contador = modelo.getLong(DETPARTIDA_CONTADOR);
                        long pausa = modelo.getLong(DETPARTIDA_PAUSA);
                        long contadortemp = contador +(hoy() - (pausa));
                        valores = new ContentValues();
                        putDato(valores, campos, DETPARTIDA_PAUSA, 0);
                        putDato(valores, campos, DETPARTIDA_CONTADOR, contadortemp);
                        int i = updateRegistroDetalle(tabla, id, secuencia, valores);
                        modelo = CRUDutil.setModelo(campos, id, secuencia);
                        System.out.println("registros actualizados on pausa = " + i);
                        System.out.println("pausa off = " + modelo.getLong(DETPARTIDA_PAUSA));

                        cronometro();
                        timer.start();
                        trekOnpausa = false;
                    }

                }
            });

            btntrekReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btntrek.setText(getString(R.string.inicio_trek));
                    btntrekPausa.setVisibility(View.GONE);

                    valores = new ContentValues();
                    countUp = 0;
                    putDato(valores, campos, DETPARTIDA_COMPLETADA, 0);
                    putDato(valores, campos, DETPARTIDA_TIEMPOREAL, 0);
                    trek.setText(String.format(Locale.getDefault(), "%s %s",
                            JavaUtil.getDecimales(0), getString(R.string.horas)));
                    trekOn = false;

                    putDato(valores, campos, DETPARTIDA_CONTADOR, 0);
                    putDato(valores, campos, DETPARTIDA_PAUSA, 0);
                    int i = updateRegistroDetalle(tabla, id, secuencia, valores);
                    modelo = queryObjectDetalle(campos, id, secuencia);
                    contador = modelo.getLong(DETPARTIDA_CONTADOR);
                    completada = modelo.getDouble(DETPARTIDA_COMPLETADA);
                    cantidadTotal = partida.getDouble(PARTIDA_CANTIDAD);
                    tiemporeal = (modelo.getDouble(DETPARTIDA_TIEMPOREAL) * HORASLONG) / 1000;
                    tiempo = (modelo.getDouble(DETPARTIDA_TIEMPO) * cantidadTotal * HORASLONG) / 1000;
                    String asText = JavaUtil.relojContador(countUp);
                    trek.setText(String.format(Locale.getDefault(), "%s", asText));
                    completada = (((tiemporeal * 100) / tiempo) + ((countUp * 100) / tiempo));
                    AndroidUtil.bars(contexto, progressBarPartida, progressBarPartida2, false,100,90, 120, completada,
                            completadaPartida, trek, R.color.Color_contador_ok, R.color.Color_contador_acept,
                            R.color.Color_contador_notok);

                    tiempoTotalDetPartida.setText(String.format(Locale.getDefault(),
                            "%s %s %s %s %s", JavaUtil.getDecimales(((tiemporeal + countUp) / 3600)),
                            getString(R.string.horas), getString(R.string.de),
                            JavaUtil.getDecimales(tiempo / 3600), getString(R.string.horas)));
                    timer.stop();

                }
            });

            partida_completada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (contador > 0 && isChecked) {

                        valores = new ContentValues();
                        putDato(valores, campos, DETPARTIDA_COMPLETA, 1);
                        putDato(valores, campos, DETPARTIDA_COMPLETADA, completada);
                        putDato(valores, campos, DETPARTIDA_TIEMPOREAL, (((double) (tiemporeal + countUp) / 3600)));
                        putDato(valores, campos, DETPARTIDA_CONTADOR, 0);
                        putDato(valores, campos, DETPARTIDA_PAUSA, 0);
                        updateRegistroDetalle(tabla, id, secuencia, valores);
                        modelo = queryObjectDetalle(campos, id, secuencia);
                        timer.stop();
                        trek.setVisibility(View.GONE);
                        btntrek.setVisibility(View.GONE);
                    } else {

                        trek.setVisibility(View.VISIBLE);
                        btntrek.setVisibility(View.VISIBLE);
                    }
                }

            });

    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.detpartida;
        tituloPlural = tituloSingular;
        tituloNuevo = R.string.nuevo_detalle_partida;
    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_cud_detpartida_trabajo;

    }

    @Override
    protected void setInicio() {

        descripcion = (EditMaterial) ctrl(R.id.etdesccdetpartida_trb);
        precio = (EditMaterial) ctrl(R.id.etpreciocdetpartida_trb);
        cantidadPartida = (EditMaterial) ctrl(R.id.etcanttotpartida_trb);
        nombre = (EditMaterial) ctrl(R.id.etnombredetpartida_trb);
        tiempoDet = (EditMaterial) ctrl(R.id.ettiempocdetpartida_trb);
        tiempoTotalDetPartida = (TextView) ctrl(R.id.tvtiempototaldetpartida_trb);
        imagen = (ImageView) ctrl(R.id.imgcdetpartida_trb);
        tipoDetPartida = (TextView) ctrl(R.id.tvtipocdetpartida_trb);
        partida_completada = (CheckBox) ctrl(R.id.cbox_hacer_detpartida_completa_trb);
        btntrek = (Button) ctrl(R.id.btn_trek);
        btntrekPausa = (Button) ctrl(R.id.btn_trek_pausa);
        btntrekReset = (Button) ctrl(R.id.btn_trek_reset);
        progressBarPartida = (ProgressBar) ctrl(R.id.progressBardetpartida_trb);
        progressBarPartida2 = (ProgressBar) ctrl(R.id.progressBar2detpartida);
        trek = (TextView) ctrl(R.id.tvtrek);
        timer = (Chronometer) ctrl(R.id.chronodetpartida);
        completadaPartida = (EditMaterial) ctrl(R.id.etcompletadadetpartida_trb);
        precio.setActivo(false);
        cantidadPartida.setActivo(false);

    }




    @Override
    protected void setContenedor() {

        setDato(DETPARTIDA_NOMBRE, nombre.getText().toString());
        setDato(DETPARTIDA_DESCRIPCION, descripcion.getText().toString());
        setDato(DETPARTIDA_CANTIDAD, 1);
        if (partida.getInt(PARTIDA_TIPO_ESTADO) <= TPRESUPPENDENTREGA) {
            setDato(DETPARTIDA_PRECIO, (Double.parseDouble(tiempoDet.getText().toString()) *
                    CommonPry.hora * cantidadTotal));
        }
        setDato(DETPARTIDA_RUTAFOTO, path);
        setDato(DETPARTIDA_ID_DETPARTIDA, idDetPartida);
        setDato(DETPARTIDA_ID_PARTIDA, id);
        setDato(DETPARTIDA_TIPO, tipo);


        if (partida_completada.isChecked()) {
            setDato(DETPARTIDA_COMPLETA, 1);
        } else {
            setDato(DETPARTIDA_COMPLETA, 0);

        }
                setDato(DETPARTIDA_TIEMPO, tiempoDet.getText().toString());

        CommonPry.Calculos.actualizarPartidaProyecto(id);

    }

    private int getTipoEstado(String idEstado) {

        if (idEstado != null) {

            ArrayList<Modelo> listaEstados = queryList(CAMPOS_ESTADO, null, null);

            for (Modelo estado : listaEstados) {

                if (estado.getString(ESTADO_ID_ESTADO).equals(idEstado)) {

                    return estado.getInt(ESTADO_TIPOESTADO);
                }

            }
        }
        return 0;
    }

    @Override
    protected boolean update() {
        if (super.update()) {
                new CommonPry.Calculos.TareaActualizarTareaAuto().execute(idDetPartida);
            return true;
        }
        return false;
    }

    @Override
    protected void setcambioFragment() {

        if (origen.equals(PARTIDA)) {
            bundle = new Bundle();
            new CommonPry.Calculos.TareaActualizaProy().execute(idProyecto_Partida);
            partida = queryObjectDetalle(CAMPOS_PARTIDA, idProyecto_Partida, secuenciaPartida);
            proyecto = queryObject(CAMPOS_PROYECTO, idProyecto_Partida);
            bundle.putSerializable(MODELO, partida);
            bundle.putSerializable(PROYECTO, proyecto);
            bundle.putString(ORIGEN, DETPARTIDA);
            bundle.putString(SUBTITULO, subTitulo);
            bundle.putString(CAMPO_ID, id);
            bundle.putInt(CAMPO_SECUENCIA, secuenciaPartida);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaProyecto());
            bundle = null;
        }else if (origen.equals(INICIO)){
            startTimer();
        }

    }

}