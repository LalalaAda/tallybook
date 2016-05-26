package com.tuzhida.thisgood;

import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by Paul-Sartre on 2015/12/10.
 */
public class MyApplication extends LitePalApplication {
     private static Context sContext;

    @Override
    public void onCreate() {

        super.onCreate();
        sContext=getApplicationContext();
    }

    public static Context getsContext() {
        return sContext;
    }
}
