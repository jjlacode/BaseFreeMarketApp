package com.codevsolution.base.media;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.codevsolution.base.R;
import com.codevsolution.base.android.controls.NestedScrollCoordinatorLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.style.Estilos;

import java.io.File;
import java.io.IOException;

public class VideoPlayRec implements SurfaceHolder.Callback {

    private MediaRecorder recorder;
    private MediaController mediaController;
    private ImageView btnRec;
    private ImageView btnStop;
    private boolean grabando;
    private File archivo;
    private String path;
    private Context context;
    private View view;
    private Listeners listener;
    private boolean land;
    private AppCompatActivity activity;
    private boolean multiPanel;
    private boolean tablet;
    private int ancho;
    private int alto;
    private ViewGroupLayout vistaAudioVideoControl;
    private SurfaceView surface;
    private VideoView videoView;
    private CamaraUtil camaraUtil;
    private SurfaceHolder holder;
    public static final int REC = 1;
    public static final int PLAY = 2;
    private int modo;
    private ViewGroupLayout vistaForm;

    public VideoPlayRec(AppCompatActivity activity, View view, Context context, ViewGroup viewGroup, int modo) {

        this.view = view;
        this.context = context;
        this.activity = activity;
        this.modo = modo;
        crearLayout(viewGroup);

    }

    public void init() {

        btnStop.setVisibility(View.GONE);
        btnRec.setVisibility(View.GONE);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        multiPanel = esMultiPanel(metrics);

        land = context.getResources().getBoolean(R.bool.esLand) && multiPanel;
        tablet = context.getResources().getBoolean(R.bool.esTablet);
        ancho = metrics.widthPixels;
        alto = metrics.heightPixels;

        if (!land) {
            btnRec.setMinimumHeight((int) ((double) alto / 4));
            btnRec.setMinimumWidth(ancho);
        } else {
            btnRec.setMinimumWidth((int) ((double) ancho / 4));
            btnRec.setMinimumHeight((int) ((double) alto / 4));
        }
        if (!land) {
            btnStop.setMinimumHeight((int) ((double) alto / 4));
            btnStop.setMinimumWidth(ancho);
        } else {
            btnStop.setMinimumWidth((int) ((double) ancho / 4));
            btnStop.setMinimumHeight((int) ((double) alto / 4));
        }

        btnRec.setOnClickListener(view -> {

            grabar();
            btnStop.setVisibility(View.VISIBLE);
            btnRec.setVisibility(View.GONE);

        });

        btnStop.setOnClickListener(view -> detener());

        if (path == null) {
            if (surface != null) {
                Estilos.setLayoutParams(vistaForm.getViewGroup(), surface, ancho, (int) ((double) alto) / 2);
                btnRec.setVisibility(View.VISIBLE);

            }
        } else {

            if (videoView != null) {
                Estilos.setLayoutParams(vistaForm.getViewGroup(), videoView, ancho, alto);
                playbackRecordedVideo();
            }
        }

    }

    private void crearLayout(ViewGroup viewGroup) {

        viewGroup.removeAllViewsInLayout();
        NestedScrollCoordinatorLayout nested = new NestedScrollCoordinatorLayout(context);
        nested.setPassMode(NestedScrollCoordinatorLayout.PASS_MODE_PARENT_FIRST);
        viewGroup.addView(nested);
        vistaForm = new ViewGroupLayout(context, nested);

        if (modo == REC) {
            surface = (SurfaceView) vistaForm.addVista(new VideoView(context));
            holder = surface.getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        } else if (modo == PLAY) {
            videoView = (VideoView) vistaForm.addVista(new VideoView(context));

        }

        vistaAudioVideoControl = new ViewGroupLayout(context, vistaForm.getViewGroup());
        vistaAudioVideoControl.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);

        btnRec = vistaAudioVideoControl.addImageView(Estilos.getIdDrawable(context, "ic_rec_video_indigo"), 1);
        btnStop = vistaAudioVideoControl.addImageView(Estilos.getIdDrawable(context, "ic_stop_indigo"), 1);

    }


    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void prepareRecorderVideo() {

        try {
            archivo = MediaUtil.createPublicVideoFile(context);
            path = archivo.getAbsolutePath();
            System.out.println("path = " + path);
            listener.onRec(path);
            recorder.setOutputFile(path);

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            CamcorderProfile camcorderProfile = CamcorderProfile.get(CamaraUtil.getIdCameraBack(context), CamcorderProfile.QUALITY_HIGH);
            recorder.setProfile(camcorderProfile);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void grabar() {


        recorder.start();
        grabando = true;
        surface.setVisibility(View.VISIBLE);

    }

    public void detener() {

        if (grabando) {

            recorder.stop();
            grabando = false;
            listener.onEndRec();

        }

    }

    public void setListener(Listeners listener) {
        this.listener = listener;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (path == null) {
            recorder = new MediaRecorder();
            prepareRecorderVideo();
            recorder.setPreviewDisplay(holder.getSurface());
            try {
                recorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {


        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public interface Listeners {

        void onRec(String path);

        void onEndRec();

    }

    public void playbackRecordedVideo() {
        videoView.setVideoURI(Uri.fromFile(new File(path)));
        mediaController = new MediaController(context);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.requestFocus();
        videoView.seekTo(100);
        videoView.setZOrderOnTop(false);
    }


    public boolean esMultiPanel(DisplayMetrics metrics) {
        // Determinar que siempre sera multipanel
        return ((float) metrics.densityDpi / (float) metrics.widthPixels) < 0.30;
    }

    private boolean checkCameraHardware(Context context) {
        // this device has a camera
        // no camera on this device
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


}
