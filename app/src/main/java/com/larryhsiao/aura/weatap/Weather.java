package com.larryhsiao.aura.weatap;

/**
 * The Weather
 */
public interface Weather {
    /**
     * The time of this weather record.
     */
    String time();

    /**
     * Is it raining?
     */
    boolean raining();
}
