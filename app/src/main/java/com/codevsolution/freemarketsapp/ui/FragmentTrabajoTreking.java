package com.codevsolution.freemarketsapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.crud.FragmentRVR;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.codevsolution.freemarketsapp.sqlite.ContratoPry;

import java.util.ArrayList;
import java.util.Locale;

import static com.codevsolution.base.javautil.JavaUtil.hoy;

public class FragmentTrabajoTreking extends FragmentRVR implements Interactor.ConstantesPry,
        ContratoPry.Tablas, Interactor.TiposDetPartida, Interactor.TiposEstados {


    public FragmentTrabajoTreking() {
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
    public void setOnClickRV(String id, int secuencia, ModeloSQL modeloSQL) {

        bundle = new Bundle();
        putBundle(CAMPO_ID, id);
        putBundle(MODELO, modeloSQL);
        putBundle(CAMPO_SECUENCIA, secuencia);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCUDDetpartidaTrabajo());
    }

    @Override
    protected void setTabla() {

        tabla = TABLA_DETPARTIDA;

    }

    @Override
    protected void setDatos() {
        startTimer();
        System.out.println("lista = " + lista.sizeLista());
    }


    @Override
    protected void setOnCronometro(Chronometer arg0) {

        boolean onTick = setCounterUp(arg0) % 60 == 0;
        if (origen.equals(INICIO) && onTick) {

            actualizarConsultasRV();
            setRv();
            System.out.println("onTick");

        } else if (onTick) {

            super.setOnCronometro(arg0);
        }
    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.detpartida;
        tituloPlural = tituloSingular;
        tituloNuevo = R.string.nuevo_detalle_partida;
    }

    @Override
    protected void setLayout() {

        layoutItem = R.layout.item_list_detpartida_treking;

    }

    @Override
    protected void setInicio() {


    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<ModeloSQL> entrada) {

            TextView tipo, nombre, ltiempo, lcantidad, limporte, tiempo, cantidad, importe;
            ImageView imagen;

            tipo = itemView.findViewById(R.id.tvtipoldetpaetida);
            nombre = itemView.findViewById(R.id.tvnomldetpartida);
            ltiempo = itemView.findViewById(R.id.ltiempoldetpartida);
            lcantidad = itemView.findViewById(R.id.lcantldetpartida);
            limporte = itemView.findViewById(R.id.limpldetpartida);
            tiempo = itemView.findViewById(R.id.tvtiempoldetpartida);
            cantidad = itemView.findViewById(R.id.tvcantldetpartida);
            importe = itemView.findViewById(R.id.tvimpldetpartida);
            imagen = itemView.findViewById(R.id.imgldetpartida);

            String tipodetpartida = entrada.get(posicion).getString(DETPARTIDA_TIPO);
            tipo.setText(tipodetpartida.toUpperCase());
            nombre.setText(entrada.get(posicion).getString(DETPARTIDA_NOMBRE));
            tiempo.setText(entrada.get(posicion).getString(DETPARTIDA_TIEMPO));
            if (entrada.get(posicion).getString(DETPARTIDA_TIPO).equals(Interactor.TiposDetPartida.TIPOTRABAJO)) {
                importe.setText(JavaUtil.formatoMonedaLocal(
                        (entrada.get(posicion).getDouble(DETPARTIDA_TIEMPO) * Interactor.hora)));
            } else {
                importe.setText(entrada.get(posicion).getString(DETPARTIDA_PRECIO));
            }
            if (entrada.get(posicion).getString(DETPARTIDA_RUTAFOTO) != null) {
                if (tipodetpartida.equals(TIPOPRODUCTOPROV)) {
                    MediaUtil imagenUtil = new MediaUtil(contexto);
                    imagenUtil.setImageFireStoreCircle(entrada.get(posicion).getString(DETPARTIDA_RUTAFOTO), imagen);

                } else {
                    imagen.setImageURI(entrada.get(posicion).getUri(DETPARTIDA_RUTAFOTO));
                }
            }

            if (!tipodetpartida.equals(TIPOTRABAJO)) {

                ltiempo.setVisibility(View.GONE);
                tiempo.setVisibility(View.GONE);
            }


            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView nomPartida, nombre, proy, tvcomplet, ttiempo, treking;
        ImageView imagen, imgEstado;
        ProgressBar pbar, pbar2;
        CardView card;

        public ViewHolderRV(View itemView) {
            super(itemView);
            nomPartida = itemView.findViewById(R.id.tvpartldetpaetidat);
            nombre = itemView.findViewById(R.id.tvnomldetpartidat);
            proy = itemView.findViewById(R.id.tvproydetpartidat);
            imagen = itemView.findViewById(R.id.imgldetpartidat);
            pbar = itemView.findViewById(R.id.pbarldetpartidat);
            pbar2 = itemView.findViewById(R.id.pbar2ldetpartidat);
            ttiempo = itemView.findViewById(R.id.tvttotalldetpartidat);
            tvcomplet = itemView.findViewById(R.id.tvcompletadaldetpartidat);
            card = itemView.findViewById(R.id.carddetpartidat);
            treking = itemView.findViewById(R.id.tvtrekldetpaetidat);
            imgEstado = itemView.findViewById(R.id.imgestadoldetpartidat);
        }

        @Override
        public void bind(ModeloSQL modeloSQL) {

            String id = modeloSQL.getString(DETPARTIDA_ID_PARTIDA);
            int secuencia = modeloSQL.getInt(DETPARTIDA_SECUENCIA);
            modeloSQL = consultaBD.queryObjectDetalle(CAMPOS_DETPARTIDA, id, secuencia);

            nombre.setText(modeloSQL.getString(DETPARTIDA_NOMBRE));

            try {
                MediaUtil mediaUtil = new MediaUtil(contexto);
                mediaUtil.setImageUriCircle(modeloSQL.getString(DETPARTIDA_RUTAFOTO), imagen);

            } catch (Exception e) {
                e.printStackTrace();
            }

            long ahora = hoy();
            ModeloSQL partida = consultaBD.queryObject(CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA, id);
            nomPartida.setText(partida.getString(PARTIDA_NOMBRE));
            double cantidadTotal = partida.getDouble(PARTIDA_CANTIDAD);
            double tiempodet = (modeloSQL.getDouble(DETPARTIDA_TIEMPO) * cantidadTotal * HORASLONG) / 1000;
            double tiemporeal = (modeloSQL.getDouble(DETPARTIDA_TIEMPOREAL) * HORASLONG) / 1000;
            String idProyecto = partida.getString(PARTIDA_ID_PROYECTO);
            ModeloSQL proyecto = queryObject(CAMPOS_PROYECTO, idProyecto);
            proy.setText(proyecto.getString(PROYECTO_NOMBRE));
            long contador = modeloSQL.getLong(DETPARTIDA_CONTADOR);
            long pausa = modeloSQL.getLong(DETPARTIDA_PAUSA);
            if (pausa > 0) {
                ahora = pausa;
                imgEstado.setImageResource(R.drawable.ic_pausa_indigo);
            } else if (contador > 0) {
                imgEstado.setImageResource(R.drawable.ic_play_indigo);
            } else {
                contador = ahora;
                imgEstado.setImageResource(R.drawable.ic_stop_indigo);
            }

            double count = ((double) (ahora - contador)) / 1000;


            double completada = (((tiemporeal * 100) / tiempodet) + ((count * 100) / tiempodet));

            String asText = JavaUtil.relojContador((ahora - contador) / 1000);
            treking.setText(String.format(Locale.getDefault(), "%s", asText));

            ttiempo.setText(String.format(Locale.getDefault(),
                    "%s %s %s %s %s", JavaUtil.getDecimales(((tiemporeal + count) / 3600)),
                    getString(R.string.horas), getString(R.string.de),
                    JavaUtil.getDecimales(tiempodet / 3600), getString(R.string.horas)));

            tvcomplet.setText(String.format(Locale.getDefault(),
                    "%s %s", JavaUtil.getDecimales(completada), "% completa"));

            AndroidUtil.barsCard(contexto, pbar, pbar2, false, 100, 90, 120, completada,
                    tvcomplet, treking, R.color.Color_contador_ok, R.color.Color_contador_acept,
                    R.color.Color_contador_notok, card, R.color.Color_card_ok, R.color.Color_card_acept,
                    R.color.Color_card_notok);

            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopTimer();
    }
}