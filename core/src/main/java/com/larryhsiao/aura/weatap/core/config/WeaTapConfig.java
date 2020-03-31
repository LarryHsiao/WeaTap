package com.larryhsiao.aura.weatap.core.config;

/**
 * Config of WeaTap
 */
public interface WeaTapConfig {
    enum Sensitive {
        LOW,
        MEDIUM,
        HIGH
    }

    Sensitive sensitive();

    void newSensitive(Sensitive sensitive);

    String apiKey();
}
