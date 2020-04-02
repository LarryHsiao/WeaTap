package com.larryhsiao.aura.weatap;

import com.larryhsiao.aura.weatap.core.Weather;
import com.silverhetch.clotho.Source;

import static com.larryhsiao.aura.weatap.core.Weather.Condition.RAIN;
import static com.larryhsiao.aura.weatap.core.Weather.Condition.THUNDERSTORM;

/**
 * Source to build image resource to indicates the user should bring the umbrella or not.
 */
public class UmbrellaImagRes implements Source<Integer> {
    private final Weather weather;

    public UmbrellaImagRes(Weather weather) {this.weather = weather;}

    @Override
    public Integer value() {
        if (weather.condition() == RAIN || weather.condition() == THUNDERSTORM) {
            return R.drawable.ic_umbrella;
        } else {
            return R.drawable.ic_umbrella_collapsed;
        }
    }
}
