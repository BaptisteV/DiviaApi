package com.example.divia.controller;

import com.example.divia.model.MorningResponse;
import com.example.divia.model.divia.Line;
import com.example.divia.model.divia.Stop;
import com.example.divia.service.DiviaApiService;
import com.example.divia.service.MorningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Divia API", description = "API for Dijon public transport information")
public class DiviaController {

    private final DiviaApiService diviaApiService;
    private final MorningService morningService;

    public DiviaController(DiviaApiService diviaApiService, MorningService morningService) {
        this.diviaApiService = diviaApiService;
        this.morningService = morningService;
    }

    @GetMapping("/morning")
    @Operation(summary = "Get next passages Foch Gare => Valmy", description = "Get next passages at Foch Gare to Valmy")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved next passages Foch Gare => Valmy")
    public ResponseEntity<MorningResponse> getMorning() {
        MorningResponse morning = morningService.getMorning();
        return ResponseEntity.ok(morning);
    }

    @GetMapping("/lines")
    @Operation(summary = "Get all lines", description = "Retrieve all available transport lines")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved lines")
    public ResponseEntity<List<Line>> getAllLines() {
        List<Line> lines = diviaApiService.getLines();
        return ResponseEntity.ok(lines);
    }

    @GetMapping("/lines/{lineId}")
    @Operation(summary = "Get line by ID", description = "Retrieve a specific line by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved line")
    @ApiResponse(responseCode = "404", description = "Line not found")
    public ResponseEntity<Line> getLineById(
            @Parameter(description = "Line ID") @PathVariable String lineId) {
        Optional<Line> line = diviaApiService.getLine(lineId);
        return line.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lines/search")
    @Operation(summary = "Find line by number and direction",
            description = "Search for a line by its number and direction (A or R)")
    @ApiResponse(responseCode = "200", description = "Successfully found line")
    @ApiResponse(responseCode = "404", description = "Line not found")
    public ResponseEntity<Line> findLine(
            @Parameter(description = "Line number (e.g., T1, T2, B1)") @RequestParam String number,
            @Parameter(description = "Direction (A or R), defaults to A") @RequestParam(defaultValue = "A") String direction) {
        Optional<Line> line = diviaApiService.findLine(number, direction);
        return line.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stops/{stopId}")
    @Operation(summary = "Get stop by ID", description = "Retrieve a specific stop by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved stop")
    @ApiResponse(responseCode = "404", description = "Stop not found")
    public ResponseEntity<Stop> getStopById(
            @Parameter(description = "Stop ID") @PathVariable String stopId) {
        Optional<Stop> stop = diviaApiService.getStop(stopId);
        return stop.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stops/search")
    @Operation(summary = "Find stop by line and name",
            description = "Search for a stop by line number, stop name, and direction")
    @ApiResponse(responseCode = "200", description = "Successfully found stop")
    @ApiResponse(responseCode = "404", description = "Stop not found")
    public ResponseEntity<Stop> findStop(
            @Parameter(description = "Line number (e.g., T1, T2, B1)") @RequestParam String lineNumber,
            @Parameter(description = "Stop name") @RequestParam String stopName,
            @Parameter(description = "Direction (A or R), defaults to A") @RequestParam(defaultValue = "A") String direction) {
        Optional<Stop> stop = diviaApiService.findStop(lineNumber, stopName, direction);
        return stop.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lines/{lineId}/stops")
    @Operation(summary = "Get stops for a line", description = "Retrieve all stops for a specific line")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved stops")
    public ResponseEntity<List<Stop>> getStopsByLineId(
            @Parameter(description = "Line ID") @PathVariable String lineId) {
        List<Stop> stops = diviaApiService.getStopsByLineId(lineId);
        return ResponseEntity.ok(stops);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the API is healthy and initialized")
    @ApiResponse(responseCode = "200", description = "API is healthy")
    public ResponseEntity<String> healthCheck() {
        try {
            // This will trigger initialization if not already done
            diviaApiService.getLines();
            return ResponseEntity.ok("API is healthy and initialized");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("API is not healthy: " + e.getMessage());
        }
    }
}