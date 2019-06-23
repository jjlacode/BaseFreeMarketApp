package jjlacode.com.freelanceproject.util.crud;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.adapter.RVAdapter;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.android.AndroidUtil;

public abstract class FragmentCRUD extends FragmentCUD implements JavaUtil.Constantes, CommonPry.Constantes {

    protected LinearLayout frLista;
    protected View viewRV;
    protected int layoutItem;
    protected RecyclerView rv;
    protected AutoCompleteTextView auto;
    protected boolean maestroDetalleSeparados;
    protected ArrayList<Modelo> listafiltrada;
    protected ImageView buscar;
    protected ImageView renovar;
    protected ImageView inicio;
    protected ImageView lupa;
    protected ListaAdaptadorFiltroRV adaptadorFiltroRV;
    protected RVAdapter adaptadorRV;
    protected SwipeRefreshLayout refreshLayout;


    public FragmentCRUD() {
    }

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
        frdetalle = view.findViewById(R.id.layout_detalle);

        frPie = view.findViewById(R.id.layout_pie);
        viewBotones = inflater.inflate(R.layout.btn_sdb,container,false);
        viewRV = inflater.inflate(R.layout.rvlayout,container,false);
        viewCuerpo = inflater.inflate(layoutCuerpo,container,false);
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
        if(viewCuerpo.getParent() != null) {
            ((ViewGroup)viewCuerpo.getParent()).removeView(viewCuerpo); // <- fix
        }


        frdetalle.addView(viewCuerpo);
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

        Chronometer timer = (Chronometer) view.findViewById(R.id.chronocrud);
        setTimer(timer);

        setInicio();

        AndroidUtil.ocultarTeclado(activityBase, view);

