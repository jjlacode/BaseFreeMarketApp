package com.codevsolution.base.android;

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

import com.codevsolution.base.adapter.ListaAdaptadorFiltro;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.animation.OneFrameLayout;
import com.codevsolution.base.style.Estilos;

import java.util.ArrayList;

//import com.codevsolution.freemarketsapp.logica.Interactor;

public abstract class FragmentRVnoSQL extends FragmentBase {

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
    protected ListaAdaptadorFiltro adaptadorFiltroRV;
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
    protected boolean refreshOn;
    protected LinearLayoutCompat lybusca;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        Log.d(TAG, getMetodo());

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
        lybusca = viewRV.findViewById(Estilos.getIdResource(contexto, "lybusca_rv"));
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
        refreshOn = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, getMetodo());

        selector();
    }

    @Override
    protected void setLayoutExtra() {
        super.setLayoutExtra();

        Log.d(TAG, getMetodo());

        layoutPie = Estilos.getIdLayout(contexto, "btn_sdb");
    }

    protected void selector() {

        Log.d(TAG, getMetodo());

        visible(inicio);
        listaRV();

        acciones();
    }

    protected void acciones() {
        super.acciones();

        Log.d(TAG, getMetodo());

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        if (refreshOn) {
                            if (listab == null) {

                                setLista();

                            }
                            rv.setVisibility(View.VISIBLE);
                            setRv();
                        }

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
        Log.d(TAG, getMetodo());

        return getString(tituloPlural);
    }

    protected void setControls(View view) {
        Log.d(TAG, getMetodo());

    }

    protected void setAcciones() {
        Log.d(TAG, getMetodo());

    }

    protected String[] setCamposFiltro() {
        Log.d(TAG, getMetodo());

        return null;
    }

    protected abstract TipoViewHolder setViewHolder(View view);

    protected void listaRV() {

        Log.d(TAG, getMetodo());

        if (listab == null) {
            setLista();
        } else {
            lista = listab;
        }

        setRv();

    }

    protected void setMultiColumnas() {
        Log.d(TAG, getMetodo());

        multiColunas = true;
    }

    protected void setRv() {
        Log.d(TAG, getMetodo());

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
        Log.d(TAG, getMetodo());

    }

    protected void setManagerRV() {
        Log.d(TAG, getMetodo());

    }

    protected abstract ListaAdaptadorFiltro setAdaptadorAuto
            (Context context, int layoutItem, ArrayList<?> lista, String[] campos);

    protected void setLista() {
        Log.d(TAG, getMetodo());

    }

    protected void actualizarConsultasRV() {
        Log.d(TAG, getMetodo());

        if (listab == null) {

            setLista();
        } else {
            lista = listab;
        }

    }

    protected void onClickRV(View v) {
        Log.d(TAG, getMetodo());

        setOnClickRV(lista.get(rv.getChildAdapterPosition(v)));
        setOnClickRV(lista.get(rv.getChildAdapterPosition(v)), rv.getChildAdapterPosition(v));
    }

    public abstract void setOnClickRV(Object object);

    public void setOnClickRV(Object object, int position) {
        Log.d(TAG, getMetodo());

    }


    protected void setOnItemClickAuto() {
        Log.d(TAG, getMetodo());

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
