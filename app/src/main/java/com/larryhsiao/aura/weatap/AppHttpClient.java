package com.larryhsiao.aura.weatap;

import android.content.Context;
import com.silverhetch.clotho.Source;
import okhttp3.OkHttpClient;

/**
 * Source to get the client this app using.
 */
public class AppHttpClient implements Source<OkHttpClient> {
    private final Context context;

    public AppHttpClient(Context context) {this.context = context;}

    @Override
    public OkHttpClient value() {
        return ((WeaTapApplication) context.getApplicationContext()).httpClient();
    }
}
