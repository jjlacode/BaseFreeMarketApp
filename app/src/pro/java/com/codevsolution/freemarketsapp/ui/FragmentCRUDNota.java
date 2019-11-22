package com.codevsolution.freemarketsapp.ui;
// Created by jjlacode on 8/05/19. 

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.CheckPermisos;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.android.controls.ViewImagenLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.media.AudioPlayRec;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.media.VideoPlayRec;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.io.File;
import java.util.ArrayList;

import static com.codevsolution.base.time.calendar.DiaCalBase.HORACAL;

public class FragmentCRUDNota extends FragmentCRUD implements Interactor.ConstantesPry,
        ContratoPry.Tablas, Interactor.TiposNota {


    private EditMaterialLayout titulo;
    private EditMaterialLayout descripcion;
    private String tipoNota;
    private ViewImagenLayout ampliar;
    private TextView fecha;
    private TextView tvTipo;
    private long fechaNota;
    private ImageButton btncompartir;
    private AudioPlayRec audioPlayRec;
    private VideoPlayRec videoPlayRec;
    private boolean ampliado;
    private String pathVideo;
    private String pathAudio;
    private ViewGroupLayout vistaAudioVideo;

    public FragmentCRUDNota() {
        // Required empty public constructor
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
    protected void setLista() {

        if (idrelacionado != null) {

            lista = CRUDutil.setListaModelo(campos,NOTA_ID_RELACIONADO,idrelacionado,IGUAL);

        } else {

            lista = CRUDutil.setListaModelo(campos,NOTA_ID_RELACIONADO,null,IGUAL);

        }
        if (origen==null){
            origen = NULL;
        }

        System.out.println("origen = " + origen);
        if (!origen.equals(NULL) && !origen.equals(NUEVOREGISTRO)) {
            visibleSoloBtnBack();
        }else{
            gone(btnback);
        }
    }

    @Override
    protected void onClickNuevo() {
        icFragmentos.enviarBundleAFragment(null,new FragmentNuevaNota());
    }

    @Override
    protected void setNuevo() {

        allGone();
        visible(titulo.getLinearLayout());
        visible(descripcion.getLinearLayout());
        visible(btnback);
        gone(btndelete);

        switch (tipoNota){

            case NOTATEXTO:

                visible(btnsave);
                break;

            case NOTAAUDIO:

                recAudio();
                gone(btnsave);
                break;

            case NOTAVIDEO:

                recVideo();
                btnsave.setVisibility(View.GONE);

                break;

            case NOTAIMAGEN:

                imagen.getLinearLayoutCompat().setVisibility(View.VISIBLE);
                btnsave.setVisibility(View.GONE);
                imagen.setImageUriPerfil(activityBase, path);
                break;

        }

    }

    private void recAudio() {

        if (CheckPermisos.validarPermisos(activityBase, CheckPermisos.RECORD_AUDIO, 100)) {
            btnsave.setVisibility(View.GONE);

            audioPlayRec = new AudioPlayRec(activityBase, view, contexto, vistaAudioVideo.getViewGroup());

            audioPlayRec.setPath(pathAudio);
            audioPlayRec.init();

            audioPlayRec.setListener(new AudioPlayRec.Listeners() {
                @Override
                public void onRec(String p) {
                    pathAudio = p;
                }

                @Override
                public void onEndRec() {

                    onUpdate();
                }
            });
        }
    }

    private void recVideo() {

        if (CheckPermisos.validarPermisos(activityBase, CheckPermisos.CAMERA, 100)) {
            btnsave.setVisibility(View.GONE);

            videoPlayRec = new VideoPlayRec(activityBase, view, contexto, vistaAudioVideo.getViewGroup(), VideoPlayRec.REC);

            videoPlayRec.setPath(pathAudio);
            videoPlayRec.init();

            videoPlayRec.setListener(new VideoPlayRec.Listeners() {
                @Override
                public void onRec(String p) {
                    pathVideo = p;
                }

                @Override
                public void onEndRec() {

                    onUpdate();
                }
            });
        }
    }

    @Override
    protected void setTabla() {

        tabla = TABLA_NOTA;

    }

    @Override
    protected void setTablaCab() {

        tablaCab = ContratoPry.getTabCab(tabla);
    }

    @Override
    protected void setCampos() {

        campos = ContratoPry.obtenerCampos(tabla);

    }

    @Override
    protected void setBundle() {

        idrelacionado = bundle.getString(IDREL);
        fechaNota = bundle.getLong(FECHA) + bundle.getLong(HORACAL);
        tipoNota = bundle.getString(TIPO);
        System.out.println("idrelacionado = " + idrelacionado);

    }

    @Override
    protected void setDatos() {

        modeloSQL = CRUDutil.updateModelo(campos, id);
        btndelete.setVisibility(View.VISIBLE);
        nuevo = false;
        autoGuardado = true;
        btnsave.setVisibility(View.GONE);

        descripcion.setText(modeloSQL.getString(NOTA_DESCRIPCION));
        titulo.setText(modeloSQL.getString(NOTA_TITULO));
        fechaNota = modeloSQL.getLong(NOTA_FECHA);
        path = modeloSQL.getString(NOTA_RUTAFOTO);
        fecha.setText(JavaUtil.getDateTime(fechaNota));
        visible(fecha);
        imagen.getLinearLayoutCompat().setVisibility(View.GONE);
        tipoNota = modeloSQL.getString(NOTA_TIPO);
        visible(tvTipo);
        btncompartir.setVisibility(View.GONE);

        if (tipoNota != null) {

            tvTipo.setText(tipoNota.toUpperCase());

            switch (tipoNota) {

                case NOTATEXTO:

                    visible(titulo.getLinearLayout());
                    visible(descripcion.getLinearLayout());
                    visiblePie();

                    break;

                case NOTAAUDIO:

                    pathAudio = path;
                    if (pathAudio != null) {
                        visible(btncompartir);
                        visible(descripcion.getLinearLayout());
                        visible(titulo.getLinearLayout());
                        visiblePie();
                        audioPlayRec = new AudioPlayRec(activityBase, view, contexto, vistaAudioVideo.getViewGroup());

                        audioPlayRec.setPath(pathAudio);
                        audioPlayRec.init();
                        audioPlayRec.setListener(new AudioPlayRec.Listeners() {
                            @Override
                            public void onRec(String p) {
                                pathAudio = p;
                            }

                            @Override
                            public void onEndRec() {

                                onUpdate();
                            }
                        });
                    } else {

                        recAudio();
                    }

                    break;

                case NOTAVIDEO:

                    pathVideo = path;

                    if (pathVideo != null) {
                        visible(btncompartir);
                        visible(descripcion.getLinearLayout());
                        visible(titulo.getLinearLayout());
                        visiblePie();
                        videoPlayRec = new VideoPlayRec(activityBase, view, contexto, vistaAudioVideo.getViewGroup(), VideoPlayRec.PLAY);

                        videoPlayRec.setPath(pathVideo);
                        videoPlayRec.init();
                        videoPlayRec.setListener(new VideoPlayRec.Listeners() {
                            @Override
                            public void onRec(String p) {
                                pathVideo = p;
                            }

                            @Override
                            public void onEndRec() {

                                onUpdate();
                            }
                        });
                    } else {

                        recVideo();
                    }


                    break;

                case NOTAIMAGEN:

                    visible(imagen.getLinearLayoutCompat());
                    visible(titulo.getLinearLayout());
                    visible(descripcion.getLinearLayout());
                    visible(btncompartir);
                    visible(ampliar.getLinearLayoutCompat());
                    gone(vistaAudioVideo.getViewGroup());
                    visiblePie();

                    if (modeloSQL.getString(NOTA_RUTAFOTO) != null) {
                        path = modeloSQL.getString(NOTA_RUTAFOTO);
                        imagen.setImageUriPerfil(activityBase, path);

                    }

            }
        }

    }


    @Override
    protected void setAcciones() {

        btncompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tipo = null;

                if (tipoNota.equals(NOTAIMAGEN)){

                    tipo = "image/*";

                }else if (tipoNota.equals(NOTAVIDEO)){

                    path = pathVideo;

                    tipo = "video/*";

                }else if (tipoNota.equals(NOTAAUDIO)){

                    path = pathAudio;

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
                    ampliar.setImageResource(R.drawable.ic_ampliar_secondary_dark);
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

        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalle);


        tvTipo = vistaForm.addTextView(R.string.tipo_nota);
        fecha = vistaForm.addTextView(R.string.fecha);
        gone(fecha);
        gone(tvTipo);
        titulo = vistaForm.addEditMaterialLayout(R.string.titulo, NOTA_TITULO);
        descripcion = vistaForm.addEditMaterialLayout(R.string.descripcion, NOTA_DESCRIPCION);

        vistaAudioVideo = new ViewGroupLayout(contexto, vistaForm.getViewGroup());

        ampliar = vistaForm.addViewImagenLayout(R.drawable.ic_ampliar_secondary_dark);
        imagen = vistaForm.addViewImagenLayout();
        gone(imagen.getLinearLayoutCompat());
        btncompartir = vistaForm.addImageButtonSecundary(R.drawable.ic_compartir_indigo);
        actualizarArrays(vistaForm);

        autoGuardado = false;

    }

    @Override
    protected boolean onDelete() {

        if (tipoNota.equals(NOTAVIDEO)) {
            path = pathVideo;
        } else if (tipoNota.equals(NOTAAUDIO)) {
            path = pathAudio;
        }

            if (path!=null) {

                File file = new File(path);
                boolean res = file.delete();
                if (res) {
                    Toast.makeText(contexto, "Archivo adjunto borrado", Toast.LENGTH_SHORT).show();
                    path=null;
                    if (delete()) {
                        id = null;
                        modeloSQL = null;
                        selector();
                        return true;
                    }
                }else{
                    Toast.makeText(contexto, "error al borrar el archivo adjunto", Toast.LENGTH_SHORT).show();
                    path = null;
                    if (delete()) {
                        id = null;
                        modeloSQL = null;
                        selector();
                        return true;
                    }
                }
            } else {
                path=null;
                if (delete()) {
                    id = null;
                    modeloSQL = null;
                    selector();
                    return true;
                }
            }
            selector();

        return false;
    }

    @Override
    protected void setLayout() {

        layoutItem = R.layout.item_list_nota;

    }

    @Override
    protected void setContenedor() {

        if (tipoNota.equals(NOTAVIDEO)) {
            path = pathVideo;
        } else if (tipoNota.equals(NOTAAUDIO)) {
            path = pathAudio;
        }

        setDato(NOTA_DESCRIPCION, descripcion.getText().toString());
        setDato(NOTA_TITULO,titulo.getText().toString());
        if (path!=null) {
            setDato(NOTA_RUTAFOTO, path);
        }

        if (id==null) {
            setDato(NOTA_ID_RELACIONADO, idrelacionado);
            if (idrelacionado!=null) {
                setDato(NOTA_NOMBREREL, subTitulo);
            }
            if (fechaNota==0){
                fechaNota = JavaUtil.hoy();
            }
            setDato(NOTA_FECHA, fechaNota);
            setDato(NOTA_FECHAF, JavaUtil.getDateTime(fechaNota));
            setDato(NOTA_TIPO, tipoNota);
            if (origen.equals(NUEVOREGISTRO)) {
                origen = NULL;
            }
            nuevo = false;

        }

    }

    @Override
    protected boolean onBack() {

        if (tipoNota != null && (tipoNota.equals(NOTAAUDIO) || tipoNota.equals(NOTAVIDEO))) {
            if (tipoNota.equals(NOTAAUDIO)) {
                path = pathAudio;
            } else {
                path = pathVideo;
            }

            if (path != null) {
                Uri uri = Uri.parse(path);
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                try {

                    mmr.setDataSource(contexto, uri);
                    String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    int duracion = Integer.parseInt(durationStr);
                    if (duracion == 0) {
                        onDelete();
                    }

                } catch (Exception e) {
                    onDelete();
                }

            }
        }
        return super.onBack();
    }

    @Override
    protected void setcambioFragment() {


        if (origen.equals(EVENTO)) {

            enviarBundle();
            bundle.putString(CAMPO_ID, idrelacionado);
            bundle.putSerializable(MODELO, CRUDutil.updateModelo(CAMPOS_EVENTO, idrelacionado));
            bundle.putString(ACTUAL, origen);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDEvento());

        } else if (origen.equals(PRESUPUESTO)) {

            enviarBundle();
            bundle.putString(CAMPO_ID, idrelacionado);
            bundle.putSerializable(MODELO, CRUDutil.updateModelo(CAMPOS_PROYECTO,idrelacionado));
            bundle.putString(ACTUAL, origen);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());

        } else if (origen.equals(PROYECTO)) {

            enviarBundle();
            bundle.putString(CAMPO_ID, idrelacionado);
            bundle.putSerializable(MODELO, CRUDutil.updateModelo(CAMPOS_PROYECTO,idrelacionado));
            bundle.putString(ACTUAL, origen);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDProyecto());

        } else if (origen.equals(CLIENTE)) {

            enviarBundle();
            bundle.putString(CAMPO_ID, idrelacionado);
            bundle.putSerializable(MODELO, CRUDutil.updateModelo(CAMPOS_CLIENTE,idrelacionado));
            bundle.putString(ACTUAL, origen);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDCliente());

        } else if (origen.equals(PROSPECTO)) {

            enviarBundle();
            bundle.putString(CAMPO_ID, idrelacionado);
            bundle.putSerializable(MODELO, CRUDutil.updateModelo(CAMPOS_CLIENTE,idrelacionado));
            bundle.putString(ACTUAL, origen);
            icFragmentos.enviarBundleAFragment(bundle, new FragmentCRUDCliente());

        }else if (origen.equals(NOTAS)) {

            enviarBundle();
            bundle.putString(ACTUAL, origen);
            icFragmentos.enviarBundleAFragment(bundle, new Notas());

        } else if (id != null) {

            id = null;
            modeloSQL = null;
            origen = null;
            gonePie();
            selector();

        }


    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View view, ArrayList<ModeloSQL> entrada) {

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
        public void bind(ModeloSQL modeloSQL) {

            descripcion.setText(modeloSQL.getString(NOTA_TITULO));
            fecha.setText(JavaUtil.getDateTime(modeloSQL.getLong(NOTA_FECHA)));
            String tipo = modeloSQL.getString(NOTA_TIPO);
            tipoNota.setText(tipo);
            //tipoNota.setVisibility(View.GONE);
            //imagenTarea.setVisibility(View.GONE);

            if (tipo != null) {

                switch (tipo) {

                    case NOTATEXTO:

                        imagen.setImageResource(R.drawable.ic_format_list_numbered_black_24dp);

                        break;

                    case NOTAAUDIO:

                        imagen.setImageResource(R.drawable.ic_mic_black_24dp);
                        if (modeloSQL.getString(NOTA_RUTAFOTO) != null) {
                            path = modeloSQL.getString(NOTA_RUTAFOTO);
                        }

                        break;

                    case NOTAVIDEO:

                        if (modeloSQL.getString(NOTA_RUTAFOTO) != null) {
                            path = modeloSQL.getString(NOTA_RUTAFOTO);
                            if (path != null) {
                                Bitmap bitmap = MediaUtil.getMini(path);
                                imagen.setImageBitmap(bitmap);
                                //ImagenUtil.setImageDrawableCircle(bitmapDrawable, imagen);
                            }

                        } else {
                            imagen.setImageResource(R.drawable.ic_videocam_black_24dp);
                        }

                        break;

                    case NOTAIMAGEN:

                        if (modeloSQL.getString(NOTA_RUTAFOTO) != null) {
                            imagen.setVisibility(View.VISIBLE);
                            path = modeloSQL.getString(NOTA_RUTAFOTO);

                            ImagenUtil.setImageUriCircle(path, imagen);
                        } else {
                            //imagenTarea.setVisibility(View.GONE);
                            imagen.setImageResource(R.drawable.ic_add_a_photo_black_24dp);
                        }
                }
            }

            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }


}