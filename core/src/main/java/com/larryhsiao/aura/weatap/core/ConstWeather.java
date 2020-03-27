package com.larryhsiao.aura.weatap.core;

/**
 * Constant of {@link Weather} obj.
 */
public class ConstWeather implements Weather {
    private final String time;
    private final Condition condition;
    private final float rainVolume;

    public ConstWeather(String time, Condition condition, float rainVolume) {
        this.time = time;
        this.condition = condition;
        this.rainVolume = rainVolume;
    }

    @Override
    public String time() {
        return time;
    }

    @Override
    public Condition condition() {
        return condition;
    }

    @Override
    public float rainVolume() {
        return rainVolume;
    }
}
