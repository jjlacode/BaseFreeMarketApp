package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.FragmentBaseCRUD;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.CommonPry;

import static jjlacode.com.freelanceproject.util.CommonPry.Constantes.PARTIDA;

public class FragmentPartidasProyecto extends FragmentBaseCRUD implements ContratoPry.Tablas {

    RecyclerView rvPartidas;
    ArrayList<Modelo> objListaPartidas;
    TextView nombreProyecto;
    TextView titulo;
    Modelo proyecto;
    private Button btnback;

    private static ConsultaBD consulta = new ConsultaBD();
    public FragmentPartidasProyecto() {
        // Required empty public constructor
    }


    @Override
    protected void setTabla() {

    }

    @Override
    protected void setTablaCab() {

    }

    @Override
    protected void setContext() {

    }

    @Override
    protected void setCampos() {

    }

    @Override
    protected void setCampoID() {

    }

    @Override
    protected void setBundle() {

        if (bundle!=null) {
            proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
            namef = bundle.getString(NAMEF);
            bundle = null;
            bundle = new Bundle();
            bundle.putSerializable(TABLA_PROYECTO,proyecto);
            bundle.putString(NAMEF,namef);
            bundle.putString(NAMESUB, PARTIDA);
            icFragmentos.enviarBundleAActivity(bundle);

        }

    }

    @Override
    protected void setDatos() {

        nombreProyecto.setText(proyecto.getString(PROYECTO_NOMBRE));

        //objListaPartidas = consulta.queryListDetalle
        //        (CAMPOS_PARTIDA,proyecto.getString(PROYECTO_ID_PROYECTO),TABLA_PROYECTO);

        objListaPartidas = consulta.queryList(CAMPOS_PARTIDA,PARTIDA_ID_PROYECTO,
                proyecto.getString(PROYECTO_ID_PROYECTO),null,IGUAL,null);

        AdaptadorPartida adaptadorPartida = new AdaptadorPartida(objListaPartidas,namef);

        rvPartidas.setAdapter(adaptadorPartida);

        adaptadorPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Modelo partida = objListaPartidas.get(rvPartidas.getChildAdapterPosition(v));

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putSerializable(TABLA_PARTIDA,partida);
                bundle.putString(NAMEF,namef);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCUDPartidaProyecto());

            }
        });

    }

    @Override
    protected void setAcciones() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_partidas_proyecto, container, false);
        // Inflate the layout for this fragment
        nombreProyecto = view.findViewById(R.id.tvtitproyectopartida);
        titulo = view.findViewById(R.id.tvtitpartidas);
        btnback = view.findViewById(R.id.btnvolverpartidas);
        rvPartidas = view.findViewById(R.id.rvpartidas);
        rvPartidas.setLayoutManager(new LinearLayoutManager(getContext()));

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString(NAMEF,namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDProyecto());

            }
        });

        return view;
    }

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

    }

    public static class AdaptadorPartida extends RecyclerView.Adapter<AdaptadorPartida.PartidaViewHolder>
            implements View.OnClickListener, CommonPry.Constantes, ContratoPry.Tablas {

        ArrayList<Modelo> listaPartidas;
        private View.OnClickListener listener;
        private String namef;

        public AdaptadorPartida(ArrayList<Modelo> listaPartidas, String namef){

            this.listaPartidas=listaPartidas;
            this.namef = namef;
        }

        @NonNull
        @Override
        public PartidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_partida,null,false);

            view.setOnClickListener(this);


            return new PartidaViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull PartidaViewHolder partidaViewHolder, int position) {

            partidaViewHolder.descripcionPartida.setText(listaPartidas.get(position).getCampos(PARTIDA_DESCRIPCION));
            partidaViewHolder.tiempoPartida.setText(listaPartidas.get(position).getCampos(PARTIDA_TIEMPO));
            partidaViewHolder.cantidadPartida.setText(listaPartidas.get(position).getCampos(PARTIDA_CANTIDAD));
            partidaViewHolder.importePartida.setText(JavaUtil.formatoMonedaLocal(listaPartidas.get(position).getDouble(PARTIDA_PRECIO)));
            partidaViewHolder.completadaPartida.setText(listaPartidas.get(position).getCampos(PARTIDA_COMPLETADA));
            partidaViewHolder.progressBarPartida.setProgress(Integer.parseInt(listaPartidas.get(position).getCampos(PARTIDA_COMPLETADA)));

            if (listaPartidas.get(position).getString(PARTIDA_RUTAFOTO)!=null){

                partidaViewHolder.imagenPartida.setImageURI(listaPartidas.get(position).getUri(PARTIDA_RUTAFOTO));
            }

                long retraso = listaPartidas.get(position).getLong(PARTIDA_PROYECTO_RETRASO);
                if (retraso > 3 * DIASLONG) {
                    partidaViewHolder.imagenret.setImageResource(R.drawable.alert_box_r);
                } else if (retraso > DIASLONG) {
                    partidaViewHolder.imagenret.setImageResource(R.drawable.alert_box_a);
                } else {
                    partidaViewHolder.imagenret.setImageResource(R.drawable.alert_box_v);
                }

            if (namef.equals(PRESUPUESTO)){

                partidaViewHolder.progressBarPartida.setVisibility(View.GONE);

            }else{

                partidaViewHolder.progressBarPartida.setVisibility(View.VISIBLE);
            }
            System.out.println("listaPartidas: "+ listaPartidas.size() + " registros");

        }

        @Override
        public int getItemCount() {
            return listaPartidas.size();
        }

        @Override
        public void onClick(View v) {

            if (listener!= null){

                listener.onClick(v);


            }


        }

        public class PartidaViewHolder extends RecyclerView.ViewHolder{

            ImageView imagenPartida,imagenret;
            TextView descripcionPartida,tiempoPartida,cantidadPartida,completadaPartida,importePartida;
            ProgressBar progressBarPartida;

            public PartidaViewHolder(@NonNull View itemView) {
                super(itemView);

                imagenPartida = itemView.findViewById(R.id.imglpartida);
                imagenret = itemView.findViewById(R.id.imgretlpartida);
                descripcionPartida = itemView.findViewById(R.id.tvdescripcionpartida);
                tiempoPartida = itemView.findViewById(R.id.tvtiempopartida);
                cantidadPartida = itemView.findViewById(R.id.tvcantidadpartida);
                importePartida = itemView.findViewById(R.id.tvimppartida);
                completadaPartida = itemView.findViewById(R.id.tvcompletadapartida);
                progressBarPartida = itemView.findViewById(R.id.progressBarpartida);

            }

        }
    }
}