        return view;
    }

    protected void selector(){

        maestroDetalle();

        if (bundle.getBoolean(NUEVOREGISTRO)){

            if (tablaCab==null) {
                id = null;
            }
            modelo = null;
            secuencia=0;
            activityBase.toolbar.setSubtitle(tituloNuevo);
            vaciarControles();
            path = null;
            setImagen();
            if (maestroDetalleSeparados) {
                if (layoutCabecera > 0) {
                    frCabecera.setVisibility(View.GONE);
                }
                refreshLayout.setVisibility(View.GONE);
                frdetalle.setVisibility(View.VISIBLE);
                frPie.setVisibility(View.VISIBLE);
            }
            activityBase.fab.hide();
            activityBase.fab2.hide();
            setNuevo();
            btndelete.setVisibility(View.GONE);
            bundle.putBoolean(NUEVOREGISTRO,false);
        }else if (modelo!=null) {

            btndelete.setVisibility(View.VISIBLE);

            id = modelo.getString(campoID);
            if (tablaCab != null) {
                secuencia = modelo.getInt(SECUENCIA);
            }
            if (maestroDetalleSeparados){
                if (layoutCabecera>0) {
                    frCabecera.setVisibility(View.GONE);
                }
                refreshLayout.setVisibility(View.GONE);
                frdetalle.setVisibility(View.VISIBLE);
                frPie.setVisibility(View.VISIBLE);
                activityBase.fab.hide();
                activityBase.fab2.hide();
            }
            datos();

            if (subTitulo==null) {
                activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());
            }

        }else if (id!=null  && (secuencia>0||tablaCab==null)) {

            btndelete.setVisibility(View.VISIBLE);

            if (tablaCab != null) {
                modelo = CRUDutil.setModelo(campos,id,secuencia);
            }else{
                modelo = CRUDutil.setModelo(campos,id);
            }

            if (maestroDetalleSeparados){
                if (layoutCabecera>0) {
                    frCabecera.setVisibility(View.GONE);
                }
                refreshLayout.setVisibility(View.GONE);
                frdetalle.setVisibility(View.VISIBLE);
                frPie.setVisibility(View.VISIBLE);
                activityBase.fab.hide();
                activityBase.fab2.hide();
            }
            datos();

            if (subTitulo==null) {
                activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());
            }

        }else{
                maestroDetalle();
                btndelete.setVisibility(View.GONE);
                activityBase.fab2.hide();
                activityBase.fab.show();
                listaRV();
            if (subTitulo==null) {
                activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());
            }
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
                vaciarControles();
                path=null;
                setImagen();
                maestroDetalle();
                if (maestroDetalleSeparados){

                    frCabecera.setVisibility(View.GONE);
                    frLista.setVisibility(View.GONE);
                    frdetalle.setVisibility(View.VISIBLE);
                    frPie.setVisibility(View.VISIBLE);
                    activityBase.fab.hide();
                    activityBase.fab2.hide();

                }
                activityBase.toolbar.setSubtitle(tituloNuevo);

                setNuevo();

            }
        });

        activityBase.fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdate();
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
                    lista.clearAddAll(adaptadorFiltroRV.getLista());
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
                    lista.clearAddAll(adaptadorFiltroRV.getLista());
                }

                setRv();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected boolean onUpdate(){

        if (update()) {
                selector();
            return true;
        }
        return false;
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

        if (!lista.chech() && !maestroDetalleSeparados){
            frdetalle.setVisibility(View.GONE);
        }else{
            frdetalle.setVisibility(View.VISIBLE);
        }

        setRv();



    }

    protected void setRv(){

        adaptadorRV = new RVAdapter(setViewHolder(view),lista.getLista(), layoutItem, getString(tituloPlural));
        rv.setAdapter(adaptadorRV);

        onSetAdapter(lista.getLista());

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

        //if (lista.chech()) {
            rv.setVisibility(View.VISIBLE);
            auto.setVisibility(View.VISIBLE);
            buscar.setVisibility(View.VISIBLE);
            renovar.setVisibility(View.VISIBLE);
            inicio.setVisibility(View.VISIBLE);
            lupa.setVisibility(View.VISIBLE);

        //}else {
            //auto.setVisibility(View.GONE);
            buscar.setVisibility(View.GONE);
            //renovar.setVisibility(View.GONE);
        if (listab==null) {
            inicio.setVisibility(View.GONE);
        }
            //frlupa.setVisibility(View.GONE);
        //}



    }
    protected abstract ListaAdaptadorFiltroRV setAdaptadorAuto
            (Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos);

    protected void setLista() {

    }

    protected void  maestroDetalle(){


        if(!land && !tablet){

            maestroDetallePort();

        }else if (land && !tablet){

            maestroDetalleLand();

        }else if (!land ){

            maestroDetalleTabletPort();

        }else {

            maestroDetalleTabletLand();

        }

    }

    protected void maestroDetallePort(){

        setMaestroDetallePort();
        if (maestroDetalleSeparados) {
            defectoMaestroDetalleSeparados();
        }else {
            defectoMaestroDetalleJuntos();
        }

    }

    protected void maestroDetalleLand(){

        setMaestroDetalleLand();
        if (maestroDetalleSeparados) {
            defectoMaestroDetalleSeparados();
        }else{
            defectoMaestroDetalleJuntos();
        }
    }

    protected void maestroDetalleTabletPort(){

        setMaestroDetalleTabletPort();
        if (maestroDetalleSeparados) {
            defectoMaestroDetalleSeparados();
        }else{
            defectoMaestroDetalleJuntos();

        }
    }

    protected void maestroDetalleTabletLand(){

        setMaestroDetalleTabletLand();
        if (maestroDetalleSeparados) {
            defectoMaestroDetalleSeparados();
        }else{
            defectoMaestroDetalleJuntos();
        }

    }

    protected void defectoMaestroDetalleSeparados(){


            if (layoutCabecera>0) {
                frCabecera.setVisibility(View.VISIBLE);
            }
            frLista.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.VISIBLE);
            frdetalle.setVisibility(View.GONE);
            frPie.setVisibility(View.GONE);

        setDefectoMaestroDetalleSeparados();

    }

    protected void setDefectoMaestroDetalleSeparados(){

    }

    protected void setDefectoMaestroDetalleJuntos(){

    }

    protected void defectoMaestroDetalleJuntos(){

        frLista.setVisibility(View.VISIBLE);
        frdetalle.setVisibility(View.VISIBLE);
        frPie.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.VISIBLE);
        btnback.setVisibility(View.GONE);
        if (layoutCabecera>0) {
            frCabecera.setVisibility(View.VISIBLE);
        }else{
            frCabecera.setVisibility(View.GONE);
        }

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


    protected void actualizarConsultasRV(){

        if (listab==null) {
            if (tablaCab!=null){
                modelo = null;
                secuencia = 0;
                lista = CRUDutil.setListaModeloDetalle(campos,id,tablaCab);
            }else {
                modelo = null;
                id = null;
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
                secuencia = modelo.getInt(SECUENCIA);
            }
            if (maestroDetalleSeparados){
                if (layoutCabecera>0) {
                    frCabecera.setVisibility(View.GONE);
                }
                refreshLayout.setVisibility(View.GONE);
                frdetalle.setVisibility(View.VISIBLE);
                frPie.setVisibility(View.VISIBLE);
                activityBase.fab.hide();
                activityBase.fab2.hide();
            }
            selector();

            if (subTitulo==null) {
                activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());
            }
    }

    protected void onSetAdapter(ArrayList<Modelo> lista){

        if (!maestroDetalleSeparados && lista != null && lista.size() > 0) {

                modelo = lista.get(0);

                if (tablaCab != null) {
                    secuencia = lista.get(0).getInt(SECUENCIA);
                } else {
                    id = lista.get(0).getString(campoID);
                }

                selector();
        }

    }

    protected void setOnItemClickAuto(){

        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {

                auto.setText("");
                modelo = adaptadorFiltroRV.getItem(position);
                if (modelo!=null) {
                    id = modelo.getString(campoID);
                    if (tablaCab != null) {
                        secuencia = modelo.getInt(SECUENCIA);
                    }
                }

                selector();

            }
        });

    }

    protected void cambiarFragment(){

        enviarBundle();
        setcambioFragment();
        listaRV();
    }

}
