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

import jjlacode.com.freelanceproject.adapter.AdaptadorCliente;
import jjlacode.com.freelanceproject.interfaces.ICFragmentos;
import jjlacode.com.freelanceproject.model.Modelo;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.utilidades.Utilidades;

public class FragmentCliente extends Fragment
        implements Common.Constantes, Contract.Tablas, Utilidades.Constantes {

    private RecyclerView rvClientes;
    private ArrayList<Modelo> objListaClientes;

    private String namef;

    private ICFragmentos icFragmentos;
    private Bundle bundle;

    public FragmentCliente() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_cliente, container, false);

        objListaClientes = new ArrayList<>();
        rvClientes = vista.findViewById(R.id.rvCli);

        rvClientes.setLayoutManager(new LinearLayoutManager(getContext()));

        bundle = getArguments();

        if (bundle!=null) {
            namef = bundle.getString("namef");
            bundle = null;
            bundle = new Bundle();
            bundle.putString("namef",namef);
            icFragmentos.enviarBundleAActivity(bundle);
            bundle = null;
        }

        if (namef.equals(PROSPECTO)){

                objListaClientes = QueryDB.queryListCampo
                        (CAMPOS_CLIENTE,CLIENTE_DESCRIPCIONTIPOCLI,PROSPECTO, null);

        }else if (namef.equals(CLIENTE)) {

                objListaClientes = QueryDB.queryList(CAMPOS_CLIENTE,CLIENTE_DESCRIPCIONTIPOCLI,
                        PROSPECTO,null, DIFERENTE,null);
        }

        AdaptadorCliente adapter = new AdaptadorCliente(objListaClientes);

        rvClientes.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idCliente = objListaClientes.get
                        (rvClientes.getChildAdapterPosition(v)).getString(CLIENTE_ID_CLIENTE);

                Modelo cliente = QueryDB.queryObject(CAMPOS_CLIENTE,idCliente);

                System.out.println("idCliente = " + idCliente);
                System.out.println("idCliente = " + cliente.getString(CLIENTE_ID_CLIENTE));

                bundle = new Bundle();
                bundle.putSerializable(TABLA_CLIENTE,cliente);
                bundle.putString("namef",namef);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentUDCliente());
                bundle = null;

            }
        });

        return vista;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            Activity activity = (Activity) context;
            icFragmentos = (ICFragmentos) activity;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
