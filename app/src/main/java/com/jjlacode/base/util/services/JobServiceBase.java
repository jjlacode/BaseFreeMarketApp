package com.jjlacode.base.util.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;

import com.jjlacode.freelanceproject.services.AutoArranque;

public class JobServiceBase extends JobService {


    public JobServiceBase() {
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        //Log.d(this.getClass().getSimpleName(),"onStartJob");
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                setJob();

                jobFinished(params, false);

            }

        });

        AutoArranque.scheduleJob(getApplicationContext());
        return true;
    }

    protected void setJob() {
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}
