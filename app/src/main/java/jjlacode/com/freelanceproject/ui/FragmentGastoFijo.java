package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jjlacode.com.androidutils.FragmentBase;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;

public class FragmentGastoFijo extends FragmentBase implements ContratoPry.Tablas {

    private RecyclerView rvGastoFijo;
    private ArrayList<Modelo> list;

    private ConsultaBD consulta = new ConsultaBD();

    public FragmentGastoFijo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_gastofijo, container, false);

        bundle = getArguments();
        if (bundle != null) {

            namef = bundle.getString("namef");
            bundle = null;

        }

        list = consulta.queryList(CAMPOS_GASTOFIJO);

        rvGastoFijo = view.findViewById(R.id.rvlgastofijo);
        rvGastoFijo.setLayoutManager(new LinearLayoutManager(getContext()));

        AdaptadorGastoFijo adapter = new AdaptadorGastoFijo(list, namef);
        rvGastoFijo.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Modelo gastoFijo = list.get(rvGastoFijo.getChildAdapterPosition(v));
                bundle = new Bundle();
                bundle.putSerializable(TABLA_GASTOFIJO, gastoFijo);
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDGastoFijo());

            }
        });

        return view;
    }


    public static class AdaptadorGastoFijo extends RecyclerView.Adapter<AdaptadorGastoFijo.ViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas {

        private ArrayList<Modelo> list;
        private View.OnClickListener listener;
        private String namef;

        public AdaptadorGastoFijo(ArrayList<Modelo> list, String namef) {

            this.list = list;
            this.namef = namef;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_gastofijo, null, false);

            view.setOnClickListener(this);


            return new ViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

            viewHolder.nombre.setText(list.get(position).getString(GASTOFIJO_NOMBRE));
            viewHolder.descripcion.setText(list.get(position).getString(GASTOFIJO_DESCRIPCION));
            viewHolder.cantidad.setText(list.get(position).getString(GASTOFIJO_CANTIDAD));
            viewHolder.importe.setText(JavaUtil.formatoMonedaLocal(list.get(position).getDouble(GASTOFIJO_IMPORTE)));
            viewHolder.anios.setText(list.get(position).getString(GASTOFIJO_ANYOS));
            viewHolder.meses.setText(list.get(position).getString(GASTOFIJO_MESES));
            viewHolder.dias.setText(list.get(position).getString(GASTOFIJO_DIAS));

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

        class ViewHolder extends RecyclerView.ViewHolder {


            TextView nombre,descripcion,cantidad,importe,anios,meses,dias;

            ViewHolder(@NonNull View itemView) {
                super(itemView);


                nombre = itemView.findViewById(R.id.tvnomlgasto);
                descripcion = itemView.findViewById(R.id.tvdesclgasto);
                cantidad = itemView.findViewById(R.id.tvcantlgasto);
                importe = itemView.findViewById(R.id.tvimplgasto);
                anios = itemView.findViewById(R.id.tvanioslgasto);
                meses = itemView.findViewById(R.id.tvmeseslgasto);
                dias = itemView.findViewById(R.id.tvdiaslgasto);



            }
        }
    }
}
