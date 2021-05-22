package com.techdot.drizzly20;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.techdot.drizzly20.weatherProvider.Weather;
import com.techdot.drizzly20.weatherProvider.WeatherResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {

    public static String appId = "94fcc47c31cb32ea1469f997383fbda4";

    TextView mainWeather;
    TextView date;
    ImageView mainWeatherImage;
    TextView windSpeed;
    TextView humidity;
    TextView feelsLike;
    TextView maxTemp;
    TextView minTemp;

    TextView myLocation;

    TextView windSpeedUnits;
    TextView humidityUnits;
    ImageView windSpeedIcon;
    ImageView humidityIcon;
    ImageView locationIcon;

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

        setData(latitude, longitude);
    }

    private void initializeOtherWeather() {
        mainWeather = findViewById(R.id.mainTemperature);
        mainWeatherImage = findViewById(R.id.weatherImage);
        date = findViewById(R.id.todayDate);
        windSpeed = findViewById(R.id.windSpeed);
        humidity = findViewById(R.id.humidity);
        feelsLike = findViewById(R.id.feelsLikeWeather);
        maxTemp = findViewById(R.id.maxTemp);
        minTemp = findViewById(R.id.minTemp);

        windSpeedIcon = findViewById(R.id.windSpeedIcon);
        windSpeedUnits = findViewById(R.id.windSpeedUnits);
        humidityIcon = findViewById(R.id.humidityIcon);
        humidityUnits = findViewById(R.id.humidityUnits);

        myLocation = findViewById(R.id.myLocation);
        locationIcon = findViewById(R.id.locationIcon);
    }

    private void setData(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherAPI api = retrofit.create(WeatherAPI.class);

        Integer latitudeInt = (int) latitude;
        Integer longitudeInt = (int) longitude;

        Log.v("LATITUDE STRING", String.valueOf(latitudeInt));
        Log.v("LONGITUDE STRING", String.valueOf(longitudeInt));

        Call<WeatherResponse> call = api.getCurrentWeatherData(String.valueOf(latitudeInt), String.valueOf(longitudeInt), appId);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {


                if (response.code() == 200) {

                    windSpeedIcon.setVisibility(View.VISIBLE);
                    windSpeedUnits.setVisibility(View.VISIBLE);
                    humidityIcon.setVisibility(View.VISIBLE);
                    humidityUnits.setVisibility(View.VISIBLE);

                    WeatherResponse weatherResponse = response.body();

                    assert weatherResponse != null;
                    double temp = weatherResponse.main.getTemp() - 273.15;
                    int temperature = (int) temp;
                    mainWeather.setText(String.valueOf(temperature) + "°C");

                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy\nEEEE");
                    Date currentDate = new Date(System.currentTimeMillis());
                    String dateString = formatter.format(currentDate);
                    date.setText(dateString);

                    double maxTemperature = weatherResponse.main.getTempMax() - 273.15;
                    int maxTempInt = (int) maxTemperature;
                    maxTemp.setText("Maximum: " + String.valueOf(maxTempInt) + "°C");

                    double minTemperature = weatherResponse.main.getTempMin() - 273.15;
                    int minTempInt = (int) minTemperature;
                    minTemp.setText("Minimum: " + String.valueOf(minTempInt) + "°C");

                    int wind = (int) (weatherResponse.wind.getSpeed() * 2.23);
                    windSpeed.setText(String.valueOf(wind));

                    humidity.setText(String.valueOf(weatherResponse.main.getHumidity()));

                    int feelsLikeTemp = (int) (weatherResponse.main.getFeelsLike() - 273.15);
                    feelsLike.setText("Feels like: " + String.valueOf(feelsLikeTemp) + "°C");

                    Geocoder geocoder = new Geocoder(WeatherActivity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        String address = addresses.get(0).getCountryName();

                        locationIcon.setVisibility(View.VISIBLE);
                        myLocation.setText(address);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    setImage(weatherResponse);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                mainWeather.setText("Error!");
            }
        });
    }

    private void setImage(WeatherResponse weatherResponse) {
        Weather weather = weatherResponse.weather.get(0);
        if (weather.getId() == 800) {
            mainWeatherImage.setImageResource(R.drawable.art_clear);
        }
        if (weather.getId() == 801) {
            mainWeatherImage.setImageResource(R.drawable.art_light_clouds);
        }
        if (weather.getId() == 802 || weather.getId() == 803 || weather.getId() == 804) {
            mainWeatherImage.setImageResource(R.drawable.art_clouds);
        }
        if (weather.getId() == 741) {
            mainWeatherImage.setImageResource(R.drawable.art_fog);
        }
        if (weather.getId() == 500) {
            mainWeatherImage.setImageResource(R.drawable.art_light_rain);
        }
        if (weather.getId() == 501 || weather.getId() == 502 || weather.getId() == 503 || weather.getId() == 504 || weather.getId() == 511 || weather.getId() == 520 || weather.getId() == 521 || weather.getId() == 522 || weather.getId() == 531) {
            mainWeatherImage.setImageResource(R.drawable.art_rain);
        }
        if (weather.getId() == 600 || weather.getId() == 601 || weather.getId() == 602 || weather.getId() == 611 || weather.getId() == 612 || weather.getId() == 613 || weather.getId() == 615 || weather.getId() == 616 || weather.getId() == 620 || weather.getId() == 621 || weather.getId() == 622) {
            mainWeatherImage.setImageResource(R.drawable.art_snow);
        }
        if (weather.getId() == 200 || weather.getId() == 201 || weather.getId() == 202 || weather.getId() == 210 || weather.getId() == 211 || weather.getId() == 212 || weather.getId() == 221 || weather.getId() == 230 || weather.getId() == 231 || weather.getId() == 232) {
            mainWeatherImage.setImageResource(R.drawable.art_storm);
        }
    }
}