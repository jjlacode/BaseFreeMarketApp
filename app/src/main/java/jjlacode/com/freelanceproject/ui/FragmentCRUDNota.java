package jjlacode.com.freelanceproject.ui;
// Created by jjlacode on 8/05/19. 

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.android.AppActivity;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.crud.CRUDutil;
import jjlacode.com.freelanceproject.util.crud.FragmentCRUD;
import jjlacode.com.freelanceproject.util.media.MediaUtil;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;


public class FragmentCRUDNota extends FragmentCRUD implements CommonPry.Constantes,
        ContratoPry.Tablas , CommonPry.TiposNota, MediaPlayer.OnCompletionListener {


    private String idrelacionado;
    private Spinner sptiponota;
    private EditText descripcion;
    private ArrayList<String> listaTipoNota;
    private String tipoNota;
    private Button recVideo;
    private Button playVideo;
    private TextView fecha;
    private TextView tvTipo;
    private long fechaNota;
    private MediaPlayer player = new MediaPlayer();
    private MediaRecorder recorder = new MediaRecorder();
    private MediaUtil.MyVideoView videoView;
    private RelativeLayout rl;
    private ImageButton btncompartir;
    private File archivo;
    private ImageView b1;
    private ImageView b2;
    private ImageView b3;
    private TextView tv1;
    private boolean grabando;
    private boolean reproduciendo;


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

        if (idrelacionado != null) {

            lista = CRUDutil.setListaModelo(campos,NOTA_ID_RELACIONADO,idrelacionado,IGUAL);

        } else {

            lista = CRUDutil.setListaModelo(campos,NOTA_ID_RELACIONADO,null,IGUAL);

        }
    }

    @Override
    protected void actualizarConsultasRV() {


    }

    @Override
    protected void setNuevo() {

        btndelete.setVisibility(View.GONE);
        playVideo.setVisibility(View.GONE);
        recVideo.setVisibility(View.GONE);
        imagen.setVisibility(View.GONE);
        fecha.setVisibility(View.GONE);
        descripcion.setVisibility(View.GONE);
        btnsave.setVisibility(View.GONE);
        tvTipo.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        rl.setVisibility(View.GONE);
        sptiponota.setVisibility(View.VISIBLE);
        btncompartir.setVisibility(View.GONE);
        rl.setVisibility(View.GONE);
        b1.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);
        b3.setVisibility(View.GONE);
        tv1.setVisibility(View.GONE);

    }

    @Override
    protected void setDefectoMaestroDetalleSeparados() {
        super.setDefectoMaestroDetalleSeparados();

        if (idrelacionado != null && id!=null && modelo == null) {
            frPie.setVisibility(View.VISIBLE);
            btnsave.setVisibility(View.GONE);
            btndelete.setVisibility(View.GONE);
        }else if (idrelacionado!=null){
            rv.setVisibility(View.GONE);
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

        if (!origen.equals(INICIO)){
            btnback.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void setDatos() {

        if (id!=null) {
            modelo = CRUDutil.setModelo(campos, id);
            btnsave.setVisibility(View.VISIBLE);
            btndelete.setVisibility(View.VISIBLE);
        }

        descripcion.setText(modelo.getString(NOTA_DESCRIPCION));
        fechaNota = modelo.getLong(NOTA_FECHA);
        path = modelo.getString(NOTA_RUTA);
        fecha.setText(JavaUtil.getDateTime(fechaNota));
        playVideo.setVisibility(View.GONE);
        recVideo.setVisibility(View.GONE);
        imagen.setVisibility(View.GONE);
        sptiponota.setVisibility(View.GONE);
        tipoNota = modelo.getString(NOTA_TIPO);
        tvTipo.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        btncompartir.setVisibility(View.GONE);
        rl.setVisibility(View.GONE);
        b1.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);
        b3.setVisibility(View.GONE);
        tv1.setVisibility(View.GONE);

        if (tipoNota != null) {

            tvTipo.setText(tipoNota.toUpperCase());

            switch (tipoNota) {

                case NOTATEXTO:

                    descripcion.setVisibility(View.VISIBLE);

                    break;

                case NOTAAUDIO:

                    if (path!=null) {
                        btncompartir.setVisibility(View.VISIBLE);
                        descripcion.setVisibility(View.VISIBLE);
                    }
                        if (path == null) {

                            b3.setVisibility(View.VISIBLE);
                            tv1.setText("Listo para grabar");

                        }else {
                            b2.setVisibility(View.VISIBLE);
                            tv1.setText("Listo para reproducir");

                        }
                        tv1.setVisibility(View.VISIBLE);
                    if (!land) {
                        b1.setMinimumHeight((int) ((double) alto / 4));
                        b1.setMinimumWidth(ancho);
                    }else{
                        b1.setMinimumWidth((int) ((double) ancho / 4));
                        b1.setMinimumHeight((int) ((double) alto / 4));
                    }
                    if (!land) {
                        b2.setMinimumHeight((int) ((double) alto / 4));
                        b2.setMinimumWidth(ancho);
                    }else{
                        b2.setMinimumWidth((int) ((double) ancho / 4));
                        b2.setMinimumHeight((int) ((double) alto / 4));
                    }
                    if (!land) {
                        b3.setMinimumHeight((int) ((double) alto / 4));
                        b3.setMinimumWidth(ancho);
                    }else{
                        b3.setMinimumWidth((int) ((double) ancho / 4));
                        b3.setMinimumHeight((int) ((double) alto / 4));
                    }


                    break;

                case NOTAVIDEO:

                    if (path!=null) {

                        playVideo.setVisibility(View.GONE);
                        btncompartir.setVisibility(View.VISIBLE);
                        descripcion.setVisibility(View.VISIBLE);
                        videoView.setVisibility(View.VISIBLE);
                        rl.setVisibility(View.VISIBLE);
                        path = modelo.getString(NOTA_RUTA);
                        if (!land) {
                            videoView.setVideoSize(ancho, (int) ((double) alto / 2));
                        } else {
                            videoView.setVideoSize((int) ((double) ancho / 2), (int) ((double) alto / 2));
                        }

                        videoView.setVisibility(View.VISIBLE);

                        MediaController mc = new MediaController(contexto);
                        mc.setAnchorView(videoView);

                        videoView.setMediaController(mc);

                        videoView.setVideoURI(Uri.parse(path));
                        videoView.requestFocus();
                    }

                    break;

                case NOTAIMAGEN:

                    imagen.setVisibility(View.VISIBLE);
                    btncompartir.setVisibility(View.VISIBLE);

                    mediaUtil = new MediaUtil(contexto);

                    if (modelo.getString(NOTA_RUTA) != null) {
                        descripcion.setVisibility(View.VISIBLE);
                        path = modelo.getString(NOTA_RUTA);
                        System.out.println("path set Datos= " + path);
                        setImagenUri(mediaUtil, path);
                        if (!land) {
                            System.out.println("alto = " + alto);
                            System.out.println("ancho = " + ancho);
                            imagen.setMinimumHeight((int) ((double) alto / 2));
                            imagen.setMinimumWidth(ancho);
                        }else{
                            imagen.setMinimumWidth((int) ((double) ancho / 2));
                            imagen.setMinimumHeight((int) ((double) alto / 2));

                        }
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
                (getContext(), R.layout.spinner_item_tipo, listaTipoNota);

        sptiponota.setAdapter(adapterTipoNota);

        sptiponota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tipoNota = listaTipoNota.get(position);
                recVideo.setVisibility(View.GONE);
                imagen.setVisibility(View.GONE);
                descripcion.setVisibility(View.GONE);
                b1.setVisibility(View.GONE);
                b2.setVisibility(View.GONE);
                b3.setVisibility(View.GONE);
                tv1.setVisibility(View.GONE);

                switch (tipoNota) {

                    case NOTATEXTO:

                        descripcion.setVisibility(View.VISIBLE);
                        btnsave.setVisibility(View.VISIBLE);

                        break;

                    case NOTAAUDIO:

                        btnsave.setVisibility(View.GONE);
                        if (path == null) {

                            b3.setVisibility(View.VISIBLE);
                            tv1.setText("Listo para grabar");
                        }else {
                            b2.setVisibility(View.VISIBLE);
                            tv1.setText("Listo para reproducir");
                        }
                        tv1.setVisibility(View.VISIBLE);

                        if (!land) {
                            b1.setMinimumHeight((int) ((double) alto / 4));
                            b1.setMinimumWidth(ancho);
                        }else{
                            b1.setMinimumWidth((int) ((double) ancho / 4));
                            b1.setMinimumHeight((int) ((double) alto / 4));
                        }
                        if (!land) {
                            b2.setMinimumHeight((int) ((double) alto / 4));
                            b2.setMinimumWidth(ancho);
                        }else{
                            b2.setMinimumWidth((int) ((double) ancho / 4));
                            b2.setMinimumHeight((int) ((double) alto / 4));
                        }
                        if (!land) {
                            b3.setMinimumHeight((int) ((double) alto / 4));
                            b3.setMinimumWidth(ancho);
                        }else{
                            b3.setMinimumWidth((int) ((double) ancho / 4));
                            b3.setMinimumHeight((int) ((double) alto / 4));
                        }




                        break;

                    case NOTAVIDEO:

                        recVideo.setVisibility(View.VISIBLE);
                        btnsave.setVisibility(View.GONE);


                        break;

                    case NOTAIMAGEN:

                        imagen.setVisibility(View.VISIBLE);
                        btnsave.setVisibility(View.GONE);
                        mediaUtil = new MediaUtil(contexto);
                        setImagenUri(mediaUtil, path);
                        if (!land) {
                            imagen.setMinimumHeight((int) ((double) alto / 2));
                            imagen.setMinimumWidth(ancho);
                        }else{
                            imagen.setMinimumWidth((int) ((double) ancho / 2));
                            imagen.setMinimumHeight((int) ((double) alto / 2));

                        }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Video play path = " + path);


                if (!land) {
                    videoView.setVideoSize(ancho, (int) ((double) alto / 2));
                }else{
                    videoView.setVideoSize((int) ((double) ancho / 2), (int) ((double) alto / 2) );
                }

                videoView.setVisibility(View.VISIBLE);
                rl.setVisibility(View.VISIBLE);

                MediaController mc = new MediaController(contexto);
                mc.setAnchorView(videoView);

                videoView.setMediaController(mc);

                videoView.setVideoURI(Uri.parse(path));
                videoView.requestFocus();

                videoView.start();

            }
        });

        recVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onUpdate();
                mediaUtil = new MediaUtil(contexto);
                try {
                    startActivityForResult(mediaUtil.recordVideoIntent(), VIDEORECORD);
                } catch (IOException e) {

                    e.printStackTrace();
                }
                mediaUtil.addVideoToGallery();
                path = mediaUtil.getPath(mediaUtil.getVideoUri());
                System.out.println("Video path = " + path);
                onUpdate();

            }
        });

        btncompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tipo = null;

                if (tipoNota.equals(NOTAIMAGEN)){

                    tipo = "image/*";

                }else if (tipoNota.equals(NOTAVIDEO)){

                    tipo = "video/*";

                }else if (tipoNota.equals(NOTAAUDIO)){

                    tipo = "audio/*";
                }

                AppActivity.compartir(path,tipo);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detener(v);
                b1.setVisibility(View.GONE);
                b2.setVisibility(View.VISIBLE);

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reproducir(v);
                b3.setVisibility(View.GONE);
                b1.setVisibility(View.VISIBLE);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                grabar(v);
                b3.setVisibility(View.GONE);
                b1.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.nota;
        tituloPlural = R.string.notas;
    }

    @Override
    protected void setInicio() {

        sptiponota = view.findViewById(R.id.sptiponota);
        descripcion = view.findViewById(R.id.etdesc_nota);
        imagen = view.findViewById(R.id.imagen_nota);
        b1 = view.findViewById(R.id.btn_grabar_audio_stop);
        playVideo = view.findViewById(R.id.btn_play_video);
        b2 = view.findViewById(R.id.btn_play_audio);
        b3 = view.findViewById(R.id.btn_grabar_audio);
        tv1 = view.findViewById(R.id.tvaudio);
        recVideo = view.findViewById(R.id.btn_grabar_video);
        fecha = view.findViewById(R.id.tvfechanota);
        tvTipo = view.findViewById(R.id.tvtiponota);
        btncompartir = view.findViewById(R.id.btncompartirnota);
        rl = view.findViewById(R.id.rl_videoview);
        videoView = new MediaUtil.MyVideoView(activityBase.getApplicationContext());
        rl.addView(videoView);

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
        layoutItem = R.layout.item_list_nota;

    }

    @Override
    protected void setContenedor() {

        setDato(NOTA_DESCRIPCION, descripcion.getText().toString());
        if (path!=null) {
            setDato(NOTA_RUTA, path);
        }

        if (id==null) {
            setDato(NOTA_ID_RELACIONADO, idrelacionado);
            setDato(NOTA_FECHA, JavaUtil.hoy());
            setDato(NOTA_FECHAF,JavaUtil.getDateTime(JavaUtil.hoy()));
            setDato(NOTA_TIPO, tipoNota);
        }

    }

    @Override
    protected void setcambioFragment() {

        System.out.println("actual = " + tituloPlural);
        System.out.println("subTitulo = " + subTitulo);

        if (id!=null){

            id=null;
            modelo=null;
            listaRV();

        }else if (origen.equals(EVENTO)) {

            enviarBundle();
            bundle.putString(ID, idrelacionado);
            bundle.putSerializable(MODELO, CRUDutil.setModelo(CAMPOS_EVENTO,idrelacionado));
            bundle.putString(ACTUAL, origen);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());
        } else if (origen.equals(PRESUPUESTO)) {

            enviarBundle();
            bundle.putString(ID, idrelacionado);
            bundle.putSerializable(MODELO, CRUDutil.setModelo(CAMPOS_PROYECTO,idrelacionado));
            bundle.putString(ACTUAL, origen);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
        } else if (origen.equals(PROYECTO)) {

            enviarBundle();
            bundle.putString(ID, idrelacionado);
            bundle.putSerializable(MODELO, CRUDutil.setModelo(CAMPOS_PROYECTO,idrelacionado));
            bundle.putString(ACTUAL, origen);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
        } else if (origen.equals(CLIENTE)) {

            enviarBundle();
            bundle.putString(ID, idrelacionado);
            bundle.putSerializable(MODELO, CRUDutil.setModelo(CAMPOS_CLIENTE,idrelacionado));
            bundle.putString(ACTUAL, origen);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDCliente());
        } else if (origen.equals(PROSPECTO)) {

            enviarBundle();
            bundle.putString(ID, idrelacionado);
            bundle.putSerializable(MODELO, CRUDutil.setModelo(CAMPOS_CLIENTE,idrelacionado));
            bundle.putString(ACTUAL, origen);
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
            ImageView imagen;

            tipoNota = view.findViewById(R.id.tvtipo_lnota);
            descripcion = view.findViewById(R.id.tvdesc_lnota);
            imagen = view.findViewById(R.id.imagen_lnota);
            fecha = view.findViewById(R.id.tvfechalnota);
            descripcion.setText(entrada.get(posicion).getString(NOTA_DESCRIPCION));
            fecha.setText(JavaUtil.getDateTime(entrada.get(posicion).getLong(NOTA_FECHA)));
            String tipo = entrada.get(posicion).getString(NOTA_TIPO);
            tipoNota.setText(tipo);
            tipoNota.setVisibility(View.GONE);
            super.setEntradas(posicion, view, entrada);
        }
    }


        public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView descripcion, fecha, tipoNota;
        ImageView imagen;

        public ViewHolderRV(View itemView) {
            super(itemView);
            tipoNota = itemView.findViewById(R.id.tvtipo_lnota);
            descripcion = itemView.findViewById(R.id.tvdesc_lnota);
            imagen = itemView.findViewById(R.id.imagen_lnota);
            fecha = itemView.findViewById(R.id.tvfechalnota);

        }

        @Override
        public void bind(Modelo modelo) {

            descripcion.setText(modelo.getString(NOTA_DESCRIPCION));
            fecha.setText(JavaUtil.getDateTime(modelo.getLong(NOTA_FECHA)));
            String tipo = modelo.getString(NOTA_TIPO);
            tipoNota.setText(tipo);
            tipoNota.setVisibility(View.GONE);
            //imagenTarea.setVisibility(View.GONE);

            if (tipo != null) {

                switch (tipo) {

                    case NOTATEXTO:

                        imagen.setImageResource(R.drawable.ic_format_list_numbered_black_24dp);

                        break;

                    case NOTAAUDIO:

                        imagen.setImageResource(R.drawable.ic_mic_black_24dp);
                        if (modelo.getString(NOTA_RUTA) != null) {
                            path = modelo.getString(NOTA_RUTA);
                        }

                        break;

                    case NOTAVIDEO:

                        imagen.setImageResource(R.drawable.ic_videocam_black_24dp);
                        if (modelo.getString(NOTA_RUTA) != null) {
                            path = modelo.getString(NOTA_RUTA);
                        }

                        break;

                    case NOTAIMAGEN:

                        if (modelo.getString(NOTA_RUTA) != null) {
                            imagen.setVisibility(View.VISIBLE);
                            path = modelo.getString(NOTA_RUTA);
                            MediaUtil imagenUtil = new MediaUtil(AppActivity.getAppContext());
                            imagenUtil.setImageUriCircle(path, imagen);
                        } else {
                            //imagenTarea.setVisibility(View.GONE);
                            imagen.setImageResource(R.drawable.ic_add_a_photo_black_24dp);
                        }
                }
            }

            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    public void grabar(View v) {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        File pathAudio = new File(Environment.getExternalStorageDirectory()
                .getPath());
        try {
            archivo = File.createTempFile("temporal", ".3gp", pathAudio);
        } catch (IOException e) {
        }
        path = archivo.getAbsolutePath();
        recorder.setOutputFile(path);
        try {
            recorder.prepare();
        } catch (IOException e) {
        }
        recorder.start();
        grabando = true;
        tv1.setText("Grabando");
    }

    public void detener(View v) {

        if (grabando) {
            recorder.stop();
            grabando = false;
            recorder.release();
            onUpdate();
            player = new MediaPlayer();
            player.setOnCompletionListener(this);
            try {
                player.setDataSource(path);
            } catch (IOException e) {
            }
            try {
                player.prepare();
            } catch (IOException e) {
            }

        }
        if (reproduciendo){
            player.pause();
            reproduciendo = false;
        }
        tv1.setText("Listo para reproducir");
    }

    public void reproducir(View v) {
        player.start();
        reproduciendo = true;
        tv1.setText("Reproduciendo");
    }

    public void onCompletion(MediaPlayer mp) {
        b1.setVisibility(View.GONE);
        b2.setVisibility(View.VISIBLE);
        tv1.setText("Listo");
    }
}