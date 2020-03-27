package com.larryhsiao.aura.weatap.core;

/**
 * Cached value of weather.
 */
public class CachedWeather implements Weather {
    private final Weather weather;
    private String time = null;
    private Condition condition = null;
    private float rainVolume = -1f;

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

    @Override
    public float rainVolume() {
        if (rainVolume == -1f) {
            rainVolume = weather.rainVolume();
        }
        return rainVolume;
    }
}
