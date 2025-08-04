package com.example.divia.model.divia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class TotemResponse {
    @JsonProperty("stopId")
    private final String stopId;
    @JsonProperty("stopName")
    private final String stopName;
    @JsonProperty("lineId")
    private final String lineId;
    @JsonProperty("lineName")
    private final String lineName;
    private List<HoraireResponse> horaires;

    public List<HoraireResponse> getHoraires() {
        return horaires;
    }

    public void setHoraires(List<HoraireResponse> horaires) {
        this.horaires = horaires;
    }

    public TotemResponse(String stopId, String stopName, String lineId, String lineName) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.lineId = lineId;
        this.lineName = lineName;
    }

    public void refreshMinutesLeft() {
        LocalDateTime now = LocalDateTime.now();
        for (HoraireResponse horaire : horaires) {
            horaire.setTimeLeft(now);
        }
    }
}
