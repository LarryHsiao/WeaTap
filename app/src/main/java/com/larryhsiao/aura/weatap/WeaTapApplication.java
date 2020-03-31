package com.larryhsiao.aura.weatap;

import android.app.Application;
import com.google.gson.Gson;
import com.larryhsiao.aura.weatap.core.Weather;
import com.larryhsiao.aura.weatap.core.config.CeresWeaTapConfig;
import com.larryhsiao.aura.weatap.core.config.WeaTapConfig;
import com.larryhsiao.aura.weatap.core.gson.WeatherTypeAdapter;
import com.silverhetch.aura.storage.SPCeres;
import com.squareup.picasso.Picasso;
import okhttp3.OkHttpClient;

import static com.larryhsiao.aura.weatap.BuildConfig.OPEN_WEATHER_API_KEY;

/**
 * Application of WeaTap
 */
public class WeaTapApplication extends Application {
    private CeresWeaTapConfig ceres;
    private OkHttpClient httpClient;
    private Gson gson = null;
    private Picasso picasso = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public OkHttpClient httpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder().build();
        }
        return httpClient;
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

    public WeaTapConfig config() {
        if (ceres == null) {
            ceres = new CeresWeaTapConfig(
                    OPEN_WEATHER_API_KEY,
                    new SPCeres(getSharedPreferences(
                            "config", MODE_PRIVATE
                    ))
            );
        }
        return ceres;
    }
}
