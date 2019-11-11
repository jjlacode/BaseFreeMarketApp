package com.codevsolution.base.crud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.animation.OneFrameLayout;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public abstract class FragmentCRUD extends FragmentCUD {


    protected int layoutItem;
    protected RecyclerView rv;
    protected AutoCompleteTextView auto;
    protected ListaModeloSQL lista;
    protected boolean maestroDetalleSeparados;
    protected ArrayList<ModeloSQL> listafiltrada;
    protected ImageView buscar;
    protected ImageView renovar;
    protected ImageView inicio;
    protected ImageView lupa;
    protected ImageView voz;
    protected ListaAdaptadorFiltroModelo adaptadorFiltroRV;
    protected RVAdapter adaptadorRV;
    protected SwipeRefreshLayout refreshLayout;
    private OneFrameLayout frameAnimation;
    private String stemp = "";
    private int posicion;

    public FragmentCRUD() {
    }

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);
        Log.d(TAG, getMetodo());

        //frLista = view.findViewById(R.id.layout_rv);
        frLista = view.findViewById(getId("layout_rv"));

        //viewRV = inflater.inflate(R.layout.rvlayout, container, false);
        viewRV = inflater.inflate(getLayout("rvlayout"), container, false);
        if (viewRV.getParent() != null) {
            ((ViewGroup) viewRV.getParent()).removeView(viewRV); // <- fix
        }
        frLista.addView(viewRV);

        rv = view.findViewById(getId("rv"));
        refreshLayout = view.findViewById(getId("swipeRefresh"));
        auto = view.findViewById(getId("auto"));
        buscar = view.findViewById(getId("imgbuscar"));
        renovar = view.findViewById(getId("imgrenovar"));
        inicio = view.findViewById(getId("imginicio"));
        lupa = view.findViewById(getId("imgsearch"));
        voz = view.findViewById(getId("imgvoz"));
        frameAnimation = view.findViewById(getId("frameanimation"));

        frameAnimation.setAncho((int) (ancho * densidad));

        refreshLayout.setColorSchemeResources(
                color("s1"),
                color("s2"),
                color("s3"),
                color("s4")
        );

        if (land) {
            frCuerpo.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            frCuerpo.setOrientation(LinearLayout.VERTICAL);
        }

    }

    @Override
    protected void setLayout() {
        layoutPie = getIdLayout("btn_sdb");
    }

    protected void selector() {

        Log.d(TAG, getMetodo());

        subTitulo = getString(tituloPlural);
        activityBase.toolbar.setSubtitle(subTitulo);

        maestroDetalle();

        if (nuevo) {

            if (tablaCab == null) {
                id = null;
            }
            modeloSQL = null;
            secuencia = 0;
            if (tituloNuevo > 0) {
                subTitulo = getString(tituloNuevo);
                activityBase.toolbar.setSubtitle(subTitulo);
            }
            vaciarControles();
            path = null;
            setImagen();
            setNuevo();
            if (bundle != null) {
                bundle.putBoolean(NUEVOREGISTRO, false);
            }
        } else if (modeloSQL != null) {

            id = modeloSQL.getString(campoID);
            if (tablaCab != null) {
                secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
            }
            datos();

        } else if (nnn(id) && (secuencia > 0 || tablaCab == null)) {

            if (tablaCab != null) {
                modeloSQL = CRUDutil.updateModelo(campos, id, secuencia);
            } else {
                modeloSQL = CRUDutil.updateModelo(campos, id);
            }

            datos();

        } else {

            back = false;

        }

        enviarAct();

        acciones();

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void acciones() {
        super.acciones();
        Log.d(TAG, getMetodo());

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        if (listab == null) {

                            setLista();

                        }
                        setRv();
                        refreshLayout.setRefreshing(false);
                    }
                }
        );


        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (adaptadorFiltroRV.getLista() != null) {
                    lista.clearAddAllLista(adaptadorFiltroRV.getLista());
                }

                setRv();
            }
        });

        lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        renovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actualizarConsultasRV();
                setRv();
                auto.setText("");
                stemp = "";
                auto.setHint(stemp);
            }
        });

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tablaCab != null) {
                    lista = CRUDutil.setListaModeloDetalle(campos, id, tablaCab);
                    setLista();
                } else {
                    lista = CRUDutil.setListaModelo(campos);
                    setLista();
                }
                setRv();
                if (subTitulo == null) {
                    activityBase.toolbar.setSubtitle(tituloPlural);
                }
                enviarAct();
            }
        });

        voz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reconocimientoVoz(RECOGNIZE_SPEECH_ACTIVITY);
            }
        });

        auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                System.out.println("Auto before textChange");


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (grabarVoz == null) {
                    System.out.println("Auto textChange");
                    System.out.println("s = " + s.toString());

                    if (id == null || secuencia == 0) {
                        auto.setDropDownWidth(0);
                    } else {
                        auto.setDropDownWidth(ancho);
                    }
                    if (!s.toString().contains(" ")) {

                        if (adaptadorFiltroRV.getLista() != null) {
                            lista.clearAddAllLista(adaptadorFiltroRV.getLista());
                            setRv();
                        }

                    } else if (!auto.getText().toString().equals("")) {
                        stemp += "+" + s.toString();
                        auto.setHint(stemp);
                        auto.setText("");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                System.out.println("Auto after textChange");


            }
        });


        frameAnimation.setOnSwipeListener(new OneFrameLayout.OnSwipeListener() {
            @Override
            public void rightSwipe() {
                setOnRightSwipe();
            }

            @Override
            public void leftSwipe() {
                setOnLeftSwipe();
            }
        });


    }


    protected void setOnRightSwipe() {
        Log.d(TAG, getMetodo());

    }

    protected void setOnLeftSwipe() {
        Log.d(TAG, getMetodo());

    }

    @Override
    protected boolean onUpdate() {
        Log.d(TAG, getMetodo());

        if (comprobarDatos() && update()) {
            selector();
            return true;
        }
        return false;
    }

    protected abstract TipoViewHolder setViewHolder(View view);

    protected void listaRV() {

        Log.d(TAG, getMetodo());

        if (listab == null) {
            if (tablaCab == null) {
                lista = CRUDutil.setListaModelo(campos);
            } else {
                lista = CRUDutil.setListaModeloDetalle(campos, id, tablaCab);
            }
            setLista();
        } else {
            lista = listab;
        }


        if (!lista.chechLista() && !maestroDetalleSeparados && !nuevo) {
            frdetalle.setVisibility(View.GONE);
        } else {
            frdetalle.setVisibility(View.VISIBLE);
        }

        setRv();
    }

    protected void setRv() {

        Log.d(TAG, getMetodo());

        RecyclerView.LayoutManager layoutManager = null;

        int columnas = (int) (rv.getWidth() / (metrics.density * 300));
        if (columnas < 1) {
            columnas = 1;
        }
        layoutManager = new GridLayoutManager(contexto, columnas);

        rv.setLayoutManager(layoutManager);
        adaptadorRV = new RVAdapter(setViewHolder(view), lista.getLista(), layoutItem);
        rv.setAdapter(adaptadorRV);

        onSetAdapter(lista.getLista());

        adaptadorRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickRV(v);
            }
        });

        adaptadorFiltroRV = setAdaptadorAuto(contexto, layoutItem, lista.getLista(), campos);

        auto.setAdapter(adaptadorFiltroRV);

        auto.setThreshold(3);

        setOnItemClickAuto();

        rv.setVisibility(View.VISIBLE);
        auto.setVisibility(View.VISIBLE);
        buscar.setVisibility(View.VISIBLE);
        renovar.setVisibility(View.VISIBLE);
        inicio.setVisibility(View.VISIBLE);
        lupa.setVisibility(View.VISIBLE);

        buscar.setVisibility(View.GONE);

        if (listab == null) {
            inicio.setVisibility(View.GONE);
        }

    }

    protected abstract ListaAdaptadorFiltroModelo setAdaptadorAuto
            (Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos);

    protected void setLista() {
        Log.d(TAG, getMetodo());

    }

    protected void maestroDetalle() {

        Log.d(TAG, getMetodo());

        if (!land && !tablet) {

            maestroDetallePort();

        } else if (land && !tablet) {

            maestroDetalleLand();

        } else if (!land) {

            maestroDetalleTabletPort();

        } else {

            maestroDetalleTabletLand();

        }

    }

    protected void maestroDetallePort() {

        Log.d(TAG, getMetodo());

        setMaestroDetallePort();
        if (maestroDetalleSeparados) {
            defectoMaestroDetalleSeparados();
        } else {
            defectoMaestroDetalleJuntos();
        }

    }

    protected void maestroDetalleLand() {

        Log.d(TAG, getMetodo());

        setMaestroDetalleLand();
        if (maestroDetalleSeparados) {
            defectoMaestroDetalleSeparados();
        } else {
            defectoMaestroDetalleJuntos();
        }

    }

    protected void maestroDetalleTabletPort() {

        Log.d(TAG, getMetodo());

        setMaestroDetalleTabletPort();
        if (maestroDetalleSeparados) {
            defectoMaestroDetalleSeparados();
        } else {
            defectoMaestroDetalleJuntos();

        }

    }

    protected void maestroDetalleTabletLand() {

        Log.d(TAG, getMetodo());

        setMaestroDetalleTabletLand();
        if (maestroDetalleSeparados) {
            defectoMaestroDetalleSeparados();
        } else {
            defectoMaestroDetalleJuntos();
        }

    }

    protected void defectoMaestroDetalleSeparados() {

        Log.d(TAG, getMetodo());


        if (nuevo) {
            if (layoutCabecera > 0 || cabecera) {
                gone(frCabecera);
            }
            gone(frLista);
            gone(rv);
            gone(refreshLayout);
            visible(frameAnimationCuerpo);
            visible(frdetalle);
            visible(frPie);
            activityBase.fabVoz.show();
            activityBase.fabNuevo.show();
            activityBase.fabNuevo.setSize(FloatingActionButton.SIZE_MINI);
            activityBase.fabVoz.setSize(FloatingActionButton.SIZE_MINI);
        } else if ((id != null && secuencia > 0) || (id != null && tablaCab == null) || (modeloSQL != null)) {
            if (layoutCabecera > 0 || cabecera) {
                gone(frCabecera);
            }
            gone(frLista);
            gone(rv);
            gone(refreshLayout);
            visible(frameAnimationCuerpo);
            visible(frdetalle);
            visible(frPie);
            activityBase.fabVoz.show();
            activityBase.fabNuevo.show();
            activityBase.fabNuevo.setSize(FloatingActionButton.SIZE_MINI);
            activityBase.fabVoz.setSize(FloatingActionButton.SIZE_MINI);

        } else {

            if (layoutCabecera > 0 || cabecera) {
                visible(frCabecera);
            }
            visible(frLista);
            visible(rv);
            visible(refreshLayout);
            gone(frameAnimationCuerpo);
            gone(frPie);
            activityBase.fabVoz.show();
            activityBase.fabNuevo.show();
            activityBase.fabNuevo.setSize(FloatingActionButton.SIZE_NORMAL);
            activityBase.fabVoz.setSize(FloatingActionButton.SIZE_NORMAL);

            if (grabarVoz == null) {
                listaRV();
                grabarVoz = null;
            }
        }

        setDefectoMaestroDetalleSeparados();

    }

    protected void setDefectoMaestroDetalleSeparados() {

        Log.d(TAG, getMetodo());

    }

    protected void setDefectoMaestroDetalleJuntos() {

        Log.d(TAG, getMetodo());

    }

    protected void defectoMaestroDetalleJuntos() {

        Log.d(TAG, getMetodo());

        visible(frLista);
        visible(rv);
        visible(refreshLayout);
        visible(frameAnimationCuerpo);
        visible(frdetalle);
        visible(frPie);
        if (layoutCabecera > 0 || cabecera) {
            visible(frCabecera);
        } else {
            gone(frCabecera);
        }
        activityBase.fabVoz.show();
        activityBase.fabNuevo.show();
        visible(btndelete);
        listaRV();

        setDefectoMaestroDetalleJuntos();
    }

    protected void setMaestroDetallePort() {
        maestroDetalleSeparados = true;
    }

    protected void setMaestroDetalleLand() {
        maestroDetalleSeparados = false;
    }

    protected void setMaestroDetalleTabletLand() {
        maestroDetalleSeparados = false;
    }

    protected void setMaestroDetalleTabletPort() {
        maestroDetalleSeparados = false;
    }


    protected void actualizarConsultasRV() {

        Log.d(TAG, getMetodo());

        if (listab == null) {
            if (tablaCab != null) {
                modeloSQL = null;
                secuencia = 0;
                lista = CRUDutil.setListaModeloDetalle(campos, id, tablaCab);
            } else {
                modeloSQL = null;
                id = null;
                lista = CRUDutil.setListaModelo(campos);
            }
            setLista();
        } else {
            lista = listab;
        }

    }

    protected void onClickRV(View v) {

        Log.d(TAG, getMetodo());

        modeloSQL = lista.getItem(rv.getChildAdapterPosition(v));

        posicion = rv.getChildAdapterPosition(v);
        setOnClickRV(modeloSQL);


    }

    protected void setOnClickRV(ModeloSQL modeloSQL) {

        id = modeloSQL.getString(campoID);

        if (tablaCab != null) {
            secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
        }

        if (id != null) {
            selector();
        }

        if (subTitulo == null) {
            activityBase.toolbar.setSubtitle(tituloPlural);
        }

    }

    @Override
    protected void setOnLeftSwipeCuerpo() {

        if (posicion < lista.sizeLista() - 1) {
            posicion++;
            setOnClickRV(lista.getItem(posicion));
        }
    }

    @Override
    protected void setOnRigthSwipeCuerpo() {

        if (posicion > 0) {
            posicion--;
            setOnClickRV(lista.getItem(posicion));
        }
    }

    protected void onSetAdapter(ArrayList<ModeloSQL> lista) {

        Log.d(TAG, getMetodo());

        if (!maestroDetalleSeparados && lista != null && lista.size() > 0 && id == null && !nuevo) {

            modeloSQL = lista.get(0);

            if (tablaCab != null) {
                secuencia = lista.get(0).getInt(CAMPO_SECUENCIA);
            } else {
                id = lista.get(0).getString(campoID);
            }

        }

    }

    protected void setOnItemClickAuto() {

        Log.d(TAG, getMetodo());

        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {

                auto.setText("");
                modeloSQL = adaptadorFiltroRV.getItem(position);
                if (modeloSQL != null) {
                    id = modeloSQL.getString(campoID);
                    if (tablaCab != null) {
                        secuencia = modeloSQL.getInt(CAMPO_SECUENCIA);
                    }
                }

                selector();

            }
        });

    }

    protected boolean onDelete() {
        Log.d(TAG, getMetodo());

        if ((id == null && tablaCab == null) || (tablaCab != null && secuencia == 0)) {
            selector();
        } else {
            if (delete()) {

                modeloSQL = null;
                if (tablaCab == null) {
                    id = null;
                }
                secuencia = 0;
                selector();
                cambiarFragment();

                return true;
            }
        }

        return false;
    }


    @Override
    protected boolean onBack() {

        back = true;

        System.out.println("modeloSQL.getLong(campoTimeStamp) = " + modeloSQL.getLong(CAMPO_TIMESTAMP));
        System.out.println("modeloSQL.getLong(campoCreate) = " + modeloSQL.getLong(CAMPO_CREATEREG));
        if (modeloSQL.getLong(CAMPO_CREATEREG) == modeloSQL.getLong(CAMPO_TIMESTAMP)) {
            delete();
        }
        cambiarFragment();
        selector();

        subTitulo = getString(tituloPlural);
        activityBase.toolbar.setSubtitle(subTitulo);
        return true;
    }

    @Override
    protected boolean setBack() {
        return back;
    }

    protected void cambiarFragment() {

        Log.d(TAG, getMetodo());

        modeloSQL = null;
        if (tablaCab == null) {
            id = null;
        }
        secuencia = 0;
        nuevo = false;
        enviarBundle();
        setcambioFragment();
        listaRV();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, getMetodo());

        System.out.println("requestCode = " + requestCode);

        if (resultCode == RESULT_OK) {

            if (requestCode == RECOGNIZE_SPEECH_ACTIVITY) {
                ArrayList<String> speech = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                grabarVoz = speech.get(0);
                String buscar = null;
                if (grabarVoz.length() >= 7) {
                    buscar = grabarVoz.substring(0, 7).toLowerCase();
                }
                if (grabarVoz.equals("renovar lista")) {
                    actualizarConsultasRV();
                    setRv();
                    //auto.setText("");
                } else if (grabarVoz.equals("limpiar lista")) {
                    actualizarConsultasRV();
                    setRv();
                    //auto.setText("");
                } else if (grabarVoz.equals("lista completa")) {
                    if (tablaCab != null) {
                        lista = CRUDutil.setListaModeloDetalle(campos, id, tablaCab);
                        setLista();
                    } else {
                        lista = CRUDutil.setListaModelo(campos);
                        setLista();
                    }
                    setRv();
                    if (subTitulo == null) {
                        activityBase.toolbar.setSubtitle(tituloPlural);
                    }
                    //auto.setText("");
                    enviarAct();
                } else if (buscar != null && buscar.equals("buscar ")) {
                    grabarVoz = grabarVoz.substring(7);
                    System.out.println("grabarVoz sub= " + grabarVoz);
                    ListaModeloSQL suggestion = new ListaModeloSQL();
                    if (grabarVoz != null) {

                        for (ModeloSQL item : lista.getLista()) {

                            for (int i = 2; i < campos.length; i += 3) {

                                if (item.getString(campos[i]) != null && !item.getString(campos[i]).equals("")) {
                                    if (item.getString(campos[i]).toLowerCase().contains(grabarVoz.toLowerCase())) {

                                        suggestion.addModelo(item);
                                    }
                                }
                            }

                        }

                        System.out.println("suggestion = " + suggestion.sizeLista());
                        listab = new ListaModeloSQL(suggestion);
                        System.out.println("listab = " + listab.sizeLista());
                        actualizarConsultasRV();
                        System.out.println("lista = " + lista.sizeLista());
                        setRv();
                        System.out.println("lista = " + lista.sizeLista());
                        auto.setText(grabarVoz);
                        auto.setSelection(grabarVoz.length());
                        auto.setDropDownWidth(0);
                        if (id != null) {
                            auto.setDropDownWidth(ancho);
                        }
                        //grabarVoz=null;
                    }
                }
            }
        }
    }

}
