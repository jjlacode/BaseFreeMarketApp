package com.codevsolution.base.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codevsolution.base.R;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.Contador;

import java.io.File;
import java.io.IOException;

public class AudioPlayRec implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

    private MediaPlayer mp;
    private MediaRecorder recorder;
    private ImageView btnRec;
    private ImageView btnPlay;
    private ImageView btnStop;
    private ImageView btnPause;
    private TextView estado;
    private ProgressBar bar;
    private boolean grabando;
    private boolean reproduciendo;
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
    private int leng;
    private Contador contador;
    private ViewGroupLayout vistaAudioVideoControl;
    private MediaController mediaController;
    private Handler handler = new Handler();
    private ViewGroupLayout vistaForm;
    private ViewGroup viewGroup;


    public AudioPlayRec(AppCompatActivity activity, View view, Context context, ViewGroup viewGroup) {

        this.view = view;
        this.context = context;
        this.activity = activity;
        this.viewGroup = viewGroup;
        crearLayout(viewGroup);

    }

    public void init() {

        btnStop.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnRec.setVisibility(View.GONE);
        btnPlay.setVisibility(View.GONE);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        multiPanel = esMultiPanel(metrics);

        land = context.getResources().getBoolean(R.bool.esLand) && multiPanel;
        tablet = context.getResources().getBoolean(R.bool.esTablet);
        ancho = metrics.widthPixels;
        alto = metrics.heightPixels;

        if (!land) {
            btnPlay.setMinimumHeight((int) ((double) alto / 4));
            btnPlay.setMinimumWidth(ancho);
        } else {
            btnPlay.setMinimumWidth((int) ((double) ancho / 4));
            btnPlay.setMinimumHeight((int) ((double) alto / 4));
        }
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
        if (!land) {
            btnPause.setMinimumHeight((int) ((double) alto / 4));
            btnPause.setMinimumWidth(ancho);
        } else {
            btnPause.setMinimumWidth((int) ((double) ancho / 4));
            btnPause.setMinimumHeight((int) ((double) alto / 4));
        }

        if (path != null) {
            prepararPlay();
        } else {

            btnRec.setVisibility(View.VISIBLE);
            estado.setText(context.getString(R.string.listo_grabar));

        }

        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                grabar();
                btnStop.setVisibility(View.VISIBLE);
                btnRec.setVisibility(View.GONE);

            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reproducir();
                btnStop.setVisibility(View.VISIBLE);
                vistaAudioVideoControl.getViewGroup().setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.GONE);
                bar.setVisibility(View.VISIBLE);

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                detener();

            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mp.pause();
                contador.pause();
                leng = mp.getCurrentPosition();
                btnPlay.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);


            }
        });
        Estilos.setLayoutParams(viewGroup, vistaForm.getViewGroup(), ancho, (int) ((double) alto / 3));
        vistaForm.getViewGroup().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mediaController.setEnabled(true);
                mediaController.show();
                return true;
            }
        });
    }

    private void crearLayout(ViewGroup viewGroup) {

        viewGroup.removeAllViewsInLayout();
        vistaForm = new ViewGroupLayout(context, viewGroup);
        estado = vistaForm.addTextView("");

        vistaAudioVideoControl = new ViewGroupLayout(context, vistaForm.getViewGroup());
        vistaAudioVideoControl.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);

        btnRec = vistaAudioVideoControl.addImageView(R.drawable.ic_rec_indigo, 1);
        btnStop = vistaAudioVideoControl.addImageView(R.drawable.ic_stop_indigo, 1);
        btnPause = vistaAudioVideoControl.addImageView(R.drawable.ic_pausa_indigo, 1);
        btnPlay = vistaAudioVideoControl.addImageView(R.drawable.ic_play_indigo, 1);
        bar = (ProgressBar) vistaForm.addVista(new ProgressBar(context, null, Estilos.pBarStyleAcept(context)));
        // Estilos.getIdStyle(context,"Widget.Holo.ProgressBar.Horizontal")));

    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setEstado(int rEstado) {
        estado = view.findViewById(rEstado);
    }

    public void prepareRecorderAudio() {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            path = MediaUtil.createAudioFile(context).getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void grabar() {

        recorder = new MediaRecorder();
        prepareRecorderAudio();
        recorder.setOutputFile(path);
        listener.onRec(path);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
        grabando = true;
        estado.setVisibility(View.VISIBLE);
        estado.setText(context.getString(R.string.grabando));

    }

    public void detener() {

        if (grabando) {
            recorder.stop();
            grabando = false;
            recorder.release();
            listener.onEndRec();
            prepararPlay();

        } else if (reproduciendo) {
            mp.stop();
            mediaController.hide();
            leng = 0;
            reproduciendo = false;
            contador.stop();
            bar.setVisibility(View.GONE);
            bar.setProgress(0);
            prepararPlay();
        }

    }

    public void reproducir() {


        if (reproduciendo) {

            mp.seekTo(leng);
        }

            mp.start();
        estado.setVisibility(View.VISIBLE);
        estado.setText(context.getString(R.string.play));
            reproduciendo = true;
            mp.setOnCompletionListener(this);
        bar.setVisibility(View.VISIBLE);
            bar.setMax(mp.getDuration());
            contador = new Contador();
            contador.setListener(new Contador.Listener() {
                @Override
                public void onTick(int ticks) {
                    //estado.setText(String.valueOf(segundos));
                    bar.setProgress(mp.getCurrentPosition());
                }
            });
            contador.start(500);
        btnStop.setVisibility(View.VISIBLE);
    }

    public void prepararPlay() {

        mp = new MediaPlayer();
        Uri uri = Uri.fromFile(new File(path));
        try {

            mp.setDataSource(context, uri);
            mp.prepareAsync();
            mp.setOnPreparedListener(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

        btnStop.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnRec.setVisibility(View.GONE);
        btnPlay.setVisibility(View.VISIBLE);
        bar.setVisibility(View.GONE);
        leng = 0;
        bar.setProgress(0);
        contador.stop();
        prepararPlay();
        estado.setText(context.getString(R.string.listo_play));

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        btnStop.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnRec.setVisibility(View.GONE);
        btnPlay.setVisibility(View.VISIBLE);
        estado.setText(context.getString(R.string.listo_play));
        mediaController = new MediaController(context);
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(vistaForm.getViewGroup());

        handler.post(new Runnable() {
            public void run() {
                mediaController.setEnabled(true);
                mediaController.show();
            }
        });
    }

    //--MediaPlayerControl methods----------------------------------------------------
    public void start() {
        reproducir();
    }


    public void pause() {
        mp.pause();
        contador.pause();
        leng = mp.getCurrentPosition();
    }

    public int getDuration() {
        return mp.getDuration();
    }

    public int getCurrentPosition() {
        return mp.getCurrentPosition();
    }

    public void seekTo(int i) {
        mp.seekTo(i);
    }

    public boolean isPlaying() {
        return mp.isPlaying();
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public int getAudioSessionId() {
        return 0;
    }
    //------------------------------------------------------------

    public void setListener(Listeners listener) {
        this.listener = listener;
    }

    public interface Listeners {

        void onRec(String path);

        void onEndRec();

    }

    public boolean esMultiPanel(DisplayMetrics metrics) {
        // Determinar que siempre sera multipanel
        return ((float) metrics.densityDpi / (float) metrics.widthPixels) < 0.30;
    }


}
