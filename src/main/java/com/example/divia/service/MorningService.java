package com.example.divia.service;

import com.example.divia.model.MorningResponse;
import com.example.divia.model.divia.TotemResponse;
import com.example.divia.model.openmeteo.MeteoResponse;
import org.springframework.stereotype.Service;

@Service
public class MorningService {
    private final DiviaApiService diviaApiService;
    private final WeatherService weatherService;

    public MorningService(DiviaApiService diviaApiService, WeatherService weatherService) {
        this.diviaApiService = diviaApiService;
        this.weatherService = weatherService;
    }

    public MorningResponse getMorning() {
        TotemResponse totem = diviaApiService.getTotem();
        MeteoResponse weather = weatherService.getWeather();
        return new MorningResponse(totem, weather);
    }
}
