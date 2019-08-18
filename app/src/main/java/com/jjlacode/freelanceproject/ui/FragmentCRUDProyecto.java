package com.jjlacode.freelanceproject.ui;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.adapter.BaseViewHolder;
import com.jjlacode.base.util.adapter.ListaAdaptadorFiltro;
import com.jjlacode.base.util.adapter.ListaAdaptadorFiltroModelo;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.base.util.android.AppActivity;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.base.util.android.controls.ImagenLayout;
import com.jjlacode.base.util.android.controls.ScalableImageView;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.crud.FragmentCRUD;
import com.jjlacode.base.util.crud.ListaModelo;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.media.MediaUtil;
import com.jjlacode.base.util.sqlite.ConsultaBD;
import com.jjlacode.base.util.sqlite.ContratoPry;
import com.jjlacode.base.util.time.DatePickerFragment;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.logica.Interactor;
import com.jjlacode.freelanceproject.templates.PresupuestoPDF;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.jjlacode.base.util.sqlite.ConsultaBD.checkQueryList;
import static com.jjlacode.base.util.sqlite.ConsultaBD.insertRegistro;
import static com.jjlacode.base.util.sqlite.ConsultaBD.putDato;
import static com.jjlacode.base.util.sqlite.ConsultaBD.queryList;
import static com.jjlacode.base.util.sqlite.ConsultaBD.queryListDetalle;
import static com.jjlacode.base.util.sqlite.ConsultaBD.queryObject;
import static com.jjlacode.base.util.sqlite.ConsultaBD.updateRegistro;
import static com.jjlacode.base.util.sqlite.ConsultaBD.updateRegistrosDetalle;

