package jjlacode.com.freelanceproject.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import jjlacode.com.freelanceproject.interfaces.ICFragmentos;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.model.Modelo;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;
import jjlacode.com.freelanceproject.utilities.Common;
import jjlacode.com.utilidades.Utilidades;

public class FragmentEvento extends Fragment implements Contract.Tablas {


    View vista;
    RecyclerView rvEvento;
    ArrayList<Modelo> objListaEvento;
    private Bundle bundle;
    private String namef;
    private ICFragmentos icFragmentos;
    private Activity activity;

    public FragmentEvento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_evento, container, false);

        bundle = getArguments();

        if (bundle!=null){

            namef = bundle.getString("namef");
            bundle = null;
        }

        rvEvento = vista.findViewById(R.id.rvEvento);
        rvEvento.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Modelo> listaEventos = QueryDB.queryList(CAMPOS_EVENTO, null, null);

        AdaptadorEventoInt adaptadorEvento = new AdaptadorEventoInt(listaEventos,namef);
        rvEvento.setAdapter(adaptadorEvento);

        rvEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idEvento = objListaEvento.get
                        (rvEvento.getChildAdapterPosition(v)).getCampos(Contract.Tablas.EVENTO_ID_EVENTO);

                Modelo evento = QueryDB.queryObject(CAMPOS_EVENTO,idEvento);

                bundle =new Bundle();
                bundle.putSerializable(TABLA_EVENTO,evento);
            }
        });
        return vista;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.activity = (Activity) context;
            icFragmentos = (ICFragmentos) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class AdaptadorEventoInt extends RecyclerView.Adapter<FragmentEvento.AdaptadorEventoInt.EventoViewHolder>
            implements Common.TiposEvento,View.OnClickListener {

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
            eventoViewHolder.fechaini.setText(Utilidades.getDate(Long.parseLong(listaEvento.get(position).getCampos
                    (Contract.Tablas.EVENTO_FECHAINIEVENTO))));
            eventoViewHolder.fechafin.setText(Utilidades.getDate(Long.parseLong(listaEvento.get(position).getCampos
                    (Contract.Tablas.EVENTO_FECHAFINEVENTO))));
            eventoViewHolder.horaini.setText(Utilidades.getTime(Long.parseLong(listaEvento.get(position).getCampos
                    (Contract.Tablas.EVENTO_HORAINIEVENTO))));
            eventoViewHolder.horafin.setText(Utilidades.getTime(Long.parseLong(listaEvento.get(position).getCampos
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

                    Common.AppActivity.hacerLlamada(Common.AppActivity.getAppContext()
                            ,eventoViewHolder.telefono.getText().toString());
                }
            });

            eventoViewHolder.btnemail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



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

                    Common.AppActivity.getAppContext().getContentResolver().update(Contract.crearUriTabla
                                    (listaEvento.get(position).getCampos
                                            (Contract.Tablas.EVENTO_ID_EVENTO),TABLA_EVENTO)
                            ,valores,null,null);

                }
            });

            eventoViewHolder.btneditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String idEvento = listaEvento.get(position).getCampos
                            (Contract.Tablas.EVENTO_ID_EVENTO);

                    Modelo evento = QueryDB.queryObject(CAMPOS_EVENTO,idEvento);

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
}
