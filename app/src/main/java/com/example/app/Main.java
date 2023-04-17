package com.example.app;

import android.app.Application;
import live.videosdk.rtc.android.VideoSDK;

public class Main extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VideoSDK.initialize(getApplicationContext());
    }
}