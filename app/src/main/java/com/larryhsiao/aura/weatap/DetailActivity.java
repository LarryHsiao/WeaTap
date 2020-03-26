package com.larryhsiao.aura.weatap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Detail view for given forecast data
 */
public class DetailActivity extends Activity {
    private static final String ARG_FORECAST_JSON = "ARG_FORECAST_JSON";

    private List<Weather> forecast;

    public static Intent newIntent(Context context, List<Weather> forecast) {
        Gson gson = ((WeaTapApplication) context.getApplicationContext()).gsonParser();
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(ARG_FORECAST_JSON, gson.toJson(forecast));
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_detail);
        LinearLayout root = findViewById(R.id.detail_container);
        Gson gson = ((WeaTapApplication) getApplicationContext()).gsonParser();
        forecast = gson.fromJson(
                getIntent().getStringExtra(ARG_FORECAST_JSON),
                new TypeToken<ArrayList<Weather>>() {
                }.getType()
        );

        for (Weather weather : forecast) {
            final TextView text = new TextView(this);
            text.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            text.setText(weather.condition().toString());
            root.addView(text);
        }
    }
}
