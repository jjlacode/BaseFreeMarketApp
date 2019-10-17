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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.time.DatePickerFragment;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.codevsolution.freemarketsapp.templates.PresupuestoPDF;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.codevsolution.base.sqlite.ConsultaBD.checkQueryList;
import static com.codevsolution.base.sqlite.ConsultaBD.insertRegistro;
import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
import static com.codevsolution.base.sqlite.ConsultaBD.queryList;
import static com.codevsolution.base.sqlite.ConsultaBD.queryListDetalle;
import static com.codevsolution.base.sqlite.ConsultaBD.queryObject;
import static com.codevsolution.base.sqlite.ConsultaBD.updateRegistro;
import static com.codevsolution.base.sqlite.ConsultaBD.updateRegistrosDetalle;

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
    private ImageButton btnVerPdf;
    private ImageButton btnenviarPdf;
    private ImageButton btnVerEventos;
    private Spinner spEstadoProyecto;


    private ArrayList<String> listaEstados;
    private ArrayList<ModeloSQL> listaClientes;
    private ArrayList<ModeloSQL> objEstados;

    private String idCliente;
    private String idEstado;
    private String nombreCliente;

    private int peso = 0;
    private int posCliente = 0;
    private int totcompletada = 0;

    private long fechaCalculada = 0;
    private long fechaInicioCalculada = 0;
    private long horaInicioCalculada = 0;
    private long horaInicioAcordada = 0;
    private long fechaInicioAcordada = 0;
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

    public FragmentCRUDProyecto() {
        // Required empty public constructor
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

    protected void actualizarConsultas() {

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
                }
            }
            lista = CRUDutil.clonaListaModelo(campos,listatemp);

            enviarAct();
            setSubtitulo(actual);
        }

    }

    @Override
    protected void setLayout() {

        //layoutCuerpo = R.layout.fragment_crud_proyecto;
        //layoutCabecera = R.layout.cabecera_crud_proyecto;
        layoutItem = R.layout.item_list_proyecto;
        cabecera = true;

    }

    @Override
    protected void setTabla() {

        tabla = TABLA_PROYECTO;
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

        if (actual.equals(PRESUPUESTO)) {
            if (actualtemp==null) {
                actualtemp = actual;
            }
        }else if (actual.equals(PROYECTO)){
            if (actualtemp==null) {
                actualtemp = actual;
            }
        }else if (actual.equals(COBROS)){
            if (actualtemp==null) {
                actualtemp = PROYECTO;
            }
        }else if (actual.equals(HISTORICO)){
            if (actualtemp==null) {
                actualtemp = PROYECTO;
            }
        } else if (actual.equals(GARANTIA)) {
            if (actualtemp == null) {
                actualtemp = PROYECTO;
            }
        }else{
            if (actualtemp==null) {
                actualtemp = actual;
            }

        }
        setSubtitulo(actual);

    }

    private void setSubtitulo(String actual){

        switch (actual) {
            case PRESUPUESTO:
                subTitulo = getString(R.string.presupuestos);
                break;
            case PROYECTO:
                subTitulo = getString(R.string.proyectos);
                break;
            case COBROS:
                subTitulo = getString(R.string.cobros);
                break;
            case HISTORICO:
                subTitulo = getString(R.string.historico);
                break;
            case GARANTIA:
                subTitulo = getString(R.string.garantia);
                break;
            default:
                subTitulo = getString(R.string.proyectos);

                break;
        }

        activityBase.toolbar.setSubtitle(subTitulo);
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
        bundle.putSerializable(LISTA, new ListaModeloSQL(CAMPOS_EVENTO, EVENTO_PROYECTOREL, id, null, IGUAL, null));
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

        tituloSingular = R.string.proyecto;
        tituloPlural = R.string.proyectos;
        tituloNuevo = R.string.nuevo_proyecto;

    }


    @Override
    protected void setInicio() {

        new Tareafechas().execute(false);

        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalle);

        imagen = (ImagenLayout) vistaForm.addVista(new ImagenLayout(contexto));
        imagen.setFocusable(false);
        imagen.setTextTitulo(tituloSingular);
        nombrePry = vistaForm.addEditMaterialLayout(getString(R.string.nombre), PROYECTO_NOMBRE, null, null);
        descripcionPry = vistaForm.addEditMaterialLayout(getString(R.string.descripcion), PROYECTO_DESCRIPCION, null, null);
        descripcionPry.setTipo(EditMaterialLayout.TEXTO|EditMaterialLayout.MULTI);
        spClienteProyecto = vistaForm.addEditMaterialLayout(getString(R.string.cliente));
        spClienteProyecto.setActivo(false);
        spClienteProyecto.btnInicioVisible(false);
        spClienteProyecto.btnAccion2Enable(true);
        spClienteProyecto.setImgBtnAccion2(R.drawable.cliente);
        spEstadoProyecto = (Spinner) vistaForm.addVista(new Spinner(contexto));
        estadoProyecto = vistaForm.addEditMaterialLayout(R.string.estado);
        estadoProyecto.btnAccion2Enable(true);
        estadoProyecto.btnInicioVisible(false);
        estadoProyecto.setImgBtnAccion2(R.drawable.alert_box_v);
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
        ViewGroupLayout vistaAcordada = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaAcordada.setOrientacion(LinearLayoutCompat.HORIZONTAL);
        fechaInicioCalculadaPry = vistaAcordada.addEditMaterialLayout(R.string.fecha_acordada);
        fechaInicioCalculadaPry.setActivo(false);
        fechaInicioCalculadaPry.btnInicioVisible(false);

        horaInicioCalculadaPry = vistaAcordada.addEditMaterialLayout(R.string.hora_acordada);
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
        totalPartidasPry = vistaImportepartidas.addEditMaterialLayout(R.string.tiempo_partidas);
        totalPartidasPry.setActivo(false);
        totalPartidasPry.btnInicioVisible(false);
        pvpPartidas = vistaImportepartidas.addEditMaterialLayout(R.string.total_partidas);
        pvpPartidas.setActivo(false);
        pvpPartidas.btnInicioVisible(false);
        actualizarArrays(vistaImportepartidas);
        ViewGroupLayout vistaImporte = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistaImporte.setOrientacion(LinearLayoutCompat.HORIZONTAL);
        importeCalculadoPry = vistaImporte.addEditMaterialLayout(R.string.importe_calculado);
        importeCalculadoPry.setActivo(false);
        importeCalculadoPry.btnInicioVisible(false);
        importeFinalPry = vistaImporte.addEditMaterialLayout(R.string.importe_final);
        actualizarArrays(vistaImporte);
        btnPartidasPry = vistaForm.addButtonPrimary(R.string.partidas_proyecto);
        btnPartidasPry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verPartidas();
            }
        });
        ViewGroupLayout vistabtn = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
        vistabtn.setOrientacion(LinearLayoutCompat.HORIZONTAL);

        btnEvento = vistabtn.addImageButtonSecundary(R.drawable.ic_evento_indigo);
        btnEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevoEvento();
            }
        });
        btnVerEventos = vistabtn.addImageButtonSecundary(R.drawable.ic_lista_eventos_indigo);
        btnVerEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verEventos();
            }
        });
        btnNota = vistabtn.addImageButtonSecundary(R.drawable.ic_nueva_nota_indigo);
        btnNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevaNota();
            }
        });
        btnVerNotas = vistabtn.addImageButtonSecundary(R.drawable.ic_lista_notas_indigo);
        btnVerNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verNotas();
            }
        });
        actualizarArrays(vistabtn);
        btnActualizar = vistaForm.addButtonPrimary(R.string.estado);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarEstado();
                update();

                if (peso > 6) {
                    spClienteProyecto.setImgBtnAccion2(R.drawable.clientev);
                } else if (peso > 3) {
                    spClienteProyecto.setImgBtnAccion2(R.drawable.clientea);
                } else if (peso > 0) {
                    spClienteProyecto.setImgBtnAccion2(R.drawable.clienter);
                } else {
                    spClienteProyecto.setImgBtnAccion2(R.drawable.cliente);
                }

                setDatos();
            }
        });
        btnActualizar2 = vistaForm.addButtonPrimary(R.string.estado);
        btnActualizar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarEstadoNoAceptado();
                update();
                setDatos();
            }
        });
        btnVerPdf = vistaForm.addImageButtonSecundary(R.drawable.ic_pdf_indigo);
        btnVerPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modeloSQL.getString(PROYECTO_RUTAPDF) != null) {
                    AppActivity.mostrarPDF(modeloSQL.getString(PROYECTO_RUTAPDF));
                } else {
                    update();
                }

            }
        });

        btnenviarPdf = vistaForm.addImageButtonSecundary(R.drawable.ic_txt_pdf_indigo);
        btnenviarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modeloSQL.getString(PROYECTO_RUTAPDF) != null) {

                    ModeloSQL cliente = CRUDutil.updateModelo(CAMPOS_CLIENTE, idCliente);
                    String email = cliente.getString(CLIENTE_EMAIL);
                    String asunto = "Presupuesto solicitado";
                    String mensaje = "Envio presupuesto" + modeloSQL.getString(PROYECTO_NOMBRE) + "solicitado por usted";
                    PresupuestoPDF presupuestoPDF = new PresupuestoPDF();
                    presupuestoPDF.buscarPDF(modeloSQL.getString(PROYECTO_RUTAPDF));
                    presupuestoPDF.enviarPDFEmail(contexto, modeloSQL.getString(PROYECTO_RUTAPDF), email, asunto, mensaje);
                } else {
                    update();
                }
            }
        });
        btncompartirPdf = vistaForm.addImageButtonSecundary(R.drawable.ic_compartir_indigo);
        btncompartirPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modeloSQL.getString(PROYECTO_RUTAPDF) != null) {

                    AppActivity.compartirPdf(modeloSQL.getString(PROYECTO_RUTAPDF));

                } else {
                    update();
                }
            }
        });

        actualizarArrays(vistaForm);

        ViewGroupLayout vistaCab = new ViewGroupLayout(contexto, frCabecera);
        vistaCab.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);

        btnpresupuestos = vistaCab.addButtonSecondary("Presup");
        btnproyectos = vistaCab.addButtonSecondary(R.string.proyecto);
        btncobros = vistaCab.addButtonSecondary(R.string.cobros);
        btnhistorico = vistaCab.addButtonSecondary(R.string.historico);
        btngarantias = vistaCab.addButtonSecondary(R.string.garantia);

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



    @Override
    protected void setDatos() {

        Interactor.Calculos.actualizarPresupuesto(id);
        //new TareaFechasDatos().execute(false);

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
        visible(imagen);

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
            spClienteProyecto.setImgBtnAccion2(R.drawable.clientev);
        } else if (peso > 3) {
            spClienteProyecto.setImgBtnAccion2(R.drawable.clientea);
        } else if (peso > 0) {
            spClienteProyecto.setImgBtnAccion2(R.drawable.clienter);
        } else {
            spClienteProyecto.setImgBtnAccion2(R.drawable.cliente);
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
            btnActualizar.setVisibility(View.GONE);
            btnActualizar2.setVisibility(View.GONE);
            gone(pvpPartidas.getLinearLayout());
            gone(fechaEntregaPresup.getLinearLayout());
            gone(totalPartidasPry.getLinearLayout());

        } else {

            visible(fechaCalculadaPry.getLinearLayout());
            visible(fechaInicioCalculadaPry.getLinearLayout());
            visible(btnActualizar);
            visible(pvpPartidas.getLinearLayout());
            gone(fechaEntregaPresup.getLinearLayout());
            visible(totalPartidasPry.getLinearLayout());
            fechaCalculada = modeloSQL.getLong(PROYECTO_FECHAENTREGACALCULADA);
            fechaInicioCalculada = modeloSQL.getLong(PROYECTO_FECHAINICIOCALCULADA);
            horaInicioCalculada = modeloSQL.getLong(PROYECTO_HORAINICIOCALCULADA);

            if (modeloSQL.getInt(PROYECTO_TIPOESTADO) == TNUEVOPRESUP) {
                valores = new ContentValues();
                setDato(PROYECTO_PRECIOHORA, Interactor.hora);
                setDato(PROYECTO_FECHAENTREGAACORDADA,fechaCalculada);
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
            estadoProyecto.setImgBtnAccion2(R.drawable.alert_box_r);
        } else if (retraso > Interactor.DIASLONG) {
            estadoProyecto.setImgBtnAccion2(R.drawable.alert_box_a);
            } else {
            estadoProyecto.setImgBtnAccion2(R.drawable.alert_box_v);
            }

        if (modeloSQL.getInt(PROYECTO_TIPOESTADO) >= TPRESUPPENDENTREGA) {

                btnVerPdf.setVisibility(View.VISIBLE);
                btnenviarPdf.setVisibility(View.VISIBLE);
                btncompartirPdf.setVisibility(View.VISIBLE);
        }else{

            btnVerPdf.setVisibility(View.GONE);
            btnenviarPdf.setVisibility(View.GONE);
            btncompartirPdf.setVisibility(View.GONE);
        }

        comprobarEstado();


        if (idCliente!=null) {
            ModeloSQL cliente = queryObject(CAMPOS_CLIENTE, idCliente);
            nombreCliente = cliente.getString(CLIENTE_NOMBRE);
            spClienteProyecto.setText(nombreCliente);
        }

    }


    private void calculoTotales() {

        ArrayList<ModeloSQL> listaPartidas = queryListDetalle(CAMPOS_PARTIDA, id, TABLA_PROYECTO);
        double precioHora;

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
                setDato(PROYECTO_PRECIOHORA, Interactor.hora);
                setDato(PROYECTO_FECHAENTREGACALCULADA,fechaCalculada);
                CRUDutil.actualizarRegistro(modeloSQL, valores);
                modeloSQL = CRUDutil.updateModelo(modeloSQL);
            } else if (modeloSQL.getInt(PROYECTO_TIPOESTADO) >= TPRESUPACEPTADO) {
                valores = new ContentValues();
                setDato(PROYECTO_FECHAENTREGAACORDADA,fechaCalculada);
                setDato(PROYECTO_FECHAINICIOACORDADA,fechaInicioCalculada);
                setDato(PROYECTO_HORAINICIOACORDADA,horaInicioCalculada);
                CRUDutil.actualizarRegistro(modeloSQL, valores);
                modeloSQL = CRUDutil.updateModelo(modeloSQL);
            }

                calculoTotales();
        }

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

        ArrayList<ModeloSQL> listaEstados = queryList(CAMPOS_ESTADO, null, null);

        for (ModeloSQL estado : listaEstados) {

            if (estado.getInt(ESTADO_TIPOESTADO)==tipoEstado){

                    return estado.getString(ESTADO_ID_ESTADO);
            }

        }
        return null;
    }

    private String getIdEstado(String descTipoEstado){

        ArrayList<ModeloSQL> listaEstados = queryList(CAMPOS_ESTADO, null, null);

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
            //showDatePickerDialogEntrega();
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
        putDato(valores,CAMPOS_PROYECTO,PROYECTO_ID_ESTADO,idEstado);
        updateRegistro(TABLA_PROYECTO, id,valores);
        CRUDutil.actualizarRegistro(modeloSQL, valores);

        modificarEstadoPartidas(id,idEstado);

        comprobarEstado();

        new Interactor.Calculos.TareaTipoCliente().execute(idCliente);

        Log.d(TAG,"modificarEstado");
    }

    private void actualizarVinculoPartidaBase() {


        ListaModeloSQL listaPartidas = new ListaModeloSQL(CAMPOS_PARTIDA, id, tabla, null, null);
        int res = 0;

        for (ModeloSQL partida : listaPartidas.getLista()) {

            String idpartidabase = partida.getString(PARTIDA_ID_PARTIDABASE);
            if (idpartidabase!=null){

                ContentValues values = new ContentValues();
                values.putNull(PARTIDA_ID_PARTIDABASE);
                res += CRUDutil.actualizarRegistro(partida,values);
            }

        }
        Log.d(TAG,"Partidas base desvinculadas = " + res);
    }

    private void crearEventoPresupuesto() {

       new TareaGenerarPdf().execute(id);

            long fechaini = JavaUtil.sumaDiaMesAnio(JavaUtil.hoy());
            long horaini = JavaUtil.sumaHoraMin(JavaUtil.hoy());
            String asunto = "Presupuesto solicitado";
            String mensaje = "Envio presupuesto solicitado por usted";
        ModeloSQL cliente = queryObject(CAMPOS_CLIENTE, idCliente);
            valores = new ContentValues();
        putDato(valores, CAMPOS_EVENTO, EVENTO_TIPO, Interactor.TiposEvento.TIPOEVENTOEMAIL);
            putDato(valores, CAMPOS_EVENTO, EVENTO_DESCRIPCION, "Envío presupuesto a cliente");
            putDato(valores, CAMPOS_EVENTO, EVENTO_EMAIL, cliente.getString(CLIENTE_EMAIL));
            putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTO, fechaini);
            putDato(valores, CAMPOS_EVENTO, EVENTO_HORAINIEVENTO, horaini + HORASLONG);
            putDato(valores, CAMPOS_EVENTO, EVENTO_FECHAINIEVENTOF, JavaUtil.getDate(fechaini));
            putDato(valores, CAMPOS_EVENTO, EVENTO_HORAINIEVENTOF, JavaUtil.getTime(horaini + HORASLONG));
            putDato(valores, CAMPOS_EVENTO, EVENTO_ASUNTO, asunto);
            putDato(valores, CAMPOS_EVENTO, EVENTO_MENSAJE, mensaje);
            putDato(valores, CAMPOS_EVENTO, EVENTO_RUTAADJUNTO,rutaPdf);
        putDato(valores, CAMPOS_EVENTO, EVENTO_RUTAFOTO, modeloSQL.getString(PROYECTO_RUTAFOTO));
            putDato(valores, CAMPOS_EVENTO, EVENTO_PROYECTOREL, id);
            putDato(valores, CAMPOS_EVENTO, EVENTO_CLIENTEREL, idCliente);
        putDato(valores, CAMPOS_EVENTO, EVENTO_NOMPROYECTOREL, modeloSQL.getString(PROYECTO_NOMBRE));
            putDato(valores, CAMPOS_EVENTO, EVENTO_NOMCLIENTEREL, nombreCliente);
            putDato(valores, CAMPOS_EVENTO, EVENTO_AVISO, 50 * MINUTOSLONG);

            insertRegistro(TABLA_EVENTO, valores);
    }

    private static String generarPDF(String id) {

        PresupuestoPDF presupuestoPDF = new PresupuestoPDF();
        presupuestoPDF.setNombreArchivo(id);
        ModeloSQL modeloSQL = CRUDutil.updateModelo(CAMPOS_PROYECTO, id);
        presupuestoPDF.crearPdf(id, modeloSQL.getString(PROYECTO_RUTAFOTO));
        ContentValues valores = new ContentValues();
        putDato(valores,CAMPOS_PROYECTO,PROYECTO_RUTAPDF,presupuestoPDF.getRutaArchivo());
        System.out.println("presupuestoPDF ruta = " + presupuestoPDF.getRutaArchivo());
        System.out.println("modeloSQL = " + modeloSQL);
        System.out.println("valores = " + valores);
        int res = ConsultaBD.updateRegistro(modeloSQL.getNombreTabla(), id, valores);
        System.out.println("res = " + res);
        System.out.println("modeloRutapdf = " + modeloSQL.getString(PROYECTO_RUTAPDF));

        return presupuestoPDF.getRutaArchivo();
    }
    public static class TareaGenerarPdf extends AsyncTask<String, Float, Boolean> {

        private String rutaPdftmp;

        @Override
        protected Boolean doInBackground(String... strings) {

            rutaPdftmp = generarPDF(strings[0]);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            rutaPdf = rutaPdftmp;
            System.out.println("rutaPdf = " + rutaPdf);

        }
    }


    private void modificarEstadoNoAceptado() {

        String idPresupNoAceptado;

        ArrayList<ModeloSQL> listaEstados = queryList(CAMPOS_ESTADO, null, null);

        for (ModeloSQL estado : listaEstados) {

            if (estado.getInt(ESTADO_TIPOESTADO) == TPRESUPNOACEPTADO) {

                idPresupNoAceptado = estado.getString(ESTADO_ID_ESTADO);
                ContentValues valores = new ContentValues();
                putDato(valores,CAMPOS_PROYECTO,PROYECTO_ID_ESTADO,idPresupNoAceptado);
                updateRegistro(TABLA_PROYECTO, id,valores);

                comprobarEstado();
                break;

            }
        }

        Log.d(TAG,"modificarEstadoNoAceptado");

    }

    private void modificarEstadoPartidas(String idProyecto, String idEstado){

        valores = new ContentValues();
        putDato(valores,CAMPOS_PARTIDA, PARTIDA_ID_ESTADO,idEstado);
        updateRegistrosDetalle(TABLA_PARTIDA,idProyecto,TABLA_PROYECTO,valores,null);

        Log.d(TAG,"modificarEstadoPartidas");
    }

    @Override
    protected void setContenedor() {

        setDato(PROYECTO_NOMBRE,nombrePry.getText().toString());
        setDato(PROYECTO_DESCRIPCION,descripcionPry.getText().toString());
        setDato(PROYECTO_ID_CLIENTE, idCliente);
        setDato(PROYECTO_ID_ESTADO, idEstado);
        setDato(PROYECTO_FECHAENTREGAACORDADA,fechaAcordada);
        setDato(PROYECTO_FECHAENTREGAACORDADAF, JavaUtil.getDate(fechaAcordada));

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
        setDato(PROYECTO_RETRASO,retraso);
        setDato(PROYECTO_IMPORTEPRESUPUESTO,preciototal);
        setDato(PROYECTO_IMPORTEFINAL,importeFinalPry.getText().toString(),DOUBLE);
        setDato(PROYECTO_RUTAFOTO,path);
        setDato(PROYECTO_TOTCOMPLETADO,totcompletada);
        if (id ==null){

            setDato(PROYECTO_FECHAENTRADA, JavaUtil.hoy());
            setDato(PROYECTO_FECHAENTRADAF, JavaUtil.getDateTime(JavaUtil.hoy()));
        }

        if (modeloSQL != null && id != null && Interactor.getTipoEstado(modeloSQL.getString(PROYECTO_ID_ESTADO)) >= TPRESUPPENDENTREGA) {
            new TareaGenerarPdf().execute(id);
            System.out.println("Generar pdf");
        }

    }


    @Override
    protected void setcambioFragment() {

        id = null;
    }

    private void listaObjetosEstados() {

        objEstados = new ArrayList<>();
        String seleccion = null;
        /*
        if (actual.equals(PROYECTO)){
            seleccion = ESTADO_TIPOESTADO + " >3";
        }else if (actual.equals(PRESUPUESTO)){
            seleccion = ESTADO_TIPOESTADO + " <4";
        }else if (actual.equals(COBROS)){
            seleccion = ESTADO_TIPOESTADO + " >6";
        }else if (actual.equals(HISTORICO)){
            seleccion = ESTADO_TIPOESTADO + " == 8";
        }

         */

        objEstados = queryList(CAMPOS_ESTADO,seleccion,null);

        obtenerListaEstados();
    }

    private void obtenerListaEstados() {

        listaEstados = new ArrayList<String>();
        listaEstados.add(getString(R.string.seleccion_estado));

        for (int i=0;i<objEstados.size();i++){

            listaEstados.add(objEstados.get(i).getString(ESTADO_DESCRIPCION));
        }
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
            setDato(PROYECTO_FECHAENTREGAACORDADA,fechaCalculada);
            setDato(PROYECTO_FECHAINICIOACORDADA,fechaInicioCalculada);
            setDato(PROYECTO_HORAINICIOACORDADA,horaInicioCalculada);
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

    public class TareaFechasDatos extends AsyncTask<Boolean, Float, Integer> {

        @Override
        protected Integer doInBackground(Boolean... booleans) {

            System.out.println("TAREA FECHAS DATOS EJECUTANDO");
            Interactor.Calculos.recalcularFechas(booleans[0]);
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            actualizarFechas();
            System.out.println("TAREA FECHAS DATOS EJECUTADO");
        }
    }

    private void actualizarFechas(){

        System.out.println("TAREA FECHAS DATOS EJECUTADO");
        fechaInicioCalculadaPry.setText(modeloSQL.getString(PROYECTO_FECHAINICIOCALCULADAF));
        fechaCalculadaPry.setText(modeloSQL.getString(PROYECTO_FECHAENTREGACALCULADAF));
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
                    grabarVoz = speech.get(0);

                    if (grabarVoz.equals("partidas")) {

                        verPartidas();

                    } else if (grabarVoz.equals("presupuestos")) {

                        Toast.makeText(getContext(), PRESUPUESTOS, Toast.LENGTH_SHORT).show();
                        actual = PRESUPUESTO;
                        setSubtitulo(actual);
                        actualtemp = actual;
                        selector();

                    } else if (grabarVoz.equals("proyectos")) {

                        Toast.makeText(getContext(), PROYECTOS, Toast.LENGTH_SHORT).show();
                        actual = PROYECTO;
                        setSubtitulo(actual);
                        actualtemp = actual;
                        selector();

                    } else if (grabarVoz.equals("cobros")) {

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

                    } else if (grabarVoz.equals("garantias")) {

                        Toast.makeText(getContext(), GARANTIA, Toast.LENGTH_SHORT).show();
                        actual = GARANTIA;
                        setSubtitulo(actual);
                        actualtemp = PROYECTO;
                        selector();

                    } else if (grabarVoz.equals("nuevo evento")) {

                        nuevoEvento();
                    } else if (grabarVoz.equals("ver eventos")) {

                        verEventos();
                    } else if (grabarVoz.equals("nueva nota")) {

                        nuevaNota();
                    } else if (grabarVoz.equals("ver notas")) {

                        verNotas();
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
                        setDato(PROYECTO_FECHAENTREGAPRESUP,fechaEntregaP);
                        setDato(PROYECTO_FECHAENTREGAPRESUPF,selectedDate);
                        updateRegistro(tabla,id,valores);

                        new Tareafechas().execute(false);

                    }
                });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        ImagenLayout imagenProyecto;
        ImageView imagenEstado, imagenCliente;
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
        public void bind(ModeloSQL modeloSQL) {

            nombreProyecto.setText(modeloSQL.getString(PROYECTO_NOMBRE));
            descripcionProyecto.setText(modeloSQL.getString(PROYECTO_DESCRIPCION));
            clienteProyecto.setText(modeloSQL.getString(PROYECTO_CLIENTE_NOMBRE));
            estadoProyecto.setText(modeloSQL.getString(PROYECTO_DESCRIPCION_ESTADO));
            importe.setText(JavaUtil.formatoMonedaLocal(modeloSQL.getDouble(PROYECTO_IMPORTEPRESUPUESTO)));

            if (actualtemp.equals(PROYECTO)){

                progressBarProyecto.setProgress(modeloSQL.getInt(PROYECTO_TOTCOMPLETADO));

                long retraso = modeloSQL.getLong(PROYECTO_RETRASO);
                if (retraso > 3 * DIASLONG){imagenEstado.setImageResource(R.drawable.alert_box_r);}
                else if (retraso > DIASLONG){imagenEstado.setImageResource(R.drawable.alert_box_a);}
                else {imagenEstado.setImageResource(R.drawable.alert_box_v);}

            }else{

                progressBarProyecto.setVisibility(View.GONE);
                long retraso = modeloSQL.getLong(PROYECTO_RETRASO);
                if (retraso > 3 * DIASLONG){imagenEstado.setImageResource(R.drawable.alert_box_r);}
                else if (retraso > DIASLONG){imagenEstado.setImageResource(R.drawable.alert_box_a);}
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






