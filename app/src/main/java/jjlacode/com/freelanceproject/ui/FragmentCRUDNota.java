package jjlacode.com.freelanceproject.ui;
// Created by jjlacode on 8/05/19. 

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.android.AppActivity;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.android.controls.EditMaterial;
import jjlacode.com.freelanceproject.util.crud.CRUDutil;
import jjlacode.com.freelanceproject.util.crud.FragmentCRUD;
import jjlacode.com.freelanceproject.util.media.AudioPlayRec;
import jjlacode.com.freelanceproject.util.media.MediaUtil;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.crud.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;


public class FragmentCRUDNota extends FragmentCRUD implements CommonPry.Constantes,
        ContratoPry.Tablas , CommonPry.TiposNota{


    private String idrelacionado;
    private EditMaterial titulo;
    private EditMaterial descripcion;
    private ArrayList<String> listaTipoNota;
    private ImageView notaTexto;
    private ImageView notaAudio;
    private ImageView notaVideo;
    private ImageView notaImagen;
    private String tipoNota;
    private ImageView recVideo;
    private ImageView playVideo;
    private ImageView ampliar;
    private TextView fecha;
    private TextView tvTipo;
    private long fechaNota;
    private MediaUtil.MyVideoView videoView;
    private RelativeLayout rl;
    private ImageButton btncompartir;
    private AudioPlayRec audioPlayRec;
    private boolean ampliado;


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

        if (!origen.equals(DIARIO)){
            visibleSoloBtnBack();
        }
    }

    @Override
    protected void setNuevo() {

        allGone();

        imagenPantalla(recVideo,4,2);
        imagenPantalla(playVideo,8,4);

        int padimg = 10;
        if (!land){
            padimg = (int) ((double)ancho/10);
        }else {
            padimg = (int) ((double)ancho/20);
        }

        imagenPantalla(notaTexto,4,2);
        notaTexto.setPadding(padimg, padimg,padimg,0);
        imagenPantalla(notaAudio,4,2);
        notaAudio.setPadding(0, padimg,padimg,0);
        imagenPantalla(notaVideo,4,2);
        notaVideo.setPadding(padimg, padimg,padimg,0);
        imagenPantalla(notaImagen,4,2);
        notaImagen.setPadding(0, padimg,padimg,0);

        visible(notaTexto);
        visible(notaAudio);
        visible(notaVideo);
        visible(notaImagen);

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
    protected void setTabla() {

        tabla = TABLA_NOTA;

    }

    @Override
    protected void setBundle() {

        idrelacionado = bundle.getString(IDREL);

    }

    @Override
    protected void setDatos() {

        if (id!=null) {
            modelo = CRUDutil.setModelo(campos, id);
            btnsave.setVisibility(View.VISIBLE);
            btndelete.setVisibility(View.VISIBLE);
        }

        descripcion.setText(modelo.getString(NOTA_DESCRIPCION));
        titulo.setText(modelo.getString(NOTA_TITULO));
        fechaNota = modelo.getLong(NOTA_FECHA);
        path = modelo.getString(NOTA_RUTA);
        fecha.setText(JavaUtil.getDateTime(fechaNota));
        playVideo.setVisibility(View.GONE);
        recVideo.setVisibility(View.GONE);
        imagen.setVisibility(View.GONE);
        tipoNota = modelo.getString(NOTA_TIPO);
        tvTipo.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        btncompartir.setVisibility(View.GONE);
        rl.setVisibility(View.GONE);

        if (tipoNota != null) {

            tvTipo.setText(tipoNota.toUpperCase());

            switch (tipoNota) {

                case NOTATEXTO:

                    visible(titulo);
                    visible(descripcion);

                    break;

                case NOTAAUDIO:

                    if (path!=null) {
                        visible(btncompartir);
                        visible(descripcion);
                        visible(titulo);
                    }
                    audioPlayRec = new AudioPlayRec(activityBase,view,contexto,R.id.btn_grabar_audio,
                            R.id.btn_play_audio, R.id.btn_audio_stop, R.id.btn_audio_pausa,
                            R.id.tvaudio, R.id.pbaraudio);

                    audioPlayRec.setPath(path);
                    audioPlayRec.init();
                    audioPlayRec.setListener(new AudioPlayRec.Listeners() {
                        @Override
                        public void onRec(String p) {
                            path = p;
                        }

                        @Override
                        public void onEndRec() {

                            onUpdate();
                        }
                    });

                    break;

                case NOTAVIDEO:

                    if (path!=null) {

                        visible(playVideo);
                        visible(titulo);
                        visible(btncompartir);
                        visible(descripcion);
                        imagenPantalla(playVideo,4,2);

                    }

                    break;

                case NOTAIMAGEN:

                    visible(imagen);
                    visible(titulo);
                    visible(descripcion);
                    visible(btncompartir);
                    visible(ampliar);
                    imagenPantalla(ampliar,10,6);

                    mediaUtil = new MediaUtil(contexto);

                    if (modelo.getString(NOTA_RUTA) != null) {
                        path = modelo.getString(NOTA_RUTA);
                        System.out.println("path set Datos= " + path);
                        setImagenUri(mediaUtil, path);
                        if (!ampliado) {
                            imagenPantalla(4, 2);
                        }else{
                            imagenPantalla(2,1);
                        }
                    }

            }
        }

    }

    @Override
    protected void setAcciones() {

        notaTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visible(titulo);
                visible(descripcion);
                visible(btnsave);
                tipoNota = NOTATEXTO;
                gone(notaTexto);
                gone(notaAudio);
                gone(notaVideo);
                gone(notaImagen);
            }
        });

        notaAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnsave.setVisibility(View.GONE);
                tipoNota = NOTAAUDIO;
                gone(notaTexto);
                gone(notaAudio);
                gone(notaVideo);
                gone(notaImagen);


                audioPlayRec = new AudioPlayRec(activityBase,view,contexto,R.id.btn_grabar_audio,
                        R.id.btn_play_audio, R.id.btn_audio_stop, R.id.btn_audio_pausa,
                        R.id.tvaudio, R.id.pbaraudio );

                audioPlayRec.setPath(path);
                audioPlayRec.init();
                audioPlayRec.setListener(new AudioPlayRec.Listeners() {
                    @Override
                    public void onRec(String p) {
                        path = p;
                    }

                    @Override
                    public void onEndRec() {

                        onUpdate();
                    }
                });
            }
        });

        notaVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recVideo.setVisibility(View.VISIBLE);
                btnsave.setVisibility(View.GONE);
                tipoNota = NOTAVIDEO;
                gone(notaTexto);
                gone(notaAudio);
                gone(notaVideo);
                gone(notaImagen);

            }
        });

        notaImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tipoNota = NOTAIMAGEN;
                gone(notaTexto);
                gone(notaAudio);
                gone(notaVideo);
                gone(notaImagen);

                imagen.setVisibility(View.VISIBLE);
                btnsave.setVisibility(View.GONE);
                mediaUtil = new MediaUtil(contexto);
                setImagenUri(mediaUtil, path);
                imagenPantalla(2,1);
            }
        });

        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Video play path = " + path);
                gone(playVideo);

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

                videoView.setVideoURI(Uri.fromFile(new File(path)));
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

        ampliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ampliado) {
                    ampliado = true;
                    ampliar.setImageResource(R.drawable.ic_reducir_indigo);
                    datos();
                }else{
                    ampliado = false;
                    ampliar.setImageResource(R.drawable.ic_ampliar_indigo);
                    datos();

                }

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
        tituloNuevo = R.string.nueva_nota;
    }

    @Override
    protected void setInicio() {

        notaTexto = (ImageView) ctrl(R.id.btn_texto);
        notaAudio = (ImageView) ctrl(R.id.btn_audio);
        notaVideo = (ImageView) ctrl(R.id.btn_video);
        notaImagen = (ImageView) ctrl(R.id.btn_imagen);
        titulo = (EditMaterial) ctrl(R.id.ettitulo_nota);
        descripcion = (EditMaterial) ctrl(R.id.etdesc_nota);
        imagen = (ImageView) ctrl(R.id.imagen_nota);
        playVideo = (ImageView) ctrl(R.id.btn_play_video);
        recVideo = (ImageView) ctrl(R.id.btn_grabar_video);
        ampliar = (ImageView) ctrl(R.id.ampliar_imagen);
        fecha = (TextView) ctrl(R.id.tvfechanota);
        tvTipo = (TextView) ctrl(R.id.tvtiponota);
        btncompartir = (ImageButton) ctrl(R.id.btncompartirnota);
        rl = (RelativeLayout) ctrl(R.id.rl_videoview);
        videoView = new MediaUtil.MyVideoView(activityBase.getApplicationContext());
        rl.addView(videoView);

    }

    @Override
    protected boolean onDelete() {

        if (delete()) {
            id = null;
            modelo = null;
            if (path!=null) {

                File file = new File(path);
                boolean res = file.delete();
                if (res) {
                    Toast.makeText(contexto, "Archivo adjunto borrado", Toast.LENGTH_SHORT).show();
                    selector();
                    return true;
                }else{
                    Toast.makeText(contexto, "error al borrar el archivo adjunto", Toast.LENGTH_SHORT).show();
                    selector();
                    return false;
                }
            }
            selector();
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
        setDato(NOTA_TITULO,titulo.getText().toString());
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

        System.out.println("actual = " + getString(tituloPlural));
        System.out.println("subTitulo = " + subTitulo);

        if (id!=null){

            id=null;
            modelo=null;
            selector();

        }else if (origen.equals(EVENTO)) {

            enviarBundle();
            bundle.putString(ID, idrelacionado);
            Modelo evento = CRUDutil.setModelo(CAMPOS_EVENTO,idrelacionado);
            bundle.putSerializable(MODELO, evento);
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

            descripcion.setText(modelo.getString(NOTA_TITULO));
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


}