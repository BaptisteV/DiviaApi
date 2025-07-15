package com.example.divia.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalDateTime;

public class HoraireResponse {
    @JsonProperty("arrivesAt")
    private LocalDateTime arrivesAt;

    @JsonProperty("waitDurationMinutes")
    private Double waitDurationMinutes;

    private Duration waitDuration;

    public HoraireResponse(LocalDateTime currentTime, LocalDateTime horaireTime) {
        this.arrivesAt = horaireTime;
        setWaitDuration(Duration.between(currentTime, horaireTime));
    }

    public LocalDateTime getArrivesAt() {
        return arrivesAt;
    }

    public void setWaitDuration(Duration waitDuration) {
        this.waitDuration = waitDuration;
        Double durationInMinute = this.waitDuration.toSeconds() / 60.0;
        this.waitDurationMinutes = Math.round(durationInMinute * 100.0) / 100.0;
    }
}
