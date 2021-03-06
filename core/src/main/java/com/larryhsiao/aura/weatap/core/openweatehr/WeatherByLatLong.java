package com.larryhsiao.aura.weatap.core.openweatehr;

import com.google.gson.JsonParser;
import com.larryhsiao.aura.weatap.core.Weather;
import com.larryhsiao.aura.weatap.core.config.WeaTapConfig;
import com.silverhetch.clotho.Source;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Fetch current weather by geometry.
 */
public class WeatherByLatLong implements Source<Weather> {
    private final WeaTapConfig config;
    private final Source<OkHttpClient> client;
    private final double latitude;
    private final double longitude;

    public WeatherByLatLong(Source<OkHttpClient> client,
                            WeaTapConfig config,
                            double latitude,
                            double longitude) {
        this.client = client;
        this.config = config;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public Weather value() {
        try {
            Request request = new Request.Builder().url(
                    new HttpUrl.Builder()
                            .scheme("https")
                            .host("api.openweathermap.org")
                            .addEncodedPathSegments("/data/2.5/weather")
                            .addQueryParameter("lat", "" + latitude)
                            .addQueryParameter("lon", "" + longitude)
                            .addQueryParameter("appid", config.apiKey())
                            .build()
            ).build();
            Response res = client.value().newCall(request).execute();
            if (res.isSuccessful()) {
                return new OWWeather(
                        config, JsonParser.parseString(res.body().string()).getAsJsonObject()
                );
            } else {
                throw new RuntimeException("Fetching forecast failed: " + res.code());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
