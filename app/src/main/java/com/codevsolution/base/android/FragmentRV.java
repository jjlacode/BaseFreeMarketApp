package com.codevsolution.base.android;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.codevsolution.base.adapter.ListaAdaptadorFiltro;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.animation.OneFrameLayout;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.style.Estilos;

import java.util.ArrayList;

//import com.codevsolution.freemarketsapp.logica.Interactor;

public abstract class FragmentRV extends FragmentBase {

    //protected LinearLayoutCompat frLista;
    protected View viewRV;
    protected int layoutItem;
    protected RecyclerView rv;
    protected AutoCompleteTextView auto;
    protected ArrayList listafiltrada;
    protected ImageView buscar;
    protected ImageView renovar;
    protected ImageView inicio;
    protected ImageView lupa;
    protected ImageView voz;
    protected ListaAdaptadorFiltroModelo adaptadorFiltroRV;
    protected RVAdapter adaptadorRV;
    protected SwipeRefreshLayout refreshLayout;
    protected String subTitulo;
    protected ArrayList listab;
    protected ArrayList lista;
    protected int tituloPlural;
    protected String[] campos;
    protected OneFrameLayout fragment_container;
    protected RecyclerView.LayoutManager layoutManager;
    protected boolean multiColunas;
    protected Object object;
    protected ModeloSQL modeloSQL;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        //frLista = view.findViewById(R.id.layout_rv);
/*
        viewRV = inflater.inflate(R.layout.rvlayout, container, false);
        if (viewRV.getParent() != null) {
            ((ViewGroup) viewRV.getParent()).removeView(viewRV); // <- fix
        }
        frLista.addView(viewRV);

 */
        viewRV = addVista(Estilos.getIdLayout(contexto, "rvlayout"), frLista);

        //rv = viewRV.findViewById(R.id.rv);
        rv = viewRV.findViewById(Estilos.getIdResource(contexto, "rv"));
        refreshLayout = viewRV.findViewById(Estilos.getIdResource(contexto, "swipeRefresh"));
        auto = viewRV.findViewById(Estilos.getIdResource(contexto, "auto"));
        buscar = viewRV.findViewById(Estilos.getIdResource(contexto, "imgbuscar"));
        renovar = viewRV.findViewById(Estilos.getIdResource(contexto, "imgrenovar"));
        inicio = viewRV.findViewById(Estilos.getIdResource(contexto, "imginicio"));
        lupa = viewRV.findViewById(Estilos.getIdResource(contexto, "imgsearch"));
        voz = viewRV.findViewById(Estilos.getIdResource(contexto, "imgvoz"));
        fragment_container = view.findViewById(Estilos.getIdResource(contexto, "frameanimation"));
        btnback = viewBotones.findViewById(Estilos.getIdResource(contexto, "btn_back"));
        btndelete = viewBotones.findViewById(Estilos.getIdResource(contexto, "btn_del"));
        btnsave = viewBotones.findViewById(Estilos.getIdResource(contexto, "btn_save"));

        gone(btndelete);
        gone(btnsave);

        refreshLayout.setColorSchemeResources(
                Estilos.getIdColor(contexto, "s1"),
                Estilos.getIdColor(contexto, "s2"),
                Estilos.getIdColor(contexto, "s3"),
                Estilos.getIdColor(contexto, "s4")
        );


        if (land) {
            frCuerpo.setOrientation(LinearLayoutCompat.HORIZONTAL);
        } else {
            frCuerpo.setOrientation(LinearLayoutCompat.VERTICAL);
        }
        visible(frLista);
    }

    @Override
    public void onResume() {
        super.onResume();

        selector();
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        enviarAct();
    }

    @Override
    protected void setLayoutExtra() {
        super.setLayoutExtra();

        layoutPie = Estilos.getIdLayout(contexto, "btn_sdb");
    }

    protected void selector() {

        visible(inicio);
        listaRV();

        acciones();
    }

    protected void acciones() {
        super.acciones();

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        if (listab == null) {

                            setLista();

                        }
                        rv.setVisibility(View.VISIBLE);
                        setRv();
                        refreshLayout.setRefreshing(false);
                    }
                }
        );


        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (adaptadorFiltroRV.getLista() != null) {
                    lista.clear();
                    lista.addAll(adaptadorFiltroRV.getLista());
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
            }
        });

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setRv();
                if (subTitulo == null) {
                    subTitulo = setSubtitulo();
                    icFragmentos.showSubTitle(subTitulo);
                }
            }
        });

        auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                auto.setDropDownWidth(0);
                if (adaptadorFiltroRV.getLista() != null) {
                    lista.clear();
                    lista.addAll(adaptadorFiltroRV.getLista());
                }

                setRv();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setAcciones();
    }

    protected String setSubtitulo() {
        return getString(tituloPlural);
    }

    protected void setControls(View view) {
    }

    protected void setAcciones() {
    }

    protected String[] setCamposFiltro() {
        return null;
    }

    protected abstract TipoViewHolder setViewHolder(View view);

    protected void listaRV() {

        if (listab == null) {
            setLista();
        } else {
            lista = listab;
        }

        setRv();

    }

    protected void setMultiColumnas() {

        multiColunas = true;
    }

    protected void setRv() {

        setMultiColumnas();
        int columnas = 1;
        if (multiColunas) {
            columnas = (int) (rv.getWidth() / (metrics.density * 300));
        }
        if (columnas < 1) {
            columnas = 1;
        }
        layoutManager = new GridLayoutManager(contexto, columnas);
        setManagerRV();
        rv.setLayoutManager(layoutManager);
        adaptadorRV = new RVAdapter(setViewHolder(view), lista, layoutItem);
        rv.setAdapter(adaptadorRV);

        adaptadorRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickRV(v);
            }
        });

        adaptadorFiltroRV = setAdaptadorAuto(contexto, layoutItem, lista, setCamposFiltro());

        auto.setAdapter(adaptadorFiltroRV);

        auto.setThreshold(3);

        setOnItemClickAuto();

        //if (lista.chechLista()) {
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

        onSetRV();

    }

    protected void onSetRV() {

    }

    protected void setManagerRV() {

    }

    protected ListaAdaptadorFiltro setAdaptadorAutoFiltro
            (Context context, int layoutItem, ArrayList lista, String[] campos) {
        return null;
    }

    protected abstract ListaAdaptadorFiltroModelo setAdaptadorAuto
            (Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos);

    protected void setLista() {
    }

    protected void actualizarConsultasRV() {

        if (listab == null) {

            setLista();
        } else {
            lista = listab;
        }

    }

    protected void onClickRV(View v) {

        setOnClickRV(lista.get(rv.getChildAdapterPosition(v)));
    }

    public abstract void setOnClickRV(Object object);

    public void setOnClickRV(Object object, int position) {

    }


    protected void setOnItemClickAuto() {

        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {

                auto.setText("");
                object = adaptadorFiltroRV.getItem(position);

                selector();

            }
        });

    }

}
