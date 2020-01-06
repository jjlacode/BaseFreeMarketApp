package com.codevsolution.base.services;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.codevsolution.base.android.AppActivity;


public class AutoArranqueVoz extends BroadcastReceiver {

    private static JobScheduler jobScheduler;
    private static int jobId = 1564;

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("intent = " + intent.getAction());
        Log.d("TAG", "onReceive autoarranque");
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            scheduleJob(context);
        }
    }

    public static void scheduleJob(Context context) {

        ComponentName serviceComponent1 = new ComponentName(AppActivity.getAppContext(), ServicioVoz.class);
        JobInfo info = new JobInfo.Builder(jobId, serviceComponent1)
                .setMinimumLatency(500)
                .setOverrideDeadline(500)
                .build();
        jobScheduler = (JobScheduler) AppActivity.getAppContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(info);

    }

    public static void cancelJob() {
        if (jobScheduler != null) {
            jobScheduler.cancel(jobId);
        }
    }
}
