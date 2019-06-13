package jjlacode.com.freelanceproject.ui;
// Created by jjlacode on 9/06/19. 

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.android.controls.EditMaterial;
import jjlacode.com.freelanceproject.util.crud.FragmentCRUD;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.media.MediaUtil;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;


public class FragmentCRUDTarea extends FragmentCRUD implements CommonPry.Constantes, ContratoPry.Tablas {

    private EditMaterial tiempo, nombre, descripcion;
    private ImageButton btnNota;
    private ImageButton btnVerNotas;

    public FragmentCRUDTarea() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroRV setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroRV(context, layoutItem, lista, campos);
    }

    @Override
    protected void setTabla() {

        tabla = TABLA_TAREA;

    }

    @Override
    protected void setTitulo() {

        tituloSingular = R.string.tarea;
        tituloPlural = R.string.tareas;
        tituloNuevo = R.string.nueva_tarea;

    }

    @Override
    protected void setDatos() {

        nombre.setText(modelo.getString(TAREA_NOMBRE));
        descripcion.setText(modelo.getString(TAREA_DESCRIPCION));
        tiempo.setText(modelo.getString(TAREA_TIEMPO));

        imagenMediaPantalla();
    }

    @Override
    protected void setAcciones() {

        btnNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarBundle();
                bundle.putString(IDREL,modelo.getString(campoID));
                bundle.putString(SUBTITULO, modelo.getString(TAREA_DESCRIPCION));
                bundle.putString(ORIGEN, TAREA);
                bundle.putSerializable(MODELO,null);
                bundle.putString(ID,null);
                bundle.putBoolean(NUEVOREGISTRO,true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
            }
        });

        btnVerNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarBundle();
                bundle.putString(IDREL,modelo.getString(campoID));
                bundle.putString(SUBTITULO, modelo.getString(TAREA_DESCRIPCION));
                bundle.putString(ORIGEN, TAREA);
                bundle.putSerializable(MODELO,null);
                bundle.putString(ID,null);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
            }
        });

    }

    @Override
    protected void setInicio() {

        nombre = (EditMaterial) ctrl(R.id.etnombretarea);
        descripcion = (EditMaterial) ctrl(R.id.etdesctarea);
        tiempo = (EditMaterial) ctrl(R.id.ettiempotarea);
        imagen = (ImageView) ctrl(R.id.imgtarea);
        btnNota = (ImageButton) ctrl(R.id.btn_crearnota_tarea);
        btnVerNotas = (ImageButton) ctrl(R.id.btn_vernotas_tarea);

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_tarea;
        layoutItem = R.layout.item_list_tarea;

    }

    @Override
    protected void setContenedor() {

        setDato(TAREA_NOMBRE,nombre.getText().toString());
        setDato(TAREA_DESCRIPCION,descripcion.getText().toString());
        setDato(TAREA_TIEMPO,tiempo.getText().toString(),DOUBLE);
        setDato(TAREA_RUTAFOTO,path);

    }

    public class AdaptadorFiltroRV extends ListaAdaptadorFiltroRV {

        public AdaptadorFiltroRV(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<Modelo> entrada) {

            TextView tiempo, nombre, descripcion;
            ImageView imagenTarea;

            tiempo = itemView.findViewById(R.id.tvtiempoltareas);
            imagenTarea = itemView.findViewById(R.id.imgltarea);
            nombre = itemView.findViewById(R.id.tvnomltarea);
            descripcion = itemView.findViewById(R.id.tvdescripcionltareas);

            tiempo.setText(JavaUtil.getDecimales(entrada.get(posicion).getDouble(TAREA_TIEMPO)));
            nombre.setText(entrada.get(posicion).getString(TAREA_NOMBRE));
            descripcion.setText(entrada.get(posicion).getString(TAREA_DESCRIPCION));
            String path = entrada.get(posicion).getString(TAREA_RUTAFOTO);
            if (path!=null){
                new MediaUtil(contexto).setImageUriCircle(path,imagenTarea);
            }

            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView tiempo, nombre, descripcion;
        ImageView imagenTarea;

        public ViewHolderRV(View itemView) {
            super(itemView);

            tiempo = itemView.findViewById(R.id.tvtiempoltareas);
            imagenTarea = itemView.findViewById(R.id.imgltarea);
            nombre = itemView.findViewById(R.id.tvnomltarea);
            descripcion = itemView.findViewById(R.id.tvdescripcionltareas);

        }

        @Override
        public void bind(Modelo modelo) {

            tiempo.setText(JavaUtil.getDecimales(modelo.getDouble(TAREA_TIEMPO)));
            nombre.setText(modelo.getString(TAREA_NOMBRE));
            descripcion.setText(modelo.getString(TAREA_DESCRIPCION));
            if (modelo.getString(TAREA_RUTAFOTO)!=null){
                new MediaUtil(contexto).setImageUriCircle(modelo.getString(TAREA_RUTAFOTO), imagenTarea);
            }

            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }


}
