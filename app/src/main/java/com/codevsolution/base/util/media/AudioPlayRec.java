package com.codevsolution.base.util.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codevsolution.base.util.time.Contador;
import com.codevsolution.freemarketsapp.R;

import java.io.File;
import java.io.IOException;

public class AudioPlayRec implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

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


    public AudioPlayRec(AppCompatActivity activity, View view, Context context){

        this.view = view;
        this.context = context;
        this.activity = activity;

    }

    public AudioPlayRec(AppCompatActivity activity,View view, Context context, int rRec,
                        int rPlay, int rStop, int rPause, int rEstado, int rBar) {

        btnRec = view.findViewById(rRec);
        btnPlay = view.findViewById(rPlay);
        btnStop = view.findViewById(rStop);
        btnPause = view.findViewById(rPause);
        estado = view.findViewById(rEstado);
        bar = view.findViewById(rBar);
        this.view = view;
        this.context = context;
        this.activity = activity;
        init();

    }

    public void init(){

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
        }else{
            btnPlay.setMinimumWidth((int) ((double) ancho / 4));
            btnPlay.setMinimumHeight((int) ((double) alto / 4));
        }
        if (!land) {
            btnRec.setMinimumHeight((int) ((double) alto / 4));
            btnRec.setMinimumWidth(ancho);
        }else{
            btnRec.setMinimumWidth((int) ((double) ancho / 4));
            btnRec.setMinimumHeight((int) ((double) alto / 4));
        }
        if (!land) {
            btnStop.setMinimumHeight((int) ((double) alto / 4));
            btnStop.setMinimumWidth(ancho);
        }else{
            btnStop.setMinimumWidth((int) ((double) ancho / 4));
            btnStop.setMinimumHeight((int) ((double) alto / 4));
        }
        if (!land) {
            btnPause.setMinimumHeight((int) ((double) alto / 4));
            btnPause.setMinimumWidth(ancho);
        }else{
            btnPause.setMinimumWidth((int) ((double) ancho / 4));
            btnPause.setMinimumHeight((int) ((double) alto / 4));
        }

        if (path!=null){
            prepararPlayAudio();
        }else{

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

    }

    public void setPath(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }

    public void setBtnPlay(int rplay){

        btnPlay = view.findViewById(rplay);
    }

    public void setBtnRec(int rRec){

        btnRec = view.findViewById(rRec);
    }

    public void setBtnStop(int rStop){

        btnPlay = view.findViewById(rStop);
    }

    public void setBtnPause(int rpause){

        btnPause = view.findViewById(rpause);
    }

    public void setEstado(int rEstado){
        estado = view.findViewById(rEstado);
    }

    public void grabar() {

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        File pathAudio = new File(Environment.getExternalStorageDirectory().getPath());
        try {
            archivo = File.createTempFile("audio", ".3gp", pathAudio);
        } catch (IOException e) {
            e.printStackTrace();
        }
        path = archivo.getAbsolutePath();
        recorder.setOutputFile(path);
        listener.onRec(path);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
        grabando = true;
        estado.setText(context.getString(R.string.grabando));

    }

    public void detener() {

        if (grabando) {
            recorder.stop();
            grabando = false;
            recorder.release();
            listener.onEndRec();
            prepararPlayAudio();

        }else if (reproduciendo){
            mp.stop();
            leng = 0;
            reproduciendo = false;
            contador.stop();
            bar.setVisibility(View.GONE);
            bar.setProgress(0);
            prepararPlayAudio();
        }

    }

    public void reproducir() {

        if (reproduciendo) {
            mp.seekTo(leng);
            mp.start();
        }else {
            mp.start();
            estado.setText(context.getString(R.string.play_audio));
            reproduciendo = true;
            mp.setOnCompletionListener(this);
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
        }
    }

    public void prepararPlayAudio(){

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
        prepararPlayAudio();
        estado.setText(context.getString(R.string.listo_play));

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        btnStop.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnRec.setVisibility(View.GONE);
        btnPlay.setVisibility(View.VISIBLE);
        estado.setText(context.getString(R.string.listo_play));

    }

    public void setListener(Listeners listener) {
        this.listener = listener;
    }

    public interface Listeners {

        void onRec(String path);
        void onEndRec();

    }

    public boolean esMultiPanel(DisplayMetrics metrics) {
        // Determinar que siempre sera multipanel
        return ((float)metrics.densityDpi / (float)metrics.widthPixels) < 0.30;
    }

}
