package com.larryhsiao.aura.weatap;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.google.android.gms.location.*;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static androidx.swiperefreshlayout.widget.CircularProgressDrawable.LARGE;
import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
import static com.larryhsiao.aura.weatap.BuildConfig.OPEN_WEATHER_API_KEY;
import static com.larryhsiao.aura.weatap.Weather.Condition.*;

/**
 * Entry Activity for this app
 */
public class MainActivity extends Activity {
    private Handler handler = new Handler();
    private static final int RC_LOCATION_PERMISSION = 1000;
    private ImageView conditionImage;
    private boolean userTapped = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conditionImage = findViewById(R.id.main_conditionImage);
        CircularProgressDrawable progress = new CircularProgressDrawable(this);
        progress.setStyle(LARGE);
        progress.start();
        conditionImage.setImageDrawable(progress);
        conditionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userTapped = true;
                showDetail();
            }
        });

        if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) ==
                PERMISSION_GRANTED) {
            fetchLocation();
        } else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, RC_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_LOCATION_PERMISSION &&
                ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) ==
                        PERMISSION_GRANTED) {
            fetchLocation();
        } else {
            showNoGps();
        }
    }

    private void showDetail() {
        View confirm = findViewById(R.id.main_confirmButton);
        confirm.animate().alpha(1);
        confirm.setVisibility(View.VISIBLE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fetchLocation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!userTapped) {
                    showDetail();
                }
            }
        }, 3000);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            showNoNetwork();
            return;
        }

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(GPS_PROVIDER) && !lm.isProviderEnabled(NETWORK_PROVIDER)) {
            showNoGps();
            return;
        }

        FusedLocationProviderClient client =
                LocationServices.getFusedLocationProviderClient(this);
        LocationRequest request = LocationRequest.create();
        request.setPriority(PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        client.requestLocationUpdates(request, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    onLocationLoaded(locationResult.getLocations().get(0));
                } else {
                    showNoGps();
                }
            }
        }, Looper.getMainLooper());
    }

    private void onLocationLoaded(final Location location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(
                            new HttpUrl.Builder()
                                    .scheme("https")
                                    .host("api.openweathermap.org")
                                    .addEncodedPathSegments("/data/2.5/forecast")
                                    .addQueryParameter("lat", "" + location.getLatitude())
                                    .addQueryParameter("lon", "" + location.getLongitude())
                                    .addQueryParameter("appid", OPEN_WEATHER_API_KEY)
                                    .build()
                    ).build();
                    Response res = client.newCall(request).execute();
                    if (res.isSuccessful()) {
                        proceedForecast(res.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void proceedForecast(String response) {
        final List<Weather> forecasts = new OWForecasts(
                JsonParser.parseString(response).getAsJsonObject()
        ).value();

        if (forecasts.size() == 0) {
            showNoGps();
            return;
        }

        int rainCounter = 0;
        for (int i = 0; i < forecasts.size() && i <= 5; i++) {
            if (forecasts.get(i).condition() == RAIN) {
                rainCounter++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showRain();
                    }
                });
            }
        }

        final boolean rain = rainCounter > 0;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!rain) {
                    showNextForecastCondition(forecasts.get(0));
                }
            }
        });
    }

    private void showRain() {
        conditionImage.setImageResource(R.drawable.ic_umbrella);
        preferExit();
    }

    private void showNoNetwork() {
        conditionImage.setImageResource(R.drawable.ic_no_newtwork);
        preferExit();
    }

    private void showNextForecastCondition(Weather weather) {
        if (weather.condition() == CLEAR) {
            conditionImage.setImageResource(R.drawable.ic_clear);
        } else if (weather.condition() == CLOUD) {
            conditionImage.setImageResource(R.drawable.ic_cloud);
        } else {
            conditionImage.setImageResource(R.drawable.ic_ok);
        }
        preferExit();
    }

    private void showNoGps() {
        conditionImage.setImageResource(R.drawable.ic_no_gps);
        preferExit();
    }

    private void preferExit() {
        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (!userTapped) {
                            finish();
                        }
                    }
                },
                1500
        );
    }
}
