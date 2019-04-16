package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsSpinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import jjlacode.com.androidutils.AppActivity;
import jjlacode.com.androidutils.DatePickerFragment;
import jjlacode.com.androidutils.FragmentC;
import jjlacode.com.androidutils.FragmentCUD;
import jjlacode.com.androidutils.FragmentUD;
import jjlacode.com.androidutils.ICFragmentos;
import jjlacode.com.androidutils.ImagenUtil;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.ListaAdaptadorFiltro;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.utilities.CommonPry;

public class FragmentUDProyecto extends FragmentCUD
        implements CommonPry.Constantes, ContratoPry.Tablas, CommonPry.Estados, CommonPry.TiposEstados {

    private String idProyecto;

    private ImageButton imagenTipoClienteProyecto;
    private ImageButton btnfechaentrega;
    private EditText nombrePry;
    private EditText descripcionPry;
    private TextView estadoProyecto;
    private TextView fechaEntregaPresup;
    private TextView fechaAcordadaPry;
    private EditText importeFinalPry;
    private TextView labelfentregap;
    private TextView labelimportefinal;
    private Button btnActualizar;
    private Button btnActualizar2;
    private ArrayList<Modelo> objClientes;
    private String idCliente;
    private int peso = 0;
    private int posCliente = 0;
    private long fechaCalculada = 0;
    private long fechaAcordada = 0;
    private long fechaEntregaP = 0;
    private String idEstado;
    private int totcompletada = 0;
    private double totPartidas = 0.0;
    private double precioPartidas = 0.0;
    //private double impTotalCalculado = 0.0;
    private Modelo proyecto = null;

    private static ConsultaBD consulta = new ConsultaBD();
    private AutoCompleteTextView spClienteProyecto;
    private String nombreCliente;
    private ArrayList<Modelo> listaClientes;
    private ArrayList<Modelo> objEstados;
    private ImageButton btnimgEstadoPry;
    private TextView fechaEntradaPry;
    private TextView fechaCalculadaPry;
    private TextView fechaFinalPry;
    private TextView totalPartidasPry;
    private TextView pvpPartidas;
    private TextView importeCalculadoPry;
    private TextView labelfcalc;
    private TextView labelfacor;
    private TextView labelffinal;
    private TextView labelimportecalculado;
    private ImageButton btnfechaacord;
    private ArrayList<String> listaEstados;
    private Spinner spEstadoProyecto;
    private Button btnEvento;
    private Button btnPartidasPry;
    private double preciototal;

    public FragmentUDProyecto() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {

        if (idProyecto!=null) {
            new CommonPry.Calculos.TareaActualizaProy().execute(idProyecto);
            cargarDatos();
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ud_proyecto, container, false);

        imagen = view.findViewById(R.id.imudpry);
        imagenTipoClienteProyecto = view.findViewById(R.id.imgbtntipocliudpry);
        btnimgEstadoPry = view.findViewById(R.id.imgbtnestudpry);
        nombrePry = view.findViewById(R.id.etnomudpry);
        descripcionPry = view.findViewById(R.id.etdescudpry);
        spClienteProyecto = view.findViewById(R.id.sptipocliudpry);
        spEstadoProyecto = view.findViewById(R.id.spestudpry);
        estadoProyecto = view.findViewById(R.id.tvestudproy);
        fechaEntradaPry = view.findViewById(R.id.proyecto_ud_tv_fecha_entrada);
        fechaEntregaPresup = view.findViewById(R.id.tvfentpresuppry);
        fechaCalculadaPry = view.findViewById(R.id.tvfcalcudpry);
        fechaAcordadaPry = view.findViewById(R.id.tvfacorudpry);
        fechaFinalPry = view.findViewById(R.id.tvffinudpry);
        totalPartidasPry = view.findViewById(R.id.tvtotpartudpry);
        pvpPartidas = view.findViewById(R.id.tvpreciopartidasudpry);
        importeCalculadoPry = view.findViewById(R.id.tvimpcaludpry);
        importeFinalPry = view.findViewById(R.id.etimpfinudpry);
        btnsave = view.findViewById(R.id.btnsaveudpry);
        btndelete = view.findViewById(R.id.btndeludpry);
        btnback = view.findViewById(R.id.btnbackudpry);
        btnEvento = view.findViewById(R.id.btneventoudpry);
        btnPartidasPry = view.findViewById(R.id.btnpartudpry);
        btnActualizar = view.findViewById(R.id.btnactualizar);
        btnActualizar2 = view.findViewById(R.id.btnactualizar2);
        btnfechaacord = view.findViewById(R.id.btnfechaacord);
        btnfechaentrega = view.findViewById(R.id.btnfechaentrega);
        labelfcalc = view.findViewById(R.id.lfcalcudpry);
        labelfacor = view.findViewById(R.id.lfacorudpry);
        labelffinal = view.findViewById(R.id.lffinudpry);
        labelfentregap = view.findViewById(R.id.lfentpresupudpry);
        labelimportecalculado = view.findViewById(R.id.limpcaludpry);
        labelimportefinal = view.findViewById(R.id.limpfinudpry);

        bundle = getArguments();

        if (bundle != null) {
            proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
            namef = bundle.getString("namef");
            bundle = null;
            bundle = new Bundle();
            bundle.putSerializable(TABLA_PROYECTO,proyecto);
            icFragmentos.enviarBundleAActivity(bundle);
            bundle = null;
            if (proyecto!=null) {
                idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);
                //proyecto = consulta.queryObject(CAMPOS_PROYECTO, idProyecto);
                if (idProyecto!=null) {
                    new CommonPry.Calculos.TareaActualizaProy().execute(idProyecto);
                    calculoTotales();
                }
            }
        }

        if (idProyecto!=null) {

           spEstadoProyecto.setVisibility(View.GONE);


           cargarDatos();

        }else{

            btndelete.setVisibility(View.GONE);
            btnEvento.setVisibility(View.GONE);
            btnPartidasPry.setVisibility(View.GONE);
            estadoProyecto.setVisibility(View.GONE);
            fechaAcordadaPry.setVisibility(View.GONE);
            fechaCalculadaPry.setVisibility(View.GONE);
            btnfechaacord.setVisibility(View.GONE);
            btnfechaentrega.setVisibility(View.GONE);
            labelfacor.setVisibility(View.GONE);
            labelfcalc.setVisibility(View.GONE);
            btnActualizar.setVisibility(View.GONE);
            btnActualizar2.setVisibility(View.GONE);
            pvpPartidas.setVisibility(View.GONE);
            fechaEntregaPresup.setVisibility(View.GONE);
            labelfentregap.setVisibility(View.GONE);
            fechaFinalPry.setVisibility(View.GONE);
            labelfentregap.setVisibility(View.GONE);
            labelffinal.setVisibility(View.GONE);
            totalPartidasPry.setVisibility(View.GONE);
            labelimportecalculado.setVisibility(View.GONE);
            labelimportefinal.setVisibility(View.GONE);
            importeFinalPry.setVisibility(View.GONE);
            importeCalculadoPry.setVisibility(View.GONE);
            btnimgEstadoPry.setVisibility(View.GONE);
            fechaEntradaPry.setVisibility(View.GONE);

            listaObjetosEstados();

            ArrayAdapter<String> adaptadorEstado = new ArrayAdapter<>
                    (getContext(),android.R.layout.simple_spinner_item,listaEstados);

            spEstadoProyecto.setAdapter(adaptadorEstado);

            if (namef.equals(PRESUPUESTO)){

                spEstadoProyecto.setSelection(1);
            }

            spEstadoProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position>0){

                        idEstado = objEstados.get(position-1).getString(ESTADO_ID_ESTADO);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        if (CommonPry.permiso) {
            imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mostrarDialogoOpcionesImagen();


                }

            });
        }


        setAdaptadorClientes(spClienteProyecto);

        if (idCliente!=null) {
            Modelo cliente = consulta.queryObject(CAMPOS_CLIENTE, idCliente);
            nombreCliente = cliente.getString(CLIENTE_NOMBRE);
            spClienteProyecto.setText(nombreCliente);
        }

        spClienteProyecto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Modelo cliente = (Modelo) spClienteProyecto.getAdapter().getItem(position);
                idCliente = cliente.getString(CLIENTE_ID_CLIENTE);
                nombreCliente = cliente.getString(CLIENTE_NOMBRE);
                spClienteProyecto.setText(nombreCliente);
                peso = cliente.getInt(CLIENTE_PESOTIPOCLI);
                if (peso>6){imagenTipoClienteProyecto.setImageResource(R.drawable.clientev);}
                else if (peso>3){imagenTipoClienteProyecto.setImageResource(R.drawable.clientea);}
                else if (peso>0){imagenTipoClienteProyecto.setImageResource(R.drawable.clienter);}
                else {imagenTipoClienteProyecto.setImageResource(R.drawable.cliente);}

            }

        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delete();

                    new CommonPry.Calculos.Tareafechas().execute();
                    bundle = new Bundle();

                    if (namef.equals(AGENDA)) {

                        bundle.putString("namef", namef);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentAgenda());
                        bundle = null;

                    } else {

                        bundle.putString("namef", namef);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentProyecto());
                        bundle = null;
                    }

            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (idProyecto!=null) {

                    update();
                    cambiarFragment();

                }else{
                    registrar();
                    cargarDatos();
                }



            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    bundle = new Bundle();

                    if (namef.equals(AGENDA)) {

                        bundle.putString("namef", namef);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentAgenda());
                        bundle = null;

                    } else {

                        bundle.putString("namef", namef);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentProyecto());
                        bundle = null;
                    }
            }
        });

        btnEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                Modelo cliente = consulta.queryObject(CAMPOS_CLIENTE,idCliente);

                bundle = new Bundle();
                bundle.putSerializable(TABLA_CLIENTE,cliente);
                bundle.putSerializable(TABLA_PROYECTO, proyecto);
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCEvento());
                bundle = null;
            }
        });
        btnPartidasPry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                enviarProyectoTemporal(namef, PARTIDA, new FragmentPartidasProyecto());

            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modificarEstado();
                update();

                if (peso > 6) {
                    imagenTipoClienteProyecto.setImageResource(R.drawable.clientev);
                } else if (peso > 3) {
                    imagenTipoClienteProyecto.setImageResource(R.drawable.clientea);
                } else if (peso > 0) {
                    imagenTipoClienteProyecto.setImageResource(R.drawable.clienter);
                } else {
                    imagenTipoClienteProyecto.setImageResource(R.drawable.cliente);
                }

            }
        });
        btnActualizar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modificarEstadoNoAceptado();
                update();
            }
        });
        imagenTipoClienteProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (idProyecto!=null){
                update();
                Modelo cliente = consulta.queryObject(CAMPOS_CLIENTE,idCliente);

                bundle = new Bundle();
                bundle.putSerializable(TABLA_CLIENTE,cliente);
                bundle.putSerializable(TABLA_PROYECTO, proyecto);
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentUDCliente());
                bundle = null;
                }else{
                    mostrarDialogoTipoCliente();
                }
            }
        });

        btnfechaacord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    showDatePickerDialogAcordada();

            }
        });

        btnfechaentrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialogEntrega();

            }
        });


        return view;
    }

    private void cargarDatos() {

        btndelete.setVisibility(View.VISIBLE);
        btnEvento.setVisibility(View.VISIBLE);
        btnPartidasPry.setVisibility(View.VISIBLE);
        estadoProyecto.setVisibility(View.VISIBLE);
        fechaAcordadaPry.setVisibility(View.VISIBLE);
        fechaCalculadaPry.setVisibility(View.VISIBLE);
        btnfechaacord.setVisibility(View.VISIBLE);
        btnfechaentrega.setVisibility(View.VISIBLE);
        labelfacor.setVisibility(View.VISIBLE);
        labelfcalc.setVisibility(View.VISIBLE);
        btnActualizar.setVisibility(View.VISIBLE);
        btnActualizar2.setVisibility(View.VISIBLE);
        pvpPartidas.setVisibility(View.VISIBLE);
        fechaEntregaPresup.setVisibility(View.VISIBLE);
        labelfentregap.setVisibility(View.VISIBLE);
        fechaFinalPry.setVisibility(View.VISIBLE);
        labelfentregap.setVisibility(View.VISIBLE);
        labelffinal.setVisibility(View.VISIBLE);
        totalPartidasPry.setVisibility(View.VISIBLE);
        labelimportecalculado.setVisibility(View.VISIBLE);
        labelimportefinal.setVisibility(View.VISIBLE);
        importeFinalPry.setVisibility(View.VISIBLE);
        importeCalculadoPry.setVisibility(View.VISIBLE);
        btnimgEstadoPry.setVisibility(View.VISIBLE);
        fechaEntradaPry.setVisibility(View.VISIBLE);

        nombrePry.setText(proyecto.getString(PROYECTO_NOMBRE));
        descripcionPry.setText(proyecto.getString(PROYECTO_DESCRIPCION));
        idCliente = proyecto.getString(PROYECTO_ID_CLIENTE);
        idEstado = proyecto.getString(PROYECTO_ID_ESTADO);
        idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);
        fechaEntradaPry.setText(JavaUtil.getDateTime(proyecto.getLong(PROYECTO_FECHAENTRADA)));
        if (proyecto.getLong(PROYECTO_FECHAENTREGAPRESUP) == 0) {
            fechaEntregaPresup.setText
                    (R.string.establecer_fecha_entrega_presup);
        } else if (proyecto.getLong(PROYECTO_FECHAENTREGAPRESUP) > 0) {
            fechaEntregaPresup.setText(JavaUtil.getDate(proyecto.getLong(PROYECTO_FECHAENTREGAPRESUP)));
        }

        totalPartidasPry.setText(JavaUtil.getDecimales(totPartidas));

        pvpPartidas.setText(JavaUtil.formatoMonedaLocal(precioPartidas));

        importeCalculadoPry.setText(JavaUtil.formatoMonedaLocal(preciototal));

        if (totPartidas == 0) {

            fechaAcordadaPry.setVisibility(View.GONE);
            fechaCalculadaPry.setVisibility(View.GONE);
            btnfechaacord.setVisibility(View.GONE);
            btnfechaentrega.setVisibility(View.GONE);
            labelfacor.setVisibility(View.GONE);
            labelfcalc.setVisibility(View.GONE);
            btnActualizar.setVisibility(View.GONE);
            pvpPartidas.setVisibility(View.GONE);
            fechaEntregaPresup.setVisibility(View.GONE);
            labelfentregap.setVisibility(View.GONE);

        } else {

            fechaAcordadaPry.setVisibility(View.VISIBLE);
            fechaCalculadaPry.setVisibility(View.VISIBLE);
            btnfechaacord.setVisibility(View.VISIBLE);
            labelfacor.setVisibility(View.VISIBLE);
            labelfcalc.setVisibility(View.VISIBLE);
            btnActualizar.setVisibility(View.VISIBLE);
            pvpPartidas.setVisibility(View.VISIBLE);
            btnfechaentrega.setVisibility(View.GONE);
            fechaEntregaPresup.setVisibility(View.GONE);
            labelfentregap.setVisibility(View.GONE);

            if (proyecto.getLong(PROYECTO_FECHAENTREGAACORDADA) == 0) {

                fechaAcordadaPry.setText("No asignada");
                btnActualizar.setVisibility(View.GONE);


            } else {

                fechaAcordada = proyecto.getLong(PROYECTO_FECHAENTREGAACORDADA);
                fechaAcordadaPry.setText(JavaUtil.getDate(fechaAcordada));
                btnActualizar.setVisibility(View.VISIBLE);
                if (proyecto.getInt(PROYECTO_TIPOESTADO) > 2) {

                    btnfechaentrega.setVisibility(View.VISIBLE);
                    fechaEntregaPresup.setVisibility(View.VISIBLE);
                    labelfentregap.setVisibility(View.VISIBLE);
                }

            }


            fechaCalculada = proyecto.getLong(PROYECTO_FECHAENTREGACALCULADA);
            fechaCalculadaPry.setText(JavaUtil.getDate(fechaCalculada));

        }


        if (proyecto.getLong(PROYECTO_FECHAFINAL) == 0) {
            fechaFinalPry.setVisibility(View.GONE);
            labelffinal.setVisibility(View.GONE);
        } else {
            fechaFinalPry.setVisibility(View.VISIBLE);
            labelffinal.setVisibility(View.VISIBLE);
            fechaFinalPry.setText(JavaUtil.getDate(proyecto.getLong(PROYECTO_FECHAFINAL)));
        }


        if (precioPartidas == 0) {
            importeCalculadoPry.setVisibility(View.GONE);
            labelimportecalculado.setVisibility(View.GONE);
        } else {
            importeCalculadoPry.setVisibility(View.VISIBLE);
            labelimportecalculado.setVisibility(View.VISIBLE);
            importeCalculadoPry.setText(JavaUtil.formatoMonedaLocal
                    (preciototal));
        }

        if (proyecto.getInt(PROYECTO_TIPOESTADO) < 4) {
            importeFinalPry.setVisibility(View.GONE);
            labelimportefinal.setVisibility(View.GONE);
        } else {
            importeFinalPry.setVisibility(View.VISIBLE);
            labelimportefinal.setVisibility(View.VISIBLE);
            importeFinalPry.setText(JavaUtil.formatoMonedaLocal(proyecto.getDouble(PROYECTO_IMPORTEFINAL)));
        }

        if (proyecto.getString(PROYECTO_RUTAFOTO) != null) {
            imagen.setImageURI(proyecto.getUri(PROYECTO_RUTAFOTO));
        }
        estadoProyecto.setText(proyecto.getString(PROYECTO_DESCRIPCION_ESTADO));

        if (PRESUPUESTO.equals(namef)) {
            btnimgEstadoPry.setVisibility(View.GONE);
        } else {
            long retraso = proyecto.getLong(PROYECTO_RETRASO);
            if (retraso > 3 * CommonPry.DIASLONG) {
                btnimgEstadoPry.setImageResource(R.drawable.alert_box_r);
            } else if (retraso > CommonPry.DIASLONG) {
                btnimgEstadoPry.setImageResource(R.drawable.alert_box_a);
            } else {
                btnimgEstadoPry.setImageResource(R.drawable.alert_box_v);
            }
        }

        comprobarEstado();
    }

    private void calculoTotales() {

        ArrayList<Modelo> listaPartidas = consulta.queryListDetalle(CAMPOS_PARTIDA,idProyecto,TABLA_PROYECTO);

        int x = 0;
        for (int i = 0; i < listaPartidas.size(); i++) {

            int completada = listaPartidas.get(i).getInt(PARTIDA_COMPLETADA);

            totcompletada += completada;

            x = i+1;
        }
        totcompletada = (int) (Math.round(((double) totcompletada) / (double) x));

        totPartidas = proyecto.getDouble(PROYECTO_TIEMPO);
        precioPartidas = totPartidas * CommonPry.hora;
        preciototal = proyecto.getDouble(PROYECTO_IMPORTEPRESUPUESTO);

    }


    private void enviarProyectoTemporal(String namef, String namefsub, Fragment myfragment) {

        if (idProyecto!=null) {
            new CommonPry.Calculos.Tareafechas().execute();
            Modelo pry = consulta.queryObject(CAMPOS_PROYECTO, idProyecto);
            fechaCalculada = pry.getLong(PROYECTO_FECHAENTREGACALCULADA);
            calculoTotales();
        }

        Modelo proyectotmp = new Modelo(CAMPOS_PROYECTO);
        proyectotmp.setCampos(PROYECTO_ID_PROYECTO,idProyecto);
        proyectotmp.setCampos(PROYECTO_ID_ESTADO,idEstado);
        proyectotmp.setCampos(PROYECTO_NOMBRE,nombrePry.getText().toString());
        proyectotmp.setCampos(PROYECTO_DESCRIPCION,descripcionPry.getText().toString());
        proyectotmp.setCampos(PROYECTO_ID_CLIENTE,idCliente);
        proyectotmp.setCampos(PROYECTO_FECHAENTREGAACORDADA,fechaAcordada);
        //proyectotmp.setCampos(PROYECTO_FECHAENTREGAPRESUP,fechaEntregaP);
        proyectotmp.setCampos(PROYECTO_FECHAENTREGACALCULADA,fechaCalculada);
        if (fechaCalculada<=fechaAcordada){proyectotmp.setCampos(PROYECTO_RETRASO,0);
        }else {
            proyectotmp.setCampos(PROYECTO_RETRASO,fechaCalculada - fechaAcordada);
        }
        proyectotmp.setCampos(PROYECTO_IMPORTEPRESUPUESTO,preciototal);
        proyectotmp.setCampos(PROYECTO_IMPORTEFINAL,JavaUtil.sinFormato(importeFinalPry.getText().toString()));
        if (proyecto!=null) {
            proyectotmp.setCampos(PROYECTO_FECHAENTRADA, proyecto.getLong(PROYECTO_FECHAENTRADA));
            proyectotmp.setCampos(PROYECTO_FECHAFINAL, proyecto.getLong(PROYECTO_FECHAFINAL));
        }
        proyectotmp.setCampos(PROYECTO_RUTAFOTO,path);
        proyectotmp.setCampos(PROYECTO_TOTCOMPLETADO,totcompletada);

        bundle = new Bundle();
        bundle.putSerializable(TABLA_PROYECTO, proyectotmp);
        bundle.putString("namef", namef);
        bundle.putString("namefsub", namefsub);
        icFragmentos.enviarBundleAFragment(bundle, myfragment);
        bundle = null;
    }

    protected void update() {

        new CommonPry.Calculos.Tareafechas().execute();
        //Modelo pry = ConsultaBD.queryObject(CAMPOS_PROYECTO,idProyecto);
        fechaCalculada = proyecto.getLong(PROYECTO_FECHAENTREGACALCULADA);
        calculoTotales();

       contenedor();

        consulta.updateRegistro(TABLA_PROYECTO,idProyecto,valores);

    }

    protected void delete(){

        consulta.deleteRegistro(TABLA_PROYECTO,idProyecto);
    }

    private void comprobarEstado() {

        Modelo proyecto = consulta.queryObject(CAMPOS_PROYECTO, idProyecto);


        if (proyecto!=null && proyecto.getInt(PROYECTO_TIPOESTADO) > 0) {


            btnActualizar2.setVisibility(View.GONE);

            String convertir = getString(R.string.convertir);

            switch (proyecto.getInt(PROYECTO_TIPOESTADO)) {

                case TNUEVOPRESUP:
                    btnActualizar.setText(String.format("%s %s", convertir, PRESUPPENDENTREGA));
                    break;
                case TPRESUPPENDENTREGA:
                    btnActualizar.setText(String.format("%s %s", convertir, PRESUPESPERA));
                    break;
                case TPRESUPESPERA:
                    btnActualizar.setText(String.format("%s %s", convertir, PRESUPACEPTADO));
                    btnActualizar2.setVisibility(View.VISIBLE);
                    btnActualizar2.setText(String.format("%s %s", convertir, PRESUPNOACEPTADO));
                    break;
                case TPRESUPACEPTADO:
                    importeFinalPry.setVisibility(View.VISIBLE);
                    labelimportefinal.setVisibility(View.VISIBLE);
                    importeFinalPry.setText(JavaUtil.formatoMonedaLocal
                            (proyecto.getDouble(PROYECTO_IMPORTEFINAL)));
                    btnActualizar.setText(String.format("%s %s", convertir, PROYECTEJECUCION));
                    break;
                case TPROYECTEJECUCION:
                    btnActualizar.setText(String.format("%s %s", convertir, PROYECPENDENTREGA));
                    break;
                case TPROYECPENDENTREGA:
                    btnActualizar.setText(String.format("%s %s", convertir, PROYECTPENDCOBRO));
                    break;
                case TPROYECTPENDCOBRO:
                    btnActualizar.setText(String.format("%s %s", convertir, PROYECTCOBRADO));
            }

            estadoProyecto.setGravity(View.TEXT_ALIGNMENT_CENTER);
        }

    }


    private void modificarEstado() {

        String idNuevoPresup = null;
        String idPresupPendEntrega = null;
        String idPresupEnEspera = null;
        String idPresupAceptado = null;
        String idProyEnEjecucion = null;
        String idProyPendEntrega = null;
        String idProyPendCobro = null;
        String idProyCobrado = null;

        ArrayList<Modelo> listaEstados = consulta.queryList(CAMPOS_ESTADO,null, null);

        for (Modelo estado : listaEstados) {

            switch (estado.getInt(ESTADO_TIPOESTADO)){

                case TNUEVOPRESUP:
                    idNuevoPresup = estado.getString(ESTADO_ID_ESTADO);
                    break;
                case TPRESUPPENDENTREGA:
                    idPresupPendEntrega = estado.getString(ESTADO_ID_ESTADO);
                    break;
                case TPRESUPESPERA:
                    idPresupEnEspera = estado.getString(ESTADO_ID_ESTADO);
                    break;
                case TPRESUPACEPTADO:
                    idPresupAceptado = estado.getString(ESTADO_ID_ESTADO);
                    break;
                case TPROYECTEJECUCION:
                    idProyEnEjecucion = estado.getString(ESTADO_ID_ESTADO);
                    break;
                case TPROYECPENDENTREGA:
                    idProyPendEntrega = estado.getString(ESTADO_ID_ESTADO);
                    break;
                case TPROYECTPENDCOBRO:
                    idProyPendCobro = estado.getString(ESTADO_ID_ESTADO);
                    break;
                case TPROYECTCOBRADO:
                    idProyCobrado = estado.getString(ESTADO_ID_ESTADO);
            }


        }

        if (idEstado.equals(idNuevoPresup)) {

            estadoProyecto.setText(PRESUPPENDENTREGA);
            idEstado = idPresupPendEntrega;

        } else if (idEstado.equals(idPresupPendEntrega)) {

            estadoProyecto.setText(PRESUPESPERA);
            idEstado = idPresupEnEspera;
            //showDatePickerDialogEntrega();
            btnfechaentrega.setVisibility(View.VISIBLE);
            fechaEntregaPresup.setVisibility(View.VISIBLE);
            labelfentregap.setVisibility(View.VISIBLE);

        } else if (idEstado.equals(idPresupEnEspera) && fechaEntregaP >0) {

            estadoProyecto.setText(PRESUPACEPTADO);
            idEstado = idPresupAceptado;

        } else if (idEstado.equals(idPresupEnEspera) && fechaEntregaP == 0) {

            Toast.makeText(getContext(),"Fecha entrega presup no establecida",Toast.LENGTH_SHORT).show();

        } else if (idEstado.equals(idPresupAceptado)) {

            estadoProyecto.setText(PROYECTEJECUCION);
            idEstado = idProyEnEjecucion;

        } else if (idEstado.equals(idProyEnEjecucion)) {

            estadoProyecto.setText(PROYECPENDENTREGA);
            idEstado = idProyPendEntrega;

        } else if (idEstado.equals(idProyPendEntrega)) {

            estadoProyecto.setText(PROYECTPENDCOBRO);
            idEstado = idProyPendCobro;
            if (Double.parseDouble(JavaUtil.sinFormato(importeFinalPry.getText().toString()))==0) {
                importeFinalPry.setText(JavaUtil.formatoMonedaLocal(precioPartidas));
            }

        } else if (idEstado.equals(idProyPendCobro)) {

            estadoProyecto.setText(PROYECTCOBRADO);
            idEstado = idProyCobrado;

        }

        ContentValues valores = new ContentValues();
        consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_ID_ESTADO,idEstado);
        consulta.updateRegistro(TABLA_PROYECTO,idProyecto,valores);

        modificarEstadoPartidas(idProyecto,idEstado);

        comprobarEstado();

        new CommonPry.Calculos.TareaTipoCliente().execute(idCliente);
    }

    private void modificarEstadoNoAceptado() {

        String idPresupNoAceptado;

        ArrayList<Modelo> listaEstados = consulta.queryList(CAMPOS_ESTADO,null, null);

        for (Modelo estado : listaEstados) {

            if (estado.getInt(ESTADO_TIPOESTADO) == TPRESUPNOACEPTADO) {

                idPresupNoAceptado = estado.getString(ESTADO_ID_ESTADO);
                ContentValues valores = new ContentValues();
                consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_ID_ESTADO,idPresupNoAceptado);
                consulta.updateRegistro(TABLA_PROYECTO,idProyecto,valores);

                comprobarEstado();
                break;

            }
        }

    }

    private void modificarEstadoPartidas(String idProyecto, String idEstado){

        ContentValues valores = new ContentValues();
        consulta.putDato(valores,CAMPOS_PARTIDA, PARTIDA_ID_ESTADO,idEstado);
        consulta.updateRegistrosDetalle(TABLA_PARTIDA,idProyecto,TABLA_PROYECTO,valores,null);
    }

    @Override
    protected boolean registrar() {


        contenedor();

        if (idCliente != null) {

            Modelo cliente = consulta.queryObject(CAMPOS_CLIENTE,idCliente);

            if (cliente.getString(CLIENTE_NOMBRE).equals(spClienteProyecto.getText().toString())) {

                System.out.println("valores = " + valores);
                idProyecto = consulta.idInsertRegistro(TABLA_PROYECTO, valores);
                proyecto = consulta.queryObject(CAMPOS_PROYECTO,idProyecto);
                System.out.println("idProyecto = " + idProyecto);

            } else {
                mostrarDialogoTipoCliente();
            }
        } else {
            //Toast.makeText(getContext(), "Debe elegir un cliente", Toast.LENGTH_LONG).show();
            mostrarDialogoTipoCliente();
        }
        return true;
    }

    @Override
    protected boolean contenedor() {

        valores = new ContentValues();

        consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_NOMBRE,nombrePry.getText().toString());
        consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_DESCRIPCION,descripcionPry.getText().toString());
        consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_ID_CLIENTE,idCliente);
        consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_ID_ESTADO,idEstado);
        consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_FECHAENTREGAACORDADA,fechaAcordada);
        //consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_FECHAENTREGAPRESUP,fechaEntregaP);

        if (fechaCalculada<=fechaAcordada){
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_RETRASO,0);
        }else {
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_RETRASO,(fechaCalculada - fechaAcordada));
        }
        consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_IMPORTEPRESUPUESTO,preciototal);
        consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_IMPORTEFINAL,
                JavaUtil.comprobarDouble(importeFinalPry.getText().toString()));
        if (path != null) {
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_RUTAFOTO,path);
        }
        consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_TOTCOMPLETADO,totcompletada);
        if (idProyecto==null){

            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_FECHAENTRADA,JavaUtil.hoy());
        }
        return true;
    }

    @Override
    protected void cambiarFragment() {

        bundle = new Bundle();

        if (namef.equals(AGENDA)) {

            bundle.putString("namef", namef);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentAgenda());
            bundle = null;

        } else {

            bundle.putString("namef", namef);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentProyecto());
            bundle = null;
        }

    }

    private void listaObjetosEstados() {

        objEstados = new ArrayList<>();
        String seleccion = null;
        if (namef.equals(PROYECTO)){
            seleccion = ESTADO_TIPOESTADO + " >3";
        }else if (namef.equals(PRESUPUESTO)){
            seleccion = ESTADO_TIPOESTADO + " <4";
        }else if (namef.equals(COBROS)){
            seleccion = ESTADO_TIPOESTADO + " >6";
        }else if (namef.equals(HISTORICO)){
            seleccion = ESTADO_TIPOESTADO + " == 8";
        }

        objEstados = consulta.queryList(CAMPOS_ESTADO,seleccion,null);

        obtenerListaEstados();
    }

    private void obtenerListaEstados() {

        listaEstados = new ArrayList<String>();
        listaEstados.add("Seleccione Estado");

        for (int i=0;i<objEstados.size();i++){

            listaEstados.add(objEstados.get(i).getString(ESTADO_DESCRIPCION));
        }
    }


    private void setAdaptadorClientes(final AutoCompleteTextView autoCompleteTextView) {

        listaClientes = consulta.queryList(CAMPOS_CLIENTE, null, null);

        autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(getContext(),R.layout.item_list_cliente,listaClientes,CLIENTE_NOMBRE) {
            @Override
            public void onEntrada(Modelo entrada, View view) {

                ImageView imgcli = view.findViewById(R.id.imgclilcliente);
                TextView nombreCli = view.findViewById(R.id.tvnomclilcliente);
                TextView contactoCli = view.findViewById(R.id.tvcontacclilcliente);
                TextView telefonoCli = view.findViewById(R.id.tvtelclilcliente);
                TextView emailCli = view.findViewById(R.id.tvemailclilcliente);
                TextView dirCli = view.findViewById(R.id.tvdirclilcliente);

                dirCli.setText(entrada.getString(CLIENTE_DIRECCION));

                int peso = entrada.getInt
                        (CLIENTE_PESOTIPOCLI);

                if (peso > 6) {
                    imgcli.setImageResource(R.drawable.clientev);
                } else if (peso > 3) {
                    imgcli.setImageResource(R.drawable.clientea);
                } else if (peso > 0) {
                    imgcli.setImageResource(R.drawable.clienter);
                } else {
                    imgcli.setImageResource(R.drawable.cliente);
                }

                nombreCli.setText(entrada.getString(CLIENTE_NOMBRE));
                contactoCli.setText(entrada.getString(CLIENTE_CONTACTO));
                telefonoCli.setText(entrada.getString(CLIENTE_TELEFONO));
                emailCli.setText(entrada.getString(CLIENTE_EMAIL));

            }

        });

    }

    private void mostrarDialogoTipoCliente() {

        final CharSequence[] opciones = {"Nuevo cliente","Nuevo prospecto","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Nuevo cliente")){

                    enviarProyectoTemporal(namef, CLIENTE,new FragmentCCliente());

                }else if (opciones[which].equals("Nuevo prospecto")){

                    enviarProyectoTemporal(namef, PROSPECTO,new FragmentCCliente());

                }else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void showDatePickerDialogAcordada() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance
                (fechaCalculada, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        //String selectedDate = CommonPry.twoDigits(day) + " / " +
                        //        CommonPry.twoDigits(month+1) + " / " + year;
                        fechaAcordada = JavaUtil.fechaALong(year, month, day);
                        if (fechaAcordada>0){
                            fechaEntregaPresup.setVisibility(View.VISIBLE);
                            btnfechaentrega.setVisibility(View.VISIBLE);
                            labelfentregap.setVisibility(View.VISIBLE);
                        }
                        //String selectedDate = CommonPry.formatDateForUi(year,month,day);
                        String selectedDate = JavaUtil.getDate(fechaAcordada);
                        fechaAcordadaPry.setText(selectedDate);
                        btnActualizar.setVisibility(View.VISIBLE);
                        ContentValues valores = new ContentValues();
                        consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_FECHAENTREGAACORDADA,fechaAcordada);
                        consulta.updateRegistro(TABLA_PROYECTO,idProyecto,valores);

                            new CommonPry.Calculos.Tareafechas().execute();

                    }
                });
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void showDatePickerDialogEntrega() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance
                (JavaUtil.hoy(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        //String selectedDate = CommonPry.twoDigits(day) + " / " +
                        //        CommonPry.twoDigits(month+1) + " / " + year;
                        fechaEntregaP = JavaUtil.fechaALong(year, month, day);
                        //String selectedDate = CommonPry.formatDateForUi(year,month,day);
                        String selectedDate = JavaUtil.getDate(fechaEntregaP);
                        fechaEntregaPresup.setText(selectedDate);
                        ContentValues valores = new ContentValues();
                        consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_FECHAENTREGAPRESUP,fechaEntregaP);
                        consulta.updateRegistro(TABLA_PROYECTO,idProyecto,valores);


                            new CommonPry.Calculos.Tareafechas().execute();

                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }


}






