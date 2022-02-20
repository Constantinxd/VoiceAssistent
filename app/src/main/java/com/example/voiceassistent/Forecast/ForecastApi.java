package com.example.voiceassistent.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ForecastApi {
    @GET("/current?access_key=9c80e0951ca8b952875cfad8857eda13")
    Call<Forecast> getCurrentWeather(@Query("query") String city);
}
