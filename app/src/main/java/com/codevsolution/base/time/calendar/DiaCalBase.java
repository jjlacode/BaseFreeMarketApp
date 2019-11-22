package com.codevsolution.base.time.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.FragmentRV;
import com.codevsolution.base.animation.OneFrameLayout;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;

import java.io.Serializable;
import java.util.ArrayList;


public abstract class DiaCalBase extends FragmentRV {

    protected static final String THORACAL = "texto_hora_calendario";
    public static final String HORACAL = "hora_calendario";
    protected ListaModeloSQL listaCont;
    protected long fecha;
    protected long horaCal;
    protected String campo;
    private boolean descanso;

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        fecha = bundle.getLong(FECHA);
        campo = bundle.getString(CAMPO);
        System.out.println("fecha = " + TimeDateUtil.getDateString(fecha));

        listaCont = setListaDia(fecha);
    }

    protected ListaModeloSQL setListaDia(long fecha) {

        return null;
    }

    @Override
    protected void setAcciones() {

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnBack();
            }
        });
    }

    protected abstract void setOnBack();

    protected abstract int setLayoutRvDia();

    protected abstract TipoViewHolder setViewHolderDia(View view);

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

    @Override
    protected String[] setCamposFiltro() {
        return campos;
    }

    @Override
    protected void setLista() {

        lista = new ArrayList<>();

        for (int i = 0; i < 48 * 30; i += 30) {

            DiaCal diaCal = new DiaCal(i * MINUTOSLONG, TimeDateUtil.getTimeString(i * MINUTOSLONG));
            lista.add(diaCal);

        }

    }

    @Override
    protected void setManagerRV() {
        super.setManagerRV();

    }

    @Override
    protected void onSetRV() {
        super.onSetRV();
        subTitulo = TimeDateUtil.getDateString(fecha);
        activityBase.toolbar.setSubtitle(subTitulo);
    }

    @Override
    public void setOnClickRV(Object object) {

        mostrarDialogNuevo(object);
    }

    @Override
    protected void setLayout() {

        layoutItem = R.layout.item_list_diacal;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setInicio() {

        gone(frameAnimationCuerpo);


        fragment_container.setOnSwipeListener(new OneFrameLayout.OnSwipeListener() {
            @Override
            public void rightSwipe() {

                fecha -= DIASLONG;
                listaCont = setListaDia(fecha);
                actualizarConsultasRV();
                setRv();
            }

            @Override
            public void leftSwipe() {

                fecha += DIASLONG;
                listaCont = setListaDia(fecha);
                actualizarConsultasRV();
                setRv();

            }
        });


    }


    protected abstract void onClickHora(DiaCal diaCal);

    protected void mostrarDialogNuevo(final Object object) {

        final CharSequence[] opciones = {Estilos.getString(contexto, "crear_nuevo"),
                Estilos.getString(contexto, "cancelar")};
        final AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle(Estilos.getString(contexto, "nuevo"));
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals(Estilos.getString(contexto, "crear_nuevo"))) {


                    onClickHora((DiaCal) object);

                } else {

                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }


    public class DiaCal implements Serializable {

        long horaCal;
        String tHoraCal;

        public DiaCal(long horaCal, String tHoraCal) {
            this.horaCal = horaCal;
            this.tHoraCal = tHoraCal;
        }

        public long getHoraCal() {
            return horaCal;
        }

        public void setHoraCal(long horaCal) {
            this.horaCal = horaCal;
        }

        public String gettHoraCal() {
            return tHoraCal;
        }

        public void settHoraCal(String tHoraCal) {
            this.tHoraCal = tHoraCal;
        }

    }

    protected boolean setColorCard(long fecha, long horaCal) {
        return false;
    }

    protected boolean getDescanso() {
        return descanso;
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
            rvAdapter = new RVAdapter(setViewHolderDia(itemView), listaCont.getLista(),
                    setLayoutRvDia());

        }

        @Override
        public void bind(ArrayList<?> lista, int position) {

            DiaCal diaCal = (DiaCal) lista.get(position);

            hora.setText(diaCal.gettHoraCal());
            horaCal = diaCal.getHoraCal();

            rvcont.setAdapter(rvAdapter);

            rvAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

            if (setColorCard(fecha, horaCal)) {
                card.setCardBackgroundColor(getResources().getColor(Estilos.colorPrimary(contexto)));
                descanso = false;
            } else {
                card.setCardBackgroundColor(Estilos.getColor(contexto, "Color_cal_notok"));
                descanso = true;
            }

            super.bind(lista, position);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

}
