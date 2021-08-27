package com.trap.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView cityWeather,temperatureWeather,conditionWeather,humidityWeather,maxTempertureWeather,minTemperatureWeather,pressureWeather,windWeather,realWeather,visibilityWeather;
    private ImageView ivWeather;
    private EditText editCity;
    private LinearLayout linearLayoutWeather;
    private Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        cityWeather=findViewById(R.id.tvCityWeather);
        temperatureWeather=findViewById(R.id.textViewTempWeather);
        conditionWeather=findViewById(R.id.textViewConWeather);
        humidityWeather=findViewById(R.id.tvhumdityWeather);
        maxTempertureWeather=findViewById(R.id.tvmaxtempWeather);
        minTemperatureWeather=findViewById(R.id.tvmintempWeather);
        pressureWeather=findViewById(R.id.tvpressureWeather);
        windWeather=findViewById(R.id.tvwindWeather);
        realWeather=findViewById(R.id.tvrealWeather);
        visibilityWeather=findViewById(R.id.tvvisibiltyWeather);
        ivWeather=findViewById(R.id.imageViewWeather);
        editCity=findViewById(R.id.editcityname);
        linearLayoutWeather=findViewById(R.id.linear_layout_main);
        b1=findViewById(R.id.search);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             String cityName=editCity.getText().toString();
             getWeatherData(cityName);
             editCity.setText("");
            }
        });

    }
    public void getWeatherData(String name){
        WeatherAPI weatherAPI=RetrofitWeather.getclient().create(WeatherAPI.class);
        Call<OpenWeather> call= weatherAPI.getWeatherWithCityName(name);
        call.enqueue(new Callback<OpenWeather>() {
            @Override
            public void onResponse(Call<OpenWeather> call, Response<OpenWeather> response) {
                if (response.isSuccessful()) {
                    cityWeather.setText(response.body().getName() + " , " + response.body().getSys().getCountry());
                    temperatureWeather.setText(((response.body().getMain().getTemp() - 273.15) + " ").substring(0, 4) + " °C");
                    conditionWeather.setText(response.body().getWeather().get(0).getDescription());
                    humidityWeather.setText(": " + response.body().getMain().getHumidity() + " %");
                    maxTempertureWeather.setText(": " + ((response.body().getMain().getTempMax() - 273.15) + "").substring(0, 4) + " °C");
                    minTemperatureWeather.setText(": " + ((response.body().getMain().getTempMin() - 273.15) + "").substring(0, 4) + " °C");
                    pressureWeather.setText(": " + response.body().getMain().getPressure() + " hPa");
                    windWeather.setText(": " + response.body().getWind().getSpeed() + " m/s");
                    realWeather.setText(": "+((response.body().getMain().getFeelsLike() - 273.15) + " ").substring(0, 4) + " °C");
                    visibilityWeather.setText(": "+response.body().getVisibility() + " m");
                    if((conditionWeather.getText().toString()).contains("haze")){
                        linearLayoutWeather.setBackgroundResource(R.drawable.haze);
                    }
                    else if((conditionWeather.getText().toString()).contains("clear")){
                        linearLayoutWeather.setBackgroundResource(R.drawable.clear);
                    }
                    else if((conditionWeather.getText().toString()).contains("clouds")){
                        linearLayoutWeather.setBackgroundResource(R.drawable.clouds);
                    }
                    else if((conditionWeather.getText().toString()).contains("mist")){
                        linearLayoutWeather.setBackgroundResource(R.drawable.mist);
                    }
                    else if((conditionWeather.getText().toString()).contains("rain")){
                        linearLayoutWeather.setBackgroundResource(R.drawable.rain);
                    }
                    String icode = response.body().getWeather().get(0).getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/" + icode + "@2x.png")
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(ivWeather);
                    Log.e("Temp", String.valueOf(response.body().getMain().getTemp()));
                }
                else{
                    Toast.makeText(WeatherActivity.this,"City Not Found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OpenWeather> call, Throwable t) {
                Log.e("Error ",String.valueOf (t));
            }
        });
    }
}