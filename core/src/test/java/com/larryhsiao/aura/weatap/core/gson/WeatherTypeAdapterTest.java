package com.larryhsiao.aura.weatap.core.gson;

import com.google.gson.Gson;
import com.larryhsiao.aura.weatap.core.ConstWeather;
import com.larryhsiao.aura.weatap.core.Weather;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.larryhsiao.aura.weatap.core.Weather.Condition.RAIN;

/**
 * Unit-test for the class {@link WeatherTypeAdapter}
 */
public class WeatherTypeAdapterTest {
    /**
     * Check to Json.
     */
    @Test
    public void toJson() {
        Weather weather = new ConstWeather("123", RAIN, -1f);
        Assertions.assertEquals(
                "{\"condition\":\"RAIN\",\"time\":\"123\",\"rainVolume\":-1.0}",
                new Gson().newBuilder()
                        .registerTypeHierarchyAdapter(
                                Weather.class,
                                new WeatherTypeAdapter()
                        ).create().toJson(weather)
        );
    }

    /**
     * Check back from Json.
     */
    @Test
    public void fromJson() {
        Gson gson = new Gson()
                .newBuilder()
                .registerTypeHierarchyAdapter(
                        Weather.class,
                        new WeatherTypeAdapter()
                ).create();

        Assertions.assertEquals(
                RAIN,
                gson.fromJson("{\"condition\":\"RAIN\",\"time\":\"123\"}",
                        Weather.class
                ).condition()
        );
    }

    /**
     * Convert Json as array
     */
    @Test
    public void arrayToJson() {
        List<ConstWeather> weather = Collections.singletonList(new ConstWeather("123", RAIN, -1f));

        Assertions.assertEquals(
                "[{\"condition\":\"RAIN\",\"time\":\"123\",\"rainVolume\":-1.0}]",
                new Gson().newBuilder()
                        .registerTypeHierarchyAdapter(
                                Weather.class,
                                new WeatherTypeAdapter()
                        ).create().toJson(weather)
        );
    }
}