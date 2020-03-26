package com.larryhsiao.aura.weatap.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.larryhsiao.aura.weatap.ConstWeather;
import com.larryhsiao.aura.weatap.Weather;

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
                .name("time").value(value.time()).endObject();
    }

    @Override
    public Weather read(JsonReader in) throws IOException {
        String time = "";
        Weather.Condition condition = CLEAR;
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "time":
                    time = in.nextString();
                    break;
                case "condition":
                    condition = Weather.Condition.valueOf(in.nextString());
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        return new ConstWeather(time, condition);
    }
}
