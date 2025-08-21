package com.example.divia.service;

import com.example.divia.model.FochResponse;
import com.example.divia.model.openmeteo.WeatherResponse;
import org.springframework.stereotype.Service;

@Service
public class MorningService {
    private final FochService fochService;
    private final WeatherService weatherService;

    public MorningService(FochService fochService, WeatherService weatherService) {
        this.fochService = fochService;
        this.weatherService = weatherService;
    }

    public WeatherResponse getWeather() {
        return weatherService.getWeather();
    }

    public FochResponse getFoch() {
        return fochService.getFoch();
    }
}
