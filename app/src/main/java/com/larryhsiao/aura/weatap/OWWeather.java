package com.larryhsiao.aura.weatap;

import com.google.gson.JsonObject;
import com.silverhetch.clotho.time.HttpTimeFormat;

import java.util.Date;

/**
 * Weather info from OpenWeather
 */
public class OWWeather implements Weather {
    private final JsonObject obj;

    public OWWeather(JsonObject obj) {
        this.obj = obj;
    }

    @Override
    public String time() {
        return new HttpTimeFormat().value().format(

                new Date(obj.get("dt").getAsLong() * 1000)
        );
    }

    @Override
    public boolean raining() {
        try {
            int weatherId = obj.getAsJsonArray("weather")
                    .get(0)
                    .getAsJsonObject()
                    .get("id").getAsInt();
            return weatherId >= 500 && weatherId < 600;
        } catch (Exception ignore) {
            return false;
        }
    }
}
