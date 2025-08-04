package com.example.divia.model.divia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalDateTime;

public class HoraireResponse {
    @JsonProperty("arrivesAt")
    private LocalDateTime arrivesAt;

    @JsonProperty("minutesLeft")
    private long minutesLeft;

    @JsonProperty("secondsLeft")
    private long secondsLeft;

    public HoraireResponse(LocalDateTime horaireTime, LocalDateTime currentTime) {
        this.arrivesAt = horaireTime;
        setTimeLeft(currentTime);
    }

    public void setTimeLeft(LocalDateTime currentTime) {
        var duration = Duration.between(currentTime, this.arrivesAt);
        this.secondsLeft = duration.getSeconds() % 60;
        this.minutesLeft = duration.toMinutes();
    }
}
