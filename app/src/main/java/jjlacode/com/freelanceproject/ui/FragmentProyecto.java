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

import java.util.ArrayList;

import jjlacode.com.androidutils.ICFragmentos;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.adapter.AdaptadorProyecto;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;
import jjlacode.com.freelanceproject.R;

public class FragmentProyecto extends Fragment implements Common.Constantes, Contract.Tablas {

    private String idProyecto;
    private String namef;

    View vista;
    RecyclerView rvproyectos;
    Activity activity;
    ICFragmentos icFragmentos;
    ArrayList<Modelo> listaProyectos;
    Bundle bundle;

    public FragmentProyecto() {
        // Required empty public constructor
    }

    public static FragmentProyecto newInstance() {
        FragmentProyecto fragment = new FragmentProyecto();
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

        vista = inflater.inflate(R.layout.fragment_proyecto, container, false);

        bundle = getArguments();
        if (bundle!=null) {

            namef = bundle.getString("namef");
            bundle = null;
            bundle = new Bundle();
            bundle.putString("namef",namef);
            icFragmentos.enviarBundleAActivity(bundle);
            bundle = null;

        }

        ArrayList<Modelo> lista = QueryDB.queryList(CAMPOS_PROYECTO,null, null);

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

        rvproyectos = vista.findViewById(R.id.rvproyectos);
        rvproyectos.setLayoutManager(new LinearLayoutManager(getContext()));

        AdaptadorProyecto adapter = new AdaptadorProyecto(listaProyectos, namef);
        rvproyectos.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idProyecto = listaProyectos.get(rvproyectos.getChildAdapterPosition(v)).getString(PROYECTO_ID_PROYECTO);

                Modelo proyecto = QueryDB.queryObject(CAMPOS_PROYECTO,idProyecto);

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString("namef",namef);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentUDProyecto());

            }
        });

        return vista;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            this.activity = (Activity) context;
            icFragmentos = (ICFragmentos) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
