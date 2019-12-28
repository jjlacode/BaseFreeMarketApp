package com.codevsolution.freemarketsapp.ui;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.android.controls.ViewImagenLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.module.PdfViewerModule;
import com.codevsolution.base.settings.PreferenciasBase;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.DatePickerFragment;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.codevsolution.freemarketsapp.templates.PresupuestoPDF;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.codevsolution.base.sqlite.ConsultaBD.checkQueryList;
import static com.codevsolution.base.sqlite.ConsultaBD.insertRegistro;
import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.base.sqlite.ConsultaBD.queryList;
import static com.codevsolution.base.sqlite.ConsultaBD.queryListDetalle;
import static com.codevsolution.base.sqlite.ConsultaBD.queryObject;
import static com.codevsolution.base.sqlite.ConsultaBD.updateRegistro;
import static com.codevsolution.base.sqlite.ConsultaBD.updateRegistroDetalle;

public class FragmentCRUDProyecto extends FragmentCRUD
        implements Interactor.ConstantesPry, ContratoPry.Tablas, Interactor.Estados,
        Interactor.TiposEstados {

    private EditMaterialLayout spClienteProyecto;
    private EditMaterialLayout nombrePry;
    private EditMaterialLayout descripcionPry;
    private EditMaterialLayout estadoProyecto;
    private EditMaterialLayout fechaEntregaPresup;
    private EditMaterialLayout horaInicioCalculadaPry;
    private EditMaterialLayout importeFinalPry;
    private EditMaterialLayout fechaEntradaPry;
    private EditMaterialLayout fechaCalculadaPry;
    private EditMaterialLayout fechaInicioCalculadaPry;
    private EditMaterialLayout fechaFinalPry;
    private EditMaterialLayout totalPartidasPry;
    private EditMaterialLayout pvpPartidas;
    private EditMaterialLayout importeCalculadoPry;
    private ImageButton btnEvento;
    private Button btnPartidasPry;
    private Button btnActualizar;
    private Button btnActualizar2;
    private ImageButton btncompartirPdf;
    private ImageButton btnenviarPdf;
    private ImageButton btnVerEventos;
    private Spinner spEstadoProyecto;


    private ArrayList<String> listaEstados;
    private ArrayList<ModeloSQL> objEstados;

    private String idCliente;
    private String idEstado;
    private String nombreCliente;

    private int peso = 0;
    private int totcompletada = 0;

    private long fechaCalculada = 0;
    private long fechaInicioCalculada = 0;
    private long horaInicioCalculada = 0;
    private long fechaAcordada = 0;
    private long fechaEntregaP = 0;

    private double totPartidas = 0.0;
    private double precioPartidas = 0.0;
    private double preciototal= 0.0;

    private Button btnpresupuestos;
    private Button btnproyectos;
    private Button btncobros;
    private Button btnhistorico;
    private Button btngarantias;

    public String actualtemp;
    private ImageButton btnNota;
    private ImageButton btnVerNotas;
    public static String rutaPdf;
    private boolean enGarantia;
    private ModeloSQL cliente;
    private CheckBox chSplit;
    private CheckBox chFija;
    private EditMaterialLayout precioHora;
    private EditMaterialLayout costeProy;
    private EditMaterialLayout beneficio;
    private EditMaterialLayout porcBenef;
    private PDFView viewerPdf;
    private Button btnAntPage;
    private Button btnNextPage;
    private int pageNumber;
    private EditMaterialLayout etNumeroPag;
    private long timeStamp;
    private Button btnAsignarAEvento;
    private ModeloSQL evento;
    private PdfViewerModule pdfViewer;

    public FragmentCRUDProyecto() {
        // Required empty public constructor
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected String setTAG() {
        return getClass().getSimpleName();
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


        activityBase.fabNuevo.hide();

        actualizarConsultas();

    }

    private void actualizarConsultas() {

        new Interactor.Calculos.TareaActualizarProys().execute();

        if (listab==null) {
            lista = CRUDutil.setListaModelo(campos);
        }else{
            inicio.setVisibility(View.VISIBLE);
            lista = CRUDutil.clonaListaModelo(campos,listab);
        }

        if (lista.chechLista()) {

            ArrayList<ModeloSQL> listatemp = new ArrayList<>();

            for (ModeloSQL item : lista.getLista()) {

                int estado = item.getInt(PROYECTO_TIPOESTADO);
                System.out.println("actual = " + actual);

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

                    case GARANTIA:

                        if (estado == 8) {
                            listatemp.add(item);
                        }

                        break;

                    case HISTORICO:

                        if (estado == 9 || estado == 0) {
                                listatemp.add(item);
                            }

                        break;

                    default:
                        listatemp.add(item);
                }
            }
            lista = CRUDutil.clonaListaModelo(campos,listatemp);
            System.out.println("lista = " + lista.sizeLista());

            enviarAct();
            setSubtitulo(actual);
        }

    }

    @Override
    protected void setLayout() {

        layoutItem = R.layout.item_list_layout;
        cabecera = true;

    }

    @Override
    protected void setTabla() {

        tabla = TABLA_PROYECTO;
    }

    @Override
    protected void setBundle() {


        if(bundle.containsKey(CLIENTE)){
            cliente = (ModeloSQL) bundle.getSerializable(CLIENTE);
            idCliente = cliente.getString(CLIENTE_ID_CLIENTE);
            if (actual.equals(PRESUPUESTO)) {
                idEstado = getIdEstado(TNUEVOPRESUP);
            } else {
                idEstado = getIdEstado(TPROYECTEJECUCION);
            }
            onUpdate();

        }

        if (id !=null) {
            modeloSQL = CRUDutil.updateModelo(campos, id);
            idCliente = modeloSQL.getString(PROYECTO_ID_CLIENTE);
            new Interactor.Calculos.TareaActualizaProy().execute(id);
        }

        if (actual==null){
            actual=PROYECTO;
        }

        switch (actual) {

            case COBROS:

            case HISTORICO:

            case GARANTIA:
                if (actualtemp == null) {
                    actualtemp = PROYECTO;
                }
                break;
            default:
                if (actualtemp == null) {
                    actualtemp = actual;
                }

                break;
        }
        setSubtitulo(actual);

        evento = (ModeloSQL) bundle.getSerializable(EVENTO);
        if (evento != null) {
            visible(btnAsignarAEvento);
        }

    }

    private void setSubtitulo(String actual){

        switch (actual) {
            case PRESUPUESTO:
                tituloPlural = R.string.presupuestos;
                break;
            case PROYECTO:
                tituloPlural = R.string.proyectos;
                break;
            case COBROS:
                tituloPlural = R.string.cobros;
                break;
            case HISTORICO:
                tituloPlural = R.string.historico;
                break;
            case GARANTIA:
                tituloPlural = R.string.garantia;
                break;
            default:
                tituloPlural = R.string.proyectos;

                break;
        }

        subTitulo = getString(tituloPlural);
        activityBase.toolbar.setSubtitle(subTitulo);
        //reproducir(subTitulo);
        imagen.setTextTitulo(subTitulo);

    }

    @Override
    protected void setAcciones() {

        btnpresupuestos.setVisibility(View.VISIBLE);
        btnproyectos.setVisibility(View.VISIBLE);
        btncobros.setVisibility(View.VISIBLE);
        btnhistorico.setVisibility(View.VISIBLE);
        btngarantias.setVisibility(View.VISIBLE);

        btnpresupuestos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),PRESUPUESTOS, Toast.LENGTH_SHORT).show();
                actual = PRESUPUESTO;
                setSubtitulo(actual);
                actualtemp = actual;
                selector();
            }
        });

        btnproyectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),PROYECTOS, Toast.LENGTH_SHORT).show();
                actual = PROYECTO;
                actualtemp = actual;
                setSubtitulo(actual);

                selector();
            }
        });

        btncobros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),PROYCOBROS, Toast.LENGTH_SHORT).show();
                actual = COBROS;
                actualtemp = PROYECTO;
                setSubtitulo(actual);

                selector();
            }
        });

        btngarantias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), PROYGARANTIA, Toast.LENGTH_SHORT).show();
                actual = GARANTIA;
                actualtemp = PROYECTO;
                setSubtitulo(actual);

                selector();
            }
        });

        btnhistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),PROYHISTORICO, Toast.LENGTH_SHORT).show();
                actual = HISTORICO;
                actualtemp = PROYECTO;
                setSubtitulo(actual);

                selector();
            }
        });

        chSplit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    CRUDutil.actualizarCampo(modeloSQL, PROYECTO_SPLIT, 1);
                    modeloSQL = CRUDutil.updateModelo(modeloSQL);
                } else {
                    CRUDutil.actualizarCampo(modeloSQL, PROYECTO_SPLIT, 0);
                    modeloSQL = CRUDutil.updateModelo(modeloSQL);
                }
            }
        });

        chFija.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    CRUDutil.actualizarCampo(modeloSQL, PROYECTO_FIJA, 1);
                    modeloSQL = CRUDutil.updateModelo(modeloSQL);
                } else {
                    CRUDutil.actualizarCampo(modeloSQL, PROYECTO_FIJA, 0);
                    modeloSQL = CRUDutil.updateModelo(modeloSQL);
                }
            }
        });

    }


    private void nuevoEvento() {
        update();
        ModeloSQL cliente = queryObject(CAMPOS_CLIENTE, idCliente);

        bundle = new Bundle();
        bundle.putSerializable(CLIENTE, cliente);
        bundle.putSerializable(PROYECTO, modeloSQL);
        bundle.putString(SUBTITULO, actualtemp);
        bundle.putString(ORIGEN, actualtemp);
        bundle.putBoolean(NUEVOREGISTRO, true);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevoEvento());
        bundle = null;
    }

    private void verEventos() {
        update();
        ModeloSQL cliente = queryObject(CAMPOS_CLIENTE, idCliente);

        bundle = new Bundle();
        bundle.putSerializable(CLIENTE, cliente);
        bundle.putSerializable(PROYECTO, modeloSQL);
        bundle.putString(SUBTITULO, actualtemp);
        bundle.putString(ORIGEN, actualtemp);
        bundle.putSerializable(LISTA, new ListaModeloSQL(CAMPOS_EVENTO, EVENTO_PROYECTOREL, id));
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
        bundle = null;
    }

    private void verNotas() {
        enviarBundle();
        bundle.putString(IDREL, modeloSQL.getString(PROYECTO_ID_PROYECTO));
        bundle.putString(SUBTITULO, modeloSQL.getString(PROYECTO_NOMBRE));
        bundle.putString(ORIGEN, PROYECTO);
        bundle.putString(ACTUAL, NOTA);
        bundle.putSerializable(LISTA, null);
        bundle.putSerializable(MODELO, null);
        bundle.putString(CAMPO_ID, null);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
    }

    private void nuevaNota() {
        bundle = new Bundle();
        bundle.putString(IDREL, modeloSQL.getString(PROYECTO_ID_PROYECTO));
        bundle.putString(SUBTITULO, modeloSQL.getString(PROYECTO_NOMBRE));
        bundle.putString(ORIGEN, PROYECTO);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevaNota());
    }

    private void verPartidas() {
        update();
        bundle.putSerializable(PROYECTO, modeloSQL);
        bundle.putString(ORIGEN, actualtemp);
        bundle.putString(ACTUAL, PARTIDA);
        bundle.putString(SUBTITULO, actualtemp);
        bundle.putString(CAMPO_ID, id);
        bundle.putInt(CAMPO_SECUENCIA, 0);
        bundle.putSerializable(MODELO, null);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDPartidaProyecto());
    }

    @Override
    protected void setOnLeftSwipe() {

        if (id == null) {
            switch (actual) {

                case GARANTIA:
                    Toast.makeText(getContext(), PROYHISTORICO, Toast.LENGTH_SHORT).show();
                    actual = HISTORICO;
                    setSubtitulo(actual);
                    actualtemp = PROYECTO;
                    selector();
                    break;

                case COBROS:
                    Toast.makeText(getContext(), PROYGARANTIA, Toast.LENGTH_SHORT).show();
                    actual = GARANTIA;
                    setSubtitulo(actual);
                    actualtemp = PROYECTO;
                    selector();
                    break;

                case PRESUPUESTO:
                    Toast.makeText(getContext(), PROYECTOS, Toast.LENGTH_SHORT).show();
                    actual = PROYECTO;
                    actualtemp = actual;
                    setSubtitulo(actual);
                    selector();
                    break;
                case PROYECTO:
                    Toast.makeText(getContext(), PROYCOBROS, Toast.LENGTH_SHORT).show();
                    actual = COBROS;
                    actualtemp = PROYECTO;
                    setSubtitulo(actual);
                    selector();
                    break;
            }
        }
    }


    @Override
    protected void setOnRightSwipe() {

        if (id == null) {
            switch (actual) {

                case PROYECTO:
                    Toast.makeText(getContext(), PRESUPUESTOS, Toast.LENGTH_SHORT).show();
                    actual = PRESUPUESTO;
                    setSubtitulo(actual);
                    actualtemp = actual;
                    selector();
                    break;

                case COBROS:
                    Toast.makeText(getContext(), PROYECTOS, Toast.LENGTH_SHORT).show();
                    actual = PROYECTO;
                    actualtemp = actual;
                    setSubtitulo(actual);
                    selector();
                    break;
                case GARANTIA:
                    Toast.makeText(getContext(), PROYCOBROS, Toast.LENGTH_SHORT).show();
                    actual = COBROS;
                    actualtemp = PROYECTO;
                    setSubtitulo(actual);
                    selector();
                    break;

                case HISTORICO:
                    Toast.makeText(getContext(), PROYGARANTIA, Toast.LENGTH_SHORT).show();
                    actual = GARANTIA;
                    actualtemp = PROYECTO;
                    setSubtitulo(actual);
                    selector();
                    break;
            }
        }
    }

    @Override
    protected void setTitulo() {

        if (nnn(actual)){

            setSubtitulo(actual);

        }else {

            tituloSingular = R.string.proyecto;
            tituloPlural = R.string.proyectos;
            tituloNuevo = R.string.nuevo_proyecto;
        }

    }


    @Override
    protected void setInicio() {

        new Tareafechas().execute(false);

        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalle);

        btnAsignarAEvento = vistaForm.addButtonPrimary(R.string.asignar_evento);
        btnAsignarAEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                putBundle(PROYECTO, modeloSQL);
                putBundle(MODELO, evento);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
            }
        });
        gone(btnAsignarAEvento);
        imagen = vistaForm.addViewImagenLayout();

        imagen.getLinearLayoutCompat().setFocusable(false);
        imagen.setTextTitulo(tituloSingular);
        nombrePry = vistaForm.addEditMaterialLayout(getString(R.string.nombre), PROYECTO_NOMBRE, null, null);
        descripcionPry = vistaForm.addEditMaterialLayout(getString(R.string.descripcion), PROYECTO_DESCRIPCION, null, null);
        descripcionPry.setTipo(EditMaterialLayout.TEXTO|EditMaterialLayout.MULTI);
        spClienteProyecto = vistaForm.addEditMaterialLayout(getString(R.string.cliente));
        spClienteProyecto.setActivo(false);
        spClienteProyecto.btnInicioVisible(false);
        spClienteProyecto.btnAccion3Enable(true);
        spClienteProyecto.setImgBtnAccion3(R.drawable.cliente);
        spEstadoProyecto = (Spinner) vistaForm.addVista(new Spinner(contexto));
        estadoProyecto = vistaForm.addEditMaterialLayout(R.string.estado);
        estadoProyecto.btnAccion3Enable(true);
        estadoProyecto.btnInicioVisible(false);
        estadoProyecto.setImgBtnAccion3(R.drawable.alert_box_v);
        fechaEntradaPry = vistaForm.addEditMaterialLayout(R.string.fecha_entrada);
        fechaEntradaPry.setActivo(false);
        fechaEntradaPry.btnInicioVisible(false);
        fechaEntregaPresup = vistaForm.addEditMaterialLayout(R.string.fecha_entrega_presup);
        fechaEntregaPresup.setActivo(false);
        fechaEntregaPresup.btnInicioVisible(false);
        fechaEntregaPresup.btnAccionEnable(true);
        fechaEntregaPresup.setImgBtnAccion(R.drawable.ic_search_black_24dp);
        fechaEntregaPresup.setClickAccion(new EditMaterialLayout.ClickAccion() {
            @Override
            public void onClickAccion(View view) {
                showDatePickerDialogEntrega();
            }
        });

        ViewGroupLayout vistaSplit = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaSplit.setOrientacion(LinearLayoutCompat.HORIZONTAL);
        chSplit = (CheckBox) vistaSplit.addVista(new CheckBox(contexto), 1);
        chSplit.setText(R.string.mantener_entera);
        chFija = (CheckBox) vistaSplit.addVista(new CheckBox(contexto), 1);
        chFija.setText(R.string.no_mover);
        actualizarArrays(vistaSplit);

        ViewGroupLayout vistaAcordada = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaAcordada.setOrientacion(LinearLayoutCompat.HORIZONTAL);
        fechaInicioCalculadaPry = vistaAcordada.addEditMaterialLayout(R.string.fecha_acordada, 1);
        fechaInicioCalculadaPry.setActivo(false);
        fechaInicioCalculadaPry.btnInicioVisible(false);
        horaInicioCalculadaPry = vistaAcordada.addEditMaterialLayout(R.string.hora_acordada, 1);
        horaInicioCalculadaPry.setActivo(false);
        horaInicioCalculadaPry.btnInicioVisible(false);
        actualizarArrays(vistaAcordada);

        fechaCalculadaPry = vistaForm.addEditMaterialLayout(R.string.fecha_calculada);
        fechaCalculadaPry.setActivo(false);
        fechaCalculadaPry.btnInicioVisible(false);
        fechaFinalPry = vistaForm.addEditMaterialLayout(R.string.fecha_final);
        fechaFinalPry.setActivo(false);
        fechaFinalPry.btnInicioVisible(false);

        ViewGroupLayout vistaImportepartidas = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaImportepartidas.setOrientacion(LinearLayoutCompat.HORIZONTAL);
        precioHora = vistaImportepartidas.addEditMaterialLayout(R.string.precio_hora, 1);
        precioHora.setActivo(false);
        precioHora.btnInicioVisible(false);
        totalPartidasPry = vistaImportepartidas.addEditMaterialLayout(R.string.tiempo_partidas, 1);
        totalPartidasPry.setActivo(false);
        totalPartidasPry.btnInicioVisible(false);
        pvpPartidas = vistaImportepartidas.addEditMaterialLayout(R.string.total_partidas, 1);
        pvpPartidas.setActivo(false);
        pvpPartidas.btnInicioVisible(false);
        actualizarArrays(vistaImportepartidas);

        ViewGroupLayout vistaImporte = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaImporte.setOrientacion(LinearLayoutCompat.HORIZONTAL);
        importeCalculadoPry = vistaImporte.addEditMaterialLayout(R.string.importe_calculado, 1);
        importeCalculadoPry.setActivo(false);
        importeCalculadoPry.btnInicioVisible(false);
        importeFinalPry = vistaImporte.addEditMaterialLayout(R.string.importe_final, 1);
        actualizarArrays(vistaImporte);

        ViewGroupLayout vistaCoste = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaCoste.setOrientacion(LinearLayoutCompat.HORIZONTAL);

        costeProy = vistaCoste.addEditMaterialLayout(R.string.coste, 1);
        costeProy.setActivo(false);
        costeProy.btnInicioVisible(false);
        beneficio = vistaCoste.addEditMaterialLayout(R.string.beneficio, 1);
        beneficio.setActivo(false);
        beneficio.btnInicioVisible(false);
        porcBenef = vistaCoste.addEditMaterialLayout(R.string.porcbenef, 1);
        porcBenef.setActivo(false);
        porcBenef.btnInicioVisible(false);

        btnPartidasPry = vistaForm.addButtonPrimary(activityBase, R.string.partidas_proyecto);
        btnPartidasPry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verPartidas();
            }
        });

        ViewGroupLayout vistabtn = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistabtn.setOrientacion(LinearLayoutCompat.HORIZONTAL);
        btnEvento = vistabtn.addImageButtonSecundary(activityBase, R.drawable.ic_evento_indigo, 1);
        btnEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevoEvento();
            }
        });
        btnVerEventos = vistabtn.addImageButtonSecundary(activityBase, R.drawable.ic_lista_eventos_indigo, 1);
        btnVerEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verEventos();
            }
        });
        btnNota = vistabtn.addImageButtonSecundary(activityBase, R.drawable.ic_nueva_nota_indigo, 1);
        btnNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevaNota();
            }
        });
        btnVerNotas = vistabtn.addImageButtonSecundary(activityBase, R.drawable.ic_lista_notas_indigo, 1);
        btnVerNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verNotas();
            }
        });
        actualizarArrays(vistabtn);

        btnActualizar = vistaForm.addButtonPrimary(activityBase, R.string.estado);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarEstado();
                update();

                if (peso > 6) {
                    spClienteProyecto.setImgBtnAccion3(R.drawable.clientev);
                } else if (peso > 3) {
                    spClienteProyecto.setImgBtnAccion3(R.drawable.clientea);
                } else if (peso > 0) {
                    spClienteProyecto.setImgBtnAccion3(R.drawable.clienter);
                } else {
                    spClienteProyecto.setImgBtnAccion3(R.drawable.cliente);
                }

                setDatos();
            }
        });
        btnActualizar2 = vistaForm.addButtonPrimary(activityBase, R.string.estado);
        btnActualizar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarEstadoNoAceptado();
                update();
                setDatos();
            }
        });

        pdfViewer = new PdfViewerModule(vistaForm.getViewGroup(),contexto,this,(int) (altoReal * 0.65),rutaPdf);

        actualizarArrays(vistaForm);

        ViewGroupLayout vistaCab = new ViewGroupLayout(contexto, frCabecera);
        vistaCab.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);

        btnpresupuestos = vistaCab.addButtonSecondary(activityBase, R.string.presup, 1);
        btnpresupuestos.setSingleLine(true);
        btnproyectos = vistaCab.addButtonSecondary(activityBase, R.string.proyecto, 1);
        btnproyectos.setSingleLine(true);
        btncobros = vistaCab.addButtonSecondary(activityBase, R.string.cobros, 1);
        btncobros.setSingleLine(true);
        btnhistorico = vistaCab.addButtonSecondary(activityBase, R.string.historico, 1);
        btnhistorico.setSingleLine(true);
        btngarantias = vistaCab.addButtonSecondary(activityBase, R.string.garantia, 1);
        btngarantias.setSingleLine(true);

        actualizarArrays(vistaCab);

        estadoProyecto.setActivo(false);
        //nombrePry.setCounterEnable(true);
        //nombrePry.setCounterMax(30);

        gone(btnpresupuestos);
        gone(btnproyectos);
        gone(btncobros);
        gone(btnhistorico);
        gone(btngarantias);

        if (actual==null){
            actual=PROYECTO;
            actualtemp=PROYECTO;

        }

    }

    protected void cargarPresupuesto() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Interactor.Calculos.actualizarPresupuesto(id);
                rutaPdf = generarPDF(id);
                activityBase.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        pdfViewer.cargarDocumento(rutaPdf);

                    }
                });
            }
        }).start();
    }

    @Override
    protected void alGuardarCampos() {
        super.alGuardarCampos();

        System.out.println("Al guardar campos");
        timeStamp = pdfViewer.getTimestamp();
        if (nnn(id) && nn(modeloSQL) && timeStamp < TimeDateUtil.ahora() - (10 * SEGUNDOSLONG)) {

            cargarPresupuesto();
        }

    }

    @Override
    protected void setDatos() {

        Interactor.Calculos.actualizarPresupuesto(id);

        cargarPresupuesto();

        activityBase.fabNuevo.hide();

        spEstadoProyecto.setVisibility(View.GONE);

        btndelete.setVisibility(View.VISIBLE);
        btnEvento.setVisibility(View.VISIBLE);
        btnPartidasPry.setVisibility(View.VISIBLE);
        estadoProyecto.getLinearLayout().setVisibility(View.VISIBLE);
        fechaCalculadaPry.getLinearLayout().setVisibility(View.VISIBLE);
        fechaInicioCalculadaPry.getLinearLayout().setVisibility(View.VISIBLE);
        horaInicioCalculadaPry.getLinearLayout().setVisibility(View.VISIBLE);
        btnActualizar.setVisibility(View.VISIBLE);
        btnActualizar2.setVisibility(View.VISIBLE);
        pvpPartidas.getLinearLayout().setVisibility(View.VISIBLE);
        fechaEntregaPresup.getLinearLayout().setVisibility(View.VISIBLE);
        fechaFinalPry.getLinearLayout().setVisibility(View.VISIBLE);
        totalPartidasPry.getLinearLayout().setVisibility(View.VISIBLE);
        importeFinalPry.getLinearLayout().setVisibility(View.VISIBLE);
        importeCalculadoPry.getLinearLayout().setVisibility(View.VISIBLE);
        fechaEntradaPry.getLinearLayout().setVisibility(View.VISIBLE);
        visible(btnNota);
        visible(imagen.getLinearLayoutCompat());
        visible(chSplit);
        visible(chFija);
        visible(precioHora.getLinearLayout());
        visible(costeProy.getLinearLayout());
        visible(beneficio.getLinearLayout());
        visible(porcBenef.getLinearLayout());

        precioHora.setText(JavaUtil.formatoMonedaLocal(modeloSQL.getDouble(PROYECTO_PRECIOHORA)));
        double coste = modeloSQL.getDouble(PROYECTO_COSTE);
        costeProy.setText(JavaUtil.formatoMonedaLocal(coste));
        double importe = modeloSQL.getDouble(PROYECTO_IMPORTEPRESUPUESTO);
        if (modeloSQL.getDouble(PROYECTO_IMPORTEFINAL) > 0) {
            importe = modeloSQL.getDouble(PROYECTO_IMPORTEFINAL);
        }
        double bnf = importe - coste;
        beneficio.setText(JavaUtil.formatoMonedaLocal(bnf));
        double pbenef = (double) (100) / (importe / bnf);
        porcBenef.setText(JavaUtil.getDecimales(pbenef));

        if (modeloSQL.getInt(PROYECTO_SPLIT) == 1) {
            chSplit.setChecked(true);
        } else {
            chSplit.setChecked(false);
        }

        if (modeloSQL.getInt(PROYECTO_FIJA) == 1) {
            chFija.setChecked(true);
        } else {
            chFija.setChecked(false);
        }


        String seleccion = EVENTO_PROYECTOREL+" = '"+id+"'";
        if (checkQueryList(CAMPOS_EVENTO,seleccion,null)){
            btnVerEventos.setVisibility(View.VISIBLE);
        }else {
            btnVerEventos.setVisibility(View.GONE);
        }

        seleccion = NOTA_ID_RELACIONADO+" = '"+id+"'";
        if (checkQueryList(CAMPOS_NOTA,seleccion,null)){
            btnVerNotas.setVisibility(View.VISIBLE);
        }else {
            btnVerNotas.setVisibility(View.GONE);
        }

        nombrePry.setText(modeloSQL.getString(PROYECTO_NOMBRE));
        descripcionPry.setText(modeloSQL.getString(PROYECTO_DESCRIPCION));
        idCliente = modeloSQL.getString(PROYECTO_ID_CLIENTE);
        peso = modeloSQL.getInt(PROYECTO_CLIENTE_PESOTIPOCLI);//cliente.getInt(CLIENTE_PESOTIPOCLI);
        if (peso > 6) {
            spClienteProyecto.setImgBtnAccion3(R.drawable.clientev);
        } else if (peso > 3) {
            spClienteProyecto.setImgBtnAccion3(R.drawable.clientea);
        } else if (peso > 0) {
            spClienteProyecto.setImgBtnAccion3(R.drawable.clienter);
        } else {
            spClienteProyecto.setImgBtnAccion3(R.drawable.cliente);
        }

        idEstado = modeloSQL.getString(PROYECTO_ID_ESTADO);
        id = modeloSQL.getString(PROYECTO_ID_PROYECTO);
        fechaEntradaPry.setText(JavaUtil.getDateTime(modeloSQL.getLong(PROYECTO_FECHAENTRADA)));
        if (modeloSQL.getLong(PROYECTO_FECHAENTREGAPRESUP) == 0) {
            fechaEntregaPresup.setText
                    (getResources().getString(R.string.establecer_fecha_entrega_presup));
        } else if (modeloSQL.getLong(PROYECTO_FECHAENTREGAPRESUP) > 0) {
            fechaEntregaPresup.setText(JavaUtil.getDate(modeloSQL.getLong(PROYECTO_FECHAENTREGAPRESUP)));
        }

        calculoTotales();

        totalPartidasPry.setText(JavaUtil.getDecimales(totPartidas));

        pvpPartidas.setText(JavaUtil.formatoMonedaLocal(precioPartidas));

        importeCalculadoPry.setText(JavaUtil.formatoMonedaLocal(preciototal));

        if (preciototal == 0) {

            gone(fechaCalculadaPry.getLinearLayout());
            gone(fechaInicioCalculadaPry.getLinearLayout());
            gone(horaInicioCalculadaPry.getLinearLayout());
            btnActualizar.setVisibility(View.GONE);
            btnActualizar2.setVisibility(View.GONE);
            gone(pvpPartidas.getLinearLayout());
            gone(fechaEntregaPresup.getLinearLayout());
            gone(totalPartidasPry.getLinearLayout());

        } else {

            visible(fechaCalculadaPry.getLinearLayout());
            visible(fechaInicioCalculadaPry.getLinearLayout());
            visible(horaInicioCalculadaPry.getLinearLayout());
            visible(btnActualizar);
            visible(pvpPartidas.getLinearLayout());
            gone(fechaEntregaPresup.getLinearLayout());
            visible(totalPartidasPry.getLinearLayout());
            fechaCalculada = modeloSQL.getLong(PROYECTO_FECHAENTREGACALCULADA);
            fechaInicioCalculada = modeloSQL.getLong(PROYECTO_FECHAINICIOCALCULADA);
            horaInicioCalculada = modeloSQL.getLong(PROYECTO_HORAINICIOCALCULADA);

            if (modeloSQL.getInt(PROYECTO_TIPOESTADO) == TNUEVOPRESUP) {
                valores = new ContentValues();
                putDato(valores,PROYECTO_PRECIOHORA, Interactor.hora);
                putDato(valores,PROYECTO_FECHAENTREGAACORDADA,fechaCalculada);
                CRUDutil.actualizarRegistro(modeloSQL, valores);
                modeloSQL = CRUDutil.updateModelo(modeloSQL);
            }

            if (horaInicioCalculada == 0) {
                horaInicioCalculadaPry.setText(getString(R.string.sin_asignar));
            }else{
                horaInicioCalculadaPry.setText(TimeDateUtil.getTimeString(horaInicioCalculada));
            }

            if (fechaInicioCalculada == 0) {

                fechaInicioCalculadaPry.setText(getString(R.string.sin_asignar));
                fechaCalculadaPry.setText(JavaUtil.getDateTime(fechaCalculada));


            } else {

                fechaCalculadaPry.setText(JavaUtil.getDateTime(fechaCalculada));
                fechaInicioCalculadaPry.setText(TimeDateUtil.getDateString(fechaInicioCalculada));
                if (modeloSQL.getInt(PROYECTO_TIPOESTADO) > 2) {

                    visible(fechaEntregaPresup.getLinearLayout());
                }

            }

        }


        if (modeloSQL.getLong(PROYECTO_FECHAFINAL) == 0) {
            gone(fechaFinalPry.getLinearLayout());
        } else {
            visible(fechaFinalPry.getLinearLayout());
            fechaFinalPry.setText(JavaUtil.getDate(modeloSQL.getLong(PROYECTO_FECHAFINAL)));
        }


        if (preciototal == 0) {
            gone(importeCalculadoPry.getLinearLayout());
        } else {
            visible(importeCalculadoPry.getLinearLayout());
            importeCalculadoPry.setText(JavaUtil.formatoMonedaLocal
                    (preciototal));
        }

        if (modeloSQL.getInt(PROYECTO_TIPOESTADO) < 4) {
            gone(importeFinalPry.getLinearLayout());
        } else {
            visible(importeFinalPry.getLinearLayout());
            importeFinalPry.setText(JavaUtil.formatoMonedaLocal(modeloSQL.getDouble(PROYECTO_IMPORTEFINAL)));
        }


        estadoProyecto.setText(modeloSQL.getString(PROYECTO_DESCRIPCION_ESTADO));

        long retraso = modeloSQL.getLong(PROYECTO_RETRASO);
        if (retraso > 3 * Interactor.DIASLONG) {
            estadoProyecto.setImgBtnAccion3(R.drawable.alert_box_r);
        } else if (retraso > Interactor.DIASLONG) {
            estadoProyecto.setImgBtnAccion3(R.drawable.alert_box_a);
            } else {
            estadoProyecto.setImgBtnAccion3(R.drawable.alert_box_v);
            }

        /*
        if (modeloSQL.getInt(PROYECTO_TIPOESTADO) >= TPRESUPPENDENTREGA) {

                btnenviarPdf.setVisibility(View.VISIBLE);
                btncompartirPdf.setVisibility(View.VISIBLE);
        }else{

            btnenviarPdf.setVisibility(View.GONE);
            btncompartirPdf.setVisibility(View.GONE);
        }

         */

        comprobarEstado();


        System.out.println("idCliente = " + idCliente);
        if (idCliente!=null) {
            ModeloSQL cliente = queryObject(CAMPOS_CLIENTE, idCliente);
            nombreCliente = cliente.getString(CLIENTE_NOMBRE);
            System.out.println("nombreCliente = " + nombreCliente);
            spClienteProyecto.setText(nombreCliente);
        }

        timeStamp = TimeDateUtil.ahora();
    }


    private void calculoTotales() {

        ArrayList<ModeloSQL> listaPartidas = queryListDetalle(CAMPOS_PARTIDA, id);

        int x = 0;
        for (int i = 0; i < listaPartidas.size(); i++) {

            int completada = listaPartidas.get(i).getInt(PARTIDA_COMPLETADA);

            totcompletada += completada;

            x = i+1;
        }
        totcompletada = (int) (Math.round(((double) totcompletada) / (double) x));

        totPartidas = modeloSQL.getDouble(PROYECTO_TIEMPO);
        precioPartidas = totPartidas * modeloSQL.getDouble(PROYECTO_PRECIOHORA);
        preciototal = modeloSQL.getDouble(PROYECTO_IMPORTEPRESUPUESTO);

        Log.d(TAG,"calculosTotales");


    }

    @Override
    protected boolean update() {

        new Tareafechas().execute(false);
        if (modeloSQL != null) {
            fechaCalculada = modeloSQL.getLong(PROYECTO_FECHAENTREGACALCULADA);
            fechaInicioCalculada = modeloSQL.getLong(PROYECTO_FECHAINICIOCALCULADA);
            horaInicioCalculada = modeloSQL.getLong(PROYECTO_HORAINICIOCALCULADA);
            if (modeloSQL.getInt(PROYECTO_TIPOESTADO) == TNUEVOPRESUP) {
                valores = new ContentValues();
                putDato(valores,PROYECTO_PRECIOHORA, Interactor.hora);
                putDato(valores,PROYECTO_FECHAENTREGACALCULADA,fechaCalculada);
                CRUDutil.actualizarRegistro(modeloSQL, valores);
                modeloSQL = CRUDutil.updateModelo(modeloSQL);
            } else if (modeloSQL.getInt(PROYECTO_TIPOESTADO) >= TPRESUPACEPTADO) {
                valores = new ContentValues();
                putDato(valores,PROYECTO_FECHAENTREGAACORDADA,fechaCalculada);
                putDato(valores,PROYECTO_FECHAINICIOACORDADA,fechaInicioCalculada);
                putDato(valores,PROYECTO_HORAINICIOACORDADA,horaInicioCalculada);
                CRUDutil.actualizarRegistro(modeloSQL, valores);
                modeloSQL = CRUDutil.updateModelo(modeloSQL);
            }

                calculoTotales();
        }
        System.out.println("idCliente = " + idCliente);
        System.out.println("idEstado = " + idEstado);

        if (idCliente!=null && idEstado!=null) {

            if (super.update()) {
                Log.d(TAG, "Registro actualizado");
            return true;
            }

            Log.e(TAG, "Error al actualizar el registro");

            return false;
        }

        return false;
    }

    @Override
    protected boolean delete() {

        new Tareafechas().execute(false);

        return super.delete();
    }

    private void comprobarEstado() {

        ModeloSQL proyecto = queryObject(CAMPOS_PROYECTO, id);


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
                    visible(importeFinalPry.getLinearLayout());
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
                case TPROYECTCOBRADO:
                    btnActualizar.setText(String.format("%s %s", convertir, PROYECTHISTORICO));
            }

            estadoProyecto.setGravedad(View.TEXT_ALIGNMENT_CENTER);
        }

        Log.d(TAG,"comprobarEstado");

    }

    private String getIdEstado(int tipoEstado){

        ArrayList<ModeloSQL> listaEstados = queryList(CAMPOS_ESTADO);

        for (ModeloSQL estado : listaEstados) {

            if (estado.getInt(ESTADO_TIPOESTADO)==tipoEstado){

                    return estado.getString(ESTADO_ID_ESTADO);
            }

        }
        return null;
    }

    private String getIdEstado(String descTipoEstado){

        ArrayList<ModeloSQL> listaEstados = queryList(CAMPOS_ESTADO);

        for (ModeloSQL estado : listaEstados) {

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
        String idProyHistorico = getIdEstado(TPROYECTHISTORICO);

        if (idEstado.equals(idNuevoPresup)) {

            estadoProyecto.setText(PRESUPPENDENTREGA);
            idEstado = idPresupPendEntrega;

            crearEventoPresupuesto();

        } else if (idEstado.equals(idPresupPendEntrega)) {

            estadoProyecto.setText(PRESUPESPERA);
            idEstado = idPresupEnEspera;
            visible(fechaEntregaPresup.getLinearLayout());

        } else if (idEstado.equals(idPresupEnEspera) && fechaEntregaP >0) {

            estadoProyecto.setText(PRESUPACEPTADO);
            idEstado = idPresupAceptado;


        } else if (idEstado.equals(idPresupEnEspera) && fechaEntregaP == 0) {

            Toast.makeText(getContext(),"Fecha entrega presup no establecida",Toast.LENGTH_SHORT).show();

        } else if (idEstado.equals(idPresupAceptado)) {

            estadoProyecto.setText(PROYECTEJECUCION);
            idEstado = idProyEnEjecucion;
            new TareaFechasGuardar().execute(true);

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

        } else if (idEstado.equals(idProyCobrado) && !enGarantia) {

            estadoProyecto.setText(PROYECTHISTORICO);
            idEstado = idProyHistorico;

        }

        valores = new ContentValues();
        putDato(valores,PROYECTO_ID_ESTADO,idEstado);
        updateRegistro(TABLA_PROYECTO, id,valores);
        CRUDutil.actualizarRegistro(modeloSQL, valores);

        modificarEstadoPartidas(id,idEstado);

        comprobarEstado();

        new Interactor.Calculos.TareaTipoCliente().execute(idCliente);

        Log.d(TAG,"modificarEstado");
    }


    private void crearEventoPresupuesto() {

            long fechaini = JavaUtil.sumaDiaMesAnio(JavaUtil.hoy());
            long horaini = JavaUtil.sumaHoraMin(JavaUtil.hoy());
            String asunto = "Presupuesto solicitado";
            String mensaje = "Envio presupuesto solicitado por usted";
        ModeloSQL cliente = queryObject(CAMPOS_CLIENTE, idCliente);
            valores = new ContentValues();
        putDato(valores, EVENTO_TIPO, Interactor.TiposEvento.TIPOEVENTOEMAIL);
            putDato(valores, EVENTO_DESCRIPCION, "Envío presupuesto a cliente");
            putDato(valores, EVENTO_EMAIL, cliente.getString(CLIENTE_EMAIL));
            putDato(valores, EVENTO_FECHAINIEVENTO, fechaini);
            putDato(valores, EVENTO_HORAINIEVENTO, horaini + HORASLONG);
            putDato(valores, EVENTO_FECHAINIEVENTOF, JavaUtil.getDate(fechaini));
            putDato(valores, EVENTO_HORAINIEVENTOF, JavaUtil.getTime(horaini + HORASLONG));
            putDato(valores, EVENTO_ASUNTO, asunto);
            putDato(valores, EVENTO_MENSAJE, mensaje);
            putDato(valores, EVENTO_RUTAADJUNTO,rutaPdf);
        putDato(valores, EVENTO_RUTAFOTO, modeloSQL.getString(PROYECTO_RUTAFOTO));
            putDato(valores, EVENTO_PROYECTOREL, id);
            putDato(valores, EVENTO_CLIENTEREL, idCliente);
        putDato(valores, EVENTO_NOMPROYECTOREL, modeloSQL.getString(PROYECTO_NOMBRE));
            putDato(valores, EVENTO_NOMCLIENTEREL, nombreCliente);
            putDato(valores, EVENTO_AVISO, 50 * MINUTOSLONG);

            insertRegistro(TABLA_EVENTO, valores);
    }

    private String generarPDF(String id) {

        PresupuestoPDF presupuestoPDF = new PresupuestoPDF();
        presupuestoPDF.setNombreArchivo(id);
        ModeloSQL modeloSQL = CRUDutil.updateModelo(CAMPOS_PROYECTO, id);
        presupuestoPDF.crearPdf(id, modeloSQL.getString(PROYECTO_RUTAFOTO));
        ContentValues valores = new ContentValues();
        putDato(valores,PROYECTO_RUTAPDF,presupuestoPDF.getRutaArchivo());
        System.out.println("presupuestoPDF ruta = " + presupuestoPDF.getRutaArchivo());
        System.out.println("modeloSQL = " + modeloSQL);
        System.out.println("valores = " + valores);
        int res = ConsultaBD.updateRegistro(modeloSQL.getNombreTabla(), id, valores);
        System.out.println("res = " + res);
        System.out.println("modeloRutapdf = " + modeloSQL.getString(PROYECTO_RUTAPDF));

        return presupuestoPDF.getRutaArchivo();
    }

    private void modificarEstadoNoAceptado() {

        String idPresupNoAceptado;

        ArrayList<ModeloSQL> listaEstados = queryList(CAMPOS_ESTADO);

        for (ModeloSQL estado : listaEstados) {

            if (estado.getInt(ESTADO_TIPOESTADO) == TPRESUPNOACEPTADO) {

                idPresupNoAceptado = estado.getString(ESTADO_ID_ESTADO);
                ContentValues valores = new ContentValues();
                putDato(valores,PROYECTO_ID_ESTADO,idPresupNoAceptado);
                updateRegistro(TABLA_PROYECTO, id,valores);

                comprobarEstado();
                break;

            }
        }

        Log.d(TAG,"modificarEstadoNoAceptado");

    }

    private void modificarEstadoPartidas(String idProyecto, String idEstado){

        valores = new ContentValues();
        putDato(valores, PARTIDA_ID_ESTADO,idEstado);
        updateRegistroDetalle(TABLA_PARTIDA,idProyecto,TABLA_PROYECTO,valores);

        Log.d(TAG,"modificarEstadoPartidas");
    }

    @Override
    protected void setContenedor() {

        putDato(valores,PROYECTO_NOMBRE,nombrePry.getText().toString());
        putDato(valores,PROYECTO_DESCRIPCION,descripcionPry.getText().toString());
        putDato(valores,PROYECTO_ID_CLIENTE, idCliente);
        putDato(valores,PROYECTO_ID_ESTADO, idEstado);
        putDato(valores,PROYECTO_FECHAENTREGAACORDADA,fechaAcordada);
        putDato(valores,PROYECTO_FECHAENTREGAACORDADAF, JavaUtil.getDate(fechaAcordada));

        System.out.println("fechaCalculada = " + fechaCalculada);
        System.out.println("fechaAcordada = " + fechaAcordada);
        long retraso = 0;

        if (modeloSQL != null) {
            long fechafinal = modeloSQL.getLong(PROYECTO_FECHAFINAL);
            long fechaentrada = modeloSQL.getLong(PROYECTO_FECHAENTRADA);
            if (fechafinal>0){
                retraso = (JavaUtil.hoy() - fechafinal);
            }else if (fechaCalculada <= fechaAcordada || fechaEntregaP == 0) {
                if (fechaEntregaP == 0) {
                    retraso = JavaUtil.hoy() - fechaentrada;
                }
            } else {
                retraso = (fechaCalculada - fechaAcordada);
            }
        }
        putDato(valores,PROYECTO_RETRASO,retraso);
        putDato(valores,PROYECTO_IMPORTEPRESUPUESTO,preciototal);
        putDato(valores,PROYECTO_IMPORTEFINAL,importeFinalPry.getText().toString());
        putDato(valores,PROYECTO_RUTAFOTO,path);
        putDato(valores,PROYECTO_TOTCOMPLETADO,totcompletada);
        if (id ==null){

            putDato(valores,PROYECTO_FECHAENTRADA, JavaUtil.hoy());
            putDato(valores,PROYECTO_FECHAENTRADAF, JavaUtil.getDateTime(JavaUtil.hoy()));
        }

    }


    @Override
    protected void setcambioFragment() {

        id = null;
    }



    public class TareaFechasGuardar extends AsyncTask<Boolean, Float, Integer> {

        @Override
        protected Integer doInBackground(Boolean... booleans) {

            System.out.println("EJECUTANDO TAREA FECHAS FECHAS");
            Interactor.Calculos.recalcularFechas(booleans[0]);
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            valores = new ContentValues();
            putDato(valores,PROYECTO_FECHAENTREGAACORDADA,fechaCalculada);
            putDato(valores,PROYECTO_FECHAINICIOACORDADA,fechaInicioCalculada);
            putDato(valores,PROYECTO_HORAINICIOACORDADA,horaInicioCalculada);
            CRUDutil.actualizarRegistro(modeloSQL, valores);
            modeloSQL = CRUDutil.updateModelo(modeloSQL);

            System.out.println("TAREA FECHAS GUARDAR EJECUTADO");
        }

    }

    public static class Tareafechas extends AsyncTask<Boolean, Float, Integer> {

        @Override
        protected Integer doInBackground(Boolean... booleans) {

            System.out.println("EJECUTANDO TAREA FECHAS");
            Interactor.Calculos.recalcularFechas(booleans[0]);
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);


            System.out.println("TAREA FECHAS EJECUTADO");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, getMetodo());

        System.out.println("requestCode = " + requestCode);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case RECOGNIZE_SPEECH_ACTIVITY:

                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String clave = getPref(PreferenciasBase.CLAVEVOZ, "");
                    if (speech != null && clave != null && (clave.equals("") || speech.get(0).contains(clave))) {

                        if (speech.get(0).contains(clave)) {
                            grabarVoz = speech.get(0).replace(clave, "").toLowerCase();
                        } else {
                            grabarVoz = speech.get(0).toLowerCase();
                        }

                        if (grabarVoz.contains(getString(R.string.partidas))) {

                            verPartidas();

                        } else if (grabarVoz.contains(getString(R.string.presupuestos))) {

                            Toast.makeText(getContext(), PRESUPUESTOS, Toast.LENGTH_SHORT).show();
                            actual = PRESUPUESTO;
                            setSubtitulo(actual);
                            actualtemp = actual;
                            selector();

                        } else if (grabarVoz.contains(getString(R.string.proyectos))) {

                            Toast.makeText(getContext(), PROYECTOS, Toast.LENGTH_SHORT).show();
                            actual = PROYECTO;
                            setSubtitulo(actual);
                            actualtemp = actual;
                            selector();

                        } else if (grabarVoz.contains(getString(R.string.cobros))) {

                            Toast.makeText(getContext(), PROYCOBROS, Toast.LENGTH_SHORT).show();
                            actual = COBROS;
                            actualtemp = PROYECTO;
                            setSubtitulo(actual);
                            selector();

                        } else if (grabarVoz.equals("historico")) {

                            Toast.makeText(getContext(), PROYHISTORICO, Toast.LENGTH_SHORT).show();
                            actual = HISTORICO;
                            setSubtitulo(actual);
                            actualtemp = PROYECTO;
                            selector();

                        } else if (grabarVoz.contains(getString(R.string.garantias)) ||
                                grabarVoz.contains(getString(R.string.garantia))) {

                            Toast.makeText(getContext(), GARANTIA, Toast.LENGTH_SHORT).show();
                            actual = GARANTIA;
                            setSubtitulo(actual);
                            actualtemp = PROYECTO;
                            selector();

                        } else if (grabarVoz.contains(getString(R.string.nuevo_evento))) {

                            nuevoEvento();
                        } else if (grabarVoz.contains(getString(R.string.ver_eventos))) {

                            verEventos();
                        } else if (grabarVoz.contains(getString(R.string.nueva_nota))) {

                            nuevaNota();
                        } else if (grabarVoz.contains(getString(R.string.ver_notas))) {

                            verNotas();
                        }
                    }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

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
                        putDato(valores,PROYECTO_FECHAENTREGAPRESUP,fechaEntregaP);
                        putDato(valores,PROYECTO_FECHAENTREGAPRESUPF,selectedDate);
                        updateRegistro(tabla,id,valores);

                        new Tareafechas().execute(false);

                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        ViewImagenLayout imagenProyecto, imagenEstado, imagenCliente;
        TextView nombreProyecto,descripcionProyecto,clienteProyecto, estadoProyecto,
                importe;
        ProgressBar progressBarProyecto;
        CardView card;
        RelativeLayout relativeLayout;

        public ViewHolderRV(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.ry_item_list);

        }

        @Override
        public void bind(ModeloSQL modeloSQL) {

            ViewGroupLayout vistaCard = new ViewGroupLayout(contexto, relativeLayout, new CardView(contexto));
            card = (CardView) vistaCard.getViewGroup();
            LinearLayoutCompat mainLinear = (LinearLayoutCompat) vistaCard.addVista(new LinearLayoutCompat(contexto));
            mainLinear.setOrientation(ViewGroupLayout.ORI_LLC_HORIZONTAL);
            ViewGroupLayout vistaImagen = new ViewGroupLayout(contexto, mainLinear);
            vistaImagen.setOrientacion(ViewGroupLayout.ORI_LLC_VERTICAL, 2.5f);
            imagenProyecto = vistaImagen.addViewImagenLayout(1);
            Estilos.setLayoutParams(mainLinear, imagenProyecto.getLinearLayoutCompat(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT, 1, 5);
            ViewGroupLayout vistaImagen2 = new ViewGroupLayout(contexto, vistaImagen.getViewGroup());
            vistaImagen2.setOrientacion(Estilos.Constantes.ORI_LLC_HORIZONTAL, 3f);
            imagenCliente = vistaImagen2.addViewImagenLayout(1);
            Estilos.setLayoutParams(vistaImagen2.getViewGroup(), imagenCliente.getLinearLayoutCompat(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT, 1, 5);
            imagenEstado = vistaImagen2.addViewImagenLayout(1);
            Estilos.setLayoutParams(vistaImagen2.getViewGroup(), imagenEstado.getLinearLayoutCompat(), ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.MATCH_PARENT, 1, 5);
            ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, mainLinear);
            vistaForm.setOrientacion(ViewGroupLayout.ORI_LL_VERTICAL, 1);
            nombreProyecto = vistaForm.addTextView(modeloSQL.getString(PROYECTO_NOMBRE));
            descripcionProyecto = vistaForm.addTextView(modeloSQL.getString(PROYECTO_DESCRIPCION));
            clienteProyecto = vistaForm.addTextView(modeloSQL.getString(PROYECTO_CLIENTE_NOMBRE));
            estadoProyecto = vistaForm.addTextView(modeloSQL.getString(PROYECTO_DESCRIPCION_ESTADO));
            importe = vistaForm.addTextView(JavaUtil.formatoMonedaLocal(modeloSQL.getDouble(PROYECTO_IMPORTEPRESUPUESTO)));
            progressBarProyecto = (ProgressBar) vistaForm.addVista(new ProgressBar(contexto, null, Estilos.pBarStyleAcept(contexto)));


            nombreProyecto.setText(modeloSQL.getString(PROYECTO_NOMBRE));
            descripcionProyecto.setText(modeloSQL.getString(PROYECTO_DESCRIPCION));
            clienteProyecto.setText(modeloSQL.getString(PROYECTO_CLIENTE_NOMBRE));
            estadoProyecto.setText(modeloSQL.getString(PROYECTO_DESCRIPCION_ESTADO));
            importe.setText(JavaUtil.formatoMonedaLocal(modeloSQL.getDouble(PROYECTO_IMPORTEPRESUPUESTO)));

            if (actualtemp.equals(PROYECTO)){

                progressBarProyecto.setProgress(modeloSQL.getInt(PROYECTO_TOTCOMPLETADO));

                long retraso = modeloSQL.getLong(PROYECTO_RETRASO);
                if (retraso > 3 * DIASLONG){imagenEstado.setImageResource(R.drawable.alert_box_r);} else if (retraso > DIASLONG) {
                    imagenEstado.setImageResource(contexto, R.drawable.alert_box_a);
                }
                else {imagenEstado.setImageResource(R.drawable.alert_box_v);}

            }else{

                progressBarProyecto.setVisibility(View.GONE);
                long retraso = modeloSQL.getLong(PROYECTO_RETRASO);
                if (retraso > 3 * DIASLONG){imagenEstado.setImageResource(R.drawable.alert_box_r);} else if (retraso > DIASLONG) {
                    imagenEstado.setImageResource(contexto, R.drawable.alert_box_a);
                }
                else {imagenEstado.setImageResource(R.drawable.alert_box_v);}

            }
            if (modeloSQL.getString(PROYECTO_RUTAFOTO) != null) {
                imagenProyecto.setImageUriCard(activityBase, modeloSQL.getString(PROYECTO_RUTAFOTO));
            }
            int peso = modeloSQL.getInt(PROYECTO_CLIENTE_PESOTIPOCLI);

            if (peso>6){imagenCliente.setImageResource(R.drawable.clientev);}
            else if (peso>3){imagenCliente.setImageResource(R.drawable.clientea);}
            else if (peso>0){imagenCliente.setImageResource(R.drawable.clienter);}
            else {imagenCliente.setImageResource(R.drawable.cliente);}

            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {

            return new ViewHolderRV(view);
        }
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<ModeloSQL> entrada) {

            ImagenLayout imagen = view.findViewById(R.id.imglistaproyectos);
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
            if (retraso > 3 * Interactor.DIASLONG) {
                imgest.setImageResource(R.drawable.alert_box_r);
            } else if (retraso > Interactor.DIASLONG) {
                imgest.setImageResource(R.drawable.alert_box_a);
            } else {
                imgest.setImageResource(R.drawable.alert_box_v);
            }

            if (entrada.get(posicion).getCampos(ContratoPry.Tablas.PROYECTO_RUTAFOTO) != null) {
                //imagenTarea.setImageURI(Uri.parse(entrada.getCampos
                //        (ContratoPry.Tablas.PROYECTO_RUTAFOTO)));
                imagen.setImageUriCard(activityBase,entrada.get(posicion).getString(PROYECTO_RUTAFOTO));
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






