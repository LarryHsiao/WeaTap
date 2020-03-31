package com.larryhsiao.aura.weatap.core.openweatehr;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larryhsiao.aura.weatap.core.CachedWeather;
import com.larryhsiao.aura.weatap.core.Weather;
import com.larryhsiao.aura.weatap.core.config.WeaTapConfig;
import com.silverhetch.clotho.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * Source to build Forecast info from open weather.
 */
public class OWForecasts implements Source<List<Weather>> {
    private final WeaTapConfig config;
    private final JsonObject obj;

    public OWForecasts(WeaTapConfig config, JsonObject obj) {
        this.config = config;
        this.obj = obj;
    }

    @Override
    public List<Weather> value() {
        List<Weather> res = new ArrayList<>();
        for (JsonElement item : obj.getAsJsonArray("list")) {
            res.add(new CachedWeather(new OWWeather(config, item.getAsJsonObject())));
        }
        return res;
    }
}
