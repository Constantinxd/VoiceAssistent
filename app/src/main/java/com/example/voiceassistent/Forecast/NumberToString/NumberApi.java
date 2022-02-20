package com.example.voiceassistent.Forecast.NumberToString;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NumberApi {
    @GET("/json/convert/num2str")
    Call<Number> getCurrentNumber(@Query("num") String num);
}
