package com.larryhsiao.aura.weatap;

import com.larryhsiao.aura.weatap.core.Weather;
import com.silverhetch.clotho.Source;

/**
 * Source to build image resource to indicates the user should bring the umbrella or not.
 */
public class UmbrellaImagRes implements Source<Integer> {
    private final Weather weather;

    public UmbrellaImagRes(Weather weather) {this.weather = weather;}

    @Override
    public Integer value() {
        if (weather.condition() == Weather.Condition.RAIN) {
            return R.drawable.ic_umbrella;
        } else {
            return R.drawable.ic_umbrella_collapsed;
        }
    }
}
