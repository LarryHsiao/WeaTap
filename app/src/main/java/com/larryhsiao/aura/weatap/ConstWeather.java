package com.larryhsiao.aura.weatap;

/**
 * Constant of {@link Weather} obj.
 */
public class ConstWeather implements Weather {
    private final String time;
    private final Condition condition;

    public ConstWeather(String time, Condition condition) {
        this.time = time;
        this.condition = condition;
    }

    @Override
    public String time() {
        return time;
    }

    @Override
    public Condition condition() {
        return condition;
    }
}
