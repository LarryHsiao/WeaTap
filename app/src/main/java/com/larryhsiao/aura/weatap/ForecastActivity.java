package com.larryhsiao.aura.weatap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;

/**
 * Detail view for given forecast data
 */
public class ForecastActivity extends AppCompatActivity {
    private static final String ARG_FORECAST_JSON = "ARG_FORECAST_JSON";
    private ForecastAdapter adapter;

    public static Intent newIntent(Context context, List<Weather> forecast) {
        Gson gson = ((WeaTapApplication) context.getApplicationContext()).jsonParser();
        Intent intent = new Intent(context, ForecastActivity.class);
        intent.putExtra(ARG_FORECAST_JSON, gson.toJson(forecast));
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_detail);
        setTitle(R.string.forecast);
        Gson gson = ((WeaTapApplication) getApplicationContext()).jsonParser();
        List<Weather> forecast = gson.fromJson(
                getIntent().getStringExtra(ARG_FORECAST_JSON),
                new TypeToken<ArrayList<Weather>>() {}.getType()
        );
        RecyclerView list = findViewById(R.id.forecast_list);
        list.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL, false));
        list.setAdapter(adapter = new ForecastAdapter());
        adapter.load(forecast);
    }
}
