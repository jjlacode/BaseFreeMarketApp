package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import jjlacode.com.androidutils.DatePickerFragment;
import jjlacode.com.androidutils.ICFragmentos;
import jjlacode.com.androidutils.ImagenUtil;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;

public class FragmentUDProyecto extends Fragment
        implements Common.Constantes, Contract.Tablas, Common.Estados, Common.TiposEstados {

    private String idProyecto;
    private String namef;

    private String path;
    Bitmap bitmap;

    View vista;
    ImageView imgPry;
    ImageButton imagenTipoClienteProyecto;
    ImageButton btnimgEstadoPry;
    ImageButton btnfechaacord;
    ImageButton btnfechaentrega;
    EditText nombrePry;
    EditText descripcionPry;
    Spinner spClienteProyecto;
    TextView estadoProyecto;
    TextView fechaEntradaPry;
    TextView fechaEntregaPresup;
    TextView fechaAcordadaPry;
    TextView fechaCalculadaPry;
    TextView fechaFinalPry;
    TextView totalPartidasPry;
    TextView pvpPartidas;
    TextView totalGastosPry;
    TextView importeCalculadoPry;
    EditText importeFinalPry;
    TextView labelfcalc;
    TextView labelfacor;
    TextView labelffinal;
    TextView labelfentregap;
    TextView labelimportecalculado;
    TextView labelimportefinal;
    Button btnSavePry;
    Button btnDelPry;
    Button btnbackpry;
    Button btnEvento;
    Button btnPartidasPry;
    Button btnGastosPry;
    Button btnActualizar;
    Button btnActualizar2;
    ArrayList<String> listaClientes;
    ArrayList<Modelo> objClientes;
    String idCliente;
    int peso = 0;
    int posCliente = 0;
    long fechaCalculada = 0;
    long fechaAcordada = 0;
    long fechaEntregaP = 0;
    String idEstado;
    int totcompletada = 0;
    double totPartidas = 0.0;
    double precioPartidas = 0.0;
    double totGastos = 0.0;
    double impTotalCalculado = 0.0;
    double importeFinal = 0.0;
    Bundle bundle = null;
    Modelo proyecto = null;

    private AppCompatActivity activity;
    private ICFragmentos icFragmentos;
    ImagenUtil imagen;

    public FragmentUDProyecto() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_ud_proyecto, container, false);

        imgPry = vista.findViewById(R.id.imudpry);
        imagenTipoClienteProyecto = vista.findViewById(R.id.imgbtntipocliudpry);
        btnimgEstadoPry = vista.findViewById(R.id.imgbtnestudpry);
        nombrePry = vista.findViewById(R.id.etnomudpry);
        descripcionPry = vista.findViewById(R.id.etdescudpry);
        spClienteProyecto = vista.findViewById(R.id.sptipocliudpry);
        estadoProyecto = vista.findViewById(R.id.tvestudproy);
        fechaEntradaPry = vista.findViewById(R.id.proyecto_ud_tv_fecha_entrada);
        fechaEntregaPresup = vista.findViewById(R.id.tvfentpresuppry);
        fechaCalculadaPry = vista.findViewById(R.id.tvfcalcudpry);
        fechaAcordadaPry = vista.findViewById(R.id.tvfacorudpry);
        fechaFinalPry = vista.findViewById(R.id.tvffinudpry);
        totalPartidasPry = vista.findViewById(R.id.tvtotpartudpry);
        pvpPartidas = vista.findViewById(R.id.tvpreciopartidasudpry);
        totalGastosPry = vista.findViewById(R.id.tvtotgastudpry);
        importeCalculadoPry = vista.findViewById(R.id.tvimpcaludpry);
        importeFinalPry = vista.findViewById(R.id.etimpfinudpry);
        btnSavePry = vista.findViewById(R.id.btnsaveudpry);
        btnDelPry = vista.findViewById(R.id.btndeludpry);
        btnbackpry = vista.findViewById(R.id.btnbackudpry);
        btnEvento = vista.findViewById(R.id.btneventoudpry);
        btnPartidasPry = vista.findViewById(R.id.btnpartudpry);
        btnGastosPry = vista.findViewById(R.id.btngasudpry);
        btnActualizar = vista.findViewById(R.id.btnactualizar);
        btnActualizar2 = vista.findViewById(R.id.btnactualizar2);
        btnfechaacord = vista.findViewById(R.id.btnfechaacord);
        btnfechaentrega = vista.findViewById(R.id.btnfechaentrega);
        labelfcalc = vista.findViewById(R.id.lfcalcudpry);
        labelfacor = vista.findViewById(R.id.lfacorudpry);
        labelffinal = vista.findViewById(R.id.lffinudpry);
        labelfentregap = vista.findViewById(R.id.lfentpresupudpry);
        labelimportecalculado = vista.findViewById(R.id.limpcaludpry);
        labelimportefinal = vista.findViewById(R.id.limpfinudpry);

        bundle = getArguments();

        if (bundle != null) {
            proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
            namef = bundle.getString("namef");
            bundle = null;
            bundle = new Bundle();
            bundle.putSerializable(TABLA_PROYECTO,proyecto);
            icFragmentos.enviarBundleAActivity(bundle);
            bundle = null;
            idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);
            proyecto = QueryDB.queryObject(CAMPOS_PROYECTO,idProyecto);
            calculoTotales();
        }

        nombrePry.setText(proyecto.getString(PROYECTO_NOMBRE));
        descripcionPry.setText(proyecto.getString(PROYECTO_DESCRIPCION));
        idCliente = proyecto.getString(PROYECTO_ID_CLIENTE);
        idEstado = proyecto.getString(PROYECTO_ID_ESTADO);
        idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);
        fechaEntradaPry.setText(JavaUtil.getDate(proyecto.getLong(PROYECTO_FECHAENTRADA)));
        if (proyecto.getLong(PROYECTO_FECHAENTREGAPRESUP) == 0) {
            fechaEntregaPresup.setText
                    (R.string.establecer_fecha_entrega_presup);
        }else if (proyecto.getLong(PROYECTO_FECHAENTREGAPRESUP) > 0) {
            fechaEntregaPresup.setText(JavaUtil.getDate(Long.parseLong(proyecto.getCampos(PROYECTO_FECHAENTREGAPRESUP))));
        }

        //calculoTotales();//Calculo de totales de tiempos e importes de partidas y gastos

        totalPartidasPry.setText(String.format(Locale.getDefault(),"%#.2f",totPartidas));

        pvpPartidas.setText(String.format(Locale.getDefault(),"%#.2f %s", precioPartidas, JavaUtil.monedaLocal()));



        totalGastosPry.setText(JavaUtil.formatoMonedaLocal(totGastos));

        impTotalCalculado = precioPartidas + totGastos;
        importeCalculadoPry.setText(String.format(Locale.getDefault(),"%#.2f %s", impTotalCalculado, JavaUtil.monedaLocal()));

        if (totPartidas == 0) {

            fechaAcordadaPry.setVisibility(View.GONE);
            fechaCalculadaPry.setVisibility(View.GONE);
            btnfechaacord.setVisibility(View.GONE);
            btnfechaentrega.setVisibility(View.GONE);
            labelfacor.setVisibility(View.GONE);
            labelfcalc.setVisibility(View.GONE);
            btnActualizar.setVisibility(View.GONE);
            pvpPartidas.setVisibility(View.GONE);

        } else {

            fechaAcordadaPry.setVisibility(View.VISIBLE);
            fechaCalculadaPry.setVisibility(View.VISIBLE);
            btnfechaacord.setVisibility(View.VISIBLE);
            labelfacor.setVisibility(View.VISIBLE);
            labelfcalc.setVisibility(View.VISIBLE);
            btnActualizar.setVisibility(View.VISIBLE);
            pvpPartidas.setVisibility(View.VISIBLE);

            if (proyecto.getLong(PROYECTO_FECHAENTREGAACORDADA) == 0) {

                fechaAcordadaPry.setText("No asignada");
                btnActualizar.setVisibility(View.GONE);
                btnfechaentrega.setVisibility(View.GONE);

            } else {

                fechaAcordada = proyecto.getLong(PROYECTO_FECHAENTREGAACORDADA);
                fechaAcordadaPry.setText(JavaUtil.getDate(fechaAcordada));
                btnActualizar.setVisibility(View.VISIBLE);
                if (proyecto.getInt(PROYECTO_TIPOESTADO)>3){

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

        if (proyecto.getLong(PROYECTO_FECHAENTREGAPRESUP) == 0) {
            fechaEntregaPresup.setVisibility(View.GONE);
            labelfentregap.setVisibility(View.GONE);
        } else {
            fechaEntregaPresup.setVisibility(View.VISIBLE);
            labelfentregap.setVisibility(View.VISIBLE);
            fechaEntregaP = proyecto.getLong(PROYECTO_FECHAENTREGAPRESUP);
            fechaEntregaPresup.setText(JavaUtil.getDate(fechaEntregaP));
            btnfechaacord.setVisibility(View.GONE);
        }

        if (impTotalCalculado == 0) {
            importeCalculadoPry.setVisibility(View.GONE);
            labelimportecalculado.setVisibility(View.GONE);
        } else {
            importeCalculadoPry.setVisibility(View.VISIBLE);
            labelimportecalculado.setVisibility(View.VISIBLE);
            importeCalculadoPry.setText(JavaUtil.formatoMonedaLocal
                    (impTotalCalculado));
        }

        if (proyecto.getInt(PROYECTO_TIPOESTADO)<4) {
            importeFinalPry.setVisibility(View.GONE);
            labelimportefinal.setVisibility(View.GONE);
        } else {
            importeFinalPry.setVisibility(View.VISIBLE);
            labelimportefinal.setVisibility(View.VISIBLE);
            importeFinalPry.setText(JavaUtil.formatoMonedaLocal
                    (proyecto.getDouble(PROYECTO_IMPORTEFINAL)));
        }

        if (proyecto.getCampos(PROYECTO_RUTAFOTO) != null) {
            bitmap = BitmapFactory.decodeFile(proyecto.getString(PROYECTO_RUTAFOTO));
            imgPry.setImageBitmap(bitmap);
        }
        estadoProyecto.setText(proyecto.getString(PROYECTO_DESCRIPCION_ESTADO));

        switch (namef) {

            case PRESUPUESTO:
                btnimgEstadoPry.setVisibility(View.GONE);
                break;
            default:
                long retraso = Long.parseLong(proyecto.getCampos(PROYECTO_RETRASO));
                if (retraso > 3 * Common.DIASLONG) {
                    btnimgEstadoPry.setImageResource(R.drawable.alert_box_r);
                } else if (retraso > Common.DIASLONG) {
                    btnimgEstadoPry.setImageResource(R.drawable.alert_box_a);
                } else {
                    btnimgEstadoPry.setImageResource(R.drawable.alert_box_v);
                }

        }

        comprobarEstado();


        if (Common.permiso) {
            imgPry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mostrarDialogoOpciones();


                }

            });
        }


            objClientes = QueryDB.queryList(CAMPOS_CLIENTE,null, null);

        listaClientes = new ArrayList<String>();

        for (int i = 0; i < objClientes.size(); i++) {

            listaClientes.add(objClientes.get(i).getString(CLIENTE_NOMBRE));
            if (objClientes.get(i).getString(CLIENTE_ID_CLIENTE).equals(idCliente)) {
                posCliente = i;
            }
        }

        ArrayAdapter<CharSequence> adaptadorCliente = new ArrayAdapter
                (getContext(), android.R.layout.simple_spinner_item, listaClientes);

        spClienteProyecto.setAdapter(adaptadorCliente);
        spClienteProyecto.setSelection(posCliente);

        spClienteProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                idCliente = objClientes.get(position).getString(CLIENTE_ID_CLIENTE);
                peso = objClientes.get(position).getInt(CLIENTE_PESOTIPOCLI);
                posCliente = position;
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnDelPry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int res = QueryDB.deleteRegistro(TABLA_PROYECTO,idProyecto);

                if (res > 0) {

                    new Common.Calculos.Tareafechas().execute();
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

            }
        });
        btnSavePry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int res = modificarProyecto();

                if (res > 0) {


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
            }
        });

        btnbackpry.setOnClickListener(new View.OnClickListener() {
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

                modificarProyecto();
                Modelo cliente = QueryDB.queryObject(CAMPOS_CLIENTE,idCliente);

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

                modificarProyecto();
                enviarProyectoTemporal(namef, PARTIDA, new FragmentPartidasProyecto());

            }
        });
        btnGastosPry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modificarProyecto();
                enviarProyectoTemporal(namef, GASTO, new FragmentGastoProyecto());

            }
        });
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modificarEstado();
                modificarProyecto();

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
                modificarProyecto();
            }
        });
        imagenTipoClienteProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modificarProyecto();
                Modelo cliente = QueryDB.queryObject(CAMPOS_CLIENTE,idCliente);

                bundle = new Bundle();
                bundle.putSerializable(TABLA_CLIENTE,cliente);
                bundle.putSerializable(TABLA_PROYECTO, proyecto);
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentUDCliente());
                bundle = null;
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


        return vista;
    }

    private void calculoTotales() {

        ArrayList<Modelo> listaPartidas = QueryDB.queryListDetalle(CAMPOS_PARTIDA,idProyecto,TABLA_PROYECTO);

        int x = 0;
        for (int i = 0; i < listaPartidas.size(); i++) {

            double tiempo = listaPartidas.get(i).getDouble(PARTIDA_TIEMPO);
            double cantidad = listaPartidas.get(i).getDouble(PARTIDA_CANTIDAD);
            int completada = listaPartidas.get(i).getInt(PARTIDA_COMPLETADA);
            totPartidas += (tiempo * cantidad);
            precioPartidas += totPartidas * Common.hora;
            totcompletada += completada;

            x = i;
        }
        totcompletada = (int) (Math.round(((double) totcompletada) / x));

        ArrayList<Modelo> listaGastos = QueryDB.queryListDetalle
                (CAMPOS_GASTO,idProyecto,TABLA_PROYECTO);

        for (int i = 0; i < listaGastos.size(); i++) {

            double  importe = listaGastos.get(i).getDouble(GASTO_IMPORTE);
            double cantidad = listaGastos.get(i).getDouble(GASTO_CANTIDAD);
            double beneficio = listaGastos.get(i).getDouble(GASTO_BENEFICIO);
            totGastos += (importe * cantidad)+(((importe * cantidad)/100)*beneficio);
        }
    }

    private void enviarProyectoTemporal(String namef, String namefsub, Fragment myfragment) {

        new Common.Calculos.Tareafechas().execute();
        Modelo pry = QueryDB.queryObject(CAMPOS_PROYECTO,idProyecto);
        fechaCalculada = pry.getLong(PROYECTO_FECHAENTREGACALCULADA);

        Modelo proyectotmp = new Modelo(CAMPOS_PROYECTO);
        proyectotmp.setCampos(PROYECTO_ID_PROYECTO,idProyecto);
        proyectotmp.setCampos(PROYECTO_ID_ESTADO,idEstado);
        proyectotmp.setCampos(PROYECTO_NOMBRE,nombrePry.getText().toString());
        proyectotmp.setCampos(PROYECTO_DESCRIPCION,descripcionPry.getText().toString());
        proyectotmp.setCampos(PROYECTO_ID_CLIENTE,idCliente);
        proyectotmp.setCampos(PROYECTO_FECHAENTREGAACORDADA,fechaAcordada);
        proyectotmp.setCampos(PROYECTO_FECHAENTREGAPRESUP,fechaEntregaP);
        proyectotmp.setCampos(PROYECTO_FECHAENTREGACALCULADA,fechaCalculada);
        if (fechaCalculada<=fechaAcordada){proyectotmp.setCampos(PROYECTO_RETRASO,0);
        }else {
            proyectotmp.setCampos(PROYECTO_RETRASO,fechaCalculada - fechaAcordada);
        }
        proyectotmp.setCampos(PROYECTO_IMPORTEPRESUPUESTO,impTotalCalculado);
        proyectotmp.setCampos(PROYECTO_IMPORTEFINAL,importeFinal);
        proyectotmp.setCampos(PROYECTO_FECHAENTRADA,proyecto.getLong(PROYECTO_FECHAENTRADA));
        proyectotmp.setCampos(PROYECTO_FECHAFINAL,proyecto.getLong(PROYECTO_FECHAFINAL));

        bundle = new Bundle();
        bundle.putSerializable(TABLA_PROYECTO, proyectotmp);
        bundle.putString("namef", namef);
        bundle.putString("namefsub", namefsub);
        icFragmentos.enviarBundleAFragment(bundle, myfragment);
        bundle = null;
    }

    private int modificarProyecto() {

        new Common.Calculos.Tareafechas().execute();
        //Modelo pry = QueryDB.queryObject(CAMPOS_PROYECTO,idProyecto);
        fechaCalculada = proyecto.getLong(PROYECTO_FECHAENTREGACALCULADA);

        ContentValues valores = new ContentValues();
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_NOMBRE,nombrePry.getText().toString());
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_DESCRIPCION,descripcionPry.getText().toString());
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_ID_CLIENTE,idCliente);
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_FECHAENTREGAACORDADA,fechaAcordada);
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_FECHAENTREGAPRESUP,fechaEntregaP);

        if (fechaCalculada<=fechaAcordada){QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_RETRASO,0);
        }else {
            QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_RETRASO,(fechaCalculada - fechaAcordada));
        }
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_IMPORTEPRESUPUESTO,impTotalCalculado);
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_IMPORTEFINAL,importeFinal);
        if (path != null) {
            QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_RUTAFOTO,path);
        }
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_TOTCOMPLETADO,totcompletada);

        return QueryDB.updateRegistro(TABLA_PROYECTO,idProyecto,valores);

    }


    private void comprobarEstado() {

        Modelo proyecto = QueryDB.queryObject(CAMPOS_PROYECTO, idProyecto);


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

        ArrayList<Modelo> listaEstados = QueryDB.queryList(CAMPOS_ESTADO,null, null);

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
            importeFinal = impTotalCalculado;
            importeFinalPry.setText(JavaUtil.formatoMonedaLocal(importeFinal));

        } else if (idEstado.equals(idProyPendCobro)) {

            estadoProyecto.setText(PROYECTCOBRADO);
            idEstado = idProyCobrado;

        }

        ContentValues valores = new ContentValues();
        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_ID_ESTADO,idEstado);
        QueryDB.updateRegistro(TABLA_PROYECTO,idProyecto,valores);

        modificarEstadoPartidas(idProyecto,idEstado);

        comprobarEstado();

        new Common.Calculos.TareaTipoCliente().execute(idCliente);
    }

    private void modificarEstadoNoAceptado() {

        String idPresupNoAceptado;

        ArrayList<Modelo> listaEstados = QueryDB.queryList(CAMPOS_ESTADO,null, null);

        for (Modelo estado : listaEstados) {

            if (estado.getInt(ESTADO_TIPOESTADO) == TPRESUPNOACEPTADO) {

                idPresupNoAceptado = estado.getString(ESTADO_ID_ESTADO);
                ContentValues valores = new ContentValues();
                QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_ID_ESTADO,idPresupNoAceptado);
                QueryDB.updateRegistro(TABLA_PROYECTO,idProyecto,valores);

                comprobarEstado();
                break;

            }
        }

    }

    private void modificarEstadoPartidas(String idProyecto, String idEstado){

        ContentValues valores = new ContentValues();
        QueryDB.putDato(valores,CAMPOS_PARTIDA,PARTIDA_ID_ESTADO,idEstado);
        QueryDB.updateRegistrosDetalle(TABLA_PARTIDA,idProyecto,TABLA_PROYECTO,valores,null);
    }
    /*
    private void mostrarDialogoOpcionesImagen() {

        final CharSequence[] opciones = {"Hacer foto desde cámara", "Elegir de la galería", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Hacer foto desde cámara")) {

                    abrirCamara();

                } else if (opciones[which].equals("Elegir de la galería")) {

                    abrirGaleria();

                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void abrirGaleria(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                MediaStore.ImagenUtil.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult
                (intent.createChooser(intent, "Seleccione"), COD_SELECCIONA);
    }

    private void abrirCamara() {

        File dir = null;
        File dir_path = null;
        boolean iscreada = false;

        dir_path = Environment.getExternalStorageDirectory();
        dir = new File(dir_path.getAbsolutePath(),DIRECTORIO_IMAGEN);
        iscreada = dir.isDirectory();

        if (!iscreada) {

            iscreada = dir.mkdirs();

        }

        Long consecutivo = System.currentTimeMillis() / 100;
        String nombre = consecutivo.toString() + ".jpg";

        String path = nombre;

        File fileImagen = new File(dir.getAbsolutePath(),path);
            //fileImagen = new File(path);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                Uri uri = FileProvider.getUriForFile(getContext(),
                        BuildConfig.APPLICATION_ID + ".providerFreelanceProject", fileImagen);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                if (iscreada) {
                    startActivityForResult(intent, COD_FOTO);
                }

            } else {


                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));

                if (iscreada) {
                    startActivityForResult(intent, COD_FOTO);
                }
            }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ContentResolver resolver = getActivity().getContentResolver();

        switch (requestCode) {

            case COD_SELECCIONA:
                Uri mipath = data.getData();
                imgPry.setImageURI(mipath);
                File imageFile = new File(Common.getRealPathFromURI(mipath, resolver));
                path = imageFile.getPath();
                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {

                                Log.i("Path", "" + path);
                            }
                        });
                bitmap = BitmapFactory.decodeFile(path);
                imgPry.setImageBitmap(bitmap);

                break;
        }
    }

    */
    public void mostrarDialogoOpciones() {

        final CharSequence[] opciones = {"Hacer foto desde cámara", "Elegir de la galería", "Cancelar"};
        final androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                imagen = new ImagenUtil(getContext());

                if (opciones[which].equals("Hacer foto desde cámara")) {

                            try {
                                startActivityForResult(imagen.takePhotoIntent(), COD_FOTO);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            imagen.addToGallery();

                } else if (opciones[which].equals("Elegir de la galería")) {

                startActivityForResult(imagen.openGalleryIntent(), COD_SELECCIONA);

                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String photoPath;

        switch (requestCode) {

            case COD_SELECCIONA:
                imagen.setPhotoUri(data.getData());
                photoPath = imagen.getPath();
                try {
                    Bitmap bitmap = ImagenUtil.ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    imgPry.setImageBitmap(bitmap);
                    path = photoPath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case COD_FOTO:
                photoPath = imagen.getPhotoPath();
                try {
                    Bitmap bitmap = ImagenUtil.ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    imgPry.setImageBitmap(bitmap); //imageView is your ImageView
                    path = photoPath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (AppCompatActivity) context;
            icFragmentos = (ICFragmentos) this.activity;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void showDatePickerDialogAcordada() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance
                (fechaCalculada, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        //String selectedDate = Common.twoDigits(day) + " / " +
                        //        Common.twoDigits(month+1) + " / " + year;
                        fechaAcordada = JavaUtil.fechaALong(year, month, day);
                        //String selectedDate = Common.formatDateForUi(year,month,day);
                        String selectedDate = JavaUtil.getDate(fechaAcordada);
                        fechaAcordadaPry.setText(selectedDate);
                        btnActualizar.setVisibility(View.VISIBLE);
                        ContentValues valores = new ContentValues();
                        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_FECHAENTREGAACORDADA,fechaAcordada);
                        int res = QueryDB.updateRegistro(TABLA_PROYECTO,idProyecto,valores);
                        if (res > 0) {

                            new Common.Calculos.Tareafechas().execute();
                        }
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
                        //String selectedDate = Common.twoDigits(day) + " / " +
                        //        Common.twoDigits(month+1) + " / " + year;
                        fechaEntregaP = JavaUtil.fechaALong(year, month, day);
                        //String selectedDate = Common.formatDateForUi(year,month,day);
                        String selectedDate = JavaUtil.getDate(fechaEntregaP);
                        fechaEntregaPresup.setText(selectedDate);
                        ContentValues valores = new ContentValues();
                        QueryDB.putDato(valores,CAMPOS_PROYECTO,PROYECTO_FECHAENTREGAPRESUP,fechaEntregaP);
                        int res = QueryDB.updateRegistro(TABLA_PROYECTO,idProyecto,valores);
                        if (res > 0) {

                            new Common.Calculos.Tareafechas().execute();
                        }
                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }


}






