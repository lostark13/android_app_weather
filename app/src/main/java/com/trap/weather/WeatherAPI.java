package com.trap.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("weather?&appid=67231500e3d34b61a37c0fb52b169dc7")
    Call<OpenWeather>getWeatherWithLocation(@Query("lat")double lat,@Query("lon") double lon);

    @GET("weather?&appid=67231500e3d34b61a37c0fb52b169dc7")
    Call<OpenWeather>getWeatherWithCityName(@Query("q")String name);

    @GET("uvi?&appid=67231500e3d34b61a37c0fb52b169dc7")
    Call<OpenUV>getUVWithLocation(@Query("lat")double lat,@Query("lon") double lon);
}
