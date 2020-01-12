package com.codevsolution.freemarketsapp.ui;
// Created by jjlacode on 9/06/19. 

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.codevsolution.freemarketsapp.sqlite.ContratoPry;

import java.util.ArrayList;

import static com.codevsolution.freemarketsapp.logica.Interactor.TiposDetPartida.TIPOTRABAJO;

public class FragmentCRUDTrabajo extends FragmentCRUD implements Interactor.ConstantesPry, ContratoPry.Tablas {

    private ImageButton btnNota;
    private ImageButton btnVerNotas;
    private Button addPartida;

    public FragmentCRUDTrabajo() {
        // Required empty public constructor
    }

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
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

        String seleccion = NOTA_ID_RELACIONADO + " = '" + id + "'";
        if (checkQueryList(CAMPOS_NOTA, seleccion, null)) {
            btnVerNotas.setVisibility(View.VISIBLE);
        } else {
            btnVerNotas.setVisibility(View.GONE);
        }

        if (nnn(AndroidUtil.getSharePreference(contexto, PERSISTENCIA, PARTIDABASE_ID_PARTIDABASE, NULL))) {

            visible(addPartida);
            gone(btnNota);
            gone(btnVerNotas);
        } else {
            gone(addPartida);
            visible(btnNota);
        }

    }

    @Override
    protected void setNuevo() {
        super.setNuevo();
        onUpdate();
    }

    @Override
    protected void onClickRV(View v) {
        super.onClickRV(v);

    }

    @Override
    protected void setBundle() {
        super.setBundle();
    }


    private int crearTrabajoBase(String idPartidabase) {

        ContentValues valores = new ContentValues();
        putDato(valores, DETPARTIDABASE_ID_DETPARTIDABASE, modeloSQL.getString(TRABAJO_ID_TRABAJO));
        putDato(valores, DETPARTIDABASE_TIPO, TIPOTRABAJO);
        putDato(valores, DETPARTIDABASE_ID_PARTIDABASE, idPartidabase);
        return crearRegistroSec(CAMPOS_DETPARTIDABASE, idPartidabase, valores);
    }

    @Override
    protected void setInicio() {

        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalle);

        imagen = vistaForm.addViewImagenLayout();
        imagen.getLinearLayoutCompat().setFocusable(false);
        vistaForm.addEditMaterialLayout(getString(R.string.nombre), TRABAJO_NOMBRE, null, null);
        vistaForm.addEditMaterialLayout(getString(R.string.descripcion), TRABAJO_DESCRIPCION, null, null);
        EditMaterialLayout tiempo = vistaForm.addEditMaterialLayout(getString(R.string.tiempo), TRABAJO_TIEMPO, null, null);
        tiempo.setTipo(EditMaterialLayout.NUMERO | EditMaterialLayout.DECIMAL);
        addPartida = vistaForm.addButtonPrimary(R.string.add_detpartida);
        addPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = new Bundle();
                String idPartidabase = AndroidUtil.getSharePreference(contexto, PERSISTENCIA, PARTIDABASE_ID_PARTIDABASE, NULL);
                if (nnn(idPartidabase)) {
                    putBundle(CAMPO_ID, idPartidabase);
                    putBundle(CAMPO_SECUENCIA, crearTrabajoBase(idPartidabase));
                    AndroidUtil.setSharePreference(contexto, PERSISTENCIA, PARTIDABASE_ID_PARTIDABASE, NULL);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaBaseTrabajo());
                }
            }
        });
        btnNota = vistaForm.addImageButtonSecundary(R.drawable.ic_nueva_nota_indigo);
        btnNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarBundle();
                bundle.putString(IDREL, modeloSQL.getString(campoID));
                bundle.putString(SUBTITULO, modeloSQL.getString(TRABAJO_DESCRIPCION));
                bundle.putString(ORIGEN, TRABAJO);
                bundle.putSerializable(MODELO,null);
                bundle.putString(CAMPO_ID,null);
                bundle.putBoolean(NUEVOREGISTRO,true);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentNuevaNota());
            }
        });
        btnVerNotas = vistaForm.addImageButtonSecundary(R.drawable.ic_lista_notas_indigo);
        btnVerNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarBundle();
                bundle.putString(IDREL, modeloSQL.getString(campoID));
                bundle.putString(SUBTITULO, modeloSQL.getString(TRABAJO_DESCRIPCION));
                bundle.putString(ORIGEN, TAREA);
                bundle.putSerializable(MODELO,null);
                bundle.putString(CAMPO_ID,null);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
            }
        });

        actualizarArrays(vistaForm);
    }

    @Override
    protected void setLayout() {

        //layoutCuerpo = R.layout.fragment_crud_trabajo;
        layoutItem = R.layout.item_list_trabajo;

    }

    @Override
    protected boolean update() {
        return super.update();
    }

    @Override
    protected void setContenedor() {

        //setDato(TRABAJO_RUTAFOTO,path);

    }

    @Override
    protected void setcambioFragment() {
        super.setcambioFragment();

    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<ModeloSQL> entrada) {

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
        public void bind(ModeloSQL modeloSQL) {

            tiempo.setText(JavaUtil.getDecimales(modeloSQL.getDouble(TRABAJO_TIEMPO)));
            nombre.setText(modeloSQL.getString(TRABAJO_NOMBRE));
            descripcion.setText(modeloSQL.getString(TRABAJO_DESCRIPCION));
            if (modeloSQL.getString(TRABAJO_RUTAFOTO) != null) {
                new MediaUtil(contexto).setImageUriCircle(modeloSQL.getString(TRABAJO_RUTAFOTO), imagenTarea);
            }

            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }


}
