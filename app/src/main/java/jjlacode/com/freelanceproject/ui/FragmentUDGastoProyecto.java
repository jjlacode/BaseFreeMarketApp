package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
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

public class FragmentUDGastoProyecto extends Fragment implements Contract.Tablas {

    private String namef;

    TextView nombreProyecto;
    EditText descripcion;
    EditText importe;
    EditText cantidad;
    Button btnVolver;
    Button btnSave;
    Button btnDel;
    Button btnNuevoProducto;
    RecyclerView rvProductos;
    String idProducto;
    EditText beneficio;
    View vista;
    ArrayList<Modelo> listaProductos;
    AdaptadorProducto adaptador;
    Context context = AppActivity.getAppContext();
    ContentResolver resolver = context.getContentResolver();
    Modelo gasto;

    private Bundle bundle;
    private AppCompatActivity activity;
    private ICFragmentos icFragmentos;
    private Modelo proyecto;

    public FragmentUDGastoProyecto() {
        // Required empty public constructor
    }

    public static FragmentUDGastoProyecto newInstance() {
        FragmentUDGastoProyecto fragment = new FragmentUDGastoProyecto();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_ud_gasto_proyecto, container, false);
        // Inflate the layout for this fragment

        descripcion = vista.findViewById(R.id.etdescripcionUDgasto);
        importe = vista.findViewById(R.id.etimporteUDgasto);
        cantidad = vista.findViewById(R.id.etcantidadUDgasto);
        nombreProyecto = vista.findViewById(R.id.tvtitproyudgasto);
        btnSave = vista.findViewById(R.id.btnsaveUDgasto);
        btnDel = vista.findViewById(R.id.btndelUDgasto);
        btnVolver = vista.findViewById(R.id.btnvolverUDgasto);
        btnNuevoProducto = vista.findViewById(R.id.btnnuevoproductoudgasto);
        rvProductos = vista.findViewById(R.id.rvudgasto);
        beneficio = vista.findViewById(R.id.etbenefudgasto);

        bundle = getArguments();

        if (bundle!=null) {
            proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
            gasto = (Modelo) bundle.getSerializable(TABLA_GASTO);
            namef = bundle.getString("namef");
            bundle = null;
            bundle = new Bundle();
            bundle.putSerializable(TABLA_PROYECTO,proyecto);
            bundle.putString("namefsub", TABLA_GASTO);
            icFragmentos.enviarBundleAActivity(bundle);
            bundle = null;
        }

        rvProductos.setLayoutManager(new LinearLayoutManager(getContext()));

        nombreProyecto.setText(proyecto.getString(PROYECTO_NOMBRE));

        descripcion.setText(gasto.getString(GASTO_DESCRIPCION));
        importe.setText(gasto.getString(GASTO_IMPORTE));
        cantidad.setText(gasto.getString(GASTO_CANTIDAD));
        idProducto = gasto.getString(GASTO_ID_PROYECTO);

            listaProductos = QueryDB.queryList(CAMPOS_PRODUCTO,null,null);

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

                modificarGasto();

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString("namef",namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentGastoProyecto());
                bundle = null;

            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                borrarGasto();

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString("namef",namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentGastoProyecto());
                bundle = null;

            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle = new Bundle();
                bundle.putSerializable(TABLA_PROYECTO,proyecto);
                bundle.putString("namef",namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentGastoProyecto());
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

                            idProducto = QueryDB.idInsertRegistro(TABLA_PRODUCTO,valores);

                            recargaRv();

                            Toast.makeText(getContext(), "Nuevo producto guardada", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error al guardar producto", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });

        return vista;
    }

    private  void recargaRv() {

            listaProductos = QueryDB.queryList(CAMPOS_PRODUCTO, null, null);

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

    private void modificarGasto() {


            try {

                ContentValues valores = new ContentValues();
                QueryDB.putDato(valores,CAMPOS_GASTO,GASTO_DESCRIPCION,descripcion.getText().toString());
                QueryDB.putDato(valores,CAMPOS_GASTO,GASTO_IMPORTE,importe.getText().toString());
                QueryDB.putDato(valores,CAMPOS_GASTO,GASTO_CANTIDAD,cantidad.getText().toString());
                QueryDB.putDato(valores,CAMPOS_GASTO,GASTO_ID_PRODUCTO,idProducto);

                resolver.update(Contract.crearUriTablaDetalle
                        (gasto.getString(GASTO_ID_PROYECTO), gasto.getInt(GASTO_SECUENCIA),
                                TABLA_GASTO), valores,null,null);
            }catch (Exception e){Toast.makeText(getContext(),
                    "Error al modificar producto", Toast.LENGTH_SHORT).show();
            }
    }

    private void borrarGasto() {

        try {

            resolver.delete(Contract.crearUriTablaDetalle
                    (gasto.getString(GASTO_ID_PROYECTO), gasto.getInt(GASTO_SECUENCIA),
                            TABLA_GASTO), null, null);
            Toast.makeText(getContext(),"Gasto borrado",Toast.LENGTH_LONG).show();

        }catch (Exception e){

            Toast.makeText(getContext(),"Error al borrar gasto",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            this.activity = (AppCompatActivity) context;
            icFragmentos = (ICFragmentos) this.activity;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
