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
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.google.android.gms.location.*;
import com.larryhsiao.aura.weatap.core.Weather;
import com.larryhsiao.aura.weatap.core.openweatehr.ForecastByLatLong;
import com.silverhetch.aura.location.LocationAddress;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.swiperefreshlayout.widget.CircularProgressDrawable.LARGE;
import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
import static com.larryhsiao.aura.weatap.core.Weather.Condition.RAIN;

/**
 * Entry Activity for this app
 */
public class MainActivity extends Activity {
    private static final int RC_LOCATION_PERMISSION = 1000;
    private Handler handler = new Handler();

    private final ArrayList<Weather> forecasts = new ArrayList<>();
    private final Location currentLocation = new Location("Const");
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
        TextView locationText = findViewById(R.id.main_locationText);
        locationText.setVisibility(locationText.getText().toString().trim().isEmpty() ? GONE : VISIBLE);
        locationText.animate().alpha(1);
        View confirm = findViewById(R.id.main_confirmButton);
        confirm.animate().alpha(1);
        confirm.setVisibility(VISIBLE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View more = findViewById(R.id.main_more);
        more.animate().alpha(1);
        more.setVisibility(forecasts.isEmpty() ? GONE : VISIBLE);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(DetailActivity.newIntent(v.getContext(), forecasts, currentLocation));
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
        this.currentLocation.set(location);
        TextView locationText = findViewById(R.id.main_locationText);
        locationText.setText(new LocationString(new LocationAddress(
                this, location
        ).value()).value());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    forecasts.clear();
                    forecasts.addAll(new ForecastByLatLong(
                            new AppHttpClient(MainActivity.this),
                            BuildConfig.OPEN_WEATHER_API_KEY,
                            location.getLatitude(),
                            location.getLongitude()
                    ).value());
                    proceedForecast();
                } catch (Exception e) {
                    showNoGps();
                }
            }
        }).start();
    }

    private void proceedForecast() {
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
        conditionImage.setImageResource(new UmbrellaImagRes(weather).value());
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
