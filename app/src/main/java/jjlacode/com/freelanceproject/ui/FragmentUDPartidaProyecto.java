package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jjlacode.com.androidutils.AppActivity;
import jjlacode.com.androidutils.ICFragmentos;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.adapter.AdaptadorTareas;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;

public class FragmentUDPartidaProyecto extends Fragment implements Common.Constantes, Contract.Tablas {

    private String namef;

    View vista;
    Long retraso;
    EditText descripcionPartida;
    EditText tiempoPartida;
    EditText cantidadPartida;
    EditText completadaPartida;
    Button btnSavePartida;
    Button btnDelPartida;
    Button btnVolverPartida;
    Button btnNuevaTarea;
    RecyclerView rvTareas;
    ProgressBar progressBarPartida;
    ImageView imagenPartida;
    TextView labelCompletada;
    ArrayList<Modelo> listaTareas;
    String idTarea;
    Context context = AppActivity.getAppContext();
    AdaptadorTareas adapter;

    //ContentResolver resolver = context.getContentResolver();
    private Bundle bundle;
    private Modelo proyecto;
    private ICFragmentos icFragments;
    private Modelo partida;
    private AppCompatActivity activity;

    public FragmentUDPartidaProyecto() {
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
        vista = inflater.inflate(R.layout.fragment_ud_partida_proyecto, container, false);

        descripcionPartida = vista.findViewById(R.id.etdescripcionUDpartida);
        tiempoPartida = vista.findViewById(R.id.ettiempoUDpartida);
        cantidadPartida = vista.findViewById(R.id.etcantidadUDpartida);
        completadaPartida = vista.findViewById(R.id.etcompletadaUDpartida);
        btnSavePartida = vista.findViewById(R.id.btnsaveUDpartida);
        btnDelPartida = vista.findViewById(R.id.btndelUDpartida);
        btnVolverPartida = vista.findViewById(R.id.btnvolverUDpartida);
        btnNuevaTarea = vista.findViewById(R.id.btnnuevatareaUDpartida);
        progressBarPartida = vista.findViewById(R.id.progressBarUDpartida);
        imagenPartida = vista.findViewById(R.id.imgUDpartida);
        labelCompletada = vista.findViewById(R.id.lcompletadaUDpartida);
        rvTareas = vista.findViewById(R.id.rvtareasUDpartida);

        bundle = getArguments();

        if (bundle!=null) {
            proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
            partida = (Modelo) bundle.getSerializable(TABLA_PARTIDA);
            namef = bundle.getString("namef");
            bundle = null;
            bundle = new Bundle();
            bundle.putSerializable(TABLA_PROYECTO,proyecto);
            bundle.putString("namefsub", TABLA_PARTIDA);
            icFragments.enviarBundleAActivity(bundle);
            bundle = null;
        }

        if (namef.equals(PRESUPUESTO)){

            progressBarPartida.setVisibility(View.GONE);
            completadaPartida.setVisibility(View.GONE);
            labelCompletada.setVisibility(View.GONE);
        }else{

            progressBarPartida.setVisibility(View.VISIBLE);
            completadaPartida.setVisibility(View.VISIBLE);
            labelCompletada.setVisibility(View.VISIBLE);
        }

            descripcionPartida.setText(partida.getString(PARTIDA_DESCRIPCION));
            tiempoPartida.setText(partida.getString(PARTIDA_TIEMPO));
            cantidadPartida.setText(partida.getString(PARTIDA_CANTIDAD));
            completadaPartida.setText(partida.getString(PARTIDA_COMPLETADA));
            progressBarPartida.setProgress(partida.getInt(PARTIDA_COMPLETADA));
            retraso = partida.getLong(PARTIDA_PROYECTO_RETRASO);
            if (retraso<1){imagenPartida.setImageResource(R.drawable.alert_box_v);}
            else if (retraso>=1 && retraso<3){imagenPartida.setImageResource(R.drawable.alert_box_a);}
            else {imagenPartida.setImageResource(R.drawable.alert_box_r);}

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

                actualizarPartida();

                cambiarFragment();

            }
        });

        btnDelPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BorrarPartida();

                cambiarFragment();

            }
        });

        btnVolverPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cambiarFragment();

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

    private void cambiarFragment() {

        if (namef.equals(AGENDA)){

            bundle = new Bundle();
            bundle.putString("namef",namef);
            icFragments.enviarBundleAFragment(bundle,new FragmentAgenda());
            bundle = null;

        }else {

            bundle = new Bundle();
            bundle.putSerializable(TABLA_PROYECTO,proyecto);
            bundle.putString("namef",namef);
            icFragments.enviarBundleAFragment(bundle,new FragmentPartidasProyecto());
        }
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

    private void BorrarPartida() {


        QueryDB.deleteRegistroDetalle
                (TABLA_PARTIDA,partida.getString(PARTIDA_ID_PROYECTO),partida.getString(PARTIDA_SECUENCIA));

        new Common.Calculos.Tareafechas().execute();
    }

    private void actualizarPartida() {

        ContentValues valores = new ContentValues();

        QueryDB.putDato(valores,CAMPOS_PARTIDA,PARTIDA_DESCRIPCION,descripcionPartida.getText().toString());
        QueryDB.putDato(valores,CAMPOS_PARTIDA,PARTIDA_TIEMPO,tiempoPartida.getText().toString());
        QueryDB.putDato(valores,CAMPOS_PARTIDA,PARTIDA_CANTIDAD,cantidadPartida.getText().toString());
        QueryDB.putDato(valores,CAMPOS_PARTIDA,PARTIDA_COMPLETADA,completadaPartida.getText().toString());

        QueryDB.updateRegistroDetalle
                (TABLA_PARTIDA,partida.getString(PARTIDA_ID_PROYECTO),partida.getInt(PARTIDA_SECUENCIA),valores);


        new Common.Calculos.Tareafechas().execute();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            this.activity = (AppCompatActivity) context;
            icFragments = (ICFragmentos) this.activity;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
