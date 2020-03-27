package com.larryhsiao.aura.weatap;

import android.app.Application;
import com.google.gson.Gson;
import com.larryhsiao.aura.weatap.gson.WeatherTypeAdapter;
import com.squareup.picasso.Picasso;

/**
 * Application of WeaTap
 */
public class WeaTapApplication extends Application {
    private Gson gson = null;
    private Picasso picasso = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Gson jsonParser() {
        if (gson == null) {
            gson = new Gson().newBuilder()
                    .registerTypeHierarchyAdapter(Weather.class, new WeatherTypeAdapter())
                    .create();
        }
        return gson;
    }

    public Picasso imageLoader() {
        if (picasso == null) {
            picasso = new Picasso.Builder(this).build();
        }
        return picasso;
    }
}
