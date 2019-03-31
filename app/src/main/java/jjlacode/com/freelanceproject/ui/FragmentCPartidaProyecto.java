package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.adapter.AdaptadorTareas;
import jjlacode.com.freelanceproject.interfaces.ICFragmentos;
import jjlacode.com.freelanceproject.model.Modelo;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;
import jjlacode.com.freelanceproject.R;


public class FragmentCPartidaProyecto extends Fragment implements Common.Constantes, Contract.Tablas {

    private String idProyecto;
    private String namef;

    View vista;
    int secuencia;
    EditText descripcionPartida;
    EditText tiempoPartida;
    EditText cantidadPartida;
    Button btnSavePartida;
    Button btnVolver;
    Button btnNuevaTarea;
    RecyclerView rvTareas;
    ArrayList<Modelo> listaTareas;
    String idTarea;
    Context context = Common.AppActivity.getAppContext();
    AdaptadorTareas adapter;

    private TextView titulo;
    private TextView nombreProyecto;
    private Bundle bundle;
    private Modelo proyecto;
    private Activity activity;
    private ICFragmentos icFragments;


    public FragmentCPartidaProyecto() {
        // Required empty public constructor
    }


    public static FragmentCPartidaProyecto newInstance() {
        FragmentCPartidaProyecto fragment = new FragmentCPartidaProyecto();
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
        vista = inflater.inflate(R.layout.fragment_c_partida_proyecto, container, false);

        descripcionPartida = vista.findViewById(R.id.etdescripcionNpartida);
        tiempoPartida = vista.findViewById(R.id.ettiempoNpartida);
        cantidadPartida = vista.findViewById(R.id.etcantidadNpartida);
        btnSavePartida = vista.findViewById(R.id.btnsaveNpartida);
        btnVolver = vista.findViewById(R.id.btnvolverNpartida);
        rvTareas = vista.findViewById(R.id.rvtareasnpartida);
        btnNuevaTarea = vista.findViewById(R.id.btnnuevatareanpartida);
        titulo = vista.findViewById(R.id.tvtitnpartida);
        nombreProyecto = vista.findViewById(R.id.tvnomprynpartida);

        bundle = getArguments();

        if (bundle!=null){

            namef = bundle.getString("namef");
            proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
            idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);
        }

        if (namef == PROYECTO){

            titulo.setText(getString(R.string.partida_proyecto));

        }else if (namef == PRESUPUESTO){

            titulo.setText(getString(R.string.partida_presupuesto));
        }

        nombreProyecto.setText(proyecto.getString(PROYECTO_NOMBRE));
        rvTareas.setLayoutManager(new LinearLayoutManager(getContext()));

            listaTareas = QueryDB.queryList(CAMPOS_TAREA,null,null);

        idTarea = listaTareas.get(0).getString(TAREA_ID_TAREA);

        adapter = new AdaptadorTareas(listaTareas);

        rvTareas.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (rvTareas.getChildAdapterPosition(v) > 0) {


                    descripcionPartida.setText(listaTareas.get(rvTareas.getChildAdapterPosition(v)).getString(TAREA_DESCRIPCION));
                    tiempoPartida.setText(listaTareas.get(rvTareas.getChildAdapterPosition(v)).getString(TAREA_TIEMPO));
                    idTarea = (listaTareas.get(rvTareas.getChildAdapterPosition(v)).getString(TAREA_ID_TAREA));

                }else if (rvTareas.getChildAdapterPosition(v) == 0) {

                    descripcionPartida.setText(null);
                    tiempoPartida.setText(null);
                    idTarea = listaTareas.get(0).getString(TAREA_ID_TAREA);
                }

            }
        });

        btnSavePartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (registrarPartida()) {

                    new Common.Calculos.Tareafechas().execute();

                    bundle = new Bundle();
                    bundle.putSerializable(TABLA_PROYECTO, proyecto);
                    bundle.putString("namef", namef);
                    icFragments.enviarBundleAFragment(bundle, new FragmentPartidasProyecto());
                    bundle = null;

                }else{

                    Toast.makeText(context, "Faltan datos obligatorios", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString("namef",namef);
                icFragments.enviarBundleAFragment(bundle, new FragmentPartidasProyecto());
                bundle = null;
            }
        });
        btnNuevaTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (descripcionPartida.getText()!=null && tiempoPartida.getText()!=null) {
                    int cont = 0;
                    for (Modelo tarea : listaTareas) {
                        if (tarea.getString(TAREA_DESCRIPCION).equals(descripcionPartida.getText().toString())) {
                            Toast.makeText(context, "La tarea ya existe", Toast.LENGTH_SHORT).show();
                            cont++;
                        }
                    }
                    if (cont == 0) {

                        try {
                            ContentValues valores = new ContentValues();
                            QueryDB.putDato(valores,CAMPOS_TAREA,TAREA_DESCRIPCION,descripcionPartida.getText().toString());
                            QueryDB.putDato(valores,CAMPOS_TAREA,TAREA_TIEMPO,tiempoPartida.getText().toString());

                            idTarea = QueryDB.idInsertRegistro(TABLA_TAREA,valores);

                            recargaRv();

                            Toast.makeText(getContext(), "Nueva tarea guardada", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error al guardar tarea", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });

        return vista;
    }

    private  void recargaRv(){

            listaTareas = QueryDB.queryList(CAMPOS_TAREA,null,null);

        adapter = new AdaptadorTareas(listaTareas);
        rvTareas.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (rvTareas.getChildAdapterPosition(v) > 0) {


                    descripcionPartida.setText(listaTareas.get(rvTareas.getChildAdapterPosition(v)).getString(TAREA_DESCRIPCION));
                    tiempoPartida.setText(listaTareas.get(rvTareas.getChildAdapterPosition(v)).getString(TAREA_TIEMPO));
                    idTarea = (listaTareas.get(rvTareas.getChildAdapterPosition(v)).getString(TAREA_ID_TAREA));

                }else if (rvTareas.getChildAdapterPosition(v) == 0) {

                    descripcionPartida.setText(null);
                    tiempoPartida.setText(null);
                    idTarea = listaTareas.get(0).getString(TAREA_ID_TAREA);
                }

            }
        });


    }

    private boolean registrarPartida() {


            try {

                ContentValues valores = new ContentValues();

                QueryDB.putDato(valores,CAMPOS_PARTIDA,PARTIDA_DESCRIPCION,descripcionPartida.getText().toString());
                QueryDB.putDato(valores,CAMPOS_PARTIDA,PARTIDA_TIEMPO,tiempoPartida.getText().toString());
                QueryDB.putDato(valores,CAMPOS_PARTIDA,PARTIDA_CANTIDAD,cantidadPartida.getText().toString());
                QueryDB.putDato(valores,CAMPOS_PARTIDA,PARTIDA_ID_TAREA,idTarea);
                QueryDB.putDato(valores,CAMPOS_PARTIDA,PARTIDA_ID_ESTADO,proyecto.getString(PROYECTO_ID_ESTADO));
                QueryDB.putDato(valores,CAMPOS_PARTIDA,PARTIDA_ID_PROYECTO,idProyecto);

                QueryDB.insertRegistroDetalle(CAMPOS_PARTIDA,idProyecto,TABLA_PROYECTO,valores);
                Toast.makeText(getContext(), "Nueva partida guardada", Toast.LENGTH_SHORT).show();

                return true;

            }catch (Exception e){Toast.makeText(getContext(), "Error al guardar partida", Toast.LENGTH_SHORT).show();}

        return  false;
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
