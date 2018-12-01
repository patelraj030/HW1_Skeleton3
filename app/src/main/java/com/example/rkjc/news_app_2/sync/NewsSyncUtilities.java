package com.example.rkjc.news_app_2.sync;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class NewsSyncUtilities {

    private static final int NEWS_SYNC_INTERVAL_SECONDS = (int) (TimeUnit.SECONDS.toSeconds(10));
    private static final int SYNC_FLEXTIME_SECONDS = NEWS_SYNC_INTERVAL_SECONDS;
    private static final String NEWS_TAG = "news-tag";
    private static boolean sInitialized = false;

    private static final String TAG = NewsSyncUtilities.class.getSimpleName();

    synchronized public static void scheduleJob(@NonNull final Context context) {

        if (sInitialized){
            return;
        }
        Log.i(TAG, "Job scheduled");

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Job myJob = dispatcher.newJobBuilder()
                .setService(NewsSyncFirebaseJobService.class) // the JobService that will be called
                .setTag(NEWS_TAG)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(
                        NEWS_SYNC_INTERVAL_SECONDS,
                        NEWS_SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .build();

        dispatcher.mustSchedule(myJob);

        sInitialized = true;
    }
}
