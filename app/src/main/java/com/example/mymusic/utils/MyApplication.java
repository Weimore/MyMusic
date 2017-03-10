package com.example.mymusic.utils;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by wn123 on 2017/2/28.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context=getApplicationContext();
        LitePal.initialize(context);
    }
    public static Context getContext(){
        return context;
    }
}
