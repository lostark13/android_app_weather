package com.trap.weather;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitWeather {

private static Retrofit retrofit;
public static Retrofit getclient(){
    if(retrofit==null){
        retrofit=new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
               .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
    return retrofit;
}
}

