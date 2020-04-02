package com.larryhsiao.aura.weatap;

import com.larryhsiao.aura.weatap.core.Weather;
import com.silverhetch.clotho.Source;

import java.util.Calendar;

import static com.larryhsiao.aura.weatap.core.Weather.Condition.*;
import static java.util.Calendar.HOUR_OF_DAY;

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
        if (weather.condition() == CLOUD) {
            return isNight() ? R.drawable.ic_moon_cloud : R.drawable.ic_cloud;
        } else if (weather.condition() == RAIN) {
            return R.drawable.ic_umbrella;
        }else if(weather.condition() == THUNDERSTORM){
            return R.drawable.ic_thunderstorm;
        }
        return isNight() ? R.drawable.ic_moon : R.drawable.ic_clear;
    }

    private boolean isNight() {
        int hourOfDay = Calendar.getInstance().get(HOUR_OF_DAY);
        return hourOfDay > 18 || hourOfDay < 5;
    }
}
