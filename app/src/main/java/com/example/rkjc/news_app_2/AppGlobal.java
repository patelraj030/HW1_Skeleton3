package com.example.rkjc.news_app_2;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class AppGlobal extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
