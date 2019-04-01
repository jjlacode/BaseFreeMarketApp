package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import jjlacode.com.androidutils.ICFragmentos;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.adapter.AdaptadorPartida;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.QueryDB;

import static jjlacode.com.freelanceproject.utilities.Common.Constantes.PARTIDA;

public class FragmentPartidasProyecto extends Fragment implements Contract.Tablas {

    private String namef;

    View vista;
    RecyclerView rvPartidas;
    ArrayList<Modelo> objListaPartidas;
    Activity activity;
    ICFragmentos icFragments;
    TextView nombreProyecto;
    TextView titulo;
    Button btnVolver;
    Modelo proyecto;
    Bundle bundle;

    public FragmentPartidasProyecto() {
        // Required empty public constructor
    }

    public static FragmentPartidasProyecto newInstance() {
        FragmentPartidasProyecto fragment = new FragmentPartidasProyecto();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_partidas_proyecto, container, false);
        // Inflate the layout for this fragment
        nombreProyecto = vista.findViewById(R.id.tvtitproyectopartida);
        titulo = vista.findViewById(R.id.tvtitpartidas);
        btnVolver = vista.findViewById(R.id.btnvolverpartidas);
        rvPartidas = vista.findViewById(R.id.rvpartidas);
        rvPartidas.setLayoutManager(new LinearLayoutManager(getContext()));

        bundle = getArguments();

        if (bundle!=null) {
            proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
            namef = bundle.getString("namef");
            bundle = null;
            bundle = new Bundle();
            bundle.putSerializable(TABLA_PROYECTO,proyecto);
            bundle.putString("namefsub", PARTIDA);
            icFragments.enviarBundleAActivity(bundle);
        }

            nombreProyecto.setText(proyecto.getString(PROYECTO_NOMBRE));

            objListaPartidas = QueryDB.queryListDetalle
                    (CAMPOS_PARTIDA,proyecto.getString(PROYECTO_ID_PROYECTO),TABLA_PROYECTO);

        AdaptadorPartida adaptadorPartida = new AdaptadorPartida(objListaPartidas,namef);

        rvPartidas.setAdapter(adaptadorPartida);

        adaptadorPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String secuenciapartida = objListaPartidas.get
                        (rvPartidas.getChildAdapterPosition(v)).getString(PARTIDA_SECUENCIA);
                //Modelo partida = QueryDB.queryObjectDetalle
                //        (CAMPOS_PARTIDA,proyecto.getString(PROYECTO_ID_PROYECTO),secuenciapartida);

                Modelo partida = objListaPartidas.get(rvPartidas.getChildAdapterPosition(v));

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putSerializable(TABLA_PARTIDA,partida);
                bundle.putString("namef",namef);
                icFragments.enviarBundleAFragment(bundle,new FragmentUDPartidaProyecto());

            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString("namef",namef);
                icFragments.enviarBundleAFragment(bundle, new FragmentUDProyecto());

            }
        });

        return vista;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            this.activity = (Activity) context;
            icFragments = (ICFragmentos) this.activity;
        }
    }

    @Override
    public void onDetach() {

        super.onDetach();
        icFragments = null;
    }

}
