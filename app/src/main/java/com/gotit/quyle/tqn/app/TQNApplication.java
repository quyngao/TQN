package com.gotit.quyle.tqn.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.gotit.quyle.tqn.local.PrefManager;
import com.gotit.quyle.tqn.service.FlickrClient;

/**
 * Created by QUYLE on 1/17/18.
 */

public class TQNApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        TQNApplication.context = this;

    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static FlickrClient getRestClient() {
        return (FlickrClient) FlickrClient.getInstance(FlickrClient.class, TQNApplication.context);
    }


    public static PrefManager getPrefManager() {
        return PrefManager.getInstance(TQNApplication.context);
    }


}
