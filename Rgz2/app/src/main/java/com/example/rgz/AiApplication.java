package com.example.rgz;

import android.app.Application;
import com.yandex.mapkit.MapKitFactory;

// Инициализация Yandex MapKit при запуске приложения.
public class AiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
    }
}
