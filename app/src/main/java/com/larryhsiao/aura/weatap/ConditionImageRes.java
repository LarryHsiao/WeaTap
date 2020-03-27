package com.larryhsiao.aura.weatap;

import com.silverhetch.clotho.Source;

/**
 * Source to build condition image resource.
 */
public class ConditionImageRes implements Source<Integer> {
    private final Weather weather;

    public ConditionImageRes(Weather weather) {
        this.weather = weather;
    }

    @Override
    public Integer value() {
        // include Clear case
        if (weather.condition() == Weather.Condition.CLOUD) {
            return R.drawable.ic_cloud;
        }
        return R.drawable.ic_clear;
    }
}
