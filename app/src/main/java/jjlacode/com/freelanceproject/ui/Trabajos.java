package jjlacode.com.freelanceproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.android.AppActivity;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.util.media.MediaUtil;
import jjlacode.com.freelanceproject.util.time.calendar.clases.CalendarioBase;
import jjlacode.com.freelanceproject.util.time.calendar.clases.Day;

import static jjlacode.com.freelanceproject.CommonPry.Constantes.EVENTO;
import static jjlacode.com.freelanceproject.CommonPry.Constantes.PROYECTO;
import static jjlacode.com.freelanceproject.CommonPry.Constantes.TRABAJOS;
import static jjlacode.com.freelanceproject.CommonPry.TiposEvento.TIPOEVENTOCITA;
import static jjlacode.com.freelanceproject.CommonPry.TiposEvento.TIPOEVENTOEMAIL;
import static jjlacode.com.freelanceproject.CommonPry.TiposEvento.TIPOEVENTOEVENTO;
import static jjlacode.com.freelanceproject.CommonPry.TiposEvento.TIPOEVENTOLLAMADA;
import static jjlacode.com.freelanceproject.CommonPry.TiposEvento.TIPOEVENTOTAREA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ACTUAL;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.CALENDARIO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.FECHA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ID;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.LISTA;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.MODELO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.NUEVOREGISTRO;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ORIGEN;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.SUBTITULO;
import static jjlacode.com.freelanceproject.util.android.AppActivity.viewOnMapA;

public class Trabajos extends CalendarioBase implements CommonPry.TiposEstados {

    @Override
    protected void setLayoutItem() {

        layoutItem = R.layout.item_list_trabajos_en_curso;

    }

    @Override
    protected boolean setIfLista(Modelo modelo) {

        return ((modelo.getInt(PROYECTO_TIPOESTADO) >= TPRESUPACEPTADO) &&
                (modelo.getInt(PROYECTO_TIPOESTADO) < TPROYECTPENDCOBRO));
    }

    @Override
    protected boolean setIfListaHoy(Modelo modelo, long hoy) {

        return (JavaUtil.getDate(modelo.getLong(campo)).equals(JavaUtil.getDate(hoy)));
    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_PROYECTO;
        campo = PROYECTO_FECHAENTREGAACORDADA;

    }

    @Override
    protected void setTitulo() {

        titulo = R.string.proyectos_en_curso;

    }

    @Override
    protected void setOnDayClick(Day day, int position) {

    }

    @Override
    protected void setOnDayLongClick(Day day, int position) {

    }

    @Override
    protected void setNuevo(long fecha) {

        bundle = new Bundle();
        bundle.putBoolean(NUEVOREGISTRO,true);
        bundle.putString(ORIGEN,TRABAJOS);
        bundle.putLong(FECHA,fecha);
        bundle.putString(ACTUAL, PROYECTO);
        bundle.putString(ID,null);
        bundle.putSerializable(LISTA,null);
        bundle.putSerializable(MODELO, null);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
    }

    @Override
    protected void setVerLista() {

        bundle = new Bundle();
        bundle.putString(ORIGEN,TRABAJOS);
        bundle.putString(ACTUAL, PROYECTO);
        bundle.putString(ID,null);
        bundle.putSerializable(MODELO, null);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
    }

    @Override
    protected void setOnPrevMonth() {

    }

    @Override
    protected void setOnNextMonth() {

    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView descripcion,cliente;
        ProgressBar pbar;
        ImageView imagen;
        ImageButton ver;
        CardView card;

        public ViewHolderRV(View itemView) {
            super(itemView);

            descripcion = itemView.findViewById(R.id.tvdesctrabajos);
            imagen = itemView.findViewById(R.id.imgtrabajos);
            pbar = itemView.findViewById(R.id.pbartrabajos);
            ver = itemView.findViewById(R.id.btnvertrabajos);
            card = itemView.findViewById(R.id.cardtrabajos);
            cliente = itemView.findViewById(R.id.tvclientetrabajos);

        }

        @Override
        public void bind(final Modelo modelo) {


            double completada = modelo.getDouble(PROYECTO_TOTCOMPLETADO);
            descripcion.setText(modelo.getString(PROYECTO_DESCRIPCION));
            pbar.setProgress((int)completada);
            cliente.setText(modelo.getString(PROYECTO_CLIENTE_NOMBRE));
            MediaUtil imagenUtil = new MediaUtil(getContext());
            try{
                imagenUtil.setImageUri(modelo.getString(PROYECTO_RUTAFOTO),imagen);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (completada>0){
                visible(pbar);
            }else{
                gone(pbar);
            }


                long retraso = modelo.getLong(PROYECTO_RETRASO);

                    if (retraso > 3 * CommonPry.DIASLONG) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));
                    } else if (retraso > CommonPry.DIASLONG) {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));
                    } else {
                        card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));
                    }//imgret.setImageResource(R.drawable.alert_box_v);}


            ver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bundle = new Bundle();
                    bundle.putSerializable(MODELO, modelo);
                    bundle.putString(ID,modelo.getString(PROYECTO_ID_PROYECTO));
                    bundle.putString(ORIGEN, TRABAJOS);
                    bundle.putString(SUBTITULO, modelo.getString(PROYECTO_NOMBRE));
                    bundle.putString(ACTUAL, PROYECTO);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());

                }
            });

            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

}
