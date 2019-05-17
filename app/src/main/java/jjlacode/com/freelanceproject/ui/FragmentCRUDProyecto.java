package jjlacode.com.freelanceproject.ui;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.AppActivity;
import jjlacode.com.freelanceproject.util.BaseViewHolder;
import jjlacode.com.freelanceproject.util.DatePickerFragment;
import jjlacode.com.freelanceproject.util.FragmentCRUD;
import jjlacode.com.freelanceproject.util.ImagenUtil;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.ListaAdaptadorFiltro;
import jjlacode.com.freelanceproject.util.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.ListaModelo;
import jjlacode.com.freelanceproject.util.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.CommonPry;
import jjlacode.com.freelanceproject.templates.PresupuestoPDF;
import jjlacode.com.freelanceproject.util.TipoViewHolder;

public class FragmentCRUDProyecto extends FragmentCRUD
        implements CommonPry.Constantes, ContratoPry.Tablas, CommonPry.Estados,
        CommonPry.TiposEstados {

    private ImageButton imagenTipoClienteProyecto;
    private ImageButton btnfechaentrega;
    private AutoCompleteTextView spClienteProyecto;
    private EditText nombrePry;
    private EditText descripcionPry;
    private TextView estadoProyecto;
    private TextView fechaEntregaPresup;
    private TextView fechaAcordadaPry;
    private EditText importeFinalPry;
    private TextView labelfentregap;
    private TextView labelimportefinal;
    private TextView labelhoras;
    private TextView labelfentrada;
    private TextView labeltotpartidas;
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
    private Button btnEvento;
    private Button btnPartidasPry;
    private Button btnActualizar;
    private Button btnActualizar2;
    private Button btncompartirPdf;
    private Button btnVerPdf;
    private Button btnenviarPdf;
    private Button btnVerEventos;
    private ImageButton btnimgEstadoPry;
    private ImageButton btnfechaacord;
    private Spinner spEstadoProyecto;


    private ArrayList<Modelo> objClientes;
    private ArrayList<String> listaEstados;
    private ArrayList<Modelo> listaClientes;
    private ArrayList<Modelo> objEstados;

    private String idCliente;
    private String idEstado;
    private String nombreCliente;

    private int peso = 0;
    private int posCliente = 0;
    private int totcompletada = 0;

    private long fechaCalculada = 0;
    private long fechaAcordada = 0;
    private long fechaEntregaP = 0;

    private double totPartidas = 0.0;
    private double precioPartidas = 0.0;
    private double preciototal= 0.0;

    private Button btnpresupuestos;
    private Button btnproyectos;
    private Button btncobros;
    private Button btnhistorico;

    private String actualtemp;

    public FragmentCRUDProyecto() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view){

        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroRV setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroRV(context,layoutItem,lista,campos);
    }

    @Override
    protected void setLista() {

        btnpresupuestos.setVisibility(View.VISIBLE);
        btnproyectos.setVisibility(View.VISIBLE);
        btncobros.setVisibility(View.VISIBLE);
        btnhistorico.setVisibility(View.VISIBLE);

        btnpresupuestos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),PRESUPUESTOS, Toast.LENGTH_SHORT).show();
                actual = PRESUPUESTO;
                actualtemp = actual;
                    actualizarConsultas(listab);
            }
        });

        btnproyectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),PROYECTOS, Toast.LENGTH_SHORT).show();
                actual = PROYECTO;
                actualtemp = actual;
                actualizarConsultas(listab);
            }
        });

        btncobros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),PROYCOBROS, Toast.LENGTH_SHORT).show();
                actual = COBROS;
                actualtemp = PROYECTO;
                actualizarConsultas(listab);
            }
        });

        btnhistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),PROYHISTORICO, Toast.LENGTH_SHORT).show();
                actual = HISTORICO;
                actualtemp = PROYECTO;
                actualizarConsultas(listab);
            }
        });

        actualizarConsultas(listab);



    }

    protected void actualizarConsultas(ListaModelo lista) {

        new CommonPry.Calculos.TareaActualizarProys().execute();

        if (lista==null) {
            setListaModelo();
            lista = this.lista;
        }else{
            inicio.setVisibility(View.VISIBLE);
        }

        System.out.println("lista.size() = " + lista.size());

        if (lista.chech()) {

            ArrayList<Modelo> listatemp = new ArrayList<>();

            for (Modelo item : lista.getLista()) {

                int estado = item.getInt(PROYECTO_TIPOESTADO);

                switch (actual) {

                    case PRESUPUESTO:

                        if (estado >= 1 && estado <= 3) {
                            listatemp.add(item);
                        }

                        break;

                    case PROYECTO:

                            if (estado > 3 && estado <= 6) {
                                listatemp.add(item);
                            }

                        break;

                    case COBROS:

                            if (estado == 7) {
                                listatemp.add(item);
                            }

                        break;

                    case HISTORICO:

                            if (estado == 8 || estado == 0) {
                                listatemp.add(item);
                            }

                        break;
                }
            }
            System.out.println("listatemp = " + listatemp.size());
            setListaModelo(listatemp);
            System.out.println("lista final= " + lista.size());

        }
        setRv();
        enviarAct();

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_cud_proyecto;
        layoutCabecera = R.layout.cabacera_crud_proyecto;
        layoutitem = R.layout.item_list_proyecto;

    }

    @Override
    protected void setTabla() {

        tabla = TABLA_PROYECTO;
    }

    @Override
    protected void setTablaCab() {

        tablaCab = null;

    }

    @Override
    protected void setContext() {

        contexto = getContext();

    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_PROYECTO;

    }

    @Override
    protected void setCampoID() {

        campoID = PROYECTO_ID_PROYECTO;

    }

    @Override
    protected void setBundle() {

        if (bundle != null) {

            if (modelo !=null) {
                id = getID();
                idCliente = getString(PROYECTO_ID_CLIENTE);
            }else{

                id =null;
            }
            if (id !=null) {
                new CommonPry.Calculos.TareaActualizaProy().execute(id);
            }
            if(bundle.containsKey(CLIENTE)){
                idCliente = bundle.getString(CLIENTE);
            }

        }

    }

    @Override
    protected void setAcciones() {

            setLista();

        btnVerPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PresupuestoPDF presupuestoPDF = new PresupuestoPDF();
                presupuestoPDF.setNombreArchivo(id);
                presupuestoPDF.verPDF(icFragmentos,bundle,getContext());

            }
        });

        btnenviarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PresupuestoPDF presupuestoPDF = new PresupuestoPDF();
                presupuestoPDF.setNombreArchivo(id);
                presupuestoPDF.enviarPDFEmail(icFragmentos,getContext(),null,null,null);

            }
        });

        btncompartirPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PresupuestoPDF presupuestoPDF = new PresupuestoPDF();
                presupuestoPDF.setNombreArchivo(id);
                presupuestoPDF.compartirPDF(icFragmentos,getContext());

            }
        });

        btnEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                Modelo cliente = consulta.queryObject(CAMPOS_CLIENTE,idCliente);

                bundle = new Bundle();
                bundle.putSerializable(TABLA_CLIENTE,cliente);
                bundle.putSerializable(TABLA_PROYECTO, modelo);
                bundle.putString(SUBTITULO, actualtemp);
                bundle.putString(ORIGEN, actualtemp);
                bundle.putBoolean(NUEVOREGISTRO, true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
                bundle = null;
            }
        });

        btnVerEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                Modelo cliente = consulta.queryObject(CAMPOS_CLIENTE,idCliente);

                bundle = new Bundle();
                bundle.putSerializable(TABLA_CLIENTE,cliente);
                bundle.putSerializable(TABLA_PROYECTO, modelo);
                bundle.putString(SUBTITULO, actualtemp);
                bundle.putString(ORIGEN, actualtemp);
                bundle.putString(IDREL, id);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
                bundle = null;
            }
        });

        btnPartidasPry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                bundle.putSerializable(PROYECTO,modelo);
                bundle.putString(ORIGEN,PARTIDA);
                bundle.putString(SUBTITULO, actualtemp);
                bundle.putString(ORIGEN, actualtemp);
                bundle.putString(ID,id);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaProyecto());

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

                setDatos();

            }
        });
        btnActualizar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modificarEstadoNoAceptado();
                update();
                setDatos();
            }
        });
        imagenTipoClienteProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    mostrarDialogoTipoCliente();
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


    }

    @Override
    protected void setTitulo() {

        tituloSingular = R.string.proyecto;
        tituloPlural = R.string.proyectos;
    }


    @Override
    protected void setInicio() {

        imagen = setImageView(R.id.imudpry);
        imagenTipoClienteProyecto = setImageButton(R.id.imgbtntipocliudpry);
        btnimgEstadoPry = setImageButton(R.id.imgbtnestudpry);
        nombrePry = setEditText(R.id.etnomudpry);
        descripcionPry = setEditText(R.id.etdescudpry);
        spClienteProyecto = setAutoCompleteTextView(R.id.sptipocliudpry);
        spEstadoProyecto = setSpinner(R.id.spestudpry);
        estadoProyecto = setTextView(R.id.tvestudproy);
        fechaEntradaPry = setTextView(R.id.proyecto_ud_tv_fecha_entrada);
        fechaEntregaPresup = setTextView(R.id.tvfentpresuppry);
        fechaCalculadaPry = setTextView(R.id.tvfcalcudpry);
        fechaAcordadaPry = setTextView(R.id.tvfacorudpry);
        fechaFinalPry = setTextView(R.id.tvffinudpry);
        totalPartidasPry = setTextView(R.id.tvtotpartudpry);
        pvpPartidas = setTextView(R.id.tvpreciopartidasudpry);
        importeCalculadoPry = setTextView(R.id.tvimpcaludpry);
        importeFinalPry = setEditText(R.id.etimpfinudpry);
        btnEvento = setButton(R.id.btneventoudpry);
        btnPartidasPry = setButton(R.id.btnpartudpry);
        btnActualizar = setButton(R.id.btnactualizar);
        btnActualizar2 = setButton(R.id.btnactualizar2);
        btnfechaacord = setImageButton(R.id.btnfechaacord);
        btnfechaentrega = setImageButton(R.id.btnfechaentrega);
        labelfcalc = setTextView(R.id.lfcalcudpry);
        labelfacor = setTextView(R.id.lfacorudpry);
        labelffinal = setTextView(R.id.lffinudpry);
        labelfentregap = setTextView(R.id.lfentpresupudpry);
        labelimportecalculado = setTextView(R.id.limpcaludpry);
        labelimportefinal = setTextView(R.id.limpfinudpry);
        labelfentrada = setTextView(R.id.lfentudpry);
        labelhoras = setTextView(R.id.lpreciopartidasudpry);
        labeltotpartidas = setTextView(R.id.ltotpartudpry);
        btnVerPdf = setButton(R.id.btnverpdfudpry);
        btnenviarPdf = setButton(R.id.btnenviarpdfudpry);
        btncompartirPdf = setButton(R.id.btncompartirpdfudpry);
        btnpresupuestos = view.findViewById(R.id.btnpresuplpry);
        btnproyectos = view.findViewById(R.id.btnproyectoslpry);
        btncobros = view.findViewById(R.id.btnproycobroslpry);
        btnhistorico = view.findViewById(R.id.btnhistoricopry);
        btnVerEventos = view.findViewById(R.id.btnvereventosudpry);

        btnpresupuestos.setVisibility(View.GONE);
        btnproyectos.setVisibility(View.GONE);
        btncobros.setVisibility(View.GONE);
        btnhistorico.setVisibility(View.GONE);

    }



    @Override
    protected void setDatos() {

        spEstadoProyecto.setVisibility(View.GONE);


        labeltotpartidas.setVisibility(View.VISIBLE);
        labelfentrada.setVisibility(View.VISIBLE);
        labelhoras.setVisibility(View.VISIBLE);
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

        String seleccion = EVENTO_PROYECTOREL+" = '"+id+"'";
        if (consulta.checkQueryList(CAMPOS_EVENTO,seleccion,null)){
            btnVerEventos.setVisibility(View.VISIBLE);
        }else {
            btnVerEventos.setVisibility(View.GONE);
        }

        if (actualtemp.equals(PRESUPUESTO)|| actualtemp.equals(PROYECTO) ) {
            frLista.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }

        if (getString(PROYECTO_RUTAFOTO)!=null){
            setImagenUriCircle(contexto,getString(PROYECTO_RUTAFOTO));
        }

        nombrePry.setText(getString(PROYECTO_NOMBRE));
        descripcionPry.setText(getString(PROYECTO_DESCRIPCION));
        idCliente = getString(PROYECTO_ID_CLIENTE);
        idEstado = getString(PROYECTO_ID_ESTADO);
        //id = modelo.getString(PROYECTO_ID_PROYECTO);
        fechaEntradaPry.setText(JavaUtil.getDateTime(getLong(PROYECTO_FECHAENTRADA)));
        if (getLong(PROYECTO_FECHAENTREGAPRESUP) == 0) {
            fechaEntregaPresup.setText
                    (R.string.establecer_fecha_entrega_presup);
        } else if (getLong(PROYECTO_FECHAENTREGAPRESUP) > 0) {
            fechaEntregaPresup.setText(JavaUtil.getDate(getLong(PROYECTO_FECHAENTREGAPRESUP)));
        }

        calculoTotales();

        totalPartidasPry.setText(JavaUtil.getDecimales(totPartidas));

        pvpPartidas.setText(JavaUtil.formatoMonedaLocal(precioPartidas));

        importeCalculadoPry.setText(JavaUtil.formatoMonedaLocal(preciototal));

        if (preciototal == 0) {

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

            if (modelo.getLong(PROYECTO_FECHAENTREGAACORDADA) == 0) {

                fechaAcordadaPry.setText("No asignada");
                btnActualizar.setVisibility(View.GONE);


            } else {

                fechaAcordada = modelo.getLong(PROYECTO_FECHAENTREGAACORDADA);
                fechaAcordadaPry.setText(JavaUtil.getDate(fechaAcordada));
                btnActualizar.setVisibility(View.VISIBLE);
                if (modelo.getInt(PROYECTO_TIPOESTADO) > 2) {

                    btnfechaentrega.setVisibility(View.VISIBLE);
                    fechaEntregaPresup.setVisibility(View.VISIBLE);
                    labelfentregap.setVisibility(View.VISIBLE);
                }

            }


            fechaCalculada = modelo.getLong(PROYECTO_FECHAENTREGACALCULADA);
            fechaCalculadaPry.setText(JavaUtil.getDate(fechaCalculada));

        }


        if (modelo.getLong(PROYECTO_FECHAFINAL) == 0) {
            fechaFinalPry.setVisibility(View.GONE);
            labelffinal.setVisibility(View.GONE);
        } else {
            fechaFinalPry.setVisibility(View.VISIBLE);
            labelffinal.setVisibility(View.VISIBLE);
            fechaFinalPry.setText(JavaUtil.getDate(modelo.getLong(PROYECTO_FECHAFINAL)));
        }


        if (preciototal == 0) {
            importeCalculadoPry.setVisibility(View.GONE);
            labelimportecalculado.setVisibility(View.GONE);
        } else {
            importeCalculadoPry.setVisibility(View.VISIBLE);
            labelimportecalculado.setVisibility(View.VISIBLE);
            importeCalculadoPry.setText(JavaUtil.formatoMonedaLocal
                    (preciototal));
        }

        if (modelo.getInt(PROYECTO_TIPOESTADO) < 4) {
            importeFinalPry.setVisibility(View.GONE);
            labelimportefinal.setVisibility(View.GONE);
        } else {
            importeFinalPry.setVisibility(View.VISIBLE);
            labelimportefinal.setVisibility(View.VISIBLE);
            importeFinalPry.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(PROYECTO_IMPORTEFINAL)));
        }


        estadoProyecto.setText(getString(PROYECTO_DESCRIPCION_ESTADO));

            long retraso = getLong(PROYECTO_RETRASO);
            if (retraso > 3 * CommonPry.DIASLONG) {
                btnimgEstadoPry.setImageResource(R.drawable.alert_box_r);
            } else if (retraso > CommonPry.DIASLONG) {
                btnimgEstadoPry.setImageResource(R.drawable.alert_box_a);
            } else {
                btnimgEstadoPry.setImageResource(R.drawable.alert_box_v);
            }

        if (getInt(PROYECTO_TIPOESTADO)>=TPRESUPPENDENTREGA){

                btnVerPdf.setVisibility(View.VISIBLE);
                btnenviarPdf.setVisibility(View.VISIBLE);
                btncompartirPdf.setVisibility(View.VISIBLE);
        }else{

            btnVerPdf.setVisibility(View.GONE);
            btnenviarPdf.setVisibility(View.GONE);
            btncompartirPdf.setVisibility(View.GONE);
        }

        comprobarEstado();

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

    }


    @Override
    protected void setNuevo() {

        labeltotpartidas.setVisibility(View.GONE);
        labelfentrada.setVisibility(View.GONE);
        labelhoras.setVisibility(View.GONE);
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
        btnVerPdf.setVisibility(View.GONE);
        btnenviarPdf.setVisibility(View.GONE);
        btncompartirPdf.setVisibility(View.GONE);
        nombrePry.setVisibility(View.GONE);
        descripcionPry.setVisibility(View.GONE);
        imagen.setVisibility(View.GONE);
        spEstadoProyecto.setVisibility(View.GONE);
        btnsave.setVisibility(View.GONE);

        String seleccion = EVENTO_PROYECTOREL+" = '"+id+"'";
        if (consulta.checkQueryList(CAMPOS_EVENTO,seleccion,null)){
            btnVerEventos.setVisibility(View.VISIBLE);
        }else {
            btnVerEventos.setVisibility(View.GONE);
        }

        setAdaptadorClientes(spClienteProyecto);

        System.out.println("idCliente = " + idCliente);

        if (idCliente!=null) {
            Modelo cliente = consulta.queryObject(CAMPOS_CLIENTE, idCliente);
            nombreCliente = cliente.getString(CLIENTE_NOMBRE);
            spClienteProyecto.setText(nombreCliente);
            nombrePry.setVisibility(View.VISIBLE);
            descripcionPry.setVisibility(View.VISIBLE);
            imagen.setVisibility(View.VISIBLE);
            spEstadoProyecto.setVisibility(View.VISIBLE);
            btnsave.setVisibility(View.VISIBLE);
        }

        listaObjetosEstados();

        ArrayAdapter<String> adaptadorEstado = new ArrayAdapter<>
                (getContext(),android.R.layout.simple_spinner_item,listaEstados);

        spEstadoProyecto.setAdapter(adaptadorEstado);

        if (subTitulo.equals(NUEVO + " " + PRESUPUESTO)){

            spEstadoProyecto.setSelection(TNUEVOPRESUP);
            idEstado = objEstados.get(TNUEVOPRESUP).getString(ESTADO_ID_ESTADO);
        }else if(subTitulo.equals(NUEVO + " " + PROYECTO)){
            spEstadoProyecto.setSelection(TPRESUPACEPTADO);
            idEstado = objEstados.get(TPRESUPACEPTADO).getString(ESTADO_ID_ESTADO);
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
                if (idCliente!=null) {
                    nombrePry.setVisibility(View.VISIBLE);
                    descripcionPry.setVisibility(View.VISIBLE);
                    imagen.setVisibility(View.VISIBLE);
                    btnsave.setVisibility(View.VISIBLE);
                    if (idEstado!=null){update();}
                }else{
                    nombrePry.setVisibility(View.GONE);
                    descripcionPry.setVisibility(View.GONE);
                    imagen.setVisibility(View.GONE);
                    btnsave.setVisibility(View.GONE);
                }

            }

        });

    }

    @Override
    protected void setMaestroDetallePort() { maestroDetalleSeparados = true;}

    @Override
    protected void setMaestroDetalleLand() { maestroDetalleSeparados = false;}

    @Override
    protected void setMaestroDetalleTabletLand() { maestroDetalleSeparados = false;}

    @Override
    protected void setMaestroDetalleTabletPort() { maestroDetalleSeparados = false;}

    private void calculoTotales() {

        ArrayList<Modelo> listaPartidas = consulta.queryListDetalle(CAMPOS_PARTIDA, id,TABLA_PROYECTO);

        int x = 0;
        for (int i = 0; i < listaPartidas.size(); i++) {

            int completada = listaPartidas.get(i).getInt(PARTIDA_COMPLETADA);

            totcompletada += completada;

            x = i+1;
        }
        totcompletada = (int) (Math.round(((double) totcompletada) / (double) x));

        totPartidas = getDouble(PROYECTO_TIEMPO);
        precioPartidas = totPartidas * CommonPry.hora;
        preciototal = getDouble(PROYECTO_IMPORTEPRESUPUESTO);

        Log.d(TAG,"calculosTotales");

    }

    @Override
    protected boolean update() {

        new CommonPry.Calculos.Tareafechas().execute();
        if (modelo!=null) {
            fechaCalculada = getLong(PROYECTO_FECHAENTREGACALCULADA);
            calculoTotales();
        }

        if (idCliente!=null && idEstado!=null) {
            return super.update();
        }

        return false;
    }

    @Override
    protected boolean delete() {

        new CommonPry.Calculos.Tareafechas().execute();

        return super.delete();
    }

    private void comprobarEstado() {

        Modelo proyecto = consulta.queryObject(CAMPOS_PROYECTO, id);


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

        Log.d(TAG,"comprobarEstado");

    }

    private String getIdEstado(int tipoEstado){

        ArrayList<Modelo> listaEstados = consulta.queryList(CAMPOS_ESTADO,null, null);

        for (Modelo estado : listaEstados) {

            if (estado.getInt(ESTADO_TIPOESTADO)==tipoEstado){

                    return estado.getString(ESTADO_ID_ESTADO);
            }

        }
        return null;
    }

    private String getIdEstado(String descTipoEstado){

        ArrayList<Modelo> listaEstados = consulta.queryList(CAMPOS_ESTADO,null, null);

        for (Modelo estado : listaEstados) {

            if (estado.getString(ESTADO_DESCRIPCION).equals(descTipoEstado)){

                return estado.getString(ESTADO_ID_ESTADO);
            }

        }
        return null;
    }

    private void modificarEstado() {

        String idNuevoPresup = getIdEstado(TNUEVOPRESUP);
        String idPresupPendEntrega = getIdEstado(TPRESUPPENDENTREGA);
        String idPresupEnEspera = getIdEstado(TPRESUPESPERA);
        String idPresupAceptado = getIdEstado(TPRESUPACEPTADO);
        String idProyEnEjecucion = getIdEstado(TPROYECTEJECUCION);
        String idProyPendEntrega = getIdEstado(TPROYECPENDENTREGA);
        String idProyPendCobro = getIdEstado(TPROYECTPENDCOBRO);
        String idProyCobrado = getIdEstado(TPROYECTCOBRADO);

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

        valores = new ContentValues();
        consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_ID_ESTADO,idEstado);
        consulta.updateRegistro(TABLA_PROYECTO, id,valores);

        modificarEstadoPartidas(id,idEstado);

        comprobarEstado();

        new CommonPry.Calculos.TareaTipoCliente().execute(idCliente);

        Log.e(TAG,"modificarEstado");
    }

    private void modificarEstadoNoAceptado() {

        String idPresupNoAceptado;

        ArrayList<Modelo> listaEstados = consulta.queryList(CAMPOS_ESTADO,null, null);

        for (Modelo estado : listaEstados) {

            if (estado.getInt(ESTADO_TIPOESTADO) == TPRESUPNOACEPTADO) {

                idPresupNoAceptado = estado.getString(ESTADO_ID_ESTADO);
                ContentValues valores = new ContentValues();
                consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_ID_ESTADO,idPresupNoAceptado);
                consulta.updateRegistro(TABLA_PROYECTO, id,valores);

                comprobarEstado();
                break;

            }
        }

        Log.d(TAG,"modificarEstadoNoAceptado");

    }

    private void modificarEstadoPartidas(String idProyecto, String idEstado){

        valores = new ContentValues();
        consulta.putDato(valores,CAMPOS_PARTIDA, PARTIDA_ID_ESTADO,idEstado);
        consulta.updateRegistrosDetalle(TABLA_PARTIDA,idProyecto,TABLA_PROYECTO,valores,null);

        Log.d(TAG,"modificarEstadoPartidas");
    }

    @Override
    protected void setContenedor() {

        setDato(PROYECTO_NOMBRE,nombrePry.getText().toString());
        setDato(PROYECTO_DESCRIPCION,descripcionPry.getText().toString());
        setDato(PROYECTO_ID_CLIENTE,JavaUtil.noNuloString(idCliente));
        setDato(PROYECTO_ID_ESTADO,JavaUtil.noNuloString(idEstado));
        setDato(PROYECTO_FECHAENTREGAACORDADA,fechaAcordada);
        setDato(PROYECTO_FECHAENTREGAACORDADAF,JavaUtil.getDate(fechaAcordada));

        if (fechaCalculada<=fechaAcordada){
            setDato(PROYECTO_RETRASO,0);
        }else {
            setDato(PROYECTO_RETRASO,(fechaCalculada - fechaAcordada));
        }
        setDato(PROYECTO_IMPORTEPRESUPUESTO,preciototal);
        setDato(PROYECTO_IMPORTEFINAL,importeFinalPry.getText().toString(),DOUBLE);
        if (path != null) {
            setDato(PROYECTO_RUTAFOTO,path);
        }
        setDato(PROYECTO_TOTCOMPLETADO,totcompletada);
        if (id ==null){

            setDato(PROYECTO_FECHAENTRADA,JavaUtil.hoy());
            setDato(PROYECTO_FECHAENTRADAF,JavaUtil.getDateTime(JavaUtil.hoy()));
        }

    }


    @Override
    protected void setcambioFragment() {

    }

    private void listaObjetosEstados() {

        objEstados = new ArrayList<>();
        String seleccion = null;
        if (actual.equals(PROYECTO)){
            seleccion = ESTADO_TIPOESTADO + " >3";
        }else if (actual.equals(PRESUPUESTO)){
            seleccion = ESTADO_TIPOESTADO + " <4";
        }else if (actual.equals(COBROS)){
            seleccion = ESTADO_TIPOESTADO + " >6";
        }else if (actual.equals(HISTORICO)){
            seleccion = ESTADO_TIPOESTADO + " == 8";
        }

        objEstados = consulta.queryList(CAMPOS_ESTADO,seleccion,null);

        obtenerListaEstados();
    }

    private void obtenerListaEstados() {

        listaEstados = new ArrayList<String>();
        listaEstados.add(getString(R.string.seleccion_estado));

        for (int i=0;i<objEstados.size();i++){

            listaEstados.add(objEstados.get(i).getString(ESTADO_DESCRIPCION));
        }
    }


    private void setAdaptadorClientes(final AutoCompleteTextView autoCompleteTextView) {

        listaClientes = consulta.queryList(CAMPOS_CLIENTE);

        autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(getContext(),
                R.layout.item_list_cliente,listaClientes,CAMPOS_CLIENTE) {

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

                    bundle = new Bundle();
                    bundle.putBoolean(NUEVOREGISTRO,true);
                    bundle.putString(SUBTITULO,CLIENTE);
                    bundle.putString(ORIGEN, actualtemp);
                    icFragmentos.enviarBundleAFragment(bundle,new FragmentCRUDCliente());

                }else if (opciones[which].equals("Nuevo prospecto")){

                    bundle = new Bundle();
                    bundle.putBoolean(NUEVOREGISTRO,true);
                    bundle.putString(SUBTITULO,PROSPECTO);
                    bundle.putString(ORIGEN, actualtemp);
                    icFragmentos.enviarBundleAFragment(bundle,new FragmentCRUDCliente());

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
                        fechaAcordada = JavaUtil.fechaALong(year, month, day);
                        if (fechaAcordada>0){
                            fechaEntregaPresup.setVisibility(View.VISIBLE);
                            btnfechaentrega.setVisibility(View.VISIBLE);
                            labelfentregap.setVisibility(View.VISIBLE);
                        }
                        String selectedDate = JavaUtil.getDate(fechaAcordada);
                        fechaAcordadaPry.setText(selectedDate);
                        btnActualizar.setVisibility(View.VISIBLE);
                        valores = new ContentValues();
                        setDato(PROYECTO_FECHAENTREGAACORDADA,fechaAcordada);
                        setDato(PROYECTO_FECHAENTREGAACORDADAF,selectedDate);
                        actualizarRegistro();

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
                        fechaEntregaP = JavaUtil.fechaALong(year, month, day);
                        String selectedDate = JavaUtil.getDate(fechaEntregaP);
                        fechaEntregaPresup.setText(selectedDate);
                        valores = new ContentValues();
                        setDato(PROYECTO_FECHAENTREGAPRESUP,fechaEntregaP);
                        setDato(PROYECTO_FECHAENTREGAPRESUPF,selectedDate);
                        actualizarRegistro();

                            new CommonPry.Calculos.Tareafechas().execute();

                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        ImageView imagenProyecto, imagenEstado, imagenCliente;
        TextView nombreProyecto,descripcionProyecto,clienteProyecto, estadoProyecto,
                importe;
        ProgressBar progressBarProyecto;

        public ViewHolderRV(View itemView) {
            super(itemView);
            imagenProyecto = itemView.findViewById(R.id.imglistaproyectos);
            imagenCliente = itemView.findViewById(R.id.imgclientelistaproyectos);
            imagenEstado = itemView.findViewById(R.id.imgestadolistaproyectos);
            nombreProyecto = itemView.findViewById(R.id.tvnombrelistaproyectos);
            descripcionProyecto = itemView.findViewById(R.id.tvdesclistaproyectos);
            clienteProyecto = itemView.findViewById(R.id.tvnombreclientelistaproyectos);
            estadoProyecto = itemView.findViewById(R.id.tvestadolistaproyectos);
            progressBarProyecto = itemView.findViewById(R.id.progressBarlistaproyectos);
            importe = itemView.findViewById(R.id.tvimptotlistaproyectos);
        }

        @Override
        public void bind(Modelo modelo) {

            nombreProyecto.setText(modelo.getString(PROYECTO_NOMBRE));
            descripcionProyecto.setText(modelo.getString(PROYECTO_DESCRIPCION));
            clienteProyecto.setText(modelo.getString(PROYECTO_CLIENTE_NOMBRE));
            estadoProyecto.setText(modelo.getString(PROYECTO_DESCRIPCION_ESTADO));
            importe.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(PROYECTO_IMPORTEPRESUPUESTO)));

            if (actualtemp.equals(PROYECTO)){

                progressBarProyecto.setProgress(modelo.getInt(PROYECTO_TOTCOMPLETADO));

                long retraso = modelo.getLong(PROYECTO_RETRASO);
                if (retraso > 3 * DIASLONG){imagenEstado.setImageResource(R.drawable.alert_box_r);}
                else if (retraso > DIASLONG){imagenEstado.setImageResource(R.drawable.alert_box_a);}
                else {imagenEstado.setImageResource(R.drawable.alert_box_v);}

            }else{

                progressBarProyecto.setVisibility(View.GONE);
                long retraso = modelo.getLong(PROYECTO_RETRASO);
                if (retraso > 3 * DIASLONG){imagenEstado.setImageResource(R.drawable.alert_box_r);}
                else if (retraso > DIASLONG){imagenEstado.setImageResource(R.drawable.alert_box_a);}
                else {imagenEstado.setImageResource(R.drawable.alert_box_v);}

            }
            if (modelo.getString(PROYECTO_RUTAFOTO)!=null) {
                ImagenUtil imagenUtil =new ImagenUtil(AppActivity.getAppContext());
                imagenUtil.setImageUriCircle(modelo.getString(PROYECTO_RUTAFOTO),imagenProyecto);
            }
            int peso = modelo.getInt(PROYECTO_CLIENTE_PESOTIPOCLI);

            if (peso>6){imagenCliente.setImageResource(R.drawable.clientev);}
            else if (peso>3){imagenCliente.setImageResource(R.drawable.clientea);}
            else if (peso>0){imagenCliente.setImageResource(R.drawable.clienter);}
            else {imagenCliente.setImageResource(R.drawable.cliente);}

            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {

            return new ViewHolderRV(view);
        }
    }
    public class AdaptadorFiltroRV extends ListaAdaptadorFiltroRV{

        public AdaptadorFiltroRV(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<Modelo> entrada) {

            ImageView imagen = view.findViewById(R.id.imglistaproyectos);
            TextView nombre = view.findViewById(R.id.tvnombrelistaproyectos);
            TextView descripcion = view.findViewById(R.id.tvdesclistaproyectos);
            ImageView imgcli = view.findViewById(R.id.imgclientelistaproyectos);
            TextView nomcli = view.findViewById(R.id.tvnombreclientelistaproyectos);
            ImageView imgest = view.findViewById(R.id.imgestadolistaproyectos);
            TextView estado = view.findViewById(R.id.tvestadolistaproyectos);
            ProgressBar bar = view.findViewById(R.id.progressBarlistaproyectos);
            TextView precio = view.findViewById(R.id.tvimptotlistaproyectos);

            descripcion.setText(entrada.get(posicion).getCampos(PROYECTO_DESCRIPCION));

            nombre.setText(entrada.get(posicion).getCampos(PROYECTO_NOMBRE));
            nomcli.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.CLIENTE_NOMBRE));
            estado.setText(entrada.get(posicion).getCampos(ContratoPry.Tablas.ESTADO_DESCRIPCION));
            precio.setText(JavaUtil.formatoMonedaLocal(entrada.get(posicion).getDouble(PROYECTO_IMPORTEPRESUPUESTO)));

            bar.setProgress(Integer.parseInt(entrada.get(posicion).getCampos
                    (ContratoPry.Tablas.PROYECTO_TOTCOMPLETADO)));

            long retraso = Long.parseLong(entrada.get(posicion).getCampos
                    (ContratoPry.Tablas.PROYECTO_RETRASO));
            if (retraso > 3 * CommonPry.DIASLONG) {
                imgest.setImageResource(R.drawable.alert_box_r);
            } else if (retraso > CommonPry.DIASLONG) {
                imgest.setImageResource(R.drawable.alert_box_a);
            } else {
                imgest.setImageResource(R.drawable.alert_box_v);
            }

            if (entrada.get(posicion).getCampos(ContratoPry.Tablas.PROYECTO_RUTAFOTO) != null) {
                //imagen.setImageURI(Uri.parse(entrada.getCampos
                //        (ContratoPry.Tablas.PROYECTO_RUTAFOTO)));
                imagenUtil = new ImagenUtil(contexto);
                imagenUtil.setImageUriCircle(entrada.get(posicion).getString(PROYECTO_RUTAFOTO),imagen);
            }
            int peso = Integer.parseInt(entrada.get(posicion).getCampos
                    (ContratoPry.Tablas.CLIENTE_PESOTIPOCLI));

            if (peso > 6) {
                imgcli.setImageResource(R.drawable.clientev);
            } else if (peso > 3) {
                imgcli.setImageResource(R.drawable.clientea);
            } else if (peso > 0) {
                imgcli.setImageResource(R.drawable.clienter);
            } else {
                imgcli.setImageResource(R.drawable.cliente);
            }


            super.setEntradas(posicion, view, entrada);
        }
    }

}






