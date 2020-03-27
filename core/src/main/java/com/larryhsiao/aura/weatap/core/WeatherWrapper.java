package com.larryhsiao.aura.weatap.core;

/**
 * Wrapper class for {@link Weather}
 */
public class WeatherWrapper implements Weather {
    private final Weather origin;

    public WeatherWrapper(Weather origin) {this.origin = origin;}

    @Override
    public String time() {
        return origin.time();
    }

    @Override
    public Condition condition() {
        return origin.condition();
    }

    @Override
    public float rainVolume() {
        return origin.rainVolume();
    }
}
