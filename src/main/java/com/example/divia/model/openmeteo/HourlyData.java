package com.example.divia.model.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HourlyData {
    @JsonProperty("time")
    private List<String> time;

    @JsonProperty("temperature_2m")
    private List<Double> temperature2m;

    @JsonProperty("precipitation")
    private List<Double> precipitation;

    @JsonProperty("rain")
    private List<Double> rain;
}
