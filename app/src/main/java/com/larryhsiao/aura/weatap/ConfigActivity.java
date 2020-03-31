package com.larryhsiao.aura.weatap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import com.larryhsiao.aura.weatap.core.config.WeaTapConfig;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

/**
 * Activity for configurations.
 */
public class ConfigActivity extends Activity {
    private WeaTapConfig config;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = ((WeaTapApplication) getApplication()).config();

        setContentView(R.layout.page_config);
        findViewById(R.id.config_closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        IndicatorSeekBar options = findViewById(R.id.config_sensitivity);
        options.setProgress(config.sensitive().ordinal());
        options.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                // Leave it empty
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar indicatorSeekBar) {
                // Leave it empty
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar indicatorSeekBar) {
                config.newSensitive(WeaTapConfig.Sensitive.values()[indicatorSeekBar.getProgress()]);
            }
        });
    }
}
