package com.example.divia.model.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeteoResponse {
    @JsonProperty("latitude")
    private double latitude;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("generationtime_ms")
    private double generationTimeMs;

    @JsonProperty("utc_offset_seconds")
    private int utcOffsetSeconds;

    @JsonProperty("timezone")
    private String timezone;

    @JsonProperty("timezone_abbreviation")
    private String timezoneAbbreviation;

    @JsonProperty("elevation")
    private double elevation;

    @JsonProperty("hourly_units")
    private HourlyUnits hourlyUnits;

    @JsonProperty("hourly")
    private HourlyData hourly;
}
