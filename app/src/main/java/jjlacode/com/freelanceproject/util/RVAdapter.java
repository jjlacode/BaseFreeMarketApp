package jjlacode.com.freelanceproject.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>
        implements View.OnClickListener{

    protected ArrayList<Modelo> list;
    private View.OnClickListener listener;
    private String namef;
    private int layout;


    public RVAdapter(ArrayList<Modelo> list, int layout, String namef) {

        this.list = list;
        this.namef = namef;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(layout, null, false);

        view.setOnClickListener(this);


        return new ViewHolder(view);

    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {


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

    public class ViewHolder extends RecyclerView.ViewHolder {



        protected ViewHolder(@NonNull View itemView) {
            super(itemView);



        }

    }

}