package com.larryhsiao.aura.weatap;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.larryhsiao.aura.weatap.core.Weather;
import com.silverhetch.aura.view.ViewHolder;
import com.silverhetch.clotho.time.HttpTimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for represent forecast data.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final List<Weather> forecasts = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_weather,
                                parent,
                                false
                        )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Weather forecast = forecasts.get(position);

        TextView time = holder.getTextView(R.id.itemForecast_time);
        time.setText(timeIndicator(forecast));

        ImageView conditionIcon = holder.getImageView(R.id.itemForecast_conditionIcon);
        conditionIcon.setImageResource(new ConditionImageRes(forecast).value());
    }

    private String timeIndicator(Weather weather) {
        try {
            return new SimpleDateFormat("HH:mm").format(
                    new HttpTimeFormat().value().parse(weather.time())
            );
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    /**
     * Load up this list.
     */
    public void load(List<Weather> forecast) {
        this.forecasts.clear();
        this.forecasts.addAll(forecast);
    }
}
