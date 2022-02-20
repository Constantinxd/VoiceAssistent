package com.example.voiceassistent.Forecast.NumberToString;

import com.example.voiceassistent.Forecast.Forecast;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Number implements Serializable {
    @SerializedName("str")
    @Expose
    public String StringNumber;
}
