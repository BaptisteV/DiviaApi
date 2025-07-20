package com.example.divia.model.divia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalDateTime;

public class HoraireResponse {
    @JsonProperty("arrivesAt")
    private LocalDateTime arrivesAt;

    @JsonProperty("minutesLeft")
    private Double minutesLeft;

    public HoraireResponse(LocalDateTime horaireTime, LocalDateTime currentTime) {
        this.arrivesAt = horaireTime;
        setMinutesLeft(currentTime);
    }

    public void setMinutesLeft(LocalDateTime currentTime) {
        double durationInMinute = Duration.between(currentTime, this.arrivesAt).toSeconds() / 60.0;
        this.minutesLeft = Math.round(durationInMinute * 100.0) / 100.0;
    }
}
