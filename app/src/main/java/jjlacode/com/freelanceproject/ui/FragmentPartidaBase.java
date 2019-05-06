package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.FragmentBaseCRUD;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.CommonPry;

public class FragmentPartidaBase extends FragmentBaseCRUD implements ContratoPry.Tablas, CommonPry.Constantes {

    private RecyclerView rvPartidaBase;
    private ArrayList<Modelo> list;
    private ConsultaBD consulta = new ConsultaBD();

    public FragmentPartidaBase() {
        // Required empty public constructor
    }

    @Override
    protected void setTabla() {

        tabla = TABLA_PARTIDABASE;

    }

    @Override
    protected void setTablaCab() {

        tablaCab = null;
    }

    @Override
    protected void setContext() {

        contexto = getContext();
    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_PARTIDABASE;

    }

    @Override
    protected void setCampoID() {
        campoID = PARTIDABASE_ID_PARTIDABASE;
    }

    @Override
    protected void setBundle() {

    }

    @Override
    protected void setDatos() {

        DatabaseReference dbProveedor =
                FirebaseDatabase.getInstance().getReference()
                        .child("productos");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list = consulta.queryList(CAMPOS_PARTIDABASE);

                for (Modelo partidabase : list) {

                    String id = partidabase.getString(PARTIDABASE_ID_PARTIDABASE);

                    if (id!=null) {
                        CommonPry.Calculos.sincronizarPartidaBase(id);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        dbProveedor.addValueEventListener(eventListener);

        rvPartidaBase.setLayoutManager(new LinearLayoutManager(getContext()));

        AdaptadorPartidaBase adapter = new AdaptadorPartidaBase(list, namef);
        rvPartidaBase.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Modelo partidaBase = list.get(rvPartidaBase.getChildAdapterPosition(v));
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PARTIDABASE, partidaBase);
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentUDPartidaBase());

            }
        });


    }

    @Override
    protected void setAcciones() {

    }


    @Override
    protected void setLayout() {

        layout = R.layout.fragment_partidas_base;
    }

    @Override
    protected void setInicio() {

        rvPartidaBase = view.findViewById(R.id.rvpartidasbase);

    }


    public static class AdaptadorPartidaBase extends RecyclerView.Adapter<AdaptadorPartidaBase.PartidaViewHolder>
            implements View.OnClickListener, CommonPry.Constantes, ContratoPry.Tablas {

        ArrayList<Modelo> listaPartidas;
        private View.OnClickListener listener;
        private String namef;

        public AdaptadorPartidaBase(ArrayList<Modelo> listaPartidas, String namef){

            this.listaPartidas=listaPartidas;
            this.namef = namef;
        }

        @NonNull
        @Override
        public PartidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_partidabase,null,false);

            view.setOnClickListener(this);


            return new PartidaViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull PartidaViewHolder partidaViewHolder, int position) {

            partidaViewHolder.descripcionPartida.setText(listaPartidas.get(position).getCampos(PARTIDABASE_DESCRIPCION));
            partidaViewHolder.tiempoPartida.setText(listaPartidas.get(position).getCampos(PARTIDABASE_TIEMPO));
            partidaViewHolder.importePartida.setText(JavaUtil.formatoMonedaLocal(listaPartidas.get(position).getDouble(PARTIDABASE_PRECIO)));

            if (listaPartidas.get(position).getString(PARTIDABASE_RUTAFOTO)!=null){

                partidaViewHolder.imagenPartida.setImageURI(listaPartidas.get(position).getUri(PARTIDABASE_RUTAFOTO));
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

            ImageView imagenPartida;
            TextView descripcionPartida,tiempoPartida,importePartida;

            public PartidaViewHolder(@NonNull View itemView) {
                super(itemView);

                imagenPartida = itemView.findViewById(R.id.imglpartidabase);
                descripcionPartida = itemView.findViewById(R.id.tvdescripcionpartidabase);
                tiempoPartida = itemView.findViewById(R.id.tvtiempopartidabase);
                importePartida = itemView.findViewById(R.id.tvimppartidabase);

            }

        }
    }

}