public class FragmentCRUDProyecto extends FragmentCRUD
        implements Interactor.Constantes, ContratoPry.Tablas, Interactor.Estados,
        Interactor.TiposEstados {

    private ScalableImageView imagenTipoClienteProyecto;
    private ImageView btnfechaentrega;
    private AutoCompleteTextView spClienteProyecto;
    private EditMaterial nombrePry;
    private EditMaterial descripcionPry;
    private EditMaterial estadoProyecto;
    private EditMaterial fechaEntregaPresup;
    private EditMaterial fechaAcordadaPry;
    private EditMaterial importeFinalPry;
    private EditMaterial fechaEntradaPry;
    private EditMaterial fechaCalculadaPry;
    private EditMaterial fechaFinalPry;
    private EditMaterial totalPartidasPry;
    private EditMaterial pvpPartidas;
    private EditMaterial importeCalculadoPry;
    private ImageButton btnEvento;
    private Button btnPartidasPry;
    private Button btnActualizar;
    private Button btnActualizar2;
    private ImageButton btncompartirPdf;
    private ImageButton btnVerPdf;
    private ImageButton btnenviarPdf;
    private ImageButton btnVerEventos;
    private ScalableImageView btnimgEstadoPry;
    private ImageView btnfechaacord;
    private Spinner spEstadoProyecto;


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
    private Button btngarantias;

    public String actualtemp;
    private ImageButton btnNota;
    private ImageButton btnVerNotas;
    public static String rutaPdf;
    private boolean enGarantia;

    public FragmentCRUDProyecto() {
        // Required empty public constructor
    }

    @Override
    protected void setTAG() {
        TAG = getClass().getSimpleName();
    }

    @Override
    protected TipoViewHolder setViewHolder(View view){

        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }


    @Override
    protected void setLista() {

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

        System.out.println("lista.sizeLista() = " + lista.sizeLista());

        if (lista.chechLista()) {

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
                        System.out.println("estado = " + estado);

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
            System.out.println("listatemp = " + listatemp.size());
            lista = CRUDutil.clonaListaModelo(campos,listatemp);
            System.out.println("lista final= " + lista.sizeLista());

            enviarAct();
        }

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_proyecto;
        layoutCabecera = R.layout.cabecera_crud_proyecto;
        layoutItem = R.layout.item_list_proyecto;

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
            idCliente = bundle.getString(CLIENTE);
            System.out.println("idCliente bundle = " + idCliente);

        }

        if (id !=null) {
                modelo = CRUDutil.setModelo(campos,id);
                idCliente = modelo.getString(PROYECTO_ID_CLIENTE);
            new Interactor.Calculos.TareaActualizaProy().execute(id);
        }

        if (actual==null){
            actual=PROYECTO;
        }

        if (actual.equals(PRESUPUESTO)) {
            activityBase.toolbar.setTitle(R.string.presupuestos);
            if (actualtemp==null) {
                actualtemp = actual;
            }
        }else if (actual.equals(PROYECTO)){
            activityBase.toolbar.setTitle(R.string.proyectos);
            if (actualtemp==null) {
                actualtemp = actual;
            }
        }else if (actual.equals(COBROS)){
            activityBase.toolbar.setTitle(R.string.cobros);
            if (actualtemp==null) {
                actualtemp = PROYECTO;
            }
        }else if (actual.equals(HISTORICO)){
            activityBase.toolbar.setTitle(R.string.historico);
            if (actualtemp==null) {
                actualtemp = PROYECTO;
            }
        } else if (actual.equals(GARANTIA)) {
            activityBase.toolbar.setTitle(R.string.garantia);
            if (actualtemp == null) {
                actualtemp = PROYECTO;
            }
        }else{
            activityBase.toolbar.setTitle(R.string.proyectos);
            if (actualtemp==null) {
                actualtemp = actual;
            }

        }

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
                activityBase.toolbar.setTitle(R.string.presupuestos);
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
                activityBase.toolbar.setTitle(R.string.proyectos);
                selector();
            }
        });

        btncobros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),PROYCOBROS, Toast.LENGTH_SHORT).show();
                actual = COBROS;
                actualtemp = PROYECTO;
                activityBase.toolbar.setTitle(R.string.cobros);
                selector();
            }
        });

        btngarantias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), PROYGARANTIA, Toast.LENGTH_SHORT).show();
                actual = GARANTIA;
                actualtemp = PROYECTO;
                activityBase.toolbar.setTitle(R.string.garantia);
                selector();
            }
        });

        btnhistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),PROYHISTORICO, Toast.LENGTH_SHORT).show();
                actual = HISTORICO;
                actualtemp = PROYECTO;
                activityBase.toolbar.setTitle(R.string.historico);
                selector();
            }
        });


        btnVerPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (modelo.getString(PROYECTO_RUTAPDF)!=null) {
                    AppActivity.mostrarPDF(modelo.getString(PROYECTO_RUTAPDF));
                }else{
                    update();
                }

            }
        });

        btnenviarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (modelo.getString(PROYECTO_RUTAPDF)!=null) {

                    Modelo cliente = CRUDutil.setModelo(CAMPOS_CLIENTE, idCliente);
                    String email = cliente.getString(CLIENTE_EMAIL);
                    String asunto = "Presupuesto solicitado";
                    String mensaje = "Envio presupuesto" + modelo.getString(PROYECTO_NOMBRE) + "solicitado por usted";
                    PresupuestoPDF presupuestoPDF = new PresupuestoPDF();
                    presupuestoPDF.buscarPDF(modelo.getString(PROYECTO_RUTAPDF));
                    presupuestoPDF.enviarPDFEmail(contexto, modelo.getString(PROYECTO_RUTAPDF), email, asunto, mensaje);
                }else{
                    update();
                }

            }
        });

        btncompartirPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (modelo.getString(PROYECTO_RUTAPDF)!=null) {

                    AppActivity.compartirPdf(modelo.getString(PROYECTO_RUTAPDF));

                }else{
                    update();
                }

            }
        });

        btnEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nuevoEvento();
            }
        });

        btnVerEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verEventos();
            }
        });

        btnPartidasPry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verPartidas();

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

                nuevoCliente();
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

        btnNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nuevaNota();
            }
        });

        btnVerNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verNotas();
            }
        });


    }

    private void nuevoCliente() {
        //mostrarDialogoTipoCliente();
        bundle = new Bundle();
        bundle.putBoolean(NUEVOREGISTRO, true);
        bundle.putString(ACTUAL, PROSPECTO);
        bundle.putString(ORIGEN, actualtemp);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDCliente());
    }

    private void nuevoEvento() {
        update();
        Modelo cliente = queryObject(CAMPOS_CLIENTE, idCliente);

        bundle = new Bundle();
        bundle.putSerializable(CLIENTE, cliente);
        bundle.putSerializable(PROYECTO, modelo);
        bundle.putString(SUBTITULO, actualtemp);
        bundle.putString(ORIGEN, actualtemp);
        bundle.putBoolean(NUEVOREGISTRO, true);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevoEvento());
        bundle = null;
    }

    private void verEventos() {
        update();
        Modelo cliente = queryObject(CAMPOS_CLIENTE, idCliente);

        bundle = new Bundle();
        bundle.putSerializable(CLIENTE, cliente);
        bundle.putSerializable(PROYECTO, modelo);
        bundle.putString(SUBTITULO, actualtemp);
        bundle.putString(ORIGEN, actualtemp);
        bundle.putSerializable(LISTA, new ListaModelo(CAMPOS_EVENTO, EVENTO_PROYECTOREL, id, null, IGUAL, null));
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
        bundle = null;
    }

    private void verNotas() {
        enviarBundle();
        bundle.putString(IDREL, modelo.getString(PROYECTO_ID_PROYECTO));
        bundle.putString(SUBTITULO, modelo.getString(PROYECTO_NOMBRE));
        bundle.putString(ORIGEN, PROYECTO);
        bundle.putString(ACTUAL, NOTA);
        bundle.putSerializable(LISTA, null);
        bundle.putSerializable(MODELO, null);
        bundle.putString(CAMPO_ID, null);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
    }

    private void nuevaNota() {
        bundle = new Bundle();
        bundle.putString(IDREL, modelo.getString(PROYECTO_ID_PROYECTO));
        bundle.putString(SUBTITULO, modelo.getString(PROYECTO_NOMBRE));
        bundle.putString(ORIGEN, PROYECTO);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevaNota());
    }

    private void verPartidas() {
        update();
        bundle.putSerializable(PROYECTO, modelo);
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

            switch (actual) {

                case GARANTIA:
                    Toast.makeText(getContext(), PROYHISTORICO, Toast.LENGTH_SHORT).show();
                    actual = HISTORICO;
                    activityBase.toolbar.setTitle(R.string.historico);
                    actualtemp = PROYECTO;
                    selector();
                    break;

                case COBROS:
                    Toast.makeText(getContext(), PROYGARANTIA, Toast.LENGTH_SHORT).show();
                    actual = GARANTIA;
                    activityBase.toolbar.setTitle(R.string.garantia);
                    actualtemp = PROYECTO;
                    selector();
                    break;

                case PRESUPUESTO:
                    Toast.makeText(getContext(),PROYECTOS, Toast.LENGTH_SHORT).show();
                    actual = PROYECTO;
                    actualtemp = actual;
                    activityBase.toolbar.setTitle(R.string.proyectos);
                    selector();
                    break;
                case PROYECTO:
                    Toast.makeText(getContext(),PROYCOBROS, Toast.LENGTH_SHORT).show();
                    actual = COBROS;
                    actualtemp = PROYECTO;
                    activityBase.toolbar.setTitle(R.string.cobros);
                    selector();
                    break;
            }
    }


    @Override
    protected void setOnRightSwipe() {

        switch (actual) {

            case PROYECTO:
                Toast.makeText(getContext(), PRESUPUESTOS, Toast.LENGTH_SHORT).show();
                actual = PRESUPUESTO;
                activityBase.toolbar.setTitle(R.string.presupuestos);
                actualtemp = actual;
                selector();
                break;

            case COBROS:
                Toast.makeText(getContext(),PROYECTOS, Toast.LENGTH_SHORT).show();
                actual = PROYECTO;
                actualtemp = actual;
                activityBase.toolbar.setTitle(R.string.proyectos);
                selector();
                break;
            case GARANTIA:
                Toast.makeText(getContext(),PROYCOBROS, Toast.LENGTH_SHORT).show();
                actual = COBROS;
                actualtemp = PROYECTO;
                activityBase.toolbar.setTitle(R.string.cobros);
                selector();
                break;

            case HISTORICO:
                Toast.makeText(getContext(), PROYGARANTIA, Toast.LENGTH_SHORT).show();
                actual = GARANTIA;
                actualtemp = PROYECTO;
                activityBase.toolbar.setTitle(R.string.garantia);
                selector();
                break;
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

        imagen = (ImagenLayout) ctrl(R.id.imudpry);
        imagenTipoClienteProyecto = (ScalableImageView) ctrl(R.id.imgbtntipocliudpry);
        btnimgEstadoPry = (ScalableImageView) ctrl(R.id.imgbtnestudpry);
        nombrePry = (EditMaterial) ctrl(R.id.etnomudpry, PROYECTO_NOMBRE);
        descripcionPry = (EditMaterial) ctrl(R.id.etdescudpry, PROYECTO_DESCRIPCION);
        spClienteProyecto = (AutoCompleteTextView) ctrl(R.id.sptipocliudpry);
        spEstadoProyecto = (Spinner) ctrl(R.id.spestudpry);
        estadoProyecto = (EditMaterial) ctrl(R.id.tvestudproy);
        fechaEntradaPry = (EditMaterial) ctrl(R.id.proyecto_ud_tv_fecha_entrada);
        fechaEntregaPresup = (EditMaterial) ctrl(R.id.tvfentpresuppry);
        fechaCalculadaPry = (EditMaterial) ctrl(R.id.tvfcalcudpry);
        fechaAcordadaPry = (EditMaterial) ctrl(R.id.tvfacorudpry);
        fechaFinalPry = (EditMaterial) ctrl(R.id.tvffinudpry);
        totalPartidasPry = (EditMaterial) ctrl(R.id.tvtotpartudpry);
        pvpPartidas = (EditMaterial) ctrl(R.id.tvpreciopartidasudpry);
        importeCalculadoPry = (EditMaterial) ctrl(R.id.tvimpcaludpry);
        importeFinalPry = (EditMaterial) ctrl(R.id.etimpfinudpry);
        btnEvento = (ImageButton) ctrl(R.id.btneventoudpry);
        btnPartidasPry = (Button) ctrl(R.id.btnpartudpry);
        btnActualizar = (Button) ctrl(R.id.btnactualizar);
        btnActualizar2 = (Button) ctrl(R.id.btnactualizar2);
        btnfechaacord = (ImageView) ctrl(R.id.btnfechaacord);
        btnfechaentrega = (ImageView) ctrl(R.id.btnfechaentrega);
        btnVerPdf = (ImageButton) ctrl(R.id.btnverpdfudpry);
        btnenviarPdf = (ImageButton) ctrl(R.id.btnenviarpdfudpry);
        btncompartirPdf = (ImageButton) ctrl(R.id.btncompartirpdfudpry);
        btnpresupuestos = (Button) ctrl(R.id.btnpresuplpry);
        btnproyectos = (Button) ctrl(R.id.btnproyectoslpry);
        btncobros = (Button) ctrl(R.id.btnproycobroslpry);
        btnhistorico = (Button) ctrl(R.id.btnhistoricopry);
        btngarantias = (Button) ctrl(R.id.btngarantiapry);
        btnVerEventos = (ImageButton) ctrl(R.id.btnvereventosudpry);
        btnNota = (ImageButton) ctrl(R.id.btn_crearnota_proy);
        btnVerNotas = (ImageButton) ctrl(R.id.btn_vernotas_proy);

        estadoProyecto.setActivo(false);

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

        //if (id!=null){ modelo = consulta.queryObject(campos,id);}

        spEstadoProyecto.setVisibility(View.GONE);

        btndelete.setVisibility(View.VISIBLE);
        btnEvento.setVisibility(View.VISIBLE);
        btnPartidasPry.setVisibility(View.VISIBLE);
        estadoProyecto.setVisibility(View.VISIBLE);
        fechaAcordadaPry.setVisibility(View.VISIBLE);
        fechaCalculadaPry.setVisibility(View.VISIBLE);
        btnfechaacord.setVisibility(View.VISIBLE);
        btnfechaentrega.setVisibility(View.VISIBLE);
        btnActualizar.setVisibility(View.VISIBLE);
        btnActualizar2.setVisibility(View.VISIBLE);
        pvpPartidas.setVisibility(View.VISIBLE);
        fechaEntregaPresup.setVisibility(View.VISIBLE);
        fechaFinalPry.setVisibility(View.VISIBLE);
        totalPartidasPry.setVisibility(View.VISIBLE);
        importeFinalPry.setVisibility(View.VISIBLE);
        importeCalculadoPry.setVisibility(View.VISIBLE);
        btnimgEstadoPry.setVisibility(View.VISIBLE);
        fechaEntradaPry.setVisibility(View.VISIBLE);
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

        /*
        if (actualtemp.equals(PRESUPUESTO)|| actualtemp.equals(PROYECTO) ) {
            frLista.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }



        if (modelo.getString(PROYECTO_RUTAFOTO)!=null){
            path = modelo.getString(PROYECTO_RUTAFOTO);
            setImagenUri(contexto,path);
        }

         */

        nombrePry.setText(modelo.getString(PROYECTO_NOMBRE));
        descripcionPry.setText(modelo.getString(PROYECTO_DESCRIPCION));
        idCliente = modelo.getString(PROYECTO_ID_CLIENTE);
        peso = modelo.getInt(PROYECTO_CLIENTE_PESOTIPOCLI);//cliente.getInt(CLIENTE_PESOTIPOCLI);
        if (peso > 6) {
            imagenTipoClienteProyecto.setImageResource(R.drawable.clientev);
        } else if (peso > 3) {
            imagenTipoClienteProyecto.setImageResource(R.drawable.clientea);
        } else if (peso > 0) {
            imagenTipoClienteProyecto.setImageResource(R.drawable.clienter);
        } else {
            imagenTipoClienteProyecto.setImageResource(R.drawable.cliente);
        }

        idEstado = modelo.getString(PROYECTO_ID_ESTADO);
        id = modelo.getString(PROYECTO_ID_PROYECTO);
        fechaEntradaPry.setText(JavaUtil.getDateTime(modelo.getLong(PROYECTO_FECHAENTRADA)));
        if (modelo.getLong(PROYECTO_FECHAENTREGAPRESUP) == 0) {
            fechaEntregaPresup.setText
                    (getResources().getString(R.string.establecer_fecha_entrega_presup));
        } else if (modelo.getLong(PROYECTO_FECHAENTREGAPRESUP) > 0) {
            fechaEntregaPresup.setText(JavaUtil.getDate(modelo.getLong(PROYECTO_FECHAENTREGAPRESUP)));
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
            btnActualizar.setVisibility(View.GONE);
            btnActualizar2.setVisibility(View.GONE);
            pvpPartidas.setVisibility(View.GONE);
            fechaEntregaPresup.setVisibility(View.GONE);
            totalPartidasPry.setVisibility(View.GONE);

        } else {

            fechaAcordadaPry.setVisibility(View.VISIBLE);
            fechaCalculadaPry.setVisibility(View.VISIBLE);
            btnfechaacord.setVisibility(View.VISIBLE);
            btnActualizar.setVisibility(View.VISIBLE);
            pvpPartidas.setVisibility(View.VISIBLE);
            btnfechaentrega.setVisibility(View.GONE);
            fechaEntregaPresup.setVisibility(View.GONE);
            totalPartidasPry.setVisibility(View.VISIBLE);

            if (modelo.getLong(PROYECTO_FECHAENTREGAACORDADA) == 0) {

                fechaAcordadaPry.setText(getString(R.string.sin_asignar));
                btnActualizar.setVisibility(View.GONE);


            } else {

                fechaAcordada = modelo.getLong(PROYECTO_FECHAENTREGAACORDADA);
                fechaAcordadaPry.setText(JavaUtil.getDate(fechaAcordada));
                btnActualizar.setVisibility(View.VISIBLE);
                if (modelo.getInt(PROYECTO_TIPOESTADO) > 2) {

                    btnfechaentrega.setVisibility(View.VISIBLE);
                    fechaEntregaPresup.setVisibility(View.VISIBLE);
                }

            }

            fechaCalculada = modelo.getLong(PROYECTO_FECHAENTREGACALCULADA);
            fechaCalculadaPry.setText(JavaUtil.getDate(fechaCalculada));

        }


        if (modelo.getLong(PROYECTO_FECHAFINAL) == 0) {
            fechaFinalPry.setVisibility(View.GONE);
        } else {
            fechaFinalPry.setVisibility(View.VISIBLE);
            fechaFinalPry.setText(JavaUtil.getDate(modelo.getLong(PROYECTO_FECHAFINAL)));
        }


        if (preciototal == 0) {
            importeCalculadoPry.setVisibility(View.GONE);
        } else {
            importeCalculadoPry.setVisibility(View.VISIBLE);
            importeCalculadoPry.setText(JavaUtil.formatoMonedaLocal
                    (preciototal));
        }

        if (modelo.getInt(PROYECTO_TIPOESTADO) < 4) {
            importeFinalPry.setVisibility(View.GONE);
        } else {
            importeFinalPry.setVisibility(View.VISIBLE);
            importeFinalPry.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(PROYECTO_IMPORTEFINAL)));
        }


        estadoProyecto.setText(modelo.getString(PROYECTO_DESCRIPCION_ESTADO));

            long retraso = modelo.getLong(PROYECTO_RETRASO);
        if (retraso > 3 * Interactor.DIASLONG) {
                btnimgEstadoPry.setImageResource(R.drawable.alert_box_r);
        } else if (retraso > Interactor.DIASLONG) {
                btnimgEstadoPry.setImageResource(R.drawable.alert_box_a);
            } else {
                btnimgEstadoPry.setImageResource(R.drawable.alert_box_v);
            }

        if (modelo.getInt(PROYECTO_TIPOESTADO)>=TPRESUPPENDENTREGA){

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
            Modelo cliente = queryObject(CAMPOS_CLIENTE, idCliente);
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
    protected void setOnClickNuevo() {
        super.setOnClickNuevo();
        idCliente = null;
    }

    @Override
    protected void setNuevo() {

        if (actual.equals(PRESUPUESTO)) {
            tituloNuevo = R.string.nuevo_presupuesto;
        }else{
            tituloNuevo = R.string.nuevo_proyecto;
        }

        imagenTipoClienteProyecto.setImageResource(R.drawable.ic_person_add_black_24dp);

        activityBase.toolbar.setSubtitle(tituloNuevo);

        allGone();
        visible(imagenTipoClienteProyecto);
        visible(spClienteProyecto);
        gone(imagen);

        listaObjetosEstados();

        ArrayAdapter<String> adaptadorEstado = new ArrayAdapter<>
                (contexto,android.R.layout.simple_spinner_item,listaEstados);

        spEstadoProyecto.setAdapter(adaptadorEstado);

        if (actual.equals(PRESUPUESTO)){

            spEstadoProyecto.setSelection(TNUEVOPRESUP);
            idEstado = objEstados.get(TNUEVOPRESUP-1).getString(ESTADO_ID_ESTADO);
        }else {

            spEstadoProyecto.setSelection(TPROYECTEJECUCION);
            idEstado = objEstados.get(TPROYECTEJECUCION-1).getString(ESTADO_ID_ESTADO);
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

        setAdaptadorClientes(spClienteProyecto);
        spClienteProyecto.setText("");

        System.out.println("idCliente = " + idCliente);

        if (idCliente!=null) {
            Modelo cliente = queryObject(CAMPOS_CLIENTE, idCliente);
            nombreCliente = cliente.getString(CLIENTE_NOMBRE);
            spClienteProyecto.setText(nombreCliente);
            nombrePry.setVisibility(View.VISIBLE);
            descripcionPry.setVisibility(View.VISIBLE);
            spEstadoProyecto.setVisibility(View.VISIBLE);
            btnsave.setVisibility(View.VISIBLE);
            peso = cliente.getInt(CLIENTE_PESOTIPOCLI);
            if (peso>6){imagenTipoClienteProyecto.setImageResource(R.drawable.clientev);}
            else if (peso>3){imagenTipoClienteProyecto.setImageResource(R.drawable.clientea);}
            else if (peso>0){imagenTipoClienteProyecto.setImageResource(R.drawable.clienter);}
            else {imagenTipoClienteProyecto.setImageResource(R.drawable.cliente);}

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


    private void calculoTotales() {

        ArrayList<Modelo> listaPartidas = queryListDetalle(CAMPOS_PARTIDA, id,TABLA_PROYECTO);

        int x = 0;
        for (int i = 0; i < listaPartidas.size(); i++) {

            int completada = listaPartidas.get(i).getInt(PARTIDA_COMPLETADA);

            totcompletada += completada;

            x = i+1;
        }
        totcompletada = (int) (Math.round(((double) totcompletada) / (double) x));

        totPartidas = modelo.getDouble(PROYECTO_TIEMPO);
        precioPartidas = totPartidas * Interactor.hora;
        preciototal = modelo.getDouble(PROYECTO_IMPORTEPRESUPUESTO);

        Log.d(TAG,"calculosTotales");

    }

    @Override
    protected boolean update() {

        new Interactor.Calculos.Tareafechas().execute();
        if (modelo!=null) {
            fechaCalculada = modelo.getLong(PROYECTO_FECHAENTREGACALCULADA);
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

        new Interactor.Calculos.Tareafechas().execute();

        return super.delete();
    }

    private void comprobarEstado() {

        Modelo proyecto = queryObject(CAMPOS_PROYECTO, id);


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

        ArrayList<Modelo> listaEstados = queryList(CAMPOS_ESTADO,null, null);

        for (Modelo estado : listaEstados) {

            if (estado.getInt(ESTADO_TIPOESTADO)==tipoEstado){

                    return estado.getString(ESTADO_ID_ESTADO);
            }

        }
        return null;
    }

    private String getIdEstado(String descTipoEstado){

        ArrayList<Modelo> listaEstados = queryList(CAMPOS_ESTADO,null, null);

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
        String idProyHistorico = getIdEstado(TPROYECTHISTORICO);

        if (idEstado.equals(idNuevoPresup)) {

            estadoProyecto.setText(PRESUPPENDENTREGA);
            idEstado = idPresupPendEntrega;

            crearEventoPresupuesto();

        } else if (idEstado.equals(idPresupPendEntrega)) {

            estadoProyecto.setText(PRESUPESPERA);
            idEstado = idPresupEnEspera;
            //showDatePickerDialogEntrega();
            btnfechaentrega.setVisibility(View.VISIBLE);
            fechaEntregaPresup.setVisibility(View.VISIBLE);

        } else if (idEstado.equals(idPresupEnEspera) && fechaEntregaP >0) {

            estadoProyecto.setText(PRESUPACEPTADO);
            idEstado = idPresupAceptado;
            actualizarVinculoPartidaBase();

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

        } else if (idEstado.equals(idProyCobrado) && !enGarantia) {

            estadoProyecto.setText(PROYECTHISTORICO);
            idEstado = idProyHistorico;

        }

        valores = new ContentValues();
        putDato(valores,CAMPOS_PROYECTO,PROYECTO_ID_ESTADO,idEstado);
        updateRegistro(TABLA_PROYECTO, id,valores);
        CRUDutil.actualizarRegistro(modelo,valores);

        modificarEstadoPartidas(id,idEstado);

        comprobarEstado();

        new Interactor.Calculos.TareaTipoCliente().execute(idCliente);

        Log.d(TAG,"modificarEstado");
    }

    private void actualizarVinculoPartidaBase() {


        ListaModelo listaPartidas = new ListaModelo(CAMPOS_PARTIDA,id,tabla,null,null);
        int res = 0;

        for (Modelo partida : listaPartidas.getLista()) {

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
            Modelo cliente = queryObject(CAMPOS_CLIENTE, idCliente);
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
            putDato(valores, CAMPOS_EVENTO, EVENTO_RUTAFOTO, modelo.getString(PROYECTO_RUTAFOTO));
            putDato(valores, CAMPOS_EVENTO, EVENTO_PROYECTOREL, id);
            putDato(valores, CAMPOS_EVENTO, EVENTO_CLIENTEREL, idCliente);
            putDato(valores, CAMPOS_EVENTO, EVENTO_NOMPROYECTOREL, modelo.getString(PROYECTO_NOMBRE));
            putDato(valores, CAMPOS_EVENTO, EVENTO_NOMCLIENTEREL, nombreCliente);
            putDato(valores, CAMPOS_EVENTO, EVENTO_AVISO, 50 * MINUTOSLONG);

            insertRegistro(TABLA_EVENTO, valores);
    }

    private static String generarPDF(String id) {

        PresupuestoPDF presupuestoPDF = new PresupuestoPDF();
        presupuestoPDF.setNombreArchivo(id);
        Modelo modelo = CRUDutil.setModelo(CAMPOS_PROYECTO,id);
        presupuestoPDF.crearPdf(id,modelo.getString(PROYECTO_RUTAFOTO));
        ContentValues valores = new ContentValues();
        putDato(valores,CAMPOS_PROYECTO,PROYECTO_RUTAPDF,presupuestoPDF.getRutaArchivo());
        System.out.println("presupuestoPDF ruta = " + presupuestoPDF.getRutaArchivo());
        System.out.println("modelo = " + modelo);
        System.out.println("valores = " + valores);
        int res = ConsultaBD.updateRegistro(modelo.getNombreTabla(),id,valores);
        System.out.println("res = " + res);
        System.out.println("modeloRutapdf = " + modelo.getString(PROYECTO_RUTAPDF));

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

        ArrayList<Modelo> listaEstados = queryList(CAMPOS_ESTADO,null, null);

        for (Modelo estado : listaEstados) {

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
        setDato(PROYECTO_ID_CLIENTE, JavaUtil.noNuloString(idCliente));
        setDato(PROYECTO_ID_ESTADO, JavaUtil.noNuloString(idEstado));
        setDato(PROYECTO_FECHAENTREGAACORDADA,fechaAcordada);
        setDato(PROYECTO_FECHAENTREGAACORDADAF, JavaUtil.getDate(fechaAcordada));

        System.out.println("fechaCalculada = " + fechaCalculada);
        System.out.println("fechaAcordada = " + fechaAcordada);
        long retraso = 0;

        if (modelo!=null) {
            long fechafinal = modelo.getLong(PROYECTO_FECHAFINAL);
            long fechaentrada = modelo.getLong(PROYECTO_FECHAENTRADA);
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

        if (modelo != null && id != null && Interactor.getTipoEstado(modelo.getString(PROYECTO_ID_ESTADO)) >= TPRESUPPENDENTREGA) {
            new TareaGenerarPdf().execute(id);
            System.out.println("Generar pdf");
        }

    }


    @Override
    protected void setcambioFragment() {

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


    private void setAdaptadorClientes(final AutoCompleteTextView autoCompleteTextView) {

        listaClientes = queryList(CAMPOS_CLIENTE);

        autoCompleteTextView.setAdapter(new ListaAdaptadorFiltro(getContext(),
                R.layout.item_list_cliente, listaClientes) {

            @Override
            public void onEntrada(Object entrada, View view) {

                ImagenLayout imgcli = view.findViewById(R.id.imglcliente);
                ImagenLayout imgcliPeso = view.findViewById(R.id.imglclientepeso);
                TextView nombreCli = view.findViewById(R.id.tvnomclilcliente);
                TextView contactoCli = view.findViewById(R.id.tvcontacclilcliente);
                TextView telefonoCli = view.findViewById(R.id.tvtelclilcliente);
                TextView emailCli = view.findViewById(R.id.tvemailclilcliente);
                TextView dirCli = view.findViewById(R.id.tvdirclilcliente);

                dirCli.setText(((Modelo) entrada).getString(CLIENTE_DIRECCION));

                int peso = ((Modelo) entrada).getInt
                        (CLIENTE_PESOTIPOCLI);

                if (peso > 6) {
                    imgcliPeso.setImageResource(R.drawable.clientev);
                } else if (peso > 3) {
                    imgcliPeso.setImageResource(R.drawable.clientea);
                } else if (peso > 0) {
                    imgcliPeso.setImageResource(R.drawable.clienter);
                } else {
                    imgcliPeso.setImageResource(R.drawable.cliente);
                }

                nombreCli.setText(((Modelo) entrada).getString(CLIENTE_NOMBRE));
                contactoCli.setText(((Modelo) entrada).getString(CLIENTE_CONTACTO));
                telefonoCli.setText(((Modelo) entrada).getString(CLIENTE_TELEFONO));
                emailCli.setText(((Modelo) entrada).getString(CLIENTE_EMAIL));
                imgcli.setImageUriPerfil(activityBase, ((Modelo) entrada).getString(CLIENTE_RUTAFOTO));

            }

            @Override
            public List onFilter(ArrayList entradas, CharSequence constraint) {

                List suggestion = new ArrayList();

                for (Object item : entradas) {

                    for (int i = 2; i < CAMPOS_CLIENTE.length; i += 3) {


                        if (((Modelo) item).getString(CAMPOS_CLIENTE[i]) != null && !((Modelo) item).getString(CAMPOS_CLIENTE[i]).equals("")) {
                            if (((Modelo) item).getString(CAMPOS_CLIENTE[i]).toLowerCase().contains(constraint.toString().toLowerCase())) {

                                suggestion.add(item);
                                break;
                            }
                        }
                    }

                }

                return null;
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
                    bundle.putString(ACTUAL,CLIENTE);
                    bundle.putString(ORIGEN, actualtemp);
                    icFragmentos.enviarBundleAFragment(bundle,new FragmentCRUDCliente());

                }else if (opciones[which].equals("Nuevo prospecto")){

                    bundle = new Bundle();
                    bundle.putBoolean(NUEVOREGISTRO,true);
                    bundle.putString(ACTUAL,PROSPECTO);
                    bundle.putString(ORIGEN, actualtemp);
                    icFragmentos.enviarBundleAFragment(bundle,new FragmentCRUDCliente());

                }else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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
                        activityBase.toolbar.setTitle(R.string.presupuestos);
                        actualtemp = actual;
                        selector();

                    } else if (grabarVoz.equals("proyectos")) {

                        Toast.makeText(getContext(), PROYECTOS, Toast.LENGTH_SHORT).show();
                        actual = PROYECTO;
                        activityBase.toolbar.setTitle(R.string.proyectos);
                        actualtemp = actual;
                        selector();

                    } else if (grabarVoz.equals("cobros")) {

                        Toast.makeText(getContext(), PROYCOBROS, Toast.LENGTH_SHORT).show();
                        actual = COBROS;
                        actualtemp = PROYECTO;
                        activityBase.toolbar.setTitle(R.string.cobros);
                        selector();

                    } else if (grabarVoz.equals("historico")) {

                        Toast.makeText(getContext(), PROYHISTORICO, Toast.LENGTH_SHORT).show();
                        actual = HISTORICO;
                        activityBase.toolbar.setTitle(R.string.historico);
                        actualtemp = PROYECTO;
                        selector();

                    } else if (grabarVoz.equals("garantias")) {

                    } else if (grabarVoz.equals("nuevo evento")) {

                        nuevoEvento();
                    } else if (grabarVoz.equals("ver eventos")) {

                        verEventos();
                    } else if (grabarVoz.equals("nueva nota")) {

                        nuevaNota();
                    } else if (grabarVoz.equals("ver notas")) {

                        verNotas();
                    } else if (grabarVoz.equals("nuevo cliente") || grabarVoz.equals("nuevo prospecto")) {

                        nuevoCliente();
                    }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

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
                        }
                        String selectedDate = JavaUtil.getDate(fechaAcordada);
                        fechaAcordadaPry.setText(selectedDate);
                        btnActualizar.setVisibility(View.VISIBLE);
                        valores = new ContentValues();
                        setDato(PROYECTO_FECHAENTREGAACORDADA,fechaAcordada);
                        setDato(PROYECTO_FECHAENTREGAACORDADAF,selectedDate);
                        updateRegistro(tabla,id,valores);

                        new Interactor.Calculos.Tareafechas().execute();

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
                        updateRegistro(tabla,id,valores);

                        new Interactor.Calculos.Tareafechas().execute();

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
                MediaUtil imagenUtil =new MediaUtil(AppActivity.getAppContext());
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

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
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
                mediaUtil = new MediaUtil(contexto);
                mediaUtil.setImageUriCircle(entrada.get(posicion).getString(PROYECTO_RUTAFOTO),imagen);
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






