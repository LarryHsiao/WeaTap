package com.larryhsiao.aura.weatap.openweatehr;

import com.google.gson.JsonParser;
import com.larryhsiao.aura.weatap.Weather;
import com.silverhetch.clotho.Source;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.List;

import static com.larryhsiao.aura.weatap.BuildConfig.OPEN_WEATHER_API_KEY;

/**
 * Source to build OpenWeather forecast data.
 */
public class ForecastByLatLong implements Source<List<Weather>> {
    private final double latitude;
    private final double longitude;

    public ForecastByLatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public List<Weather> value() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(
                    new HttpUrl.Builder()
                            .scheme("https")
                            .host("api.openweathermap.org")
                            .addEncodedPathSegments("/data/2.5/forecast")
                            .addQueryParameter("lat", "" + latitude)
                            .addQueryParameter("lon", "" + longitude)
                            .addQueryParameter("appid", OPEN_WEATHER_API_KEY)
                            .build()
            ).build();
            Response res = client.newCall(request).execute();
            if (res.isSuccessful()) {
                return new OWForecasts(
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
