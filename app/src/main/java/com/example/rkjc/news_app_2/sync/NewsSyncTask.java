package com.example.rkjc.news_app_2.sync;

import android.content.Context;

import com.example.rkjc.news_app_2.utils.NotificationUtils;

public class NewsSyncTask {

    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    static final String ACTION_CHARGING_REMINDER = "charging-reminder";

    public static void executeTask(Context context, String action) {
        if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            NotificationUtils.clearAllNotifications(context);
        } else if (ACTION_CHARGING_REMINDER.equals(action)) {
            syncingNewsNotification(context);
        }
    }


    private static void syncingNewsNotification(Context context) {
        NotificationUtils.showNewsNotification(context);
    }
}
