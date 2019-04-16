package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
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

import java.util.ArrayList;

import jjlacode.com.androidutils.FragmentUD;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.utilities.CommonPry;

public class FragmentUDPartidaBase extends FragmentUD implements CommonPry.Constantes,
        ContratoPry.Tablas, CommonPry.TiposDetPartida {

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

    private Modelo partidabase;

    private static ConsultaBD consulta = new ConsultaBD();
    private String idPartidabase;

    public FragmentUDPartidaBase() {
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
            partidabase = (Modelo) bundle.getSerializable(TABLA_PARTIDABASE);
            idPartidabase = partidabase.getString(PARTIDABASE_ID_PARTIDA);
            namef = bundle.getString("namef");
            bundle = null;
            bundle = new Bundle();
            bundle.putString("namefsub", TABLA_PARTIDABASE);
            icFragmentos.enviarBundleAActivity(bundle);
            bundle = null;
        }

        cargarDatos();

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
                partidabase = consulta.queryObject(CAMPOS_PARTIDABASE, idPartidabase);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PARTIDABASE, partidabase);
                bundle.putString("namef",namef);
                bundle.putString("tipo", TIPOTAREA);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCUDDetpartidaBase());

            }

        });

        btnNuevoProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                partidabase = consulta.queryObject(CAMPOS_PARTIDABASE, idPartidabase);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PARTIDABASE, partidabase);
                bundle.putString("namef",namef);
                bundle.putString("tipo", TIPOPRODUCTO);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCUDDetpartidaBase());

            }

        });

        btnNuevoProdProv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                partidabase = consulta.queryObject(CAMPOS_PARTIDABASE, idPartidabase);
                bundle = new Bundle();
                bundle.putSerializable(TABLA_PARTIDABASE, partidabase);
                bundle.putString("namef",namef);
                bundle.putString("tipo", TIPOPRODUCTOPROV);
                icFragmentos.enviarBundleAFragment(bundle,new FragmentCUDDetpartidaBase());

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

        progressBarPartida.setVisibility(View.GONE);
        completadaPartida.setVisibility(View.GONE);
        labelCompletada.setVisibility(View.GONE);
        cantidadPartida.setVisibility(View.GONE);


        nombrePartida.setText(partidabase.getString(PARTIDABASE_NOMBRE));
        descripcionPartida.setText(partidabase.getString(PARTIDABASE_DESCRIPCION));
        tiempoPartida.setText(partidabase.getString(PARTIDABASE_TIEMPO));
        importePartida.setText(JavaUtil.formatoMonedaLocal(partidabase.getDouble(PARTIDABASE_PRECIO)));

        if (partidabase.getString(PARTIDABASE_RUTAFOTO)!=null){

            imagen.setImageURI(partidabase.getUri(PARTIDABASE_RUTAFOTO));
        }

        imagenret.setVisibility(View.GONE);


        listaDetpartidas = consulta.queryListDetalle(CAMPOS_DETPARTIDABASE, idPartidabase,TABLA_PARTIDABASE);

        if (listaDetpartidas!=null && listaDetpartidas.size()>0) {

            rvdetalles.setLayoutManager(new LinearLayoutManager(getContext()));

            AdaptadorDetpartida adapter = new AdaptadorDetpartida(listaDetpartidas, namef);

            rvdetalles.setAdapter(adapter);

            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String tipo = (listaDetpartidas.get(rvdetalles.getChildAdapterPosition(v)).
                            getString(DETPARTIDABASE_TIPO));
                    Modelo detpartidabase = listaDetpartidas.get(rvdetalles.getChildAdapterPosition(v));
                    bundle = new Bundle();
                    bundle.putSerializable(TABLA_PARTIDABASE, partidabase);
                    bundle.putSerializable(TABLA_DETPARTIDABASE, detpartidabase);
                    bundle.putString("namef", namef);
                    bundle.putString("tipo", tipo);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaBase());

                }
            });

        }else{

            rvdetalles.setVisibility(View.GONE);
        }
    }

    private void cambiarFragment() {


            bundle = new Bundle();
            bundle.putString("namef",namef);
            icFragmentos.enviarBundleAFragment(bundle,new FragmentPartidaBase());

    }


    @Override
    protected void delete() {


        consulta.deleteRegistro(TABLA_PARTIDABASE, idPartidabase);

    }

    @Override
    protected void update() {

        valores = new ContentValues();

        consulta.putDato(valores,CAMPOS_PARTIDABASE,PARTIDABASE_NOMBRE,nombrePartida.getText().toString());
        consulta.putDato(valores,CAMPOS_PARTIDABASE,PARTIDABASE_DESCRIPCION,descripcionPartida.getText().toString());
        consulta.putDato(valores,CAMPOS_PARTIDABASE,PARTIDABASE_TIEMPO,JavaUtil.comprobarDouble(tiempoPartida.getText().toString()));
        consulta.putDato(valores,CAMPOS_PARTIDABASE,PARTIDABASE_PRECIO,JavaUtil.comprobarDouble(importePartida.getText().toString()));

        consulta.updateRegistro(TABLA_PARTIDABASE, idPartidabase,valores);

    }


    public static class AdaptadorDetpartida extends RecyclerView.Adapter<AdaptadorDetpartida.DetpartidaViewHolder>
            implements View.OnClickListener, ContratoPry.Tablas, CommonPry.TiposDetPartida {

        private ArrayList<Modelo> listDetpartida;
        private View.OnClickListener listener;
        private String namef;

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

            String tipodetpartida = listDetpartida.get(position).getString(DETPARTIDABASE_TIPO);
            detpartidaViewHolder.tipo.setText(tipodetpartida.toUpperCase());
            detpartidaViewHolder.nombre.setText(listDetpartida.get(position).getString(DETPARTIDABASE_NOMBRE));
            detpartidaViewHolder.tiempo.setText(listDetpartida.get(position).getString(DETPARTIDABASE_TIEMPO));
            detpartidaViewHolder.cantidad.setText(listDetpartida.get(position).getString(DETPARTIDABASE_CANTIDAD));
            if (listDetpartida.get(position).getString(DETPARTIDABASE_TIPO).equals(CommonPry.TiposDetPartida.TIPOTAREA)) {
                detpartidaViewHolder.importe.setText(JavaUtil.formatoMonedaLocal(
                        (listDetpartida.get(position).getDouble(DETPARTIDABASE_TIEMPO)*CommonPry.hora*
                                listDetpartida.get(position).getDouble(DETPARTIDABASE_CANTIDAD))));
            }else{
                detpartidaViewHolder.importe.setText(listDetpartida.get(position).getString(DETPARTIDABASE_PRECIO));
            }
            if (listDetpartida.get(position).getString(DETPARTIDABASE_RUTAFOTO)!=null) {
                detpartidaViewHolder.imagen.setImageURI(listDetpartida.get(position).getUri(DETPARTIDABASE_RUTAFOTO));
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
