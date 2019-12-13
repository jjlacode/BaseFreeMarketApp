package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

public class DiaCalNotas extends DiaCalHorario implements ContratoPry.Tablas,
        JavaUtil.Constantes, Interactor.TiposEvento, Interactor.ConstantesPry {

    @Override
    protected FragmentBase setFragment() {
        return this;
    }

    @Override
    protected ListaModeloSQL setListaDia(long fecha) {

        return Notas.getListaNotas(fecha);
    }

    @Override
    protected void setOnBack() {

        bundle = new Bundle();
        bundle.putString(ORIGEN,NOTAS);
        bundle.putString(ACTUAL, NOTAS);
        bundle.putString(CAMPO_ID,null);
        bundle.putSerializable(LISTA,null);
        bundle.putSerializable(MODELO, null);

        icFragmentos.enviarBundleAFragment(bundle, new Notas());
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

        horaCal = diaCal.getHoraCal();

        bundle = new Bundle();
        bundle.putBoolean(NUEVOREGISTRO,true);
        bundle.putString(ORIGEN,NOTAS);
        bundle.putLong(FECHA,fecha);
        bundle.putLong(HORACAL,horaCal);
        bundle.putString(ACTUAL, NOTA);
        bundle.putString(CAMPO_ID,null);
        bundle.putSerializable(LISTA,null);
        bundle.putSerializable(MODELO, null);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());
    }


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView hora;
        CardView card;
        RecyclerView rvcont;
        RVAdapter rvAdapter;

        public ViewHolderRV(View itemView) {
            super(itemView);
            rvcont = itemView.findViewById(R.id.rvcontenidodiacal);
            hora = itemView.findViewById(R.id.tvhoradiacal);
            card = itemView.findViewById(R.id.carddiacal);
            rvAdapter = new RVAdapter(new ViewHolderRVcont(itemView),listaCont.getLista(),
                    R.layout.item_list_nota_diario);

        }

        @Override
        public void bind(ModeloSQL modeloSQL) {

            hora.setText(modeloSQL.getString(THORACAL));
            horaCal = modeloSQL.getLong(HORACAL);

            rvcont.setAdapter(rvAdapter);

            rvAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                }
            });


            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    public class ViewHolderRVcont extends BaseViewHolder implements TipoViewHolder, Interactor.TiposNota {

        TextView descripcion, fechanota, rel;
        ImageView imagen,ver;
        CardView card;

        public ViewHolderRVcont(View itemView) {
            super(itemView);
            rel = itemView.findViewById(R.id.tvrelnotadiario);
            descripcion = itemView.findViewById(R.id.tvdescnotadiario);
            imagen = itemView.findViewById(R.id.imagennotadiario);
            fechanota = itemView.findViewById(R.id.tvfechanotadiario);
            ver = itemView.findViewById(R.id.btnvernotadiario);
            card = itemView.findViewById(R.id.cardnotadiario);

        }

        @Override
        public void bind(final ModeloSQL modeloSQL) {

            gone(card);

            if (JavaUtil.sumaHoraMin(modeloSQL.getLong(NOTA_FECHA)) >= horaCal &&
                    JavaUtil.sumaHoraMin(modeloSQL.getLong(NOTA_FECHA)) < horaCal + (30 * MINUTOSLONG)) {

                visible(card);

                descripcion.setText(modeloSQL.getString(NOTA_TITULO));
                fecha = modeloSQL.getLong(NOTA_FECHA);
                fechanota.setText(JavaUtil.getDateTime(fecha));
                String tipo = modeloSQL.getString(NOTA_TIPO);
                rel.setText(modeloSQL.getString(NOTA_NOMBREREL));
                System.out.println("modeloSQL = " + modeloSQL.getString(NOTA_NOMBREREL));
                if (modeloSQL.getString(NOTA_NOMBREREL) != null) {
                    visible(rel);
                    card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
                } else {
                    gone(rel);
                }
                String path;
                //imagenTarea.setVisibility(View.GONE);

                if (tipo != null) {

                    switch (tipo) {

                        case NOTATEXTO:

                            imagen.setImageResource(R.drawable.ic_nota_texto_indigo);

                            break;

                        case NOTAAUDIO:

                            imagen.setImageResource(R.drawable.ic_nota_audio_indigo);
                            if (modeloSQL.getString(NOTA_RUTAFOTO) != null) {
                                path = modeloSQL.getString(NOTA_RUTAFOTO);
                            }

                            break;

                        case NOTAVIDEO:

                            imagen.setImageResource(R.drawable.ic_nota_video_indigo);
                            if (modeloSQL.getString(NOTA_RUTAFOTO) != null) {
                                path = modeloSQL.getString(NOTA_RUTAFOTO);
                            }

                            break;

                        case NOTAIMAGEN:

                            if (modeloSQL.getString(NOTA_RUTAFOTO) != null) {
                                imagen.setVisibility(View.VISIBLE);
                                path = modeloSQL.getString(NOTA_RUTAFOTO);
                                MediaUtil imagenUtil = new MediaUtil(AppActivity.getAppContext());
                                imagenUtil.setImageUriCircle(path, imagen);
                            } else {
                                //imagenTarea.setVisibility(View.GONE);
                                imagen.setImageResource(R.drawable.ic_nota_imagen_indigo);
                            }
                    }
                }

                ver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bundle = new Bundle();
                        bundle.putSerializable(MODELO, modeloSQL);
                        bundle.putString(CAMPO_ID, modeloSQL.getString(NOTA_ID_NOTA));
                        bundle.putString(ORIGEN, NOTAS);
                        if (modeloSQL.getString(NOTA_ID_RELACIONADO) != null) {
                            bundle.putString(IDREL, modeloSQL.getString(NOTA_ID_RELACIONADO));
                            bundle.putString(SUBTITULO, modeloSQL.getString(NOTA_NOMBREREL));
                        } else {
                            bundle.putString(SUBTITULO, JavaUtil.getDate(fecha));
                        }
                        bundle.putString(ACTUAL, NOTA);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDNota());

                    }
                });
            }

            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVcont(view);
        }
    }
}
