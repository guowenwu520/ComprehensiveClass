package com.example.vaporwaveVideo.app;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        setContext(this);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static Context getContext() {
        return context;
    }
}
