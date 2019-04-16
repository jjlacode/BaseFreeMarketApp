package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import jjlacode.com.androidutils.AppActivity;
import jjlacode.com.androidutils.FragmentBase;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.utilities.CommonPry;

public class FragmentPerfil extends FragmentBase implements CommonPry.Constantes, ContratoPry.Tablas {

    RecyclerView rvPerfil;
    ArrayList<Modelo> lista;

    private ConsultaBD consulta = new ConsultaBD();

    public FragmentPerfil() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        bundle = getArguments();
        if (bundle != null) {

            namef = bundle.getString("namef");
            bundle = null;
            bundle = new Bundle();
            bundle.putString("namef",namef);
            icFragmentos.enviarBundleAActivity(bundle);
            bundle = null;

        }

            lista = consulta.queryList(CAMPOS_PERFIL,null, null);

        rvPerfil = view.findViewById(R.id.rvPerfil);
        rvPerfil.setLayoutManager(new LinearLayoutManager(getContext()));

        AdaptadorPerfil adapter = new AdaptadorPerfil(lista, namef);
        rvPerfil.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Modelo perfil = lista.get(rvPerfil.getChildAdapterPosition(v));

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PERFIL, perfil);
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDPerfil());

            }
        });

        return view;
    }

    public static class AdaptadorPerfil extends RecyclerView.Adapter<AdaptadorPerfil.ViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas {

        private ArrayList<Modelo> lista;
        private View.OnClickListener listener;
        private String namef;

        public AdaptadorPerfil(ArrayList<Modelo> lista, String namef) {

            this.lista = lista;
            this.namef = namef;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_perfil, null, false);

            view.setOnClickListener(this);


            return new ViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

            viewHolder.nombre.setText(lista.get(position).getString(PERFIL_NOMBRE));
            viewHolder.descripcion.setText(lista.get(position).getString(PERFIL_DESCRIPCION));

            if (lista.get(position).getString(PERFIL_NOMBRE).equals(CommonPry.perfila)){

                viewHolder.card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_ok));
            }

        }

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

        class ViewHolder extends RecyclerView.ViewHolder {


            TextView nombre,descripcion;
            CardView card;

            ViewHolder(@NonNull View itemView) {
                super(itemView);


                nombre = itemView.findViewById(R.id.tvnomlperfil);
                descripcion = itemView.findViewById(R.id.tvdesclperfil);
                card = itemView.findViewById(R.id.cardlperfil);


            }
        }
    }
}
