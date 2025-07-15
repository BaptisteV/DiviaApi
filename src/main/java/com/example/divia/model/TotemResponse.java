package com.example.divia.model;

import java.util.List;

public class TotemResponse {
    private String stopId;
    private String stopName;
    private String lineId;
    private String lineName;
    private List<HoraireResponse> horaires;

    public List<HoraireResponse> getHoraires() {
        return horaires;
    }

    public void setHoraires(List<HoraireResponse> horaires) {
        this.horaires = horaires;
    }

    private String rawHtml;

    public TotemResponse(String stopId, String stopName, String lineId, String lineName) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.lineId = lineId;
        this.lineName = lineName;
    }

    // Getters and setters
    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getRawHtml() {
        return rawHtml;
    }

    public void setRawHtml(String rawHtml) {
        this.rawHtml = rawHtml;
    }
}
