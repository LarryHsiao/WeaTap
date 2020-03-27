package com.larryhsiao.aura.weatap;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.larryhsiao.aura.weatap.openweatehr.WeatherByLatLong;
import com.silverhetch.clotho.time.HttpTimeFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;

/**
 * Detail view for given forecast data
 */
public class DetailActivity extends AppCompatActivity {
    private static final String ARG_FORECAST_JSON = "ARG_FORECAST_JSON";
    private static final String ARG_LOCATION = "ARG_LOCATION";
    private ForecastAdapter adapter;
    private Location location;

    public static Intent newIntent(Context context, List<Weather> forecast, Location location) {
        Gson gson = ((WeaTapApplication) context.getApplicationContext()).jsonParser();
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(ARG_FORECAST_JSON, gson.toJson(forecast));
        intent.putExtra(ARG_LOCATION, location);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_detail);
        location = getIntent().getParcelableExtra(ARG_LOCATION);
        Gson gson = ((WeaTapApplication) getApplicationContext()).jsonParser();
        List<Weather> forecast = gson.fromJson(
                getIntent().getStringExtra(ARG_FORECAST_JSON),
                new TypeToken<ArrayList<Weather>>() {}.getType()
        );
        forecast = forecast.subList(0, 15);
        final RecyclerView list = findViewById(R.id.detail_list);
        list.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL, false));
        list.setAdapter(adapter = new ForecastAdapter());
        adapter.load(forecast);

        new Thread(() -> {
            final int iconRes = new ConditionImageRes(
                    new WeatherByLatLong(
                            new AppHttpClient(DetailActivity.this),
                            location.getLatitude(),
                            location.getLongitude()
                    ).value()
            ).value();

            runOnUiThread(() -> ((ImageView) findViewById(R.id.detail_currentConditionImage))
                    .setImageResource(iconRes));
        }).start();
    }
}