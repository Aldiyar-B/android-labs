package com.example.lab11;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

public class Lab11Application extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
    }
}