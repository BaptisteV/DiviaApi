package com.example.divia.controller;

import com.example.divia.model.FochResponse;
import com.example.divia.model.openmeteo.WeatherResponse;
import com.example.divia.service.MorningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Morning API", description = "API for tram departures and weather")
public class DiviaController {

    private final MorningService morningService;

    public DiviaController(MorningService morningService) {
        this.morningService = morningService;
    }

    @GetMapping("/weather")
    @Operation(summary = "Get weather", description = "Get weather")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved weather")
    public ResponseEntity<WeatherResponse> getWeather() {
        WeatherResponse weather = morningService.getWeather();
        return ResponseEntity.ok(weather);
    }

    @GetMapping("/foch")
    @Operation(summary = "Get next passages Foch Gare => Valmy", description = "Get next passages at Foch Gare to Valmy")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved next passages Foch Gare => Valmy")
    public ResponseEntity<FochResponse> getFoch() {
        FochResponse foch = morningService.getFoch();
        return ResponseEntity.ok(foch);
    }
}