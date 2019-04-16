package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import jjlacode.com.androidutils.AppActivity;
import jjlacode.com.androidutils.FragmentBase;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.utilities.CommonPry;

public class FragmentEvento extends FragmentBase implements ContratoPry.Tablas {

    private static ConsultaBD consulta = new ConsultaBD();
    private RecyclerView rvEvento;
    private ArrayList<Modelo> listaEventos;


    public FragmentEvento() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evento, container, false);

        bundle = getArguments();

        if (bundle!=null){

            namef = bundle.getString("namef");
            bundle = null;
        }

        rvEvento = view.findViewById(R.id.rvEvento);
        rvEvento.setLayoutManager(new LinearLayoutManager(getContext()));

        listaEventos = consulta.queryList(CAMPOS_EVENTO);

        AdaptadorEventoInt adaptadorEvento = new AdaptadorEventoInt(listaEventos,namef);
        rvEvento.setAdapter(adaptadorEvento);

        return view;
    }

    class AdaptadorEventoInt extends RecyclerView.Adapter<FragmentEvento.AdaptadorEventoInt.EventoViewHolder>
            implements CommonPry.TiposEvento,View.OnClickListener {

        ArrayList<Modelo> listaEvento;
        private View.OnClickListener listener;
        private String namef;

        public AdaptadorEventoInt(ArrayList<Modelo> listaEvento, String namef) {

            this.listaEvento = listaEvento;
            this.namef = namef;
        }

        @NonNull
        @Override
        public FragmentEvento.AdaptadorEventoInt.EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_evento, null, false);

            view.setOnClickListener(this);


            return new AdaptadorEventoInt.EventoViewHolder(view);
        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull final FragmentEvento.AdaptadorEventoInt.EventoViewHolder eventoViewHolder, final int position) {

            eventoViewHolder.tipo.setText(listaEvento.get(position).getString(EVENTO_TIPOEVENTO).toUpperCase());
            eventoViewHolder.descripcion.setText(listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_DESCRIPCION));
            eventoViewHolder.telefono.setText(listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_TELEFONO));
            eventoViewHolder.lugar.setText(listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_LUGAR));
            eventoViewHolder.email.setText(listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_EMAIL));
            eventoViewHolder.nomPryRel.setText(listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_NOMPROYECTOREL));
            eventoViewHolder.nomCliRel.setText(listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_NOMCLIENTEREL));
            eventoViewHolder.fechaini.setText(JavaUtil.getDate(Long.parseLong(listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_FECHAINIEVENTO))));
            eventoViewHolder.fechafin.setText(JavaUtil.getDate(Long.parseLong(listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_FECHAFINEVENTO))));
            eventoViewHolder.horaini.setText(JavaUtil.getTime(Long.parseLong(listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_HORAINIEVENTO))));
            eventoViewHolder.horafin.setText(JavaUtil.getTime(Long.parseLong(listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_HORAFINEVENTO))));
            eventoViewHolder.pbar.setProgress(Integer.parseInt(listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_COMPLETADA)));
            eventoViewHolder.porccompleta.setText(listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_COMPLETADA));

            String tipoEvento = listaEvento.get(position).getCampos
                    (ContratoPry.Tablas.EVENTO_TIPOEVENTO);

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
                    (ContratoPry.Tablas.EVENTO_RUTAFOTO)!=null) {
                eventoViewHolder.foto.setImageURI(Uri.parse(listaEvento.get(position).getCampos
                        (ContratoPry.Tablas.EVENTO_RUTAFOTO)));
            }

            long retraso = JavaUtil.hoy()-listaEvento.get(position).getLong(EVENTO_FECHAINIEVENTO);
            if (retraso > 3 * CommonPry.DIASLONG){eventoViewHolder.card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_notok));}
            else if (retraso > CommonPry.DIASLONG){eventoViewHolder.card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_acept));}
            else {eventoViewHolder.card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_ok));}//imgret.setImageResource(R.drawable.alert_box_v);}
            if(tipoEvento.equals(TAREA))
            {eventoViewHolder.card.setCardBackgroundColor(getResources().getColor(R.color.Color_card_tarea));}

            eventoViewHolder.btnllamada.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppActivity.hacerLlamada(AppActivity.getAppContext()
                            ,eventoViewHolder.telefono.getText().toString());
                }
            });

            eventoViewHolder.btnemail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppActivity.enviarEmail(getContext(),eventoViewHolder.email.getText().toString());

                }
            });

            eventoViewHolder.btnmapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!eventoViewHolder.lugar.getText().toString().equals("")){

                        Intent i= AppActivity.viewOnMapA(eventoViewHolder.lugar.getText().toString());
                        startActivity(i);
                    }




                }
            });


            eventoViewHolder.completa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    ContentValues valores = new ContentValues();

                    consulta.putDato(valores,CAMPOS_EVENTO,EVENTO_COMPLETADA,"100");
                    consulta.updateRegistro(TABLA_EVENTO,listaEvento.get(position).getString
                            (EVENTO_ID_EVENTO),valores);

                }
            });

            eventoViewHolder.btneditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String idEvento = listaEvento.get(position).getCampos
                            (ContratoPry.Tablas.EVENTO_ID_EVENTO);

                    Modelo evento = consulta.queryObject(CAMPOS_EVENTO,idEvento);

                    bundle = new Bundle();
                    bundle.putSerializable(TABLA_EVENTO,evento);
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

        class EventoViewHolder extends RecyclerView.ViewHolder {

            TextView descripcion,fechaini,telefono,lugar,nomPryRel,nomCliRel,
                    fechafin, horaini,horafin,porccompleta,email,tipo;
            ImageButton btnllamada, btnmapa, btnemail;
            ProgressBar pbar;
            ImageView foto,imgret;
            CheckBox completa;
            Button btneditar;
            CardView card;

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
                foto = itemView.findViewById(R.id.imglevento);
                completa = itemView.findViewById(R.id.cBoxcompletlevento);
                btneditar = itemView.findViewById(R.id.btneditevento);
                tipo = itemView.findViewById(R.id.tvtipolevento);
                card = itemView.findViewById(R.id.cardlevento);

            }
        }



    }
}
