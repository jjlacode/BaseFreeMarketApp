package jjlacode.com.freelanceproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.android.AppActivity;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.media.MediaUtil;
import jjlacode.com.freelanceproject.util.time.TimeDateUtil;
import jjlacode.com.freelanceproject.util.time.calendar.clases.DiaCalBase;

import static jjlacode.com.freelanceproject.CommonPry.Constantes.CLIENTE;
import static jjlacode.com.freelanceproject.CommonPry.Constantes.DIARIO;
import static jjlacode.com.freelanceproject.CommonPry.Constantes.EVENTO;
import static jjlacode.com.freelanceproject.CommonPry.Constantes.PROYECTO;

public class DiaCalDiario extends DiaCalBase implements ContratoPry.Tablas,
        JavaUtil.Constantes, CommonPry.TiposEvento {


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
    protected ListaAdaptadorFiltroRV setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new ListaAdaptadorFiltroRV(context,layoutItem,lista,campos);
    }

    public class AdaptadorFiltroRV extends ListaAdaptadorFiltroRV {

        public AdaptadorFiltroRV(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<Modelo> entrada) {

            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRVcont extends BaseViewHolder implements TipoViewHolder, CommonPry.TiposNota {

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
        public void bind(final Modelo modelo) {

            descripcion.setText(modelo.getString(DIARIO_DESCRIPCION));
            fecha = modelo.getLong(DIARIO_CREATE);
            fechaDiario.setText(TimeDateUtil.getDateString(fecha));
            rel.setText(modelo.getString(DIARIO_NOMBREREL));

            String path;
            if (modelo.getString(DIARIO_RUTAFOTO) != null) {
                imagen.setVisibility(View.VISIBLE);
                path = modelo.getString(DIARIO_RUTAFOTO);
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
                    bundle.putSerializable(MODELO, modelo);
                    bundle.putString(CAMPO_ID,modelo.getString(DIARIO_ID_RELACIONADO));
                    bundle.putString(ORIGEN, DIARIO);
                    String destinoRel = modelo.getString(DIARIO_REL);
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


            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVcont(view);
        }
    }
}
