package com.larryhsiao.aura.weatap;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.google.android.gms.location.*;
import com.google.gson.JsonParser;
import com.silverhetch.aura.location.LocationAddress;
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

/**
 * Entry Activity for this app
 */
public class MainActivity extends Activity {
    private static final int RC_LOCATION_PERMISSION = 1000;
    private ImageView conditionImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conditionImage = findViewById(R.id.main_conditionImage);
        CircularProgressDrawable progress = new CircularProgressDrawable(this);
        progress.setStyle(LARGE);
        progress.start();
        conditionImage.setImageDrawable(progress);

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

    private void fetchLocation() {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
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
//        Toast.makeText(this, new LocationAddress(this, location).value().getAddressLine(0), Toast.LENGTH_LONG).show();
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

        int rainCounter = 0;
        for (int i = 0; i < forecasts.size() && i <= 5; i++) {
            if (forecasts.get(i).raining()) {
                rainCounter++;
                final int finalI = i;
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
                    showNotRain();
                }
            }
        });
    }

    private void showRain() {
        conditionImage.setImageResource(R.drawable.ic_umbrella);
        exit();
    }

    private void showNotRain() {
        conditionImage.setImageResource(R.drawable.ic_ok);
        exit();
    }

    private void showNoGps() {
        conditionImage.setImageResource(R.drawable.ic_no_gps);
        exit();
    }

    private void exit() {
        new Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },
                1500
        );
    }
}
