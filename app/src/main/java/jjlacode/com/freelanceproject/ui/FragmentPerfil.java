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

import java.util.ArrayList;

import jjlacode.com.freelanceproject.adapter.AdaptadorPerfil;
import jjlacode.com.freelanceproject.interfaces.ICFragmentos;
import jjlacode.com.freelanceproject.model.Modelo;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;
import jjlacode.com.freelanceproject.R;

public class FragmentPerfil extends Fragment implements Common.Constantes, Contract.Tablas {

    private String idPerfil;
    private String namef;

    View vista;
    RecyclerView rvPerfil;
    Activity activity;
    ICFragmentos icFragmentos;
    ArrayList<Modelo> lista;
    Bundle bundle;

    public FragmentPerfil() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vista = inflater.inflate(R.layout.fragment_perfil, container, false);

        bundle = getArguments();
        if (bundle != null) {

            namef = bundle.getString("namef");
            bundle = null;

        }

            lista = QueryDB.queryList(CAMPOS_PERFIL,null, null);

        rvPerfil = vista.findViewById(R.id.rvPerfil);
        rvPerfil.setLayoutManager(new LinearLayoutManager(getContext()));

        AdaptadorPerfil adapter = new AdaptadorPerfil(lista, namef);
        rvPerfil.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idPerfil = lista.get(rvPerfil.getChildAdapterPosition(v)).getString(PERFIL_ID_PERFIL);

                Modelo perfil =  QueryDB.queryObject(CAMPOS_PERFIL,idPerfil);

                bundle = new Bundle();
                bundle.putSerializable(PERFIL, perfil);
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentUDPerfil());

            }
        });

        return vista;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            icFragmentos = (ICFragmentos) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
