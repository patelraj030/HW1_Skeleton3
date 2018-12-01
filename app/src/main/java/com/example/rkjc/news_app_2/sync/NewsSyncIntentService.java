package com.example.rkjc.news_app_2.sync;

import android.app.IntentService;
import android.content.Intent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class NewsSyncIntentService extends IntentService {

    public static final String EXTRA_PARAM2 = "com.example.rkjc.news_app_2.sync.extra.PARAM2";

    public NewsSyncIntentService() {
        super("NewsSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        NewsSyncTask.executeTask(this, action);
    }

}
