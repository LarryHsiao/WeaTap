package com.larryhsiao.aura.weatap;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

import static com.larryhsiao.aura.weatap.BuildConfig.OPEN_WEATHER_API_KEY;

/**
 * Entry Activity for this app
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(
                            new HttpUrl.Builder()
                                    .scheme("https")
                                    .host("api.openweathermap.org")
                                    .addEncodedPathSegments("/data/2.5/forecast")
                                    .addQueryParameter("lat", "24.1157643")
                                    .addQueryParameter("lon", "120.7183163")
                                    .addQueryParameter("appid", OPEN_WEATHER_API_KEY)
                                    .build()
                    ).build();
                    Response res = client.newCall(request).execute();
                    if (res.isSuccessful()) {
                        proceedForecast(res.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void proceedForecast(String response) {
        final List<Weather> forecasts = new OWForecasts(
                JsonParser.parseString(response).getAsJsonObject()
        ).value();

        int rainCounter = 0;
        for (int i = 0; i < forecasts.size() && i <= 5; i++) {
            if (forecasts.get(i).raining()) {
                rainCounter++;
                final int finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showRain();
                    }
                });
            }
        }

        final boolean rain = rainCounter > 0;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!rain) {
                    showNotRain();
                }
            }
        });
    }

    private void showRain() {
        ImageView conditionImage = findViewById(R.id.main_conditionImage);
        conditionImage.setImageResource(R.drawable.ic_umbrella);
        exit();
    }

    private void showNotRain() {
        ImageView conditionImage = findViewById(R.id.main_conditionImage);
        conditionImage.setImageResource(R.drawable.ic_ok);
        exit();
    }

    private void exit() {
        new Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },
                1500
        );
    }
}
