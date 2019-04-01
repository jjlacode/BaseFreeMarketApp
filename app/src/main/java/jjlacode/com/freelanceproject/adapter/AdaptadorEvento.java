package jjlacode.com.freelanceproject.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import jjlacode.com.androidutils.AppActivity;
import jjlacode.com.androidutils.ICFragmentos;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.ui.FragmentUDEvento;
import jjlacode.com.freelanceproject.utilities.Common;

public class AdaptadorEvento extends RecyclerView.Adapter<AdaptadorEvento.EventoViewHolder>
        implements Common.TiposEvento,View.OnClickListener {

    ArrayList<Modelo> listaEvento;
    private View.OnClickListener listener;
    private ICFragmentos icFragmentos;
    private Bundle bundle;
    private String namef;
    private Activity activity;
    private Context context;

    public AdaptadorEvento(ArrayList<Modelo> listaEvento, String namef) {

        this.listaEvento = listaEvento;
        this.namef = namef;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_evento, null, false);

        view.setOnClickListener(this);


        return new EventoViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull final EventoViewHolder eventoViewHolder, final int position) {

        eventoViewHolder.descripcion.setText(listaEvento.get(position).getCampos
                (Contract.Tablas.EVENTO_DESCRIPCION));
        eventoViewHolder.telefono.setText(listaEvento.get(position).getCampos
                (Contract.Tablas.EVENTO_TELEFONO));
        eventoViewHolder.lugar.setText(listaEvento.get(position).getCampos
                (Contract.Tablas.EVENTO_LUGAR));
        eventoViewHolder.email.setText(listaEvento.get(position).getCampos
                (Contract.Tablas.EVENTO_EMAIL));
        eventoViewHolder.nomPryRel.setText(listaEvento.get(position).getCampos
                (Contract.Tablas.EVENTO_NOMPROYECTOREL));
        eventoViewHolder.nomCliRel.setText(listaEvento.get(position).getCampos
                (Contract.Tablas.EVENTO_NOMCLIENTEREL));
        eventoViewHolder.fechaini.setText(JavaUtil.getDate(Long.parseLong(listaEvento.get(position).getCampos
                (Contract.Tablas.EVENTO_FECHAINIEVENTO))));
        eventoViewHolder.fechafin.setText(JavaUtil.getDate(Long.parseLong(listaEvento.get(position).getCampos
                (Contract.Tablas.EVENTO_FECHAFINEVENTO))));
        eventoViewHolder.horaini.setText(JavaUtil.getTime(Long.parseLong(listaEvento.get(position).getCampos
                (Contract.Tablas.EVENTO_HORAINIEVENTO))));
        eventoViewHolder.horafin.setText(JavaUtil.getTime(Long.parseLong(listaEvento.get(position).getCampos
                (Contract.Tablas.EVENTO_HORAFINEVENTO))));
        eventoViewHolder.pbar.setProgress(Integer.parseInt(listaEvento.get(position).getCampos
                (Contract.Tablas.EVENTO_COMPLETADA)));
        eventoViewHolder.porccompleta.setText(listaEvento.get(position).getCampos
                (Contract.Tablas.EVENTO_COMPLETADA));

        String tipoEvento = listaEvento.get(position).getCampos
                (Contract.Tablas.EVENTO_TIPOEVENTO);

        eventoViewHolder.fechaini.setVisibility(View.GONE);
        eventoViewHolder.fechafin.setVisibility(View.GONE);
        eventoViewHolder.horaini.setVisibility(View.GONE);
        eventoViewHolder.horafin.setVisibility(View.GONE);
        eventoViewHolder.btnllamada.setVisibility(View.GONE);
        eventoViewHolder.btnmapa.setVisibility(View.GONE);
        eventoViewHolder.btnemail.setVisibility(View.GONE);
        eventoViewHolder.telefono.setVisibility(View.GONE);
        eventoViewHolder.lugar.setVisibility(View.GONE);
        eventoViewHolder.email.setVisibility(View.GONE);

        switch (tipoEvento){

            case TAREA:
                break;

            case CITA:
                eventoViewHolder.lugar.setVisibility(View.VISIBLE);
                eventoViewHolder.fechaini.setVisibility(View.VISIBLE);
                eventoViewHolder.horaini.setVisibility(View.VISIBLE);
                eventoViewHolder.btnmapa.setVisibility(View.VISIBLE);
                break;

            case EMAIL:
                eventoViewHolder.btnemail.setVisibility(View.VISIBLE);
                eventoViewHolder.fechaini.setVisibility(View.VISIBLE);
                eventoViewHolder.horaini.setVisibility(View.VISIBLE);
                eventoViewHolder.email.setVisibility(View.VISIBLE);
                break;

            case LLAMADA:
                eventoViewHolder.telefono.setVisibility(View.VISIBLE);
                eventoViewHolder.fechaini.setVisibility(View.VISIBLE);
                eventoViewHolder.horaini.setVisibility(View.VISIBLE);
                eventoViewHolder.btnllamada.setVisibility(View.VISIBLE);
                break;

            case EVENTO:
                eventoViewHolder.fechaini.setVisibility(View.VISIBLE);
                eventoViewHolder.horaini.setVisibility(View.VISIBLE);
                eventoViewHolder.fechafin.setVisibility(View.VISIBLE);
                eventoViewHolder.horafin.setVisibility(View.VISIBLE);
                break;

        }
        if (listaEvento.get(position).getCampos
                (Contract.Tablas.EVENTO_RUTAFOTO)!=null) {
            eventoViewHolder.foto.setImageURI(Uri.parse(listaEvento.get(position).getCampos
                    (Contract.Tablas.EVENTO_RUTAFOTO)));
        }

        eventoViewHolder.btnllamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppActivity.hacerLlamada(activity,AppActivity.getAppContext()
                ,eventoViewHolder.telefono.getText().toString(),Common.permiso);
            }
        });

        eventoViewHolder.btnemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppActivity.enviarEmail(context,eventoViewHolder.email.getText().toString());


            }
        });

        eventoViewHolder.btnmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        eventoViewHolder.completa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                ContentValues valores = new ContentValues();

                valores.put(Contract.Tablas.EVENTO_COMPLETADA,"100");

                AppActivity.getAppContext().getContentResolver().update(Contract.crearUriTabla
                        (listaEvento.get(position).getCampos
                                (Contract.Tablas.EVENTO_ID_EVENTO),Contract.Tablas.TABLA_EVENTO)
                        ,valores,null,null);

            }
        });

        eventoViewHolder.btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context = AppActivity.getAppContext();

                if (context instanceof Activity) {
                    activity = (Activity) context;
                    icFragmentos = (ICFragmentos) activity;
                }

                bundle = new Bundle();
                bundle.putString(Contract.Tablas.EVENTO_ID_EVENTO, listaEvento.get(position).getCampos
                        (Contract.Tablas.EVENTO_ID_EVENTO));
                bundle.putString("namef",namef);
                icFragmentos.enviarBundleAFragment(bundle, new FragmentUDEvento());
                bundle = null;
            }
        });


    }

    @Override
    public int getItemCount() {

        return listaEvento.size();
    }

    @Override
    public void onClick(View v) {

        if (listener!=null){

            listener.onClick(v);
        }

    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder {

        TextView descripcion,fechaini,telefono,lugar,nomPryRel,nomCliRel,
                fechafin, horaini,horafin,porccompleta,email;
        ImageButton btnllamada, btnmapa, btnemail;
        ProgressBar pbar;
        ImageView foto,imgret;
        CheckBox completa;
        Button btneditar;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);

            descripcion = itemView.findViewById(R.id.tvdesclevento);
            fechaini = itemView.findViewById(R.id.tvfinilevento);
            fechafin = itemView.findViewById(R.id.tvffinlevento);
            horaini = itemView.findViewById(R.id.tvhinilevento);
            horafin = itemView.findViewById(R.id.tvhfinlevento);
            telefono = itemView.findViewById(R.id.tvtelefonoevento);
            lugar = itemView.findViewById(R.id.tvlugarevento);
            email = itemView.findViewById(R.id.tvemaillevento);
            nomPryRel = itemView.findViewById(R.id.tvnompryrellevento);
            nomCliRel = itemView.findViewById(R.id.tvnomclirelevento);
            porccompleta = itemView.findViewById(R.id.tvcompporcevento);
            btnllamada = itemView.findViewById(R.id.imgbtnllamadaevento);
            btnmapa = itemView.findViewById(R.id.imgbtnmapaevento);
            btnemail = itemView.findViewById(R.id.imgbtnemaillevento);
            pbar = itemView.findViewById(R.id.pbarevento);
            foto = itemView.findViewById(R.id.imgnevento);
            imgret = itemView.findViewById(R.id.imgretrasoevento);
            completa = itemView.findViewById(R.id.cBoxcompletlevento);
            btneditar = itemView.findViewById(R.id.btneditevento);

        }
    }



}