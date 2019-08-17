package com.jjlacode.base.util.crud;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jjlacode.base.util.adapter.ListaAdaptadorFiltroModelo;
import com.jjlacode.base.util.adapter.RVAdapter;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.logica.Interactor;

import java.util.ArrayList;

public abstract class FragmentRVR extends FragmentBaseCRUD {

    protected LinearLayout frLista;
    protected View viewRV;
    protected int layoutItem;
    protected RecyclerView rv;
    protected AutoCompleteTextView auto;
    protected ArrayList<Modelo> listafiltrada;
    protected ImageView buscar;
    protected ImageView renovar;
    protected ImageView inicio;
    protected ImageView lupa;
    protected ListaAdaptadorFiltroModelo adaptadorFiltroRV;
    protected RVAdapter adaptadorRV;
    protected SwipeRefreshLayout refreshLayout;


    @Override
    public void onResume() {
        super.onResume();

        selector();
        setDatos();

        setAcciones();

    }

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container){

        super.setOnCreateView(view,inflater,container);

        frLista = view.findViewById(R.id.layout_rv);

        viewRV = inflaterMain.inflate(R.layout.rvlayout,containerMain,false);
        if(viewRV.getParent() != null) {
            ((ViewGroup) viewRV.getParent()).removeView(viewRV); // <- fix
        }
        frLista.addView(viewRV);

        rv = view.findViewById(R.id.rv);
        refreshLayout = view.findViewById(R.id.swipeRefresh);
        auto = view.findViewById(R.id.auto);
        buscar = view.findViewById(R.id.imgbuscar);
        renovar = view.findViewById(R.id.imgrenovar);
        inicio = view.findViewById(R.id.imginicio);
        lupa = view.findViewById(R.id.imgsearch);

        refreshLayout.setColorSchemeResources(
                R.color.s1,
                R.color.s2,
                R.color.s3,
                R.color.s4
        );
        System.out.println("setOnCreateView RVR");

    }

    protected void selector(){

        gonePie();
            visible(inicio);
        visible(frLista);
        activityBase.fabNuevo.hide();
        activityBase.fabVoz.show();
            listaRV();
        if (subTitulo==null) {
            activityBase.toolbar.setSubtitle(Interactor.setNamefdef());
        } else {
            activityBase.toolbar.setSubtitle(subTitulo);

        }
            enviarAct();

        acciones();

    }

    @Override
    protected void acciones() {
        super.acciones();

        activityBase.fabNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                modelo = null;
                if (tablaCab==null) {
                    id = null;
                }
                secuencia=0;

                path=null;


            }
        });


        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        if (listab==null) {

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

                if (adaptadorFiltroRV.getLista()!=null){
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
            }
        });

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tablaCab!=null){
                    lista = CRUDutil.setListaModeloDetalle(campos,id,tablaCab);
                    setLista();
                }else {
                    lista = CRUDutil.setListaModelo(campos);
                    setLista();
                }
                setRv();
                if (subTitulo==null) {
                    activityBase.toolbar.setSubtitle(Interactor.setNamefdef());
                }
                enviarAct();
            }
        });

        auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (id==null) {
                    auto.setDropDownWidth(0);
                }else{
                    auto.setDropDownWidth(ancho);
                }
                if (adaptadorFiltroRV.getLista()!=null){
                    lista.clearAddAllLista(adaptadorFiltroRV.getLista());
                }

                setRv();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    protected abstract TipoViewHolder setViewHolder(View view);

    protected void listaRV(){

        lista = new ListaModelo();
        if (listab==null) {
            if (tablaCab==null){
                lista = CRUDutil.setListaModelo(campos);
            }else{
                lista = CRUDutil.setListaModeloDetalle(campos,id,tablaCab);
            }
            setLista();
        }else {
            lista.clearAddAllLista(listab);
        }

        setRv();

    }

    protected void setRv(){

        adaptadorRV = new RVAdapter(setViewHolder(view),lista.getLista(), layoutItem);
        rv.setAdapter(adaptadorRV);

        adaptadorRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickRV(v);
            }
        });

        adaptadorFiltroRV = setAdaptadorAuto(contexto, layoutItem,lista.getLista(),campos);

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
        if (listab==null) {
            inicio.setVisibility(View.GONE);
        }

    }

    protected abstract ListaAdaptadorFiltroModelo setAdaptadorAuto
            (Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos);

    protected void setLista() {

    }

    protected void actualizarConsultasRV(){

        lista = new ListaModelo();

        if (listab==null) {
            if (tablaCab!=null){
                lista = CRUDutil.setListaModeloDetalle(campos,id,tablaCab);
            }else {
                lista = CRUDutil.setListaModelo(campos);
            }
            setLista();
        }else{
            lista.clearAddAllLista(listab);
        }
        gonePie();

    }

    protected void onClickRV(View v){

        modelo = lista.getItem(rv.getChildAdapterPosition(v));
        id = modelo.getString(campoID);

        if (tablaCab != null) {
            secuencia = modelo.getInt(CAMPO_SECUENCIA);
        }

        setOnClickRV(id, secuencia, modelo);
    }

    public abstract void setOnClickRV(String id, int secuencia, Modelo modelo);


    protected void setOnItemClickAuto(){

        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {

                auto.setText("");
                modelo = adaptadorFiltroRV.getItem(position);
                if (modelo!=null) {
                    id = modelo.getString(campoID);
                    if (tablaCab != null) {
                        secuencia = modelo.getInt(CAMPO_SECUENCIA);
                    }
                }

                selector();

            }
        });

    }

}
