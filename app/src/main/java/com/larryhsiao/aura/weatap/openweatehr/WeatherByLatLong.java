package com.larryhsiao.aura.weatap.openweatehr;

import com.google.gson.JsonParser;
import com.larryhsiao.aura.weatap.Weather;
import com.silverhetch.clotho.Source;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.larryhsiao.aura.weatap.BuildConfig.OPEN_WEATHER_API_KEY;


/**
 * Fetch current weather by geometry.
 */
public class WeatherByLatLong implements Source<Weather> {
    private final Source<OkHttpClient> client;
    private final double latitude;
    private final double longitude;

    public WeatherByLatLong(Source<OkHttpClient> client, double latitude, double longitude) {
        this.client = client;
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
                            .addQueryParameter("appid", OPEN_WEATHER_API_KEY)
                            .build()
            ).build();
            Response res = client.value().newCall(request).execute();
            if (res.isSuccessful()) {
                return new OWWeather(
                        JsonParser.parseString(res.body().string()).getAsJsonObject()
                );
            } else {
                throw new RuntimeException("Fetching forecast failed: " + res.code());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
