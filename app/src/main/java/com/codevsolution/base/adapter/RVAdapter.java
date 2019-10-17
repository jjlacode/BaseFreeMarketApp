package com.codevsolution.base.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;

import java.util.ArrayList;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.LISTA;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.LISTAMODELO;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.MODELO;

public class RVAdapter extends RecyclerView.Adapter<BaseViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    protected ArrayList list;
    protected ListaModeloSQL listaModeloSQL;
    private View.OnClickListener listener;
    private View.OnLongClickListener longClickListener;
    private String tipoVH;
    private int layout;
    private TipoViewHolder tipoViewHolder;

    public RVAdapter(TipoViewHolder tipoViewHolder, ArrayList<?> list, int layout) {

        this.list = list;
        this.tipoVH = LISTA;
        if (list != null && list.size() > 0 && list.get(0) instanceof ModeloSQL) {
            tipoVH = MODELO;
        }
        this.layout = layout;
        this.tipoViewHolder = tipoViewHolder;

    }

    public RVAdapter(TipoViewHolder tipoViewHolder, ListaModeloSQL list, int layout) {

        this.listaModeloSQL = list;
        this.list = new ArrayList();
        for (ArrayList<ModeloSQL> modeloSQLArrayList : listaModeloSQL) {
            this.list.add(modeloSQLArrayList.size());
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

        if (tipoVH != null && tipoVH.equals(LISTAMODELO)) {

            holder.bind(listaModeloSQL, position);
        } else if (tipoVH != null && tipoVH.equals(LISTA)) {

            holder.bind(list, position);

        } else if (tipoVH != null && tipoVH.equals(MODELO)) {

            holder.bind((ModeloSQL) list.get(position));
        } else {

            holder.bind(list, position);

        }

    }

    /*
   Añade una lista completa de items
    */
    public void addAll(ArrayList<?> lista) {
        list.addAll(lista);
        notifyDataSetChanged();
    }

    public void addAll(ListaModeloSQL lista) {
        listaModeloSQL.addAll(lista);
        this.list = new ArrayList();
        for (ArrayList<ModeloSQL> modeloSQLArrayList : listaModeloSQL) {
            this.list.add(modeloSQLArrayList.size());
        }
        notifyDataSetChanged();
    }

    public void clearAddAll(ArrayList<?> lista) {
        list.clear();
        list.addAll(lista);
        notifyDataSetChanged();
    }

    public void clearAddAll(ListaModeloSQL lista) {
        listaModeloSQL.clear();
        listaModeloSQL.addAll(lista);
        this.list = new ArrayList();
        for (ArrayList<ModeloSQL> modeloSQLArrayList : listaModeloSQL) {
            this.list.add(modeloSQLArrayList.size());
        }
        notifyDataSetChanged();
    }

    /*
    Permite limpiar todos los elementos del recycler
     */
    public void clear() {

        if (tipoVH != null && tipoVH.equals(LISTAMODELO)) {

            listaModeloSQL.clear();
            list.clear();

        } else {

            list.clear();
        }
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @Override
    public int getItemCount() {

        if (tipoVH != null && tipoVH.equals(LISTAMODELO)) {

            if (listaModeloSQL != null) {
                return listaModeloSQL.size();
            }

        } else if (list != null) {

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