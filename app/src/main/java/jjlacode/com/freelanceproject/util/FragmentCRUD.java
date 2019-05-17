package jjlacode.com.freelanceproject.util;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.MainActivity;
import jjlacode.com.freelanceproject.R;

import static jjlacode.com.freelanceproject.R.color.colorAccent;

public abstract class FragmentCRUD extends FragmentCUD implements JavaUtil.Constantes, CommonPry.Constantes{

    protected RelativeLayout frPrincipal;
    protected LinearLayout frLista;
    protected LinearLayout frdetalle;
    protected LinearLayout frPie;
    protected LinearLayout frCabecera;
    protected View viewCabecera;
    protected View viewRV;
    protected View viewCuerpo;
    protected View viewBotones;
    protected int layoutCuerpo;
    protected int layoutCabecera;
    protected int layoutitem;
    protected RecyclerView rv;
    protected AutoCompleteTextView auto;
    protected boolean maestroDetalleSeparados;
    protected ArrayList<Modelo> listafiltrada;
    protected ImageView buscar;
    protected ImageView renovar;
    protected ImageView inicio;
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
        btnsave.setVisibility(View.GONE);
        btndelete.setTextColor(getResources().getColor(colorAccent));

        refreshLayout.setColorSchemeResources(
                R.color.s1,
                R.color.s2,
                R.color.s3,
                R.color.s4
        );

        setInicio();

        return view;
    }

    protected void selector(){

        if (bundle.getBoolean(NUEVOREGISTRO)){

            id=null;
            modelo = null;
            secuencia=0;
            icFragmentos.showSubTitle(getString(R.string.nuevo)+" " + getString(tituloSingular));
            setNuevo();
            btndelete.setVisibility(View.GONE);
            activityBase.fab.hide();
            activityBase.fab2.show();
        }else if (modelo!=null) {

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
                activityBase.fab2.show();
            }
            setDatos();

            activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());

        }else{
                maestroDetalle();
                activityBase.fab2.hide();
                activityBase.fab.show();
                listaRV();
                activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());
                enviarAct();
        }

        activityBase.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modelo = new Modelo(campos);
                setDatos();
                modelo = null;
                id=null;
                secuencia=0;
                maestroDetalle();
                if (maestroDetalleSeparados){

                    frCabecera.setVisibility(View.GONE);
                    frLista.setVisibility(View.GONE);
                    frdetalle.setVisibility(View.VISIBLE);
                    frPie.setVisibility(View.VISIBLE);
                    activityBase.fab.hide();
                    activityBase.fab2.show();

                }
                activityBase.toolbar.setSubtitle(getString(R.string.nuevo) + " " + getString(tituloSingular));

                setNuevo();

            }
        });

        activityBase.fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdate();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected boolean onUpdate(){

        if (update()) {
            if (maestroDetalleSeparados){
                btndelete.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return false;
    }

    protected abstract TipoViewHolder setViewHolder(View view);

    protected void listaRV(){

        if (listab==null) {
            if (tablaCab==null){
                setListaModelo();
            }else{
                setListaModeloDetalle();
            }
            setLista();
        }else {
            lista = listab;
        }

        setRv();


    }

    protected void setRv(){

        adaptadorRV = new RVAdapter(setViewHolder(view),lista.getLista(),layoutitem, getString(tituloPlural));
        rv.setAdapter(adaptadorRV);

        onSetAdapter(lista.getLista());

        adaptadorRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickRV(v);
            }
        });

        adaptadorFiltroRV = setAdaptadorAuto(contexto,layoutitem,lista.getLista(),campos);

        auto.setAdapter(adaptadorFiltroRV);

        setOnItemClickAuto();

        if (lista.chech()) {
            auto.setVisibility(View.VISIBLE);
            buscar.setVisibility(View.VISIBLE);
            renovar.setVisibility(View.VISIBLE);
            inicio.setVisibility(View.VISIBLE);

        }else {
            auto.setVisibility(View.GONE);
            buscar.setVisibility(View.GONE);
            renovar.setVisibility(View.GONE);
            inicio.setVisibility(View.GONE);
        }

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        if (listab==null) {

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

                lista.clearAddAll(adaptadorFiltroRV.getLista());

                setRv();
            }
        });

        renovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listab==null) {
                    if (tablaCab!=null){
                        setListaModeloDetalle();
                        setLista();
                    }else {
                        setListaModelo();
                        setLista();
                    }
                    setLista();
                }else{
                    lista = listab;
                }
                setRv();
            }
        });

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tablaCab!=null){
                    setListaModeloDetalle();
                    setLista();
                }else {
                    setListaModelo();
                    setLista();
                }
                setRv();
                activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());
                enviarAct();
            }
        });

    }
    protected abstract ListaAdaptadorFiltroRV setAdaptadorAuto
            (Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos);

    protected abstract void setLista();

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

    @Override
    protected void acciones() {
        super.acciones();



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
            icFragmentos.fabVisible();

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

    protected abstract void setMaestroDetallePort();
    protected abstract void setMaestroDetalleLand();
    protected abstract void setMaestroDetalleTabletLand();
    protected abstract void setMaestroDetalleTabletPort();


    protected void actualizarConsultasRV(){


            if (tablaCab != null) {

                setListaModeloDetalle();

            } else {

                setListaModelo();

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
                activityBase.fab2.show();
            }
            setDatos();

            activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());
    }

    protected void onSetAdapter(ArrayList<Modelo> lista){

        if (!maestroDetalleSeparados && lista != null && lista.size() > 0) {

                modelo = lista.get(0);

                if (tablaCab != null) {
                    secuencia = lista.get(0).getInt(SECUENCIA);
                } else {
                    id = lista.get(0).getString(campoID);
                }

                setDatos();
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

                setDatos();

            }
        });

    }

    protected void cambiarFragment(){

        enviarBundle();
        setcambioFragment();
        listaRV();
    }

}
