package com.larryhsiao.aura.weatap;

/**
 * Cached value of weather.
 */
public class CachedWeather implements Weather {
    private final Weather weather;
    private String time = null;
    private Condition condition = null;

    public CachedWeather(Weather origin) {
        this.weather = origin;
    }

    @Override
    public String time() {
        if (time == null) {
            time = weather.time();
        }
        return time;
    }

    @Override
    public Condition condition() {
        if (condition == null) {
            condition = weather.condition();
        }
        return condition;
    }
}
