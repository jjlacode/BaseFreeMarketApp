package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import static jjlacode.com.freelanceproject.sqlite.ContratoPry.*;

public class FragmentAmortizacion extends FragmentBase implements Tablas {


    private RecyclerView rvAmortizacion;
    private ArrayList<Modelo> lista;

    ConsultaBD consulta = new ConsultaBD();


    public FragmentAmortizacion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_amortizacion, container, false);

        bundle = getArguments();
        if (bundle != null) {

            namef = bundle.getString("namef");
            bundle = null;

        }

        lista = consulta.queryList(CAMPOS_AMORTIZACION);

        rvAmortizacion = view.findViewById(R.id.rvlamortizacion);
        rvAmortizacion.setLayoutManager(new LinearLayoutManager(getContext()));

        AdaptadorAmortizacion adapter = new AdaptadorAmortizacion(lista, namef);
        rvAmortizacion.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Modelo amortizacion = lista.get(rvAmortizacion.getChildAdapterPosition(v));
                bundle = new Bundle();
                bundle.putSerializable(TABLA_AMORTIZACION, amortizacion);
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDAmortizacion());

            }
        });

        return view;
    }

    public static class AdaptadorAmortizacion extends RecyclerView.Adapter<AdaptadorAmortizacion.ViewHolder>
            implements View.OnClickListener, Tablas , JavaUtil.Constantes{

        private ArrayList<Modelo> list;
        private View.OnClickListener listener;
        private String namef;

        public AdaptadorAmortizacion(ArrayList<Modelo> list, String namef) {

            this.list = list;
            this.namef = namef;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_amortizacion, null, false);

            view.setOnClickListener(this);


            return new ViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

            viewHolder.nombre.setText(list.get(position).getString(AMORTIZACION_NOMBRE));
            viewHolder.descripcion.setText(list.get(position).getString(AMORTIZACION_DESCRIPCION));
            viewHolder.cantidad.setText(list.get(position).getString(AMORTIZACION_CANTIDAD));
            viewHolder.importe.setText(list.get(position).getString(AMORTIZACION_IMPORTE));
            viewHolder.fecha.setText(JavaUtil.getDate(list.get(position).getLong(AMORTIZACION_FECHACOMPRA)));
            viewHolder.anios.setText(list.get(position).getString(AMORTIZACION_ANYOS));
            viewHolder.meses.setText(list.get(position).getString(AMORTIZACION_MESES));
            viewHolder.dias.setText(list.get(position).getString(AMORTIZACION_DIAS));
            if (list.get(position).getString(AMORTIZACION_RUTAFOTO)!=null){

                viewHolder.imagen.setImageURI(list.get(position).getUri(AMORTIZACION_RUTAFOTO));
            }
            if (list.get(position).getLong(AMORTIZACION_FECHACOMPRA)>= JavaUtil.hoy()){

                viewHolder.card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_notok));
            }
            long compra = list.get(position).getLong(AMORTIZACION_FECHACOMPRA);
            long amort = (list.get(position).getLong(AMORTIZACION_DIAS)*DIASLONG)+
                    (list.get(position).getLong(AMORTIZACION_MESES)*MESESLONG)+
                    (list.get(position).getLong(AMORTIZACION_ANYOS)*ANIOSLONG);
            long fechafin = compra + amort;
            long amortizado = JavaUtil.hoy()-compra;
            int res = (int) ((100/(double)(fechafin-compra))*amortizado);
            viewHolder.progressBar.setProgress(res);
            if (res<30){
                viewHolder.card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_notok));
            }else if(res>=30 && res<60){

                viewHolder.card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_acept));
            }else if(res>=60){

                viewHolder.card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_ok));
            }

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


            TextView nombre,descripcion,fecha,cantidad, importe, anios, meses, dias;
            ImageView imagen;
            CardView card;
            ProgressBar progressBar;

            ViewHolder(@NonNull View itemView) {
                super(itemView);


                nombre = itemView.findViewById(R.id.tvnomlamort);
                descripcion = itemView.findViewById(R.id.tvdesclamort);
                cantidad = itemView.findViewById(R.id.tvcantlamort);
                importe = itemView.findViewById(R.id.tvimplamort);
                fecha = itemView.findViewById(R.id.tvfechalamort);
                anios = itemView.findViewById(R.id.tvanioslamort);
                meses = itemView.findViewById(R.id.tvmeseslamort);
                dias = itemView.findViewById(R.id.tvdiaslamort);
                imagen = itemView.findViewById(R.id.imglamort);
                card = itemView.findViewById(R.id.cardlamort);
                progressBar = itemView.findViewById(R.id.progressBarlamort);


            }
        }
    }
}
