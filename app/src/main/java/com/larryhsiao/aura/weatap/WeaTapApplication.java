package com.larryhsiao.aura.weatap;

import android.app.Application;
import com.google.gson.Gson;
import com.larryhsiao.aura.weatap.gson.WeatherTypeAdapter;

/**
 * Application of WeaTap
 */
public class WeaTapApplication extends Application {
    private Gson gson = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public Gson gsonParser() {
        if (gson == null) {
            gson = new Gson().newBuilder()
                    .registerTypeHierarchyAdapter(Weather.class, new WeatherTypeAdapter())
                    .create();
        }
        return gson;
    }
}
