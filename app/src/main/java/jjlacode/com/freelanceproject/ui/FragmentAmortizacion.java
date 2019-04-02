package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jjlacode.com.androidutils.ICFragmentos;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.adapter.AdaptadorAmortizacion;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;

public class FragmentAmortizacion extends Fragment implements Contract.Tablas {

    private String idAmortizacion;
    private String namef;

    private RecyclerView rvAmortizacion;
    private AppCompatActivity activity;
    private ICFragmentos icFragmentos;
    private ArrayList<Modelo> lista;
    private Bundle bundle;

    public FragmentAmortizacion() {
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

        View vista = inflater.inflate(R.layout.fragment_amortizacion, container, false);

        bundle = getArguments();
        if (bundle != null) {

            namef = bundle.getString("namef");
            bundle = null;

        }

            lista = QueryDB.queryList(CAMPOS_AMORTIZACION,null,null);

        rvAmortizacion = vista.findViewById(R.id.rvAmortizacion);
        rvAmortizacion.setLayoutManager(new LinearLayoutManager(getContext()));

        AdaptadorAmortizacion adapter = new AdaptadorAmortizacion(lista, namef);
        rvAmortizacion.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idAmortizacion = lista.get(rvAmortizacion.getChildAdapterPosition(v)).getCampos(AMORTIZACION_ID_AMORTIZACION);

                Modelo amortizacion = QueryDB.queryObject(CAMPOS_AMORTIZACION,idAmortizacion);

                bundle = new Bundle();
                bundle.putSerializable(TABLA_AMORTIZACION, amortizacion);
                bundle.putString("namef", namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentUDAmortizacion());

            }
        });

        //TODO falta configurar consulta a tabla amortizacion
        return vista;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (AppCompatActivity) context;
            icFragmentos = (ICFragmentos) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
