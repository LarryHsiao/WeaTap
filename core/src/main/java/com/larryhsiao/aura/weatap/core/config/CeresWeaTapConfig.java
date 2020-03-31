package com.larryhsiao.aura.weatap.core.config;

import com.silverhetch.clotho.storage.Ceres;

/**
 * Config implemented with Ceres
 */
public class CeresWeaTapConfig implements WeaTapConfig {
    private static final String KEY_SENSITIVE = "KEY_SENSITIVE";
    private final String owKey;
    private final Ceres ceres;

    public CeresWeaTapConfig(String owKey, Ceres ceres) {
        this.owKey = owKey;
        this.ceres = ceres;}

    @Override
    public Sensitive sensitive() {
        try {
            return Sensitive.valueOf(ceres.get(KEY_SENSITIVE));
        } catch (Exception e) {
            return Sensitive.MEDIUM;
        }
    }

    @Override
    public void newSensitive(Sensitive sensitive) {
        ceres.store(KEY_SENSITIVE, sensitive.name());
    }

    @Override
    public String apiKey() {
        return owKey;
    }
}
