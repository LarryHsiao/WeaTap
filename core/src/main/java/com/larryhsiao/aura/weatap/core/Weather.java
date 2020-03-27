package com.larryhsiao.aura.weatap.core;

/**
 * The Weather
 */
public interface Weather {
    enum Condition {
        CLEAR,
        CLOUD,
        RAIN,
    }

    /**
     * The time of this weather record.
     */
    String time();

    /**
     * The condition of this weather
     */
    Condition condition();

    /**
     * The volume(mm) one hour.
     */
    float rainVolume();
}
