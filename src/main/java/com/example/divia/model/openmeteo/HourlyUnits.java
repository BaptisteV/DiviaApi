package com.example.divia.model.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HourlyUnits {
    @JsonProperty("time")
    private String time;

    @JsonProperty("temperature_2m")
    private String temperature2m;

    @JsonProperty("precipitation")
    private String precipitation;

    @JsonProperty("rain")
    private String rain;
}

