package com.larryhsiao.aura.weatap.core.openweatehr;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larryhsiao.aura.weatap.core.CachedWeather;
import com.larryhsiao.aura.weatap.core.Weather;
import com.silverhetch.clotho.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * Source to build Forecast info from open weather.
 */
public class OWForecasts implements Source<List<Weather>> {
    private final JsonObject obj;

    public OWForecasts(JsonObject obj) {
        this.obj = obj;
    }

    @Override
    public List<Weather> value() {
        List<Weather> res = new ArrayList<>();
        for (JsonElement item : obj.getAsJsonArray("list")) {
            res.add(new CachedWeather(new OWWeather(item.getAsJsonObject())));
        }
        return res;
    }
}
