package jjlacode.com.freelanceproject.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import jjlacode.com.freelanceproject.utilities.CommonPry;

public class FragmentProyecto extends FragmentBase implements CommonPry.Constantes, ContratoPry.Tablas {

    private String idProyecto;
    private RecyclerView rvproyectos;
    private ArrayList<Modelo> listaProyectos;

    private ConsultaBD consulta = new ConsultaBD();

    public FragmentProyecto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_proyecto, container, false);

        bundle = getArguments();
        if (bundle!=null) {

            namef = bundle.getString("namef");
            bundle = null;
            bundle = new Bundle();
            bundle.putString("namef",namef);
            icFragmentos.enviarBundleAActivity(bundle);
            bundle = null;

        }

        new CommonPry.Calculos.TareaActualizarProys().execute();

        ArrayList<Modelo> lista = consulta.queryList
                (CAMPOS_PROYECTO,PROYECTO_ID_PROYECTO,PARTIDABASE,null, DIFERENTE,null);

        if (lista!=null && lista.size()>0) {

            listaProyectos = new ArrayList<>();

            switch (namef) {

                case PRESUPUESTO:
                    for (Modelo item : lista) {

                        int estado = item.getInt(PROYECTO_TIPOESTADO);
                        if (estado >= 1 && estado <= 3) {
                            listaProyectos.add(item);
                        }

                    }
                    break;
                case PROYECTO:
                    for (Modelo item : lista) {

                        int estado = item.getInt(PROYECTO_TIPOESTADO);
                        if (estado > 3 && estado <= 6) {
                            listaProyectos.add(item);
                        }

                    }
                    break;
                case COBROS:
                    for (Modelo item : lista) {

                        int estado = item.getInt(PROYECTO_TIPOESTADO);
                        if (estado == 7) {
                            listaProyectos.add(item);
                        }

                    }
                    break;
                case HISTORICO:
                    for (Modelo item : lista) {

                        int estado = item.getInt(PROYECTO_TIPOESTADO);
                        if (estado == 8 || estado == 0) {
                            listaProyectos.add(item);
                        }

                    }
                    break;
            }

        }

        rvproyectos = view.findViewById(R.id.rvproyectos);
        rvproyectos.setLayoutManager(new LinearLayoutManager(getContext()));

        AdaptadorProyecto adapter = new AdaptadorProyecto(listaProyectos, namef);
        rvproyectos.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idProyecto = listaProyectos.get(rvproyectos.getChildAdapterPosition(v)).getString(PROYECTO_ID_PROYECTO);

                Modelo proyecto = consulta.queryObject(CAMPOS_PROYECTO,idProyecto);

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString("namef",namef);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentUDProyecto());

            }
        });

        return view;
    }

    public static class AdaptadorProyecto extends RecyclerView.Adapter<AdaptadorProyecto.ProyectoViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas {


        private ArrayList<Modelo> listaProyecto;
        private String namef;

        private View.OnClickListener listener;

        public AdaptadorProyecto(ArrayList<Modelo> listaProyecto, String namef) {
            this.listaProyecto = listaProyecto;
            this.namef = namef;
        }

        @NonNull
        @Override
        public ProyectoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_proyecto,null,false);

            view.setOnClickListener(this);

            return new ProyectoViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull ProyectoViewHolder proyectoViewHolder, int position) {


            proyectoViewHolder.nombreProyecto.setText(listaProyecto.get(position).getCampos(PROYECTO_NOMBRE));
            proyectoViewHolder.descripcionProyecto.setText(listaProyecto.get(position).getCampos(PROYECTO_DESCRIPCION));
            proyectoViewHolder.clienteProyecto.setText(listaProyecto.get(position).getCampos(PROYECTO_CLIENTE_NOMBRE));
            proyectoViewHolder.estadoProyecto.setText(listaProyecto.get(position).getCampos(PROYECTO_DESCRIPCION_ESTADO));
            proyectoViewHolder.importe.setText(JavaUtil.formatoMonedaLocal(listaProyecto.get(position).getDouble(PROYECTO_IMPORTEPRESUPUESTO)));

            if (namef.equals(PROYECTO)){

                proyectoViewHolder.progressBarProyecto.setProgress(listaProyecto.get(position).getInt(PROYECTO_TOTCOMPLETADO));

                long retraso = listaProyecto.get(position).getLong(PROYECTO_RETRASO);
                if (retraso > 3 * DIASLONG){proyectoViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_r);}
                else if (retraso > DIASLONG){proyectoViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_a);}
                else {proyectoViewHolder.imagenEstado.setImageResource(R.drawable.alert_box_v);}

            }else{

                proyectoViewHolder.progressBarProyecto.setVisibility(View.GONE);
                proyectoViewHolder.imagenEstado.setVisibility(View.GONE);

            }
            System.out.println("ruta foto pry = " + listaProyecto.get(position).getCampos(PROYECTO_RUTAFOTO));
            if (listaProyecto.get(position).getCampos(PROYECTO_RUTAFOTO)!=null) {
                proyectoViewHolder.imagenProyecto.setImageURI(Uri.parse(listaProyecto.get(position).getCampos(PROYECTO_RUTAFOTO)));
            }
            int peso = Integer.parseInt(listaProyecto.get(position).getCampos(PROYECTO_CLIENTE_PESOTIPOCLI));

            if (peso>6){proyectoViewHolder.imagenCliente.setImageResource(R.drawable.clientev);}
            else if (peso>3){proyectoViewHolder.imagenCliente.setImageResource(R.drawable.clientea);}
            else if (peso>0){proyectoViewHolder.imagenCliente.setImageResource(R.drawable.clienter);}
            else {proyectoViewHolder.imagenCliente.setImageResource(R.drawable.cliente);}



        }

        @Override
        public int getItemCount() {
            if (listaProyecto==null){
                return 0;
            }
            return listaProyecto.size();
        }

        @Override
        public void onClick(View v) {

            if (listener!= null){

                listener.onClick(v);


            }

        }

        public class ProyectoViewHolder extends RecyclerView.ViewHolder {

            ImageView imagenProyecto, imagenEstado, imagenCliente;
            TextView nombreProyecto,descripcionProyecto,clienteProyecto, estadoProyecto,
                    importe;
            ProgressBar progressBarProyecto;

            public ProyectoViewHolder(@NonNull View itemView) {
                super(itemView);

                imagenProyecto = itemView.findViewById(R.id.imglistaproyectos);
                imagenCliente = itemView.findViewById(R.id.imgclientelistaproyectos);
                imagenEstado = itemView.findViewById(R.id.imgestadolistaproyectos);
                nombreProyecto = itemView.findViewById(R.id.tvnombrelistaproyectos);
                descripcionProyecto = itemView.findViewById(R.id.tvdesclistaproyectos);
                clienteProyecto = itemView.findViewById(R.id.tvnombreclientelistaproyectos);
                estadoProyecto = itemView.findViewById(R.id.tvestadolistaproyectos);
                progressBarProyecto = itemView.findViewById(R.id.progressBarlistaproyectos);
                importe = itemView.findViewById(R.id.tvimptotlproyectos);

            }
        }
    }
}
