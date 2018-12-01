package com.example.rkjc.news_app_2.sync;

import android.os.AsyncTask;
import android.util.Log;

import com.example.rkjc.news_app_2.data.NewsItemRepository;
import com.example.rkjc.news_app_2.database.AppDatabase;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class NewsSyncFirebaseJobService extends JobService {

    private static final String TAG = NewsSyncFirebaseJobService.class.getSimpleName();

    private AsyncTask mBackgroundTask;
    private AppDatabase mAppDatabase;

    @Override
    public boolean onStartJob(final JobParameters job) {

        mAppDatabase = AppDatabase.getsInstance(this.getApplicationContext());
        Log.d(TAG, "Job started");
        mBackgroundTask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                NewsSyncTask.executeTask(NewsSyncFirebaseJobService.this, NewsSyncTask.ACTION_CHARGING_REMINDER);
                NewsItemRepository newsItemRepository = new NewsItemRepository(mAppDatabase);
                newsItemRepository.getNewsFromAPIAndStoreIntoDatabase();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };

        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}
