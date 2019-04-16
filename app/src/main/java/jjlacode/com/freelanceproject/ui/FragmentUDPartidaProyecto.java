package jjlacode.com.freelanceproject.ui;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import jjlacode.com.androidutils.AppActivity;
import jjlacode.com.androidutils.FragmentUD;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.GlideApp;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.utilities.CommonPry;

import static jjlacode.com.freelanceproject.utilities.CommonPry.permiso;

public class FragmentUDPartidaProyecto extends FragmentUD implements CommonPry.Constantes,
        ContratoPry.Tablas, CommonPry.TiposDetPartida {

    private Long retraso;
    private EditText nombrePartida;
    private EditText descripcionPartida;
    private EditText tiempoPartida;
    private EditText importePartida;
    private EditText cantidadPartida;
    private EditText completadaPartida;
    private Button btnNuevaTarea;
    private Button btnNuevoProd;
    private Button btnNuevoProdProv;
    private ImageView imagenret;

    private RecyclerView rvdetalles;
    private ProgressBar progressBarPartida;
    private TextView labelCompletada;
    private ArrayList<Modelo> listaDetpartidas;

    private Modelo proyecto;
    private Modelo partida;
    private String idDetPartida;

    private static ConsultaBD consulta = new ConsultaBD();
    private String idPartida;
    private int secuencia;

    public FragmentUDPartidaProyecto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ud_partida_proyecto, container, false);

        nombrePartida = view.findViewById(R.id.etnomudpartida);
        descripcionPartida = view.findViewById(R.id.etdescripcionUDpartida);
        tiempoPartida = view.findViewById(R.id.ettiempoUDpartida);
        importePartida = view.findViewById(R.id.etprecioUDpartida);
        cantidadPartida = view.findViewById(R.id.etcantidadUDpartida);
        completadaPartida = view.findViewById(R.id.etcompletadaUDpartida);
        btnsave = view.findViewById(R.id.btnsaveUDpartida);
        btndelete = view.findViewById(R.id.btndelUDpartida);
        btnback = view.findViewById(R.id.btnvolverUDpartida);
        btnNuevaTarea = view.findViewById(R.id.btntareaudpartida);
        btnNuevoProd = view.findViewById(R.id.btnprodudpartida);
        btnNuevoProdProv = view.findViewById(R.id.btnprovudpartida);
        progressBarPartida = view.findViewById(R.id.progressBarUDpartida);
        imagen = view.findViewById(R.id.imgudpartida);
        imagenret = view.findViewById(R.id.imgretudpartida);
        labelCompletada = view.findViewById(R.id.lcompletadaUDpartida);
        rvdetalles = view.findViewById(R.id.rvdetalleUDpartida);

        bundle = getArguments();

        if (bundle!=null) {
            proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
            partida = (Modelo) bundle.getSerializable(TABLA_PARTIDA);
            idPartida = partida.getString(PARTIDA_ID_PROYECTO);
            secuencia = partida.getInt(PARTIDA_SECUENCIA);
            idDetPartida = partida.getString(PARTIDA_ID_PARTIDA);
            namef = bundle.getString("namef");
            bundle = null;
            bundle = new Bundle();
            bundle.putSerializable(TABLA_PROYECTO,proyecto);
            bundle.putString("namefsub", TABLA_PARTIDA);
            icFragmentos.enviarBundleAActivity(bundle);
            bundle = null;
        }


        cargarDatos();

        if (permiso) {
            imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mostrarDialogoOpcionesImagen();
                }
            });
        }


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();

                cambiarFragment();

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delete();

                cambiarFragment();

            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cambiarFragment();

            }
        });

        btnNuevaTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                partida = consulta.queryObjectDetalle(CAMPOS_PARTIDA,idPartida, secuencia);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putSerializable(TABLA_PARTIDA,partida);
                bundle.putString("namef",namef);
                bundle.putString("tipo", TIPOTAREA);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCUDDetpartida());

            }

        });

        btnNuevoProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                partida = consulta.queryObjectDetalle(CAMPOS_PARTIDA,idPartida, secuencia);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putSerializable(TABLA_PARTIDA,partida);
                bundle.putString("namef",namef);
                bundle.putString("tipo", TIPOPRODUCTO);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCUDDetpartida());

            }

        });

        btnNuevoProdProv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                partida = consulta.queryObjectDetalle(CAMPOS_PARTIDA,idPartida, secuencia);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putSerializable(TABLA_PARTIDA,partida);
                bundle.putString("namef",namef);
                bundle.putString("tipo", TIPOPRODUCTOPROV);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCUDDetpartida());

            }

        });


        return view;
    }

    @Override
    public void onResume() {
        cargarDatos();
        super.onResume();
    }

    private void cargarDatos() {

        if (namef.equals(PRESUPUESTO)){

            progressBarPartida.setVisibility(View.GONE);
            completadaPartida.setVisibility(View.GONE);
            labelCompletada.setVisibility(View.GONE);
        }else{

            progressBarPartida.setVisibility(View.VISIBLE);
            completadaPartida.setVisibility(View.VISIBLE);
            labelCompletada.setVisibility(View.VISIBLE);
        }

        nombrePartida.setText(partida.getString(PARTIDA_NOMBRE));
        descripcionPartida.setText(partida.getString(PARTIDA_DESCRIPCION));
        tiempoPartida.setText(partida.getString(PARTIDA_TIEMPO));
        importePartida.setText(JavaUtil.formatoMonedaLocal(partida.getDouble(PARTIDA_PRECIO)));
        cantidadPartida.setText(partida.getString(PARTIDA_CANTIDAD));
        completadaPartida.setText(partida.getString(PARTIDA_COMPLETADA));
        progressBarPartida.setProgress(partida.getInt(PARTIDA_COMPLETADA));

        if (partida.getString(PARTIDA_RUTAFOTO)!=null){

            imagen.setImageURI(partida.getUri(PARTIDA_RUTAFOTO));
            path = partida.getString(PARTIDA_RUTAFOTO);
        }

        retraso = partida.getLong(PARTIDA_PROYECTO_RETRASO);
        if (retraso<1){
            imagenret.setImageResource(R.drawable.alert_box_r);}
        else if (retraso<3){
            imagenret.setImageResource(R.drawable.alert_box_a);}
        else {
            imagenret.setImageResource(R.drawable.alert_box_v);}

        listaDetpartidas = consulta.queryListDetalle(CAMPOS_DETPARTIDA,idDetPartida,TABLA_PARTIDA);

        if (listaDetpartidas!=null && listaDetpartidas.size()>0) {

            rvdetalles.setLayoutManager(new LinearLayoutManager(getContext()));

            AdaptadorDetpartida adapter = new AdaptadorDetpartida(listaDetpartidas, namef);

            rvdetalles.setAdapter(adapter);

            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    idDetPartida = (listaDetpartidas.get(rvdetalles.getChildAdapterPosition(v)).
                            getString(DETPARTIDA_ID_PARTIDA));
                    String secuenciadetpartida = (listaDetpartidas.get(rvdetalles.getChildAdapterPosition(v)).
                            getString(DETPARTIDA_SECUENCIA));
                    String tipo = (listaDetpartidas.get(rvdetalles.getChildAdapterPosition(v)).
                            getString(DETPARTIDA_TIPO));
                    Modelo detpartida = consulta.queryObjectDetalle(CAMPOS_DETPARTIDA, idDetPartida, secuenciadetpartida);
                    bundle = new Bundle();
                    bundle.putSerializable(TABLA_PROYECTO, proyecto);
                    bundle.putSerializable(TABLA_PARTIDA, partida);
                    bundle.putSerializable(TABLA_DETPARTIDA, detpartida);
                    bundle.putString("namef", namef);
                    bundle.putString("tipo", tipo);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartida());

                }
            });
        }else{

            rvdetalles.setVisibility(View.GONE);
        }
    }

    private void cambiarFragment() {

        if (namef.equals(AGENDA)){

            bundle = new Bundle();
            bundle.putString("namef",namef);
            icFragmentos.enviarBundleAFragment(bundle,new FragmentAgenda());
            bundle = null;

        }else {

            bundle = new Bundle();
            bundle.putSerializable(TABLA_PROYECTO,proyecto);
            bundle.putString("namef",namef);
            icFragmentos.enviarBundleAFragment(bundle,new FragmentPartidasProyecto());
        }
    }


    @Override
    protected void delete() {


        consulta.deleteRegistroDetalle
                (TABLA_PARTIDA,partida.getString(PARTIDA_ID_PROYECTO),partida.getString(PARTIDA_SECUENCIA));

        new CommonPry.Calculos.Tareafechas().execute();
        new CommonPry.Calculos.TareaActualizaProy().execute(proyecto.getString(PROYECTO_ID_PROYECTO));
    }

    @Override
    protected void update() {


        ContentValues valores = new ContentValues();

        consulta.putDato(valores,CAMPOS_PARTIDA,PARTIDA_NOMBRE,nombrePartida.getText().toString());
        consulta.putDato(valores,CAMPOS_PARTIDA,PARTIDA_DESCRIPCION,descripcionPartida.getText().toString());
        consulta.putDato(valores,CAMPOS_PARTIDA,PARTIDA_TIEMPO,JavaUtil.comprobarDouble(tiempoPartida.getText().toString()));
        consulta.putDato(valores,CAMPOS_PARTIDA,PARTIDA_PRECIO,JavaUtil.comprobarDouble(importePartida.getText().toString()));
        consulta.putDato(valores,CAMPOS_PARTIDA,PARTIDA_CANTIDAD,JavaUtil.comprobarDouble(cantidadPartida.getText().toString()));
        consulta.putDato(valores,CAMPOS_PARTIDA,PARTIDA_COMPLETADA,JavaUtil.comprobarInteger(completadaPartida.getText().toString()));
        consulta.putDato(valores,CAMPOS_PARTIDA,PARTIDA_RUTAFOTO,path);

        consulta.updateRegistroDetalle
                (TABLA_PARTIDA,partida.getString(PARTIDA_ID_PROYECTO),partida.getInt(PARTIDA_SECUENCIA),valores);

        new CommonPry.Calculos.Tareafechas().execute();
        new CommonPry.Calculos.TareaActualizaProy().execute(proyecto.getString(PROYECTO_ID_PROYECTO));

    }


    public static class AdaptadorDetpartida extends RecyclerView.Adapter<AdaptadorDetpartida.DetpartidaViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas, CommonPry.TiposDetPartida {

        private ArrayList<Modelo> listDetpartida;
        private View.OnClickListener listener;
        private String namef;
        private Context context = AppActivity.getAppContext();

        public AdaptadorDetpartida(ArrayList<Modelo> listDetpartida, String namef) {

            this.listDetpartida = listDetpartida;
            this.namef = namef;
        }

        @NonNull
        @Override
        public DetpartidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_detpartida, null, false);

            view.setOnClickListener(this);


            return new DetpartidaViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull DetpartidaViewHolder detpartidaViewHolder, int position) {

            String tipodetpartida = listDetpartida.get(position).getString(DETPARTIDA_TIPO);
            detpartidaViewHolder.tipo.setText(tipodetpartida.toUpperCase());
            detpartidaViewHolder.nombre.setText(listDetpartida.get(position).getString(DETPARTIDA_NOMBRE));
            detpartidaViewHolder.tiempo.setText(listDetpartida.get(position).getString(DETPARTIDA_TIEMPO));
            detpartidaViewHolder.cantidad.setText(listDetpartida.get(position).getString(DETPARTIDA_CANTIDAD));
            if (listDetpartida.get(position).getString(DETPARTIDA_TIPO).equals(CommonPry.TiposDetPartida.TIPOTAREA)) {
                detpartidaViewHolder.importe.setText(JavaUtil.formatoMonedaLocal(
                        (listDetpartida.get(position).getDouble(DETPARTIDA_TIEMPO)*CommonPry.hora*
                                listDetpartida.get(position).getDouble(DETPARTIDA_CANTIDAD))));
            }else{
                detpartidaViewHolder.importe.setText(listDetpartida.get(position).getString(DETPARTIDA_PRECIO));
            }
            if (listDetpartida.get(position).getString(DETPARTIDA_RUTAFOTO)!=null) {
                if (tipodetpartida.equals(TIPOPRODUCTOPROV)){
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference spaceRef = storageRef.child(listDetpartida.get(position).getString(DETPARTIDA_RUTAFOTO));
                    GlideApp.with(context)
                            .load(spaceRef)
                            .into(detpartidaViewHolder.imagen);
                }
                detpartidaViewHolder.imagen.setImageURI(listDetpartida.get(position).getUri(DETPARTIDA_RUTAFOTO));
            }

            if (!tipodetpartida.equals(TIPOTAREA)){

                detpartidaViewHolder.ltiempo.setVisibility(View.GONE);
                detpartidaViewHolder.tiempo.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {

            return listDetpartida.size();
        }

        @Override
        public void onClick(View v) {

            if (listener != null) {

                listener.onClick(v);


            }

        }

        class DetpartidaViewHolder extends RecyclerView.ViewHolder {

            TextView tipo,nombre,ltiempo,lcantidad,limporte,tiempo,cantidad,importe;
            ImageView imagen;

            DetpartidaViewHolder(@NonNull View itemView) {
                super(itemView);

                tipo = itemView.findViewById(R.id.tvtipoldetpaetida);
                nombre = itemView.findViewById(R.id.tvnomldetpartida);
                ltiempo = itemView.findViewById(R.id.ltiempoldetpartida);
                lcantidad = itemView.findViewById(R.id.lcantldetpartida);
                limporte = itemView.findViewById(R.id.limpldetpartida);
                tiempo = itemView.findViewById(R.id.tvtiempoldetpartida);
                cantidad = itemView.findViewById(R.id.tvcantldetpartida);
                importe = itemView.findViewById(R.id.tvimpldetpartida);
                imagen = itemView.findViewById(R.id.imgldetpartida);


            }
        }
    }

}
