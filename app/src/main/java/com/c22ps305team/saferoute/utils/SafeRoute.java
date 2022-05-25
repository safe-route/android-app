package com.c22ps305team.saferoute.utils;

import android.app.Application;
import android.content.Context;

public class SafeRoute extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
