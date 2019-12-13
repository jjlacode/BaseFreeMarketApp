package com.codevsolution.freemarketsapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

public class DiaCalDiario extends DiaCalHorario implements ContratoPry.Tablas,
        JavaUtil.Constantes, Interactor.TiposEvento, Interactor.ConstantesPry {

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected void setOnBack() {

        bundle = new Bundle();
        bundle.putString(ORIGEN,DIARIO);
        bundle.putString(ACTUAL, DIARIO);
        bundle.putString(CAMPO_ID,null);
        bundle.putSerializable(LISTA,null);
        bundle.putSerializable(MODELO, null);

        icFragmentos.enviarBundleAFragment(bundle, new Diario());
    }

    @Override
    protected int setLayoutRvDia() {
        return R.layout.item_list_nota_diario;
    }

    @Override
    protected TipoViewHolder setViewHolderDia(View view) {
        return new ViewHolderRVcont(view);
    }

    @Override
    protected void onClickHora(DiaCal diaCal) {


    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos) {
        return new ListaAdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<ModeloSQL> entrada) {

            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRVcont extends BaseViewHolder implements TipoViewHolder, Interactor.TiposNota {

        TextView descripcion, fechaDiario, rel;
        ImageView imagen,ver;
        CardView card;

        public ViewHolderRVcont(View itemView) {
            super(itemView);
            rel = itemView.findViewById(R.id.tvreldiario);
            descripcion = itemView.findViewById(R.id.tvdescdiario);
            imagen = itemView.findViewById(R.id.imagendiario);
            fechaDiario = itemView.findViewById(R.id.tvfechadiario);
            ver = itemView.findViewById(R.id.btnverdiario);
            card = itemView.findViewById(R.id.carddiario);

        }

        @Override
        public void bind(final ModeloSQL modeloSQL) {

            descripcion.setText(modeloSQL.getString(DIARIO_DESCRIPCION));
            fecha = modeloSQL.getLong(DIARIO_CREATE);
            fechaDiario.setText(TimeDateUtil.getDateString(fecha));
            rel.setText(modeloSQL.getString(DIARIO_NOMBREREL));

            String path;
            if (modeloSQL.getString(DIARIO_RUTAFOTO) != null) {
                imagen.setVisibility(View.VISIBLE);
                path = modeloSQL.getString(DIARIO_RUTAFOTO);
                MediaUtil imagenUtil = new MediaUtil(AppActivity.getAppContext());
                imagenUtil.setImageUriCircle(path, imagen);
            } else {
                //imagenTarea.setVisibility(View.GONE);
                imagen.setImageResource(R.drawable.ic_nota_imagen_indigo);
            }

            ver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    bundle = new Bundle();
                    bundle.putSerializable(MODELO, modeloSQL);
                    bundle.putString(CAMPO_ID, modeloSQL.getString(DIARIO_ID_RELACIONADO));
                    bundle.putString(ORIGEN, DIARIO);
                    String destinoRel = modeloSQL.getString(DIARIO_REL);
                    bundle.putString(ACTUAL, destinoRel);

                    switch (destinoRel){

                        case EVENTO :
                            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
                            break;

                        case PROYECTO :
                            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
                            break;

                        case CLIENTE :
                            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDCliente());

                    }

                }
            });


            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVcont(view);
        }
    }
}
