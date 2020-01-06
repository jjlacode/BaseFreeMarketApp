package com.codevsolution.base.services;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.ENEJECUCION;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.PREFERENCIAS;
import static com.codevsolution.base.settings.PreferenciasBase.SERVVOZ;


public class AutoArranqueJedi extends BroadcastReceiver {

    private static int jobId = 645;
    private static JobScheduler jobScheduler;

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("intent = " + intent.getAction());
        Log.d("TAG", "onReceive autoarranque");
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            scheduleJob(context);
        }
    }

    public static void scheduleJob(Context context) {

        boolean vozOn = AndroidUtil.getSharePreference(context, PREFERENCIAS, SERVVOZ, false);
        boolean enEjecucion = AndroidUtil.getSharePreference(context, PREFERENCIAS, ENEJECUCION, false);
        if (vozOn && !enEjecucion) {
            ComponentName serviceComponent1 = new ComponentName(AppActivity.getAppContext(), ServicioJediVoz.class);
            JobInfo info1 = new JobInfo.Builder(jobId, serviceComponent1)
                    .setMinimumLatency(500)
                    .setOverrideDeadline(500)
                    .build();
            jobScheduler = (JobScheduler) AppActivity.getAppContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(info1);
        }
    }

    public static void cancelJob(Context context) {

        if (jobScheduler != null) {
            jobScheduler.cancel(jobId);
        }
    }
}
