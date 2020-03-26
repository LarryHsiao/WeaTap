package com.larryhsiao.aura.weatap.gson;

import com.google.gson.Gson;
import com.larryhsiao.aura.weatap.ConstWeather;
import com.larryhsiao.aura.weatap.Weather;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static com.larryhsiao.aura.weatap.Weather.Condition.RAIN;

/**
 * Unit-test for the class {@link WeatherTypeAdapter}
 */
public class WeatherTypeAdapterTest {
    /**
     * Check to Json.
     */
    @Test
    public void toJson() {
        Weather weather = new ConstWeather("123", RAIN);
        Assert.assertEquals(
                "{\"condition\":\"RAIN\",\"time\":\"123\"}",
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

        Assert.assertEquals(
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
        List<ConstWeather> weather = Collections.singletonList(new ConstWeather("123", RAIN));

        Assert.assertEquals(
                "[{\"condition\":\"RAIN\",\"time\":\"123\"}]",
                new Gson().newBuilder()
                        .registerTypeHierarchyAdapter(
                                Weather.class,
                                new WeatherTypeAdapter()
                        ).create().toJson(weather)
        );
    }
}