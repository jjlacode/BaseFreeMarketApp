package jjlacode.com.freelanceproject.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;

import jjlacode.com.freelanceproject.R;

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
    protected String namesubclasstemp;
    protected ArrayList<Modelo> listafiltrada;
    protected ImageView buscar;
    protected ImageView renovar;
    protected ListaAdaptadorFiltroRV adaptadorFiltroRV;


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
        auto = view.findViewById(R.id.auto);
        buscar = view.findViewById(R.id.imgbuscar);
        renovar = view.findViewById(R.id.imgrenovar);

        setInicio();

        return view;
    }

    protected void selector(){

        listaRV();
        maestroDetalle();

        if (modelo!=null){

            setDatos();

        } else if (nuevo) {

            setNuevo();
            btndelete.setVisibility(View.GONE);
            if (!maestroDetalleSeparados){
                icFragmentos.fabVisible();
                enviarAct();
            }else{
                frLista.setVisibility(View.GONE);
            }

        }
    }

    @Override
    protected boolean onUpdate(){

        if (update()) {
            nuevo = false;
            maestroDetalle();
            enviarAct();
            listaRV();
            return true;
        }
        return false;
    }

    protected abstract TipoViewHolder setViewHolder(View view);

    protected void listaRV(){

        actualizarConsultasRV();

        setLista();

        setRv();

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
                actualizarConsultasRV();
                setRv();
            }
        });


    }

    protected void setRv(){

        RVAdapter adapter = new RVAdapter(setViewHolder(view),lista.getLista(),layoutitem, namef);
        rv.setAdapter(adapter);

        onSetAdapter(lista.getLista());

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickRV(v);
            }
        });

        adaptadorFiltroRV = setAdaptadorAuto(contexto,layoutitem,lista.getLista(),campos);

        auto.setAdapter(adaptadorFiltroRV);

        setOnItemClickAuto();

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

        if (nuevo || modelo!=null){
            if (layoutCabecera>0) {
                frCabecera.setVisibility(View.GONE);
            }
            rv.setVisibility(View.GONE);
            frdetalle.setVisibility(View.VISIBLE);
            frPie.setVisibility(View.VISIBLE);
            icFragmentos.fabOculto();
            if (!nuevo) {
                btndelete.setVisibility(View.VISIBLE);
            }
        }else {
            if (layoutCabecera>0) {
                frCabecera.setVisibility(View.VISIBLE);
            }
            frLista.setVisibility(View.VISIBLE);
            rv.setVisibility(View.VISIBLE);
            frdetalle.setVisibility(View.GONE);
            frPie.setVisibility(View.GONE);
            icFragmentos.fabVisible();

        }

        setDefectoMaestroDetalleSeparados();

    }

    protected void setDefectoMaestroDetalleSeparados(){

    }

    protected void setDefectoMaestroDetalleJuntos(){

    }

    protected void defectoMaestroDetalleJuntos(){

        frLista.setVisibility(View.VISIBLE);
        rv.setVisibility(View.VISIBLE);
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


                System.out.println("idRV = " + id);
                System.out.println("tablaCab = " + tablaCab);
                if (tablaCab != null) {

                    setListaModeloDetalle();
                    //lista.setLista(consulta.queryList(campos,campoID,id,null,IGUAL,null));
                    if (lista.chech()){
                        System.out.println("listadetalle = " + lista.size());
                    }else{
                        Log.e(TAG,"listadetalle nula");
                    }

                } else {

                    setListaModelo();
                    if (lista.chech()){
                        System.out.println("lista = " + lista.size());
                    }else{
                        Log.e(TAG,"lista nula");
                    }

                }

    }

    protected void onClickRV(View v){

        if (lista!=null && lista.size()>0) {
            modelo = lista.getItem(rv.getChildAdapterPosition(v));
            if (modelo != null) {
                id = modelo.getString(campoID);
                if (tablaCab != null) {
                    secuencia = modelo.getInt(SECUENCIA);
                }
            }else{
                Log.e(TAG, "modelo null");
            }
            if (nuevo) {
                nuevo = false;
                namesubclass = namesubtemp;
            } else {
                namesubtemp = namesubclass;
            }
            enviarAct();
            maestroDetalle();
            setDatos();
        }else{
            Log.e(TAG, "Lista RV null");
        }
    }

    protected void onSetAdapter(ArrayList<Modelo> lista){

        maestroDetalle();
        if (!maestroDetalleSeparados) {


            if (!nuevo && lista != null && lista.size() > 0) {

                frdetalle.setVisibility(View.VISIBLE);

                modelo = lista.get(0);

                if (tablaCab!=null){
                    secuencia = lista.get(0).getInt(SECUENCIA);
                }else{
                    id = lista.get(0).getString(campoID);
                }

                setDatos();

            } else if (!nuevo) {
                frdetalle.setVisibility(View.GONE);
            } else {
                    frdetalle.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void setOnItemClickAuto(){

        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {

                auto.setText("");
                modelo = (Modelo) auto.getAdapter().getItem(position);
                id = modelo.getString(campoID);
                if (tablaCab!=null){
                    secuencia = lista.getItem(0).getInt(SECUENCIA);
                }
                if (nuevo){
                    nuevo = false;
                    namesubclass = namesubclasstemp;
                }else {
                    namesubclasstemp = namesubclass;
                }
                enviarAct();
                maestroDetalle();
                setDatos();

            }
        });



    }


    protected void cambiarFragment(){

        maestroDetalle();
        setcambioFragment();
        enviarBundle();
        enviarAct();
        listaRV();
    }


}
