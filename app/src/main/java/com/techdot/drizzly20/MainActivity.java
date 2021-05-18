package com.techdot.drizzly20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Objects;


public class MainActivity extends AppCompatActivity{

    private LocationManager locationManager;
    TextView test;
    private static final int REQUEST_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    double lat = location.getLatitude();
                                    double lon = location.getLongitude();

                                    Intent weatherIntent = new Intent(MainActivity.this, WeatherActivity.class);
                                    weatherIntent.putExtra("latitude", lat);
                                    weatherIntent.putExtra("longitude", lon);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(weatherIntent);
                                            finish();
                                        }
                                    }, 1000);
                                }
                            }
                        });
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 500);
            }
        }

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        try {
//            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            onLocationChanged(location);
//        } catch (SecurityException se) {
//            se.printStackTrace();
//        }
    }

//    @Override
//    public void onLocationChanged(@NonNull Location location) {
//        double longitude= location.getLongitude();
//        double latitude= location.getLatitude();
//        // Code to start the weather intent
//            Intent weatherIntent = new Intent(MainActivity.this, WeatherActivity.class);
//            weatherIntent.putExtra("latitude", latitude);
//            weatherIntent.putExtra("longitude", longitude);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(weatherIntent);
//                finish();
//            }
//        }, 1000);
//    }

//    @Override
//    public void onProviderEnabled(@NonNull String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(@NonNull String provider) {
//
//    }
}