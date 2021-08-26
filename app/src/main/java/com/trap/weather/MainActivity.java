package com.trap.weather;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView city, temperature, condition, humidity, maxTemperture, minTemperature, pressure, wind, realFeel, visibilty;
    private ImageView iv;
    private FloatingActionButton b1;
    LocationManager locationManager;
    LocationListener locationListener;
    double lon, lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.tvCity);
        temperature = findViewById(R.id.textViewTemp);
        condition = findViewById(R.id.textViewCon);
        humidity = findViewById(R.id.tvhumidity);
        maxTemperture = findViewById(R.id.tvmaxtemp);
        minTemperature = findViewById(R.id.tvmintemp);
        pressure = findViewById(R.id.tvpressure);
        wind = findViewById(R.id.tvwind);
        realFeel = findViewById(R.id.tvreal);
        visibilty = findViewById(R.id.tvvisibilty);
        iv = findViewById(R.id.imageView);
        b1 = findViewById(R.id.fab);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(i);
            }
        });
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                Log.e("lat :", String.valueOf(lat));
                Log.e("lon :", String.valueOf(lon));
                getWeatherData(lat, lon);
            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }
    }

    public void getWeatherData(double lat, double lon) {
        WeatherAPI weatherAPI = RetrofitWeather.getclient().create(WeatherAPI.class);
        Call<OpenWeather> call = weatherAPI.getWeatherWithLocation(lat, lon);
        call.enqueue(new Callback<OpenWeather>() {
            @Override
            public void onResponse(Call<OpenWeather> call, Response<OpenWeather> response) {
                city.setText(response.body().getName() + " , " + response.body().getSys().getCountry());
                temperature.setText(((response.body().getMain().getTemp() - 273.15) + " ").substring(0, 4) + " °C");
                visibilty.setText(response.body().getVisibility() + " m");
                condition.setText(response.body().getWeather().get(0).getDescription());
                humidity.setText(":" + response.body().getMain().getHumidity() + " %");
                maxTemperture.setText(":" + ((response.body().getMain().getTempMax() - 273.15) + "").substring(0, 4) + " °C");
                minTemperature.setText(":" + ((response.body().getMain().getTempMin() - 273.15) + "").substring(0, 4) + " °C");
                pressure.setText(":" + response.body().getMain().getPressure() + " hPa");
                wind.setText(":" + response.body().getWind().getSpeed() + " m/s");
                realFeel.setText(((response.body().getMain().getFeelsLike() - 273.15) + " ").substring(0, 4) + " °C");
                String icode = response.body().getWeather().get(0).getIcon();
                Picasso.get().load("https://openweathermap.org/img/wn/" + icode + "@2x.png")
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(iv);
            }

            @Override
            public void onFailure(Call<OpenWeather> call, Throwable t) {
                Log.e("Error ", String.valueOf(t));
            }
        });
    }
}
