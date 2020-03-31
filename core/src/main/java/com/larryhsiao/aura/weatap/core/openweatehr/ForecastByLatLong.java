package com.larryhsiao.aura.weatap.core.openweatehr;

import com.google.gson.JsonParser;
import com.larryhsiao.aura.weatap.core.Weather;
import com.larryhsiao.aura.weatap.core.config.WeaTapConfig;
import com.silverhetch.clotho.Source;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.List;

/**
 * Source to build OpenWeather forecast data.
 */
public class ForecastByLatLong implements Source<List<Weather>> {
    private final WeaTapConfig config;
    private final Source<OkHttpClient> client;
    private final double latitude;
    private final double longitude;

    public ForecastByLatLong(
            WeaTapConfig config,
            Source<OkHttpClient> client,
            double latitude,
            double longitude
    ) {
        this.config = config;
        this.client = client;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public List<Weather> value() {
        try {
            Request request = new Request.Builder().url(
                    new HttpUrl.Builder()
                            .scheme("https")
                            .host("api.openweathermap.org")
                            .addEncodedPathSegments("/data/2.5/forecast")
                            .addQueryParameter("lat", "" + latitude)
                            .addQueryParameter("lon", "" + longitude)
                            .addQueryParameter("appid", config.apiKey())
                            .build()
            ).build();
            Response res = client.value().newCall(request).execute();
            if (res.isSuccessful()) {
                return new OWForecasts(
                        config,
                        JsonParser.parseString(res.body().string()).getAsJsonObject()
                ).value();
            } else {
                throw new RuntimeException("Fetching forecast failed: " + res.code());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
