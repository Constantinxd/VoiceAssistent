package com.example.voiceassistent.Forecast;

import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.core.util.Consumer;

import com.example.voiceassistent.AI;
import com.example.voiceassistent.Forecast.Forecast;
import com.example.voiceassistent.Forecast.ForecastService;
import com.example.voiceassistent.Forecast.NumberToString.NumberToString;
import com.example.voiceassistent.Message.Message;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastToString {
    public static void getForecast(String city, final Consumer<String> callback){
        ForecastApi api = ForecastService.getApi();
        Call<Forecast> call = api.getCurrentWeather(city);

        call.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                Forecast result = response.body();

                if (result.current != null) {
                    NumberToString.getNumber(result.current.temperature, new Consumer<String>() {
                        @Override
                        public void accept(String number) {
                            if (!isDigit(number)) {
                                number = (result.current.temperature < 0) ?
                                        "минус " + number.substring(0, number.indexOf(" руб")) :
                                        number.substring(0, number.indexOf(" руб"));
                            }
                            String answer = "Сейчас где-то " + number +
                                    " " + getEndOfDegree(result.current.temperature) + " и " +
                                    result.current.weather_descriptions.get(0);
                            callback.accept(answer);
                        }
                    });
                } else {
                    callback.accept("Не могу узнать погоду");
                }
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                Log.w("WEATHER", t.getMessage());
            }
        });
    }

    private static boolean isDigit(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String getEndOfDegree(int num) {
        int abs = Math.abs(num % 10);

        if (abs == 1)
            return "градус";

        if (abs == 5 || abs == 6 || abs == 7 || abs == 8 || abs == 9 ||
                abs == 0 || num == 11 || num == 12 || num == 13 || num == 14)
            return "градусов";

        return "градуса";
    }
}
