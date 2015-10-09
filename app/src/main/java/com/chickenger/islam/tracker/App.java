package com.chickenger.islam.tracker;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import timber.log.Timber;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        Timber.plant(BuildConfig.DEBUG ? new Timber.DebugTree() : new Timber.Tree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {

            }
        });
    }
}
