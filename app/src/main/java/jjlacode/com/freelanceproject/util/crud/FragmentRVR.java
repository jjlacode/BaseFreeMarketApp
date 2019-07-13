package jjlacode.com.freelanceproject.util.crud;

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

import java.util.ArrayList;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.adapter.RVAdapter;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;

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
    protected ListaAdaptadorFiltroRV adaptadorFiltroRV;
    protected RVAdapter adaptadorRV;
    protected SwipeRefreshLayout refreshLayout;


    @Override
    public void onResume() {
        super.onResume();

        setDatos();

        setAcciones();

    }

    /*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.contenido, container, false);
        land = getResources().getBoolean(R.bool.esLand);
        tablet = getResources().getBoolean(R.bool.esTablet);
        System.out.println("land = " + land);
        System.out.println("tablet = " + tablet);

        frPrincipal = view.findViewById(R.id.contenedor);
        frLista = view.findViewById(R.id.layout_rv);

        frPie = view.findViewById(R.id.layout_pie);
        viewBotones = inflater.inflate(R.layout.btn_sdb,container,false);
        viewRV = inflater.inflate(R.layout.rvlayout,container,false);
        frCabecera = view.findViewById(R.id.layout_cabecera);
        if (layoutCabecera>0) {
            viewCabecera = inflater.inflate(layoutCabecera, container,false);
            if(viewCabecera.getParent() != null) {
                ((ViewGroup)viewCabecera.getParent()).removeView(viewCabecera); // <- fix
            }
            if (viewCabecera!=null) {
                frCabecera.addView(viewCabecera);
            }
        }
        if(viewBotones.getParent() != null) {
            ((ViewGroup)viewBotones.getParent()).removeView(viewBotones); // <- fix
        }
        if(viewRV.getParent() != null) {
            ((ViewGroup)viewRV.getParent()).removeView(viewRV); // <- fix
        }

        frLista.addView(viewRV);
        frPie.addView(viewBotones);

        btnback = view.findViewById(R.id.btn_back);
        btnsave = view.findViewById(R.id.btn_save);
        btndelete = view.findViewById(R.id.btn_del);
        rv = view.findViewById(R.id.rv);
        refreshLayout = view.findViewById(R.id.swipeRefresh);
        auto = view.findViewById(R.id.auto);
        buscar = view.findViewById(R.id.imgbuscar);
        renovar = view.findViewById(R.id.imgrenovar);
        inicio = view.findViewById(R.id.imginicio);
        lupa = view.findViewById(R.id.imgsearch);
        //btnsave.setVisibility(View.GONE);
        //btndelete.setTextColor(getResources().getColor(colorAccent));

        refreshLayout.setColorSchemeResources(
                R.color.s1,
                R.color.s2,
                R.color.s3,
                R.color.s4
        );

        setInicio();

        AndroidUtil.ocultarTeclado(activityBase, view);

        return view;
    }
    */

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
        //btnsave.setVisibility(View.GONE);
        //btndelete.setTextColor(getResources().getColor(colorAccent));

        refreshLayout.setColorSchemeResources(
                R.color.s1,
                R.color.s2,
                R.color.s3,
                R.color.s4
        );
        System.out.println("setOnCreateView RVR");

    }

    protected void selector(){

            btndelete.setVisibility(View.GONE);
            visible(inicio);
            activityBase.fab2.hide();
            activityBase.fab.show();
            listaRV();
            if (subTitulo==null) {
                activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());
            enviarAct();
        }

        acciones();

    }

    @Override
    protected void acciones() {
        super.acciones();

        activityBase.fab.setOnClickListener(new View.OnClickListener() {
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
                    activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());
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

        if (listab==null) {
            if (tablaCab==null){
                lista = CRUDutil.setListaModelo(campos);
            }else{
                lista = CRUDutil.setListaModeloDetalle(campos,id,tablaCab);
            }
            setLista();
        }else {
            lista = listab;
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
    protected abstract ListaAdaptadorFiltroRV setAdaptadorAuto
            (Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos);

    protected void setLista() {

    }

    protected void actualizarConsultasRV(){

        if (listab==null) {
            if (tablaCab!=null){
                lista = CRUDutil.setListaModeloDetalle(campos,id,tablaCab);
            }else {
                lista = CRUDutil.setListaModelo(campos);
            }
            setLista();
        }else{
            lista = listab;
        }

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
