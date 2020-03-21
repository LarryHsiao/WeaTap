package com.larryhsiao.aura.weatap;

/**
 * The Weather
 */
public interface Weather {
    public enum Condition {
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
}
