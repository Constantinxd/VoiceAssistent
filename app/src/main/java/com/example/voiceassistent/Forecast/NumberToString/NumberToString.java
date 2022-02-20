package com.example.voiceassistent.Forecast.NumberToString;

import android.util.Log;

import androidx.core.util.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NumberToString {
    public static void getNumber(int number, final Consumer<String> callback){
        NumberApi api = NumberService.getApi();
        Call<Number> call = api.getCurrentNumber(number + "");

        call.enqueue(new Callback<Number>() {
            @Override
            public void onResponse(Call<Number> call, Response<Number> response) {
                Number result = response.body();
                System.out.println(result);
                if (result.StringNumber != null) {
                    System.out.println(result.StringNumber);
                    callback.accept(result.StringNumber + "");
                } else {
                    callback.accept(number + "");
                }
            }

            @Override
            public void onFailure(Call<Number> call, Throwable t) {
                Log.w("NUMBER_TO_STRING", t.getMessage());
            }
        });
    }
}
