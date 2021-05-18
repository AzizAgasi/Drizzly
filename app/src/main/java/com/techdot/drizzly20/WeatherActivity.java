package com.techdot.drizzly20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class WeatherActivity extends AppCompatActivity {

    TextView mainWeather;
    TextView date;
    ImageView mainWeatherImage;
    TextView windSpeed;
    TextView rainPercent;
    TextView humidity;
    TextView feelsLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Bundle data = getIntent().getExtras();

        double latitude = data.getDouble("latitude");
        double longitude = data.getDouble("longitude");

        Log.v(WeatherActivity.class.getSimpleName(), "Latitude is: " + latitude);
        Log.v(WeatherActivity.class.getSimpleName(), "Longitude is: " + longitude);

        initializeOtherWeather();

        setDummyData();
    }

    private void initializeOtherWeather() {
        mainWeather = findViewById(R.id.mainTemperature);
        mainWeatherImage = findViewById(R.id.weatherImage);
        date = findViewById(R.id.todayDate);
        windSpeed = findViewById(R.id.windSpeed);
        rainPercent = findViewById(R.id.rainPercent);
        humidity = findViewById(R.id.humidity);
        feelsLike = findViewById(R.id.feelsLikeWeather);
    }

    private void setDummyData() {
        mainWeatherImage.setImageResource(R.drawable.art_clear);
        mainWeather.setText("24℃");
        feelsLike.setText("Feels like: 19℃");
        windSpeed.setText("11");
        rainPercent.setText("2");
        humidity.setText("30");
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm z");
        Date currentDate = new Date(System.currentTimeMillis());
        String dateString = formatter.format(currentDate);
        date.setText(dateString);
    }
}