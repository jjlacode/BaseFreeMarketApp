package jjlacode.com.freelanceproject.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<BaseViewHolder>
        implements View.OnClickListener{

    protected ArrayList<Modelo> list;
    private View.OnClickListener listener;
    private String namef;
    private int layout;
    private TipoViewHolder tipoViewHolder;

    public RVAdapter(TipoViewHolder tipoViewHolder,ArrayList<Modelo> list, int layout, String namef) {

        this.list = list;
        this.namef = namef;
        this.layout = layout;
        this.tipoViewHolder = tipoViewHolder;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(layout, null, false);

        view.setOnClickListener(this);

        return tipoViewHolder.holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        holder.bind(list.get(position));
    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    @Override
    public void onClick(View v) {

        if (listener != null) {

            listener.onClick(v);

        }

    }



}