package jjlacode.com.freelanceproject.ui;
// Created by jjlacode on 8/05/19. 

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.AppActivity;
import jjlacode.com.freelanceproject.util.BaseViewHolder;
import jjlacode.com.freelanceproject.util.FragmentCRUD;
import jjlacode.com.freelanceproject.util.ImagenUtil;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.ListaAdaptadorFiltro;
import jjlacode.com.freelanceproject.util.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.CommonPry;
import jjlacode.com.freelanceproject.util.TipoViewHolder;

import static android.app.Activity.RESULT_OK;


public class FragmentCRUDNota extends FragmentCRUD implements CommonPry.Constantes,
        ContratoPry.Tablas , CommonPry.TiposNota {


    private String idrelacionado;
    private Spinner sptiponota;
    private EditText descripcion;
    private ArrayList<String> listaTipoNota;
    private String tipoNota;
    private Button playAudio;
    private Button recAudio;
    private Button recVideo;
    private Button playVideo;
    private TextView fecha;
    private TextView tvTipo;
    private long fechaNota;
    private MediaPlayer play = new MediaPlayer();
    private MediaRecorder rec = new MediaRecorder();
    private boolean reproduciendo;
    private boolean grabando;

    public FragmentCRUDNota() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {

        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroRV setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroRV(context, layoutItem, lista, campos);
    }

    @Override
    protected void setLista() {

    }

    @Override
    protected void actualizarConsultasRV() {

        if (idrelacionado != null) {

            setListaModelo(NOTA_ID_RELACIONADO,idrelacionado,IGUAL);
            //lista = consulta.queryList(CAMPOS_NOTA, NOTA_ID_RELACIONADO, idrelacionado, null, IGUAL, null);

        } else {

            setListaModelo(NOTA_ID_RELACIONADO,null,IGUAL);
            //lista = consulta.queryList(CAMPOS_NOTA, NOTA_ID_RELACIONADO, null, null, IGUAL, null);
        }
    }

    @Override
    protected void setNuevo() {

        btndelete.setVisibility(View.GONE);
        playAudio.setVisibility(View.GONE);
        playVideo.setVisibility(View.GONE);
        recAudio.setVisibility(View.GONE);
        recVideo.setVisibility(View.GONE);
        imagen.setVisibility(View.GONE);
        fecha.setVisibility(View.GONE);
        descripcion.setVisibility(View.GONE);
        btnsave.setVisibility(View.GONE);
        tvTipo.setVisibility(View.GONE);


    }

    @Override
    protected void setDefectoMaestroDetalleSeparados() {
        super.setDefectoMaestroDetalleSeparados();

        if (idrelacionado != null && !nuevo && modelo == null) {
            frPie.setVisibility(View.VISIBLE);
            btnsave.setVisibility(View.GONE);
            btndelete.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setMaestroDetallePort() {
        maestroDetalleSeparados = true;
    }

    @Override
    protected void setMaestroDetalleLand() {
        maestroDetalleSeparados = false;

    }

    @Override
    protected void setMaestroDetalleTabletLand() {
        maestroDetalleSeparados = false;

    }

    @Override
    protected void setMaestroDetalleTabletPort() {
        maestroDetalleSeparados = false;

    }

    @Override
    protected void setTabla() {

        tabla = TABLA_NOTA;

    }

    @Override
    protected void setTablaCab() {

        tablaCab = null;
    }

    @Override
    protected void setContext() {

        contexto = getContext();
    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_NOTA;
    }

    @Override
    protected void setCampoID() {

        campoID = NOTA_ID_NOTA;
    }

    @Override
    protected void setBundle() {

        idrelacionado = bundle.getString(IDREL);


    }

    @Override
    protected void setDatos() {

        descripcion.setText(modelo.getString(NOTA_DESCRIPCION));
        fechaNota = modelo.getLong(NOTA_FECHA);
        fecha.setText(JavaUtil.getDateTime(fechaNota));
        playAudio.setVisibility(View.GONE);
        playVideo.setVisibility(View.GONE);
        recAudio.setVisibility(View.GONE);
        recVideo.setVisibility(View.GONE);
        imagen.setVisibility(View.GONE);
        sptiponota.setVisibility(View.GONE);
        tipoNota = modelo.getString(NOTA_TIPO);
        tvTipo.setVisibility(View.VISIBLE);


        if (tipoNota != null) {

            tvTipo.setText(tipoNota.toUpperCase());

            switch (tipoNota) {

                case NOTATEXTO:

                    break;

                case NOTAAUDIO:

                    playAudio.setVisibility(View.VISIBLE);
                    recAudio.setVisibility(View.VISIBLE);
                    path = modelo.getString(NOTA_RUTA);
                    break;

                case NOTAVIDEO:

                    playVideo.setVisibility(View.VISIBLE);
                    recVideo.setVisibility(View.VISIBLE);
                    path = modelo.getString(NOTA_RUTA);

                    break;

                case NOTAIMAGEN:

                    imagen.setVisibility(View.VISIBLE);

                    if (modelo.getString(NOTA_RUTA) != null) {
                        path = modelo.getString(NOTA_RUTA);
                        setImagenUri(contexto,path, imagen);
                    }

            }
        }

    }

    @Override
    protected void setAcciones() {

        listaTipoNota = new ArrayList<>();
        listaTipoNota.add("Elija el tipo de Nota");
        listaTipoNota.add(NOTATEXTO);
        listaTipoNota.add(NOTAAUDIO);
        listaTipoNota.add(NOTAVIDEO);
        listaTipoNota.add(NOTAIMAGEN);

        ArrayAdapter<String> adapterTipoNota = new ArrayAdapter<>
                (getContext(), R.layout.spinner_item_tipo_evento, listaTipoNota);

        sptiponota.setAdapter(adapterTipoNota);

        sptiponota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tipoNota = listaTipoNota.get(position);
                descripcion.setVisibility(View.VISIBLE);
                btnsave.setVisibility(View.VISIBLE);

                switch (tipoNota) {

                    case NOTATEXTO:

                        break;

                    case NOTAAUDIO:

                        recAudio.setVisibility(View.VISIBLE);

                        break;

                    case NOTAVIDEO:

                        recVideo.setVisibility(View.VISIBLE);

                        break;

                    case NOTAIMAGEN:

                        imagen.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reproduciendo = !reproduciendo;
                AppActivity.reproducirAudio(play, path, reproduciendo);
                if (reproduciendo) {
                    playAudio.setText("Stop");
                } else {
                    playAudio.setText("Reproducir audio");
                }

            }
        });

        recAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                grabando = !grabando;
                AppActivity.grabarAudio(rec, grabando);
                if (grabando) {
                    recAudio.setText("Stop");
                } else {
                    recAudio.setText("Grabar audio");
                }
            }
        });

        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        recVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void setInicio() {

        sptiponota = view.findViewById(R.id.sptiponota);
        descripcion = view.findViewById(R.id.etdesc_nota);
        imagen = view.findViewById(R.id.imagen_nota);
        playAudio = view.findViewById(R.id.btn_play_audio);
        playVideo = view.findViewById(R.id.btn_play_video);
        recAudio = view.findViewById(R.id.btn_grabar_audio);
        recVideo = view.findViewById(R.id.btn_grabar_video);
        fecha = view.findViewById(R.id.tvfechanota);
        tvTipo = view.findViewById(R.id.tvtiponota);

    }

    @Override
    protected boolean onDelete() {

        if (delete()) {
            if (tablaCab == null) {
                id = null;
                modelo = null;
            } else {
                secuencia = 0;
                modelo = null;
            }

            onResume();

            return true;
        }

        return false;
    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_nota;
        layoutitem = R.layout.item_list_nota;

    }

    @Override
    protected void setContenedor() {

        setDato(NOTA_DESCRIPCION, descripcion.getText().toString());
        setDato(NOTA_ID_RELACIONADO, idrelacionado);
        setDato(NOTA_RUTA, path);
        setDato(NOTA_FECHA, JavaUtil.hoy());
        setDato(NOTA_TIPO, tipoNota);
    }

    @Override
    protected void setcambioFragment() {

        System.out.println("namef = " + namef);
        if (namesubclass.equals(EVENTO)) {

            enviarBundle();
            bundle.putString(ID, idrelacionado);
            bundle.putSerializable(MODELO, null);
            System.out.println("Enviando a Evento");
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
        } else if (namesubclass.equals(AGENDA)) {

            enviarBundle();
            bundle.putString(NAMESUB, getString(R.string.notas));
            icFragmentos.enviarBundleAFragment(bundle, new FragmentAgenda());
        } else if (namesubclass.equals(PRESUPUESTO)) {

            enviarBundle();
            bundle.putString(ID, idrelacionado);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
        } else if (namesubclass.equals(PROYECTO)) {

            enviarBundle();
            bundle.putString(ID, idrelacionado);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
        } else if (namesubclass.equals(CLIENTE)) {

            enviarBundle();
            bundle.putString(ID, idrelacionado);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDCliente());
        } else if (namesubclass.equals(PROSPECTO)) {

            enviarBundle();
            bundle.putString(ID, idrelacionado);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDCliente());
        }


    }

    public class AdaptadorFiltroRV extends ListaAdaptadorFiltroRV{

        public AdaptadorFiltroRV(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<Modelo> entrada) {

            TextView descripcion, fecha, tipoNota;
            Button playAudio, playVideo;
            ImageView imagen;

            tipoNota = view.findViewById(R.id.tvtipo_lnota);
            descripcion = view.findViewById(R.id.tvdesc_lnota);
            imagen = view.findViewById(R.id.imagen_lnota);
            playAudio = view.findViewById(R.id.btn_play_audiol);
            playVideo = view.findViewById(R.id.btn_play_videol);
            fecha = view.findViewById(R.id.tvfechalnota);
            super.setEntradas(posicion, view, entrada);
        }
    }


        public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView descripcion, fecha, tipoNota;
        Button playAudio, playVideo;
        ImageView imagen;

        public ViewHolderRV(View itemView) {
            super(itemView);
            tipoNota = itemView.findViewById(R.id.tvtipo_lnota);
            descripcion = itemView.findViewById(R.id.tvdesc_lnota);
            imagen = itemView.findViewById(R.id.imagen_lnota);
            playAudio = itemView.findViewById(R.id.btn_play_audiol);
            playVideo = itemView.findViewById(R.id.btn_play_videol);
            fecha = itemView.findViewById(R.id.tvfechalnota);
        }

        @Override
        public void bind(Modelo modelo) {

            descripcion.setText(modelo.getString(NOTA_DESCRIPCION));
            fecha.setText(JavaUtil.getDateTime(modelo.getLong(NOTA_FECHA)));
            String tipo = modelo.getString(NOTA_TIPO);
            tipoNota.setText(tipo);
            playAudio.setVisibility(View.GONE);
            playVideo.setVisibility(View.GONE);
            imagen.setVisibility(View.GONE);

            if (tipo != null) {

                switch (tipo) {

                    case NOTATEXTO:

                        break;

                    case NOTAAUDIO:

                        if (modelo.getString(NOTA_RUTA) != null) {
                            playAudio.setVisibility(View.VISIBLE);
                            path = modelo.getString(NOTA_RUTA);
                        } else {
                            playAudio.setVisibility(View.GONE);
                        }

                        break;

                    case NOTAVIDEO:

                        if (modelo.getString(NOTA_RUTA) != null) {
                            playVideo.setVisibility(View.VISIBLE);
                            path = modelo.getString(NOTA_RUTA);
                        } else {
                            playVideo.setVisibility(View.GONE);
                        }

                        break;

                    case NOTAIMAGEN:

                        if (modelo.getString(NOTA_RUTA) != null) {
                            imagen.setVisibility(View.VISIBLE);
                            path = modelo.getString(NOTA_RUTA);
                            ImagenUtil imagenUtil = new ImagenUtil(AppActivity.getAppContext());
                            imagenUtil.setImageUriCircle(path, imagen);
                        } else {
                            imagen.setVisibility(View.GONE);
                        }
                }
            }

            playAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    reproduciendo = !reproduciendo;
                    AppActivity.reproducirAudio(play, path, reproduciendo);
                    if (reproduciendo) {
                        playAudio.setText("Stop");
                    } else {
                        playAudio.setText("Reproducir audio");
                    }
                }
            });

            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return null;
        }
    }
}