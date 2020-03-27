package com.larryhsiao.aura.weatap.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.larryhsiao.aura.weatap.ConstWeather;
import com.larryhsiao.aura.weatap.Weather;
import com.larryhsiao.aura.weatap.WeatherWrapper;

import java.io.IOException;

import static com.larryhsiao.aura.weatap.Weather.Condition.CLEAR;

/**
 * Adapter for gson to Serialize/Deserialize {@link Weather}
 */
public class WeatherTypeAdapter extends TypeAdapter<Weather> {
    @Override
    public void write(JsonWriter out, Weather value) throws IOException {
        out.beginObject()
                .name("condition").value(value.condition().toString())
                .name("time").value(value.time())
                .name("rainVolume").value(value.rainVolume())
                .endObject();
    }

    @Override
    public Weather read(JsonReader in) throws IOException {
        Weather weather = new ConstWeather("", CLEAR, -1f);
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "time":
                    final String time = in.nextString();
                    weather = new WeatherWrapper(weather) {
                        @Override
                        public String time() {
                            return time;
                        }
                    };
                    break;
                case "condition":
                    final Weather.Condition condition = Weather.Condition.valueOf(in.nextString());
                    weather = new WeatherWrapper(weather) {
                        @Override
                        public Condition condition() {
                            return condition;
                        }
                    };
                    break;
                case "rainVolume":
                    final float rainVolume = ((float) in.nextDouble());
                    weather = new WeatherWrapper(weather) {
                        @Override
                        public float rainVolume() {
                            return rainVolume;
                        }
                    };
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        return weather;
    }
}
