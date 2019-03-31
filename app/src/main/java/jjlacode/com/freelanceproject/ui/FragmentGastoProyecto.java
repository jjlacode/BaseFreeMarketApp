package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.adapter.AdaptadorGasto;
import jjlacode.com.freelanceproject.interfaces.ICFragmentos;
import jjlacode.com.freelanceproject.model.Modelo;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.R;

import static jjlacode.com.freelanceproject.utilities.Common.Constantes.GASTO;

public class FragmentGastoProyecto extends Fragment implements Contract.Tablas {

    private String idProyecto;
    private String namef;

    Button btnVolver;
    TextView nombreProyecto;
    TextView tituloCabecera;
    RecyclerView rvGastos;
    ArrayList<Modelo>listaGastos;
    View vista;
    AdaptadorGasto adaptador;
    Bundle bundle;
    Modelo proyecto;

    private String secuenciaGasto;
    private Activity activity;
    private ICFragmentos icFragments;

    public FragmentGastoProyecto() {
        // Required empty public constructor
    }

    public static FragmentGastoProyecto newInstance() {
        FragmentGastoProyecto fragment = new FragmentGastoProyecto();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vista =inflater.inflate(R.layout.fragment_gasto_proyecto, container, false);

        nombreProyecto = vista.findViewById(R.id.tvtitproyectolgasto);
        btnVolver = vista.findViewById(R.id.btnvolvergastos);
        rvGastos = vista.findViewById(R.id.rvgastos);
        tituloCabecera = vista.findViewById(R.id.tvtitulolgastos);

        bundle = getArguments();

        if (bundle!=null) {
            proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
            idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);
            namef = bundle.getString("namef");
            bundle = new Bundle();
            bundle.putSerializable(TABLA_PROYECTO,proyecto);
            bundle.putString("namefsub", TABLA_GASTO);
            icFragments.enviarBundleAActivity(bundle);
            bundle = null;
        }


        rvGastos.setLayoutManager(new LinearLayoutManager(getContext()));

        tituloCabecera.setText(GASTO.toUpperCase());

        nombreProyecto.setText(proyecto.getString(PROYECTO_NOMBRE));

        String orden = GASTO_SECUENCIA + " ASC";

            listaGastos = QueryDB.queryListDetalle(CAMPOS_GASTO,idProyecto,TABLA_PROYECTO,null,orden);

        adaptador = new AdaptadorGasto(listaGastos);

        rvGastos.setAdapter(adaptador);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Modelo gasto = listaGastos.get(rvGastos.getChildAdapterPosition(v));

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putSerializable(TABLA_GASTO,gasto);
                bundle.putString("namef",namef);
                icFragments.enviarBundleAFragment(bundle,new FragmentUDGastoProyecto());

            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString("namef",namef);
                icFragments.enviarBundleAFragment(bundle,new FragmentUDProyecto());
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
    }

}
