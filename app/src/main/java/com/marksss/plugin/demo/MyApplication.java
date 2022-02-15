package com.marksss.plugin.demo;

import android.app.Application;

/**
 * Created by shenxl on 2022/2/10.
 */
public class MyApplication extends Application {
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
