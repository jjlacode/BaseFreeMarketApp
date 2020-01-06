package com.codevsolution.base.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import com.codevsolution.base.android.AndroidUtil;

import java.util.Timer;
import java.util.TimerTask;

public class ServicioJedi extends JobService {

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

        proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (primerPaso && sensorEvent.values[0] == proximitySensor.getMaximumRange()) {
                    if (timer != null) {
                        timer.cancel();
                    }
                    primerPaso = false;
                    iniciado = false;
                    AndroidUtil.playBeep();
                    AutoArranqueVoz.scheduleJob(getApplicationContext());

                    //Intent intent = new Intent(ACCION_JEDI);
                    //sendBroadcast(intent);
                    //jobFinished(params,false);
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
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

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
        }

        AutoArranqueJedi.scheduleJob(getApplicationContext());
        super.onDestroy();
    }
}
