package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import jjlacode.com.freelanceproject.adapter.AdaptadorProducto;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;


public class FragmentCGastoProyecto extends Fragment implements Common.Constantes, Contract.Tablas {

    TextView nombreProyecto;
    EditText descripcion;
    EditText importe;
    EditText cantidad;
    Button btnVolver;
    Button btnSave;
    Button btnNuevoProducto;
    RecyclerView rvProductos;
    String idProducto;
    int secuencia;
    View vista;
    ArrayList<Modelo> listaProductos;
    AdaptadorProducto adaptador;
    Context context = AppActivity.getAppContext();
    ContentResolver resolver = context.getContentResolver();
    Modelo gasto;

    private String namef;
    private String idProyecto;
    private Bundle bundle;
    private Modelo proyecto;
    private AppCompatActivity activity;
    private ICFragmentos icFragments;
    private TextView titulo;

    public FragmentCGastoProyecto() {
        // Required empty public constructor
    }

    public static FragmentCGastoProyecto newInstance() {
        FragmentCGastoProyecto fragment = new FragmentCGastoProyecto();

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
        vista = inflater.inflate(R.layout.fragment_c_gasto_proyecto, container, false);
        // Inflate the layout for this fragment

        descripcion = vista.findViewById(R.id.etdescripcionNgasto);
        importe = vista.findViewById(R.id.etimporteNgasto);
        cantidad = vista.findViewById(R.id.etcantidadNgasto);
        nombreProyecto = vista.findViewById(R.id.tvtitproyNgasto);
        titulo = vista.findViewById(R.id.tvtitNgasto);
        btnSave = vista.findViewById(R.id.btnsaveNgasto);
        btnVolver = vista.findViewById(R.id.btnvolverNgasto);
        btnNuevoProducto = vista.findViewById(R.id.btnnuevoProductoNgasto);
        rvProductos = vista.findViewById(R.id.rvproductosNgastos);

        bundle = getArguments();

        if (bundle!=null) {
            namef = bundle.getString("namef");
            proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
            bundle = null;
        }

        if (namef == PROYECTO){

            titulo.setText(getString(R.string.gasto_proyecto));

        }else if (namef == PRESUPUESTO){

            titulo.setText(getString(R.string.gasto_presupuesto));
        }

        rvProductos.setLayoutManager(new LinearLayoutManager(getContext()));

        nombreProyecto.setText(proyecto.getString(PROYECTO_NOMBRE));

            listaProductos = QueryDB.queryList(CAMPOS_PRODUCTO,null,null);

        idProducto = listaProductos.get(0).getString(PRODUCTO_ID_PRODUCTO);

        adaptador = new AdaptadorProducto(listaProductos);

        rvProductos.setAdapter(adaptador);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rvProductos.getChildAdapterPosition(v) > 0) {


                    descripcion.setText(listaProductos.get(rvProductos.getChildAdapterPosition(v)).getString(PRODUCTO_DESCRIPCION));
                    importe.setText(listaProductos.get(rvProductos.getChildAdapterPosition(v)).getString(PRODUCTO_IMPORTE));
                    idProducto = (listaProductos.get(rvProductos.getChildAdapterPosition(v)).getString(PRODUCTO_ID_PRODUCTO));

                }else if (rvProductos.getChildAdapterPosition(v) == 0) {

                    descripcion.setText(null);
                    importe.setText(null);
                    idProducto = listaProductos.get(0).getString(PRODUCTO_ID_PRODUCTO);
                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegistrarGasto();

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString("namef",namef);
                icFragments.enviarBundleAFragment(bundle, new FragmentGastoProyecto());
                bundle = null;

            }
        });


        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString("namef",namef);
                icFragments.enviarBundleAFragment(bundle, new FragmentGastoProyecto());
                bundle = null;

            }
        });

        btnNuevoProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (descripcion.getText()!=null && importe.getText()!=null) {
                    int cont = 0;
                    for (Modelo producto : listaProductos) {
                        if (producto.getString(PRODUCTO_DESCRIPCION).equals(descripcion.getText().toString())) {
                            Toast.makeText(getContext(), "El producto ya existe", Toast.LENGTH_SHORT).show();
                            cont++;
                        }
                    }
                    if (cont == 0) {

                        try {
                            ContentValues valores = new ContentValues();

                            QueryDB.putDato(valores,CAMPOS_PRODUCTO,PRODUCTO_DESCRIPCION,descripcion.getText().toString());
                            QueryDB.putDato(valores,CAMPOS_PRODUCTO,PRODUCTO_IMPORTE,importe.getText().toString());

                            Uri uri = resolver.insert(Contract.obtenerUriContenido(TABLA_PRODUCTO), valores);
                            idProducto = Contract.obtenerIdTabla(uri);

                            recargaRv();

                            Toast.makeText(getContext(), "Nuevo producto guardado", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error al guardar producto", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });

        return vista;
    }

    private void RegistrarGasto() {

        idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);

        ArrayList<Modelo> listaGastos = QueryDB.queryListDetalle(CAMPOS_GASTO,idProyecto,TABLA_PROYECTO);

        secuencia = 0;

        if (listaGastos!=null && listaGastos.size()>0) {
            secuencia = listaGastos.size() + 1;
        }else{
            secuencia = 1;
        }
        if (secuencia>0) {

            try {

                ContentValues valores = new ContentValues();

                QueryDB.putDato(valores,CAMPOS_GASTO,GASTO_DESCRIPCION,descripcion.getText().toString());
                QueryDB.putDato(valores,CAMPOS_GASTO,GASTO_IMPORTE,importe.getText().toString());
                QueryDB.putDato(valores,CAMPOS_GASTO,GASTO_CANTIDAD,cantidad.getText().toString());
                QueryDB.putDato(valores,CAMPOS_GASTO,GASTO_SECUENCIA,secuencia);
                QueryDB.putDato(valores,CAMPOS_GASTO,GASTO_ID_PRODUCTO,idProducto);
                QueryDB.putDato(valores,CAMPOS_GASTO,GASTO_ID_PROYECTO,idProyecto);

                resolver.insert(Contract.crearUriTablaDetalle(idProyecto,secuencia, TABLA_GASTO), valores);
                Toast.makeText(getContext(), "Nuevo gasto guardado", Toast.LENGTH_SHORT).show();

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString("namef",namef);
                icFragments.enviarBundleAFragment(bundle, new FragmentGastoProyecto());
                bundle = null;

            }catch (Exception e){Toast.makeText(getContext(), "Error al guardar gasto", Toast.LENGTH_SHORT).show();}
        }
    }

    private  void recargaRv() {

            listaProductos = QueryDB.queryList(CAMPOS_PRODUCTO,null, null);

        adaptador = new AdaptadorProducto(listaProductos);
        rvProductos.setAdapter(adaptador);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (rvProductos.getChildAdapterPosition(v) > 0) {


                    descripcion.setText(listaProductos.get(rvProductos.getChildAdapterPosition(v)).getString(PRODUCTO_DESCRIPCION));
                    importe.setText(listaProductos.get(rvProductos.getChildAdapterPosition(v)).getString(PRODUCTO_IMPORTE));
                    idProducto = (listaProductos.get(rvProductos.getChildAdapterPosition(v)).getString(PRODUCTO_ID_PRODUCTO));

                } else if (rvProductos.getChildAdapterPosition(v) == 0) {

                    descripcion.setText(null);
                    importe.setText(null);
                    idProducto = listaProductos.get(0).getString(PRODUCTO_ID_PRODUCTO);
                }

            }
        });

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
