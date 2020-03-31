package com.larryhsiao.aura.weatap.core.openweatehr;

import com.google.gson.JsonObject;
import com.larryhsiao.aura.weatap.core.Weather;
import com.larryhsiao.aura.weatap.core.config.WeaTapConfig;
import com.silverhetch.clotho.time.HttpTimeFormat;

import java.util.Date;

/**
 * Weather info from OpenWeather
 */
public class OWWeather implements Weather {
    private final WeaTapConfig config;
    private final JsonObject obj;

    public OWWeather(WeaTapConfig config, JsonObject obj) {
        this.config = config;
        this.obj = obj;
    }

    @Override
    public String time() {
        return new HttpTimeFormat().value().format(
                new Date(obj.get("dt").getAsLong() * 1000)
        );
    }

    @Override
    public Condition condition() {
        try {
            int weatherId = weatherObj().get("id").getAsInt();
            if (weatherId >= 200 && weatherId < 700 && isRainByVolume()) {
                return Condition.RAIN;
            } else if (weatherId == 800) {
                return Condition.CLEAR;
            } else {
                return Condition.CLOUD;
            }
        } catch (Exception ignore) {
            return Condition.CLEAR;
        }
    }

    private boolean isRainByVolume() {
        switch (config.sensitive()) {
            case LOW:
                return (obj.has("rain") == rainVolume() >= 0.5);
            case HIGH:
                return true; // Use weather obj to determine if is raining now.
            default:
                return (obj.has("rain") == rainVolume() >= 0.1);
        }
    }

    @Override
    public float rainVolume() {
        try {
            if (obj.has("rain")) {
                JsonObject rain = obj.getAsJsonObject("rain");
                if (rain.has("3h")) {
                    return rain.get("3h").getAsFloat() / 3f;
                }
            }
            return 0f;
        } catch (Exception e) {
            return 0f;
        }
    }

    private JsonObject weatherObj() {
        return obj.getAsJsonArray("weather")
                .get(0).getAsJsonObject();
    }
}
