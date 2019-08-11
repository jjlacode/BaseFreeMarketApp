package com.jjlacode.freelanceproject.services;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jjlacode.freelanceproject.util.android.AppActivity;


public class AutoArranque extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("intent = " + intent.getAction());
        Log.d("TAG","onReceive autoarranque");
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            scheduleJob(context);
        }
    }

    public static void scheduleJob(Context context) {

        ComponentName serviceComponent = new ComponentName(AppActivity.getAppContext(), AvisoEventos.class);
        JobInfo info = new JobInfo.Builder(0, serviceComponent)
                .setMinimumLatency(60000)
                .setOverrideDeadline(60000)
                .build();
        ComponentName serviceComponent1 = new ComponentName(AppActivity.getAppContext(), AvisoMsgChat.class);
        JobInfo info1 = new JobInfo.Builder(1, serviceComponent1)
                .setMinimumLatency(1000)
                .setOverrideDeadline(1000)
                .build();
        JobScheduler jobScheduler = (JobScheduler) AppActivity.getAppContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(info);
        jobScheduler.schedule(info1);

    }
}
