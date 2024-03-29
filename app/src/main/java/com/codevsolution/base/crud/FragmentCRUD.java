package com.codevsolution.base.crud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.animation.OneFrameLayout;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.TimeDateUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class FragmentCRUD extends FragmentCUD {


    protected int layoutItem;
    protected RecyclerView rv;
    protected AutoCompleteTextView auto;
    protected ListaModeloSQL lista;
    protected boolean maestroDetalleSeparados;
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
    private boolean autoborrado;
    private boolean buscaVoz;

    public FragmentCRUD() {
    }

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);
        Log.d(TAG, getMetodo());

        viewRV = addVista(Estilos.getIdLayout(contexto, "rvlayout"), frLista);

        rv = viewRV.findViewById(getId("rv"));
        refreshLayout = viewRV.findViewById(getId("swipeRefresh"));
        auto = viewRV.findViewById(getId("auto"));
        buscar = viewRV.findViewById(getId("imgbuscar"));
        renovar = viewRV.findViewById(getId("imgrenovar"));
        inicio = viewRV.findViewById(getId("imginicio"));
        lupa = viewRV.findViewById(getId("imgsearch"));
        voz = viewRV.findViewById(getId("imgvoz"));
        frameAnimation = view.findViewById(getId("frameanimation"));

        frameAnimation.setAncho((int) (ancho * densidad));

        refreshLayout.setColorSchemeResources(
                color("s1"),
                color("s2"),
                color("s3"),
                color("s4")
        );

        if (land) {
            frCuerpo.setOrientation(LinearLayoutCompat.HORIZONTAL);
            Estilos.setLayoutParams(frLista, 1);
            Estilos.setLayoutParams(frameAnimationCuerpo, 1);
        } else {
            frCuerpo.setOrientation(LinearLayoutCompat.VERTICAL);
        }

        autoborrado = true;

    }

    @Override
    protected void setLayout() {
        layoutPie = getIdLayout("btn_sdb");
    }

    protected void selector() {

        Log.d(TAG, getMetodo());
        if (crudUtil == null) {
            crudUtil = new CRUDutil((FragmentBaseCRUD) setFragment());
        }

        if (subTitulo == null) {
            if (tituloPlural > 0) {
                subTitulo = getString(tituloPlural);
            }
        }
        activityBase.toolbar.setSubtitle(subTitulo);

        maestroDetalle();

        if (nuevo) {

            if (tablaCab == null) {
                id = null;
            }
            modeloSQL = null;
            secuencia = 0;

            vaciarControles();
            path = null;
            crudUtil.setImagen(contexto);
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
                modeloSQL = crudUtil.updateModelo(campos, id, secuencia);
            } else {
                modeloSQL = crudUtil.updateModelo(campos, id);
            }

            datos();

        } else {

            back = false;
            setTitulo();
            if (tituloPlural != 0 && playOn) {
                reproducir(getString(tituloPlural));
            }

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

        if (nnn(busca) && !busca.isEmpty()) {

            if (tablaCab != null) {
                lista = crudUtil.setListaModeloDetalle(campos, id);
                setLista();
            } else {
                lista = crudUtil.setListaModelo(campos);
                setLista();
            }
            setRv();
            if (subTitulo == null) {
                activityBase.toolbar.setSubtitle(tituloPlural);
            }
            ListaModeloSQL listaSug = listaBusquedaVoz(busca);
            busca = null;
            bundle.putString(BUSCA, busca);

            if (listaSug.sizeLista() == 1) {
                modeloSQL = listaSug.getItem(0);
                for (int i = 0; i < lista.getLista().size(); i++) {
                    if (lista.getItem(i) == modeloSQL) {
                        posicion = i;
                        break;
                    }
                }
                setOnClickRV(modeloSQL);
            }
        }

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
                    lista = crudUtil.setListaModeloDetalle(campos, id);
                    setLista();
                } else {
                    lista = crudUtil.setListaModelo(campos);
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
                buscaVoz = true;
                startVoz();
            }
        });

        auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


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

            @Override
            public void afterTextChanged(Editable s) {


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

    @Override
    protected void speechProcess(String speech) {
        super.speechProcess(speech);

        if (buscaVoz) {
            if (tablaCab != null) {
                lista = crudUtil.setListaModeloDetalle(campos, id);
                setLista();
            } else {
                lista = crudUtil.setListaModelo(campos);
                setLista();
            }
            setRv();
            if (subTitulo == null) {
                activityBase.toolbar.setSubtitle(tituloPlural);
            }
            ListaModeloSQL listaSug = listaBusquedaVoz(speech);

            if (listaSug.sizeLista() == 1) {
                modeloSQL = listaSug.getItem(0);
                for (int i = 0; i < lista.getLista().size(); i++) {
                    if (lista.getItem(i) == modeloSQL) {
                        posicion = i;
                        break;
                    }
                }
                setOnClickRV(modeloSQL);
            }
            buscaVoz = false;
        }
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
                lista = crudUtil.setListaModelo(campos);
            } else {
                lista = crudUtil.setListaModeloDetalle(campos, id);
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

        if (modulo || (!land && !tablet)) {

            maestroDetallePort();

        } else if (land && !tablet && !modulo) {

            maestroDetalleLand();

        } else if (!land && !modulo) {

            maestroDetalleTabletPort();

        } else if (!modulo) {

            maestroDetalleTabletLand();

        } else {
            maestroDetallePort();
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
            if (layoutListaPie > 0 || listaPie) {
                gone(frListaPie);
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
            frPubli.setMinimumHeight(activityBase.fabVoz.getHeight());
        } else if ((id != null && secuencia > 0) || (id != null && tablaCab == null) || (modeloSQL != null)) {
            if (layoutCabecera > 0 || cabecera) {
                gone(frCabecera);
            }
            if (layoutListaPie > 0 || listaPie) {
                gone(frListaPie);
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
            frPubli.setMinimumHeight(activityBase.fabVoz.getHeight());

        } else {

            if (layoutCabecera > 0 || cabecera) {
                visible(frCabecera);
            }
            if (layoutListaPie > 0 || listaPie) {
                visible(frListaPie);
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
            frPubli.setMinimumHeight(activityBase.fabVoz.getHeight());

                listaRV();
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
        if (layoutListaPie > 0 || listaPie) {
            visible(frListaPie);
        } else {
            gone(frListaPie);
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
                lista = crudUtil.setListaModeloDetalle(campos, id);
            } else {
                modeloSQL = null;
                id = null;
                lista = crudUtil.setListaModelo(campos);
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
        nuevo = false;
        if (autoborrado && nn(modeloSQL) && modeloSQL.getLong(CAMPO_CREATEREG) >= (TimeDateUtil.ahora() - (5 * MINUTOSLONG))
                && modeloSQL.getLong(CAMPO_CREATEREG) == modeloSQL.getLong(CAMPO_TIMESTAMP)) {
            mostrarDialogDelete();
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
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, getMetodo());

        if (resultCode == RESULT_OK) {

            if (requestCode == RECOGNIZE_SPEECH_ACTIVITY) {
                ArrayList<String> speech = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                System.out.println("speech = " + speech.get(0));
                String clave = getPref(PreferenciasBase.CLAVEVOZ, "");
                if (speech != null && clave != null && (clave.equals("") || speech.get(0).contains(clave))) {

                    if (speech.get(0).contains(clave)) {
                        grabarVoz = speech.get(0).replace(clave, "").toLowerCase();
                    } else {
                        grabarVoz = speech.get(0).toLowerCase();
                    }

                    if (grabarVoz.contains("renovar lista")) {
                        actualizarConsultasRV();
                        setRv();
                        //auto.setText("");
                    } else if (grabarVoz.contains("limpiar lista")) {
                        actualizarConsultasRV();
                        setRv();
                        //auto.setText("");
                    } else if (grabarVoz.contains("lista completa")) {
                        if (tablaCab != null) {
                            lista = crudUtil.setListaModeloDetalle(campos, id);
                            setLista();
                        } else {
                            lista = crudUtil.setListaModelo(campos);
                            setLista();
                        }
                        setRv();
                        if (subTitulo == null) {
                            activityBase.toolbar.setSubtitle(tituloPlural);
                        }
                        //auto.setText("");
                        enviarAct();
                    } else if (grabarVoz.contains(Estilos.getString(contexto, "buscar"))) {
                        grabarVoz = grabarVoz.replaceFirst(Estilos.getString(contexto, "buscar"), "");
                        System.out.println("grabarVoz sub= " + grabarVoz);
                        listaBusquedaVoz(grabarVoz);
                    } else if (grabarVoz.contains(Estilos.getString(contexto, "busca"))) {
                        grabarVoz = grabarVoz.replaceFirst(Estilos.getString(contexto, "busca"), "");
                        System.out.println("grabarVoz sub= " + grabarVoz);
                        listaBusquedaVoz(grabarVoz);
                    }
                }
            }
        }
    }

 */


    protected ListaModeloSQL listaBusquedaVoz(String grabarVoz) {

        String[] results = grabarVoz.split(Pattern.quote(" "));
        String campoBusqueda = NULL;
        StringBuilder res = new StringBuilder();
        for (final EditMaterialLayout editMaterial : materialEditLayouts) {
            for (String result : results) {
                System.out.println("editMaterial hint = " + editMaterial.getHint());
                System.out.println("result = " + result);
                if (!result.isEmpty() && editMaterial.getHint().equalsIgnoreCase(result.toLowerCase())) {
                    for (int i = 1; i < results.length; i++) {
                        res.append(results[i]);
                        if (i < results.length - 1) {
                            res.append(" ");
                        }
                    }

                    for (Object o : camposEdit) {

                        if (((Map) o).get("materialEdit") == editMaterial) {

                            campoBusqueda = (String) ((Map) o).get("campoEdit");
                            break;

                        }
                    }
                }
                if (nnn(campoBusqueda)) {
                    break;
                }
            }
            if (nnn(campoBusqueda)) {
                break;
            }
        }
        if (campoBusqueda != null && campoBusqueda.equals(NULL)) {
            for (final EditMaterial editMaterial : materialEdits) {
                for (String result : results) {
                    System.out.println("editMaterial hint = " + editMaterial.getHint());
                    System.out.println("result = " + result);
                    if (!result.isEmpty() && editMaterial.getHint().equalsIgnoreCase(result.toLowerCase())) {
                        for (int i = 1; i < results.length; i++) {
                            res.append(results[i]);
                            if (i < results.length - 1) {
                                res.append(" ");
                            }
                        }

                        for (Object o : camposEdit) {

                            if (((Map) o).get("materialEdit") == editMaterial) {

                                campoBusqueda = (String) ((Map) o).get("campoEdit");
                                break;

                            }
                        }
                    }
                    if (nnn(campoBusqueda)) {
                        break;
                    }
                }
                if (nnn(campoBusqueda)) {
                    break;
                }
            }
        }
        if (nnn(campoBusqueda)) {
            listab = new ListaModeloSQL(crudUtil.listaBusqueda(grabarVoz, new String[]{campoBusqueda}));
        } else {
            listab = new ListaModeloSQL(crudUtil.listaBusqueda(grabarVoz, campos));
        }
        actualizarConsultasRV();
        setRv();
        auto.setText(grabarVoz);
        auto.setSelection(grabarVoz.length());
        auto.setDropDownWidth(0);
        if (id != null) {
            auto.setDropDownWidth(ancho);
        }
        return listab;
    }


}
