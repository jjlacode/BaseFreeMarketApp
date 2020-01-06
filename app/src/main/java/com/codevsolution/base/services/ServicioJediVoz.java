package com.codevsolution.base.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.CheckPermisos;
import com.codevsolution.base.interfaces.SpeechDelegate;
import com.codevsolution.base.speech.GoogleVoiceTypingDisabledException;
import com.codevsolution.base.speech.SpeechRecognitionNotAvailable;
import com.codevsolution.base.speech.SpeechUtil;
import com.codevsolution.base.style.Estilos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.NULL;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.PREFERENCIAS;
import static com.codevsolution.base.logica.InteractorBase.Constantes.ACCION_JEDI;
import static com.codevsolution.base.logica.InteractorBase.Constantes.EXTRA_VOZ;
import static com.codevsolution.base.settings.PreferenciasBase.CLAVEVOZ;

public class ServicioJediVoz extends JobService implements SpeechDelegate, SpeechUtil.stopDueToDelay, SensorEventListener {

    private SensorManager sensorManagerProx;
    private Sensor proximitySensor;
    private SensorEventListener proximitySensorListener;
    private String TAG = "TRService";
    private Sensor mALS;
    private boolean listenerSensorProx;
    private boolean primerPaso;
    private Timer timer;
    private JobParameters params;
    private boolean iniciado;
    public static SpeechDelegate delegate;
    private boolean listenCom;
    private Context context;
    private static int volNotification = 100;
    private static int volAlarm = 100;
    private static int volMusic = 100;
    private static int volRing = 100;
    private static int volSystem = 100;

    @Override
    public boolean onStartJob(JobParameters params) {

        Log.d(this.getClass().getSimpleName(), "onStartJob");
        this.params = params;
        iniciado = false;
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!listenerSensorProx && sensorProximidad()) {
                    sensorManagerProx.registerListener(proximitySensorListener,
                            proximitySensor, 1000 * 1000);
                    listenerSensorProx = true;
                    Log.e(TAG, "sensor prox. ok");
                }
            }

        });

        return true;
    }

    protected boolean sensorProximidad() {
        sensorManagerProx =
                (SensorManager) getSystemService(SENSOR_SERVICE);

        proximitySensor =
                sensorManagerProx != null ? sensorManagerProx.getDefaultSensor(Sensor.TYPE_PROXIMITY) : null;

        if (proximitySensor == null) {
            Log.e(TAG, "Proximity sensor not available.");
            return false;
        }

        proximitySensorListener = this;

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        return false;
    }

    @Override
    public void onDestroy() {
        Log.e("Servicio jedi", "destroy");
        if (listenerSensorProx) {
            sensorManagerProx.unregisterListener(proximitySensorListener);
            listenerSensorProx = false;
        }

        AutoArranqueJedi.scheduleJob(getApplicationContext());
        super.onDestroy();
    }

    public boolean startVoz() {

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
        SpeechUtil.getInstance().setStopListeningAfterInactivity(2000);
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
    public void onSpecifiedCommandPronounced(String event) {


    }

    @Override
    public void onStartOfSpeech() {

    }

    @Override
    public void onSpeechRmsChanged(float value) {

    }

    @Override
    public void onSpeechPartialResults(List<String> results) {


    }

    @Override
    public void onSpeechResult(String result) {

        Log.d("Result", result + "");
        enableBeepSoundOfRecorder();
        String clave = AndroidUtil.getSharePreference(this, PREFERENCIAS, CLAVEVOZ, NULL);

        if (!TextUtils.isEmpty(result)) {

            System.out.println("result = " + result);
            if (clave != null && !clave.isEmpty() && result.toLowerCase().contains(clave.toLowerCase())) {
                Log.e("VOZ", "Clave correcta");
                sendCom(result);
                SpeechUtil.getInstance().stopListening();

            } else if (clave == null) {
                Toast.makeText(context, "Debe asignar una clave para los comandos de voz en preferencias", Toast.LENGTH_SHORT).show();
            }
            //SpeechUtil.getInstance().shutdown();
        }

    }

    private boolean sendCom(String result) {

        System.out.println("Comando voz = " + result);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        if (!comprobarsApps(result)) {
            Intent intent = new Intent(ACCION_JEDI);
            intent.putExtra(EXTRA_VOZ, result);
            sendBroadcast(intent);
            return true;
        }
        return false;
    }

    private boolean comprobarsApps(String msg) {
        if (msg.contains(Estilos.getString(context, "abrir")) ||
                msg.contains(Estilos.getString(context, "abre"))) {

            if (msg.contains(Estilos.getString(context, "abrir"))) {
                msg = msg.replaceFirst(Estilos.getString(context, "abrir"), "");
            } else if (msg.contains(Estilos.getString(context, "abre"))) {
                msg = msg.replaceFirst(Estilos.getString(context, "abre"), "");
            }
            ArrayList<String> listaApps = AndroidUtil.getDownloadedApps(context);
            for (String app : listaApps) {

                String[] results = app.split(Pattern.quote("."));
                System.out.println("results = " + Arrays.toString(results));
                for (String result : results) {
                    System.out.println("result = " + result.toLowerCase());
                    System.out.println("msg = " + msg.toLowerCase());
                    if (!result.equals("com") && !result.equals("android") &&
                            result.toLowerCase().contains(msg.trim().toLowerCase())) {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage(app);
                        startActivity(intent);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Function to remove the beep sound of voice recognizer.
     */
    private void muteBeepSoundOfRecorder() {
        /*
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

         */
    }

    private void enableBeepSoundOfRecorder() {
        /*
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (amanager != null) {
            if (volNotification==0){volNotification = 75;}
            if (volAlarm==0){volAlarm = 75;}
            if (volMusic==0){volMusic = 75;}
            if (volRing==0){volRing = 75;}
            if (volSystem==0){volSystem = 75;}
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            amanager.setStreamMute(AudioManager.STREAM_ALARM, false);
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            amanager.setStreamMute(AudioManager.STREAM_RING, false);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);

            amanager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,volNotification,0);
            amanager.setStreamVolume(AudioManager.STREAM_ALARM,volAlarm,0);
            amanager.setStreamVolume(AudioManager.STREAM_MUSIC,volMusic,0);
            amanager.setStreamVolume(AudioManager.STREAM_RING,volRing,0);
            amanager.setStreamVolume(AudioManager.STREAM_SYSTEM,volSystem,0);
        }

         */
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (primerPaso && sensorEvent.values[0] == proximitySensor.getMaximumRange()) {
            if (timer != null) {
                timer.cancel();
            }
            primerPaso = false;
            iniciado = false;
            //AndroidUtil.playBeep();
            startVoz();

        } else if (sensorEvent.values[0] < proximitySensor.getMaximumRange() && iniciado) {
            primerPaso = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    primerPaso = false;
                    if (timer != null) {
                        timer.cancel();
                    }
                }
            }, 500);
        } else {
            primerPaso = false;
            iniciado = true;
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
