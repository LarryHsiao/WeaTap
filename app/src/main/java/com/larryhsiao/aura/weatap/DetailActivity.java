package com.larryhsiao.aura.weatap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;

/**
 * Detail view for given forecast data
 */
public class DetailActivity extends AppCompatActivity {
    private static final String ARG_FORECAST_JSON = "ARG_FORECAST_JSON";

    private Weather weather;

    public static Intent newIntent(Context context, Weather weather) {
        Gson gson = ((WeaTapApplication) context.getApplicationContext()).gsonParser();
        Intent intent = new Intent();
        intent.putExtra(ARG_FORECAST_JSON, gson.toJson(weather));
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_detail);

        Gson gson = ((WeaTapApplication) getApplicationContext()).gsonParser();
        weather = gson.fromJson(
                getIntent().getStringExtra(ARG_FORECAST_JSON),
                Weather.class
        );
        System.out.println(
                "================= " + weather.condition()
        );
    }
}
