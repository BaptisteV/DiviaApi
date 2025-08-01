package com.example.divia.service;

import com.example.divia.SimpleCache;
import com.example.divia.model.openmeteo.MeteoResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherService {
    private static final int MeteoCacheDurationInSeconds = 60 * 60;
    private final SimpleCache<MeteoResponse> weatherCache;
    private final WebClient webClient;

    private final static double dijonLat = 47.33;
    private final static double dijonLong = 5.048;

    public WeatherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.open-meteo.com")
                .build();
        this.weatherCache = new SimpleCache<>(this::getWeatherFromApi, MeteoCacheDurationInSeconds);
    }

    @PostConstruct
    private void init() {
        MeteoResponse weather = getWeatherFromApi();
        this.weatherCache.Set(weather);
    }

    private MeteoResponse getWeatherFromApi() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast")
                        .queryParam("latitude", dijonLat)
                        .queryParam("longitude", dijonLong)
                        .queryParam("hourly", "temperature_2m,precipitation,rain")
                        .build())
                .accept(MediaType.APPLICATION_JSON)  // 👈 Explicitly set acceptable response type
                .retrieve()
                .bodyToMono(MeteoResponse.class)
                .block();
    }

    public MeteoResponse getWeather() {
        return weatherCache.Get();
    }
}