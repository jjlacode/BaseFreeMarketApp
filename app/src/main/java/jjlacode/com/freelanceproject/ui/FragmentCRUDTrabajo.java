package jjlacode.com.freelanceproject.ui;
// Created by jjlacode on 9/06/19. 

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.android.controls.EditMaterial;
import jjlacode.com.freelanceproject.util.crud.FragmentCRUD;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.media.MediaUtil;

import static jjlacode.com.freelanceproject.util.sqlite.ConsultaBD.checkQueryList;


public class FragmentCRUDTrabajo extends FragmentCRUD implements CommonPry.Constantes, ContratoPry.Tablas {

    private EditMaterial tiempo, nombre, descripcion;
    private ImageButton btnNota;
    private ImageButton btnVerNotas;
    private String idRel;
    private Modelo proyecto;
    private Modelo partida;

    public FragmentCRUDTrabajo() {
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

        tabla = TABLA_TRABAJO;

    }

    @Override
    protected void setTitulo() {

        tituloSingular = R.string.tarea;
        tituloPlural = R.string.tareas;
        tituloNuevo = R.string.nueva_tarea;

    }

    @Override
    protected void setDatos() {

        nombre.setText(modelo.getString(TRABAJO_NOMBRE));
        descripcion.setText(modelo.getString(TRABAJO_DESCRIPCION));
        tiempo.setText(modelo.getString(TRABAJO_TIEMPO));

        String seleccion = NOTA_ID_RELACIONADO + " = '" + id + "'";
        if (checkQueryList(CAMPOS_NOTA, seleccion, null)) {
            btnVerNotas.setVisibility(View.VISIBLE);
        } else {
            btnVerNotas.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onClickRV(View v) {
        super.onClickRV(v);
        if (origen.equals(DETPARTIDA)) {
            bundle = new Bundle();
            putBundle(TRABAJO, modelo);
            putBundle(PROYECTO, proyecto);
            putBundle(PARTIDA, partida);
            putBundle(CAMPO_ID, idRel);
            putBundle(ORIGEN, PARTIDA);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaTrabajo());
        }
    }

    @Override
    protected void setBundle() {
        super.setBundle();
        idRel = getStringBundle(IDREL, "");
        proyecto = (Modelo) getBundleSerial(PROYECTO);
        partida = (Modelo) getBundleSerial(PARTIDA);
    }

    @Override
    protected void setAcciones() {

        btnNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarBundle();
                bundle.putString(IDREL,modelo.getString(campoID));
                bundle.putString(SUBTITULO, modelo.getString(TRABAJO_DESCRIPCION));
                bundle.putString(ORIGEN, TRABAJO);
                bundle.putSerializable(MODELO,null);
                bundle.putString(CAMPO_ID,null);
                bundle.putBoolean(NUEVOREGISTRO,true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevaNota());
            }
        });

        btnVerNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarBundle();
                bundle.putString(IDREL,modelo.getString(campoID));
                bundle.putString(SUBTITULO, modelo.getString(TRABAJO_DESCRIPCION));
                bundle.putString(ORIGEN, TAREA);
                bundle.putSerializable(MODELO,null);
                bundle.putString(CAMPO_ID,null);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
            }
        });

    }

    @Override
    protected void setInicio() {

        nombre = (EditMaterial) ctrl(R.id.etnombretarea, TRABAJO_NOMBRE);
        descripcion = (EditMaterial) ctrl(R.id.etdesctarea, TRABAJO_DESCRIPCION);
        tiempo = (EditMaterial) ctrl(R.id.ettiempotarea, TRABAJO_TIEMPO);
        imagen = (ImageView) ctrl(R.id.imgtarea);
        btnNota = (ImageButton) ctrl(R.id.btn_crearnota_tarea);
        btnVerNotas = (ImageButton) ctrl(R.id.btn_vernotas_tarea);

    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_trabajo;
        layoutItem = R.layout.item_list_trabajo;

    }

    @Override
    protected boolean update() {
        if (super.update()){
            new CommonPry.Calculos.TareaSincronizarPartidasBase().execute();
            return true;
        }
        return false;
    }

    @Override
    protected void setContenedor() {

        setDato(TRABAJO_NOMBRE,nombre.getText().toString());
        setDato(TRABAJO_DESCRIPCION,descripcion.getText().toString());
        setDato(TRABAJO_TIEMPO,tiempo.getText().toString(),DOUBLE);
        setDato(TRABAJO_RUTAFOTO,path);

    }

    @Override
    protected void setcambioFragment() {
        super.setcambioFragment();
        if (origen.equals(DETPARTIDA) && id != null) {
            bundle = new Bundle();
            putBundle(TRABAJO, modelo);
            putBundle(CAMPO_ID, idRel);
            putBundle(ORIGEN, PARTIDA);
            putBundle(PROYECTO, proyecto);
            putBundle(PARTIDA, partida);

            icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaTrabajo());
        }

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

            tiempo.setText(JavaUtil.getDecimales(entrada.get(posicion).getDouble(TRABAJO_TIEMPO)));
            nombre.setText(entrada.get(posicion).getString(TRABAJO_NOMBRE));
            descripcion.setText(entrada.get(posicion).getString(TRABAJO_DESCRIPCION));
            String path = entrada.get(posicion).getString(TRABAJO_RUTAFOTO);
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

            tiempo.setText(JavaUtil.getDecimales(modelo.getDouble(TRABAJO_TIEMPO)));
            nombre.setText(modelo.getString(TRABAJO_NOMBRE));
            descripcion.setText(modelo.getString(TRABAJO_DESCRIPCION));
            if (modelo.getString(TRABAJO_RUTAFOTO)!=null){
                new MediaUtil(contexto).setImageUriCircle(modelo.getString(TRABAJO_RUTAFOTO), imagenTarea);
            }

            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }


}
