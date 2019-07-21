package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jjlacode.com.freelanceproject.model.Categorias;
import jjlacode.com.freelanceproject.model.Proveedores;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.android.AndroidUtil;
import jjlacode.com.freelanceproject.util.android.controls.EditMaterial;
import jjlacode.com.freelanceproject.util.crud.CRUDutil;
import jjlacode.com.freelanceproject.util.crud.FragmentCRUD;
import jjlacode.com.freelanceproject.util.media.MediaUtil;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltro;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.model.ProdProv;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.CommonPry;

import static jjlacode.com.freelanceproject.util.JavaUtil.hoy;
import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.*;

public class FragmentCUDDetpartida extends FragmentCRUD implements CommonPry.Constantes,
        ContratoPry.Tablas, CommonPry.TiposDetPartida, CommonPry.TiposEstados {

    private EditMaterial descripcion;
    private EditMaterial precio;
    private EditMaterial cantidad;
    private EditMaterial descProv;
    private EditMaterial refProv;
    private LinearLayout lyAutoNombre;
    private LinearLayout lyAutoCat;
    private LinearLayout lyAutoProv;
    private TextView tipoDetPartida;
    private TextView tiempoTotalDetPartida;
    private Button btntrek;
    private Button btntrekPausa;
    private Button btntrekReset;
    private TextView trek;
    private AutoCompleteTextView autoNombre;
    private EditMaterial tiempoDet;
    private Button btnNuevaTarea;
    private Button btnNuevoProd;
    private RecyclerView rvDetpartida;
    private String tipo;
    private Modelo proyecto;
    private Modelo partida;
    private ArrayList<Modelo> lista;
    private String idDetPartida;

    private String idProyecto_Partida;
    private int secuenciaPartida;
    //private AdaptadorProdProv mAdapter;
    private ArrayList<ProdProv> listaProdProv;
    private ArrayList<Proveedores> listaProv;
    private ArrayList<Categorias> listaCat;
    private AutoCompleteTextView autoCat;
    private AutoCompleteTextView autoProv;
    private String proveedor = TODOS;
    private String categoria = TODAS;
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
    private AdaptadorProdProv mAdapter;
    private EditMaterial cantidadPartida;
    private EditMaterial nombre;
    private DatabaseReference dbProductos;
    private Query query;


    public FragmentCUDDetpartida() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {

        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroRV setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroRV(context, layoutItem, lista, campos);
    }

    @Override
    protected void setNuevo() {

        autoNombre.setThreshold(3);
        allGone();
        visible(tipoDetPartida);
        visible(lyAutoNombre);
        visible(autoNombre);
        visible(nombre);
        visible(descripcion);
        visible(rvDetpartida);
        tipoDetPartida.setText(tipo.toUpperCase());

        switch (tipo){

            case TIPOTRABAJO:

                visible(tiempoDet);
                break;

            case TIPOPRODUCTO:

                visible(cantidad);
                visible(refProv);
                visible(descProv);
                break;

            case TIPOPRODUCTOPROV:

                visible(cantidad);
                visible(refProv);
                visible(descProv);
                visible(lyAutoCat);
                visible(lyAutoProv);
                visible(autoCat);
                visible(autoProv);
                break;

            case TIPOPARTIDA:

                break;
        }


    }

    @Override
    protected void setTabla() {

        tabla = TABLA_DETPARTIDA;

    }


    @Override
    protected void setBundle() {

        proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
        partida = (Modelo) bundle.getSerializable(TABLA_PARTIDA);

        if (partida != null) {
            secuenciaPartida = partida.getInt(PARTIDA_SECUENCIA);
            idProyecto_Partida = partida.getString(PARTIDA_ID_PROYECTO);
        }
        tipo = bundle.getString(TIPO);
        if (origen.equals(INICIO)){
            if (!isOnTimer()){
                startTimer();
            }
        }

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
        btndelete.setVisibility(View.VISIBLE);
        tipoDetPartida.setText(tipo.toUpperCase());
        nombre.setText(modelo.getString(DETPARTIDA_NOMBRE));
        descripcion.setText(modelo.getString(DETPARTIDA_DESCRIPCION));
        precio.setText(modelo.getString(DETPARTIDA_PRECIO));
        cantidad.setText(modelo.getString(DETPARTIDA_CANTIDAD));
        tiempoDet.setText(modelo.getString(DETPARTIDA_TIEMPO));
        descProv.setText(modelo.getString(DETPARTIDA_DESCUENTOPROVCAT));
        refProv.setText(modelo.getString(DETPARTIDA_REFPROVCAT));

        //Visualizamos campos comunes
        visible(nombre);
        visible(descripcion);
        visible(imagen);
        visible(tipoDetPartida);
        visible(precio);

        //Visualizamos campos dependiendo del tipo de detalle
        switch (tipo){

            case TIPOTRABAJO:

                visible(btntrek);
                tiemporeal = (modelo.getDouble(DETPARTIDA_TIEMPOREAL) * HORASLONG) / 1000;
                if (partida == null) {
                    partida = queryObject(CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA, id, null, IGUAL, null);
                }
                cantidadTotal = partida.getDouble(PARTIDA_CANTIDAD);
                tiempo = ((modelo.getDouble(DETPARTIDA_TIEMPO) * cantidadTotal * HORASLONG)) / 1000;
                tiempoTotalDetPartida.setText(String.format(Locale.getDefault(),
                        "%s %s %s %s %s", JavaUtil.getDecimales(((tiemporeal + countUp) / 3600)),
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

                break;
            case TIPOPRODUCTO:

                visible(cantidad);
                visible(refProv);
                visible(descProv);
                break;
            case TIPOPRODUCTOPROV:

                visible(cantidad);
                visible(refProv);
                visible(descProv);
                visible(lyAutoCat);
                visible(lyAutoProv);
                break;
            case TIPOPARTIDA:

                break;
        }
        completadaPartida.setVisibility(View.VISIBLE);
        progressBarPartida.setVisibility(View.VISIBLE);
        completada = modelo.getDouble(DETPARTIDA_COMPLETADA);


        if (tipo.equals(TIPOPRODUCTOPROV) && modelo.getString(DETPARTIDA_RUTAFOTO) != null) {
            mediaUtil = new MediaUtil(contexto);
            path = modelo.getString(DETPARTIDA_RUTAFOTO);
            setImagenFireStoreCircle(contexto, path, imagen);
        } else if (modelo.getString(DETPARTIDA_RUTAFOTO) != null) {
            mediaUtil = new MediaUtil(contexto);
            path = modelo.getString(DETPARTIDA_RUTAFOTO);
            mediaUtil.setImageUri(modelo.getString(DETPARTIDA_RUTAFOTO), imagen);
        }

    }

    @Override
    protected void setOnCronometro(Chronometer arg0) {

        boolean onTick = setCounterUp(arg0) % 60 == 0;
        if (origen.equals(INICIO) && onTick) {

            actualizarConsultasRV();
            setRv();
            System.out.println("onTick");

        }else if (onTick) {

            super.setOnCronometro(arg0);
        }
    }

    @Override
    protected void setAcciones() {


        if (tipo != null) {
            rv();
            if (mAdapter!=null) {
                mAdapter.startListening();
            }

            setAdaptadorAuto(autoNombre);

            setAdaptadorAutoCat();

            setAdaptadorAutoProv();

            autoNombre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    switch (tipo) {

                        case TIPOTRABAJO:

                            Modelo tarea = (Modelo) autoNombre.getAdapter().getItem(position);
                            idDetPartida = tarea.getString(TRABAJO_ID_TRABAJO);
                            nombre.setText(tarea.getString(TRABAJO_NOMBRE));
                            descripcion.setText(tarea.getString(TRABAJO_DESCRIPCION));
                            tiempoDet.setText(tarea.getString(TRABAJO_TIEMPO));

                            if (tarea.getString(TRABAJO_RUTAFOTO) != null) {
                                imagen.setImageURI(tarea.getUri(TRABAJO_RUTAFOTO));
                                path = tarea.getString(TRABAJO_RUTAFOTO);
                            }

                            autoNombre.setText("");

                            break;

                        case TIPOPRODUCTO:

                            Modelo producto = (Modelo) autoNombre.getAdapter().getItem(position);
                            idDetPartida = producto.getString(PRODUCTO_ID_PRODUCTO);
                            nombre.setText(producto.getString(PRODUCTO_NOMBRE));
                            descripcion.setText(producto.getString(PRODUCTO_DESCRIPCION));
                            precio.setText(producto.getString(PRODUCTO_PRECIO));
                            cantidad.setActivo(true);

                            if (producto.getString(PRODUCTO_RUTAFOTO) != null) {
                                imagen.setImageURI(producto.getUri(PRODUCTO_RUTAFOTO));
                                path = producto.getString(PRODUCTO_RUTAFOTO);
                            }
                            autoNombre.setText("");

                            break;

                        case TIPOPARTIDA:

                            Modelo partida = (Modelo) autoNombre.getAdapter().getItem(position);
                            idDetPartida = partida.getString(PARTIDABASE_ID_PARTIDABASE);
                            nombre.setText(partida.getString(PARTIDABASE_NOMBRE));
                            descripcion.setText(partida.getString(PARTIDABASE_DESCRIPCION));
                            precio.setText(partida.getString(PARTIDABASE_PRECIO));
                            tiempoDet.setText(partida.getString(PARTIDABASE_TIEMPO));
                            if (partida.getString(PARTIDABASE_RUTAFOTO) != null) {
                                imagen.setImageURI(partida.getUri(PARTIDABASE_RUTAFOTO));
                                path = partida.getString(PARTIDABASE_RUTAFOTO);
                            }
                            autoNombre.setText("");

                            break;

                        case TIPOPRODUCTOPROV:

                            ProdProv prodprov = (ProdProv) autoNombre.getAdapter().getItem(position);
                            idDetPartida = prodprov.getId();
                            nombre.setText(prodprov.getNombre());
                            descripcion.setText(prodprov.getDescripcion());
                            precio.setText(String.valueOf(prodprov.getPrecio()));
                            refProv.setText(prodprov.getRefprov());
                            cantidad.setActivo(true);

                            if (prodprov.getRutafoto() != null) {

                                setImagenFireStoreCircle(contexto, prodprov.getRutafoto(), imagen);
                                path = prodprov.getRutafoto();
                            }

                            autoNombre.setText("");

                    }
                }

            });

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
                        //ConsultaBD.putDato(valores, campos, DETPARTIDA_TIEMPOREAL, (((double) (tiemporeal + countUp) / 3600)));
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

    }

    private void nuevaTarea() {

        if (descripcion.getText() != null && tiempoDet.getText() != null && autoNombre.getText() != null) {

            int cont = 0;

            ContentValues valorestarea = new ContentValues();

            putDato(valorestarea, CAMPOS_TRABAJO, TRABAJO_DESCRIPCION, descripcion.getText().toString());
            putDato(valorestarea, CAMPOS_TRABAJO, TRABAJO_TIEMPO, tiempoDet.getText().toString());
            putDato(valorestarea, CAMPOS_TRABAJO, TRABAJO_NOMBRE, autoNombre.getText().toString());
            putDato(valorestarea, CAMPOS_TRABAJO, TRABAJO_RUTAFOTO, path);

            if (lista != null && lista.size() > 0) {

                for (Modelo tarea : lista) {
                    if (tarea.getString(TRABAJO_NOMBRE).equals(autoNombre.getText().toString())) {
                        updateRegistro(TABLA_TRABAJO, tarea.getString(TRABAJO_ID_TRABAJO), valorestarea);
                        Toast.makeText(getContext(), "tarea actualizada", Toast.LENGTH_SHORT).show();
                        cont++;
                    }
                }
            }
            if (cont == 0) {

                try {

                    idDetPartida = idInsertRegistro(TABLA_TRABAJO, valorestarea);

                    Toast.makeText(getContext(), "Nueva tarea guardada", Toast.LENGTH_SHORT).show();

                    rv();

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error al guardar tarea", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void nuevoProducto() {

        if (descripcion.getText() != null && precio.getText() != null && autoNombre.getText() != null) {

            int cont = 0;

            ContentValues valoresprod = new ContentValues();

            putDato(valoresprod, CAMPOS_PRODUCTO, PRODUCTO_DESCRIPCION, descripcion.getText().toString());
            putDato(valoresprod, CAMPOS_PRODUCTO, PRODUCTO_PRECIO, precio.getText().toString());
            putDato(valoresprod, CAMPOS_PRODUCTO, PRODUCTO_NOMBRE, autoNombre.getText().toString());
            putDato(valoresprod, CAMPOS_PRODUCTO, PRODUCTO_RUTAFOTO, path);

            if (lista != null && lista.size() > 0) {

                for (Modelo producto : lista) {
                    if (producto.getString(PRODUCTO_NOMBRE).equals(autoNombre.getText().toString())) {
                        updateRegistro(TABLA_PRODUCTO, producto.getString(PRODUCTO_ID_PRODUCTO), valoresprod);
                        Toast.makeText(getContext(), "producto actualizdo", Toast.LENGTH_SHORT).show();
                        cont++;
                    }
                }
            }
            if (cont == 0) {

                try {


                    idDetPartida = idInsertRegistro(TABLA_PRODUCTO, valoresprod);

                    Toast.makeText(getContext(), "Nuevo producto guardado", Toast.LENGTH_SHORT).show();

                    rv();

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error al guardar producto", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.detpartida;
        tituloPlural = tituloSingular;
        tituloNuevo = R.string.nuevo_detalle_partida;
    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_cud_detpartida;
        layoutItem = R.layout.item_list_detpartida_treking;

    }

    @Override
    protected void setInicio() {

        btnNuevaTarea = (Button) ctrl(R.id.btnntareacdetpartida);
        btnNuevoProd = (Button) ctrl(R.id.btnnprodcdetpartida);
        rvDetpartida = (RecyclerView) ctrl(R.id.rvcdetpartida);
        descripcion = (EditMaterial) ctrl(R.id.etdesccdetpartida);
        precio = (EditMaterial) ctrl(R.id.etpreciocdetpartida);
        cantidad = (EditMaterial) ctrl(R.id.etcantcdetpartida);
        cantidadPartida = (EditMaterial) ctrl(R.id.etcanttotpartida);
        lyAutoNombre = (LinearLayout) ctrl(R.id.layout_autonomdetpartida);
        lyAutoCat = (LinearLayout) ctrl(R.id.layout_autocatdetpartida);
        lyAutoProv = (LinearLayout) ctrl(R.id.layout_autoprovdetpartida);
        autoNombre = (AutoCompleteTextView) ctrl(R.id.autonomdetpartida);
        nombre = (EditMaterial) ctrl(R.id.etnombredetpartida);
        tiempoDet = (EditMaterial) ctrl(R.id.ettiempocdetpartida);
        tiempoTotalDetPartida = (TextView) ctrl(R.id.tvtiempototaldetpartida);
        imagen = (ImageView) ctrl(R.id.imgcdetpartida);
        refProv = (EditMaterial) ctrl(R.id.tvrefprovcdetpartida);
        descProv = (EditMaterial) ctrl(R.id.etporcdesprovcdetpartida);
        tipoDetPartida = (TextView) ctrl(R.id.tvtipocdetpartida);
        autoCat = (AutoCompleteTextView) ctrl(R.id.autocat);
        autoProv = (AutoCompleteTextView) ctrl(R.id.autoprov);
        partida_completada = (CheckBox) ctrl(R.id.cbox_hacer_detpartida_completa);
        btntrek = (Button) ctrl(R.id.btn_trek);
        btntrekPausa = (Button) ctrl(R.id.btn_trek_pausa);
        btntrekReset = (Button) ctrl(R.id.btn_trek_reset);
        progressBarPartida = (ProgressBar) ctrl(R.id.progressBardetpartida);
        progressBarPartida2 = (ProgressBar) ctrl(R.id.progressBar2detpartida);
        trek = (TextView) ctrl(R.id.tvtrek);
        timer = (Chronometer) ctrl(R.id.chronodetpartida);
        completadaPartida = (EditMaterial) ctrl(R.id.etcompletadadetpartida);
        cantidad.setText("1");

    }

    private void rv() {

        mediaUtil = new MediaUtil(contexto);

        switch (tipo) {

            case TIPOTRABAJO:

                rvDetpartida.setLayoutManager(new LinearLayoutManager(getContext()));
                rvDetpartida.setHasFixedSize(true);

                lista = queryList(CAMPOS_TRABAJO);
                AdaptadorTareas adaptadorTareas = new AdaptadorTareas(lista);
                rvDetpartida.setAdapter(adaptadorTareas);
                adaptadorTareas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        idDetPartida = lista.get(rvDetpartida.getChildAdapterPosition(v))
                                .getString(TRABAJO_ID_TRABAJO);
                        Modelo tarea = queryObject(CAMPOS_TRABAJO, idDetPartida);
                        nombre.setText(tarea.getString(TRABAJO_NOMBRE));
                        descripcion.setText(tarea.getString(TRABAJO_DESCRIPCION));
                        tiempoDet.setText(tarea.getString(TRABAJO_TIEMPO));
                        if (tarea.getString(TRABAJO_RUTAFOTO) != null) {
                            path = tarea.getString(TRABAJO_RUTAFOTO);
                            mediaUtil.setImageUriCircle(path, imagen);
                        }
                    }
                });
                break;

            case TIPOPRODUCTO:

                rvDetpartida.setLayoutManager(new LinearLayoutManager(getContext()));
                rvDetpartida.setHasFixedSize(true);

                lista = queryList(CAMPOS_PRODUCTO);
                AdaptadorProducto adaptadorProducto = new AdaptadorProducto(lista);
                rvDetpartida.setAdapter(adaptadorProducto);
                adaptadorProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        idDetPartida = lista.get(rvDetpartida.getChildAdapterPosition(v))
                                .getString(PRODUCTO_ID_PRODUCTO);
                        Modelo producto = queryObject(CAMPOS_PRODUCTO, idDetPartida);
                        nombre.setText(producto.getString(PRODUCTO_NOMBRE));
                        descripcion.setText(producto.getString(PRODUCTO_DESCRIPCION));
                        precio.setText(producto.getString(PRODUCTO_PRECIO));

                        if (producto.getString(PRODUCTO_RUTAFOTO) != null) {
                            path = producto.getString(PRODUCTO_RUTAFOTO);
                            mediaUtil.setImageUriCircle(path, imagen);
                        }

                    }
                });

                break;

            case TIPOPARTIDA:

                rvDetpartida.setLayoutManager(new LinearLayoutManager(getContext()));
                rvDetpartida.setHasFixedSize(true);
                lista = queryList(CAMPOS_PARTIDABASE);
                AdaptadorPartida adaptadorPartida = new AdaptadorPartida(lista);
                rvDetpartida.setAdapter(adaptadorPartida);
                adaptadorPartida.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        idDetPartida = lista.get(rvDetpartida.getChildAdapterPosition(v))
                                .getString(PARTIDABASE_ID_PARTIDABASE);
                        Modelo partidabase = lista.get(rvDetpartida.getChildAdapterPosition(v));
                        nombre.setText(partidabase.getString(PARTIDABASE_NOMBRE));
                        descripcion.setText(partidabase.getString(PARTIDABASE_DESCRIPCION));
                        precio.setText(partidabase.getString(PARTIDABASE_PRECIO));

                        if (partidabase.getString(PARTIDABASE_RUTAFOTO) != null) {
                            path = partidabase.getString(PARTIDABASE_RUTAFOTO);
                            mediaUtil.setImageUriCircle(path, imagen);
                        }

                    }
                });

                break;

            case TIPOPRODUCTOPROV:


                listaProdProv = new ArrayList<>();

                dbProductos =
                        FirebaseDatabase.getInstance().getReference();

                query = dbProductos.child("productos");



                ValueEventListener eventListenerProd = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ProdProv prodProv = ds.getValue(ProdProv.class);
                            prodProv.setId(ds.getRef().getKey());
                            listaProdProv.add(prodProv);
                        }
                        ArrayList<ProdProv> prodProvtemp = new ArrayList<>();

                        for (ProdProv prodProv : listaProdProv) {

                            //if ((prodProv.getCategoria().equals(categoria) || categoria.equals(TODAS)) &&
                            //        (prodProv.getProveedor().equals(proveedor)) || proveedor.equals(TODOS)) {
                            prodProvtemp.add(prodProv);
                            //}

                        }
                        System.out.println("lista provtmp: " + prodProvtemp.size());

                        listaProdProv.clear();
                        listaProdProv.addAll(prodProvtemp);
                        prodProvtemp.clear();

                        System.out.println("lista prov: " + listaProdProv.size());
                        AdaptadorProveedor provAdapter = new AdaptadorProveedor(listaProdProv);

                        rvDetpartida.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvDetpartida.setHasFixedSize(true);

                        rvDetpartida.setAdapter(provAdapter);

                        provAdapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                int i = rvDetpartida.getChildViewHolder(v).getAdapterPosition();
                                idDetPartida = listaProdProv.get(i).getId();
                                System.out.println("idDetPartida = " + idDetPartida);

                                refProv.setText(listaProdProv.get(i).getRefprov());
                                autoNombre.setText(listaProdProv.get(i).getNombre());
                                descripcion.setText(listaProdProv.get(i).getDescripcion());
                                precio.setText(String.valueOf(listaProdProv.get(i).getPrecio()));

                                path = listaProdProv.get(i).getRutafoto();
                                if (path != null) {
                                    setImagenFireStoreCircle(contexto, path, imagen);
                                }


                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                };

                query.addValueEventListener(eventListenerProd);


        }

    }


    //Stop Listening Adapter
    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter!=null) {
            mAdapter.stopListening();
        }
    }


    @Override
    protected void setContenedor() {

        setDato(DETPARTIDA_NOMBRE, nombre.getText().toString());
        setDato(DETPARTIDA_DESCRIPCION, descripcion.getText().toString());
        if (tipo.equals(TIPOTRABAJO)) {
            setDato(DETPARTIDA_CANTIDAD, 1);

        } else {
            setDato(DETPARTIDA_CANTIDAD, cantidad.getText().toString());
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

        switch (tipo) {

            case TIPOTRABAJO:

                nuevaTarea();
                setDato(DETPARTIDA_TIEMPO, tiempoDet.getText().toString());

                break;

            case TIPOPRODUCTO:

                nuevoProducto();
                setDato(DETPARTIDA_PRECIO, precio.getText().toString());

                break;

            case TIPOPARTIDA:

                setDato(DETPARTIDABASE_TIEMPO, tiempoDet.getText().toString());
                setDato(DETPARTIDABASE_PRECIO, precio.getText().toString());

                break;


            case TIPOPRODUCTOPROV:

                setDato(DETPARTIDA_PRECIO, precio.getText().toString());
                setDato(DETPARTIDA_DESCUENTOPROVCAT, descProv.getText().toString());
                setDato(DETPARTIDA_REFPROVCAT, refProv.getText().toString());
                break;

        }

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
            if (tipo.equals(TIPOTRABAJO)) {
                new CommonPry.Calculos.TareaActualizarTareaAuto().execute(idDetPartida);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void setcambioFragment() {

        if (origen.equals(PARTIDA)) {
            bundle = new Bundle();
            new CommonPry.Calculos.TareaActualizaProy().execute(idProyecto_Partida);
            partida = queryObjectDetalle(CAMPOS_PARTIDA, idProyecto_Partida, secuenciaPartida);
            bundle.putSerializable(MODELO, partida);
            bundle.putSerializable(PROYECTO, proyecto);
            bundle.putString(TIPO, tipo);
            bundle.putString(ORIGEN, DETPARTIDA);
            bundle.putString(SUBTITULO, subTitulo);
            bundle.putString(CAMPO_ID, idProyecto_Partida);
            bundle.putInt(CAMPO_SECUENCIA, secuenciaPartida);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaProyecto());
            bundle = null;
        }else if (origen.equals(INICIO)){
            startTimer();
        }

    }

    private void setAdaptadorAuto(AutoCompleteTextView autoCompleteTextView) {


        switch (tipo) {

            case TIPOTRABAJO:

                autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(getContext(),
                        R.layout.item_list_trabajo, lista, CAMPOS_TRABAJO) {

                    @Override
                    public void onEntrada(Modelo entrada, View view) {

                        ImageView imagenTarea = view.findViewById(R.id.imgltarea);
                        TextView nombreTarea = view.findViewById(R.id.tvnomltarea);
                        TextView descTarea = view.findViewById(R.id.tvdescripcionltareas);
                        TextView tiempoTarea = view.findViewById(R.id.tvtiempoltareas);


                        nombreTarea.setText(entrada.getString(TRABAJO_NOMBRE));
                        descTarea.setText(entrada.getString(TRABAJO_DESCRIPCION));
                        tiempoTarea.setText(entrada.getString(TRABAJO_TIEMPO));

                        if (entrada.getString(TRABAJO_RUTAFOTO) != null) {
                            imagenTarea.setImageURI(entrada.getUri(TRABAJO_RUTAFOTO));
                        }

                    }

                });
                break;

            case TIPOPRODUCTO:


                autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(getContext(),
                        R.layout.item_list_producto, lista, CAMPOS_PRODUCTO) {

                    @Override
                    public void onEntrada(Modelo entrada, View view) {

                        ImageView imagen = view.findViewById(R.id.imglproductos);
                        TextView nombre = view.findViewById(R.id.tvnombrelproductos);
                        TextView descripcion = view.findViewById(R.id.tvdescripcionlproductos);
                        TextView importe = view.findViewById(R.id.tvimportelproductos);


                        nombre.setText(entrada.getString(PRODUCTO_NOMBRE));
                        descripcion.setText(entrada.getString(PRODUCTO_DESCRIPCION));
                        importe.setText(entrada.getString(PRODUCTO_PRECIO));

                        if (entrada.getString(PRODUCTO_RUTAFOTO) != null) {
                            imagen.setImageURI(entrada.getUri(PRODUCTO_RUTAFOTO));
                        }

                    }
                });

                break;

            case TIPOPARTIDA:

                autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(getContext(),
                        R.layout.item_list_partidabase, lista, CAMPOS_PARTIDABASE) {

                    @Override
                    public void onEntrada(Modelo entrada, View view) {

                        ImageView imagen = view.findViewById(R.id.imglpartidabase);
                        TextView descripcion = view.findViewById(R.id.tvdescripcionpartidabase);
                        TextView importe = view.findViewById(R.id.tvimppartidabase);


                        autoNombre.setText(entrada.getString(PARTIDABASE_NOMBRE));
                        descripcion.setText(entrada.getString(PARTIDABASE_DESCRIPCION));
                        importe.setText(entrada.getString(PARTIDABASE_PRECIO));

                        if (entrada.getString(PARTIDABASE_RUTAFOTO) != null) {
                            imagen.setImageURI(entrada.getUri(PARTIDABASE_RUTAFOTO));
                        }

                    }
                });

                break;

            case TIPOPRODUCTOPROV:

                autoCompleteTextView.setAdapter(new ListaAdaptadorFiltroProdProv(getContext(),
                        R.layout.item_list_prodprov, listaProdProv) {
                    @Override
                    public void onEntrada(ProdProv entrada, View view) {

                        ImageView imagen = view.findViewById(R.id.imagenprov);
                        TextView nombre = view.findViewById(R.id.tvnomprov);
                        TextView descripcion = view.findViewById(R.id.tvdescprov);
                        TextView importe = view.findViewById(R.id.tvprecioprov);
                        TextView refProv = view.findViewById(R.id.tvrefprov);

                        nombre.setText(entrada.getNombre());
                        descripcion.setText(entrada.getDescripcion());
                        importe.setText(String.valueOf(entrada.getPrecio()));
                        refProv.setText(entrada.getRefprov());
                        String rutafoto = entrada.getRutafoto();

                        if (entrada.getRutafoto() != null) {

                            setImagenFireStoreCircle(contexto, rutafoto, imagen);
                            //FirebaseStorage storage = FirebaseStorage.getInstance();
                            //StorageReference storageRef = storage.getReference();
                            //StorageReference spaceRef = storageRef.child(rutafoto);
                            //GlideApp.with(getContext())
                            //        .load(spaceRef)
                            //        .into(imagenTarea);
                        }

                    }
                });


                break;


        }

    }

    private void setAdaptadorAutoCat() {

        listaCat = new ArrayList<>();

        DatabaseReference dbCategorias =
                FirebaseDatabase.getInstance().getReference()
                        .child("categorias");

        ValueEventListener eventListenerCat = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Categorias categoria = ds.getValue(Categorias.class);
                    categoria.setId(ds.getRef().getKey());
                    listaCat.add(categoria);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        dbCategorias.addValueEventListener(eventListenerCat);


        ListaAdaptadorFiltroCat adapterCat = new ListaAdaptadorFiltroCat
                (contexto, R.layout.item_list_categoria, listaCat);

        autoCat.setAdapter(adapterCat);

        autoCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                categoria = listaCat.get(position).getNombre();
                autoCat.setText(categoria);
                rv();

            }
        });

    }

    private void setAdaptadorAutoProv() {

        listaProv = new ArrayList<>();

        DatabaseReference dbProveedor =
                FirebaseDatabase.getInstance().getReference()
                        .child("proveedores");

        ValueEventListener eventListenerProv = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Proveedores proveedor = ds.getValue(Proveedores.class);
                    proveedor.setId(ds.getRef().getKey());
                    listaProv.add(proveedor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        dbProveedor.addValueEventListener(eventListenerProv);


        ListaAdaptadorFiltroProv adapterProv = new ListaAdaptadorFiltroProv
                (contexto, R.layout.item_list_proveedor, listaProv);

        autoProv.setAdapter(adapterProv);

        autoProv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                proveedor = listaProv.get(position).getNombre();
                autoProv.setText(proveedor);
                rv();
            }
        });
    }

    public static class AdaptadorPartida extends RecyclerView.Adapter<AdaptadorPartida.PartidaViewHolder>
            implements View.OnClickListener, CommonPry.Constantes, ContratoPry.Tablas {

        ArrayList<Modelo> listaPartidas;
        private View.OnClickListener listener;

        public AdaptadorPartida(ArrayList<Modelo> listaPartidas) {

            this.listaPartidas = listaPartidas;
        }

        @NonNull
        @Override
        public PartidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_partidabase, null, false);

            view.setOnClickListener(this);


            return new PartidaViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull PartidaViewHolder partidaViewHolder, int position) {

            partidaViewHolder.descripcionPartida.setText(listaPartidas.get(position).getCampos(PARTIDABASE_DESCRIPCION));
            partidaViewHolder.tiempoPartida.setText(listaPartidas.get(position).getCampos(PARTIDABASE_TIEMPO));
            partidaViewHolder.importePartida.setText(JavaUtil.formatoMonedaLocal(listaPartidas.get(position).getDouble(PARTIDABASE_PRECIO)));

            if (listaPartidas.get(position).getString(PARTIDABASE_RUTAFOTO) != null) {

                partidaViewHolder.imagenPartida.setImageURI(listaPartidas.get(position).getUri(PARTIDABASE_RUTAFOTO));
            }


            System.out.println("listaPartidas: " + listaPartidas.size() + " registros");

        }

        @Override
        public int getItemCount() {
            return listaPartidas.size();
        }

        @Override
        public void onClick(View v) {

            if (listener != null) {

                listener.onClick(v);


            }


        }

        public class PartidaViewHolder extends RecyclerView.ViewHolder {

            ImageView imagenPartida;
            TextView descripcionPartida, tiempoPartida, importePartida;

            public PartidaViewHolder(@NonNull View itemView) {
                super(itemView);

                imagenPartida = itemView.findViewById(R.id.imglpartidabase);
                descripcionPartida = itemView.findViewById(R.id.tvdescripcionpartidabase);
                tiempoPartida = itemView.findViewById(R.id.tvtiempopartidabase);
                importePartida = itemView.findViewById(R.id.tvimppartidabase);

            }

        }
    }

    public class AdaptadorProdProv extends FirebaseRecyclerAdapter<ProdProv, AdaptadorProdProv.ProdProvHolder>
            implements View.OnClickListener {

        View.OnClickListener listener;

        public AdaptadorProdProv(@NonNull FirebaseRecyclerOptions<ProdProv> options) {
            super(options);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @NonNull
        @Override
        public ProdProvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_prodprov, parent, false);

            view.setOnClickListener(this);
            return new ProdProvHolder(view);
        }

        @Override
        protected void onBindViewHolder(@NonNull ProdProvHolder prodProvHolder, int i, @NonNull ProdProv prodProv) {

            prodProvHolder.setRefprov(prodProv.getRefprov());
            prodProvHolder.setNombre(prodProv.getNombre());
            prodProvHolder.setDescripcion(prodProv.getDescripcion());
            prodProvHolder.setPrecio(prodProv.getPrecio());
            prodProvHolder.setRutafoto(prodProv.getRutafoto());


        }

        @Override
        public void onError(@NonNull DatabaseError error) {
            super.onError(error);
            Log.e(TAG, error.toString());
        }

        @Override
        public void onClick(View v) {

            if (listener != null) {

                listener.onClick(v);
            }

        }

        public class ProdProvHolder extends RecyclerView.ViewHolder {

            private View mView;
            private TextView refprov, nombre, descripcion, precio;
            private ImageView imagen;
            private String path;

            public ProdProvHolder(View itemView) {
                super(itemView);
                mView = itemView;
            }

            public ProdProv getItem() {

                ProdProv prodProv = new ProdProv();
                prodProv.setRefprov(refprov.getText().toString());
                prodProv.setNombre(nombre.getText().toString());
                prodProv.setDescripcion(descripcion.getText().toString());
                prodProv.setPrecio(JavaUtil.comprobarDouble(precio.getText().toString()));
                prodProv.setRutafoto(path);

                return prodProv;

            }

            public void setRefprov(String refprov) {
                this.refprov = mView.findViewById(R.id.tvrefprov);
                this.refprov.setText(refprov);
            }

            public void setNombre(String nombre) {
                this.nombre = mView.findViewById(R.id.tvnomprov);
                this.nombre.setText(nombre);
            }

            public void setDescripcion(String descripcion) {
                this.descripcion = mView.findViewById(R.id.tvdescprov);
                this.descripcion.setText(descripcion);
            }

            public void setPrecio(double precio) {
                this.precio = mView.findViewById(R.id.tvprecioprov);
                this.precio.setText(JavaUtil.formatoMonedaLocal(precio));
            }

            public void setRutafoto(String rutafoto) {
                //FirebaseStorage storage = FirebaseStorage.getInstance();
                //StorageReference storageRef = storage.getReference();
                //StorageReference spaceRef = storageRef.child(rutafoto);
                mediaUtil = new MediaUtil(getContext());
                imagen = mView.findViewById(R.id.imagenprov);
                setImagenFireStoreCircle(contexto, rutafoto, imagen);
                path = rutafoto;
            }
        }
    }


    public class AdaptadorProveedor extends RecyclerView.Adapter<AdaptadorProveedor.ViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas {

        View.OnClickListener listener;
        ArrayList<ProdProv> lista;

        public AdaptadorProveedor(ArrayList<ProdProv> lista) {
            this.lista = lista;
        }

        @Override
        public void onClick(View v) {

        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_prodprov, null, false);

            view.setOnClickListener(this);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.refprov.setText(lista.get(position).getRefprov());
            holder.nombre.setText(lista.get(position).getNombre());
            holder.descripcion.setText(lista.get(position).getDescripcion());
            holder.precio.setText(String.valueOf(lista.get(position).getPrecio()));
            if (lista.get(position).getRutafoto() != null) {

                MediaUtil imagenUtil = new MediaUtil(contexto);
                imagenUtil.setImageFireStoreCircle(lista.get(position).getRutafoto(), holder.imagenProv);
            }
        }

        @Override
        public int getItemCount() {
            if (lista != null) {
                return lista.size();
            }
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView refprov, nombre, descripcion, precio;
            private ImageView imagenProv;
            private String path;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                refprov = itemView.findViewById(R.id.tvrefprov);
                nombre = itemView.findViewById(R.id.tvnomprov);
                descripcion = itemView.findViewById(R.id.tvdescprov);
                precio = itemView.findViewById(R.id.tvprecioprov);
                imagenProv = itemView.findViewById(R.id.imagenprov);

            }
        }

    }

    public class AdaptadorProducto extends RecyclerView.Adapter<AdaptadorProducto.ProductoViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas {

        ArrayList<Modelo> listaProductos;
        View.OnClickListener listener;

        public AdaptadorProducto(ArrayList<Modelo> listaProductos) {
            this.listaProductos = listaProductos;
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_producto_detpartida, null, false);

            view.setOnClickListener(this);

            return new ProductoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder productoViewHolder, int position) {

            productoViewHolder.importe.setText(listaProductos.get(position).getString(PRODUCTO_PRECIO));
            productoViewHolder.nombre.setText(listaProductos.get(position).getString(PRODUCTO_NOMBRE));

            if (listaProductos.get(position).getString(PRODUCTO_RUTAFOTO) != null) {

                productoViewHolder.imagen.setImageURI(listaProductos.get(position).getUri(PRODUCTO_RUTAFOTO));
            }

        }

        @Override
        public int getItemCount() {
            return listaProductos.size();
        }

        @Override
        public void onClick(View v) {

            if (listener != null) {

                listener.onClick(v);
            }

        }

        public class ProductoViewHolder extends RecyclerView.ViewHolder {

            TextView importe, nombre;
            ImageView imagen;

            public ProductoViewHolder(@NonNull View itemView) {
                super(itemView);

                importe = itemView.findViewById(R.id.tvimportelproductos_detpartida);
                nombre = itemView.findViewById(R.id.tvnombrelproductos_detpartida);
                imagen = itemView.findViewById(R.id.imglproductos_detpartida);
            }
        }
    }

    public class AdaptadorTareas extends RecyclerView.Adapter<AdaptadorTareas.TareaViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas {

        ArrayList<Modelo> listaTareas;
        private View.OnClickListener listener;

        public AdaptadorTareas(ArrayList<Modelo> listaTareas) {
            this.listaTareas = listaTareas;
        }

        @NonNull
        @Override
        public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_trabajo_detpartida, null, false);

            view.setOnClickListener(this);

            return new TareaViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull TareaViewHolder tareaViewHolder, int position) {

            tareaViewHolder.tiempo.setText(listaTareas.get(position).getCampos(TRABAJO_TIEMPO));
            tareaViewHolder.nombre.setText(listaTareas.get(position).getString(TRABAJO_NOMBRE));
            if (listaTareas.get(position).getString(TRABAJO_RUTAFOTO) != null) {

                tareaViewHolder.imagen.setImageURI(listaTareas.get(position).getUri(TRABAJO_RUTAFOTO));
            }

        }

        @Override
        public int getItemCount() {
            return listaTareas.size();
        }

        @Override
        public void onClick(View v) {

            if (listener != null) {

                listener.onClick(v);
            }

        }

        public class TareaViewHolder extends RecyclerView.ViewHolder {

            TextView tiempo, nombre;
            ImageView imagen;

            public TareaViewHolder(@NonNull View itemView) {
                super(itemView);


                tiempo = itemView.findViewById(R.id.tvtiempoltareas_detpartida);
                imagen = itemView.findViewById(R.id.imgltarea_detpartida);
                nombre = itemView.findViewById(R.id.tvnomltarea_detpartida);
            }
        }
    }


    public abstract class ListaAdaptadorFiltroProdProv extends ArrayAdapter<ProdProv> {

        private ArrayList<ProdProv> entradas;
        private ArrayList<ProdProv> entradasfiltro;
        private int R_layout_IdView;
        private Context contexto;

        ListaAdaptadorFiltroProdProv(Context contexto, int R_layout_IdView, ArrayList<ProdProv> entradas) {
            super(contexto, R_layout_IdView, entradas);
            this.contexto = contexto;
            this.entradas = entradas;
            this.entradasfiltro = new ArrayList<>(entradas);
            this.R_layout_IdView = R_layout_IdView;
        }

        @Override
        public View getView(int posicion, View view, ViewGroup pariente) {
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R_layout_IdView, null);
            }
            onEntrada(entradasfiltro.get(posicion), view);
            System.out.println("entradasfiltro = " + entradasfiltro.get(posicion));
            return view;
        }

        @Override
        public int getCount() {
            return entradasfiltro.size();
        }


        @NonNull
        @Override
        public Filter getFilter() {

            Filter filter;
            filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    List<ProdProv> suggestion = new ArrayList<>();
                    if (constraint != null) {

                        for (ProdProv item : entradas) {

                            if (item.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            } else if (item.getDescripcion().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            } else if (item.getRefprov().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            }


                        }
                        // Query the autocomplete API for the entered constraint
                        // Results
                        results.values = suggestion;
                        results.count = suggestion.size();
                    }
                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    entradasfiltro.clear();

                    if (results != null && results.count > 0) {
                        for (ProdProv item : (List<ProdProv>) results.values) {
                            entradasfiltro.add(item);
                        }
                        notifyDataSetChanged();
                    } else if (constraint == null) {
                        // no filter, addModelo entire original list back in
                        entradasfiltro.addAll(entradas);
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        @Override
        public ProdProv getItem(int posicion) {
            return entradasfiltro.get(posicion);
        }

        @Override
        public long getItemId(int posicion) {
            return posicion;
        }

        /**
         * Devuelve cada una de las entradas con cada una de las vistas a la que debe de ser asociada
         *
         * @param entrada La entrada que será la asociada a la view. La entrada es del tipo del paquete/handler
         * @param view    View particular que contendrá los datos del paquete/handler
         */
        public abstract void onEntrada(ProdProv entrada, View view);
    }

    public class ListaAdaptadorFiltroProv extends ArrayAdapter<Proveedores> {

        private ArrayList<Proveedores> entradas;
        private ArrayList<Proveedores> entradasfiltro;
        private int R_layout_IdView;
        private Context contexto;

        ListaAdaptadorFiltroProv(Context contexto, int R_layout_IdView, ArrayList<Proveedores> entradas) {
            super(contexto, R_layout_IdView, entradas);
            this.contexto = contexto;
            this.entradas = entradas;
            this.entradasfiltro = new ArrayList<>(entradas);
            this.R_layout_IdView = R_layout_IdView;
        }

        @Override
        public View getView(int position, View view, ViewGroup pariente) {
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R_layout_IdView, null);
            }

            TextView nombre, descripcion, direccion;
            ImageView imagenProv;

            nombre = view.findViewById(R.id.tvnomproveedor);
            descripcion = view.findViewById(R.id.tvdescproveedor);
            direccion = view.findViewById(R.id.tvdirproveedor);
            imagenProv = view.findViewById(R.id.imagenproveedor);

            nombre.setText(entradasfiltro.get(position).getNombre());
            descripcion.setText(entradasfiltro.get(position).getDescripcion());
            direccion.setText(entradasfiltro.get(position).getDireccion());
            String pathprov = entradasfiltro.get(position).getRutafoto();
            if (pathprov != null) {
                setImagenFireStoreCircle(contexto, pathprov, imagenProv);
            }


            return view;
        }

        @Override
        public int getCount() {
            return entradasfiltro.size();
        }


        @NonNull
        @Override
        public Filter getFilter() {

            Filter filter;
            filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    List<Proveedores> suggestion = new ArrayList<>();
                    if (constraint != null) {

                        for (Proveedores item : entradas) {

                            if (item.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            } else if (item.getDescripcion().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            }


                        }
                        // Query the autocomplete API for the entered constraint
                        // Results
                        results.values = suggestion;
                        results.count = suggestion.size();
                    }
                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    entradasfiltro.clear();

                    if (results != null && results.count > 0) {
                        for (Proveedores item : (List<Proveedores>) results.values) {
                            entradasfiltro.add(item);
                        }
                        notifyDataSetChanged();
                    } else if (constraint == null) {
                        // no filter, addModelo entire original list back in
                        entradasfiltro.addAll(entradas);
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        @Override
        public Proveedores getItem(int posicion) {
            return entradasfiltro.get(posicion);
        }

        @Override
        public long getItemId(int posicion) {
            return posicion;
        }

    }

    public class ListaAdaptadorFiltroCat extends ArrayAdapter<Categorias> {

        private ArrayList<Categorias> entradas;
        private ArrayList<Categorias> entradasfiltro;
        private int R_layout_IdView;
        private Context contexto;

        ListaAdaptadorFiltroCat(Context contexto, int R_layout_IdView, ArrayList<Categorias> entradas) {
            super(contexto, R_layout_IdView, entradas);
            this.contexto = contexto;
            this.entradas = entradas;
            this.entradasfiltro = new ArrayList<>(entradas);
            this.R_layout_IdView = R_layout_IdView;
        }

        @Override
        public View getView(int position, View view, ViewGroup pariente) {
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R_layout_IdView, null);
            }

            TextView nombre, descripcion;
            ImageView imagencat;

            nombre = view.findViewById(R.id.tvnomcat);
            descripcion = view.findViewById(R.id.tvdesccat);
            imagencat = view.findViewById(R.id.imagencat);

            nombre.setText(entradasfiltro.get(position).getNombre());
            descripcion.setText(entradasfiltro.get(position).getDescripcion());

            String pathcat = entradasfiltro.get(position).getRutafoto();
            if (pathcat != null) {
                setImagenFireStoreCircle(contexto, pathcat, imagencat);
            }
            return view;
        }

        @Override
        public int getCount() {
            return entradasfiltro.size();
        }


        @NonNull
        @Override
        public Filter getFilter() {

            Filter filter;
            filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    List<Categorias> suggestion = new ArrayList<>();
                    if (constraint != null) {

                        for (Categorias item : entradas) {

                            if (item.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            } else if (item.getDescripcion().toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                            }


                        }
                        // Query the autocomplete API for the entered constraint
                        // Results
                        results.values = suggestion;
                        results.count = suggestion.size();
                    }
                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    entradasfiltro.clear();

                    if (results != null && results.count > 0) {
                        for (Categorias item : (List<Categorias>) results.values) {
                            entradasfiltro.add(item);
                        }
                        notifyDataSetChanged();
                    } else if (constraint == null) {
                        // no filter, addModelo entire original list back in
                        entradasfiltro.addAll(entradas);
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        @Override
        public Categorias getItem(int posicion) {
            return entradasfiltro.get(posicion);
        }

        @Override
        public long getItemId(int posicion) {
            return posicion;
        }

    }

    public class AdaptadorFiltroRV extends ListaAdaptadorFiltroRV {

        public AdaptadorFiltroRV(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<Modelo> entrada) {

            TextView tipo, nombre, ltiempo, lcantidad, limporte, tiempo, cantidad, importe;
            ImageView imagen;

            tipo = itemView.findViewById(R.id.tvtipoldetpaetida);
            nombre = itemView.findViewById(R.id.tvnomldetpartida);
            ltiempo = itemView.findViewById(R.id.ltiempoldetpartida);
            lcantidad = itemView.findViewById(R.id.lcantldetpartida);
            limporte = itemView.findViewById(R.id.limpldetpartida);
            tiempo = itemView.findViewById(R.id.tvtiempoldetpartida);
            cantidad = itemView.findViewById(R.id.tvcantldetpartida);
            importe = itemView.findViewById(R.id.tvimpldetpartida);
            imagen = itemView.findViewById(R.id.imgldetpartida);

            String tipodetpartida = entrada.get(posicion).getString(DETPARTIDA_TIPO);
            tipo.setText(tipodetpartida.toUpperCase());
            nombre.setText(entrada.get(posicion).getString(DETPARTIDA_NOMBRE));
            tiempo.setText(entrada.get(posicion).getString(DETPARTIDA_TIEMPO));
            if (entrada.get(posicion).getString(DETPARTIDA_TIPO).equals(CommonPry.TiposDetPartida.TIPOTRABAJO)) {
                importe.setText(JavaUtil.formatoMonedaLocal(
                        (entrada.get(posicion).getDouble(DETPARTIDA_TIEMPO) * CommonPry.hora)));
            } else {
                importe.setText(entrada.get(posicion).getString(DETPARTIDA_PRECIO));
            }
            if (entrada.get(posicion).getString(DETPARTIDA_RUTAFOTO) != null) {
                if (tipodetpartida.equals(TIPOPRODUCTOPROV)) {
                    MediaUtil imagenUtil = new MediaUtil(contexto);
                    imagenUtil.setImageFireStoreCircle(entrada.get(posicion).getString(DETPARTIDA_RUTAFOTO), imagen);

                } else {
                    imagen.setImageURI(entrada.get(posicion).getUri(DETPARTIDA_RUTAFOTO));
                }
            }

            if (!tipodetpartida.equals(TIPOTRABAJO)) {

                ltiempo.setVisibility(View.GONE);
                tiempo.setVisibility(View.GONE);
            }


            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView nomPartida, nombre, proy, tvcomplet, ttiempo, treking;
        ImageView imagen, imgEstado;
        ProgressBar pbar, pbar2;
        CardView card;

        public ViewHolderRV(View itemView) {
            super(itemView);
            nomPartida = itemView.findViewById(R.id.tvpartldetpaetidat);
            nombre = itemView.findViewById(R.id.tvnomldetpartidat);
            proy = itemView.findViewById(R.id.tvproydetpartidat);
            imagen = itemView.findViewById(R.id.imgldetpartidat);
            pbar = itemView.findViewById(R.id.pbarldetpartidat);
            pbar2 = itemView.findViewById(R.id.pbar2ldetpartidat);
            ttiempo = itemView.findViewById(R.id.tvttotalldetpartidat);
            tvcomplet = itemView.findViewById(R.id.tvcompletadaldetpartidat);
            card = itemView.findViewById(R.id.carddetpartidat);
            treking = itemView.findViewById(R.id.tvtrekldetpaetidat);
            imgEstado = itemView.findViewById(R.id.imgestadoldetpartidat);
        }

        @Override
        public void bind(Modelo modelo) {

            String id = modelo.getString(DETPARTIDA_ID_PARTIDA);
            int secuencia = modelo.getInt(DETPARTIDA_SECUENCIA);
            modelo = queryObjectDetalle(CAMPOS_DETPARTIDA,id,secuencia);

            nombre.setText(modelo.getString(DETPARTIDA_NOMBRE));

            try {
                MediaUtil mediaUtil = new MediaUtil(contexto);
                mediaUtil.setImageUriCircle(modelo.getString(DETPARTIDA_RUTAFOTO), imagen);

            } catch (Exception e) {
                e.printStackTrace();
            }

            long ahora = hoy();
            Modelo partida = queryObject(CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA, id, null, IGUAL, null);
            nomPartida.setText(partida.getString(PARTIDA_NOMBRE));
            double cantidadTotal = partida.getDouble(PARTIDA_CANTIDAD);
            double tiempodet = (modelo.getDouble(DETPARTIDA_TIEMPO) * cantidadTotal * HORASLONG) / 1000;
            double tiemporeal = (modelo.getDouble(DETPARTIDA_TIEMPOREAL) * HORASLONG) / 1000;
            String idProyecto = partida.getString(PARTIDA_ID_PROYECTO);
            Modelo proyecto = queryObject(CAMPOS_PROYECTO, idProyecto);
            proy.setText(proyecto.getString(PROYECTO_NOMBRE));
            long contador = modelo.getLong(DETPARTIDA_CONTADOR);
            long pausa = modelo.getLong(DETPARTIDA_PAUSA);
            if (pausa>0){
                ahora = pausa;
                imgEstado.setImageResource(R.drawable.ic_pausa_indigo);
            }else if (contador>0){
                imgEstado.setImageResource(R.drawable.ic_play_indigo);
            }else{
                contador = ahora;
                imgEstado.setImageResource(R.drawable.ic_stop_indigo);
            }

            double count = ((double) (ahora-contador))/1000;


            double completada = (((tiemporeal * 100) / tiempodet) + ((count * 100) / tiempodet));

            String asText = JavaUtil.relojContador((ahora-contador)/1000);
            treking.setText(String.format(Locale.getDefault(), "%s", asText));

            ttiempo.setText(String.format(Locale.getDefault(),
                    "%s %s %s %s %s", JavaUtil.getDecimales(((tiemporeal + count) / 3600)),
                    getString(R.string.horas), getString(R.string.de),
                    JavaUtil.getDecimales(tiempodet / 3600), getString(R.string.horas)));

            tvcomplet.setText(String.format(Locale.getDefault(),
                    "%s %s", JavaUtil.getDecimales(completada), "% completa"));

            AndroidUtil.barsCard(contexto, pbar, pbar2, false,100,90,120, completada,
                    tvcomplet, treking, R.color.Color_contador_ok, R.color.Color_contador_acept,
                    R.color.Color_contador_notok,card,R.color.Color_card_ok,R.color.Color_card_acept,
                    R.color.Color_card_notok);

            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

}