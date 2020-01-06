package com.codevsolution.base.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.CheckPermisos;
import com.codevsolution.base.interfaces.SpeechDelegate;
import com.codevsolution.base.speech.GoogleVoiceTypingDisabledException;
import com.codevsolution.base.speech.SpeechRecognitionNotAvailable;
import com.codevsolution.base.speech.SpeechUtil;

import java.util.List;
import java.util.Objects;
import java.util.Timer;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.NULL;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.PREFERENCIAS;
import static com.codevsolution.base.logica.InteractorBase.Constantes.ACCION_JEDI;
import static com.codevsolution.base.logica.InteractorBase.Constantes.EXTRA_VOZ;
import static com.codevsolution.base.settings.PreferenciasBase.CLAVEVOZ;

public class ServicioVoz extends JobService implements SpeechDelegate, SpeechUtil.stopDueToDelay {

    public static SpeechDelegate delegate;
    private boolean listenCom;
    private Timer timer;
    private Context context;
    private JobParameters params;
    private static int volNotification = 100;
    private static int volAlarm = 100;
    private static int volMusic = 100;
    private static int volRing = 100;
    private static int volSystem = 100;

    @Override
    public boolean onStartJob(JobParameters params) {

        this.params = params;
        Log.d(this.getClass().getSimpleName(), "onStartJob");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ((AudioManager) Objects.requireNonNull(
                        getSystemService(Context.AUDIO_SERVICE))).setStreamMute(AudioManager.STREAM_SYSTEM, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        context = this;
        SpeechUtil.init(this);
        //SpeechUtil.getInstance().setStopListeningAfterInactivity(2000);
        delegate = this;
        SpeechUtil.getInstance().setListener(this);

        if (SpeechUtil.getInstance().isListening()) {
            SpeechUtil.getInstance().stopListening();
            enableBeepSoundOfRecorder();
        } else {
            //System.setProperty("rx.unsafe-disable", "True");
            if (CheckPermisos.validarPermisos(this, CheckPermisos.RECORD_AUDIO, 243)) {
                try {
                    SpeechUtil.getInstance().startListening(this);
                    Log.i("VOZ", "Start listening servicio voz");

                } catch (SpeechRecognitionNotAvailable speechRecognitionNotAvailable) {
                    speechRecognitionNotAvailable.printStackTrace();
                } catch (GoogleVoiceTypingDisabledException e) {
                    e.printStackTrace();
                }
                muteBeepSoundOfRecorder();
            }
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    @Override
    public void onSpecifiedCommandPronounced(String event) {

        /*
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ((AudioManager) Objects.requireNonNull(
                        getSystemService(Context.AUDIO_SERVICE))).setStreamMute(AudioManager.STREAM_SYSTEM, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (SpeechUtil.getInstance().isListening()) {
            enableBeepSoundOfRecorder();
            SpeechUtil.getInstance().stopListening();
        } else {

            if (CheckPermisos.validarPermisos(this,CheckPermisos.RECORD_AUDIO,243)) {
                try {
                    SpeechUtil.getInstance().startListening(this);
                    muteBeepSoundOfRecorder();

                    Log.i("VOZ", "Start listening servicio voz");

                } catch (SpeechRecognitionNotAvailable speechRecognitionNotAvailable) {
                    speechRecognitionNotAvailable.printStackTrace();
                } catch (GoogleVoiceTypingDisabledException e) {
                    e.printStackTrace();
                }
            }
        }

         */
    }

    @Override
    public void onStartOfSpeech() {

    }

    @Override
    public void onSpeechRmsChanged(float value) {

    }

    @Override
    public void onSpeechPartialResults(List<String> results) {

        /*
        String clave = AndroidUtil.getSharePreference(this, PREFERENCIAS, CLAVEVOZ, NULL);
        for (String partial : results) {
            Log.d("Result", partial+"");

            if (clave!=null && !clave.isEmpty()) {
                if (partial.equals(clave)) {
                    Log.e("VOZ", "Clave correcta");
                    listenCom = true;
                } else if(listenCom){
                    if(sendCom(partial)){
                        listenCom = false;
                    }
                }else {
                    results.clear();
                }
            }else{
                sendCom(partial);
            }
        }

         */
    }

    @Override
    public void onSpeechResult(String result) {

        Log.d("Result", result + "");
        enableBeepSoundOfRecorder();
        String clave = AndroidUtil.getSharePreference(this, PREFERENCIAS, CLAVEVOZ, NULL);

        if (!TextUtils.isEmpty(result)) {

            if (clave != null && !clave.isEmpty() && result.contains(clave)) {
                Log.e("VOZ", "Clave correcta");
                sendCom(result);
            } else if (clave != null) {
                sendCom(result);
            }
            SpeechUtil.getInstance().stopListening();
            jobFinished(params, false);
            AutoArranqueVoz.cancelJob();
            AutoArranqueJedi.scheduleJob(context);

        }

    }

    private boolean sendCom(String result) {

        System.out.println("Comando voz " + result);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ACCION_JEDI);
        intent.putExtra(EXTRA_VOZ, result);
        sendBroadcast(intent);

        return true;
    }

    /**
     * Function to remove the beep sound of voice recognizer.
     */
    private void muteBeepSoundOfRecorder() {
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (amanager != null) {
            volNotification = amanager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            volAlarm = amanager.getStreamVolume(AudioManager.STREAM_ALARM);
            amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
            volMusic = amanager.getStreamVolume(AudioManager.STREAM_MUSIC);
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            volRing = amanager.getStreamVolume(AudioManager.STREAM_RING);
            amanager.setStreamMute(AudioManager.STREAM_RING, true);
            volSystem = amanager.getStreamVolume(AudioManager.STREAM_SYSTEM);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        }
    }

    private void enableBeepSoundOfRecorder() {
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (amanager != null) {
            amanager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volNotification, 0);
            amanager.setStreamVolume(AudioManager.STREAM_ALARM, volAlarm, 0);
            amanager.setStreamVolume(AudioManager.STREAM_MUSIC, volMusic, 0);
            amanager.setStreamVolume(AudioManager.STREAM_RING, volRing, 0);
            amanager.setStreamVolume(AudioManager.STREAM_SYSTEM, volSystem, 0);
        }
    }


}
