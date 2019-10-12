package com.codevsolution.freemarketsapp.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCUD;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.codevsolution.base.javautil.JavaUtil.hoy;
import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.base.sqlite.ConsultaBD.queryList;
import static com.codevsolution.base.sqlite.ConsultaBD.queryObject;
import static com.codevsolution.base.sqlite.ConsultaBD.queryObjectDetalle;
import static com.codevsolution.base.sqlite.ConsultaBD.updateRegistroDetalle;

public class FragmentCUDDetpartidaTrabajo extends FragmentCUD implements Interactor.ConstantesPry,
        ContratoPry.Tablas, Interactor.TiposDetPartida, Interactor.TiposEstados {

    private EditMaterial descripcion;
    private EditMaterial etPrecio;
    private EditMaterial etBeneficio;
    private EditMaterial etPrecioFinal;
    private EditMaterial etCantidad;
    private EditMaterial etOrden;
    private TextView tipoDetPartida;
    private TextView etTiempoTotalDetPartida;
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
    private Chronometer chronometer;
    private long countUp;
    private double completada;
    private double tiempo;
    private double tiemporeal;
    private double cantidadPartida;
    private CheckBox partida_completada;
    private boolean trekOnpausa;
    private EditMaterial etCantidadPartida;
    private EditMaterial nombre;
    private String idDetPartida;
    private double cantidad;
    private double precioHora;


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
        if (nn(partida) && partida.getInt(PARTIDA_TIPO_ESTADO)==TNUEVOPRESUP) {
            trabajo = CRUDutil.updateModelo(CAMPOS_TRABAJO,modelo.getString(DETPARTIDA_ID_DETPARTIDA));
        }
        origen = getStringBundle(ORIGEN, "");

        if (partida != null) {
            secuenciaPartida = partida.getInt(PARTIDA_SECUENCIA);
            idProyecto_Partida = partida.getString(PARTIDA_ID_PROYECTO);
            id = partida.getString(PARTIDA_ID_PARTIDA);

        }
        if (origen.equals(INICIO)){
            if (!isOnTimer()){
                startTimer();
            }
        }


        tipo = TIPOTRABAJO;


    }

    public void cronometro() {

        modelo = CRUDutil.updateModelo(campos, id, secuencia);
        contador = modelo.getLong(DETPARTIDA_CONTADOR);
        completada = modelo.getDouble(DETPARTIDA_COMPLETADA);
        cantidadPartida = partida.getDouble(PARTIDA_CANTIDAD) + modelo.getDouble(DETPARTIDA_CANTIDAD);
        tiemporeal = (modelo.getDouble(DETPARTIDA_TIEMPOREAL) * HORASLONG) / 1000;
        tiempo = (modelo.getDouble(DETPARTIDA_TIEMPO) * cantidadPartida * HORASLONG) / 1000;

        System.out.println("contador = " + contador);


        //chronometer.setBase(contador);

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {

                if (contador == 0) {
                    contador = hoy();
                    valores = new ContentValues();
                    putDato(valores, campos, DETPARTIDA_CONTADOR, contador);
                    updateRegistroDetalle(tabla, id, secuencia, valores);
                    modelo = CRUDutil.updateModelo(campos, id, secuencia);
                }
                countUp = (hoy() - contador) / 1000;
                String asText = JavaUtil.relojContador(countUp);
                trek.setText(String.format(Locale.getDefault(), "%s", asText));
                completada = (((tiemporeal * 100) / tiempo) + ((countUp * 100) / tiempo));
                AndroidUtil.bars(contexto, progressBarPartida, progressBarPartida2, false,100, 90, 120, completada,
                        completadaPartida, trek, R.color.Color_contador_ok, R.color.Color_contador_acept,
                        R.color.Color_contador_notok);

                etTiempoTotalDetPartida.setText(String.format(Locale.getDefault(),
                        "%s %s %s %s %s", JavaUtil.getDecimales(((tiemporeal + countUp) / 3600)),
                        getString(R.string.horas), getString(R.string.de),
                        JavaUtil.getDecimales(tiempo / 3600), getString(R.string.horas)));
            }
        });
        //chronometer.start();
    }


    @Override
    protected void setDatos() {

        if (isOnTimer()){
            stopTimer();
            chronometer.stop();
        }

        modelo = CRUDutil.updateModelo(campos,id,secuencia);

        tipo = modelo.getString(DETPARTIDA_TIPO);


        allGone();

        if (partida == null) {
            partida = queryObject(CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA, id, null, IGUAL, null);
        }

        tipoDetPartida.setText(partida.getString(PARTIDA_NOMBRE)+" "+tipo.toUpperCase());

        if (trabajo != null && nn(partida) && partida.getInt(PARTIDA_TIPO_ESTADO)==TNUEVOPRESUP) {
            nombre.setText(trabajo.getString(TRABAJO_NOMBRE));
            descripcion.setText(trabajo.getString(TRABAJO_DESCRIPCION));
            tiempoDet.setText(trabajo.getString(TRABAJO_TIEMPO));
            path = trabajo.getString(TRABAJO_RUTAFOTO);
            idDetPartida = trabajo.getString(TRABAJO_ID_TRABAJO);
            precioHora = Interactor.hora;
        }else {
            nombre.setText(modelo.getString(DETPARTIDA_NOMBRE));
            descripcion.setText(modelo.getString(DETPARTIDA_DESCRIPCION));
            tiempoDet.setText(modelo.getString(DETPARTIDA_TIEMPO));
            precioHora = proyecto.getDouble(PROYECTO_PRECIOHORA);
        }

        //Visualizamos campos comunes
        visible(nombre);
        visible(descripcion);
        visible(imagen);
        visible(tipoDetPartida);
        visible(etPrecio);
        visible(etCantidadPartida);
        visible(tiempoDet);
        visible(etTiempoTotalDetPartida);
        visible(etBeneficio);
        visible(etCantidad);
        visible(etPrecioFinal);
        visible(etOrden);

        //Visualizamos campos dependiendo del tipo de detalle

        visible(btntrek);
        completada = modelo.getDouble(DETPARTIDA_COMPLETADA);
        tiemporeal = (modelo.getDouble(DETPARTIDA_TIEMPOREAL) * HORASLONG) / 1000;

        cantidadPartida = partida.getDouble(PARTIDA_CANTIDAD);
        cantidad = modelo.getDouble(DETPARTIDA_CANTIDAD);
        if (cantidad==0){
            cantidad = 1;
            valores = new ContentValues();
            setDato(DETPARTIDA_CANTIDAD, cantidad);
            CRUDutil.actualizarRegistro(modelo,valores);
            modelo = CRUDutil.updateModelo(modelo);
            System.out.println("modelo.getDouble(DETPARTIDA_CANTIDAD) = " + modelo.getDouble(DETPARTIDA_CANTIDAD));

        }
        etCantidad.setText(JavaUtil.getDecimales(cantidad));
        etOrden.setText(String.valueOf(modelo.getInt(DETPARTIDA_ORDEN)));
        int orden = 0;
        if (modelo.getInt(DETPARTIDA_ORDEN)==0){
            ListaModelo lista = CRUDutil.setListaModelo(CAMPOS_DETPARTIDA,DETPARTIDA_ID_PARTIDA,id,IGUAL);
            for (Modelo detPartida : lista.getLista()) {
                if (detPartida.getInt(DETPARTIDA_ORDEN)>orden){
                    orden = detPartida.getInt(DETPARTIDA_ORDEN);
                }
            }
            valores = new ContentValues();
            putDato(valores,campos,DETPARTIDA_ORDEN,orden+1);
            CRUDutil.actualizarRegistro(modelo,valores);
            modelo = CRUDutil.updateModelo(modelo);
            etOrden.setText(String.valueOf(modelo.getInt(DETPARTIDA_ORDEN)));

        }
        etPrecio.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(DETPARTIDA_TIEMPO) *
                cantidadPartida * cantidad * precioHora));
        etBeneficio.setText(JavaUtil.getDecimales(modelo.getDouble(DETPARTIDA_BENEFICIO)) + " %");
        etPrecioFinal.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(DETPARTIDA_TIEMPO) *
                cantidadPartida * cantidad * precioHora *(1+((modelo.getDouble(DETPARTIDA_BENEFICIO))/100))));
        tiempo = ((modelo.getDouble(DETPARTIDA_TIEMPO) * cantidadPartida * cantidad * HORASLONG)) / 1000;
        etTiempoTotalDetPartida.setText(String.format(Locale.getDefault(),
                "%s %s %s %s %s", JavaUtil.getDecimales(((((tiempo / 100) * completada) + countUp) / 3600)),
                getString(R.string.horas), getString(R.string.de),
                JavaUtil.getDecimales(tiempo / 3600), getString(R.string.horas)));
        etCantidadPartida.setText(String.format(Locale.getDefault(),
                "%s %s", JavaUtil.getDecimales(cantidadPartida), getString(R.string.cant)));
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
                    chronometer.start();
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
            imagen.setImageUri(modelo.getString(DETPARTIDA_RUTAFOTO));
        }

    }

    @Override
    protected void setAcciones() {


        if (partida.getInt(PARTIDA_TIPO_ESTADO) == TNUEVOPRESUP) {

            etBeneficio.setAlCambiarListener(new EditMaterial.AlCambiarListener() {
                @Override
                public void antesCambio(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void cambiando(CharSequence s, int start, int before, int count) {

                    if (timer!=null){
                        timer.cancel();
                    }
                }

                @Override
                public void despuesCambio(Editable s) {

                    etPrecioFinal.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(DETPARTIDA_TIEMPO) *
                            cantidadPartida * cantidad * precioHora
                            *(1 + ((JavaUtil.comprobarDouble(s.toString())) / 100))));

                    final Editable temp = s;
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            activityBase.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (temp.toString().equals("")) {
                                        etBeneficio.setText("0 %");
                                    }

                                    valores = new ContentValues();
                                    setDato(DETPARTIDA_BENEFICIO, JavaUtil.comprobarDouble(etBeneficio.getTexto()));
                                    CRUDutil.actualizarRegistro(modelo,valores);
                                    modelo = CRUDutil.updateModelo(modelo);
                                    Interactor.Calculos.actualizarPartidaProyecto(id);

                                }
                            });


                        }
                    }, 2000);

                }
            });
        }else {
            etBeneficio.setActivo(false);
        }

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
                        chronometer.stop();
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
                        chronometer.start();
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
                        chronometer.stop();

                        int i = updateRegistroDetalle(tabla, id, secuencia, valores);
                        modelo = CRUDutil.updateModelo(campos, id, secuencia);
                        System.out.println("registros actualizados on pausa = " + i);
                        System.out.println("pausa on = " + modelo.getLong(DETPARTIDA_PAUSA));

                    } else if (trekOn) {
                        btntrekPausa.setText(getString(R.string.pausa_trek));
                        modelo = CRUDutil.updateModelo(campos, id, secuencia);
                        contador = modelo.getLong(DETPARTIDA_CONTADOR);
                        long pausa = modelo.getLong(DETPARTIDA_PAUSA);
                        long contadortemp = contador +(hoy() - (pausa));
                        valores = new ContentValues();
                        putDato(valores, campos, DETPARTIDA_PAUSA, 0);
                        putDato(valores, campos, DETPARTIDA_CONTADOR, contadortemp);
                        int i = updateRegistroDetalle(tabla, id, secuencia, valores);
                        modelo = CRUDutil.updateModelo(campos, id, secuencia);
                        System.out.println("registros actualizados on pausa = " + i);
                        System.out.println("pausa off = " + modelo.getLong(DETPARTIDA_PAUSA));

                        cronometro();
                        chronometer.start();
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
                    cantidadPartida = partida.getDouble(PARTIDA_CANTIDAD);
                    tiemporeal = (modelo.getDouble(DETPARTIDA_TIEMPOREAL) * HORASLONG) / 1000;
                    tiempo = (modelo.getDouble(DETPARTIDA_TIEMPO) * cantidadPartida * HORASLONG) / 1000;
                    String asText = JavaUtil.relojContador(countUp);
                    trek.setText(String.format(Locale.getDefault(), "%s", asText));
                    completada = (((tiemporeal * 100) / tiempo) + ((countUp * 100) / tiempo));
                    AndroidUtil.bars(contexto, progressBarPartida, progressBarPartida2, false,100,90, 120, completada,
                            completadaPartida, trek, R.color.Color_contador_ok, R.color.Color_contador_acept,
                            R.color.Color_contador_notok);

                    etTiempoTotalDetPartida.setText(String.format(Locale.getDefault(),
                            "%s %s %s %s %s", JavaUtil.getDecimales(((tiemporeal + countUp) / 3600)),
                            getString(R.string.horas), getString(R.string.de),
                            JavaUtil.getDecimales(tiempo / 3600), getString(R.string.horas)));
                    chronometer.stop();

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

                        new Interactor.Calculos.TareaActualizarTareaAuto().execute(idDetPartida);

                        chronometer.stop();
                        trek.setVisibility(View.GONE);
                        btntrek.setVisibility(View.GONE);
                    } else {

                        valores = new ContentValues();
                        putDato(valores, campos, DETPARTIDA_COMPLETA, 0);
                        putDato(valores, campos, DETPARTIDA_COMPLETADA, completada);
                        updateRegistroDetalle(tabla, id, secuencia, valores);
                        modelo = queryObjectDetalle(campos, id, secuencia);

                        new Interactor.Calculos.TareaActualizarTareaAuto().execute(idDetPartida);

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
        etPrecio = (EditMaterial) ctrl(R.id.etpreciocdetpartida_trb);
        etBeneficio = (EditMaterial) ctrl(R.id.etbenefcdetpartida_trb,DETPARTIDA_BENEFICIO);
        etPrecioFinal = (EditMaterial) ctrl(R.id.etpreciofinalcdetpartida_trb);
        etCantidadPartida = (EditMaterial) ctrl(R.id.etcanttotpartida_trb);
        etCantidad = (EditMaterial) ctrl(R.id.etcantcdetpartida_trb);
        etOrden = (EditMaterial) ctrl(R.id.etordencdetpartida_trb,DETPARTIDA_ORDEN);
        nombre = (EditMaterial) ctrl(R.id.etnombredetpartida_trb);
        tiempoDet = (EditMaterial) ctrl(R.id.ettiempocdetpartida_trb);
        etTiempoTotalDetPartida = (TextView) ctrl(R.id.tvtiempototaldetpartida_trb);
        imagen = (ImagenLayout) ctrl(R.id.imgcdetpartida_trb);
        tipoDetPartida = (TextView) ctrl(R.id.tvtipocdetpartida_trb);
        partida_completada = (CheckBox) ctrl(R.id.cbox_hacer_detpartida_completa_trb);
        btntrek = (Button) ctrl(R.id.btn_trek);
        btntrekPausa = (Button) ctrl(R.id.btn_trek_pausa);
        btntrekReset = (Button) ctrl(R.id.btn_trek_reset);
        progressBarPartida = (ProgressBar) ctrl(R.id.progressBardetpartida_trb);
        progressBarPartida2 = (ProgressBar) ctrl(R.id.progressBar2detpartida);
        trek = (TextView) ctrl(R.id.tvtrek);
        chronometer = (Chronometer) ctrl(R.id.chronodetpartida);
        completadaPartida = (EditMaterial) ctrl(R.id.etcompletadadetpartida_trb);
        etPrecio.setActivo(false);
        etPrecioFinal.setActivo(false);
        etCantidadPartida.setActivo(false);
        nombre.setActivo(false);
        descripcion.setActivo(false);
        etCantidad.setActivo(false);
        tiempoDet.setActivo(false);
        imagen.getImagen().setClickable(false);


    }

    @Override
    protected void setContenedor() {


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
    public void onPause() {
        super.onPause();

        new Interactor.Calculos.TareaActualizarTareaAuto().execute(idDetPartida);
        new Interactor.Calculos.TareaActualizaProy().execute(idProyecto_Partida);

    }

    @Override
    protected void setcambioFragment() {

        if (origen.equals(PARTIDA)) {
            bundle = new Bundle();
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