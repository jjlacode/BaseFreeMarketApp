package com.jjlacode.base.util.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jjlacode.base.util.crud.ListaModelo;
import com.jjlacode.base.util.crud.Modelo;

import java.util.ArrayList;

import static com.jjlacode.base.util.JavaUtil.Constantes.LISTA;
import static com.jjlacode.base.util.JavaUtil.Constantes.LISTAMODELO;
import static com.jjlacode.base.util.JavaUtil.Constantes.MODELO;

public class RVAdapter extends RecyclerView.Adapter<BaseViewHolder>
        implements View.OnClickListener, View.OnLongClickListener{

    protected ArrayList list;
    protected ListaModelo listaModelo;
    private View.OnClickListener listener;
    private View.OnLongClickListener longClickListener;
    private String tipoVH;
    private int layout;
    private TipoViewHolder tipoViewHolder;

    public RVAdapter(TipoViewHolder tipoViewHolder, ArrayList<?> list, int layout) {

        this.list = list;
        this.tipoVH = LISTA;
        if (list.size()>0 && list.get(0) instanceof Modelo){
            tipoVH = MODELO;
        }
        this.layout = layout;
        this.tipoViewHolder = tipoViewHolder;

    }

    public RVAdapter(TipoViewHolder tipoViewHolder, ListaModelo list, int layout) {

        this.listaModelo = list;
        this.list = new ArrayList();
        for (ArrayList<Modelo> modeloArrayList : listaModelo) {
            this.list.add(modeloArrayList.size());
        }
        this.tipoVH = LISTAMODELO;
        this.layout = layout;
        this.tipoViewHolder = tipoViewHolder;

    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(layout, null, false);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

        return tipoViewHolder.holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        if (tipoVH !=null && tipoVH.equals(LISTAMODELO)){

            holder.bind(listaModelo,position);
        }else if (tipoVH !=null && tipoVH.equals(LISTA)){

            holder.bind(list,position);

        }else if (tipoVH !=null && tipoVH.equals(MODELO)){

            holder.bind((Modelo) list.get(position));
        }else {

            holder.bind(list,position);

        }

    }

    /*
   Añade una lista completa de items
    */
    public void addAll(ArrayList<?> lista){
        list.addAll(lista);
        notifyDataSetChanged();
    }

    public void addAll(ListaModelo lista){
        listaModelo.addAll(lista);
        this.list = new ArrayList();
        for (ArrayList<Modelo> modeloArrayList : listaModelo) {
            this.list.add(modeloArrayList.size());
        }
        notifyDataSetChanged();
    }

    public void clearAddAll(ArrayList<?> lista) {
        list.clear();
        list.addAll(lista);
        notifyDataSetChanged();
    }

    public void clearAddAll(ListaModelo lista) {
        listaModelo.clear();
        listaModelo.addAll(lista);
        this.list = new ArrayList();
        for (ArrayList<Modelo> modeloArrayList : listaModelo) {
            this.list.add(modeloArrayList.size());
        }
        notifyDataSetChanged();
    }
    /*
    Permite limpiar todos los elementos del recycler
     */
    public void clear(){

        if (tipoVH !=null && tipoVH.equals(LISTAMODELO)) {

            listaModelo.clear();
            list.clear();

        }else {

            list.clear();
        }
        notifyDataSetChanged();
    }
    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }

    @Override
    public int getItemCount() {

        if (tipoVH !=null && tipoVH.equals(LISTAMODELO)){

            if (listaModelo!=null) {
                return listaModelo.size();
            }

        }else if (list!=null) {

            return list.size();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {

        if (listener != null) {

            listener.onClick(v);

        }

    }

    @Override
    public boolean onLongClick(View view) {

        if (longClickListener != null) {

            longClickListener.onLongClick(view);
            return true;
        }
        return false;
    }
}