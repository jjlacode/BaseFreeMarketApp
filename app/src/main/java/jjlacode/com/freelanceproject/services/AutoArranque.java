package jjlacode.com.freelanceproject.services;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import jjlacode.com.freelanceproject.util.AppActivity;


public class AutoArranque extends BroadcastReceiver {

    private static final int PERIOD_MS = 60000;

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("intent = " + intent.getAction());
        Log.d("TAG","onReceive autoarranque");
        //if (intent.getAction()!=null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            scheduleJob(context);
        //}
    }

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(AppActivity.getAppContext(), AvisoEventos.class);
        JobInfo info = new JobInfo.Builder(0, serviceComponent)
        .setMinimumLatency(PERIOD_MS)
        .setOverrideDeadline(PERIOD_MS)
        .build();
        JobScheduler jobScheduler = (JobScheduler) AppActivity.getAppContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int res = jobScheduler.schedule(info);

        if (res == JobScheduler.RESULT_SUCCESS){
            Log.d("TAG","Scheduler iniciado con  exito");
        }else {
            Log.d("TAG","Scheduler a fallado");
        }
    }
}
