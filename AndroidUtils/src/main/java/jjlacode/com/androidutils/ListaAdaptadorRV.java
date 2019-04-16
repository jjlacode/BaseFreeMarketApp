package jjlacode.com.androidutils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class ListaAdaptadorRV extends RecyclerView.Adapter<ListaAdaptadorRV.ViewHolderAdapter> implements View.OnClickListener {

    private ArrayList<Modelo> lista;
    private View.OnClickListener listener;
    private String namef;
    private int r_layout;

    public ListaAdaptadorRV(ArrayList<Modelo> lista, int r_layout, String namef) {

        this.lista = lista;
        this.namef = namef;
        this.r_layout = r_layout;
    }

    @NonNull
    @Override
    public ViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(r_layout, null, false);

        view.setOnClickListener(this);


        return new ViewHolderAdapter(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAdapter viewHolderAdapter, int position) {

        //perfilViewHolder.descpipcionPerfil.setText(listaPerfil.get(position).getDescripcion_tipo_cliente();
        onEntrada(viewHolderAdapter, lista.get(position));
    }

    public abstract void onEntrada(ViewHolderAdapter viewHolderAdapter, Modelo entrada);

    @Override
    public int getItemCount() {

        return lista.size();
    }

    @Override
    public void onClick(View v) {

        if (listener != null) {

            listener.onClick(v);


        }

    }

    class ViewHolderAdapter extends RecyclerView.ViewHolder {


        //TextView descripcionPerfil;

        ViewHolderAdapter(@NonNull View itemView) {
            super(itemView);


            //descripcionPerfil = itemView.findViewById(R.id.tvdescripcionPerfil);


        }
    }
}